package com.dt.core.dao.sql;

import java.io.Serializable;
import java.util.HashMap;

public interface SQL extends Serializable {
    String PNAME_PREFIX = "PARAM";

    /**
     * IgnorColon
     */
    SubSQL setIgnorColon(boolean b);

    String getSQL();

    String getParamedSQL();

    Object[] getParams();

    String getParamNamedSQL();

    HashMap<String, Object> getNamedParams();

    boolean isEmpty();

    boolean isAllParamsEmpty();

    boolean isAllParamsEmpty(boolean isCE);

    SQL top();

    SQL parent();

    void setParent(SQL sql);

    void beginParamNameSQL();

    void endParamNameSQL();

    String getNextParamName(boolean withColon);

}
