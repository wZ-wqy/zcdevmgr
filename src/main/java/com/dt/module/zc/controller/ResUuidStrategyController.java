package com.dt.module.zc.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.dt.module.zc.entity.ResUuidStrategy;
import com.dt.module.zc.service.IResUuidStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dt.core.tool.util.DbUtil;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.tool.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import com.dt.core.common.base.BaseController;

/**
 * <p>
 * 编号生成策略 前端控制器
 * </p>
 *
 * @author lank
 * @since 2021-03-07
 */
@Controller
@RequestMapping("/api/zc/resUuidStrategy")
public class ResUuidStrategyController extends BaseController {


	@Autowired
	IResUuidStrategyService ResUuidStrategyServiceImpl;


	@ResponseBody
	@Acl(info = "根据Id删除", value = Acl.ACL_USER)
	@RequestMapping(value = "/deleteById.do")
	public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		return R.SUCCESS_OPER(ResUuidStrategyServiceImpl.removeById(id));
	}

	@ResponseBody
	@Acl(info = "根据Id查询", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectById.do")
	public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		return R.SUCCESS_OPER(ResUuidStrategyServiceImpl.getById(id));
	}

	@ResponseBody
	@Acl(info = "插入", value = Acl.ACL_USER)
	@RequestMapping(value = "/insert.do")
	public R insert(ResUuidStrategy entity) {
		return R.SUCCESS_OPER(ResUuidStrategyServiceImpl.save(entity));
	}

	@ResponseBody
	@Acl(info = "根据Id更新", value = Acl.ACL_USER)
	@RequestMapping(value = "/updateById.do")
	public R updateById(ResUuidStrategy entity) {
		return R.SUCCESS_OPER(ResUuidStrategyServiceImpl.updateById(entity));
	}

	@ResponseBody
	@Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
	@RequestMapping(value = "/insertOrUpdate.do")
	public R insertOrUpdate(ResUuidStrategy entity) {
		return R.SUCCESS_OPER(ResUuidStrategyServiceImpl.saveOrUpdate(entity));
	}

	@ResponseBody
	@Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectList.do")
	public R selectList() {
		return R.SUCCESS_OPER(ResUuidStrategyServiceImpl.list(null));
	}

	@ResponseBody
	@Acl(info = "查询所有,有分页", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectPage.do")
	public R selectPage(String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10")  String pageSize,@RequestParam(value = "pageIndex", required = true, defaultValue = "1")  String pageIndex) {
		JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
		if (ToolUtil.isEmpty(respar)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		int pagesize = respar.getIntValue("pagesize");
		int pageindex = respar.getIntValue("pageindex");
		QueryWrapper<ResUuidStrategy> ew = new QueryWrapper<ResUuidStrategy>();
		//ew.and(i -> i.eq("user_id", getUserId()).apply(pagesize>10, "rtime>sysdate-1","23"));
		IPage<ResUuidStrategy> pdata = ResUuidStrategyServiceImpl.page(new Page<ResUuidStrategy>(pageindex, pagesize), ew);
		JSONObject retrunObject = new JSONObject();
		retrunObject.put("iTotalRecords", pdata.getTotal());
		retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
		retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(),SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
		return R.clearAttachDirect(retrunObject);
	}


}

