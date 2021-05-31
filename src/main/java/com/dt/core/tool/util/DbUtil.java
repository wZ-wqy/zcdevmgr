package com.dt.core.tool.util;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.tool.util.support.DateTimeKit;

import java.util.Date;

/**
 * @author: lank
 * @date: 2017年8月12日 上午10:21:06
 * @Description:
 */
public class DbUtil {
    /**
     * @Description: pageSize:每页显示数
     */
    public static String TYPE_ORACLE = "oracle";
    public static String TYPE_MYSQL = "mysql";
    public static String TYPE_SQLLITE = "sqllite";

    private static String getOraclePageSql(String sql, int pageSize, int pageIndex) {
        if (ToolUtil.isOneEmpty(pageSize, pageIndex) || pageSize == -1 || pageIndex == -1) {
            return sql;
        }
        int first = (pageSize * pageIndex);
        int second = (pageIndex - 1) * pageSize + 1;
        String ressql = " select * from(select rownum num,u.* from(" + sql + ") u where rownum<=" + first
                + ") where num>=" + second;
        return ressql;
    }

    public static int getTotalPage(int total, int rows) {
        if (rows <= 0) {
            return 1;
        }
        int totalPage = (total % rows) == 0 ? (total / rows) : ((total / rows) + 1);
        return totalPage;
    }

    private static String getMysqlPageSql(String sql, int pageSize, int pageIndex) {
        if (ToolUtil.isOneEmpty(pageSize, pageIndex) || pageSize == -1 || pageIndex == -1 || pageSize == 0
                || pageIndex == 0) {
            return sql;
        }
        int first = pageSize * (pageIndex - 1);
        int second = pageSize;
        String ressql = " select * from(" + sql + ") u limit " + first + "," + second;
        return ressql;
    }

    public static String getDBPageSql(String type, String sql, int pageSize, int pageIndex) {
        if (type.equals(TYPE_ORACLE)) {
            return getOraclePageSql(sql, pageSize, pageIndex);
        } else if (type.equals(TYPE_MYSQL)) {
            return getMysqlPageSql(sql, pageSize, pageIndex);
        }
        return sql;
    }

    // 至少存在一组数据,start,length|pageSize,pageIndex 否则返回null
    public static JSONObject formatPageParameter(String start, String length, String pageSize, String pageIndex) {
        int c_pageIndex = -1;
        int c_pageSize = -1;
        if (ToolUtil.isOneEmpty(start, length)) {
            // start,length数据不存在
            if (ToolUtil.isOneEmpty(pageIndex, pageSize)) {
                return null;
            } else {
                c_pageIndex = ConvertUtil.toInt(pageIndex, -1);
                c_pageSize = ConvertUtil.toInt(pageSize, -1);
            }
        } else {
            int startV = ConvertUtil.toInt(start);
            int lengthV = ConvertUtil.toInt(length);
            c_pageSize = lengthV;
            c_pageIndex = startV / lengthV + 1;
        }
        JSONObject res = new JSONObject();
        res.put("pageindex", c_pageIndex);
        res.put("pagesize", c_pageSize);
        return res;
    }

    public static String queryBuild(String tab, String orderby) {
        String sql = "select * from " + tab;
        if (ToolUtil.isNotEmpty(orderby)) {
            sql += " order by " + orderby;
        }
        return sql;
    }

    public static String queryByIdBuild(String tab, String key, Object value, String orderby) {
        String sql = "select * from " + tab + " where " + key + "='" + value + "' ";
        if (ToolUtil.isNotEmpty(orderby)) {
            sql += " order by " + orderby;
        }
        return sql;
    }

    public static String getDbDateString(String type) {
        if (type.equals(TYPE_ORACLE)) {
            return "sysdate";
        } else if (type.equals(TYPE_MYSQL)) {
            return "now()";
        } else {
            return "sysdate";
        }
    }

    private static String getDbDayString(String type, String day, String daytype) {
        if ("before".equals(daytype)) {
            if (type.equals(TYPE_ORACLE)) {
                return " sysdate-" + day;
            } else if (type.equals(TYPE_MYSQL)) {
                return " str_to_date('"
                        + DateTimeKit.format(DateTimeKit.offsiteDay(new Date(), ToolUtil.toInt(day) * (-1)),
                        DateTimeKit.NORM_DATE_PATTERN)
                        + "', '%Y-%m-%d %H')";
            } else {
                return "sysdate-" + day;
            }
        } else {
            if (type.equals(TYPE_ORACLE)) {
                return " sysdate+" + day;
            } else if (type.equals(TYPE_MYSQL)) {
                return " str_to_date('"
                        + DateTimeKit.format(DateTimeKit.offsiteDay(new Date(), ToolUtil.toInt(day) * (1)),
                        DateTimeKit.NORM_DATE_PATTERN)
                        + "', '%Y-%m-%d %H')";
            } else {
                return "sysdate+" + day;
            }
        }

    }

    public static String getDbDayAfterString(String type, String day) {
        return getDbDayString(type, day, "after");
    }

    public static String getDbDayBeforeString(String type, String day) {
        return getDbDayString(type, day, "before");
    }

    public static void main(String[] args) {


    }
}
