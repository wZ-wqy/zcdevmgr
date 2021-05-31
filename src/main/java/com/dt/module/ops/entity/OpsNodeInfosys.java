package com.dt.module.ops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-03-06
 */

@TableName("ops_node_infosys")

public class OpsNodeInfosys extends BaseModel<OpsNodeInfosys> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("about")
    private String about;
    @TableField("status")
    private String status;
    @TableField("ifmain")
    private String ifmain;
    @TableField("opsmethod")
    private String opsmethod;
    @TableField("devmethod")
    private String devmethod;
    @TableField("tcontact")
    private String tcontact;
    @TableField("bcontact")
    private String bcontact;
    @TableField("bpart")
    private String bpart;
    @TableField("lastdrilldate")
    private String lastdrilldate;
    @TableField("ondatestr")
    private String ondatestr;
    @TableField("downdatestr")
    private String downdatestr;
    @TableField("os")
    private String os;
    @TableField("db")
    private String db;
    @TableField("app")
    private String app;
    @TableField("grade")
    private String grade;
    @TableField("rto")
    private String rto;
    @TableField("rpo")
    private String rpo;
    @TableField("Hardware")
    private String Hardware;
    @TableField("bkmethod")
    private String bkmethod;
    @TableField("sameplacebkmethod")
    private String sameplacebkmethod;
    @TableField("diffplacebkmethod")
    private String diffplacebkmethod;
    @TableField("type")
    private String type;
    @TableField("label1")
    private String label1;
    @TableField("label2")
    private String label2;
    @TableField("label3")
    private String label3;
    @TableField("label4")
    private String label4;
    @TableField("mark")
    private String mark;


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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIfmain() {
        return ifmain;
    }

    public void setIfmain(String ifmain) {
        this.ifmain = ifmain;
    }

    public String getOpsmethod() {
        return opsmethod;
    }

    public void setOpsmethod(String opsmethod) {
        this.opsmethod = opsmethod;
    }

    public String getDevmethod() {
        return devmethod;
    }

    public void setDevmethod(String devmethod) {
        this.devmethod = devmethod;
    }

    public String getTcontact() {
        return tcontact;
    }

    public void setTcontact(String tcontact) {
        this.tcontact = tcontact;
    }

    public String getBcontact() {
        return bcontact;
    }

    public void setBcontact(String bcontact) {
        this.bcontact = bcontact;
    }

    public String getBpart() {
        return bpart;
    }

    public void setBpart(String bpart) {
        this.bpart = bpart;
    }

    public String getLastdrilldate() {
        return lastdrilldate;
    }

    public void setLastdrilldate(String lastdrilldate) {
        this.lastdrilldate = lastdrilldate;
    }

    public String getOndatestr() {
        return ondatestr;
    }

    public void setOndatestr(String ondatestr) {
        this.ondatestr = ondatestr;
    }

    public String getDowndatestr() {
        return downdatestr;
    }

    public void setDowndatestr(String downdatestr) {
        this.downdatestr = downdatestr;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRto() {
        return rto;
    }

    public void setRto(String rto) {
        this.rto = rto;
    }

    public String getRpo() {
        return rpo;
    }

    public void setRpo(String rpo) {
        this.rpo = rpo;
    }

    public String getHardware() {
        return Hardware;
    }

    public void setHardware(String Hardware) {
        this.Hardware = Hardware;
    }

    public String getBkmethod() {
        return bkmethod;
    }

    public void setBkmethod(String bkmethod) {
        this.bkmethod = bkmethod;
    }

    public String getSameplacebkmethod() {
        return sameplacebkmethod;
    }

    public void setSameplacebkmethod(String sameplacebkmethod) {
        this.sameplacebkmethod = sameplacebkmethod;
    }

    public String getDiffplacebkmethod() {
        return diffplacebkmethod;
    }

    public void setDiffplacebkmethod(String diffplacebkmethod) {
        this.diffplacebkmethod = diffplacebkmethod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public String getLabel4() {
        return label4;
    }

    public void setLabel4(String label4) {
        this.label4 = label4;
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
        return "OpsNodeInfosys{" +
                "id=" + id +
                ", name=" + name +
                ", about=" + about +
                ", status=" + status +
                ", ifmain=" + ifmain +
                ", opsmethod=" + opsmethod +
                ", devmethod=" + devmethod +
                ", tcontact=" + tcontact +
                ", bcontact=" + bcontact +
                ", bpart=" + bpart +
                ", lastdrilldate=" + lastdrilldate +
                ", ondatestr=" + ondatestr +
                ", downdatestr=" + downdatestr +
                ", os=" + os +
                ", db=" + db +
                ", app=" + app +
                ", grade=" + grade +
                ", rto=" + rto +
                ", rpo=" + rpo +
                ", Hardware=" + Hardware +
                ", bkmethod=" + bkmethod +
                ", sameplacebkmethod=" + sameplacebkmethod +
                ", diffplacebkmethod=" + diffplacebkmethod +
                ", type=" + type +
                ", label1=" + label1 +
                ", label2=" + label2 +
                ", label3=" + label3 +
                ", label4=" + label4 +
                ", mark=" + mark +
                "}";
    }
}
