package com.dt.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.entity.UserShiro;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
public interface ISysUserInfoService extends IService<SysUserInfo> {

    List<SysMenus> listMyMenus(String user_id);

    R setDefaultUserMenus(String user_id, String id);

    R selectUserInfoByOpenId(String open_id);

    R modifyPassword(String user_id, String pwd);

    R addUser(SysUserInfo user);

    List<HashMap<String, Object>> listUserRoles(String user_id);

    UserShiro queryUserShiroByUserId(String user_id);

    R queryUserIdByUserName(String user_id);

    R queryUserReceivingaddr(String user_id);

    R deleteUserReceivingaddr(String user_id, String id);

    SysUserInfo selectOneByEmpl(String empl);

    R changeUserPwdForce(String user_id, String pwd1, String pwd2);
}
