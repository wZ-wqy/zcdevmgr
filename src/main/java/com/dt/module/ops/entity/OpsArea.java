package com.dt.module.ops.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 区域
 * </p>
 *
 * @author lank
 * @since 2021-02-01
 */
 
@TableName("ops_area")
 
public class OpsArea extends BaseModel<OpsArea> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 位置ID
     */
    @TableField("locid")
    private String locid;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;
    /**
     * 类型
     */
    @TableField("type")
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocid() {
        return locid;
    }

    public void setLocid(String locid) {
        this.locid = locid;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OpsArea{" +
        "id=" + id +
        ", locid=" + locid +
        ", name=" + name +
        ", mark=" + mark +
        ", type=" + type +
        "}";
    }
}
