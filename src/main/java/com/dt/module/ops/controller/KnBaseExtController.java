package com.dt.module.ops.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.ct.entity.CtCategory;
import com.dt.module.ct.service.ICtCategoryService;
import com.dt.module.ops.entity.KnBase;
import com.dt.module.ops.entity.KnBaseUser;
import com.dt.module.ops.service.IKnBaseService;
import com.dt.module.ops.service.IKnBaseUserService;
import com.dt.module.ops.service.impl.KnBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequestMapping("/api/ops/knBase/ext")
public class KnBaseExtController extends BaseController {

    @Autowired
    IKnBaseService KnBaseServiceImpl;

    @Autowired
    IKnBaseUserService KnBaseUserServiceImpl;

    @Autowired
    ICtCategoryService CtCategoryServiceImpl;

    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/listCategory.do")
    public R listCategory( ) {
        QueryWrapper<CtCategory> ct=new QueryWrapper<CtCategory>();
        ct.eq("isaction","Y");
        ct.eq("root", CategoryEnum.CATEGORY_KN.getValue());
        ct.orderByDesc("route_name");
        return R.SUCCESS_OPER(CtCategoryServiceImpl.list(ct));
    }

    @ResponseBody
    @Acl(info = "查询所有,有分页", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/selectPageSearch.do")
    public R selectPageSearch(String fullsearch,String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10")  String pageSize,@RequestParam(value = "pageIndex", required = true, defaultValue = "1")  String pageIndex) {
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        QueryWrapper<KnBase> ew = new QueryWrapper<KnBase>();
        ew.eq("isshow","1");
        if(ToolUtil.isNotEmpty(fullsearch)){
            ew.apply("MATCH (title,label,ct) AGAINST ('"+fullsearch.trim()+"' IN BOOLEAN MODE)");
        }
        ew.select("type","isshow","reviewcnt","lasttime","inserttime","id","catid","catname","title","profile","label","attach","lusername","shareurl");
        ew.orderByDesc("update_time");
        IPage<KnBase> pdata = KnBaseServiceImpl.page(new Page<KnBase>(pageindex, pagesize), ew);
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", pdata.getTotal());
        retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
        retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        return R.clearAttachDirect(retrunObject);
    }

    @ResponseBody
    @Acl(info = "查询所有,有分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectPage.do")
    public R selectPage(String search,String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10")  String pageSize,@RequestParam(value = "pageIndex", required = true, defaultValue = "1")  String pageIndex) {
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String dtsearch=ps.getString("search[value]");
        String catid=ps.getString("catid");
        String type=ps.getString("type");
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        QueryWrapper<KnBase> ew = new QueryWrapper<KnBase>();
        ew.like(ToolUtil.isNotEmpty(search),"title",search);
        ew.like(ToolUtil.isNotEmpty(dtsearch),"title",dtsearch);
        ew.eq(ToolUtil.isNotEmpty(type),"type", type);
        ew.inSql(ToolUtil.isNotEmpty(catid),"catid"," select id from ct_category where dr='0' and concat('-',route)  like '%-"+catid+"-%' or id= "+catid);
        ew.select("type","isshow","reviewcnt","lasttime","inserttime","id","catid","catname","title","profile","label","attach","lusername","shareurl");
        ew.orderByDesc("update_time");
        IPage<KnBase> pdata = KnBaseServiceImpl.page(new Page<KnBase>(pageindex, pagesize), ew);
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", pdata.getTotal());
        retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
        retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        return R.clearAttachDirect(retrunObject);
    }


    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        UpdateWrapper<KnBase> u=new UpdateWrapper<>();
        u.setSql("reviewcnt=reviewcnt+1");
        u.eq("id",id);
        KnBaseServiceImpl.update(u);
        return R.SUCCESS_OPER(KnBaseServiceImpl.getById(id));
    }


    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/save.do")
    public R save(KnBase entity) {
        if(ToolUtil.isNotEmpty(entity.getId())){
            KnBaseUser ur=new KnBaseUser();
            ur.setUserid(this.getUserId());
            ur.setUsername(this.getName());
            ur.setKnbaseid(entity.getId());
            KnBaseUserServiceImpl.save(ur);
        }else{
            entity.setReviewcnt(new BigDecimal(0));
            entity.setInserttime(new Date());
        }
        entity.setLasttime(new Date());
        entity.setLuserid(this.getUserId());
        entity.setLusername(this.getName());
        return R.SUCCESS_OPER(KnBaseServiceImpl.saveOrUpdate(entity));
    }


}
