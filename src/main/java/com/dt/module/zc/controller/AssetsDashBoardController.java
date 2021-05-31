package com.dt.module.zc.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zc/dashboard/ext")
public class AssetsDashBoardController extends BaseController {


    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/report.do")
    public R dashboard() {
        String sql = "select   " +
                "  (select count(1) from sys_process_data where dr='0' and bustype='LY' and pstatus='running') lywaitcnt,   " +
                "  (select count(1) from sys_process_data where dr='0' and bustype='JY' and pstatus='running') jywaitcnt,   " +
                "  (select count(1) from sys_process_data where dr='0' and bustype='DB' and pstatus='running') dbwaitcnt,   " +
                "  (select count(1) from res_repair where dr='0' and fstatus='wait') bxcnt,   " +
                "  (select count(1) from res where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "') zccnt,   " +
                "  (select sum(net_worth) from res where dr='0' and recycle<>'scrap' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "') zcnetworth   ";

        JSONObject res = ConvertUtil.OtherJSONObjectToFastJSONObject(db.uniqueRecord(sql).toJsonObject());
        //资产状态
        String sql3 = "select   " +
                "  tab.*,   " +
                "  case when name2 is null   " +
                "    then '未知'   " +
                "  else name2 end name   " +
                "from (   " +
                "       select   " +
                "         t.*,   " +
                "         (select name   " +
                "          from sys_dict_item   " +
                "          where dict_item_id = t.recycle) name2   " +
                "       from (   " +
                "              select   " +
                "                recycle,   " +
                "                count(1) cnt   " +
                "              from res    " +
                "              where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'   " +
                "              group by recycle) t  order by 2 desc   " +
                "     ) tab";
        RcdSet s3 = db.query(sql3);
        JSONArray meta_arr = new JSONArray();
        JSONArray data_arr = new JSONArray();
        for (int i = 0; i < s3.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(s3.getRcd(i).getString("name"));
            meta_arr.add(meta);

            JSONArray data = new JSONArray();
            data.add(i);
            data.add(s3.getRcd(i).getInteger("cnt"));
            data_arr.add(data);

        }
        res.put("chart_meta", meta_arr);
        res.put("chart_data", data_arr);

        //部门
        String sql4 = "select   " +
                "  tab.*,   " +
                "  case when name2 is null   " +
                "    then '未设置'   " +
                "  else name2 end name   " +
                "from (   " +
                "       select   " +
                "         t.*,   " +
                "         (select node_name   " +
                "          from hrm_org_part   " +
                "          where node_id = t.part_id) name2   " +
                "       from (   " +
                "              select   " +
                "                part_id,   " +
                "                count(1) cnt   " +
                "              from res   " +
                "              where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'   " +
                "              group by part_id) t) tab order by 2 desc";
        RcdSet s4 = db.query(sql4);
        JSONArray partmeta_arr = new JSONArray();
        JSONArray partdata_arr = new JSONArray();
        for (int i = 0; i < s4.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(s4.getRcd(i).getString("name"));
            partmeta_arr.add(meta);

            JSONArray data = new JSONArray();
            data.add(i);
            data.add(s4.getRcd(i).getInteger("cnt"));
            partdata_arr.add(data);

        }
        res.put("part_chart_meta", partmeta_arr);
        res.put("part_chart_data", partdata_arr);


        //资产分类
        String sql5 = "select   " +
                "  tab.*,   " +
                "  case when name2 is null   " +
                "    then '未设置'   " +
                "  else name2 end name   " +
                "from (   " +
                "       select   " +
                "         t.*,   " +
                "         (select  name   " +
                "          from ct_category   " +
                "          where id = t.class_id) name2   " +
                "       from (   " +
                "              select   " +
                "                class_id,   " +
                "                count(1) cnt   " +
                "              from res   " +
                "              where dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'   " +
                "              group by class_id) t) tab order by 2 desc";
        RcdSet s5 = db.query(sql5);
        JSONArray catmeta_arr = new JSONArray();
        JSONArray catdata_arr = new JSONArray();
        for (int i = 0; i < s5.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(s5.getRcd(i).getString("name"));
            catmeta_arr.add(meta);

            JSONArray data = new JSONArray();
            data.add(i);
            data.add(s5.getRcd(i).getInteger("cnt"));
            catdata_arr.add(data);

        }
        res.put("cat_chart_meta", catmeta_arr);
        res.put("cat_chart_data", catdata_arr);

        return R.SUCCESS_OPER();
    }
    /**
     * @Description:首页统计数据
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getstatistics.do")
    public R getstatistics() {

        String sql="select \n" +
                "(select count(1) from res_repair_item  a, res_repair b  where a.busuuid =b.fuuid  and b.fstatus in ('wait') ) bxcnt,\n" +
                "(select count(1) from res where ishandle='0' and  dr='0' and category ='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' ) assetscnt,\n" +
                "(select count(1) from res where ishandle='1' and  dr='0' and category ='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' ) assetshandlecnt,\n" +
                "(select count(1) from res where ishandle='0' and  dr='0' and category ='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' and recycle='"+ AssetsRecycleEnum.RECYCLE_INUSE.getValue()+"' ) assetsinusecnt,\n" +
                "(select count(1) from res where ishandle='0' and  dr='0' and category ='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' and recycle='"+ AssetsRecycleEnum.RECYCLE_IDLE.getValue()+"' ) assetsidlecnt,\n" +
                "(select sum(net_worth) from res where ishandle='0' and  dr='0' and category ='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' and  recycle<>'"+ AssetsRecycleEnum.RECYCLE_SCRAP.getValue()+"' ) assetsnetworth,\n" +
                "(select count(1) from res where ishandle='0' and dr='0' and category ='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' and  recycle='"+ AssetsRecycleEnum.RECYCLE_SCRAP.getValue()+"') assetsscrapcnt\n" ;
        return R.SUCCESS_OPER(ConvertUtil.OtherJSONObjectToFastJSONObject(db.uniqueRecord(sql).toJsonObject()));
    }

    /**
     * @Description:资产分类统计
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getAssetsCategory.do")
    public R getAssetsCategory() {

        String sql5 = "select   " +
                "  tab.*,   " +
                "  case when name2 is null   " +
                "    then '未设置'   " +
                "  else name2 end name   " +
                "from (   " +
                "       select   " +
                "         t.*,   " +
                "         (select  name   " +
                "          from ct_category   " +
                "          where id = t.class_id) name2   " +
                "       from (   " +
                "              select   " +
                "                class_id,   " +
                "                count(1) cnt   " +
                "              from res   " +
                "              where ishandle='0' and dr = '0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'   " +
                "              group by class_id) t) tab order by 2 desc";
        RcdSet s5 = db.query(sql5);
        JSONArray catmeta_arr = new JSONArray();
        JSONArray catdata_arr = new JSONArray();
        for (int i = 0; i < s5.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(s5.getRcd(i).getString("name"));
            catmeta_arr.add(meta);

            JSONArray data = new JSONArray();
            data.add(i);
            data.add(s5.getRcd(i).getInteger("cnt"));
            catdata_arr.add(data);

        }
        JSONObject res=new JSONObject();
        res.put("cat_chart_meta", catmeta_arr);
        res.put("cat_chart_data", catdata_arr);
      return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:状态统计
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getAssetsRecycle.do")
    public R getAssetsRecycle(String belongcompid) {

        String sql="select recycle name,sum(cnt) cnt,sum(net_worth)  networth from ( \n" +
                "select 'borrow' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select 'allocation' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select 'repair' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select 'inuse' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select 'stopuse' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select 'idle' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select 'scrap' recycle, 0  cnt,0 net_worth \n" +
                "union all \n" +
                "select recycle ,count(1) cnt ,sum(net_worth) networth from res where dr='0' and ishandle='0' and category ='"+CategoryEnum.CATEGORY_ASSETS.getValue()+"'\n" +
                "group by recycle )t group by recycle order by 2 desc";
        return R.SUCCESS_OPER(ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql).toJsonArrayWithJsonObject()));
    }


}
