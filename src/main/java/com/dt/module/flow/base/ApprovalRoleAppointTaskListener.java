package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.process.assign.Assignee;
import com.bstek.uflo.process.listener.TaskListener;
import com.bstek.uflo.process.node.TaskNode;
import com.dt.module.flow.service.impl.FlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class    ApprovalRoleAppointTaskListener implements TaskListener {

    private static Logger _log = LoggerFactory.getLogger(ApprovalRoleAppointTaskListener.class);

    @Autowired
    FlowService flowService;

    @Override
    public boolean beforeTaskCreate(Context context, ProcessInstance processInstance, TaskNode taskNode) {

        List<Assignee> newassignee=new ArrayList<Assignee>();
        if(taskNode.getAssignees().size()>0){
            for(int i=0;i< taskNode.getAssignees().size();i++){
                System.out.println(taskNode.getAssignees().get(i).getProviderId());
                newassignee.add(flowService.queryAssignee(processInstance.getBusinessId(),taskNode.getAssignees().get(i),processInstance.getPromoter(), null));
                System.out.println("assignees:" + taskNode.getAssignees().get(i).getId()+" "+taskNode.getAssignees().get(i).getName()+" "+taskNode.getAssignees().get(i).getProviderId());
            }
        }
        taskNode.setAssignees(newassignee);
        System.out.println("---------------------------------\n\n");
        return false;
    }

    @Override
    public void onTaskCreate(Context context, Task task) {

        System.out.println("###BaseTaskListener onTaskCreate###");
        System.out.println("task.getBusinessId()" + task.getBusinessId());
        System.out.println("task.getAssignee()" + task.getAssignee());
        System.out.println("task.getOpinion()" + task.getOpinion());
        System.out.println("task.getOwner()" + task.getOwner());
        System.out.println("task.getPriority()" + task.getPriority());
        System.out.println("task.getSubject()" + task.getSubject());
        System.out.println("task.getTaskName()" + task.getTaskName());
        System.out.println("task.getUrl()" + task.getUrl());
        System.out.println("task.getDescription()" + task.getDescription());
        System.out.println("task.getNodeName()" + task.getNodeName());
        System.out.println("task.getProcessInstanceId()" + task.getProcessInstanceId());
        System.out.println("task.getType()" + task.getType());
        System.out.println("task.getId()" + task.getId());
        System.out.println("task.getProcessId()" + task.getProcessId());
        System.out.println("task.getPrevTask()" + task.getPrevTask());
        System.out.println("task.getPrevState()" + task.getPrevState());
        System.out.println("---------------------------------\n\n");
    }

    @Override
    public void onTaskComplete(Context context, Task task) {
        System.out.println("###BaseTaskListener onTaskComplete###");
        System.out.println("task.getBusinessId()" + task.getBusinessId());
        System.out.println("task.getAssignee()" + task.getAssignee());
        System.out.println("task.getOpinion()" + task.getOpinion());
        System.out.println("task.getOwner()" + task.getOwner());
        System.out.println("task.getPriority()" + task.getPriority());
        System.out.println("task.getSubject()" + task.getSubject());
        System.out.println("task.getTaskName()" + task.getTaskName());
        System.out.println("task.getUrl()" + task.getUrl());
        System.out.println("task.getDescription()" + task.getDescription());
        System.out.println("task.getNodeName()" + task.getNodeName());
        System.out.println("task.getProcessInstanceId()" + task.getProcessInstanceId());
        System.out.println("task.getType()" + task.getType());
        System.out.println("task.getId()" + task.getId());
        System.out.println("task.getProcessId()" + task.getProcessId());
        System.out.println("task.getPrevTask()" + task.getPrevTask());
        System.out.println("task.getPrevState()" + task.getPrevState());

        System.out.println("---------------------------------\n\n");
    }
}
