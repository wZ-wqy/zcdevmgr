package com.dt.module.form.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2020-03-28
 */
@Service
public class FormService extends BaseService {

    public static String OPER_TYPE_INSERT = "insert";

    public static String OPER_TYPE_UPDATE = "update";



    private void parseFromJsonMetaColDB() {
        //   System.out.println(db.query("select COLUMN_NAME col from information_schema.COLUMNS where table_name ='sys_process_form' and COLUMN_NAME like 'd%' and COLUMN_NAME<>'dr'").toJsonArrayWithJsonObject());
    }

    /**
     * @Description:从json解析
     */
    private HashMap<String, String> parseFromJsonMetaCol() {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONArray cols = new JSONArray();

        String s = "[{\"col\":\"duuid\"},{\"col\":\"dtitle\"},{\"col\":\"dct\"},{\"col\":\"durl\"},{\"col\":\"dname\"},{\"col\":\"dmark\"},{\"col\":\"dmessage\"},{\"col\":\"dsex\"},{\"col\":\"dstatus\"},{\"col\":\"dtype\"},{\"col\":\"dsubtype\"},{\"col\":\"dpwd\"},{\"col\":\"daddr\"},{\"col\":\"dcontact\"},{\"col\":\"dpic1\"},{\"col\":\"dpic2\"},{\"col\":\"dpic3\"},{\"col\":\"duser\"},{\"col\":\"dresult\"},{\"col\":\"dtotal\"},{\"col\":\"dbacktime\"},{\"col\":\"dlevel\"},{\"col\":\"dmethod\"},{\"col\":\"dfile\"},{\"col\":\"ddict\"},{\"col\":\"dattach1\"},{\"col\":\"dattach2\"},{\"col\":\"dattach3\"},{\"col\":\"dcard\"},{\"col\":\"df1\"},{\"col\":\"df2\"},{\"col\":\"df3\"},{\"col\":\"df4\"},{\"col\":\"df5\"},{\"col\":\"df6\"},{\"col\":\"df7\"},{\"col\":\"df8\"},{\"col\":\"df9\"},{\"col\":\"df10\"},{\"col\":\"dn1\"},{\"col\":\"dn2\"},{\"col\":\"dn3\"},{\"col\":\"dn4\"},{\"col\":\"dn5\"},{\"col\":\"dn6\"},{\"col\":\"dn7\"},{\"col\":\"dn8\"},{\"col\":\"dn9\"},{\"col\":\"dn10\"},{\"col\":\"duuid\"},{\"col\":\"dtitle\"},{\"col\":\"dprofile\"},{\"col\":\"dct\"},{\"col\":\"dlevel\"},{\"col\":\"dcat\"},{\"col\":\"durl\"},{\"col\":\"dname\"},{\"col\":\"dmark\"},{\"col\":\"dmessage\"},{\"col\":\"dstatus\"},{\"col\":\"dtype\"},{\"col\":\"dsubtype\"},{\"col\":\"dpwd\"},{\"col\":\"dpic1\"},{\"col\":\"dpic2\"},{\"col\":\"dpic3\"},{\"col\":\"duser\"},{\"col\":\"dresult\"},{\"col\":\"dtotal\"},{\"col\":\"dbacktime\"},{\"col\":\"dmethod\"},{\"col\":\"dfile\"},{\"col\":\"ddict\"},{\"col\":\"dattach1\"},{\"col\":\"dattach2\"},{\"col\":\"dattach3\"},{\"col\":\"dcard\"},{\"col\":\"dcode\"},{\"col\":\"dxm\"},{\"col\":\"dsex\"},{\"col\":\"daddr\"},{\"col\":\"dcontact\"},{\"col\":\"dmail\"},{\"col\":\"dqq\"},{\"col\":\"dhtml\"},{\"col\":\"ddate1\"},{\"col\":\"ddate2\"},{\"col\":\"ddate3\"},{\"col\":\"df1\"},{\"col\":\"df2\"},{\"col\":\"df3\"},{\"col\":\"df4\"},{\"col\":\"df5\"},{\"col\":\"df6\"},{\"col\":\"df7\"},{\"col\":\"df8\"},{\"col\":\"df9\"},{\"col\":\"df10\"},{\"col\":\"dn1\"},{\"col\":\"dn2\"},{\"col\":\"dn3\"},{\"col\":\"dn4\"},{\"col\":\"dn5\"},{\"col\":\"dn6\"},{\"col\":\"dn7\"},{\"col\":\"dn8\"},{\"col\":\"dn9\"},{\"col\":\"dn10\"}]\n";
        cols = JSONArray.parseArray(s);
        for (int i = 0; i < cols.size(); i++) {
            map.put(cols.getJSONObject(i).getString("col"), cols.getJSONObject(i).getString("col"));
        }
        return map;
    }


    /**
     * @Description:从json解析
     * type:insert,update
     */
    public R parseFromJsonToSqlTpl(String json_tpl, String json_value, String opertype, String process_data_id, String primary_value) {
        if (ToolUtil.isOneEmpty(json_tpl, json_value, opertype)) {
            return R.FAILURE();
        }
        //   parseFromJsonMetaColDB();
        HashMap<String, String> metacols = parseFromJsonMetaCol();
        System.out.println(json_value);
        JSONObject e = JSONObject.parseObject(json_value);

        Iterator<String> keys = e.keySet().iterator();// jsonObject.keys();
        HashMap<String, String> map = new HashMap<String, String>();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, e.getString(key));
        }
        Iterator<String> keySetIterator = map.keySet().iterator();
        Insert ins = new Insert("sys_process_form");
        Update ups = new Update("sys_process_form");
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            String v = map.get(key);
            if (metacols.containsKey(key)) {
                if (opertype.equals(OPER_TYPE_INSERT)) {
                    ins.setIf(key, v);
                } else if (opertype.equals(OPER_TYPE_UPDATE)) {
                    ups.setIf(key, v);
                }
            }
        }
        String ressql = "";
        String fid = "";
        if (opertype.equals(OPER_TYPE_UPDATE)) {
            fid = primary_value;
            ups.setIf("fdata", json_value);
            ups.where().andIf("id=?", primary_value);
            ressql = ups.getSQL();

        } else if (opertype.equals(OPER_TYPE_INSERT)) {
            fid = db.getUUID();
            ins.set("id", fid);
            ins.setIf("ftpldatamd5", MD5Util.encrypt(json_tpl));
            ins.setIf("ftpldata", json_tpl);
            ins.setIf("fdata", json_value);
            ins.setIf("dr", "0");
            ins.setIf("processdataid", process_data_id);
            ressql = ins.getSQL();
        }
        JSONObject res = new JSONObject();
        res.put("out", ressql);
        res.put("id", fid);
        return R.SUCCESS_OPER(res);
    }


}
