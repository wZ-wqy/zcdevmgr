package com.dt.module.cmdb.entity;

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
 * @since 2020-04-07
 */

@TableName("res_action_item")

public class ResActionItem extends BaseModel<ResActionItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busuuid")
    private String busuuid;
    @TableField("status")
    private String status;
    @TableField("backtime")
    private Date backtime;
    @TableField("resid")
    private String resid;
    @TableField("backtimestr")
    private String backtimestr;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBacktime() {
        return backtime;
    }

    public void setBacktime(Date backtime) {
        this.backtime = backtime;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getBacktimestr() {
        return backtimestr;
    }

    public void setBacktimestr(String backtimestr) {
        this.backtimestr = backtimestr;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResActionItem{" +
                "id=" + id +
                ", busuuid=" + busuuid +
                ", status=" + status +
                ", backtime=" + backtime +
                ", resid=" + resid +
                ", backtimestr=" + backtimestr +
                "}";
    }
}
