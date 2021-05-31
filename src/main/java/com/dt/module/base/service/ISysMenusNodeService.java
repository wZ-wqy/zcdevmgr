package com.dt.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysMenusNode;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lank
 * @since 2018-07-27
 */
public interface ISysMenusNodeService extends IService<SysMenusNode> {

    R queryMenuNodesForStageSetting(String menu_id);

}
