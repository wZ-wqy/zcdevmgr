package com.dt.module.cmdb.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.cmdb.entity.ResLabelTpl;
import com.dt.module.cmdb.service.IResLabelTplService;
import com.dt.module.flow.entity.SysProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/cmdb/resLabelTpl/ext")
public class ResLabelTplExtController extends BaseController {

    @Autowired
    IResLabelTplService ResLabelTplServiceImpl;


    /**
     * @Description:设置默认标签
     */
    @ResponseBody
    @Acl(info = "设置默认标签", value = Acl.ACL_USER)
    @RequestMapping(value = "/setdef.do")
    public R setdef(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        UpdateWrapper<ResLabelTpl> uw = new UpdateWrapper<ResLabelTpl>();
        uw.set("ifdef", "0");
        ResLabelTplServiceImpl.update(uw);
        UpdateWrapper<ResLabelTpl> uw2 = new UpdateWrapper<ResLabelTpl>();
        uw2.set("ifdef", "1");
        uw2.eq("id", id);
        ResLabelTplServiceImpl.update(uw2);
        return R.SUCCESS_OPER();
    }

}
