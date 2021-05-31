package com.dt.core.dao.sql;

public class SelectWhere extends Where {

    private static final long serialVersionUID = 1110184249008912276L;

    protected SQLKeyword getKeyword() {
        return SQLKeyword.WHERE;
    }

    public Select parent() {
        return (Select) super.parent();
    }

    public Select top() {
        return (Select) super.top();
    }
}
