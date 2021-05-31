package com.dt.module.cmdb.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.cmdb.entity.ResAttrs;
import com.dt.module.cmdb.service.IResAttrsService;
import com.dt.module.ct.entity.CtCategory;
import com.dt.module.ct.service.ICtCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-06-17
 */
@Controller
@RequestMapping("/api/cmdb/resAttrs/ext")
public class ResAttrsExtController extends BaseController {


    @Autowired
    IResAttrsService ResAttrsServiceImpl;

    @Autowired
    ICtCategoryService CtCategoryServiceImpl;

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByCatId.do")
    public R selectById(@RequestParam(value = "catid", required = true, defaultValue = "") String catid) {
        QueryWrapper<ResAttrs> ew = new QueryWrapper<ResAttrs>();
        ew.and(i -> i.eq("catid", catid));
        ew.orderByAsc("sort");
        return R.SUCCESS_OPER(ResAttrsServiceImpl.list(ew));
    }

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectAllAttrByCatId.do")
    public R selectByCatIdAllAttr(@RequestParam(value = "catid", required = true, defaultValue = "") String catid) {
        CtCategory ct = CtCategoryServiceImpl.getById(catid);
        String route = ct.getRoute();
        String sql = "select t.* from res_attrs t where ifinheritable='1' and dr='0' and catid<>? and catid in (" + route.replaceAll("-", ",") + ") union all (select * from res_attrs where dr='0' and catid=? order by sort)";
        JSONArray res = ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql, catid, catid).toJsonArrayWithJsonObject());
        return R.SUCCESS_OPER(res);
    }

    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResAttrs entity) {

        if (ToolUtil.isEmpty(entity.getAttrcode())) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        entity.setAttrcode(entity.getAttrcode().trim().toLowerCase());
        return R.SUCCESS_OPER(ResAttrsServiceImpl.saveOrUpdate(entity));
    }


    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectInheritableAttrByCatId.do")
    public R selectInheritableAttrByCatId(@RequestParam(value = "catid", required = true, defaultValue = "") String catid) {

        CtCategory ct = CtCategoryServiceImpl.getById(catid);
        if (ct != null) {
            String route = ct.getRoute();
            BigDecimal nodelevel = ct.getNodeLevel();
            JSONArray res = new JSONArray();
            if (nodelevel.compareTo(new BigDecimal(1)) == 1) {
                String sql = "select (select route_name from ct_category where id=t.catid) catname,t.* from res_attrs t where ifinheritable='1' and dr='0' and catid in (" + route.replaceAll("-", ",").replace("," + catid, "") + ")";
                res = ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql).toJsonArrayWithJsonObject());
            }
            return R.SUCCESS_OPER(res);
        } else {
            return R.FAILURE_NO_DATA();
        }

    }


}

