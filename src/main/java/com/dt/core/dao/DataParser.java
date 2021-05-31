package com.dt.core.dao;

import com.dt.core.dao.util.BITBoolean;
import com.dt.core.dao.util.DateUtil;
import com.dt.core.dao.util.TFBoolean;
import com.dt.core.dao.util.YNBoolean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class DataParser {
    public String parseString(Object val) {
        if (val == null)
            return null;
        if (val instanceof String) {
            return (String) val;
        } else {
            return val.toString();
        }
    }

    public Boolean parseBoolean(Object val) {
        if (val == null)
            return null;
        if (val instanceof Boolean) {
            return (Boolean) val;
        } else {
            String strval = parseString(val);

            YNBoolean yn = YNBoolean.parse(strval);
            if (yn != YNBoolean.NULL)
                return yn.getValue();

            TFBoolean tf = TFBoolean.parse(strval);
            if (tf != TFBoolean.NULL)
                return tf.getValue();

            BITBoolean bit = BITBoolean.parse(strval);
            if (bit != BITBoolean.NULL)
                return bit.getValue();

            return null;
        }
    }

    public Byte parseByte(Object val) {
        if (val == null)
            return null;
        if (isNumberType(val)) {
            return (Byte) val;
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).byteValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).byteValue();
        } else {
            return Byte.parseByte(parseString(val));
        }
    }

    public Integer parseInteger(Object val) {
        if (val == null)
            return null;
        if (val instanceof Long) {
            return Integer.parseInt(parseString(val));
        } else if (isNumberType(val)) {
            return (Integer) val;
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).intValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).intValue();
        } else {
            return Integer.parseInt(parseString(val));
        }
    }

    public BigInteger parseBigInteger(Object val) {
        if (val == null)
            return null;
        if (val instanceof BigInteger) {
            return (BigInteger) val;
        } else if (isNumberType(val)) {
            return new BigInteger(val.toString());
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).toBigInteger();
        } else {
            return new BigInteger(parseString(val));
        }
    }

    public Float parseFloat(Object val) {
        if (val == null)
            return null;
        if (isNumberType(val)) {
            return (Float) val;
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).floatValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).floatValue();
        } else {
            return null;
        }
    }

    public Double parseDouble(Object val) {
        if (val == null)
            return null;
        if (isNumberType(val)) {
            return (Double) val;
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).doubleValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).doubleValue();
        } else {
            return null;
        }
    }

    public BigDecimal parseBigDecimal(Object val) {
        if (val == null)
            return null;
        if (val instanceof BigDecimal) {
            return (BigDecimal) val;
        } else if (isNumberType(val)) {
            return new BigDecimal(val.toString());
        } else if (val instanceof BigInteger) {
            return new BigDecimal(val.toString());
        } else {
            return null;
        }
    }

    public Date parseDate(Object val) {
        if (val == null)
            return null;
        if (val instanceof Date) {
            return (Date) val;
        } else if (val instanceof java.sql.Date) {
            return (Date) val;
        } else if (val instanceof java.sql.Timestamp) {
            return (Date) val;
        } else if (val instanceof String) {
            return DateUtil.parse(val + "");
        } else {
            return null;
        }
    }

    public Character parseChar(Object val) {
        if (val == null)
            return null;
        if (val instanceof String) {
            return ((String) val).charAt(0);
        } else {
            return val.toString().charAt(0);
        }
    }

    public boolean isNumberType(Object valueNotNull) {
        if (valueNotNull instanceof Integer) {
            return true;
        } else if (valueNotNull instanceof Long) {
            return true;
        } else if (valueNotNull instanceof Short) {
            return true;
        } else if (valueNotNull instanceof Byte) {
            return true;
        } else if (valueNotNull instanceof Float) {
            return true;
        } else return valueNotNull instanceof Double;
    }

    public Short parseShort(Object val) {
        if (val == null)
            return null;
        if (isNumberType(val)) {
            return (Short) val;
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).shortValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).shortValue();
        } else {
            return Short.parseShort(parseString(val));
        }
    }

    public Long parseLong(Object val) {
        if (val == null)
            return null;
        if (isNumberType(val)) {
            return (Long) val;
        } else if (val instanceof BigDecimal) {
            return ((BigDecimal) val).longValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).longValue();
        } else {
            return Long.parseLong(parseString(val));
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean isRawType(Class cls) {
        if (cls.equals(int.class)) {
            return true;
        } else if (cls.equals(double.class)) {
            return true;
        } else if (cls.equals(short.class)) {
            return true;
        } else if (cls.equals(boolean.class)) {
            return true;
        } else if (cls.equals(float.class)) {
            return true;
        } else return cls.equals(byte.class);
    }

    @SuppressWarnings("rawtypes")
    public Object parse(Class cls, String value) {
        if (cls.equals(int.class)) {
            return parseInteger(value);
        } else if (cls.equals(double.class)) {
            return parseDouble(value);
        } else if (cls.equals(short.class)) {
            return parseShort(value);
        } else if (cls.equals(boolean.class)) {
            return parseBoolean(value);
        } else if (cls.equals(float.class)) {
            return parseFloat(value);
        } else if (cls.equals(byte.class)) {
            return parseByte(value);
        } else if (cls.equals(String.class)) {
            return value;
        } else if (cls.equals(Short.class)) {
            return parseShort(value);
        } else if (cls.equals(Byte.class)) {
            return parseByte(value);
        } else if (cls.equals(Byte.class)) {
            return parseByte(value);
        } else if (cls.equals(Boolean.class)) {
            return parseBoolean(value);
        } else if (cls.equals(Integer.class)) {
            return parseInteger(value);
        } else if (cls.equals(Float.class)) {
            return parseFloat(value);
        } else if (cls.equals(Double.class)) {
            return parseDouble(value);
        } else if (cls.equals(BigDecimal.class)) {
            return parseBigDecimal(value);
        } else if (cls.equals(BigInteger.class)) {
            return parseBigInteger(value);
        } else if (cls.equals(Date.class)) {
            return parseDate(value);
        } else if (cls.equals(Character.class)) {
            return parseChar(value);
        } else {
            return null;
        }
    }

}
