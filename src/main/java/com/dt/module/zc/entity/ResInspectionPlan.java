package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
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
 * @since 2020-11-26
 */
 
@TableName("res_inspection_plan")
 
public class ResInspectionPlan extends BaseModel<ResInspectionPlan> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("status")
    private String status;
    @TableField("cron")
    private String cron;
    @TableField("actionusers")
    private String actionusers;
    @TableField("mark")
    private String mark;
    @TableField("busid")
    private String busid;
    @TableField("retention")
    private BigDecimal retention;
    /**
     * 自由巡检 free 固定巡检 fix
     */
    @TableField("method")
    private String method;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getActionusers() {
        return actionusers;
    }

    public void setActionusers(String actionusers) {
        this.actionusers = actionusers;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInspectionPlan{" +
        "id=" + id +
        ", name=" + name +
        ", status=" + status +
        ", cron=" + cron +
        ", actionusers=" + actionusers +
        ", mark=" + mark +
        ", busid=" + busid +
        ", retention=" + retention +
        ", method=" + method +
        "}";
    }
}
