package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Delete;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.SQL;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: lank
 * @date: Nov 4, 2017 8:08:34 PM
 * @Description:
 */
@Service
public class ModuleItemMapService extends BaseService {

    /**
     *  更新系统模块
     *  @param module_id
     *  @param items
     */
    public R updateModuleItem(String module_id, String items) {
        List<SQL> sqls = new ArrayList<SQL>();
        Delete d = new Delete();
        d.from("sys_modules_item");
        d.where().and("module_id=?", module_id);
        sqls.add(d);
        JSONArray items_arr = JSONArray.parseArray(items);
        for (int i = 0; i < items_arr.size(); i++) {
            Insert me = new Insert("sys_modules_item");
            me.set("module_item_id", db.getUUID());
            me.set("module_id", module_id);
            me.setIf("ct", items_arr.getJSONObject(i).getString("url"));
            me.set("status", "Y");
            me.set("type", "url");
            sqls.add(me);
        }
        db.executeSQLList(sqls);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询所有api,其中已经在模块中则选中
     */
    public R queryModuleItem(String module_id) {
        if (ToolUtil.isEmpty(module_id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        String sql = "select * " +
                "from (select " +
                "'Y'           selected, " +
                "a.module_item_id, " +
                "a.status, " +
                "a.type, " +
                "a.ct          url, " +
                "b.mark, " +
                "b.ctacl, " +
                "case when b.id is null " +
                "then '0' " +
                "else '1' end  urlstatuscode, " +
                "case when b.id is null " +
                "then '不存在' " +
                "else '存在' end urlstatus, " +
                "case when b.ctacl is null " +
                "then '未知' " +
                "when b.ctacl = '" + Acl.ACL_ALLOW + "' " +
                "then '公共访问' " +
                "when b.ctacl = '" + Acl.ACL_USER + "' " +
                "then '用户认证' " +
                "when b.ctacl = '" + Acl.ACL_DENY + "' " +
                "then '权限认证' " +
                "else '未知' end ctacltext " +
                "from sys_modules_item a left join sys_api b on a.ct = b.ct " +
                "where module_id = ? " +
                "union all select " +
                "'N'           selected, " +
                "''            module_item_id, " +
                "''            status, " +
                "'url'         type, " +
                "b.ct          url, " +
                "b.mark, " +
                "b.ctacl, " +
                "'0'           urlstatuscode, " +
                "'存在'          urlstatus, " +
                "case when b.ctacl is null " +
                "then '未知' " +
                "when b.ctacl = '" + Acl.ACL_ALLOW + "' " +
                "then '公共访问' " +
                "when b.ctacl = '" + Acl.ACL_USER + "' " +
                "then '用户认证' " +
                "when b.ctacl = '" + Acl.ACL_DENY + "' " +
                "then '权限认证' " +
                "else '未知' end ctacltext " +
                "from sys_api b " +
                "where ctacl not in ('" + Acl.ACL_ALLOW + "') and ct not in (select ct " +
                "from sys_modules_item " +
                "where module_id = ?) " +
                "order by status, ctacl desc, url) endtab order by selected desc,url";
        return R.SUCCESS_OPER(db.query(sql, module_id, module_id).toJsonArrayWithJsonObject());
    }
}
