package com.dt.module.cmdb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.mapper.ResMapper;
import com.dt.module.cmdb.service.IResService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-08-16
 */
@Service
public class ResServiceImpl extends ServiceImpl<ResMapper, Res> implements IResService {

}
