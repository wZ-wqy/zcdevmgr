package com.dt.module.zbx.service.impl;

import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import io.github.hengyunabc.zabbix.api.ZabbixApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class Demo {

    public static void main(String[] args) throws Exception {


        ZabbixApi zabbixApi = new DefaultZabbixApi("http://47.92.240.43:15211//api_jsonrpc.php");
        zabbixApi.init();
        boolean login = zabbixApi.login("admin", "zabbix");

        String curtimestr = Long.toString(System.currentTimeMillis() / 1000L);
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(30, ChronoUnit.DAYS);
        Long stime = now.atZone(ZoneOffset.UTC).toEpochSecond();
        String stimestr = Long.toString(stime);
        Request request = RequestBuilder.newBuilder().method("hostgroup.create")
                .paramEntry("name", "extend")
                .build();

        System.out.println(zabbixApi.call(request));
    }
}
