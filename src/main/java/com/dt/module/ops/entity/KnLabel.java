package com.dt.module.ops.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 知识库标签
 * </p>
 *
 * @author lank
 * @since 2021-02-10
 */
 
@TableName("kn_label")
 
public class KnLabel extends BaseModel<KnLabel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 用户
     */
    @TableField("userid")
    private String userid;
    /**
     * 分类
     */
    @TableField("catid")
    private String catid;
    /**
     * 标签名称
     */
    @TableField("name")
    private String name;
    /**
     * 标签备注
     */
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "KnLabel{" +
        "id=" + id +
        ", userid=" + userid +
        ", catid=" + catid +
        ", name=" + name +
        ", mark=" + mark +
        "}";
    }
}
