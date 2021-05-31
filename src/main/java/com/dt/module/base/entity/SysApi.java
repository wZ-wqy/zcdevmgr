package com.dt.module.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2018-07-27
 */
@TableName("SYS_API")
public class SysApi extends BaseModel<SysApi> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("CT")
    private String ct;
    @TableField("CTACL")
    private String ctacl;
    @TableField("APITYPE")
    private String apitype;
    @TableField("RECTIME")
    private Date rectime;
    @TableField("MARK")
    private String mark;
    @TableField("INFO")
    private String info;
    @TableField("TYPE")
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getCtacl() {
        return ctacl;
    }

    public void setCtacl(String ctacl) {
        this.ctacl = ctacl;
    }

    public String getApitype() {
        return apitype;
    }

    public void setApitype(String apitype) {
        this.apitype = apitype;
    }

    public Date getRectime() {
        return rectime;
    }

    public void setRectime(Date rectime) {
        this.rectime = rectime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysApi{" +
                ", id=" + id +
                ", ct=" + ct +
                ", ctacl=" + ctacl +
                ", apitype=" + apitype +
                ", rectime=" + rectime +
                ", mark=" + mark +
                ", info=" + info +
                ", type=" + type +
                "}";
    }
}
