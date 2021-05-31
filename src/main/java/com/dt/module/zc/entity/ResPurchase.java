package com.dt.module.zc.entity;

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
 * @since 2020-12-12
 */
 
@TableName("res_purchase")
 
public class ResPurchase extends BaseModel<ResPurchase> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 类目主名称
     */
    @TableField("name")
    private String name;
    @TableField("status")
    private String status;
    @TableField("busid")
    private String busid;
    @TableField("pinst")
    private String pinst;
    @TableField("plan")
    private String plan;
    @TableField("zcname")
    private String zcname;
    @TableField("zcmodel")
    private String zcmodel;
    @TableField("unit")
    private String unit;
    @TableField("cnt")
    private String cnt;
    @TableField("estprice")
    private String estprice;
    @TableField("contractamount")
    private String contractamount;
    /**
     * 用途
     */
    @TableField("purpose")
    private String purpose;
    /**
     * 原因
     */
    @TableField("reason")
    private String reason;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;
    @TableField("create_username")
    private String createUsername;
    @TableField("files")
    private String files;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getPinst() {
        return pinst;
    }

    public void setPinst(String pinst) {
        this.pinst = pinst;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getZcname() {
        return zcname;
    }

    public void setZcname(String zcname) {
        this.zcname = zcname;
    }

    public String getZcmodel() {
        return zcmodel;
    }

    public void setZcmodel(String zcmodel) {
        this.zcmodel = zcmodel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getEstprice() {
        return estprice;
    }

    public void setEstprice(String estprice) {
        this.estprice = estprice;
    }

    public String getContractamount() {
        return contractamount;
    }

    public void setContractamount(String contractamount) {
        this.contractamount = contractamount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResPurchase{" +
        "id=" + id +
        ", name=" + name +
        ", status=" + status +
        ", busid=" + busid +
        ", pinst=" + pinst +
        ", plan=" + plan +
        ", zcname=" + zcname +
        ", zcmodel=" + zcmodel +
        ", unit=" + unit +
        ", cnt=" + cnt +
        ", estprice=" + estprice +
        ", contractamount=" + contractamount +
        ", purpose=" + purpose +
        ", reason=" + reason +
        ", mark=" + mark +
        ", createUsername=" + createUsername +
        ", files=" + files +
        "}";
    }
}
