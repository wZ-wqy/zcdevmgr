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
 * @since 2018-07-27
 */
@TableName("SYS_REGION")
public class SysRegion extends BaseModel<SysRegion> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("CODE")
    private String code;
    @TableField("NAME")
    private String name;
    @TableField("PARENTID")
    private String parentid;
    @TableField("FIRST_LETTER")
    private String firstLetter;
    @TableField("LEV")
    private String lev;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getLev() {
        return lev;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysRegion{" +
                ", id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", parentid=" + parentid +
                ", firstLetter=" + firstLetter +
                ", lev=" + lev +
                "}";
    }
}
