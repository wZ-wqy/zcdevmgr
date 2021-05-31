package com.dt.module.form.entity;

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
 * @since 2020-03-28
 */

@TableName("sys_form_item")

public class SysFormItem extends BaseModel<SysFormItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("formid")
    private String formid;
    @TableField("ct")
    private String ct;
    @TableField("ctvalue")
    private String ctvalue;
    @TableField("processid")
    private String processid;
    @TableField("type")
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getCtvalue() {
        return ctvalue;
    }

    public void setCtvalue(String ctvalue) {
        this.ctvalue = ctvalue;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
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
        return "SysFormItem{" +
                "id=" + id +
                ", formid=" + formid +
                ", ct=" + ct +
                ", ctvalue=" + ctvalue +
                ", processid=" + processid +
                ", type=" + type +
                "}";
    }
}
