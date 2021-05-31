package com.dt.module.base.busenum;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum CategoryEnum implements IEnum<Serializable> {

    CATEGORY_KN("1", "知识库"),
    CATEGORY_ASSETS("3", "资产"),
    CATEGORY_ASSETSSTOCK("15", "资产库存"),
    CATEGORY_FLOW("4", "流程"),
    CATEGORY_FLOWTPL("5", "流程模板"),
    CATEGORY_FORMTPL("6", "表单模板"),
    CATEGORY_HC("7", "耗材物品当前"),
    CATEGORY_BJ("8", "备件"),
    CATEGORY_SOFT("12", "软件");

    private String code;
    private String name;

    CategoryEnum(final String code, final String name) {
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
        for (CategoryEnum e : CategoryEnum.values()) {
            if (e.getValue().equals(code)) {
                return e.name;
            }
        }
        return "";
    }
}
