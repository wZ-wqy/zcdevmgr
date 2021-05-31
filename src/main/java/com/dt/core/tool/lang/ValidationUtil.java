package com.dt.core.tool.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lank
 * @date: 2020年1月26日 上午9:16:25
 * @Description:
 */
public class ValidationUtil {

    /**
     * 正则表达式：验证用户名
     */
    private static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式：验证密码
     */
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 正则表达式：验证汉字
     */
    private static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    private static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 校验用户名
     *
     * @param username username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验汉字
     *
     * @param chinese chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    // public static boolean isUrl(String url) {
    // return UrlValidator.getInstance().isValid(url);
    // }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
     * 17+除9的任意数 147
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isMobileValid(String str) {
        if (str == null) {
            return false;
        }
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhoneValid(String str) {
        if (str == null) {
            return false;
        }
        Matcher m;
        boolean b;
        Pattern p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }


    // public static boolean isInetAddressValid(String data) {
    // if (data == null) {
    // return false;
    // }
    // return InetAddressValidator.getInstance().isValid(data);
    // }

    // public static boolean isDateValid(String value, DatePattern pattern) {
    // return DateValidator.getInstance().validate(value, pattern.toString()) !=
    // null;
    // }

    // public static boolean isDomainValid(String value) {
    // return DomainValidator.getInstance().isValid(value);
    // }

    public static void main(String[] args) {

        // String token = "186281499523";
        // logger.info("checkToken:【{}】for 【{}】,result is:【{}】", token,
        // "isMobileValid",
        // isMobileValid(token));
        //
        // token = "028-545435";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "isPhoneValid",
        // isPhoneValid(token));
        //
        // token = "nevermissing@vip.qq.com";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "isEmailValid",
        // isEmailValid(token));
        //
        // token = "dasd";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "IsIntegerValid", IsIntegerValid(token));
        //
        // token = "120.123.123.12";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "isInetAddressValid", isInetAddressValid(token));
        //
        // token = "http://www.baidu.com/";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "isUrl",
        // isUrl(token));
        //
        // token = "2016-01-01 12:12:12";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "isDateValid",
        // isDateValid(token, DatePattern.year_to_second));
        //
        // token = "www.apache.org";
        // logger.info("checkToken:【{}】 for 【{}】,result is:【{}】", token,
        // "isDomainValid", isDomainValid(token));

    }
}
