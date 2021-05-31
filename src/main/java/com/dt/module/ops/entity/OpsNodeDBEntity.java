package com.dt.module.ops.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-01-24
 */
@ExcelTarget("OpsNodeDBEntity")
public class OpsNodeDBEntity {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    @Excel(name = "编号", width = 30)
    private String id;

    @Excel(name = "节点名称", width = 25)
    private String xtname;


    @Excel(name = "节点状态", width = 25)
    private String sysstatusstr;

    @Excel(name = "节点备份类型", width = 18)
    private String nodebackupstr;


    @Excel(name = "IP", width = 15)
    private String ip;

    @Excel(name = "数据库类型", width = 20)
    private String sysdbdtlstr;

    @Excel(name = "数据库名称", width = 15)
    private String dbinstance;

    @Excel(name = "备份策略", width = 15)
    private String bkstrategy;

    @Excel(name = "保留策略", width = 15)
    private String bkkeep;

    @Excel(name = "备份类型", width = 15)
    private String dbbktypestr;

    @Excel(name = "备份方式", width = 15)
    private String dbbkmethodstr;

    @Excel(name = "日志模式", width = 15)
    private String dbbkarchtypestr;

    @Excel(name = "备份大小", width = 15)
    private String dsize;


    @Excel(name = "当前状况", width = 15)
    private String dbbkstatusstr;

    @Excel(name = "备注", width = 30)
    private String mark;


    /**
     * @return the dsize
     */
    public String getDsize() {
        return dsize == null ? "" : dsize;
    }


    /**
     * @param dsize the dsize to set
     */
    public void setDsize(String dsize) {
        this.dsize = dsize;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return the xtname
     */
    public String getXtname() {
        return xtname == null ? "" : xtname;
    }


    /**
     * @param xtname the xtname to set
     */
    public void setXtname(String xtname) {
        this.xtname = xtname;
    }


    /**
     * @return the ip
     */
    public String getIp() {
        return ip == null ? "" : ip;
    }


    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }


    /**
     * @return the sysdbdtlstr
     */
    public String getSysdbdtlstr() {
        return sysdbdtlstr;
    }


    /**
     * @param sysdbdtlstr the sysdbdtlstr to set
     */
    public void setSysdbdtlstr(String sysdbdtlstr) {
        this.sysdbdtlstr = sysdbdtlstr;
    }


    /**
     * @return the dbinstance
     */
    public String getDbinstance() {
        return dbinstance == null ? "" : dbinstance;
    }


    /**
     * @param dbinstance the dbinstance to set
     */
    public void setDbinstance(String dbinstance) {
        this.dbinstance = dbinstance;
    }


    /**
     * @return the bkstrategy
     */
    public String getBkstrategy() {

        return bkstrategy == null ? "" : bkstrategy;
    }


    /**
     * @param bkstrategy the bkstrategy to set
     */
    public void setBkstrategy(String bkstrategy) {
        this.bkstrategy = bkstrategy;
    }


    /**
     * @return the bkkeep
     */
    public String getBkkeep() {
        return bkkeep == null ? "" : bkkeep;
    }


    /**
     * @param bkkeep the bkkeep to set
     */
    public void setBkkeep(String bkkeep) {
        this.bkkeep = bkkeep;
    }


    /**
     * @return the dbbktypestr
     */
    public String getDbbktypestr() {
        return dbbktypestr;
    }


    /**
     * @param dbbktypestr the dbbktypestr to set
     */
    public void setDbbktypestr(String dbbktypestr) {
        this.dbbktypestr = dbbktypestr;
    }


    /**
     * @return the dbbkmethodstr
     */
    public String getDbbkmethodstr() {
        return dbbkmethodstr;
    }


    /**
     * @param dbbkmethodstr the dbbkmethodstr to set
     */
    public void setDbbkmethodstr(String dbbkmethodstr) {
        this.dbbkmethodstr = dbbkmethodstr;
    }


    /**
     * @return the dbbkarchtypestr
     */
    public String getDbbkarchtypestr() {
        return dbbkarchtypestr;
    }


    /**
     * @param dbbkarchtypestr the dbbkarchtypestr to set
     */
    public void setDbbkarchtypestr(String dbbkarchtypestr) {
        this.dbbkarchtypestr = dbbkarchtypestr;
    }


    /**
     * @return the dbbkstatusstr
     */
    public String getDbbkstatusstr() {
        return dbbkstatusstr;
    }


    /**
     * @param dbbkstatusstr the dbbkstatusstr to set
     */
    public void setDbbkstatusstr(String dbbkstatusstr) {
        this.dbbkstatusstr = dbbkstatusstr;
    }


    /**
     * @return the mark
     */
    public String getMark() {
        return mark == null ? "" : mark;
    }


    /**
     * @param mark the mark to set
     */
    public void setMark(String mark) {
        this.mark = mark;
    }


    /**
     * @return the nodebackupstr
     */
    public String getNodebackupstr() {
        return nodebackupstr;
    }


    /**
     * @param nodebackupstr the nodebackupstr to set
     */
    public void setNodebackupstr(String nodebackupstr) {
        this.nodebackupstr = nodebackupstr;
    }


    public void fullEntity(JSONObject obj) {
        this.id = obj.getString("id");
        this.sysstatusstr = obj.getString("sysstatusstr");
        this.nodebackupstr = obj.getString("nodebackupstr");
        this.xtname = obj.getString("xtname");
        this.ip = obj.getString("ip");
        this.sysdbdtlstr = obj.getString("sysdbdtlstr");
        this.dbinstance = obj.getString("dbinstance");
        this.dbbkstatusstr = obj.getString("dbbkstatusstr");
        this.dbbktypestr = obj.getString("dbbktypestr");
        this.dbbkmethodstr = obj.getString("dbbkmethodstr");
        this.dbbkarchtypestr = obj.getString("dbbkarchtypestr");
        this.bkstrategy = obj.getString("bkstrategy");
        this.bkkeep = obj.getString("bkkeep");
        this.dsize = obj.getString("dsize");
        this.mark = obj.getString("mark");

    }

    public String getSysstatusstr() {
        return sysstatusstr;
    }

    public void setSysstatusstr(String sysstatusstr) {
        this.sysstatusstr = sysstatusstr;
    }
}
