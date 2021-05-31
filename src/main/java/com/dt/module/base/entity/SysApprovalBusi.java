package com.dt.module.base.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author lank
 * @since 2020-12-28
 */
 
@TableName("sys_approval_busi")
 
public class SysApprovalBusi extends BaseModel<SysApprovalBusi> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("code")
    private String code;
    @TableField("name")
    private String name;
    @TableField("webapproval")
    private String webapproval;
    @TableField("mobileapproval")
    private String mobileapproval;
    @TableField("mark")
    private String mark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebapproval() {
        return webapproval;
    }

    public void setWebapproval(String webapproval) {
        this.webapproval = webapproval;
    }

    public String getMobileapproval() {
        return mobileapproval;
    }

    public void setMobileapproval(String mobileapproval) {
        this.mobileapproval = mobileapproval;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysApprovalBusi{" +
        "id=" + id +
        ", code=" + code +
        ", name=" + name +
        ", webapproval=" + webapproval +
        ", mobileapproval=" + mobileapproval +
        ", mark=" + mark +
        "}";
    }
}
