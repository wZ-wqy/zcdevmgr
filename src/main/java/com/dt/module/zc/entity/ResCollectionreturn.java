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
 
@TableName("res_collectionreturn")

public class ResCollectionreturn extends BaseModel<ResCollectionreturn> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 单据
     */
    @TableField("busuuid")
    private String busuuid;
    /**
     * 类型
     */
    @TableField("bustype")
    private String bustype;
    /**
     * 领用时间
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
     * 领用人ID/退库人ID
     */
    @TableField("cruserid")
    private String cruserid;
    /**
     * 领用人/退库人
     */
    @TableField("crusername")
    private String crusername;
    /**
     * 处理人ID
     */
    @TableField("processuserid")
    private String processuserid;
    /**
     * 处理人
     */
    @TableField("processusername")
    private String processusername;
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
     * 领用人ID
     */
    @TableField("tuseduserid")
    private String tuseduserid;
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
     * 预计退还时间
     */
    @TableField("returndate")
    private Date returndate;
    /**
     * 实际退还时间
     */
    @TableField("rreturndate")
    private Date rreturndate;

    @TableField("mark")
    private String mark;


    @TableField("name")
    private String name;

    @TableField("tusedcompanyname")
    private String tusedcompanyname;

    @TableField("tpartname")
    private String tpartname;

    @TableField("tusedusername")
    private String tusedusername;

    @TableField("tlocname")
    private String tlocname;

    public String getTusedcompanyname() {
        return tusedcompanyname;
    }

    public void setTusedcompanyname(String tusedcompanyname) {
        this.tusedcompanyname = tusedcompanyname;
    }

    public String getTpartname() {
        return tpartname;
    }

    public void setTpartname(String tpartname) {
        this.tpartname = tpartname;
    }

    public String getTusedusername() {
        return tusedusername;
    }

    public void setTusedusername(String tusedusername) {
        this.tusedusername = tusedusername;
    }

    public String getTlocname() {
        return tlocname;
    }

    public void setTlocname(String tlocname) {
        this.tlocname = tlocname;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


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

    public String getBustype() {
        return bustype;
    }

    public void setBustype(String bustype) {
        this.bustype = bustype;
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

    public String getCruserid() {
        return cruserid;
    }

    public void setCruserid(String cruserid) {
        this.cruserid = cruserid;
    }

    public String getCrusername() {
        return crusername;
    }

    public void setCrusername(String crusername) {
        this.crusername = crusername;
    }

    public String getProcessuserid() {
        return processuserid;
    }

    public void setProcessuserid(String processuserid) {
        this.processuserid = processuserid;
    }

    public String getProcessusername() {
        return processusername;
    }

    public void setProcessusername(String processusername) {
        this.processusername = processusername;
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

    public String getTuseduserid() {
        return tuseduserid;
    }

    public void setTuseduserid(String tuseduserid) {
        this.tuseduserid = tuseduserid;
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
        return "ResCollectionreturn{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", bustype=" + bustype +
                ", busdate=" + busdate +
                ", status=" + status +
                ", pinst=" + pinst +
                ", cruserid=" + cruserid +
                ", crusername=" + crusername +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", tusedcompanyid=" + tusedcompanyid +
                ", tpartid=" + tpartid +
                ", tuseduserid=" + tuseduserid +
                ", tloc=" + tloc +
                ", tlocdtl=" + tlocdtl +
                ", returndate=" + returndate +
                ", rreturndate=" + rreturndate +
                ", mark=" + mark +
                "}";
    }
}
