package com.dt.core.dao;

import com.dt.core.tool.util.DbUtil;

import java.util.Date;
import java.util.Map;

public abstract class SpringMySQLDao extends SpringDAO {
    protected RcdSet getPageSet(RcdSet set, String sql, int pageIndex, int pageSize, Map<String, Object> params) {
        if (pageIndex <= 0)
            pageIndex = 1;
        int begin = (pageIndex - 1) * pageSize + 1;

        params.put("PAGED_QUERY_ROW_BEGIN", new Integer(begin));
        params.put("PAGESIZE", new Integer(pageSize));
        String querySql = "SELECT * FROM( " + sql + " ) PAGED_QUERY LIMIT :PAGED_QUERY_ROW_BEGIN,:PAGESIZE ";
        this.njdbcTemplate.query(querySql, params, new RcdRowMapper(set));
        return set;
    }

    @Override
    protected RcdSet getPageSet(RcdSet set, String sql, int pageIndex, int pageSize, Object... params) {
        if (pageIndex <= 0)
            pageIndex = 1;
        int begin = (pageIndex - 1) * pageSize + 1;

        Object[] ps = new Object[params.length + 2];
        System.arraycopy(params, 0, ps, 0, params.length);
        ps[params.length] = begin;
        ps[params.length + 1] = pageSize;

        String querySql = "SELECT * FROM( " + sql + " ) PAGED_QUERY LIMIT ?,? ";
        this.jdbcTemplate.query(querySql, new RcdRowMapper(set), ps);
        return set;
    }

    @Override
    public Date getBDDate() {
        return this.uniqueDate("select now()");
    }

    @Override
    public String getDBType() {
        return DbUtil.TYPE_MYSQL;
    }
}
