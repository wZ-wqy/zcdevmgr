package com.dt.module.zbx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zbx.service.impl.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zbx/host")
public class HostController extends BaseController {

    @Autowired
    HostService hostService;

    /**
     * @Description:根据主机列表
     */
    @ResponseBody
    @Acl(info = "根据主机列表", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/hostList.do")
    public R hostList(String hosts) {
        return hostService.hostList(hosts);
    }


}
