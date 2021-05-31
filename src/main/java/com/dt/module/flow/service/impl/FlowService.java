package com.dt.module.flow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bstek.uflo.process.assign.Assignee;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysApprovalMeta;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysApprovalMetaService;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.flow.service.IUfloBlobService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FlowService extends BaseService {


    private static Logger log = LoggerFactory.getLogger(FlowService.class);

    @Autowired
    IUfloBlobService UfloBlobServiceImpl;

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    @Autowired
    ISysApprovalMetaService SysApprovalMetaServiceImpl;

    @Autowired
    SysUfloProcessService sysUfloProcessService;

    //指定-组织角色:按照组织的角色动态获取到审批人,需要提前获取
    public static String NODE_APPROVAL_ROLE ="assigneeNodeApprovalRoleProvider";

    //指定-发起人部门角色:按照流程发起人所在的部门角色,需要提前获取
    public static String LOCALDEPARTMENT_APPROVAL_ROLE="assigneeLocalDepartmentApprovalRoleProvider";

    //指定-动态部门角色:按照指定部门的角色--动态指定人,需要提前获取
    public static String APPOINT_DEPARTMENT_APPROVAL_ROLE ="assigneeAppointDepartmentApprovalRoleProvider";

    //指定-固定角色:固定角色--静态指定人,需要提前获取
    public static String FIX_APPROVAL_ROLE="assigneeFixApprovalRoleProvider";

    //指定-人员:静态指定人
    public static String SYS_USER_INFO="assigneeSysUerInfoProvider";


    /**
     * @Description:根据流程查询,预先查询审批人
     * @param busid 流程业务ID
     * @param userid 流程发起人
     * @param tplkey 流程模版
     */
    @SuppressWarnings("unchecked")
    public R queryTaskAssignment(String busid,String userid,String tplkey,String nodeid) {

        Rcd rs=db.uniqueRecord("select a.ID_ id from uflo_blob a,uflo_process b where a.PROCESS_ID_ =b.ID_ and b.key_=?",tplkey);
        if(rs==null){
            return R.FAILURE("未找到流程配置文件");
        }
        String xml= UfloBlobServiceImpl.getById(rs.getString("id")).getBlobValue();
        log.info("当前的流程配置XML文件\n"+xml+"\n开始解析");
        List<SysApprovalMeta> list=new ArrayList<>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> tasks=root.selectNodes("task");
            for(int i=0;i<tasks.size();i++){
                List<Element> assignee=tasks.get(i).selectNodes("assignee");
                for(int j=0;j<assignee.size();j++){
                    String providerid=assignee.get(j).attributeValue("provider-id");
                    String name=assignee.get(j).attributeValue("name");
                    String id=assignee.get(j).attributeValue("id");
                    log.info("process info:"+providerid+","+name+ ","+id);
                    SysApprovalMeta meta=new SysApprovalMeta();
                    meta.setBusid(busid);
                    meta.setTaskassigneeprovider(providerid);
                    meta.setTaskassigneename(name);
                    meta.setTaskassigneeid(id);
                    QueryWrapper<SysUserInfo> q=new QueryWrapper<SysUserInfo>();
                    if(NODE_APPROVAL_ROLE.equals(providerid)){
                        log.info("NODE_APPROVAL_ROLE[指定-组织角色]");
                        String nodeid2=id.split("-")[0];
                        String approvalid2=id.split("-")[1];
                        q.inSql("user_id",  "select a.userid from sys_user_approval a,sys_user_info b where b.dr='0' and a.userid=b.user_id and a.approvalid='"+approvalid2+"' and a.dr='0' and a.nodeid='"+nodeid2+"' ");
                        List<SysUserInfo> assigneeuser=SysUserInfoServiceImpl.list(q);
                        if(assigneeuser.size()!=1){
                            return R.FAILURE("未获取到审批人或该角色存在多个审批人,类型[指定-指定-组织角色]:"+providerid+",审批信息:"+name);
                        }
                        meta.setUserid(assigneeuser.get(0).getUserId());
                        meta.setUsername(assigneeuser.get(0).getName());
                        list.add(meta);
                    }else if(LOCALDEPARTMENT_APPROVAL_ROLE.equals(providerid)){
                        log.info("LOCALDEPARTMENT_APPROVAL_ROLE[指定-发起人部门角色]");
                        System.out.println("select a.userid from sys_user_approval a,sys_user_info b where b.dr='0' and a.userid=b.user_id and  a.dr='0' and a.approvalid='"+id+"' and a.nodeid in (select node_id from hrm_org_employee where user_id ='"+userid+"')");
                        q.inSql("user_id","select a.userid from sys_user_approval a,sys_user_info b where b.dr='0' and a.userid=b.user_id and  a.dr='0' and a.approvalid='"+id+"' and a.nodeid in (select node_id from hrm_org_employee where user_id ='"+userid+"')");
                        List<SysUserInfo> assigneeuser=SysUserInfoServiceImpl.list(q);
                        if(assigneeuser.size()!=1){
                            return R.FAILURE("未获取到审批人或该角色存在多个审批人,类型[指定-指定-发起人部门角色]:"+providerid+",审批信息:"+name);
                        }
                        meta.setUserid(assigneeuser.get(0).getUserId());
                        meta.setUsername(assigneeuser.get(0).getName());
                        list.add(meta);
                    }else if(APPOINT_DEPARTMENT_APPROVAL_ROLE.equals(providerid)){
                        log.info("APPOINT_DEPARTMENT_APPROVAL_ROLE[指定-动态部门角色]");
                        QueryWrapper<SysApprovalMeta> qa=new QueryWrapper<>();
                        qa.eq("busid",busid);
                        qa.eq("taskassigneeid",id);
                        qa.eq("taskassigneeprovider", providerid);
                        qa.isNotNull("nodeid");
                        qa.isNotNull("userid");
                        List<SysApprovalMeta> metalist =SysApprovalMetaServiceImpl.list(qa);
                        for(int k=0;k<metalist.size();k++){
                            QueryWrapper<SysUserInfo> q2=new QueryWrapper<SysUserInfo>();
                            q2.inSql("user_id","select a.userid from sys_user_approval a,sys_user_info b where a.userid=b.user_id and b.dr='0' and a.approvalid='"+id+"' and a.dr='0' and a.nodeid='"+metalist.get(k).getNodeid()+"'");
                            List<SysUserInfo> assigneeuser=SysUserInfoServiceImpl.list(q);
                            if(assigneeuser.size()!=1){
                                return R.FAILURE("未获取到审批人或该角色存在多个审批人,类型[指定-动态部门角色]:"+providerid+",审批信息:"+name);
                            }
                            metalist.get(k).setUsername(assigneeuser.get(0).getName());
                            metalist.get(k).setUserid(assigneeuser.get(0).getUserId());
                            list.add(metalist.get(k));
                        }
                    }else if(FIX_APPROVAL_ROLE.equals(providerid)){
                        log.info("FIX_APPROVAL_ROLE[指定-固定角色]");
                        q.inSql("user_id","select a.userid from sys_user_approval a,sys_user_info b where a.userid=b.user_id and b.dr='0' and a.dr='0' and a.approvalid='"+id+"'");
                        List<SysUserInfo> assigneeuser=SysUserInfoServiceImpl.list(q);
                        if(assigneeuser.size()!=1){
                            return R.FAILURE("未获取到审批人或该角色存在多个审批人,类型[指定-固定角色]:"+providerid+",审批信息:"+name);
                        }
                        meta.setUserid(assigneeuser.get(0).getUserId());
                        meta.setUsername(assigneeuser.get(0).getName());
                        list.add(meta);
                    }else if(SYS_USER_INFO.equals(providerid)){
                        log.info("指定-人员");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Busid:"+busid+",设置审批人数:"+list.size());
        if(list.size()>0){
            SysApprovalMetaServiceImpl.saveOrUpdateBatch(list);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:根据UUID查询流程数据
     */
    public R queryFlowDataByBusId(String uuid) {
        JSONObject res = new JSONObject();
        Rcd rs = db.uniqueRecord("select * from sys_process_data where dr='0' and busid=?", uuid);
        if (ToolUtil.isNotEmpty(rs)) {
            res = ConvertUtil.OtherJSONObjectToFastJSONObject(rs.toJsonObject());
            R r = sysUfloProcessService.loadProcessTaskinfo(rs.getString("processinstanceid"));
            res.put("taskinfodata", r.queryDataToJSONArray());
        }
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:根据UUID查询流程任务数据
     */
    public R queryFlowTaskInfoByBusid(String uuid) {
        JSONObject res = new JSONObject();
        Rcd rs = db.uniqueRecord("select * from sys_process_data where dr='0' and busid=?", uuid);
        if (ToolUtil.isNotEmpty(rs)) {
            res = ConvertUtil.OtherJSONObjectToFastJSONObject(rs.toJsonObject());
            R r = sysUfloProcessService.loadProcessTaskinfo(rs.getString("processinstanceid"));
            res.put("taskinfodata", r.queryDataToJSONArray());
        }
        return R.SUCCESS_OPER(res);
    }

    public Assignee queryAssignee(String busid,Assignee roleassignee,String promoter,String nodeid){
        System.out.println("Start to queryAssigneeByRole");
        System.out.println("assignee_id:"+roleassignee.getId());
        System.out.println("assignee_name:"+roleassignee.getName());
        System.out.println("assignee_providerId:"+roleassignee.getProviderId());
        System.out.println("promoter:"+promoter);
        System.out.println("nodeid:"+nodeid);
        String providerid=roleassignee.getProviderId();
        String assigneeid=roleassignee.getId();
        Assignee assignee=new Assignee();
        assignee.setProviderId(SYS_USER_INFO);
        if(FIX_APPROVAL_ROLE.equals(providerid) || NODE_APPROVAL_ROLE.equals(providerid) ||LOCALDEPARTMENT_APPROVAL_ROLE.equals(providerid)){
            QueryWrapper<SysApprovalMeta> qa=new QueryWrapper<>();
            qa.eq("busid",busid);
            qa.eq("taskassigneeid",assigneeid);
            qa.eq("taskassigneeprovider", providerid);
            SysApprovalMeta sysApprovalMeta =SysApprovalMetaServiceImpl.getOne(qa);
            if(sysApprovalMeta==null){
                assignee=roleassignee;
            }else{
                assignee.setId(sysApprovalMeta.getUserid());
                assignee.setName(sysApprovalMeta.getUsername());
            }
        }else if(APPOINT_DEPARTMENT_APPROVAL_ROLE.equals(providerid)){
            QueryWrapper<SysApprovalMeta> qa=new QueryWrapper<>();
            qa.eq("busid",busid);
            qa.eq("taskassigneeid",assigneeid);
            qa.eq("taskassigneeprovider", providerid);
         //   qa.eq("nodeid",nodeid);
            SysApprovalMeta sysApprovalMeta =SysApprovalMetaServiceImpl.getOne(qa);
            if(sysApprovalMeta==null){
                assignee=roleassignee;
            }else{
                assignee.setId(sysApprovalMeta.getUserid());
                assignee.setName(sysApprovalMeta.getUsername());
            }
        }else{
            assignee=roleassignee;
        }
        return assignee;
    }
}
