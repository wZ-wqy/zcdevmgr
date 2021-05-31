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
 * @since 2020-02-01
 */

@TableName("ops_node_item")

public class OpsNodeItem extends BaseModel<OpsNodeItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("nid")
    private String nid;
    @TableField("item")
    private String item;
    @TableField("value")
    private String value;
    @TableField("type")
    private String type;
    @TableField("dbinstance")
    private String dbinstance;
    @TableField("archtype")
    private String archtype;
    @TableField("bkstrategy")
    private String bkstrategy;
    @TableField("bktype")
    private String bktype;
    @TableField("bkkeep")
    private String bkkeep;
    @TableField("bkstatus")
    private String bkstatus;
    @TableField("mark")
    private String mark;
    @TableField("bkmethod")
    private String bkmethod;
    @TableField("dsize")
    private String dsize;


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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbinstance() {
        return dbinstance;
    }

    public void setDbinstance(String dbinstance) {
        this.dbinstance = dbinstance;
    }

    public String getArchtype() {
        return archtype;
    }

    public void setArchtype(String archtype) {
        this.archtype = archtype;
    }

    public String getBkstrategy() {
        return bkstrategy;
    }

    public void setBkstrategy(String bkstrategy) {
        this.bkstrategy = bkstrategy;
    }

    public String getBktype() {
        return bktype;
    }

    public void setBktype(String bktype) {
        this.bktype = bktype;
    }

    public String getBkkeep() {
        return bkkeep;
    }

    public void setBkkeep(String bkkeep) {
        this.bkkeep = bkkeep;
    }

    public String getBkstatus() {
        return bkstatus;
    }

    public void setBkstatus(String bkstatus) {
        this.bkstatus = bkstatus;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBkmethod() {
        return bkmethod;
    }

    public void setBkmethod(String bkmethod) {
        this.bkmethod = bkmethod;
    }

    public String getDsize() {
        return dsize;
    }

    public void setDsize(String dsize) {
        this.dsize = dsize;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OpsNodeItem{" +
                "id=" + id +
                ", nid=" + nid +
                ", item=" + item +
                ", value=" + value +
                ", type=" + type +
                ", dbinstance=" + dbinstance +
                ", archtype=" + archtype +
                ", bkstrategy=" + bkstrategy +
                ", bktype=" + bktype +
                ", bkkeep=" + bkkeep +
                ", bkstatus=" + bkstatus +
                ", mark=" + mark +
                ", bkmethod=" + bkmethod +
                ", dsize=" + dsize +
                "}";
    }
}
