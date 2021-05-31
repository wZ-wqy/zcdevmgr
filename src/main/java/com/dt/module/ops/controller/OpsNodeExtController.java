package com.dt.module.ops.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.controller.FileUpDownController;
import com.dt.module.base.entity.SysFiles;
import com.dt.module.base.service.ISysFilesService;
import com.dt.module.ops.entity.OpsNode;
import com.dt.module.ops.entity.OpsNodeDBEntity;
import com.dt.module.ops.entity.OpsNodeEntity;
import com.dt.module.ops.service.IOpsNodeService;
import com.dt.module.ops.service.impl.OpsNodeService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
 * @since 2020-01-24
 */
@Controller
@RequestMapping("/api/ops/opsNode/ext")
public class OpsNodeExtController extends BaseController {

    @Autowired
    IOpsNodeService OpsNodeServiceImpl;

    @Autowired
    ISysFilesService SysFilesServiceImpl;


    @Autowired
    OpsNodeService opsNodeService;


    /**
     * @Description:批量进行归档操作
     * @param ids
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/archAction.do")
    public R archAction(String ids) {
        JSONArray idsarr = JSONArray.parseArray(ids);
        List<String> idslist = new ArrayList<String>();
        for (int i = 0; i < idsarr.size(); i++) {
            idslist.add(idsarr.getString(i));
        }
        UpdateWrapper<OpsNode> ups = new UpdateWrapper<OpsNode>();
        ups.set("arch", "1");
        ups.in("id", idslist);
        OpsNodeServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }


    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(OpsNode entity) {

        List<String> sqls = new ArrayList<String>();
        String tid = db.getUUID();
        entity.setImportlabel(tid);
        entity.setArch("0");
        OpsNodeServiceImpl.saveOrUpdate(entity);

        if (ToolUtil.isNotEmpty(entity.getMiddleware())) {
            JSONArray mid_arr = JSONArray.parseArray(entity.getMiddleware());
            if (mid_arr.size() > 0) {
                String nid = "";
                if (ToolUtil.isEmpty(entity.getId())) {
                    // 新增
                    QueryWrapper<OpsNode> ew = new QueryWrapper<OpsNode>();
                    ew.eq("importlabel", tid);
                    OpsNode opsnode = OpsNodeServiceImpl.getOne(ew);
                    nid = opsnode.getId();
                } else {
                    nid = entity.getId();
                }
                sqls.add("delete from ops_node_item where type='middleware' and nid='" + nid + "' ");
                for (int i = 0; i < mid_arr.size(); i++) {
                    Insert me = new Insert("ops_node_item");
                    me.set("id", db.getUUID());
                    me.set("dr", 0);
                    me.setIf("nid", nid);
                    me.set("type", "middleware");
                    me.setIf("value", mid_arr.getString(i));
                    sqls.add(me.getSQL());
                }
            } else {
                if (!ToolUtil.isEmpty(entity.getId())) {
                    sqls.add("delete from ops_node_item where type='middleware' and nid='" + entity.getId() + "' ");
                }
            }

            if (sqls.size() > 0) {
                db.executeStringList(sqls);
            }
        }

        return R.SUCCESS_OPER();
    }

    @ResponseBody
    @Acl(info = " ", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList(String search) {
        return opsNodeService.selecList(search);
    }


    /**
     * @Description:获取首页数据
     */
    @ResponseBody
    @Acl(info = " ", value = Acl.ACL_USER)
    @RequestMapping(value = "/dashboard.do")
    public R dashboard() {
        JSONObject res = new JSONObject();

        String sql = "select (select count(1) from ops_node where dr='0' and arch='0' and status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online')) oscnt,"
                + "  (select count(1) from ops_node_item a,ops_node b where  b.arch='0' and b.dr='0' and a.dr='0' and a.nid=b.id and a.type='dbinstance' and b.status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online'))dbinstancecnt,"
                + "  (select sum(cnt) from (select count(cnt) cnt from (select distinct ip ,count(1) cnt from  ops_node where arch='0' and status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and dr='0' group by ip) t where cnt>1 union all select count(1) cnt from ops_node where arch='0' and status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and ip is null)end) exceptioncnt ,"
                + "  (select count(1) from ops_node a,sys_dict_item b where a.arch='0' and a.status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and a.dr='0' and a.db=b.dict_item_id and b.dict_id='sysdb' and b.dr='0') dbcnt,"
                + "  (select count(1) from ops_node a,sys_dict_item b where a.arch='0' and a.status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and a.dr='0' and a.monitor=b.dict_item_id and b.dict_id='sysmonitor' and b.dr='0' and b.name='监控中') monitorcnt,"
                + "  (select sum((length(middleware)-length(replace(middleware, ',','')) ) +1)  cnt from ops_node where arch='0' and status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and middleware<>'[]' and dr='0') midcnt";
        Rcd rcd = db.uniqueRecord(sql);
        if (rcd != null) {
            res = ConvertUtil.OtherJSONObjectToFastJSONObject((rcd.toJsonObject()));
        }
        // 操作系统
        String ossql = "select b.name,count(1) cnt from ops_node a,sys_dict_item b where a.arch='0' and a.status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and a.dr='0' and a.os=b.dict_item_id and b.dict_id='sysos'\n"
                + "group by b.name order by 2 desc";
        JSONArray os_meta_arr = new JSONArray();
        JSONArray os_data_arr = new JSONArray();
        RcdSet os_rs = db.query(ossql);
        for (int i = 0; i < os_rs.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(os_rs.getRcd(i).getString("name"));
            os_meta_arr.add(meta);
            JSONArray data = new JSONArray();
            data.add(i);
            data.add(os_rs.getRcd(i).getInteger("cnt"));
            os_data_arr.add(data);
        }
        res.put("os_chart_meta", os_meta_arr);
        res.put("os_chart_data", os_data_arr);

        // 数据库
        JSONArray db_meta_arr = new JSONArray();
        JSONArray db_data_arr = new JSONArray();
        String dbsql = "select b.name,count(1) cnt from ops_node a,sys_dict_item b where a.arch='0' and a.status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and a.dr='0' and a.db=b.dict_item_id and b.dict_id='sysdb'\n"
                + "group by b.name order by 2 desc";
        RcdSet db_rs = db.query(dbsql);
        for (int i = 0; i < db_rs.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(db_rs.getRcd(i).getString("name"));
            db_meta_arr.add(meta);
            JSONArray data = new JSONArray();
            data.add(i);
            data.add(db_rs.getRcd(i).getInteger("cnt"));
            db_data_arr.add(data);
        }
        res.put("db_chart_meta", db_meta_arr);
        res.put("db_chart_data", db_data_arr);

        // 中间件
        String midsql = "select b.code name ,count(1) cnt from ops_node c ,ops_node_item a,sys_dict_item b where c.arch='0' and c.status in (select dict_item_id from sys_dict_item where dr='0' and dict_id='sysstatus' and code='online') and c.dr='0' and c.id=a.nid and a.value=b.dict_item_id and a.dr='0' group by code order by 2 desc";
        JSONArray mid_meta_arr = new JSONArray();
        JSONArray mid_data_arr = new JSONArray();
        RcdSet mid_rs = db.query(midsql);
        for (int i = 0; i < mid_rs.size(); i++) {
            JSONArray meta = new JSONArray();
            meta.add(i);
            meta.add(mid_rs.getRcd(i).getString("name"));
            mid_meta_arr.add(meta);
            JSONArray data = new JSONArray();
            data.add(i);
            data.add(mid_rs.getRcd(i).getInteger("cnt"));
            mid_data_arr.add(data);
        }
        res.put("mid_chart_meta", mid_meta_arr);
        res.put("mid_chart_data", mid_data_arr);

        return R.SUCCESS_OPER(res);
    }

    @ResponseBody
    @Acl(info = " ", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectListImport.do")

    public R selectListImport(String id, HttpServletRequest request, HttpServletResponse response) {
        SysFiles fileobj = SysFilesServiceImpl.getById(id);

        String fileurl = fileobj.getPath();
        String filePath = FileUpDownController.getWebRootDir() + ".." + File.separatorChar + fileurl;
        R r = new R();
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setStartSheetIndex(0);
            List<OpsNodeEntity> result = ExcelImportUtil.importExcel(new File(filePath), OpsNodeEntity.class, params);
            r = opsNodeService.importOpsNodeEntitys(result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.FAILURE("导入数据异常");
        }
        return r;
    }

    @ResponseBody
    @Acl(info = " ", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectListExport.do")
    public void selectListExport(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();

        R res = opsNodeService.selecList(ps.getString("search"));

        JSONArray data = res.queryDataToJSONArray();
        List<OpsNodeEntity> data_excel = new ArrayList<OpsNodeEntity>();
        for (int i = 0; i < data.size(); i++) {
            OpsNodeEntity entity = new OpsNodeEntity();
            entity.fullEntity(data.getJSONObject(i));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("数据");
        parms.setHeaderHeight(1000);

        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, OpsNodeEntity.class, data_excel);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "node.xls";
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

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectDBListSimple.do")
    public R selectDBListSimple() {
        String sql = "select concat(dbtype,\"_\",name) dbname,id from (select name,id,"
                + "  (select name from sys_dict_item where dict_item_id=db) dbtype,"
                + "  (select count(1) from ops_node_item where dr='0' and nid=t.id and type='dbinstance') cnt"
                + "  from ops_node t where arch='0' and dr='0' and db is not null) end order by 1";

        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectDBList.do")
    public R selectDBList(String dbinstid, String nodeid) {
        return opsNodeService.selectDBList(dbinstid, nodeid);
    }

    @ResponseBody
    @Acl(info = " ", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectListDBImport.do")
    public R selectListDBImport(String id, HttpServletRequest request, HttpServletResponse response) {
        SysFiles fileobj = SysFilesServiceImpl.getById(id);
        String fileurl = fileobj.getPath();
        String filePath = FileUpDownController.getWebRootDir() + ".." + File.separatorChar + fileurl;
        R r = new R();
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setStartSheetIndex(0);
            List<OpsNodeDBEntity> result = ExcelImportUtil.importExcel(new File(filePath), OpsNodeDBEntity.class,
                    params);
            r = opsNodeService.importOpsNodeDBEntitys(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.FAILURE("导入数据异常");
        }
        return r;
    }

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectDBListExport.do")
    public void selectDBListExport(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        // TypedHashMap<String, Object> ps = (TypedHashMap<String, Object>)
        // HttpKit.getRequestParameters();

        R res = opsNodeService.selectDBList(null, null);

        JSONArray data = res.queryDataToJSONArray();
        List<OpsNodeDBEntity> data_excel = new ArrayList<OpsNodeDBEntity>();
        for (int i = 0; i < data.size(); i++) {
            OpsNodeDBEntity entity = new OpsNodeDBEntity();
            entity.fullEntity(data.getJSONObject(i));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("数据");
        parms.setHeaderHeight(1000);

        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, OpsNodeDBEntity.class, data_excel);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "db.xls";
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
