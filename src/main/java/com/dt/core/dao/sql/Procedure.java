package com.dt.core.dao.sql;

import com.dt.core.dao.SpringDAO;
import com.dt.core.dao.util.TypedHashMap;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 过程和函数都是用这个类进行处理
 */
public class Procedure extends SubSQL {

    public static final String RETURN = "RETURN";
    private static final long serialVersionUID = -588109545247394266L;
    private String name;
    private boolean isFunction = false;
    private ArrayList<String> parameterNames = new ArrayList<String>();
    private ArrayList<Object> parameterValues = new ArrayList<Object>();
    private ArrayList<ArgumentType> parameterTypes = new ArrayList<ArgumentType>();
    private ArrayList<ArgumentDataType> parameterDataTypes = new ArrayList<ArgumentDataType>();
    private SpringDAO dao = null;

    public Procedure(String name, boolean isFunction) {
        this.name = name;
        this.isFunction = isFunction;
        if (this.isFunction) {
            this.setParameter(RETURN, ArgumentType.OUT, ArgumentDataType.VARCHAR, RETURN);
        }
    }

    public static void main(String[] args) throws Exception {
    }

    public boolean isFunction() {
        return isFunction;
    }

    /**
     * 设置参数： 设置参数时，setParameter的调用顺序要与实际参数的顺序一致
     */
    public Procedure setParameter(String name, ArgumentType paramType, ArgumentDataType dataType, Object value) {
        name = name.trim().toUpperCase();
        int i = parameterNames.indexOf(name);
        if (i == -1) {
            parameterNames.add(name);
            parameterValues.add(value);
            parameterTypes.add(paramType);
            parameterDataTypes.add(dataType);
        } else {
            parameterValues.set(i, value);
            parameterTypes.set(i, paramType);
            parameterDataTypes.set(i, dataType);
        }
        return this;
    }

    /**
     * 当参数值不为null时设置参数，通过参数值自动识别参数类型： 设置参数时，setParameter的调用顺序要与实际参数的顺序一致
     */
    public Procedure setParameter(String name, ArgumentType paramType, Object value) {
        return setParameter(name, paramType, ArgumentDataType.getType(value), value);
    }

    /**
     * 当参数值不为null时设置参数，通过参数值自动识别参数类型： 设置参数时，setParameter的调用顺序要与实际参数的顺序一致
     */
    public Procedure setInParameter(String name, Object value) {
        return setParameter(name, ArgumentType.IN, ArgumentDataType.getType(value), value);
    }

    /**
     * 当参数值不为null时设置参数，通过参数值自动识别参数类型： 设置参数时，setParameter的调用顺序要与实际参数的顺序一致
     */
    public Procedure setOutParameter(String name, Object value) {
        return setParameter(name, ArgumentType.OUT, ArgumentDataType.getType(value), value);
    }

    /**
     * 当参数值不为null时设置参数，通过参数值自动识别参数类型： 设置参数时，setParameter的调用顺序要与实际参数的顺序一致
     */
    public Procedure setInOutParameter(String name, Object value) {
        return setParameter(name, ArgumentType.INOUT, ArgumentDataType.getType(value), value);
    }

    protected SE getSE() {
        String sql = getOrignalSQL();
        SE se = SE.get(sql, parameterValues.toArray());
        return se;
    }

    private String getOrignalSQL() {

        String sql = "{ ";

        if (isFunction) {
            sql += "? =";
        }

        sql += " call " + this.name + " (";
        for (int i = (isFunction ? 1 : 0); i < parameterNames.size(); i++) {
            sql += "?,";
        }

        if (parameterNames.size() > 0) {
            sql = sql.substring(0, sql.length() - 1);
        }

        sql += ")";
        sql += " } ";
        return sql;

    }

    public String getSQL() {
        return getSE().getSQL();
    }

    public String getParamedSQL() {
        return getSE().getParamedSQL();
    }

    public Object[] getParams() {
        return getSE().getParams();
    }

    public String getParamNamedSQL() {
        return getSE().getParamNamedSQL();
    }

    public HashMap<String, Object> getNamedParams() {
        return getSE().getNamedParams();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        return false;
    }

    public TypedHashMap<String, Object> execute() throws Exception {

        CallableStatement call = createCallableStatement(this.getOrignalSQL());
        int j = 0;
        for (int i = 0; i < parameterNames.size(); i++) {
            j++;
            if (parameterTypes.get(i).isOut()) {
                call.registerOutParameter(j, parameterDataTypes.get(i).getDbType());
            }
        }

        setCallableParameters(call);
        call.executeUpdate();
        TypedHashMap<String, Object> ret = new TypedHashMap<String, Object>();
        j = 0;
        for (int i = 0; i < parameterNames.size(); i++) {
            j++;
            if (parameterTypes.get(i).isOut()) {
                Object value = parameterDataTypes.get(i).getValue(call, j);
                ret.put(parameterNames.get(i), value);
            }
        }
        return ret;
    }

    public SpringDAO getDao() {
        return dao;
    }

    public Procedure setDao(SpringDAO dao) {
        this.dao = dao;
        return this;
    }

    private CallableStatement createCallableStatement(String sql) throws SQLException {
        Connection db = dao.getDataSource().getConnection();
        CallableStatement st = null;
        try {
            st = db.prepareCall(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return st;
    }

    private void setCallableParameters(CallableStatement st) throws Exception {
        int fld = 0;
        for (int i = 0; i < parameterValues.size(); i++) {
            fld++;
            if (isFunction && i == 0) {

                continue;
            }

            Object val = parameterValues.get(i);

            if (val == null) {
                ArgumentDataType adt = parameterDataTypes.get(i);
                st.setNull(fld, adt.getDbType());
            } else if (val instanceof String) {
                st.setString(fld, (String) val);
            } else if (val instanceof Character) {
                st.setString(fld, val + "");
            } else if (val instanceof Byte) {
                st.setByte(fld, (Byte) val);
            } else if (val instanceof Short) {
                st.setShort(fld, (Short) val);
            } else if (val instanceof Integer) {
                st.setInt(fld, (Integer) val);
            } else if (val instanceof Long) {
                st.setLong(fld, (Long) val);
            } else if (val instanceof Float) {
                st.setFloat(fld, (Float) val);
            } else if (val instanceof Double) {
                st.setDouble(fld, (Double) val);
            } else if (val instanceof BigDecimal) {
                st.setBigDecimal(fld, (BigDecimal) val);
            } else if (val instanceof java.sql.Date) {
                st.setDate(fld, (java.sql.Date) val);
            } else if (val instanceof java.util.Date) {
                st.setDate(fld, new java.sql.Date(((java.util.Date) val).getTime()));
            } else {
                throw new SQLException("Data Type Not Support:" + val.getClass().getName());
            }

        }
    }

}
