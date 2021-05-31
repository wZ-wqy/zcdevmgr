package com.dt.module.ops.entity;

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
 * @since 2020-01-24
 */

@TableName("ops_node_dbdtl")

public class OpsNodeDbdtl extends BaseModel<OpsNodeDbdtl> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("nid")
    private String nid;
    @TableField("instance")
    private String instance;
    @TableField("bkstrategy")
    private String bkstrategy;
    @TableField("bkarchtype")
    private String bkarchtype;
    @TableField("bktype")
    private String bktype;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getBkstrategy() {
        return bkstrategy;
    }

    public void setBkstrategy(String bkstrategy) {
        this.bkstrategy = bkstrategy;
    }

    public String getBkarchtype() {
        return bkarchtype;
    }

    public void setBkarchtype(String bkarchtype) {
        this.bkarchtype = bkarchtype;
    }

    public String getBktype() {
        return bktype;
    }

    public void setBktype(String bktype) {
        this.bktype = bktype;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OpsNodeDbdtl{" +
                "id=" + id +
                ", nid=" + nid +
                ", instance=" + instance +
                ", bkstrategy=" + bkstrategy +
                ", bkarchtype=" + bkarchtype +
                ", bktype=" + bktype +
                ", mark=" + mark +
                "}";
    }
}
