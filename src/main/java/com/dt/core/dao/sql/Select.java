package com.dt.core.dao.sql;

import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.SpringDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Select extends DML implements QueryableSQL {

    private static final long serialVersionUID = 7749949467796807954L;
    private ArrayList<SQL> tables = new ArrayList<SQL>();
    private ArrayList<String> tableAliases = new ArrayList<String>();

    private ArrayList<SQL> fields = new ArrayList<SQL>();
    private ArrayList<String> fieldsAliases = new ArrayList<String>();

    private ArrayList<String> fieldsPrefix = new ArrayList<String>();
    private String currentFieldPrefix = null;

    private SelectWhere where = new SelectWhere();
    private OrderBy orderBy = new OrderBy();
    private GroupBy groupBy = new GroupBy();
    private SpringDAO dao = null;

    public Select() {
        this.where.setParent(this);
        this.orderBy.setParent(this);
        this.groupBy.setParent(this);
    }

    public static Select init() {
        return new Select();
    }

    public SelectWhere where() {
        return where;
    }

    public GroupBy groupBy() {
        return groupBy;
    }

    public OrderBy orderBy() {
        return orderBy;
    }

    public Select from(SE table, String alias) {
        this.tables.add(table);
        table.setParent(this);
        this.tableAliases.add(alias);
        currentFieldPrefix = null;
        return this;
    }

    public Select from(String table, String alias, Object... ps) {
        currentFieldPrefix = null;
        return from(SE.get(table, ps), alias);
    }

    public Select from(Select table, String alias) {
        this.tables.add(table);
        table.setParent(this);
        this.tableAliases.add(alias);
        currentFieldPrefix = null;
        return this;
    }

    public Select from(String table) {
        currentFieldPrefix = null;
        return from(SE.get(table), null);
    }

    public Select froms(String... table) {
        for (String tab : table) {
            from(SE.get(tab), null);
        }
        currentFieldPrefix = null;
        return this;
    }

    public Select fromAs(String... tableOrAlias) {
        String[] tabs;
        if (tableOrAlias.length % 2 == 1) {
            tabs = new String[tableOrAlias.length + 1];
            for (int i = 0; i < tableOrAlias.length; i++)
                tabs[i] = tableOrAlias[i];
            tabs[tabs.length - 1] = null;
        } else
            tabs = tableOrAlias;

        for (int i = 0; i < tabs.length; i++) {
            String table = tabs[i];
            i++;
            String alias = tabs[i];

            from(SE.get(table), alias);
        }
        currentFieldPrefix = null;
        return this;
    }

    public Select prefix() {
        int last = tableAliases.size() - 1;
        String pfx = tableAliases.get(last);
        if (pfx == null || pfx.length() == 0)
            pfx = tables.get(last).getSQL();
        return prefix(pfx);
    }

    public Select prefix(String pfx) {
        currentFieldPrefix = pfx;
        return this;
    }

    public Select select(SE se, String alias) {
        this.fields.add(se);
        se.setParent(this);
        this.fieldsAliases.add(alias);
        this.fieldsPrefix.add(currentFieldPrefix);
        return this;
    }

    public Select select(Select se, String alias) {
        this.fields.add(se);
        se.setParent(this);
        this.fieldsAliases.add(alias);
        this.fieldsPrefix.add(currentFieldPrefix);
        return this;
    }

    public Select select(String fld, Object... ps) {

        return this.select(SE.get(fld, ps), "");
    }

    public Select select(String fld, String alias, Object... ps) {
        return this.select(SE.get(fld, ps), alias);
    }

    public Select selects(SE... se) {
        for (SE s : se) {
            this.select(s, null);
        }
        return this;
    }

    public Select selects(String... fld) {
        for (String s : fld) {// by lank
            this.select(s);
        }
        return this;
    }

    public Select selects(Select... se) {
        for (Select s : se) {
            this.select(s, null);
        }
        return this;
    }

    public Select selectAs(String... fldOrAlias) {
        String[] fields;
        if (fldOrAlias.length % 2 == 1) {
            fields = new String[fldOrAlias.length + 1];
            for (int i = 0; i < fldOrAlias.length; i++)
                fields[i] = fldOrAlias[i];
            fields[fields.length - 1] = null;
        } else
            fields = fldOrAlias;

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            i++;
            String alias = fields[i];
            this.select(field, alias);
        }
        return this;
    }

    public String getSQL() {
        StringBuffer sql = new StringBuffer();
        if (this.isEmpty())
            return "";
        sql.append(SQLKeyword.SELECT.toString() + SQLKeyword.SPACER.toString());
        if (this.fields.size() == 0)
            sql.append(SQLKeyword.SELECT_STAR.toString() + SQLKeyword.SPACER);
        else {
            for (int i = 0; i < this.fields.size(); i++) {
                fields.get(i).setIgnorColon(ignorColon);
                sql.append(join(
                        (this.fieldsPrefix.get(i) == null ? "" : this.fieldsPrefix.get(i) + SQLKeyword.DOT.toString())
                                + this.fields.get(i).getSQL(),
                        (this.fieldsAliases.get(i) == null ? "" : this.fieldsAliases.get(i))));
                if (i < this.fields.size() - 1)
                    sql.append(SQLKeyword.COMMA);
            }
        }
        sql.append(SQLKeyword.SPACER.toString() + SQLKeyword.FROM + SQLKeyword.SPACER);
        for (int i = 0; i < this.tables.size(); i++) {
            tables.get(i).setIgnorColon(ignorColon);
            String sub = "(" + this.tables.get(i).getSQL() + ")";
            sql.append(join(sub, (this.tableAliases.get(i) == null ? "" : this.tableAliases.get(i))));
            if (i < this.tables.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        if (!this.where().isEmpty()) {
            where().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + where().getSQL());
        }

        if (!this.groupBy().isEmpty()) {
            groupBy().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + groupBy().getSQL());
        }
        if (!this.orderBy().isEmpty()) {
            orderBy().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + orderBy().getSQL());
        }
        return sql.toString();
    }

    public String getParamedSQL() {
        StringBuffer sql = new StringBuffer();
        if (this.isEmpty())
            return "";
        sql.append(SQLKeyword.SELECT.toString() + SQLKeyword.SPACER.toString());
        if (this.fields.size() == 0)
            sql.append(SQLKeyword.SELECT_STAR.toString() + SQLKeyword.SPACER);
        else {
            for (int i = 0; i < this.fields.size(); i++) {
                this.fields.get(i).setIgnorColon(ignorColon);
                sql.append(join(
                        (this.fieldsPrefix.get(i) == null ? "" : this.fieldsPrefix.get(i) + SQLKeyword.DOT.toString())
                                + this.fields.get(i).getParamedSQL(),
                        (this.fieldsAliases.get(i) == null ? "" : this.fieldsAliases.get(i))));
                if (i < this.fields.size() - 1)
                    sql.append(SQLKeyword.COMMA);
            }
        }
        sql.append(SQLKeyword.SPACER.toString() + SQLKeyword.FROM + SQLKeyword.SPACER);
        for (int i = 0; i < this.tables.size(); i++) {
            tables.get(i).setIgnorColon(ignorColon);
            String sub = "(" + this.tables.get(i).getParamedSQL() + ")";

            sql.append(join(sub, (this.tableAliases.get(i) == null ? "" : this.tableAliases.get(i))));
            if (i < this.tables.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        if (!this.where().isEmpty()) {
            where().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + where().getParamedSQL());
        }
        if (!this.groupBy().isEmpty()) {
            groupBy().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + groupBy().getParamedSQL());
        }
        if (!this.orderBy().isEmpty()) {
            orderBy().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + orderBy().getParamedSQL());
        }
        return sql.toString();
    }

    public Object[] getParams() {
        if (this.isEmpty())
            return new Object[]{};
        ArrayList<Object> ps = new ArrayList<Object>();
        for (int i = 0; i < this.fields.size(); i++) {
            fields.get(i).setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.fields.get(i).getParams()));
        }
        for (int i = 0; i < this.tables.size(); i++) {
            tables.get(i).setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.tables.get(i).getParams()));
        }
        if (!this.where().isEmpty()) {
            this.where().setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.where().getParams()));
        }
        if (!this.groupBy().isEmpty()) {
            groupBy().setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.groupBy().getParams()));
        }
        if (!this.orderBy().isEmpty()) {
            orderBy().setIgnorColon(ignorColon);
            ps.addAll(Utils.toArrayList(this.orderBy().getParams()));
        }
        return ps.toArray(new Object[ps.size()]);
    }

    public String getParamNamedSQL() {
        StringBuffer sql = new StringBuffer();
        if (this.isEmpty())
            return "";
        this.beginParamNameSQL();
        sql.append(SQLKeyword.SELECT.toString() + SQLKeyword.SPACER.toString());
        if (this.fields.size() == 0)
            sql.append(SQLKeyword.SELECT_STAR.toString() + SQLKeyword.SPACER);
        else {
            for (int i = 0; i < this.fields.size(); i++) {
                fields.get(i).setIgnorColon(ignorColon);
                sql.append(join(
                        (this.fieldsPrefix.get(i) == null ? "" : this.fieldsPrefix.get(i) + SQLKeyword.DOT.toString())
                                + this.fields.get(i).getParamNamedSQL(),
                        (this.fieldsAliases.get(i) == null ? "" : this.fieldsAliases.get(i))));
                if (i < this.fields.size() - 1)
                    sql.append(SQLKeyword.COMMA);
            }
        }
        sql.append(SQLKeyword.SPACER.toString() + SQLKeyword.FROM + SQLKeyword.SPACER);
        for (int i = 0; i < this.tables.size(); i++) {
            tables.get(i).setIgnorColon(ignorColon);
            String sub = "(" + this.tables.get(i).getParamNamedSQL() + ")";
            sql.append(join(sub, (this.tableAliases.get(i) == null ? "" : this.tableAliases.get(i))));
            if (i < this.tables.size() - 1)
                sql.append(SQLKeyword.COMMA);
        }
        if (!this.where().isEmpty()) {
            where().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + where().getParamNamedSQL());
        }
        if (!this.groupBy().isEmpty()) {
            groupBy().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + groupBy().getParamNamedSQL());
        }
        if (!this.orderBy().isEmpty()) {
            orderBy().setIgnorColon(ignorColon);
            sql.append(SQLKeyword.SPACER.toString() + orderBy().getParamNamedSQL());
        }
        this.endParamNameSQL();
        return sql.toString();
    }

    public HashMap<String, Object> getNamedParams() {
        HashMap<String, Object> ps = new HashMap<String, Object>();
        if (this.isEmpty())
            return ps;
        this.beginParamNameSQL();
        for (int i = 0; i < this.fields.size(); i++) {
            fields.get(i).setIgnorColon(ignorColon);
            ps.putAll(this.fields.get(i).getNamedParams());
        }
        for (int i = 0; i < this.tables.size(); i++) {
            tables.get(i).setIgnorColon(ignorColon);
            ps.putAll(this.tables.get(i).getNamedParams());
        }
        if (!this.where().isEmpty()) {
            where().setIgnorColon(ignorColon);
            ps.putAll(this.where().getNamedParams());
        }
        if (!this.groupBy().isEmpty()) {
            groupBy().setIgnorColon(ignorColon);
            ps.putAll(this.groupBy().getNamedParams());
        }
        if (!this.orderBy().isEmpty()) {
            orderBy().setIgnorColon(ignorColon);
            ps.putAll(this.orderBy().getNamedParams());
        }
        this.endParamNameSQL();
        return ps;
    }

    public boolean isEmpty() {
        return this.tables.size() == 0;
    }

    public boolean isAllParamsEmpty() {
        if (this.isEmpty())
            return false;

        for (int i = 0; i < this.fields.size(); i++) {
            fields.get(i).setIgnorColon(ignorColon);
            if (!this.fields.get(i).isAllParamsEmpty())
                return false;
        }
        for (int i = 0; i < this.tables.size(); i++) {
            tables.get(i).setIgnorColon(ignorColon);
            if (!this.tables.get(i).isAllParamsEmpty())
                return false;
        }
        if (!this.where().isEmpty()) {
            where().setIgnorColon(ignorColon);
            if (!this.where().isAllParamsEmpty())
                return false;
        }
        if (!this.groupBy().isEmpty()) {
            groupBy().setIgnorColon(ignorColon);
            if (!this.groupBy().isAllParamsEmpty())
                return false;
        }
        if (!this.orderBy().isEmpty()) {
            orderBy().setIgnorColon(ignorColon);
            return this.orderBy().isAllParamsEmpty();
        }
        return true;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        return isAllParamsEmpty();
    }

    public SpringDAO getDao() {
        return dao;
    }

    public void setDao(SpringDAO dao) {
        this.dao = dao;
    }

    public RcdSet query() {
        return dao.query(this);
    }

    public Rcd record() {
        return dao.uniqueRecord(this);
    }

    public Integer intValue() {
        return dao.uniqueInteger(this);
    }

    public String stringValue() {
        return dao.uniqueString(this);
    }

    public Long longValue() {
        return dao.uniqueLong(this);
    }

    public Date dateValue() {
        return dao.uniqueDate(this);
    }

    public BigDecimal decimalValue() {
        return dao.uniqueDecimal(this);
    }

}
