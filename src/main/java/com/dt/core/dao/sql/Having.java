package com.dt.core.dao.sql;

public class Having extends ConditionExpression<Having> {

    private static final long serialVersionUID = -6807113206749133680L;

    protected SQLKeyword getKeyword() {
        return SQLKeyword.GROUP$HAVING;
    }

    public GroupBy parent() {
        return (GroupBy) super.parent();
    }

}
