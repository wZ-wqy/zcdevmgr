package com.dt.module.zc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.entity.ResPurchase;
import com.dt.module.zc.service.IResPurchaseService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zc/resPurchase/ext")
public class ResPurchaseExtController  extends BaseController {


    @Autowired
    IResPurchaseService ResPurchaseServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;


    /**
     * @Description:查询采购数据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id,String busid) {

        if(ToolUtil.isNotEmpty(busid)){
            QueryWrapper<ResPurchase> q=new QueryWrapper<>();
            q.eq("busid",busid);
            return R.SUCCESS_OPER(ResPurchaseServiceImpl.getOne(q));
        }else{
            return R.SUCCESS_OPER(ResPurchaseServiceImpl.getById(id));
        }

    }

    /**
     * @Description:查询采购数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        QueryWrapper<ResPurchase> q=new QueryWrapper<>();
        q.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResPurchaseServiceImpl.list(q));
    }

    /**
     * @Description:查询采购单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResPurchase entity) {
        if(ToolUtil.isEmpty(entity.getId())){
            entity.setBusid(assetsOperService.createUuid(AssetsConstant.UUID_PURCHASE));
            entity.setStatus("apply");
        }
        return R.SUCCESS_OPER(ResPurchaseServiceImpl.saveOrUpdate(entity));
    }

    /**
     * @Description:查询采购流程审批
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/approval.do")
    public R approval(String busid) {
        QueryWrapper<ResPurchase> q=new QueryWrapper<>();
        q.eq("busid",busid);
        ResPurchase obj=ResPurchaseServiceImpl.getOne(q);
        if(!"apply".equals(obj.getStatus())){
            return R.SUCCESS_OPER("当前状态不能送审");
        }
        JSONObject res=new JSONObject();
        res.put("ifsp","1");
        res.put("busid",busid);
        res.put("formtype","none");
        res.put("title",obj.getName());
        res.put("ptype", AssetsConstant.ASSETS_BUS_TYPE_RES_PURCHASE);
        res.put("psubtype", AssetsConstant.ASSETS_BUS_TYPE_RES_PURCHASE);
        res.put("processdefid",obj.getName());
        RcdSet rs=db.query("select * from sys_process_def where dr='0' and owner in (select id from ct_category where code='ZCCG' and dr='0')");
        if(rs.size()!=1){
            return R.FAILURE("未找到流程模版或无法正确匹配到流程模版");
        }
        res.put("processdefid",rs.getRcd(0).getString("id"));
        return R.SUCCESS_OPER(res);

    }



}
