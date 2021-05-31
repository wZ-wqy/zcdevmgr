package com.dt.core.dao;

import com.dt.core.tool.util.DbUtil;

import java.util.Date;
import java.util.Map;

public abstract class SpringOracleDao extends SpringDAO {

    public static void main(String[] args) {
        // SpringOracleDao a=new SpringOracleDao();


    }

    protected RcdSet getPageSet(RcdSet set, String sql, int pageIndex, int pageSize, Map<String, Object> params) {
        if (pageIndex <= 0)
            pageIndex = 1;
        int begin = (pageIndex - 1) * pageSize + 1;

        params.put("PAGED_QUERY_ROW_BEGIN", new Integer(begin));
        params.put("PAGESIZE", new Integer(pageSize));
        String querySql = "SELECT * FROM ( SELECT PAGED_QUERY.*,ROWNUM PAGED_QUERY_ROWNUM FROM ( " + sql
                + " ) PAGED_QUERY) WHERE PAGED_QUERY_ROWNUM <= :PAGED_QUERY_ROW_BEGIN + :PAGESIZE - 1 AND PAGED_QUERY_ROWNUM>=:PAGED_QUERY_ROW_BEGIN";
        this.njdbcTemplate.query(querySql, params, new RcdRowMapper(set));
        return set;
    }

    @Override
    protected RcdSet getPageSet(RcdSet set, String sql, int pageIndex, int pageSize, Object... params) {
        if (pageIndex <= 0)
            pageIndex = 1;
        int begin = (pageIndex - 1) * pageSize + 1;

        Object[] ps = new Object[params.length + 3];
        System.arraycopy(params, 0, ps, 0, params.length);
        ps[params.length] = begin;
        ps[params.length + 1] = pageSize;
        ps[params.length + 2] = begin;

        String querySql = "SELECT * FROM ( SELECT PAGED_QUERY.*,ROWNUM PAGED_QUERY_ROWNUM FROM ( " + sql
                + " ) PAGED_QUERY) WHERE PAGED_QUERY_ROWNUM <= ? + ? - 1 AND PAGED_QUERY_ROWNUM>=?";
        this.jdbcTemplate.query(querySql, new RcdRowMapper(set), ps);
        return set;
    }

    @Override
    public Date getBDDate() {
        return this.uniqueDate("SELECT SYSDATE FROM DUAL");
    }

    @Override
    public String getDBType() {
        return DbUtil.TYPE_ORACLE;
    }

}
