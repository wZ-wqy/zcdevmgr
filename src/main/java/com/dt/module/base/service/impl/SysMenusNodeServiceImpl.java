package com.dt.module.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysMenusNode;
import com.dt.module.base.mapper.SysMenusNodeMapper;
import com.dt.module.base.service.ISysMenusNodeService;
import com.dt.module.db.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2018-07-27
 */
@Service
public class SysMenusNodeServiceImpl extends ServiceImpl<SysMenusNodeMapper, SysMenusNode>
        implements ISysMenusNodeService {

    @Autowired
    DB db;

    /**
     * @Description:根据菜单查询节点
     * @param menu_id
     */
    public R queryMenuNodesForStageSetting(String menu_id) {
        String sql = "select case is_g_show when 'Y' then '显示' when 'N' then '隐藏' else '未知' end is_g_show_text,(select count(1) from sys_modules_item where module_id=node_id) acl_cnt,a.*,case type when 'dir' then '目录' when 'menu' then '菜单' when 'btn' then '按钮' else '未知' end typetext from sys_menus_node a where menu_id=? and dr='0' order by node_id";
        return R.SUCCESS_OPER(db.query(sql, menu_id).toJsonArrayWithJsonObject());
    }

}
