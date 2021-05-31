package com.dt.module.flow.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2019-12-03
 */
@Controller
@RequestMapping("/api/flow/sysProcessData/ext")
public class SysProcessDataExtController extends BaseController {

    @Autowired
    ISysProcessDataService SysProcessDataServiceImpl;

    /**
     * @Description:根据ID删除数据
     * @param id
     */
    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(SysProcessDataServiceImpl.removeById(id));
    }


    /**
     * @Description:根据业务ID查询数据
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByBusinessId.do")
    public R selectById(@RequestParam(value = "businessid", required = true, defaultValue = "") String businessid) {
        QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
        qw.eq("busid", businessid);
        SysProcessData r = SysProcessDataServiceImpl.getOne(qw);
        if (r == null) {
            return R.FAILURE_NO_DATA();
        } else {
            return R.SUCCESS_OPER(r);
        }

    }

    /**
     * @Description:根据业务ID查询数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectMyList.do")
    public R selectListByMy(String sdate, String edate, String type) {
        QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
        System.out.println(this.getUserId());
        qw.eq("pstartuserid", this.getUserId());
        qw.isNotNull("ptype");
        if (ToolUtil.isNotEmpty(sdate)) {
            qw.ge("create_time", sdate);
        }
        if (ToolUtil.isNotEmpty(edate)) {
            qw.le("create_time", edate);
        }
        if (ToolUtil.isNotEmpty(type)) {
            JSONArray type_arr = JSONArray.parseArray(type);
            if (type_arr.size() > 0) {
                List<String> colsv = new ArrayList<String>();
                for (int i = 0; i < type_arr.size(); i++) {
                    colsv.add(type_arr.getString(i));
                }
                qw.in("ptype", colsv);
            }
        }
        qw.orderByDesc("create_time");
        return R.SUCCESS_OPER(SysProcessDataServiceImpl.list(qw));

    }

    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList(String sdate, String edate, String type) {
        QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
        qw.isNotNull("ptype");
        if (ToolUtil.isNotEmpty(sdate)) {
            qw.ge("create_time", sdate);
        }
        if (ToolUtil.isNotEmpty(edate)) {
            qw.le("create_time", edate);
        }
        if (ToolUtil.isNotEmpty(type)) {
            JSONArray type_arr = JSONArray.parseArray(type);
            if (type_arr.size() > 0) {
                List<String> colsv = new ArrayList<String>();
                for (int i = 0; i < type_arr.size(); i++) {
                    colsv.add(type_arr.getString(i));
                }
                qw.in("ptype", colsv);
            }
        }
        qw.orderByDesc("create_time");
        return R.SUCCESS_OPER(SysProcessDataServiceImpl.list(qw));
    }

}
