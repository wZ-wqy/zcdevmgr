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
 * @since 2018-07-27
 */
@TableName("SYS_MENUS")
public class SysMenus extends BaseModel<SysMenus> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单
     */
    @TableId("MENU_ID")
    private String menuId;
    /**
     * 菜单名称
     */
    @TableField("NAME")
    private String name;
    /**
     * 备注
     */
    @TableField("MARK")
    private String mark;
    /**
     * 排序
     */
    @TableField("SORT")
    private String sort;
    /**
     * 多模式
     */
    @TableField("ORG_ID")
    private String orgId;
    /**
     * PC |
     */
    @TableField("TYPE")
    private String type;
    @TableField("USED")
    private String used;


    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }

    @Override
    public String toString() {
        return "SysMenus{" +
                ", menuId=" + menuId +
                ", name=" + name +
                ", mark=" + mark +
                ", sort=" + sort +
                ", orgId=" + orgId +
                ", type=" + type +
                ", used=" + used +
                "}";
    }
}
