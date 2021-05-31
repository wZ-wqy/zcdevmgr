package com.dt.module.ops.entity;

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
 * @since 2021-02-11
 */
 
@TableName("kn_base_user")
 
public class KnBaseUser extends BaseModel<KnBaseUser> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("knbaseid")
    private String knbaseid;
    @TableField("userid")
    private String userid;
    @TableField("username")
    private String username;
    @TableField("ct")
    private String ct;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKnbaseid() {
        return knbaseid;
    }

    public void setKnbaseid(String knbaseid) {
        this.knbaseid = knbaseid;
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

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "KnBaseUser{" +
        "id=" + id +
        ", knbaseid=" + knbaseid +
        ", userid=" + userid +
        ", username=" + username +
        ", ct=" + ct +
        "}";
    }
}
