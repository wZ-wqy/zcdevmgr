package com.dt.module.zc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-08-05
 */

@TableName("res_residual_item")

public class ResResidualItem extends BaseModel<ResResidualItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("uuid")
    private String uuid;
    @TableField("resid")
    private String resid;
    @TableField("checkstatus")
    private String checkstatus;
    @TableField("curresidualvalue")
    private BigDecimal curresidualvalue;
    @TableField("buyprice")
    private BigDecimal buyprice;
    @TableField("bnetworth")
    private BigDecimal bnetworth;
    @TableField("anetworth")
    private BigDecimal anetworth;
    @TableField("lossprice")
    private BigDecimal lossprice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus;
    }

    public BigDecimal getCurresidualvalue() {
        return curresidualvalue;
    }

    public void setCurresidualvalue(BigDecimal curresidualvalue) {
        this.curresidualvalue = curresidualvalue;
    }

    public BigDecimal getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(BigDecimal buyprice) {
        this.buyprice = buyprice;
    }

    public BigDecimal getBnetworth() {
        return bnetworth;
    }

    public void setBnetworth(BigDecimal bnetworth) {
        this.bnetworth = bnetworth;
    }

    public BigDecimal getAnetworth() {
        return anetworth;
    }

    public void setAnetworth(BigDecimal anetworth) {
        this.anetworth = anetworth;
    }

    public BigDecimal getLossprice() {
        return lossprice;
    }

    public void setLossprice(BigDecimal lossprice) {
        this.lossprice = lossprice;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResResidualItem{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", resid=" + resid +
                ", checkstatus=" + checkstatus +
                ", curresidualvalue=" + curresidualvalue +
                ", buyprice=" + buyprice +
                ", bnetworth=" + bnetworth +
                ", anetworth=" + anetworth +
                ", lossprice=" + lossprice +
                "}";
    }
}
