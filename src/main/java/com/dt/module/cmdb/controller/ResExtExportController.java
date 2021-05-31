package com.dt.module.cmdb.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONArray;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.cmdb.entity.DictItemEntity;
import com.dt.module.cmdb.entity.ResEntity;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lank
 * @date: Oct 21, 2019 7:28:06 PM
 * @Description:
 */
@Controller
@RequestMapping("/api/base/res")
public class ResExtExportController extends BaseController {

    @Autowired
    AssetsOperService assetsOperService;


    /**
     * @Description:导出数据字典数据
     */
    @RequestMapping("/exportDictItems.do")
    @Acl(value = Acl.ACL_USER)
    public void exportDictItems(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        String sql = "select * from (   " +
                "  select   " +
                "    b.name,   " +
                "    a.name item_name   " +
                "  from sys_dict_item a, sys_dict b   " +
                "  where a.dict_id = b.dict_id and a.dr = '0' and b.dr = '0'   " +
                "  union all   " +
                "  select   " +
                "    '资产类型明细'   name,   " +
                "    route_name item_name   " +
                "  from ct_category   " +
                "  where root = '" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and dr='0'   " +
                "  union all   " +
                "  select   " +
                "    '公司' name,   " +
                "    route_name   " +
                "  from hrm_org_part   " +
                "  where type = 'comp' and dr='0'   " +
                "  union all   " +
                "  select   " +
                "    '部门' name,   " +
                "    route_name   " +
                "  from hrm_org_part   " +
                "  where type = 'part' and dr='0'   " +
                ") tab order by 1   ";

        RcdSet rs = db.query(sql);

        List<DictItemEntity> data_excel = new ArrayList<DictItemEntity>();
        for (int i = 0; i < rs.size(); i++) {
            DictItemEntity entity = new DictItemEntity();
            entity.fullResEntity(ConvertUtil.OtherJSONObjectToFastJSONObject(rs.getRcd(i).toJsonObject()));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("数据字典项");
        parms.setHeaderHeight(1000);

        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, DictItemEntity.class, data_excel);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "dictItem.xls";
        filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        try {
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description:导出资产
     */
    @RequestMapping("/exportAllRes.do")
    @Acl(value = Acl.ACL_USER)
    public void exportAllRes(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        R res = assetsOperService.queryAssetsData(ps);
        System.out.println(res);
        JSONArray data = res.queryDataToJSONArray();
        System.out.println(data);
        List<ResEntity> data_excel = new ArrayList<ResEntity>();

        for (int i = 0; i < data.size(); i++) {
            ResEntity entity = new ResEntity();
            entity.fullResEntity(data.getJSONObject(i));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("数据");
        parms.setHeaderHeight(1000);
        System.out.println(data_excel);
        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, ResEntity.class, data_excel);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "data.xls";
        filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        try {
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description:导出资产
     */
    @RequestMapping("/exportServerData.do")
    @Acl(value = Acl.ACL_USER)
    public void exportServerData(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();

        R res = assetsOperService.queryAssetsData(ps);

        JSONArray data = res.queryDataToJSONArray();
        List<ResEntity> data_excel = new ArrayList<ResEntity>();
        for (int i = 0; i < data.size(); i++) {
            ResEntity entity = new ResEntity();
            entity.fullResEntity(data.getJSONObject(i));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("数据");
        parms.setHeaderHeight(1000);

        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, ResEntity.class, data_excel);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "data.xls";
        filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        try {
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
