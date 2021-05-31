package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 盘点表
 * </p>
 *
 * @author lank
 * @since 2021-01-05
 */
 
@TableName("res_inventory")
 
public class ResInventory extends BaseModel<ResInventory> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 盘点单名称
     */
    @TableField("name")
    private String name;
    /**
     * 盘点单批次号
     */
    @TableField("batchid")
    private String batchid;
    /**
     * 盘点结束，是否同步状态,1|0
     */
    @TableField("syncstatus")
    private String syncstatus;
    /**
     * wait|start|finish|cancel
     */
    @TableField("status")
    private String status;
    /**
     * 资产采购时间
     */
    @TableField("resstartdate")
    private Date resstartdate;
    /**
     * 资产采购时间
     */
    @TableField("resenddate")
    private Date resenddate;
    /**
     * 使用公司
     */
    @TableField("usedcomp")
    private String usedcomp;
    /**
     * 使用公司名称
     */
    @TableField("usedcompname")
    private String usedcompname;
    /**
     * 使用部门
     */
    @TableField("usedpart")
    private String usedpart;
    /**
     * 使用部门名称
     */
    @TableField("usedpartname")
    private String usedpartname;
    /**
     * 使用部门数据
     */
    @TableField("usedpartdata")
    private String usedpartdata;
    /**
     * 所属公司
     */
    @TableField("belongcomp")
    private String belongcomp;
    /**
     * 所属公司名称
     */
    @TableField("belongcompname")
    private String belongcompname;
    /**
     * 资产分类
     */
    @TableField("rescat")
    private String rescat;
    /**
     * 资产分类名称
     */
    @TableField("rescatname")
    private String rescatname;
    /**
     * 资产分类数据
     */
    @TableField("rescatdata")
    private String rescatdata;
    /**
     * 资产区域
     */
    @TableField("area")
    private String area;
    /**
     * 资产区域名称
     */
    @TableField("areaname")
    private String areaname;
    /**
     * 资产区域数据
     */
    @TableField("areadata")
    private String areadata;
    /**
     * 盘点单负责人
     */
    @TableField("adminuserid")
    private String adminuserid;
    /**
     * 盘点单负责人
     */
    @TableField("adminusername")
    private String adminusername;
    /**
     * 盘点单开始时间
     */
    @TableField("starttime")
    private Date starttime;
    /**
     * 盘点单结束时间
     */
    @TableField("finishtime")
    private Date finishtime;
    /**
     * 盘点人
     */
    @TableField("pduserlist")
    private String pduserlist;
    /**
     * 盘点人数据
     */
    @TableField("pduserdata")
    private String pduserdata;
    /**
     * 是否支持手工盘点,1|0
     */
    @TableField("manualinventory")
    private String manualinventory;
    /**
     * 是否全员盘点,1|0
     */
    @TableField("allusersinventory")
    private String allusersinventory;
    /**
     * 资产盘点数量
     */
    @TableField("cnt")
    private BigDecimal cnt;
    @TableField("mark")
    private String mark;
    /**
     * 盘亏
     */
    @TableField("inventoryloss")
    private BigDecimal inventoryloss;
    /**
     * 盘盈
     */
    @TableField("inventorysurplus")
    private BigDecimal inventorysurplus;


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

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(String syncstatus) {
        this.syncstatus = syncstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getResstartdate() {
        return resstartdate;
    }

    public void setResstartdate(Date resstartdate) {
        this.resstartdate = resstartdate;
    }

    public Date getResenddate() {
        return resenddate;
    }

    public void setResenddate(Date resenddate) {
        this.resenddate = resenddate;
    }

    public String getUsedcomp() {
        return usedcomp;
    }

    public void setUsedcomp(String usedcomp) {
        this.usedcomp = usedcomp;
    }

    public String getUsedcompname() {
        return usedcompname;
    }

    public void setUsedcompname(String usedcompname) {
        this.usedcompname = usedcompname;
    }

    public String getUsedpart() {
        return usedpart;
    }

    public void setUsedpart(String usedpart) {
        this.usedpart = usedpart;
    }

    public String getUsedpartname() {
        return usedpartname;
    }

    public void setUsedpartname(String usedpartname) {
        this.usedpartname = usedpartname;
    }

    public String getUsedpartdata() {
        return usedpartdata;
    }

    public void setUsedpartdata(String usedpartdata) {
        this.usedpartdata = usedpartdata;
    }

    public String getBelongcomp() {
        return belongcomp;
    }

    public void setBelongcomp(String belongcomp) {
        this.belongcomp = belongcomp;
    }

    public String getBelongcompname() {
        return belongcompname;
    }

    public void setBelongcompname(String belongcompname) {
        this.belongcompname = belongcompname;
    }

    public String getRescat() {
        return rescat;
    }

    public void setRescat(String rescat) {
        this.rescat = rescat;
    }

    public String getRescatname() {
        return rescatname;
    }

    public void setRescatname(String rescatname) {
        this.rescatname = rescatname;
    }

    public String getRescatdata() {
        return rescatdata;
    }

    public void setRescatdata(String rescatdata) {
        this.rescatdata = rescatdata;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAreadata() {
        return areadata;
    }

    public void setAreadata(String areadata) {
        this.areadata = areadata;
    }

    public String getAdminuserid() {
        return adminuserid;
    }

    public void setAdminuserid(String adminuserid) {
        this.adminuserid = adminuserid;
    }

    public String getAdminusername() {
        return adminusername;
    }

    public void setAdminusername(String adminusername) {
        this.adminusername = adminusername;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(Date finishtime) {
        this.finishtime = finishtime;
    }

    public String getPduserlist() {
        return pduserlist;
    }

    public void setPduserlist(String pduserlist) {
        this.pduserlist = pduserlist;
    }

    public String getPduserdata() {
        return pduserdata;
    }

    public void setPduserdata(String pduserdata) {
        this.pduserdata = pduserdata;
    }

    public String getManualinventory() {
        return manualinventory;
    }

    public void setManualinventory(String manualinventory) {
        this.manualinventory = manualinventory;
    }

    public String getAllusersinventory() {
        return allusersinventory;
    }

    public void setAllusersinventory(String allusersinventory) {
        this.allusersinventory = allusersinventory;
    }

    public BigDecimal getCnt() {
        return cnt;
    }

    public void setCnt(BigDecimal cnt) {
        this.cnt = cnt;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public BigDecimal getInventoryloss() {
        return inventoryloss;
    }

    public void setInventoryloss(BigDecimal inventoryloss) {
        this.inventoryloss = inventoryloss;
    }

    public BigDecimal getInventorysurplus() {
        return inventorysurplus;
    }

    public void setInventorysurplus(BigDecimal inventorysurplus) {
        this.inventorysurplus = inventorysurplus;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResInventory{" +
        "id=" + id +
        ", name=" + name +
        ", batchid=" + batchid +
        ", syncstatus=" + syncstatus +
        ", status=" + status +
        ", resstartdate=" + resstartdate +
        ", resenddate=" + resenddate +
        ", usedcomp=" + usedcomp +
        ", usedcompname=" + usedcompname +
        ", usedpart=" + usedpart +
        ", usedpartname=" + usedpartname +
        ", usedpartdata=" + usedpartdata +
        ", belongcomp=" + belongcomp +
        ", belongcompname=" + belongcompname +
        ", rescat=" + rescat +
        ", rescatname=" + rescatname +
        ", rescatdata=" + rescatdata +
        ", area=" + area +
        ", areaname=" + areaname +
        ", areadata=" + areadata +
        ", adminuserid=" + adminuserid +
        ", adminusername=" + adminusername +
        ", starttime=" + starttime +
        ", finishtime=" + finishtime +
        ", pduserlist=" + pduserlist +
        ", pduserdata=" + pduserdata +
        ", manualinventory=" + manualinventory +
        ", allusersinventory=" + allusersinventory +
        ", cnt=" + cnt +
        ", mark=" + mark +
        ", inventoryloss=" + inventoryloss +
        ", inventorysurplus=" + inventorysurplus +
        "}";
    }
}
