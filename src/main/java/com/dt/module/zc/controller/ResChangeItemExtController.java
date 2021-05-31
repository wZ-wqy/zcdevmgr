package com.dt.module.zc.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zc/resChangeItem/ext")
public class ResChangeItemExtController extends BaseController {


    /**
     * @Description:填充数据
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/fillctdata.do")
    public R fillctdata() {
        return R.SUCCESS_OPER();
    }

}
