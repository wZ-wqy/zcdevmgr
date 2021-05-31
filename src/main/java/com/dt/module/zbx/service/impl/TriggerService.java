package com.dt.module.zbx.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TriggerService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;

    /**
     * @Description:获取所有触发器
     */
    public R getTriggers(String triggers) {
        Request request = RequestBuilder.newBuilder().method("trigger.get")
                .paramEntry("output", "extend")
                .paramEntry("sortfield", "lastchange")
                .paramEntry("sortorder", "DESC")
                .paramEntry("selectHosts", new String[]{"hostid", "name"})
                .paramEntry("selectLastEvent", "extend")
                .paramEntry("maintenance", "false")
                .paramEntry("only_true", "true")
                .paramEntry("monitored", "true")
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        JSONArray reqres = resJson.getJSONArray("result");
        JSONArray res = new JSONArray();
        for (int i = 0; i < reqres.size(); i++) {
            JSONObject e = new JSONObject();
            e.put("acknowledged", reqres.getJSONObject(i).getJSONObject("lastEvent").getString("acknowledged"));
            e.put("hostid", reqres.getJSONObject(i).getJSONArray("hosts").getJSONObject(0).getString("hostid"));
            e.put("hostname", reqres.getJSONObject(i).getJSONArray("hosts").getJSONObject(0).getString("name"));
            e.put("lastchange", reqres.getJSONObject(i).getString("lastchange"));
            e.put("lasteventname", reqres.getJSONObject(i).getJSONObject("lastEvent").getString("name"));
            e.put("severity", reqres.getJSONObject(i).getJSONObject("lastEvent").getString("severity"));
            e.put("eventid", reqres.getJSONObject(i).getJSONObject("lastEvent").getString("eventid"));
            e.put("objectid", reqres.getJSONObject(i).getJSONObject("lastEvent").getString("objectid"));
            res.add(e);
        }
        return R.SUCCESS_OPER(res);
    }


}
