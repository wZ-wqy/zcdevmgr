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
 * 处置
 * </p>
 *
 * @author lank
 * @since 2021-02-08
 */
 
@TableName("res_handle")
 
public class ResHandle extends BaseModel<ResHandle> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    /**
     * 单据
     */
    @TableField("busuuid")
    private String busuuid;
    /**
     * 流程ID
     */
    @TableField("pinst")
    private String pinst;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 处理人ID
     */
    @TableField("processuserid")
    private String processuserid;
    /**
     * 处理人
     */
    @TableField("processusername")
    private String processusername;
    /**
     * 处理时间
     */
    @TableField("processdate")
    private Date processdate;
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 类型名称
     */
    @TableField("typename")
    private String typename;
    /**
     * 状态
     */
    @TableField("status")
    private String status;
    /**
     * 内容
     */
    @TableField("ct")
    private String ct;
    /**
     * 处理时间
     */
    @TableField("busidate")
    private Date busidate;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;
    /**
     * 数量
     */
    @TableField("cnt")
    private BigDecimal cnt;


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

    public String getPinst() {
        return pinst;
    }

    public void setPinst(String pinst) {
        this.pinst = pinst;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getProcessdate() {
        return processdate;
    }

    public void setProcessdate(Date processdate) {
        this.processdate = processdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public Date getBusidate() {
        return busidate;
    }

    public void setBusidate(Date busidate) {
        this.busidate = busidate;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public BigDecimal getCnt() {
        return cnt;
    }

    public void setCnt(BigDecimal cnt) {
        this.cnt = cnt;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResHandle{" +
        "id=" + id +
        ", busuuid=" + busuuid +
        ", pinst=" + pinst +
        ", title=" + title +
        ", processuserid=" + processuserid +
        ", processusername=" + processusername +
        ", processdate=" + processdate +
        ", type=" + type +
        ", typename=" + typename +
        ", status=" + status +
        ", ct=" + ct +
        ", busidate=" + busidate +
        ", mark=" + mark +
        ", cnt=" + cnt +
        "}";
    }
}
