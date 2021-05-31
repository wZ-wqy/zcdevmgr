package com.dt.module.base.entity;

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
 * @since 2018-07-24
 */
@TableName("SYS_ROLE_INFO")
public class SysRoleInfo extends BaseModel<SysRoleInfo> {

    private static final long serialVersionUID = 1L;

    @TableId("ROLE_ID")
    private String roleId;
    @TableField("ROLE_NAME")
    private String roleName;
    @TableField("ORG_ID")
    private String orgId;

    @TableField("IS_ACTION")
    private String isAction;
    @TableField("REMARK")
    private String remark;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    public String getIsAction() {
        return isAction;
    }

    public void setIsAction(String isAction) {
        this.isAction = isAction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

    @Override
    public String toString() {
        return "SysRoleInfo{" +
                ", roleId=" + roleId +
                ", roleName=" + roleName +
                ", orgId=" + orgId +
                ", isAction=" + isAction +
                ", remark=" + remark +
                "}";
    }
}
