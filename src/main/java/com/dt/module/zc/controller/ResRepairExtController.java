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
 * 前端控制器
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
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/cancellation.do")
    public R cancellation(String id) {
        ResRepair dbobj = ResRepairServiceImpl.getById(id);
        if (AssetsConstant.BX_STATUS_UNDERREPAIR.equals(dbobj.getFstatus())) {

            //修改单据
            ResRepair e = new ResRepair();
            e.setId(id);
            e.setFstatus(AssetsConstant.BX_STATUS_CANCEL);
            ResRepairServiceImpl.saveOrUpdate(e);

            //更新资产状态，作废单据
            UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
            ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
            ups.setSql("recycle=prerecycle");
            ResServiceImpl.update(ups);

            //删除历史记录
            QueryWrapper<ResRepairItem> ew = new QueryWrapper<ResRepairItem>();
            ew.and(i -> i.eq("repairid", id));
            ResRepairItemServiceImpl.remove(ew);


        } else {
            return R.FAILURE("当前状态不允许作废");
        }
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:生产维修单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResRepair entity, String items, String files) {
        String status = entity.getFstatus();
        String id = entity.getId();

        JSONArray arr = JSONArray.parseArray(items);
        if (ToolUtil.isNotEmpty(id)) {
            //更新报修单据
            //盘点维护单是否已完成，完成的维修单不允许修改
            ResRepair dbobj = ResRepairServiceImpl.getById(entity.getId());
            if (AssetsConstant.BX_STATUS_FINSH.equals(dbobj.getFstatus())) {
                return R.FAILURE("当前状态不允许更新");
            }
            //如果修改了维修状态,则同步修改
            if (AssetsConstant.BX_STATUS_FINSH.equals(status)) {
                //更新资产状态，完成单据
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
                ups.setSql("recycle=prerecycle");
                ResServiceImpl.update(ups);
            }
            ResRepairServiceImpl.saveOrUpdate(entity);
        } else {
            //新增报修单据
            ArrayList<ResRepairItem> cols = new ArrayList<ResRepairItem>();
            String uuid = assetsOperService.createUuid(AssetsConstant.UUID_BX);
            entity.setFuuid(uuid);
            ResRepairServiceImpl.saveOrUpdate(entity);
            QueryWrapper<ResRepair> ew = new QueryWrapper<ResRepair>();
            ew.and(i -> i.eq("fuuid", uuid));
            ResRepair dbobj = ResRepairServiceImpl.getOne(ew);
            id = dbobj.getId();
            //记录需要维修的资产
            for (int i = 0; i < arr.size(); i++) {
                ResRepairItem e = new ResRepairItem();
                e.setRepairid(id);
                e.setBusuuid(uuid);
                e.setResid(arr.getJSONObject(i).getString("id"));
                cols.add(e);
            }
            ResRepairItemServiceImpl.saveOrUpdateBatch(cols);
            if (!AssetsConstant.BX_STATUS_FINSH.equals(status)) {
                //修改资产状态,记录原资产状态
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
                ups.setSql("prerecycle=recycle");
                ups.set("recycle", AssetsRecycleEnum.RECYCLE_REPAIR.getValue());
                ResServiceImpl.update(ups);
            }
        }

        //全量删除图片
        QueryWrapper<ResRepairFile> fq = new QueryWrapper<ResRepairFile>();
        String finalId = id;
        fq.and(i -> i.eq("repiarid", finalId));
        ResRepairFileServiceImpl.remove(fq);
        //重新更新图片数据
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
     * @Description:查询维修单据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
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
     * @Description:删除维修单据
     */
    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResRepair obj = ResRepairServiceImpl.getById(id);
        if (AssetsConstant.BX_STATUS_UNDERREPAIR.equals(obj.getFstatus())) {
            return R.FAILURE("当前状态不允许删除");
        } else {
            QueryWrapper<ResRepairItem> ew = new QueryWrapper<ResRepairItem>();
            ew.and(i -> i.eq("repairid", id));
            ResRepairItemServiceImpl.remove(ew);
            return R.SUCCESS_OPER(ResRepairServiceImpl.removeById(id));
        }

    }

    /**
     * @Description:查询维修单据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<ResRepair> ew = new QueryWrapper<ResRepair>();
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResRepairServiceImpl.list(ew));

    }

    /**
     * @Description:查询我的维修单据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
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
     * @Description:查询我的维修单据
     */
    @ResponseBody
    @Acl(info = "查询所有,有分页", value = Acl.ACL_USER)
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
     * @Description:结束维修
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/finish.do")
    public R finish(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResRepair obj = ResRepairServiceImpl.getById(id);
        if (!AssetsConstant.BX_STATUS_UNDERREPAIR.equals(obj.getFstatus())) {
            return R.FAILURE("当前状态不允许变更");
        } else {
            //修改单据
            ResRepair e = new ResRepair();
            e.setId(id);
            e.setFstatus(AssetsConstant.BX_STATUS_FINSH);
            ResRepairServiceImpl.saveOrUpdate(e);

            //更新资产状态，完成单据
            UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
            ups.inSql("id", "select resid from res_repair_item where dr='0' and repairid='" + id + "'");
            ups.setSql("recycle=prerecycle");
            ResServiceImpl.update(ups);

            //修改资产
            return R.SUCCESS_OPER();

        }

    }


    /**
     * @Description:导出报修
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
                HSSFSheet  sheet = writableWorkbook.createSheet();// 创建新的一页
                String[] lik = {"uuid", "recyclestr", "mark", "money", "wbout_datestr", "create_username", "zcuuid", "name", "classfullname", "zc_cnt", "status", "wbsupplierstr"};
                String[] liv = {"编号", "单据状态", "报修原因", "报修费用", "处理日期", "处理人", "资产编号", "资产名称", "资产分类", "数量", "状态", "备注"};
                HSSFRow row = sheet.createRow(0);
                // 列数计数
                // 将第一行信息加到页中。

                for (int column = 0; column < liv.length; column++) { // 遍历key集合
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
                        m.put("status", "完成");
                    } else if ("cancel".equals(status)) {
                        m.put("status", "取消");
                    } else if ("underrepair".equals(status)) {
                        m.put("status", "维修中");
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

                    JSONArray jsonArray = JSONArray.parseArray(m.get("assets").toString());// 得到data对应的JSONArray
                    // 遍历jsonArray

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i); // 得到数组的每项
                        for (int j = 0; j < lik.length; j++) {
                            String keys = lik[j]; // 得到key
                            if(m.containsKey(keys)) {
                                Object value = m.get(keys); // 得到key对应的value
                                if(value==""||value==null){
                                    value = "无";
                                }
                                row2.createCell(j).setCellValue(value.toString());
                            } else {
                                if (keys.equals("zcuuid")) {
                                    keys = "uuid";
                                }
                                String value = item.getString(keys); // 得到key对应的value
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
                writableWorkbook.write(outputStream); // 加入到文件中
                writableWorkbook.close(); // 关闭文件，释放资源
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }







    /**
     * @Description:打印单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
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
                            m.put("status", "完成");
                        } else if ("cancel".equals(status)) {
                            m.put("status", "取消");
                        } else if ("underrepair".equals(status)) {
                            m.put("status", "维修中");
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

