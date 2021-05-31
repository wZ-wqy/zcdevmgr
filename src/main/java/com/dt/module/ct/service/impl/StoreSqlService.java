package com.dt.module.ct.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author: lank
 * @date: Nov 1, 2017 8:49:51 AM
 * @Description:
 */
@Service
public class StoreSqlService extends BaseService {

    private static Logger log = LoggerFactory.getLogger(StoreSqlService.class);

    public static String ACL_PUBLIC = "public";
    public static String ACL_USER = "user";
    public static String ACL_SYSTEM = "system";
    public static String RETURN_ACTION = "action";
    public static String RETURN_OBJECT = "object";
    public static String RETURN_ARRARY = "array";
    public static String VAR_SPLIT = "@";

    /**
     * @Description: 根据条件返回数据，无分页功能
     */
    @SuppressWarnings("rawtypes")
    public R commandAction(TypedHashMap<String, Object> ps, String user_id, String acl) {
        String sql = "";
        String store_id = ps.getString("store_id");
        String return_type = "";
        String is_used = "N";
        if (ToolUtil.isNotEmpty(store_id)) {
            String storesql = "select * from ct_uri where store_id=?";
            if (ToolUtil.isNotEmpty(acl) && acl.equals(ACL_PUBLIC)) {
                storesql = storesql + " and acl='" + ACL_PUBLIC + "'";
            }
            Rcd brs = db.uniqueRecord(storesql, store_id);
            if (ToolUtil.isNotEmpty(brs)) {
                sql = brs.getString("sqltext");
                return_type = brs.getString("return_type");
                is_used = brs.getString("is_used");
            }
        }
        // 判断是否可以使用
        if (!is_used.equals("Y")) {
            return R.FAILURE("功能未激活");
        }
        // 处理自定义变量,格式:@var@
        Iterator<Entry<String, Object>> i = ps.entrySet().iterator();
        while (i.hasNext()) {
            Entry entry = i.next();
            String key = entry.getKey().toString();
            String value = (String) entry.getValue();
            log.info("key:" + key + ",value:" + value);
            if (key.startsWith(VAR_SPLIT)) {
                log.info("key to replace:" + key + VAR_SPLIT + ",value:" + value);
                sql = sql.replaceAll(key + VAR_SPLIT, value);
            }
        }
        // 处理系统定义的变量,格式:@<var>@
        log.info("execute sql:" + sql);
        if (ToolUtil.isNotEmpty(sql)) {
            if (return_type.equals(RETURN_ARRARY)) {
                RcdSet rs = db.query(sql);
                return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
            } else if (return_type.equals(RETURN_OBJECT)) {
                Rcd rs = db.uniqueRecord(sql);
                return R.SUCCESS_OPER(rs.toJsonObject());
            } else {
                db.execute(sql);
                return R.SUCCESS_OPER();
            }
        } else {
            return R.FAILURE("Sql语句有误");
        }
    }

    /**
     * @Description:查询sql
     */
    public R queryStoreSql(String cat_id) {
        String sql = "select * from ct_uri where dr='0' ";
        if (ToolUtil.isNotEmpty(cat_id)) {
            sql = sql + " and cat_id='" + cat_id + "'";
        }
        RcdSet rs = db.query(sql);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }

    /**
     * @Description:根据Id查询sql
     */
    public R queryStoreSqlById(String store_id) {
        Rcd rs = db.uniqueRecord("select * from ct_uri where store_id=?", store_id);
        return R.SUCCESS_OPER(rs.toJsonObject());
    }

    /**
     * @Description:检查sql是否符合格式要求
     */
    private R checkStoreSqlFormat(TypedHashMap<String, Object> ps) {

        // 弱弱的检查下
        String msg = "Sql文本于返回类型不匹配";
        String sql = ps.getString("sqltext", "").trim();
        String return_type = ps.getString("return_type", RETURN_ACTION);
        if (sql.toLowerCase().startsWith("select")) {
            if (return_type.equals(RETURN_ARRARY) || return_type.equals(RETURN_OBJECT)) {
                return R.SUCCESS_OPER();
            } else {
                return R.FAILURE(msg);
            }
        } else {
            if (return_type.equals(RETURN_ACTION)) {
                return R.SUCCESS_OPER();
            } else {
                return R.FAILURE(msg);
            }
        }

    }

    /**
     * @Description:添加sql
     */
    public R addStoreSql(TypedHashMap<String, Object> ps, String user_id) {
        R rs = checkStoreSqlFormat(ps);
        if (rs.isFailed()) {
            return rs;
        }
        Insert me = new Insert("ct_uri");
        me.set("store_id", db.getUUID());
        me.setIf("name", ps.getString("name"));
        me.setIf("cat_id", ps.getString("cat_id"));
        me.setIf("uri", ps.getString("uri"));
        me.setIf("uri_parameter", ps.getString("uri_parameter"));
        me.setIf("user_id", user_id);
        me.setIf("sqltext", ps.getString("sqltext"));
        me.setIf("db_id", ps.getString("db_id"));
        me.set("dr", "0");
        me.set("acl", ps.getString("acl", ACL_USER));
        me.setIf("mark", ps.getString("mark"));
        me.set("return_type", ps.getString("return_type", RETURN_ACTION));
        me.setIf("is_used", ps.getString("is_used"));
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:更新sql
     */
    public R updateStoreSql(TypedHashMap<String, Object> ps, String user_id) {
        R rs = checkStoreSqlFormat(ps);
        if (rs.isFailed()) {
            return rs;
        }
        Update me = new Update("ct_uri");
        me.setIf("name", ps.getString("name"));
        me.setIf("uri", ps.getString("uri"));
        me.setIf("uri_parameter", ps.getString("uri_parameter"));
        me.setIf("user_id", user_id);
        me.setIf("sqltext", ps.getString("sqltext"));
        me.setIf("db_id", ps.getString("db_id"));
        me.set("acl", ps.getString("acl", ACL_USER));
        me.setIf("mark", ps.getString("mark"));
        me.set("return_type", ps.getString("return_type", RETURN_ACTION));
        me.setIf("is_used", ps.getString("is_used"));
        me.where().and("store_id=?", ps.getString("store_id"));
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:删除sql
     */
    public R deleteStoreSql(String store_id) {
        Update me = new Update("ct_uri");
        me.set("dr", "1");
        me.where().and("store_id=?", store_id);
        db.execute(me);
        return R.SUCCESS_OPER();
    }
}
