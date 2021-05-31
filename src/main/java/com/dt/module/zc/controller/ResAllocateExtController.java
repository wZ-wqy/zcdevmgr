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
import com.dt.module.zc.service.IResAllocateItemService;
import com.dt.module.zc.service.IResAllocateService;
import com.dt.module.zc.service.impl.ResAllocateService;
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
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
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
 * @since 2020-04-25
 */
@Controller
@RequestMapping("/api/zc/resAllocate/ext")
public class ResAllocateExtController extends BaseController {

    @Autowired
    AssetsFlowService assetsFlowService;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    IResAllocateItemService ResAllocateItemServiceImpl;

    @Autowired
    IResAllocateService ResAllocateServiceImpl;

    @Autowired
    ResAllocateService resAllocateService;

    @Autowired
    IResService ResServiceImpl;

    /**
     * @Description:查询调拨单据数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        return resAllocateService.selectList(null, null);
    }

    /**
     * @Description:查询我到调拨
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/myList.do")
    public R myList(String statustype) {
        return resAllocateService.selectList(this.getUserId(), statustype);
    }

    /**
     * @Description:查询单条调拨数据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResAllocate obj = ResAllocateServiceImpl.getById(id);
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t,res_allocate_item b where t.id=b.resid and b.dr='0' and b.allocateid='" + id + "'";
        res.put("items", db.query(sql).toJsonArrayWithJsonObject());
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:取消调拨
     */
    @ResponseBody
    @Acl(info = "取消", value = Acl.ACL_USER)
    @RequestMapping(value = "/cancel.do")
    public R cancel(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResAllocate obj = ResAllocateServiceImpl.getById(id);
        if(FlowConstant.PSTATUS_APPLY.equals(obj.getStatus())){
            UpdateWrapper<ResAllocate> u=new UpdateWrapper<>();
            u.set("status",FlowConstant.PSTATUS_CANCEL);
            u.eq("id",id);
            ResAllocateServiceImpl.update(u);
            UpdateWrapper<Res> u2=new UpdateWrapper<>();
            u2.set("inprocess","0");
            u2.inSql("id","select resid from res_allocate_item where dr='0' and busuuid='"+obj.getUuid()+"'");
            ResServiceImpl.update(u2);
        }else{
            return R.FAILURE("当前单据状态错误，作废操作失败");
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:根据业务id查询单据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByBusid.do")
    public R selectByBusid(@RequestParam(value = "busid", required = true, defaultValue = "") String busid) {
        QueryWrapper<ResAllocate> qw = new QueryWrapper<ResAllocate>();
        qw.and(i -> i.eq("uuid", busid));
        ResAllocate obj = ResAllocateServiceImpl.getOne(qw);
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res t,res_allocate_item b where t.id=b.resid and b.dr='0' and b.busuuid=?";
        res.put("items", db.query(sql, busid).toJsonArrayWithJsonObject());
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:生产调拨单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/save.do")
    public R save(ResAllocate entity, String items) {
        return resAllocateService.save(entity, items);
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
        String sql = "select t.*,date_format(busdate,'%Y-%m-%d') outrq,date_format(acttime,'%Y-%m-%d') inrq from res_allocate t where t.dr='0' and t.uuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        String fname = "assetsdb.docx";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        String filePath = ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname;
        File file = new File(filePath);

        if (file.exists()) {
            for (int i = 0; i < datars.size(); i++) {
                try {
                    String busid=datars.getRcd(i).getString("uuid");
                    HashMap<String, Object> m = new HashMap<String, Object>();
                    m.put("name", datars.getRcd(i).getString("name"));
                    m.put("uuid", datars.getRcd(i).getString("uuid"));
                    String status=datars.getRcd(i).getString("status");
                    if(FlowConstant.PSTATUS_FINISH_NO_APPROVAL.equals(status)||FlowConstant.PSTATUS_FINISH.equals(status)){
                        m.put("status", "完成");
                    }else if(FlowConstant.PSTATUS_CANCEL.equals(status)){
                        m.put("status", "取消");
                    }else{
                        m.put("status", "未完成");
                    }
                    m.put("uuid", busid);
                    m.put("allocateusername", datars.getRcd(i).getString("allocateusername"));
                    m.put("frombelongcompname", datars.getRcd(i).getString("frombelongcompname"));
                    m.put("tobelongcompname", datars.getRcd(i).getString("tobelongcompname"));
                    m.put("tolocname", datars.getRcd(i).getString("tolocname"));
                    m.put("tolocdtl", datars.getRcd(i).getString("tolocdtl"));
                    m.put("mark", datars.getRcd(i).getString("mark"));
                    m.put("outrq", datars.getRcd(i).getString("outrq"));
                    m.put("inrq", datars.getRcd(i).getString("inrq"));

                    String sqlassets="select "+ AssetsConstant.resSqlbody +" t.zc_cnt,t.name,t.uuid from res_allocate_item a ,res t where a.dr='0' and a.resid =t.id and a.busuuid =?";
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

