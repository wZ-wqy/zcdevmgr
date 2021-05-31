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
 * @since 2021-02-01
 */
 
@TableName("ops_rack")
 
public class OpsRack extends BaseModel<OpsRack> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    @TableField("layerid")
    private String layerid;
    @TableField("rackid")
    private String rackid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayerid() {
        return layerid;
    }

    public void setLayerid(String layerid) {
        this.layerid = layerid;
    }

    public String getRackid() {
        return rackid;
    }

    public void setRackid(String rackid) {
        this.rackid = rackid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OpsRack{" +
        "id=" + id +
        ", layerid=" + layerid +
        ", rackid=" + rackid +
        "}";
    }
}
