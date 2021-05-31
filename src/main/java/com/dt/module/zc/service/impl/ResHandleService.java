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
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.entity.ResHandle;
import com.dt.module.zc.entity.ResHandleItem;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResHandleItemService;
import com.dt.module.zc.service.IResHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResHandleService extends BaseService {

    public static String TYPE_HANDLE="handle";
    public static String TYPE_CLEAN="clean";
    public static String TYPE_TRANSFER="transfer";
    public static String TYPE_GIVE="give";

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResService ResServiceImpl;


    @Autowired
    IResHandleService ResHandleServiceImpl;

    @Autowired
    IResHandleItemService ResHandleItemServiceImpl;

    /**
     * @Description:启动流程
     */
    public R startFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {

        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResHandle> ups = new UpdateWrapper<ResHandle>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("uuid", uuid);
            ResHandleServiceImpl.update(ups);
            confirm(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消单据
     */
    public R cancel(String busid, String status) {

        return R.SUCCESS_OPER();
    }

    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        String ct = "无";
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_HANDLE);
        q.eq("busuuid",busid);
        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++){
            ResChangeItem entity=list.get(j);
            String busuuid = entity.getBusuuid();
            String resid = entity.getResid();
            String sql = "select  " +
                    " a.typename,"+
                    "  b.*  " +
                    "from res_handle_item b,res_handle a where b.busuuid =a.busuuid and b.dr='0' and b.busuuid=? and b.resid=?";
            Rcd rs = db.uniqueRecord(sql, busuuid, resid);
            if (rs != null) {
                ct = "资产处置!【类型】"+rs.getString("typename");
            }
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }

        return R.SUCCESS_OPER();
    }

    /**
     * @Description:确认单据
     */
    public R confirm(String busid, String status) {

        UpdateWrapper<ResHandle> ups = new UpdateWrapper<ResHandle>();
        ups.set("status", status);
        ups.eq("busuuid", busid);
        ResHandleServiceImpl.update(ups);
        String sql2 = "update res_handle_item a,res b set a.update_by='" + this.getUserId() + "'";
        sql2 = sql2 + ",b.ishandle='1' ";
        sql2 = sql2 + ",b.inprocess='0' ";
        sql2 = sql2 + ",b.handlebusid=a.busuuid ";
        sql2 = sql2 + ",b.handledate=now()";
        sql2 = sql2 + " where a.resid=b.id and a.busuuid=? and a.dr='0'";
        db.execute(sql2, busid);


        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResHandleItem> qw = new QueryWrapper<ResHandleItem>();
        qw.and(i -> i.eq("busuuid", busid));
        List<ResHandleItem> items =ResHandleItemServiceImpl.list(qw);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_HANDLE);
            e.setCt("资产处置");
            e.setFillct("1");
            e.setCreateBy(this.getUserId());
            e.setCdate(new Date());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:创建单据
     */
    public R save(ResHandle entity, String items) {
        String id = "";
        String uuid = "";
        if (ToolUtil.isNotEmpty(entity.getId())) {
            id = entity.getId();
            uuid = entity.getBusuuid();
        } else {
            JSONArray arr = JSONArray.parseArray(items);
            ArrayList<ResHandleItem> cols = new ArrayList<ResHandleItem>();
            uuid = assetsOperService.createUuid(AssetsConstant.UUID_HANDLE);
            entity.setBusuuid(uuid);
            entity.setProcessuserid(this.getUserId());
            entity.setProcessusername(this.getName());
            entity.setCnt(new BigDecimal(arr.size()));
            entity.setStatus(FlowConstant.PSTATUS_APPLY);
            ResHandleServiceImpl.saveOrUpdate(entity);
            for (int i = 0; i < arr.size(); i++) {
                ResHandleItem e = new ResHandleItem();
                e.setBusuuid(uuid);
                e.setResid(arr.getJSONObject(i).getString("id"));
                cols.add(e);
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.set("inprocess", "1");
                ups.set("inprocessuuid", uuid);
                ups.set("inprocesstype", AssetsConstant.ASSETS_BUS_TYPE_HANDLE);
                ups.eq("id", arr.getJSONObject(i).getString("id"));
                ResServiceImpl.update(ups);
            }
           ResHandleItemServiceImpl.saveBatch(cols);
        }
        JSONObject r = new JSONObject();
        r.put("busid", uuid);
        this.confirm(uuid,FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        return R.SUCCESS_OPER(r);
    }


    /**
     * @Description:结束流程
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
     * @Description:查询单据列表
     */
    public R selectList(String user_id, String statustype) {
        QueryWrapper<ResHandle> ew = new QueryWrapper<ResHandle>();
        if (ToolUtil.isNotEmpty(user_id)) {
            ew.eq("create_by", user_id);
        }
        if ("finish".equals(statustype)) {
            ew.in("status", FlowConstant.PSTATUS_FINISH, FlowConstant.PSTATUS_FINISH_NO_APPROVAL, FlowConstant.PSTATUS_CANCEL);
        } else if ("inprogress".equals(statustype)) {
            ew.notIn("status", FlowConstant.PSTATUS_FINISH, FlowConstant.PSTATUS_FINISH_NO_APPROVAL, FlowConstant.PSTATUS_CANCEL);
        }
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResHandleServiceImpl.list(ew));
    }
    /**
     * @Description:查询单据数据
     */
    public R selectData(String busid, String resid) {
        JSONObject res = new JSONObject();
        String sql2 = "select " + AssetsConstant.resSqlbody + " t.*," +
                "b.* " +
                "from res_handle_item b,res t where b.dr='0' and t.dr='0' " +
                "and t.id=b.resid " +
                "and b.busuuid=?";
        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and resid='" + resid + "'";
        }
        Rcd rsone = db.uniqueRecord(" select date_format(busidate,'%Y-%m-%d') busidatestr,  t.* from res_handle t where dr='0' and busuuid=?", busid);
        res = ConvertUtil.OtherJSONObjectToFastJSONObject(rsone.toJsonObject());
        RcdSet rs = db.query(sql2, busid);
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(rs.toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }



}
