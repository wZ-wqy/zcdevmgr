package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.ResAllocate;
import com.dt.module.zc.entity.ResAllocateItem;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.service.IResAllocateItemService;
import com.dt.module.zc.service.IResAllocateService;
import com.dt.module.zc.service.IResChangeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-04-25
 */
@Service
public class ResAllocateService extends BaseService {

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResAllocateService ResAllocateServiceImpl;

    @Autowired
    IResAllocateItemService ResAllocateItemServiceImpl;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    AssetsOperService assetsOperService;

    /**
     * @Description:启动流程
     */
    public R startFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResAllocate> ups = new UpdateWrapper<ResAllocate>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("uuid", uuid);
            ResAllocateServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResAllocate> ups = new UpdateWrapper<ResAllocate>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("uuid", uuid);
            ResAllocateServiceImpl.update(ups);
            confirm(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消单据
     */
    public R cancel(String busid, String status) {
        String sql2 = "update res_allocate_item a,res b set " +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
                "where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);
        UpdateWrapper<ResAllocate> ups = new UpdateWrapper<ResAllocate>();
        ups.set("status", status);
        ups.eq("uuid", busid);
        ResAllocateServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        String ct = "无";
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_DB);
        q.eq("busuuid",busid);

        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++){
            ResChangeItem entity=list.get(j);
            String busuuid = entity.getBusuuid();
            String resid = entity.getResid();
            String sql = "select  " +
                    "(select route_name from hrm_org_part where node_id=b.fcompid) fusedcompanyname,  " +
                    "(select route_name from hrm_org_part where node_id=b.tousedcompid) tusedcompanyname,  " +
                    "(select name from sys_dict_item where dr='0' and dict_item_id=b.floc) flocstr,  " +
                    "(select name from sys_dict_item where dr='0' and dict_item_id=b.toloc) tlocstr,  " +
                    "  b.*  " +
                    "from res_allocate_item b where dr='0' and b.busuuid=? and b.resid=?";
            Rcd rs = db.uniqueRecord(sql, busuuid, resid);
            if (rs != null) {
                String fusedcompanyname = rs.getString("fusedcompanyname");
                String tusedcompanyname = rs.getString("tusedcompanyname");
                String flocstr = rs.getString("flocstr");
                String tlocstr = rs.getString("tlocstr");
                String flocdtl = rs.getString("flocdtl");
                String tolocdtl = rs.getString("tolocdtl");
                ct = "确认调拨!【使用公司】字段由 \"" + fusedcompanyname + "\" 变更为 \"" + tusedcompanyname + "\"";
                ct = ct + ";【存放区域】字段由 \"" + flocstr + "\" 变更为 \"" + tlocstr + "\"";
                ct = ct + ";【位置】字段由 \"" + flocdtl + "\" 变更为 \"" + tolocdtl + "\"";
                ct = ct + ";【状态】字段由 \"调拨中\" 变更为 \"闲置\"";
            }
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }

        return R.SUCCESS_OPER();
    }

    /**
     * @Description:确认单据
     */
    public R confirm(String busid, String status) {

        UpdateWrapper<ResAllocate> ups = new UpdateWrapper<ResAllocate>();
        ups.set("status", status);
        ups.eq("uuid", busid);
        ResAllocateServiceImpl.update(ups);
        String sql2 = "update res_allocate_item a,res b set a.update_by='" + this.getUserId() + "'";
        sql2 = sql2 + ",b.recycle=a.frecycle";
        sql2 = sql2 + ",b.locdtl=a.tolocdtl";
        sql2 = sql2 + ",b.loc=a.toloc";
        sql2 = sql2 + ",b.inprocess='0'";
        sql2 = sql2 + ",b.inprocessuuid=''";
        sql2 = sql2 + ",b.inprocesstype=''";
        sql2 = sql2 + ",a.acttime=now()";
        sql2 = sql2 + ",b.belong_company_id=a.tobelongcompid";
        sql2 = sql2 + " where a.resid=b.id and a.busuuid=? and a.dr='0'";
        db.execute(sql2, busid);
        String sql4 = "update res_allocate a set acttime=now() where uuid=?";
        db.execute(sql4, busid);

        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResAllocateItem> qw = new QueryWrapper<ResAllocateItem>();
        qw.and(i -> i.eq("busuuid", busid));
        List<ResAllocateItem> items = ResAllocateItemServiceImpl.list(qw);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_DB);
            e.setCt("资产调拨");
            e.setFillct("1");
            e.setCreateBy(this.getUserId());
            e.setCdate(new Date());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:创建单据
     */
    public R save(ResAllocate entity, String items) {
        String id = "";
        String uuid = "";
        if (ToolUtil.isNotEmpty(entity.getId())) {
            id = entity.getId();
            uuid = entity.getUuid();
        } else {
            ArrayList<ResAllocateItem> cols = new ArrayList<ResAllocateItem>();
            uuid = assetsOperService.createUuid(AssetsConstant.UUID_DB);
            entity.setUuid(uuid);
            entity.setStatus(FlowConstant.PSTATUS_APPLY);
            ResAllocateServiceImpl.saveOrUpdate(entity);

//            QueryWrapper<ResAllocate> ew = new QueryWrapper<ResAllocate>();
//            String finalUuid = uuid;
//            ew.and(i -> i.eq("uuid", finalUuid));
//            ResAllocate dbobj = ResAllocateServiceImpl.getOne(ew);

            JSONArray arr = JSONArray.parseArray(items);
            for (int i = 0; i < arr.size(); i++) {
                ResAllocateItem e = new ResAllocateItem();
                e.setBusuuid(uuid);
                e.setResid(arr.getJSONObject(i).getString("id"));
                e.setFrecycle(arr.getJSONObject(i).getString("recycle"));

                e.setFcompid(entity.getFcompid());
                e.setFcompname(entity.getFcompname());

                e.setFrombelongcompid(entity.getFrombelongcompid());
                e.setFrombelongcompname(entity.getFrombelongcompname());
                e.setTobelongcompid(entity.getTobelongcompid());
                e.setTobelongcompname(entity.getTobelongcompname());

                e.setTousedcompid(entity.getTousedcompid());
                e.setTousedcompname(entity.getTousedcompname());


                e.setBusdate(entity.getBusdate());
                e.setToloc(entity.getToloc());
                e.setTolocname(entity.getTolocname());
                e.setTolocdtl(entity.getTolocdtl());
                cols.add(e);

                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.set("inprocess", "1");
                ups.set("inprocessuuid", uuid);
                ups.set("inprocesstype", AssetsConstant.ASSETS_BUS_TYPE_DB);
                ups.eq("id", arr.getJSONObject(i).getString("id"));
                ResServiceImpl.update(ups);
            }
            ResAllocateItemServiceImpl.saveBatch(cols);
        }
        JSONObject r = new JSONObject();
        r.put("busid", uuid);
        return R.SUCCESS_OPER(r);

    }


    /**
     * @Description:结束流程
     */
    public R finishFlow(String busid, String status) {
        if (FlowConstant.PSTATUS_DTL_FAILED.equals(status)) {
            return cancel(busid, FlowConstant.PSTATUS_DTL_FAILED);
        } else if (FlowConstant.PSTATUS_DTL_SUCCESS.equals(status)) {
            return confirm(busid, FlowConstant.PSTATUS_DTL_SUCCESS);
        } else {
            return R.FAILURE_NO_DATA();
        }
    }

    /**
     * @Description:查询单据列表
     */
    public R selectList(String user_id, String statustype) {
        QueryWrapper<ResAllocate> ew = new QueryWrapper<ResAllocate>();
        if (ToolUtil.isNotEmpty(user_id)) {
            ew.eq("create_by", user_id);
        }
        if ("finish".equals(statustype)) {
            ew.in("status", FlowConstant.PSTATUS_FINISH, FlowConstant.PSTATUS_FINISH_NO_APPROVAL, FlowConstant.PSTATUS_CANCEL);
        } else if ("inprogress".equals(statustype)) {
            ew.notIn("status", FlowConstant.PSTATUS_FINISH, FlowConstant.PSTATUS_FINISH_NO_APPROVAL, FlowConstant.PSTATUS_CANCEL);
        }
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResAllocateServiceImpl.list(ew));
    }


}
