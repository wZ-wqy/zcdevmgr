package com.dt.module.cmdb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.annotion.Res;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.SQL;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: lank
 * @date: Dec 31, 2018 7:32:04 PM
 * @Description:
 */
@Controller
@RequestMapping("/api/base")
public class ResExtController extends BaseController {


    @Autowired
    AssetsOperService assetsOperService;

    /**
     * @Description:根据ID批量删除数据
     * @param ids
     */
    @ResponseBody
    @Acl(info = "根据ID批量删除数据", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/deleteByIds.do")
    public R deleteByIds(String ids) {
        if (ToolUtil.isEmpty(ids)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = simpleDateFormat.format(date);
        JSONArray ids_arr = JSONArray.parseArray(ids);
        if (ids_arr.size() > 10000) {
            return R.FAILURE("不得超过1000个");
        }
        ArrayList<SQL> sqls = new ArrayList<SQL>();
        for (int i = 0; i < ids_arr.size(); i++) {
            String id = ids_arr.getString(i);
            Update me = new Update("res");
            me.set("dr", "1");
            me.setIf("update_time", nowtime);
            me.setIf("update_by", this.getUserId());
            me.where().and("id=?", id);
            sqls.add(me);
        }
        db.executeSQLList(sqls);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:数据复核
     * @param search
     */
    @ResponseBody
    @Acl(info = "数据复核", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/needreview.do")
    @Transactional
    public R needreview(String search) {

        // reviewed(已复核),insert(待核(录入)),updated(待核(已更新))
        String sql = "select ";
        sql = sql + AssetsConstant.resSqlbody + " t.* from res t where dr=0  and changestate<>'reviewed'";
        if (ToolUtil.isNotEmpty(search)) {
            sql = sql + " and  (rack like '%" + search + "%' or fs1 like '%" + search + "%' or mark like '%" + search
                    + "%' or uuid like '%" + search + "%' or model like '%" + search + "%'  or  sn like '%" + search
                    + "%' )";
        }
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:数据复核操作
     * @param ids
     */
    @ResponseBody
    @Acl(info = "数据复核操作", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/review.do")
    @Transactional
    public R review(String ids) {
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);
        List<SQL> sqls = new ArrayList<SQL>();
        JSONArray ids_arr = JSONArray.parseArray(ids);
        for (int i = 0; i < ids_arr.size(); i++) {
            Update me = new Update("res");
            me.set("changestate", "reviewed");
            me.setIf("review_userid", this.getUserId());
            me.setIf("review_date", nowtime);
            me.where().and("id=?", ids_arr.getString(i));
            sqls.add(me);

            Insert ins = new Insert("res_history");
            ins.set("oper_type", "复核操作");
            ins.set("id", db.getUUID());
            ins.set("res_id", ids_arr.getString(i));
            ins.set("oper_time", nowtime);
            ins.set("oper_user", this.getUserId());
            sqls.add(ins);
        }
        db.executeSQLList(sqls);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:新增数据
     */
    @ResponseBody
    @Acl(info = "新增Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/addResCustom.do")
    @Transactional
    public R addResCustom() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return assetsOperService.saveAssets(ps);
    }

    /**
     * @Description:根据类型查询数据
     */
    @ResponseBody
    @Acl(info = "查询Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/queryResAllByClass.do")
    public R queryResAllByClass() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return assetsOperService.queryAssetsData(ps);
    }

    /**
     * @Description:根据类型查询数据
     */
    @ResponseBody
    @Acl(info = "查询Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/queryPageResAllByClass.do")
    public R queryPageResAllByClass(String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10") String pageSize, @RequestParam(value = "pageIndex", required = true, defaultValue = "1") String pageIndex) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        String sql = assetsOperService.buildQueryAssetsDataSql(ps);
        String sqlcnt = "select count(1) value from (" + sql + ") tab";
        int count = db.uniqueRecord(sqlcnt).getInteger("value");
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", count);
        retrunObject.put("iTotalDisplayRecords", count);
        retrunObject.put("success", true);
        retrunObject.put("code", 200);
        retrunObject.put("data", ConvertUtil.OtherJSONObjectToFastJSONArray(
                db.query(DbUtil.getDBPageSql(db.getDBType(), sql, pagesize, pageindex)).toJsonArrayWithJsonObject()));
        return R.clearAttachDirect(retrunObject);
    }

    /**
     * @Description:查询Res
     */
    @ResponseBody
    @Acl(info = "查询Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/queryPageResAll.do")
    public R queryResAllForPage(String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10") String pageSize, @RequestParam(value = "pageIndex", required = true, defaultValue = "1") String pageIndex) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        String sql = assetsOperService.buildQueryAssetsDataSql(ps);
        System.out.println(sql);
        String sqlcnt = "select count(1) value from (" + sql + ") tab";
        int count = db.uniqueRecord(sqlcnt).getInteger("value");
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", count);
        retrunObject.put("iTotalDisplayRecords", count);
        retrunObject.put("success", true);
        retrunObject.put("code", 200);
        retrunObject.put("data", ConvertUtil.OtherJSONObjectToFastJSONArray(
                db.query(DbUtil.getDBPageSql(db.getDBType(), sql, pagesize, pageindex)).toJsonArrayWithJsonObject()));
        return R.clearAttachDirect(retrunObject);
    }

    /**
     * @Description:查询Res
     */
    @ResponseBody
    @Acl(info = "查询Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/queryResAll.do")
    public R queryResAll() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return assetsOperService.queryAssetsData(ps);
    }


    /**
     * @Description:根据ID查询Res
     * @param id
     */
    @ResponseBody
    @Acl(info = "查询Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/queryResAllById.do")
    public R queryResAllById(String id) {
        return assetsOperService.queryAssetsDataById(id);
    }


    /**
     * @Description:根据编号查询Res
     * @param uuid
     */
    @ResponseBody
    @Acl(info = "查询Res", value = Acl.ACL_USER)
    @RequestMapping(value = "/res/queryResAllByUUID.do")
    public R queryResAllByUUID(String uuid) {
        return assetsOperService.queryAssetsDataByBusid(uuid);
    }



}
