package com.dt.core.tool.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

public class PingYinUtil {

    private final static int[] li_SecPosValue = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212,
            3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};

    // {id:"",name:"v"}

    private final static String[] lc_FirstLetter = {"a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "w", "x", "y", "z"};

    public static String getAllFirstLetter(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }

        String _str = "";
        for (int i = 0; i < str.length(); i++) {
            _str = _str + getFirstLetter(str.substring(i, i + 1));
        }

        return _str;
    }

    public static String getFirstLetter(String chinese) {
        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }
        chinese = conversionStr(chinese, "GB2312", "ISO8859-1");

        if (chinese.length() > 1) // 判断是不是汉字
        {
            int li_SectorCode = chinese.charAt(0); // 汉字区码
            int li_PositionCode = chinese.charAt(1); // 汉字位码
            li_SectorCode = li_SectorCode - 160;
            li_PositionCode = li_PositionCode - 160;
            int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
            if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                for (int i = 0; i < 23; i++) {
                    if (li_SecPosCode >= li_SecPosValue[i] && li_SecPosCode < li_SecPosValue[i + 1]) {
                        chinese = lc_FirstLetter[i];
                        break;
                    }
                }
            } else // 非汉字字符,如图形符号或ASCII码
            {
                chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }

        return chinese;
    }

    public static String conversionStr(String str, String charsetName, String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {
        }
        return str;
    }

    public static JSONObject formatFirstChar2(JSONArray data, String col) {
        if (col == null || col.trim().length() == 0) {
            col = "name";
        }

        JSONObject res = new JSONObject(false);
        for (int i = 0; i < data.size(); i++) {
            JSONObject e = data.getJSONObject(i);
            String v = getPYIndexStr(e.getString(col) == null ? "1" : e.getString(col).trim(), true);
            String vf = "*";

            // v肯定存在
            if (v.length() > 0) {
                vf = v.substring(0, 1).toUpperCase();
                if (!(vf.charAt(0) >= 'A' && vf.charAt(0) <= 'Z')) {
                    vf = "*";
                }
            }
            if (res.containsKey(vf)) {
                res.getJSONArray(vf).add(e);
            } else {
                JSONArray d = new JSONArray();
                d.add(e);
                res.put(vf, d);
            }
        }

        return res;

    }

    // 速度较快
    public static JSONObject formatFirstChar(JSONArray data, String col) {
        if (col == null || col.trim().length() == 0) {
            col = "name";
        }
        HashMap<String, JSONArray> c = new HashMap<String, JSONArray>();
        c.put("A", new JSONArray());
        c.put("B", new JSONArray());
        c.put("C", new JSONArray());
        c.put("D", new JSONArray());
        c.put("E", new JSONArray());
        c.put("F", new JSONArray());
        c.put("G", new JSONArray());
        c.put("H", new JSONArray());
        c.put("I", new JSONArray());
        c.put("J", new JSONArray());
        c.put("K", new JSONArray());
        c.put("L", new JSONArray());
        c.put("M", new JSONArray());
        c.put("N", new JSONArray());
        c.put("O", new JSONArray());
        c.put("P", new JSONArray());
        c.put("Q", new JSONArray());
        c.put("R", new JSONArray());
        c.put("S", new JSONArray());
        c.put("T", new JSONArray());
        c.put("U", new JSONArray());
        c.put("V", new JSONArray());
        c.put("W", new JSONArray());
        c.put("X", new JSONArray());
        c.put("Y", new JSONArray());
        c.put("Z", new JSONArray());

        JSONArray xx = new JSONArray();
        for (int i = 0; i < data.size(); i++) {

            JSONObject e = data.getJSONObject(i);
            String v = getPYIndexStr(e.getString(col) == null ? "" : e.getString(col).trim(), true);
            // v不存在或者首字母不再map中

            if (v.length() == 0 || !c.containsKey(v.substring(0, 1).toUpperCase())) {
                xx.add(e);
            } else {
                c.get(v.substring(0, 1).toUpperCase()).add(e);
            }

        }

        Iterator<Entry<String, JSONArray>> iterator2 = c.entrySet().iterator();
        JSONObject res = new JSONObject(true);
        if (xx.size() > 0) {
            res.put("*", xx);
        }
        while (iterator2.hasNext()) {
            Entry<String, JSONArray> entry = iterator2.next();
            if (entry.getValue().size() > 0) {

                res.put(entry.getKey(), entry.getValue());
            }
        }

        return res;
    }

    public static void main(String[] args) {


    }

    /**
     * 返回首字母
     */
    public static String getPYIndexStr(String strChinese, boolean bUpCase) {
        try {
            StringBuffer buffer = new StringBuffer();
            byte[] b = strChinese.getBytes("GBK");// 把中文转化成byte数组
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 255) > 128) {
                    int char1 = b[i++] & 255;
                    char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
                    int chart = char1 + (b[i] & 255);
                    buffer.append(getPYIndexChar((char) chart, bUpCase));
                    continue;
                }
                char c = (char) b[i];
                if (!Character.isJavaIdentifierPart(c))// 确定指定字符是否可以是 Java
                    // 标识符中首字符以外的部分。
                    c = 'A';
                buffer.append(c);
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519")
                    .append(e.getMessage()).toString());
        }
        return null;
    }

    /**
     * 得到首字母
     */
    private static char getPYIndexChar(char strChinese, boolean bUpCase) {

        int charGBK = strChinese;

        char result;

        if (charGBK >= 45217 && charGBK <= 45252)

            result = 'A';

        else if (charGBK >= 45253 && charGBK <= 45760)

            result = 'B';

        else if (charGBK >= 45761 && charGBK <= 46317)

            result = 'C';

        else if (charGBK >= 46318 && charGBK <= 46825)

            result = 'D';

        else if (charGBK >= 46826 && charGBK <= 47009)

            result = 'E';

        else if (charGBK >= 47010 && charGBK <= 47296)

            result = 'F';

        else if (charGBK >= 47297 && charGBK <= 47613)

            result = 'G';

        else if (charGBK >= 47614 && charGBK <= 48118)

            result = 'H';

        else if (charGBK >= 48119 && charGBK <= 49061)

            result = 'J';

        else if (charGBK >= 49062 && charGBK <= 49323)

            result = 'K';

        else if (charGBK >= 49324 && charGBK <= 49895)

            result = 'L';

        else if (charGBK >= 49896 && charGBK <= 50370)

            result = 'M';

        else if (charGBK >= 50371 && charGBK <= 50613)

            result = 'N';

        else if (charGBK >= 50614 && charGBK <= 50621)

            result = 'O';

        else if (charGBK >= 50622 && charGBK <= 50905)

            result = 'P';

        else if (charGBK >= 50906 && charGBK <= 51386)

            result = 'Q';

        else if (charGBK >= 51387 && charGBK <= 51445)

            result = 'R';

        else if (charGBK >= 51446 && charGBK <= 52217)

            result = 'S';

        else if (charGBK >= 52218 && charGBK <= 52697)

            result = 'T';

        else if (charGBK >= 52698 && charGBK <= 52979)

            result = 'W';

        else if (charGBK >= 52980 && charGBK <= 53688)

            result = 'X';

        else if (charGBK >= 53689 && charGBK <= 54480)

            result = 'Y';

        else if (charGBK >= 54481 && charGBK <= 55289)

            result = 'Z';

        else

            result = (char) (65 + (new Random()).nextInt(25));

        if (!bUpCase)

            result = Character.toLowerCase(result);

        return result;

    }

}