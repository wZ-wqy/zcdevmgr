package com.dt.module.base.busenum;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum UserTypeEnum implements IEnum<Serializable> {

    SYSTEM("sys", "系统"), EMPL("empl", "组织"), CRM("crm", "会员粉丝"), WX("wx", "微信用户");

    private String code;

    private String desc;

    UserTypeEnum(final String code, final String desc) {
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
