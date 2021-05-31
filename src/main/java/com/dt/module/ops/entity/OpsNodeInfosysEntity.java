package com.dt.module.ops.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-03-06
 */

@ExcelTarget("OpsNodeInfosysEntity")

public class OpsNodeInfosysEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "编号", width = 30)
    private String id;
    @Excel(name = "名称", width = 30)
    private String name;
    @Excel(name = "介绍", width = 30)
    private String about;
    @Excel(name = "类型", width = 30)
    private String typestr;
    @Excel(name = "是否重要信息", width = 30)
    private String ifmain;
    @Excel(name = "评级", width = 30)
    private String gradestr;
    @Excel(name = "RTO", width = 30)
    private String rto;
    @Excel(name = "RPO", width = 30)
    private String rpo;
    @Excel(name = "运维模式", width = 30)
    private String opsmethodstr;
    @Excel(name = "开发模式", width = 30)
    private String devmethodstr;
    @Excel(name = "技术联系", width = 50)
    private String tcontact;
    @Excel(name = "业务联系", width = 50)
    private String bcontact;
    @Excel(name = "系统所属", width = 30)
    private String bpart;
    @Excel(name = "最近演练", width = 30)
    private String lastdrilldate;
    @Excel(name = "上线时间", width = 30)
    private String ondatestr;
    @Excel(name = "下线时间", width = 30)
    private String downdatestr;
    @Excel(name = "硬件", width = 30)
    private String hardware;
    @Excel(name = "操作系统", width = 30)
    private String os;
    @Excel(name = "数据库", width = 30)
    private String db;
    @Excel(name = "应用", width = 30)
    private String app;
    @Excel(name = "同城", width = 30)
    private String sameplacebkmethod;
    @Excel(name = "异地", width = 30)
    private String diffplacebkmethod;
    @Excel(name = "备份", width = 30)
    private String bkmethod;
    @Excel(name = "标签1", width = 30)
    private String label1;
    @Excel(name = "标签2", width = 30)
    private String label2;
    @Excel(name = "备注", width = 30)
    @TableField("mark")
    private String mark;

    /**
     * @return the hardware
     */
    public String getHardware() {
        return hardware;
    }

    /**
     * @param hardware the hardware to set
     */
    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

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

    public String getIfmain() {
        return ifmain;
    }

    public void setIfmain(String ifmain) {
        this.ifmain = ifmain;
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

    /**
     * @return the gradestr
     */
    public String getGradestr() {
        return gradestr;
    }

    /**
     * @param gradestr the gradestr to set
     */
    public void setGradestr(String gradestr) {
        this.gradestr = gradestr;
    }

    /**
     * @return the opsmethodstr
     */
    public String getOpsmethodstr() {
        return opsmethodstr;
    }

    /**
     * @param opsmethodstr the opsmethodstr to set
     */
    public void setOpsmethodstr(String opsmethodstr) {
        this.opsmethodstr = opsmethodstr;
    }

    /**
     * @return the devmethodstr
     */
    public String getDevmethodstr() {
        return devmethodstr;
    }

    /**
     * @param devmethodstr the devmethodstr to set
     */
    public void setDevmethodstr(String devmethodstr) {
        this.devmethodstr = devmethodstr;
    }

    /**
     * @return the typestr
     */
    public String getTypestr() {
        return typestr;
    }

    /**
     * @param typestr the typestr to set
     */
    public void setTypestr(String typestr) {
        this.typestr = typestr;
    }

    /**
     * @return the mark
     */
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void fullEntity(JSONObject obj) {
        this.name = obj.getString("name");
        this.id = obj.getString("id");
        this.about = obj.getString("about");
        this.typestr = obj.getString("typestr");
        this.ifmain = obj.getString("ifmain");
        this.gradestr = obj.getString("gradestr");
        this.rto = obj.getString("rto");
        this.rpo = obj.getString("rpo");
        this.opsmethodstr = obj.getString("opsmethodstr");
        this.devmethodstr = obj.getString("devmethodstr");
        this.tcontact = obj.getString("tcontact");
        this.bcontact = obj.getString("bcontact");
        this.bpart = obj.getString("bpart");
        this.lastdrilldate = obj.getString("lastdrilldate");
        this.ondatestr = obj.getString("ondatestr");
        this.downdatestr = obj.getString("downdatestr");
        this.hardware = obj.getString("hardware");
        this.os = obj.getString("os");
        this.db = obj.getString("db");
        this.app = obj.getString("app");
        this.sameplacebkmethod = obj.getString("sameplacebkmethod");
        this.diffplacebkmethod = obj.getString("diffplacebkmethod");
        this.bkmethod = obj.getString("bkmethod");
        this.label1 = obj.getString("label1");

        this.label2 = obj.getString("label2");

        this.mark = obj.getString("mark");


    }

}
