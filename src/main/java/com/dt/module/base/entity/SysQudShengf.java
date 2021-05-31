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
@TableName("SYS_QUD_SHENGF")
public class SysQudShengf extends BaseModel<SysQudShengf> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("MINGC")
    private String mingc;
    @TableField("JIANC")
    private String jianc;
    @TableField("GUOBM")
    private String guobm;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMingc() {
        return mingc;
    }

    public void setMingc(String mingc) {
        this.mingc = mingc;
    }

    public String getJianc() {
        return jianc;
    }

    public void setJianc(String jianc) {
        this.jianc = jianc;
    }

    public String getGuobm() {
        return guobm;
    }

    public void setGuobm(String guobm) {
        this.guobm = guobm;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysQudShengf{" +
                ", id=" + id +
                ", mingc=" + mingc +
                ", jianc=" + jianc +
                ", guobm=" + guobm +
                "}";
    }
}
