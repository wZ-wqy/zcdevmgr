package com.dt.module.cmdb.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: lank
 * @date: Nov 3, 2019 3:09:21 PM
 * @Description:
 */
@ExcelTarget("DictItemEntity")
public class DictItemEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "名称", width = 20)
    private String name;

    @Excel(name = "选项", width = 20)
    private String item_name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the item_name
     */
    public String getItem_name() {
        return item_name;
    }

    /**
     * @param item_name the item_name to set
     */
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void fullResEntity(JSONObject obj) {

        this.name = obj.getString("name");
        this.item_name = obj.getString("item_name");
    }
}
