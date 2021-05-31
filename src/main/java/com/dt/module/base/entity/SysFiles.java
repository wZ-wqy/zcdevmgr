package com.dt.module.base.entity;

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
 * @since 2020-04-11
 */

@TableName("sys_files")

public class SysFiles extends BaseModel<SysFiles> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 文件路径
     */
    @TableField("path")
    private String path;
    /**
     * 文件类型
     */
    @TableField("type")
    private String type;
    @TableField("bus")
    private String bus;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;
    /**
     * 文件名
     */
    @TableField("filename")
    private String filename;
    /**
     * 原文件名
     */
    @TableField("filename_o")
    private String filenameO;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilenameO() {
        return filenameO;
    }

    public void setFilenameO(String filenameO) {
        this.filenameO = filenameO;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysFiles{" +
                "id=" + id +
                ", path=" + path +
                ", type=" + type +
                ", bus=" + bus +
                ", mark=" + mark +
                ", filename=" + filename +
                ", filenameO=" + filenameO +
                "}";
    }
}
