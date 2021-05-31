package com.dt.module.flow.entity;

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
 * @since 2020-04-10
 */

@TableName("sys_process_form")

public class SysProcessForm extends BaseModel<SysProcessForm> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("fdata")
    private String fdata;
    @TableField("ftpldata")
    private String ftpldata;
    @TableField("processdataid")
    private String processdataid;
    @TableField("duuid")
    private String duuid;
    /**
     * 流程标题
     */
    @TableField("dtitle")
    private String dtitle;
    /**
     * 流程内容
     */
    @TableField("dct")
    private String dct;
    /**
     * 链接
     */
    @TableField("durl")
    private String durl;
    @TableField("dname")
    private String dname;
    /**
     * 流程备注
     */
    @TableField("dmark")
    private String dmark;
    @TableField("dmessage")
    private String dmessage;
    /**
     * 性别
     */
    @TableField("dsex")
    private String dsex;
    /**
     * 状态
     */
    @TableField("dstatus")
    private String dstatus;
    /**
     * 大类型
     */
    @TableField("dtype")
    private String dtype;
    /**
     * 小类型
     */
    @TableField("dsubtype")
    private String dsubtype;
    /**
     * 密码
     */
    @TableField("dpwd")
    private String dpwd;
    /**
     * 地址
     */
    @TableField("daddr")
    private String daddr;
    /**
     * 联系人
     */
    @TableField("dcontact")
    private String dcontact;
    /**
     * 图片
     */
    @TableField("dpic1")
    private String dpic1;
    /**
     * 图片
     */
    @TableField("dpic2")
    private String dpic2;
    /**
     * 图片
     */
    @TableField("dpic3")
    private String dpic3;
    /**
     * 用户
     */
    @TableField("duser")
    private String duser;
    @TableField("dresult")
    private String dresult;
    /**
     * 总数
     */
    @TableField("dtotal")
    private BigDecimal dtotal;
    @TableField("dbacktime")
    private Date dbacktime;
    /**
     * 等级，级别
     */
    @TableField("dlevel")
    private String dlevel;
    @TableField("dmethod")
    private String dmethod;
    /**
     * 附件
     */
    @TableField("dfile")
    private String dfile;
    @TableField("ddict")
    private String ddict;
    /**
     * 附件
     */
    @TableField("dattach1")
    private String dattach1;
    /**
     * 附件
     */
    @TableField("dattach2")
    private String dattach2;
    /**
     * 附件
     */
    @TableField("dattach3")
    private String dattach3;
    /**
     * card
     */
    @TableField("dcard")
    private String dcard;
    @TableField("df1")
    private String df1;
    @TableField("df2")
    private String df2;
    @TableField("df3")
    private String df3;
    @TableField("df4")
    private String df4;
    @TableField("df5")
    private String df5;
    @TableField("df6")
    private String df6;
    @TableField("df7")
    private String df7;
    @TableField("df8")
    private String df8;
    @TableField("df9")
    private String df9;
    @TableField("df10")
    private String df10;
    @TableField("dn1")
    private BigDecimal dn1;
    @TableField("dn2")
    private BigDecimal dn2;
    @TableField("dn3")
    private BigDecimal dn3;
    @TableField("dn4")
    private BigDecimal dn4;
    @TableField("dn5")
    private BigDecimal dn5;
    @TableField("dn6")
    private BigDecimal dn6;
    @TableField("dn7")
    private BigDecimal dn7;
    @TableField("dn8")
    private BigDecimal dn8;
    @TableField("dn9")
    private BigDecimal dn9;
    @TableField("dn10")
    private BigDecimal dn10;
    @TableField("ftpldatamd5")
    private String ftpldatamd5;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFdata() {
        return fdata;
    }

    public void setFdata(String fdata) {
        this.fdata = fdata;
    }

    public String getFtpldata() {
        return ftpldata;
    }

    public void setFtpldata(String ftpldata) {
        this.ftpldata = ftpldata;
    }

    public String getProcessdataid() {
        return processdataid;
    }

    public void setProcessdataid(String processdataid) {
        this.processdataid = processdataid;
    }

    public String getDuuid() {
        return duuid;
    }

    public void setDuuid(String duuid) {
        this.duuid = duuid;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getDct() {
        return dct;
    }

    public void setDct(String dct) {
        this.dct = dct;
    }

    public String getDurl() {
        return durl;
    }

    public void setDurl(String durl) {
        this.durl = durl;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDmark() {
        return dmark;
    }

    public void setDmark(String dmark) {
        this.dmark = dmark;
    }

    public String getDmessage() {
        return dmessage;
    }

    public void setDmessage(String dmessage) {
        this.dmessage = dmessage;
    }

    public String getDsex() {
        return dsex;
    }

    public void setDsex(String dsex) {
        this.dsex = dsex;
    }

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDsubtype() {
        return dsubtype;
    }

    public void setDsubtype(String dsubtype) {
        this.dsubtype = dsubtype;
    }

    public String getDpwd() {
        return dpwd;
    }

    public void setDpwd(String dpwd) {
        this.dpwd = dpwd;
    }

    public String getDaddr() {
        return daddr;
    }

    public void setDaddr(String daddr) {
        this.daddr = daddr;
    }

    public String getDcontact() {
        return dcontact;
    }

    public void setDcontact(String dcontact) {
        this.dcontact = dcontact;
    }

    public String getDpic1() {
        return dpic1;
    }

    public void setDpic1(String dpic1) {
        this.dpic1 = dpic1;
    }

    public String getDpic2() {
        return dpic2;
    }

    public void setDpic2(String dpic2) {
        this.dpic2 = dpic2;
    }

    public String getDpic3() {
        return dpic3;
    }

    public void setDpic3(String dpic3) {
        this.dpic3 = dpic3;
    }

    public String getDuser() {
        return duser;
    }

    public void setDuser(String duser) {
        this.duser = duser;
    }

    public String getDresult() {
        return dresult;
    }

    public void setDresult(String dresult) {
        this.dresult = dresult;
    }

    public BigDecimal getDtotal() {
        return dtotal;
    }

    public void setDtotal(BigDecimal dtotal) {
        this.dtotal = dtotal;
    }

    public Date getDbacktime() {
        return dbacktime;
    }

    public void setDbacktime(Date dbacktime) {
        this.dbacktime = dbacktime;
    }

    public String getDlevel() {
        return dlevel;
    }

    public void setDlevel(String dlevel) {
        this.dlevel = dlevel;
    }

    public String getDmethod() {
        return dmethod;
    }

    public void setDmethod(String dmethod) {
        this.dmethod = dmethod;
    }

    public String getDfile() {
        return dfile;
    }

    public void setDfile(String dfile) {
        this.dfile = dfile;
    }

    public String getDdict() {
        return ddict;
    }

    public void setDdict(String ddict) {
        this.ddict = ddict;
    }

    public String getDattach1() {
        return dattach1;
    }

    public void setDattach1(String dattach1) {
        this.dattach1 = dattach1;
    }

    public String getDattach2() {
        return dattach2;
    }

    public void setDattach2(String dattach2) {
        this.dattach2 = dattach2;
    }

    public String getDattach3() {
        return dattach3;
    }

    public void setDattach3(String dattach3) {
        this.dattach3 = dattach3;
    }

    public String getDcard() {
        return dcard;
    }

    public void setDcard(String dcard) {
        this.dcard = dcard;
    }

    public String getDf1() {
        return df1;
    }

    public void setDf1(String df1) {
        this.df1 = df1;
    }

    public String getDf2() {
        return df2;
    }

    public void setDf2(String df2) {
        this.df2 = df2;
    }

    public String getDf3() {
        return df3;
    }

    public void setDf3(String df3) {
        this.df3 = df3;
    }

    public String getDf4() {
        return df4;
    }

    public void setDf4(String df4) {
        this.df4 = df4;
    }

    public String getDf5() {
        return df5;
    }

    public void setDf5(String df5) {
        this.df5 = df5;
    }

    public String getDf6() {
        return df6;
    }

    public void setDf6(String df6) {
        this.df6 = df6;
    }

    public String getDf7() {
        return df7;
    }

    public void setDf7(String df7) {
        this.df7 = df7;
    }

    public String getDf8() {
        return df8;
    }

    public void setDf8(String df8) {
        this.df8 = df8;
    }

    public String getDf9() {
        return df9;
    }

    public void setDf9(String df9) {
        this.df9 = df9;
    }

    public String getDf10() {
        return df10;
    }

    public void setDf10(String df10) {
        this.df10 = df10;
    }

    public BigDecimal getDn1() {
        return dn1;
    }

    public void setDn1(BigDecimal dn1) {
        this.dn1 = dn1;
    }

    public BigDecimal getDn2() {
        return dn2;
    }

    public void setDn2(BigDecimal dn2) {
        this.dn2 = dn2;
    }

    public BigDecimal getDn3() {
        return dn3;
    }

    public void setDn3(BigDecimal dn3) {
        this.dn3 = dn3;
    }

    public BigDecimal getDn4() {
        return dn4;
    }

    public void setDn4(BigDecimal dn4) {
        this.dn4 = dn4;
    }

    public BigDecimal getDn5() {
        return dn5;
    }

    public void setDn5(BigDecimal dn5) {
        this.dn5 = dn5;
    }

    public BigDecimal getDn6() {
        return dn6;
    }

    public void setDn6(BigDecimal dn6) {
        this.dn6 = dn6;
    }

    public BigDecimal getDn7() {
        return dn7;
    }

    public void setDn7(BigDecimal dn7) {
        this.dn7 = dn7;
    }

    public BigDecimal getDn8() {
        return dn8;
    }

    public void setDn8(BigDecimal dn8) {
        this.dn8 = dn8;
    }

    public BigDecimal getDn9() {
        return dn9;
    }

    public void setDn9(BigDecimal dn9) {
        this.dn9 = dn9;
    }

    public BigDecimal getDn10() {
        return dn10;
    }

    public void setDn10(BigDecimal dn10) {
        this.dn10 = dn10;
    }

    public String getFtpldatamd5() {
        return ftpldatamd5;
    }

    public void setFtpldatamd5(String ftpldatamd5) {
        this.ftpldatamd5 = ftpldatamd5;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysProcessForm{" +
                "id=" + id +
                ", fdata=" + fdata +
                ", ftpldata=" + ftpldata +
                ", processdataid=" + processdataid +
                ", duuid=" + duuid +
                ", dtitle=" + dtitle +
                ", dct=" + dct +
                ", durl=" + durl +
                ", dname=" + dname +
                ", dmark=" + dmark +
                ", dmessage=" + dmessage +
                ", dsex=" + dsex +
                ", dstatus=" + dstatus +
                ", dtype=" + dtype +
                ", dsubtype=" + dsubtype +
                ", dpwd=" + dpwd +
                ", daddr=" + daddr +
                ", dcontact=" + dcontact +
                ", dpic1=" + dpic1 +
                ", dpic2=" + dpic2 +
                ", dpic3=" + dpic3 +
                ", duser=" + duser +
                ", dresult=" + dresult +
                ", dtotal=" + dtotal +
                ", dbacktime=" + dbacktime +
                ", dlevel=" + dlevel +
                ", dmethod=" + dmethod +
                ", dfile=" + dfile +
                ", ddict=" + ddict +
                ", dattach1=" + dattach1 +
                ", dattach2=" + dattach2 +
                ", dattach3=" + dattach3 +
                ", dcard=" + dcard +
                ", df1=" + df1 +
                ", df2=" + df2 +
                ", df3=" + df3 +
                ", df4=" + df4 +
                ", df5=" + df5 +
                ", df6=" + df6 +
                ", df7=" + df7 +
                ", df8=" + df8 +
                ", df9=" + df9 +
                ", df10=" + df10 +
                ", dn1=" + dn1 +
                ", dn2=" + dn2 +
                ", dn3=" + dn3 +
                ", dn4=" + dn4 +
                ", dn5=" + dn5 +
                ", dn6=" + dn6 +
                ", dn7=" + dn7 +
                ", dn8=" + dn8 +
                ", dn9=" + dn9 +
                ", dn10=" + dn10 +
                ", ftpldatamd5=" + ftpldatamd5 +
                "}";
    }
}
