package com.dt.core.dao.sql;

import com.dt.core.dao.SpringDAO;
import com.dt.core.dao.util.TypedHashMap;
import jodd.util.ArraysUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Update extends DML implements ExecutableSQL {

    private static final long serialVersionUID = 6830730456560631689L;
    private ArrayList<SQL> values = new ArrayList<SQL>();
    private ArrayList<String> fields = new ArrayList<String>();
    private String table = null;
    private String tableAlias = null;
    private UpdateWhere where = new UpdateWhere();
    private SpringDAO dao = null;

    public Update() {

    }

    public Update(String table) {
        this.where.setParent(this);
        this.update(table);
    }

    public static Update create(String table) {
        return new Update(table);
    }

    public UpdateWhere where() {
        return this.where;
    }

    public Update update(String table, String alias) {
        this.table = table;
        this.tableAlias = alias;
        return this;
    }

    public Update update(String table) {
        this.table = table;
        this.tableAlias = null;
        return this;
    }

    public Update set(String fld, Object val) {
        if (val instanceof SQL) {
            set(fld, (SQL) val);
        } else {
            set(fld, SE.get(SQLKeyword.QUESTION.toString(), val));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Update sets(Object... nvs) {
        TypedHashMap<String, Object> map = TypedHashMap.toMap(nvs);
        for (String key : map.keySet()) {
            this.set(key, map.get(key));
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    public Update sets(Map values, String... excludes) {
        for (int i = 0; i < excludes.length; i++) {
            excludes[i] = excludes[i].toLowerCase().trim();
        }

        for (Object field : values.keySet()) {
            if (ArraysUtil.indexOf(excludes, field.toString().toLowerCase()) != -1)
                continue;
            setIf(field.toString(), values.get(field));
        }

        return this;
    }

    public Update setIf(String fld, Object val) {
        if (val instanceof SQL) {
            setIf(fld, (SQL) val);
        } else {
            setIf(fld, SE.get(SQLKeyword.QUESTION.toString(), val));
        }
        return this;
    }

    public Update set(String fld, SQL se) {
        values.add(se);
        se.setParent(this);
        fields.add(fld);
        return this;
    }

    public Update setIf(String fld, SQL se) {
        if (se.isAllParamsEmpty())
            return this;
        return set(fld, se);
    }

    public Update setSE(String fld, SE se) {
        return set(fld, se);
    }

    public Update setSEIf(String fld, SE se) {
        return setIf(fld, se);
    }

    public Update setSE(String fld, String se, Object... ps) {
        return set(fld, SE.get(se, ps));
    }

    public Update setSEIf(String fld, String se, Object... ps) {
        return setIf(fld, SE.get(se, ps));
    }

    public String getSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        sql.append(SQLKeyword.UPDATE.toString() + SQLKeyword.SPACER.toString() + this.table);
        if (this.tableAlias != null) {
            sql.append(SQLKeyword.SPACER.toString() + this.tableAlias + SQLKeyword.SPACER.toString());
        }
        sql.append(SQLKeyword.SPACER.toString() + SQLKeyword.UPDATE$SET.toString() + SQLKeyword.SPACER.toString());
        for (int i = 0; i < fields.size(); i++) {
            values.get(i).setIgnorColon(ignorColon);
            sql.append(fields.get(i) + SQLKeyword.OP$EAUALS + values.get(i).getSQL());
            if (i < fields.size() - 1)
                sql.append(SQLKeyword.COMMA.toString());
        }
        if (!this.where().isEmpty()) {
            where.setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + this.where().getSQL());
        }
        return sql.toString();
    }

    public String getParamedSQL() {
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        sql.append(SQLKeyword.UPDATE.toString() + SQLKeyword.SPACER.toString() + this.table);
        if (this.tableAlias != null) {
            sql.append(SQLKeyword.SPACER.toString() + this.tableAlias + SQLKeyword.SPACER.toString());
        }
        sql.append(SQLKeyword.SPACER.toString() + SQLKeyword.UPDATE$SET.toString() + SQLKeyword.SPACER.toString());
        for (int i = 0; i < fields.size(); i++) {
            values.get(i).setIgnorColon(ignorColon);
            sql.append(fields.get(i) + SQLKeyword.OP$EAUALS + values.get(i).getParamedSQL());
            if (i < fields.size() - 1)
                sql.append(SQLKeyword.COMMA.toString());
        }
        if (!this.where().isEmpty()) {
            where.setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + this.where().getParamedSQL());
        }
        return sql.toString();
    }

    public Object[] getParams() {
        if (this.isEmpty())
            return new Object[]{};
        ArrayList<Object> ps = new ArrayList<Object>();
        for (SQL val : values) {
            val.setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(val.getParams()));
        }
        if (!this.where().isEmpty()) {
            where.setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.where().getParams()));
        }
        return ps.toArray(new Object[ps.size()]);
    }

    public String getParamNamedSQL() {
        if (this.isEmpty())
            return "";
        this.beginParamNameSQL();
        StringBuffer sql = new StringBuffer();
        sql.append(SQLKeyword.UPDATE.toString() + SQLKeyword.SPACER.toString() + this.table);
        if (this.tableAlias != null) {
            sql.append(SQLKeyword.SPACER.toString() + this.tableAlias + SQLKeyword.SPACER.toString());
        }
        sql.append(SQLKeyword.SPACER.toString() + SQLKeyword.UPDATE$SET.toString() + SQLKeyword.SPACER.toString());
        for (int i = 0; i < fields.size(); i++) {
            values.get(i).setIgnorColon(ignorColon);
            sql.append(fields.get(i) + SQLKeyword.OP$EAUALS + values.get(i).getParamNamedSQL());
            if (i < fields.size() - 1)
                sql.append(SQLKeyword.COMMA.toString());
        }
        if (!this.where().isEmpty()) {
            where.setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + this.where().getParamNamedSQL());
        }
        this.endParamNameSQL();
        return sql.toString();
    }

    public HashMap<String, Object> getNamedParams() {
        HashMap<String, Object> ps = new HashMap<String, Object>();
        if (this.isEmpty())
            return ps;
        this.beginParamNameSQL();
        for (SQL val : values) {
            val.setIgnorColon(ignorColon);
            ps.putAll(val.getNamedParams());
        }
        if (!this.where().isEmpty()) {
            where.setIgnorColon(ignorColon);
            ps.putAll(this.where().getNamedParams());
        }
        this.endParamNameSQL();
        return ps;
    }

    public boolean isEmpty() {
        return fields.size() == 0;
    }

    public boolean isAllParamsEmpty() {
        if (this.isEmpty())
            return false;
        for (SQL val : values) {
            val.setIgnorColon(ignorColon);
            if (!val.isAllParamsEmpty())
                return false;
        }
        if (!this.where().isEmpty()) {
            return this.where().isAllParamsEmpty();
        }
        return true;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        return isAllParamsEmpty();
    }

    public Integer execute() {
        return dao.execute(this);
    }

    public SpringDAO getDao() {
        return dao;
    }

    public Update setDao(SpringDAO dao) {
        this.dao = dao;
        return this;
    }

}
