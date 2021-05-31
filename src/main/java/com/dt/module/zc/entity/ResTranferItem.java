package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author lank
 * @since 2020-12-15
 */
 
@TableName("res_tranfer_item")
 
public class ResTranferItem extends BaseModel<ResTranferItem> {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("resid")
    private String resid;
    @TableField("resname")
    private String resname;
    @TableField("tranfercnt")
    private BigDecimal tranfercnt;
    @TableField("rtransferdate")
    private Date rtransferdate;
    /**
     * 单据号
     */
    @TableField("busid")
    private String busid;
    /**
     * 转移状态
     */
    @TableField("tranferstatus")
    private String tranferstatus;
    @TableField("frecycle")
    private String frecycle;
    @TableField("trecycle")
    private String trecycle;
    /**
     * 从属于公司
     */
    @TableField("fbelongcompid")
    private String fbelongcompid;
    /**
     * 从属于公司
     */
    @TableField("fbelongcompname")
    private String fbelongcompname;
    /**
     * 从使用公司
     */
    @TableField("fusedcompid")
    private String fusedcompid;
    /**
     * 从使用公司
     */
    @TableField("fusedcompname")
    private String fusedcompname;
    /**
     * 从使用部门
     */
    @TableField("fusedpartid")
    private String fusedpartid;
    /**
     * 从使用部门
     */
    @TableField("fusedpartname")
    private String fusedpartname;
    /**
     * 从使用人
     */
    @TableField("fuseduserid")
    private String fuseduserid;
    /**
     * 从使用人
     */
    @TableField("fusedusername")
    private String fusedusername;
    /**
     * 从区域
     */
    @TableField("floc")
    private String floc;
    /**
     * 从区域
     */
    @TableField("flocname")
    private String flocname;
    /**
     * 从位置
     */
    @TableField("flocdtl")
    private String flocdtl;
    /**
     * 去使用公司
     */
    @TableField("tusedcompid")
    private String tusedcompid;
    /**
     * 去使用公司
     */
    @TableField("tusedcompname")
    private String tusedcompname;
    /**
     * 去使用部门
     */
    @TableField("tusedpartid")
    private String tusedpartid;
    /**
     * 去使用部门
     */
    @TableField("tusedpartname")
    private String tusedpartname;
    /**
     * 去使用人
     */
    @TableField("tuseduserid")
    private String tuseduserid;
    /**
     * 去使用人
     */
    @TableField("tusedusername")
    private String tusedusername;
    /**
     * 去区域
     */
    @TableField("tloc")
    private String tloc;
    /**
     * 去区域
     */
    @TableField("tlocname")
    private String tlocname;
    /**
     * 去位置
     */
    @TableField("tlocdtl")
    private String tlocdtl;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public BigDecimal getTranfercnt() {
        return tranfercnt;
    }

    public void setTranfercnt(BigDecimal tranfercnt) {
        this.tranfercnt = tranfercnt;
    }

    public Date getRtransferdate() {
        return rtransferdate;
    }

    public void setRtransferdate(Date rtransferdate) {
        this.rtransferdate = rtransferdate;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getTranferstatus() {
        return tranferstatus;
    }

    public void setTranferstatus(String tranferstatus) {
        this.tranferstatus = tranferstatus;
    }

    public String getFrecycle() {
        return frecycle;
    }

    public void setFrecycle(String frecycle) {
        this.frecycle = frecycle;
    }

    public String getTrecycle() {
        return trecycle;
    }

    public void setTrecycle(String trecycle) {
        this.trecycle = trecycle;
    }

    public String getFbelongcompid() {
        return fbelongcompid;
    }

    public void setFbelongcompid(String fbelongcompid) {
        this.fbelongcompid = fbelongcompid;
    }

    public String getFbelongcompname() {
        return fbelongcompname;
    }

    public void setFbelongcompname(String fbelongcompname) {
        this.fbelongcompname = fbelongcompname;
    }

    public String getFusedcompid() {
        return fusedcompid;
    }

    public void setFusedcompid(String fusedcompid) {
        this.fusedcompid = fusedcompid;
    }

    public String getFusedcompname() {
        return fusedcompname;
    }

    public void setFusedcompname(String fusedcompname) {
        this.fusedcompname = fusedcompname;
    }

    public String getFusedpartid() {
        return fusedpartid;
    }

    public void setFusedpartid(String fusedpartid) {
        this.fusedpartid = fusedpartid;
    }

    public String getFusedpartname() {
        return fusedpartname;
    }

    public void setFusedpartname(String fusedpartname) {
        this.fusedpartname = fusedpartname;
    }

    public String getFuseduserid() {
        return fuseduserid;
    }

    public void setFuseduserid(String fuseduserid) {
        this.fuseduserid = fuseduserid;
    }

    public String getFusedusername() {
        return fusedusername;
    }

    public void setFusedusername(String fusedusername) {
        this.fusedusername = fusedusername;
    }

    public String getFloc() {
        return floc;
    }

    public void setFloc(String floc) {
        this.floc = floc;
    }

    public String getFlocname() {
        return flocname;
    }

    public void setFlocname(String flocname) {
        this.flocname = flocname;
    }

    public String getFlocdtl() {
        return flocdtl;
    }

    public void setFlocdtl(String flocdtl) {
        this.flocdtl = flocdtl;
    }

    public String getTusedcompid() {
        return tusedcompid;
    }

    public void setTusedcompid(String tusedcompid) {
        this.tusedcompid = tusedcompid;
    }

    public String getTusedcompname() {
        return tusedcompname;
    }

    public void setTusedcompname(String tusedcompname) {
        this.tusedcompname = tusedcompname;
    }

    public String getTusedpartid() {
        return tusedpartid;
    }

    public void setTusedpartid(String tusedpartid) {
        this.tusedpartid = tusedpartid;
    }

    public String getTusedpartname() {
        return tusedpartname;
    }

    public void setTusedpartname(String tusedpartname) {
        this.tusedpartname = tusedpartname;
    }

    public String getTuseduserid() {
        return tuseduserid;
    }

    public void setTuseduserid(String tuseduserid) {
        this.tuseduserid = tuseduserid;
    }

    public String getTusedusername() {
        return tusedusername;
    }

    public void setTusedusername(String tusedusername) {
        this.tusedusername = tusedusername;
    }

    public String getTloc() {
        return tloc;
    }

    public void setTloc(String tloc) {
        this.tloc = tloc;
    }

    public String getTlocname() {
        return tlocname;
    }

    public void setTlocname(String tlocname) {
        this.tlocname = tlocname;
    }

    public String getTlocdtl() {
        return tlocdtl;
    }

    public void setTlocdtl(String tlocdtl) {
        this.tlocdtl = tlocdtl;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ResTranferItem{" +
        "id=" + id +
        ", resid=" + resid +
        ", resname=" + resname +
        ", tranfercnt=" + tranfercnt +
        ", rtransferdate=" + rtransferdate +
        ", busid=" + busid +
        ", tranferstatus=" + tranferstatus +
        ", frecycle=" + frecycle +
        ", trecycle=" + trecycle +
        ", fbelongcompid=" + fbelongcompid +
        ", fbelongcompname=" + fbelongcompname +
        ", fusedcompid=" + fusedcompid +
        ", fusedcompname=" + fusedcompname +
        ", fusedpartid=" + fusedpartid +
        ", fusedpartname=" + fusedpartname +
        ", fuseduserid=" + fuseduserid +
        ", fusedusername=" + fusedusername +
        ", floc=" + floc +
        ", flocname=" + flocname +
        ", flocdtl=" + flocdtl +
        ", tusedcompid=" + tusedcompid +
        ", tusedcompname=" + tusedcompname +
        ", tusedpartid=" + tusedpartid +
        ", tusedpartname=" + tusedpartname +
        ", tuseduserid=" + tuseduserid +
        ", tusedusername=" + tusedusername +
        ", tloc=" + tloc +
        ", tlocname=" + tlocname +
        ", tlocdtl=" + tlocdtl +
        ", mark=" + mark +
        "}";
    }
}
