package com.dt.module.base.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author lank
 * @since 2020-12-28
 */
 
@TableName("sys_approval_meta")
 
public class SysApprovalMeta extends BaseModel<SysApprovalMeta> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 单据ID
     */
    @TableField("busid")
    private String busid;
    /**
     * 用户
     */
    @TableField("userid")
    private String userid;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 提供者
     */
    @TableField("taskassigneeprovider")
    private String taskassigneeprovider;
    /**
     * ID
     */
    @TableField("taskassigneeid")
    private String taskassigneeid;
    /**
     * 名称
     */
    @TableField("taskassigneename")
    private String taskassigneename;
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 组织节点
     */
    @TableField("nodeid")
    private String nodeid;
    /**
     * 审批节点ID
     */
    @TableField("approvalid")
    private String approvalid;
    /**
     * 审批角色
     */
    @TableField("approvalcode")
    private String approvalcode;
    /**
     * 备注
     */
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

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaskassigneeprovider() {
        return taskassigneeprovider;
    }

    public void setTaskassigneeprovider(String taskassigneeprovider) {
        this.taskassigneeprovider = taskassigneeprovider;
    }

    public String getTaskassigneeid() {
        return taskassigneeid;
    }

    public void setTaskassigneeid(String taskassigneeid) {
        this.taskassigneeid = taskassigneeid;
    }

    public String getTaskassigneename() {
        return taskassigneename;
    }

    public void setTaskassigneename(String taskassigneename) {
        this.taskassigneename = taskassigneename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getApprovalid() {
        return approvalid;
    }

    public void setApprovalid(String approvalid) {
        this.approvalid = approvalid;
    }

    public String getApprovalcode() {
        return approvalcode;
    }

    public void setApprovalcode(String approvalcode) {
        this.approvalcode = approvalcode;
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
        return "SysApprovalMeta{" +
        "id=" + id +
        ", name=" + name +
        ", busid=" + busid +
        ", userid=" + userid +
        ", username=" + username +
        ", taskassigneeprovider=" + taskassigneeprovider +
        ", taskassigneeid=" + taskassigneeid +
        ", taskassigneename=" + taskassigneename +
        ", type=" + type +
        ", nodeid=" + nodeid +
        ", approvalid=" + approvalid +
        ", approvalcode=" + approvalcode +
        ", mark=" + mark +
        "}";
    }
}
