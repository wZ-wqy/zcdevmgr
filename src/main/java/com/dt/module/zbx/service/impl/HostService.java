package com.dt.module.zbx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;

    /**
     * @Description:获取所有主机
     */
    public R hostList(String hosts) {
        Request request = RequestBuilder.newBuilder().method("host.get")
                .paramEntry("output", new String[]{"status", "name", "available", "host", "hostid", "error"})
                .paramEntry("selectGroups", "extend")
                .paramEntry("selectInterfaces", "extend")
                .paramEntry("selectParentTemplates", new String[]{"templateid", "name"})
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONArray("result"));
    }


}
