package com.dt.module.flow.entity;


import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.model.task.TaskType;

import java.util.Date;

/**
 * @author Jacky.gao
 * @since 2013年10月14日
 */
public class TaskInfo {
    private long taskId;
    private String taskName;
    private String assignee;
    private String assigneename;
    private String owner;
    private String ownername;
    private TaskState state;
    private long processInstanceId;
    private Date createDate;
    private String opinion;
    private String url;
    private TaskType type;
    private Date duedate;
    private String businessId;
    private Date endDate;
    private String description;
    private long processId;

    /**
     * @return the assigneename
     */
    public String getAssigneename() {
        return assigneename;
    }

    /**
     * @param assigneename the assigneename to set
     */
    public void setAssigneename(String assigneename) {
        this.assigneename = assigneename;
    }

    /**
     * @return the ownername
     */
    public String getOwnername() {
        return ownername;
    }

    /**
     * @param ownername the ownername to set
     */
    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getProcessId() {
        return processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }
}
