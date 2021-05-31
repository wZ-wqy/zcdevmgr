package com.dt.module.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.module.base.entity.SysDictItem;
import com.dt.module.base.mapper.SysDictItemMapper;
import com.dt.module.base.service.ISysDictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {


    /**
     * @Description: 根据dictid查询数据字典项
     * @param dictId
     */
    @Override
    public List<SysDictItem> selectDictItemByDict(String dictId) {
        return this.baseMapper.selectDictItemByDict(dictId);
    }

}
