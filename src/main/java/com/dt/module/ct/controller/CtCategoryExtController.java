package com.dt.module.ct.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.ct.entity.CtCategory;
import com.dt.module.ct.service.ICtCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2018-07-30
 */
@Controller
@RequestMapping("/api/ctCategory/ext")
public class CtCategoryExtController extends BaseController {


    @Autowired
    ICtCategoryService CtCategoryServiceImpl;

    /**
     * @Description:根据根节点查询所以类目
     * @param root
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectListByRoot.do")
    public R selectList(String root) {
        QueryWrapper<CtCategory> ew = new QueryWrapper<CtCategory>();
        ew.and(i -> i.eq("root", root)).orderByAsc("route_name");
        return R.SUCCESS_OPER(CtCategoryServiceImpl.list(ew));
    }


    /**
     * @Description:查询所有数据
     */
    @ResponseBody
    @Acl(info = "查询所有,有分页", value = Acl.ACL_DENY)
    @RequestMapping(value = "/selectPage.do")
    public R selectPage(String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10") String pageSize, @RequestParam(value = "pageIndex", required = true, defaultValue = "1") String pageIndex) {
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        QueryWrapper<CtCategory> ew = new QueryWrapper<CtCategory>();

        IPage<CtCategory> pdata = CtCategoryServiceImpl.page(new Page<CtCategory>(pageindex, pagesize), ew);
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", pdata.getTotal());
        retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
        retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        return R.clearAttachDirect(retrunObject);
    }


}

