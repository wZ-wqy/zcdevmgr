package com.dt.core.dao.util;

import com.dt.core.dao.DataParser;

import java.util.Date;
import java.util.HashMap;

public class TypedHashMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private DataParser dp = new DataParser();

    public TypedHashMap() {
    }

    public TypedHashMap(HashMap<K, V> map) {
        if (map == null)
            return;
        for (K key : map.keySet()) {
            V value = map.get(key);
            this.put(key, value);
        }
    }

    public void printData() {
        for (Entry<K, V> e : this.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue());
        }
    }

    /**
     * 名值对转换
     */
    @SuppressWarnings("rawtypes")
    public static TypedHashMap toMap(Object... psarr) {
        TypedHashMap<String, Object> ps = new TypedHashMap<String, Object>();
        for (int i = 0; i < psarr.length; i++) {
            String p = psarr[i] + "";
            i++;
            if (i >= psarr.length) {
                ps.put(p, null);
                break;
            }
            Object v = psarr[i];
            ps.put(p, v);
        }
        return ps;
    }

    @SuppressWarnings("unlikely-arg-type")
    public Integer getInt(V key) {
        return dp.parseInteger(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public Integer getInt(V key, Integer value) {
        if (this.containsKey(key)) {
            return dp.parseInteger(this.get(key));
        } else {
            return value;
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    public Integer[] getIntArray(V key) {
        try {
            return (Integer[]) this.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    public String getString(V key) {
        return dp.parseString(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public String getString(V key, String defaultStr) {
        if (this.containsKey(key)) {
            return dp.parseString(this.get(key));
        } else {
            return defaultStr;
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    public String[] getStringArray(V key) {
        try {
            return (String[]) this.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    public Float getFloat(V key) {
        return dp.parseFloat(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public Double getDouble(V key) {
        return dp.parseDouble(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public Date getDate(V key) {
        return dp.parseDate(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public Boolean getBoolean(V key) {
        return dp.parseBoolean(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public Long getLong(V key) {
        return dp.parseLong(this.get(key));
    }

    @SuppressWarnings("unlikely-arg-type")
    public Short getShort(V key) {
        return dp.parseShort(this.get(key));
    }
}
