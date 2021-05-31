package com.dt.core.dao.sql;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBy extends SubSQL {

    private static final long serialVersionUID = -8454799837794172590L;

    private ArrayList<SE> ses = new ArrayList<SE>();

    private ArrayList<SQLKeyword> orders = new ArrayList<SQLKeyword>();

    private ArrayList<SQLKeyword> nulls = new ArrayList<SQLKeyword>();

    public OrderBy by(SE se, SQLKeyword sortType, SQLKeyword nulls) {
        ses.add(se);
        se.setParent(this);
        orders.add(sortType);
        this.nulls.add(nulls);
        return this;
    }

    public OrderBy asc(SE se) {
        return by(se, SQLKeyword.ASC, null);
    }

    public OrderBy desc(SE se) {
        return by(se, SQLKeyword.DESC, null);
    }

    public OrderBy ascNL(SE se) {
        return by(se, SQLKeyword.ASC, SQLKeyword.LAST);
    }

    public OrderBy descNL(SE se) {
        return by(se, SQLKeyword.DESC, SQLKeyword.LAST);
    }

    public OrderBy ascNF(SE se) {
        return by(se, SQLKeyword.ASC, SQLKeyword.FIRST);
    }

    public OrderBy descNF(SE se) {
        return by(se, SQLKeyword.DESC, SQLKeyword.FIRST);
    }

    public OrderBy asc(String se, Object... ps) {
        return asc(SE.get(se, ps));
    }

    public OrderBy desc(String se, Object... ps) {
        return desc(SE.get(se, ps));
    }

    public OrderBy ascNL(String se, Object... ps) {
        return ascNL(SE.get(se, ps));
    }

    public OrderBy descNL(String se, Object... ps) {
        return descNL(SE.get(se, ps));
    }

    public OrderBy ascNF(String se, Object... ps) {
        return ascNF(SE.get(se, ps));
    }

    public OrderBy descNF(String se, Object... ps) {
        return descNF(SE.get(se, ps));
    }

    public String getSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        sql.append(join(SQLKeyword.ORDER.toString(), SQLKeyword.ORDER$BY.toString()));
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            sql.append(join(ses.get(i).getSQL(), orders.get(i).toString(), (nulls.get(i) == null ? ""
                    : SQLKeyword.NULLS.toString() + SQLKeyword.SPACER.toString() + nulls.get(i).toString())));
            if (i < ses.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        return sql.toString();
    }

    public String getParamedSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        sql.append(join(SQLKeyword.ORDER.toString(), SQLKeyword.ORDER$BY.toString()));
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            sql.append(join(ses.get(i).getParamedSQL(), orders.get(i).toString(), (nulls.get(i) == null ? ""
                    : SQLKeyword.NULLS.toString() + SQLKeyword.SPACER.toString() + nulls.get(i).toString())));
            if (i < ses.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        return sql.toString();
    }

    public Object[] getParams() {

        if (this.isEmpty())
            return new Object[]{};
        ArrayList<Object> ps = new ArrayList<Object>();
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(ses.get(i).getParams()));
        }
        return ps.toArray(new Object[ps.size()]);
    }

    public String getParamNamedSQL() {
        if (this.isEmpty())
            return "";
        this.beginParamNameSQL();
        StringBuffer sql = new StringBuffer();
        sql.append(join(SQLKeyword.ORDER.toString(), SQLKeyword.ORDER$BY.toString()));
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            sql.append(join(ses.get(i).getParamNamedSQL(), orders.get(i).toString(), (nulls.get(i) == null ? ""
                    : SQLKeyword.NULLS.toString() + SQLKeyword.SPACER.toString() + nulls.get(i).toString())));
            if (i < ses.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        this.endParamNameSQL();
        return sql.toString();
    }

    public HashMap<String, Object> getNamedParams() {
        HashMap<String, Object> ps = new HashMap<String, Object>();
        if (this.isEmpty())
            return ps;
        this.beginParamNameSQL();
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            ps.putAll(ses.get(i).getNamedParams());
        }
        this.endParamNameSQL();
        return ps;
    }

    public boolean isEmpty() {
        return ses.size() == 0;
    }

    public Select parent() {
        return (Select) super.parent();
    }

    public boolean isAllParamsEmpty() {
        if (this.isEmpty())
            return false;
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            if (!ses.get(i).isAllParamsEmpty())
                return false;
        }
        return true;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        return isAllParamsEmpty();
    }
}
