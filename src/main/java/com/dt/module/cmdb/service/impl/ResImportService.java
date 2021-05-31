package com.dt.module.cmdb.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.cache.CacheConfig;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.impl.SysUserInfoService;
import com.dt.module.cmdb.entity.ResEntity;
import com.dt.module.cmdb.entity.ResImportResultEntity;
import com.dt.module.db.DB;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
public class ResImportService extends BaseService {

    @Autowired
    @Lazy
    AssetsOperService assetsOperService;

    @Autowired
    SysUserInfoService sysUserInfoService;

    public static String IMPORT_TYPE_INSERT="insert";
    public static String IMPORT_TYPE_UPDATE="update";

    /**
     *
     * @Description:资产导入
     */
    @Transactional(value="transactionManager")
    public R importResNormal(String file, String type, String category) {
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setStartSheetIndex(0);
            List<ResEntity> result = ExcelImportUtil.importExcel(new File(file), ResEntity.class, params);
            return executeEntitysImport(result, type, category);
        } catch (Exception e) {
            e.printStackTrace();
            return R.FAILURE("导入数据异常");
        }
    }


    /**
     * @Description:检查价格
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkBuyPrice'+#value")
    public R checkBuyPrice(String value) {
        if (ToolUtil.isEmpty(value)) {
            return R.SUCCESS_OPER("0");
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
     * @Description:检查uuid
     */
    private int checkResIfUnique(String uuid) {
        return db.uniqueRecord("select count(1) cnt from res where dr='0' and uuid=?", uuid).getInteger("cnt");
    }

    /**
     * @Description:匹配资数据字典
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#dict+'_'+#name")
    public R checkDictItem(String dict, String name) {

        if (ToolUtil.isEmpty(name)) {
            JSONObject e = new JSONObject();
            e.put("dict_item_id", "");
            return R.SUCCESS_OPER(e);
        }
        Rcd rs = db.uniqueRecord("select dict_item_id from sys_dict_item where dr='0' and dict_id=? and name=?",
                dict, name);
        if (rs == null) {
            return R.FAILURE("无法匹配数据字典项目:Dict:" + dict + ",value:" + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }


    /**
     * @Description:匹配组织
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkOrgItem'+#type+'_'+#name")
    public R checkOrgItem(String type, String name) {
        if (ToolUtil.isEmpty(name)) {
            JSONObject e = new JSONObject();
            e.put("node_id", "");
            return R.SUCCESS_OPER(e);
        }
        Rcd rs = db.uniqueRecord("select node_id from hrm_org_part where dr='0' and type=? and route_name=?", type, name);
        if (rs == null) {
            return R.FAILURE("无法匹配到组织,类型" + type + ",名称:" + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }


    /**
     * @Description:匹配资产类目
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#name")
    public R checkAssetsCategory(String name) {

        if (ToolUtil.isEmpty(name)) {
            return R.FAILURE("大类不允许为空或该行为空");
        }

        Rcd rs = db.uniqueRecord("select * from ct_category where dr='0' and route_name=?", name);
        if (rs == null) {
            return R.FAILURE("无法匹配大类," + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }


    /**
     * @Description:检查资产导入数据
     */
    public R checkResEntity(ResEntity re, String type, String importlabel, String category) {
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);
        String sql = "";
        int uuidR = checkResIfUnique(re.getUuid());
        R buypriceR = checkBuyPrice(re.getBuy_price());
        if (buypriceR.isFailed()) {
            return R.FAILURE(buypriceR.getMessage());
        }

        R checkBuyTimeR = checkDateTime(re.getBuy_price());
        if (checkBuyTimeR.isFailed()) {
            return R.FAILURE(checkBuyTimeR.getMessage());
        }

        R checkwboutdateR = checkDateTime(re.getWbout_datestr());
        if (checkwboutdateR.isFailed()) {
            return R.FAILURE(checkwboutdateR.getMessage());
        }
        R checkbfoutdateR = checkDateTime(re.getBfout_datestr());
        if (checkbfoutdateR.isFailed()) {
            return R.FAILURE(checkbfoutdateR.getMessage());
        }
        // 数据字典选项
        R classR = checkAssetsCategory(re.getClassfullname());
        if (classR.isFailed()) {
            return R.FAILURE(classR.getMessage());
        }

        R rackR = checkDictItem("devrack", re.getRackstr());
        System.out.println(rackR.toString());
        if (rackR.isFailed()) {
            return R.FAILURE(rackR.getMessage());
        }

        R brandR = checkDictItem("devbrand", re.getBrandstr());
        if (brandR.isFailed()) {
            return R.FAILURE(brandR.getMessage());
        }

        R recycleR = checkDictItem("devrecycle", re.getRecyclestr());
        if (recycleR.isFailed()) {
            return R.FAILURE(recycleR.getMessage());
        }

        R wbR = checkDictItem("devwb", re.getWbstr());
        if (wbR.isFailed()) {
            return R.FAILURE(wbR.getMessage());
        }
        R riskR = checkDictItem("devrisk", re.getRiskstr());
        if (riskR.isFailed()) {
            return R.FAILURE(riskR.getMessage());
        }

        R locR = checkDictItem("devdc", re.getLocstr());
        if (locR.isFailed()) {
            return R.FAILURE(locR.getMessage());
        }

        R envR = checkDictItem("devenv", re.getEnvstr());
        if (envR.isFailed()) {
            return R.FAILURE(envR.getMessage());
        }

        R zcwbcomouteR = checkDictItem("zcwbcomoute", re.getWb_autostr());
        if (zcwbcomouteR.isFailed()) {
            return R.FAILURE(zcwbcomouteR.getMessage());
        }

        R zcsourceR = checkDictItem("zcsource", re.getZcsourcestr());
        if (zcsourceR.isFailed()) {
            return R.FAILURE(zcsourceR.getMessage());
        }

        R zcsupperR = checkDictItem("zcsupper", re.getSupplierstr());
        if (zcsupperR.isFailed()) {
            return R.FAILURE(zcsupperR.getMessage());
        }

        R wbsupplierR = checkDictItem("zcwbsupper", re.getWbsupplierstr());
        if (wbsupplierR.isFailed()) {
            return R.FAILURE(wbsupplierR.getMessage());
        }

        R belongcompR = checkOrgItem("comp", re.getBelongcomp_fullname());
        if (belongcompR.isFailed()) {
            return R.FAILURE(belongcompR.getMessage());
        }

        R partR = checkOrgItem("part", re.getPart_fullname());
        if (partR.isFailed()) {
            return R.FAILURE(partR.getMessage());
        }

        if (partR.isFailed()) {
            return R.FAILURE(partR.getMessage());
        }

        R uselifeR = checkDictItem("zcusefullife", re.getUsefullifestr());
        if (uselifeR.isFailed()) {
            return R.FAILURE(uselifeR.getMessage());
        }

//		String typestr = null;
//		if (ToolUtil.isNotEmpty(re.getTypestr())) {
//			// 支持的小类类型:网点、服务器、电脑、安全设备,IT备件
//			R r = checkDictItemSub("'devsafety','devdotequipment','devservertype','devcompute','devbjpj'", re.getTypestr(),
//					classR.queryDataToJSONObject().getString("dict_item_id"));
//			if (r.isFailed()) {
//				return R.FAILURE(r.getMessage());
//			} else {
//				typestr = r.queryDataToJSONObject().getString("dict_item_id");
//			}
//		}
        String emplid = re.getEmplid();
        String useduserid = "";
        if (ToolUtil.isNotEmpty(emplid)) {
            Rcd userrs = db.uniqueRecord("select user_id from sys_user_info where dr='0' and empl_id=?", emplid);
            if (userrs != null) {
                useduserid = userrs.getString("user_id");
            }
        }
        if (IMPORT_TYPE_INSERT.equals(type)) {
            Insert me = new Insert("res");
            me.set("importlabel", importlabel);
            me.set("id", db.getUUID());
            me.set("dr", "0");
            me.setIf("unit", re.getUnit() == null ? null : re.getUnit());
            me.setIf("changestate", "updated");
            me.setIf("create_time", nowtime);
            me.setIf("create_by", this.getUserId());
            me.setIf("update_time", nowtime);
            me.setIf("update_by", this.getUserId());
            me.setIf("category", category);
            me.setIf("fs10","后台Excel批量导入");
            /////////////// 开始处理///////////
            me.setIf("name", re.getName()== null ? "" : re.getName());
            me.setIf("fs1", re.getFs1() == null ? "" : re.getFs1());
            me.setIf("fs2", re.getFs2() == null ? "" : re.getFs2());
            me.setIf("fs20", re.getFs20() == null ? "" : re.getFs20());
            me.setIf("ip", re.getIp() == null ? "" : re.getIp());
            me.setIf("frame", re.getFrame() == null ? "" : re.getFrame());
            me.setIf("model", re.getModel() == null ? "" : re.getModel());
            me.setIf("confdesc", re.getConfdesc() == null ? "" : re.getConfdesc());
            me.setIf("mark", re.getMark() == null ? "" : re.getMark());
            me.setIf("locdtl", re.getLocdtl() == null ? "" : re.getLocdtl());
            me.setIf("sn", re.getSn() == null ? "" : re.getSn());
            me.setIf("batch", re.getBatch() == null ? "" :  re.getBatch());
            me.setIf("net_worth", re.getNet_worth() == null ? "0" : re.getNet_worth());
            me.setIf("accumulateddepreciation", re.getAccumulateddepreciation() == null ? "0" : re.getAccumulateddepreciation());
            me.setIf("buy_price", re.getBuy_price() == null ? "0" : re.getBuy_price());
            me.setIf("wbout_date", re.getWbout_datestr() == null ? null : re.getWbout_datestr() + " 01:00:00");
            me.setIf("bfout_date", re.getBfout_datestr() == null ? null : re.getBfout_datestr() + " 01:00:00");
            me.setIf("buy_time", re.getBuy_timestr() == null ? null : re.getBuy_timestr() + " 01:00:00");
            me.setIf("fd1", re.getFd1str() == null ? null : re.getFd1str() + " 00:00:00");
            me.setIf("used_userid", useduserid);
            if(ToolUtil.isNotEmpty(useduserid)){
                me.setIf("part_id",sysUserInfoService.queryUserPart(useduserid));
            }else{
                me.setIf("part_id",partR.queryDataToJSONObject().getString("node_id"));
            }
            // 数据字典匹配
            me.setIf("class_id", classR.queryDataToJSONObject().getString("id"));
            me.setIf("rack", rackR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("brand", brandR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("recycle", recycleR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("wb", wbR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("risk", riskR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("env", envR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("wb_auto", zcwbcomouteR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("zcsource", zcsourceR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("supplier", zcsupperR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("wbsupplier", wbsupplierR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("belong_company_id", belongcompR.queryDataToJSONObject().getString("node_id"));
            me.setIf("usefullife", uselifeR.queryDataToJSONObject().getString("dict_item_id"));

            // 处理资产编号,必需不存在
            if (ToolUtil.isEmpty(re.getUuid())) {
                // 插入时候，无编号自动生产
                me.setIf("uuid", assetsOperService.createUuid(AssetsConstant.UUID_ZC));
            } else {
                return R.FAILURE("不允许存在资产编号");
            }
            sql = me.getSQL();
        } else if (IMPORT_TYPE_UPDATE.equals(type)) {
            Update me = new Update("res");
            me.set("importlabel", importlabel);
            me.setIf("changestate", "updated");
            me.setIf("update_time", nowtime);
            me.setIf("update_by", this.getUserId());
            /////////////// 开始处理////////////
            me.setIf("name", re.getName()== null ? "" : re.getName());
            me.setIf("unit", re.getUnit() == null ? null : re.getUnit());
            me.setIf("fs1", re.getFs1() == null ? "" : re.getFs1());
            me.setIf("fs2", re.getFs2() == null ? "" : re.getFs2());
            me.setIf("fs20", re.getFs20() == null ? "" : re.getFs20());
            me.setIf("ip", re.getIp() == null ? "" : re.getIp());
            me.setIf("frame", re.getFrame() == null ? "" : re.getFrame());
            me.setIf("model", re.getModel() == null ? "" : re.getModel());
            me.setIf("confdesc", re.getConfdesc() == null ? "" : re.getConfdesc());
            me.setIf("mark", re.getMark() == null ? "" : re.getMark());
            me.setIf("locdtl", re.getLocdtl() == null ? "" : re.getLocdtl());
            me.setIf("sn", re.getSn() == null ? "" : re.getSn());
            me.setIf("batch", re.getBatch() == null ? "" :  re.getBatch());
            me.setIf("net_worth", re.getNet_worth() == null ? "0" : re.getNet_worth());
            me.setIf("buy_price", re.getBuy_price() == null ? "0" : re.getBuy_price());
            me.setIf("accumulateddepreciation", re.getAccumulateddepreciation() == null ? "0" : re.getAccumulateddepreciation());
            me.setIf("buy_time", re.getBuy_timestr() == null ? null : re.getBuy_timestr() + " 01:00:00");
            me.setIf("wbout_date", re.getWbout_datestr() == null ? null : re.getWbout_datestr() + " 01:00:00");
            me.setIf("bfout_date", re.getBfout_datestr() == null ? null : re.getBfout_datestr() + " 01:00:00");
            me.setIf("fd1", re.getFd1str() == null ? null : re.getFd1str() + " 00:00:00");
            me.setIf("used_userid", useduserid);
            if(ToolUtil.isNotEmpty(useduserid)){
                me.setIf("part_id",sysUserInfoService.queryUserPart(useduserid));
            }else{
                me.setIf("part_id",partR.queryDataToJSONObject().getString("node_id"));
            }
            // 数据字典匹配
            me.setIf("class_id", classR.queryDataToJSONObject().getString("id"));
            me.setIf("rack", rackR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("brand", brandR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("recycle", recycleR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("wb", wbR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("risk", riskR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("env", envR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("wb_auto", zcwbcomouteR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("zcsource", zcsourceR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("supplier", zcsupperR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("wbsupplier", wbsupplierR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("belong_company_id", belongcompR.queryDataToJSONObject().getString("node_id"));
            me.setIf("usefullife", uselifeR.queryDataToJSONObject().getString("dict_item_id"));
            // 处理资产编号,必需一条
            if (uuidR == 1) {
                me.set("uuid", re.getUuid());
            } else {
                return R.FAILURE("资产编号不存在");
            }
            me.where().and("uuid=?", re.getUuid());
            sql = me.getSQL();
        }
        return R.SUCCESS_OPER(sql);
    }

    /**
     * @Description:检查导入
     */
    private ResImportResultEntity checkResEntitys(List<ResEntity> result, String type, String category,String importlabel) {
        ResImportResultEntity cres = new ResImportResultEntity();
        for (int i = 0; i < result.size(); i++) {
            R r = checkResEntity(result.get(i), type, importlabel, category);
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
     * @Description:执行导入
     */

    @Transactional(value="transactionManager")
    public R executeEntitysImport(List<ResEntity> resultdata, String type, String category) throws SQLException {
        String importlabel = ToolUtil.getUUID();
        ResImportResultEntity result = checkResEntitys(resultdata, type, category,importlabel);
        result.printResult();
        if (!result.is_success_all) {
            return R.FAILURE("操作失败", result.covertJSONObjectResult());
        }else{
            if(result.success_cnt!=resultdata.size()){
                return R.FAILURE("数量不一致");
            }
        }
       // result.success_cmds.add("INSERT INTO res (importlabel,id,dr,changestate,create_time,create_by,update_time,update_by,category,name,fs1,fs2,fs20,ip,frame,model,confdesc,mark,locdtl,sn,net_worth,accumulateddepreciation,buy_price,wbout_date,buy_time,fd1,used_userid,class_id,rack,brand,recycle,wb,risk,loc,env,wb_auto,zcsource,supplier,wbsupplier,belong_company_id,part_id,usefullife,uuid) VALUES('ecfe29b5ee55493d80c7dbd8eb7d9359','c46e0ddf-11ae-46d7-add2-8c15f9ed90bc','0','updated','2021-01-04 12:14:21','1151420235196588033','2021-01-04 12:14:21','1151420235196588033','3','他天天','','','','','','吞吞吐吐','','','12','','12012','12','1212','2020-12-26 01:00:00','2020-12-25 01:00:00','2020-12-25 00:00:00','','319','','hp','idle','valid','','xyl','','','1252422763608596481','1252454262470893569','1252422861805641729','35','','1290416493251977218','ZC7365-436E-A48C')");
        for(int i=0;i<result.success_cmds.size();i++){
            System.out.println(result.success_cmds.get(i));
        }
        String importresult="SUCCESS";
        Connection con = DB.instance().getDataSource().getConnection();
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            for(int i=0;i<result.success_cmds.size();i++){
                stmt.execute(result.success_cmds.get(i));
            }
            con.commit();
        }catch(Exception e){
            importresult=e.getMessage();
            con.rollback();
        }finally{
            con.close();
       // assetsOperService.checkMaintenanceMethod();
        if("SUCCESS".equals(importresult)){
            return R.SUCCESS_OPER();
        }else{
            return R.FAILURE("导入报错,"+importresult);
        }
    }
    }

}
