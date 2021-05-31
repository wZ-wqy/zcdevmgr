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
 * @since 2020-04-25
 */

@TableName("res_repair_item")

public class ResRepairItem extends BaseModel<ResRepairItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 变更前资产状态
     */
    @TableField("residprerecycle")
    private String residprerecycle;
    @TableField("resid")
    private String resid;
    @TableField("repairid")
    private String repairid;
    @TableField("mark")
    private String mark;
    @TableField("busuuid")
    private String busuuid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResidprerecycle() {
        return residprerecycle;
    }

    public void setResidprerecycle(String residprerecycle) {
        this.residprerecycle = residprerecycle;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getRepairid() {
        return repairid;
    }

    public void setRepairid(String repairid) {
        this.repairid = repairid;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBusuuid() {
        return busuuid;
    }

    public void setBusuuid(String busuuid) {
        this.busuuid = busuuid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResRepairItem{" +
                "id=" + id +
                ", residprerecycle=" + residprerecycle +
                ", resid=" + resid +
                ", repairid=" + repairid +
                ", mark=" + mark +
                ", busuuid=" + busuuid +
                "}";
    }
}
