package com.dt.module.base.entity;

public class ScheduleJob {
    private String JobSeq = "";
    private String JobName = "";
    private String JobGroup = "";
    private String JobDesc = "";
    private String Node = "";
    private String NodeName = "";
    private String JobType = "";
    private String jobEnable = "";
    // 当前job全部设置成有效
    private boolean JobInstanceValid = true;
    private String CronTrigger = "";
    private String CronExpression = "";
    private String JobClassName = "";
    private String Jobtrigger = "";
    private String JobPlanStatus = "";
    // tab,scheplan
    private String JobSource = "";
    private String lastRun = "";

    public boolean isJobInstanceValid() {
        return JobInstanceValid;
    }

    public void setJobInstanceValid(boolean jobInstValid) {
        JobInstanceValid = jobInstValid;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getJobSource() {
        return JobSource;
    }

    public void setJobSource(String jobSource) {
        JobSource = jobSource;
    }

    public String getJobSeq() {
        return JobSeq;
    }

    public void setJobSeq(String jobSeq) {
        JobSeq = jobSeq;
    }

    public String getJobTrueName() {
        return JobName;
    }

    public String getJobRunName() {
        return JobName + "#idle#" + JobSeq;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getJobGroup() {
        return JobGroup;
    }

    public void setJobGroup(String jobGroup) {
        JobGroup = jobGroup;
    }

    public String getJobDesc() {
        return JobDesc;
    }

    public void setJobDesc(String jobDesc) {
        JobDesc = jobDesc;
    }

    public String getNode() {
        return Node;
    }

    public void setNode(String node) {
        Node = node;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobtype) {
        JobType = jobtype;
    }

    public String getJobEnable() {
        return jobEnable;
    }

    public void setJobEnable(String jobenable) {
        this.jobEnable = jobenable;
    }

    public String getCronTrigger() {
        return CronTrigger;
    }

    public void setCronTrigger(String cronTrigger) {
        CronTrigger = cronTrigger;
    }

    public String getCronExpression() {
        return CronExpression;
    }

    public void setCronExpression(String cronExpression) {
        CronExpression = cronExpression;
    }

    public String getJobClassName() {
        return JobClassName;
    }

    public void setJobClassName(String jobClassName) {
        JobClassName = jobClassName;
    }

    public String getJobTrigger() {
        return Jobtrigger;
    }

    public void setJobTrigger(String jobtrigger) {
        Jobtrigger = jobtrigger;
    }

    public String getJobPlanStatus() {
        return JobPlanStatus;
    }

    public void setJobPlanStatus(String jobPlanStatus) {
        JobPlanStatus = jobPlanStatus;
    }

    /**
     * @return the last_run
     */
    public String getLastRun() {
        return lastRun;
    }

    public void setLastRun(String lastRun) {
        this.lastRun = lastRun;
    }
}
