package com.dt.module.ct.service.impl;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import org.springframework.stereotype.Service;

/**
 * @author: lank
 * @date: 2017年8月12日 上午7:04:31
 * @Description:
 */
@Service
public class ContentService extends BaseService {

    public static String TYPE_NEWS = "news";
    public static String TYPE_OHTER = "other";
    public static String TYPE_DOC = "doc";
    public static String SORT_CREATE = "create";
    public static String SORT_MODIFY = "modify";
    public static String SORT_SORT = "sort";

    /**
     * @Description:新增CT
     */
    public R addContent(TypedHashMap<String, Object> ps, String type) {
        Insert me = new Insert("ct_content");
        String idctl = ps.getString("selfid", "N");
        String id = ToolUtil.getUUID();
        if ("Y".equals(idctl)) {
            id = ps.getString("id", id);
        }
        me.set("id", id);
        me.set("dr", "0");
        me.set("type", type);
        me.set("display", ToolUtil.parseYNValueDefN(ps.getString("display")));
        me.set("digest", ToolUtil.parseYNValueDefN(ps.getString("digest")));
        me.setIf("cat_id", ps.getString("cat_id"));
        me.setIf("title", ps.getString("title"));
        me.setIf("profile", ps.getString("profile"));
        me.setIf("urltype", ps.getString("urltype"));
        me.setIf("url", ps.getString("url"));
        me.setIf("mpic", ps.getString("mpic"));
        me.setIf("mpic_loc", ps.getString("mpic_loc"));
        me.setIf("hits", ps.getString("hits"));
        me.setIf("author", ps.getString("author"));
        me.setIf("tag", ps.getString("tag"));
        me.setIf("content", ps.getString("content"));
        me.setIf("mark", ps.getString("mark"));
        me.setSE("create_time", DbUtil.getDbDateString(db.getDBType()));
        me.setSE("update_time", DbUtil.getDbDateString(db.getDBType()));

        me.setIf("col_a", ps.getString("col_a"));
        me.setIf("col_b", ps.getString("col_b"));
        me.setIf("col_c", ps.getString("col_c"));
        me.setIf("col_d", ps.getString("col_d"));
        me.setIf("col_e", ps.getString("col_e"));
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:修改CT
     */
    public R updateContent(TypedHashMap<String, Object> ps) {
        Update me = new Update("ct_content");
        me.set("display", ToolUtil.parseYNValueDefN(ps.getString("display")));
        me.set("digest", ToolUtil.parseYNValueDefN(ps.getString("digest")));
        me.setIf("cat_id", ps.getString("cat_id"));
        me.setIf("title", ps.getString("title"));
        me.setIf("profile", ps.getString("profile"));
        me.setIf("content", ps.getString("content"));
        me.setIf("urltype", ps.getString("urltype"));
        me.setIf("url", ps.getString("url"));
        me.setIf("mpic", ps.getString("mpic"));
        me.setIf("mpic_loc", ps.getString("mpic_loc"));
        me.setIf("hits", ps.getString("hits"));
        me.setIf("author", ps.getString("author"));
        me.setIf("tag", ps.getString("tag"));
        me.setIf("mark", ps.getString("mark"));
        me.setIf("col_a", ps.getString("col_a"));
        me.setIf("col_b", ps.getString("col_b"));
        me.setIf("col_c", ps.getString("col_c"));
        me.setIf("col_d", ps.getString("col_d"));
        me.setIf("col_e", ps.getString("col_e"));
        me.setSE("modify_time", DbUtil.getDbDateString(db.getDBType()));
        me.where().and("id=?", ps.getString("id"));
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:删除CT
     */
    public R deleteContent(String id, String type) {
        if (ToolUtil.isOneEmpty(id, type)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        Update me = new Update("ct_content");
        me.set("deleted", "Y");
        me.where().and("id=?", id).and("type=?", type);
        db.execute(me);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description:根据ID查找CT
     * @param id
     */
    public R queryContentById(String id) {
        Rcd rs = db.uniqueRecord("select * from ct_content where id=?", id);
        if (ToolUtil.isEmpty(rs)) {
            return R.FAILURE_NO_DATA();
        }
        return R.SUCCESS_OPER(rs.toJsonObject());
    }

    /**
     * @Description:更新节点名称
     * @param ps
     * @param type
     */
    private String rebuildQueryContentSql(TypedHashMap<String, Object> ps, String type) {
        String sdate = ps.getString("sdate");
        String edate = ps.getString("edate");
        String sort = ps.getString("sort");
        String noContent = ps.getString("noContent", "N");
        String sql = "select <#CONTENT#> id,cat_id,digest,title,profile,urltype,url,type,mpic,mpic_loc,hits,author,create_time,update_time ,display,mark,tag from ct_content where dr='0' and type='"
                + type + "' ";
        if ("Y".equals(noContent)) {
            sql = sql.replaceAll("<#CONTENT#>", "");
        } else {
            sql = sql.replaceAll("<#CONTENT#>", "CONTENT,");
        }
        if (ToolUtil.isNotEmpty(sdate)) {
            if (db.getDBType().equals(DbUtil.TYPE_ORACLE)) {
                sql = sql + " and create_time>=to_date('" + sdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss') ";
            } else if (db.getDBType().equals(DbUtil.TYPE_MYSQL)) {
                sql = sql + " and create_time>=str_to_date('" + sdate + "','%Y-%m-%d %H') ";
            }

        }
        if (ToolUtil.isNotEmpty(edate)) {
            if (db.getDBType().equals(DbUtil.TYPE_ORACLE)) {
                sql = sql + " and create_time<=to_date('" + edate + " 23:59:59','yyyy-mm-dd hh24:mi:ss') ";
            } else if (db.getDBType().equals(DbUtil.TYPE_MYSQL)) {
                sql = sql + " and create_time<=str_to_date('" + sdate + "','%Y-%m-%d %H') ";
            }

        }
        if (ToolUtil.isEmpty(sort)) {
            // 如果是新闻
            if (type.equals(ContentService.TYPE_NEWS)) {
                sql = sql + " order by create_time desc";
            }
        } else {
            // 按照预定的排序
            if (sort.equals(SORT_CREATE)) {
                sql = sql + " order by create_time desc";
            }
        }
        return sql;
    }

    /**
     * @Description:查找CT数量
     */
    public int queryContentCount(TypedHashMap<String, Object> ps, String type) {
        String sql = rebuildQueryContentSql(ps, type);
        sql = "select count(1) value from (" + sql + ") tab";
        int total = db.uniqueRecord(sql).getInteger("value");
        return total;
    }

    /**
     * @Description:查找页数
     */
    public int queryContentPageCount(TypedHashMap<String, Object> ps, String type, int pageSize) {
        int total = queryContentCount(ps, type);
        return DbUtil.getTotalPage(total, pageSize);
    }

    /**
     * @Description:查找CT
     */
    public R queryContentPage(TypedHashMap<String, Object> ps, int pageSize, int pageIndex, String type) {
        String sql = rebuildQueryContentSql(ps, type);
        return R.SUCCESS_OPER(
                db.query(DbUtil.getDBPageSql(db.getDBType(), sql, pageSize, pageIndex)).toJsonArrayWithJsonObject());
    }
}
