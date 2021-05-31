package com.dt.module.ct.controller;


import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.ct.service.impl.DocMgrService;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dt.module.ct.entity.DocMgr;
import com.dt.module.ct.service.IDocMgrService;
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
 * @since 2020-09-03
 */
@Controller
@RequestMapping("/api/ct/docMgr/ext")
public class DocMgrExtController extends BaseController {


    @Autowired
    IDocMgrService DocMgrServiceImpl;

    @Autowired
    DocMgrService docMgrService;

    /**
     * @Description 查询所有
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        return docMgrService.listDoc(search);
    }

    /**
     * @Description 存在则更新,否则插入
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(DocMgr entity) {
        return R.SUCCESS_OPER(DocMgrServiceImpl.saveOrUpdate(entity)
        );
    }


}

