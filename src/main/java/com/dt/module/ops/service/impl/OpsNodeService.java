package com.dt.module.ops.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.cache.CacheConfig;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.ops.entity.OpsNodeDBEntity;
import com.dt.module.ops.entity.OpsNodeDBImportResultEntity;
import com.dt.module.ops.entity.OpsNodeEntity;
import com.dt.module.ops.entity.OpsNodeImportResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: lank
 * @date: Jan 24, 2020 2:02:52 PM
 * @Description:
 */

@Service
public class OpsNodeService extends BaseService {


    private static Logger log = LoggerFactory.getLogger(OpsNodeService.class);


    public static String sql = "select  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.nodebackup and dict_id = 'nodebak' )   nodebackupstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.runenv and dict_id = 'sysenv' )   sysenvstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.syslevel and dict_id = 'syslevel' ) syslevelstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.busitype and dict_id = 'systype' ) systypestr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.loc and dict_id = 'sysloc' ) syslocstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.os and dict_id = 'sysos' ) sysosstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.osdtl and dict_id = 'sysosdtl' ) sysosdtlstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.db and dict_id = 'sysdb' ) sysdbstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.dbdtl and dict_id = 'sysdbdtl' ) sysdbdtlstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.execenv and dict_id = 'sysexecenv' ) sysexecenvstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.monitor and dict_id = 'sysmonitor' ) sysmonitorstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.status and dict_id = 'sysstatus' ) statusstr,  "
            + "(select name from sys_dict_item where dr='0' and dict_item_id=t.pwdstrategy and dict_id = 'syspwdstrategy' ) syspwdstrategystr,  "
            + "t.* from ops_node t where dr=0 ";

    /**
     * @Description:查询所有Ops对象数据
     * @param search
     */
    public R selecList(String search) {
        String sql = OpsNodeService.sql + " and arch='0'";
        if (ToolUtil.isNotEmpty(search)) {
            sql = sql + " and (name like '%" + search + "%' or ip like '%" + search + "%' or leader like '%" + search
                    + "%' or mark like '%" + search + "%')";
        }
        sql = sql + " order by name";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:检查中间件数据
     */
    public R validMiddlewareData() {
        String sql = "select t.*,(length(middleware)-length(replace(middleware, ',','')) ) +1 cnt  "
                + " from ops_node t where arch='0' and dr='0' and middleware<>'[]'";
        RcdSet rs = db.query(sql);
        List<String> sqls = new ArrayList<String>();
        sqls.add("delete from ops_node_item where type='middleware'");
        for (int i = 0; i < rs.size(); i++) {
            JSONArray e = JSONArray.parseArray(rs.getRcd(i).getString("middleware"));
            if (e.size() > 0) {
                for (int j = 0; j < e.size(); j++) {
                    Insert me = new Insert("ops_node_item");
                    me.set("id", db.getUUID());
                    me.set("dr", 0);
                    me.setIf("nid", rs.getRcd(i).getString("id"));
                    me.set("type", "middleware");
                    me.setIf("value", e.getString(j));
                    sqls.add(me.getSQL());
                }
            }
        }
        db.executeStringList(sqls);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:执行导入操作
     */
    public R importOpsNodeEntitys(List<OpsNodeEntity> resultdata) {
        OpsNodeImportResultEntity result = checkOpsNodeEntitys(resultdata);
        result.printResult();
        if (!result.is_success_all) {
            return R.FAILURE("操作失败", result.covertJSONObjectResult());
        }
        db.executeStringList(result.success_cmds);
        validMiddlewareData();
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:检查opsNode
     */
    private OpsNodeImportResultEntity checkOpsNodeEntitys(List<OpsNodeEntity> result) {
        OpsNodeImportResultEntity cres = new OpsNodeImportResultEntity();
        String importlabel = ToolUtil.getUUID();
        for (int i = 0; i < result.size(); i++) {
            R r = checkOpsNodeEntity(result.get(i), importlabel);
            System.out.println(r.getData());
            if (r.isSuccess()) {
                cres.addSuccess(r.getData().toString());
            } else {
                System.out.println("ERROR");
                OpsNodeEntity obj=result.get(i);
                obj.setMark(r.getMessage());
                cres.addFailed(obj);
            }
        }
        return cres;
    }

    /**
     * @Description:检查数据字典
     */
    @Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#dict+'_'+#name")
    public R checkDictItem(String dict, String name) {

        if(ToolUtil.isEmpty(name)){
            JSONObject r=new JSONObject();
            r.put("dict_item_id","");
            return R.SUCCESS_OPER(r);
        }
//        if (ToolUtil.isEmpty(name)) {
//            return R.FAILURE("数据字典:"+dict+",请求参数不正确");
//        }
        Rcd rs = db.uniqueRecord("select dict_item_id from dt.sys_dict_item where dr='0' and dict_id=? and name=?",
                dict, name);
        if (rs == null) {
            log.info("数据字典项目:Dict:" + dict + ",value:" + name);
            return R.FAILURE("无法匹配数据字典项目:Dict:" + dict + ",value:" + name);
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }



    /**
     * @Description:检查操作
     */
    public R checkOpsNodeEntity(OpsNodeEntity re, String importlabel) {

        Date date = new Date(); // 获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
        String nowtime = simpleDateFormat.format(date);
        String sql = "";
        String type = "insert";
        if (ToolUtil.isNotEmpty(re.getId())) {
            type = "update";
        } else {
            type = "insert";
        }

        // 检查数据字典
        R runenvR = checkDictItem("sysenv", re.getSysenvstr());
        if (runenvR.isFailed()) {
            return R.FAILURE(runenvR.getMessage());
        }

        R syslevelR = checkDictItem("syslevel", re.getSyslevelstr());
        if (syslevelR.isFailed()) {
            return R.FAILURE(syslevelR.getMessage());
        }

        R busitypeR = checkDictItem("systype", re.getSystypestr());
        if (busitypeR.isFailed()) {
            return R.FAILURE(busitypeR.getMessage());
        }

        R locR = checkDictItem("sysloc", re.getSyslocstr());
        if (locR.isFailed()) {
            return R.FAILURE(locR.getMessage());
        }

        R osR = checkDictItem("sysos", re.getSysosstr());
        if (osR.isFailed()) {
            return R.FAILURE(osR.getMessage());
        }

        R osdtlR = checkDictItem("sysosdtl", re.getSysosdtlstr());
        if (osdtlR.isFailed()) {
            return R.FAILURE(osdtlR.getMessage());
        }

        R dbR = checkDictItem("sysdb", re.getSysdbstr());
        if (dbR.isFailed()) {
            return R.FAILURE(dbR.getMessage());
        }

        R dbdtlR = checkDictItem("sysdbdtl", re.getSysdbdtlstr());
        if (dbdtlR.isFailed()) {
            return R.FAILURE(dbdtlR.getMessage());
        }

        R execenvR = checkDictItem("sysexecenv", re.getSysexecenvstr());
        if (dbdtlR.isFailed()) {
            return R.FAILURE(execenvR.getMessage());
        }

        R monitorR = checkDictItem("sysmonitor", re.getSysmonitorstr());
        if (monitorR.isFailed()) {
            return R.FAILURE(monitorR.getMessage());
        }

        R pwdstrategyR = checkDictItem("syspwdstrategy", re.getSyspwdstrategystr());
        if (pwdstrategyR.isFailed()) {
            return R.FAILURE(pwdstrategyR.getMessage());
        }
        R statusR = checkDictItem("sysstatus", re.getStatusstr());
        if (statusR.isFailed()) {
            return R.FAILURE(statusR.getMessage());
        }

        R nodebackupR = checkDictItem("nodebak", re.getNodebackupstr());
        if (nodebackupR.isFailed()) {
            return R.FAILURE(nodebackupR.getMessage());
        }

        // 中间价
        String mid = re.getMiddlewarestr();
        JSONArray mid_ids = new JSONArray();
        String mid_str = "";
        if (ToolUtil.isNotEmpty(mid)) {
            String[] mid_arr = mid.split(",");
            for (int i = 0; i < mid_arr.length; i++) {
                R r = checkDictItem("sysmid", mid_arr[i]);
                if (r.isFailed()) {
                    return R.FAILURE(r.getMessage());
                }
                if (i == 0) {
                    mid_str = mid_str + mid_arr[i];
                } else {
                    mid_str = mid_str + "," + mid_arr[i];
                }
                mid_ids.add(r.queryDataToJSONObject().getString("dict_item_id"));

            }
        }

        if (type.equals("insert")) {
            Insert me = new Insert("ops_node");
            me.set("importlabel", importlabel);
            me.set("id", db.getUUID());
            me.set("dr", "0");
            me.setIf("create_time", nowtime);
            me.setIf("create_by", this.getUserId());
            me.setIf("update_time", nowtime);
            me.setIf("update_by", this.getUserId());
            /////////////// 开始处理///////////
            me.setIf("name", re.getName());
            me.setIf("ip", re.getIp());
            me.setIf("mark", re.getMark());
            me.setIf("leader", re.getLeader());
            me.setIf("pwdmark", re.getPwdmark());
            // me.setIf("nodebackup", re.getNodebackup());

            me.setIf("label1", re.getLabel1());
            me.setIf("label2", re.getLabel2());

            me.setIf("userdb", re.getUserdb());
            me.setIf("userdbused", re.getUserdbused());
            me.setIf("userapp", re.getUserapp());
            me.setIf("userother", re.getUserother());
            me.setIf("useradmin", re.getUseradmin());
            me.setIf("usernologin", re.getUsernologin());
            me.setIf("userops", re.getUserops());
            me.setIf("usermid", re.getUsermid());
            me.setIf("nodebackupdtl", re.getNodebackupdtl());


            // 数据字典匹配
            me.setIf("nodebackup", nodebackupR.queryDataToJSONObject().getString("dict_item_id"));

            me.setIf("runenv", runenvR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("syslevel", syslevelR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("busitype", busitypeR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("os", osR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("osdtl", osdtlR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("db", dbR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("dbdtl", dbdtlR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("execenv", execenvR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("monitor", monitorR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("pwdstrategy", pwdstrategyR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("status", statusR.queryDataToJSONObject().getString("dict_item_id"));

            me.setIf("middleware", mid_ids.toJSONString());
            me.setIf("middlewarestr", mid_str);
            me.set("arch", "0");
            sql = me.getSQL();
        } else if (type.equals("update")) {
            Update me = new Update("ops_node");
            me.set("importlabel", importlabel);

            me.set("dr", "0");
            me.setIf("update_time", nowtime);
            me.setIf("update_by", this.getUserId());
            /////////////// 开始处理////////////
            me.setIf("name", re.getName());
            me.setIf("ip", re.getIp());
            me.setIf("mark", re.getMark());
            me.setIf("leader", re.getLeader());
            me.setIf("pwdmark", re.getPwdmark());
            // me.setIf("nodebackup", re.getNodebackup());

            me.setIf("label1", re.getLabel1());
            me.setIf("label2", re.getLabel2());

            me.setIf("userapp", re.getUserapp());
            me.setIf("userother", re.getUserother());
            me.setIf("useradmin", re.getUseradmin());
            me.setIf("usernologin", re.getUsernologin());
            me.setIf("userops", re.getUserops());
            me.setIf("usermid", re.getUsermid());
            me.setIf("userdb", re.getUserdb());
            me.setIf("userdbused", re.getUserdbused());
            me.setIf("nodebackupdtl", re.getNodebackupdtl());

            // 数据字典匹配
            me.setIf("nodebackup", nodebackupR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("runenv", runenvR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("syslevel", syslevelR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("busitype", busitypeR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("os", osR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("osdtl", osdtlR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("db", dbR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("dbdtl", dbdtlR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("execenv", execenvR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("monitor", monitorR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("pwdstrategy", pwdstrategyR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("status", statusR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("middleware", mid_ids.toJSONString());
            me.setIf("middlewarestr", mid_str);
            me.where().and("id=?", re.getId());


            sql = me.getSQL();
        }
        return R.SUCCESS_OPER(sql);
    }

    /**
     * @Description:查询数据库
     */
    public R selectDBList(String dbinstid, String nodeid) {
        String sql = "select  "
                + "  (select name from sys_dict_item where dr='0' and dict_item_id=b.db and dict_id = 'sysdb' ) sysdbstr,  "
                + "  (select name from sys_dict_item where dr='0' and dict_item_id=b.nodebackup and dict_id = 'nodebak' ) nodebackupstr,  "
                + "	(select name from sys_dict_item where dr='0' and dict_item_id=b.dbdtl and dict_id = 'sysdbdtl' ) sysdbdtlstr,  "
                + "	(select name from sys_dict_item where dr='0' and dict_item_id=b.status and dict_id = 'sysstatus' ) sysstatusstr,  "
                + "  (select name from sys_dict_item where dr='0' and dict_item_id=a.archtype and dict_id = 'dbbkarchtype' ) dbbkarchtypestr,  "
                + "  (select name from sys_dict_item where dr='0' and dict_item_id=a.bkmethod and dict_id = 'dbbkmethod' ) dbbkmethodstr,  "
                + "  (select name from sys_dict_item where dr='0' and dict_item_id=a.bkstatus and dict_id = 'dbbkstatus' ) dbbkstatusstr,  "
                + "  (select name from sys_dict_item where dr='0' and dict_item_id=a.bktype and dict_id = 'dbbktype' ) dbbktypestr,  "
                + "  a.*,b.name xtname,b.ip,b.db ,b.dbdtl from ops_node_item a, ops_node b where a.nid=b.id  "
                + "and a.dr='0' and b.dr='0' and a.type='dbinstance'  ";

        if (ToolUtil.isNotEmpty(dbinstid)) {
            sql = sql + " and a.id='" + dbinstid + "' ";
        }
        if (ToolUtil.isNotEmpty(nodeid)) {
            sql = sql + " and b.id='" + nodeid + "' ";
        }

        sql = sql + " order by db,ip";
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
    }

    /**
     * @Description:导入数据库
     */
    public R importOpsNodeDBEntitys(List<OpsNodeDBEntity> resultdata) {
        OpsNodeDBImportResultEntity result = checkOpsNodeDBEntitys(resultdata);
        result.printResult();
        if (!result.is_success_all) {
            return R.FAILURE("操作失败", result.covertJSONObjectResult());
        }
        db.executeStringList(result.success_cmds);
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:检查数据库
     */
    private OpsNodeDBImportResultEntity checkOpsNodeDBEntitys(List<OpsNodeDBEntity> result) {
        OpsNodeDBImportResultEntity cres = new OpsNodeDBImportResultEntity();
        String importlabel = ToolUtil.getUUID();
        for (int i = 0; i < result.size(); i++) {
            R r = checkOpsNodeDBEntity(result.get(i), importlabel);
            if (r.isSuccess()) {
                cres.addSuccess(r.getData().toString());
            } else {
                cres.addFailed(result.get(i));
            }
        }
        return cres;
    }

    /**
     * @Description:检查数据库数据导入
     */
    public R checkOpsNodeDBEntity(OpsNodeDBEntity re, String importlabel) {

        String id = re.getId();
        String sql = "";

        R archtypeR = checkDictItem("dbbkarchtype", re.getDbbkarchtypestr());
        if (archtypeR.isFailed()) {
            return R.FAILURE(archtypeR.getMessage());
        }

        R bktypeR = checkDictItem("dbbktype", re.getDbbktypestr());
        if (bktypeR.isFailed()) {
            return R.FAILURE(bktypeR.getMessage());
        }

        R bkstatusR = checkDictItem("dbbkstatus", re.getDbbkstatusstr());
        if (bkstatusR.isFailed()) {
            return R.FAILURE(bkstatusR.getMessage());
        }

        R bkmethodR = checkDictItem("dbbkmethod", re.getDbbkmethodstr());
        if (archtypeR.isFailed()) {
            return R.FAILURE(bkmethodR.getMessage());
        }

        if (ToolUtil.isEmpty(id)) {
            // 新增
            Insert me = new Insert("ops_node_item");
            me.set("id", db.getUUID());
            me.set("type", "dbinstance");
            me.setIf("mark", re.getMark());
            me.setIf("bkstrategy", re.getBkstrategy());
            me.setIf("bkkeep", re.getBkkeep());
            me.setIf("dbinstance", re.getDbinstance());
            me.set("dr", "0");
            me.set("value", importlabel);
            me.setIf("dsize", re.getDsize());

            // 获取nid
            if (ToolUtil.isOneEmpty(re.getIp(), re.getXtname())) {
                return R.FAILURE("不存在该值,名称:" + re.getXtname() + ",IP:" + re.getIp());
            }

            RcdSet rs = db.query("select * from ops_node where dr='0' and ip=? and name=?", re.getIp(), re.getXtname());
            if (rs.size() == 1) {
                me.setIf("nid", rs.getRcd(0).getString("id"));
            } else {
                return R.FAILURE("不存在该值,名称:" + re.getXtname() + ",IP:" + re.getIp());
            }

            me.setIf("archtype", archtypeR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("bktype", bktypeR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("bkstatus", bkstatusR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("bkmethod", bkmethodR.queryDataToJSONObject().getString("dict_item_id"));

            sql = me.getSQL();

        } else {
            Update me = new Update("ops_node_item");
            me.set("value", importlabel);
            me.setIf("dsize", re.getDsize());
            me.setIf("mark", re.getMark());
            me.setIf("bkstrategy", re.getBkstrategy());
            me.setIf("bkkeep", re.getBkkeep());
            me.setIf("dbinstance", re.getDbinstance());
            me.setIf("archtype", archtypeR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("bktype", bktypeR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("bkstatus", bkstatusR.queryDataToJSONObject().getString("dict_item_id"));
            me.setIf("bkmethod", bkmethodR.queryDataToJSONObject().getString("dict_item_id"));
            me.where().and("id=?", re.getId());
            sql = me.getSQL();
        }

        return R.SUCCESS_OPER(sql);
    }
}
