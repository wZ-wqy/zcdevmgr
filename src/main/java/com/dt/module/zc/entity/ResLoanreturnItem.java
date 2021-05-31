package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-08-23
 */
 
@TableName("res_loanreturn_item")

public class ResLoanreturnItem extends BaseModel<ResLoanreturnItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 单据
     */
    @TableField("busuuid")
    private String busuuid;
    /**
     * 资产ID
     */
    @TableField("resid")
    private String resid;
    /**
     * 借用时间
     */
    @TableField("busdate")
    private Date busdate;
    /**
     * 办理状态
     */
    @TableField("status")
    private String status;
    /**
     * 流程审批
     */
    @TableField("pinst")
    private String pinst;
    /**
     * 借用人ID/归还人ID
     */
    @TableField("lruserid")
    private String lruserid;
    /**
     * 借用人/归还人
     */
    @TableField("lrusername")
    private String lrusername;
    /**
     * 使用公司
     */
    @TableField("tusedcompanyid")
    private String tusedcompanyid;
    /**
     * 部门
     */
    @TableField("tpartid")
    private String tpartid;
    /**
     * 区域
     */
    @TableField("tloc")
    private String tloc;
    /**
     * 位置
     */
    @TableField("tlocdtl")
    private String tlocdtl;
    /**
     * 预计归还时间
     */
    @TableField("returndate")
    private Date returndate;
    /**
     * 实际归还时间
     */
    @TableField("rreturndate")
    private Date rreturndate;
    /**
     * 是否归还
     */
    @TableField("isreturn")
    private String isreturn;
    /**
     * 归还单据
     */
    @TableField("returnuuid")
    private String returnuuid;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;
    /**
     * 资产变更前状态
     */
    @TableField("frecycle")
    private String frecycle;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusuuid() {
        return busuuid;
    }

    public void setBusuuid(String busuuid) {
        this.busuuid = busuuid;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public Date getBusdate() {
        return busdate;
    }

    public void setBusdate(Date busdate) {
        this.busdate = busdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPinst() {
        return pinst;
    }

    public void setPinst(String pinst) {
        this.pinst = pinst;
    }

    public String getLruserid() {
        return lruserid;
    }

    public void setLruserid(String lruserid) {
        this.lruserid = lruserid;
    }

    public String getLrusername() {
        return lrusername;
    }

    public void setLrusername(String lrusername) {
        this.lrusername = lrusername;
    }

    public String getTusedcompanyid() {
        return tusedcompanyid;
    }

    public void setTusedcompanyid(String tusedcompanyid) {
        this.tusedcompanyid = tusedcompanyid;
    }

    public String getTpartid() {
        return tpartid;
    }

    public void setTpartid(String tpartid) {
        this.tpartid = tpartid;
    }

    public String getTloc() {
        return tloc;
    }

    public void setTloc(String tloc) {
        this.tloc = tloc;
    }

    public String getTlocdtl() {
        return tlocdtl;
    }

    public void setTlocdtl(String tlocdtl) {
        this.tlocdtl = tlocdtl;
    }

    public Date getReturndate() {
        return returndate;
    }

    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }

    public Date getRreturndate() {
        return rreturndate;
    }

    public void setRreturndate(Date rreturndate) {
        this.rreturndate = rreturndate;
    }

    public String getIsreturn() {
        return isreturn;
    }

    public void setIsreturn(String isreturn) {
        this.isreturn = isreturn;
    }

    public String getReturnuuid() {
        return returnuuid;
    }

    public void setReturnuuid(String returnuuid) {
        this.returnuuid = returnuuid;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getFrecycle() {
        return frecycle;
    }

    public void setFrecycle(String frecycle) {
        this.frecycle = frecycle;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResLoanreturnItem{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", resid=" + resid +
                ", busdate=" + busdate +
                ", status=" + status +
                ", pinst=" + pinst +
                ", lruserid=" + lruserid +
                ", lrusername=" + lrusername +
                ", tusedcompanyid=" + tusedcompanyid +
                ", tpartid=" + tpartid +
                ", tloc=" + tloc +
                ", tlocdtl=" + tlocdtl +
                ", returndate=" + returndate +
                ", rreturndate=" + rreturndate +
                ", isreturn=" + isreturn +
                ", returnuuid=" + returnuuid +
                ", mark=" + mark +
                ", frecycle=" + frecycle +
                "}";
    }
}
