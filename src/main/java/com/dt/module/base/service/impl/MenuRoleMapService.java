package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.SQL;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lank
 * @date: 2017年8月6日 下午10:13:08
 * @Description: 树节点模块, 角色映射
 */
@Service
public class MenuRoleMapService extends BaseService {

    /**
     * @Description: 查询一个角色拥有的节点
     */
    @Transactional
    public R treeNodeRoleMap(String role_id, String modulesarr, String menu_id) {
        if (ToolUtil.isOneEmpty(role_id, modulesarr)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        db.execute(
                "delete from sys_role_module where role_id=? and module_id in (select node_id from sys_menus_node where menu_id=?)",
                role_id, menu_id);
        JSONArray ms = JSONArray.parseArray(modulesarr);
        List<SQL> sqls = new ArrayList<SQL>();
        for (int i = 0; i < ms.size(); i++) {
            Insert me = new Insert("sys_role_module");
            me.setIf("role_id", role_id);
            me.setIf("module_id", ms.getString(i));
            sqls.add(me);
        }
        db.executeSQLList(sqls);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 一个角色拥有的节点对比树
     */
    public R treeRoleChecked(String menu_id, String role_id) {
        if (ToolUtil.isOneEmpty(menu_id, role_id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        JSONArray resarr = new JSONArray();
        String sql = "select    t.*,     (select count(1)      from sys_menus_node   "
                + "   where parent_id = t.node_id and dr='0')     children_cnt,     (select count(1)   "
                + "   from sys_role_module       where role_id = ? and module_id = t.node_id) checked,   "
                + "  ( select count(distinct a.module_id)       from sys_role_module a,sys_menus_node b   "
                + "   where role_id = ?   "
                + "  and  a.module_id=b.node_id and b.parent_id=t.node_id and dr='0') role_children_cnt   "
                + "from sys_menus_node t    where menu_id = ? and dr = '0'    order by type";

        RcdSet res = db.query(sql, role_id, role_id, menu_id);
        JSONObject e;
        JSONObject stat;
        for (int i = 0; i < res.size(); i++) {
            e = new JSONObject();
            int children_cnt = res.getRcd(i).getInteger("children_cnt");
            int role_children_cnt = res.getRcd(i).getInteger("role_children_cnt");
            String type = res.getRcd(i).getString("type");
            e.put("id", res.getRcd(i).getString("node_id"));
            e.put("type", res.getRcd(i).getString("type"));
            e.put("parent",
                    res.getRcd(i).getString("parent_id").equals("0") ? "#" : res.getRcd(i).getString("parent_id"));
            e.put("text", res.getRcd(i).getString("node_name") == null ? "" : res.getRcd(i).getString("node_name"));
            e.put("children_cnt", children_cnt);
            stat = new JSONObject();
            int checked = res.getRcd(i).getInteger("checked");
            if (type.equals(MenuService.TYPE_MENU) || type.equals(MenuService.TYPE_BTN)) {
                if (checked > 0) {
                    if (children_cnt == role_children_cnt) {
                        stat.put("selected", true);
                    } else {
                        stat.put("selected", false);
                    }
                } else {
                    stat.put("selected", false);
                }
            }
            e.put("state", stat);
            resarr.add(e);
        }
        return R.SUCCESS_OPER(resarr);
    }
}
