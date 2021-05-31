package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.entity.ResInventory;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-05-14
 */
@Service
public class ResInventoryService extends BaseService {

    public static String INVENTORY_STATAUS_CANCEL = "cancel";
    public static String INVENTORY_STATAUS_WAIT = "wait";
    public static String INVENTORY_STATAUS_DOING = "doing";
    public static String INVENTORY_STATAUS_FINISH = "finish";

    public static String INVENTORY_ITEM_STATAUS_WAIT = "wait";
    public static String INVENTORY_ITEM_STATAUS_FINISH_PLUS = "finish_plus";
    public static String INVENTORY_ITEM_STATAUS_FINISH_LOSS = "finish_loss";
    public static String INVENTORY_ITEM_STATAUS_FINISH_DIFF = "finish_diff";
    public static String INVENTORY_ITEM_STATAUS_FINISH_NORMAL = "finish_normal";


    public static String INVENTORY_ITEM_ACTION_SYNC = "1";
    public static String INVENTORY_ITEM_ACTION_NOSYNC = "0";


    /**
     * @Description:资产盘点范围
     */
    public R setInventoryRange(ResInventory obj, String category) {

        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where dr='0' and category='" + category + "'";
        if (ToolUtil.isNotEmpty(obj.getBelongcomp())) {
            sql = sql + " and t.belong_company_id='" + obj.getBelongcomp() + "' ";
        }
        if (ToolUtil.isNotEmpty(obj.getUsedcomp())) {
            sql = sql + " and t.used_company_id='" + obj.getUsedcomp() + "' ";
        }
        if (ToolUtil.isNotEmpty(obj.getUsedpart())) {
            String partdata = obj.getUsedpartdata();
            JSONArray partdataarr = JSONArray.parseArray(partdata);
            String tmp = "";
            for (int i = 0; i < partdataarr.size(); i++) {
                tmp = tmp + "'" + partdataarr.getJSONObject(i).getString("partid") + "',";
            }
            tmp = tmp + "'0'";
            sql = sql + " and t.part_id in (" + tmp + ")";
        }
        if (ToolUtil.isNotEmpty(obj.getRescat())) {
            String catdata = obj.getUsedpartdata();
            JSONArray catdataarr = JSONArray.parseArray(catdata);
            String tmp = "";
            for (int i = 0; i < catdataarr.size(); i++) {
                tmp = tmp + "'" + catdataarr.getJSONObject(i).getString("id") + "',";
            }
            tmp = tmp + "'0'";
            sql = sql + " and t.class_id in (" + tmp + ")";
        }
        if (ToolUtil.isNotEmpty(obj.getArea())) {
            String area = obj.getAreadata();
            JSONArray areaarr = JSONArray.parseArray(area);
            String tmp = "";
            for (int i = 0; i < areaarr.size(); i++) {
                tmp = tmp + "'" + areaarr.getJSONObject(i).getString("dict_item_id") + "',";
            }
            tmp = tmp + "'0'";
            sql = sql + " and t.loc in (" + tmp + ")";
        }

        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }


}
