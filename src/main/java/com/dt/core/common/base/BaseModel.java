package com.dt.core.common.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lank
 * @version 创建时间：2017年8月1日 上午8:33:12
 */
@SuppressWarnings("rawtypes")
public class BaseModel<T> extends Model implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableField(value = "DR", fill = FieldFill.INSERT)
    @TableLogic
    @JSONField(serialize = false)
    private String dr;
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * @return the dr
     */
    public String getDr() {
        return dr;
    }

    /**
     * @param dr the dr to set
     */
    public void setDr(String dr) {
        this.dr = dr;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the createBy
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * @param createBy the createBy to set
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the updateBy
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * @param updateBy the updateBy to set
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    protected Serializable pkVal() {
        return this.pkVal();
    }

}