package com.dt.module.zbx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;

    /**
     * @Description:获取所有模版
     */
    public R getTemplate(String templates) {
        Request request = RequestBuilder.newBuilder().method("template.get")
                .paramEntry("output", new String[]{"host", "name", "templateid"})
                .paramEntry("selectApplications", "count")
                .paramEntry("selectApplications", "count")
                .paramEntry("selectItems", "count")
                .paramEntry("selectTriggers", "count")
                .paramEntry("selectGraphs", "count")
                .paramEntry("selectDiscoveries", "count")
                .paramEntry("selectScreens", "count")
                .paramEntry("selectApplications", "count")
                .paramEntry("selectHosts", new String[]{"host", "name", "hostid"})
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONArray("result"));
    }
}
