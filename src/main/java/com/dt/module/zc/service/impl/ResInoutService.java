package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.zc.entity.ResInout;
import com.dt.module.zc.service.IResInoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-05-25
 */
@Service
public class ResInoutService extends BaseService {


    @Autowired
    IResInoutService ResInoutServiceImpl;

    /**
     * @Description:查询安全库存
     */
    public R selectSafetyStore() {
        String sql = "select   " +
                "  b.*,   " +
                "  t.*,   " +
                "  case when zc_cnt < downcnt   " +
                "    then '低于下限'   " +
                "  when zc_cnt > upcnt   " +
                "    then '超过上限'   " +
                "  else '未知'   " +
                "  end msg   " +
                "from (   " +
                "       select   " +
                "         class_id,   " +
                "         sum(zc_cnt) zc_cnt   " +
                "       from res   " +
                "       where dr = '0' and category = '" + CategoryEnum.CATEGORY_HC.getValue() + "'   " +
                "       group by class_id) t,   " +
                "  ct_category b   " +
                "where t.class_id = b.id and upcnt > 0 and downcnt > 0   " +
                "      and (zc_cnt < downcnt or zc_cnt > upcnt)   ";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());

    }

    public R selectHcCk(String action) {

        String sql = "select   " +
                "   (select node_name from hrm_org_part where node_id=t.compid) outbelongcompname,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) outlocstr,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) outwarehousestr,   " +
                "   (select node_name from hrm_org_part where node_id=t.belongcompid) inbelongcompname,   " +
                "   (select node_name from hrm_org_part where node_id=t.usedcompid) inusedcompname,   " +
                "   (select node_name from hrm_org_part where node_id=t.usedpartid) inpartname,   " +
                "   (select name from sys_user_info where user_id=t.useduserid) inusername,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) inlocstr,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) inwarehousestr,   " +
                "   t.*   " +
                "from res_inout t where dr='0' and action='" + action + "' order by create_time desc";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());

    }

    /**
     * @Description:查询耗材调拨数据
     */
    public R selectHcDbDataById(String id) {
        String sql = "select   " +
                "   (select node_name from hrm_org_part where node_id=t.compid) outbelongcompname,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) outlocstr,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) outwarehousestr,   " +
                "   (select node_name from hrm_org_part where node_id=t.usedcompid) inusedcompname,   " +
                "   (select node_name from hrm_org_part where node_id=t.usedpartid) inpartname,   " +
                "   (select name from sys_user_info where user_id=t.useduserid) inusername,   " +
                "   (select node_name from hrm_org_part where node_id=t.belongcompid) inbelongcompname,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) inlocstr,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) inwarehousestr,   " +
                "   t.*   " +
                "from res_inout t where dr='0' and id=?";
        Rcd rcd = db.uniqueRecord(sql, id);
        JSONObject r = ConvertUtil.OtherJSONObjectToFastJSONObject(rcd.toJsonObject());
        String sql2 = "   " +
                "select   " +
                "  (select node_name from hrm_org_part where node_id=t.belong_company_id) belongcomp_name,   " +
                "  (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) locstr,   " +
                "  (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) warehousestr,   " +
                "  (select id from ct_category where dr='0' and id=t.class_id) ctid,   " +
                "  (select model from ct_category where dr='0' and id=t.class_id) ctmodel,   " +
                "  (select name from ct_category where dr='0' and id=t.class_id) classname,   " +
                "  (select unit from ct_category where dr='0' and id=t.class_id) ctunit,   " +
                "  (select mark from ct_category where dr='0' and id=t.class_id) ctmark,   " +
                "  (select unitprice from ct_category where dr='0' and id=t.class_id) ctunitprice,   " +
                "  (select upcnt from ct_category where dr='0' and id=t.class_id) ctupcnt,   " +
                "  (select downcnt from ct_category where dr='0' and id=t.class_id) ctdowncnt,   " +
                "  (select brandmark from ct_category where dr='0' and id=t.class_id) ctbrandmark,   " +
                "  (select name from sys_dict_item where dr='0' and dict_item_id=t.supplier) supplierstr,   " +
                "t.*   " +
                "from res_inout_item t where dr='0' and uuid=?";
        r.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql2, rcd.getString("uuid")).toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询耗材出库数据
     */
    public R selectHcOutDataById(String id) {
        String sql = "select   " +
                "   (select node_name from hrm_org_part where node_id=t.compid) outbelongcompname,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) outlocstr,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) outwarehousestr,   " +
                "   (select node_name from hrm_org_part where node_id=t.usedcompid) inusedcompname,   " +
                "   (select node_name from hrm_org_part where node_id=t.usedpartid) inpartname,   " +
                "   (select name from sys_user_info where user_id=t.useduserid) inusername,   " +
                "   (select node_name from hrm_org_part where node_id=t.belongcompid) inbelongcompname,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) inlocstr,   " +
                "   (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) inwarehousestr,   " +
                "   t.*   " +
                "from res_inout t where dr='0' and id=?";
        Rcd rcd = db.uniqueRecord(sql, id);
        JSONObject r = ConvertUtil.OtherJSONObjectToFastJSONObject(rcd.toJsonObject());
        String sql2 = "   " +
                "select   " +
                "  (select node_name from hrm_org_part where node_id=t.belong_company_id) belongcomp_name,   " +
                "  (select name from sys_dict_item where dr='0' and dict_item_id=t.loc) locstr,   " +
                "  (select name from sys_dict_item where dr='0' and dict_item_id=t.warehouse) warehousestr,   " +
                "  (select id from ct_category where dr='0' and id=t.class_id) ctid,   " +
                "  (select model from ct_category where dr='0' and id=t.class_id) ctmodel,   " +
                "  (select name from ct_category where dr='0' and id=t.class_id) classname,   " +
                "  (select unit from ct_category where dr='0' and id=t.class_id) ctunit,   " +
                "  (select mark from ct_category where dr='0' and id=t.class_id) ctmark,   " +
                "  (select unitprice from ct_category where dr='0' and id=t.class_id) ctunitprice,   " +
                "  (select upcnt from ct_category where dr='0' and id=t.class_id) ctupcnt,   " +
                "  (select downcnt from ct_category where dr='0' and id=t.class_id) ctdowncnt,   " +
                "  (select brandmark from ct_category where dr='0' and id=t.class_id) ctbrandmark,   " +
                "  (select name from sys_dict_item where dr='0' and dict_item_id=t.supplier) supplierstr,   " +
                "t.*   " +
                "from res_inout_item t where dr='0' and uuid=?";
        r.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql2, rcd.getString("uuid")).toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询耗材入库数据
     */
    public R selectHcInDataById(String id) {

        ResInout in = ResInoutServiceImpl.getById(id);
        String uuid = in.getUuid();
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(in, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select   " +
                "   " +
                "   (select node_name from hrm_org_part where node_id=t.belong_company_id) belongcomp_name,   " +
                "  (select name   " +
                "   from sys_dict_item   " +
                "   where dr = '0' and dict_item_id = t.warehouse) warehousestr,   " +
                "  (select brandmark   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            brandmark,   " +
                "  (select model   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            model,   " +
                "  (select name   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            class_name,   " +
                "  (select id   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctid,   " +
                "  (select unit   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            unit,   " +
                " (select name from sys_dict_item where dr='0' and dict_item_id=t.supplier) supplierstr," +
                "  (select mark   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            remark,   " +
                "  (select unitprice   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            unitprice,   " +
                "  (select upcnt   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            upcnt,   " +
                "  date_format(buy_time,'%Y-%m-%d') buy_timestr ," +
                "  (select downcnt   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            downcnt,   " +
                "  (select name   " +
                "   from sys_dict_item   " +
                "   where dr = '0' and dict_item_id = t.loc)       locstr,   " +
                "  t.*   " +
                "from res_inout_item t where dr='0' and uuid=?";
        res.put("items", db.query(sql, uuid).toJsonArrayWithJsonObject());
        return R.SUCCESS_OPER(res);
    }


    /**
     * @Description:查询耗材统计
     */
    public R selectHcTj(String loc) {
        String sql = "select   " +
                "  (select name   " +
                "   from sys_dict_item   " +
                "   where dr = '0' and dict_item_id = t.warehouse) warehousestr,   " +
                "  (select model   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctmodel,   " +

                "  (select name   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctname,   " +

                "  (select id   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctid,   " +
                "  (select unit   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctunit,   " +
                "  (select mark   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctmark,   " +
                "  (select unitprice   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctunitprice,   " +
                "  (select upcnt   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctupcnt,   " +
                "  (select downcnt   " +
                "   from ct_category   " +
                "   where dr = '0' and id = t.class_id)            ctdowncnt,   " +
                "  (select name   " +
                "   from sys_dict_item   " +
                "   where dr = '0' and dict_item_id = t.loc)       locstr,   " +
                "  t.*   " +
                "from (   " +
                "       select   " +
                "         class_id,   " +
                "         loc,   " +
                "         warehouse,   " +
                "         sum(zc_cnt) zc_cnt   " +
                "       from res   " +
                "       where dr = '0' and category = '" + CategoryEnum.CATEGORY_HC.getValue() + "'   " +
                "       group by class_id, loc, warehouse   " +
                "       order by 1, 2, 3) t where 1=1 ";
        if (ToolUtil.isNotEmpty(loc)) {
            sql = sql + " and loc='" + loc + "'";
        }
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }
}
