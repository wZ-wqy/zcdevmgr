package com.dt.module.zbx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashBoardService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;


    /**
     * @Description:获取主机数量
     */
    public R getCountHost() {

        JSONObject res = new JSONObject();
        Request request = RequestBuilder.newBuilder().method("host.get")
                .paramEntry("output", "extend")
                .paramEntry("countOutput", true)
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        res.put("hosts", resJson.getString("result"));

        Request request2 = RequestBuilder.newBuilder().method("item.get")
                .paramEntry("output", "extend")
                .paramEntry("countOutput", true)
                .build();
        JSONObject resJson2 = zabbixUtilService.ApiCall(request2);
        res.put("items", resJson.getString("result"));

        Request request3 = RequestBuilder.newBuilder().method("problem.get")
                .paramEntry("output", "extend")
                .paramEntry("countOutput", true)
                .build();
        JSONObject resJson3 = zabbixUtilService.ApiCall(request3);
        res.put("problems", resJson.getString("result"));


        Request request4 = RequestBuilder.newBuilder().method("triggers.get")
                .paramEntry("output", "extend")
                .paramEntry("countOutput", true)
                .build();
        JSONObject resJson4 = zabbixUtilService.ApiCall(request4);
        res.put("triggers", resJson.getString("result"));

        return R.SUCCESS_OPER(res);


    }

}
