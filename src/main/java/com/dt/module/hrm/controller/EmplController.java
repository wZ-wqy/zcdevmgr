package com.dt.module.hrm.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.controller.FileUpDownController;
import com.dt.module.base.entity.SysFiles;
import com.dt.module.base.service.ISysFilesService;
import com.dt.module.hrm.entity.HrmOrgEmployeeEntity;
import com.dt.module.hrm.service.impl.EmplService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class EmplController extends BaseController {

    @Autowired
    ISysFilesService SysFilesServiceImpl;


    @Autowired
    private EmplService emplService;

    @RequestMapping("/hrm/downloadEmployeeImportTpl.do")
    @ResponseBody
    @Acl(info = "下载模版", value = Acl.ACL_USER)
    public void downloadEmployeeImportTpl(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        List<HrmOrgEmployeeEntity> data_excel = new ArrayList<HrmOrgEmployeeEntity>();
        ExportParams parms = new ExportParams();
        parms.setSheetName("人员");
        parms.setHeaderHeight(1000);
        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, HrmOrgEmployeeEntity.class, data_excel);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "employee.xls";
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

    @RequestMapping("/hrm/employeeBatchAdd.do")
    @ResponseBody
    @Acl(info = "批量添加人员", value = Acl.ACL_USER)
    public R employeeBatchAdd(String id) {
        SysFiles fileobj = SysFilesServiceImpl.getById(id);
        String fileurl = fileobj.getPath();
        String filePath = FileUpDownController.getWebRootDir() + ".." + File.separatorChar + fileurl;
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setTitleRows(0);
        params.setStartSheetIndex(0);
        List<HrmOrgEmployeeEntity> result = ExcelImportUtil.importExcel(new File(filePath), HrmOrgEmployeeEntity.class, params);
        System.out.println(filePath + result.size());
        int s = 0;
        for (int i = 0; i < result.size(); i++) {
            TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
            ps.put("name", result.get(i).getName());
            ps.put("tel", result.get(i).getTel());
            ps.put("hrmstatus","online");
            JSONObject e = new JSONObject();
            e.put("node_id", result.get(i).getOrgid().trim());
            JSONArray node = new JSONArray();
            node.add(e);
            ps.put("nodes", node.toJSONString());
            R r = emplService.addEmployee(ps);
            if (r.isFailed()) {
                s++;
            }
        }
        if (s > 0) {
            return R.SUCCESS("导入失败数量:" + s);
        }
        return R.SUCCESS_OPER();
    }

    @RequestMapping("/hrm/employeeAdd.do")
    @ResponseBody
    @Acl(info = "添加人员")
    public R employeeAdd() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return emplService.addEmployee(ps);
    }

    @RequestMapping("/hrm/employeeUpdate.do")
    @ResponseBody
    @Acl(info = "更新人员")
    public R employeeUpdate() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        return emplService.updateEmployee(ps);
    }

    @RequestMapping("/hrm/employeeQueryList.do")
    @ResponseBody
    @Acl(info = "查询人员")
    public R employeeQueryList() {
        return emplService.queryEmplList(HttpKit.getRequestParameters());
    }

    @RequestMapping("/hrm/employeeQueryById.do")
    @ResponseBody
    @Acl(info = "查询人员信息")
    public R employeeQueryById(String empl_id) {
        return emplService.queryEmplById(empl_id);
    }

    @RequestMapping("/hrm/employeeDelete.do")
    @ResponseBody
    @Acl(info = "删除人员")
    @Transactional
    public R employeeDelete(String empl_id) throws IOException {
        return emplService.delEmployee(empl_id);
    }

    @RequestMapping("/hrm/logoffEmployee.do")
    @ResponseBody
    @Acl(info = "注销人员")
    @Transactional
    public R logoffEmployee(String empl_id) throws IOException {
        return emplService.logoffEmployee(empl_id);
    }
}
