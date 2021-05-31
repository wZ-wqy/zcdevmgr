package com.dt.core.dao;

import com.dt.core.dao.util.ArrayUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public class Rcd {
    private RcdSet ownerSet = null;

    private DataParser dataParser;
    private ArrayList<Object> values = null;

    public Rcd(RcdSet set) {
        ownerSet = set;
        dataParser = new DataParser();
        values = new ArrayList<Object>();
        for (int i = 0; i < ownerSet.getMetaData().getColumnCount(); i++) {
            values.add(null);
        }
    }

    public RcdSet getOwnerSet() {
        return ownerSet;
    }

    @SuppressWarnings({"unchecked"})
    public Rcd clone() {
        Rcd r = new Rcd(ownerSet);
        r.values = (ArrayList<Object>) this.values.clone();
        return r;
    }

    public void clear() {
        for (int i = 0; i < values.size(); i++)
            values.set(i, null);
    }

    /**
     * 不被标记为Dirty
     */
    protected void _setValue(int i, Object v) {
        ColumnDataGenerater cdg = ownerSet.getMetaData().getColumnDataGenerater(i);
        if (cdg != null) // 如果指定了列数据生成器则不允许设置值
        {
            return;
        }
        values.set(i, v);
    }

    public void setValue(int i, Object v) {
        _setValue(i, v);
    }

    public void setValue(String name, Object v) {
        int i = ownerSet.getMetaData().name2index(name);
        this.setValue(i, v);
    }

    // 增加值，内部用
    protected void addValue(Object v) {
        values.add(v);
    }

    public Object getValue(int i) {
        ColumnDataGenerater cdg = ownerSet.getMetaData().getColumnDataGenerater(i);
        if (cdg == null) {
            return this.values.get(i);
        } else {
            return cdg.getValue(this);
        }
    }

    public Object getValue(String name) {
        int i = ownerSet.getMetaData().name2index(name);
        return i == -1 ? null : this.getValue(i);
    }

    public Character getChar(String name) {
        return dataParser.parseChar(getValue(name));
    }

    public Character getChar(int i) {
        return dataParser.parseChar(getValue(i));
    }

    public String getString(String name) {
        return dataParser.parseString(getValue(name));
    }

    public String getString(int i) {
        return dataParser.parseString(getValue(i));
    }

    public Boolean getBoolean(String name) {
        return dataParser.parseBoolean(getValue(name));
    }

    public Boolean getBoolean(int i) {
        return dataParser.parseBoolean(getValue(i));
    }

    public Byte getByte(String name) {
        return dataParser.parseByte(getValue(name));
    }

    public Byte getByte(int i) {
        return dataParser.parseByte(getValue(i));
    }

    public Short getShort(String name) {
        return dataParser.parseShort(getValue(name));
    }

    public Short getShort(int i) {
        return dataParser.parseShort(getValue(i));
    }

    public Integer getInteger(String name) {
        return dataParser.parseInteger(getValue(name));
    }

    public Integer getInteger(int i) {
        return dataParser.parseInteger(getValue(i));
    }

    public Long getLong(String name) {
        return dataParser.parseLong(getValue(name));
    }

    public Long getLong(int i) {
        return dataParser.parseLong(getValue(i));
    }

    public BigInteger getBigInteger(String name) {
        return dataParser.parseBigInteger(getValue(name));
    }

    public BigInteger getBigInteger(int i) {
        return dataParser.parseBigInteger(getValue(i));
    }

    public Float getFloat(String name) {
        return dataParser.parseFloat(getValue(name));
    }

    public Float getFloat(int i) {
        return dataParser.parseFloat(getValue(i));
    }

    public Double getDouble(String name) {
        return dataParser.parseDouble(getValue(name));
    }

    public Double getDouble(int i) {
        return dataParser.parseDouble(getValue(i));
    }

    public BigDecimal getBigDecimal(String name) {
        return dataParser.parseBigDecimal(getValue(name));
    }

    public BigDecimal getBigDecimal(int i) {
        return dataParser.parseBigDecimal(getValue(i));
    }

    public Date getDate(String name) {
        return dataParser.parseDate(getValue(name));
    }

    public Date getDate(int i) {
        return dataParser.parseDate(getValue(i));
    }

    /**
     * 按字段标签生成 JsonObject
     */
    public JSONObject toJsonObject() {
        RcdMetaData meta = this.ownerSet.getMetaData();
        JSONObject json = new JSONObject();
        for (int i = 0; i < meta.getColumnCount(); i++) {

            json.put(meta.getColumnLabel(i), this.getValue(i));
        }
        return json;
    }

    /**
     * 按字段标签生成 JsonObject,并指定ID
     */
    public JSONObject toJsonObject(String... ids) {
        return toJsonObject(ArrayUtil.toStringList(ids));
    }

    /**
     * 按字段标签生成 JsonObject,并指定ID
     */
    public JSONObject toJsonObject(ArrayList<String> ids) {

        RcdMetaData meta = this.ownerSet.getMetaData();
        JSONObject json = new JSONObject();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            if (ids.contains(meta.getColumnLabel(i))) {
                json.put(meta.getColumnLabel(i), this.getValue(i));
            }
        }
        return json;
    }

    /**
     * 按字段查询顺序生成 JsonArray
     */
    public JSONArray toJsonArray() {
        JSONArray json = new JSONArray();
        for (Object value : this.values) {
            json.put(value);

        }
        return json;
    }
}
