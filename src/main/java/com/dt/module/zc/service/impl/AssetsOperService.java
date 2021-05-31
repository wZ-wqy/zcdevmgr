package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import com.dt.module.base.service.impl.SysUserInfoService;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.entity.ResActionItem;
import com.dt.module.cmdb.service.IResActionItemService;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.ct.entity.CtCategory;
import com.dt.module.ct.service.ICtCategoryService;
import com.dt.module.zc.entity.ResAllocate;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.service.IResAllocateService;
import com.dt.module.zc.service.IResChangeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Service
public class AssetsOperService extends BaseService {


    @Value("${app.assets_data_pri}")
    private String appAssetsDataPri;

    @Autowired
    ResUuidStrategyService resUuidStrategyService;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResActionItemService ResActionItemServiceImpl;

    @Autowired
    IResAllocateService ResAllocateServiceImpl;

    @Autowired
    ICtCategoryService CtCategoryServiceImpl;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    SysUserInfoService sysUserInfoService;

    public R queryZcColCtlShow() {
        JSONArray res = new JSONArray();
        Rcd rs1 = db.uniqueRecord("select * from sys_params where id='zccolctl'");
        Rcd rs2 = db.uniqueRecord("select * from sys_params where id='zccolctlcommon'");
        if (rs1 == null) {
            Insert ins1 = new Insert("sys_params");
            ins1.set("dr", "0");
            ins1.set("type", "system");
            ins1.set("id", "zccolctl");
            ins1.set("name", "前端入库资产列显示");
            ins1.set("value", "{}");
            db.execute(ins1);
            rs1 = db.uniqueRecord("select * from sys_params where id='zccolctl'");
        }
        if (rs2 == null) {
            Insert ins2 = new Insert("sys_params");
            ins2.set("dr", "0");
            ins2.set("type", "system");
            ins2.set("id", "zccolctlcommon");
            ins2.set("name", "前端常用资产列显示");
            ins2.set("value", "{}");
            db.execute(ins2);
            rs2 = db.uniqueRecord("select * from sys_params where id='zccolctlcommon'");
        }
        String rs1ct = rs1.getString("value");
        JSONObject rs1obj = JSONObject.parseObject(rs1ct);
        rs1obj.put("zccolparname", rs1.getString("name"));
        rs1obj.put("zccolparid", rs1.getString("id"));
        res.add(rs1obj);

        String rs2ct = rs2.getString("value");
        JSONObject rs2obj = JSONObject.parseObject(rs2ct);
        rs2obj.put("zccolparname", rs2.getString("name"));
        rs2obj.put("zccolparid", rs2.getString("id"));
        res.add(rs2obj);
        return R.SUCCESS_OPER(res);
    }

    /**
     * @Description:查询数据字典表
     */
    public R queryDictFast(String uid, String zchccat, String comppart, String comp, String belongcomp, String dicts, String parts, String partusers, String classid, String classroot, String zccatused) {

        JSONObject res = new JSONObject();
        String[] dict_arr = dicts.split(",");
        for (int i = 0; i < dict_arr.length; i++) {
            String sql = "select t.*,t.dict_item_id value,t.name label from sys_dict_item t where dict_id=? and dr='0' order by sort";
            String cls = dict_arr[i];
            if ("zcother".equals(dict_arr[i])) {
                sql = "select t.*,t.dict_item_id value,t.name label from sys_dict_item t where dict_id=? and dr='0' and code<>'menu' order by sort";
                cls = "devclass";
            }
            RcdSet rs = db.query(sql, cls);
            res.put(dict_arr[i], ConvertUtil.OtherJSONObjectToFastJSONArray(rs.toJsonArrayWithJsonObject()));
        }

        if (ToolUtil.isNotEmpty(classid)) {
            RcdSet partrs = db.query(
                    "select id dict_item_id,name,id value,name label from ct_category where dr='0' and parent_id=? and type='goods' order by od", classid);
            res.put("btype", ConvertUtil.OtherJSONObjectToFastJSONArray(partrs.toJsonArrayWithJsonObject()));
        }

        if (ToolUtil.isNotEmpty(classroot)) {
            String subsql = " t.type='goods' and isaction='Y' and t.dr='0' and t.root=? and t.node_level>1 ";
            RcdSet partrs = db.query("select id dict_item_id,route_name name,name sname,id value,route_name label from ct_category t where  "
                    + subsql + " order by route", classroot);
            res.put("btype", ConvertUtil.OtherJSONObjectToFastJSONArray(partrs.toJsonArrayWithJsonObject()));
        }

        // 所有用户
        if (ToolUtil.isNotEmpty(partusers) && "Y".equals(partusers)) {
            RcdSet partuserrs = db
                    .query("select a.user_id,a.name,a.user_id value,a.name label from sys_user_info a,hrm_org_employee b ,hrm_org_part c where   "
                            + "  a.islogoff='0' and a.empl_id=b.empl_id and a.dr='0' and b.dr='0' and c.node_id=b.node_id");
            res.put("partusers", ConvertUtil.OtherJSONObjectToFastJSONArray(partuserrs.toJsonArrayWithJsonObject()));
        }

        RcdSet comprs = db.query("select node_id id, route_name name,node_id value,route_name label from hrm_org_part where dr='0' and type='comp' order by node_id");

        if (ToolUtil.isNotEmpty(comp) && "Y".equals(comp)) {
            res.put("comp", ConvertUtil.OtherJSONObjectToFastJSONArray(comprs.toJsonArrayWithJsonObject()));
        }

        if (ToolUtil.isNotEmpty(belongcomp) && "Y".equals(belongcomp)) {
            res.put("belongcomp", ConvertUtil.OtherJSONObjectToFastJSONArray(comprs.toJsonArrayWithJsonObject()));
        }

        //所有部门
        if (ToolUtil.isNotEmpty(comppart) && "Y".equals(comppart)) {
            JSONObject tmp = new JSONObject();
            for (int i = 0; i < comprs.size(); i++) {
                RcdSet partrs = db
                        .query("select node_id partid,route_name name, node_id value,route_name label from hrm_org_part where org_id=1 and dr='0' and parent_id=? order by route", comprs.getRcd(i).getString("id"));
                tmp.put(comprs.getRcd(i).getString("id"), ConvertUtil.OtherJSONObjectToFastJSONArray(partrs.toJsonArrayWithJsonObject()));
            }
            res.put("comppart", tmp);
        }

        if (ToolUtil.isNotEmpty(parts) && "Y".equals(parts)) {
            RcdSet partrs = db
                    .query("select node_id partid,route_name name , node_id value, route_name label from hrm_org_part where org_id=1 and dr='0' and type='part' order by route");
            res.put("parts", ConvertUtil.OtherJSONObjectToFastJSONArray(partrs.toJsonArrayWithJsonObject()));
        }

        if (ToolUtil.isNotEmpty(zccatused) && "Y".equals(zccatused)) {
            RcdSet partrs = db
                    .query("select a.id,concat(b.name,'/',a.route_name) name,a.id value,concat(b.name,'/',a.route_name) label from ct_category a,ct_category_root b where a.type='goods' and a.root=b.id and a.dr='0' and a.id in (select distinct class_id from res where dr='0')   " +
                            "order by a.root ,a.route_name");
            res.put("zccatused", ConvertUtil.OtherJSONObjectToFastJSONArray(partrs.toJsonArrayWithJsonObject()));
        }

        if (ToolUtil.isNotEmpty(zchccat) && "Y".equals(zchccat)) {
            RcdSet partrs = db
                    .query("select * from ct_category where root='" + CategoryEnum.CATEGORY_HC.getValue() + "' and dr='0' and type='goods'");
            res.put("zchccat", ConvertUtil.OtherJSONObjectToFastJSONArray(partrs.toJsonArrayWithJsonObject()));
        }

        return R.SUCCESS_OPER(res);
    }


    /**
     * @Description:生成5位随机数字
     */
    private String createUuid5() {
        return UUID.randomUUID().toString().substring(9, 23).toUpperCase();
    }

    /**
     * @param type
     * @Description:根据类型生成随机数字
     */
    public String createUuid(String type) {
        int cnt = 5;
        String id = createUuid5();
        int i = 0;
        if (type.equals(AssetsConstant.UUID_ZC)) {
           return resUuidStrategyService.createCurrentUuid();
        } else if (type.equals(AssetsConstant.UUID_BX)) {
            for (i = 0; i < cnt; i++) {
                QueryWrapper<ResActionItem> ew = new QueryWrapper<ResActionItem>();
                String finalId = type + id;
                ew.and(j -> j.eq("busuuid", finalId));
                ResActionItem rs = ResActionItemServiceImpl.getOne(ew);
                if (rs == null) {
                    break;
                } else {
                    id = createUuid5();
                }
            }
            if (i > cnt - 1) {
                return "";
            } else {
                return type + id;
            }
        } else if (type.equals(AssetsConstant.UUID_DB)) {
            for (i = 0; i < cnt; i++) {
                QueryWrapper<ResAllocate> ew = new QueryWrapper<ResAllocate>();
                String finalId = type + id;
                ew.and(j -> j.eq("uuid", finalId));
                ResAllocate rs = ResAllocateServiceImpl.getOne(ew);
                if (rs == null) {
                    break;
                } else {
                    id = createUuid5();
                }
            }
            if (i > cnt - 1) {
                return "";
            } else {
                return type + id;
            }
        } else if (type.equals(AssetsConstant.UUID_CGCW)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_CGWB)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_HANDLE)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_CGJB)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_HCRK)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_HCDB)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_HCCK)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_BF)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_ZJ)) {
            id = createUuid5();
            return type + id;
        } else if (type.equals(AssetsConstant.UUID_LY) || type.equals(AssetsConstant.UUID_TK) || type.equals(AssetsConstant.UUID_JY)
                || type.equals(AssetsConstant.UUID_ZY) || type.equals(AssetsConstant.UUID_PURCHASE) || type.equals(AssetsConstant.UUID_XJ)) {
            for (i = 0; i < cnt; i++) {
                QueryWrapper<ResActionItem> ew = new QueryWrapper<ResActionItem>();
                String finalId = type + id;
                ew.and(j -> j.eq("busuuid", finalId));
                ResActionItem rs = ResActionItemServiceImpl.getOne(ew);
                if (rs == null) {
                    break;
                } else {
                    id = createUuid5();
                }
            }
            if (i > cnt - 1) {
                return "";
            } else {
                return type + id;
            }
        }
        return "";
    }

    /**
     * @param datarange
     * @Description:根据范围类型查询资产的范围
     */
    private String buildAssetsDataRange(String datarange) {
        //idle,inuse,scrap,borrow,repair,stopuse,allocation
        String sql = "";
        if (AssetsConstant.ASSETS_BUS_TYPE_BX.equals(datarange)) {
            //维修:闲置,在用
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "','" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_LY.equals(datarange)) {
            //领用:闲置
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_TK.equals(datarange)) {
            //退库:在用
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_JY.equals(datarange)) {
            //借用:闲置,在用
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "','" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_DB.equals(datarange)) {
            //调拨:闲置
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_BF.equals(datarange)) {
            //报废:闲置,在用
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "','" + AssetsRecycleEnum.RECYCLE_INUSE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_ZJ.equals(datarange)) {
            //折旧:不选报废
            sql = sql + " and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle<>'" + AssetsRecycleEnum.RECYCLE_SCRAP.getValue() + "'";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_CG.equals(datarange) || AssetsConstant.ASSETS_BUS_TYPE_CGCW.equals(datarange) || AssetsConstant.ASSETS_BUS_TYPE_CGJB.equals(datarange) || AssetsConstant.ASSETS_BUS_TYPE_CGWB.equals(datarange)) {
            //变更:不选报废
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle<>'" + AssetsRecycleEnum.RECYCLE_SCRAP.getValue() + "'";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_XJ.equals(datarange)) {
            //巡检
            sql = sql + " and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' ";
        }  else if (AssetsConstant.ASSETS_BUS_TYPE_ZY.equals(datarange)) {
            //资产转移
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' and recycle in ('"+ AssetsRecycleEnum.RECYCLE_INUSE.getValue()+"','" + AssetsRecycleEnum.RECYCLE_IDLE.getValue() + "','" + AssetsRecycleEnum.RECYCLE_STOPUSE.getValue() + "')";
        } else if (AssetsConstant.ASSETS_BUS_TYPE_MYZY.equals(datarange)) {
            //我的资产转移
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' ";
            sql = sql + " and used_userid= '" + this.getUserId() + "'";
        }else if (AssetsConstant.ASSETS_BUS_TYPE_HANDLE.equals(datarange)) {
            //我的资产转移
            sql = sql + " and inprocess='0' and category='" + CategoryEnum.CATEGORY_ASSETS.getValue() + "' ";
        }
        return sql;
    }


    /**
     * @param ps
     * @Description:生成查询资产sql
     */
    public String buildQueryAssetsDataSql(TypedHashMap<String, Object> ps) {
        String belongcomp = ps.getString("belongcomp");
        String comp = ps.getString("comp");
        String part = ps.getString("part");
        String datarange = ps.getString("datarange");
        String classroot = ps.getString("classroot");
        String class_id = ps.getString("class_id");
        String wb = ps.getString("wb");
        String env = ps.getString("env");
        String recycle = ps.getString("recycle");
        String loc = ps.getString("loc");
        String search = ps.getString("search");
        String class_id_parents = ps.getString("class_id_parents");
        String part_parents = ps.getString("part_parents");
        String ids = ps.getString("ids");
        String warehouse = ps.getString("warehouse");
        String zcnumber = ps.getString("zcnumber");
        String category = ps.getString("category");
        String uuid = ps.getString("uuid");
        String rack = ps.getString("rack");
        String ishandle= ps.getString("ishandle");
        String used_userid = ps.getString("used_userid");
        String attrsql = "select * from res_attrs where catid=? and dr='0'";
        RcdSet attrs_rs = db.query(attrsql, class_id);
        String sql = "select";
        //扩展属性
        if (attrs_rs != null) {
            for (int i = 0; i < attrs_rs.size(); i++) {
                // 拼接sql
                String valsql = "";
                if (attrs_rs.getRcd(i).getString("inputtype").equals("inputint")) {
                    // valsql = " cast( attrvalue as signed integer)";
                    valsql = " attrvalue+0";
                } else if (attrs_rs.getRcd(i).getString("inputtype").equals("inputstr")) {
                    valsql = "attrvalue";
                } else {
                    valsql = "attrvalue";
                }
                sql = sql + " (select " + valsql
                        + " from res_attr_value i where i.dr=0 and i.resid=t.id and i.attrid='"
                        + attrs_rs.getRcd(i).getString("id") + "') \"" + attrs_rs.getRcd(i).getString("attrcode")
                        + "\",";
            }
        }
        sql = sql + AssetsConstant.resSqlbody + " t.* from res t where dr=0 ";
        //使用部门组织以下全部数据
        if (ToolUtil.isNotEmpty(part_parents)) {
            sql = sql + " and part_id in (select node_id from hrm_org_part where dr='0' and concat('-',route) like '%-"+part_parents+"-%' or node_id="+part_parents+")";
        }

        //添加数据权限
        if(ToolUtil.isNotEmpty(appAssetsDataPri)&&"1".equals(appAssetsDataPri)){
            String luserid=this.getUserId();
            if(ToolUtil.isNotEmpty(luserid)){
                String lnode=sysUserInfoService.queryUserNode(luserid);
                if(ToolUtil.isNotEmpty(lnode)){
                    sql = sql + " and part_id in (select node_id from hrm_org_part where dr='0' and concat('-',route) like '%-"+lnode+"-%' or node_id="+lnode+")";
                }
            }
        }
        //获取多个类型
        if (ToolUtil.isNotEmpty(classroot)) {
            sql = sql + " and class_id in (select id from ct_category t where t.dr='0' and t.root='" + classroot + "' and t.node_level>1)";
        }
        //获取分类以下全部数据,按照分类取数
        if (ToolUtil.isNotEmptyWithAll(class_id_parents)) {
            sql = sql + " and class_id in (select id from ct_category where dr='0' and concat('-',route) like '%-"+class_id_parents+"-%' or id="+class_id_parents+")";
        }
        //类别
        if (ToolUtil.isNotEmptyWithAll(class_id)) {
            sql = sql + " and class_id in (select id from ct_category  where dr='0' and (id='" + class_id + "' or parent_id='" + class_id + "')) ";
        }
        //区域
        if (ToolUtil.isNotEmptyWithAll(loc)) {
            sql = sql + " and loc='" + loc + "'";
        }
        //环境
        if (ToolUtil.isNotEmptyWithAll(env)) {
            sql = sql + " and env='" + env + "'";
        }
        //维保
        if (ToolUtil.isNotEmptyWithAll(wb)) {
            sql = sql + " and wb='" + wb + "'";
        }
        //状态
        if (ToolUtil.isNotEmptyWithAll(recycle)) {
            sql = sql + " and recycle='" + recycle + "'";
        }
        //使用公司
        if (ToolUtil.isNotEmpty(comp)) {
            sql = sql + " and used_company_id='" + comp + "'";
        }
        //所属公司
        if (ToolUtil.isNotEmpty(belongcomp)) {
            sql = sql + " and belong_company_id='" + belongcomp + "'";
        }
        //使用部门
        if (ToolUtil.isNotEmpty(part)) {
            sql = sql + " and part_id='" + part + "'";
        }
        //仓库
        if (ToolUtil.isNotEmpty(warehouse)) {
            sql = sql + " and warehouse='" + warehouse + "'";
        }
        //资产数
        if (ToolUtil.isNotEmpty(zcnumber)) {
            sql = sql + " and zc_cnt>" + zcnumber;
        }
        //类目
        if (ToolUtil.isNotEmpty(category)) {
            sql = sql + " and category='" + category + "'";
        }
        //编号
        if (ToolUtil.isNotEmpty(uuid)) {
            sql = sql + " and uuid='" + uuid + "'";
        }
        //机架
        if (ToolUtil.isNotEmpty(rack)) {
            sql = sql + " and rack='" + rack + "'";
        }
        //使用人
        if (ToolUtil.isNotEmpty(used_userid)) {
            sql = sql + " and used_userid='" + used_userid + "'";
        }
        //ids
        JSONArray ids_arr = JSONArray.parseArray(ids);
        if (ToolUtil.isNotEmpty(ids_arr) && ids_arr.size() > 0) {
            String idsstr = " and t.id in (";
            for (int i = 0; i < ids_arr.size(); i++) {
                idsstr = idsstr + "'" + ids_arr.getString(i) + "',";
            }
            idsstr = idsstr + "',-1')";
            sql = sql + idsstr;
        }
        //默认不显示报废数据,报废数据则,ishandle=1
        if (ToolUtil.isNotEmpty(ishandle) && "1".equals(ishandle)) {
            sql = sql + " and ishandle='1'";
        } else {
            sql = sql + " and ishandle='0'";
        }
        //datarange
        if (ToolUtil.isNotEmpty(datarange)) {
            sql = sql + buildAssetsDataRange(datarange);
        }
        String ressql = "";
        if (ToolUtil.isNotEmpty(search)) {
            ressql = "select * from (" + sql + ") end where (rack like '%" + search + "%' or fs1 like '%" + search + "%' or mark like '%" + search + "%' or name like '%"+search+"%' or uuid like '%" + search + "%' or model like '%" + search + "%'  or  sn like '%" + search + "%' or classrootname like '%" + search + "%' or locstr like '%" + search + "%' or  supplierstr like '%" + search + "%' or part_fullname like '%" + search + "%')";
        } else {
            ressql = sql;
        }
        ressql = ressql + " order by update_time desc,loc,rack,frame";

        return ressql;
    }

    /**
     * @param ps
     * @Description:查询资产s
     */
    public R queryAssetsData(TypedHashMap<String, Object> ps) {
        String sql = this.buildQueryAssetsDataSql(ps);
        RcdSet rs2 = db.query(sql);
        return R.SUCCESS_OPER(rs2.toJsonArrayWithJsonObject());
    }

    private String computeWb(String cur_wb, String wb_auto, String wbout_date_f) {
        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String wbcompute = cur_wb;
        if (ToolUtil.isNotEmpty(wb_auto) && "1".equals(wb_auto) && ToolUtil.isNotEmpty(wbout_date_f)) {
            try {
                Date wboutdate = simpleDateFormat.parse(wbout_date_f + " 01:00:00");
                if (date.getTime() > wboutdate.getTime()) {
                    // 脱保
                    wbcompute = "invalid";
                } else {
                    // 未脱保
                    wbcompute = "valid";
                }
            } catch (ParseException px) {
                px.printStackTrace();
            }
        }
        return wbcompute;
    }


    /**
     * @param uuid
     * @Description:根据UUID查询资产
     */
    public R queryAssetsDataByBusid(String uuid) {
        Rcd rs = db.uniqueRecord("select * from res t where dr=0 and uuid=?", uuid);
        if (rs == null) {
            return R.FAILURE_NO_DATA();
        }
        return queryAssetsDataById(rs.getString("id"));
    }

    /**
     * @param catid
     * @param resid
     * @Description:查询资产属性数据
     */
    public JSONArray queryAssetsAttrWithValue(String catid, String resid) {
        JSONArray res = new JSONArray();
        CtCategory ct = CtCategoryServiceImpl.getById(catid);
        if (ct == null) {
            return res;
        }
        String route = ct.getRoute();
        String attrsql = "select   " +
                "  a.*,   " +
                "  b.attrvalue   " +
                "from (   " +
                "       select t.*   " +
                "       from res_attrs t   " +
                "       where ifinheritable = '1' and dr = '0' and catid <> ? and catid in (" + route.replaceAll("-", ",") + ")   " +
                "       union all (select *   " +
                "                  from res_attrs   " +
                "                  where dr = '0' and catid = ?   " +
                "                  order by sort)   " +
                "     ) a left join (select *   " +
                "                    from res_attr_value   " +
                "                    where resid = ? and dr = '0') b on a.id = b.attrid   ";
        RcdSet attrs = db.query(attrsql, catid, catid, resid);
        return ConvertUtil.OtherJSONObjectToFastJSONArray(attrs.toJsonArrayWithJsonObject());
    }

    /**
     * @param id
     * @Description:根据资产ID查询数据
     */
    public R queryAssetsDataById(String id) {
        JSONObject data = new JSONObject();
        String class_id = "";
        Rcd rs = db.uniqueRecord("select * from res t where dr=0 and id=?", id);
        if (rs != null) {
            class_id = rs.getString("class_id");
        }
        // 获取属性数据
        JSONArray attrs = queryAssetsAttrWithValue(class_id, id);
        data.put("extattr", attrs);
        // 获取res数据
        if (ToolUtil.isNotEmpty(id)) {
            String sql = "select";
            for (int i = 0; i < attrs.size(); i++) {
                // 拼接sql
                String valsql = "";
                if ("inputint".equals(attrs.getJSONObject(i).getString("inputtype"))) {
                    valsql = " attrvalue+0";
                } else if ("inputstr".equals(attrs.getJSONObject(i).getString("inputtype"))) {
                    valsql = "attrvalue";
                } else {
                    valsql = "attrvalue";
                }
                sql = sql + " (select " + valsql
                        + " from res_attr_value i where i.dr=0 and i.resid=t.id and i.attrid='"
                        + attrs.getJSONObject(i).getString("id") + "') \"" + attrs.getJSONObject(i).getString("attrcode")
                        + "\",  ";
            }
            sql = sql + AssetsConstant.resSqlbody + " t.* from res t where dr=0 and id=?";
            Rcd rs2 = db.uniqueRecord(sql, id);
            if (rs2 != null) {
                data.put("data", ConvertUtil.OtherJSONObjectToFastJSONObject(rs2.toJsonObject()));
            }
        }
        // 获取更新记录
        RcdSet urs = db.query(
                " select * from (   " +
                        "  select   " +
                        "  (select name from sys_user_info where user_id=t.create_by)create_uname,   " +
                        "  t.* from res_change_item t where dr='0' and t.resid=? order by create_time desc) tab limit 300   ",
                id);
        data.put("updatadata", ConvertUtil.OtherJSONObjectToFastJSONArray(urs.toJsonArrayWithJsonObject()));
        // 获取故障登记表
        RcdSet grs = db.query(
                " select * from (   " +
                        "  select b.* from res_repair_item a ,res_repair b where a.repairid=b.id and a.dr='0' and a.resid=? and b.dr='0'   " +
                        "  order by create_time desc)tab limit 300",
                id);
        data.put("faultdata", ConvertUtil.OtherJSONObjectToFastJSONArray(grs.toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(data);

    }


    /**
     * @param ps
     * @Description:保存资产
     */
    public R saveAssets(TypedHashMap<String, Object> ps) {

        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);
        String id = ps.getString("id");
        String class_id = ps.getString("class_id");
        String sql = "";

        //判断资产编码是否正确
        String category="";
        if (ToolUtil.isEmpty(class_id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        } else {
            Rcd  rs = db.uniqueRecord("select * from ct_category where dr='0' and id=?", class_id);
            if (rs == null) {
                return R.FAILURE("资产别名称编码不存在,Code:" + class_id);
            }
            category=rs.getString("root");
        }
        // 自动计算脱保情况
        String wbcompute = computeWb(ps.getString("wb"), ps.getString("wb_auto"), ps.getString("wbout_date_f"));
        String uuid = "";
        if (ToolUtil.isEmpty(id)) {
            Insert me = new Insert("res");
            id = db.getUUID();
            me.set("id", id);
            uuid = createUuid(AssetsConstant.UUID_ZC);
            if (ToolUtil.isEmpty(uuid)) {
                return R.FAILURE("未生产有效编号,请稍后重新重试!");
            }
            me.set("uuid", uuid);
            me.set("dr", "0");
            me.setIf("update_time", nowtime);
            me.setIf("update_by", this.getUserId());
            me.setIf("create_time", nowtime);
            me.setIf("create_by", this.getUserId());
            me.setIf("class_id", class_id);
            me.setIf("sn", ps.getString("sn"));
            me.setIf("mark", ps.getString("mark"));
            me.setIf("maintain_userid", ps.getString("maintain_userid"));
            me.setIf("headuserid", ps.getString("headuserid"));
            me.setIf("rank", ps.getString("rank"));
            me.setIf("loc", ps.getString("loc"));
            me.setIf("locshow", ps.getString("locshow"));
            me.setIf("type", ps.getString("type"));
            me.setIf("category", category);
            me.setIf("status", ps.getString("status"));
            me.setIf("env", ps.getString("env"));
            me.setIf("risk", ps.getString("risk"));
            me.setIf("recycle", ps.getString("recycle"));
            me.setIf("sfsm", ps.getString("sfsm"));
            me.setIf("ip", ps.getString("ip"));
            me.setIf("frame", ps.getString("frame"));
            me.setIf("brand", ps.getString("brand"));
            me.setIf("wb", wbcompute);
            me.setIf("confdesc", ps.getString("confdesc"));
            me.setIf("rack", ps.getString("rack"));
            me.setIf("model", ps.getString("model"));
            me.setIf("buy_time", ps.getString("buy_time_f") == null ? null : ps.getString("buy_time_f") + " 01:00:00");
            me.setIf("changestate", "insert");
            me.setIf("net_worth", ps.getString("net_worth", "0"));
            me.setIf("buy_price", ps.getString("buy_price", "0"));
            me.setIf("mgr_part_id", "none".equals(ps.getString("mgr_part_id")) ? 0 : ps.getString("mgr_part_id"));
            me.setIf("used_userid", ps.getString("used_userid"));
            if(ToolUtil.isNotEmpty(ps.getString("used_userid"))){
                me.setIf("part_id",sysUserInfoService.queryUserPart(ps.getString("used_userid")));
            }
            me.setIf("locdtl", ps.getString("locdtl"));
            me.setIf("wb_auto", ps.getString("wb_auto"));
            me.setIf("wbout_date", ps.getString("wbout_date_f") == null ? null : ps.getString("wbout_date_f") + " 01:00:00");
            me.setIf("fs1", ps.getString("fs1"));
            me.setIf("fs2", ps.getString("fs2"));
            me.setIf("fs3", ps.getString("fs3"));
            me.setIf("fs4", ps.getString("fs4"));
            me.setIf("fs5", ps.getString("fs5"));
            me.setIf("fs6", ps.getString("fs6"));
            me.setIf("fs7", ps.getString("fs7"));
            me.setIf("fs10","直接入库");
            me.setIf("fs18", ps.getString("fs18"));
            me.setIf("fs19", ps.getString("fs19"));
            me.setIf("fs20", ps.getString("fs20"));
            me.setIf("zc_cnt", ps.getString("zc_cnt", "1"));
            me.setIf("img", ps.getString("img"));
            me.setIf("attach", ps.getString("attach"));
            me.setIf("supplier", ps.getString("supplier"));
            me.setIf("wbsupplier", ps.getString("wbsupplier"));
            me.setIf("zcsource", ps.getString("zcsource"));
            me.setIf("wbct", ps.getString("wbct"));
            me.setIf("belong_part_id", ps.getString("belong_part_id"));
            me.setIf("belong_company_id", ps.getString("belong_company_id"));
            me.setIf("used_company_id", ps.getString("used_company_id"));
            me.setIf("unit_price", ps.getString("unit_price"));
            me.setIf("warehouse", ps.getString("warehouse"));
            me.setIf("batchno", ps.getString("batchno"));
            me.setIf("usefullife", ps.getString("usefullife"));
            me.setIf("unit", ps.getString("unit"));
            me.setIf("isscrap", ps.getString("isscrap"));
            me.setIf("name", ps.getString("name"));
            me.setIf("batch", ps.getString("batch"));
            me.setIf("fd1", ps.getString("fd1str") == null ? null : ps.getString("fd1str") + " 00:00:00");
            sql = me.getSQL();
        } else {
            Update me = new Update("res");
            me.set("class_id", class_id);
            me.setIf("sn", ps.getString("sn"));
            me.setIf("mark", ps.getString("mark"));
            me.setIf("maintain_userid", ps.getString("maintain_userid"));
            me.setIf("headuserid", ps.getString("headuserid"));
            me.setIf("rank", ps.getString("rank"));
            me.setIf("loc", ps.getString("loc"));
            me.setIf("locshow", ps.getString("locshow"));
            me.setIf("status", ps.getString("status"));
            me.setIf("env", ps.getString("env"));
            me.setIf("risk", ps.getString("risk"));
            me.setIf("type", ps.getString("type"));
            me.setIf("recycle", ps.getString("recycle"));
            me.setIf("ip", ps.getString("ip"));
            me.setIf("frame", ps.getString("frame"));
            me.setIf("wb", wbcompute);
            me.setIf("confdesc", ps.getString("confdesc"));
            me.setIf("rack", ps.getString("rack"));
            me.setIf("model", ps.getString("model"));
            me.setIf("brand", ps.getString("brand"));
            me.setIf("buy_time", ps.getString("buy_time_f") == null ? null : ps.getString("buy_time_f") + " 01:00:00");
            me.setIf("changestate", "updated");
            me.setIf("buy_price", ps.getString("buy_price", "0"));
            me.setIf("net_worth", ps.getString("net_worth", "0"));
            me.setIf("mgr_part_id", "none".equals(ps.getString("mgr_part_id")) ? 0 : ps.getString("mgr_part_id"));
            me.setIf("used_userid", ps.getString("used_userid"));
            if(ToolUtil.isNotEmpty(ps.getString("used_userid"))){
                // me.setIf("part_id", "none".equals(ps.getString("part_id")) ? 0 : ps.getString("part_id"));
                me.setIf("part_id",sysUserInfoService.queryUserPart(ps.getString("used_userid")));
            }
            me.setIf("locdtl", ps.getString("locdtl"));
            me.setIf("wb_auto", ps.getString("wb_auto"));
            me.setIf("wbout_date",ps.getString("wbout_date_f") == null ? null : ps.getString("wbout_date_f") + " 01:00:00");
            me.setIf("fs1", ps.getString("fs1"));
            me.setIf("fs2", ps.getString("fs2"));
            me.setIf("fs3", ps.getString("fs3"));
            me.setIf("fs4", ps.getString("fs4"));
            me.setIf("fs5", ps.getString("fs5"));
            me.setIf("fs6", ps.getString("fs6"));
            me.setIf("fs7", ps.getString("fs7"));
            me.setIf("fs18", ps.getString("fs18"));
            me.setIf("fs19", ps.getString("fs19"));
            me.setIf("fs20", ps.getString("fs20"));
            me.setIf("zc_cnt", ps.getString("zc_cnt"));
            me.setIf("img", ps.getString("img"));
            me.setIf("attach", ps.getString("attach"));
            me.setIf("supplier", ps.getString("supplier"));
            me.setIf("wbsupplier", ps.getString("wbsupplier"));
            me.setIf("zcsource", ps.getString("zcsource"));
            me.setIf("wbct", ps.getString("wbct"));
            me.setIf("belong_part_id", ps.getString("belong_part_id"));
            me.setIf("belong_company_id", ps.getString("belong_company_id"));
            me.setIf("used_company_id", ps.getString("used_company_id"));
            me.setIf("unit_price", ps.getString("unit_price"));
            me.setIf("warehouse", ps.getString("warehouse"));
            me.setIf("batchno", ps.getString("batchno"));
            me.setIf("usefullife", ps.getString("usefullife"));
            me.setIf("unit", ps.getString("unit"));
            me.setIf("fd1", ps.getString("fd1str") == null ? null : ps.getString("fd1str") + " 00:00:00");
            me.setIf("name", ps.getString("name"));
            me.setIf("batch", ps.getString("batch"));

            me.where().and("id=?", id);
            sql = me.getSQL();
        }

        db.execute(sql);
        // 更新记录表


        // 更新其他属性，属性值、
        String attrvals = ps.getString("attrvals");
        Update del = new Update("res_attr_value");
        del.set("dr", "1");
        del.where().and("resid=?", id);
        db.execute(del);
        if (ToolUtil.isNotEmpty(attrvals)) {
            JSONArray valsarr = JSONArray.parseArray(attrvals);
            for (int i = 0; i < valsarr.size(); i++) {
                Insert me = new Insert("res_attr_value");
                me.set("id", db.getUUID());
                me.set("resid", id);
                me.set("dr", "0");
                me.setIf("attrid", valsarr.getJSONObject(i).getString("id"));
                me.setIf("attrvalue", valsarr.getJSONObject(i).getString("attrvalue"));
                db.execute(me);
            }
        }
        //记录变化
        ResChangeItem e = new ResChangeItem();
        e.setFillct("1");
        e.setCreateTime(new Date());
        e.setCreateBy(getUserId());
        e.setResid(id);
        if (ToolUtil.isNotEmpty(ps.getString("id"))) {
            e.setType("ADD");
            e.setCt("资产入库,操作人员:" + getName());
            e.setMark("资产入库");
        } else {
            e.setType("UPDATE");
            e.setCt("直接更新数据,操作人员:" + getName());
            e.setMark("直接更新数据");
        }
        ResChangeItemServiceImpl.saveOrUpdate(e);
        JSONObject r=new JSONObject();
        r.put("uuid",uuid);
        r.put("id",id);
        return R.SUCCESS_OPER(r);
    }

    /**
     * @Description:查询前端字段显示
     */
    public R queryAssetsColCtlShow() {
        JSONArray res = new JSONArray();
        //zccolctl,zccolctlcommon
        Rcd rs1 = db.uniqueRecord("select * from sys_params where id='zccolctl'");
        Rcd rs2 = db.uniqueRecord("select * from sys_params where id='zccolctlcommon'");
        if (rs1 == null) {
            Insert ins1 = new Insert("sys_params");
            ins1.set("dr", "0");
            ins1.set("type", "system");
            ins1.set("id", "zccolctl");
            ins1.set("name", "前端入库资产列显示");
            ins1.set("value", "{}");
            db.execute(ins1);
            rs1 = db.uniqueRecord("select * from sys_params where id='zccolctl'");
        }
        if (rs2 == null) {
            Insert ins2 = new Insert("sys_params");
            ins2.set("dr", "0");
            ins2.set("type", "system");
            ins2.set("id", "zccolctlcommon");
            ins2.set("name", "前端常用资产列显示");
            ins2.set("value", "{}");
            db.execute(ins2);
            rs2 = db.uniqueRecord("select * from sys_params where id='zccolctlcommon'");
        }
        String rs1ct = rs1.getString("value");
        JSONObject rs1obj = JSONObject.parseObject(rs1ct);
        rs1obj.put("zccolparname", rs1.getString("name"));
        rs1obj.put("zccolparid", rs1.getString("id"));
        res.add(rs1obj);

        String rs2ct = rs2.getString("value");
        JSONObject rs2obj = JSONObject.parseObject(rs2ct);
        rs2obj.put("zccolparname", rs2.getString("name"));
        rs2obj.put("zccolparid", rs2.getString("id"));
        res.add(rs2obj);
        return R.SUCCESS_OPER(res);
    }

    /**
     * @param id
     * @param json
     * @Description:修改资产字段前端显示
     */
    public R modifyAssetsColCtlShow(String id, String json) {
        Update ups = new Update("sys_params");
        ups.set("dr", "0");
        ups.setIf("value", json);
        ups.where().andIf("id=?", id);
        db.execute(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @param id
     * @Description:查询前端字段显示
     */
    public R queryAssetsColCtlById(String id) {
        Rcd rs2 = db.uniqueRecord("select * from sys_params where id=?", id);
        return R.SUCCESS_OPER(rs2.toJsonObject());
    }

    /**
     * @param type
     * @param items
     * @Description:检查单据生成资产条件
     */
    public R fastProcessItemCheck(String type, String items) {
        String sql = "select count(1) cnt from res where dr='0' ";
        sql = sql + buildAssetsDataRange(type);
        JSONArray items_arr = JSONArray.parseArray(items);
        if (ToolUtil.isNotEmpty(items_arr) && items_arr.size() > 0) {
            String idsstr = " and id in (";
            for (int i = 0; i < items_arr.size(); i++) {
                idsstr = idsstr + "'" + items_arr.getString(i) + "',";
            }
            idsstr = idsstr + "',-1')";
            sql = sql + idsstr;
        }
        if (!db.uniqueRecord(sql).getString("cnt").equals(items_arr.size() + "")) {
            return R.FAILURE("所选数据中可能部分数据不符合要求!");
        }
        return R.SUCCESS_OPER();
    }

}
