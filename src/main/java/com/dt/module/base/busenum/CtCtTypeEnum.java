package com.dt.module.base.busenum;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;

public enum CtCtTypeEnum implements IEnum<Serializable> {

    NEWS("news", "新闻"), DOC("doc", "文档"), COMPANY("company", "公司");

    private String code;

    private String desc;

    CtCtTypeEnum(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Serializable getValue() {
        return this.code;
    }

    @JsonValue
    public String getDesc() {
        return this.desc;
    }
}
