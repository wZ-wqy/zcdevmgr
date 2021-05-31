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
 * @since 2020-08-05
 */

@TableName("res_residual")

public class ResResidual extends BaseModel<ResResidual> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("uuid")
    private String uuid;
    @TableField("title")
    private String title;
    @TableField("residualvaluerate")
    private BigDecimal residualvaluerate;
    @TableField("depreciationrate")
    private BigDecimal depreciationrate;
    @TableField("valuestr")
    private String valuestr;
    @TableField("status")
    private String status;
    @TableField("checkstatus")
    private String checkstatus;
    @TableField("strategyid")
    private String strategyid;
    @TableField("processuserid")
    private String processuserid;
    @TableField("processusername")
    private String processusername;
    @TableField("processdate")
    private Date processdate;
    @TableField("mark")
    private String mark;
    @TableField("cnt")
    private BigDecimal cnt;
    @TableField("busidate")
    private Date busidate;


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

    public BigDecimal getResidualvaluerate() {
        return residualvaluerate;
    }

    public void setResidualvaluerate(BigDecimal residualvaluerate) {
        this.residualvaluerate = residualvaluerate;
    }

    public BigDecimal getDepreciationrate() {
        return depreciationrate;
    }

    public void setDepreciationrate(BigDecimal depreciationrate) {
        this.depreciationrate = depreciationrate;
    }

    public String getValuestr() {
        return valuestr;
    }

    public void setValuestr(String valuestr) {
        this.valuestr = valuestr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getStrategyid() {
        return strategyid;
    }

    public void setStrategyid(String strategyid) {
        this.strategyid = strategyid;
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

    public Date getBusidate() {
        return busidate;
    }

    public void setBusidate(Date busidate) {
        this.busidate = busidate;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResResidual{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", title=" + title +
                ", residualvaluerate=" + residualvaluerate +
                ", depreciationrate=" + depreciationrate +
                ", valuestr=" + valuestr +
                ", status=" + status +
                ", checkstatus=" + checkstatus +
                ", strategyid=" + strategyid +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", processdate=" + processdate +
                ", mark=" + mark +
                ", cnt=" + cnt +
                ", busidate=" + busidate +
                "}";
    }
}
