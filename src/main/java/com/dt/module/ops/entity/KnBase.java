package com.dt.module.ops.entity;

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
 * 知识库
 * </p>
 *
 * @author lank
 * @since 2021-02-14
 */
 
@TableName("kn_base")
 
public class KnBase extends BaseModel<KnBase> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 分类
     */
    @TableField("catid")
    private String catid;
    /**
     * 分类
     */
    @TableField("catname")
    private String catname;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 简介
     */
    @TableField("profile")
    private String profile;
    /**
     * 标签
     */
    @TableField("label")
    private String label;
    /**
     * 内容
     */
    @TableField("ct")
    private String ct;
    @TableField("reviewcnt")
    private BigDecimal reviewcnt;
    /**
     * 附件
     */
    @TableField("attach")
    private String attach;
    @TableField("lasttime")
    private Date lasttime;
    @TableField("inserttime")
    private Date inserttime;
    /**
     * 最后编辑人ID
     */
    @TableField("luserid")
    private String luserid;
    /**
     * 最后编辑人员
     */
    @TableField("lusername")
    private String lusername;
    /**
     * 分享URL
     */
    @TableField("shareurl")
    private String shareurl;
    @TableField("isshow")
    private String isshow;
    @TableField("type")
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public BigDecimal getReviewcnt() {
        return reviewcnt;
    }

    public void setReviewcnt(BigDecimal reviewcnt) {
        this.reviewcnt = reviewcnt;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public String getLuserid() {
        return luserid;
    }

    public void setLuserid(String luserid) {
        this.luserid = luserid;
    }

    public String getLusername() {
        return lusername;
    }

    public void setLusername(String lusername) {
        this.lusername = lusername;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "KnBase{" +
        "id=" + id +
        ", catid=" + catid +
        ", catname=" + catname +
        ", title=" + title +
        ", profile=" + profile +
        ", label=" + label +
        ", ct=" + ct +
        ", reviewcnt=" + reviewcnt +
        ", attach=" + attach +
        ", lasttime=" + lasttime +
        ", inserttime=" + inserttime +
        ", luserid=" + luserid +
        ", lusername=" + lusername +
        ", shareurl=" + shareurl +
        ", isshow=" + isshow +
        ", type=" + type +
        "}";
    }
}
