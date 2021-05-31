package com.dt.core.dao.util;

public enum YNBoolean {
    TRUE(true), FALSE(false), NULL(null);

    Boolean value;

    YNBoolean(Boolean b) {
        this.value = b;
    }

    public static YNBoolean parse(String s) {
        if (s == null)
            return YNBoolean.NULL;

        if (s.equalsIgnoreCase("Y") || s.equalsIgnoreCase("YES")) {
            return YNBoolean.TRUE;
        } else if (s.equalsIgnoreCase("N") || s.equalsIgnoreCase("NO")) {
            return YNBoolean.FALSE;
        } else {
            return YNBoolean.NULL;
        }

    }

    public static String toText(boolean b) {
        return b ? "Y" : "N";
    }

    public Boolean getValue() {
        return value;
    }
}
