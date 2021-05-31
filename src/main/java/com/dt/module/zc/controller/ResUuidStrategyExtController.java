package com.dt.module.zc.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import com.dt.module.zc.entity.ResUuidStrategy;
import com.dt.module.zc.service.IResUuidStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zc/resUuidStrategy/ext")
public class ResUuidStrategyExtController {
    @Autowired
    IResUuidStrategyService ResUuidStrategyServiceImpl;

    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/setDefault.do")
    public R setDefault(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        UpdateWrapper<ResUuidStrategy> u=new UpdateWrapper<>();
        u.set("def","0");
        UpdateWrapper<ResUuidStrategy> u1=new UpdateWrapper<>();
        u1.set("def","1");
        u1.eq("id",id);
        ResUuidStrategyServiceImpl.update(u);
        ResUuidStrategyServiceImpl.update(u1);
        return R.SUCCESS_OPER();
    }

}
