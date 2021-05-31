package com.dt.module.zbx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zbx.service.impl.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zbx/dashboard")
public class DashBoardController extends BaseController {

    @Autowired
    DashBoardService dashBoardService;

    /**
     * @Description:获取主机数量
     */
    @ResponseBody
    @Acl(info = "获取主机数量", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getCountHost.do")
    public R getCountHost() {
        return dashBoardService.getCountHost();
    }


}
