package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.assign.AssigneeProvider;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.dt.core.dao.RcdSet;
import com.dt.module.base.entity.SysUserApproval;
import com.dt.module.base.service.ISysUserApprovalService;
import com.dt.module.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AssigneeLocalDepartmentApprovalRoleProvider implements AssigneeProvider {

    private static Logger log = LoggerFactory.getLogger(AssigneeLocalDepartmentApprovalRoleProvider.class);

    @Autowired
    ISysUserApprovalService SysUserApprovalServiceImpl;

    @Override
    public boolean isTree() {
        return false;
    }

    public static String info="指定-发起人部门角色";

    @Override
    public String getName() {
        return info;
    }


    @Override
    public void queryEntities(PageQuery<Entity> pageQuery, String s) {
        List<Entity> list = new ArrayList<Entity>();
        String sql="select id,node from sys_approval_node where dr='0'";
        RcdSet rs= DB.instance().query(sql);
        pageQuery.setPageSize(50);
        for (int i = 0; i < rs.size(); i++) {
            list.add(new Entity(rs.getRcd(i).getString("id"), rs.getRcd(i).getString("node")));
        }
        pageQuery.setResult(list);
        pageQuery.setRecordCount(rs.size());
    }

    @Override
    public Collection<String> getUsers(String entityId, Context context, ProcessInstance processInstance) {
        log.info("info:"+info);
        List<String> list = new ArrayList<String>();
        list.add(entityId);
        return list;
    }

    @Override
    public boolean disable() {
        return false;
    }
}
