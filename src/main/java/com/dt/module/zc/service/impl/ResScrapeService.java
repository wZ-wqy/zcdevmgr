package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.AssetsRecycleEnum;
import com.dt.module.cmdb.entity.Res;
import com.dt.module.cmdb.service.IResService;
import com.dt.module.flow.service.impl.FlowConstant;
import com.dt.module.zc.entity.*;
import com.dt.module.zc.service.IResChangeItemService;
import com.dt.module.zc.service.IResScrapeItemService;
import com.dt.module.zc.service.IResScrapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResScrapeService extends BaseService {

    @Autowired
    AssetsOperService assetsOperService;

    @Autowired
    IResService ResServiceImpl;

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResScrapeService ResScrapeServiceImpl;

    @Autowired
    IResScrapeItemService ResScrapeItemServiceImpl;

    /**
     * @Description:启动报废流程
     */
    public R startFlow(String pinst, String uuid, String ifsp) {
        if ("1".equals(ifsp)) {
            UpdateWrapper<ResScrape> ups = new UpdateWrapper<ResScrape>();
            ups.set("pinst", pinst);
            ups.set("status", FlowConstant.PSTATUS_DTL_INAPPROVAL);
            ups.eq("uuid", uuid);
            ResScrapeServiceImpl.update(ups);
        } else if ("0".equals(ifsp)) {
            UpdateWrapper<ResScrape> ups = new UpdateWrapper<ResScrape>();
            ups.set("status", FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
            ups.eq("uuid", uuid);
            ResScrapeServiceImpl.update(ups);
            confirm(uuid, FlowConstant.PSTATUS_DTL_FINISH_NO_APPROVAL);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消报废
     */
    public R cancel(String busid, String status) {
        //更新RES数据
        String sql2 = "update res_scrape_item a,res b set " +
                "b.inprocess='0'," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
                "where a.resid=b.id and a.uuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);
        UpdateWrapper<ResScrape> ups = new UpdateWrapper<ResScrape>();
        ups.set("status", status);
        ups.eq("uuid", busid);
        ResScrapeServiceImpl.update(ups);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:生成报废单据
     */
    public R create(ResScrape entity, String busitimestr, String items) throws ParseException {
        ArrayList<ResScrapeItem> cols = new ArrayList<ResScrapeItem>();
        String id = entity.getId();
        String uuid = "";
        if (ToolUtil.isEmpty(id)) {
            uuid = assetsOperService.createUuid(AssetsConstant.UUID_BF);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(busitimestr);
            entity.setBusidate(date);
            entity.setUuid(uuid);
            entity.setStatus(FlowConstant.PSTATUS_APPLY);
            if (ToolUtil.isEmpty(entity.getProcessuserid())) {
                entity.setProcessuserid(this.getUserId());
            }
            if (ToolUtil.isEmpty(entity.getProcessusername())) {
                entity.setProcessusername(this.getUserName());
            }
            JSONArray itemsarr = JSONArray.parseArray(items);
            for (int i = 0; i < itemsarr.size(); i++) {
                UpdateWrapper<Res> ups = new UpdateWrapper<Res>();
                ups.set("inprocess", "1");
                ups.set("inprocessuuid", uuid);
                ups.set("inprocesstype", AssetsConstant.ASSETS_BUS_TYPE_BF);
                ups.eq("id", itemsarr.getJSONObject(i).getString("id"));
                ResServiceImpl.update(ups);
                ResScrapeItem e = new ResScrapeItem();
                e.setUuid(uuid);
                e.setResid(itemsarr.getJSONObject(i).getString("id"));
                e.setPrestatus(itemsarr.getJSONObject(i).getString("status"));
                cols.add(e);
            }
            entity.setCnt(new BigDecimal(cols.size()));
            ResScrapeItemServiceImpl.saveBatch(cols);
            ResScrapeServiceImpl.save(entity);
        } else {
            return R.FAILURE("参数有误");
        }
        JSONObject res = new JSONObject();
        res.put("busid", uuid);
        return R.SUCCESS_OPER(res);
    }


    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_BF);
        q.eq("busuuid",busid);
        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++) {
            ResChangeItem entity = list.get(j);
            String ct = "";
            ct = "【状态】变更为 \"报废\"";
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:报废确认
     */
    public R confirm(String busid, String status) {

        //保存变更前数据
        UpdateWrapper<ResScrape> ups = new UpdateWrapper<ResScrape>();
        ups.set("status", status);
        ups.eq("uuid", busid);
        ResScrapeServiceImpl.update(ups);

        //更新数据
        String sql2 = "update res_scrape_item a,res b set " +
                "b.recycle='" + AssetsRecycleEnum.RECYCLE_SCRAP.getValue() + "'," +
                "b.inprocess='0'," +
                "b.isscrap='1'," +
                "b.uuidbf=a.uuid," +
                "b.scrapdate=a.create_time," +
                "b.inprocessuuid=''," +
                "b.inprocesstype='' " +
                "where a.resid=b.id and a.uuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, busid);

        //记录资产变更
        QueryWrapper<ResScrapeItem> ew = new QueryWrapper<ResScrapeItem>();
        ew.and(i -> i.eq("uuid", busid));
        List<ResScrapeItem> items = ResScrapeItemServiceImpl.list(ew);
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(busid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_BF);
            e.setFillct("0");
            e.setCdate(new Date());
            e.setCt("资产报废");
            e.setCreateBy(this.getUserId());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(busid);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:结束报废流程
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
     * @Description:查询流程单据数据
     */
    public R selectList(String user_id, String statustype) {
        QueryWrapper<ResScrape> ew = new QueryWrapper<ResScrape>();
        if (ToolUtil.isNotEmpty(user_id)) {
            ew.eq("create_by", user_id);
        }
        if ("finish".equals(statustype)) {
            ew.in("status", FlowConstant.PSTATUS_FINISH, FlowConstant.PSTATUS_FINISH_NO_APPROVAL, FlowConstant.PSTATUS_CANCEL);
        } else if ("inprogress".equals(statustype)) {
            ew.notIn("status", FlowConstant.PSTATUS_FINISH, FlowConstant.PSTATUS_FINISH_NO_APPROVAL, FlowConstant.PSTATUS_CANCEL);
        }
        ew.orderByDesc("create_time");
        return R.SUCCESS_OPER(ResScrapeServiceImpl.list(ew));
    }


}
