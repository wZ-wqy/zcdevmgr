package com.dt.module.hrm.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.hrm.service.impl.HrmPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/hrm/hrmPosition")
public class HrmPositionExtController {

    @Autowired
    HrmPositionService hrmPositionService;

    /**
     * @Description:查询所有岗位
     */
    @ResponseBody
    @Acl(info = "查询所有岗位", value = Acl.ACL_ALLOW)
    @RequestMapping(value = "/listPositions.do")
    public R listPositions() {
        TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
        String search=ps.getString("search");
        return hrmPositionService.listPositions(search);
    }

}
