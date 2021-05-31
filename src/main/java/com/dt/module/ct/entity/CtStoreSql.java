package com.dt.module.ct.entity;

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
 * @since 2018-07-30
 */
@TableName("CT_STORE_SQL")
public class CtStoreSql extends BaseModel<CtStoreSql> {

    private static final long serialVersionUID = 1L;

    @TableId("STORE_ID")
    private String storeId;
    @TableField("NAME")
    private String name;
    @TableField("CAT_ID")
    private String catId;
    @TableField("URI")
    private String uri;
    @TableField("URI_PARAMETER")
    private String uriParameter;
    @TableField("USER_ID")
    private String userId;
    @TableField("SQL")
    private String sql;
    @TableField("DB_ID")
    private String dbId;
    /**
     * public,user
     */
    @TableField("ACL")
    private String acl;
    @TableField("MARK")
    private String mark;
    @TableField("RETURN_TYPE")
    private String returnType;
    @TableField("IS_USED")
    private String isUsed;
    @TableField("ALIAS_ID")
    private String aliasId;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUriParameter() {
        return uriParameter;
    }

    public void setUriParameter(String uriParameter) {
        this.uriParameter = uriParameter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    @Override
    protected Serializable pkVal() {
        return this.storeId;
    }

    @Override
    public String toString() {
        return "CtStoreSql{" +
                ", storeId=" + storeId +
                ", name=" + name +
                ", catId=" + catId +
                ", uri=" + uri +
                ", uriParameter=" + uriParameter +
                ", userId=" + userId +
                ", sql=" + sql +
                ", dbId=" + dbId +
                ", acl=" + acl +
                ", mark=" + mark +
                ", returnType=" + returnType +
                ", isUsed=" + isUsed +
                ", aliasId=" + aliasId +
                "}";
    }
}
