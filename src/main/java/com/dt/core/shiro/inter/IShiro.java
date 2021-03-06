package com.dt.core.shiro.inter;

import com.dt.core.shiro.ShiroUser;
import com.dt.module.base.entity.SysModulesItem;
import com.dt.module.base.entity.UserShiro;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import java.util.List;

/**
 * 定义shirorealm所需数据的接口
 */
public interface IShiro {

    /**
     * 根据账号获取登录用户
     *
     * @param account 账号
     */
    UserShiro user(String account);

    /**
     * 根据系统用户获取Shiro的用户
     *
     * @param user 系统用户
     */
    ShiroUser shiroUser(UserShiro user);

    /**
     * 获取权限列表通过角色id
     *
     * @param roleId 角色id
     */
    List<SysModulesItem> findPermissionsByRoleId(String roleId);

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     */
    String findRoleNameByRoleId(String roleId);

    /**
     * 获取shiro的认证信息
     */
    SimpleAuthenticationInfo info(ShiroUser shiroUser, UserShiro user, String realmName);

}
