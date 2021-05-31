package com.dt.module.zbx.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class AlarmService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;

    /**
     * @Description:获取所有告警
     */
    public R getAllAlarm(String s, String e) {
        String curtimestr = Long.toString(System.currentTimeMillis() / 1000L);
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(30, ChronoUnit.DAYS);
        Long stime = now.atZone(ZoneOffset.UTC).toEpochSecond();
        String stimestr = Long.toString(stime);
        Request request = RequestBuilder.newBuilder().method("alert.get")
                .paramEntry("output", new String[]{"clock", ",alertid", "actionid", "userid", "sendto", "subject", "message", "status", "retries", "alerttype", "error"})
                .paramEntry("selectMediatypes", new String[]{"name", "mediatypeid", "maxattempts"})
                .paramEntry("selectUsers", "extend")
                .paramEntry("eventsource", "0")
                .paramEntry("eventobject", "0")
                .paramEntry("time_from", stimestr)
                .paramEntry("time_till", curtimestr)
                .paramEntry("sortfield", "alertid")
                .paramEntry("sortorder", "DESC")
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONArray("result"));
    }

}
