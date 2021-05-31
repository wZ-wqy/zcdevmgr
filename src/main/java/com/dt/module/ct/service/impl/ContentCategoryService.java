package com.dt.module.ct.service.impl;

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
import org.springframework.stereotype.Service;

/**
 * @author: lank
 * @date: 2017年8月11日 上午11:30:01
 * @Description:
 */
@Service
public class ContentCategoryService extends BaseService {
    private String LEVEL_SPLIT = "/";

    /**
     * @Description: 删除节点
     * @param id
     */
    public R deleteCategory(String id) {
        if (db.uniqueRecord("select count(1) value from ct_category where dr='0' and parent_id=?", id)
                .getInteger("value") > 0) {
            return R.FAILURE("请先删除子节点");
        }
        Update me = new Update("ct_category");
        me.set("dr", "1");
        me.where().and("id=?", id);
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 根据ID显示第一层的数据
     * @param isAction
     * @param rootId
     */
    public R queryCategoryFirstFloor(String rootId, String isAction) {
        String sql = "select * from ct_category where root=? and dr='0' and node_level=1";
        if (ToolUtil.isNotEmpty(isAction)) {
            sql = sql + " and isaction='" + ToolUtil.parseYNValueDefY(isAction) + "'";
        }
        sql = sql + " order by od";
        return R.SUCCESS_OPER(db.query(sql, rootId).toJsonArrayWithJsonObject());
    }

    /**
     * @Description: 显示子节点数据
     */
    public R queryCategoryChildren(String parentId, String isAction) {
        String sql = "select * from ct_category where parent_id=? and dr='0' ";
        if (ToolUtil.isNotEmpty(isAction)) {
            sql = sql + " and isaction='" + ToolUtil.parseYNValueDefY(isAction) + "'";
        }
        sql = sql + " order by od";
        return R.SUCCESS_OPER(db.query(sql, parentId).toJsonArrayWithJsonObject());
    }

    /**
     * @Description: 后端angular显示内容
     */
    public R queryCategoryTreeList(String root_id) {
        if (ToolUtil.isEmpty(root_id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        JSONArray res = new JSONArray();
        String rootsql = "select * from ct_category_root where id=? and dr='0'";
        Rcd root_rs = db.uniqueRecord(rootsql, root_id);
        JSONObject root = new JSONObject();
        root.put("id", root_id);
        root.put("parent", "#");
        root.put("text", root_rs.getString("name"));
        root.put("type", "root");
        RcdSet rs = db.query("select t.*,t.name text,t.parent_id parent from ct_category t where root=? and dr='0'", root_id);
        JSONObject e = new JSONObject();
        res = ConvertUtil.OtherJSONObjectToFastJSONArray(rs.toJsonArrayWithJsonObject());
        res.add(root);
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询某个节点
     */
    public R queryCategoryById(String id) {
        String sql = "select a.*,b.name rootname from ct_category a,ct_category_root b where a.root=b.id and a.id=?";
        Rcd rs = db.uniqueRecord(sql, id);
        if (ToolUtil.isEmpty(rs)) {
            return R.SUCCESS_NO_DATA();
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }

    /**
     * @Description:查询所有数据
     */
    public R queryCategory(String root) {
        return R.SUCCESS_OPER(
                db.query("select * from ct_category where dr='0' and root=?", root).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:获取节点下一个序列号
     */
    public String getNextNodeId() {
        return db.uniqueRecord("select case when max(id) is null then 50 else max(id)+1 end value from ct_category")
                .getString("value");
    }

    /**
     * @Description:更新节点数据
     */
    public R updateCategory(TypedHashMap<String, Object> ps) {
        String id = ps.getString("id");
        String name = ps.getString("name", "idle");
        Update ups = new Update("ct_category");
        ups.setIf("name", name);
        ups.setIf("categorylevel", ps.getString("categorylevel"));
        ups.setIf("mpic", ps.getString("mpic"));
        ups.setIf("mark", ps.getString("mark"));
        ups.setIf("od", ps.getString("od"));
        ups.setIf("code", ps.getString("code"));
        ups.setIf("model", ps.getString("model"));
        ups.setIf("unit", ps.getString("unit"));
        ups.setIf("type", ps.getString("type"));
        ups.setIf("upcnt", ps.getString("upcnt"));
        ups.setIf("downcnt", ps.getString("downcnt"));
        ups.setIf("unitprice", ps.getString("unitprice"));
        ups.setIf("supplier", ps.getString("supplier"));
        ups.setIf("brandmark", ps.getString("brandmark"));
        ups.setIf("acl", ps.getString("acl"));
        ups.setIf("isaction", ps.getString("isaction"));
        ups.where().and("id=?", id);
        db.execute(ups);

        updateRouteName(id, name);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:更新节点名称
     * @param id
     * @param name
     */
    private void updateRouteName(String id, String name) {
        Rcd rs = db.uniqueRecord("select * from ct_category where dr='0' and id=?", id);
        // 判断如果一致则不需要更新routename
        if (ToolUtil.isEmpty(rs)) {
            return;
        }

        String ids = rs.getString("route");
        JSONArray arr = ConvertUtil.toJSONArrayFromString(ids, "id", "-");
        String route_name = "";
        for (int i = 0; i < arr.size(); i++) {
            route_name = route_name + LEVEL_SPLIT
                    + db.uniqueRecord("select name from ct_category where dr='0' and id=?",
                    arr.getJSONObject(i).getString("id")).getString("name");
        }
        route_name = route_name.replaceFirst(LEVEL_SPLIT, "");
        Update me = new Update("ct_category");
        me.set("route_name", route_name);
        me.where().and("id=?", id);
        db.execute(me);
        RcdSet rds = db.query("select id,name from ct_category where dr='0' and parent_id=?", id);
        for (int j = 0; j < rds.size(); j++) {
            // 递归调用
            updateRouteName(rds.getRcd(j).getString("id"), rds.getRcd(j).getString("name"));
        }
    }

    /**
     * @Description:插入节点
     */
    public R addCategory(TypedHashMap<String, Object> ps) {
        String old_id = ps.getString("old_id");
        String old_node_type = ps.getString("old_node_type");
        String name = ps.getString("name");
        if (ToolUtil.isOneEmpty(old_id, old_node_type, name)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        String id = this.getNextNodeId();
        Insert me = new Insert("ct_category");
        if ("root".equals(old_node_type)) {
            // 树的根节点添加第一个节点
            me.set("root", old_id);
            me.set("route", id);
            me.set("parent_id", old_id);
            me.set("node_level", "1");
        } else {
            // 树的添加节点
            R oldNode = queryCategoryById(old_id);
            if (!oldNode.isSuccess()) {
                return oldNode;
            }
            JSONObject oldData = JSONObject.parseObject(oldNode.getData().toString());
            me.set("root", oldData.getString("root"));
            me.set("route", oldData.getString("route") + "-" + id);
            me.set("parent_id", old_id);
            me.set("node_level", oldData.getIntValue("node_level") + 1);
        }
        me.set("id", id);
        me.set("dr", "0");
        me.setIf("categorylevel", ps.getString("categorylevel"));
        me.setIf("code", ps.getString("code"));
        me.setIf("mark", ps.getString("mark"));
        me.setIf("mpic", ps.getString("mpic"));
        me.setIf("name", ps.getString("name", "idle"));
        me.setIf("isaction", ps.getString("isaction"));
        me.setIf("od", ConvertUtil.toInt(ps.getString("od"), 99));
        db.execute(me);
        updateRouteName(id, ps.getString("name", "idle"));
        return queryCategoryById(id);
    }
}
