package com.dt.core.dao.sql;

public class DeleteWhere extends ConditionExpression<DeleteWhere> {

    private static final long serialVersionUID = 452409120718188486L;

    protected SQLKeyword getKeyword() {
        return SQLKeyword.WHERE;
    }

    public Delete parent() {
        return (Delete) super.parent();
    }

    public Delete top() {
        return (Delete) super.top();
    }

    public int execute() {
        return this.top().execute();
    }

}