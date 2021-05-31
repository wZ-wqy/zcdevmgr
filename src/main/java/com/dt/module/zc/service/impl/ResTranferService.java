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
import com.dt.module.zc.entity.ResTranfer;
import com.dt.module.zc.entity.ResTranferItem;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResTranferItemService;
import com.dt.module.zc.service.IResTranferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResTranferService extends BaseService {


    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    public static String TRANFERSTATUS_WAIT="0";
    public static String TRANFERSTATUS_FINISH="1";

    public static String CAT_INTERDEP="interdep";//跨部门
    public static String CAT_CROSSDEP="crossdep";//部门内部

    public static String BUSTYPE="self";


    @Autowired
    IResTranferItemService ResTranferItemServiceImpl;

    @Autowired
    IResTranferService ResTranferServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    @Lazy
    AssetsFlowService assetsFlowService;

    /**
     * @Description:启动资产转移流程
     */
    public R startFlow(String pinst, String busid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResTranfer> ups = new UpdateWrapper<ResTranfer>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("busid", busid);
            ResTranferServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResTranfer> ups = new UpdateWrapper<ResTranfer>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("busid", busid);
            ResTranferServiceImpl.update(ups);
            confirm(busid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消资产转移流程
     * 取消领用,流程失败，或者取消
     */
    public R cancel(String busid, String status) {

        String sql2 = "update res_tranfer_item a,res b set " +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
                "where a.resid=b.id and a.busid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);
        UpdateWrapper<ResTranfer> ups = new UpdateWrapper<ResTranfer>();
        ups.set("status", status);
        ups.eq("busid", busid);
        ResTranferServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:填充变更数据
     */
    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_ZY);
        q.eq("busuuid",busid);
        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++) {
            ResChangeItem entity = list.get(j);
            String ct = "";
            String busuuid = entity.getBusuuid();
            String resid = entity.getResid();
            entity.setFillct("1");
            entity.setCt(ct);
            // ResChangeItemServiceImpl.saveOrUpdate(entity);
        }

        return R.SUCCESS_OPER();
    }
    /**
     * @Description:确认资产转移流程
     * 取消领用,流程失败，或者取消
     */
    public R confirm(String busid, String status) {
        //保存变更前数据
        UpdateWrapper<ResTranfer> ups = new UpdateWrapper<ResTranfer>();
        ups.set("status", status);
        ups.eq("busid", busid);
        ResTranferServiceImpl.update(ups);

        String sql = "update res_tranfer_item a,res b set " +
                "   a.frecycle=b.recycle " +
                "   where a.resid=b.id and a.busid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, busid);

        //更新数据
        String sql2 = "update res_tranfer_item a,res b set " +
                "b.recycle='" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "'," +
                "b.used_userid=a.tuseduserid,"+
                "b.part_id=a.tusedpartid,"+
                "b.loc=a.tloc,"+
                "b.used_company_id=tusedcompid,"+
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
                "where a.resid=b.id and a.busid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);

        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResTranferItem> ew = new QueryWrapper<ResTranferItem>();
        ew.and(i -> i.eq("busid", busid));
        List<ResTranferItem> items = ResTranferItemServiceImpl.list(ew);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_ZY);
            e.setFillct("1");
            e.setCdate(new Date());
            e.setCreateBy(this.getUserId());
            e.setCt("发生资产转移");
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:结束资产转移流程
     */
    public R finishFlow(String busid, String status) {
        if (FlowConstant.PSTATUS_DTL_FAILED.equals(status)) {
            return cancel(busid, FlowConstant.PSTATUS_DTL_FAILED);
        } else if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(status)) {
            return confirm(busid, FlowConstant.PSTATUS_DTL_SUCCESS);
        } else {
            return R.FAILURE_NO_DATA();
        }
    }

    /**
     * @Description:保存资产转移单据
     */
    public R save(ResTranfer entity, String items) {
        String id = entity.getId();
        String busid = "";
        //获取UUID
        entity.setTranferstatus("0");
        if (ToolUtil.isNotEmpty(id)) {
            //未申请过流程修改，流程失败后修改。
            busid = entity.getBusid();
            if (!FlowConstant.PSTATUS_APPLY.equals(entity.getStatus())) {
                return R.FAILURE("当前状态不允许修改");
            }
            //
            //可能数据有变动，先解锁当前的数据,后面会重新加锁
            String sql2 = "update res_tranfer_item a,res b set b.inprocess='0',b.inprocessuuid='',b.inprocesstype='' where a.resid=b.id and a.busid=? and b.dr='0' and a.dr='0'";
            QueryWrapper<ResTranferItem> qw = new QueryWrapper<ResTranferItem>();
            String finalUuid = busid;
            qw.and(i -> i.eq("busid", finalUuid));
            ResTranferItemServiceImpl.remove(qw);


            db.execute(sql2, busid);
        } else {
            busid = assetsOperService.createUuid(AssetsConstant.UUID_ZY);
            //当前方案设置结束流程
            entity.setBusid(busid);
            //entity.setFlowapply("0");
            if(ToolUtil.isEmpty(entity.getBusdate())){
                entity.setBusdate(new Date());
            }
            entity.setStatus(FlowConstant.PSTATUS_APPLY);


        }

        entity.setTuseduserid(entity.getReceiveruserid());
        entity.setTusedusername(entity.getReceiverusername());

        JSONArray items_arr = JSONArray.parseArray(items);
        ArrayList<ResTranferItem> list = new ArrayList<ResTranferItem>();
        for (int i = 0; i < items_arr.size(); i++) {
            ResTranferItem e = new ResTranferItem();
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            e.setBusid(entity.getBusid());
            e.setTranfercnt(entity.getTranfercnt());
            e.setTranferstatus(entity.getTranferstatus());
            e.setFbelongcompid(entity.getFbelongcompid());
            e.setFbelongcompname(entity.getFbelongcompname());
            e.setFusedcompid(entity.getFusedcompid());
            e.setFusedcompname(entity.getFusedcompname());
            e.setFusedpartid(entity.getFusedpartid());
            e.setFusedpartname(entity.getFusedpartname());
            e.setFuseduserid(entity.getFuseduserid());
            e.setFusedusername(entity.getFusedusername());
            e.setFloc(entity.getFloc());
            e.setFlocname(entity.getFlocname());
            e.setFlocdtl(entity.getFlocdtl());
            e.setTusedcompid(entity.getTusedcompid());
            e.setTusedcompname(entity.getTusedcompname());
            e.setTusedpartid(entity.getTusedpartid());
            e.setTusedpartname(entity.getTusedpartname());
            e.setTuseduserid(entity.getTuseduserid());
            e.setTusedusername(entity.getTusedusername());
            e.setTloc(entity.getTloc());
            e.setTlocname(entity.getTlocname());
            e.setTlocdtl(entity.getTlocdtl());
            list.add(e);
        }
        System.out.println(entity.getId());
        System.out.println(entity.getTransfercatid());
        System.out.println(entity.getTransfercatname());
        entity.setTranfercnt(new BigDecimal(list.size()));
        //先保存item数据,清除历史
        ResTranferItemServiceImpl.saveOrUpdateBatch(list);
        ResTranferServiceImpl.saveOrUpdate(entity);
        //锁定单据中的数据
        String sql2 = "update res_tranfer_item a,res b set b.inprocess='1',b.inprocessuuid='" + busid + "',b.inprocesstype='" + AssetsConstant.ASSETS_BUS_TYPE_ZY + "' where a.resid=b.id and a.busid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);

        JSONObject res = new JSONObject();
        res.put("busid", busid);
        return R.SUCCESS_OPER(res);

    }

    /**
     * @Description:根据业务ID查询资产转移单据
     */
    public R selectByBusid(String busid) {
        return selectData(busid, null);
    }

    /**
     * @Description:查询资产转移数据
     */
    public R selectData(String busid, String resid){
        JSONObject res = new JSONObject();
        String sql2 = "select " + AssetsConstant.resSqlbody + " t.*" +
                " " +
                "from res_tranfer_item b,res t where b.dr='0' and t.dr='0' " +
                "and t.id=b.resid " +
                "and b.busid=?" ;
        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and b.resid='" + resid + "'";
        }
        Rcd rsone = db.uniqueRecord("select t.*, date_format(receivedate,'%Y-%m-%d') receivedatestr  from res_tranfer t where dr='0' and busid=?", busid);
        res = ConvertUtil.OtherJSONObjectToFastJSONObject(rsone.toJsonObject());
        RcdSet rs = db.query(sql2, busid);
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(rs.toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }

}
