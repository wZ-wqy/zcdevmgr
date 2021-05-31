package com.dt.module.cmdb.entity;

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
 * @since 2020-04-16
 */

@TableName("res_history")

public class ResHistory extends BaseModel<ResHistory> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("res_id")
    private String resId;
    @TableField("oper_time")
    private Date operTime;
    @TableField("oper_user")
    private String operUser;
    @TableField("fullct")
    private String fullct;
    @TableField("oper_type")
    private String operType;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getFullct() {
        return fullct;
    }

    public void setFullct(String fullct) {
        this.fullct = fullct;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
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
        return "ResHistory{" +
                "id=" + id +
                ", resId=" + resId +
                ", operTime=" + operTime +
                ", operUser=" + operUser +
                ", fullct=" + fullct +
                ", operType=" + operType +
                ", mark=" + mark +
                "}";
    }
}
