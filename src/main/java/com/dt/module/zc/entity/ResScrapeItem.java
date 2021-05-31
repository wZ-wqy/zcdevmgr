package com.dt.module.zc.entity;

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
 * @since 2020-05-28
 */

@TableName("res_scrape_item")

public class ResScrapeItem extends BaseModel<ResScrapeItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("uuid")
    private String uuid;
    @TableField("resid")
    private String resid;
    @TableField("prestatus")
    private String prestatus;
    @TableField("mark")
    private String mark;


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

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getPrestatus() {
        return prestatus;
    }

    public void setPrestatus(String prestatus) {
        this.prestatus = prestatus;
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
        return "ResScrapeItem{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", resid=" + resid +
                ", prestatus=" + prestatus +
                ", mark=" + mark +
                "}";
    }
}
