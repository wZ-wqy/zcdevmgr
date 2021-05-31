package com.dt.module.flow.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.dt.core.common.base.BaseTModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author lank
 * @since 2020-12-28
 */
 
@TableName("uflo_blob")
 
public class UfloBlob extends BaseTModel<UfloBlob> {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private Long id;
    @TableField("BLOB_VALUE_")
    private String blobValue;
    @TableField("NAME_")
    private String name;
    @TableField("PROCESS_ID_")
    private Long processId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlobValue() {
        return blobValue;
    }

    public void setBlobValue(String blobValue) {
        this.blobValue = blobValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UfloBlob{" +
        "id=" + id +
        ", blobValue=" + blobValue +
        ", name=" + name +
        ", processId=" + processId +
        "}";
    }
}
