package com.dt.core.common.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.shiro.ShiroUser;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.db.DB;
import com.dt.module.hrm.entity.HrmOrgEmployee;
import com.dt.module.hrm.service.IHrmOrgEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: lank
 * @date: 2017年8月26日 上午8:08:57
 */
public class BaseSC {

    @Autowired
    IHrmOrgEmployeeService HrmOrgEmployeeServiceImpl;

    @Autowired
    public DB db = null;

    public String getUserId() {
        String user_id = (String) HttpKit.getRequest().getSession().getAttribute("user_id");
        if (ToolUtil.isEmpty(user_id)) {
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser != null) {
                HttpKit.getRequest().getSession().setAttribute("user_id", shiroUser.getId());
                return shiroUser.getId();
            }
        }
        return user_id;
    }

    public String getName() {
        String name = (String) HttpKit.getRequest().getSession().getAttribute("name");
        if (ToolUtil.isEmpty(name)) {
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser != null) {
                HttpKit.getRequest().getSession().setAttribute("name", shiroUser.getName());
                return shiroUser.getName();
            }
        }
        return name;
    }

    public String getUserName() {
        String name = (String) HttpKit.getRequest().getSession().getAttribute("username");
        if (ToolUtil.isEmpty(name)) {
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser != null) {
                HttpKit.getRequest().getSession().setAttribute("username", shiroUser.getUsername());
                return shiroUser.getName();
            }
        }
        return name;
    }

    public String getUserNode(String userid) {
        String result="";
        if(userid!=null) {
            QueryWrapper<HrmOrgEmployee> q=new QueryWrapper<HrmOrgEmployee>();
            q.inSql("empl_id","select empl_id from sys_user_info where user_id='"+userid+"'");
            HrmOrgEmployee e=HrmOrgEmployeeServiceImpl.getOne(q);
            if(e!=null){
                result=e.getNodeId();
            }
        }
        return result;
    }

    public String getUserNodeByEmpld(String emplid) {
        String result="";
        if(emplid!=null) {
            QueryWrapper<HrmOrgEmployee> q=new QueryWrapper<HrmOrgEmployee>();
            q.inSql("empl_id",emplid);
            HrmOrgEmployee e=HrmOrgEmployeeServiceImpl.getOne(q);
            if(e!=null){
                result=e.getNodeId();
            }
        }
        return result;
    }

    public String getMyNode() {
        String result="";
        String userid=this.getUserId();
        if(userid!=null) {
            QueryWrapper<HrmOrgEmployee> q=new QueryWrapper<HrmOrgEmployee>();
            q.inSql("empl_id","select empl_id from sys_user_info where user_id='"+userid+"'");
            HrmOrgEmployee e=HrmOrgEmployeeServiceImpl.getOne(q);
            if(e!=null){
                result=e.getNodeId();
            }
        }
        return result;
    }
}
