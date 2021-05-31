package com.dt.module.cmdb.controller;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.controller.FileUpDownController;
import com.dt.module.base.entity.SysFiles;
import com.dt.module.base.service.ISysFilesService;
import com.dt.module.cmdb.service.impl.ResImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author: lank
 * @date: Oct 21, 2019 7:28:06 PM
 * @Description:
 */
@Controller
@RequestMapping("/api/base/res")
public class ResExtImportController extends BaseController {

    @Autowired
    ISysFilesService SysFilesServiceImpl;

    @Autowired
    ResImportService resImportService;

    /**
     * @Description:导入资产
     */
    @RequestMapping("/importResData.do")
    @Acl(value = Acl.ACL_USER)
    @ResponseBody
    @Transactional(value="transactionManager")
    public R importResData(String category, String type, String id) {
        SysFiles fileobj = SysFilesServiceImpl.getById(id);
        String fileurl = fileobj.getPath();
        String filePath = FileUpDownController.getWebRootDir() + ".." + File.separatorChar + fileurl;
        return resImportService.importResNormal(filePath, type, category);
    }

}
