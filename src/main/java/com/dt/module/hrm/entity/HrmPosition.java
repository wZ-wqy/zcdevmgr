package com.dt.module.hrm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-09-30
 */
 
@TableName("hrm_position")

public class HrmPosition extends BaseModel<HrmPosition> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 岗位名称
     */
    @TableField("name")
    private String name;
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;
    @TableField("code")
    private String code;


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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "HrmPosition{" +
                "id=" + id +
                ", name=" + name +
                ", type=" + type +
                ", mark=" + mark +
                ", code=" + code +
                "}";
    }
}
