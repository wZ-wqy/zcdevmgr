package com.dt.module.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

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
 * @since 2020-09-06
 */

@TableName("contract")

public class Contract extends BaseModel<Contract> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("title")
    private String title;
    @TableField("about")
    private String about;
    @TableField("status")
    private String status;
    @TableField("type")
    private String type;
    @TableField("direction")
    private String direction;
    @TableField("files")
    private String files;
    @TableField("effectivedate")
    private Date effectivedate;
    @TableField("closingdate")
    private Date closingdate;
    @TableField("belongcomp")
    private String belongcomp;
    @TableField("belongcompname")
    private String belongcompname;
    @TableField("belongpart")
    private String belongpart;
    @TableField("belongpartname")
    private String belongpartname;
    @TableField("totalamount")
    private BigDecimal totalamount;
    @TableField("firstparty")
    private String firstparty;
    @TableField("firstpartyname")
    private String firstpartyname;
    @TableField("firstpartyuser")
    private String firstpartyuser;
    @TableField("firstpartycontact")
    private String firstpartycontact;
    @TableField("secondparty")
    private String secondparty;
    @TableField("secondpartyname")
    private String secondpartyname;
    @TableField("secondpartyuser")
    private String secondpartyuser;
    @TableField("secondpartycontact")
    private String secondpartycontact;
    @TableField("relatedparty")
    private String relatedparty;
    @TableField("relatedpartyname")
    private String relatedpartyname;
    @TableField("relatedpartyuser")
    private String relatedpartyuser;
    @TableField("relatedpartycontact")
    private String relatedpartycontact;
    @TableField("reviewer")
    private String reviewer;
    @TableField("mark")
    private String mark;
    @TableField("busuuid")
    private String busuuid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public Date getEffectivedate() {
        return effectivedate;
    }

    public void setEffectivedate(Date effectivedate) {
        this.effectivedate = effectivedate;
    }

    public Date getClosingdate() {
        return closingdate;
    }

    public void setClosingdate(Date closingdate) {
        this.closingdate = closingdate;
    }

    public String getBelongcomp() {
        return belongcomp;
    }

    public void setBelongcomp(String belongcomp) {
        this.belongcomp = belongcomp;
    }

    public String getBelongcompname() {
        return belongcompname;
    }

    public void setBelongcompname(String belongcompname) {
        this.belongcompname = belongcompname;
    }

    public String getBelongpart() {
        return belongpart;
    }

    public void setBelongpart(String belongpart) {
        this.belongpart = belongpart;
    }

    public String getBelongpartname() {
        return belongpartname;
    }

    public void setBelongpartname(String belongpartname) {
        this.belongpartname = belongpartname;
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(BigDecimal totalamount) {
        this.totalamount = totalamount;
    }

    public String getFirstparty() {
        return firstparty;
    }

    public void setFirstparty(String firstparty) {
        this.firstparty = firstparty;
    }

    public String getFirstpartyname() {
        return firstpartyname;
    }

    public void setFirstpartyname(String firstpartyname) {
        this.firstpartyname = firstpartyname;
    }

    public String getFirstpartyuser() {
        return firstpartyuser;
    }

    public void setFirstpartyuser(String firstpartyuser) {
        this.firstpartyuser = firstpartyuser;
    }

    public String getFirstpartycontact() {
        return firstpartycontact;
    }

    public void setFirstpartycontact(String firstpartycontact) {
        this.firstpartycontact = firstpartycontact;
    }

    public String getSecondparty() {
        return secondparty;
    }

    public void setSecondparty(String secondparty) {
        this.secondparty = secondparty;
    }

    public String getSecondpartyname() {
        return secondpartyname;
    }

    public void setSecondpartyname(String secondpartyname) {
        this.secondpartyname = secondpartyname;
    }

    public String getSecondpartyuser() {
        return secondpartyuser;
    }

    public void setSecondpartyuser(String secondpartyuser) {
        this.secondpartyuser = secondpartyuser;
    }

    public String getSecondpartycontact() {
        return secondpartycontact;
    }

    public void setSecondpartycontact(String secondpartycontact) {
        this.secondpartycontact = secondpartycontact;
    }

    public String getRelatedparty() {
        return relatedparty;
    }

    public void setRelatedparty(String relatedparty) {
        this.relatedparty = relatedparty;
    }

    public String getRelatedpartyname() {
        return relatedpartyname;
    }

    public void setRelatedpartyname(String relatedpartyname) {
        this.relatedpartyname = relatedpartyname;
    }

    public String getRelatedpartyuser() {
        return relatedpartyuser;
    }

    public void setRelatedpartyuser(String relatedpartyuser) {
        this.relatedpartyuser = relatedpartyuser;
    }

    public String getRelatedpartycontact() {
        return relatedpartycontact;
    }

    public void setRelatedpartycontact(String relatedpartycontact) {
        this.relatedpartycontact = relatedpartycontact;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBusuuid() {
        return busuuid;
    }

    public void setBusuuid(String busuuid) {
        this.busuuid = busuuid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", title=" + title +
                ", about=" + about +
                ", status=" + status +
                ", type=" + type +
                ", direction=" + direction +
                ", files=" + files +
                ", effectivedate=" + effectivedate +
                ", closingdate=" + closingdate +
                ", belongcomp=" + belongcomp +
                ", belongcompname=" + belongcompname +
                ", belongpart=" + belongpart +
                ", belongpartname=" + belongpartname +
                ", totalamount=" + totalamount +
                ", firstparty=" + firstparty +
                ", firstpartyname=" + firstpartyname +
                ", firstpartyuser=" + firstpartyuser +
                ", firstpartycontact=" + firstpartycontact +
                ", secondparty=" + secondparty +
                ", secondpartyname=" + secondpartyname +
                ", secondpartyuser=" + secondpartyuser +
                ", secondpartycontact=" + secondpartycontact +
                ", relatedparty=" + relatedparty +
                ", relatedpartyname=" + relatedpartyname +
                ", relatedpartyuser=" + relatedpartyuser +
                ", relatedpartycontact=" + relatedpartycontact +
                ", reviewer=" + reviewer +
                ", mark=" + mark +
                ", busuuid=" + busuuid +
                "}";
    }
}
