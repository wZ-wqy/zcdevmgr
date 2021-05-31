package com.dt.module.zc.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.dt.module.zc.entity.ResCollectionreturnItem;
import com.dt.module.zc.service.IResCollectionreturnItemService;
import org.springframework.beans.factory.annotation.Autowired;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dt.core.tool.util.DbUtil;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.tool.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import com.dt.core.common.base.BaseController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-08-23
 */
@Controller
@RequestMapping("/api/zc/resCollectionreturnItem")
public class ResCollectionreturnItemController extends BaseController {


    @Autowired
    IResCollectionreturnItemService ResCollectionreturnItemServiceImpl;


    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(ResCollectionreturnItemServiceImpl.removeById(id));
    }

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(ResCollectionreturnItemServiceImpl.getById(id));
    }

    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(ResCollectionreturnItem entity) {
        return R.SUCCESS_OPER(ResCollectionreturnItemServiceImpl.save(entity));
    }

    @ResponseBody
    @Acl(info = "根据Id更新", value = Acl.ACL_USER)
    @RequestMapping(value = "/updateById.do")
    public R updateById(ResCollectionreturnItem entity) {
        return R.SUCCESS_OPER(ResCollectionreturnItemServiceImpl.updateById(entity));
    }

    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResCollectionreturnItem entity) {
        return R.SUCCESS_OPER(ResCollectionreturnItemServiceImpl.saveOrUpdate(entity));
    }


    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        return R.SUCCESS_OPER(ResCollectionreturnItemServiceImpl.list(null));
    }

    @ResponseBody
    @Acl(info = "查询所有,有分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectPage.do")
    public R selectPage(String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10") String pageSize, @RequestParam(value = "pageIndex", required = true, defaultValue = "1") String pageIndex) {
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        QueryWrapper<ResCollectionreturnItem> ew = new QueryWrapper<ResCollectionreturnItem>();

        IPage<ResCollectionreturnItem> pdata = ResCollectionreturnItemServiceImpl.page(new Page<ResCollectionreturnItem>(pageindex, pagesize), ew);
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", pdata.getTotal());
        retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
        retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        return R.clearAttachDirect(retrunObject);
    }


}

