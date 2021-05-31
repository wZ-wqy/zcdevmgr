package com.dt.module.ct.entity;

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
 * @since 2018-07-30
 */
@TableName("CT_CONTENT")
public class CtContent extends BaseModel<CtContent> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("CAT_ID")
    private String catId;
    @TableField("DIGEST")
    private String digest;
    @TableField("TITLE")
    private String title;
    @TableField("PROFILE")
    private String profile;
    @TableField("URLTYPE")
    private String urltype;
    @TableField("URL")
    private String url;
    @TableField("TYPE")
    private String type;
    @TableField("MPIC")
    private String mpic;
    @TableField("MPIC_LOC")
    private String mpicLoc;
    @TableField("CONTENT")
    private String content;
    @TableField("HITS")
    private String hits;
    @TableField("AUTHOR")
    private String author;
    @TableField("SORT")
    private String sort;
    @TableField("DISPLAY")
    private String display;
    @TableField("TAG")
    private String tag;
    @TableField("MARK")
    private String mark;
    @TableField("USER_ID")
    private String userId;
    @TableField("COL_A")
    private String colA;
    @TableField("COL_B")
    private String colB;
    @TableField("COL_C")
    private String colC;
    @TableField("COL_D")
    private String colD;
    @TableField("COL_E")
    private String colE;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
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

    public String getUrltype() {
        return urltype;
    }

    public void setUrltype(String urltype) {
        this.urltype = urltype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMpic() {
        return mpic;
    }

    public void setMpic(String mpic) {
        this.mpic = mpic;
    }

    public String getMpicLoc() {
        return mpicLoc;
    }

    public void setMpicLoc(String mpicLoc) {
        this.mpicLoc = mpicLoc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getColA() {
        return colA;
    }

    public void setColA(String colA) {
        this.colA = colA;
    }

    public String getColB() {
        return colB;
    }

    public void setColB(String colB) {
        this.colB = colB;
    }

    public String getColC() {
        return colC;
    }

    public void setColC(String colC) {
        this.colC = colC;
    }

    public String getColD() {
        return colD;
    }

    public void setColD(String colD) {
        this.colD = colD;
    }

    public String getColE() {
        return colE;
    }

    public void setColE(String colE) {
        this.colE = colE;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CtContent{" +
                ", id=" + id +
                ", catId=" + catId +
                ", digest=" + digest +
                ", title=" + title +
                ", profile=" + profile +
                ", urltype=" + urltype +
                ", url=" + url +
                ", type=" + type +
                ", mpic=" + mpic +
                ", mpicLoc=" + mpicLoc +
                ", content=" + content +
                ", hits=" + hits +
                ", author=" + author +
                ", sort=" + sort +
                ", display=" + display +
                ", tag=" + tag +
                ", mark=" + mark +
                ", userId=" + userId +
                ", colA=" + colA +
                ", colB=" + colB +
                ", colC=" + colC +
                ", colD=" + colD +
                ", colE=" + colE +
                "}";
    }
}
