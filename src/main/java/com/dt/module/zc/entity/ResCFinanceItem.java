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
 * @since 2020-08-14
 */

@TableName("res_c_finance_item")

public class ResCFinanceItem extends BaseModel<ResCFinanceItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busuuid")
    private String busuuid;
    @TableField("resid")
    private String resid;
    @TableField("fbelongcomp")
    private String fbelongcomp;
    @TableField("tbelongcomp")
    private String tbelongcomp;
    @TableField("fbelongpart")
    private String fbelongpart;
    @TableField("tbelongpart")
    private String tbelongpart;
    @TableField("fbuyprice")
    private BigDecimal fbuyprice;
    @TableField("tbuyprice")
    private BigDecimal tbuyprice;
    @TableField("fnetworth")
    private BigDecimal fnetworth;
    @TableField("tnetworth")
    private BigDecimal tnetworth;
    @TableField("fresidualvalue")
    private BigDecimal fresidualvalue;
    @TableField("tresidualvalue")
    private BigDecimal tresidualvalue;
    @TableField("faccumulateddepreciation")
    private BigDecimal faccumulateddepreciation;
    @TableField("taccumulateddepreciation")
    private BigDecimal taccumulateddepreciation;
    @TableField("tbelongcompstatus")
    private String tbelongcompstatus;
    @TableField("tbelongpartstatus")
    private String tbelongpartstatus;
    @TableField("tbuypricestatus")
    private String tbuypricestatus;
    @TableField("tnetworthstatus")
    private String tnetworthstatus;
    @TableField("tresidualvaluestatus")
    private String tresidualvaluestatus;
    @TableField("taccumulatedstatus")
    private String taccumulatedstatus;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusuuid() {
        return busuuid;
    }

    public void setBusuuid(String busuuid) {
        this.busuuid = busuuid;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getFbelongcomp() {
        return fbelongcomp;
    }

    public void setFbelongcomp(String fbelongcomp) {
        this.fbelongcomp = fbelongcomp;
    }

    public String getTbelongcomp() {
        return tbelongcomp;
    }

    public void setTbelongcomp(String tbelongcomp) {
        this.tbelongcomp = tbelongcomp;
    }

    public String getFbelongpart() {
        return fbelongpart;
    }

    public void setFbelongpart(String fbelongpart) {
        this.fbelongpart = fbelongpart;
    }

    public String getTbelongpart() {
        return tbelongpart;
    }

    public void setTbelongpart(String tbelongpart) {
        this.tbelongpart = tbelongpart;
    }

    public BigDecimal getFbuyprice() {
        return fbuyprice;
    }

    public void setFbuyprice(BigDecimal fbuyprice) {
        this.fbuyprice = fbuyprice;
    }

    public BigDecimal getTbuyprice() {
        return tbuyprice;
    }

    public void setTbuyprice(BigDecimal tbuyprice) {
        this.tbuyprice = tbuyprice;
    }

    public BigDecimal getFnetworth() {
        return fnetworth;
    }

    public void setFnetworth(BigDecimal fnetworth) {
        this.fnetworth = fnetworth;
    }

    public BigDecimal getTnetworth() {
        return tnetworth;
    }

    public void setTnetworth(BigDecimal tnetworth) {
        this.tnetworth = tnetworth;
    }

    public BigDecimal getFresidualvalue() {
        return fresidualvalue;
    }

    public void setFresidualvalue(BigDecimal fresidualvalue) {
        this.fresidualvalue = fresidualvalue;
    }

    public BigDecimal getTresidualvalue() {
        return tresidualvalue;
    }

    public void setTresidualvalue(BigDecimal tresidualvalue) {
        this.tresidualvalue = tresidualvalue;
    }

    public BigDecimal getFaccumulateddepreciation() {
        return faccumulateddepreciation;
    }

    public void setFaccumulateddepreciation(BigDecimal faccumulateddepreciation) {
        this.faccumulateddepreciation = faccumulateddepreciation;
    }

    public BigDecimal getTaccumulateddepreciation() {
        return taccumulateddepreciation;
    }

    public void setTaccumulateddepreciation(BigDecimal taccumulateddepreciation) {
        this.taccumulateddepreciation = taccumulateddepreciation;
    }

    public String getTbelongcompstatus() {
        return tbelongcompstatus;
    }

    public void setTbelongcompstatus(String tbelongcompstatus) {
        this.tbelongcompstatus = tbelongcompstatus;
    }

    public String getTbelongpartstatus() {
        return tbelongpartstatus;
    }

    public void setTbelongpartstatus(String tbelongpartstatus) {
        this.tbelongpartstatus = tbelongpartstatus;
    }

    public String getTbuypricestatus() {
        return tbuypricestatus;
    }

    public void setTbuypricestatus(String tbuypricestatus) {
        this.tbuypricestatus = tbuypricestatus;
    }

    public String getTnetworthstatus() {
        return tnetworthstatus;
    }

    public void setTnetworthstatus(String tnetworthstatus) {
        this.tnetworthstatus = tnetworthstatus;
    }

    public String getTresidualvaluestatus() {
        return tresidualvaluestatus;
    }

    public void setTresidualvaluestatus(String tresidualvaluestatus) {
        this.tresidualvaluestatus = tresidualvaluestatus;
    }

    public String getTaccumulatedstatus() {
        return taccumulatedstatus;
    }

    public void setTaccumulatedstatus(String taccumulatedstatus) {
        this.taccumulatedstatus = taccumulatedstatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResCFinanceItem{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", resid=" + resid +
                ", fbelongcomp=" + fbelongcomp +
                ", tbelongcomp=" + tbelongcomp +
                ", fbelongpart=" + fbelongpart +
                ", tbelongpart=" + tbelongpart +
                ", fbuyprice=" + fbuyprice +
                ", tbuyprice=" + tbuyprice +
                ", fnetworth=" + fnetworth +
                ", tnetworth=" + tnetworth +
                ", fresidualvalue=" + fresidualvalue +
                ", tresidualvalue=" + tresidualvalue +
                ", faccumulateddepreciation=" + faccumulateddepreciation +
                ", taccumulateddepreciation=" + taccumulateddepreciation +
                ", tbelongcompstatus=" + tbelongcompstatus +
                ", tbelongpartstatus=" + tbelongpartstatus +
                ", tbuypricestatus=" + tbuypricestatus +
                ", tnetworthstatus=" + tnetworthstatus +
                ", tresidualvaluestatus=" + tresidualvaluestatus +
                ", taccumulatedstatus=" + taccumulatedstatus +
                "}";
    }
}
