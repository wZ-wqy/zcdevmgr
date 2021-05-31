package com.dt.module.zbx.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zbx.entity.ZbxObjectItem;
import com.dt.module.zbx.service.IZbxObjectItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-08-01
 */
@Controller
@RequestMapping("/api/zbx/zbxObjectItem/ext")
public class ZbxObjectItemExtController extends BaseController {


    @Autowired
    IZbxObjectItemService ZbxObjectItemServiceImpl;


    /**
     * @Description:查询组
     */
    @ResponseBody
    @Acl(info = "根据Group查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByGroupId.do")
    public R selectByGroupId(@RequestParam(value = "groupid", required = true, defaultValue = "") String groupid) {
        QueryWrapper<ZbxObjectItem> ew = new QueryWrapper<ZbxObjectItem>();
        ew.and(i -> i.eq("groupid", groupid));
        return R.SUCCESS_OPER(ZbxObjectItemServiceImpl.list(ew));
    }


    /**
     * @Description:批量更新数据
     */
    @ResponseBody
    @Acl(info = "批量更新", value = Acl.ACL_USER)
    @RequestMapping(value = "/batchinsert.do")
    public R batchinsert(String items, String groupid) {
        ArrayList<ZbxObjectItem> list = new ArrayList<ZbxObjectItem>();
        System.out.println(items);
        JSONArray items_arr = JSONArray.parseArray(items);
        for (int i = 0; i < items_arr.size(); i++) {
            String objid = items_arr.getJSONObject(i).getString("objid");
            String objname = items_arr.getJSONObject(i).getString("objname");
            QueryWrapper<ZbxObjectItem> ew = new QueryWrapper<ZbxObjectItem>();
            ew.and(j -> j.eq("groupid", groupid).eq("objid", objid));
            if (ZbxObjectItemServiceImpl.list(ew).size() == 0) {
                ZbxObjectItem e = new ZbxObjectItem();
                e.setObjid(objid);
                e.setObjname(objname);
                e.setGroupid(groupid);
                list.add(e);
            }
        }
        if (list.size() > 0) {
            ZbxObjectItemServiceImpl.saveBatch(list);
        }
        return R.SUCCESS_OPER();
    }

}

