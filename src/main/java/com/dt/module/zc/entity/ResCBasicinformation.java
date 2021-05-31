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
 * @since 2020-09-21
 */

@TableName("res_c_basicinformation")

public class ResCBasicinformation extends BaseModel<ResCBasicinformation> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    @TableField("busuuid")
    private String busuuid;
    @TableField("resid")
    private String resid;
    @TableField("status")
    private String status;
    @TableField("processuserid")
    private String processuserid;
    @TableField("processusername")
    private String processusername;
    @TableField("tclassid")
    private String tclassid;
    @TableField("tmodel")
    private String tmodel;
    @TableField("tsn")
    private String tsn;
    @TableField("tzcsource")
    private String tzcsource;
    @TableField("tzccnt")
    private BigDecimal tzccnt;
    @TableField("tsupplier")
    private String tsupplier;
    @TableField("tbrand")
    private String tbrand;
    @TableField("tbuytime")
    private Date tbuytime;
    @TableField("tloc")
    private String tloc;
    @TableField("tusefullife")
    private String tusefullife;
    @TableField("tusedcompanyid")
    private String tusedcompanyid;
    @TableField("tpartid")
    private String tpartid;
    @TableField("tuseduserid")
    private String tuseduserid;
    @TableField("mark")
    private String mark;
    @TableField("tclassidstatus")
    private String tclassidstatus;
    @TableField("tmodelstatus")
    private String tmodelstatus;
    @TableField("tsnstatus")
    private String tsnstatus;
    @TableField("tzcsourcestatus")
    private String tzcsourcestatus;
    @TableField("tzccntstatus")
    private String tzccntstatus;
    @TableField("tsupplierstatus")
    private String tsupplierstatus;
    @TableField("tbrandstatus")
    private String tbrandstatus;
    @TableField("tbuytimestatus")
    private String tbuytimestatus;
    @TableField("tlocstatus")
    private String tlocstatus;
    @TableField("tusefullifestatus")
    private String tusefullifestatus;
    @TableField("tusedcompanyidstatus")
    private String tusedcompanyidstatus;
    @TableField("tpartidstatus")
    private String tpartidstatus;
    @TableField("tuseduseridstatus")
    private String tuseduseridstatus;
    @TableField("tlabel1")
    private String tlabel1;
    @TableField("tunit")
    private String tunit;
    @TableField("tconfdesc")
    private String tconfdesc;
    @TableField("tlocdtl")
    private String tlocdtl;
    @TableField("tlabel1status")
    private String tlabel1status;
    @TableField("tunitstatus")
    private String tunitstatus;
    @TableField("tconfdescstatus")
    private String tconfdescstatus;
    @TableField("tlocdtlstatus")
    private String tlocdtlstatus;
    @TableField("pinst")
    private String pinst;
    @TableField("tfs20")
    private String tfs20;
    @TableField("tfd1")
    private Date tfd1;
    @TableField("tmark")
    private String tmark;
    @TableField("tfs20status")
    private String tfs20status;
    @TableField("tfd1status")
    private String tfd1status;
    @TableField("tmarkstatus")
    private String tmarkstatus;

    @TableField("tnamestatus")
    private String tnamestatus;
    @TableField("tname")
    private String tname;

    @TableField("tbelongcomp")
    private String tbelongcomp;

    @TableField("tbelongcompstatus")
    private String tbelongcompstatus;

    @TableField("tlabel2")
    private String tlabel2;

    @TableField("tlabel2status")
    private String tlabel2status;

    @TableField("tbatch")
    private String tbatch;

    @TableField("tbatchstatus")
    private String tbatchstatus;

    public String getTbelongcomp() {
        return tbelongcomp;
    }

    public void setTbelongcomp(String tbelongcomp) {
        this.tbelongcomp = tbelongcomp;
    }

    public String getTbelongcompstatus() {
        return tbelongcompstatus;
    }

    public void setTbelongcompstatus(String tbelongcompstatus) {
        this.tbelongcompstatus = tbelongcompstatus;
    }

    public String getTlabel2() {
        return tlabel2;
    }

    public void setTlabel2(String tlabel2) {
        this.tlabel2 = tlabel2;
    }

    public String getTlabel2status() {
        return tlabel2status;
    }

    public void setTlabel2status(String tlabel2status) {
        this.tlabel2status = tlabel2status;
    }

    public String getTbatch() {
        return tbatch;
    }

    public void setTbatch(String tbatch) {
        this.tbatch = tbatch;
    }

    public String getTbatchstatus() {
        return tbatchstatus;
    }

    public void setTbatchstatus(String tbatchstatus) {
        this.tbatchstatus = tbatchstatus;
    }


    public String getTnamestatus() {
        return tnamestatus;
    }

    public void setTnamestatus(String tnamestatus) {
        this.tnamestatus = tnamestatus;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
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

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTclassid() {
        return tclassid;
    }

    public void setTclassid(String tclassid) {
        this.tclassid = tclassid;
    }

    public String getTmodel() {
        return tmodel;
    }

    public void setTmodel(String tmodel) {
        this.tmodel = tmodel;
    }

    public String getTsn() {
        return tsn;
    }

    public void setTsn(String tsn) {
        this.tsn = tsn;
    }

    public String getTzcsource() {
        return tzcsource;
    }

    public void setTzcsource(String tzcsource) {
        this.tzcsource = tzcsource;
    }

    public BigDecimal getTzccnt() {
        return tzccnt;
    }

    public void setTzccnt(BigDecimal tzccnt) {
        this.tzccnt = tzccnt;
    }

    public String getTsupplier() {
        return tsupplier;
    }

    public void setTsupplier(String tsupplier) {
        this.tsupplier = tsupplier;
    }

    public String getTbrand() {
        return tbrand;
    }

    public void setTbrand(String tbrand) {
        this.tbrand = tbrand;
    }

    public Date getTbuytime() {
        return tbuytime;
    }

    public void setTbuytime(Date tbuytime) {
        this.tbuytime = tbuytime;
    }

    public String getTloc() {
        return tloc;
    }

    public void setTloc(String tloc) {
        this.tloc = tloc;
    }

    public String getTusefullife() {
        return tusefullife;
    }

    public void setTusefullife(String tusefullife) {
        this.tusefullife = tusefullife;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getTclassidstatus() {
        return tclassidstatus;
    }

    public void setTclassidstatus(String tclassidstatus) {
        this.tclassidstatus = tclassidstatus;
    }

    public String getTmodelstatus() {
        return tmodelstatus;
    }

    public void setTmodelstatus(String tmodelstatus) {
        this.tmodelstatus = tmodelstatus;
    }

    public String getTsnstatus() {
        return tsnstatus;
    }

    public void setTsnstatus(String tsnstatus) {
        this.tsnstatus = tsnstatus;
    }

    public String getTzcsourcestatus() {
        return tzcsourcestatus;
    }

    public void setTzcsourcestatus(String tzcsourcestatus) {
        this.tzcsourcestatus = tzcsourcestatus;
    }

    public String getTzccntstatus() {
        return tzccntstatus;
    }

    public void setTzccntstatus(String tzccntstatus) {
        this.tzccntstatus = tzccntstatus;
    }

    public String getTsupplierstatus() {
        return tsupplierstatus;
    }

    public void setTsupplierstatus(String tsupplierstatus) {
        this.tsupplierstatus = tsupplierstatus;
    }

    public String getTbrandstatus() {
        return tbrandstatus;
    }

    public void setTbrandstatus(String tbrandstatus) {
        this.tbrandstatus = tbrandstatus;
    }

    public String getTbuytimestatus() {
        return tbuytimestatus;
    }

    public void setTbuytimestatus(String tbuytimestatus) {
        this.tbuytimestatus = tbuytimestatus;
    }

    public String getTlocstatus() {
        return tlocstatus;
    }

    public void setTlocstatus(String tlocstatus) {
        this.tlocstatus = tlocstatus;
    }

    public String getTusefullifestatus() {
        return tusefullifestatus;
    }

    public void setTusefullifestatus(String tusefullifestatus) {
        this.tusefullifestatus = tusefullifestatus;
    }

    public String getTusedcompanyidstatus() {
        return tusedcompanyidstatus;
    }

    public void setTusedcompanyidstatus(String tusedcompanyidstatus) {
        this.tusedcompanyidstatus = tusedcompanyidstatus;
    }

    public String getTpartidstatus() {
        return tpartidstatus;
    }

    public void setTpartidstatus(String tpartidstatus) {
        this.tpartidstatus = tpartidstatus;
    }

    public String getTuseduseridstatus() {
        return tuseduseridstatus;
    }

    public void setTuseduseridstatus(String tuseduseridstatus) {
        this.tuseduseridstatus = tuseduseridstatus;
    }

    public String getTlabel1() {
        return tlabel1;
    }

    public void setTlabel1(String tlabel1) {
        this.tlabel1 = tlabel1;
    }

    public String getTunit() {
        return tunit;
    }

    public void setTunit(String tunit) {
        this.tunit = tunit;
    }

    public String getTconfdesc() {
        return tconfdesc;
    }

    public void setTconfdesc(String tconfdesc) {
        this.tconfdesc = tconfdesc;
    }

    public String getTlocdtl() {
        return tlocdtl;
    }

    public void setTlocdtl(String tlocdtl) {
        this.tlocdtl = tlocdtl;
    }

    public String getTlabel1status() {
        return tlabel1status;
    }

    public void setTlabel1status(String tlabel1status) {
        this.tlabel1status = tlabel1status;
    }

    public String getTunitstatus() {
        return tunitstatus;
    }

    public void setTunitstatus(String tunitstatus) {
        this.tunitstatus = tunitstatus;
    }

    public String getTconfdescstatus() {
        return tconfdescstatus;
    }

    public void setTconfdescstatus(String tconfdescstatus) {
        this.tconfdescstatus = tconfdescstatus;
    }

    public String getTlocdtlstatus() {
        return tlocdtlstatus;
    }

    public void setTlocdtlstatus(String tlocdtlstatus) {
        this.tlocdtlstatus = tlocdtlstatus;
    }

    public String getPinst() {
        return pinst;
    }

    public void setPinst(String pinst) {
        this.pinst = pinst;
    }

    public String getTfs20() {
        return tfs20;
    }

    public void setTfs20(String tfs20) {
        this.tfs20 = tfs20;
    }

    public Date getTfd1() {
        return tfd1;
    }

    public void setTfd1(Date tfd1) {
        this.tfd1 = tfd1;
    }

    public String getTmark() {
        return tmark;
    }

    public void setTmark(String tmark) {
        this.tmark = tmark;
    }

    public String getTfs20status() {
        return tfs20status;
    }

    public void setTfs20status(String tfs20status) {
        this.tfs20status = tfs20status;
    }

    public String getTfd1status() {
        return tfd1status;
    }

    public void setTfd1status(String tfd1status) {
        this.tfd1status = tfd1status;
    }

    public String getTmarkstatus() {
        return tmarkstatus;
    }

    public void setTmarkstatus(String tmarkstatus) {
        this.tmarkstatus = tmarkstatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResCBasicinformation{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", resid=" + resid +
                ", status=" + status +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", tclassid=" + tclassid +
                ", tmodel=" + tmodel +
                ", tsn=" + tsn +
                ", tzcsource=" + tzcsource +
                ", tzccnt=" + tzccnt +
                ", tsupplier=" + tsupplier +
                ", tbrand=" + tbrand +
                ", tbuytime=" + tbuytime +
                ", tloc=" + tloc +
                ", tusefullife=" + tusefullife +
                ", tusedcompanyid=" + tusedcompanyid +
                ", tpartid=" + tpartid +
                ", tuseduserid=" + tuseduserid +
                ", mark=" + mark +
                ", tclassidstatus=" + tclassidstatus +
                ", tmodelstatus=" + tmodelstatus +
                ", tsnstatus=" + tsnstatus +
                ", tzcsourcestatus=" + tzcsourcestatus +
                ", tzccntstatus=" + tzccntstatus +
                ", tsupplierstatus=" + tsupplierstatus +
                ", tbrandstatus=" + tbrandstatus +
                ", tbuytimestatus=" + tbuytimestatus +
                ", tlocstatus=" + tlocstatus +
                ", tusefullifestatus=" + tusefullifestatus +
                ", tusedcompanyidstatus=" + tusedcompanyidstatus +
                ", tpartidstatus=" + tpartidstatus +
                ", tuseduseridstatus=" + tuseduseridstatus +
                ", tlabel1=" + tlabel1 +
                ", tunit=" + tunit +
                ", tconfdesc=" + tconfdesc +
                ", tlocdtl=" + tlocdtl +
                ", tlabel1status=" + tlabel1status +
                ", tunitstatus=" + tunitstatus +
                ", tconfdescstatus=" + tconfdescstatus +
                ", tlocdtlstatus=" + tlocdtlstatus +
                ", pinst=" + pinst +
                ", tfs20=" + tfs20 +
                ", tfd1=" + tfd1 +
                ", tmark=" + tmark +
                ", tfs20status=" + tfs20status +
                ", tfd1status=" + tfd1status +
                ", tmarkstatus=" + tmarkstatus +
                "}";
    }
}
