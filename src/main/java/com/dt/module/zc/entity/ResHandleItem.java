package com.dt.module.zc.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 处置明细
 * </p>
 *
 * @author lank
 * @since 2021-02-08
 */
 
@TableName("res_handle_item")
 
public class ResHandleItem extends BaseModel<ResHandleItem> {

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
     * 资产ID
     */
    @TableField("resid")
    private String resid;
    /**
     * 备注
     */
    @TableField("mark")
    private String mark;


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

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
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
        return "ResHandleItem{" +
        "id=" + id +
        ", busuuid=" + busuuid +
        ", resid=" + resid +
        ", mark=" + mark +
        "}";
    }
}
