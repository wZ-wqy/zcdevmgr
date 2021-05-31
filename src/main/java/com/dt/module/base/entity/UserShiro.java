package com.dt.module.base.entity;

import java.util.HashMap;

public class UserShiro {

    public String userId; // 主键ID

    public String account; // 账号

    public String name; // 姓名

    public String username; //

    public String password;

    public Boolean isLocked = true;

    public Boolean isAdmin = false;
    public HashMap<String, String> rolsSet;
    /**
     * md5密码盐
     */
    private String salt;

    // public List<String> roleNames; // 角色名称集

    public HashMap<String, String> getRolsSet() {
        return rolsSet;
    }

    public void setRolsSet(HashMap<String, String> rolsSet) {
        this.rolsSet = rolsSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserShiro{" +
                "userId='" + userId + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isLocked=" + isLocked +
                ", isAdmin=" + isAdmin +
                ", salt='" + salt + '\'' +
                ", rolsSet=" + rolsSet +
                '}';
    }
}
