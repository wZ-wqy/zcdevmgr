package com.dt.core.dao.util;

public enum TFBoolean {
    TRUE(true), FALSE(false), NULL(null);

    Boolean value;

    TFBoolean(Boolean b) {
        this.value = b;
    }

    public static TFBoolean parse(String s) {
        if (s == null)
            return TFBoolean.NULL;

        if (s.equalsIgnoreCase("T") || s.equalsIgnoreCase("TRUE")) {
            return TFBoolean.TRUE;
        } else if (s.equalsIgnoreCase("F") || s.equalsIgnoreCase("FALSE")) {
            return TFBoolean.FALSE;
        } else {
            return TFBoolean.NULL;
        }

    }

    public Boolean getValue() {
        return value;
    }
}
