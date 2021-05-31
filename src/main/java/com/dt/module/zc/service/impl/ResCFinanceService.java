package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.entity.ResCFinance;
import com.dt.module.zc.entity.ResCFinanceItem;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.service.IResCFinanceItemService;
import com.dt.module.zc.service.IResCFinanceService;
import com.dt.module.zc.service.IResChangeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResCFinanceService extends BaseService {


    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResCFinanceService ResCFinanceServiceImpl;

    @Autowired
    IResCFinanceItemService ResCFinanceItemServiceImpl;

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
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_CGCW);
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
                JSONObject item = res_arr.getJSONObject(0);
                ct = "";
                String tbelongcompstatus = item.getString("tbelongcompstatus");
                String tbuypricestatus = item.getString("tbuypricestatus");
                String tnetworthstatus = item.getString("tnetworthstatus");
                String tresidualvaluestatus = item.getString("tresidualvaluestatus");
                String taccumulatedstatus = item.getString("taccumulatedstatus");
                String fbelongcompfullname = item.getString("fbelongcompfullname");
                String tbelongcompfullname = item.getString("tbelongcompfullname");
                String fbuyprice = item.getString("fbuyprice");
                String tbuyprice = item.getString("tbuyprice");
                String fnetworth = item.getString("fnetworth");
                String tnetworth = item.getString("tnetworth");
                String faccumulateddepreciation = item.getString("faccumulateddepreciation");
                String taccumulateddepreciation = item.getString("taccumulateddepreciation");
                String fresidualvalue = item.getString("fresidualvalue");
                String tresidualvalue = item.getString("tresidualvalue");
                if (ToolUtil.isNotEmpty(tbelongcompstatus) && "true".equals(tbelongcompstatus)) {
                    ct = ct + "【所属公司】字段由 \"" + fbelongcompfullname + "\" 变更为 \"" + tbelongcompfullname + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tbuypricestatus) && "true".equals(tbuypricestatus)) {
                    ct = ct + "【采购单价】字段由 \"" + fbuyprice + "\" 变更为 \"" + tbuyprice + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tnetworthstatus) && "true".equals(tnetworthstatus)) {
                    ct = ct + "【资产净值】字段由 \"" + fnetworth + "\" 变更为 \"" + tnetworth + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tresidualvaluestatus) && "true".equals(tresidualvaluestatus)) {
                    ct = ct + "【设置残值】字段由 \"" + fresidualvalue + "\" 变更为 \"" + tresidualvalue + "\" ;";
                }
                if (ToolUtil.isNotEmpty(taccumulatedstatus) && "true".equals(taccumulatedstatus)) {
                    ct = ct + "【累计折旧】字段由 \"" + faccumulateddepreciation + "\" 变更为 \"" + taccumulateddepreciation + "\" ;";
                }
            }
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:资产变更
     */
    public R confirm(String uuid) {
        QueryWrapper<ResCFinance> qw = new QueryWrapper<ResCFinance>();
        qw.and(i -> i.eq("busuuid", uuid));
        ResCFinance entity = ResCFinanceServiceImpl.getOne(qw);

        String sql = "update res_c_finance_item a,res b set a.fbuyprice=b.buy_price,  " +
                "  a.fbelongcomp=b.belong_company_id,  " +
                "  a.fbelongpart=b.belong_part_id,  " +
                "  a.faccumulateddepreciation=b.accumulateddepreciation,  " +
                "  a.fnetworth=b.net_worth,  " +
                "  a.fresidualvalue=b.residualvalue  " +
                " where a.resid=b.id and a.busuuid=? and a.dr='0'";
        db.execute(sql, uuid);

        String sql2 = "update res_c_finance_item a,res b set  a.update_by='" + this.getUserId() + "'";
        if ("true".equals(entity.getTbuypricestatus())) {
            sql2 = sql2 + ",b.buy_price=a.tbuyprice";
        }
        if ("true".equals(entity.getTbelongcompstatus())) {
            sql2 = sql2 + ",b.belong_company_id=a.tbelongcomp";
        }
        if ("true".equals(entity.getTbelongpartstatus())) {
            sql2 = sql2 + ",b.belong_part_id=a.tbelongpart";
        }
        if ("true".equals(entity.getTaccumulatedstatus())) {
            sql2 = sql2 + ",b.accumulateddepreciation=a.taccumulateddepreciation";
        }
        if ("true".equals(entity.getTnetworthstatus())) {
            sql2 = sql2 + ",b.net_worth=a.tnetworth";
        }
        if ("true".equals(entity.getTresidualvaluestatus())) {
            sql2 = sql2 + ",b.residualvalue=a.tresidualvalue";
        }
        sql2 = sql2 + " where a.resid=b.id and a.busuuid=? and a.dr='0'";
        db.execute(sql2, uuid);
        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResCFinanceItem> qw2 = new QueryWrapper<ResCFinanceItem>();
        qw2.and(i -> i.eq("busuuid", uuid));
        List<ResCFinanceItem> items = ResCFinanceItemServiceImpl.list(qw2);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(uuid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_CGCW);
            e.setMark("财务信息变更");
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
        String sql2 = "select " + AssetsConstant.resSqlbody + " t.* ,b.*,   " +
                "(select route_name from hrm_org_part where node_id=b.fbelongcomp) fbelongcompfullname,   " +
                "(select node_name from hrm_org_part where node_id=b.fbelongcomp) fbelongcompname,   " +
                "(select route_name from hrm_org_part where node_id=b.tbelongcomp) tbelongcompfullname,   " +
                "(select node_name from hrm_org_part where node_id=b.tbelongcomp) tbelongcompname   " +
                "from res t, res_c_finance_item b where t.id=b.resid and t.dr='0' and b.dr='0' and b.busuuid=?";
        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and resid='" + resid + "'";
        }
        RcdSet rs = db.query(sql2, uuid);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }
}
