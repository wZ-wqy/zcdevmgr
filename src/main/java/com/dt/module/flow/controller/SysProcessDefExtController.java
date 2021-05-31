package com.dt.module.flow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.flow.entity.SysProcessDef;
import com.dt.module.flow.service.ISysProcessDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2019-12-16
 */
@Controller
@RequestMapping("/api/flow/sysProcessDef/ext")
public class SysProcessDefExtController extends BaseController {

    @Autowired
    ISysProcessDefService SysProcessDefServiceImpl;

    /**
     * @Description:根据拥有者查询数据
     * @param owner
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList(String owner) {
        QueryWrapper<SysProcessDef> ew = new QueryWrapper<SysProcessDef>();
        ew.eq("owner", owner);
        return R.SUCCESS_OPER(SysProcessDefServiceImpl.list(ew));

    }

}
