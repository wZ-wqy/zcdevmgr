package com.dt.core.dao.sql;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public enum ArgumentDataType {

    VARCHAR(Types.VARCHAR, new Class[]{String.class}),
    NUMBER(Types.NUMERIC, new Class[]{Float.class, Integer.class, Double.class}),
    INTEGER(Types.INTEGER, new Class[]{Integer.class}),
    DATE(Types.DATE, new Class[]{java.util.Date.class, java.sql.Date.class}),
    DECIMAL(Types.DECIMAL, new Class[]{BigDecimal.class}),
    DOUBLE(Types.DOUBLE, new Class[]{Double.class}),
    FLOAT(Types.FLOAT, new Class[]{Float.class});


    @SuppressWarnings("rawtypes")
    private Class[] javaTypes = null;
    private int dbType;

    @SuppressWarnings("rawtypes")
    ArgumentDataType(int dbType, Class[] javaTypes) {
        this.dbType = dbType;
        this.javaTypes = javaTypes;

    }

    @SuppressWarnings("rawtypes")
    public static ArgumentDataType getType(Class cls) {
        for (ArgumentDataType t : ArgumentDataType.values()) {
            Class[] ts = t.javaTypes;
            for (Class c : ts) {
                if (c.equals(cls)) return t;
            }
        }
        return null;
    }

    public static ArgumentDataType getType(Object val) {
        if (val == null) return null;
        return getType(val.getClass());
    }

    public int getDbType() {
        return dbType;
    }

    public Object getValue(CallableStatement call, int i) throws SQLException {
        if (this == VARCHAR) {
            return call.getString(i);
        } else if (this == INTEGER) {
            return call.getInt(i);
        } else if (this == DATE) {
            return call.getDate(i);
        } else if (this == DECIMAL) {
            return call.getBigDecimal(i);
        } else if (this == DOUBLE) {
            return call.getDouble(i);
        } else if (this == FLOAT) {
            return call.getFloat(i);
        } else {
            return call.getObject(i);
        }

    }

}
