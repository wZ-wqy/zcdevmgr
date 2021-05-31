package com.dt.module.zc.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResCollectionreturn;
import com.dt.module.zc.service.IResCollectionreturnItemService;
import com.dt.module.zc.service.IResCollectionreturnService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.ResCollectionreturnService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/api/zc/resCollectionreturn/ext")
public class ResCollectionreturnExtController extends BaseController {

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResCollectionreturnService ResCollectionreturnServiceImpl;

    @Autowired
    IResCollectionreturnItemService ResCollectionreturnItemServiceImpl;

    @Autowired
    ResCollectionreturnService resCollectionreturnService;

    /**
     * @Description:生产领用单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResCollectionreturn entity, String items) {
        return resCollectionreturnService.save(entity, items);
    }

    /**
     * @Description:查询单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        return resCollectionreturnService.selectList(null, null, null,search);
    }

    /**
     * @Description:取消领用申请
     */
    @ResponseBody
    @Acl(info = "取消", value = Acl.ACL_USER)
    @RequestMapping(value = "/cancel.do")
    public R cancel(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        ResCollectionreturn obj = ResCollectionreturnServiceImpl.getById(id);
        if(FlowConstant.PSTATUS_APPLY.equals(obj.getStatus())){
            UpdateWrapper<ResCollectionreturn> u=new UpdateWrapper<>();
            u.set("status",FlowConstant.PSTATUS_CANCEL);
            u.eq("id",id);
            ResCollectionreturnServiceImpl.update(u);
            UpdateWrapper<Res> u2=new UpdateWrapper<>();
            u2.set("inprocess","0");
            u2.inSql("id","select resid from res_collectionreturn_item where dr='0' and busuuid='"+obj.getBusuuid()+"'");
            ResServiceImpl.update(u2);
        }else{
            return R.FAILURE("当前单据状态错误，作废操作失败");
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询我的领用
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/myList.do")
    public R myList(String statustype, String bustype) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        return resCollectionreturnService.selectList(getUserId(), statustype, bustype,search);
    }

    /**
     * @Description:查询领用单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByUuid.do")
    public R selectByUuid(String uuid) {
        return resCollectionreturnService.selectByBusid(uuid);
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
        String sql = "select t.*,date_format(busdate,'%Y-%m-%d') busdatestr,date_format(returndate,'%Y-%m-%d') returndatestr,date_format(rreturndate,'%Y-%m-%d') rreturndatestr  from res_collectionreturn t where t.dr='0' and t.busuuid in (#<UUID>#)";
        JSONArray data_arr = JSONArray.parseArray(data);
        String struuid = "";
        for (int i = 0; i < data_arr.size(); i++) {
            struuid = struuid + "'" + data_arr.getString(i) + "',";
        }
        struuid = struuid + "'-1'";
        String fname = "";
        RcdSet datars = db.query(sql.replaceFirst("#<UUID>#", struuid));
        String filePath = ToolUtil.getRealPathInWebApp("") + "tpl" + File.separatorChar + fname;
        File file = new File(filePath);

     //   if (file.exists()) {
        for (int i = 0; i < datars.size(); i++) {
            try {
                String busid=datars.getRcd(i).getString("busuuid");
                String bustype=datars.getRcd(i).getString("bustype");
                if(AssetsConstant.ASSETS_BUS_TYPE_LY.equals(bustype)){
                    fname="assetsly.docx";
                }else if(AssetsConstant.ASSETS_BUS_TYPE_TK.equals(bustype)){
                    fname="assetstk.docx";
                }
                HashMap<String, Object> m = new HashMap<String, Object>();
                String status=datars.getRcd(i).getString("status");
                if(FlowConstant.PSTATUS_FINISH_NO_APPROVAL.equals(status)||FlowConstant.PSTATUS_FINISH.equals(status)){
                    m.put("status", "完成");
                }else if(FlowConstant.PSTATUS_CANCEL.equals(status)){
                    m.put("status", "取消");
                }else{
                    m.put("status", "未完成");
                }
                m.put("name", datars.getRcd(i).getString("name"));
                m.put("busid", busid);
                m.put("crusername", datars.getRcd(i).getString("crusername"));
                m.put("processusername", datars.getRcd(i).getString("processusername"));
                m.put("tlocdtl", datars.getRcd(i).getString("tlocdtl"));
                m.put("busdate", datars.getRcd(i).getString("busdatestr"));
                m.put("returndate", datars.getRcd(i).getString("returndatestr"));
                m.put("rreturndate", datars.getRcd(i).getString("rreturndatestr"));
                m.put("mark", datars.getRcd(i).getString("mark"));
                m.put("tpartname",datars.getRcd(i).getString("tpartname"));

                String sqlassets="select "+ AssetsConstant.resSqlbody +" t.zc_cnt,t.name,t.uuid from res_collectionreturn_item a ,res t where a.dr='0' and a.resid =t.id and a.busuuid =?";
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
//        }else{
//            System.out.println("tpl file not exists");
//        }
        zipOutputStream.close();
        outputStream.flush();
        outputStream.close();

    }

}
