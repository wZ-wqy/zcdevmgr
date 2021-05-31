package com.dt.module.base.busenum;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum AssetsRecycleEnum implements IEnum<Serializable> {

    RECYCLE_BORROW("borrow", "借用"),
    RECYCLE_ALLOCATION("allocation", "调拨中"),
    RECYCLE_REPAIR("repair", "维修中"),
    RECYCLE_INUSE("inuse", "在用"),
    RECYCLE_STOPUSE("stopuse", "停用"),
    RECYCLE_IDLE("idle", "闲置"),
    RECYCLE_SCRAP("scrap", "报废");

    private String code;

    private String name;

    AssetsRecycleEnum(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.code;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonValue
    public static String parseCode(String code) {
        for (AssetsRecycleEnum e : AssetsRecycleEnum.values()) {
            if (e.getValue().equals(code)) {
                return e.name;
            }
        }
        return "";
    }
}
