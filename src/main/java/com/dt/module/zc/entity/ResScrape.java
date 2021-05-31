package com.dt.module.zc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-05-28
 */

@TableName("res_scrape")

public class ResScrape extends BaseModel<ResScrape> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("uuid")
    private String uuid;
    @TableField("title")
    private String title;
    @TableField("processuserid")
    private String processuserid;
    @TableField("processusername")
    private String processusername;
    @TableField("processdate")
    private Date processdate;
    @TableField("status")
    private String status;
    @TableField("ct")
    private String ct;
    @TableField("busidate")
    private Date busidate;
    @TableField("mark")
    private String mark;
    @TableField("cnt")
    private BigDecimal cnt;

    @TableField("pinst")
    private String pinst;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessuserid() {
        return processuserid;
    }

    public void setProcessuserid(String processuserid) {
        this.processuserid = processuserid;
    }

    public String getProcessusername() {
        return processusername;
    }

    public void setProcessusername(String processusername) {
        this.processusername = processusername;
    }

    public Date getProcessdate() {
        return processdate;
    }

    public void setProcessdate(Date processdate) {
        this.processdate = processdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public Date getBusidate() {
        return busidate;
    }

    public void setBusidate(Date busidate) {
        this.busidate = busidate;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public BigDecimal getCnt() {
        return cnt;
    }

    public void setCnt(BigDecimal cnt) {
        this.cnt = cnt;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResScrape{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", title=" + title +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", processdate=" + processdate +
                ", status=" + status +
                ", ct=" + ct +
                ", busidate=" + busidate +
                ", mark=" + mark +
                ", cnt=" + cnt +
                "}";
    }
}
