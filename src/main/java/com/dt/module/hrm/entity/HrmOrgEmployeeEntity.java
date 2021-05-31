package com.dt.module.hrm.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSONObject;

@ExcelTarget("HrmOrgEmployeeEntity")
public class HrmOrgEmployeeEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

//    @Excel(name = "工号", width = 20)
//    private String emplid;

    @Excel(name = "姓名", width = 20)
    private String name;

    @Excel(name = "手机号", width = 20)
    private String tel;

    @Excel(name = "组织ID", width = 20)
    private String orgid;

    @Excel(name = "组织名称", width = 20)
    private String orgname;

    public void fullhrmOrgEmployeeEntity(JSONObject obj) {

//        this.emplid = obj.getString("emplid");
        this.name = obj.getString("name");
        this.tel = obj.getString("tel");
        this.orgname = obj.getString("orgname");
        this.orgid = obj.getString("orgid");

    }

    @Override
    public String toString() {
        return "HrmOrgEmployeeEntity{" +
//                "emplid=" + emplid +
                ", name=" + name +
                ", tel=" + tel +
                ", orgname=" + orgname +
                ", orgid=" + orgid +
                "}";
    }

//    public String getEmplid() {
//        return emplid;
//    }
//
//    public void setEmplid(String emplid) {
//        this.emplid = emplid;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
}
