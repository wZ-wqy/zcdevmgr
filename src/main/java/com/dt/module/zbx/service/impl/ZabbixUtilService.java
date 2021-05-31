package com.dt.module.zbx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.ZabbixApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * zabbix Api
 *
 * @author can
 */

@Service
@Configuration
@PropertySource(value = "classpath:config.properties")
public class ZabbixUtilService extends BaseService {

    private static ZabbixApi zabbixApi;

    @Value("${zbx.user}")
    public String zbxuser;

    @Value("${zbx.pwd}")
    public String zbxpwd;

    @Value("${zbx.server}")
    public String zbxserver;

    /**
     * @Description:调用Zabbix 接口
     * @param request
     */
    public JSONObject ApiCall(Request request) {
        if (zabbixApi == null) {
            zabbixApi = new DefaultZabbixApi(zbxserver + "/api_jsonrpc.php");
            zabbixApi.init();
            boolean loginResult = zabbixApi.login(zbxuser, zbxpwd);
            System.out.println("zabbix login.");
        }
        JSONObject response = zabbixApi.call(request);
        return response;
    }

}
