package com.dt.core.shiro.session;

import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.DbUtil;
import com.dt.module.db.DB;

import java.io.Serializable;

/**
 * @author: lank
 * @date: 2017年11月7日 下午2:17:06
 * @Description:
 */
public class SimpleSessionEntity {
    private String id;
    private String user_id;
    private String cookie;
    private String session;
    private String start_time;
    private String client;
    private String ip;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Serializable entity() {
        return session;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void save() {
        Insert me = new Insert("sys_session");
        me.set("id", MD5Util.encrypt(cookie + start_time));
        me.set("cookie", cookie);
        me.set("dr", 0);
        me.set("dtsession", session + "");
        me.setIf("start_time", start_time);
        me.setIf("token", token);
        me.setIf("ip", ip);
        DB.instance().execute(me);
    }

    public void update(SimpleSessionEntity entity) {
        Update me = new Update("sys_session");
        me.set("dtsession", entity.session + "");
        me.setSE("lastaccess", DbUtil.getDbDateString(DB.instance().getDBType()));
        me.where().and("cookie=?", entity.cookie);
        DB.instance().execute(me);
    }

    /**
     * @return the start_time
     */
    public String getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the user_id
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the client
     */
    public String getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}
