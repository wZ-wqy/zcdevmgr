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
 * @since 2018-07-27
 */
@TableName("SYS_CARD_ADDR")
public class SysCardAddr extends BaseModel<SysCardAddr> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("SHENGF")
    private String shengf;
    @TableField("CHENGS")
    private String chengs;
    @TableField("QUX")
    private String qux;
    @TableField("SHENGFMC")
    private String shengfmc;
    @TableField("CHENGSMC")
    private String chengsmc;
    @TableField("QUXMC")
    private String quxmc;
    @TableField("USER_ID")
    private String userId;
    @TableField("CARD_NO")
    private String cardNo;
    @TableField("CARD_BANK")
    private String cardBank;
    @TableField("CARD_PART")
    private String cardPart;
    @TableField("CARD_NAME")
    private String cardName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShengf() {
        return shengf;
    }

    public void setShengf(String shengf) {
        this.shengf = shengf;
    }

    public String getChengs() {
        return chengs;
    }

    public void setChengs(String chengs) {
        this.chengs = chengs;
    }

    public String getQux() {
        return qux;
    }

    public void setQux(String qux) {
        this.qux = qux;
    }

    public String getShengfmc() {
        return shengfmc;
    }

    public void setShengfmc(String shengfmc) {
        this.shengfmc = shengfmc;
    }

    public String getChengsmc() {
        return chengsmc;
    }

    public void setChengsmc(String chengsmc) {
        this.chengsmc = chengsmc;
    }

    public String getQuxmc() {
        return quxmc;
    }

    public void setQuxmc(String quxmc) {
        this.quxmc = quxmc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public String getCardPart() {
        return cardPart;
    }

    public void setCardPart(String cardPart) {
        this.cardPart = cardPart;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysCardAddr{" +
                ", id=" + id +
                ", shengf=" + shengf +
                ", chengs=" + chengs +
                ", qux=" + qux +
                ", shengfmc=" + shengfmc +
                ", chengsmc=" + chengsmc +
                ", quxmc=" + quxmc +
                ", userId=" + userId +
                ", cardNo=" + cardNo +
                ", cardBank=" + cardBank +
                ", cardPart=" + cardPart +
                ", cardName=" + cardName +
                "}";
    }
}
