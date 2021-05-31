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
 * @since 2019-04-04
 */

@TableName("res_attr_value")

public class ResAttrValue extends BaseModel<ResAttrValue> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("res_id")
    private String resId;
    @TableField("attr_id")
    private String attrId;
    @TableField("attr_value")
    private String attrValue;
    @TableField("mark")
    private String mark;
    @TableField("name")
    private String name;
    @TableField("user_id")
    private String userId;


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

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResAttrValue{" +
                "id=" + id +
                ", resId=" + resId +
                ", attrId=" + attrId +
                ", attrValue=" + attrValue +
                ", mark=" + mark +
                ", name=" + name +
                ", userId=" + userId +
                "}";
    }
}
