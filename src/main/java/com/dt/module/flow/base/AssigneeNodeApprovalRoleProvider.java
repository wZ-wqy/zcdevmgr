
package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.assign.AssigneeProvider;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.dt.core.dao.RcdSet;
import com.dt.module.db.DB;
import com.dt.module.flow.service.impl.FlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AssigneeNodeApprovalRoleProvider implements AssigneeProvider {

    private static Logger log = LoggerFactory.getLogger(AssigneeNodeApprovalRoleProvider.class);

    @Autowired
    FlowService flowService;

    @Override
    public boolean isTree() {
        return false;
    }

    public static String info="指定-组织角色";

    @Override
    public String getName() {
        return info;
    }

    @Override
    public void queryEntities(PageQuery<Entity> pageQuery, String s) {
        List<Entity> list = new ArrayList<Entity>();
        String sql = "select t.* ,(select route_name from hrm_org_part i where i.node_id=t.nodeid )route_name,(select node from sys_approval_node i where i.id=t.approvalid )node from (select a.nodeid,a.approvalid ,count(1) v from sys_user_approval a,sys_user_info b where a.dr='0' and b.dr='0' and a.userid=b.user_id group by a.nodeid,a.approvalid)t";
        RcdSet rs = DB.instance().query(sql);
        pageQuery.setPageSize(50);
        for (int i = 0; i < rs.size(); i++) {
            list.add(new Entity(rs.getRcd(i).getString("nodeid") + "-" + rs.getRcd(i).getString("approvalid"), rs.getRcd(i).getString("route_name") + "-" + rs.getRcd(i).getString("node")));
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

