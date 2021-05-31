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
@ExcelTarget("OpsNodeEntity")
public class OpsNodeEntity {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    @Excel(name = "编号", width = 30)
    private String id;

    @Excel(name = "名称", width = 30)
    private String name;
    @Excel(name = "IP", width = 15)
    private String ip;
    @Excel(name = "业务类型", width = 15)
    private String systypestr;
    @Excel(name = "位置", width = 15)
    private String syslocstr;

    @Excel(name = "操作系统", width = 15)
    private String sysosstr;
    @Excel(name = "操作系统详情", width = 30)
    private String sysosdtlstr;
    @Excel(name = "数据库", width = 15)
    private String sysdbstr;
    @Excel(name = "数据库详情", width = 18)
    private String sysdbdtlstr;
    @Excel(name = "中间件", width = 40)
    private String middlewarestr;
    @Excel(name = "执行环境", width = 15)
    private String sysexecenvstr;
    @Excel(name = "监控部署", width = 15)
    private String sysmonitorstr;
    @Excel(name = "改密策略", width = 15)
    private String syspwdstrategystr;
    @Excel(name = "密码备注", width = 20)
    private String pwdmark;
    @Excel(name = "负责人", width = 15)
    private String leader;

    @Excel(name = "节点备份类型", width = 18)
    private String nodebackupstr;
    @Excel(name = "节点备份详情", width = 20)
    private String nodebackupdtl;


    @Excel(name = "状态", width = 15)
    private String statusstr;

    @Excel(name = "超级用户", width = 20)
    private String useradmin;

    @Excel(name = "数据库安装用户", width = 20)
    private String userdb;

    @Excel(name = "数据库使用用户", width = 20)
    private String userdbused;

    @Excel(name = "应用用户", width = 20)
    private String userapp;

    @Excel(name = "中间件用户", width = 20)
    private String usermid;

    @Excel(name = "运维用户", width = 15)
    private String userops;

    @Excel(name = "其他用户", width = 15)
    private String userother;

    @Excel(name = "Nologin用户", width = 15)

    private String usernologin;

    @Excel(name = "标签1", width = 15)
    private String label1;

    @Excel(name = "标签2", width = 15)
    private String label2;
    @Excel(name = "风险等级", width = 15)
    private String syslevelstr;
    @Excel(name = "运行环境", width = 15)
    private String sysenvstr;
    @Excel(name = "备注", width = 20)
    private String mark;

    /**
     * @return the status
     */
    public String getStatusstr() {
        return statusstr;
    }

    /**
     * @param status the status to set
     */
    public void setStatusstr(String status) {
        this.statusstr = status;
    }

    /**
     * @return the label1
     */
    public String getLabel1() {
        return label1 == null ? "" : label1;
    }

    /**
     * @param label1 the label1 to set
     */
    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    /**
     * @return the label2
     */
    public String getLabel2() {
        return label2 == null ? "" : label2;
    }

    /**
     * @param label2 the label2 to set
     */
    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name == null ? "" : name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the systypestr
     */
    public String getSystypestr() {
        return systypestr;
    }

    /**
     * @param systypestr the systypestr to set
     */
    public void setSystypestr(String systypestr) {
        this.systypestr = systypestr;
    }

    /**
     * @return the syslocstr
     */
    public String getSyslocstr() {
        return syslocstr;
    }

    /**
     * @param syslocstr the syslocstr to set
     */
    public void setSyslocstr(String syslocstr) {
        this.syslocstr = syslocstr;
    }

    /**
     * @return the sysosstr
     */
    public String getSysosstr() {
        return sysosstr;
    }

    /**
     * @param sysosstr the sysosstr to set
     */
    public void setSysosstr(String sysosstr) {
        this.sysosstr = sysosstr;
    }

    /**
     * @return the sysosdtlstr
     */
    public String getSysosdtlstr() {
        return sysosdtlstr;
    }

    /**
     * @param sysosdtlstr the sysosdtlstr to set
     */
    public void setSysosdtlstr(String sysosdtlstr) {
        this.sysosdtlstr = sysosdtlstr;
    }

    /**
     * @return the sysdbstr
     */
    public String getSysdbstr() {
        return sysdbstr;
    }

    /**
     * @param sysdbstr the sysdbstr to set
     */
    public void setSysdbstr(String sysdbstr) {
        this.sysdbstr = sysdbstr;
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
     * @return the middlewarestr
     */
    public String getMiddlewarestr() {
        return middlewarestr;
    }

    /**
     * @param middlewarestr the middlewarestr to set
     */
    public void setMiddlewarestr(String middlewarestr) {
        this.middlewarestr = middlewarestr;
    }

    /**
     * @return the sysexecenvstr
     */
    public String getSysexecenvstr() {
        return sysexecenvstr;
    }

    /**
     * @param sysexecenvstr the sysexecenvstr to set
     */
    public void setSysexecenvstr(String sysexecenvstr) {
        this.sysexecenvstr = sysexecenvstr;
    }

    /**
     * @return the sysmonitorstr
     */
    public String getSysmonitorstr() {
        return sysmonitorstr;
    }

    /**
     * @param sysmonitorstr the sysmonitorstr to set
     */
    public void setSysmonitorstr(String sysmonitorstr) {
        this.sysmonitorstr = sysmonitorstr;
    }

    /**
     * @return the syspwdstrategystr
     */
    public String getSyspwdstrategystr() {
        return syspwdstrategystr;
    }

    /**
     * @param syspwdstrategystr the syspwdstrategystr to set
     */
    public void setSyspwdstrategystr(String syspwdstrategystr) {
        this.syspwdstrategystr = syspwdstrategystr;
    }

    /**
     * @return the pwdmark
     */
    public String getPwdmark() {
        return pwdmark == null ? "" : pwdmark;
    }

    /**
     * @param pwdmark the pwdmark to set
     */
    public void setPwdmark(String pwdmark) {
        this.pwdmark = pwdmark;
    }

    /**
     * @return the leader
     */
    public String getLeader() {
        return leader == null ? "" : leader;
    }

    /**
     * @param leader the leader to set
     */
    public void setLeader(String leader) {
        this.leader = leader;
    }

    /**
     * @return the syslevelstr
     */
    public String getSyslevelstr() {
        return syslevelstr;
    }

    /**
     * @param syslevelstr the syslevelstr to set
     */
    public void setSyslevelstr(String syslevelstr) {
        this.syslevelstr = syslevelstr;
    }

    /**
     * @return the sysenvstr
     */
    public String getSysenvstr() {
        return sysenvstr;
    }

    /**
     * @param sysenvstr the sysenvstr to set
     */
    public void setSysenvstr(String sysenvstr) {
        this.sysenvstr = sysenvstr;
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

    /**
     * @return the useradmin
     */
    public String getUseradmin() {
        return useradmin == null ? "" : useradmin;
    }

    /**
     * @param useradmin the useradmin to set
     */
    public void setUseradmin(String useradmin) {
        this.useradmin = useradmin;
    }

    /**
     * @return the userdb
     */
    public String getUserdb() {
        return userdb == null ? "" : userdb;
    }

    /**
     * @param userdb the userdb to set
     */
    public void setUserdb(String userdb) {
        this.userdb = userdb;
    }

    /**
     * @return the userapp
     */
    public String getUserapp() {
        return userapp == null ? "" : userapp;
    }

    /**
     * @param userapp the userapp to set
     */
    public void setUserapp(String userapp) {
        this.userapp = userapp;
    }

    /**
     * @return the usermid
     */
    public String getUsermid() {
        return usermid == null ? "" : usermid;
    }

    /**
     * @param usermid the usermid to set
     */
    public void setUsermid(String usermid) {
        this.usermid = usermid;
    }

    /**
     * @return the userops
     */
    public String getUserops() {
        return userops == null ? "" : userops;
    }

    /**
     * @param userops the userops to set
     */
    public void setUserops(String userops) {
        this.userops = userops;
    }

    /**
     * @return the userother
     */
    public String getUserother() {
        return userother == null ? "" : userother;
    }

    /**
     * @param userother the userother to set
     */
    public void setUserother(String userother) {
        this.userother = userother;
    }


    /**
     * @return the nodebackupdtl
     */
    public String getNodebackupdtl() {
        return nodebackupdtl == null ? "" : nodebackupdtl;
    }

    /**
     * @param nodebackupdtl the nodebackupdtl to set
     */
    public void setNodebackupdtl(String nodebackupdtl) {
        this.nodebackupdtl = nodebackupdtl;


    }


    /**
     * @return the usernologin
     */
    public String getUsernologin() {
        return usernologin == null ? "" : usernologin;
    }

    /**
     * @param usernologin the usernologin to set
     */
    public void setUsernologin(String usernologin) {
        this.usernologin = usernologin;
    }

    /**
     * @return the userdbused
     */
    public String getUserdbused() {
        return userdbused == null ? "" : userdbused;

    }

    /**
     * @param userdbused the userdbused to set
     */
    public void setUserdbused(String userdbused) {
        this.userdbused = userdbused;
    }

    public void fullEntity(JSONObject obj) {
        this.nodebackupstr = obj.getString("nodebackupstr");
        this.name = obj.getString("name");
        this.id = obj.getString("id");
        this.ip = obj.getString("ip");
        this.systypestr = obj.getString("systypestr");
        this.syslocstr = obj.getString("syslocstr");
        this.sysosstr = obj.getString("sysosstr");
        this.sysosdtlstr = obj.getString("sysosdtlstr");
        this.sysdbstr = obj.getString("sysdbstr");
        this.sysdbdtlstr = obj.getString("sysdbdtlstr");
        this.middlewarestr = obj.getString("middlewarestr");
        this.sysexecenvstr = obj.getString("sysexecenvstr");
        this.sysmonitorstr = obj.getString("sysmonitorstr");
        this.syspwdstrategystr = obj.getString("syspwdstrategystr");
        this.pwdmark = obj.getString("pwdmark");
        this.leader = obj.getString("leader");
        this.syslevelstr = obj.getString("syslevelstr");
        this.sysenvstr = obj.getString("sysenvstr");
        this.mark = obj.getString("mark");
        this.label2 = obj.getString("label2");
        this.label1 = obj.getString("label1");
        this.statusstr = obj.getString("statusstr");
        this.nodebackupdtl = obj.getString("nodebackupdtl");

        this.useradmin = obj.getString("useradmin");
        this.userdb = obj.getString("userdb");
        this.userother = obj.getString("userother");
        this.usernologin = obj.getString("usernologin");
        this.userops = obj.getString("userops");
        this.usermid = obj.getString("usermid");
        this.userapp = obj.getString("userapp");
        this.userdbused = obj.getString("userdbused");

    }
}
