package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: lank
 * @date: 2017年8月6日 下午8:49:32
 * @Description: Menu树根据ID可有多棵, node_id整个表唯一递增
 */
@Service
public class MenuService extends BaseService {
    public static String TYPE_DIR = "dir";
    public static String TYPE_MENU = "menu";
    public static String TYPE_BTN = "btn";
    private String LEVEL_SPLIT = "/";

    /**
     *  验证菜单类型
     *  @param value
     */
    public static String validType(String value) {
        if (ToolUtil.isEmpty(value)) {
            return TYPE_DIR;
        }
        if (value.equals(TYPE_DIR) || value.equals(TYPE_MENU) || value.equals(TYPE_BTN)) {
            return value;
        }
        return TYPE_DIR;
    }

    /**
     * @Description:查询菜单一个节点的数据
     */
    public R queryNodeById() {
        return R.SUCCESS();
    }

    /**
     * @Description:添加一个节点
     */
    @Transactional
    public R addNode(TypedHashMap<String, Object> ps) {
        String menu_id = ps.getString("menu_id");
        String old_node_id = ps.getString("old_node_id");
        String old_route = ps.getString("old_route");
        String node_name = ps.getString("node_name");
        String mark = ps.getString("mark");
        String logo = ps.getString("logo");
        String keyvalue = ps.getString("keyvalue");

        String node_id = getNextNodeId();
        Insert ins = new Insert("sys_menus_node");
        String type = ps.getString("actiontype", "add");
        String nodeid = getNextNodeId();
        if (ToolUtil.isOneEmpty(keyvalue)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }

        if (type.equals("addmaster")) {
            // 增加第一个节点
            if (ToolUtil.isEmpty(menu_id)) {
                return R.FAILURE_REQ_PARAM_ERROR();
            }
            ins.set("node_id", nodeid);
            ins.set("parent_id", "0");
            ins.set("route", nodeid);
        } else {
            Rcd kvrs = db.uniqueRecord(
                    "select * from sys_menus_node where dr='0' and parent_id in (select node_id from sys_menus_node where route=?) and keyvalue=?",
                    old_route, keyvalue);
            if (kvrs != null) {
                return R.FAILURE("已经存在");
            }
            nodeid = node_id;
            ins.set("node_id", node_id);
            ins.set("parent_id", old_node_id);
            ins.set("route", old_route + "-" + node_id);
        }
        ins.set("menu_id", menu_id);
        ins.setIf("sort", ps.getString("sort"));
        ins.set("node_name", node_name);
        ins.setIf("keyvalue", keyvalue);
        ins.set("is_action", ps.getString("is_action"));
        ins.set("is_g_show", ps.getString("is_g_show"));
        ins.setIf("logo", logo);
        ins.setIf("mark", mark);
        ins.setIf("params", ps.getString("params"));
        ins.setIf("type", validType(ps.getString("type")));
        ins.set("dr", "0");
        db.execute(ins);
        updateRouteName(nodeid, node_name);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:删除一个节点
     */
    public R deleteNode(String node_id) {
        int v = db.uniqueRecord("select count(1) value from sys_menus_node where dr='0' and parent_id=? ", node_id)
                .getInteger("value");
        if (v > 0) {
            return R.FAILURE("请先删除子节点");
        } else {
            Update ups = new Update("sys_menus_node");
            ups.set("dr", "1");
            ups.where().and("node_id=?", node_id);
            db.execute(ups);
            return R.SUCCESS_OPER();
        }
    }

    /**
     * @Description:更新节点数据
     */
    @Transactional
    public R updateNode(TypedHashMap<String, Object> ps) {
        String menu_id = ps.getString("menu_id");
        String node_id = ps.getString("node_id");
        String node_name = ps.getString("node_name");
        String mark = ps.getString("mark");
        String keyvalue = ps.getString("keyvalue");
        String module_id = ps.getString("module_id");
        String sort = ps.getString("sort");
        String logo = ps.getString("logo");
        if (ToolUtil.isEmpty(node_name)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        Update ups = new Update("sys_menus_node");
        ups.set("node_name", node_name);
        ups.setIf("keyvalue", keyvalue);
        ups.setIf("sort", sort);
        ups.setIf("logo", logo);
        ups.setIf("mark", mark);
        ups.setIf("params", ps.getString("params"));
        ups.setIf("module_id", module_id);
        ups.setIf("is_action", ps.getString("is_action"));
        ups.setIf("is_g_show", ps.getString("is_g_show"));
        ups.setIf("type", validType(ps.getString("type")));
        ups.where().and("menu_id=?", menu_id).and("node_id=?", node_id);
        db.execute(ups);
        updateRouteName(node_id, node_name);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:更新节点路径名称
     */
    private void updateRouteName(String node_id, String node_name) {
        Rcd rs = db.uniqueRecord("select * from sys_menus_node where dr='0' and node_id=?", node_id);
        // 判断如果一致则不需要更新routename
        if (ToolUtil.isEmpty(rs)) {
            return;
        }

        String ids = rs.getString("route");
        JSONArray arr = ConvertUtil.toJSONArrayFromString(ids, "id", "-");
        String route_name = "";
        for (int i = 0; i < arr.size(); i++) {
            route_name = route_name + LEVEL_SPLIT
                    + db.uniqueRecord("select node_name from sys_menus_node where dr='0' and node_id=?",
                    arr.getJSONObject(i).getString("id")).getString("node_name");
        }
        route_name = route_name.replaceFirst(LEVEL_SPLIT, "");
        Update me = new Update("sys_menus_node");
        me.set("route_name", route_name);
        me.where().and("node_id=?", node_id);
        db.execute(me);
        RcdSet rds = db.query("select node_id,node_name from sys_menus_node where dr='0' and parent_id=?", node_id);
        for (int j = 0; j < rds.size(); j++) {
            updateRouteName(rds.getRcd(j).getString("node_id"), rds.getRcd(j).getString("node_name"));
        }
    }

    /**
     * @Description:获取节点下一个序列号，sys_menus_node表全局唯一
     */
    public String getNextNodeId() {
        return db.uniqueRecord(
                "select case when max(node_id) is null then 50 else max(node_id)+1 end value from sys_menus_node ")
                .getString("value");
    }

    /**
     * @Description:获取树的第一个节点的父节点
     */
    public String getRootParentId(String id) {
        String sql = "select min(parent_id) parent_id from sys_menus_node where dr='0' and menu_id=?";
        Rcd rs = db.uniqueRecord(sql, id);
        String parent_id = rs.getString("parent_id");
        if (ToolUtil.isEmpty(parent_id)) {
            return getNextNodeId();
        } else {
            return parent_id;
        }
    }
}
