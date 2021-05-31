package com.dt.module.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.module.base.entity.SysRoleInfo;
import com.dt.module.base.mapper.SysRoleInfoMapper;
import com.dt.module.base.service.ISysRoleInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
@Service
public class SysRoleInfoServiceImpl extends ServiceImpl<SysRoleInfoMapper, SysRoleInfo> implements ISysRoleInfoService {
    /**
     * @Description:是否使用
     * @param id
     */
    public Integer isUsed(String id) {
        return this.baseMapper.isUsed(id);
    }
}
