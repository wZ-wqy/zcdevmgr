package com.dt.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dt.module.base.entity.SysDictItem;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    List<SysDictItem> selectDictItemByDict(String dictId);
}
