package com.dt.core.dao.sql;

public abstract class SubSQL implements SQL {

    private static final long serialVersionUID = 6083127890659811198L;
    protected boolean ignorColon = false;
    protected boolean replaceNull = true;
    private SQL parent = null;
    private int nameIndex = 0;
    private SQL currentTop;

    /**
     * IgnorColon
     */
    public SubSQL setIgnorColon(boolean b) {
        ignorColon = b;
        return this;
    }

    public void setReplaceNull(boolean replaceNull) {
        this.replaceNull = replaceNull;
    }

    public boolean isAllParamsEmpty() {
        return isAllParamsEmpty(false);
    }

    public SQL parent() {
        return parent;
    }

    public String toString() {
        return this.getSQL();
    }

    public void setParent(SQL sql) {
        this.parent = sql;
    }

    public void beginParamNameSQL() {
        currentTop = this.top();
        if (!this.equals(currentTop))
            return;
        nameIndex = 0;
    }

    public String getNextParamName(boolean withColon) {
        if (!this.equals(currentTop))
            return currentTop.getNextParamName(withColon);
        else {
            nameIndex++;
            return (withColon ? SQLKeyword.COLON : "") + PNAME_PREFIX + "_" + nameIndex;
        }
    }

    public void endParamNameSQL() {
        if (!this.equals(currentTop))
            return;
        currentTop = null;
    }

    public SQL top() {
        SQL se = this;
        while (se.parent() != null)
            se = se.parent();
        return se;
    }

    public StringBuffer join(String... strings) {
        StringBuffer buffer = new StringBuffer();
        for (String s : strings) {
            if (s.startsWith(SQLKeyword.SPACER.toString())) {
                buffer.append(s);
            } else {
                buffer.append(SQLKeyword.SPACER.toString() + s);
            }
        }
        if (!strings[strings.length - 1].endsWith(SQLKeyword.SPACER.toString())) {
            buffer.append(SQLKeyword.SPACER.toString());
        }
        return buffer;
    }

}
