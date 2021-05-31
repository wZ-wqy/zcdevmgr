package com.dt.module.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-10-13
 */

@TableName("sys_info")

public class SysInfo extends BaseModel<SysInfo> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("ip")
    private String ip;
    @TableField("hostname")
    private String hostname;
    @TableField("version")
    private String version;
    @TableField("ct")
    private String ct;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysInfo{" +
                "id=" + id +
                ", ip=" + ip +
                ", hostname=" + hostname +
                ", version=" + version +
                ", ct=" + ct +
                ", mark=" + mark +
                "}";
    }
}
