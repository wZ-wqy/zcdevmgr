package com.dt.module.cmdb.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 *
 * </p>
 *
 * @author lank
 * @since 2020-11-14
 */

@TableName("res_label_tpl")

public class ResLabelTpl extends BaseModel<ResLabelTpl> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableField("id")
    private String id;
    /**
     * rwm|txm
     */
    @TableField("type")
    private String type;
    /**
     * 图片位置，up｜down
     */
    @TableField("picloc")
    private String picloc;
    /**
     * 字段顺序
     */
    @TableField("ctlcols")
    private String ctlcols;
    /**
     * 是否默认选中
     */
    @TableField("ifdef")
    private String ifdef;
    /**
     * 打印标签控制
     */
    @TableField("ctlcolsstr")
    private String ctlcolsstr;
    /**
     * 控制字段数值
     */
    @TableField("ctlvalue")
    private String ctlvalue;
    @TableField("conf")
    private String conf;
    @TableField("tplfileid")
    private String tplfileid;
    @TableField("name")
    private String name;
    @TableField("mark")
    private String mark;


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

    public String getPicloc() {
        return picloc;
    }

    public void setPicloc(String picloc) {
        this.picloc = picloc;
    }

    public String getCtlcols() {
        return ctlcols;
    }

    public void setCtlcols(String ctlcols) {
        this.ctlcols = ctlcols;
    }

    public String getIfdef() {
        return ifdef;
    }

    public void setIfdef(String ifdef) {
        this.ifdef = ifdef;
    }

    public String getCtlcolsstr() {
        return ctlcolsstr;
    }

    public void setCtlcolsstr(String ctlcolsstr) {
        this.ctlcolsstr = ctlcolsstr;
    }

    public String getCtlvalue() {
        return ctlvalue;
    }

    public void setCtlvalue(String ctlvalue) {
        this.ctlvalue = ctlvalue;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getTplfileid() {
        return tplfileid;
    }

    public void setTplfileid(String tplfileid) {
        this.tplfileid = tplfileid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ResLabelTpl{" +
                "id=" + id +
                ", type=" + type +
                ", picloc=" + picloc +
                ", ctlcols=" + ctlcols +
                ", ifdef=" + ifdef +
                ", ctlcolsstr=" + ctlcolsstr +
                ", ctlvalue=" + ctlvalue +
                ", conf=" + conf +
                ", tplfileid=" + tplfileid +
                ", name=" + name +
                ", mark=" + mark +
                "}";
    }
}
