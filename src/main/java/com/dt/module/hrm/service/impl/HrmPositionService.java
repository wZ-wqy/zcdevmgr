package com.dt.module.hrm.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;

import javax.tools.Tool;

@Service
public class HrmPositionService extends BaseService {


    /**
     * @Description: 查询所有岗位数据
     */
    public R listPositions(String search) {
        String sql="select t.*,(select ptname from hrm_position_type where id=t.type) typestr from hrm_position t where dr='0' ";
        if(ToolUtil.isNotEmpty(search)){
            sql=sql+" and t.name like '%"+search+"%'";
        }
        RcdSet rs = db.query(sql);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }

}
