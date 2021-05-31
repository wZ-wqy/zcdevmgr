package com.dt.module.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import com.dt.module.base.service.impl.ServerMonitorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/monitor/")
public class ServerMonitorController {

    /**
     * 查询服务器监控数据
     */
    @ResponseBody
    @Acl(info = "查询服务器监控数据", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/queryServerInfo.do")
    public R queryServerInfo() {
        JSONObject r = new JSONObject();
        r.put("mem", ServerMonitorService.getMemInfo());
        r.put("sysinfo", ServerMonitorService.getSysInfo());
        r.put("jvm", ServerMonitorService.getJvmInfo());
        r.put("sysfiles", ServerMonitorService.getSysFiles());
        r.put("cpu", ServerMonitorService.getCpuInfo());
        return R.SUCCESS_OPER(r);
    }

}
