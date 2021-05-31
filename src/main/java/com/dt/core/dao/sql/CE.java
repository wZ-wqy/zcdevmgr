package com.dt.core.dao.sql;

public class CE extends ConditionExpression<CE> implements SQL {

    private static final long serialVersionUID = 1203262402469937222L;

    public CE() {
    }

    public CE(SE se) {
        and(se);
    }

    public CE(String se, Object... ps) {
        and(SE.get(se, ps));
    }
}
