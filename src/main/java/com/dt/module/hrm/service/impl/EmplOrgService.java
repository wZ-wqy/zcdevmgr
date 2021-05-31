package com.dt.module.hrm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lank
 * @date: 2017年8月9日 上午11:11:32
 * @Description:
 */
@Service
public class EmplOrgService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(EmplOrgService.class);

    public static String ORG_PART_TYPE_COMP = "comp";
    public static String ORG_PART_TYPE_SUBCOMP = "subpart";
    public static String ORG_PART_TYPE_PART = "part";

    private String LEVEL_SPLIT = "/";

    /**
     * @Description:查询所有组织信息
     */
    public R queryEmplOrg() {
        return R.SUCCESS_OPER(db.query("select * from hrm_org_info where dr='0'").toJsonArrayWithJsonObject());
    }

    /**
     * @Description:添加组织
     */
    @Transactional
    public R addEmplOrg(TypedHashMap<String, Object> ps) {
        String cur_node_id = "";
        String org_id = ps.getString("org_id");
        String node_name = "新节点";
        String parent_id = ps.getString("parent_id");
        String type = ps.getString("type");
        log.info("node_id:" + cur_node_id);
        log.info("org_id:" + org_id);
        log.info("parent_id:" + parent_id);
        if (ToolUtil.isOneEmpty(parent_id, org_id, node_name)) {
            return R.FAILURE("无父节点或无组织节点");
        }
        // 从hrm_org_part,和hrm_org_info找到最大node_id,全局唯一
        Rcd idrs = db.uniqueRecord(
                "select case when max(node_id) is null then 50 else max(node_id)+1 end value from (select node_id node_id from hrm_org_part union all select org_id node_id from hrm_org_info) t");
        if (ToolUtil.isEmpty(idrs)) {
            return R.FAILURE("发生系统错误,请开发人员协助");
        } else {
            cur_node_id = idrs.getString("value");
        }
        Rcd verifyrs = db.uniqueRecord("select * from hrm_org_part where dr='0' and node_id=?", parent_id);
        Insert ins = new Insert("hrm_org_part");
        ins.set("org_id", org_id);
        // 判断是否是第一个节点
        if (ToolUtil.isEmpty(verifyrs) || org_id.equals(parent_id)) {
            ins.set("route", cur_node_id);
        } else {
            ins.set("route", verifyrs.getString("route") + "-" + cur_node_id);
        }
        ins.set("node_id", cur_node_id);
        ins.setIf("dr", "0");
        ins.setIf("type", type);
        ins.set("parent_id", parent_id);
        ins.setIf("node_name", node_name);

        db.execute(ins);
        updateRouteName(cur_node_id, node_name);
        JSONObject ro = new JSONObject();
        ro.put("ID", cur_node_id);
        return R.SUCCESS_OPER(ro);
    }

    /**
     * @Description:修改组织
     */
    @Transactional
    public R updateEmplOrg(TypedHashMap<String, Object> ps) {
        String node_id = ps.getString("node_id");
        String node_name = ps.getString("node_name");
        String type = ps.getString("type");
        if (ToolUtil.isEmpty(node_name)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        Update ups = new Update("hrm_org_part");
        ups.setIf("node_name", node_name);
        ups.setIf("type", type);
        ups.where().and("node_id=?", node_id);
        db.execute(ups);
        updateRouteName(node_id, node_name);
        JSONObject ro = new JSONObject();
        ro.put("ID", node_id);
        return R.SUCCESS_OPER(ro);
    }

    /**
     * @Description:更新节点路径名称
     */

    private void updateRouteName(String node_id, String node_name) {
        Rcd rs = db.uniqueRecord("select * from hrm_org_part where dr='0' and node_id=?", node_id);
        // 判断如果一致则不需要更新routename
        if (ToolUtil.isEmpty(rs)) {
            return;
        }
        if (rs.getString("node_name").equals("node_name")) {
            return;
        }
        String ids = rs.getString("route");
        JSONArray arr = ConvertUtil.toJSONArrayFromString(ids, "id", "-");
        String route_name = "";
        for (int i = 0; i < arr.size(); i++) {
            route_name = route_name + LEVEL_SPLIT
                    + db.uniqueRecord("select node_name from hrm_org_part where dr='0' and node_id=?",
                    arr.getJSONObject(i).getString("id")).getString("node_name");
        }
        route_name = route_name.replaceFirst(LEVEL_SPLIT, "");
        Update me = new Update("hrm_org_part");
        me.set("route_name", route_name);
        me.where().and("node_id=?", node_id);
        db.execute(me);
        RcdSet rds = db.query("select node_id,node_name from hrm_org_part where dr='0' and parent_id=?", node_id);
        for (int j = 0; j < rds.size(); j++) {
            // 递归调用
            updateRouteName(rds.getRcd(j).getString("node_id"), rds.getRcd(j).getString("node_name"));
        }
    }

    /**
     * @Description:删除组织
     */
    @Transactional
    public R deleteEmplOrg(String node_id) {
        if (ToolUtil.isEmpty(node_id)) {
            return R.FAILURE("无节点,请选择节点");
        }
        // 检查是否有下一级节点
        if (db.uniqueRecord("select count(1) v from hrm_org_part where dr='0' and parent_id=? ", node_id).getInteger("v") > 0) {
            return R.FAILURE("请先删除子节点");
        }
        // 检查节点是否有人员信息,如果有人,在判断是否需要删除
        if (db.uniqueRecord("select count(1) v from hrm_org_employee where dr='0' and node_id=? ", node_id).getInteger("v") > 0) {
            if (db.uniqueRecord(
                    "select count(1) v from sys_user_info a,hrm_org_employee b where a.empl_id=b.empl_id and  b.dr='0' and  a.dr='0' and b.node_id=?",
                    node_id).getInteger("v") > 0) {
                return R.FAILURE("请先删除人员信息");
            }
        }
        // 删除节点
        db.execute("delete from hrm_org_part where node_id=?", node_id);
        db.execute("delete from hrm_org_employee where node_id=?", node_id);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询某个组织信息
     */
    public R queryEmplOrgById(String node_id) {

        if (ToolUtil.isEmpty(node_id)) {
            return R.FAILURE("无父节点");
        }
        if (node_id.equals("1")) {
            return R.SUCCESS();
        }
        String sql = "select * from hrm_org_part where node_id=?";
        Rcd rs = db.uniqueRecord(sql, node_id);
        if (rs == null) {
            return R.FAILURE("该节点不存在");
        } else {
            return R.SUCCESS_OPER(rs.toJsonObject());
        }
    }

    /**
     * @Description:横行显示组织信息,类似A->B->C-D
     */
    public R queryEmplOrgLevelList() {
        return R.SUCCESS_OPER(db.query("select node_id,route_name routename,route from hrm_org_part where dr='0' order by route")
                .toJsonArrayWithJsonObject());
    }

    public R orgQueryCompany() {
        return R.SUCCESS_OPER(db.query("select * from hrm_org_part where type='" + ORG_PART_TYPE_COMP + "' and dr='0' order by node_id").toJsonArrayWithJsonObject());
    }


    public R orgQueryPartByCompany(String id) {
        return R.SUCCESS_OPER(db.query("select * from hrm_org_part where type='" + ORG_PART_TYPE_PART + "' and dr='0'  and parent_id=? order by node_id", id).toJsonArrayWithJsonObject());
    }


    /**
     * @Description:树行显示组织信息
     */
    public R queryEmplOrgNodeTree(String org_id) {
        // 公司,部门,组
        if (ToolUtil.isEmpty(org_id)) {
            return R.FAILURE("根节点不存在");
        }
        Rcd rootrs = db.uniqueRecord("select * from hrm_org_info where dr='0' and org_id=?", org_id);
        String rootname = ToolUtil.isEmpty(rootrs.getString("org_name")) ? "组织树" : rootrs.getString("org_name");
        JSONArray res = new JSONArray();
        JSONObject root = new JSONObject();
        root.put("id", org_id);
        root.put("parent", "#");
        root.put("text", rootname);
        root.put("type", "root");
        res.add(root);
        String sql = "select * from hrm_org_part where dr='0' and org_id='" + org_id + "' ";
        RcdSet rs = db.query(sql);
        JSONObject e = new JSONObject();
        for (int i = 0; i < rs.size(); i++) {
            e = new JSONObject();
            e.put("id", rs.getRcd(i).getString("node_id"));
            e.put("text", rs.getRcd(i).getString("node_name"));
            e.put("parent", rs.getRcd(i).getString("parent_id"));
            e.put("type", rs.getRcd(i).getString("type"));
            res.add(e);
        }
        return R.SUCCESS_OPER(res);
    }
}
