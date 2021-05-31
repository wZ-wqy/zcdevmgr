package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.db.DB;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;



@Service
public class SysInfoService extends BaseService {

    public static SysInfoService me() {
        return SpringContextUtil.getBean(SysInfoService.class);
    }


    /**
     * @Description:获取系统基本信息
     */
    public R uploadSysInfo(String ip, String hostname, String version, String app, String ct, String os, String uid) {
        Insert ins = new Insert("sys_info");
        ins.set("id", ToolUtil.getUUID());
        ins.set("dr", "0");
        ins.setIf("app", app);
        ins.setIf("ip", ip);
        ins.setIf("os", os);
        ins.setIf("uid", uid);
        ins.setIf("hostname", hostname);
        ins.setIf("ct", ct);
        ins.setIf("version", version);
        ins.setSE("create_time", DbUtil.getDbDateString(DB.instance().getDBType()));
        ins.setSE("update_time", DbUtil.getDbDateString(DB.instance().getDBType()));
        db.execute(ins);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:获取系统基本信息
     */
    public R uploadSysInfo() {
        JSONObject info = ServerMonitorService.getSysInfo();
        String version = "";
        String app = "";
        String hostname = info.getString("hostname");
        String ip = info.getString("ip");
        String os = info.getString("osname");

        String uid = ServerMonitorService.createUniqueSn();
        Rcd versionrs = DB.instance().uniqueRecord("select * from sys_params where dr='0' and id='version'");
        if (versionrs != null) {
            version = versionrs.getString("value");
        }
        Rcd apprs = DB.instance().uniqueRecord("select * from sys_params where dr='0' and id='app'");
        if (apprs != null) {
            app = apprs.getString("value");
        }
        String url = "http://39.105.191.22:8080/dt/api/sysinfo/ext/upload.do";
        StringBuffer params = new StringBuffer();
        params.append("ip=" + ip);
        try {
            params.append("&hostname=" + java.net.URLEncoder.encode(hostname, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //  e.printStackTrace();
        }
        try {
            params.append("&os=" + java.net.URLEncoder.encode(os, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
        params.append("&uid=" + uid);
        try {
            params.append("&app=" + java.net.URLEncoder.encode(app, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
        try {
            params.append("&version=" + java.net.URLEncoder.encode(version, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
        params.append("&ct=" + "");
        URI uri = URI.create(url + "?" + params);
        try {
            HttpKit.sendPost(uri);
        } catch (SocketException e) {

        }
        return R.SUCCESS_OPER();
    }

}
