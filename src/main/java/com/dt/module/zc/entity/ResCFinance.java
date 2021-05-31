package com.dt.module.zc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-08-14
 */

@TableName("res_c_finance")

public class ResCFinance extends BaseModel<ResCFinance> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busuuid")
    private String busuuid;
    @TableField("busidate")
    private Date busidate;
    @TableField("status")
    private String status;
    @TableField("processuserid")
    private String processuserid;
    @TableField("processusername")
    private String processusername;
    @TableField("tbelongcomp")
    private String tbelongcomp;
    @TableField("tbelongpart")
    private String tbelongpart;
    @TableField("tbuyprice")
    private BigDecimal tbuyprice;
    @TableField("tnetworth")
    private BigDecimal tnetworth;
    @TableField("tresidualvalue")
    private BigDecimal tresidualvalue;
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
    @TableField("mark")
    private String mark;


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

    public Date getBusidate() {
        return busidate;
    }

    public void setBusidate(Date busidate) {
        this.busidate = busidate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessuserid() {
        return processuserid;
    }

    public void setProcessuserid(String processuserid) {
        this.processuserid = processuserid;
    }

    public String getProcessusername() {
        return processusername;
    }

    public void setProcessusername(String processusername) {
        this.processusername = processusername;
    }

    public String getTbelongcomp() {
        return tbelongcomp;
    }

    public void setTbelongcomp(String tbelongcomp) {
        this.tbelongcomp = tbelongcomp;
    }

    public String getTbelongpart() {
        return tbelongpart;
    }

    public void setTbelongpart(String tbelongpart) {
        this.tbelongpart = tbelongpart;
    }

    public BigDecimal getTbuyprice() {
        return tbuyprice;
    }

    public void setTbuyprice(BigDecimal tbuyprice) {
        this.tbuyprice = tbuyprice;
    }

    public BigDecimal getTnetworth() {
        return tnetworth;
    }

    public void setTnetworth(BigDecimal tnetworth) {
        this.tnetworth = tnetworth;
    }

    public BigDecimal getTresidualvalue() {
        return tresidualvalue;
    }

    public void setTresidualvalue(BigDecimal tresidualvalue) {
        this.tresidualvalue = tresidualvalue;
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
        return "ResCFinance{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", busidate=" + busidate +
                ", status=" + status +
                ", processuserid=" + processuserid +
                ", processusername=" + processusername +
                ", tbelongcomp=" + tbelongcomp +
                ", tbelongpart=" + tbelongpart +
                ", tbuyprice=" + tbuyprice +
                ", tnetworth=" + tnetworth +
                ", tresidualvalue=" + tresidualvalue +
                ", taccumulateddepreciation=" + taccumulateddepreciation +
                ", tbelongcompstatus=" + tbelongcompstatus +
                ", tbelongpartstatus=" + tbelongpartstatus +
                ", tbuypricestatus=" + tbuypricestatus +
                ", tnetworthstatus=" + tnetworthstatus +
                ", tresidualvaluestatus=" + tresidualvaluestatus +
                ", taccumulatedstatus=" + taccumulatedstatus +
                ", mark=" + mark +
                "}";
    }
}
