package com.dt.module.zc.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: lank
 * @date: Dec 2, 2019 2:31:20 PM
 * @Description:
 */
@Controller
@RequestMapping("/api/zc/report")
public class AssetsReportController extends BaseController {
    @Autowired
    AssetsReportService zcReportService;


    /**
     * @Description:查询使用部门统计
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryUsedPartReport.do")
    public R queryUsedPartReport() {
        String sql = "\n" +
                "select\n" +
                "part_id,\n" +
                "case\n" +
                "when part_id is  null\n" +
                "then '未分配'\n" +
                "else part_fullname end part_fullname,\n" +
                "case\n" +
                "when part_id is  null\n" +
                "then '未分配'\n" +
                "else part_name end part_name,\n" +
                "zc_cnt\n" +
                "from (\n" +
                "select\n" +
                "(select route_name from hrm_org_part where node_id=t.part_id) part_fullname,\n" +
                "(select node_name from hrm_org_part where node_id=t.part_id) part_name,\n" +
                "t.*\n" +
                "from (select part_id,count(1) zc_cnt from res where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' group by part_id) t\n" +
                "order by 1) end";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }


    /**
     * @Description:使用公司统计
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryUsedCompReport.do")
    public R queryUsedCompReport() {
        String sql = "select\n" +
                "case\n" +
                "when used_company_id is  null\n" +
                "then '未分配'\n" +
                "else comp_fullname end comp_fullname,\n" +
                "case\n" +
                "when used_company_id is  null\n" +
                "then '未分配'\n" +
                "else comp_name end comp_name,\n" +
                "zc_cnt\n" +
                "from (\n" +
                "select\n" +
                "(select route_name from hrm_org_part where node_id=t.used_company_id) comp_fullname,\n" +
                "(select node_name from hrm_org_part where node_id=t.used_company_id) comp_name,\n" +
                "t.*\n" +
                "from (select used_company_id,count(1) zc_cnt from res where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' group by used_company_id) t\n" +
                "order by 1) end\n";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:所属公司统计
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryBelongCompReport.do")
    @Transactional
    public R queryBelongCompReport() {
        String sql = "select\n" +
                "case\n" +
                "when belong_company_id is  null\n" +
                "then '未分配'\n" +
                "else belongcomp_fullname end belongcomp_fullname,\n" +
                "case\n" +
                "when belong_company_id is  null\n" +
                "then '未分配'\n" +
                "else belongcomp_name end belongcomp_name,\n" +
                "zc_cnt\n" +
                "from (\n" +
                "select\n" +
                "(select route_name from hrm_org_part where node_id=t.belong_company_id) belongcomp_fullname,\n" +
                "(select node_name from hrm_org_part where node_id=t.belong_company_id)  belongcomp_name,\n" +
                "t.*\n" +
                "from (select belong_company_id,count(1) zc_cnt from res where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' group by belong_company_id) t\n" +
                "order by 1) end\n";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }



    /**
     * @Description:资产总值汇总表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryZcTotalAssets.do")
    public R queryZcTotalAssets() {
        String sql = "   " +
                "select   " +
                "  (select route_name from ct_category where dr='0' and id=t.class_id) classname,   " +
                "   t.*   " +
                "from (   " +
                "  select   " +
                "   " +
                "    class_id,   " +
                "    sum(zc_cnt)                           tcnt,   " +
                "    sum(buy_price * zc_cnt)               tbuyprice,   " +
                "    sum(net_worth * zc_cnt)               tnetworth,   " +
                "    sum(accumulateddepreciation * zc_cnt) taccumulateddepreciation   " +
                "  from res   " +
                "  where dr = '0' and recycle<>'scrap' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'   " +
                "  group by class_id   " +
                ")t order by 1";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }


    /**
     * @Description:资产类目汇总表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryAssetsCategory.do")
    public R queryAssetsCategory() {
        String sql = "\n" +
                "select\n" +
                "(select a.name from ct_category_root a,ct_category b where a.id=b.root and b.id=t.class_id) classrootname,\n" +
                "(select route_name from ct_category where  dr='0' and id=t.class_id) classfullname,\n" +
                "t.*\n" +
                "from (select class_id,count(1) zc_cnt from res where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' group by class_id) t\n" +
                "order by 2";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:公司部门汇总表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryPartUsedReport.do")
    public R queryPartUsedReport(String catid) {
        return zcReportService.queryPartUsedReport(catid);
    }

    /**
     * @Description:资产类型汇总表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryCatReport.do")
    public R queryUsedReport() {
        return zcReportService.queryCatReport();
    }

    /**
     * @Description:类型使用汇总表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryCatUsedReport.do")
    public R queryCatUsedeport() {

        return zcReportService.queryCatUsedReport();
    }


    /**
     * @Description:查询资产清理
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryCleaninglistReport.do")
    public R queryCleaninglistReport() {

        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询维保过期表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryWbExpiredReport.do")
    public R queryWbExpredReport(String day) {
        return zcReportService.queryWbExpiredReport(day);
    }

    /**
     * @Description:查询维保过期表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryEmployeeUsedReport.do")
    public R queryEmployeeUsedReport() {
        return zcReportService.queryEmployeeUsedReport();
    }

    /**
     * @Description:查询维保过期表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryEmployeeUsedByUser.do")
    public R queryEmployeeUsedByUser() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String userid = ps.getString("userid");
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where dr='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "'";
        if (ToolUtil.isNotEmpty(userid)) {
            sql = sql + " and used_userid='" + userid + "'";
        } else {
            sql = sql + " and used_userid is null";
        }
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:查询资产退库过期时间
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryTkZcExpire.do")
    public R queryTkZcExpire(String day) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String sql = "select " + AssetsConstant.resSqlbody + "b.busdate,b.crusername,b.processuserid,b.processusername,b.returndate,b.isreturn,t.* from res t ,res_collectionreturn_item b where t.recycle='" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "' and t.dr='0'   " +
                " and b.isreturn='0' and t.id=b.resid and b.dr='0' " +
                " and returndate<= date_add(curdate(), INTERVAL " + day + " DAY)" +
                " order by returndate ";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:查询归还调用
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryGhZcExpire.do")
    public R queryGhZcExpire(String day) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String sql = "select " + AssetsConstant.resSqlbody + "b.busdate, b.lrusername,b.returndate,b.isreturn," +
                " (select route_name from hrm_org_employee aa,hrm_org_part bb where aa.node_id=bb.node_id and empl_id=(select empl_id from sys_user_info where user_id=b.lruserid) limit 1 ) lruserorginfo," +
                " t.* from res t ,res_loanreturn_item b where b.isreturn='0' and t.recycle='" + AssetsRecycleEnum.RECYCLE_BORROW.getValue() + "' and t.dr='0'   " +
                " and t.id=b.resid and b.dr='0' " +
                " and b.returndate<= date_add(curdate(), INTERVAL " + day + " DAY)" +
                " order by b.returndate ";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:查询报废报表
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryZcBfReport.do")
    public R queryZcBfReport() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String sql = "select " + AssetsConstant.resSqlbody +
                " t.* from res t where t.dr='0' and t.recycle='" + AssetsRecycleEnum.RECYCLE_SCRAP.getValue() + "'";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }


}