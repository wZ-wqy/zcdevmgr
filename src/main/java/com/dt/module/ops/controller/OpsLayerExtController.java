package com.dt.module.ops.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.CategoryEnum;
import com.dt.module.base.service.ISysDictItemService;
import com.dt.module.ops.entity.OpsLayer;
import com.dt.module.ops.entity.OpsRack;
import com.dt.module.ops.entity.OpsRackInfo;
import com.dt.module.ops.service.IOpsLayerService;
import com.dt.module.ops.service.IOpsRackInfoService;
import com.dt.module.ops.service.IOpsRackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/ops/opsLayer/ext")
public class OpsLayerExtController extends BaseController {

    @Autowired
    IOpsLayerService OpsLayerServiceImpl;

    @Autowired
    ISysDictItemService SysDictItemServiceImpl;

    @Autowired
    IOpsRackService OpsRackServiceImpl;

    @Autowired
    IOpsRackInfoService OpsRackInfoServiceImpl;

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectByAreaId.do")
    public R selectByAreaId(@RequestParam(value = "areaid", required = true, defaultValue = "") String areaid) {
        QueryWrapper<OpsLayer> q=new QueryWrapper<>();
        q.eq("areaid",areaid);
        q.select("(select count(1) cnt from (select layerid from ops_rack) t where dr='0' and layerid=id) rackcnt",
                "id","name","locid","areaid",
                "(select name from sys_dict_item where dict_item_id=locid) locname");
        List<Map<String, Object>> maps =OpsLayerServiceImpl.getBaseMapper().selectMaps(q);
        return R.SUCCESS_OPER(maps);
    }

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectLayer.do")
    public R selectLayer() {
        QueryWrapper<OpsLayer> q=new QueryWrapper<>();
        q.select("id","CONCAT( (select name from ops_area where id=areaid),'-',name) layername");
        q.orderByDesc("2");
        List<Map<String, Object>> maps =OpsLayerServiceImpl.getBaseMapper().selectMaps(q);
        return R.SUCCESS_OPER(maps);
    }



    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/chooseRackByLayerId.do")
    public R chooseRackByLayerId( @RequestParam(value = "layerid", required = true, defaultValue = "") String layerid) {
        QueryWrapper<OpsRackInfo> q=new QueryWrapper<>();
        q.notInSql("id","select rackid from ops_rack where dr='0' and layerid='"+layerid+"'");
        q.select("id","code","name","mark","capacity");
        q.orderByDesc("name");
        List<Map<String, Object>> maps =OpsRackInfoServiceImpl.getBaseMapper().selectMaps(q);
        return R.SUCCESS_OPER(maps);
    }

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/listRack.do")
    public R listRack(@RequestParam(value = "areaid", required = true, defaultValue = "") String areaid,@RequestParam(value = "layerid", required = true, defaultValue = "") String layerid) {
        QueryWrapper<OpsRack> q=new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(layerid)){
            q.eq("layerid",layerid);
        }else{
            q.inSql("layerid","select id from ops_layer where areaid='"+areaid+"' ");
        }
        q.select("id","rackid","layerid","(select name from ops_rack_info where id=rackid) rackname",
                "(select code from ops_rack_info where id=rackid) rackcode",
                "(select capacity from ops_rack_info where id=rackid) rackcapacity",
                "(select mark from ops_rack_info where id=rackid) rackmark",
                "(select name from ops_layer where layerid=id) layername",
                "(select b.name from ops_layer a,sys_dict_item b where a.locid=b.dict_item_id and layerid=a.id) maplocname",
                "(select a.locid from ops_layer a where layerid=a.id) maplocid",
                "(select a.name from ops_area a,ops_layer b where a.id=b.areaid and b.id=layerid) areaname",
                "(select count(1) from res a,ops_layer b where a.dr='0' and a.category='"+ CategoryEnum.CATEGORY_ASSETS.getValue()+"' and a.ishandle ='0' and a.loc=b.locid and b.id=layerid and a.rack=rackid )assetcnt"
        );
        List<Map<String, Object>> maps =OpsRackServiceImpl.getBaseMapper().selectMaps(q);
        return R.SUCCESS_OPER(maps);
    }

    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/removeRackById.do")
    public R removeRackById(String id) {
        OpsRackServiceImpl.removeById(id);
        return R.SUCCESS_OPER( );
    }



    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/addRacK.do")
    public R addRacK(String layerid,String items) {
        JSONArray itemsarr= JSONArray.parseArray(items);
        if(ToolUtil.isEmpty(layerid)||itemsarr.size()==0){
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        List<OpsRack> lists=new ArrayList<OpsRack>();
        HashSet<String> sets = new HashSet<String>(itemsarr.size());
        for(int i=0;i<itemsarr.size();i++){
            String id=itemsarr.getJSONObject(i).getString("id");
            if(sets.contains(id)){
                break;
            }
            OpsRack obj=new OpsRack();
            obj.setLayerid(layerid);
            obj.setRackid(id);
            sets.add(id);
            lists.add(obj);
        }
        if(lists.size()>0){
            OpsRackServiceImpl.saveOrUpdateBatch(lists);
        }
        return R.SUCCESS_OPER();
    }

}
