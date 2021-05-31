package com.dt.module.zbx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zbx.service.impl.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zbx/template")
public class TemplateController extends BaseController {

    @Autowired
    TemplateService templateService;

    /**
     * @Description:获取所有模版
     */
    @ResponseBody
    @Acl(info = "获取所有模版", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getTemplate.do")
    public R getTemplate(String templates) {
        return templateService.getTemplate(templates);
    }


}
