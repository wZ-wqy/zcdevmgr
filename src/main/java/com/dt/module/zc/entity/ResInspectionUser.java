package com.dt.module.zc.entity;

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
 * @since 2020-11-18
 */

@TableName("res_inspection_user")

public class ResInspectionUser extends BaseModel<ResInspectionUser> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busid")
    private String busid;
    @TableField("userid")
    private String userid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInspectionUser{" +
                "id=" + id +
                ", busid=" + busid +
                ", userid=" + userid +
                "}";
    }
}
