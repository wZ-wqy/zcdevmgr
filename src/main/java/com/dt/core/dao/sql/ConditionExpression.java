package com.dt.core.dao.sql;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * �������ʽ Ĭ���� AND ��ʼ������ʹ��StartWith����������ʼ�ַ�
 */
public class ConditionExpression<E> extends SubSQL {
    /**
     *
     */
    private static final long serialVersionUID = -6253773468492989499L;
    protected ArrayList<SQL> ses = new ArrayList<SQL>();
    protected ArrayList<SQLKeyword> logics = new ArrayList<SQLKeyword>();

    private SQLKeyword startWith = SQLKeyword.AND;

    public ConditionExpression() {
    }

    public ConditionExpression(SE se) {
        and(se);
    }

    public ConditionExpression(String se, Object... ps) {
        and(SE.get(se, ps));
    }

    protected SQLKeyword getKeyword() {
        return startWith;
    }

    @SuppressWarnings("unchecked")
    public E startWithAND() {
        this.startWith = SQLKeyword.AND;
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E startWithOR() {
        this.startWith = SQLKeyword.OR;
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E startWithSPACER() {
        this.startWith = SQLKeyword.SPACER;
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E startWithWHERE() {
        this.startWith = SQLKeyword.WHERE;
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E or(SE se) {
        ses.add(se);
        se.setParent(this);
        logics.add(SQLKeyword.OR);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E orIf(SE se) {
        if (se.isAllParamsEmpty(true))
            return (E) this;
        return or(se);
    }

    @SuppressWarnings("unchecked")
    public E and(SE se) {
        ses.add(se);
        se.setParent(this);
        logics.add(SQLKeyword.AND);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E andIf(SE se) {
        if (se.isAllParamsEmpty(true))
            return (E) this;
        return and(se);
    }

    public E and(String se, Object... ps) {
        return and(SE.get(se, ps));
    }

    @SuppressWarnings("unchecked")
    public E andIf(String se, Object... ps) {
        SE _se = SE.get(se, ps);
        if (_se.isAllParamsEmpty(true))
            return (E) this;
        return and(_se);
    }

    public E or(String se, Object... ps) {
        return or(SE.get(se, ps));
    }

    @SuppressWarnings("unchecked")
    public E orIf(String se, Object... ps) {
        SE _se = SE.get(se, ps);
        if (_se.isAllParamsEmpty(true))
            return (E) this;
        return or(SE.get(se, ps));
    }

    public String getSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        if (this.getKeyword() == SQLKeyword.SPACER) {
            sql.append(this.getKeyword().toString());
        } else {
            sql.append(SQLKeyword.SPACER + this.getKeyword().toString() + SQLKeyword.SPACER);
        }
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            if (i == 0) {
                sql.append(join(ses.get(i).getSQL()));
            } else {
                sql.append(join(logics.get(i).toString(), ses.get(i).getSQL()));
            }
        }
        return sql.toString();
    }

    public String getParamedSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        if (this.getKeyword() == SQLKeyword.SPACER) {
            sql.append(this.getKeyword().toString());
        } else {
            sql.append(SQLKeyword.SPACER + this.getKeyword().toString() + SQLKeyword.SPACER);
        }
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            if (i == 0) {
                sql.append(join(ses.get(i).getParamedSQL()));
            } else {
                sql.append(join(logics.get(i).toString(), ses.get(i).getParamedSQL()));
            }
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
        StringBuffer sql = new StringBuffer();
        if (this.getKeyword() == SQLKeyword.SPACER) {
            sql.append(this.getKeyword().toString());
        } else {
            sql.append(SQLKeyword.SPACER + this.getKeyword().toString() + SQLKeyword.SPACER);
        }
        this.beginParamNameSQL();

        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            if (i == 0) {
                sql.append(join(ses.get(i).getParamNamedSQL()));
            } else {
                sql.append(join(logics.get(i).toString(), ses.get(i).getParamNamedSQL()));
            }
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

    /**
     * 默认为CE模式 CE=true
     */

    public boolean isAllParamsEmpty() {
        return isAllParamsEmpty(true);
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        if (this.isEmpty())
            return false;
        for (int i = 0; i < ses.size(); i++) {
            ses.get(i).setIgnorColon(ignorColon);
            if (!ses.get(i).isAllParamsEmpty(isCE))
                return false;
        }
        return true;
    }

}
