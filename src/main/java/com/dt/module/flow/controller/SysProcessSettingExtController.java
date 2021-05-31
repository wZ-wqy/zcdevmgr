package com.dt.module.flow.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.flow.entity.SysProcessDef;
import com.dt.module.flow.entity.SysProcessSetting;
import com.dt.module.flow.service.ISysProcessDefService;
import com.dt.module.flow.service.ISysProcessSettingService;
import com.dt.module.form.entity.SysForm;
import com.dt.module.form.service.ISysFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-04-07
 */
@Controller
@RequestMapping("/api/flow/sysProcessSetting/ext")
public class SysProcessSettingExtController extends BaseController {


    @Autowired
    ISysProcessSettingService SysProcessSettingServiceImpl;

    @Autowired
    ISysFormService SysFormServiceImpl;

    @Autowired
    ISysProcessDefService SysProcessDefServiceImpl;

    /**
     * @Description:根据Code查询数据
     * @param code
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByCode.do")
    public R selectById(@RequestParam(value = "code", required = true, defaultValue = "") String code) {
        QueryWrapper<SysProcessSetting> ew = new QueryWrapper<SysProcessSetting>();
        ew.and(i -> i.eq("code", code));
        SysProcessSetting setting = SysProcessSettingServiceImpl.getOne(ew);
        if (setting == null) {
            return R.FAILURE_NO_DATA();
        }
        SysProcessDef def = SysProcessDefServiceImpl.getById(setting.getProcessdefid());
        if (def == null) {
            return R.FAILURE_NO_DATA();
        }
        SysForm form = SysFormServiceImpl.getById(def.getForm());
        JSONObject res = new JSONObject();
        res.put("processdefid", setting.getProcessdefid());
        res.put("code", code);
        res.put("formct", form.getCt());
        res.put("process", def.getPtplid());
        res.put("processKey", def.getPtplkey());
        return R.SUCCESS_OPER(res);
    }


}

