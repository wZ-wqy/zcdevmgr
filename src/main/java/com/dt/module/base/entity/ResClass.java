package com.dt.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2019-04-04
 */

@TableName("res_class")

public class ResClass extends BaseModel<ResClass> {

    private static final long serialVersionUID = 1L;

    @TableId("class_id")
    private String classId;
    @TableField("class_code")
    private String classCode;
    @TableField("pid")
    private String pid;
    @TableField("name")
    private String name;
    @TableField("type")
    private String type;
    @TableField("img")
    private String img;
    @TableField("status")
    private String status;
    @TableField("sort")
    private BigDecimal sort;
    @TableField("subtype")
    private String subtype;
    @TableField("mark")
    private String mark;


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    protected Serializable pkVal() {
        return this.classId;
    }

    @Override
    public String toString() {
        return "ResClass{" +
                "classId=" + classId +
                ", classCode=" + classCode +
                ", pid=" + pid +
                ", name=" + name +
                ", type=" + type +
                ", img=" + img +
                ", status=" + status +
                ", sort=" + sort +
                ", subtype=" + subtype +
                ", mark=" + mark +
                "}";
    }
}
