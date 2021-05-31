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
 * @since 2018-07-24
 */
@TableName("SYS_PARAMS")
public class SysParams extends BaseModel<SysParams> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("NAME")
    private String name;
    @TableField("VALUE")
    private String value;
    @TableField("TYPE")
    private String type;

    @TableField("MARK")
    private String mark;


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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysParams{" +
                ", id=" + id +
                ", name=" + name +
                ", value=" + value +
                ", type=" + type +
                ", mark=" + mark +
                "}";
    }
}
