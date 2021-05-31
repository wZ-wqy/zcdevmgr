package com.dt.core.dao.sql;

public abstract class DML extends SubSQL {

    private static final long serialVersionUID = 4747961455863315546L;

    public SE toSE() {
        SE se = SE.get(this.getParamedSQL(), this.getParams());
        return se;
    }

}
