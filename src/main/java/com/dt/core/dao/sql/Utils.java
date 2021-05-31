package com.dt.core.dao.sql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

    public static boolean isNumeric(Object arg) {
        boolean b = arg instanceof Byte;
        b = b || (arg instanceof Short);
        b = b || (arg instanceof Integer);
        b = b || (arg instanceof Long);
        b = b || (arg instanceof Float);
        b = b || (arg instanceof Double);
        b = b || (arg instanceof BigDecimal);
        b = b || (arg instanceof BigInteger);
        return b;
    }

    public static boolean isDate(Object arg) {
        boolean b = arg instanceof java.util.Date;
        b = b || (arg instanceof java.sql.Date);
        b = b || (arg instanceof java.sql.Timestamp);
        return b;
    }

    public static boolean isBoolean(Object arg) {
        boolean b = arg instanceof Boolean;
        return b;
    }

    public static Object cast(String type, String val) {
        if (type == null)
            return val;

        if (Boolean.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("booleab") || type.equals("bool")) {
            try {
                return Boolean.parseBoolean(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (Short.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("short")) {
            try {
                return Short.parseShort(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (Integer.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("int")
                || type.equalsIgnoreCase("integer")) {
            try {
                return Integer.parseInt(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (Long.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("long")) {
            try {
                return Long.parseLong(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (BigInteger.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("biginteger")) {
            try {
                return new BigInteger(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (Float.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("float")) {
            try {
                return Float.parseFloat(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (Double.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("double")) {
            try {
                return Double.parseDouble(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (BigDecimal.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("BigDecimal")
                || type.equalsIgnoreCase("decimal")) {
            try {
                return new BigDecimal(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (java.util.Date.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("Date")) {
            try {
                return parseDateTime(val);
            } catch (Exception ex) {
                return val;
            }
        } else if (java.sql.Date.class.getName().equalsIgnoreCase(type)) {
            try {
                java.util.Date dt = parseDateTime(val);
                if (dt == null)
                    return val;
                else {
                    return new java.sql.Date(dt.getTime());
                }
            } catch (Exception ex) {
                return val;
            }
        } else if (java.sql.Timestamp.class.getName().equalsIgnoreCase(type) || type.equalsIgnoreCase("Timestamp")) {
            try {
                java.util.Date dt = parseDateTime(val);
                if (dt == null)
                    return val;
                else {
                    return new java.sql.Timestamp(dt.getTime());
                }
            } catch (Exception ex) {
                return val;
            }
        } else {
            return val;
        }

    }

    public static Date parseDateTime(String dateStr, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Date dt = null;
        try {
            dt = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static Date parseDateTime(String dateStr) {
        Date dt = parseDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
        if (dt == null) {
            dt = parseDateTime(dateStr, "yyyy/MM/dd HH:mm:ss");
        }

        if (dt == null) {
            dt = parseDateTime(dateStr, "yyyy-MM-dd");
        }

        if (dt == null) {
            dt = parseDateTime(dateStr, "yyyy/MM/dd");
        }
        return dt;
    }

    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mDateTime;
        try {
            formatter.applyPattern(format);
            mDateTime = formatter.format(date);
        } catch (Exception e) {
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            mDateTime = formatter.format(date);
        }
        return mDateTime;
    }

    private static String castSQLInjection(String sql) {
        return sql.replaceAll("'", "''");
    }

    private static String castDateValue(Date v) {
        String d = formatDateTime(v, "yyyy-MM-dd HH:mm:ss");
        return " to_date('" + d + "','yyyy-mm-dd hh24:mi:ss') ";
    }

    public static String castValue(Object val) {
        if (val == null) {
            return " null ";
        }
        if (isNumeric(val)) {
            return " " + val.toString() + " ";
        } else if (isDate(val)) {
            return " " + castDateValue((Date) val) + " ";
        } else {
            return " '" + castSQLInjection(val.toString()) + "' ";
        }
    }

    public static ArrayList<Object> toArrayList(Object[] arr) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object o : arr)
            list.add(o);
        return list;
    }

}
