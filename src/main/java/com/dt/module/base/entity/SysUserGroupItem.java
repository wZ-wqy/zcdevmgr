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
@TableName("SYS_USER_GROUP_ITEM")
public class SysUserGroupItem extends BaseModel<SysUserGroupItem> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("GROUP_ID")
    private String groupId;
    @TableField("USER_ID")
    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUserGroupItem{" +
                ", id=" + id +
                ", groupId=" + groupId +
                ", userId=" + userId +
                "}";
    }
}
