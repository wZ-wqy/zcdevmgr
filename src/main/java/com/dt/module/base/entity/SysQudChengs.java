package com.dt.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2018-07-27
 */
@TableName("SYS_QUD_CHENGS")
public class SysQudChengs extends BaseModel<SysQudChengs> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("MINGC")
    private String mingc;
    @TableField("JIB")
    private String jib;
    @TableField("SHENGF_ID")
    private String shengfId;
    @TableField("YOUB")
    private String youb;
    @TableField("QUH")
    private String quh;
    @TableField("CHENGSID_TQ")
    private String chengsidTq;
    @TableField("JIANC")
    private String jianc;
    @TableField("GUOBM")
    private String guobm;
    @TableField("RENKSL")
    private String renksl;
    @TableField("RENJSR")
    private String renjsr;
    @TableField("RENJXSZC")
    private String renjxszc;
    @TableField("RENJFZZC")
    private String renjfzzc;
    @TableField("XIUGR")
    private String xiugr;
    @TableField("XIUGRQ")
    private Date xiugrq;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMingc() {
        return mingc;
    }

    public void setMingc(String mingc) {
        this.mingc = mingc;
    }

    public String getJib() {
        return jib;
    }

    public void setJib(String jib) {
        this.jib = jib;
    }

    public String getShengfId() {
        return shengfId;
    }

    public void setShengfId(String shengfId) {
        this.shengfId = shengfId;
    }

    public String getYoub() {
        return youb;
    }

    public void setYoub(String youb) {
        this.youb = youb;
    }

    public String getQuh() {
        return quh;
    }

    public void setQuh(String quh) {
        this.quh = quh;
    }

    public String getChengsidTq() {
        return chengsidTq;
    }

    public void setChengsidTq(String chengsidTq) {
        this.chengsidTq = chengsidTq;
    }

    public String getJianc() {
        return jianc;
    }

    public void setJianc(String jianc) {
        this.jianc = jianc;
    }

    public String getGuobm() {
        return guobm;
    }

    public void setGuobm(String guobm) {
        this.guobm = guobm;
    }

    public String getRenksl() {
        return renksl;
    }

    public void setRenksl(String renksl) {
        this.renksl = renksl;
    }

    public String getRenjsr() {
        return renjsr;
    }

    public void setRenjsr(String renjsr) {
        this.renjsr = renjsr;
    }

    public String getRenjxszc() {
        return renjxszc;
    }

    public void setRenjxszc(String renjxszc) {
        this.renjxszc = renjxszc;
    }

    public String getRenjfzzc() {
        return renjfzzc;
    }

    public void setRenjfzzc(String renjfzzc) {
        this.renjfzzc = renjfzzc;
    }

    public String getXiugr() {
        return xiugr;
    }

    public void setXiugr(String xiugr) {
        this.xiugr = xiugr;
    }

    public Date getXiugrq() {
        return xiugrq;
    }

    public void setXiugrq(Date xiugrq) {
        this.xiugrq = xiugrq;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysQudChengs{" +
                ", id=" + id +
                ", mingc=" + mingc +
                ", jib=" + jib +
                ", shengfId=" + shengfId +
                ", youb=" + youb +
                ", quh=" + quh +
                ", chengsidTq=" + chengsidTq +
                ", jianc=" + jianc +
                ", guobm=" + guobm +
                ", renksl=" + renksl +
                ", renjsr=" + renjsr +
                ", renjxszc=" + renjxszc +
                ", renjfzzc=" + renjfzzc +
                ", xiugr=" + xiugr +
                ", xiugrq=" + xiugrq +
                "}";
    }
}
