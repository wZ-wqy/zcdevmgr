package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 编号生成策略
 * </p>
 *
 * @author lank
 * @since 2021-03-07
 */
 
@TableName("res_uuid_strategy")
 
public class ResUuidStrategy extends BaseModel<ResUuidStrategy> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("seq")
    private BigDecimal seq;
    @TableField("split")
    private String split;
    @TableField("str1")
    private String str1;
    @TableField("def")
    private String def;
    @TableField("uuidrule")
    private String uuidrule;
    @TableField("ct")
    private String ct;


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

    public BigDecimal getSeq() {
        return seq;
    }

    public void setSeq(BigDecimal seq) {
        this.seq = seq;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getUuidrule() {
        return uuidrule;
    }

    public void setUuidrule(String uuidrule) {
        this.uuidrule = uuidrule;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResUuidStrategy{" +
        "id=" + id +
        ", name=" + name +
        ", seq=" + seq +
        ", split=" + split +
        ", str1=" + str1 +
        ", def=" + def +
        ", uuidrule=" + uuidrule +
        ", ct=" + ct +
        "}";
    }
}
