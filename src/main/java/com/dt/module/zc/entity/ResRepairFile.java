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
 * @since 2020-04-19
 */

@TableName("res_repair_file")

public class ResRepairFile extends BaseModel<ResRepairFile> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("fileid")
    private String fileid;
    @TableField("repiarid")
    private String repiarid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getRepiarid() {
        return repiarid;
    }

    public void setRepiarid(String repiarid) {
        this.repiarid = repiarid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResRepairFile{" +
                "id=" + id +
                ", fileid=" + fileid +
                ", repiarid=" + repiarid +
                "}";
    }
}
