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
import com.dt.module.zc.entity.ResCBasicinformation;
import com.dt.module.zc.entity.ResCBasicinformationItem;
import com.dt.module.zc.service.IResCBasicinformationItemService;
import com.dt.module.zc.service.IResCBasicinformationService;
import com.dt.module.zc.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.ParseException;
import java.util.ArrayList;

@Controller
@RequestMapping("/api/zc/resCBasicinformation/ext")
public class ResCBasicinformationExtController extends BaseController {


    @Autowired
    AssetsOperService assetsOperService;


    @Autowired
    IResCBasicinformationService ResCBasicinformationServiceImpl;


    @Autowired
    IResCBasicinformationItemService ResCBasicinformationItemServiceImpl;

    @Autowired
    ResCBasicinformationService resCBasicinformationService;


    /**
     * @Description:生成变更单据
     */
    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(ResCBasicinformation entity, String items) throws ParseException {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String uuid = assetsOperService.createUuid(AssetsConstant.ASSETS_BUS_TYPE_CGJB);
        entity.setStatus(FlowConstant.PSTATUS_FINISH_NO_APPROVAL);
        entity.setBusuuid(uuid);
        ArrayList<ResCBasicinformationItem> list = new ArrayList<ResCBasicinformationItem>();
        JSONArray items_arr = JSONArray.parseArray(items);
        for (int i = 0; i < items_arr.size(); i++) {
            ResCBasicinformationItem e = new ResCBasicinformationItem();
            e.setBusuuid(uuid);
            e.setResid(items_arr.getJSONObject(i).getString("id"));
            e.setTbrand(entity.getTbrand());
            e.setTbuytime(entity.getTbuytime());
            e.setTclassid(entity.getTclassid());
            e.setTloc(entity.getTloc());
            e.setTmodel(entity.getTmodel());
            e.setTpartid(entity.getTpartid());
            e.setTsn(entity.getTsn());
            e.setTsupplier(entity.getTsupplier());
            e.setTusedcompanyid(entity.getTusedcompanyid());
            e.setTusefullife(entity.getTusefullife());
            e.setTuseduserid(entity.getTuseduserid());
            e.setTzccnt(entity.getTzccnt());
            e.setTzcsource(entity.getTzcsource());
            e.setTlabel1(entity.getTlabel1());
            e.setTunit(entity.getTunit());
            e.setTconfdesc(entity.getTconfdesc());
            e.setTlocdtl(entity.getTlocdtl());
            e.setTclassidstatus(entity.getTclassidstatus());
            e.setTmodelstatus(entity.getTmodelstatus());
            e.setTsnstatus(entity.getTsnstatus());
            e.setTzcsourcestatus(entity.getTzcsourcestatus());
            e.setTzccntstatus(entity.getTzccntstatus());
            e.setTsupplierstatus(entity.getTsupplierstatus());
            e.setTbrandstatus(entity.getTbrandstatus());
            e.setTbuytimestatus(entity.getTbuytimestatus());
            e.setTlocstatus(entity.getTlocstatus());
            e.setTusefullifestatus(entity.getTusefullifestatus());
            e.setTusedcompanyidstatus(entity.getTusedcompanyidstatus());
            e.setTpartidstatus(entity.getTpartidstatus());
            e.setTuseduseridstatus(entity.getTuseduseridstatus());
            e.setTlabel1status(entity.getTlabel1status());
            e.setTlocdtlstatus(entity.getTlocdtlstatus());
            e.setTunitstatus(entity.getTunitstatus());
            e.setTconfdescstatus(entity.getTconfdescstatus());
            e.setTmarkstatus(entity.getTmarkstatus());
            e.setTfd1status(entity.getTfd1status());
            e.setTfs20status(entity.getTfs20status());
            e.setTfd1(entity.getTfd1());
            e.setTmark(entity.getTmark());
            e.setTfs20(entity.getTfs20());
            e.setTnamestatus(entity.getTnamestatus());
            e.setTname(entity.getTname());
            e.setTlabel2(entity.getTlabel2());
            e.setTlabel2status(entity.getTlabel2status());
            e.setTbatch(entity.getTbatch());
            e.setTbatchstatus(entity.getTbatchstatus());
            e.setTbelongcomp(entity.getTbelongcomp());
            e.setTbelongcompstatus(entity.getTbelongcompstatus());
           list.add(e);
        }
        ResCBasicinformationServiceImpl.save(entity);
        ResCBasicinformationItemServiceImpl.saveBatch(list);
        resCBasicinformationService.confirm(uuid);
        JSONObject r = new JSONObject();
        r.put("busid", uuid);
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询变更单据
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        String sql = "select   " +
                "(select name from sys_user_info where user_id=b.create_by) createusername," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tzcsource) tzcsourcestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tsupplier) tsupplierstr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tusefullife) tusefullifestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tloc) tlocstr,   " +
                "(select node_name from hrm_org_part where node_id=b.tpartid) tpartname," +
                "(select node_name from hrm_org_part where node_id=b.tbelongcomp) tbelongcompanyname," +
                "(select name from sys_user_info where user_id=b.tuseduserid) tusedusername," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tbrand) tbrandstr,   " +
                "(select route_name from ct_category where dr='0' and id=b.tclassid) tclassfullname,   " +
                "date_format(tfd1,'%Y-%m-%d') tfd1str,   " +
                "date_format(tbuytime,'%Y-%m-%d') tbuytimestr,b.*    " +
                "from res_c_basicinformation b where dr='0' ";
        if(ToolUtil.isNotEmpty(search)){
            sql=sql+ " and busuuid like '%"+search+"%' ";
        }
        sql=sql+" order by create_time desc";
        RcdSet rs = db.query(sql);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }

    /**
     * @Description:根据业务ID查询变更
     */
    @ResponseBody
    @Acl(info = "查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByUuid.do")
    public R selectByUuid(String uuid) {
        return resCBasicinformationService.selectByBusid(uuid);
    }
}
