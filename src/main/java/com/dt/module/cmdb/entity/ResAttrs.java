package com.dt.module.cmdb.entity;

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
 * @since 2020-06-18
 */

@TableName("res_attrs")

public class ResAttrs extends BaseModel<ResAttrs> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("attrname")
    private String attrname;
    @TableField("attrcode")
    private String attrcode;
    @TableField("inputtype")
    private String inputtype;
    @TableField("catid")
    private String catid;
    @TableField("ifneed")
    private String ifneed;
    @TableField("ifinheritable")
    private String ifinheritable;
    @TableField("dict")
    private String dict;
    @TableField("sort")
    private BigDecimal sort;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrcode() {
        return attrcode;
    }

    public void setAttrcode(String attrcode) {
        this.attrcode = attrcode;
    }

    public String getInputtype() {
        return inputtype;
    }

    public void setInputtype(String inputtype) {
        this.inputtype = inputtype;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getIfneed() {
        return ifneed;
    }

    public void setIfneed(String ifneed) {
        this.ifneed = ifneed;
    }

    public String getIfinheritable() {
        return ifinheritable;
    }

    public void setIfinheritable(String ifinheritable) {
        this.ifinheritable = ifinheritable;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResAttrs{" +
                "id=" + id +
                ", attrname=" + attrname +
                ", attrcode=" + attrcode +
                ", inputtype=" + inputtype +
                ", catid=" + catid +
                ", ifneed=" + ifneed +
                ", ifinheritable=" + ifinheritable +
                ", dict=" + dict +
                ", sort=" + sort +
                "}";
    }
}
