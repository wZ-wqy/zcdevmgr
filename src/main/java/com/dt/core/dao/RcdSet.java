package com.dt.core.dao;

import com.dt.core.dao.util.ArrayUtil;
import jodd.datetime.JDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class RcdSet implements Iterable<Rcd> {
    RcdSetExporter exporter = new RcdSetExporter(this);
    private RcdMetaData metaData = null;
    private ArrayList<Rcd> records = new ArrayList<Rcd>();
    private int pageSize = 0;
    private int pageIndex = 0;
    private int pageCount = 0;
    private int totalRowCount = 0;

    /**
     * 增加一个记录
     */
    public void add(Rcd r) {
        this.records.add(r);
    }

    /**
     * 插入记录到指定位置
     */
    public void insertAt(int i, Rcd r) {
        records.add(i, r);
    }

    /**
     * 删除在指定位置的记录
     */
    public void remove(int i) {
        records.remove(i);
    }

    /**
     * 删除指定的记录
     */
    public void remove(Rcd r) {
        records.remove(r);
    }

    /**
     * 确定记录所在的序号
     */
    public int indexOf(Rcd r) {
        return records.indexOf(r);
    }

    /**
     * 返回指定位置的记录
     */
    public Rcd getRcd(int i) {
        return records.get(i);
    }

    /**
     * 记录集大小
     */
    public int size() {
        return records.size();
    }

    /**
     * 取得查询的元数据
     */
    public RcdMetaData getMetaData() {
        if (!isMetaDataInited()) {
            try {
                initeMetaData(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return metaData;

    }

    /**
     * 元数据初始化是否完成
     */
    public boolean isMetaDataInited() {
        return metaData != null;
    }

    protected void initeMetaData(ResultSetMetaData meta) throws SQLException {

        metaData = new RcdMetaData();
        if (meta == null)
            return;

        int ct = meta.getColumnCount();
        metaData.setColumnCount(ct);

        for (int colIndex = 1; colIndex <= ct; colIndex++) {
            metaData.addColumnDataGenerater(null);
            metaData.addCatalogName(meta.getCatalogName(colIndex));
            metaData.addColumnClassName(meta.getColumnClassName(colIndex));
            metaData.addColumnLabel(meta.getColumnLabel(colIndex));
            metaData.addColumnType(meta.getColumnType(colIndex));
            metaData.addColumnTypeName(meta.getColumnTypeName(colIndex));
            metaData.addSchemaName(meta.getSchemaName(colIndex));
            metaData.addTableName(meta.getTableName(colIndex));
            metaData.setMap(meta.getColumnLabel(colIndex), colIndex - 1);

        }
    }

    /**
     * 通过 for(Record r:set){} 循环遍历
     */
    public Iterator<Rcd> iterator() {
        return records.iterator();
    }

    /**
     * 增加一个虚拟列
     */
    public void addNewColumn(String colName, ColumnDataGenerater cdg) {
        if (this.size() == 0)
            return;
        // 填入空值
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            r.addValue(null);
        }
        metaData.addColumnDataGenerater(cdg);
        metaData.addCatalogName("");
        metaData.addColumnClassName("");
        metaData.addColumnLabel(colName);
        metaData.addColumnType(0);
        metaData.addColumnTypeName("");
        metaData.addSchemaName("");
        metaData.addTableName("");
        metaData.setMap(colName, metaData.getColumnCount());
        metaData.setColumnCount(metaData.getColumnCount() + 1);
    }

    /**
     * 增加一个实际数据列
     */
    public void addNewColumn(String colName, Object[] data) {
        if (this.size() == 0)
            return;

        for (int i = 0; i < data.length; i++) {
            Rcd r = this.getRcd(i);
            r.addValue(data[i]);
        }

        if (data.length < this.size()) {
            for (int i = data.length; i < this.size(); i++) {
                Rcd r = this.getRcd(i);
                r.setValue(i, null);
            }
        }

        metaData.addColumnDataGenerater(null);
        metaData.addCatalogName("");
        metaData.addColumnClassName("");
        metaData.addColumnLabel(colName);
        metaData.addColumnType(0);
        metaData.addColumnTypeName("");
        metaData.addSchemaName("");
        metaData.addTableName("");
        metaData.setMap(colName, metaData.getColumnCount());
        metaData.setColumnCount(metaData.getColumnCount() + 1);

    }

    /**
     * 把某一列专成数组
     */
    public Object[] toArray(int index) {
        Object[] arr = new Object[this.size()];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            arr[i] = r.getValue(index);
        }
        return arr;
    }

    /**
     * 把指定的列转成二维数组
     */
    public Object[][] toArray(int... indexes) {
        Object[][] arr = new Object[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getValue(indexes[j]);
            }
        }
        return arr;
    }

    /**
     * 把指定列名的列转换成数组
     */
    public Object[] toArray(String name) {
        return toArray(this.metaData.name2index(name));
    }

    /**
     * 把多个指定列名的的列转成二维数组
     */
    public Object[][] toArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toArray(indexes);
    }

    /**
     * 转指定类型的数组
     */
    public BigDecimal[] toBigDecimalArray(int index) {
        return ArrayUtil.castBigDecimal(this.toArray(index));
    }

    public BigDecimal[][] toBigDecimalArray(int... indexes) {
        BigDecimal[][] arr = new BigDecimal[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getBigDecimal(indexes[j]);
            }
        }
        return arr;
    }

    public BigDecimal[] toBigDecimalArray(String name) {
        return this.toBigDecimalArray(this.metaData.name2index(name));
    }

    public BigDecimal[][] toBigDecimalArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toBigDecimalArray(indexes);
    }

    public Boolean[] toBooleanArray(int index) {
        return ArrayUtil.castBoolean(this.toArray(index));
    }

    public Boolean[][] toBooleanArray(int... indexes) {
        Boolean[][] arr = new Boolean[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getBoolean(indexes[j]);
            }
        }
        return arr;
    }

    public Boolean[] toBooleanArray(String name) {
        return this.toBooleanArray(this.metaData.name2index(name));
    }

    public Boolean[][] toBooleanArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toBooleanArray(indexes);
    }

    public Byte[] toByteArray(int index) {
        return ArrayUtil.castByte(this.toArray(index));
    }

    public Byte[][] toByteArray(int... indexes) {
        Byte[][] arr = new Byte[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getByte(indexes[j]);
            }
        }
        return arr;
    }

    public Byte[] toByteArray(String name) {
        return this.toByteArray(this.metaData.name2index(name));
    }

    public Byte[][] toByteArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toByteArray(indexes);
    }

    public Date[] toDateArray(int index) {
        return ArrayUtil.castDate(this.toArray(index));
    }

    public Date[][] toDateArray(int... indexes) {
        Date[][] arr = new Date[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getDate(indexes[j]);
            }
        }
        return arr;
    }

    public Date[] toDateArray(String name) {
        return this.toDateArray(this.metaData.name2index(name));
    }

    public Date[][] toDateArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toDateArray(indexes);
    }

    public Double[] toDoubleArray(int index) {
        return ArrayUtil.castDouble(this.toArray(index));
    }

    public Double[][] toDoubleArray(int... indexes) {
        Double[][] arr = new Double[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getDouble(indexes[j]);
            }
        }
        return arr;
    }

    public Double[] toDoubleArray(String name) {
        return this.toDoubleArray(this.metaData.name2index(name));
    }

    public Double[][] toDoubleArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toDoubleArray(indexes);
    }

    public Float[] toFloatArray(int index) {
        return ArrayUtil.castFloat(this.toArray(index));
    }

    public Float[][] toFloatArray(int... indexes) {
        Float[][] arr = new Float[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getFloat(indexes[j]);
            }
        }
        return arr;
    }

    public Float[] toFloatArray(String name) {
        return this.toFloatArray(this.metaData.name2index(name));
    }

    public Float[][] toFloatArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toFloatArray(indexes);
    }

    public Integer[] toIntArray(String name) {
        return this.toIntegerArray(this.metaData.name2index(name));
    }

    public Integer[] toIntegerArray(int index) {
        return ArrayUtil.castInteger(this.toArray(index));
    }

    public Integer[][] toIntegerArray(int... indexes) {
        Integer[][] arr = new Integer[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getInteger(indexes[j]);
            }
        }
        return arr;
    }

    public Integer[][] toIntegerArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toIntegerArray(indexes);
    }

    public Long[] toLongArray(int index) {
        return ArrayUtil.castLong(this.toArray(index));
    }

    public Long[][] toLongArray(int... indexes) {
        Long[][] arr = new Long[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getLong(indexes[j]);
            }
        }
        return arr;
    }

    public Long[] toLongArray(String name) {
        return this.toLongArray(this.metaData.name2index(name));
    }

    public Long[][] toLongArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toLongArray(indexes);
    }

    public Short[] toShortArray(int index) {
        return ArrayUtil.castShort(this.toArray(index));
    }

    public Short[][] toShortArray(int... indexes) {
        Short[][] arr = new Short[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getShort(indexes[j]);
            }
        }
        return arr;
    }

    public Short[] toShortArray(String name) {
        return this.toShortArray(this.metaData.name2index(name));
    }

    public Short[][] toShortArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toShortArray(indexes);
    }

    public String[] toStringArray(int index) {
        String[] arr = ArrayUtil.castString(this.toArray(index));
        if (arr == null)
            arr = new String[]{};
        return arr;
    }

    public String[][] toStringArray(int... indexes) {
        String[][] arr = new String[this.size()][indexes.length];
        for (int i = 0; i < this.size(); i++) {
            Rcd r = this.getRcd(i);
            for (int j = 0; j < indexes.length; j++) {
                arr[i][j] = r.getString(indexes[j]);
            }
        }
        return arr;
    }

    public String[] toStringArray(String name) {
        if (this.size() == 0)
            return new String[]{};
        return this.toStringArray(this.metaData.name2index(name));
    }

    public String[][] toStringArray(String... names) {
        int[] indexes = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            indexes[i] = this.metaData.name2index(names[i]);
        }
        return toStringArray(indexes);
    }

    protected void setPageInfos(int pageSize, int pageIndex, int totalRowCount, int pageCount, String sql) {
        if (this.metaData != null) {
            this.metaData.setSql(sql);
        }
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.totalRowCount = totalRowCount;
    }

    /**
     * 页行数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 位于所在查询中的第几页
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 位于所在查询中的第几页
     */
    public int getCurrentPageIndex() {
        return pageIndex;
    }

    /**
     * 下一页码
     */
    public int getNextPageIndex() {
        return pageIndex == pageCount ? pageCount : pageIndex + 1;
    }

    /**
     * 上一页码
     */
    public int getPrevPageIndex() {
        return pageIndex == 1 ? 1 : pageIndex - 1;
    }

    /**
     * 总页数
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * 总行数
     */
    public int getTotalRowCount() {
        return totalRowCount;
    }

    /**
     * 获得导出器
     */
    public RcdSetExporter getExporter() {
        return exporter;
    }

    /**
     * 把记录集转换成List
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList getList(int colindex) {
        ArrayList list = new ArrayList();
        for (Rcd r : this) {
            list.add(r.getValue(colindex));
        }
        return list;
    }

    /**
     * 把记录集转换成List
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList getList(String colname) {
        ArrayList list = new ArrayList();
        for (Rcd r : this) {
            list.add(r.getValue(colname));
        }
        return list;
    }

    /**
     * 把记录集转换成Map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public HashMap getMap(String keycolname, String valuecolname) {
        HashMap map = new HashMap();
        for (Rcd r : this) {
            map.put(r.getValue(keycolname), r.getValue(valuecolname));
        }
        return map;
    }

    /**
     * 把记录集转换成Map
     */

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HashMap getMap(int keycolname, int valuecolname) {
        HashMap map = new HashMap();
        for (Rcd r : this) {
            map.put(r.getValue(keycolname), r.getValue(valuecolname));
        }
        return map;
    }

    /**
     * 转换为JsonArray，内部元素是JsonObject
     */
    public JSONArray toJsonArrayWithJsonObject() {
        JSONArray arr = new JSONArray();
        for (Rcd r : this) {
            arr.put(r.toJsonObject());
        }
        return arr;
    }

    /**
     * 转换为JsonArray，内部元素是JsonObject
     */
    public JSONArray toJsonArrayWithJsonObject(String... ids) {
        ArrayList<String> list = ArrayUtil.toStringList(ids);
        JSONArray arr = new JSONArray();
        for (Rcd r : this) {
            arr.put(r.toJsonObject(list));
        }
        return arr;
    }

    /**
     * 转换为JsonArray，内部元素是JsonArray
     */
    public JSONArray toJsonArrayWithJsonArray() {
        JSONArray arr = new JSONArray();
        for (Rcd r : this) {
            arr.put(r.toJsonArray());
        }
        return arr;
    }

    /**
     * 转换为JsonObject，内部元素是JsonArray,并以keyfield的指定列作为Json键
     */
    public JSONObject toJsonObjectWithJsonArray(int keyfield) {
        JSONObject json = new JSONObject();
        for (Rcd r : this) {
            json.put(r.getString(keyfield), r.toJsonArray());
        }
        return json;
    }

    /**
     * 转换为JsonObject，内部元素是JsonArray,并以keyfield的指定列作为Json键
     */
    public JSONObject toJsonObjectWithJsonArray(String keyfield) {
        return toJsonObjectWithJsonArray(this.metaData.name2index(keyfield));
    }

    /**
     * 转换为JsonObject，内部元素是JsonObject,并以keyfield的指定列作为Json键
     */
    public JSONObject toJsonObjectWithJsonObject(int keyfield) {
        JSONObject json = new JSONObject();
        for (Rcd r : this) {
            json.put(r.getString(keyfield), r.toJsonObject());
        }
        return json;
    }

    /**
     * 转换为JsonObject，内部元素是JsonObject,并以keyfield的指定列作为Json键
     */
    public JSONObject toJsonObjectWithJsonObject(String keyfield) {
        return toJsonObjectWithJsonObject(this.metaData.name2index(keyfield));
    }

    /**
     * 增加一个日期格式化的新列 YYYY yes year MM yes month DD yes day of month D day of week
     * MML month name long MMS month name short DL day of week name long DS day
     * of week name short hh yes hour mm yes minute ss yes seconds mss yes
     * milliseconds DDD day of year WW week of year WWW week of year with 'W'
     * prefix W week of month E era (AD or BC) TZL time zone name long TZS time
     * zone name short
     */
    public void addNewDateFormatedColumn(final int field, final String fmt, String newColumnLabel) {
        this.addNewColumn(newColumnLabel, new ColumnDataGenerater() {
            @Override
            public Object getValue(Rcd r) {
                JDateTime d = new JDateTime(r.getDate(field));
                return d.toString(fmt);
            }
        });
    }

    /**
     * 如果是一个日期列,进行日期格式化 YYYY yes year MM yes month DD yes day of month D day of
     * week MML month name long MMS month name short DL day of week name long DS
     * day of week name short hh yes hour mm yes minute ss yes seconds mss yes
     * milliseconds DDD day of year WW week of year WWW week of year with 'W'
     * prefix W week of month E era (AD or BC) TZL time zone name long TZS time
     * zone name short
     */
    public void addNewDateFormatedColumn(String field, String fmt, String newColumnLabel) {
        int f = getMetaData().name2index(field);
        addNewDateFormatedColumn(f, fmt, newColumnLabel);
    }

    /**
     * 进行数字格式化
     */
    public void formatNumber(int field, String fmt) {

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList toList(String field) {
        ArrayList list = new ArrayList();
        for (Rcd r : this) {
            list.add(r.getValue(field));
        }
        return list;
    }

    /**
     * 排序
     *
     * @param type asx/desc
     */
    public void sort(final String field, final String type, final boolean nullslast) {
        final String _type = type.trim().toUpperCase();
        Collections.sort(records, new Comparator<Rcd>() {

            public int compare(Rcd o1, Rcd o2) {
                Object v1 = o1.getValue(field);
                Object v2 = o2.getValue(field);

                if (o1.getDouble(field) == null) {

                }

                if (v1 == null && v2 == null)
                    return nullslast ? -1 : 0;
                if (v1 == null && v2 != null)
                    return nullslast ? -1 : -1;
                if (v1 != null && v2 == null)
                    return nullslast ? -1 : 1;


                if (v1 instanceof Integer) {
                    if (o1.getInteger(field) > o2.getInteger(field)) {
                        return _type.equals("ASC") ? 1 : -1;
                    }
                    if (o1.getInteger(field) < o2.getInteger(field)) {
                        return _type.equals("ASC") ? -1 : 1;
                    } else {
                        return 0;
                    }
                } else if (v1 instanceof Double) {
                    if (o1.getDouble(field) > o2.getDouble(field)) {
                        return _type.equals("ASC") ? 1 : -1;
                    }
                    if (o1.getDouble(field) < o2.getDouble(field)) {
                        return _type.equals("ASC") ? -1 : 1;
                    } else {
                        return 0;
                    }
                } else if (v1 instanceof BigDecimal) {
                    if (o1.getBigDecimal(field).doubleValue() > o2.getBigDecimal(field).doubleValue()) {
                        return _type.equals("ASC") ? 1 : -1;
                    }
                    if (o1.getBigDecimal(field).doubleValue() < o2.getBigDecimal(field).doubleValue()) {
                        return _type.equals("ASC") ? -1 : 1;
                    } else {
                        return 0;
                    }
                } else if (v1 instanceof String) {
                    return o1.getString(field).compareTo(o2.getString(field)) * (_type.equals("ASC") ? 1 : -1);
                } else
                    return 0;

            }

        });

    }

    public Object toRcdSetJson() {
        JSONObject json = new JSONObject();
        JSONArray data = new JSONArray();
        String[] labels = this.getMetaData().getColumnLabels();
        json.put("fields", labels);

        for (Rcd r : this) {
            JSONArray arr = new JSONArray();
            for (String lb : labels) {
                arr.put(r.getValue(lb));
            }
            data.put(arr);
        }

        json.put("data", data);

        return json;
    }

}
