package com.dt.module.ops.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.ops.entity.OpsNodeInfosys;
import com.dt.module.ops.entity.OpsNodeInfosysEntity;
import com.dt.module.ops.service.IOpsNodeInfosysService;
import com.dt.module.ops.service.impl.OpsNodeInfosysService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-03-05
 */
@Controller
@RequestMapping("/api/ops/opsNodeInfosys/ext")
public class OpsNodeInfosysExtController extends BaseController {


    @Autowired
    IOpsNodeInfosysService OpsNodeInfosysServiceImpl;

    @Autowired
    OpsNodeInfosysService opsNodeInfosysService;

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        return opsNodeInfosysService.selectList(search);
    }

    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(OpsNodeInfosys entity) {
        return R.SUCCESS_OPER(OpsNodeInfosysServiceImpl.saveOrUpdate(entity));
    }

    /**
     * @Description:导出Ops数据
     */
    @ResponseBody
    @Acl(info = " ", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectListExport.do")
    public void selectListExport(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        R res = opsNodeInfosysService.selectList(ps.getString("search"));
        JSONArray data = res.queryDataToJSONArray();
        List<OpsNodeInfosysEntity> data_excel = new ArrayList<OpsNodeInfosysEntity>();
        for (int i = 0; i < data.size(); i++) {
            OpsNodeInfosysEntity entity = new OpsNodeInfosysEntity();
            entity.fullEntity(data.getJSONObject(i));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("数据");
        parms.setHeaderHeight(1000);

        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, OpsNodeInfosysEntity.class, data_excel);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "sysinfo.xls";
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

