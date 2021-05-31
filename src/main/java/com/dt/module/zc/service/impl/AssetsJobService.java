package com.dt.module.zc.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.tool.lang.SpringContextUtil;
import org.springframework.stereotype.Service;

@Service
public class AssetsJobService extends BaseService {

    public static AssetsJobService me() {
        return SpringContextUtil.getBean(AssetsJobService.class);
    }

    public void checkMaintenanceMethod() {
        // 转脱保
        String sql1 = "update res set wb='invalid' where id in (select t.id from (select id  from res where wbout_date is not null and dr = 0 and (wb <> 'invalid' or wb is null) and wb_auto = '1' and wbout_date < now())t)";
        db.execute(sql1);
        // 转在保
        String sql2 ="update res set wb='valid' where id in (select t.id from (select id from res where wbout_date is not null and dr = 0 and (wb <> 'valid' or wb is null) and wb_auto = '1' and wbout_date > now())t)";
        db.execute(sql2);
    }

    public void checkAssetsPart() {
        String sql="update res t set part_id=(select a.node_id from hrm_org_employee a,hrm_org_part b where a.node_id=b.node_id and b.type='part' and a.user_id=t.used_userid) where dr='0' and used_userid is not null and used_userid<>'' and category in ('3') ";
        db.execute(sql);
    }

}
