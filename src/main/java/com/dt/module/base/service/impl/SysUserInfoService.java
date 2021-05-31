package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.cache.CacheConfig;
import com.dt.core.common.base.BaseCommon;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.module.hrm.entity.HrmOrgEmployee;
import com.dt.module.hrm.service.IHrmOrgEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
@Service
public class SysUserInfoService extends BaseService {

    @Autowired
    IHrmOrgEmployeeService HrmOrgEmployeeServiceImpl;

    private static Logger _log = LoggerFactory.getLogger(SysUserInfoService.class);

    public String queryUserComp(String userId){
        String result="";
        Rcd rs=db.uniqueRecord("select b.route from hrm_org_employee a,hrm_org_part b where a.node_id=b.node_id and b.type='part' and a.user_id=?",userId);
        if(rs==null){
            return result;
        }
        String route=rs.getString("route");
        Rcd rs2=db.uniqueRecord("select node_id from hrm_org_part where type='comp' and node_id in ("+route.replaceAll("-",",")+") order by node_id desc limit 1");
        if(rs2==null){
            return result;
        }
        result=rs2.getString("node_id");
        return result;
    }

    public String queryUserNode(String userId){
        String result="";
        QueryWrapper<HrmOrgEmployee> q=new QueryWrapper<HrmOrgEmployee>();
        q.eq("user_id",userId);
        HrmOrgEmployee e=HrmOrgEmployeeServiceImpl.getOne(q);
        if(e!=null){
            result=e.getNodeId();
        }
        return result;
    }

    public String queryUserPart(String userId){
        String result="";
        Rcd rs=db.uniqueRecord("select a.node_id from hrm_org_employee a,hrm_org_part b where a.node_id=b.node_id and b.type='part' and a.user_id=?",userId);
        if(rs!=null){
            result=rs.getString("node_id");
        }
        return result;
    }
    /**
     * @Description:查询用户拥有的菜单
     * @param menu_id
     * @param user_id
     */
   // @Cacheable(value = CacheConfig.CACHE_USER_1200_600, key = "'um'+#user_id+#menu_id")
    public R listMyMenusById(String user_id, String menu_id) {
        String basesql = "";
        if (BaseCommon.isSuperAdmin(user_id)) {
            basesql = "select * from sys_menus_node where dr='0' and type <>'btn' and menu_id='" + menu_id + "' and parent_id = ? order by sort";
        } else {
            if (db.getDBType().equals(DbUtil.TYPE_ORACLE)) {
                basesql = " select distinct level1 node_id                                                 "
                        + "   from (select *                                                               "
                        + "           from (select b.module_id,                                            "
                        + "                        c.route,                                                "
                        + "                        c.node_name,                                            "
                        + "                        decode(instr(route, '-'),                               "
                        + "                               0,                                               "
                        + "                               route,                                           "
                        + "                               substr(route, 1, instr(route, '-') - 1)) level1  "
                        + "                   from sys_user_role a, sys_role_module b, sys_menus_node c    "
                        + "                  where c.node_id = b.module_id  and c.type <>'btn'             "
                        + "                    and a.role_id = b.role_id                                   "
                        + "                    and user_id = '<#USER_ID#>')                                "
                        + "         union all                                                              "
                        + "         select *                                                               "
                        + "           from (select b.module_id,                                            "
                        + "                        c.route,                                                "
                        + "                        c.node_name,                                            "
                        + "                        decode(length(route) - length(replace(route, '-', '')), "
                        + "                               0,                                               "
                        + "                               '-1',                                            "
                        + "                               1,                                               "
                        + "                               substr(route,                                    "
                        + "                                      instr(route, '-', 1, 1) + 1,              "
                        + "                                      length(route) - instr(route, '-', 1, 1)), "
                        + "                               substr(route,                                    "
                        + "                                      instr(route, '-', 1, 1) + 1,              "
                        + "                                      instr(route, '-', 1, 2) -                 "
                        + "                                      instr(route, '-', 1, 1) - 1)) level2      "
                        + "                   from sys_user_role a, sys_role_module b, sys_menus_node c    "
                        + "                  where c.node_id = b.module_id                                 "
                        + "                    and a.role_id = b.role_id  and  and c.type <>'btn'          "
                        + "                    and user_id = '<#USER_ID#>')                                "
                        + "         union all                                                              "
                        + "         select *                                                               "
                        + "           from (select b.module_id,                                            "
                        + "                        c.route,                                                "
                        + "                        c.node_name,                                            "
                        + "                        decode(length(route) - length(replace(route, '-', '')), "
                        + "                               0,                                               "
                        + "                               '-1',                                            "
                        + "                               1,                                               "
                        + "                               '-1',                                            "
                        + "                               2,                                               "
                        + "                               substr(route,                                    "
                        + "                                      instr(route, '-', 1, 2) + 1,              "
                        + "                                      length(route) - instr(route, '-', 1, 2)), "
                        + "                               substr(route,                                    "
                        + "                                      instr(route, '-', 1, 2) + 1,              "
                        + "                                      instr(route, '-', 1, 3) -                 "
                        + "                                      instr(route, '-', 1, 2) - 1)) level3      "
                        + "                   from sys_user_role a, sys_role_module b, sys_menus_node c    "
                        + "                  where c.node_id = b.module_id                                 "
                        + "                    and a.role_id = b.role_id   and c.type <>'btn'              "
                        + "                    and user_id = '<#USER_ID#>'))                               "
                        + "  where level1 <> '-1'";
            } else if (db.getDBType().equals(DbUtil.TYPE_MYSQL)) {
                basesql = "select distinct level1 node_id  from (select *  from (select b.module_id, "
                        + "c.route,  c.node_name,  case instr(route, '-')  when 0 then route  else "
                        + "substr(route, 1, instr(route, '-') - 1)  end level1 "
                        + "from sys_user_role a, sys_role_module b, sys_menus_node c "
                        + "where c.node_id = b.module_id and a.role_id = b.role_id "
                        + "and user_id = '<#USER_ID#>') a  union all  select *  from (   "
                        + "select b.module_id,  c.route,  c.node_name, "
                        + "case length(route) - length(replace(route, '-', ''))  when 0 then '-1'  when 1 then "
                        + "substr(route,  locate('-',route)+ 1,  length(route) - locate('-',route))  else "
                        + "substr(route,  locate('-',route) + 1, "
                        + "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end "
                        + "- locate('-',route) - 1)  end level2 "
                        + "from sys_user_role a, sys_role_module b, sys_menus_node c "
                        + "where c.node_id = b.module_id  and a.role_id = b.role_id "
                        + "and user_id = '<#USER_ID#>'  )  b  union all  select *  from ( "
                        + "select b.module_id,  c.route,  c.node_name, "
                        + "case length(route) - length(replace(route, '-', ''))  when  0 then '-1' "
                        + "when 1 then '-1'  when 2 then  substr(route, "
                        + "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end + 1, "
                        + "length(route) - case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end) "
                        + "else  substr(route, "
                        + "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end + 1, "
                        + "case when substring_index(route,'-',4)=substring_index(route,'-',3)then 0 else length(substring_index(route,'-',3))+1 end - "
                        + "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end - 1) end level3 "
                        + "from sys_user_role a, sys_role_module b, sys_menus_node c "
                        + "where  c.type<>'btn' and c.node_id = b.module_id  and a.role_id = b.role_id "
                        + "and user_id = '<#USER_ID#>') c) d  where level1 <> '-1'";
            }

            basesql = "select a.* from sys_menus_node a, (" + basesql + ") b "
                    + "where a.is_g_show='Y' and a.type<>'btn' and a.dr='0' and a.node_id = b.node_id and menu_id = '" + menu_id
                    + "' and parent_id = ?  order by sort ";
            basesql = basesql.replaceAll("<#USER_ID#>", user_id);

        }
        _log.debug("getMenu sql:" + basesql + ",menu_id:" + menu_id);
        String btnsql = "select keyvalue p from sys_menus_node where  is_g_show='Y' and dr='0' and parent_id=? and type='btn' "
                + "and node_id in (select module_id from sys_user_role a,sys_role_module b  where a.user_id=? and a.role_id=b.role_id) ";

        JSONArray r = new JSONArray();
        RcdSet first_rs = db.query(basesql, 0);
        for (int i = 0; i < first_rs.size(); i++) {
            // 处理第一层数据
            JSONObject first_obj = ConvertUtil.OtherJSONObjectToFastJSONObject(first_rs.getRcd(i).toJsonObject());
            _log.debug("显示第一层菜单数据:\n" + first_obj);
            String first_key = first_rs.getRcd(i).getString("keyvalue");
            first_obj.put("state", first_key);
            int second_pid = first_rs.getRcd(i).getInteger("node_id");
            RcdSet second_rs = db.query(basesql, second_pid);
            JSONArray second_arr = new JSONArray();
            for (int j = 0; j < second_rs.size(); j++) {
                // 处理第二层数据
                JSONObject second_obj = ConvertUtil.OtherJSONObjectToFastJSONObject(second_rs.getRcd(j).toJsonObject());
                _log.debug("显示第二层菜单数据:\n" + second_obj);
                String second_key = second_rs.getRcd(j).getString("keyvalue");
                // 菜单显示控制
                second_obj.put("state", first_key + "." + second_key);
                int third_pid = second_rs.getRcd(j).getInteger("node_id");
                RcdSet third_rs = db.query(basesql, third_pid);
                second_obj.put("children_cnt", third_rs.size());
                JSONArray third_arr = ConvertUtil.OtherJSONObjectToFastJSONArray(third_rs.toJsonArrayWithJsonObject());
                for (int f = 0; f < third_arr.size(); f++) {
                    _log.debug("显示第三层菜单数据:\n" + third_arr);
                    // 菜单显示控制
                    third_arr.getJSONObject(f).put("state",
                            first_key + "." + second_key + "." + third_arr.getJSONObject(f).getString("keyvalue"));
                }
                second_obj.put("btn_cnt", 0);
                if ("menu".equals(second_rs.getRcd(j).getString("type"))) {
                    RcdSet second_btn_rs = db.query(btnsql, second_rs.getRcd(j).getString("node_id"), user_id);
                    second_obj.put("btn_cnt", second_btn_rs.size());
                    second_obj.put("btn",
                            ConvertUtil.OtherJSONObjectToFastJSONArray(second_btn_rs.toJsonArrayWithJsonObject()));
                }

                second_obj.put("children", third_arr);
                second_arr.add(second_obj);
            }
            first_obj.put("children_cnt", second_rs.size());
            first_obj.put("children", second_arr);
            first_obj.put("btn_cnt", 0);
            if ("menu".equals(first_rs.getRcd(i).getString("type"))) {
                RcdSet first_btn_rs = db.query(btnsql, first_rs.getRcd(i).getString("node_id"), user_id);
                first_obj.put("btn_cnt", first_btn_rs.size());
                first_obj.put("btn",
                        ConvertUtil.OtherJSONObjectToFastJSONArray(first_btn_rs.toJsonArrayWithJsonObject()));
            }
            r.add(first_obj);
        }
        return R.SUCCESS_OPER(r);
    }

}
