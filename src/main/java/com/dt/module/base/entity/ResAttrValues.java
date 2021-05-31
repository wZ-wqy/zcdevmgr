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
 * @since 2019-04-10
 */

@TableName("res_attr_values")

public class ResAttrValues extends BaseModel<ResAttrValues> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("attr_value")
    private String attrValue;
    @TableField("attr_value_id")
    private String attrValueId;
    @TableField("name")
    private String name;
    @TableField("mark")
    private String mark;
    @TableField("res_id")
    private String resId;
    @TableField("attr_id")
    private String attrId;
    @TableField("status")
    private String status;
    @TableField("type")
    private String type;
    @TableField("pwd")
    private String pwd;
    @TableField("contact")
    private String contact;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(String attrValueId) {
        this.attrValueId = attrValueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResAttrValues{" +
                "id=" + id +
                ", attrValue=" + attrValue +
                ", attrValueId=" + attrValueId +
                ", name=" + name +
                ", mark=" + mark +
                ", resId=" + resId +
                ", attrId=" + attrId +
                ", status=" + status +
                ", type=" + type +
                ", pwd=" + pwd +
                ", contact=" + contact +
                "}";
    }
}
