package com.dt.module.ct.entity;

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
 * @since 2020-03-28
 */

@TableName("ct_category")

public class CtCategory extends BaseModel<CtCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private BigDecimal id;
    @TableField("root")
    private BigDecimal root;
    @TableField("name")
    private String name;
    @TableField("mpic")
    private String mpic;
    @TableField("parent_id")
    private BigDecimal parentId;
    @TableField("route")
    private String route;
    @TableField("mark")
    private String mark;
    @TableField("node_level")
    private BigDecimal nodeLevel;
    @TableField("od")
    private BigDecimal od;
    @TableField("isaction")
    private String isaction;
    @TableField("route_name")
    private String routeName;
    @TableField("code")
    private String code;
    @TableField("type")
    private String type;
    @TableField("action")
    private String action;


    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getRoot() {
        return root;
    }

    public void setRoot(BigDecimal root) {
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMpic() {
        return mpic;
    }

    public void setMpic(String mpic) {
        this.mpic = mpic;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public BigDecimal getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(BigDecimal nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public BigDecimal getOd() {
        return od;
    }

    public void setOd(BigDecimal od) {
        this.od = od;
    }

    public String getIsaction() {
        return isaction;
    }

    public void setIsaction(String isaction) {
        this.isaction = isaction;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CtCategory{" +
                "id=" + id +
                ", root=" + root +
                ", name=" + name +
                ", mpic=" + mpic +
                ", parentId=" + parentId +
                ", route=" + route +
                ", mark=" + mark +
                ", nodeLevel=" + nodeLevel +
                ", od=" + od +
                ", isaction=" + isaction +
                ", routeName=" + routeName +
                ", code=" + code +
                ", type=" + type +
                ", action=" + action +
                "}";
    }
}
