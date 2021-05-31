package com.dt.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dt.module.base.entity.SysRoleInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
public interface ISysRoleInfoService extends IService<SysRoleInfo> {
    Integer isUsed(String id);
}
