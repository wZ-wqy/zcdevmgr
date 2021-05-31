package com.dt.module.ct.entity;

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
 * @since 2018-07-30
 */
@TableName("CT_CATEGORY_ROOT")
public class CtCategoryRoot extends BaseModel<CtCategoryRoot> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("NAME")
    private String name;
    @TableField("TYPE")
    private String type;
    @TableField("MARK")
    private String mark;
    @TableField("OD")
    private String od;
    /**
     * bus|system
     */
    @TableField("INTER_TYPE")
    private String interType;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getInterType() {
        return interType;
    }

    public void setInterType(String interType) {
        this.interType = interType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CtCategoryRoot{" +
                ", id=" + id +
                ", name=" + name +
                ", type=" + type +
                ", mark=" + mark +
                ", od=" + od +
                ", interType=" + interType +
                "}";
    }
}
