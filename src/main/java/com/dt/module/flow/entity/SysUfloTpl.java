package com.dt.module.flow.entity;

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
 * @since 2019-11-30
 */

@TableName("sys_uflo_tpl")

public class SysUfloTpl extends BaseModel<SysUfloTpl> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("filename")
    private String filename;
    @TableField("content")
    private String content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUfloTpl{" +
                "id=" + id +
                ", filename=" + filename +
                ", content=" + content +
                "}";
    }
}
