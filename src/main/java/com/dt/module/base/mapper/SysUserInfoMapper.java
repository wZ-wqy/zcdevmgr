package com.dt.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysUserInfo;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {
    //查询某个用户的菜单
    List<SysMenus> listMyMenus(String user_id);

    List<HashMap<String, Object>> listUserRoles(String user_id);
}
