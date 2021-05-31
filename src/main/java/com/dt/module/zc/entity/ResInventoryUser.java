package com.dt.module.zc.entity;

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
 * @since 2020-05-15
 */

@TableName("res_inventory_user")

public class ResInventoryUser extends BaseModel<ResInventoryUser> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("pdid")
    private String pdid;
    /**
     * 盘点单分配人ID
     */
    @TableField("userid")
    private String userid;
    /**
     * 盘点单分配人
     */
    @TableField("username")
    private String username;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdid() {
        return pdid;
    }

    public void setPdid(String pdid) {
        this.pdid = pdid;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInventoryUser{" +
                "id=" + id +
                ", pdid=" + pdid +
                ", userid=" + userid +
                ", username=" + username +
                "}";
    }
}
