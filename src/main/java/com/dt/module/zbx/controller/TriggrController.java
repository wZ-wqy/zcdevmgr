package com.dt.module.zbx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zbx.service.impl.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zbx/trigger")
public class TriggrController extends BaseController {

    @Autowired
    TriggerService triggerService;

    /**
     * @Description:获取所有触发器
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getTriggers.do")
    public R getTriggers(String triggers) {
        return triggerService.getTriggers(triggers);
    }


}
