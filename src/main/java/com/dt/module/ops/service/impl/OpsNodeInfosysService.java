package com.dt.module.ops.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-03-06
 */
@Service
public class OpsNodeInfosysService extends BaseService {

    public static String sql = "select  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.type and dict_id = 'sysinfotype' ) typestr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.opsmethod and dict_id = 'sysinfoops' )   opsmethodstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.devmethod and dict_id = 'sysinfodev' ) devmethodstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.grade and dict_id = 'sysinfograde' ) gradestr,  "
            + "t.* from ops_node_infosys t where dr=0 ";



    /**
     * @Description:搜索Ops数据
     * @param search
     */
    public R selectList(String search) {
        String sql = OpsNodeInfosysService.sql;
        if (ToolUtil.isNotEmpty(search)) {
            sql=sql+" and t.name like '%"+search+"%'";
        }
        sql = sql + " order by name";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }


}
