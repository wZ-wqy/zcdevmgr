package com.dt.core.dao.sql;

import com.dt.core.dao.Rcd;

import java.math.BigDecimal;
import java.util.Date;

public interface QueryableSQL {
    Rcd record();

    Integer intValue();

    String stringValue();

    Long longValue();

    Date dateValue();

    BigDecimal decimalValue();

}
