package com.dt.module.zc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.zc.entity.ResCBasicinformation;
import com.dt.module.zc.entity.ResCBasicinformationItem;
import com.dt.module.zc.entity.ResChangeItem;
import com.dt.module.zc.service.IResCBasicinformationItemService;
import com.dt.module.zc.service.IResCBasicinformationService;
import com.dt.module.zc.service.IResChangeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResCBasicinformationService extends BaseService {

    @Autowired
    IResChangeItemService ResChangeItemServiceImpl;

    @Autowired
    IResCBasicinformationService ResCBasicinformationServiceImpl;

    @Autowired
    IResCBasicinformationItemService ResCBasicinformationItemServiceImpl;


    /**
     * @Description:根据变更ID查询变更数据
     */
    public R selectByBusid(String uuid) {
        return selectData(uuid, null);
    }

    /**
     * @Description:填充变更数据
     */
    public R fillChangeContent(String busid) {
        QueryWrapper<ResChangeItem> q=new QueryWrapper<>();
        q.eq("type",AssetsConstant.ASSETS_BUS_TYPE_CGJB);
        q.eq("busuuid",busid);
        List<ResChangeItem> list=ResChangeItemServiceImpl.list(q);
        for(int j=0;j<list.size();j++){
            ResChangeItem entity=list.get(j);
            String ct = "无";
            String busuuid = entity.getBusuuid();
            String resid = entity.getResid();
            R res = this.selectData(busuuid, resid);
            JSONArray res_arr = res.queryDataToJSONArray();
            if (res_arr.size() == 1) {
                JSONObject item = res_arr.getJSONObject(0);
                ct = "";
                String tclassidstatus = item.getString("tclassidstatus");
                String tmodelstatus = item.getString("tmodelstatus");
                String tsnstatus = item.getString("tsnstatus");
                String tzcsourcestatus = item.getString("tzcsourcestatus");
                String tzccntstatus = item.getString("tzccntstatus");
                String tsupplierstatus = item.getString("tsupplierstatus");
                String tbrandstatus = item.getString("tbrandstatus");
                String tbuytimestatus = item.getString("tbuytimestatus");
                String tlocstatus = item.getString("tlocstatus");
                String tusefullifestatus = item.getString("tusefullifestatus");
                String tusedcompanyidstatus = item.getString("tusedcompanyidstatus");
                String tpartidstatus = item.getString("tpartidstatus");
                String tuseduseridstatus = item.getString("tuseduseridstatus");
                String tlabel1status = item.getString("tlabel1status");
                String tconfdescstatus = item.getString("tconfdescstatus");
                String tlocdtlstatus = item.getString("tlocdtlstatus");
                String tunitstatus = item.getString("tunitstatus");
                String tfd1status = item.getString("tfd1status");
                String tmarkstatus = item.getString("tmarkstatus");
                String tfs20status = item.getString("tfs20status");
                String fclassfullname = item.getString("fclassfullname");
                String tclassfullname = item.getString("tclassfullname");
                String fmodel = item.getString("fmodel");
                String tmodel = item.getString("tmodel");
                String fsn = item.getString("fsn");
                String tsn = item.getString("tsn");
                String funit = item.getString("funit");
                String tunit = item.getString("tunit");
                String fzccnt = item.getString("fzccnt");
                String tzccnt = item.getString("tzccnt");
                String fsupplierstr = item.getString("fsupplierstr");
                String tsupplierstr = item.getString("tsupplierstr");
                String fbrandstr = item.getString("fbrandstr");
                String tbrandstr = item.getString("tbrandstr");
                String fzcsourcestr = item.getString("fzcsourcestr");
                String tzcsourcestr = item.getString("tzcsourcestr");
                String flocstr = item.getString("flocstr");
                String tlocstr = item.getString("tlocstr");
                String flocdtl = item.getString("flocdtl");
                String tlocdtl = item.getString("tlocdtl");
                String fusefullifestr = item.getString("fusefullifestr");
                String tusefullifestr = item.getString("tusefullifestr");
                String fbuytimestr = item.getString("fbuytimestr");
                String tbuytimestr = item.getString("tbuytimestr");
                String fconfdesc = item.getString("fconfdesc");
                String tconfdesc = item.getString("tconfdesc");
                String fusedcompanyname = item.getString("fusedcompanyname");
                String tusedcompanyname = item.getString("tusedcompanyname");
                String fpartname = item.getString("fpartname");
                String tpartname = item.getString("tpartname");
                String fusedusername = item.getString("fusedusername");
                String tusedusername = item.getString("tusedusername");
                String flabel1 = item.getString("flabel1");
                String tlabel1 = item.getString("tlabel1");
                String ffs20 = item.getString("ffs20");
                String tfs20 = item.getString("tfs20");
                String fmark = item.getString("fmark");
                String tmark = item.getString("tmark");
                String ffd1str = item.getString("ffd1str");
                String tfd1str = item.getString("tfd1str");
                String tname=item.getString("tname");
                String fname=item.getString("fname");
                String tnamestatus=item.getString("tnamestatus");

                String fbelongcompstr=item.getString("fbelongcompstr");
                String tbelongcompstr=item.getString("tbelongcompstr");
                String tbelongcompstatus=item.getString("tbelongcompstatus");

                String flabel2=item.getString("flabel2");
                String tlabel2=item.getString("tlabel2");
                String tlabel2status=item.getString("tlabel2status");

                String fbatch=item.getString("fbatch");
                String tbatch=item.getString("tbatch");
                String tbatchstatus=item.getString("tbatchstatus");

                if (ToolUtil.isNotEmpty(tbatchstatus) && "true".equals(tbatchstatus)) {
                    ct = ct + "【批次号】字段由 \"" + fbatch + "\" 变更为 \"" + tbatch + "\" ;";
                }

                if (ToolUtil.isNotEmpty(tlabel2status) && "true".equals(tlabel2status)) {
                    ct = ct + "【标签2】字段由 \"" + flabel2 + "\" 变更为 \"" + tlabel2 + "\" ;";
                }

                if (ToolUtil.isNotEmpty(tbelongcompstatus) && "true".equals(tbelongcompstatus)) {
                    ct = ct + "【所属公司】字段由 \"" + fbelongcompstr + "\" 变更为 \"" + tbelongcompstr+ "\" ;";
                }


                if (ToolUtil.isNotEmpty(tnamestatus) && "true".equals(tnamestatus)) {
                    ct = ct + "【资产名称】字段由 \"" + fname + "\" 变更为 \"" + tname + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tfd1status) && "true".equals(tfd1status)) {
                    ct = ct + "【生产日期】字段由 \"" + ffd1str + "\" 变更为 \"" + tfd1str + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tmarkstatus) && "true".equals(tmarkstatus)) {
                    ct = ct + "【备注】字段由 \"" + fmark + "\" 变更为 \"" + tmark + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tfs20status) && "true".equals(tfs20status)) {
                    ct = ct + "【其他编号】字段由 \"" + ffs20 + "\" 变更为 \"" + tfs20 + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tclassidstatus) && "true".equals(tclassidstatus)) {
                    ct = ct + "【资产类别】字段由 \"" + fclassfullname + "\" 变更为 \"" + tclassfullname + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tmodelstatus) && "true".equals(tmodelstatus)) {
                    ct = ct + "【规格类型】字段由 \"" + fmodel + "\" 变更为 \"" + tmodel + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tsnstatus) && "true".equals(tsnstatus)) {
                    ct = ct + "【序列】字段由 \"" + fsn + "\" 变更为 \"" + tsn + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tzcsourcestatus) && "true".equals(tzcsourcestatus)) {
                    ct = ct + "【来源】字段由 \"" + fzcsourcestr + "\" 变更为 \"" + tzcsourcestr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tzccntstatus) && "true".equals(tzccntstatus)) {
                    ct = ct + "【数量】字段由 \"" + fzccnt + "\" 变更为 \"" + tzccnt + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tsupplierstatus) && "true".equals(tsupplierstatus)) {
                    ct = ct + "【供应商】字段由 \"" + fsupplierstr + "\" 变更为 \"" + tsupplierstr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tbrandstatus) && "true".equals(tbrandstatus)) {
                    ct = ct + "【品牌】字段由 \"" + fbrandstr + "\" 变更为 \"" + tbrandstr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tbuytimestatus) && "true".equals(tbuytimestatus)) {
                    ct = ct + "【采购日期】字段由 \"" + fbuytimestr + "\" 变更为 \"" + tbuytimestr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tlocstatus) && "true".equals(tlocstatus)) {
                    ct = ct + "【存放区域】字段由 \"" + flocstr + "\" 变更为 \"" + tlocstr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tusefullifestatus) && "true".equals(tusefullifestatus)) {
                    ct = ct + "【使用周期】字段由 \"" + fusefullifestr + "\" 变更为 \"" + tusefullifestr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tusefullifestatus) && "true".equals(tusefullifestatus)) {
                    ct = ct + "【使用周期】字段由 \"" + fusefullifestr + "\" 变更为 \"" + tusefullifestr + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tusedcompanyidstatus) && "true".equals(tusedcompanyidstatus)) {
                    ct = ct + "【使用公司】字段由 \"" + fusedcompanyname + "\" 变更为 \"" + tusedcompanyname + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tpartidstatus) && "true".equals(tpartidstatus)) {
                    ct = ct + "【使用部门】字段由 \"" + fpartname + "\" 变更为 \"" + tpartname + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tuseduseridstatus) && "true".equals(tuseduseridstatus)) {
                    ct = ct + "【使用人】字段由 \"" + fusedusername + "\" 变更为 \"" + tusedusername + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tlabel1status) && "true".equals(tlabel1status)) {
                    ct = ct + "【标签1】字段由 \"" + flabel1 + "\" 变更为 \"" + tlabel1 + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tconfdescstatus) && "true".equals(tconfdescstatus)) {
                    ct = ct + "【配置描述】字段由 \"" + fconfdesc + "\" 变更为 \"" + tconfdesc + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tlocdtlstatus) && "true".equals(tlocdtlstatus)) {
                    ct = ct + "【位置】字段由 \"" + flocdtl + "\" 变更为 \"" + tlocdtl + "\" ;";
                }
                if (ToolUtil.isNotEmpty(tunitstatus) && "true".equals(tunitstatus)) {
                    ct = ct + "【计量单位】字段由 \"" + funit + "\" 变更为 \"" + tunit + "\" ;";
                }

            }
            entity.setFillct("1");
            entity.setCt(ct);
            ResChangeItemServiceImpl.saveOrUpdate(entity);
        }
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:变更确认
     */
    public R confirm(String uuid) {
        QueryWrapper<ResCBasicinformation> qw = new QueryWrapper<ResCBasicinformation>();
        qw.and(i -> i.eq("busuuid", uuid));
        ResCBasicinformation entity = ResCBasicinformationServiceImpl.getOne(qw);
        String sql = " update res_c_basicinformation_item a,res b set   " +
                "   a.fclassid=b.class_id  " +
                " , a.fname=b.name " +
                " , a.fmodel=b.model  " +
                " , a.fsn=b.sn  " +
                " , a.fzcsource=b.zcsource  " +
                " , a.fzccnt=b.zc_cnt  " +
                " , a.fsupplier=b.supplier  " +
                " , a.fbrand=b.brand  " +
                " , a.fbuytime=b.buy_time  " +
                " , a.ffd1=b.fd1  " +
                " , a.ffs20=b.fs20  " +
                " , a.fmark=b.mark  " +
                " , a.floc=b.loc  " +
                " , a.fconfdesc=b.confdesc  " +
                " , a.fusefullife=b.usefullife  " +
                " , a.fusedcompanyid=b.used_company_id  " +
                " , a.fpartid=b.part_id  " +
                " , a.flabel1=b.fs1  " +
                " , a.flabel2=b.fs2  " +
                " , a.funit=b.unit  " +
                " , a.fbatch=b.batch  " +
                " , a.fbelongcomp=b.belong_company_id  " +
                " , a.fuseduserid=b.used_userid  " +
                " where a.resid=b.id and a.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql, uuid);
        String sql2 = "update res_c_basicinformation_item b,res a set a.update_by='" + this.getUserId() + "'";
        if ("true".equals(entity.getTlabel2status())) {
            sql2 = sql2 + ",a.fs2=b.tlabel2";
        }
        if ("true".equals(entity.getTbatchstatus())) {
            sql2 = sql2 + ",a.batch=b.tbatch";
        }
        if ("true".equals(entity.getTbelongcompstatus())) {
            sql2 = sql2 + ",a.belong_company_id=b.tbelongcomp";
        }

        if ("true".equals(entity.getTnamestatus())) {
            sql2 = sql2 + ",a.name=b.tname";
        }
        if ("true".equals(entity.getTclassidstatus())) {
            sql2 = sql2 + ",a.class_id=b.tclassid";
        }
        if ("true".equals(entity.getTmodelstatus())) {
            sql2 = sql2 + ",a.model=b.tmodel";
        }
        if ("true".equals(entity.getTclassidstatus())) {
            sql2 = sql2 + ",a.class_id=b.tclassid";
        }
        if ("true".equals(entity.getTsnstatus())) {
            sql2 = sql2 + ",a.sn=b.tsn";
        }
        if ("true".equals(entity.getTzcsourcestatus())) {
            sql2 = sql2 + ",a.zcsource=b.tzcsource";
        }
        if ("true".equals(entity.getTzccntstatus())) {
            sql2 = sql2 + ",a.zc_cnt=b.tzccnt";
        }
        if ("true".equals(entity.getTsupplierstatus())) {
            sql2 = sql2 + ",a.supplier=b.tsupplier";
        }
        if ("true".equals(entity.getTbrandstatus())) {
            sql2 = sql2 + ",a.brand=b.tbrand";
        }
        if ("true".equals(entity.getTbuytimestatus())) {
            sql2 = sql2 + ",a.buy_time=b.tbuytime";
        }
        if ("true".equals(entity.getTlocstatus())) {
            sql2 = sql2 + ",a.loc=b.tloc";
        }
        if ("true".equals(entity.getTusefullifestatus())) {
            sql2 = sql2 + ",a.usefullife=b.tusefullife";
        }
        if ("true".equals(entity.getTusedcompanyidstatus())) {
            sql2 = sql2 + ",a.used_company_id=b.tusedcompanyid";
        }
        if ("true".equals(entity.getTpartidstatus())) {
            sql2 = sql2 + ",a.part_id=b.tpartid";
        }
        if ("true".equals(entity.getTuseduseridstatus())) {
            sql2 = sql2 + ",a.used_userid=b.tuseduserid";
        }
        if ("true".equals(entity.getTlabel1status())) {
            sql2 = sql2 + ",a.fs1=b.tlabel1";
        }
        if ("true".equals(entity.getTmarkstatus())) {
            sql2 = sql2 + ",a.mark=b.tmark";
        }
        if ("true".equals(entity.getTfd1status())) {
            sql2 = sql2 + ",a.fd1=b.tfd1";
        }
        if ("true".equals(entity.getTfs20status())) {
            sql2 = sql2 + ",a.fs20=b.tfs20";
        }
        if ("true".equals(entity.getTlocdtlstatus())) {
            sql2 = sql2 + ",a.locdtl=b.tlocdtl";
        }
        if ("true".equals(entity.getTunitstatus())) {
            sql2 = sql2 + ",a.unit=b.tunit";
        }
        if ("true".equals(entity.getTconfdescstatus())) {
            sql2 = sql2 + ",a.confdesc=b.tconfdesc";
        }
        sql2 = sql2 + " where a.id=b.resid and b.busuuid=? and b.dr='0' and a.dr='0'";
        db.execute(sql2, uuid);
        //记录资产变更
        ArrayList<ResChangeItem> cols = new ArrayList<ResChangeItem>();
        QueryWrapper<ResCBasicinformationItem> qw2 = new QueryWrapper<ResCBasicinformationItem>();
        qw2.and(i -> i.eq("busuuid", uuid));
        List<ResCBasicinformationItem> items = ResCBasicinformationItemServiceImpl.list(qw2);
        for (int i = 0; i < items.size(); i++) {
            ResChangeItem e = new ResChangeItem();
            e.setBusuuid(uuid);
            e.setResid(items.get(i).getResid());
            e.setType(AssetsConstant.ASSETS_BUS_TYPE_CGJB);
            e.setMark("实体信息变更");
            e.setFillct("0");
            e.setCdate(new Date());
            cols.add(e);
        }
        ResChangeItemServiceImpl.saveBatch(cols);
        fillChangeContent(uuid);
        return R.SUCCESS_OPER();
    }


    /**
     * @Description:查询变更数据
     */
    public R selectData(String uuid, String resid) {

        String sql2 = "select " + AssetsConstant.resSqlbody + " t.* ,b.*,   " +
                "(select name from sys_user_info where user_id=b.fuseduserid) fusedusername," +
                "(select name from sys_user_info where user_id=b.tuseduserid) tusedusername," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tzcsource) tzcsourcestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tsupplier) tsupplierstr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tusefullife) tusefullifestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tloc) tlocstr,   " +
                "(select node_name from hrm_org_part where node_id=b.tpartid) tpartname," +
                "(select node_name from hrm_org_part where node_id=b.tbelongcomp) tbelongcompanyname," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.tbrand) tbrandstr,   " +
                "(select route_name from ct_category where dr='0' and id=b.tclassid) tclassfullname,   " +
                "date_format(tbuytime,'%Y-%m-%d') tbuytimestr,   " +
                "date_format(fbuytime,'%Y-%m-%d') fbuytimestr,    " +
                "date_format(tfd1,'%Y-%m-%d') tfd1str,   " +
                "date_format(ffd1,'%Y-%m-%d') ffd1str,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fzcsource) fzcsourcestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fsupplier) fsupplierstr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fusefullife) fusefullifestr,   " +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.floc) flocstr,   " +
                "(select node_name from hrm_org_part where node_id=b.fpartid) fpartname," +
                "(select node_name from hrm_org_part where node_id=b.fbelongcomp) fbelongcompanyname," +
                "(select name from sys_dict_item where dr='0' and dict_item_id=b.fbrand) fbrandstr,   " +
                "(select route_name from ct_category where dr='0' and id=b.fclassid) fclassfullname  " +
                "from res t,res_c_basicinformation_item b where t.id=b.resid and t.dr='0' and b.dr='0' and b.busuuid=?";

        if (ToolUtil.isNotEmpty(resid)) {
            sql2 = sql2 + " and resid='" + resid + "'";
        }
        RcdSet rs = db.query(sql2, uuid);
        return R.SUCCESS_OPER(rs.toJsonArrayWithJsonObject());
    }
}
