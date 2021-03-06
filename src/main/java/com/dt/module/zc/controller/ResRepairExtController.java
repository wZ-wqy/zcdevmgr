package com.dt.module.zc.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.entity.ResEntity;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.zc.entity.ResRepair;
import com.dt.module.zc.entity.ResRepairFile;
import com.dt.module.zc.entity.ResRepairItem;
import com.dt.module.zc.service.IResRepairFileService;
import com.dt.module.zc.service.IResRepairItemService;
import com.dt.module.zc.service.IResRepairService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import com.google.zxing.WriterException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.asm.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author lank
 * @since 2020-04-19
 */
@Controller
@RequestMapping("/api/zc/resRepair/ext")
public class ResRepairExtController extends BaseController {

    @Autowired
    IResRepairFileService ResRepairFileServiceImpl;


    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResRepairItemService ResRepairItemServiceImpl;


    @Autowired
    IResRepairService ResRepairServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    @ResponseBody
    @Acl(info = "???????????????,????????????", value = Acl.ACL_USER)
    @RequestMapping(value = "/cancellation.do")
    public R cancellation(String id) {
        ResRepair dbobj = ResRepairServiceImpl.getById(id);
        if (AssetsConstant.BX_STATUS_UNDERREPAIR.equals(dbobj.getFstatus())) {

            //????????????
            ResRepair e = new ResRepair();
            e.setId(id);
            e.setFstatus(AssetsConstant.BX_STATUS_CANCEL);
            ResRepairServiceImpl.saveOrUpdate(e);

            //?????????????????????????????????
            UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
            ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
            ups.setSql("recycle=prerecycle");
            ResServiceImpl.update(ups);

            //??????????????????
            QueryWrapper<ResRepairItem> ew = new QueryWrapper<ResRepairItem>();
            ew.and(i -> i.eq("repairid", id));
            ResRepairItemServiceImpl.remove(ew);


        } else {
            return R.FAILURE("???????????????????????????");
        }
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:??????????????????
     */
    @ResponseBody
    @Acl(info = "???????????????,????????????", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResRepair entity, String items, String files) {
        String status = entity.getFstatus();
        String id = entity.getId();

        JSONArray arr = JSONArray.parseArray(items);
        if (ToolUtil.isNotEmpty(id)) {
            //??????????????????
            //??????????????????????????????????????????????????????????????????
            ResRepair dbobj = ResRepairServiceImpl.getById(entity.getId());
            if (AssetsConstant.BX_STATUS_FINSH.equals(dbobj.getFstatus())) {
                return R.FAILURE("???????????????????????????");
            }
            //???????????????????????????,???????????????
            if (AssetsConstant.BX_STATUS_FINSH.equals(status)) {
                //?????????????????????????????????
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
                ups.setSql("recycle=prerecycle");
                ResServiceImpl.update(ups);
            }
            ResRepairServiceImpl.saveOrUpdate(entity);
        } else {
            //??????????????????
            ArrayList<ResRepairItem> cols = new ArrayList<ResRepairItem>();
            String uuid = assetsOperService.createUuid(AssetsConstant.UUID_BX);
            entity.setFuuid(uuid);
            ResRepairServiceImpl.saveOrUpdate(entity);
            QueryWrapper<ResRepair> ew = new QueryWrapper<ResRepair>();
            ew.and(i -> i.eq("fuuid", uuid));
            ResRepair dbobj = ResRepairServiceImpl.getOne(ew);
            id = dbobj.getId();
            //???????????????????????????
            for (int i = 0; i < arr.size(); i++) {
                ResRepairItem e = new ResRepairItem();
                e.setRepairid(id);
                e.setBusuuid(uuid);
                e.setResid(arr.getJSONObject(i).getString("id"));
                cols.add(e);
            }
            ResRepairItemServiceImpl.saveOrUpdateBatch(cols);
            if (!AssetsConstant.BX_STATUS_FINSH.equals(status)) {
                //??????????????????,?????????????????????
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
                ups.setSql("prerecycle=recycle");
                ups.set("recycle", AssetsRecycleEnum.RECYCLE_REPAIR.getValue());
                ResServiceImpl.update(ups);
            }
        }

        //??????????????????
        QueryWrapper<ResRepairFile> fq = new QueryWrapper<ResRepairFile>();
        String finalId = id;
        fq.and(i -> i.eq("repiarid", finalId));
        ResRepairFileServiceImpl.remove(fq);
        //????????????????????????
        if (ToolUtil.isNotEmpty(files)) {
            ArrayList<ResRepairFile> flist = new ArrayList<ResRepairFile>();
            String[] arrfiles = files.split("#");
            for (int i = 0; i < arrfiles.length; i++) {
                ResRepairFile e = new ResRepairFile();
                e.setRepiarid(id);
                e.setFileid(arrfiles[i]);
                if (arrfiles[i].length() > 6) {
                    flist.add(e);
                }
            }
            if (flist.size() > 0) {
                ResRepairFileServiceImpl.saveBatch(flist);
            }
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:??????????????????
     */
    @ResponseBody
    @Acl(info = "??????Id??????", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {

        JSONObject res;
        ResRepair bill = ResRepairServiceImpl.getById(id);
        res = JSONObject.parseObject(JSON.toJSONString(bill, SerializerFeature.WriteDateUseDateFormat));
        QueryWrapper<ResRepairFile> ew = new QueryWrapper<ResRepairFile>();
        ew.and(i -> i.eq("repiarid", id));
        List<ResRepairFile> list = ResRepairFileServiceImpl.list(ew);
        res.put("files", JSONArray.parseArray(JSON.toJSONString(list, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t,res_repair_item b where  t.id=b.resid and b.dr='0' and b.repairid='" + id + "'";
        res.put("items", db.query(sql).toJsonArrayWithJsonObject());
        return R.SUCCESS_OPER(res);
    }


    /**
     * @Description:??????????????????
     */
    @ResponseBody
    @Acl(info = "??????Id??????", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResRepair obj = ResRepairServiceImpl.getById(id);
        if (AssetsConstant.BX_STATUS_UNDERREPAIR.equals(obj.getFstatus())) {
            return R.FAILURE("???????????????????????????");
        } else {
            QueryWrapper<ResRepairItem> ew = new QueryWrapper<ResRepairItem>();
            ew.and(i -> i.eq("repairid", id));
            ResRepairItemServiceImpl.remove(ew);
            return R.SUCCESS_OPER(ResRepairServiceImpl.removeById(id));
        }

    }

    /**
     * @Description:??????????????????
     */
    @ResponseBody
    @Acl(info = "????????????,?????????", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<ResRepair> ew = new QueryWrapper<ResRepair>();
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResRepairServiceImpl.list(ew));

    }

    /**
     * @Description:????????????????????????
     */
    @ResponseBody
    @Acl(info = "????????????,?????????", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectMyList.do")
    public R selectMyList(String statustype) {

        QueryWrapper<ResRepair> ew = new QueryWrapper<ResRepair>();
        ew.and(i -> i.eq("create_by", this.getUserId()));
        if (ToolUtil.isNotEmpty(statustype)) {
            if ("inprogress".equals(statustype)) {
                ew.notIn("fstatus","finish","cancel");
            } else if("finish".equals(statustype)){
                ew.in("fstatus","finish","cancel");
            }else {

            }
        }
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResRepairServiceImpl.list(ew));

    }

    /**
     * @Description:????????????????????????
     */
    @ResponseBody
    @Acl(info = "????????????,?????????", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectMyDataPage.do")
    public R selectMyDataPage(String statuscode, String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10") String pageSize, @RequestParam(value = "pageIndex", required = true, defaultValue = "1") String pageIndex) {
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        QueryWrapper<ResRepair> ew = new QueryWrapper<ResRepair>();
        ew.and(i -> i.eq("create_by", this.getUserId()));
        if (ToolUtil.isNotEmpty(statuscode)) {
            if ("doing".equals(statuscode)) {
                ew.and(i -> i.eq("fstatus", "wait"));
            } else {
                ew.and(i -> i.ne("fstatus", "wait"));
            }
        }
        ew.orderByDesc("create_time");
        IPage<ResRepair> pdata = ResRepairServiceImpl.page(new Page<ResRepair>(pageindex, pagesize), ew);
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", pdata.getTotal());
        retrunObject.put("success", true);
        retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
        retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        return R.clearAttachDirect(retrunObject);
    }


    /**
     * @Description:????????????
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/finish.do")
    public R finish(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResRepair obj = ResRepairServiceImpl.getById(id);
        if (!AssetsConstant.BX_STATUS_UNDERREPAIR.equals(obj.getFstatus())) {
            return R.FAILURE("???????????????????????????");
        } else {
            //????????????
            ResRepair e = new ResRepair();
            e.setId(id);
            e.setFstatus(AssetsConstant.BX_STATUS_FINSH);
            ResRepairServiceImpl.saveOrUpdate(e);

            //?????????????????????????????????
            UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
            ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
            ups.setSql("recycle=prerecycle");
            ResServiceImpl.update(ups);

            //????????????
            return R.SUCCESS_OPER();

        }

    }


    /**
     * @Description:????????????
     */
    @RequestMapping("/printData.do")
    @Acl(value = Acl.ACL_USER)
    public void exportServerData(HttpServletRequest request, HttpServletResponse response, @RequestParam("data") String data) throws IOException, WriterException {


        String sql = "select t.* from res_repair t where t.dr='0' and t.fuuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        String fname = "assetsbx.docx";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        String filePath = ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname;
        File file = new File(filePath);
        String[] uulist = struuid.split(",");
        if(uulist.length > 2){
            HSSFWorkbook  writableWorkbook = new HSSFWorkbook();
            String busid = "123";
            HashMap<String, Object> m = new HashMap<String, Object>();
            try {
                HSSFSheet  sheet = writableWorkbook.createSheet();// ??????????????????
                String[] lik = {"uuid", "recyclestr", "mark", "money", "wbout_datestr", "create_username", "zcuuid", "name", "classfullname", "zc_cnt", "status", "wbsupplierstr"};
                String[] liv = {"??????", "????????????", "????????????", "????????????", "????????????", "?????????", "????????????", "????????????", "????????????", "??????", "??????", "??????"};
                HSSFRow row = sheet.createRow(0);
                // ????????????
                // ?????????????????????????????????

                for (int column = 0; column < liv.length; column++) { // ??????key??????
                    HSSFCell cell=row.createCell(column);
                    cell.setCellValue(liv[column]);
                }

                for (int ex = 0; ex < uulist.length-1; ex++) {
                    HSSFRow row2 = sheet.createRow(ex+1);
                    busid = datars.getRcd(ex).getString("fuuid");
                    m.put("name", datars.getRcd(ex).getString("fname"));
                    m.put("uuid", busid);
                    String status = datars.getRcd(ex).getString("fstatus");
                    //underrepair,cancel,finish
                    if ("finish".equals(status)) {
                        m.put("status", "??????");
                    } else if ("cancel".equals(status)) {
                        m.put("status", "??????");
                    } else if ("underrepair".equals(status)) {
                        m.put("status", "?????????");
                    }
                    m.put("reason", datars.getRcd(ex).getString("freason"));
                    m.put("processuser", datars.getRcd(ex).getString("fprocessuser"));
                    m.put("processtime", datars.getRcd(ex).getString("fprocesstime"));
                    m.put("money", datars.getRcd(ex).getString("fmoney"));
                    m.put("mark", datars.getRcd(ex).getString("fmark"));

                    String sqlassets = "select " + AssetsConstant.resSqlbody + " t.zc_cnt,t.name,t.uuid from res_repair_item a ,res t where a.dr='0' and a.resid =t.id and a.busuuid =?";
                    RcdSet rs = db.query(sqlassets, busid);
                    List<JSONObject> list = new ArrayList<>();
                    for (int j = 0; j < rs.size(); j++) {
                        list.add(ConvertUtil.OtherJSONObjectToFastJSONObject(rs.getRcd(j).toJsonObject()));

                    }
                    m.put("assets", list);

                    JSONArray jsonArray = JSONArray.parseArray(m.get("assets").toString());// ??????data?????????JSONArray
                    // ??????jsonArray

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i); // ?????????????????????
                        for (int j = 0; j < lik.length; j++) {
                            String keys = lik[j]; // ??????key
                            if(m.containsKey(keys)) {
                                Object value = m.get(keys); // ??????key?????????value
                                if(value==""||value==null){
                                    value = "???";
                                }
                                row2.createCell(j).setCellValue(value.toString());
                            } else {
                                if (keys.equals("zcuuid")) {
                                    keys = "uuid";
                                }
                                String value = item.getString(keys); // ??????key?????????value
                                row2.createCell(j).setCellValue(value.toString());
                            }


                        }

                    }

                }
                OutputStream outputStream = response.getOutputStream();
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/x-download");
                String filedisplay = "data.xlsx";
                filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
                writableWorkbook.write(outputStream); // ??????????????????
                writableWorkbook.close(); // ???????????????????????????
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }







    /**
     * @Description:????????????
     */
    @ResponseBody
    @Acl(info = "???????????????,????????????", value = Acl.ACL_USER)
    @RequestMapping(value = "/print.do")
    public void print(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("data") String data) throws IOException, WriterException {


        OutputStream outputStream = httpServletResponse.getOutputStream();
        String sql = "select t.* from res_repair t where t.dr='0' and t.fuuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        String fname = "assetsbx.docx";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        String filePath = ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname;
        File file = new File(filePath);

        if (file.exists()) {
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                for (int i = 0; i < datars.size(); i++) {
                    try {
                        String busid = datars.getRcd(i).getString("fuuid");
                        HashMap<String, Object> m = new HashMap<String, Object>();
                        m.put("name", datars.getRcd(i).getString("fname"));
                        m.put("uuid", busid);
                        String status = datars.getRcd(i).getString("fstatus");
                        //underrepair,cancel,finish
                        if ("finish".equals(status)) {
                            m.put("status", "??????");
                        } else if ("cancel".equals(status)) {
                            m.put("status", "??????");
                        } else if ("underrepair".equals(status)) {
                            m.put("status", "?????????");
                        }
                        m.put("reason", datars.getRcd(i).getString("freason"));
                        m.put("processuser", datars.getRcd(i).getString("fprocessuser"));
                        m.put("processtime", datars.getRcd(i).getString("fprocesstime"));
                        m.put("money", datars.getRcd(i).getString("fmoney"));
                        m.put("mark", datars.getRcd(i).getString("fmark"));

                        String sqlassets = "select " + AssetsConstant.resSqlbody + " t.zc_cnt,t.name,t.uuid from res_repair_item a ,res t where a.dr='0' and a.resid =t.id and a.busuuid =?";
                        RcdSet rs = db.query(sqlassets, busid);
                        List<JSONObject> list = new ArrayList<>();
                        for (int j = 0; j < rs.size(); j++) {
                            list.add(ConvertUtil.OtherJSONObjectToFastJSONObject(rs.getRcd(j).toJsonObject()));
                        }
                        m.put("assets", list);
                        HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy();
                        Configure config = Configure.newBuilder().bind("assets", hackLoopTableRenderPolicy).build();
                        XWPFTemplate tpl = XWPFTemplate.compile(ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname, config).render(m);
                        ZipEntry entry = new ZipEntry(busid + ".docx");
                        zipOutputStream.putNextEntry(entry);
                        tpl.write(zipOutputStream);
                        zipOutputStream.flush();
                        tpl.close();
                        zipOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }else{
            System.out.println("tpl file not exists");
        }
        outputStream.flush();
        outputStream.close();

    }

}

