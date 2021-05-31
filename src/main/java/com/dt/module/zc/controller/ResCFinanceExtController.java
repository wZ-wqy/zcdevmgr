package com.dt.module.zc.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResCFinance;
import com.dt.module.zc.entity.ResCFinanceItem;
import com.dt.module.zc.service.IResCFinanceItemService;
import com.dt.module.zc.service.IResCFinanceService;
import com.dt.module.zc.service.impl.ResCFinanceService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;


@Controller
@RequestMapping("/api/zc/resCFinance/ext")
public class ResCFinanceExtController extends BaseController {

    @Autowired
    IResCFinanceService ResCFinanceServiceImpl;

    @Autowired
    IResCFinanceItemService ResCFinanceItemServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;



    @Autowired
    ResCFinanceService resCFinanceService;



    /**
     * @Description:生成变更单据
     */
    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(ResCFinance entity, String items) {
        String uuid = assetsOperService.createUuid(AssetsConstant.UUID_CGCW);
        entity.setStatus(FlowConstant.PSTATUS_FINISH_NO_APPROVAL);
        entity.setBusuuid(uuid);
        ArrayList<ResCFinanceItem> list = new ArrayList<ResCFinanceItem>();
        JSONArray items_arr = JSONArray.parseArray(items);
        for (int i = 0; i < items_arr.size(); i++) {
            ResCFinanceItem e = new ResCFinanceItem();
            e.setBusuuid(uuid);
            e.setTbelongcomp(entity.getTbelongcomp());
            e.setTbelongpart(entity.getTbelongpart());
            e.setTbuyprice(entity.getTbuyprice());
            e.setTnetworth(entity.getTnetworth());
            e.setTresidualvalue(entity.getTresidualvalue());
            e.setTaccumulateddepreciation(entity.getTaccumulateddepreciation());
            e.setTbelongcompstatus(entity.getTbelongcompstatus());
            e.setTbelongpartstatus(entity.getTbelongpartstatus());
            e.setTbuypricestatus(entity.getTbuypricestatus());
            e.setTnetworthstatus(entity.getTnetworthstatus());
            e.setTresidualvaluestatus(entity.getTresidualvaluestatus());
            e.setTaccumulatedstatus(entity.getTaccumulatedstatus());
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            list.add(e);
        }
        ResCFinanceServiceImpl.save(entity);
        ResCFinanceItemServiceImpl.saveBatch(list);
        resCFinanceService.confirm(uuid);
        JSONObject r = new JSONObject();
        r.put("busid", uuid);
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询所有变更单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        String sql = "select   " +
                " (select name from sys_user_info where user_id=b.create_by) createusername," +
                "(select route_name from hrm_org_part where node_id=b.tbelongcomp) tbelongcompfullname,   " +
                "(select node_name from hrm_org_part where node_id=b.tbelongcomp) tbelongcompname,   " +
                "b.*   " +
                "from  res_c_finance b where dr='0' ";
        if(ToolUtil.isNotEmpty(search)){
            sql=sql+ " and busuuid like '%"+search+"%' ";
        }
        sql=sql+" order by create_time desc";
        RcdSet rs = db.query(sql);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }

    /**
     * @Description:根据业务ID查询单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByUuid.do")
    public R selectByUuid(String uuid) {
        return resCFinanceService.selectByBusid(uuid);
    }

}
