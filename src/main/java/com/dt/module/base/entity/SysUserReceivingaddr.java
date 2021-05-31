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
 * @since 2018-07-30
 */
@TableName("SYS_USER_RECEIVINGADDR")
public class SysUserReceivingaddr extends BaseModel<SysUserReceivingaddr> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("USER_ID")
    private String userId;
    @TableField("PROVINCEID")
    private String provinceid;
    @TableField("PROVINCECODE")
    private String provincecode;
    @TableField("PROVINCENAME")
    private String provincename;
    @TableField("CITYID")
    private String cityid;
    @TableField("CITYCODE")
    private String citycode;
    @TableField("CITYNAME")
    private String cityname;
    @TableField("AREAID")
    private String areaid;
    @TableField("AREACODE")
    private String areacode;
    @TableField("AREANAME")
    private String areaname;
    @TableField("CT")
    private String ct;
    @TableField("CONTACTUSER")
    private String contactuser;
    @TableField("CONTACT")
    private String contact;
    @TableField("ZCODE")
    private String zcode;
    @TableField("OD")
    private String od;

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

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode;
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

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
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

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
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

    public String getContactuser() {
        return contactuser;
    }

    public void setContactuser(String contactuser) {
        this.contactuser = contactuser;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getZcode() {
        return zcode;
    }

    public void setZcode(String zcode) {
        this.zcode = zcode;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUserReceivingaddr{" + ", id=" + id + ", userId=" + userId + ", provinceid=" + provinceid
                + ", provincecode=" + provincecode + ", provincename=" + provincename + ", cityid=" + cityid
                + ", citycode=" + citycode + ", cityname=" + cityname + ", areaid=" + areaid + ", areacode=" + areacode
                + ", areaname=" + areaname + ", ct=" + ct + ", contactuser=" + contactuser + ", contact=" + contact
                + ", zcode=" + zcode + ", od=" + od + "}";
    }
}
