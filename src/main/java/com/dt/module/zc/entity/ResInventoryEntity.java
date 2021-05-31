package com.dt.module.zc.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: lank
 * @date: Oct 21, 2019 7:19:11 PM
 * @Description:
 */
@ExcelTarget("ResInventoryEntity")
public class ResInventoryEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Excel(name = "盘点单据", width = 18)
    private String pdbatchid;

    @Excel(name = "盘点状态", width = 10)
    private String pdstatusstr;

    @Excel(name = "资产编号", width = 16)
    private String uuid;


    @Excel(name = "资产名称", width = 15)
    private String name;

    @Excel(name = "资产类别", width = 15)
    private String classname;

    @Excel(name = "类别明细(必需)", width = 20)
    private String classfullname;

    @Excel(name = "资产供应商", width = 15)
    private String supplierstr;

    @Excel(name = "品牌", width = 10)
    private String brandstr;

    @Excel(name = "型号", width = 20)
    private String model;

    @Excel(name = "序列号", width = 25)
    private String sn;

    @Excel(name = "资产状态", width = 10)
    private String recyclestr;

    @Excel(name = "来源", width = 8)
    private String zcsourcestr;

    @Excel(name = "配置描述", width = 15)
    private String confdesc;

    @Excel(name = "其他编号", width = 12)
    private String fs20;

    @Excel(name = "存放区域", width = 15)
    private String locstr;

    @Excel(name = "位置", width = 10)
    private String locdtl;


    //组织
    @Excel(name = "所属公司", width = 20)
    private String belongcomp_fullname;


    @Excel(name = "使用部门", width = 20)
    private String part_fullname;

    //财务
    @Excel(name = "采购日期", width = 15)
    private String buy_timestr;

    @Excel(name = "原值", width = 10)
    private String buy_price;

    @Excel(name = "净值", width = 10)
    private String net_worth;

    private String processmsg = "";


    public ResInventoryEntity() {
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the fs20
     */
    public String getFs20() {
        return fs20;
    }

    /**
     * @param fs20 the fs20 to set
     */
    public void setFs20(String fs20) {
        this.fs20 = fs20;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the brandstr
     */
    public String getBrandstr() {
        return brandstr;
    }

    /**
     * @param brandstr the brandstr to set
     */
    public void setBrandstr(String brandstr) {
        this.brandstr = brandstr;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the confdesc
     */
    public String getConfdesc() {
        return confdesc;
    }

    /**
     * @param confdesc the confdesc to set
     */
    public void setConfdesc(String confdesc) {
        this.confdesc = confdesc;
    }

    /**
     * @return the sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * @param sn the sn to set
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * @return the recyclestr
     */
    public String getRecyclestr() {
        return recyclestr;
    }

    /**
     * @param recyclestr the recyclestr to set
     */
    public void setRecyclestr(String recyclestr) {
        this.recyclestr = recyclestr;
    }

    /**
     * @return the net_worth
     */
    public String getNet_worth() {
        return net_worth;
    }

    /**
     * @param net_worth the net_worth to set
     */
    public void setNet_worth(String net_worth) {
        this.net_worth = net_worth;
    }

    /**
     * @return the locstr
     */
    public String getLocstr() {
        return locstr;
    }

    /**
     * @param locstr the locstr to set
     */
    public void setLocstr(String locstr) {
        this.locstr = locstr;
    }

    /**
     * @return the buy_timestr
     */
    public String getBuy_timestr() {
        return buy_timestr;
    }

    /**
     * @param buy_timestr the buy_timestr to set
     */
    public void setBuy_timestr(String buy_timestr) {
        this.buy_timestr = buy_timestr;
    }

    /**
     * @return the locdtl
     */
    public String getLocdtl() {
        return locdtl;
    }

    /**
     * @param locdtl the locdtl to set
     */
    public void setLocdtl(String locdtl) {
        this.locdtl = locdtl;
    }

    /**
     * @return the part_fullname
     */
    public String getPart_fullname() {
        return part_fullname;
    }

    /**
     * @param part_fullname the part_fullname to set
     */
    public void setPart_fullname(String part_fullname) {
        this.part_fullname = part_fullname;
    }

    /**
     * @return the buy_price
     */
    public String getBuy_price() {
        return buy_price;
    }

    /**
     * @param buy_price the buy_price to set
     */
    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void fullResEntity(JSONObject obj) {

        this.name=obj.getString("name");
        this.supplierstr = obj.getString("supplierstr");
        this.uuid = obj.getString("uuid");
        this.classname = obj.getString("classname");
        this.classfullname = obj.getString("classfullname");
        this.brandstr = obj.getString("brandstr");
        this.model = obj.getString("model");
        this.confdesc = obj.getString("confdesc");
        this.sn = obj.getString("sn");
        this.recyclestr = obj.getString("recyclestr");

        this.locstr = obj.getString("locstr");
        this.buy_timestr = obj.getString("buy_timestr");

        this.buy_price = obj.getString("buy_price");
        this.part_fullname = obj.getString("part_fullname");
        this.locdtl = obj.getString("locdtl");

        this.fs20 = obj.getString("fs20");
        this.net_worth = obj.getString("net_worth");
        this.zcsourcestr = obj.getString("zcsourcestr");

        this.belongcomp_fullname = obj.getString("belongcomp_fullname");

        this.pdbatchid = obj.getString("pdbatchid");
        this.pdstatusstr = obj.getString("pdstatusstr");



    }

    public String getSupplierstr() {
        return supplierstr;
    }

    public void setSupplierstr(String supplierstr) {
        this.supplierstr = supplierstr;
    }

    /**
     * @return the classfullname
     */
    public String getClassfullname() {
        return classfullname;
    }

    /**
     * @param classfullname the classfullname to set
     */
    public void setClassfullname(String classfullname) {
        this.classfullname = classfullname;
    }

    /**
     * @return the classname
     */
    public String getClassname() {
        return classname;
    }

    /**
     * @param classname the classname to set
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * @return the processmsg
     */
    public String getProcessmsg() {
        return processmsg;
    }

    /**
     * @param processmsg the processmsg to set
     */
    public void setProcessmsg(String processmsg) {
        this.processmsg = processmsg;
    }


    public String getZcsourcestr() {
        return zcsourcestr;
    }

    public void setZcsourcestr(String zcsourcestr) {
        this.zcsourcestr = zcsourcestr;
    }


    public String getBelongcomp_fullname() {
        return belongcomp_fullname;
    }

    public void setBelongcomp_fullname(String belongcomp_fullname) {
        this.belongcomp_fullname = belongcomp_fullname;
    }


    public String getPdbatchid() {
        return pdbatchid;
    }

    public void setPdbatchid(String pdbatchid) {
        this.pdbatchid = pdbatchid;
    }

    public String getPdstatusstr() {
        return pdstatusstr;
    }

    public void setPdstatusstr(String pdstatusstr) {
        this.pdstatusstr = pdstatusstr;
    }



}
