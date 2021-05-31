package com.dt.module.zc.entity;

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
 * @since 2020-08-14
 */

@TableName("res_c_maintenance")

public class ResCMaintenance extends BaseModel<ResCMaintenance> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busuuid")
    private String busuuid;
    @TableField("busidate")
    private Date busidate;
    @TableField("status")
    private String status;
    @TableField("processuserid")
    private String processuserid;
    @TableField("processusername")
    private String processusername;
    @TableField("twb")
    private String twb;
    @TableField("twbsupplier")
    private String twbsupplier;
    @TableField("twbauto")
    private String twbauto;
    @TableField("twbct")
    private String twbct;
    @TableField("twboutdate")
    private Date twboutdate;
    @TableField("twbstatus")
    private String twbstatus;
    @TableField("twbsupplierstatus")
    private String twbsupplierstatus;
    @TableField("twbautostatus")
    private String twbautostatus;
    @TableField("twbctstatus")
    private String twbctstatus;
    @TableField("twboutdatestatus")
    private String twboutdatestatus;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusuuid() {
        return busuuid;
    }

    public void setBusuuid(String busuuid) {
        this.busuuid = busuuid;
    }

    public Date getBusidate() {
        return busidate;
    }

    public void setBusidate(Date busidate) {
        this.busidate = busidate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessuserid() {
        return processuserid;
    }

    public void setProcessuserid(String processuserid) {
        this.processuserid = processuserid;
    }

    public String getProcessusername() {
        return processusername;
    }

    public void setProcessusername(String processusername) {
        this.processusername = processusername;
    }

    public String getTwb() {
        return twb;
    }

    public void setTwb(String twb) {
        this.twb = twb;
    }

    public String getTwbsupplier() {
        return twbsupplier;
    }

    public void setTwbsupplier(String twbsupplier) {
        this.twbsupplier = twbsupplier;
    }

    public String getTwbauto() {
        return twbauto;
    }

    public void setTwbauto(String twbauto) {
        this.twbauto = twbauto;
    }

    public String getTwbct() {
        return twbct;
    }

    public void setTwbct(String twbct) {
        this.twbct = twbct;
    }

    public Date getTwboutdate() {
        return twboutdate;
    }

    public void setTwboutdate(Date twboutdate) {
        this.twboutdate = twboutdate;
    }

    public String getTwbstatus() {
        return twbstatus;
    }

    public void setTwbstatus(String twbstatus) {
        this.twbstatus = twbstatus;
    }

    public String getTwbsupplierstatus() {
        return twbsupplierstatus;
    }

    public void setTwbsupplierstatus(String twbsupplierstatus) {
        this.twbsupplierstatus = twbsupplierstatus;
    }

    public String getTwbautostatus() {
        return twbautostatus;
    }

    public void setTwbautostatus(String twbautostatus) {
        this.twbautostatus = twbautostatus;
    }

    public String getTwbctstatus() {
        return twbctstatus;
    }

    public void setTwbctstatus(String twbctstatus) {
        this.twbctstatus = twbctstatus;
    }

    public String getTwboutdatestatus() {
        return twboutdatestatus;
    }

    public void setTwboutdatestatus(String twboutdatestatus) {
        this.twboutdatestatus = twboutdatestatus;
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
        return "ResCMaintenance{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", busidate=" + busidate +
                ", status=" + status +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", twb=" + twb +
                ", twbsupplier=" + twbsupplier +
                ", twbauto=" + twbauto +
                ", twbct=" + twbct +
                ", twboutdate=" + twboutdate +
                ", twbstatus=" + twbstatus +
                ", twbsupplierstatus=" + twbsupplierstatus +
                ", twbautostatus=" + twbautostatus +
                ", twbctstatus=" + twbctstatus +
                ", twboutdatestatus=" + twboutdatestatus +
                ", mark=" + mark +
                "}";
    }
}
