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

@TableName("sys_file_conf")

public class SysFileConf extends BaseModel<SysFileConf> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 系统路径
     */
    @TableField("path")
    private String path;
    /**
     * 是否使用中
     */
    @TableField("is_used")
    private String isUsed;
    @TableField("limit_str")
    private String limitStr;
    @TableField("type")
    private String type;
    /**
     * 保留文件名称
     */
    @TableField("keepname")
    private String keepname;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getLimitStr() {
        return limitStr;
    }

    public void setLimitStr(String limitStr) {
        this.limitStr = limitStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeepname() {
        return keepname;
    }

    public void setKeepname(String keepname) {
        this.keepname = keepname;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysFileConf{" +
                "id=" + id +
                ", name=" + name +
                ", path=" + path +
                ", isUsed=" + isUsed +
                ", limitStr=" + limitStr +
                ", type=" + type +
                ", keepname=" + keepname +
                "}";
    }
}
