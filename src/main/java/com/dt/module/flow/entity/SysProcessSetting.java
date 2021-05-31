package com.dt.module.flow.entity;

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
 * @since 2020-06-26
 */

@TableName("sys_process_setting")

public class SysProcessSetting extends BaseModel<SysProcessSetting> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 流程名称
     */
    @TableField("name")
    private String name;
    /**
     * 流程编码
     */
    @TableField("code")
    private String code;
    /**
     * 流程定义ID
     */
    @TableField("processdefid")
    private String processdefid;
    /**
     * 流程定义名称
     */
    @TableField("processdefname")
    private String processdefname;
    /**
     * 流程类型 system|user
     */
    @TableField("type")
    private String type;
    /**
     * 流程表单
     */
    @TableField("form")
    private String form;
    /**
     * 流程表单名称
     */
    @TableField("formname")
    private String formname;
    /**
     * 备注
     */
    @TableField("mark")
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProcessdefid() {
        return processdefid;
    }

    public void setProcessdefid(String processdefid) {
        this.processdefid = processdefid;
    }

    public String getProcessdefname() {
        return processdefname;
    }

    public void setProcessdefname(String processdefname) {
        this.processdefname = processdefname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getFormname() {
        return formname;
    }

    public void setFormname(String formname) {
        this.formname = formname;
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
        return "SysProcessSetting{" +
                "id=" + id +
                ", name=" + name +
                ", code=" + code +
                ", processdefid=" + processdefid +
                ", processdefname=" + processdefname +
                ", type=" + type +
                ", form=" + form +
                ", formname=" + formname +
                ", mark=" + mark +
                "}";
    }
}
