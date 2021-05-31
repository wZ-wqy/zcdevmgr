package com.dt.core.shiro;

import com.dt.core.shiro.inter.IShiro;
import com.dt.core.shiro.service.ShiroServiceImpl;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysModulesItem;
import com.dt.module.base.entity.UserShiro;
import com.dt.module.base.service.ISysUserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroDbRealm extends AuthorizingRealm {

    private static Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    /**
     * 提供账户信息返回认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {

        IShiro shiroService = ShiroServiceImpl.me();
        // authcToken 中储存着输入的用户名和密码
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        log.info("cache:" + this.getAuthenticationCacheName());
        log.info("###################Action 登录认证#################");
        log.info("userid:" + token.getUsername());
        // 从数据库中获取密码
        UserShiro user = SysUserInfoServiceImpl.queryUserShiroByUserId(token.getUsername());
        if (ToolUtil.isEmpty(user.userId)) {
            throw new UnknownAccountException();//// 没找到帐号
        }

        if (user.getIsLocked()) {
            throw new LockedAccountException(); // 帐号锁定
        }

        ShiroUser shiroUser = shiroService.shiroUser(user);
        // 进行对比,成功返回info,shiroUser
        SimpleAuthenticationInfo info = shiroService.info(shiroUser, user, super.getName());
        return info;
    }

    /**
     * 提供用户信息返回权限信息,SecurityUtils.getSubject().isPermitted（）时调用,
     * 一般@RequiresPermissions会调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        this.clearCachedAuthenticationInfo(principals);
        log.info("cache:" + this.getAuthorizationCacheName());
        log.info("###################Action 权限认证#################");
        IShiro shiroService = ShiroServiceImpl.me();
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        List<String> roleList = shiroUser.getRoleList();
        Set<String> permissionSet = new HashSet<String>();
        Set<String> roleNameSet = new HashSet<String>();
        // 处理每个角色的权限
        if (roleList.size() > 0) {
            for (String roleId : roleList) {
                log.info("Role Id:" + roleId);
                List<SysModulesItem> permissions = shiroService.findPermissionsByRoleId(roleId);
                if (permissions != null) {
                    for (SysModulesItem permission : permissions) {
                        if (ToolUtil.isNotEmpty(permission)) {
                            permissionSet.add(permission.getCt());
                        }
                    }
                }
                String roleName = shiroService.findRoleNameByRoleId(roleId);
                roleNameSet.add(roleName);
            }
        } else {
            log.info("no role get.");
        }
        // 将权限名称提供给info
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionSet);
        info.addRoles(roleNameSet);
        return info;
    }
}
