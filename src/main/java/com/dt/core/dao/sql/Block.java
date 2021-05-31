package com.dt.core.dao.sql;

import com.dt.core.dao.SpringDAO;
import com.dt.core.dao.util.ArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class Block extends SubSQL {

    private static final long serialVersionUID = -858248563510964256L;
    private ArrayList<Integer> tabs = new ArrayList<Integer>();
    private ArrayList<SQL> lines = new ArrayList<SQL>();

    private int tab = 0;
    private SpringDAO dao = null;

    public static void main(String[] args) {


    }

    private String getTab(int tab) {
        String tabs = "";
        for (int i = 1; i <= tab; i++) {
            tabs += "\t";
        }
        return tabs;
    }

    /**
     * 做n个tab缩进
     */
    public Block tab(int n) {
        tab += n;
        return this;
    }

    /**
     * 做1个tab缩进
     */
    public Block tab() {
        return tab(1);
    }

    /**
     * 做n个tab缩出
     */
    public Block detab(int n) {
        tab -= n;
        return this;
    }

    /**
     * 做1个tab缩出
     */
    public Block detab() {
        return detab(1);
    }

    public Block ln(SQL sql) {
        tabs.add(tab);
        lines.add(sql);
        return this;
    }

    /**
     * 追加行
     */
    public Block ln(String sql, Object... ps) {
        ln(SE.get(sql, ps));
        return this;
    }

    /**
     * 追加多行代码
     */
    public Block blk(SQL... subs) {
        for (int i = 0; i < subs.length; i++) {
            SQL sql = subs[i];
            ln(sql);
        }
        return this;
    }

    public Block blk(String... subs) {
        for (int i = 0; i < subs.length; i++) {
            String sql = subs[i];
            ln(sql);
        }
        return this;
    }

    public Block IF(CE ce) {
        ce.startWithSPACER();
        return ln(SE.get("IF" + ce.getParamedSQL() + " THEN ", ce.getParams()));
    }

    public Block IF(String ce, Object... ps) {
        IF(new CE(ce, ps));
        tab();
        return this;
    }

    public Block ELSIF(CE ce) {
        ce.startWithSPACER();
        detab();
        ln(SE.get("ELSIF" + ce.getParamedSQL() + " THEN ", ce.getParams()));
        tab();
        return this;
    }

    public Block ELSIF(String ce, Object... ps) {
        ELSIF(new CE(ce, ps));
        return this;
    }

    public Block ELSE() {
        detab();
        ln("ELSE");
        tab();
        return this;
    }

    public Block END_IF() {
        detab();
        ln("END IF;");
        return this;
    }

    public Block NULL() {
        ln("NULL;");
        return this;
    }

    public Block DECLARE() {
        ln("DECLARE");
        tab();
        return this;
    }

    public Block BEGIN() {
        if (tab != 0) {
            detab();
        }
        ln("BEGIN");
        tab();
        return this;
    }

    public Block END() {
        detab();
        ln("END;");
        return this;
    }

    public Block EXCEPTION(SE se) {
        detab();
        ln("EXCEPTION WHEN " + se.getParamedSQL() + " THEN", se.getParams());
        tab();
        return this;
    }

    public Block EXCEPTION(String se) {
        EXCEPTION(SE.get(se));
        return this;
    }

    public Block EXCEPTION() {
        return EXCEPTION("OTHERS");
    }

    /**
     * 单行注释
     */
    public Block COMMENT_LINE(String cmt) {
        return ln("-- " + cmt);
    }

    /**
     * 多行注释
     */
    public Block COMMENT_BLOCK(String... cmt) {
        ln("/*");
        tab();
        blk(cmt);
        detab();
        ln("*/");
        return this;
    }

    public Block LOOP_CURSOR(String rcdVar, SQL select) {
        ln("FOR " + rcdVar + " IN (");
        tab();
        ln(select);
        detab();
        ln(") LOOP");
        tab();
        return this;
    }

    public Block LOOP_CURSOR(String rcdVar, String select, Object... ps) {
        return LOOP_CURSOR(rcdVar, SE.get(select, ps));
    }

    public Block END_LOOP() {
        detab();
        return ln("END LOOP;");
    }

    public Block WHILE(CE ce) {
        return WHILE(ce.getParamedSQL(), ce.getParams());
    }

    public Block WHILE(String ce, Object... ps) {
        ln("WHILE (" + ce + ")", ps);
        tab();
        return this;
    }

    @SuppressWarnings({"unused"})
    private Block COMMIT() {
        return ln("COMMIT;");
    }

    @SuppressWarnings({"unused"})
    private Block ROLLBACK() {
        return ln("ROLLBACK;");
    }

    public String getSQL() {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < lines.size(); i++) {
            SQL ln = lines.get(i);
            ln.setIgnorColon(true);
            int tab = tabs.get(i);
            sql.append(getTab(tab) + ln.getSQL() + "\n");
        }
        return sql.toString();
    }

    public String getParamedSQL() {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < lines.size(); i++) {
            SQL ln = lines.get(i);
            ln.setIgnorColon(true);
            int tab = tabs.get(i);
            sql.append(getTab(tab) + ln.getParamedSQL() + "\n");
        }
        return sql.toString();
    }

    public Object[] getParams() {
        Object[] ps = new Object[0];
        for (int i = 0; i < lines.size(); i++) {
            SQL ln = lines.get(i);
            ln.setIgnorColon(true);
            ps = ArrayUtil.merege(ps, ln.getParams());
        }
        return ps;
    }

    @Deprecated
    public String getParamNamedSQL() {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < lines.size(); i++) {
            SQL ln = lines.get(i);
            ln.setIgnorColon(true);
            int tab = tabs.get(i);
            sql.append(getTab(tab) + ln.getParamNamedSQL() + "\n");
        }
        return sql.toString();
    }

    @Deprecated
    public HashMap<String, Object> getNamedParams() {
        return new HashMap<String, Object>();
    }

    public boolean isEmpty() {
        for (int i = 0; i < lines.size(); i++) {
            SQL ln = lines.get(i);
            ln.setIgnorColon(true);
            if (!ln.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        return false;
    }

    public boolean execute() {
        return dao.executeBlock(this);
    }

    public SpringDAO getDao() {
        return dao;
    }

    public Block setDao(SpringDAO dao) {
        this.dao = dao;
        return this;
    }

    private void printLn() {
    }

}
