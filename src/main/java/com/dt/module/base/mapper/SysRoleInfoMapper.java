package com.dt.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dt.module.base.entity.SysRoleInfo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
public interface SysRoleInfoMapper extends BaseMapper<SysRoleInfo> {

    Integer isUsed(String id);
}
