package com.dt.module.zc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-05-27
 */

@TableName("res_inout")

public class ResInout extends BaseModel<ResInout> {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("type")
    private String type;
    /**
     * 单据号
     */
    @TableField("uuid")
    private String uuid;
    @TableField("title")
    private String title;
    /**
     * HCRK,HCCK
     */
    @TableField("action")
    private String action;
    /**
     * 待审批wait,已同意agreen,拒绝deny,打回back,无需审批none
     */
    @TableField("status")
    private String status;
    /**
     * 类型数量
     */
    @TableField("cnt")
    private BigDecimal cnt;
    /**
     * 资产来源
     */
    @TableField("zcsource")
    private String zcsource;
    /**
     * 供应商
     */
    @TableField("suppliername")
    private String suppliername;
    /**
     * 购买时间
     */
    @TableField("buytime")
    private String buytime;
    /**
     * 购买总价
     */
    @TableField("price")
    private BigDecimal price;
    @TableField("operuserid")
    private String operuserid;
    @TableField("operusername")
    private String operusername;
    /**
     * 业务时间
     */
    @TableField("busidate")
    private Date busidate;
    @TableField("rdate")
    private Date rdate;
    /**
     * 使用公司、出库
     */
    @TableField("compid")
    private String compid;
    /**
     * 区域、出库
     */
    @TableField("loc")
    private String loc;
    /**
     * 仓库、出库
     */
    @TableField("warehouse")
    private String warehouse;
    /**
     * 使用公司、进库
     */
    @TableField("usedcompid")
    private String usedcompid;
    /**
     * 使用部门、进库
     */
    @TableField("usedpartid")
    private String usedpartid;
    /**
     * 使用人、进库
     */
    @TableField("useduserid")
    private String useduserid;
    /**
     * 区域、进库
     */
    @TableField("inloc")
    private String inloc;
    /**
     * 仓库、进库
     */
    @TableField("inwarehouse")
    private String inwarehouse;
    @TableField("label1")
    private String label1;
    @TableField("label2")
    private String label2;
    @TableField("remark")
    private String remark;
    /**
     * 所属公司、出库
     */
    @TableField("belongcompid")
    private String belongcompid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getCnt() {
        return cnt;
    }

    public void setCnt(BigDecimal cnt) {
        this.cnt = cnt;
    }

    public String getZcsource() {
        return zcsource;
    }

    public void setZcsource(String zcsource) {
        this.zcsource = zcsource;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getBuytime() {
        return buytime;
    }

    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOperuserid() {
        return operuserid;
    }

    public void setOperuserid(String operuserid) {
        this.operuserid = operuserid;
    }

    public String getOperusername() {
        return operusername;
    }

    public void setOperusername(String operusername) {
        this.operusername = operusername;
    }

    public Date getBusidate() {
        return busidate;
    }

    public void setBusidate(Date busidate) {
        this.busidate = busidate;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }

    public String getCompid() {
        return compid;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getUsedcompid() {
        return usedcompid;
    }

    public void setUsedcompid(String usedcompid) {
        this.usedcompid = usedcompid;
    }

    public String getUsedpartid() {
        return usedpartid;
    }

    public void setUsedpartid(String usedpartid) {
        this.usedpartid = usedpartid;
    }

    public String getUseduserid() {
        return useduserid;
    }

    public void setUseduserid(String useduserid) {
        this.useduserid = useduserid;
    }

    public String getInloc() {
        return inloc;
    }

    public void setInloc(String inloc) {
        this.inloc = inloc;
    }

    public String getInwarehouse() {
        return inwarehouse;
    }

    public void setInwarehouse(String inwarehouse) {
        this.inwarehouse = inwarehouse;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBelongcompid() {
        return belongcompid;
    }

    public void setBelongcompid(String belongcompid) {
        this.belongcompid = belongcompid;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ResInout{" +
                "id=" + id +
                ", type=" + type +
                ", uuid=" + uuid +
                ", title=" + title +
                ", action=" + action +
                ", status=" + status +
                ", cnt=" + cnt +
                ", zcsource=" + zcsource +
                ", suppliername=" + suppliername +
                ", buytime=" + buytime +
                ", price=" + price +
                ", operuserid=" + operuserid +
                ", operusername=" + operusername +
                ", busidate=" + busidate +
                ", rdate=" + rdate +
                ", compid=" + compid +
                ", loc=" + loc +
                ", warehouse=" + warehouse +
                ", usedcompid=" + usedcompid +
                ", usedpartid=" + usedpartid +
                ", useduserid=" + useduserid +
                ", inloc=" + inloc +
                ", inwarehouse=" + inwarehouse +
                ", label1=" + label1 +
                ", label2=" + label2 +
                ", remark=" + remark +
                ", belongcompid=" + belongcompid +
                "}";
    }
}
