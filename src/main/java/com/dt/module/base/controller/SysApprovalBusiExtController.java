package com.dt.module.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysApprovalBusi;
import com.dt.module.base.service.ISysApprovalBusiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/base/sysApprovalBusi/ext")
public class SysApprovalBusiExtController extends BaseController {


    @Autowired
    ISysApprovalBusiService SysApprovalBusiServiceImpl;

    @ResponseBody
    @Acl(info = "根据code查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByCode.do")
    public R selectByCode(@RequestParam(value = "code", required = true, defaultValue = "") String code) {

        QueryWrapper<SysApprovalBusi> q=new QueryWrapper<SysApprovalBusi>();
        q.eq("code",code);
        SysApprovalBusi obj=SysApprovalBusiServiceImpl.getOne(q);
        if(obj==null){
            return R.FAILURE_NO_DATA();
        }else{
            return R.SUCCESS_OPER(obj);
        }
    }
}
