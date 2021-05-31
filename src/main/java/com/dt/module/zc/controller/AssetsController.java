package com.dt.module.zc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Numberings;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.render.WhereDelegate;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysFiles;
import com.dt.module.base.service.ISysFilesService;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResActionItemService;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.ct.entity.CtCategoryRoot;
import com.dt.module.ct.service.ICtCategoryRootService;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.entity.SysProcessForm;
import com.dt.module.flow.service.ISysProcessDataService;
import com.dt.module.flow.service.ISysProcessDefService;
import com.dt.module.flow.service.ISysProcessFormService;
import com.dt.module.form.service.ISysFormService;
import com.dt.module.form.service.impl.FormService;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsFlowService;
import com.dt.module.zc.service.impl.AssetsOperService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

@Controller
@RequestMapping("/api/zc")
public class AssetsController extends BaseController {


    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    ICtCategoryRootService CtCategoryRootServiceImpl;

    @Autowired
    FormService formService;

    @Autowired
    ISysFilesService SysFilesServiceImpl;

    @Autowired
    AssetsFlowService assetsFlowService;

    @Autowired
    ISysProcessDefService SysProcessDefServiceImpl;

    @Autowired
    ISysProcessDataService SysProcessDataServiceImpl;

    @Autowired
    ISysProcessFormService SysProcessFormServiceImpl;

    @Autowired
    ISysFormService SysFormServiceImpl;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResActionItemService ResActionItemServiceImpl;

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    @Autowired
    IResService ResServiceImpl;

    /**
     * @Description:查询数据字典
     * @param uid
     * @param zchccat
     * @param comppart
     * @param comp
     * @param belongcomp
     * @param dicts
     * @param parts
     * @param partusers
     * @param classid
     * @param classroot
     * @param zccatused
     */
    @ResponseBody
    @Acl(info = "查询数据字典", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryDictFast.do")
    @Transactional
    public R queryDictFast(String uid, String zchccat, String comppart, String comp, String belongcomp, String dicts, String parts, String partusers, String classid, String classroot, String zccatused) {
        return assetsOperService.queryDictFast(uid, zchccat, comppart, comp, belongcomp, dicts, parts, partusers, classid, classroot, zccatused);
    }


    /**
     * @Description:修改前端字段显示
     */
    @ResponseBody
    @Acl(info = "修改前端字段显示", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/modifyZcColCtlShow.do")
    public R modifyZcColCtlShow(String id, String json) {
        return assetsOperService.modifyAssetsColCtlShow(id, json);
    }

    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryZcColCtlShow.do")
    public R queryZcColCtlShow() {
        return assetsOperService.queryZcColCtlShow();
    }


    /**
     * @Description:查询前端字段显示
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryZcColCtlById.do")
    public R queryZcColCtlById(String id) {
        return assetsOperService.queryAssetsColCtlById(id);
    }

    /**
     * @Description:获取后台资产类目
     */
    @ResponseBody
    @Acl(info = "获取后台资产类目", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectZcCats.do")
    public R selectZcCats() {
        QueryWrapper<CtCategoryRoot> ew = new QueryWrapper<CtCategoryRoot>();
        ew.in("id", '8', '3', '7');
        return R.SUCCESS_OPER(CtCategoryRootServiceImpl.list(ew));
    }

    /**
     * @Description:获取资产
     */
    @ResponseBody
    @Acl(info = "获取资产", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(String id) {
        return R.SUCCESS_OPER(ResServiceImpl.getById(id));
    }

    /**
     * @Description:获取资产
     */
    @ResponseBody
    @Acl(info = "获取资产", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByUuid.do")
    public R selectByUuid(String uuid) {
        QueryWrapper<Res> q = new QueryWrapper<Res>();
        q.eq("uuid",uuid);
        return R.SUCCESS_OPER(ResServiceImpl.getOne(q));
    }

    /**
     * @Description:查询前端字段显示
     */
    @ResponseBody
    @Acl(info = "查询前端字段显示", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryzclabelcols.do")
    public R queryzclabelcols() {

        JSONArray res = new JSONArray();
        JSONObject e1 = new JSONObject();
        e1.put("name", "资产名称");
        res.add(e1);

        JSONObject e2 = new JSONObject();
        e2.put("model", "资产型号");
        res.add(e2);

        JSONObject e3 = new JSONObject();
        e3.put("buy_time", "采购日期");
        res.add(e3);

        JSONObject e4 = new JSONObject();
        e4.put("part_id", "使用部门");
        res.add(e4);

        JSONObject e5 = new JSONObject();
        e5.put("loc", "存放区域");
        res.add(e5);
        return R.SUCCESS_OPER(res);

    }



    /**
     * @Description:检查单据前置条件
     * @param type
     * @param items
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/fastProcessItemCheck.do")
    public R fastProcessItemCheck(String type, String items) {
        return assetsOperService.fastProcessItemCheck(type, items);
    }

    /**
     * @Description:下载标签
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/downloadLabelSingle.do")
    public void downloadLabelSingle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("data") String data) throws IOException, WriterException {
        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where t.dr='0' and t.uuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        int rwmh = 100;
        int rwmw = 100;
        int txmh = 80;
        int txmw = 200;
        Rcd rs = db.uniqueRecord("select * from res_label_tpl where dr='0' and ifdef='1'");
        if (rs != null) {
            String confstr = rs.getString("conf");
            JSONObject confobj = JSONObject.parseObject(confstr);
            if (confobj != null) {
                if (confobj.containsKey("rwm")) {
                    JSONObject rwm = confobj.getJSONObject("rwm");
                    if (rwm.containsKey("h")) {
                        rwmh = rwm.getIntValue("h");
                    }
                    if (rwm.containsKey("w")) {
                        rwmw = rwm.getIntValue("w");
                    }
                }
                if (confobj.containsKey("txm")) {
                    JSONObject txm = confobj.getJSONObject("txm");
                    if (txm.containsKey("h")) {
                        txmh = txm.getIntValue("h");
                    }
                    if (txm.containsKey("w")) {
                        txmw = txm.getIntValue("w");
                    }
                }
            }
            SysFiles fileobj = SysFilesServiceImpl.getById(rs.getString("tplfileid"));
            String fileurl = fileobj.getPath();
            String filename = ToolUtil.isEmpty(fileobj.getFilenameO()) ? "unknow.file" : fileobj.getFilenameO();
            String filePath = ToolUtil.getRealPathInWebApp("") + ".." + File.separatorChar + fileurl;
            File file = new File(filePath);
            System.out.println("Use Tpl File:" + ToolUtil.getRealPathInWebApp("") + ".." + File.separatorChar + fileurl);
            if (file.exists()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < datars.size(); i++) {
                    HashMap<String, Object> m = new HashMap<String, Object>();
                    m.put("name", datars.getRcd(i).getString("name"));
                    m.put("model", datars.getRcd(i).getString("model"));
                    m.put("usedusername", datars.getRcd(i).getString("used_username"));
                    m.put("partname", datars.getRcd(i).getString("part_name"));
                    m.put("buytime", datars.getRcd(i).getString("buy_timestr"));
                    m.put("classname", datars.getRcd(i).getString("classname"));
                    m.put("uuid", datars.getRcd(i).getString("uuid"));
                    m.put("txm", Pictures.ofBufferedImage(createAssetsPic("txm", datars.getRcd(i).getString("uuid")), PictureType.PNG).size(rwmw,rwmh).create());
                    m.put("rwm", Pictures.ofBufferedImage(createAssetsPic("rwm", datars.getRcd(i).getString("uuid")), PictureType.PNG).size(rwmw,rwmh).create());
                    list.add(m);
                }
                result.put("assets",list);
                HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy();
                Configure config = Configure.builder().bind("assets", hackLoopTableRenderPolicy).build();
                XWPFTemplate tpl = XWPFTemplate.compile(ToolUtil.getRealPathInWebApp("") + ".." + File.separatorChar + fileurl,config).render(result);
                ZipEntry entry = new ZipEntry("data" + ".docx");
                zipOutputStream.putNextEntry(entry);
                tpl.write(zipOutputStream);
                zipOutputStream.flush();
                tpl.close();
            }
        }
        zipOutputStream.close();
        outputStream.flush();
        outputStream.close();

    }


    /**
     * @Description:下载标签
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/downloadLabel.do")
    public void downloadLabel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("data") String data) throws IOException, WriterException {


        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where t.dr='0' and t.uuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        int rwmh = 100;
        int rwmw = 100;
        int txmh = 80;
        int txmw = 200;
        Rcd rs = db.uniqueRecord("select * from res_label_tpl where dr='0' and ifdef='1'");
        if (rs != null) {
            String confstr = rs.getString("conf");
            JSONObject confobj = JSONObject.parseObject(confstr);
            if (confobj != null) {
                if (confobj.containsKey("rwm")) {
                    JSONObject rwm = confobj.getJSONObject("rwm");
                    if (rwm.containsKey("h")) {
                        rwmh = rwm.getIntValue("h");
                    }
                    if (rwm.containsKey("w")) {
                        rwmw = rwm.getIntValue("w");
                    }
                }
                if (confobj.containsKey("txm")) {
                    JSONObject txm = confobj.getJSONObject("txm");
                    if (txm.containsKey("h")) {
                        txmh = txm.getIntValue("h");
                    }
                    if (txm.containsKey("w")) {
                        txmw = txm.getIntValue("w");
                    }
                }
            }
            HashMap<String, Object> result = new HashMap<String, Object>();

            SysFiles fileobj = SysFilesServiceImpl.getById(rs.getString("tplfileid"));
            String fileurl = fileobj.getPath();
            String filename = ToolUtil.isEmpty(fileobj.getFilenameO()) ? "unknow.file" : fileobj.getFilenameO();
            String filePath = ToolUtil.getRealPathInWebApp("") + ".." + File.separatorChar + fileurl;
            File file = new File(filePath);
            System.out.println("Use Tpl File:" + ToolUtil.getRealPathInWebApp("") + ".." + File.separatorChar + fileurl);
            if (file.exists()) {
                for (int i = 0; i < datars.size(); i++) {
                    try {
                        HashMap<String, Object> m = new HashMap<String, Object>();
                        m.put("name", datars.getRcd(i).getString("name"));
                        m.put("model", datars.getRcd(i).getString("model"));
                        m.put("usedusername", datars.getRcd(i).getString("used_username"));
                        m.put("partname", datars.getRcd(i).getString("part_name"));
                        m.put("buytime", datars.getRcd(i).getString("buy_timestr"));
                        m.put("classname", datars.getRcd(i).getString("classname"));
                        m.put("uuid", datars.getRcd(i).getString("uuid"));
                        m.put("txm", Pictures.ofBufferedImage(createAssetsPic("txm", datars.getRcd(i).getString("uuid")), PictureType.PNG).size(rwmw,rwmh).create());
                        m.put("rwm", Pictures.ofBufferedImage(createAssetsPic("rwm", datars.getRcd(i).getString("uuid")), PictureType.PNG).size(rwmw,rwmh).create());
                        XWPFTemplate tpl = XWPFTemplate.compile(ToolUtil.getRealPathInWebApp("") + ".." + File.separatorChar + fileurl).render(m);
                        ZipEntry entry = new ZipEntry(datars.getRcd(i).getString("uuid") + ".docx");
                        zipOutputStream.putNextEntry(entry);
                        tpl.write(zipOutputStream);
                        zipOutputStream.flush();
                        tpl.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        zipOutputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * @Description:生成资产标签图片
     * @param type
     * @param data
     */
    private BufferedImage createAssetsPic(String type, String data) {
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        int w = 500;
        int h = 500;
        if ("rwm".equals(type)) {
            format = BarcodeFormat.QR_CODE;
            w = 450;
            h = 450;
        } else if ("txm".equals(type)) {
            format = BarcodeFormat.CODE_128;
            h = 180;
            w = 450;
        }
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(data, format, w, h);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BufferedImage buffImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return buffImg;
    }

    /**
     * @Description:下载资产卡片
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/downloadCard.do")
    public void downloadCard(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("data") String data) throws IOException, WriterException {

        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where t.dr='0' and t.uuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        String fname = "assetscard.docx";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        String filePath = ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname;
        File file = new File(filePath);
        if (file.exists()) {
            for (int i = 0; i < datars.size(); i++) {
                try {
                    HashMap<String, Object> m = new HashMap<String, Object>();
                    m.put("uuid", datars.getRcd(i).getString("uuid"));
                    m.put("name", datars.getRcd(i).getString("name"));
                    m.put("classname", datars.getRcd(i).getString("classname"));
                    m.put("model", datars.getRcd(i).getString("model"));
                    m.put("sn", datars.getRcd(i).getString("sn"));
                    m.put("recycel", datars.getRcd(i).getString("recyclestr"));
                    m.put("unit", datars.getRcd(i).getString("unit"));
                    m.put("brand", datars.getRcd(i).getString("brandstr"));
                    m.put("supplier", datars.getRcd(i).getString("supplierstr"));
                    m.put("otheruuid", datars.getRcd(i).getString("fs20"));
                    m.put("buymoney", datars.getRcd(i).getString("buy_price"));
                    m.put("buytime", datars.getRcd(i).getString("buy_timestr"));
                    m.put("belongcompname", datars.getRcd(i).getString("belongcomp_name"));
                    m.put("compname", datars.getRcd(i).getString("comp_name"));
                    m.put("partname", datars.getRcd(i).getString("part_name"));
                    m.put("usedusername", datars.getRcd(i).getString("used_username"));
                    m.put("usefullife", datars.getRcd(i).getString("usefullifestr"));
                    m.put("conf", datars.getRcd(i).getString("confdesc"));
                    m.put("mark", datars.getRcd(i).getString("mark"));
                    m.put("loc", datars.getRcd(i).getString("locstr"));
                    m.put("source", datars.getRcd(i).getString("zcsourcestr"));

                    QueryWrapper<ResChangeItem> qw = new QueryWrapper<ResChangeItem>();
                    qw.eq("resid", datars.getRcd(i).getString("id"));
                    qw.last("limit 10");
                    qw.orderByDesc("create_time");
                    List<ResChangeItem> cis = ResChangeItemServiceImpl.list(qw);
                    m.put("assetshistory", cis);
                    HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy();
                    Configure config = Configure.builder().bind("assetshistory", hackLoopTableRenderPolicy).build();
                    XWPFTemplate tpl = XWPFTemplate.compile(ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname,config).render(m);
                    ZipEntry entry = new ZipEntry(datars.getRcd(i).getString("uuid") + ".docx");
                    zipOutputStream.putNextEntry(entry);
                    tpl.write(zipOutputStream);
                    zipOutputStream.flush();
                    tpl.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        zipOutputStream.close();
        outputStream.flush();
        outputStream.close();

    }

    /**
     * @Description:下载资产图片
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/downloadZcImage.do")
    public void downloadAllQr(String type, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("data") String data) throws IOException, WriterException {

        BarcodeFormat format = BarcodeFormat.QR_CODE;
        int w = 500;
        int h = 500;
        if ("rwm".equals(type)) {
            format = BarcodeFormat.QR_CODE;
            w = 450;
            h = 450;
        } else if ("txm".equals(type)) {
            format = BarcodeFormat.CODE_128;
            h = 180;
            w = 450;
        }
        httpServletResponse.setContentType("application/zip");
        httpServletResponse.setHeader("Content-disposition",
                "attachment; filename=" + new String("erm".getBytes(),
                        "ISO-8859-1") + ".zip");


        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        JSONArray data_arr = JSONArray.parseArray(data);
        for (int i = 0; i < data_arr.size(); i++) {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data_arr.getString(i), format, w, h);
            BufferedImage buffImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ZipEntry entry = new ZipEntry(data_arr.getString(i) + ".jpg");
            zipOutputStream.putNextEntry(entry);
            ImageIO.write(buffImg, "jpg", zipOutputStream);
            zipOutputStream.flush();
        }

        zipOutputStream.close();
        outputStream.flush();
        outputStream.close();
    }





    /**
     * @Description:查询单据数据
     */
    @ResponseBody
    @Acl(info = "查询单据", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectBillById.do")
    public R selectBillById(String id) {

        SysProcessData sd = SysProcessDataServiceImpl.getById(id);
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(sd, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t,res_action_item item where item.dr='0' and  t.id=item.resid and item.busuuid=?";
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql, sd.getBusid()).toJsonArrayWithJsonObject()));
        QueryWrapper<SysProcessForm> ew = new QueryWrapper<SysProcessForm>();
        ew.and(i -> i.eq("processdataid", id));
        SysProcessForm form = SysProcessFormServiceImpl.getOne(ew);
        if (form != null) {
            res.put("formdata", form.getFdata());
            res.put("formconf", form.getFtpldata());
        }
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询单据数据
     */
    @ResponseBody
    @Acl(info = "查询单据", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryUserAssetsHistory.do")
    public R queryUserAssetsHistory(String userid) {

        if(ToolUtil.isEmpty(userid)){
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        String sql="select t1.sourcetype, \n" + AssetsConstant.resSqlbody +" t.*"+
                "\n" +
                "from (\n" +
                "select \n" +
                "'领用' sourcetype , a.update_time ,a.resid \n" +
                " from res_collectionreturn_item a, res_collectionreturn b where b.busuuid =a.busuuid \n" +
                "and b.status in ('finish_na','success') and a.dr='0'\n" +
                "and b.tuseduserid = ?\n" +
                "union all\n" +
                "select '借用' sourcetype,a.update_time ,a.resid  from res_loanreturn_item a ,res_loanreturn b where  b.busuuid =a.busuuid \n" +
                "and b.status in ('finish_na','success') and a.dr='0'\n" +
                "and a.lruserid=?\n" +
                " union all \n" +
                "select '实体变更' sourcetype ,a.update_time ,a.resid  from res_c_basicinformation_item a,res_c_basicinformation b where a.busuuid =b.busuuid \n" +
                "and b.status in ('finish_na','success') and a.dr='0' and a.tuseduseridstatus ='true'\n" +
                "and a.tuseduserid =?\n" +
                ") t1,res t where t1.resid=t.id order by t1.update_time  desc\n" +
                "\n";

        return R.SUCCESS_OPER(db.query(sql,userid,userid,userid).toJsonArrayWithJsonObject());
    }



}
