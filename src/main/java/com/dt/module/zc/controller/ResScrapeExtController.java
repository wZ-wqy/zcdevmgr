package com.dt.module.zc.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResAllocate;
import com.dt.module.zc.entity.ResScrape;
import com.dt.module.zc.service.IResScrapeItemService;
import com.dt.module.zc.service.IResScrapeService;
import com.dt.module.zc.service.impl.ResScrapeService;
import com.dt.module.zc.service.impl.AssetsFlowService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import com.google.zxing.WriterException;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-05-28
 */
@Controller
@RequestMapping("/api/zc/resScrape/ext")
public class ResScrapeExtController extends BaseController {


    @Autowired
    IResScrapeItemService ResScrapeItemServiceImpl;

    @Autowired
    ResScrapeService resScrapeService;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    IResScrapeService ResScrapeServiceImpl;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    AssetsFlowService assetsFlowService;

    /**
     * @Description:查询报废单据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResScrape in = ResScrapeServiceImpl.getById(id);
        String uuid = in.getUuid();
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(in, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where dr='0' and id in (select resid from res_scrape_item where uuid=? and dr='0')";
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql, uuid).toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:根据业务ID查询报废单据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByBusid.do")
    public R selectByBusid(@RequestParam(value = "busid", required = true, defaultValue = "") String busid) {
        QueryWrapper<ResScrape> qw = new QueryWrapper<ResScrape>();
        qw.and(i -> i.eq("uuid", busid));
        ResScrape in = ResScrapeServiceImpl.getOne(qw);
        String uuid = in.getUuid();
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(in, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t where dr='0' and id in (select resid from res_scrape_item where uuid=? and dr='0')";
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql, uuid).toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:取消报废
     */
    @ResponseBody
    @Acl(info = "取消", value = Acl.ACL_USER)
    @RequestMapping(value = "/cancel.do")
    public R cancel(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResScrape obj = ResScrapeServiceImpl.getById(id);
        if(FlowConstant.PSTATUS_APPLY.equals(obj.getStatus())){
            UpdateWrapper<ResScrape> u=new UpdateWrapper<>();
            u.set("status",FlowConstant.PSTATUS_CANCEL);
            u.eq("id",id);
            ResScrapeServiceImpl.update(u);
            UpdateWrapper<Res> u2=new UpdateWrapper<>();
            u2.set("inprocess","0");
            u2.inSql("id","select resid from res_scrape_item where dr='0' and uuid='"+obj.getUuid()+"'");
            ResServiceImpl.update(u2);
        }else{
            return R.FAILURE("当前单据状态错误，作废操作失败");
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:生成报废单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insertOrUpdate(ResScrape entity, String busitimestr, String items) throws ParseException {
        resScrapeService.create(entity, busitimestr, items);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询报废单据列表
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        return resScrapeService.selectList(null, null);
    }

    /**
     * @Description:查询我的报废单据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/myList.do")
    public R myList(String statustype) {
        return resScrapeService.selectList(this.getUserId(), statustype);
    }


    /**
     * @Description:打印单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/print.do")
    public void print(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("data") String data) throws IOException, WriterException {


        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String sql = "select t.*,date_format(busidate,'%Y-%m-%d') bfrq from res_scrape t where t.dr='0' and t.uuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        String fname = "assetsbf.docx";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        String filePath = ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname;
        File file = new File(filePath);

        if (file.exists()) {
            for (int i = 0; i < datars.size(); i++) {
                try {
                    String busid=datars.getRcd(i).getString("uuid");
                    HashMap<String, Object> m = new HashMap<String, Object>();
                    m.put("title", datars.getRcd(i).getString("title"));
                    m.put("uuid", busid);
                    String status=datars.getRcd(i).getString("status");
                    if(FlowConstant.PSTATUS_FINISH_NO_APPROVAL.equals(status)||FlowConstant.PSTATUS_FINISH.equals(status)){
                        m.put("status", "完成");
                    }else if(FlowConstant.PSTATUS_CANCEL.equals(status)){
                        m.put("status", "取消");
                    }else{
                        m.put("status", "未完成");
                    }
                    m.put("busidate", datars.getRcd(i).getString("bfrq"));
                    m.put("processusername", datars.getRcd(i).getString("processusername"));
                    m.put("cnt", datars.getRcd(i).getString("cnt"));
                    m.put("ct", datars.getRcd(i).getString("ct"));
                    m.put("mark", datars.getRcd(i).getString("mark"));


                    String sqlassets="select "+AssetsConstant.resSqlbody +" t.zc_cnt,t.name,t.uuid from res_scrape_item a ,res t where a.dr='0' and a.resid =t.id and a.uuid =?";
                    RcdSet rs=db.query(sqlassets, busid);
                    List<JSONObject> list=new ArrayList<>();
                    for(int j=0;j<rs.size();j++){
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println("tpl file not exists");
        }
        zipOutputStream.close();
        outputStream.flush();
        outputStream.close();

    }
}

