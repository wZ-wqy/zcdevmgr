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
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.entity.ResLoanreturn;
import com.dt.module.zc.entity.ResLoanreturnItem;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResLoanreturnItemService;
import com.dt.module.zc.service.IResLoanreturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ResLoanreturnService extends BaseService {

    public static String BUS_STATUS_JY = "JY";
    public static String BUS_STATUS_GH = "GH";


    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResLoanreturnService ResLoanreturnServiceImpl;

    @Autowired
    IResLoanreturnItemService ResLoanreturnItemServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    @Lazy
    AssetsFlowService assetsFlowService;


    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid,String type) {
        if(AssetsConstant.ASSETS_BUS_TYPE_JY.equals(type)){
            QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
            q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_JY);
            q.eq("busuuid",busid);
            List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
            for(int j=0;j<list.size();j++) {
                ResChangeItem entity = list.get(j);
                String busuuid = entity.getBusuuid();
                QueryWrapper<ResLoanreturnItem> ew = new QueryWrapper<ResLoanreturnItem>();
                ew.and(i -> i.eq("busuuid", busuuid).eq("resid", entity.getResid()));
                ResLoanreturnItem item = ResLoanreturnItemServiceImpl.getOne(ew);
                String ct = "无";
                if (item != null) {
                    String recycle = item.getFrecycle();
                    String recyclestr = AssetsRecycleEnum.parseCode(recycle);
                    ct = "【状态】由 \"" + recyclestr + "\" 变更为 \"借用\"";
                }
                entity.setFillct("1");
                entity.setCt(ct);
                ResChangeItemServiceImpl.saveOrUpdate(entity);
            }
        }else if(AssetsConstant.ASSETS_BUS_TYPE_GH.equals(type)){
            QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
            q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_GH);
            q.eq("busuuid",busid);
            List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
            for(int j=0;j<list.size();j++) {
                ResChangeItem entity = list.get(j);
                String busuuid = entity.getBusuuid();
                QueryWrapper<ResLoanreturnItem> ew = new QueryWrapper<ResLoanreturnItem>();
                ew.and(i -> i.eq("busuuid", busuuid).eq("resid", entity.getResid()));
                ResLoanreturnItem item = ResLoanreturnItemServiceImpl.getOne(ew);
                String ct = "无";
                if (item != null) {
                    String recycle = item.getFrecycle();
                    String recyclestr = AssetsRecycleEnum.parseCode(recycle);
                    ct = "【状态】由 \"借用\" 变更为 \"" + recyclestr + "\"";
                }
                entity.setFillct("1");
                entity.setCt(ct);
                ResChangeItemServiceImpl.saveOrUpdate(entity);
            }
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:启动借用流程
     */
    public R startLyFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResLoanreturn> ups = new UpdateWrapper<ResLoanreturn>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("busuuid", uuid);
            ResLoanreturnServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResLoanreturn> ups = new UpdateWrapper<ResLoanreturn>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("busuuid", uuid);
            ResLoanreturnServiceImpl.update(ups);
            confirmJy(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:取消借用
     */
    public R cancelJy(String busid, String status) {

        //更新RES数据
        String sql2 = "update res_loanreturn_item a,res b set " +
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
        UpdateWrapper<ResLoanreturn> ups = new UpdateWrapper<ResLoanreturn>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResLoanreturnServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:确认借用
     */
    public R confirmJy(String busid, String status) {
        //保存变更前数据
        UpdateWrapper<ResLoanreturn> ups = new UpdateWrapper<ResLoanreturn>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResLoanreturnServiceImpl.update(ups);

        String sql = "update res_loanreturn_item a,res b set " +
                "   a.frecycle=b.recycle " +
                "   where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, busid);

        //更新数据
        String sql2 = "update res_loanreturn_item a,res b set " +
                "b.recycle='" + AssetsRecycleEnum.RECYCLE_BORROW.getValue() + "'," +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='', " +
                "b.uuidjy=a.busuuid " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);

        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResLoanreturnItem> ew = new QueryWrapper<ResLoanreturnItem>();
        ew.and(i -> i.eq("busuuid", busid));
        List<ResLoanreturnItem> items = ResLoanreturnItemServiceImpl.list(ew);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_JY);
            e.setFillct("1");
            e.setCdate(new Date());
            e.setCreateBy(this.getUserId());
            e.setCt("资产借用,借用人:" + items.get(i).getLrusername());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid,AssetsConstant.ASSETS_BUS_TYPE_JY);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:结束借用流程
     */
    public R finishJyFlow(String busid, String status) {
        if (FlowConstant.PSTATUS_DTL_FAILED.equals(status)) {
            return cancelJy(busid, FlowConstant.PSTATUS_DTL_FAILED);
        } else if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(status)) {
            return confirmJy(busid, FlowConstant.PSTATUS_DTL_SUCCESS);
        } else {
            return R.FAILURE_NO_DATA();
        }
    }

    /**
     * @Description:生成资产借用单据
     */
    public R saveJy(ResLoanreturn entity, String items) {

        String id = entity.getId();
        String uuid = "";
        entity.setBusstatus(this.BUS_STATUS_JY);

        //获取UUID
        if (ToolUtil.isNotEmpty(id)) {
            uuid = entity.getBusuuid();
            if (!FlowConstant.PSTATUS_APPLY.equals(entity.getStatus())) {
                return R.FAILURE("当前状态不允许修改");
            }
            //可能数据有变动，先解锁当前的数据,后面会重新加锁
            String sql2 = "update res_loanreturn_item a,res b set b.inprocess='0',b.inprocessuuid='',b.inprocesstype='' where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
            db.execute(sql2, uuid);
        } else {
            uuid = assetsOperService.createUuid(AssetsConstant.UUID_JY);
            //当前方案设置结束流程
            entity.setIsreturn("0");
            entity.setBusuuid(uuid);
            entity.setStatus(FlowConstant.PSTATUS_APPLY);
        }
        JSONArray items_arr = JSONArray.parseArray(items);
        ArrayList<ResLoanreturnItem> list = new ArrayList<ResLoanreturnItem>();
        for (int i = 0; i < items_arr.size(); i++) {
            ResLoanreturnItem e = new ResLoanreturnItem();
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            e.setBusdate(entity.getBusdate());
            e.setReturndate(entity.getReturndate());
            e.setBusuuid(uuid);
            e.setLruserid(entity.getLruserid());
            e.setLrusername(entity.getLrusername());
            e.setTusedcompanyid(entity.getTusedcompanyid());
            e.setTpartid(entity.getTpartid());
            e.setTloc(entity.getTloc());
            e.setTlocdtl(entity.getTlocdtl());
            e.setReturndate(entity.getReturndate());
            e.setIsreturn("0");
            list.add(e);
        }

        //先保存item数据,清除历史
        QueryWrapper<ResLoanreturnItem> qw = new QueryWrapper<ResLoanreturnItem>();
        String finalUuid = uuid;
        qw.and(i -> i.eq("busuuid", finalUuid));
        ResLoanreturnItemServiceImpl.remove(qw);
        ResLoanreturnItemServiceImpl.saveOrUpdateBatch(list);
        ResLoanreturnServiceImpl.saveOrUpdate(entity);
        //锁定单据中的数据
        String sql2 = "update res_loanreturn_item a,res b set b.inprocess='1',b.inprocessuuid='" + uuid + "',b.inprocesstype='" + AssetsConstant.ASSETS_BUS_TYPE_JY + "' where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, uuid);

        JSONObject res = new JSONObject();
        res.put("busid", uuid);
        return R.SUCCESS_OPER(res);
    }


    /**
     * @Description:资产归还确认
     */
    public R confirmGh(String uuid) {
        //更新数据
        String sql2 = "update res_loanreturn_item a,res b set   " +
                "b.recycle=a.frecycle," +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='', " +
                "b.uuidjy='' " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, uuid);
        String sql3 = " update res_loanreturn_item a,res_loanreturn_item b set " +
                " a.returnuuid=b.busuuid," +
                " a.rreturndate=b.rreturndate," +
                " a.isreturn='1'" +
                " where a.resid=b.resid and b.busuuid=? and b.dr='0'";
        db.execute(sql3, uuid);
        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResLoanreturnItem> ew = new QueryWrapper<ResLoanreturnItem>();
        ew.and(i -> i.eq("busuuid", uuid));
        List<ResLoanreturnItem> items = ResLoanreturnItemServiceImpl.list(ew);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(uuid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_GH);
            e.setFillct("0");
            e.setCdate(new Date());
            e.setMark("资产归还");
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(uuid,AssetsConstant.ASSETS_BUS_TYPE_GH);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:生成资产归还单据
     */
    public R saveGh(String id, String rreturndate, String rmark,String rprocessuserid, String rprocessusername) {
        ResLoanreturn entity = ResLoanreturnServiceImpl.getById(id);
        String status = entity.getStatus();
        String busstatus = entity.getBusstatus();
        if (BUS_STATUS_GH.equals(busstatus) || FlowConstant.PSTATUS_DTL_SUCCESS.equals(status) || FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL.equals(status)) {
        } else {
            return R.FAILURE("当前单据办理状态不可做归还操作");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(rreturndate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        entity.setBusstatus(BUS_STATUS_GH);
        entity.setRprocessuserid(rprocessuserid);
        entity.setRprocessusername(rprocessusername);
        entity.setRreturndate(date);
        entity.setIsreturn("1");
        entity.setRmark(rmark);
        ResLoanreturnServiceImpl.saveOrUpdate(entity);

        UpdateWrapper<ResLoanreturnItem> ups = new UpdateWrapper<ResLoanreturnItem>();
        ups.set("isreturn", "1");
        ups.set("rreturndate", rreturndate);
        ups.eq("busuuid", entity.getBusuuid());
        ResLoanreturnItemServiceImpl.update(ups);
        confirmGh(entity.getBusuuid());
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:查询借用归还单据
     */
    public R selectByBusid(String uuid) {
        return selectData(uuid, null);
    }

    /**
     * @Description:查询单据数据
     */
    public R selectData(String uuid, String resid) {
        JSONObject res = new JSONObject();
        String sql2 = "select " + AssetsConstant.resSqlbody + " t.*," +
                "(select name from sys_user_info where user_id=b.create_by) createusername, " +
                "date_format(busdate,'%Y-%m-%d') busdatestr, " +
                "date_format(returndate,'%Y-%m-%d') returndatestr, " +
                "date_format(rreturndate,'%Y-%m-%d') rreturndatestr, " +
                "(select route_name from hrm_org_employee aa,hrm_org_part bb where aa.node_id=bb.node_id and empl_id=(select empl_id from sys_user_info where user_id=b.lruserid) limit 1 ) lruserorginfo," +
                "b.* " +
                "from res_loanreturn_item b,res t where b.dr='0' and t.dr='0' " +
                "and t.id=b.resid " +
                "and b.busuuid=?";
        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and resid='" + resid + "'";
        }
        Rcd rsone = db.uniqueRecord("select * from res_loanreturn where dr='0' and busuuid=?", uuid);
        res = ConvertUtil.OtherJSONObjectToFastJSONObject(rsone.toJsonObject());
        RcdSet rs = db.query(sql2, uuid);
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(rs.toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询单据列表数据
     */
    public R selectList(String user_id, String statustype, String bustype,String search) {
        String sql = "select " +
                "(select name from sys_user_info where user_id=b.create_by) createusername,  " +
                "date_format(busdate,'%Y-%m-%d') busdatestr,  " +
                "date_format(rreturndate,'%Y-%m-%d') rreturndatestr,  " +
                "date_format(returndate,'%Y-%m-%d') returndatestr,  " +
                "(select route_name from hrm_org_employee aa,hrm_org_part bb where aa.node_id=bb.node_id and empl_id=(select empl_id from sys_user_info where user_id=b.lruserid) limit 1 ) lruserorginfo," +
                "b.*" +
                "from res_loanreturn b where dr='0' ";
        if (ToolUtil.isNotEmpty(user_id)) {
            sql = sql + " and b.create_by='" + this.getUserId() + "'";
        }
        if (ToolUtil.isNotEmpty(bustype)) {
            sql = sql + " and b.busstatus='" + bustype + "'";
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
