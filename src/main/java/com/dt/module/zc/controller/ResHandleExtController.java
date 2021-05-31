package com.dt.module.zc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zc.entity.ResHandle;
import com.dt.module.zc.entity.ResTranfer;
import com.dt.module.zc.service.IResHandleService;
import com.dt.module.zc.service.impl.ResHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zc/resHandle/ext")
public class ResHandleExtController extends BaseController {

    @Autowired
    IResHandleService ResHandleServiceImpl;

    @Autowired
    ResHandleService resHandleService;

    /**
     * @Description:查询
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        return resHandleService.selectList(null,null);
    }


    /**
     * @Description:更新
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/save.do")
    public R save(ResHandle entity, String items) {
        return resHandleService.save(entity,items);
    }



    /**
     * @Description:根据业务ID查询资产转移单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByBusid.do")
    public R selectByBusid(String busid) {
        return resHandleService.selectData(busid,null);
    }


}
