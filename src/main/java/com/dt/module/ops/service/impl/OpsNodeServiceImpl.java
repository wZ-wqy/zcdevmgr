package com.dt.module.ops.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.module.ops.entity.OpsNode;
import com.dt.module.ops.mapper.OpsNodeMapper;
import com.dt.module.ops.service.IOpsNodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-03-14
 */
@Service
public class OpsNodeServiceImpl extends ServiceImpl<OpsNodeMapper, OpsNode> implements IOpsNodeService {

}
