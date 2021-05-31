package com.dt.module.zc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.entity.ResResidual;
import com.dt.module.zc.entity.ResResidualItem;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResResidualItemService;
import com.dt.module.zc.service.IResResidualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author lank
 * @since 2020-08-04
 */
@Service
public class ResResidualService extends BaseService {


    public static String ITEMCHECKSTATUS_INIT = "init";
    public static String ITEMCHECKSTATUS_SUCCESS = "success";
    public static String ITEMCHECKSTATUS_FAILED = "failed";

    public static String CKSTATUS_INIT = "init";
    public static String CKSTATUS_SUCCESS = "success";
    public static String CKSTATUS_FAILED = "failed";

    public static String STATUS_FAILED = "failed";
    public static String STATUS_SUCCESS = "success";
    public static String STATUS_WAIT = "wait";
    public static String STATUS_CANCEL = "cancel";

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResResidualItemService ResResidualItemServiceImpl;

    @Autowired
    IResResidualService ResResidualServiceImpl;


    /**
     * @Description:资产折旧确认
     */
    public R confirm(String uuid) {
        //保存更新前的数据
        String sql = " update res_residual_item a,res b set   " +
                "   a.buyprice=b.buy_price  " +
                " , a.bnetworth=b.net_worth  " +
                " , a.baccumulateddepreciation=b.accumulateddepreciation  " +
                "   where a.resid=b.id and a.uuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, uuid);

        QueryWrapper<ResResidualItem> ew = new QueryWrapper<ResResidualItem>();
        ew.eq("uuid", uuid);
        List<ResResidualItem> list = ResResidualItemServiceImpl.list(ew);

        List<Res> list2 = new ArrayList<Res>();
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        for (int i = 0; i < list.size(); i++) {
            ResResidualItem item = list.get(i);
            UpdateWrapper<Res> resups = new UpdateWrapper<Res>();
            resups.set("net_worth", item.getAnetworth());
            resups.setSql("accumulateddepreciation=accumulateddepreciation+" + item.getLossprice());
            resups.setSql("lastdepreciationdate=now()");
            resups.eq("id", item.getResid());
            ResServiceImpl.update(resups);
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(uuid);
            e.setResid(item.getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_ZJ);
            e.setMark("资产折旧");
            cols.add(e);
        }

        UpdateWrapper<ResResidual> ups = new UpdateWrapper<ResResidual>();
        ups.set("status", ResResidualService.STATUS_SUCCESS);
        ups.eq("uuid", uuid);
        ResResidualServiceImpl.update(ups);
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(uuid);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_ZJ);
        q.eq("busuuid",busid);
        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++) {
            ResChangeItem entity = list.get(j);
            String ct = "无";
            String busuuid = entity.getBusuuid();
            String resid = entity.getResid();
            String sql = "select " + AssetsConstant.resSqlbody + " t.* , item.*,t.uuid zcuuid from res t,res_residual_item item where item.dr='0' and t.id=item.resid and item.uuid=? and item.resid=? ";
            Rcd rs = db.uniqueRecord(sql, busuuid, resid);
            if (rs != null) {
                String bnetworth = rs.getString("bnetworth");
                String anetworth = rs.getString("anetworth");
                String lossprice = rs.getString("lossprice");
                String buyprice = rs.getString("buyprice");
                String baccumulateddepreciation = rs.getString("baccumulateddepreciation");
                ct = "【资产净值】字段由 \"" + bnetworth + "\" 变更为 \"" + anetworth + "\";";
                ct = ct + "采购单价:" + buyprice + ";变更前累计折旧:" + baccumulateddepreciation + ";本次折旧价:" + lossprice;
            }
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }

        return R.SUCCESS_OPER();
    }


}
