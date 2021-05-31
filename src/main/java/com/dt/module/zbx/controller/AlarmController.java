package com.dt.module.zbx.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.zbx.service.impl.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/zbx/alarm")
public class AlarmController extends BaseController {

    @Autowired
    AlarmService alarmService;

    /**
     * @Description:获取所有告警
     */
    @ResponseBody
    @Acl(info = "获取所有告警", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/getAllAlarm.do")
    public R getAllAlarm(String hostid) {
        return alarmService.getAllAlarm("1", "2");
    }

}
