package com.dt.module.base.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysParams;
import com.dt.module.base.service.ISysParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/system")
public class SystemController extends BaseController {


    @Autowired
    ISysParamsService SysParamsServiceImpl;

    @ResponseBody
    @Acl(info = "获取系统版本", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/querySystemVersion.do")
    public R querySystemVersion() {
        SysParams obj=SysParamsServiceImpl.getById("version");
        if(obj!=null){
            return R.SUCCESS_OPER(obj);
        }else{
            SysParams r=new SysParams();
            r.setId("version");
            r.setName("应用版本");
            r.setType("system");
            r.setValue("0.0.0");
            SysParamsServiceImpl.saveOrUpdate(r);
            return R.SUCCESS_OPER(SysParamsServiceImpl.getById("version"));
        }
    }
}
