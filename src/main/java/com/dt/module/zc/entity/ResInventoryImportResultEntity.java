package com.dt.module.zc.entity;

import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lank
 * @date: Nov 3, 2019 9:22:54 AM
 * @Description:
 */
public class ResInventoryImportResultEntity {

    public List<String> success_cmds = new ArrayList<String>();

    public List<String> msg = new ArrayList<String>();

    public int total = 0;

    public int success_cnt = 0;

    public int failed_cnt = 0;

    public List<ResInventoryEntity> faileentity = new ArrayList<ResInventoryEntity>();

    public boolean is_success_all = false;

    public void printResult() {

        System.out.println("Total:" + total);
        System.out.println("Success::" + success_cnt);
        System.out.println("Failed:" + failed_cnt);
        if (faileentity.size() > 0) {
            System.out.println("Failed Entity List:");
            for (int i = 0; i < faileentity.size(); i++) {
                String ct = ReflectionToStringBuilder.toString(faileentity.get(i));
                System.out.println("  " + ct);
            }
        }

    }

    public JSONObject covertJSONObjectResult() {
        JSONObject res = new JSONObject();
        res.put("failed_cnt", failed_cnt);
        res.put("success_cnt", success_cnt);
        res.put("total", total);

        JSONArray arr = new JSONArray();
        if (failed_cnt > 0) {
            for (int i = 0; i < faileentity.size(); i++) {
                String ct = ReflectionToStringBuilder.toString(faileentity.get(i));
                System.out.println("  " + ct.substring(ct.indexOf("[")));
                JSONObject obj = new JSONObject();
                String msg = faileentity.get(i).getProcessmsg();
                String uuid = faileentity.get(i).getUuid();
                String sn = faileentity.get(i).getSn();
                obj.put("ct", "原因:" + msg + ",编号:" + uuid + ",SN:" + sn);
                arr.add(obj);
            }
        }
        res.put("failed_data", arr);
        return res;

    }

    public void addSuccess(String cmd) {
        success_cmds.add(cmd);
        total = total + 1;
        success_cnt = success_cnt + 1;

        is_success_all = success_cnt == total;
    }

    public void addFailed(ResInventoryEntity re) {

        failed_cnt = failed_cnt + 1;
        total = total + 1;
        faileentity.add(re);
        is_success_all = false;
    }
}
