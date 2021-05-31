package com.dt.module.flow.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.uflo.console.provider.ProcessFile;
import com.bstek.uflo.console.provider.ProcessProvider;
import com.dt.module.flow.entity.SysUfloTpl;
import com.dt.module.flow.service.ISysUfloTplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: lank
 * @date: Nov 30, 2019 4:37:06 PM
 * @Description:
 */
@Component
public class UfloTplFileProcessProvider implements ProcessProvider {

    public String prefix = "";
    @Autowired
    private ISysUfloTplService SysUfloTplServiceImpl;

    @Override
    public InputStream loadProcess(String file) {

        QueryWrapper<SysUfloTpl> ew = new QueryWrapper<SysUfloTpl>();
        ew.eq("filename", file);
        List<SysUfloTpl> tpls = SysUfloTplServiceImpl.list(ew);

        if (tpls.size() == 0) {
            return null;
        }
        String ct = tpls.get(0).getContent();
        InputStream is = new ByteArrayInputStream(ct.getBytes());
        return is;

    }

    @Override
    public List<ProcessFile> loadAllProcesses() {
        List<ProcessFile> list = new ArrayList<ProcessFile>();
        List<SysUfloTpl> tpls = SysUfloTplServiceImpl.list();
        for (int i = 0; i < tpls.size(); i++) {
            ProcessFile a = new ProcessFile(tpls.get(i).getFilename(), tpls.get(i).getUpdateTime());
            list.add(a);
        }
        Collections.sort(list, new Comparator<ProcessFile>() {
            @Override
            public int compare(ProcessFile f1, ProcessFile f2) {
                return f2.getUpdateDate().compareTo(f1.getUpdateDate());
            }
        });
        return list;

    }

    @Override
    public void saveProcess(String fileName, String content) {
        String con = content.replace("utf-8","GBK");
        QueryWrapper<SysUfloTpl> ew = new QueryWrapper<SysUfloTpl>();
        ew.eq("filename", fileName);
        List<SysUfloTpl> tpls = SysUfloTplServiceImpl.list(ew);
        if (tpls.size() > 0) {
            UpdateWrapper<SysUfloTpl> uw = new UpdateWrapper<SysUfloTpl>();
            uw.set("content", con).eq("filename", fileName);
            SysUfloTplServiceImpl.update(uw);
        } else {
            SysUfloTpl u = new SysUfloTpl();
            u.setFilename(fileName);
            u.setContent(con);
            SysUfloTplServiceImpl.save(u);
        }

    }

    @Override
    public void deleteProcess(String fileName) {


        UpdateWrapper<SysUfloTpl> ew = new UpdateWrapper<SysUfloTpl>();
        ew.set("dr", "1").eq("filename", fileName);
        SysUfloTplServiceImpl.update(ew);

    }

    @Override
    public String getName() {

        return "数据库方式";
    }

    @Override
    public String getPrefix() {

        return this.prefix;
    }

    @Override
    public boolean support(String fileName) {

        return fileName != null && !fileName.trim().equals("");
    }

    @Override
    public boolean isDisabled() {

        return false;
    }

}
