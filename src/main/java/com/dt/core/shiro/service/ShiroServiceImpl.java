package com.dt.core.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.shiro.ShiroUser;
import com.dt.core.shiro.inter.IShiro;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.module.base.entity.SysModulesItem;
import com.dt.module.base.entity.UserShiro;
import com.dt.module.base.service.ISysModulesItemService;
import com.dt.module.base.service.ISysRoleInfoService;
import com.dt.module.db.DB;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements IShiro {

    @Autowired
    DB db;

    @Autowired
    ISysRoleInfoService SysRoleInfoServiceImpl;

    @Autowired
    ISysModulesItemService SysModulesItemServiceImpl;

    public static IShiro me() {
        return SpringContextUtil.getBean(IShiro.class);
    }

    @Override
    public UserShiro user(String account) {
        return new UserShiro();
    }

    @Override
    public ShiroUser shiroUser(UserShiro user) {
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(user.getUserId()); // 账号id
        shiroUser.setAccount(user.getAccount());// 账号
        shiroUser.setName(user.getName());
        shiroUser.setUsername(user.getUsername());
        List<String> roleList = new ArrayList<String>();
        List<String> roleNameList = new ArrayList<String>();
        // 角色集合
        HashMap<String, String> rmap = user.getRolsSet();
        Iterator<?> iter = rmap.entrySet().iterator();
        while (iter.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            roleList.add((String) key);
            roleNameList.add((String) val);
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);
        return shiroUser;
    }

    @Override
    public List<SysModulesItem> findPermissionsByRoleId(String roleId) {
        QueryWrapper<SysModulesItem> wrapper = new QueryWrapper<SysModulesItem>();
        return SysModulesItemServiceImpl.list(wrapper);
    }

    @Override
    public String findRoleNameByRoleId(String roleId) {
        return SysRoleInfoServiceImpl.getById(roleId).getRoleName();
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, UserShiro user, String realmName) {
        String credentials = user.getPassword();
        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        // 参数:用户名,数据库中密码,username+sale,realmName
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }
}
