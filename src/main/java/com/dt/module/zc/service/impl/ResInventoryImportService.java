package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.cache.CacheConfig;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.entity.ResInventoryEntity;
import com.dt.module.zc.entity.ResInventoryImportResultEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: lank
 * @date: Nov 3, 2019 8:31:41 AM
 * @Description:
 */
@Service
public class ResInventoryImportService extends BaseService {

    /**
     * @Description:检查采购价格
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkBuyPrice'+#value")
    public R checkBuyPrice(String value) {
        if (ToolUtil.isEmpty(value)) {
            return R.SUCCESS_OPER("0.0");
        }
        try {
            // 支持科学计数法
            Double r = new BigDecimal(value.trim()).doubleValue();
            return R.SUCCESS_OPER(r.toString());
        } catch (Exception e) {
            return R.FAILURE("时间转换出错,请优先保证数据正确");
        }
    }

    /**
     * @Description:检查时间
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDateTime'+#value")
    public R checkDateTime(String value) {
        if (ToolUtil.isEmpty(value)) {
            return R.SUCCESS();
        }
        return R.SUCCESS();
    }

    /**
     * @Description:检查数据字典
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#dict+'_'+#name")
    public R checkDictItem(String dict, String name) {
        // 大类为空,则失败
        if ("devclass".equals(dict)) {
            if (ToolUtil.isEmpty(name)) {
                return R.FAILURE("大类不允许为空或该行为空");
            }
        }
        // 其他为空，判断为成功
        if (ToolUtil.isEmpty(name)) {
            JSONObject e = new JSONObject();
            e.put("dict_item_id", "");
            return R.SUCCESS_OPER(e);
        }
        Rcd rs = db.uniqueRecord("select dict_item_id from dt.sys_dict_item where dr='0' and dict_id=? and name=?",
                dict, name);
        if (rs == null) {
            return R.FAILURE("无法匹配数据字典项目:Dict:" + dict + ",value:" + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }


    /**
     * @Description:检查组织ID
     */
    // @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkOrgItem'+#type+'_'+#name")
    public R checkOrgItem(String type, String name) {
        if (ToolUtil.isEmpty(name)) {
            JSONObject e = new JSONObject();
            e.put("node_id", "");
            return R.SUCCESS_OPER(e);
        }
        Rcd rs = db.uniqueRecord("select node_id from hrm_org_part where dr='0' and type=? and route_name=?", type, name);
        if (rs == null) {
            return R.FAILURE("无法匹配到组织,名称:" + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }



    /**
     * @Description:判断当前的支持类型
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#name")
    public R checkZCClass(String name) {
        // 大类为空,则失败
        if (ToolUtil.isEmpty(name)) {
            return R.FAILURE("大类不允许为空或该行为空");
        }
        // 其他为空，判断为成功
        if (ToolUtil.isEmpty(name)) {
            JSONObject e = new JSONObject();
            e.put("id", "");
            return R.SUCCESS_OPER(e);
        }

        Rcd rs = db.uniqueRecord("select * from ct_category  where dr='0' and route_name=?", name);
        if (rs == null) {
            return R.FAILURE("无法匹配大类," + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }

    /**
     * @Description:检查资产
     */
    public R checkResEntity(ResInventoryEntity re, String importlabel) {
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);
        String sql = "";


        R buypriceR = checkBuyPrice(re.getBuy_price());
        if (buypriceR.isFailed()) {
            return R.FAILURE(buypriceR.getMessage());
        }

        R checkBuyTimeR = checkDateTime(re.getBuy_price());
        if (checkBuyTimeR.isFailed()) {
            return R.FAILURE(checkBuyTimeR.getMessage());
        }


        // 数据字典选项
        R classR = checkZCClass(re.getClassfullname());
        if (classR.isFailed()) {
            return R.FAILURE(classR.getMessage());
        }

        R brandR = checkDictItem("devbrand", re.getBrandstr());
        if (brandR.isFailed()) {
            return R.FAILURE(brandR.getMessage());
        }

        R recycleR = checkDictItem("devrecycle", re.getRecyclestr());
        if (recycleR.isFailed()) {
            return R.FAILURE(recycleR.getMessage());
        }


        R locR = checkDictItem("devdc", re.getLocstr());
        if (locR.isFailed()) {
            return R.FAILURE(locR.getMessage());
        }


        R zcsourceR = checkDictItem("zcsource", re.getZcsourcestr());
        if (zcsourceR.isFailed()) {
            return R.FAILURE(zcsourceR.getMessage());
        }

        R zcsupperR = checkDictItem("zcsupper", re.getSupplierstr());
        if (zcsupperR.isFailed()) {
            return R.FAILURE(zcsupperR.getMessage());
        }

        //组织信息
        R belongcompR = checkOrgItem("comp", re.getBelongcomp_fullname());
        if (belongcompR.isFailed()) {
            return R.FAILURE(belongcompR.getMessage());
        }


//        R compR = checkOrgItem("comp", re.getComp_fullname());
//        if (compR.isFailed()) {
//            return R.FAILURE(compR.getMessage());
//        }

        R partR = checkOrgItem("part", re.getPart_fullname());
        if (partR.isFailed()) {
            return R.FAILURE(partR.getMessage());
        }


        String pdstatus = ResInventoryService.INVENTORY_ITEM_STATAUS_WAIT;
        if ("已盘点".equals(re.getPdstatusstr())) {
            pdstatus = ResInventoryService.INVENTORY_STATAUS_FINISH;
        }

        String pdsyncneed = ResInventoryService.INVENTORY_ITEM_ACTION_NOSYNC;
//        if ("更新".equals(re.getPdsyncneedstr())) {
//            pdsyncneed = ResInventoryService.INVENTORY_ITEM_ACTION_SYNC;
//        }

        Update me = new Update("res_inventory_item");
        me.set("importlabel", importlabel);
        me.setIf("update_time", nowtime);
        me.setIf("update_by", this.getUserId());

        /////////////// 开始处理////////////

        me.setIf("pdstatus", pdstatus);
        me.setIf("pdsyncneed", pdsyncneed);
        me.setIf("pduserid", this.getUserId());
        me.setIf("pdtime", nowtime);

        me.setIf("fs20", re.getFs20() == null ? "" : re.getFs20());
        me.setIf("model", re.getModel() == null ? "" : re.getModel());
        me.setIf("confdesc", re.getConfdesc() == null ? "" : re.getConfdesc());
        me.setIf("locdtl", re.getLocdtl() == null ? "" : re.getLocdtl());
        me.setIf("sn", re.getSn() == null ? "" : re.getSn());
        me.setIf("net_worth", re.getNet_worth() == null ? "0" : re.getNet_worth());
        me.setIf("buy_price", re.getBuy_price() == null ? "0" : re.getBuy_price());
        me.setIf("buy_time", re.getBuy_timestr() == null ? null : re.getBuy_timestr() + " 01:00:00");

        // 数据字典匹配
        me.setIf("class_id", classR.queryDataToJSONObject().getString("id"));
        me.setIf("brand", brandR.queryDataToJSONObject().getString("dict_item_id"));
        me.setIf("recycle", recycleR.queryDataToJSONObject().getString("dict_item_id"));
        me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
        me.setIf("zcsource", zcsourceR.queryDataToJSONObject().getString("dict_item_id"));
        me.setIf("supplier", zcsupperR.queryDataToJSONObject().getString("dict_item_id"));
        me.setIf("belong_company_id", belongcompR.queryDataToJSONObject().getString("node_id"));

        me.setIf("part_id", partR.queryDataToJSONObject().getString("node_id"));

        me.where().and("uuid=?", re.getUuid()).andIf("pdbatchid=?", re.getPdbatchid());
        sql = me.getSQL();

        return R.SUCCESS_OPER(sql);
    }

    /**
     * @Description:资产盘点数据导入结果
     */
    private ResInventoryImportResultEntity checkResInventoryEntitys(List<ResInventoryEntity> result) {
        String importlabel = ToolUtil.getUUID();
        ResInventoryImportResultEntity cres = new ResInventoryImportResultEntity();
        for (int i = 0; i < result.size(); i++) {
            R r = checkResEntity(result.get(i), importlabel);
            if (r.isSuccess()) {
                cres.addSuccess(r.getData().toString());
            } else {
                result.get(i).setProcessmsg(r.getMessage());
                cres.addFailed(result.get(i));
            }
        }
        return cres;
    }


    /**
     * @Description:执行资产导入
     */
    public R executeEntitysImport(List<ResInventoryEntity> resultdata) {
        ResInventoryImportResultEntity result = checkResInventoryEntitys(resultdata);
        result.printResult();
        if (!result.is_success_all) {
            return R.FAILURE("操作失败", result.covertJSONObjectResult());
        }
        db.executeStringList(result.success_cmds);
        return R.SUCCESS_OPER();

    }


}
