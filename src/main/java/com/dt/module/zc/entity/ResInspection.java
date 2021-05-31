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
 * @since 2020-11-27
 */
 
@TableName("res_inspection")
 
public class ResInspection extends BaseModel<ResInspection> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("busid")
    private String busid;
    @TableField("status")
    private String status;
    @TableField("sdate")
    private Date sdate;
    @TableField("edate")
    private Date edate;
    @TableField("mark")
    private String mark;
    @TableField("actionusers")
    private String actionusers;
    @TableField("normalcnt")
    private BigDecimal normalcnt;
    @TableField("faultcnt")
    private BigDecimal faultcnt;
    @TableField("retention")
    private BigDecimal retention;
    @TableField("method")
    private String method;
    @TableField("actingcnt")
    private BigDecimal actingcnt;
    @TableField("cnt")
    private BigDecimal cnt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSdate() {
        return sdate;
    }

    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getActionusers() {
        return actionusers;
    }

    public void setActionusers(String actionusers) {
        this.actionusers = actionusers;
    }

    public BigDecimal getNormalcnt() {
        return normalcnt;
    }

    public void setNormalcnt(BigDecimal normalcnt) {
        this.normalcnt = normalcnt;
    }

    public BigDecimal getFaultcnt() {
        return faultcnt;
    }

    public void setFaultcnt(BigDecimal faultcnt) {
        this.faultcnt = faultcnt;
    }

    public BigDecimal getRetention() {
        return retention;
    }

    public void setRetention(BigDecimal retention) {
        this.retention = retention;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getActingcnt() {
        return actingcnt;
    }

    public void setActingcnt(BigDecimal actingcnt) {
        this.actingcnt = actingcnt;
    }

    public BigDecimal getCnt() {
        return cnt;
    }

    public void setCnt(BigDecimal cnt) {
        this.cnt = cnt;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInspection{" +
        "id=" + id +
        ", name=" + name +
        ", busid=" + busid +
        ", status=" + status +
        ", sdate=" + sdate +
        ", edate=" + edate +
        ", mark=" + mark +
        ", actionusers=" + actionusers +
        ", normalcnt=" + normalcnt +
        ", faultcnt=" + faultcnt +
        ", retention=" + retention +
        ", method=" + method +
        ", actingcnt=" + actingcnt +
        ", cnt=" + cnt +
        "}";
    }
}
