package com.dt.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2018-07-29
 */
@TableName("SYS_SESSION")
public class SysSession extends BaseModel<SysSession> {

    private static final long serialVersionUID = 1L;

    @TableField("TOKEN")
    private String token;
    @TableField("LASTACCESS")
    private Date lastaccess;
    @TableField("EXPIRE")
    private String expire;
    @TableField("COOKIE")
    private String cookie;
    @TableField("DTSESSION")
    private String dtsession;
    @TableId("ID")
    private String id;
    @TableField("USER_ID")
    private String userId;
    @TableField("START_TIME")
    private String startTime;
    @TableField("LOGIN_TIME")
    private Date loginTime;
    @TableField("IP")
    private String ip;
    @TableField("AGENT")
    private String agent;
    @TableField("CLIENT")
    private String client;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastaccess() {
        return lastaccess;
    }

    public void setLastaccess(Date lastaccess) {
        this.lastaccess = lastaccess;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getDtsession() {
        return dtsession;
    }

    public void setDtsession(String dtsession) {
        this.dtsession = dtsession;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysSession{" +
                ", token=" + token +
                ", lastaccess=" + lastaccess +
                ", expire=" + expire +
                ", cookie=" + cookie +
                ", dtsession=" + dtsession +
                ", id=" + id +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", loginTime=" + loginTime +
                ", ip=" + ip +
                ", agent=" + agent +
                ", client=" + client +
                "}";
    }
}
