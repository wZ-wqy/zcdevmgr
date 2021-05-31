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

@TableName("hrm_org_part")

public class HrmOrgPart extends BaseModel<HrmOrgPart> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织节点ID
     */
    @TableId("node_id")
    private BigDecimal nodeId;
    /**
     * 组织名称
     */
    @TableField("node_name")
    private String nodeName;
    /**
     * 组织所属ID
     */
    @TableField("org_id")
    private BigDecimal orgId;
    /**
     * 父节点
     */
    @TableField("parent_id")
    private BigDecimal parentId;
    /**
     * 路径编码
     */
    @TableField("route")
    private String route;
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 全路径名称
     */
    @TableField("route_name")
    private String routeName;


    public BigDecimal getNodeId() {
        return nodeId;
    }

    public void setNodeId(BigDecimal nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public BigDecimal getOrgId() {
        return orgId;
    }

    public void setOrgId(BigDecimal orgId) {
        this.orgId = orgId;
    }

    public BigDecimal getParentId() {
        return parentId;
    }

    public void setParentId(BigDecimal parentId) {
        this.parentId = parentId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    @Override
    protected Serializable pkVal() {
        return this.nodeId;
    }

    @Override
    public String toString() {
        return "HrmOrgPart{" +
                "nodeId=" + nodeId +
                ", nodeName=" + nodeName +
                ", orgId=" + orgId +
                ", parentId=" + parentId +
                ", route=" + route +
                ", type=" + type +
                ", routeName=" + routeName +
                "}";
    }
}
