package com.dt.core.dao.sql;

public class UpdateWhere extends ConditionExpression<UpdateWhere> {

    private static final long serialVersionUID = 971068194493582298L;

    protected SQLKeyword getKeyword() {
        return SQLKeyword.WHERE;
    }

    public Update parent() {
        return (Update) super.parent();
    }

    public Update top() {
        return (Update) super.top();
    }

    public Integer execute() {

        return top().execute();
    }

}
