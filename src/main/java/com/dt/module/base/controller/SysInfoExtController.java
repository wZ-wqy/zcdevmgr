package com.dt.module.base.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.service.impl.SysInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/sysinfo/ext")
public class SysInfoExtController extends BaseController {

    @Autowired
    SysInfoService sysInfoService;

    /**
     * 记录系统信息
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/upload.do")
    public R upload(String ip, String hostname, String ct, String version, String app, String os, String uid) {
        return sysInfoService.uploadSysInfo(ip, hostname, version, app, ct, os, uid);
    }

    /**
     * 检查系统信息
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/check.do")
    public R check() {
        return sysInfoService.uploadSysInfo();
    }
}
