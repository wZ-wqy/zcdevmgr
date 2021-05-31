package com.dt.module.hrm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-04-13
 */

@TableName("hrm_org_info")

public class HrmOrgInfo extends BaseModel<HrmOrgInfo> {

    private static final long serialVersionUID = 1L;

    @TableId("org_id")
    private BigDecimal orgId;
    /**
     * 组织名称
     */
    @TableField("org_name")
    private String orgName;
    /**
     * 是否有效
     */
    @TableField("is_action")
    private String isAction;


    public BigDecimal getOrgId() {
        return orgId;
    }

    public void setOrgId(BigDecimal orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIsAction() {
        return isAction;
    }

    public void setIsAction(String isAction) {
        this.isAction = isAction;
    }

    @Override
    protected Serializable pkVal() {
        return this.orgId;
    }

    @Override
    public String toString() {
        return "HrmOrgInfo{" +
                "orgId=" + orgId +
                ", orgName=" + orgName +
                ", isAction=" + isAction +
                "}";
    }
}
