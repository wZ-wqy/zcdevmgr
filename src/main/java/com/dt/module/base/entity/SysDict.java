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
@TableName("SYS_DICT")
public class SysDict extends BaseModel<SysDict> {

    private static final long serialVersionUID = 1L;

    @TableId("DICT_ID")
    private String dictId;
    @TableField("NAME")
    private String name;
    @TableField("DICT_LEVEL")
    private String dictLevel;
    @TableField("STATUS")
    private String status;

    @TableField("MARK")
    private String mark;


    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDictLevel() {
        return dictLevel;
    }

    public void setDictLevel(String dictLevel) {
        this.dictLevel = dictLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


    @Override
    protected Serializable pkVal() {
        return this.dictId;
    }

    @Override
    public String toString() {
        return "SysDict{" +
                ", dictId=" + dictId +
                ", name=" + name +
                ", dictLevel=" + dictLevel +
                ", status=" + status +
                ", mark=" + mark +
                "}";
    }
}
