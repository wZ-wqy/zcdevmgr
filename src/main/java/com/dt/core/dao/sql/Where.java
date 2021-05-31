package com.dt.core.dao.sql;

public class Where extends ConditionExpression<Where> {

    private static final long serialVersionUID = 4833884351300549416L;

    protected SQLKeyword getKeyword() {
        return SQLKeyword.WHERE;
    }
}
