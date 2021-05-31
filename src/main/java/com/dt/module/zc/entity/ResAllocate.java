package com.dt.module.zc.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-08-23
 */
 
@TableName("res_allocate")

public class ResAllocate extends BaseModel<ResAllocate> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("uuid")
    private String uuid;
    @TableField("allocateuserid")
    private String allocateuserid;
    @TableField("allocateusername")
    private String allocateusername;
    @TableField("frombelongcompid")
    private String frombelongcompid;
    @TableField("frombelongcompname")
    private String frombelongcompname;
    @TableField("touseduserid")
    private String touseduserid;
    @TableField("tousedusername")
    private String tousedusername;
    @TableField("tousedpartid")
    private String tousedpartid;
    @TableField("tousedpartname")
    private String tousedpartname;
    @TableField("tousedcompid")
    private String tousedcompid;
    @TableField("tousedcompname")
    private String tousedcompname;
    @TableField("tobelongcompid")
    private String tobelongcompid;
    @TableField("tobelongcompname")
    private String tobelongcompname;
    @TableField("tobelongpartid")
    private String tobelongpartid;
    @TableField("tobelongpartname")
    private String tobelongpartname;
    @TableField("toloc")
    private String toloc;
    @TableField("tolocname")
    private String tolocname;
    /**
     * 未完成 doing，已完成 finish，已取消 cancel
     */
    @TableField("status")
    private String status;
    @TableField("acttime")
    private Date acttime;
    @TableField("tolocdtl")
    private String tolocdtl;
    @TableField("mark")
    private String mark;
    @TableField("fcompid")
    private String fcompid;
    @TableField("fcompname")
    private String fcompname;
    @TableField("floc")
    private String floc;
    @TableField("flocname")
    private String flocname;
    @TableField("flocdtl")
    private String flocdtl;
    @TableField("busdate")
    private Date busdate;

    @TableField("name")
    private String name;

    @TableField("pinst")
    private String pinst;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinst() {
        return pinst;
    }

    public void setPinst(String pinst) {
        this.pinst = pinst;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAllocateuserid() {
        return allocateuserid;
    }

    public void setAllocateuserid(String allocateuserid) {
        this.allocateuserid = allocateuserid;
    }

    public String getAllocateusername() {
        return allocateusername;
    }

    public void setAllocateusername(String allocateusername) {
        this.allocateusername = allocateusername;
    }

    public String getFrombelongcompid() {
        return frombelongcompid;
    }

    public void setFrombelongcompid(String frombelongcompid) {
        this.frombelongcompid = frombelongcompid;
    }

    public String getFrombelongcompname() {
        return frombelongcompname;
    }

    public void setFrombelongcompname(String frombelongcompname) {
        this.frombelongcompname = frombelongcompname;
    }

    public String getTouseduserid() {
        return touseduserid;
    }

    public void setTouseduserid(String touseduserid) {
        this.touseduserid = touseduserid;
    }

    public String getTousedusername() {
        return tousedusername;
    }

    public void setTousedusername(String tousedusername) {
        this.tousedusername = tousedusername;
    }

    public String getTousedpartid() {
        return tousedpartid;
    }

    public void setTousedpartid(String tousedpartid) {
        this.tousedpartid = tousedpartid;
    }

    public String getTousedpartname() {
        return tousedpartname;
    }

    public void setTousedpartname(String tousedpartname) {
        this.tousedpartname = tousedpartname;
    }

    public String getTousedcompid() {
        return tousedcompid;
    }

    public void setTousedcompid(String tousedcompid) {
        this.tousedcompid = tousedcompid;
    }

    public String getTousedcompname() {
        return tousedcompname;
    }

    public void setTousedcompname(String tousedcompname) {
        this.tousedcompname = tousedcompname;
    }

    public String getTobelongcompid() {
        return tobelongcompid;
    }

    public void setTobelongcompid(String tobelongcompid) {
        this.tobelongcompid = tobelongcompid;
    }

    public String getTobelongcompname() {
        return tobelongcompname;
    }

    public void setTobelongcompname(String tobelongcompname) {
        this.tobelongcompname = tobelongcompname;
    }

    public String getTobelongpartid() {
        return tobelongpartid;
    }

    public void setTobelongpartid(String tobelongpartid) {
        this.tobelongpartid = tobelongpartid;
    }

    public String getTobelongpartname() {
        return tobelongpartname;
    }

    public void setTobelongpartname(String tobelongpartname) {
        this.tobelongpartname = tobelongpartname;
    }

    public String getToloc() {
        return toloc;
    }

    public void setToloc(String toloc) {
        this.toloc = toloc;
    }

    public String getTolocname() {
        return tolocname;
    }

    public void setTolocname(String tolocname) {
        this.tolocname = tolocname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getActtime() {
        return acttime;
    }

    public void setActtime(Date acttime) {
        this.acttime = acttime;
    }

    public String getTolocdtl() {
        return tolocdtl;
    }

    public void setTolocdtl(String tolocdtl) {
        this.tolocdtl = tolocdtl;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getFcompid() {
        return fcompid;
    }

    public void setFcompid(String fcompid) {
        this.fcompid = fcompid;
    }

    public String getFcompname() {
        return fcompname;
    }

    public void setFcompname(String fcompname) {
        this.fcompname = fcompname;
    }

    public String getFloc() {
        return floc;
    }

    public void setFloc(String floc) {
        this.floc = floc;
    }

    public String getFlocname() {
        return flocname;
    }

    public void setFlocname(String flocname) {
        this.flocname = flocname;
    }

    public String getFlocdtl() {
        return flocdtl;
    }

    public void setFlocdtl(String flocdtl) {
        this.flocdtl = flocdtl;
    }

    public Date getBusdate() {
        return busdate;
    }

    public void setBusdate(Date busdate) {
        this.busdate = busdate;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResAllocate{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", allocateuserid=" + allocateuserid +
                ", allocateusername=" + allocateusername +
                ", frombelongcompid=" + frombelongcompid +
                ", frombelongcompname=" + frombelongcompname +
                ", touseduserid=" + touseduserid +
                ", tousedusername=" + tousedusername +
                ", tousedpartid=" + tousedpartid +
                ", tousedpartname=" + tousedpartname +
                ", tousedcompid=" + tousedcompid +
                ", tousedcompname=" + tousedcompname +
                ", tobelongcompid=" + tobelongcompid +
                ", tobelongcompname=" + tobelongcompname +
                ", tobelongpartid=" + tobelongpartid +
                ", tobelongpartname=" + tobelongpartname +
                ", toloc=" + toloc +
                ", tolocname=" + tolocname +
                ", status=" + status +
                ", acttime=" + acttime +
                ", tolocdtl=" + tolocdtl +
                ", mark=" + mark +
                ", fcompid=" + fcompid +
                ", fcompname=" + fcompname +
                ", floc=" + floc +
                ", flocname=" + flocname +
                ", flocdtl=" + flocdtl +
                ", busdate=" + busdate +
                "}";
    }
}
