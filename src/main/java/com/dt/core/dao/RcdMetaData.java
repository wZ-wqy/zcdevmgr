package com.dt.core.dao;

import java.util.ArrayList;
import java.util.HashMap;

public class RcdMetaData {
    private int columnCount = 0;
    private ArrayList<String> catalogNames = new ArrayList<String>();
    private ArrayList<String> columnClassName = new ArrayList<String>();
    private ArrayList<String> columnLabel = new ArrayList<String>();
    private ArrayList<Integer> columnType = new ArrayList<Integer>();
    private ArrayList<ColumnDataGenerater> columnDataGeneraters = new ArrayList<ColumnDataGenerater>();
    private ArrayList<String> columnTypeName = new ArrayList<String>();
    private ArrayList<String> schemaName = new ArrayList<String>();
    private ArrayList<String> tableName = new ArrayList<String>();
    private HashMap<String, Integer> nameIndexMap = new HashMap<String, Integer>();
    private String sql = null;

    public int getColumnCount() {
        return columnCount;
    }

    protected void setColumnCount(int c) {
        columnCount = c;
    }

    protected void addCatalogName(String val) {
        catalogNames.add(val);
    }

    public String getCatalogName(int i) {
        return catalogNames.get(i);
    }

    protected void addColumnClassName(String val) {
        columnClassName.add(val);
    }

    public String getColumnClassName(int i) {
        return columnClassName.get(i);
    }

    protected void addColumnLabel(String val) {
        columnLabel.add(val);
    }

    public String getColumnLabel(int i) {
        return columnLabel.get(i).toLowerCase();
    }

    public String[] getColumnLabels() {
        String[] arr = new String[columnLabel.size()];
        return columnLabel.toArray(arr);
    }

    protected void addColumnType(Integer val) {
        columnType.add(val);
    }

    public Integer getColumnType(int i) {
        return columnType.get(i);
    }

    protected void addColumnDataGenerater(ColumnDataGenerater val) {
        columnDataGeneraters.add(val);
    }

    public ColumnDataGenerater getColumnDataGenerater(int i) {
        return columnDataGeneraters.get(i);
    }

    protected void addColumnTypeName(String val) {
        columnTypeName.add(val);
    }

    public String getColumnTypeName(int i) {
        return columnTypeName.get(i);
    }

    protected void addSchemaName(String val) {
        schemaName.add(val);
    }

    public String getSchemaName(int i) {
        return schemaName.get(i);
    }

    protected void addTableName(String val) {
        tableName.add(val);
    }

    public String getTableName(int i) {
        return tableName.get(i);
    }

    protected void setMap(String name, int idx) {
        nameIndexMap.put(name.toUpperCase(), idx);
    }

    public int name2index(String name) {
        Integer i = nameIndexMap.get(name.toUpperCase());
        if (i == null) {

            // (new Throwable()).printStackTrace();
            return -1;
        } else {
            return i;
        }
    }

    @SuppressWarnings("unused")
    private String getExtJsStoreColumnType(int i) {

        // [{"name":"MC","type":"string"},{"name":"CKD","type":"java.math.BigDecimal"}]
        String na = columnClassName.get(i);

        if (na.equals("java.lang.String")) {
            na = null;
        } else if (na.equals("java.math.BigDecimal")) {
            na = "float";
        } else if (na.equals("java.util.Date") || na.equals("java.sql.Date") || na.equals("java.sql.Timestamp")) {
            na = "date";
        } else if (na.equals("java.lang.Boolean")) {
            na = "bool";
        } else if (na.equals("java.lang.Integer")) {
            na = "int";
        }

        return na;
    }

    public String getSql() {
        return sql;
    }

    protected void setSql(String sql) {
        this.sql = sql;
    }

}
