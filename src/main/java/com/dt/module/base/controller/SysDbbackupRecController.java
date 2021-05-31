package com.dt.module.base.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysDbbackupRec;
import com.dt.module.base.service.ISysDbbackupRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lank
 * @since 2020-06-25
 */
@Controller
@RequestMapping("/api/sysDbbackupRec")
public class SysDbbackupRecController extends BaseController {


    @Autowired
    ISysDbbackupRecService SysDbbackupRecServiceImpl;

    /**
     * 下载备份数据文件
     *  @param id
     */
    @ResponseBody
    @Acl(info = "下载", value = Acl.ACL_USER)
    @RequestMapping(value = "/downFile.do")
    public R downFile(HttpServletResponse response, @RequestParam(value = "id", required = true, defaultValue = "") String id) throws Exception {

        SysDbbackupRec rec = SysDbbackupRecServiceImpl.getById(id);
        if (rec == null) {
            return R.FAILURE("数据备份条目不存在");
        }
        String filepath = rec.getFilepath();
        File file = new File(filepath);
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("backupfile".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return R.FAILURE("文件不存在");
        }
        return R.SUCCESS_OPER();
    }

    /**
     * 根据ID删除备份记录
     *  @param id
     */
    @ResponseBody
    @Acl(info = "根据Id删除", value = Acl.ACL_USER)
    @RequestMapping(value = "/deleteById.do")
    public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(SysDbbackupRecServiceImpl.removeById(id));
    }

    /**
     * 根据ID查询备份记录
     *  @param id
     */
    @ResponseBody
    @Acl(info = "根据Id查询", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectById.do")
    public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return R.SUCCESS_OPER(SysDbbackupRecServiceImpl.getById(id));
    }


    /**
     * 插入
     *  @param entity
     */
    @ResponseBody
    @Acl(info = "插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insert.do")
    public R insert(SysDbbackupRec entity) {
        return R.SUCCESS_OPER(SysDbbackupRecServiceImpl.save(entity));
    }

    /**
     * 更新
     *  @param entity
     */
    @ResponseBody
    @Acl(info = "根据Id更新", value = Acl.ACL_USER)
    @RequestMapping(value = "/updateById.do")
    public R updateById(SysDbbackupRec entity) {
        return R.SUCCESS_OPER(SysDbbackupRecServiceImpl.updateById(entity));
    }

    /**
     * 存在则更新,否则插入
     *  @param entity
     */
    @ResponseBody
    @Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
    @RequestMapping(value = "/insertOrUpdate.do")
    public R insertOrUpdate(SysDbbackupRec entity) {
        return R.SUCCESS_OPER(SysDbbackupRecServiceImpl.saveOrUpdate(entity));
    }

    /**
     * 无分页查询备份数据
     */
    @ResponseBody
    @Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectList.do")
    public R selectList() {
        return R.SUCCESS_OPER(SysDbbackupRecServiceImpl.list());
    }

    /**
     * 分页查询备份数据
     */
    @ResponseBody
    @Acl(info = "查询所有,有分页", value = Acl.ACL_USER)
    @RequestMapping(value = "/selectPage.do")
    public R selectPage(String start, String length, @RequestParam(value = "pageSize", required = true, defaultValue = "10") String pageSize, @RequestParam(value = "pageIndex", required = true, defaultValue = "1") String pageIndex) {
        JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
        if (ToolUtil.isEmpty(respar)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        int pagesize = respar.getIntValue("pagesize");
        int pageindex = respar.getIntValue("pageindex");
        QueryWrapper<SysDbbackupRec> ew = new QueryWrapper<SysDbbackupRec>();
        ew.orderByDesc("create_time");
        IPage<SysDbbackupRec> pdata = SysDbbackupRecServiceImpl.page(new Page<SysDbbackupRec>(pageindex, pagesize), ew);
        JSONObject retrunObject = new JSONObject();
        retrunObject.put("iTotalRecords", pdata.getTotal());
        retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
        retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(), SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
        return R.clearAttachDirect(retrunObject);
    }


}

