package com.dt.core.dao.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

    public static long MICRO_SECOND = 1;
    public static long SECOND = 1000;
    public static long MINUTE = 60000;
    public static long HOUR = 3600000;
    public static long DAY = 86400000;
    public static long WEEK = 604800000;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static long getSystemMilles() {
        return System.currentTimeMillis();
    }

    public static String getFormatTime() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    public static String format(Date date, String format) {
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

    public static String format(Date date) {
        if (date == null) {
            return "";
        }
        return format(date, "yyyy-MM-dd");
    }

    public static String getCurrTime(String format) {
        Calendar cal = Calendar.getInstance();
        return format(cal.getTime(), format);
    }

    public static int getCurrentYear() {

        return Integer.parseInt(getCurrTime("yyyy"));

    }

    public static int getCurrentMonth() {

        return Integer.parseInt(getCurrTime("MM"));

    }

    public static int getYearPart(Date dt) {
        return Integer.parseInt(format(dt, "yyyy"));
    }

    public static int getMonthPart(Date dt) {
        return Integer.parseInt(format(dt, "MM"));
    }

    public static int getDayPart(Date dt) {
        return Integer.parseInt(format(dt, "dd"));
    }

    public static int getHourPart(Date dt) {
        return Integer.parseInt(format(dt, "HH"));
    }

    public static int getMinutePart(Date dt) {
        return Integer.parseInt(format(dt, "mm"));
    }

    public static int getSecondPart(Date dt) {
        return Integer.parseInt(format(dt, "ss"));
    }

    public static Date getSameDayByWeek(Date date, int w) {
        long myTime = date.getTime() + WEEK * w;
        date.setTime(myTime);
        return date;
    }

    public static Date getSameDateByYear(Date date, int y) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        year = year + y;
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    public static String getChineseWeek(Calendar date) {
        final String[] dayNames = {"??????", "??????", "??????", "??????", "?????????", "??????", "??????"};
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayNames[dayOfWeek - 1]);
        return dayNames[dayOfWeek - 1];

    }

    public static Date parse(String dateStr, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Date dt = null;
        try {
            dt = sdf.parse(dateStr);
        } catch (Exception e) {

        }
        return dt;
    }

    public static Date parse(String dateStr) {

        Date dt = parse(dateStr, "yyyy-MM-dd HH:mm:ss");
        if (dt == null) {
            dt = parse(dateStr, "yyyy/MM/dd HH:mm:ss");
        }

        if (dt == null) {
            dt = parse(dateStr, "yyyy-MM-dd");
        }

        if (dt == null) {
            dt = parse(dateStr, "yyyy/MM/dd");
        }

        return dt;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param date
     * @return
     */
    public static Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * ????????????????????????????????????
     *
     * @param date
     * @return
     */
    public static Date firstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * ???????????????
     *
     * @param year ??????
     * @return boolean
     */
    public static boolean isLeapYear(int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.isLeapYear(year);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param day    ???????????????
     * @param offset ?????????????????????????????????????????????????????????
     * @return Date
     */
    public static Date getDateByOffset(Date day, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        c.add(Calendar.DAY_OF_MONTH, offset);
        return c.getTime();
    }

    public static void main(String[] args) {
        // Date d = getSameDayByWeek(new Date(), -2);

        String t = getChineseWeek(Calendar.getInstance());
        System.out.println(t);
        // format(d,
        // "YYYY-MM-DD");

        // System.out.println(getTwoYear());

        System.out.println(getSameDateByYear(new Date(), -1));

        /*
         * Date d=new Date(); Date d2=DateUtil.addDay(d, -30);
         * System.out.println(DateUtil.format(d,"yyyy-M-d"));
         * System.out.println(DateUtil.format(d2,"yyyy-M-d"));
         */
    }

    public static String formatTime(Timestamp time) {
        if (time == null) {
            return "";
        }
        return time.toString().substring(0, 11);
    }

    /**
     * ??????????????2008-10-08 ????????????????????????????????OCT, 08. 2008
     *
     * @return
     */
    public static String formateDate(String date) {
        if (date.length() != 10) {
            return "";
        }

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String dayOFmonth = date.substring(8);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(dayOFmonth));
        return df.format(cal.getTime());

    }

    /**
     * ??????????????2008-10-08 ????????????????????????????????OCT, 08. 2008
     *
     * @return
     */
    public static String formateDate(Date date) {
        return formateDate(format(date));

    }

    /**
     * ??????????????????????????????????
     *
     * @return
     */
    public static String getTwoYear() {
        Calendar rightNow = Calendar.getInstance();
        return (rightNow.get(Calendar.YEAR) + "").substring(2, 4);

    }

    /**
     * ??????????????????????
     */
    public static Date getUtilDateNow() {
        return new Date();
    }

    /**
     * ??????????????????????
     */
    public static java.sql.Date getSqlDateNow() {
        return new java.sql.Date(getUtilDateNow().getTime());
    }

    /**
     * ??????????????????????
     */
    public static Timestamp getTimestampNow() {
        return new Timestamp(getUtilDateNow().getTime());
    }

    /**
     * ??????????????????????????????????????????????????????
     */

    public static Date addDay(String datestr, String datefmt, int day) {
        SimpleDateFormat df = new SimpleDateFormat(datefmt);
        Date olddate = null;
        try {
            df.setLenient(false);
            olddate = new Date(df.parse(datestr).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("??????");
        }

        return addDay(olddate, day);
    }

    /**
     * ??????????????????????????????????????????????????????
     */
    public static Date addDay(Date olddate, int day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);
        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;
        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new Date(cal.getTimeInMillis());
    }

    public static Date addSecond(Date olddate, int sec) {
        long m = olddate.getTime();
        m += sec * 1000;
        return new Date(m);
    }

    /**
     * ????????????????????????????????
     */
    public static boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DATE) == c2.get(Calendar.DATE);

    }

    public static boolean isBefore(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        return c1.before(c2);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param date1 ???????????????
     * @param date2 ???????????????<font color="red">??????????????????date1??????</font>
     * @param type  DateUtils.Type.X???????????????
     * @return long???
     * @throws Exception
     */
    public static long getDiff(Date date1, Date date2, Type type) throws Exception {

        if (!isBefore(date1, date2))
            throw new Exception("?????????????????????????????????????????????");

        // long d = Math.abs(date1.getTime() - date2.getTime());
        switch (type) {
            case Year: {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                cal1.setTime(date1);
                int year1 = cal1.get(Calendar.YEAR);
                int month1 = cal1.get(Calendar.MONTH);
                int day1 = cal1.get(Calendar.DAY_OF_MONTH);
                int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
                int minute1 = cal1.get(Calendar.MINUTE);
                int second1 = cal1.get(Calendar.SECOND);

                cal2.setTime(date2);
                int year2 = cal2.get(Calendar.YEAR);
                int month2 = cal2.get(Calendar.MONTH);
                int day2 = cal2.get(Calendar.DAY_OF_MONTH);
                int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
                int minute2 = cal2.get(Calendar.MINUTE);
                int second2 = cal2.get(Calendar.SECOND);

                int yd = year2 - year1;

                if (month1 > month2) {
                    yd -= 1;
                } else {
                    if (day1 > day2) {
                        yd -= 1;
                    } else {
                        if (hour1 > hour2) {
                            yd -= 1;
                        } else {
                            if (minute1 > minute2) {
                                yd -= 1;
                            } else {
                                if (second1 > second2) {
                                    yd -= 1;
                                }
                            }
                        }
                    }
                }
                return yd;
            }
            case Month: {
                // ???????????????
                long year = getDiff(date1, date2, Type.Year);

                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                cal1.setTime(date1);
                int month1 = cal1.get(Calendar.MONTH);
                int day1 = cal1.get(Calendar.DAY_OF_MONTH);
                int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
                int minute1 = cal1.get(Calendar.MINUTE);
                int second1 = cal1.get(Calendar.SECOND);

                cal2.setTime(date2);
                int month2 = cal2.get(Calendar.MONTH);
                int day2 = cal2.get(Calendar.DAY_OF_MONTH);
                int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
                int minute2 = cal2.get(Calendar.MINUTE);
                int second2 = cal2.get(Calendar.SECOND);

                int md = (month2 + 12) - month1;

                if (day1 > day2) {
                    md -= 1;
                } else {
                    if (hour1 > hour2) {
                        md -= 1;
                    } else {
                        if (minute1 > minute2) {
                            md -= 1;
                        } else {
                            if (second1 > second2) {
                                md -= 1;
                            }
                        }
                    }
                }
                return (long) md + year * 12;
            }
            case Week: {
                return getDiff(date1, date2, Type.Day) / 7;
            }
            case Day: {
                long d1 = date1.getTime();
                long d2 = date2.getTime();
                return (int) ((d2 - d1) / (24 * 60 * 60 * 1000));
            }
            case Hour: {
                long d1 = date1.getTime();
                long d2 = date2.getTime();
                return (int) ((d2 - d1) / (60 * 60 * 1000));
            }
            case Minutes: {
                long d1 = date1.getTime();
                long d2 = date2.getTime();
                return (int) ((d2 - d1) / (60 * 1000));
            }
            case Seconds: {
                long d1 = date1.getTime();
                long d2 = date2.getTime();
                return (int) ((d2 - d1) / 1000);
            }
            default:
                throw new Exception("?????????????????????????????????????????????????????????????????????????????????");
        }
    }

    public enum Type {
        Year, Month, Week, Day, Hour, Minutes, Seconds
    }

}
