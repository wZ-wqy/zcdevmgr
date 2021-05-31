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
import com.dt.module.zc.entity.ResCMaintenance;
import com.dt.module.zc.entity.ResCMaintenanceItem;
import com.dt.module.zc.service.IResCMaintenanceItemService;
import com.dt.module.zc.service.IResCMaintenanceService;
import com.dt.module.zc.service.impl.ResCMaintenanceService;
import com.dt.module.zc.service.impl.AssetsConstant;
import com.dt.module.zc.service.impl.AssetsOperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;

@Controller
@RequestMapping("/api/zc/resCMaintenance/ext")
public class ResCMaintenanceExtController extends BaseController {


    @Autowired
    IResCMaintenanceService ResCMaintenanceServiceImpl;

    @Autowired
    IResCMaintenanceItemService ResCMaintenanceItemServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    ResCMaintenanceService resCMaintenanceService;

    /**
     * @Description:生成单据
     */
    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(ResCMaintenance entity, String items) throws ParseException {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String uuid = assetsOperService.createUuid(AssetsConstant.ASSETS_BUS_TYPE_CGWB);
        entity.setStatus(FlowConstant.PSTATUS_FINISH_NO_APPROVAL);
        entity.setBusuuid(uuid);
        ArrayList<ResCMaintenanceItem> list = new ArrayList<ResCMaintenanceItem>();
        JSONArray items_arr = JSONArray.parseArray(items);
        for (int i = 0; i < items_arr.size(); i++) {
            ResCMaintenanceItem e = new ResCMaintenanceItem();
            e.setBusuuid(uuid);
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            e.setTwb(entity.getTwb());
            e.setTwbauto(entity.getTwbauto());
            e.setTwbct(entity.getTwbct());
            e.setTwboutdate(entity.getTwboutdate());
            e.setTwbsupplier(entity.getTwbsupplier());
            e.setTwbstatus(entity.getTwbstatus());
            e.setTwbsupplierstatus(entity.getTwbsupplierstatus());
            e.setTwbautostatus(entity.getTwbautostatus());
            e.setTwbctstatus(entity.getTwbctstatus());
            e.setTwboutdatestatus(entity.getTwboutdatestatus());
            list.add(e);
        }
        ResCMaintenanceServiceImpl.save(entity);
        ResCMaintenanceItemServiceImpl.saveBatch(list);
        resCMaintenanceService.confirm(uuid);
        JSONObject r = new JSONObject();
        r.put("busid", uuid);
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        String sql = "select t.*,  " +
                "(select name from sys_user_info where user_id=t.create_by) createusername," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=t.twb)twbstr," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=t.twbauto)twbautostr," +
                "date_format(twboutdate,'%Y-%m-%d') twboutdatestr,  " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=t.twbsupplier) twbsupplierstr  " +
                "from res_c_maintenance t where dr='0' ";
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
        return resCMaintenanceService.selectByBusid(uuid);
    }

}
