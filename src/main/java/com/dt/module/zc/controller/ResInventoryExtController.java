package com.dt.module.zc.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.controller.FileUpDownController;
import com.dt.module.base.entity.SysFiles;
import com.dt.module.base.service.ISysFilesService;
import com.dt.module.zc.entity.*;
import com.dt.module.zc.service.IResInventoryItemSService;
import com.dt.module.zc.service.IResInventoryItemService;
import com.dt.module.zc.service.IResInventoryService;
import com.dt.module.zc.service.IResInventoryUserService;
import com.dt.module.zc.service.impl.ResInventoryImportService;
import com.dt.module.zc.service.impl.ResInventoryService;
import com.dt.module.zc.service.impl.AssetsConstant;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-05-14
 */
@Controller
@RequestMapping("/api/zc/resInventory/ext")
public class ResInventoryExtController extends BaseController {

    @Autowired
    ResInventoryImportService resInventoryImportService;


    @Autowired
    ISysFilesService SysFilesServiceImpl;

    @Autowired
    IResInventoryService ResInventoryServiceImpl;

    @Autowired
    IResInventoryUserService ResInventoryUserServiceImpl;

    @Autowired
    IResInventoryItemService ResInventoryItemServiceImpl;

    @Autowired
    IResInventoryItemSService ResInventoryItemSServiceImpl;

    @Autowired
    ResInventoryService resInventoryService;


//    /**
//     * @Description:查询盘点
//     */
//    @ResponseBody
//    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
//    @RequestMapping(value = "/selectById.do")
//    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
//
//        ResInventory r = ResInventoryServiceImpl.getById(id);
//        JSONObject res = JSONObject.parseObject(JSON.toJSONString(r, SerializerFeature.WriteDateUseDateFormat,
//                SerializerFeature.DisableCircularReferenceDetect));
//
//        String sql = "select " + AssetsConstant.resSqlbody + " t.* from res_inventory_item t where dr=0 and pdid=?";
//        res.put("items", db.query(sql, id).toJsonArrayWithJsonObject());
//        return R.SUCCESS_OPER(res);
//    }


    /**
     * @Description:判断该资产是否在本次巡检批次中
     * @param pdid 本次盘点ID
     * @param resid 资产ID
     */
    @ResponseBody
    @Acl(info = "判断该资产是否在本次巡检批次中", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryResInInventory.do")
    public R selectById(String resid,String pdid) {
        QueryWrapper<ResInventoryItem> q=new QueryWrapper<>();
        q.eq("pdid",pdid);
        q.eq("resid",resid);
        int cnt=ResInventoryItemServiceImpl.count(q);
        JSONObject r=new JSONObject();
        if
        (cnt>0){
            r.put("result",true);
        }else{
            r.put("result",false);
        }
        return R.SUCCESS_OPER(r);
    }


    /**
     * @Description:同步盘点数据
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/syncdata.do")
    public R syncdata(String id) {

        ResInventory ri = ResInventoryServiceImpl.getById(id);
        if (ri == null) {
            return R.FAILURE_NO_DATA();
        }
        if (!ResInventoryService.INVENTORY_STATAUS_FINISH.equals(ri.getStatus())) {
            return R.FAILURE("当前单据单状态错误,不能进行同步数据操作!");
        }
        if ("1".equals(ri.getSyncstatus())) {
            return R.FAILURE("数据已同步,不需要重复操作.");
        }

        UpdateWrapper<ResInventory> uw = new UpdateWrapper<ResInventory>();
        uw.set("syncstatus", "1");
        uw.eq("id", id);
        ResInventoryServiceImpl.update(uw);
        String sql1 = "update res a,res_inventory_item b   " +
                "set a.lastinventorytime=b.resenddate " +
                "where a.id=b.resid   ";
        db.execute(sql1);
        return R.SUCCESS_OPER();

    }

    /**
     * @Description:查询盘点数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        String sql="select \n" +
                "(select count(1) from res_inventory_item rii where dr='0' and pdid=t.id and pdstatus='wait' ) waitcnt,\n" +
                "(select count(1) from res_inventory_item rii where dr='0' and pdid=t.id and pdstatus='finish_plus' ) finishpluscnt,\n" +
                "(select count(1) from res_inventory_item rii where dr='0' and pdid=t.id and pdstatus='finish_loss' ) finishlosscnt,\n" +
                "(select count(1) from res_inventory_item rii where dr='0' and pdid=t.id and pdstatus='finish_diff' ) finishdiffcnt,\n" +
                "(select count(1) from res_inventory_item rii where dr='0' and pdid=t.id and pdstatus='finish_normal' ) finishnormalcnt,\n" +
                "t.*\n" +
                "from res_inventory t where dr='0' \n";
        if(ToolUtil.isNotEmpty(search)) {
            sql = sql + " and t.name like '%" + search + "%' ";
        }
        sql=sql+" order by create_time desc " ;
        return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());

    }

    /**
     * @Description:生产盘点单据
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(ResInventory entity, String category) {

        entity.setStatus(ResInventoryService.INVENTORY_STATAUS_WAIT);
        ResInventoryServiceImpl.saveOrUpdate(entity);
        QueryWrapper<ResInventory> ew = new QueryWrapper<ResInventory>();
        ew.and(i -> i.eq("batchid", entity.getBatchid()));
        ResInventory obj = ResInventoryServiceImpl.getOne(ew);

        //处理用户
        QueryWrapper<ResInventoryUser> userqw = new QueryWrapper<ResInventoryUser>();
        userqw.and(i -> i.eq("pdid", obj.getId()));
        ResInventoryUserServiceImpl.remove(userqw);
        String pduserstr = entity.getPduserdata();
        ArrayList<ResInventoryUser> items = new ArrayList<ResInventoryUser>();
        if (ToolUtil.isNotEmpty(pduserstr)) {
            JSONArray pduserarr = JSONArray.parseArray(pduserstr);
            for (int i = 0; i < pduserarr.size(); i++) {
                ResInventoryUser e = new ResInventoryUser();
                e.setUserid(pduserarr.getJSONObject(i).getString("user_id"));
                e.setUsername(pduserarr.getJSONObject(i).getString("name"));
                e.setPdid(obj.getId());
                items.add(e);
            }
            ResInventoryUserServiceImpl.saveBatch(items);
        }


        //处理盘点单明细
        R itemsR = resInventoryService.setInventoryRange(entity, category);
        JSONArray arr = itemsR.queryDataToJSONArray();
        ArrayList<ResInventoryItem> itemlist = new ArrayList<ResInventoryItem>();
        ArrayList<ResInventoryItemS> itemslist = new ArrayList<ResInventoryItemS>();
        if (arr.size() > 0) {
            for (int i = 0; i < arr.size(); i++) {
                ResInventoryItem e1 = new ResInventoryItem();
                e1.setPdbatchid(obj.getBatchid());
                e1.setPdid(obj.getId());
                e1.setResid(arr.getJSONObject(i).getString("id"));
                e1.setPdstatus(ResInventoryService.INVENTORY_ITEM_STATAUS_WAIT);
                e1.setPdsyncneed(ResInventoryService.INVENTORY_ITEM_ACTION_NOSYNC);

                ResInventoryItemS e2 = new ResInventoryItemS();
                e2.setPdbatchid(obj.getBatchid());
                e2.setPdid(obj.getId());
                e2.setResid(arr.getJSONObject(i).getString("id"));
                itemlist.add(e1);
                itemslist.add(e2);
            }

            obj.setCnt(new BigDecimal(itemlist.size()));
            ResInventoryServiceImpl.saveOrUpdate(obj);
            //批量更新主要数据
            ResInventoryItemServiceImpl.saveBatch(itemlist);
            ResInventoryItemSServiceImpl.saveBatch(itemslist);


            String sql2 = "update res_inventory_item_s a,res b   " +
                    "set a.category=b.category,   " +
                    "a.class_id=b.class_id,   " +
                    "a.type=b.type,   " +
                    "a.gj_dl=b.gj_dl,   " +
                    "a.gj_xl=b.gj_xl,   " +
                    "a.uuid=b.uuid,   " +
                    "a.name=b.name,   " +
                    "a.zcsource=b.zcsource,   " +
                    "a.model=b.model,   " +
                    "a.sn=b.sn,   " +
                    "a.version=b.version,   " +
                    "a.res_desc=b.res_desc,   " +
                    "a.brand=b.brand,   " +
                    "a.supplier=b.supplier,   " +
                    "a.recycle=b.recycle,   " +
                    "a.prerecycle=b.prerecycle,   " +
                    "a.env=b.env,   " +
                    "a.risk=b.risk,   " +
                    "a.buy_time=b.buy_time,   " +
                    "a.offline_time=b.offline_time,   " +
                    "a.online_time=b.online_time,   " +
                    "a.ip=b.ip,   " +
                    "a.rwm=b.rwm,   " +
                    "a.confdesc=b.confdesc,   " +
                    "a.loc=b.loc,   " +
                    "a.locshow=b.locshow,   " +
                    "a.locdtl=b.locdtl,   " +
                    "a.rack=b.rack,   " +
                    "a.frame=b.frame,   " +
                    "a.belong_company_id=b.belong_company_id,   " +
                    "a.belong_part_id=b.belong_part_id,   " +
                    "a.used_company_id=b.used_company_id,   " +
                    "a.part_id=b.part_id,   " +
                    "a.used_userid=b.used_userid,   " +
                    "a.mgr_part_id=b.mgr_part_id,   " +
                    "a.maintain_userid=b.maintain_userid,   " +
                    "a.headuserid=b.headuserid,   " +
                    "a.buy_price=b.buy_price,   " +
                    "a.net_worth=b.net_worth,   " +
                    "a.zc_cnt=b.zc_cnt,   " +
                    "a.actionstatus=b.actionstatus,   " +
                    "a.wb=b.wb,   " +
                    "a.wb_auto=b.wb_auto,   " +
                    "a.wbout_date=b.wbout_date,   " +
                    "a.wbsupplier=b.wbsupplier,   " +
                    "a.wbct=b.wbct,   " +
                    "a.status=b.status,   " +
                    "a.changestatus=b.changestatus,   " +
                    "a.importlabel=b.importlabel,   " +
                    "a.img=b.img,   " +
                    "a.attach=b.attach,   " +
                    "a.mark=b.mark,   " +
                    "a.changestate=b.changestate,   " +
                    "a.review_userid=b.review_userid,   " +
                    "a.review_date=b.review_date,   " +
                    "a.fs1=b.fs1,   " +
                    "a.fs2=b.fs2,   " +
                    "a.fs3=b.fs3,   " +
                    "a.fs4=b.fs4,   " +
                    "a.fs5=b.fs5,   " +
                    "a.fs6=b.fs6,   " +
                    "a.fs7=b.fs7,   " +
                    "a.fs8=b.fs8,   " +
                    "a.fs9=b.fs9,   " +
                    "a.fs10=b.fs10,   " +
                    "a.fs11=b.fs11,   " +
                    "a.fs12=b.fs12,   " +
                    "a.fs13=b.fs13,   " +
                    "a.fs14=b.fs14,   " +
                    "a.fs15=b.fs15,   " +
                    "a.fs16=b.fs16,   " +
                    "a.fs17=b.fs17,   " +
                    "a.fs18=b.fs18,   " +
                    "a.fs19=b.fs19,   " +
                    "a.fs20=b.fs20,   " +
                    "a.fi1=b.fi1,   " +
                    "a.fi2=b.fi2,   " +
                    "a.fi3=b.fi3,   " +
                    "a.fi4=b.fi4,   " +
                    "a.fi5=b.fi5,   " +
                    "a.fi6=b.fi6,   " +
                    "a.fi7=b.fi7,   " +
                    "a.fi8=b.fi8,   " +
                    "a.fi9=b.fi9,   " +
                    "a.fi10=b.fi10,   " +
                    "a.fi11=b.fi11,   " +
                    "a.fi12=b.fi12,   " +
                    "a.fi13=b.fi13,   " +
                    "a.fi14=b.fi14,   " +
                    "a.fi15=b.fi15,   " +
                    "a.fi16=b.fi16,   " +
                    "a.fi17=b.fi17,   " +
                    "a.fi18=b.fi18,   " +
                    "a.fi19=b.fi19,   " +
                    "a.fi20=b.fi20,   " +
                    "a.fd1=b.fd1,   " +
                    "a.fd2=b.fd2,   " +
                    "a.fd3=b.fd3   " +
                    "where a.resid=b.id";
            db.executes(sql2);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:取消盘点
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/cancel.do")
    public R cancel(String id) {
        ResInventory item = ResInventoryServiceImpl.getById(id);
        if (item == null) {
            return R.FAILURE_NO_DATA();
        }
        if (ResInventoryService.INVENTORY_STATAUS_DOING.equals(item.getStatus()) ||
                ResInventoryService.INVENTORY_STATAUS_WAIT.equals(item.getStatus())) {
            UpdateWrapper<ResInventory> uw = new UpdateWrapper<ResInventory>();
            uw.set("status", ResInventoryService.INVENTORY_STATAUS_CANCEL);
            uw.eq("id", id);
            ResInventoryServiceImpl.update(uw);
        } else {
            return R.FAILURE("当前状态不允许取消盘点单");
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:删除盘点
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(String id) {
        ResInventory item = ResInventoryServiceImpl.getById(id);
        if (item == null) {
            return R.FAILURE_NO_DATA();
        }
        if (ResInventoryService.INVENTORY_STATAUS_CANCEL.equals(item.getStatus()) || ResInventoryService.INVENTORY_STATAUS_DOING.equals(item.getStatus()) ||
                ResInventoryService.INVENTORY_STATAUS_WAIT.equals(item.getStatus())) {
            ResInventoryServiceImpl.removeById(id);
        } else {
            return R.FAILURE("当前状态不允许删除盘点单");
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:指定盘点人员
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/assignusers.do")
    public R assignusers(String id, String pduserdata, String pduserlist) {

        ResInventory obj = ResInventoryServiceImpl.getById(id);
        if (obj == null) {
            return R.FAILURE_NO_DATA();
        }
        UpdateWrapper<ResInventory> uw = new UpdateWrapper<ResInventory>();
        uw.set("pduserlist", pduserlist);
        uw.set("pduserdata", pduserdata);
        uw.eq("id", id);
        ResInventoryServiceImpl.update(uw);


        QueryWrapper<ResInventoryUser> userqw = new QueryWrapper<ResInventoryUser>();
        userqw.and(i -> i.eq("pdid", id));
        ResInventoryUserServiceImpl.remove(userqw);
        String pduserstr = pduserdata;
        ArrayList<ResInventoryUser> items = new ArrayList<ResInventoryUser>();
        if (ToolUtil.isNotEmpty(pduserstr)) {
            JSONArray pduserarr = JSONArray.parseArray(pduserstr);
            for (int i = 0; i < pduserarr.size(); i++) {
                ResInventoryUser e = new ResInventoryUser();
                e.setUserid(pduserarr.getJSONObject(i).getString("user_id"));
                e.setUsername(pduserarr.getJSONObject(i).getString("name"));
                e.setPdid(obj.getId());
                items.add(e);
            }
            ResInventoryUserServiceImpl.saveBatch(items);
        }
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:查询盘点资产
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/queryInventoryRes.do")
    public R queryInventoryRes(ResInventory entity, String category) {
        return resInventoryService.setInventoryRange(entity, category);
    }


    /**
     * @Description:结束盘点
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/finish.do")
    public R finish(String id) {
        QueryWrapper<ResInventoryItem> q=new QueryWrapper<>();
        q.eq("pdstatus", "wait");
        q.eq("pdid",id);
        if(ResInventoryItemServiceImpl.count(q)>0){
            return R.FAILURE("有资产未进行盘点，不能结束。");
        }else{
            UpdateWrapper<ResInventory> uw=new UpdateWrapper<>();
            uw.setSql("resenddate=now()");
            uw.set("status",ResInventoryService.INVENTORY_STATAUS_FINISH);
            ResInventoryServiceImpl.update(uw);
        }
        return R.SUCCESS_OPER();
    }
    /**
     * @Description:手工盘点
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/manualInventoryRes.do")
    public R manualInventoryRes(String fileid, String id, HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        SysFiles fileobj = SysFilesServiceImpl.getById(fileid);
        String fileurl = fileobj.getPath();
        String filePath = FileUpDownController.getWebRootDir() + ".." + File.separatorChar + fileurl;

        ResInventory ri = ResInventoryServiceImpl.getById(id);
        if (ri == null) {
            return R.FAILURE_NO_DATA();
        }
        if (!ResInventoryService.INVENTORY_STATAUS_DOING.equals(ri.getStatus())) {
            return R.FAILURE("当前单据单状态错误,不能进行盘点操作!");
        }

        R r = R.SUCCESS_OPER();
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setTitleRows(0);
            params.setStartSheetIndex(0);
            List<ResInventoryEntity> result = ExcelImportUtil.importExcel(new File(filePath), ResInventoryEntity.class, params);
            if (result.size() > 0) {
                if (!result.get(0).getPdbatchid().equals(ri.getBatchid())) {
                    return R.FAILURE("导入的盘点单据错误!");
                }
            }
            r = resInventoryImportService.executeEntitysImport(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.FAILURE("导入数据异常");
        }
        //处理状态
        if ("0".equals(db.uniqueRecord("select count(1) cnt from res_inventory_item where dr='0' and pdstatus<>'" + ResInventoryService.INVENTORY_STATAUS_FINISH + "' and pdid=?", id).getString("cnt"))) {
            //更新状态
            Date date = new Date(); // 获取一个Date对象
            DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
            String nowtime = simpleDateFormat.format(date);
            UpdateWrapper<ResInventory> uw = new UpdateWrapper<ResInventory>();
            uw.set("status", ResInventoryService.INVENTORY_STATAUS_FINISH);
            uw.set("finishtime", nowtime);
            uw.eq("id", id);
            ResInventoryServiceImpl.update(uw);
        }
        return r;

    }


    /**
     * @Description:查询我的巡检
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/myList.do")
    public R myList(String statustype) {
        QueryWrapper<ResInventory> qw = new QueryWrapper<ResInventory>();
        qw.inSql("id","select pdid from res_inventory_user riu where userid='"+this.getUserId()+"' and dr='0'");
        if (ToolUtil.isNotEmpty(statustype)) {
            if ("finish".equals(statustype)) {
                qw.eq("status",ResInventoryService.INVENTORY_STATAUS_FINISH);
            } else if ("inprogress".equals(statustype)) {
                qw.in("status", ResInventoryService.INVENTORY_STATAUS_DOING,ResInventoryService.INVENTORY_STATAUS_WAIT);
            }
        }
        qw.orderByDesc("create_time");
        List<ResInventory> list = ResInventoryServiceImpl.list(qw);
        return R.SUCCESS_OPER(list);
    }

    /**
     * @Description:盘点动作
     */
    @ResponseBody
    @Acl(info = "盘点", value = Acl.ACL_USER)
    @RequestMapping(value = "/inventoryAction.do")
    public R inventoryAction(String pdid,String resid,String status,String pdmark) {
        if(ToolUtil.isOneEmpty(pdid,resid,status)){
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        ResInventory inv=ResInventoryServiceImpl.getById(pdid);
        if(ResInventoryService.INVENTORY_STATAUS_DOING.equals(inv.getStatus())||ResInventoryService.INVENTORY_STATAUS_WAIT.equals(inv.getStatus())){
           if(ResInventoryService.INVENTORY_STATAUS_WAIT.equals(inv.getStatus())){
               UpdateWrapper<ResInventory> u=new UpdateWrapper<>();
               u.set("status",ResInventoryService.INVENTORY_STATAUS_DOING);
               u.setSql("resstartdate=now()");
               u.eq("id",pdid);
               ResInventoryServiceImpl.update(u);
            }
        }else{
            return R.FAILURE("当前状态异常,无法继续操作");
        }

        UpdateWrapper<ResInventoryItem> uw=new UpdateWrapper<>();
        uw.set("pduserid",this.getUserId());
        uw.set("pdusername",this.getName());
        uw.setSql("pdtime=now()");
        uw.set("pdstatus",status);
        uw.set(ToolUtil.isNotEmpty(pdmark),"pdmark",pdmark);
        uw.eq("pdid",pdid);
        uw.eq("resid",resid);
        ResInventoryItemServiceImpl.update(uw);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:盘盈
     */
    @ResponseBody
    @Acl(info = "盘点盘盈", value = Acl.ACL_USER)
    @RequestMapping(value = "/inventoryActionPlus.do")
    public R inventoryPlus(String pdid,String resids) {
        ResInventory obj=ResInventoryServiceImpl.getById(pdid);
        if(obj==null){
            return R.FAILURE_NO_DATA();
        }
        JSONArray resids_arr=JSONArray.parseArray(resids);
        if(resids_arr==null||resids_arr.size()==0){
            return R.FAILURE_REQ_PARAM_ERROR();
        }

        List<ResInventoryItem> list=new ArrayList<>();
        for(int i=0;i<resids_arr.size();i++){
            String resid=resids_arr.getString(i);
            QueryWrapper<ResInventoryItem> q=new QueryWrapper<>();
            q.eq("pdid",obj.getId());
            q.eq("resid",resid);
            if(ResInventoryItemServiceImpl.getOne(q)!=null){
                continue;
            }
            ResInventoryItem e=new ResInventoryItem();
            e.setPdbatchid(obj.getBatchid());
            e.setPdid(obj.getId());
            e.setResid(resid);
            e.setPdstatus(ResInventoryService.INVENTORY_ITEM_STATAUS_FINISH_PLUS);
            e.setPdsyncneed("0");
            list.add(e);
        }
        ResInventoryItemServiceImpl.saveOrUpdateBatch(list);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:盘点追加资产
     */
    @ResponseBody
    @Acl(info = "盘点追加资产", value = Acl.ACL_USER)
    @RequestMapping(value = "/inventoryActionAdd.do")
    public R inventoryActionAdd(String pdid,String resids) {
        ResInventory obj=ResInventoryServiceImpl.getById(pdid);
        if(obj==null){
            return R.FAILURE_NO_DATA();
        }
        JSONArray resids_arr=JSONArray.parseArray(resids);
        if(resids_arr==null||resids_arr.size()==0){
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        List<ResInventoryItem> list=new ArrayList<>();
        for(int i=0;i<resids_arr.size();i++){
            String resid=resids_arr.getString(i);
            QueryWrapper<ResInventoryItem> q=new QueryWrapper<>();
            q.eq("pdid",obj.getId());
            q.eq("resid",resid);
            if(ResInventoryItemServiceImpl.getOne(q)!=null){
                continue;
            }
            ResInventoryItem e=new ResInventoryItem();
            e.setPdbatchid(obj.getBatchid());
            e.setPdid(obj.getId());
            e.setResid(resid);
            e.setPdstatus(ResInventoryService.INVENTORY_ITEM_STATAUS_WAIT);
            e.setPdsyncneed("0");
            list.add(e);
        }
        ResInventoryItemServiceImpl.saveOrUpdateBatch(list);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:盘点明细数据
     * @param pdid :盘点主键
     * @param resid :资产ID
     */

    @ResponseBody
    @Acl(info = "获取一个盘点资产的数据", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectInventoryItemDetail.do")
    public R selectInventoryItemDetail(String resid,String pdid) {
        QueryWrapper<ResInventoryItem> q=new QueryWrapper<>();
        q.eq("pdid",pdid);
        q.eq("resid",resid);
        return R.SUCCESS_OPER(ResInventoryItemServiceImpl.getOne(q));
    }

    /**
     * @Description:盘点明细数据
     * @param id :盘点主键
     * @param status :资产明细状态
     * @param simple :是否输出详细资产明细
     */
    @ResponseBody
    @Acl(info = "盘点明细数据", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectInventoryDetail.do")
    public R selectInventoryDetail(String id,String status,String simple) {
        if(ToolUtil.isOneEmpty(id,status)){
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        ResInventory in = ResInventoryServiceImpl.getById(id);
        String sql="select \n" +
                "max(case pdstatus when 'finish_diff' then cnt else 0 end)finish_diff_cnt,\n" +
                "max(case pdstatus when 'wait' then cnt else 0 end) wait_cnt,\n" +
                "max(case pdstatus when 'finish_plus' then cnt else 0 end)finish_plus_cnt,\n" +
                "max(case pdstatus when 'finish_loss' then cnt else 0 end)finish_loss_cnt,\n" +
                "max(case pdstatus when 'finish_normal' then cnt else 0 end)finish_normal_cnt,\n" +
                "max(case pdstatus when 'total' then cnt else 0 end)total\n" +
                "from \n" +
                "(\n" +
                "select coalesce(pdstatus,\"total\") pdstatus,sum(cnt) cnt from (\n" +
                "select pdstatus, count(1) cnt from res_inventory_item where dr='0' and pdid=? group by pdstatus \n" +
                "union all \n" +
                "select 'wait' pdstatus,0 cnt\n" +
                "union all \n" +
                "select 'finish_plus' pdstatus, 0 cnt\n" +
                "union all \n" +
                "select 'finish_loss' pdstatus, 0 cnt\n" +
                "union all \n" +
                "select 'finish_diff' pdstatus, 0 cnt\n" +
                "union all \n" +
                "select 'finish_normal' pdstatus, 0 cnt\n" +
                ")tab group by pdstatus with rollup\n" +
                ")inv" ;
        JSONObject res = JSONObject.parseObject(JSON.toJSONString(in, SerializerFeature.WriteDateUseDateFormat));
        res.put("statistics",db.uniqueRecord(sql,in.getId()).toJsonObject());
        String sqlitem="";
        if(ToolUtil.isNotEmpty(simple)&&"N".equals(simple)){
            sqlitem="select "+AssetsConstant.resSqlbody+" t.*,a.pdstatus,a.pdusername,pdtime from (select dr,resid, pdid,pdstatus,pdusername,pdtime from res_inventory_item) a,res t where a.dr='0' and a.pdid=? and a.resid = t.id";
        }else{
            sqlitem="select t.name,t.id,t.uuid,t.model,a.pdstatus,a.pdusername,pdtime from res_inventory_item a,res t where a.dr='0' and a.pdid=? and a.resid = t.id";
        }
        if(ToolUtil.isNotEmpty(status)){
            sqlitem=sqlitem+" and a.pdstatus='"+status+"'";
        }
        res.put("items", ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sqlitem, in.getId()).toJsonArrayWithJsonObject()));
        return R.SUCCESS_OPER(res);
    }


    /**
     * @Description:下载盘点资产
     */
    @ResponseBody
    @Acl(info = "", value = Acl.ACL_USER)
    @RequestMapping(value = "/downloadInventoryRes.do")
    public void downloadInventoryRes(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();

        String sql="select \n" +AssetsConstant.resSqlbody+
                " case \n" +
                "when a.pdstatus='wait' then '待盘'\n" +
                "when a.pdstatus='finish_plus' then '盘盈'\n" +
                "when a.pdstatus='finish_loss' then '盘亏'\n" +
                "when a.pdstatus='finish_diff' then '差异'\n" +
                "when a.pdstatus='finish_normal' then '正常' else '' end pdstatusstr,\n" +
                "t.*,\n" +
                "a.pdstatus,a.pdusername,a.pdbatchid from res_inventory_item a,res t where a.pdid=? and a.dr ='0' and a.resid=t.id order by 1";
        JSONArray data = ConvertUtil.OtherJSONObjectToFastJSONArray(db.query(sql, ps.getString("id")).toJsonArrayWithJsonObject());
        List<ResInventoryEntity> data_excel = new ArrayList<ResInventoryEntity>();
        for (int i = 0; i < data.size(); i++) {
            ResInventoryEntity entity = new ResInventoryEntity();
            entity.fullResEntity(data.getJSONObject(i));
            data_excel.add(entity);
        }

        ExportParams parms = new ExportParams();
        parms.setSheetName("盘点明细");
        parms.setHeaderHeight(1000);

        Workbook workbook;
        workbook = ExcelExportUtil.exportExcel(parms, ResInventoryEntity.class, data_excel);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filedisplay = "inventory.xls";
        filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        try {
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

