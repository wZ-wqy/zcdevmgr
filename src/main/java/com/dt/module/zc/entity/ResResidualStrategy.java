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

@TableName("res_residual_strategy")

public class ResResidualStrategy extends BaseModel<ResResidualStrategy> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("status")
    private String status;
    @TableField("strategydesc")
    private String strategydesc;
    @TableField("code")
    private String code;
    @TableField("residualvaluerate")
    private BigDecimal residualvaluerate;
    @TableField("depreciationrate")
    private BigDecimal depreciationrate;
    @TableField("valuestr")
    private String valuestr;
    @TableField("mark")
    private String mark;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStrategydesc() {
        return strategydesc;
    }

    public void setStrategydesc(String strategydesc) {
        this.strategydesc = strategydesc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getResidualvaluerate() {
        return residualvaluerate;
    }

    public void setResidualvaluerate(BigDecimal residualvaluerate) {
        this.residualvaluerate = residualvaluerate;
    }

    public BigDecimal getDepreciationrate() {
        return depreciationrate;
    }

    public void setDepreciationrate(BigDecimal depreciationrate) {
        this.depreciationrate = depreciationrate;
    }

    public String getValuestr() {
        return valuestr;
    }

    public void setValuestr(String valuestr) {
        this.valuestr = valuestr;
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
        return "ResResidualStrategy{" +
                "id=" + id +
                ", name=" + name +
                ", status=" + status +
                ", strategydesc=" + strategydesc +
                ", code=" + code +
                ", residualvaluerate=" + residualvaluerate +
                ", depreciationrate=" + depreciationrate +
                ", valuestr=" + valuestr +
                ", mark=" + mark +
                "}";
    }
}
