package com.dt.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**

 * @author lank
 * @since 2018-07-30
 */
@TableName("SYS_MODULES_ITEM")
public class SysModulesItem extends Model<SysModulesItem> {

    private static final long serialVersionUID = 1L;

    @TableId("MODULE_ITEM_ID")
    private String moduleItemId;
    @TableField("MODULE_ID")
    private String moduleId;
    @TableField("CT")
    private String ct;
    @TableField("STATUS")
    private String status;
    @TableField("TYPE")
    private String type;


    public String getModuleItemId() {
        return moduleItemId;
    }

    public void setModuleItemId(String moduleItemId) {
        this.moduleItemId = moduleItemId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
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

    @Override
    protected Serializable pkVal() {
        return this.moduleItemId;
    }

    @Override
    public String toString() {
        return "SysModulesItem{" +
                ", moduleItemId=" + moduleItemId +
                ", moduleId=" + moduleId +
                ", ct=" + ct +
                ", status=" + status +
                ", type=" + type +
                "}";
    }
}
