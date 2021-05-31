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
 * @since 2020-12-11
 */
 
@TableName("sys_user_approval")
 
public class SysUserApproval extends BaseModel<SysUserApproval> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    @TableField("userid")
    private String userid;
    @TableField("nodeid")
    private String nodeid;
    @TableField("approvalid")
    private String approvalid;
    @TableField("approvalcode")
    private String approvalcode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUserApproval{" +
        "id=" + id +
        ", userid=" + userid +
        ", nodeid=" + nodeid +
        ", approvalid=" + approvalid +
        ", approvalcode=" + approvalcode +
        "}";
    }
}
