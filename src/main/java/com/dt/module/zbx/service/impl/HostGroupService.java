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
public class HostGroupService extends BaseService {

    @Autowired
    ZabbixUtilService zabbixUtilService;

    /**
     * @Description:获取所有主机组
     */
    public R getAllHostGroups(String groups) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.get")
                .paramEntry("output", new String[]{"status", "name", "available", "host", "hostid", "error"})
                .paramEntry("selectHosts", "count")
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONArray("result"));

    }

    /**
     * @Description:增加主机组
     */
    public R addHostGroup(String name) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.create")
                .paramEntry("name", name)
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONObject("result"));
    }



    /**
     * @Description:更新主机组
     */
    public R updateHostGroup(String name, String groupid) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.update")
                .paramEntry("name", name)
                .paramEntry("groupid", groupid)
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONObject("result"));
    }



    /**
     * @Description:获取所有主机组
     */
    public R getAllHostGroupsList(String groups) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.get")
                .paramEntry("output", "extend")
                .paramEntry("selectHosts", new String[]{"hostid", "name", "status"})
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);
        return R.SUCCESS_OPER(resJson.getJSONArray("result"));

    }

    /**
     * @Description:树形格式化获取所有主机组
     */
    public R getAllHostGroupsListFormatTree(String groups) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.get")
                .paramEntry("output", "extend")
                .paramEntry("selectHosts", new String[]{"hostid", "name", "status"})
                .build();
        JSONObject resJson = zabbixUtilService.ApiCall(request);

        JSONArray reqres = resJson.getJSONArray("result");
        JSONArray res = new JSONArray();
        JSONObject root = new JSONObject();
        root.put("parent", "#");
        root.put("id", "1");
        root.put("text", "主机列表");
        root.put("type", "root");
        res.add(root);
        Integer seq = 2;
        for (int i = 0; i < reqres.size(); i++) {
            seq++;
            Integer curid = seq;
            JSONObject e = new JSONObject();
            e.put("parent", "1");
            e.put("id", curid.toString());
            e.put("groupid", reqres.getJSONObject(i).getString("groupid"));
            e.put("hostid", "-1");
            e.put("text", reqres.getJSONObject(i).getString("name"));
            e.put("type", "group");
            res.add(e);
            JSONArray hosts = reqres.getJSONObject(i).getJSONArray("hosts");
            if (hosts.size() > 0) {
                for (int j = 0; j < hosts.size(); j++) {
                    seq++;
                    JSONObject e2 = new JSONObject();
                    e2.put("parent", curid.toString());
                    e2.put("id", seq.toString());
                    e2.put("groupid", "-1");
                    e2.put("hostid", hosts.getJSONObject(j).getString("hostid"));
                    e2.put("text", hosts.getJSONObject(j).getString("name"));
                    e2.put("type", "host");
                    res.add(e2);
                }
            }
        }
        return R.SUCCESS_OPER(res);

    }

}
