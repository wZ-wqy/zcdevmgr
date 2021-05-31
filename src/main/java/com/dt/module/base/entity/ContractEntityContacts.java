package com.dt.module.base.entity;

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
 * @since 2020-09-05
 */

@TableName("contract_entity_contacts")

public class ContractEntityContacts extends BaseModel<ContractEntityContacts> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("code")
    private String code;
    @TableField("entityid")
    private String entityid;
    @TableField("name")
    private String name;
    @TableField("sex")
    private String sex;
    @TableField("mail")
    private String mail;
    @TableField("mobile")
    private String mobile;
    @TableField("mark")
    private String mark;


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

    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        return "ContractEntityContacts{" +
                "id=" + id +
                ", code=" + code +
                ", entityid=" + entityid +
                ", name=" + name +
                ", sex=" + sex +
                ", mail=" + mail +
                ", mobile=" + mobile +
                ", mark=" + mark +
                "}";
    }
}
