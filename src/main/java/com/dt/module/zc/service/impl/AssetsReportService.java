package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class AssetsReportService extends BaseService {

    /**
     * @Description:公司部门汇总表
     * @param catid
     */
    public R queryPartUsedReport(String catid) {

        String sql = "select   " +
                "  node_id        part_id,   " +
                "  route_name     part_fullname,   " +
                "  node_name      part_name,   " +
                "  case when b.cnt is null   " +
                "    then 0   " +
                "  else b.cnt end zc_cnt   " +
                "from (select *   " +
                "      from hrm_org_part   " +
                "      where org_id = '1' and dr = '0') a left join (select   " +
                "                                                      part_id,   " +
                "                                                      count(1) cnt   " +
                "                                                    from <#RES#> r   " +
                "                                                    where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'    " +
                "                                                    group by part_id) b on a.node_id = b.part_id   " +
                "union all   " +
                "select   " +
                "  '-1'         part_id,   " +
                "  '未设置组织或组织异常' part_fullname,   " +
                "  '未设置组织或组织异常' part_name,   " +
                "  count(1)     zc_cnt   " +
                "from <#RES#> r   " +
                "where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and part_id not in (select node_id   " +
                "                      from hrm_org_part   " +
                "                      where org_id = '1' and dr = '0') or part_id is null   ";

        String rssql = "";
        if (ToolUtil.isNotEmpty(catid)) {
            rssql = sql.replaceAll("<#RES#>", "(select a.*,b.id catid from res a,ct_category b where a.class_id=b.id and b.id='" + catid + "'  ) ");
        } else {
            rssql = sql.replaceAll("<#RES#>", "res");
        }
        return R.SUCCESS_OPER(db.query("select * from (" + rssql + ")tab order by part_fullname ").toJsonArrayWithJsonObject());
    }

    /**
     * @Description:资产分类表
     */
    public R queryCatReport() {
        String sql = "select   " +
                "  (select route_name   " +
                "   from ct_category   " +
                "   where id = t.class_id) catname,   " +
                "  (select b.name   " +
                "   from ct_category a,ct_category_root b   " +
                "   where a.id = t.class_id and a.root=b.id) catrootname,   " +
                "    (select code   " +
                "   from ct_category   " +
                "   where id = t.class_id) catcode,   " +
                "  t.*   " +
                "from (   " +
                "       select   " +
                "         class_id,   " +
                "         count(1) cnt   " +
                "       from res   " +
                "       where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'    " +
                "       group by class_id   " +
                "     ) t order by 1,2";

        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:资产分类使用表
     */
    public R queryCatUsedReport() {

        String sql = "select   " +
                "  t.*,   " +
                "  (select code   " +
                "   from sys_dict_item   " +
                "   where dict_item_id = t.recycle) code,   " +
                "    (select route_name   " +
                "   from ct_category i   " +
                "   where i.id=t.class_id) catname,   " +
                "     (select ii.name   " +
                "   from ct_category i,ct_category_root ii   " +
                "   where i.id=t.class_id and i.root=ii.id) catrootname   " +
                "from (   " +
                "       select   " +
                "         class_id,   " +
                "         recycle,   " +
                "         count(1) cnt   " +
                "       from res t    " +
                "       where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'    " +
                "       group by class_id, recycle) t   ";

        RcdSet rs = db.query(sql);

        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
        for (int i = 0; i < rs.size(); i++) {
            String class_id = rs.getRcd(i).getString("class_id");
            JSONObject obj = null;
            if (map.containsKey(class_id)) {
                obj = map.get(class_id);
            } else {
                obj = new JSONObject();
                obj.put("catname", rs.getRcd(i).getString("catname"));
                obj.put("catrootname", rs.getRcd(i).getString("catrootname"));
                obj.put(AssetsRecycleEnum.RECYCLE_INUSE.getValue(), "0");
                obj.put(AssetsRecycleEnum.RECYCLE_ALLOCATION.getValue(), "0");
                obj.put(AssetsRecycleEnum.RECYCLE_BORROW.getValue(), "0");
                obj.put(AssetsRecycleEnum.RECYCLE_IDLE.getValue(), "0");
                obj.put(AssetsRecycleEnum.RECYCLE_REPAIR.getValue(), "0");
                obj.put(AssetsRecycleEnum.RECYCLE_SCRAP.getValue(), "0");
                obj.put(AssetsRecycleEnum.RECYCLE_STOPUSE.getValue(), "0");
            }
            obj.put(rs.getRcd(i).getString("code"), rs.getRcd(i).getString("cnt"));
            map.put(class_id, obj);
        }
        JSONArray arr = new JSONArray();
        for (String key : map.keySet()) {
            arr.add(map.get(key));
        }
        return R.SUCCESS_OPER(arr);
    }


    /**
     * @Description:资产清理单
     */
    public R queryCleaninglistReport(String catid) {

        return R.SUCCESS_OPER();
    }


    /**
     * @Description:维保到期
     */
    public R queryWbExpiredReport(String day) {
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and wbout_date<= date_add(curdate(), INTERVAL " + day + " DAY)";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }
    /**
     * @Description:报废到期
     */
    public R queryZcBfReport(String day) {
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and bfout_date<= date_add(curdate(), INTERVAL " + day + " DAY)";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }


    /**
     * @Description:员工资产使用表
     */
    public R queryEmployeeUsedReport() {
        String sql = " select   " +
                "                (select name from sys_user_info where user_id=t.used_userid) username,   " +
                "                (select mail from sys_user_info where user_id=t.used_userid) usermail,   " +
                "                (select tel from sys_user_info where user_id=t.used_userid) usertel,   " +
                "                (select route_name  from hrm_org_employee a,hrm_org_part b,sys_user_info c where c.user_id=t.used_userid and a.node_id=b.node_id and c.empl_id=a.empl_id) part_fullname,   " +
                "                t.*   " +
                "                        from (   " +
                "                                select   " +
                "                                used_userid,   " +
                "                                count(1) cnt   " +
                "                                from res a   " +
                "                                where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'   " +
                "                                group by used_userid   " +
                "                        ) t";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());

    }
}
