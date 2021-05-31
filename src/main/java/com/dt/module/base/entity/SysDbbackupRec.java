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
 * @since 2020-06-25
 */

@TableName("sys_dbbackup_rec")

public class SysDbbackupRec extends BaseModel<SysDbbackupRec> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("dbname")
    private String dbname;
    @TableField("result")
    private String result;
    @TableField("duration")
    private String duration;
    @TableField("fileid")
    private String fileid;
    @TableField("filepath")
    private String filepath;
    @TableField("filesize")
    private String filesize;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
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
        return "SysDbbackupRec{" +
                "id=" + id +
                ", dbname=" + dbname +
                ", result=" + result +
                ", duration=" + duration +
                ", fileid=" + fileid +
                ", filepath=" + filepath +
                ", filesize=" + filesize +
                ", mark=" + mark +
                "}";
    }
}
