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
 * @since 2020-06-24
 */

@TableName("sys_process_def")

public class SysProcessDef extends BaseModel<SysProcessDef> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 流程名称
     */
    @TableField("name")
    private String name;
    @TableField("mark")
    private String mark;
    /**
     * 流程开启类型
     */
    @TableField("type")
    private String type;
    @TableField("ptplkey")
    private String ptplkey;
    @TableField("owner")
    private String owner;
    @TableField("ptplname")
    private String ptplname;
    @TableField("form")
    private String form;
    @TableField("formname")
    private String formname;
    /**
     * stop,normal
     */
    @TableField("status")
    private String status;
    /**
     * 流程ID
     */
    @TableField("ptplid")
    private String ptplid;


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

    public String getPtplkey() {
        return ptplkey;
    }

    public void setPtplkey(String ptplkey) {
        this.ptplkey = ptplkey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPtplname() {
        return ptplname;
    }

    public void setPtplname(String ptplname) {
        this.ptplname = ptplname;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPtplid() {
        return ptplid;
    }

    public void setPtplid(String ptplid) {
        this.ptplid = ptplid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysProcessDef{" +
                "id=" + id +
                ", name=" + name +
                ", mark=" + mark +
                ", type=" + type +
                ", ptplkey=" + ptplkey +
                ", owner=" + owner +
                ", ptplname=" + ptplname +
                ", form=" + form +
                ", formname=" + formname +
                ", status=" + status +
                ", ptplid=" + ptplid +
                "}";
    }
}
