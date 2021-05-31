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
 * 盘点资产
 * </p>
 *
 * @author lank
 * @since 2021-01-08
 */
 
@TableName("res_inventory_item")
 
public class ResInventoryItem extends BaseModel<ResInventoryItem> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    @TableField("pdbatchid")
    private String pdbatchid;
    @TableField("pdid")
    private String pdid;
    /**
     * 盘点状态,wait|finish
     */
    @TableField("pdstatus")
    private String pdstatus;
    /**
     * 是否需要同步数据,1|0
     */
    @TableField("pdsyncneed")
    private String pdsyncneed;
    @TableField("pdtime")
    private Date pdtime;
    @TableField("pduserid")
    private String pduserid;
    @TableField("pdusername")
    private String pdusername;
    @TableField("pdmark")
    private String pdmark;
    /**
     * 盘点标记,source|new|delete
     */
    @TableField("pdflag")
    private String pdflag;
    @TableField("resid")
    private String resid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdbatchid() {
        return pdbatchid;
    }

    public void setPdbatchid(String pdbatchid) {
        this.pdbatchid = pdbatchid;
    }

    public String getPdid() {
        return pdid;
    }

    public void setPdid(String pdid) {
        this.pdid = pdid;
    }

    public String getPdstatus() {
        return pdstatus;
    }

    public void setPdstatus(String pdstatus) {
        this.pdstatus = pdstatus;
    }

    public String getPdsyncneed() {
        return pdsyncneed;
    }

    public void setPdsyncneed(String pdsyncneed) {
        this.pdsyncneed = pdsyncneed;
    }

    public Date getPdtime() {
        return pdtime;
    }

    public void setPdtime(Date pdtime) {
        this.pdtime = pdtime;
    }

    public String getPduserid() {
        return pduserid;
    }

    public void setPduserid(String pduserid) {
        this.pduserid = pduserid;
    }

    public String getPdusername() {
        return pdusername;
    }

    public void setPdusername(String pdusername) {
        this.pdusername = pdusername;
    }

    public String getPdmark() {
        return pdmark;
    }

    public void setPdmark(String pdmark) {
        this.pdmark = pdmark;
    }

    public String getPdflag() {
        return pdflag;
    }

    public void setPdflag(String pdflag) {
        this.pdflag = pdflag;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInventoryItem{" +
        "id=" + id +
        ", pdbatchid=" + pdbatchid +
        ", pdid=" + pdid +
        ", pdstatus=" + pdstatus +
        ", pdsyncneed=" + pdsyncneed +
        ", pdtime=" + pdtime +
        ", pduserid=" + pduserid +
        ", pdusername=" + pdusername +
        ", pdmark=" + pdmark +
        ", pdflag=" + pdflag +
        ", resid=" + resid +
        "}";
    }
}
