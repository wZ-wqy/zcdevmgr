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

@TableName("res_class_attrs")

public class ResClassAttrs extends BaseModel<ResClassAttrs> {

    private static final long serialVersionUID = 1L;

    @TableId("attr_id")
    private String attrId;
    @TableField("attr_name")
    private String attrName;
    @TableField("attr_type")
    private String attrType;
    @TableField("sort")
    private BigDecimal sort;
    @TableField("attr_code")
    private String attrCode;
    @TableField("class_id")
    private String classId;


    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    protected Serializable pkVal() {
        return this.attrId;
    }

    @Override
    public String toString() {
        return "ResClassAttrs{" +
                "attrId=" + attrId +
                ", attrName=" + attrName +
                ", attrType=" + attrType +
                ", sort=" + sort +
                ", attrCode=" + attrCode +
                ", classId=" + classId +
                "}";
    }
}
