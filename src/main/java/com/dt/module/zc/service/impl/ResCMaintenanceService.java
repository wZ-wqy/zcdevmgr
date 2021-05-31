package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.entity.ResCMaintenance;
import com.dt.module.zc.entity.ResCMaintenanceItem;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.service.IResCMaintenanceItemService;
import com.dt.module.zc.service.IResCMaintenanceService;
import com.dt.module.zc.service.IResChangeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResCMaintenanceService extends BaseService {

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResCMaintenanceService ResCMaintenanceServiceImpl;

    @Autowired
    IResCMaintenanceItemService ResCMaintenanceItemServiceImpl;

    /**
     * @Description:根据变更ID查询变更数据
     */
    public R selectByBusid(String uuid) {
        return selectData(uuid, null);
    }


    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_CGWB);
        q.eq("busuuid",busid);
        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++){
            ResChangeItem entity=list.get(j);
            String ct = "无";
            String busuuid = entity.getBusuuid();
            String resid = entity.getResid();
            R res = this.selectData(busuuid, resid);
            JSONArray res_arr = res.queryDataToJSONArray();
            if (res_arr.size() == 1) {
                ct = "";
                JSONObject item = res_arr.getJSONObject(0);

                String twbstatus = item.getString("twbstatus");
                String fwbstr = item.getString("fwbstr");
                String twbstr = item.getString("twbstr");

                String twbsupplierstatus = item.getString("twbsupplierstatus");
                String fwbsupplierstr = item.getString("fwbsupplierstr");
                String twbsupplierstr = item.getString("twbsupplierstr");

                String twboutdatestatus = item.getString("twboutdatestatus");
                String fwboutdatestr = item.getString("fwboutdatestr");
                String twboutdatestr = item.getString("twboutdatestr");

                String twbctstatus = item.getString("twbctstatus");
                String fwbct = item.getString("fwbct");
                String twbct = item.getString("twbct");

                String twbautostatus = item.getString("twbautostatus");
                String twbautostr = item.getString("twbautostr");
                String fwbautostr = item.getString("fwbautostr");

                if (ToolUtil.isNotEmpty(twbstatus) && "true".equals(twbstatus)) {
                    ct = ct + "【维保状态】字段由 \"" + fwbstr + "\" 变更为 \"" + twbstr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(twbsupplierstatus) && "true".equals(twbsupplierstatus)) {
                    ct = ct + "【维保商】字段由 \"" + fwbsupplierstr + "\" 变更为 \"" + twbsupplierstr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(twboutdatestatus) && "true".equals(twboutdatestatus)) {
                    ct = ct + "【脱保日期】字段由 \"" + fwboutdatestr + "\" 变更为 \"" + twboutdatestr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(twbctstatus) && "true".equals(twbctstatus)) {
                    ct = ct + "【维保说明】字段由 \"" + fwbct + "\" 变更为 \"" + twbct + "\" ;";
                }
                if (ToolUtil.isNotEmpty(twbautostatus) && "true".equals(twbautostatus)) {
                    ct = ct + "【维保计算】字段由 \"" + fwbautostr + "\" 变更为 \"" + twbautostr + "\" ;";
                }
            }
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:资产变更确认
     */
    public R confirm(String uuid) {
        QueryWrapper<ResCMaintenance> qw = new QueryWrapper<ResCMaintenance>();
        qw.and(i -> i.eq("busuuid", uuid));
        ResCMaintenance entity = ResCMaintenanceServiceImpl.getOne(qw);

        String sql = "update res_c_maintenance_item a,res b set a.fwb=b.wb,  " +
                "  a.fwbsupplier=b.wbsupplier,  " +
                "  a.fwbauto=b.wb_auto,  " +
                "  a.fwbct=b.wbct,  " +
                "  a.fwboutdate=b.wbout_date  " +
                " where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, uuid);

        String sql2 = "update res_c_maintenance_item b,res a set a.update_by='" + this.getUserId() + "'";
        if ("true".equals(entity.getTwbstatus())) {
            sql2 = sql2 + ",a.wb=b.twb";
        }
        if ("true".equals(entity.getTwbsupplierstatus())) {
            sql2 = sql2 + ",a.wbsupplier=b.twbsupplier";
        }
        if ("true".equals(entity.getTwbautostatus())) {
            sql2 = sql2 + ",a.wb_auto=b.twbauto";
        }
        if ("true".equals(entity.getTwbctstatus())) {
            sql2 = sql2 + ",a.wbct=b.twbct";
        }
        if ("true".equals(entity.getTwboutdatestatus())) {
            sql2 = sql2 + ",a.wbout_date=b.twboutdate";
        }
        sql2 = sql2 + " where a.id=b.resid and b.busuuid=? and b.dr='0' and a.dr='0'";

        db.execute(sql2, uuid);
        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResCMaintenanceItem> qw2 = new QueryWrapper<ResCMaintenanceItem>();
        qw2.and(i -> i.eq("busuuid", uuid));
        List<ResCMaintenanceItem> items = ResCMaintenanceItemServiceImpl.list(qw2);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(uuid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_CGWB);
            e.setMark("维保信息变更");
            e.setFillct("0");
            e.setCdate(new Date());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(uuid);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询变更数据
     */
    public R selectData(String uuid, String resid) {
        String sql2 = "select " + AssetsConstant.resSqlbody + " t.*,b.*,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fwb)fwbstr," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.twb)twbstr," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fwbauto)fwbautostr," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.twbauto)twbautostr," +
                "date_format(b.twboutdate,'%Y-%m-%d') twboutdatestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.twbsupplier) twbsupplierstr,   " +
                "date_format(b.fwboutdate,'%Y-%m-%d') fwboutdatestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fwbsupplier) fwbsupplierstr   " +
                "from res_c_maintenance_item b ,res t where t.id=b.resid and t.dr='0' and b.dr='0' and b.busuuid=?";
        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and resid='" + resid + "'";
        }
        RcdSet rs = db.query(sql2, uuid);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }
}
