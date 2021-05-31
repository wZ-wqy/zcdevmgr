package com.dt.module.form.entity;

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
 * @since 2020-03-28
 */

@TableName("sys_form")

public class SysForm extends BaseModel<SysForm> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("mark")
    private String mark;
    @TableField("type")
    private String type;
    @TableField("owner")
    private String owner;
    @TableField("ct")
    private String ct;


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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
        return "SysForm{" +
                "id=" + id +
                ", name=" + name +
                ", mark=" + mark +
                ", type=" + type +
                ", owner=" + owner +
                ", ct=" + ct +
                "}";
    }
}
