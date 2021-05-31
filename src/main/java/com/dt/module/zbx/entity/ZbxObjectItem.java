package com.dt.module.zbx.entity;

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
 * @since 2020-08-02
 */

@TableName("zbx_object_item")

public class ZbxObjectItem extends BaseModel<ZbxObjectItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("groupid")
    private String groupid;
    @TableField("objid")
    private String objid;
    @TableField("objname")
    private String objname;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getObjname() {
        return objname;
    }

    public void setObjname(String objname) {
        this.objname = objname;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ZbxObjectItem{" +
                "id=" + id +
                ", groupid=" + groupid +
                ", objid=" + objid +
                ", objname=" + objname +
                "}";
    }
}
