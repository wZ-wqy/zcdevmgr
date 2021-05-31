package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
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
 * @since 2020-12-22
 */
 
@TableName("res_tranfer")
 
public class ResTranfer extends BaseModel<ResTranfer> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 单据号
     */
    @TableField("busid")
    private String busid;
    /**
     * 业务办理状态
     */
    @TableField("status")
    private String status;
    @TableField("pinst")
    private String pinst;
    /**
     * 单据标题
     */
    @TableField("name")
    private String name;
    /**
     * 业务时间
     */
    @TableField("busdate")
    private Date busdate;
    /**
     * 转移数量
     */
    @TableField("tranfercnt")
    private BigDecimal tranfercnt;
    /**
     * 转移状态,0 未完成,1 完成
     */
    @TableField("tranferstatus")
    private String tranferstatus;
    /**
     * 转移原因类型
     */
    @TableField("transfertype")
    private String transfertype;
    /**
     * 转移原因类型,离职，调离等
     */
    @TableField("transfertypename")
    private String transfertypename;
    /**
     * 转移原因类型
     */
    @TableField("transfercatid")
    private String transfercatid;
    /**
     * 转移原因类型，跨部门转移
     */
    @TableField("transfercatname")
    private String transfercatname;
    /**
     * 业务类型
     */
    @TableField("bustype")
    private String bustype;
    /**
     * 转移时间
     */
    @TableField("transferdate")
    private Date transferdate;
    /**
     * 转移人ID
     */
    @TableField("transferuserid")
    private String transferuserid;
    /**
     * 调拨人姓名
     */
    @TableField("transferusername")
    private String transferusername;
    /**
     * 接收人ID
     */
    @TableField("receiveruserid")
    private String receiveruserid;
    /**
     * 接收时间
     */
    @TableField("receivedate")
    private Date receivedate;
    /**
     * 接收人姓名
     */
    @TableField("receiverusername")
    private String receiverusername;
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
    /**
     * 流程申请
     */
    @TableField("flowapply")
    private String flowapply;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBusdate() {
        return busdate;
    }

    public void setBusdate(Date busdate) {
        this.busdate = busdate;
    }

    public BigDecimal getTranfercnt() {
        return tranfercnt;
    }

    public void setTranfercnt(BigDecimal tranfercnt) {
        this.tranfercnt = tranfercnt;
    }

    public String getTranferstatus() {
        return tranferstatus;
    }

    public void setTranferstatus(String tranferstatus) {
        this.tranferstatus = tranferstatus;
    }

    public String getTransfertype() {
        return transfertype;
    }

    public void setTransfertype(String transfertype) {
        this.transfertype = transfertype;
    }

    public String getTransfertypename() {
        return transfertypename;
    }

    public void setTransfertypename(String transfertypename) {
        this.transfertypename = transfertypename;
    }

    public String getTransfercatid() {
        return transfercatid;
    }

    public void setTransfercatid(String transfercatid) {
        this.transfercatid = transfercatid;
    }

    public String getTransfercatname() {
        return transfercatname;
    }

    public void setTransfercatname(String transfercatname) {
        this.transfercatname = transfercatname;
    }

    public String getBustype() {
        return bustype;
    }

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }

    public Date getTransferdate() {
        return transferdate;
    }

    public void setTransferdate(Date transferdate) {
        this.transferdate = transferdate;
    }

    public String getTransferuserid() {
        return transferuserid;
    }

    public void setTransferuserid(String transferuserid) {
        this.transferuserid = transferuserid;
    }

    public String getTransferusername() {
        return transferusername;
    }

    public void setTransferusername(String transferusername) {
        this.transferusername = transferusername;
    }

    public String getReceiveruserid() {
        return receiveruserid;
    }

    public void setReceiveruserid(String receiveruserid) {
        this.receiveruserid = receiveruserid;
    }

    public Date getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(Date receivedate) {
        this.receivedate = receivedate;
    }

    public String getReceiverusername() {
        return receiverusername;
    }

    public void setReceiverusername(String receiverusername) {
        this.receiverusername = receiverusername;
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

    public String getFlowapply() {
        return flowapply;
    }

    public void setFlowapply(String flowapply) {
        this.flowapply = flowapply;
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
        return "ResTranfer{" +
        "id=" + id +
        ", busid=" + busid +
        ", status=" + status +
        ", pinst=" + pinst +
        ", name=" + name +
        ", busdate=" + busdate +
        ", tranfercnt=" + tranfercnt +
        ", tranferstatus=" + tranferstatus +
        ", transfertype=" + transfertype +
        ", transfertypename=" + transfertypename +
        ", transfercatid=" + transfercatid +
        ", transfercatname=" + transfercatname +
        ", bustype=" + bustype +
        ", transferdate=" + transferdate +
        ", transferuserid=" + transferuserid +
        ", transferusername=" + transferusername +
        ", receiveruserid=" + receiveruserid +
        ", receivedate=" + receivedate +
        ", receiverusername=" + receiverusername +
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
        ", flowapply=" + flowapply +
        ", mark=" + mark +
        "}";
    }
}
