package com.dt.core.dao;

import com.dt.core.dao.sql.Block;
import com.dt.core.dao.sql.SE;
import com.dt.core.dao.sql.SQL;
import org.json.JSONObject;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

public class DBHolder {

    protected SpringDAO chief = null;
    protected ArrayList<SpringDAO> minors = new ArrayList<SpringDAO>();

    public String getUUID() {
        return chief.getUUID();
    }

    public DataSource getDataSource() {
        return chief.getDataSource();
    }

    public void setDataSource(DataSource ds) {
        chief.setDataSource(ds);
    }

    public Integer execute(String sql, HashMap<String, Object> ps) {
        Integer ret = chief.execute(sql, ps);
        for (SpringDAO dao : minors) {
            dao.execute(sql, ps);
        }
        return ret;
    }

    public Integer execute(SQL sql) {
        Integer ret = chief.execute(sql);
        for (SpringDAO dao : minors) {
            dao.execute(sql);
        }
        return ret;
    }

    public Integer execute(String sql) {
        Integer ret = chief.execute(sql);
        for (SpringDAO dao : minors) {
            dao.execute(sql);
        }
        return ret;
    }

    public Integer execute(String sql, JSONObject params) {
        Integer ret = chief.execute(sql, params);
        for (SpringDAO dao : minors) {
            dao.execute(sql, params);
        }
        return ret;
    }

    public Integer execute(String sql, Object... params) {
        Integer ret = chief.execute(sql, params);
        for (SpringDAO dao : minors) {
            dao.execute(sql, params);
        }
        return ret;
    }

    public Integer executes(String... sqls) {
        Integer ret = chief.executes(sqls);
        for (SpringDAO dao : minors) {
            dao.executes(sqls);
        }
        return ret;
    }

    public Integer executes(SQL... sqls) {
        Integer ret = chief.executes(sqls);
        for (SpringDAO dao : minors) {
            dao.executes(sqls);
        }
        return ret;
    }

    public Integer executeStringList(List<String> sqls) {
        Integer ret = chief.executeStringList(sqls);
        for (SpringDAO dao : minors) {
            dao.executeStringList(sqls);
        }
        return ret;
    }

    public Integer executeSQLList(List<SQL> sqls) {
        Integer ret = chief.executeSQLList(sqls);
        for (SpringDAO dao : minors) {
            dao.executeSQLList(sqls);
        }
        return ret;
    }

    public int[] batchExecute(String... sqls) {
        int[] ret = chief.batchExecute(sqls);
        for (SpringDAO dao : minors) {
            dao.batchExecute(sqls);
        }
        return ret;
    }

    public int[] batchExecute(List<SQL> sqls) {
        int[] ret = chief.batchExecute(sqls);
        for (SpringDAO dao : minors) {
            dao.batchExecute(sqls);
        }
        return ret;
    }

    public int[] batchExecute(String sql, List<Object[]> pslist) {
        int[] ret = chief.batchExecute(sql, pslist);
        for (SpringDAO dao : minors) {
            dao.batchExecute(sql, pslist);
        }
        return ret;
    }

    @SuppressWarnings("rawtypes")
    public int[] batchExecute(String sql, List... ps) {
        int[] ret = chief.batchExecute(sql, ps);
        for (SpringDAO dao : minors) {
            dao.batchExecute(sql, ps);
        }
        return ret;
    }

    public int[] batchExecute(SQL... sqls) {
        int[] ret = chief.batchExecute(sqls);
        for (SpringDAO dao : minors) {
            dao.batchExecute(sqls);
        }
        return ret;
    }

    public RcdSet queryPage(String sql, int pageSize, int pageIndex, Map<String, Object> params) {
        RcdSet ret = chief.queryPage(sql, pageSize, pageIndex, params);
        for (SpringDAO dao : minors) {
            dao.queryPage(sql, pageSize, pageIndex, params);
        }
        return ret;
    }

    public RcdSet query(String sql, Map<String, Object> params) {
        RcdSet ret = chief.query(sql, params);
        for (SpringDAO dao : minors) {
            dao.query(sql, params);
        }
        return ret;
    }

    public RcdSet queryPage(String sql, int pageSize, int pageIndex) {
        RcdSet ret = chief.queryPage(sql, pageSize, pageIndex);
        for (SpringDAO dao : minors) {
            dao.queryPage(sql, pageSize, pageIndex);
        }
        return ret;
    }

    public RcdSet query(String sql) {
        RcdSet ret = chief.query(sql);
        for (SpringDAO dao : minors) {
            dao.query(sql);
        }
        return ret;
    }

    public RcdSet query(SQL sql) {
        RcdSet ret = chief.query(sql);
        for (SpringDAO dao : minors) {
            dao.query(sql);
        }
        return ret;
    }

    public RcdSet queryPage(SQL sql, int pageSize, int pageIndex) {
        RcdSet ret = chief.queryPage(sql, pageSize, pageIndex);
        for (SpringDAO dao : minors) {
            dao.queryPage(sql, pageSize, pageIndex);
        }
        return ret;
    }

    public RcdSet queryPage(String sql, int pageSize, int pageIndex, Object... params) {
        RcdSet ret = chief.queryPage(sql, pageSize, pageIndex, params);
        for (SpringDAO dao : minors) {
            dao.queryPage(sql, pageSize, pageIndex, params);
        }
        return ret;
    }

    public RcdSet query(String sql, Object... params) {
        RcdSet ret = chief.query(sql, params);
        for (SpringDAO dao : minors) {
            dao.query(sql, params);
        }
        return ret;
    }

    public Rcd uniqueRecord(SQL sql) {
        Rcd ret = chief.uniqueRecord(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueRecord(sql);
        }
        return ret;
    }

    public Rcd uniqueRecord(String sql, Object... params) {
        Rcd ret = chief.uniqueRecord(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueRecord(sql, params);
        }
        return ret;
    }

    public Rcd uniqueRecord(String sql, Map<String, Object> params) {
        Rcd ret = chief.uniqueRecord(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueRecord(sql, params);
        }
        return ret;
    }

    public Object uniqueObject(String sql, HashMap<String, Object> params) {
        Object ret = chief.uniqueObject(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueObject(sql, params);
        }
        return ret;
    }

    public Object uniqueObject(String sql, Object... params) {
        Object ret = chief.uniqueObject(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueObject(sql, params);
        }
        return ret;
    }

    public Object uniqueObject(String sql) {
        Object ret = chief.uniqueObject(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueObject(sql);
        }
        return ret;
    }

    public Object uniqueObject(SQL sql) {
        Object ret = chief.uniqueObject(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueObject(sql);
        }
        return ret;
    }

    public Integer uniqueInteger(String sql) {
        Integer ret = chief.uniqueInteger(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueInteger(sql);
        }
        return ret;
    }

    public Integer uniqueInteger(SQL sql) {
        Integer ret = chief.uniqueInteger(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueInteger(sql);
        }
        return ret;
    }

    public Integer uniqueInteger(String sql, HashMap<String, Object> params) {
        Integer ret = chief.uniqueInteger(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueInteger(sql, params);
        }
        return ret;
    }

    public Integer uniqueInteger(String sql, Object... params) {
        Integer ret = chief.uniqueInteger(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueInteger(sql, params);
        }
        return ret;
    }

    public Long uniqueLong(String sql) {
        Long ret = chief.uniqueLong(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueLong(sql);
        }
        return ret;
    }

    public Long uniqueLong(SQL sql) {
        Long ret = chief.uniqueLong(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueLong(sql);
        }
        return ret;
    }

    public Long uniqueLong(String sql, HashMap<String, Object> params) {
        Long ret = chief.uniqueLong(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueLong(sql, params);
        }
        return ret;
    }

    public Long uniqueLong(String sql, Object... params) {
        Long ret = chief.uniqueLong(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueLong(sql, params);
        }
        return ret;
    }

    public BigDecimal uniqueDecimal(String sql) {
        BigDecimal ret = chief.uniqueDecimal(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueDecimal(sql);
        }
        return ret;
    }

    public BigDecimal uniqueDecimal(SQL sql) {
        BigDecimal ret = chief.uniqueDecimal(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueDecimal(sql);
        }
        return ret;
    }

    public BigDecimal uniqueDecimal(String sql, HashMap<String, Object> params) {
        BigDecimal ret = chief.uniqueDecimal(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueDecimal(sql, params);
        }
        return ret;
    }

    public BigDecimal uniqueDecimal(String sql, Object... params) {
        BigDecimal ret = chief.uniqueDecimal(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueDecimal(sql, params);
        }
        return ret;
    }

    public Date uniqueDate(String sql) {
        Date ret = chief.uniqueDate(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueDate(sql);
        }
        return ret;
    }

    public Date uniqueDate(SQL sql) {
        Date ret = chief.uniqueDate(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueDate(sql);
        }
        return ret;
    }

    public Date uniqueDate(String sql, HashMap<String, Object> params) {
        Date ret = chief.uniqueDate(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueDate(sql, params);
        }
        return ret;
    }

    public Date uniqueDate(String sql, Object... params) {
        Date ret = chief.uniqueDate(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueDate(sql, params);
        }
        return ret;
    }

    public String uniqueString(SQL sql) {
        String ret = chief.uniqueString(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueString(sql);
        }
        return ret;
    }

    public String uniqueString(String sql, HashMap<String, Object> params) {
        String ret = chief.uniqueString(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueString(sql, params);
        }
        return ret;
    }

    public String uniqueString(String sql, Object... params) {
        String ret = chief.uniqueString(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueString(sql, params);
        }
        return ret;
    }

    public Boolean uniqueBoolean(String sql, Object... params) {
        Boolean ret = chief.uniqueBoolean(sql, params);
        for (SpringDAO dao : minors) {
            dao.uniqueBoolean(sql, params);
        }
        return ret;
    }

    public Boolean uniqueBoolean(SQL sql) {
        Boolean ret = chief.uniqueBoolean(sql);
        for (SpringDAO dao : minors) {
            dao.uniqueBoolean(sql);
        }
        return ret;
    }

    public Date getBDDate() {
        Date ret = chief.getBDDate();
        for (SpringDAO dao : minors) {
            dao.getBDDate();
        }
        return ret;
    }

    public Date calcBDDate() {
        Date ret = chief.calcBDDate();
        for (SpringDAO dao : minors) {
            dao.calcBDDate();
        }
        return ret;
    }

    public SE se(String sql, HashMap<String, Object> ps, Object... pss) {
        SE ret = chief.se(sql, ps, pss);
        for (SpringDAO dao : minors) {
            dao.se(sql, ps, pss);
        }
        return ret;
    }

    public SE se(String sql, Object... pss) {
        SE ret = chief.se(sql, pss);
        for (SpringDAO dao : minors) {
            dao.se(sql, pss);
        }
        return ret;
    }

    public String joinSQLs(String[] sqls) {
        String ret = chief.joinSQLs(sqls);
        for (SpringDAO dao : minors) {
            dao.joinSQLs(sqls);
        }
        return ret;
    }

    public boolean executeBlock(Block block) {
        boolean ret = chief.executeBlock(block);
        for (SpringDAO dao : minors) {
            dao.executeBlock(block);
        }
        return ret;
    }

    public boolean createSequence(String id, SequenceType type, int len) {
        boolean ret = chief.createSequence(id, type, len);
        for (SpringDAO dao : minors) {
            dao.createSequence(id, type, len);
        }
        return ret;
    }

    public String getNextSequenceValue(String id) {
        String ret = chief.getNextSequenceValue(id);
        for (SpringDAO dao : minors) {
            dao.getNextSequenceValue(id);
        }
        return ret;
    }

}
