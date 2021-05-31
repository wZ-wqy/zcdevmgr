package com.dt.module.hrm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.*;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.UserTypeEnum;
import com.dt.module.base.entity.SysUserApproval;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserApprovalService;
import com.dt.module.base.service.impl.SysUserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lank
 * @date: 2017年8月9日 上午11:11:19
 * @Description:
 */
@Service
public class EmplService extends BaseService {

    @Autowired
    SysUserInfoServiceImpl sysUserInfoServiceImpl;

    @Autowired
    ISysUserApprovalService SysUserApprovalServiceImpl;

    /**
     * @Description: 添加员工
     */
    @Transactional
    public R addEmployee(TypedHashMap<String, Object> ps) {
        ArrayList<SQL> exeSqls = new ArrayList<SQL>();
        // 先判断组织
        String nodes = ps.getString("nodes");
        if (ToolUtil.isEmpty(nodes)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        String user_id = ps.getString("user_id");
        String approval=ps.getString("approval","[]");
        JSONArray nodes_arr = (JSONArray) JSONArray.parse(nodes);
        String emplpartCtl = ifEmplCanMultiPart();
        if (emplpartCtl.equals("Y")) {
            if (nodes_arr.size() > 1) {
                return R.FAILURE("必须属于一个组织,不可多选");
            }
        }

        SysUserInfo user = new SysUserInfo();
        user.setLocked("N");
        user.setIslogoff("0");
        user.setApproval(approval);
        user.setHrmstatus(ps.getString("hrmstatus", ""));
        user.setFposition(ps.getString("fposition", ""));
        user.setSposition(ps.getString("sposition", ""));
        user.setMark(ps.getString("mark", ""));
        user.setMail(ps.getString("mail", ""));
        user.setTel(ps.getString("tel", ""));
        user.setUserType(UserTypeEnum.EMPL.getValue().toString());
        user.setName(ps.getString("name", ""));
        R user_rs = sysUserInfoServiceImpl.addUser(user);
        if (user_rs.isFailed()) {
            return user_rs;
        }
        SysUserInfo s = ((SysUserInfo) user_rs.getData());
        String empl_id = s.getEmplId();
        for (int i = 0; i < nodes_arr.size(); i++) {
            String node_id = nodes_arr.getJSONObject(i).getString("node_id");
            if (db.uniqueRecord("select * from hrm_org_part where dr='0' and node_id=?", node_id) == null) {
                return R.FAILURE("组织不存在");
            }
            Insert ins3 = new Insert("hrm_org_employee");
            ins3.set("id", ToolUtil.getUUID());
            ins3.set("node_id", node_id);
            ins3.set("user_id", s.getUserId());
            ins3.set("dr", "0");
            ins3.set("empl_id", empl_id);
            exeSqls.add(ins3);
        }
        if (user_rs.isSuccess()) {
            db.executeSQLList(exeSqls);
            updateUserApproval(empl_id,JSONArray.parseArray(approval));
        } else {
            return user_rs;
        }

        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 根据empl_id删除员工
     */
    public R delEmployee(String empl_id) {
        Update ups = new Update("sys_user_info");
        ups.set("dr", "1");
        ups.where().and("empl_id=?", empl_id);
        db.execute(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 根据empl_id注销
     * @param empl_id
     */
    public R logoffEmployee(String empl_id) {
        Update ups = new Update("sys_user_info");
        ups.set("islogoff", "1");
        ups.where().and("empl_id=?", empl_id);
        db.execute(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 更新审批角色
     * @param approval
     * @param empl_id
     */
    public R updateUserApproval(String empl_id, JSONArray approval){

        String node=this.getUserNodeByEmpld(empl_id);
        QueryWrapper<SysUserInfo> userq=new QueryWrapper<>();
        userq.eq("empl_id", empl_id);
        String userid=sysUserInfoServiceImpl.getOne(userq).getUserId();

        List<SysUserApproval> list=new ArrayList<SysUserApproval>();
        for(int i=0;i<approval.size();i++){
            SysUserApproval obj=new SysUserApproval();
            obj.setNodeid(node);
            obj.setUserid(userid);
            obj.setApprovalid(approval.getJSONObject(i).getString("id"));
            obj.setApprovalcode(approval.getJSONObject(i).getString("code"));
            list.add(obj);
        }

        QueryWrapper<SysUserApproval> q=new QueryWrapper<SysUserApproval>();
        q.eq("userid",userid);
        SysUserApprovalServiceImpl.remove(q);
        if(list.size()>0){
            SysUserApprovalServiceImpl.saveOrUpdateBatch(list);
        }
        return R.SUCCESS_OPER();
    }
    /**
     * @Description: 根据empl_id更新员工
     */
    public R updateEmployee(TypedHashMap<String, Object> ps) {

        String nodes = ps.getString("nodes");
        String empl_id = ps.getString("empl_id");
        String user_id = ps.getString("user_id");
        String approval=ps.getString("approval","[]");

        ArrayList<SQL> exeSqls = new ArrayList<SQL>();

        if (ToolUtil.isOneEmpty(user_id, empl_id, nodes)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }

        Update u = new Update("sys_user_info");
        u.setIf("approval", approval);
        u.setIf("mark", ps.getString("mark"));
        u.setIf("fposition", ps.getString("fposition"));
        u.setIf("hrmstatus", ps.getString("hrmstatus"));
        u.setIf("mail", ps.getString("mail"));
        u.setIf("tel", ps.getString("tel", ""));
        u.set("name", ps.getString("name", ""));
        u.where().and("user_id=?", user_id);

        /************组织内用户插入的判断***********************/
        JSONArray nodes_arr = (JSONArray) JSONArray.parse(nodes);
        String emplpartCtl = ifEmplCanMultiPart();
        if (emplpartCtl.equals("Y")) {
            if (nodes_arr.size() > 1) {
                return R.FAILURE("必须属于一个组织,不可多选");
            }
        }

        Delete dls = new Delete();
        dls.from("hrm_org_employee");
        dls.where().and("empl_id=?", empl_id);
        exeSqls.add(dls);
        for (int i = 0; i < nodes_arr.size(); i++) {
            String node_id = nodes_arr.getJSONObject(i).getString("node_id");
            Insert ins3 = new Insert("hrm_org_employee");
            ins3.set("id", ToolUtil.getUUID());
            ins3.set("node_id", node_id);
            ins3.set("dr", "0");
            ins3.set("empl_id", empl_id);
            exeSqls.add(ins3);
        }
        /*********************************** 执行 **************************************/
        db.execute(u);
        db.executeSQLList(exeSqls);
        updateUserApproval(empl_id,JSONArray.parseArray(approval));
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 根据组织ID查找员工
     */
    public R queryEmplByOrg(String node_id) {
        if (ToolUtil.isEmpty(node_id)) {
            return R.FAILURE("无节点");
        }
        String sql = "select (select route_name from hrm_org_part hop where node_id =a.node_id) orgfullname,c.* from hrm_org_employee a,sys_user_info c where c.islogoff='0' and a.empl_id=c.empl_id and c.user_type= ? and a.node_id in ( select node_id from hrm_org_part where dr='0' and concat('-',route)  like '%-"+node_id+"-%' or node_id="+node_id+") and c.dr='0'";
        RcdSet rs = db.query(sql, UserTypeEnum.EMPL.getValue().toString());
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }

    /**
     * @Description: 查询员工
     */
    public R queryEmplList(TypedHashMap<String, Object> ps) {
        String node_id = ps.getString("node_id");
        String name = ps.getString("name");
        String bsql = "";
        if (node_id != null && (!node_id.equals("-1"))) {
            // 主节点
            if (node_id.equals("1")) {
                return R.SUCCESS();
            }
            Rcd routev = db.uniqueRecord("select route from hrm_org_part where node_id=?", node_id);
            if (routev == null) {
                return R.FAILURE("该节点不存在");
            }
            // String route = routev.getString("route").replaceAll("-", ",");
            bsql = "select (select name from hrm_position where id=b.fposition) fposname,b.*,c.node_name,c.route_name from hrm_org_employee a,sys_user_info b,hrm_org_part c where b.islogoff='0' and b.dr='0' and a.empl_id = b.empl_id and c.node_id=a.node_id ";
            // 不级联获取人员数据
            bsql = bsql + " and a.node_id in ( select node_id from hrm_org_part where dr='0' and concat('-',route)  like '%-"+node_id+"-%' or node_id="+node_id+")";
        } else {
            bsql = "select (select name from hrm_position where id=b.fposition) fposname,b.*,c.node_name,c.route_name from hrm_org_employee a,sys_user_info b,hrm_org_part c where b.islogoff='0' and b.dr='0' and a.empl_id = b.empl_id and c.node_id=a.node_id ";
        }
        if (name != null && (!name.trim().equals(""))) {
            bsql = bsql + " and b.name like '%" + name + "%'";
        }
        bsql = bsql + " order by create_time desc";
        return R.SUCCESS_OPER(db.query(bsql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description: 根据empl_id查找员工
     */
    public R queryEmplById(String empl_id) {
        JSONObject res = new JSONObject();
        Rcd info = db.uniqueRecord("select * from sys_user_info where dr='0' and empl_id=?", empl_id);
        if (ToolUtil.isEmpty(info)) {
            return R.FAILURE_NO_DATA();
        }
        // 获取组织信息
        res = ConvertUtil.OtherJSONObjectToFastJSONObject(info.toJsonObject());
        res.put("PARTS",
                ConvertUtil.OtherJSONObjectToFastJSONArray(db
                        .query("select a.*,b.node_name from hrm_org_employee a,hrm_org_part b where a.node_id=b.node_id and empl_id=?",
                                empl_id)
                        .toJsonArrayWithJsonObject()));
        return R.SUCCESS("获取成功", res);
    }

    /**
     * @Description: 判断用户是否可以存在多个组织中, 默认返回N
     * sys_empl_org_num_ctl:N(可以多个组织),Y(只能属于一个组织)
     */
    public String ifEmplCanMultiPart() {

        return ToolUtil.parseYNValueDefN("Y");
    }
}
