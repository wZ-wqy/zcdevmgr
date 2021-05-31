package com.dt.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2018-07-27
 */
@TableName("SYS_USER_HOMEADDR")
public class SysUserHomeaddr extends BaseModel<SysUserHomeaddr> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("USER_ID")
    private String userId;
    @TableField("PROVINCEID")
    private String provinceid;
    @TableField("PROVINCENAME")
    private String provincename;
    @TableField("CITYID")
    private String cityid;
    @TableField("CITYNAME")
    private String cityname;
    @TableField("AREAID")
    private String areaid;
    @TableField("AREANAME")
    private String areaname;
    @TableField("CT")
    private String ct;


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

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUserHomeaddr{" +
                ", id=" + id +
                ", userId=" + userId +
                ", provinceid=" + provinceid +
                ", provincename=" + provincename +
                ", cityid=" + cityid +
                ", cityname=" + cityname +
                ", areaid=" + areaid +
                ", areaname=" + areaname +
                ", ct=" + ct +
                "}";
    }
}
