package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author lank
 * @since 2020-11-26
 */
 
@TableName("res_inspection_pitem")
 
public class ResInspectionPitem extends BaseModel<ResInspectionPitem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busid")
    private String busid;
    @TableField("type")
    private String type;
    @TableField("resid")
    private String resid;
    @TableField("status")
    private String status;
    @TableField("mark")
    private String mark;
    @TableField("actionuserid")
    private String actionuserid;
    @TableField("actionusername")
    private String actionusername;
    @TableField("actiontime")
    private Date actiontime;
    @TableField("pics")
    private String pics;
    @TableField("loc")
    private String loc;
    @TableField("od")
    private BigDecimal od;
    @TableField("method")
    private String method;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getActionuserid() {
        return actionuserid;
    }

    public void setActionuserid(String actionuserid) {
        this.actionuserid = actionuserid;
    }

    public String getActionusername() {
        return actionusername;
    }

    public void setActionusername(String actionusername) {
        this.actionusername = actionusername;
    }

    public Date getActiontime() {
        return actiontime;
    }

    public void setActiontime(Date actiontime) {
        this.actiontime = actiontime;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public BigDecimal getOd() {
        return od;
    }

    public void setOd(BigDecimal od) {
        this.od = od;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInspectionPitem{" +
        "id=" + id +
        ", busid=" + busid +
        ", type=" + type +
        ", resid=" + resid +
        ", status=" + status +
        ", mark=" + mark +
        ", actionuserid=" + actionuserid +
        ", actionusername=" + actionusername +
        ", actiontime=" + actiontime +
        ", pics=" + pics +
        ", loc=" + loc +
        ", od=" + od +
        ", method=" + method +
        "}";
    }
}
