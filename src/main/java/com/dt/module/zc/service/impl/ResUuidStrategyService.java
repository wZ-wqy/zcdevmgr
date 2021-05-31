package com.dt.module.zc.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.service.IResUuidStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class ResUuidStrategyService  extends BaseService {


    @Autowired
    IResUuidStrategyService ResUuidStrategyServiceImpl;

    /**
     * @Description:获取当记录
     */
    private Rcd queryCurrentStrategyRecord() {
        //AS-{year}-{month}-{day}-{seq}-{random} assetsuuid
        return db.uniqueRecord("select * from res_uuid_strategy where dr='0' and def='1'");
    }
    /**
     * @Description:获取当前值
     */
    public String createCurrentUuid() {
        Rcd rs=queryCurrentStrategyRecord();
        String uuidrule="AS-{seq}-{random}";
        int seqno=4;
        if(rs!=null){
            uuidrule=rs.getString("uuidrule");
            seqno=rs.getInteger("seq");
        }
        Calendar cal = Calendar.getInstance();
        String year = cal.get(Calendar.YEAR)+"";
        String month= cal.get(Calendar.MONTH)+1<10?"0"+(cal.get(Calendar.MONTH)+1)+"":(cal.get(Calendar.MONTH)+1)+"";
        String day=cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE)+"":cal.get(Calendar.DATE)+"";
        String random= UUID.randomUUID().toString().substring(9, 13).toUpperCase();
        String seq="108";
        Rcd seqrs = db.uniqueRecord("select case when value is null then '108' else value end seq from sys_params where id='assetsuuid' and dr ='0'");
        if (seqrs!=null) {
            seq = (ConvertUtil.toInt(seqrs.getString("seq")) + 1) + "";
            Update me = new Update("sys_params");
            me.set("value", seq);
            me.where().and("id=?", "assetsuuid");
            db.execute(me);
        }
        return uuidrule.replace("{year}",year).replace("{month}",month).replace("{day}",day).replace("{random}",random).replace("{seq}",ConvertUtil.formatIntToString(seq, seqno, 108));
    }

}
