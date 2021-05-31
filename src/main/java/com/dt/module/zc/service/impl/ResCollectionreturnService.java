package com.dt.module.zc.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.entity.ResCollectionreturn;
import com.dt.module.zc.entity.ResCollectionreturnItem;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResCollectionreturnItemService;
import com.dt.module.zc.service.IResCollectionreturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResCollectionreturnService extends BaseService {

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResCollectionreturnService ResCollectionreturnServiceImpl;

    @Autowired
    IResCollectionreturnItemService ResCollectionreturnItemServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    @Lazy
    AssetsFlowService assetsFlowService;

    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid,String type) {
        if(AssetsConstant.ASSETS_BUS_TYPE_LY.equals(type)){
            QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
            q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_LY);
            q.eq("busuuid",busid);
            List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
            for(int j=0;j<list.size();j++) {
                ResChangeItem entity = list.get(j);
                String ct = "无";
                String busuuid = entity.getBusuuid();
                String resid = entity.getResid();
                R res = selectData(busuuid, resid);
                JSONArray res_arr = res.queryDataToJSONArray();
                if (res_arr.size() == 1) {
                    ct = "";
                    JSONObject item = res_arr.getJSONObject(0);
                    String fusedcomp = item.getString("fcompfullname");
                    String tusedcomp = item.getString("tcompfullname");
                    String fpart = item.getString("fpartfullame");
                    String tpart = item.getString("tpartfullame");
                    String fuser = item.getString("fusedusername");
                    String tuser = item.getString("tusedusername");
                    String floc = item.getString("flocstr");
                    String tloc = item.getString("tlocstr");
                    String flocdtl = item.getString("flocdtl");
                    String tlocdtl = item.getString("tlocdtl");

                    ct = "【使用公司】字段由 \"" + fusedcomp + "\" 变更为 \"" + tusedcomp + "\"";
                    ct = ct + ";【使用部门】字段由 \"" + fpart + "\" 变更为 \"" + tpart + "\"";
                    ct = ct + ";【使用人】字段由 \"" + fuser + "\" 变更为 \"" + tuser + "\"";
                    ct = ct + ";【存放区域】字段由 \"" + floc + "\" 变更为 \"" + tloc + "\"";
                    ct = ct + ";【位置】字段由 \"" + flocdtl + "\" 变更为 \"" + tlocdtl + "\"";
                    ct = ct + ";【状态】字段由 \"闲置\" 变更为 \"在用\"";
                }
                entity.setFillct("1");
                entity.setCt(ct);
                ResChangeItemServiceImpl.saveOrUpdate(entity);
            }
        }else if(AssetsConstant.ASSETS_BUS_TYPE_TK.equals(type)){
            QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
            q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_LY);
            q.eq("busuuid",busid);
            List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
            for(int j=0;j<list.size();j++) {
                ResChangeItem entity = list.get(j);
                String ct = "无";
                String busuuid = entity.getBusuuid();
                String resid = entity.getResid();

                R res = selectData(busuuid, resid);
                JSONArray res_arr = res.queryDataToJSONArray();
                if (res_arr.size() == 1) {
                    ct = "";
                    JSONObject item = res_arr.getJSONObject(0);
                    String fusedcomp = item.getString("fcompfullname");
                    String tusedcomp = item.getString("tcompfullname");
                    String fpart = item.getString("fpartfullame");
                    String tpart = item.getString("tpartfullame");
                    String fuser = item.getString("fusedusername");
                    String tuser = item.getString("tusedusername");
                    String floc = item.getString("flocstr");
                    String tloc = item.getString("tlocstr");
                    String flocdtl = item.getString("flocdtl");
                    String tlocdtl = item.getString("tlocdtl");
                    ct = "【使用公司】字段由 \"" + fusedcomp + "\" 变更为 \"" + tusedcomp + "\"";
                    ct = ct + ";【使用部门】字段由 \"" + fpart + "\" 变更为 \"" + tpart + "\"";
                    ct = ct + ";【使用人】字段由 \"" + fuser + "\" 变更为 \"" + tuser + "\"";
                    ct = ct + ";【存放区域】字段由 \"" + floc + "\" 变更为 \"" + tloc + "\"";
                    ct = ct + ";【位置】字段由 \"" + flocdtl + "\" 变更为 \"" + tlocdtl + "\"";
                    ct = ct + ";【状态】字段由 \"在用\" 变更为 \"退库\"";
                }
                entity.setFillct("1");
                entity.setCt(ct);
                ResChangeItemServiceImpl.saveOrUpdate(entity);
            }
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:启动领用流程
     */
    public R startLyFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("busuuid", uuid);
            ResCollectionreturnServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("busuuid", uuid);
            ResCollectionreturnServiceImpl.update(ups);
            confirmLy(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:启动退库流程
     */
    public R startTkFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("busuuid", uuid);
            ResCollectionreturnServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("busuuid", uuid);
            ResCollectionreturnServiceImpl.update(ups);
            confirmTk(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:结束领用流程
     */
    public R finishLyFlow(String busid, String status) {
        if (FlowConstant.PSTATUS_DTL_FAILED.equals(status)) {
            return cancelLy(busid, FlowConstant.PSTATUS_DTL_FAILED);
        } else if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(status)) {
            return confirmLy(busid, FlowConstant.PSTATUS_DTL_SUCCESS);
        } else {
            return R.FAILURE_NO_DATA();
        }
    }

    /**
     * @Description:结束退库流程
     */
    public R finishTkFlow(String busid, String status) {
        if (FlowConstant.PSTATUS_DTL_FAILED.equals(status)) {
            return cancelTk(busid, FlowConstant.PSTATUS_DTL_FAILED);
        } else if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(status)) {
            return confirmTk(busid, FlowConstant.PSTATUS_DTL_SUCCESS);
        } else {
            return R.FAILURE_NO_DATA();
        }
    }


    /**
     * @Description:取消领用
     */
    public R cancelLy(String busid, String status) {
        //更新RES数据
        String sql2 = "update res_collectionreturn_item a,res b set " +
//                "b.loc=a.tloc," +
//                "b.used_company_id=a.tusedcompanyid," +
//                "b.part_id=a.tpartid," +
//                "b.used_userid=a.tuseduserid," +
//                "b.locdtl=a.tlocdtl," +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
                //         "b.uuidly=a.busuuid " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);
        UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResCollectionreturnServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消退库
     */
    public R cancelTk(String busid, String status) {
        //更新RES数据
        String sql2 = "update res_collectionreturn_item a,res b set    " +
//                "b.loc=a.tloc," +
//                "b.used_company_id=a.tusedcompanyid," +
//                "b.part_id=a.tpartid," +
//                "b.used_userid=a.tuseduserid," +
//                "b.locdtl=a.tlocdtl," +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
//                "b.uuidly=a.busuuid " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);
        UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResCollectionreturnServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:确认领用
     */
    public R confirmLy(String busid, String status) {
        UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResCollectionreturnServiceImpl.update(ups);
        //保存变更前RES数据
        String sql = "update res_collectionreturn_item a,res b set    " +
                "   a.fusedcompanyid=b.used_company_id   " +
                " , a.fpartid=b.part_id   " +
                " , a.fuseduserid=b.used_userid   " +
         //       " , a.floc=b.loc   " +
                " , a.flocdtl=b.locdtl   " +
                "   where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, busid);

        //更新RES数据
        String sql2 = "update res_collectionreturn_item a,res b set    " +
                //      "b.loc=a.tloc," +
                "b.used_company_id=a.tusedcompanyid," +
                "b.part_id=a.tpartid," +
                "b.used_userid=a.tuseduserid," +
                "b.locdtl=a.tlocdtl," +
                "b.recycle='" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "'," +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='', " +
                "b.uuidly=a.busuuid " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);

        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResCollectionreturnItem> ew = new QueryWrapper<ResCollectionreturnItem>();
        ew.and(i -> i.eq("busuuid", busid));
        List<ResCollectionreturnItem> items = ResCollectionreturnItemServiceImpl.list(ew);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_LY);
            e.setCreateBy(this.getUserId());
            e.setCt("资产领用,领用人:" + items.get(i).getCrusername());
            e.setFillct("1");
            e.setCdate(new Date());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid,AssetsConstant.ASSETS_BUS_TYPE_LY);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:确认退库
     */
    public R confirmTk(String busid, String status) {
        UpdateWrapper<ResCollectionreturn> ups = new UpdateWrapper<ResCollectionreturn>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResCollectionreturnServiceImpl.update(ups);
        //保存变更前数据
        String sql = "update res_collectionreturn_item a,res b set    " +
                "   a.fusedcompanyid=b.used_company_id   " +
                " , a.fpartid=b.part_id   " +
                " , a.fuseduserid=b.used_userid   " +
                //       " , a.floc=b.loc   " +
                " , a.flocdtl=b.locdtl   " +
                "   where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, busid);

        //更新数据
        String sql2 = "update res_collectionreturn_item a,res b set " +
                //        "b.loc=a.tloc," +
                "b.used_company_id=a.tusedcompanyid," +
                "b.part_id=a.tpartid," +
                "b.used_userid=a.tuseduserid," +
                "b.locdtl=a.tlocdtl," +
                "b.recycle='" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "'," +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='', " +
                "b.uuidly='' " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);
        String sql3 = "update res_collectionreturn_item a,res_collectionreturn_item b set " +
                " a.returnuuid=b.busuuid," +
                " a.rreturndate=b.rreturndate," +
                " a.isreturn='1'" +
                " where a.resid=b.resid and b.busuuid=? and b.dr='0'";
        db.execute(sql3, busid);
        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResCollectionreturnItem> ew = new QueryWrapper<ResCollectionreturnItem>();
        ew.and(i -> i.eq("busuuid", busid));
        List<ResCollectionreturnItem> items = ResCollectionreturnItemServiceImpl.list(ew);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_TK);
            e.setFillct("1");
            e.setCt("资产退库,退库人:" + items.get(i).getCrusername());
            e.setCdate(new Date());
            e.setCreateBy(this.getUserId());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid,AssetsConstant.ASSETS_BUS_TYPE_TK);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:保存领用退库单据
     */
    public R save(ResCollectionreturn entity, String items) {
        String type = entity.getBustype();
        if (AssetsConstant.ASSETS_BUS_TYPE_LY.equals(type)) {
            return saveLy(entity, items);
        } else if (AssetsConstant.ASSETS_BUS_TYPE_TK.equals(type)) {
            return saveTk(entity, items);
        } else {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
    }

    /**
     * @Description:保存领用单据
     */
    public R saveLy(ResCollectionreturn entity, String items) {
        String id = entity.getId();
        String uuid = "";
        entity.setBustype(AssetsConstant.ASSETS_BUS_TYPE_LY);
        //获取UUID
        if (ToolUtil.isNotEmpty(id)) {
            //修改单据
            uuid = entity.getBusuuid();
            if (!FlowConstant.PSTATUS_APPLY.equals(entity.getStatus())) {
                return R.FAILURE("当前状态不允许修改");
            }
            //可能数据有变动，先解锁当前的数据,后面会重新加锁
            String sql2 = "update res_collectionreturn_item a,res b set b.inprocess='0',b.inprocessuuid='',b.inprocesstype='' where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
            db.execute(sql2, uuid);
        } else {
            //生产单据
            uuid = assetsOperService.createUuid(AssetsConstant.UUID_LY);
            entity.setProcessuserid(getUserId());
            entity.setProcessusername(getName());
            //设置流程申请
            entity.setBusuuid(uuid);
            //等待申请
            entity.setStatus(FlowConstant.PSTATUS_APPLY);
        }
        JSONArray items_arr = JSONArray.parseArray(items);
        ArrayList<ResCollectionreturnItem> list = new ArrayList<ResCollectionreturnItem>();
        for (int i = 0; i < items_arr.size(); i++) {
            ResCollectionreturnItem e = new ResCollectionreturnItem();
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            e.setBusdate(entity.getBusdate());
            e.setReturndate(entity.getReturndate());
            e.setBusuuid(uuid);
            e.setCruserid(entity.getCruserid());
            e.setCrusername(entity.getCrusername());
            e.setProcessuserid(entity.getProcessuserid());
            e.setProcessusername(entity.getProcessusername());
            e.setTusedcompanyid(entity.getTusedcompanyid());
            e.setTusedcompanyname(entity.getTusedcompanyname());
            e.setTpartid(entity.getTpartid());
            e.setTpartname(entity.getTpartname());
            e.setTuseduserid(entity.getTuseduserid());
            e.setTusedusername(entity.getTusedusername());
            e.setTloc(entity.getTloc());
            e.setTlocdtl(entity.getTlocdtl());
            e.setIsreturn("0");
            list.add(e);
        }
        //删除item数据,重新保存
        QueryWrapper<ResCollectionreturnItem> qw = new QueryWrapper<ResCollectionreturnItem>();
        String finalUuid = uuid;
        qw.and(i -> i.eq("busuuid", finalUuid));
        ResCollectionreturnItemServiceImpl.remove(qw);
        ResCollectionreturnItemServiceImpl.saveOrUpdateBatch(list);
        ResCollectionreturnServiceImpl.saveOrUpdate(entity);
        String sql3 = "update res_collectionreturn_item a,res b set b.inprocess='1',b.inprocessuuid='" + uuid + "',b.inprocesstype='" + AssetsConstant.ASSETS_BUS_TYPE_LY + "' where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql3, uuid);
        JSONObject res = new JSONObject();
        res.put("busid", uuid);
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:保存退库单据
     */
    public R saveTk(ResCollectionreturn entity, String items) {
        String id = entity.getId();
        String uuid = "";
        entity.setBustype(AssetsConstant.ASSETS_BUS_TYPE_TK);
        //获取UUID
        if (ToolUtil.isNotEmpty(id)) {
            uuid = entity.getBusuuid();
            //解锁之前的数据,
            String sql2 = "update res_collectionreturn_item a,res b set b.inprocess='0',b.inprocessuuid='',b.inprocesstype='' where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
            db.execute(sql2, uuid);
        } else {
            uuid = assetsOperService.createUuid(AssetsConstant.UUID_TK);
            entity.setProcessuserid(getUserId());
            entity.setProcessusername(getName());
            entity.setBusuuid(uuid);
            entity.setStatus(FlowConstant.PSTATUS_APPLY);
        }
        JSONArray items_arr = JSONArray.parseArray(items);
        ArrayList<ResCollectionreturnItem> list = new ArrayList<ResCollectionreturnItem>();
        for (int i = 0; i < items_arr.size(); i++) {
            ResCollectionreturnItem e = new ResCollectionreturnItem();
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            e.setBusdate(entity.getBusdate());
            e.setReturndate(entity.getReturndate());
            e.setRreturndate(entity.getRreturndate());
            e.setBusuuid(uuid);
            e.setCruserid(entity.getCruserid());
            e.setCrusername(entity.getCrusername());
            e.setProcessuserid(entity.getProcessuserid());
            e.setProcessusername(entity.getProcessusername());
            e.setTusedcompanyid(entity.getTusedcompanyid());
            e.setTpartid("");
            e.setTuseduserid("");
            e.setTloc(entity.getTloc());
            e.setTlocdtl(entity.getTlocdtl());
            e.setIsreturn("1");
            list.add(e);
        }

        //先保存item数据,清除历史
        QueryWrapper<ResCollectionreturnItem> qw = new QueryWrapper<ResCollectionreturnItem>();
        String finalUuid = uuid;
        qw.and(i -> i.eq("busuuid", finalUuid));
        ResCollectionreturnItemServiceImpl.remove(qw);
        ResCollectionreturnItemServiceImpl.saveOrUpdateBatch(list);
        ResCollectionreturnServiceImpl.saveOrUpdate(entity);
        String sql3 = "update res_collectionreturn_item a,res b set b.inprocess='1',b.inprocessuuid='" + uuid + "',b.inprocesstype='" + AssetsConstant.ASSETS_BUS_TYPE_TK + "' where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql3, uuid);
        JSONObject res = new JSONObject();
        res.put("busid", uuid);
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:根据Busid查询单据
     */
    public R selectByBusid(String uuid) {
        return selectData(uuid, null);
    }

    /**
     * @Description:查询单据列表
     */
    public R selectData(String uuid, String resid) {
        JSONObject res = new JSONObject();
        String sql2 = "select " + AssetsConstant.resSqlbody + " t.*," +
                "(select name from sys_user_info where user_id=b.create_by) createusername,   " +
                "(select route_name from hrm_org_part where node_id=b.tusedcompanyid) tcompfullname,   " +
                "(select node_name from hrm_org_part where node_id=b.tusedcompanyid) tcompname,   " +
                "(select route_name from hrm_org_part where node_id=b.tpartid) tpartfullame,   " +
                "(select node_name from hrm_org_part where node_id=b.tpartid) tpartname,   " +
                "(select name from sys_user_info where user_id=b.tuseduserid) tusedusername,   " +
                //       "(select name from sys_dict_item where dr='0' and dict_item_id=b.tloc) tlocstr,   " +
                "(select route_name from hrm_org_part where node_id=b.fusedcompanyid) fcompfullname,   " +
                "(select node_name from hrm_org_part where node_id=b.fusedcompanyid) fcompname,   " +
                "(select route_name from hrm_org_part where node_id=b.fpartid) fpartfullame,   " +
                "(select node_name from hrm_org_part where node_id=b.fpartid) fpartname,   " +
                "(select name from sys_user_info where user_id=b.fuseduserid) fusedusername,   " +
                //      "(select name from sys_dict_item where dr='0' and dict_item_id=b.floc) flocstr,   " +
                "date_format(busdate,'%Y-%m-%d') busdatestr,   " +
                "date_format(returndate,'%Y-%m-%d') returndatestr,   " +
                "date_format(rreturndate,'%Y-%m-%d') rreturndatestr,   " +
                "b.*   " +
                "from res_collectionreturn_item b,res t where b.dr='0' and t.dr='0' " +
                "and t.id=b.resid   " +
                "and b.busuuid=?";
        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and resid='" + resid + "'";
        }
        Rcd rsone = db.uniqueRecord("select * from res_collectionreturn where dr='0' and busuuid=?", uuid);
        res = ConvertUtil.OtherJSONObjectToFastJSONObject(rsone.toJsonObject());
        RcdSet rs = db.query(sql2, uuid);
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(rs.toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询单据列表
     */
    public R selectList(String user_id, String statustype, String bustype,String search) {
        String sql = "select   " +
                "(select name from sys_user_info where user_id=b.create_by) createusername,   " +
                "(select route_name from hrm_org_part where node_id=b.tusedcompanyid) tcompfullname,   " +
                "(select node_name from hrm_org_part where node_id=b.tusedcompanyid) tcompname,   " +
                "(select route_name from hrm_org_part where node_id=b.tpartid) tpartfullame,   " +
                "(select node_name from hrm_org_part where node_id=b.tpartid) tpartname,   " +
                "(select name from sys_user_info where user_id=b.tuseduserid) usedusername,   " +
                //       "(select name from sys_dict_item where dr='0' and dict_item_id=b.tloc) tlocstr,   " +
                "date_format(busdate,'%Y-%m-%d') busdatestr,   " +
                "date_format(rreturndate,'%Y-%m-%d') rreturndatestr,   " +
                "date_format(returndate,'%Y-%m-%d') returndatestr,   " +
                "b.*" +
                "from res_collectionreturn b where dr='0'";
        if (ToolUtil.isNotEmpty(user_id)) {
            sql = sql + " and b.create_by='" + this.getUserId() + "'";
        }
        if (ToolUtil.isNotEmpty(bustype)) {
            sql = sql + " and b.bustype='" + bustype + "'";
        }
        if (ToolUtil.isNotEmpty(statustype)) {
            if ("finish".equals(statustype)) {
                sql = sql + " and b.status in ('" + FlowConstant.PSTATUS_FINISH + "','" + FlowConstant.PSTATUS_CANCEL + "','" + FlowConstant.PSTATUS_FINISH_NO_APPROVAL + "')";
            } else if ("inprogress".equals(statustype)) {
                sql = sql + " and b.status not in ('" + FlowConstant.PSTATUS_FINISH + "','" + FlowConstant.PSTATUS_CANCEL + "','" + FlowConstant.PSTATUS_FINISH_NO_APPROVAL + "')";
            }
        }
        if(ToolUtil.isNotEmpty(search)){
            sql = sql + " and b.name like '%" + search + "%'";
        }
        sql = sql + " order by create_time desc";
        RcdSet rs = db.query(sql);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }

}
