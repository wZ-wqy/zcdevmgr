package com.dt.core.dao.util;

public enum BITBoolean {
    TRUE(true), FALSE(false), NULL(null);

    Boolean value;

    BITBoolean(Boolean b) {
        this.value = b;
    }

    public static BITBoolean parse(String s) {
        if (s == null)
            return BITBoolean.NULL;

        if (s.equalsIgnoreCase("1")) {
            return BITBoolean.TRUE;
        } else if (s.equalsIgnoreCase("0")) {
            return BITBoolean.FALSE;
        } else {
            return BITBoolean.NULL;
        }

    }

    public Boolean getValue() {
        return value;
    }
}
