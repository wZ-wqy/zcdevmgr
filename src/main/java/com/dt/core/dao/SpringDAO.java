package com.dt.core.dao;

import com.dt.core.dao.sql.*;
import com.dt.core.dao.util.TypedHashMap;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;

public abstract class SpringDAO {

    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate njdbcTemplate;
    private DataParser dataParser = new DataParser();
    private DataSource ds;
    private HashMap<String, Object> emptyMap = new HashMap<String, Object>();
    /**
     * 计算数据库的当前时间，不适合循环调用
     */
    private Long timeDiff = null;

    public static void main(String[] args) {

        Delete dls = new Delete();
        dls.from("adf");
        dls.where().and("id=?", 1);
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 创建一个序列
     */
    @SuppressWarnings({"unchecked"})
    public boolean createSequence(String id, SequenceType type, int len) {
        try {

            StoredProcedure p = getStoredProcedure("DEFINE_SEQUENCE");
            p.declareParameter("ID", Types.VARCHAR);
            p.declareParameter("TYPE", Types.VARCHAR);
            p.declareParameter("LENGTH", Types.INTEGER);
            p.execute(TypedHashMap.toMap("ID", id, "TYPE", type.name(), "LENGTH", len));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 得到序列的下一个值
     */
    @SuppressWarnings({"unchecked"})
    public String getNextSequenceValue(String id) {
        try {

            StoredProcedure p = getStoredProcedure("NEXT_VAL");
            p.declareParameter("ID", Types.VARCHAR);
            p.declareOutParameter("SVAL", Types.VARCHAR);

            HashMap<String, Object> ret = p.execute(TypedHashMap.toMap("ID", id));
            return ret.get("SVAL") + "";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    public void setDataSource(DataSource ds) {
        this.ds = ds;
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.njdbcTemplate = new NamedParameterJdbcTemplate(this.ds);

    }

    /**
     * 返回SQL影响的行数
     */
    public Integer execute(String sql, HashMap<String, Object> ps) {
        sql = translate(sql);

        int ret = this.njdbcTemplate.update(sql, ps);
        return ret;
    }

    /**
     * 执行一个SQL语句，返回执行是否成功
     */
    public Integer execute(SQL sql) {
        return execute(sql.getParamNamedSQL(), sql.getNamedParams());
    }

    /**
     * 返回SQL影响的行数
     */
    public Integer execute(String sql) {
        return execute(sql, emptyMap);
    }

    public Integer execute(String sql, JSONObject params) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (String p : params.keySet()) {
            map.put(p, params.get(p));
        }
        return this.execute(sql, map);
    }

    /**
     * 则返回SQL影响的行数
     */
    public Integer execute(String sql, Object... params) {

        sql = translate(sql);

        int ret = this.jdbcTemplate.update(sql, params);
        return ret;

    }

    @Transactional
    public Integer executes(String... sqls) {
        int i = 0;
        for (String sql : sqls) {
            sql = translate(sql);
            // i+=this.jdbcTemplate.update(sql,emptyMap);
            i += this.jdbcTemplate.update(sql);
        }
        return i;
    }

    @Transactional
    public Integer executes(SQL... sqls) {
        int i = 0;
        for (SQL sql : sqls) {
            String _sql = translate(sql.getParamedSQL());
            i += this.jdbcTemplate.update(_sql, sql.getParams());
        }
        return i;
    }

    @Transactional
    public Integer executeStringList(List<String> sqls) {
        return executes(sqls.toArray(new String[sqls.size()]));
    }

    @Transactional
    public Integer executeSQLList(List<SQL> sqls) {
        return executes(sqls.toArray(new SQL[sqls.size()]));
    }

    protected String getCountSQL(String sql) {

        return "select count(1) from(" + sql + ") a";

    }

    /**
     * 批量执行
     */
    @Transactional
    public int[] batchExecute(String... sqls) {

        for (int i = 0; i < sqls.length; i++) {
            sqls[i] = translate(sqls[i]);
        }
        return jdbcTemplate.batchUpdate(sqls);
    }

    /**
     * 批量执行
     */
    @Transactional
    public int[] batchExecute(List<SQL> sqls) {
        SQL[] _sqls = sqls.toArray(new SQL[sqls.size()]);
        return batchExecute(_sqls);
    }

    /**
     * 批量执行
     */
    @Transactional
    public int[] batchExecute(String sql, List<Object[]> pslist) {
        sql = translate(sql);
        return jdbcTemplate.batchUpdate(sql, pslist);
    }

    // ========================================================================================================================

    /**
     * 批量执行
     *
     * @param ps 每个List对应一个更新列,且每一个List的长度必须一致
     */
    @Transactional
    @SuppressWarnings({"rawtypes"})
    public int[] batchExecute(String sql, List... ps) {
        final ArrayList<Object[]> pslist = new ArrayList<Object[]>();
        int listSize = -2;
        if (ps.length > 0)
            listSize = ps[0].size();
        for (int p = 0; p < listSize; p++) {
            Object[] arr = new Object[ps.length];
            for (int i = 0; i < ps.length; i++) {
                arr[i] = ps[i].get(p);
            }
            pslist.add(arr);
        }

        sql = translate(sql);
        int[] ret = jdbcTemplate.batchUpdate(sql, pslist);
        return ret;
    }

    /**
     * 批量执行
     */
    @Transactional
    public int[] batchExecute(SQL... sqls) {

        // 进行分组
        HashMap<String, List<Object[]>> eSqls = new HashMap<String, List<Object[]>>();
        for (SQL sql : sqls) {
            String psql = sql.getParamedSQL();
            if (!eSqls.containsKey(psql)) {
                eSqls.put(psql, new ArrayList<Object[]>());
            }
            eSqls.get(psql).add(sql.getParams());
        }

        // 分组批量执行
        int[] result = new int[sqls.length];
        int i = 0;
        for (String sql : eSqls.keySet()) {
            sql = translate(sql);
            int[] x = jdbcTemplate.batchUpdate(sql, eSqls.get(sql));
            for (int j = 0; j < x.length; j++) {
                result[i + j] = x[j];
            }
        }
        return result;
    }

    // 不同数据库的分页实现
    protected abstract RcdSet getPageSet(RcdSet set, String sql, int pageSize, int pageIndex,
                                         Map<String, Object> params);


    /**
     * 如果pageSize不为0,则不分页
     */
    public RcdSet queryPage(String sql, int pageSize, int pageIndex, Map<String, Object> params) {
        if (params == null)
            params = new HashMap<String, Object>();
        if (pageSize < 0)
            pageSize = 0;
        if (pageSize == 0)
            pageIndex = 1;

        RcdSet set = new RcdSet();

        int totalPage = 1;
        String querySql = sql;
        int totalRecord = 0;
        if (pageSize > 0) {

            String countSql = getCountSQL(sql);
            countSql = translate(countSql);
            // totalRecord= this.njdbcTemplate.queryForInt(countSql, params);
            totalRecord = jdbcTemplate.queryForObject(countSql, Integer.class, params);
            totalPage = (totalRecord % pageSize) == 0 ? (totalRecord / pageSize) : (totalRecord / pageSize + 1);
            sql = translate(sql);
            set = this.getPageSet(set, sql, pageIndex, pageSize, params);

        } else {
            querySql = translate(querySql);
            this.njdbcTemplate.query(querySql, params, new RcdRowMapper(set));
        }

        if (totalRecord == 0)
            totalRecord = set.size();
        set.setPageInfos(pageSize, pageIndex, totalRecord, totalPage, sql);
        return set;
    }

    public RcdSet query(String sql, Map<String, Object> params) {
        return queryPage(sql, 0, 0, params);
    }

    public RcdSet query(String sql) {
        return queryPage(sql, 0, 0, emptyMap);
    }

    public RcdSet query(SQL sql) {
        return queryPage(sql.getParamNamedSQL(), 0, 0, sql.getNamedParams());
    }

    public RcdSet queryPage(SQL sql, int pageSize, int pageIndex) {
        return queryPage(sql.getParamNamedSQL(), pageSize, pageIndex, sql.getNamedParams());
    }

    // 不同数据库的分页实现
    protected abstract RcdSet getPageSet(RcdSet set, String sql, int pageIndex, int pageSize, Object... params);

    // ===============================================================

    public RcdSet queryPage(String sql, int pageSize, int pageIndex, Object... params) {

        if (pageSize < 0)
            pageSize = 0;
        if (pageSize == 0)
            pageIndex = 1;

        RcdSet set = new RcdSet();

        int totalPage = 1;
        String querySql = sql;
        int totalRecord = 0;
        if (pageSize > 0) {

            String countSql = getCountSQL(sql);
            countSql = translate(countSql);
            // totalRecord= this.njdbcTemplate.queryForInt(countSql, params);
            totalRecord = jdbcTemplate.queryForObject(countSql, Integer.class, params);
            totalPage = (totalRecord % pageSize) == 0 ? (totalRecord / pageSize) : (totalRecord / pageSize + 1);
            sql = translate(sql);
            set = this.getPageSet(set, sql, pageIndex, pageSize, params);

        } else {
            querySql = translate(querySql);
            this.jdbcTemplate.query(querySql, new RcdRowMapper(set), params);
        }
        set.setPageInfos(pageSize, pageIndex, totalRecord, totalPage, sql);
        return set;

    }

    public RcdSet query(String sql, Object... params) {
        return queryPage(sql, 0, 0, params);
    }

    /**
     * 查询单一记录
     */
    public Rcd uniqueRecord(SQL sql) {
        return uniqueRecord(sql.getParamNamedSQL(), sql.getNamedParams());
    }

    // ==========================================================================

    /**
     * 查询单一记录
     */
    public Rcd uniqueRecord(String sql, Object... params) {
        RcdSet set = new RcdSet();
        sql = translate(sql);
        this.jdbcTemplate.query(sql, new RcdRowMapper(set), params);
        set.setPageInfos(1, 1, 1, 1, sql);
        if (set.size() == 0)
            return null;
        else
            return set.getRcd(0);
    }

    /**
     * 查询单一记录
     */
    public Rcd uniqueRecord(String sql, Map<String, Object> params) {
        RcdSet set = new RcdSet();
        sql = translate(sql);
        this.njdbcTemplate.query(sql, params, new RcdRowMapper(set));
        set.setPageInfos(1, 1, 1, 1, sql);
        if (set.size() == 0)
            return null;
        else
            return set.getRcd(0);
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Object uniqueObject(String sql, HashMap<String, Object> params) {
        Rcd tec = uniqueRecord(sql, params);
        if (tec == null)
            return null;
        return tec.getValue(0);
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Object uniqueObject(String sql, Object... params) {
        Rcd tec = uniqueRecord(sql, params);
        return tec == null ? null : tec.getValue(0);
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Object uniqueObject(String sql) {
        return uniqueObject(sql, new HashMap<String, Object>());
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Object uniqueObject(SQL sql) {
        return uniqueObject(sql.getParamNamedSQL(), sql.getNamedParams());
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Integer uniqueInteger(String sql) {
        return dataParser.parseInteger(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Integer uniqueInteger(SQL sql) {
        return dataParser.parseInteger(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Integer uniqueInteger(String sql, HashMap<String, Object> params) {
        return dataParser.parseInteger(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Integer uniqueInteger(String sql, Object... params) {
        return dataParser.parseInteger(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Long uniqueLong(String sql) {
        return dataParser.parseLong(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Long uniqueLong(SQL sql) {
        return dataParser.parseLong(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Long uniqueLong(String sql, HashMap<String, Object> params) {
        return dataParser.parseLong(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Long uniqueLong(String sql, Object... params) {
        return dataParser.parseLong(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public BigDecimal uniqueDecimal(String sql) {
        return dataParser.parseBigDecimal(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public BigDecimal uniqueDecimal(SQL sql) {
        return dataParser.parseBigDecimal(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public BigDecimal uniqueDecimal(String sql, HashMap<String, Object> params) {
        return dataParser.parseBigDecimal(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public BigDecimal uniqueDecimal(String sql, Object... params) {
        return dataParser.parseBigDecimal(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Date uniqueDate(String sql) {
        return dataParser.parseDate(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Date uniqueDate(SQL sql) {
        return dataParser.parseDate(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Date uniqueDate(String sql, HashMap<String, Object> params) {
        return dataParser.parseDate(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Date uniqueDate(String sql, Object... params) {
        return dataParser.parseDate(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public String uniqueString(SQL sql) {
        return dataParser.parseString(uniqueObject(sql));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public String uniqueString(String sql, HashMap<String, Object> params) {
        return dataParser.parseString(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public String uniqueString(String sql, Object... params) {
        return dataParser.parseString(uniqueObject(sql, params));
    }

    //

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Boolean uniqueBoolean(String sql, Object... params) {
        return dataParser.parseBoolean(uniqueObject(sql, params));
    }

    /**
     * 查询单一值 如select count(*) from XX 等
     */
    public Boolean uniqueBoolean(SQL sql) {
        return dataParser.parseBoolean(uniqueObject(sql.getParamNamedSQL(), sql.getNamedParams()));
    }

    /**
     * 获取数据库的当前时间，不适合循环调用
     */
    public abstract Date getBDDate();

    public abstract String getDBType();

    /**
     * 计算数据库的当前时间，比getBDDate更适合循环调用
     */
    public Date calcBDDate() {
        if (timeDiff == null) {
            Date dbDate = getBDDate();
            Date appDate = new Date();
            timeDiff = dbDate.getTime() - appDate.getTime();
        }
        Date appNow = new Date();
        appNow = new Date(appNow.getTime() + timeDiff);
        return appNow;
    }

    public SE se(String sql, HashMap<String, Object> ps, Object... pss) {
        SE se = SE.get(sql, ps, pss);
        se.setDao(this);
        return se;
    }

    public SE se(String sql, Object... pss) {
        SE se = SE.get(sql, pss);
        se.setDao(this);
        return se;
    }

    public Select select() {
        Select select = new Select();
        select.setDao(this);
        return select;
    }

    public Insert insert(String table) {
        Insert insert = new Insert(table);
        insert.setDao(this);
        return insert;
    }

    public Update update(String table) {
        Update update = new Update(table);
        update.setDao(this);
        return update;
    }

    public Delete delete() {
        Delete delete = new Delete();
        delete.setDao(this);
        return delete;
    }

    public String joinSQLs(String[] sqls) {
        StringBuffer buf = new StringBuffer();
        for (String str : sqls) {
            buf.append(str + " \n");
        }
        return buf.toString();
    }

    public boolean executeBlock(Block block) {
        block.setDao(this);
        return block.execute();
    }

    private String translate(String sql) {

        if (this instanceof SpringOracleDao) {
            return sql;
        } else if (this instanceof SpringMySQLDao) {
            return sql;
        }
        return sql;
    }

    public StoredProcedure getStoredProcedure(String name) {
        StoredProcedure p = new StoredProcedure(this.getDataSource(), name, false);
        return p;
    }

    public StoredProcedure getStoredFunction(String name) {
        StoredProcedure p = new StoredProcedure(this.getDataSource(), name, true);
        return p;
    }

    public Integer tabDeleteAll(String tab) {
        return execute("delete from " + tab);
    }

    public Integer tabDeleteLogicAll(String tab) {
        return tabDeleteLogicAll(tab, "isdelete");
    }

    public Integer tabDeleteLogicAll(String tab, String col) {
        Update ups = new Update(tab);
        ups.set(col, "N");
        return execute(ups);
    }

    public Integer tabDeleteByIdLogic(String tab, String key, Object value) {
        return tabDeleteByIdLogic(tab, key, value, "isdelete");
    }

    public Integer tabDeleteByIdLogic(String tab, String key, Object value, String col) {
        Update ups = new Update(tab);
        ups.set(col, "N");
        ups.where().andIf(col + "=?", value);
        return execute(ups);
    }

    public Integer tabDeleteByParams(String tab, Object... ps) {
        String sql = "delete from " + tab + " where 1=1 ";
        // ps支持String,int
        if (tab == null || tab.trim().length() == 0 || ps.length % 2 == 1) {
            return -1;
        }
        String key = "";
        for (int i = 0; i < ps.length; i++) {
            if ((i + 1) % 2 == 1) {
                if (ps[i] instanceof String) {
                    key = ps[i] + "";
                } else {
                    return -1;
                }
            } else {
                if (ps[i] instanceof String) {
                    sql = sql + " and " + key + "='" + ps[i] + "'";
                } else {
                    sql = sql + " and " + key + "=" + ps[i];
                }
            }
        }
        return execute(sql);
    }

    public Integer tabDeleteById(String tab, String key, Object value) {
        if (value == null) {
            return -1;
        }
        return execute("delete from " + tab + " where " + key + "=?", value);
    }

    public RcdSet tabQueryTabAll(String tab) {
        return query("select * from " + tab);
    }

    public RcdSet tabQueryTabById(String tab, String key, Object value) {
        return query("select * from " + tab + " where " + key + "=?", value);
    }

}