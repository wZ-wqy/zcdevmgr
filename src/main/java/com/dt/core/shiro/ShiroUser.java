package com.dt.core.shiro;

import java.io.Serializable;
import java.util.List;

public class ShiroUser implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id; // 主键ID

    public String account; // 账号

    public String name; // 姓名

    public String username;


    public List<String> roleList; // 角色集
    public List<String> roleNames; // 角色名称集

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "ShiroUser{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", roleList=" + roleList +
                ", roleNames=" + roleNames +
                '}';
    }
}
