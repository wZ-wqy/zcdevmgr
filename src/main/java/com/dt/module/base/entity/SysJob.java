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
@TableName("SYS_JOB")
public class SysJob extends BaseModel<SysJob> {

    private static final long serialVersionUID = 1L;

    @TableId("SEQ")
    private String seq;
    @TableField("NODE")
    private String node;
    @TableField("JOBNAME")
    private String jobname;
    @TableField("JOBGROUP")
    private String jobgroup;
    @TableField("JOBCLASSNAME")
    private String jobclassname;
    @TableField("JOBCRON")
    private String jobcron;
    @TableField("JOBTYPE")
    private String jobtype;
    @TableField("JOBENABLE")
    private String jobenable;
    @TableField("MARK")
    private String mark;
    @TableField("RECDATE")
    private Date recdate;
    @TableField("LAST_RUN")
    private Date lastRun;
    @TableField("INITED")
    private String inited;


    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getJobgroup() {
        return jobgroup;
    }

    public void setJobgroup(String jobgroup) {
        this.jobgroup = jobgroup;
    }

    public String getJobclassname() {
        return jobclassname;
    }

    public void setJobclassname(String jobclassname) {
        this.jobclassname = jobclassname;
    }

    public String getJobcron() {
        return jobcron;
    }

    public void setJobcron(String jobcron) {
        this.jobcron = jobcron;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getJobenable() {
        return jobenable;
    }

    public void setJobenable(String jobenable) {
        this.jobenable = jobenable;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getRecdate() {
        return recdate;
    }

    public void setRecdate(Date recdate) {
        this.recdate = recdate;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getInited() {
        return inited;
    }

    public void setInited(String inited) {
        this.inited = inited;
    }

    @Override
    protected Serializable pkVal() {
        return this.seq;
    }

    @Override
    public String toString() {
        return "SysJob{" +
                ", seq=" + seq +
                ", node=" + node +
                ", jobname=" + jobname +
                ", jobgroup=" + jobgroup +
                ", jobclassname=" + jobclassname +
                ", jobcron=" + jobcron +
                ", jobtype=" + jobtype +
                ", jobenable=" + jobenable +
                ", mark=" + mark +
                ", recdate=" + recdate +
                ", lastRun=" + lastRun +
                ", inited=" + inited +
                "}";
    }
}
