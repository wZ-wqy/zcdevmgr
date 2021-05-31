package com.dt.core.dao;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;

public class StoredProcedure {

    protected Object ret = null;
    private ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
    private ArrayList<SqlOutParameter> outParameters = new ArrayList<SqlOutParameter>();
    private ArrayList<SqlInOutParameter> inOutParameters = new ArrayList<SqlInOutParameter>();
    private DataSource dataSource = null;
    private String procedureName = null;
    private boolean isFunction = false;

    /**
     * 当有多个参数时，这个函数貌似有问题
     */
    public StoredProcedure(DataSource dataSource, String procedureName, boolean isFunction) {
        this.procedureName = procedureName;
        this.isFunction = isFunction;
        this.dataSource = dataSource;
    }

    /**
     * 声明参数
     */
    public void declareParameter(String name, int type) {
        SqlParameter p = new SqlParameter(name, type);
        parameters.add(p);
    }

    /**
     * 声明OUT参数
     */
    public void declareOutParameter(String name, int type) {
        SqlOutParameter p = new SqlOutParameter(name, type);
        outParameters.add(p);
    }

    /**
     * 声明IN_OUT参数
     */
    public void declareInOutParameter(String name, int type) {
        SqlInOutParameter p = new SqlInOutParameter(name, type);
        inOutParameters.add(p);
    }

    /**
     * 执行
     */
    public HashMap<String, Object> execute(HashMap<String, Object> params) {
        InnerProcedure procedure = new InnerProcedure(dataSource, procedureName);
        for (SqlParameter p : parameters)
            procedure.declareParameter(p);
        for (SqlOutParameter p : outParameters)
            procedure.declareParameter(p);
        for (SqlInOutParameter p : inOutParameters)
            procedure.declareParameter(p);
        procedure.setFunction(isFunction);

        procedure.compile();
        HashMap<String, Object> map = (HashMap<String, Object>) procedure.execute(params);

        return map;
    }

    /**
     * 执行
     *
     * @param params 名称和值间隔出现即可，要求参数个数为偶数个，如为奇数，则最后一个作为被忽略 奇数位置要求字符串，如
     */
    public HashMap<String, Object> execute(Object... params) {
        HashMap<String, Object> ps = new HashMap<String, Object>();
        int max = params.length;
        if (max % 2 == 1)
            max = max - 1;
        max = max / 2;
        for (int i = 0; i < max; i++) {
            ps.put(params[i * 2].toString(), params[i * 2 + 1]);
        }
        return execute(ps);
    }

    private class InnerProcedure extends org.springframework.jdbc.object.StoredProcedure {
        InnerProcedure(DataSource dataSource, String procedureName) {
            super(dataSource, procedureName);
        }
    }
}
