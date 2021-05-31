package com.dt.module.wx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.wx.service.WxConfigService;
import com.dt.module.wx.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api")
@Configuration
@PropertySource(value = "classpath:config.properties")
public class WxConfigController extends BaseController {


    @Autowired
    private WxConfigService wxConfigService;

    @Autowired
    private WxService wxService;

    /**
     * @Description:查询所有的tickets数据
     */
    @ResponseBody
    @Acl(info = "查询所有的tickets数据", value = Acl.ACL_ALLOW)
    @RequestMapping("/wx/queryMapTickets.do")
    public R queryMapTickets() {
        return wxService.queryMapTickets();
    }


    /**
     * @Description:从本地配置中获取app的token数据
     */
    @ResponseBody
    @Acl(info = "从本地配置中获取app的token数据", value = Acl.ACL_ALLOW)
    @RequestMapping("/wx/getAccessToken.do")
    public R getAccessToken() {
        return wxService.queryAccessToken();
    }


    /**
     * @Description:从本地配置中获取app配置信息
     */
    @ResponseBody
    @Acl(info = "从本地配置中获取app配置信息", value = Acl.ACL_ALLOW)
    @RequestMapping("/wx/getConfig.do")
    public R getConfig(String url) {
        return wxConfigService.queryWxConfig(url);
    }

}
