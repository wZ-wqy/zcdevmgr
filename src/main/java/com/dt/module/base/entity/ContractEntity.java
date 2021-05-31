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

@TableName("contract_entity")

public class ContractEntity extends BaseModel<ContractEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("code")
    private String code;
    @TableField("name")
    private String name;
    @TableField("profile")
    private String profile;
    @TableField("bank")
    private String bank;
    @TableField("bankaccount")
    private String bankaccount;
    @TableField("website")
    private String website;
    @TableField("addr")
    private String addr;
    @TableField("bus")
    private String bus;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
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
        return "ContractEntity{" +
                "id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", profile=" + profile +
                ", bank=" + bank +
                ", bankaccount=" + bankaccount +
                ", website=" + website +
                ", addr=" + addr +
                ", bus=" + bus +
                ", mark=" + mark +
                "}";
    }
}
