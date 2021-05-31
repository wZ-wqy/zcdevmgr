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

@TableName("res_collectionreturn_item")

public class ResCollectionreturnItem extends BaseModel<ResCollectionreturnItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 单据
     */
    @TableField("busuuid")
    private String busuuid;
    /**
     * 领用时间
     */
    @TableField("busdate")
    private Date busdate;
    /**
     * 资产ID
     */
    @TableField("resid")
    private String resid;
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
    @TableField("fusedcompanyid")
    private String fusedcompanyid;
    /**
     * 部门
     */
    @TableField("fpartid")
    private String fpartid;
    /**
     * 领用人
     */
    @TableField("fuseduserid")
    private String fuseduserid;
    /**
     * 区域
     */
    @TableField("floc")
    private String floc;
    /**
     * 位置
     */
    @TableField("flocdtl")
    private String flocdtl;
    /**
     * 公司
     */
    @TableField("tusedcompanyid")
    private String tusedcompanyid;
    /**
     * 部门
     */
    @TableField("tpartid")
    private String tpartid;
    /**
     * 使用人
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
     * 预计退库时间
     */
    @TableField("returndate")
    private Date returndate;
    /**
     * 实际退库实际
     */
    @TableField("rreturndate")
    private Date rreturndate;
    /**
     * 是否退库
     */
    @TableField("isreturn")
    private String isreturn;
    /**
     * 退库单据
     */
    @TableField("returnuuid")
    private String returnuuid;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;


    @TableField("fusedusername")
    private String fusedusername;

    @TableField("fpartname")
    private String fpartname;

    @TableField("fusedcompanyname")
    private String fusedcompanyname;

    @TableField("flocname")
    private String flocname;

    @TableField("tusedcompanyname")
    private String tusedcompanyname;

    @TableField("tpartname")
    private String tpartname;

    @TableField("tusedusername")
    private String tusedusername;

    @TableField("tlocname")
    private String tlocname;

    public String getFusedusername() {
        return fusedusername;
    }

    public void setFusedusername(String fusedusername) {
        this.fusedusername = fusedusername;
    }

    public String getFpartname() {
        return fpartname;
    }

    public void setFpartname(String fpartname) {
        this.fpartname = fpartname;
    }

    public String getFusedcompanyname() {
        return fusedcompanyname;
    }

    public void setFusedcompanyname(String fusedcompanyname) {
        this.fusedcompanyname = fusedcompanyname;
    }

    public String getFlocname() {
        return flocname;
    }

    public void setFlocname(String flocname) {
        this.flocname = flocname;
    }

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

    public Date getBusdate() {
        return busdate;
    }

    public void setBusdate(Date busdate) {
        this.busdate = busdate;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
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

    public String getFusedcompanyid() {
        return fusedcompanyid;
    }

    public void setFusedcompanyid(String fusedcompanyid) {
        this.fusedcompanyid = fusedcompanyid;
    }

    public String getFpartid() {
        return fpartid;
    }

    public void setFpartid(String fpartid) {
        this.fpartid = fpartid;
    }

    public String getFuseduserid() {
        return fuseduserid;
    }

    public void setFuseduserid(String fuseduserid) {
        this.fuseduserid = fuseduserid;
    }

    public String getFloc() {
        return floc;
    }

    public void setFloc(String floc) {
        this.floc = floc;
    }

    public String getFlocdtl() {
        return flocdtl;
    }

    public void setFlocdtl(String flocdtl) {
        this.flocdtl = flocdtl;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResCollectionreturnItem{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", busdate=" + busdate +
                ", resid=" + resid +
                ", cruserid=" + cruserid +
                ", crusername=" + crusername +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", fusedcompanyid=" + fusedcompanyid +
                ", fpartid=" + fpartid +
                ", fuseduserid=" + fuseduserid +
                ", floc=" + floc +
                ", flocdtl=" + flocdtl +
                ", tusedcompanyid=" + tusedcompanyid +
                ", tpartid=" + tpartid +
                ", tuseduserid=" + tuseduserid +
                ", tloc=" + tloc +
                ", tlocdtl=" + tlocdtl +
                ", returndate=" + returndate +
                ", rreturndate=" + rreturndate +
                ", isreturn=" + isreturn +
                ", returnuuid=" + returnuuid +
                ", mark=" + mark +
                "}";
    }
}
