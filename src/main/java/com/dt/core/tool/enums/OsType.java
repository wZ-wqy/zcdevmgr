package com.dt.core.tool.enums;

public enum OsType {
    Windows, Linux, Mac, Unknow;

    public static OsType getOSType() {
        String osName = System.getProperties().get("os.name").toString().toUpperCase();
        for (OsType os : OsType.values()) {
            if (osName.indexOf(os.name().toUpperCase()) != -1) {
                return os;
            }
        }
        return Unknow;

    }

    public static OsType parse(String str) {
        if (str == null)
            return OsType.Unknow;
        for (OsType os : OsType.values()) {
            if (str.equalsIgnoreCase(os.name())) {
                return os;
            }
        }
        return Unknow;
    }

}
