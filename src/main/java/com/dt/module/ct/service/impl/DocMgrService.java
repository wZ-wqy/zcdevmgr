package com.dt.module.ct.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;

@Service
public class DocMgrService extends BaseService {


    /**
     * @Description:显示所有文档
     */
    public R listDoc(String search) {
        String sql="select t.*,(select name from sys_dict_item where dict_item_id=t.type)typestr from doc_mgr t where dr='0' ";
        if(ToolUtil.isNotEmpty(search)){
            sql=sql+" and t.name like '%"+search+"%' ";
        }
        sql=sql+" order by create_time desc";
        RcdSet rs = db.query(sql);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }
}
