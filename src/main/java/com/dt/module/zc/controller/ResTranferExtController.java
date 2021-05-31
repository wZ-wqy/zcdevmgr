package com.dt.module.zc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Delete;
import com.dt.core.dao.sql.Insert;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.flow.service.impl.FlowService;
import com.dt.module.zc.entity.ResTranfer;
import com.dt.module.zc.service.IResTranferService;
import com.dt.module.zc.service.impl.ResTranferService;
import com.dt.module.zc.service.impl.AssetsConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zc/resTranfer/ext")
public class ResTranferExtController extends BaseController {


    @Autowired
    ResTranferService resTranferService;

    @Autowired
    IResTranferService ResTranferServiceImpl;

    /**
     * @Description:查询资产转移
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<ResTranfer> q=new QueryWrapper<ResTranfer>();
        q.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResTranferServiceImpl.list(q));
    }

    /**
     * @Description:更新资产转移单据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/save.do")
    public R save(ResTranfer entity, String items) {
        return resTranferService.save(entity,items);
    }

    /**
     * @Description:资产转移审批
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/approval.do")
    public R approval(String busid) {
        QueryWrapper<ResTranfer> q=new QueryWrapper<>();
        q.eq("busid",busid);
        JSONObject res=new JSONObject();
        ResTranfer obj=ResTranferServiceImpl.getOne(q);

        if(!"apply".equals(obj.getStatus())){
            return R.SUCCESS_OPER("当前状态不能送审");
        }
        String catid=obj.getTransfercatid();

        RcdSet rs2= db.query("select *\n" +
                "from (\n" +
                "         select case when a.tusedpartid is null then '' else a.tusedpartid end tusedpartid,\n" +
                "                case when b.part_id is null then '' else b.part_id end         part_id\n" +
                "         from res_tranfer_item a,\n" +
                "              res b\n" +
                "         where a.resid = b.id\n" +
                "           and a.dr = '0'\n" +
                "           and a.busid = ?) t\n" +
                "where t.tusedpartid <> t.part_id",busid);


        if(rs2.size()>0){
            catid=ResTranferService.CAT_CROSSDEP;
            //sys_approval_meta
            //如果是跨部门转移，则新增转移部门
            Delete dls =new Delete("sys_approval_meta");
            dls.where().and("busid=?",busid);

            Insert ins=new Insert("sys_approval_meta");
            ins.set("id", ToolUtil.getUUID());
            ins.set("dr","0");
            ins.set("busid",busid);
            ins.setIf("nodeid",obj.getTusedpartid());
            db.executes(dls.getSQL(),ins.getSQL());

        }else{
            catid=ResTranferService.CAT_INTERDEP;
        }


        Insert ins2=new Insert("sys_approval_meta");
        ins2.set("id", ToolUtil.getUUID());
        ins2.set("dr","0");
        ins2.setIf("userid",obj.getReceiveruserid());
        ins2.setIf("username",obj.getReceiverusername());
        ins2.set("busid",busid);
     //   ins2.set("provider", FlowService.FIX_APPROVAL_ROLE);
        db.execute(ins2);


        res.put("ifsp","1");
        res.put("busid",busid);
        res.put("formtype","none");
        res.put("title",obj.getName());
        res.put("ptype", AssetsConstant.ASSETS_BUS_TYPE_ZY);
        res.put("psubtype", AssetsConstant.ASSETS_BUS_TYPE_MYZY);
        String code= AssetsConstant.ASSETS_BUS_TYPE_ZY;
        String tpl="";
        String sql="select id from sys_process_def where dr='0' and ptplname=? and\n" +
                " owner in (select id from ct_category where code=? and dr='0')";
        if(ResTranferService.CAT_INTERDEP.equals(catid)){
            tpl="TransDepSingleFlow";
        }else if(ResTranferService.CAT_CROSSDEP.equals(catid)){
            tpl="TransDepFlow";
        }
        System.out.println("tpl:"+tpl);
        RcdSet rs=db.query(sql,tpl,code);
        if(rs.size()!=1){
            return R.FAILURE("未找到流程模版或无法正确匹配到流程模版");
        }
        res.put("processdefid",rs.getRcd(0).getString("id"));
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:根据业务ID查询资产转移单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByBusid.do")
    public R selectByBusid(String busid) {
        return resTranferService.selectByBusid(busid);
    }


}
