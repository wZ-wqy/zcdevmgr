package com.dt.module.zc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.zc.entity.ResInspection;
import com.dt.module.zc.entity.ResInspectionPitem;
import com.dt.module.zc.service.IResInspectionPitemService;
import com.dt.module.zc.service.IResInspectionService;
import com.dt.module.zc.service.impl.AssetsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/zc/resInspection/ext")
public class ResInspectionExtController extends BaseController {

    @Autowired
    IResInspectionService ResInspectionServiceImpl;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResInspectionPitemService ResInspectionPitemServiceImpl;


    /**
     * @Description:查询巡检
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String busid = ps.getString("busid");
        ResInspection obj=null;
        if(ToolUtil.isEmpty(busid)){
            obj = ResInspectionServiceImpl.getById(id);
            busid=obj.getBusid();
        }else{
            QueryWrapper<ResInspection> qw=new QueryWrapper<ResInspection>();
            qw.eq("busid",busid);
            obj=ResInspectionServiceImpl.getOne(qw);
        }
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat));
        String sql = "select  b.actiontime inspectactiontime,b.actionusername inspectusername, b.pics inspectpics,b.status inspectstatus,b.mark inspectmark," + AssetsConstant.resSqlbody + " t.* from res t,res_inspection_pitem b where t.id=b.resid and t.dr='0' and b.dr='0' and b.busid=?";
        String sql2="select (select count(1) cnt from res_inspection_pitem where dr='0' and busid=?) cnt,\n" +
                "(select count(1) cnt from res_inspection_pitem where dr = '0' and busid = ? and status = 'success')success_cnt,\n" +
                "(select count(1) cnt from res_inspection_pitem where dr = '0' and busid = ? and status = 'wait')wait_cnt,\n" +
                "(select count(1) cnt from res_inspection_pitem where dr = '0' and busid = ? and status = 'failed')failed_cnt";
        res.put("statistics",ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql2,busid,busid,busid,busid)));
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql, busid).toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询巡检列表
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<ResInspection> qw = new QueryWrapper<ResInspection>();
        qw.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResInspectionServiceImpl.list(qw));
    }


    /**
     * @Description:查询我的巡检
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/myList.do")
    public R myList(String statustype) {
        QueryWrapper<ResInspection> qw = new QueryWrapper<ResInspection>();
        qw.inSql("busid","select busid from res_inspection_user where userid='"+this.getUserId()+"'");
        if (ToolUtil.isNotEmpty(statustype)) {
            if ("finish".equals(statustype)) {
                qw.eq("status","finish");
            } else if ("inprogress".equals(statustype)) {
                qw.ne("status", "finish");
            }
        }
        qw.orderByDesc("create_time");
        List<ResInspection> list = ResInspectionServiceImpl.list(qw);
        return R.SUCCESS_OPER(list);
    }

    /**
     * @Description:开始巡检
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/startInspect.do")
    public R startInspect(String busid) {
        QueryWrapper<ResInspection> inspectqw = new QueryWrapper<ResInspection>();
        inspectqw.eq("busid",busid);
        ResInspection resinspectorder=ResInspectionServiceImpl.getOne(inspectqw);
        if("wait".equals(resinspectorder.getStatus())){
            UpdateWrapper<ResInspection> order = new UpdateWrapper<ResInspection>();
            order.set("status", "acting");
            order.setSql("sdate=now()");
            order.eq("busid", busid);
            ResInspectionServiceImpl.update(order);
        }else if("finish".equals(resinspectorder.getStatus())){
            return R.FAILURE("当前巡检已结束");
        }else{
            return R.SUCCESS_OPER(resinspectorder.getStatus());
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:结束巡检
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/finishInspect.do")
    public R finishInspect(String busid,String force) {

        QueryWrapper<ResInspection> inspectqw = new QueryWrapper<ResInspection>();
        inspectqw.eq("busid",busid);
        ResInspection resinspectorder=ResInspectionServiceImpl.getOne(inspectqw);
        if("wait".equals(resinspectorder.getStatus())){
            return R.FAILURE("未开始巡检");
        }
        if("finish".equals(resinspectorder.getStatus())){
            return R.FAILURE("已结束巡检");
        }
        if(!"acting".equals(resinspectorder.getStatus())){
            return R.FAILURE("单据状态异常");
        }

        ResInspectionPitem obj=new ResInspectionPitem();
        String sql2="select (select count(1) cnt from res_inspection_pitem where dr='0' and busid=?) cnt,\n" +
                "(select count(1) cnt from res_inspection_pitem where dr = '0' and busid = ? and status = 'success')success_cnt,\n" +
                "(select count(1) cnt from res_inspection_pitem where dr = '0' and busid = ? and status = 'wait')wait_cnt,\n" +
                "(select count(1) cnt from res_inspection_pitem where dr = '0' and busid = ? and status = 'failed')failed_cnt";
        Rcd rs=db.uniqueRecord(sql2,busid,busid,busid,busid);
        if("fix".equals(resinspectorder.getMethod())){
           if(rs.getInteger("wait_cnt")>0){
               return R.FAILURE("请先完成剩下部分资产巡检!");
           }
        }
        UpdateWrapper<ResInspection> order = new UpdateWrapper<ResInspection>();
        order.set("status", "finish");
        order.set("cnt", rs.getString("cnt"));
        order.set("normalcnt", rs.getString("success_cnt"));
        order.set("faultcnt", rs.getString("failed_cnt"));
        order.set("actingcnt", rs.getString("wait_cnt"));
        order.setSql("edate=now()");
        order.eq("busid", busid);
        ResInspectionServiceImpl.update(order);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:巡检操作
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/actionInspect.do")
    public R actionInspect(String busid,String uuid,String status,String mark,String pics,String loc) {

        QueryWrapper<ResInspection> inspectqw = new QueryWrapper<ResInspection>();
        inspectqw.eq("busid",busid);
        ResInspection resinspectorder=ResInspectionServiceImpl.getOne(inspectqw);
        ResInspectionPitem obj=new ResInspectionPitem();

        QueryWrapper<Res> resqw = new QueryWrapper<Res>();
        resqw.eq("uuid",uuid);
        Res res=ResServiceImpl.getOne(resqw);
        if(res==null){
            return R.FAILURE("无该资产信息");
        }
        String resid=res.getId();
        //status: success,failed
        if("free".equals(resinspectorder.getMethod())){
            QueryWrapper<ResInspectionPitem> itemqw = new QueryWrapper<ResInspectionPitem>();
            itemqw.eq("busid",busid);
            itemqw.eq("resid",resid);
            if(ResInspectionPitemServiceImpl.getOne(itemqw)!=null){
                return R.FAILURE("本批次所选的当前资产已做过巡检");
            }
            obj.setBusid(busid);
            obj.setType("instance");
            obj.setMark(mark);
            obj.setLoc(loc);
            obj.setPics(pics);
            obj.setActiontime(new Date());
            obj.setActionuserid(this.getUserId());
            obj.setActionusername(this.getName());
            obj.setStatus(status);
            obj.setResid(resid);
        }else if("fix".equals(resinspectorder.getMethod())){
            QueryWrapper<ResInspectionPitem> itemqw = new QueryWrapper<ResInspectionPitem>();
            itemqw.eq("busid",busid);
            itemqw.eq("resid",resid);
            obj=ResInspectionPitemServiceImpl.getOne(itemqw);
            if (obj==null){
                return R.FAILURE("本批次巡检资产清单中不包含该资产");
            }

            if(!"wait".equals(obj.getStatus())){
                return R.FAILURE("本批次所选的当前资产已做过巡检");
            }
            obj.setMark(mark);
            obj.setLoc(loc);
            obj.setPics(pics);
            obj.setActiontime(new Date());
            obj.setActionuserid(this.getUserId());
            obj.setActionusername(this.getName());
            obj.setStatus(status);
        }
        ResInspectionPitemServiceImpl.saveOrUpdate(obj);
        return R.SUCCESS_OPER();
    }
}
