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
 
@TableName("res_c_basicinformation_item")

public class ResCBasicinformationItem extends BaseModel<ResCBasicinformationItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busuuid")
    private String busuuid;
    @TableField("resid")
    private String resid;
    @TableField("fclassid")
    private String fclassid;
    @TableField("fmodel")
    private String fmodel;
    @TableField("fsn")
    private String fsn;
    @TableField("fzcsource")
    private String fzcsource;
    @TableField("fzccnt")
    private BigDecimal fzccnt;
    @TableField("fsupplier")
    private String fsupplier;
    @TableField("fbrand")
    private String fbrand;
    @TableField("fbuytime")
    private Date fbuytime;
    @TableField("floc")
    private String floc;
    @TableField("fusefullife")
    private String fusefullife;
    @TableField("fusedcompanyid")
    private String fusedcompanyid;
    @TableField("fpartid")
    private String fpartid;
    @TableField("fuseduserid")
    private String fuseduserid;
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
    @TableField("flabel1")
    private String flabel1;
    @TableField("funit")
    private String funit;
    @TableField("fconfdesc")
    private String fconfdesc;
    @TableField("flocdtl")
    private String flocdtl;
    @TableField("tlabel1status")
    private String tlabel1status;
    @TableField("tunitstatus")
    private String tunitstatus;
    @TableField("tconfdescstatus")
    private String tconfdescstatus;
    @TableField("tlocdtlstatus")
    private String tlocdtlstatus;
    @TableField("tfs20")
    private String tfs20;
    @TableField("tfd1")
    private Date tfd1;
    @TableField("tmark")
    private String tmark;
    @TableField("ffs20")
    private String ffs20;
    @TableField("ffd1")
    private Date ffd1;
    @TableField("fmark")
    private String fmark;
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
    @TableField("fname")
    private String fname;

    @TableField("fbelongcomp")
    private String fbelongcomp;

    @TableField("tbelongcomp")
    private String tbelongcomp;

    @TableField("tbelongcompstatus")
    private String tbelongcompstatus;

    @TableField("flabel2")
    private String flabel2;

    @TableField("tlabel2")
    private String tlabel2;

    @TableField("tlabel2status")
    private String tlabel2status;

    @TableField("fbatch")
    private String fbatch;

    @TableField("tbatch")
    private String tbatch;

    @TableField("tbatchstatus")
    private String tbatchstatus;


    public String getFbelongcomp() {
        return fbelongcomp;
    }

    public void setFbelongcomp(String fbelongcomp) {
        this.fbelongcomp = fbelongcomp;
    }

    public String getFlabel2() {
        return flabel2;
    }

    public void setFlabel2(String flabel2) {
        this.flabel2 = flabel2;
    }

    public String getFbatch() {
        return fbatch;
    }

    public void setFbatch(String fbatch) {
        this.fbatch = fbatch;
    }

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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
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

    public String getFclassid() {
        return fclassid;
    }

    public void setFclassid(String fclassid) {
        this.fclassid = fclassid;
    }

    public String getFmodel() {
        return fmodel;
    }

    public void setFmodel(String fmodel) {
        this.fmodel = fmodel;
    }

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public String getFzcsource() {
        return fzcsource;
    }

    public void setFzcsource(String fzcsource) {
        this.fzcsource = fzcsource;
    }

    public BigDecimal getFzccnt() {
        return fzccnt;
    }

    public void setFzccnt(BigDecimal fzccnt) {
        this.fzccnt = fzccnt;
    }

    public String getFsupplier() {
        return fsupplier;
    }

    public void setFsupplier(String fsupplier) {
        this.fsupplier = fsupplier;
    }

    public String getFbrand() {
        return fbrand;
    }

    public void setFbrand(String fbrand) {
        this.fbrand = fbrand;
    }

    public Date getFbuytime() {
        return fbuytime;
    }

    public void setFbuytime(Date fbuytime) {
        this.fbuytime = fbuytime;
    }

    public String getFloc() {
        return floc;
    }

    public void setFloc(String floc) {
        this.floc = floc;
    }

    public String getFusefullife() {
        return fusefullife;
    }

    public void setFusefullife(String fusefullife) {
        this.fusefullife = fusefullife;
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

    public String getFlabel1() {
        return flabel1;
    }

    public void setFlabel1(String flabel1) {
        this.flabel1 = flabel1;
    }

    public String getFunit() {
        return funit;
    }

    public void setFunit(String funit) {
        this.funit = funit;
    }

    public String getFconfdesc() {
        return fconfdesc;
    }

    public void setFconfdesc(String fconfdesc) {
        this.fconfdesc = fconfdesc;
    }

    public String getFlocdtl() {
        return flocdtl;
    }

    public void setFlocdtl(String flocdtl) {
        this.flocdtl = flocdtl;
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

    public String getFfs20() {
        return ffs20;
    }

    public void setFfs20(String ffs20) {
        this.ffs20 = ffs20;
    }

    public Date getFfd1() {
        return ffd1;
    }

    public void setFfd1(Date ffd1) {
        this.ffd1 = ffd1;
    }

    public String getFmark() {
        return fmark;
    }

    public void setFmark(String fmark) {
        this.fmark = fmark;
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
        return "ResCBasicinformationItem{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", resid=" + resid +
                ", fclassid=" + fclassid +
                ", fmodel=" + fmodel +
                ", fsn=" + fsn +
                ", fzcsource=" + fzcsource +
                ", fzccnt=" + fzccnt +
                ", fsupplier=" + fsupplier +
                ", fbrand=" + fbrand +
                ", fbuytime=" + fbuytime +
                ", floc=" + floc +
                ", fusefullife=" + fusefullife +
                ", fusedcompanyid=" + fusedcompanyid +
                ", fpartid=" + fpartid +
                ", fuseduserid=" + fuseduserid +
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
                ", flabel1=" + flabel1 +
                ", funit=" + funit +
                ", fconfdesc=" + fconfdesc +
                ", flocdtl=" + flocdtl +
                ", tlabel1status=" + tlabel1status +
                ", tunitstatus=" + tunitstatus +
                ", tconfdescstatus=" + tconfdescstatus +
                ", tlocdtlstatus=" + tlocdtlstatus +
                ", tfs20=" + tfs20 +
                ", tfd1=" + tfd1 +
                ", tmark=" + tmark +
                ", ffs20=" + ffs20 +
                ", ffd1=" + ffd1 +
                ", fmark=" + fmark +
                ", tfs20status=" + tfs20status +
                ", tfd1status=" + tfd1status +
                ", tmarkstatus=" + tmarkstatus +
                "}";
    }
}
