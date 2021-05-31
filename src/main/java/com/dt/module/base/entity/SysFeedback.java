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
 * @since 2020-05-16
 */

@TableName("sys_feedback")

public class SysFeedback extends BaseModel<SysFeedback> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("userid")
    private String userid;
    @TableField("name")
    private String name;
    @TableField("contact")
    private String contact;
    @TableField("ct")
    private String ct;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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
        return "SysFeedback{" +
                "id=" + id +
                ", userid=" + userid +
                ", name=" + name +
                ", contact=" + contact +
                ", ct=" + ct +
                "}";
    }
}
