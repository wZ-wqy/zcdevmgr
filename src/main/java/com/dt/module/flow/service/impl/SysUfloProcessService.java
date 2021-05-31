package com.dt.module.flow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.uflo.command.impl.jump.JumpNode;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.process.flow.SequenceFlowImpl;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.TaskService;
import com.bstek.uflo.service.*;
import com.bstek.uflo.utils.EnvironmentUtils;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.entity.TaskInfo;
import com.dt.module.flow.service.ISysProcessDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lank
 * @date: Dec 1, 2019 10:02:28 AM
 * @Description:
 */

@Service
public class SysUfloProcessService extends BaseService {


    //流程运行中
    public static String P_STATUS_SFA = "submitforapproval";
    public static String P_STATUS_RUNNING = "running";
    public static String P_STATUS_FINISH = "finish";
    public static String P_STATUS_ROLLBACK = "rollback";
    public static String P_STATUS_CANCEL = "cancel";


    //流程运行中
    public static String P_DTL_STATUS_SFA = "submitforapproval";
    public static String P_DTL_STATUS_INREVIEW = "inreview";
    public static String P_DTL_STATUS_SUCCESS = "success";
    public static String P_DTL_STATUS_FAILED = "failed";

    @Autowired
    ISysProcessDataService SysProcessDataServiceImpl;

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    @Autowired
    private ProcessService processService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    public R loadProcessTaskinfo(String processInstanceId) {
        TaskQuery query = taskService.createTaskQuery();
        long processInstanceIdl = ConvertUtil.toLong(processInstanceId);
        query.rootProcessInstanceId(processInstanceIdl);
        // query.nodeName(node.getName());
        query.addTaskState(TaskState.Created);
        query.addTaskState(TaskState.InProgress);
        query.addTaskState(TaskState.Ready);
        query.addTaskState(TaskState.Suspended);
        query.addTaskState(TaskState.Reserved);


        HistoryTaskQuery historyTaskQuery = historyService.createHistoryTaskQuery();
        historyTaskQuery.rootProcessInstanceId(processInstanceIdl);
        List<HistoryTask> historyTasks = historyTaskQuery.list();
        List<TaskInfo> histaskinfo = buildHistoryTaskInfos(historyTasks);
        JSONArray data = new JSONArray();
        if (histaskinfo.size() > 0) {
            data = JSONArray.parseArray(JSON.toJSONString(histaskinfo, SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.DisableCircularReferenceDetect));
        }

        List<Task> tasks = query.list();
        List<TaskInfo> taskinfo = buildTaskInfos(tasks);
        System.out.println("taskinfo" + JSONArray.parseArray(JSON.toJSONString(taskinfo, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect)).toJSONString());
        System.out.println("histaskinfo" + JSONArray.parseArray(JSON.toJSONString(histaskinfo, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect)).toJSONString());

        return R.SUCCESS_OPER(data);
    }

    private List<TaskInfo> buildTaskInfos(List<Task> tasks) {
        List<TaskInfo> infos = new ArrayList<TaskInfo>();
        for (Task task : tasks) {
            TaskInfo info = new TaskInfo();
            info.setAssignee(task.getAssignee());
            info.setBusinessId(task.getBusinessId());
            info.setCreateDate(task.getCreateDate());
            info.setDescription(task.getDescription());
            info.setDuedate(task.getDuedate());
            info.setOpinion(task.getOpinion());
            info.setOwner(task.getOwner());
            info.setProcessId(task.getProcessId());
            info.setProcessInstanceId(task.getProcessInstanceId());
            info.setState(task.getState());
            info.setTaskId(task.getId());
            info.setTaskName(task.getTaskName());
            info.setType(task.getType());
            info.setUrl(task.getUrl());
            String assignee = info.getAssignee();
            if (assignee != null) {
                SysUserInfo u = SysUserInfoServiceImpl.getById(assignee);
                if (u != null) {
                    info.setAssigneename(u.getName());
                }
            }
            infos.add(info);
        }
        return infos;
    }

    public R cancelTask(String taskId, String opinion) {
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        taskService.cancelTask(taskId_l, op);
        return R.SUCCESS_OPER();
    }

    public R startProcess(String key, String type) {
        if (ToolUtil.isOneEmpty(key, type)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }

        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String busid = ToolUtil.getUUID();
        StartProcessInfo startProcessInfo = new StartProcessInfo(EnvironmentUtils.getEnvironment().getLoginUser());
        startProcessInfo.setBusinessId(busid);
        startProcessInfo.setCompleteStartTask(true);


        ProcessInstance inst = processService.startProcessByKey(key, startProcessInfo);

        SysProcessData pd = new SysProcessData();
        pd.setBusid(busid);
        pd.setProcesskey(key);
        pd.setPtype(type);
        pd.setPstatus(SysUfloProcessService.P_STATUS_RUNNING);
        pd.setProcessinstanceid(inst.getId() + "");
        pd.setPstartuserid(this.getUserId());
        SysProcessDataServiceImpl.save(pd);
        return R.SUCCESS_OPER();
    }


    public Object queryVariableInProcessInstance(Long inst, String key) {
        return processService.getProcessVariable(key, inst);
    }

    public void addVariablesInProcessInstance(Long inst, String key, Object obj) {
        List<Variable> vars = processService.getProcessVariables(inst);
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < vars.size(); i++) {
            map.put(vars.get(i).getKey(), vars.get(i).getValue());
        }
        map.put(key, obj);
        processService.saveProcessVariables(inst, map);
    }


    public Map<String, Object> buildVariables(String variables) {
        if (StringUtils.isBlank(variables)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = mapper.readValue(variables, ArrayList.class);
            Map<String, Object> map = new HashMap<String, Object>();
            for (Map<String, Object> m : list) {
                String key = m.get("key").toString();
                Object value = m.get("value");
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<TaskInfo> buildHistoryTaskInfos(List<HistoryTask> tasks) {
        List<TaskInfo> infos = new ArrayList<TaskInfo>();
        for (HistoryTask task : tasks) {
            TaskInfo info = new TaskInfo();
            info.setAssignee(task.getAssignee());
            info.setBusinessId(task.getBusinessId());
            info.setCreateDate(task.getCreateDate());
            info.setDescription(task.getDescription());
            info.setDuedate(task.getDuedate());
            info.setEndDate(task.getEndDate());
            info.setOpinion(task.getOpinion());
            info.setOwner(task.getOwner());
            info.setProcessId(task.getProcessId());
            info.setProcessInstanceId(task.getProcessInstanceId());
            info.setState(task.getState());
            info.setTaskName(task.getTaskName());
            info.setTaskId(task.getId());
            info.setType(task.getType());
            info.setUrl(task.getUrl());
            String assignee = task.getAssignee();
            if (assignee != null) {
                SysUserInfo u = SysUserInfoServiceImpl.getById(assignee);
                if (u != null) {
                    info.setAssigneename(u.getName());
                }
            }
            infos.add(info);
        }
        return infos;
    }

    public R queryTask(String taskId) {
        long taskId_l = ConvertUtil.toLong(taskId);
        String a = taskService.getUserData(taskService.getTask(taskId_l), "catid");
        String b = taskService.getUserData(taskService.getTask(taskId_l), "catname");
        System.out.println(a + b);
        return R.SUCCESS_OPER();
    }

    // 同意任务
    public R completeTask(String taskId, String opinion) {
        // 修改流程标记
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        //    Task tsk = taskService.getTask(taskId_l);
        taskService.start(taskId_l);
        taskService.complete(taskId_l, op);
        return R.SUCCESS_OPER();
    }

    public R completeTask(String taskId, HashMap<String, Object> map, String opinion) {
        // 修改流程标记
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        //   Task tsk = taskService.getTask(taskId_l);
        taskService.start(taskId_l);
        taskService.complete(taskId_l, map, op);
        return R.SUCCESS_OPER();
    }

    public R getAvaliableForwardTaskNodes(String taskId) {
        long taskId_l = ConvertUtil.toLong(taskId);
        List<JumpNode> nodes = taskService.getAvaliableForwardTaskNodes(taskId_l);
        return R.SUCCESS_OPER(nodes);
    }

    public R getAvaliableRollbackTaskNodes(String taskId) {
        long taskId_l = ConvertUtil.toLong(taskId);
        List<JumpNode> nodes = taskService.getAvaliableRollbackTaskNodes(taskId_l);
        return R.SUCCESS_OPER(nodes);
    }

    public R rollback(String taskId, String targetNodeName, String opinion) {
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        taskService.rollback(taskId_l, targetNodeName, null, op);
        return R.SUCCESS_OPER();
    }

    public R withdraw(String taskId, String opinion) {
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        taskService.withdraw(taskId_l, null, op);
        return R.SUCCESS_OPER();
    }

    public R forward(String taskId, String targetNodeName, String opinion) {
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        taskService.forward(taskId_l, targetNodeName, null, op);
        return R.SUCCESS_OPER();
    }

    // 流程退回，退回到流程开始位置
    public R forwardStart(String taskId, String opinion) {
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        Task tsk = taskService.getTask(taskId_l);

        ProcessDefinition process = processService.getProcessById(tsk.getProcessId());
        taskService.forward(taskId_l, process.getStartNode().getName(), null, op);
        // 修改流程标记
        String instid = tsk.getProcessInstanceId() + "";
        // 更新状态
        UpdateWrapper<SysProcessData> uw = new UpdateWrapper<SysProcessData>();
        uw.eq("processInstanceId", instid);
        uw.set("pstatus", SysUfloProcessService.P_STATUS_ROLLBACK);
        uw.set("pstatusdtl", SysUfloProcessService.P_STATUS_ROLLBACK);
        SysProcessDataServiceImpl.update(uw);
        return R.SUCCESS_OPER();
    }

    public R queryTaskNodeDtl(String taskId) {
        long taskId_l = ConvertUtil.toLong(taskId);
        Task tsk = taskService.getTask(taskId_l);
        ProcessDefinition process = processService.getProcessById(tsk.getProcessId());

        System.out.println(process.getStartNode().getName());
        System.out.println(process.getStartNode().getType());
        System.out.println(process.getStartNode().getTaskName());
        Node node = process.getNode(tsk.getNodeName());

        List<SequenceFlowImpl> flows = node.getSequenceFlows();
        if (flows.size() > 0) {
            SequenceFlowImpl flowimpl = flows.get(0);
            System.out.print(flowimpl);
        }

        System.out.print(node.getClass());
        node.getSequenceFlows();
        return R.SUCCESS_OPER();
    }

    // 拒绝，结束整个流程
    public R refuseTask(String taskId, String opinion) {
        TaskOpinion op = new TaskOpinion(opinion);
        long taskId_l = ConvertUtil.toLong(taskId);
        Task tsk = taskService.getTask(taskId_l);
        String instid = tsk.getProcessInstanceId() + "";

        List<JumpNode> nodes = taskService.getAvaliableForwardTaskNodes(taskId_l);
        if (nodes.size() == 0) {
            return R.FAILURE("无法跳转至结束流程");
        }
        JumpNode jn = nodes.get(nodes.size() - 1);
        if (jn.isTask()) {
            return R.FAILURE("获取的最后一个节点不是结束流程");
        }
        taskService.forward(taskId_l, jn.getName(), op);
        return R.SUCCESS_OPER();
    }

}
