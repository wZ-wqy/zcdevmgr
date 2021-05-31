package com.dt.core.dao.sql;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupBy extends SubSQL {

    private static final long serialVersionUID = -7289081382236930585L;

    private ArrayList<SE> ses = new ArrayList<SE>();

    private Having having = new Having();

    public GroupBy() {
        this.having.setParent(this);
    }

    public Having having() {
        return having;
    }

    public GroupBy bys(String... fld) {
        for (String f : fld) {
            this.by(f);
        }
        return this;
    }

    public GroupBy by(SE se) {
        ses.add(se);
        se.setParent(this);
        return this;
    }

    public GroupBy by(String se, Object... ps) {
        return by(SE.get(se, ps));
    }

    public String getSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        sql.append(join(SQLKeyword.GROUP.toString(), SQLKeyword.GROUP$BY.toString()));
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            sql.append(join(ses.get(i).getSQL()));
            if (i < ses.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        if (!having().isEmpty()) {
            having().setIgnorColon(ignorColon);
            sql.append(having().getSQL());
        }
        return sql.toString();
    }

    public String getParamedSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        sql.append(join(SQLKeyword.GROUP.toString(), SQLKeyword.GROUP$BY.toString()));
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            sql.append(join(ses.get(i).getParamedSQL()));
            if (i < ses.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        if (!having().isEmpty()) {
            having().setIgnorColon(ignorColon);
            sql.append(having().getParamedSQL());
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
        if (!this.having().isEmpty()) {
            having().setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.having().getParams()));
        }
        return ps.toArray(new Object[ps.size()]);
    }

    public String getParamNamedSQL() {
        if (this.isEmpty())
            return "";
        this.beginParamNameSQL();
        StringBuffer sql = new StringBuffer();
        sql.append(join(SQLKeyword.GROUP.toString(), SQLKeyword.GROUP$BY.toString()));
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            sql.append(join(ses.get(i).getParamNamedSQL()));
            if (i < ses.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        if (!having().isEmpty()) {
            having().setIgnorColon(ignorColon);
            sql.append(having().getParamNamedSQL());
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
        if (!this.having().isEmpty()) {
            having().setIgnorColon(ignorColon);
            ps.putAll(this.having().getNamedParams());
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
        if (!this.having().isEmpty()) {
            return this.having().isAllParamsEmpty();
        }
        return true;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        return isAllParamsEmpty();
    }
}
