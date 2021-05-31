package com.dt.module.zbx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;


    /**
     * @Description:根据主机获取图形数据
     */
    public R getGraphByHostId(String hostid) {
        Request request = RequestBuilder.newBuilder().method("graph.get")
                .paramEntry("output", "extend")
                .paramEntry("hostids", hostid)
                .paramEntry("sortfiled", "name")
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONArray("result"));

    }

}
