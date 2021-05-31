package com.dt.module.cmdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: lank
 * @date: Oct 21, 2019 7:19:11 PM
 * @Description:
 */
@ExcelTarget("ResEntity")
public class ResEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "资产编号", width = 25)
    private String uuid;

    @Excel(name = "#资产类别", width = 15)
    private String classname;

    @Excel(name = "*类别明细", width = 25)
    private String classfullname;

    @Excel(name = "*资产名称", width = 15)
    private String name;

    @Excel(name = "*规格型号", width = 20)
    private String model;


    @Excel(name = "资产状态", width = 10)
    private String recyclestr;

    @Excel(name = "资产供应商", width = 15)
    private String supplierstr;

    @Excel(name = "品牌", width = 10)
    private String brandstr;

    @Excel(name = "序列", width = 25)
    private String sn;

    @Excel(name = "使用期限", width = 8)
    private String usefullifestr;

    @Excel(name = "生产日期", width = 15)
    private String fd1str;

    @Excel(name = "计量单位", width = 8)
    private String unit;

    @Excel(name = "来源", width = 8)
    private String zcsourcestr;

    @Excel(name = "配置描述", width = 15)
    private String confdesc;

    @Excel(name = "其他编号", width = 12)
    private String fs20;

    @Excel(name = "备注", width = 20)
    private String mark;

    @Excel(name = "标签1", width = 12)
    private String fs1;

    @Excel(name = "标签2", width = 12)
    private String fs2;

    @Excel(name = "存放区域", width = 15)
    private String locstr;

    @Excel(name = "机柜", width = 10)
    private String rackstr;

    @Excel(name = "机架", width = 10)
    private String frame;

    @Excel(name = "位置", width = 10)
    private String locdtl;

    @Excel(name = "所属公司", width = 20)
    private String belongcomp_fullname;
//
//    @Excel(name = "使用公司", width = 20)
//    private String comp_fullname;

    @Excel(name = "使用部门", width = 20)
    private String part_fullname;

    @Excel(name = "#使用人", width = 18)
    private String used_username;

    @Excel(name = "使用人工号", width = 15)
    private String emplid;

    @Excel(name = "采购日期", width = 15)
    private String buy_timestr;

    @Excel(name = "采购单价", width = 10)
    private String buy_price;

    @Excel(name = "资产净值", width = 10)
    private String net_worth;

    @Excel(name = "累计折旧", width = 10)
    private String accumulateddepreciation;

    @Excel(name = "维保商", width = 10)
    private String wbsupplierstr;

    @Excel(name = "维保状态", width = 10)
    private String wbstr;

    @Excel(name = "脱保日期", width = 15)
    private String wbout_datestr;

    @Excel(name = "报废日期", width = 15)
    private String bfout_datestr;
    @Excel(name = "脱保计算", width = 8)
    private String wb_autostr;

    //IT资产
    @Excel(name = "运行环境", width = 18)
    private String envstr;

    @Excel(name = "风险等级", width = 10)
    private String riskstr;

    @Excel(name = "IP", width = 20)
    private String ip;

    @Excel(name = "批次号", width = 25)
    private String batch;

    private String processmsg = "";



    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }



    public ResEntity() {
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the wb_autostr
     */
    public String getWb_autostr() {
        return wb_autostr;
    }

    /**
     * @param wb_autostr the wb_autostr to set
     */
    public void setWb_autostr(String wb_autostr) {
        this.wb_autostr = wb_autostr;
    }

    /**
     * @return the fs1
     */
    public String getFs1() {
        return fs1;
    }

    /**
     * @param fs1 the fs1 to set
     */
    public void setFs1(String fs1) {
        this.fs1 = fs1;
    }

    /**
     * @return the fs2
     */
    public String getFs2() {
        return fs2;
    }

    /**
     * @param fs2 the fs2 to set
     */
    public void setFs2(String fs2) {
        this.fs2 = fs2;
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
     * @return the wbstr
     */
    public String getWbstr() {
        return wbstr;
    }

    /**
     * @param wbstr the wbstr to set
     */
    public void setWbstr(String wbstr) {
        this.wbstr = wbstr;
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
     * @return the envstr
     */
    public String getEnvstr() {
        return envstr;
    }

    /**
     * @param envstr the envstr to set
     */
    public void setEnvstr(String envstr) {
        this.envstr = envstr;
    }

    /**
     * @return the riskstr
     */
    public String getRiskstr() {
        return riskstr;
    }

    /**
     * @param riskstr the riskstr to set
     */
    public void setRiskstr(String riskstr) {
        this.riskstr = riskstr;
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
     * @return the rackstr
     */
    public String getRackstr() {
        return rackstr;
    }

    /**
     * @param rackstr the rackstr to set
     */
    public void setRackstr(String rackstr) {
        this.rackstr = rackstr;
    }

    /**
     * @return the frame
     */
    public String getFrame() {
        return frame;
    }

    /**
     * @param frame the frame to set
     */
    public void setFrame(String frame) {
        this.frame = frame;
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
     * @return the mark
     */
    public String getMark() {
        return mark;
    }

    /**
     * @param mark the mark to set
     */
    public void setMark(String mark) {
        this.mark = mark;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the used_username
     */
    public String getUsed_username() {
        return used_username;
    }

    /**
     * @param used_username the used_username to set
     */
    public void setUsed_username(String used_username) {
        this.used_username = used_username;
    }


    public String getBfout_datestr() {
        return bfout_datestr;
    }

    /**
     * @param bfout_datestr the bfout_datestr to set
     */
    public void getBfout_datestr(String bfout_datestr) {
        this.bfout_datestr = bfout_datestr;
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

    /**
     * @return the wbout_datestr
     */
    public String getWbout_datestr() {
        return wbout_datestr;
    }

    /**
     * @param wbout_datestr the wbout_datestr to set
     */
    public void setWbout_datestr(String wbout_datestr) {
        this.wbout_datestr = wbout_datestr;
    }

    public void fullResEntity(JSONObject obj) {

        this.supplierstr = obj.getString("supplierstr");
        this.uuid = obj.getString("uuid");
        this.classname = obj.getString("classname");
        this.classfullname = obj.getString("classfullname");
        this.wbout_datestr = obj.getString("wbout_datestr");
        this.brandstr = obj.getString("brandstr");
        this.model = obj.getString("model");
        this.confdesc = obj.getString("confdesc");
        this.sn = obj.getString("sn");
        this.recyclestr = obj.getString("recyclestr");
        this.wbstr = obj.getString("wbstr");
        this.envstr = obj.getString("envstr");
        this.riskstr = obj.getString("riskstr");
        this.locstr = obj.getString("locstr");
        this.rackstr = obj.getString("rackstr");
        this.frame = obj.getString("frame");
        this.buy_timestr = obj.getString("buy_timestr");
        this.mark = obj.getString("mark");
        this.ip = obj.getString("ip");
        this.buy_price = obj.getString("buy_price");
        this.name = obj.getString("name");
        this.part_fullname = obj.getString("part_fullname");
        this.used_username = obj.getString("used_username");
        this.locdtl = obj.getString("locdtl");
        this.wbsupplierstr = obj.getString("wbsupplierstr");
        this.fs1 = obj.getString("fs1");
        this.fs2 = obj.getString("fs2");
        this.fs20 = obj.getString("fs20");
        this.net_worth = obj.getString("net_worth");
        this.wb_autostr = obj.getString("wb_autostr");
        this.zcsourcestr = obj.getString("zcsourcestr");
//        this.comp_fullname = obj.getString("comp_fullname");
        this.belongcomp_fullname = obj.getString("belongcomp_fullname");
        this.usefullifestr = obj.getString("usefullifestr");
        this.unit = obj.getString("unit");
        this.emplid = obj.getString("emplid");
        this.batch = obj.getString("batch");
        this.fd1str = obj.getString("fd1str");
        this.accumulateddepreciation = obj.getString("accumulateddepreciation");

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


    public String getWbsupplierstr() {
        return wbsupplierstr;
    }

    public void setWbsupplierstr(String wbsupplierstr) {
        this.wbsupplierstr = wbsupplierstr;
    }

    public String getZcsourcestr() {
        return zcsourcestr;
    }

    public void setZcsourcestr(String zcsourcestr) {
        this.zcsourcestr = zcsourcestr;
    }

//    public String getComp_fullname() {
//        return comp_fullname;
//    }
//
//    public void setComp_fullname(String comp_fullname) {
//        this.comp_fullname = comp_fullname;
//    }

    public String getBelongcomp_fullname() {
        return belongcomp_fullname;
    }

    public void setBelongcomp_fullname(String belongcomp_fullname) {
        this.belongcomp_fullname = belongcomp_fullname;
    }


    public String getUsefullifestr() {
        return usefullifestr;
    }

    public void setUsefullifestr(String usefullifestr) {
        this.usefullifestr = usefullifestr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAccumulateddepreciation() {
        return accumulateddepreciation;
    }

    public void setAccumulateddepreciation(String accumulateddepreciation) {
        this.accumulateddepreciation = accumulateddepreciation;
    }

    public String getEmplid() {
        return emplid;
    }

    public void setEmplid(String emplid) {
        this.emplid = emplid;
    }

    public String getFd1str() {
        return fd1str;
    }

    public void setFd1str(String fd1str) {
        this.fd1str = fd1str;
    }
}
