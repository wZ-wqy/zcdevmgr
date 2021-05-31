package com.dt.core.dao.sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.dao.util.TypedHashMap;

/**
 * @author: lank
 * @date: 2020年3月20日 下午1:55:39
 * @Description:
 */
public class buildSql {

    public static void main(String[] arg) {
        JSONObject r = new JSONObject();
        r.put("table", "tab");
        JSONArray cols = new JSONArray();
        JSONObject e1 = new JSONObject();
        e1.put("col", "a");
        e1.put("type", "string");
        e1.put("exist", "N");
        cols.add(e1);

        JSONObject e2 = new JSONObject();
        e2.put("col", "b");
        e2.put("type", "string");
        e2.put("exist", "Y");
        cols.add(e2);

    }

    public void buildUpdate(JSONObject e, TypedHashMap<String, Object> ps) {
        String tab = e.getString("table");
        JSONArray cols = e.getJSONArray("cols");
        Update me = new Update(tab);
        for (int i = 0; i < cols.size(); i++) {
            String col = cols.getJSONObject(i).getString("col");
            String type = cols.getJSONObject(i).getString("type");
            String exist = cols.getJSONObject(i).getString("exist");
            if (type.equals("string")) {
                if ("Y".equals(exist)) {
                    me.set(col, ps.getString(col));
                } else {
                    me.setIf(col, ps.getString(col));
                }
            } else if (type.equals("int")) {
                if ("Y".equals(exist)) {
                    me.set(col, ps.getInt(col));
                } else {
                    me.setIf(col, ps.getInt(col));
                }
            }
        }

    }
}
