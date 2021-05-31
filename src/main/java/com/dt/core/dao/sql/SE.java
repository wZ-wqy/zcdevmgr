package com.dt.core.dao.sql;

import com.dt.core.dao.Rcd;
import com.dt.core.dao.SpringDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SimpleExpression 的缩写 简单表达式，SQL表达式
 */
public class SE extends SubSQL implements ExecutableSQL, QueryableSQL {

    private static final long serialVersionUID = 4922774964031198960L;
    private static final String PARAM_NAME_SUFFIX_CHARS = "(+-*/ ><=,)";
    private static HashMap<String, AnalyseRsult> CACHE = new HashMap<String, AnalyseRsult>();
    boolean inited = false;
    private int paramIndex = -1;
    private ArrayList<Object> paramValues = new ArrayList<Object>();
    private ArrayList<Object> paramValueIndexes = new ArrayList<Object>();
    private String originalSQL = null;
    private ArrayList<String> splitParts = new ArrayList<String>();
    private String lastSqlPart = "";
    private Map<String, Object> originalMap = null;
    private Object[] originalPs = null;
    private SpringDAO dao = null;

    public SE(String sql, Object... ps) {
        this.originalSQL = sql;
        this.originalMap = new HashMap<String, Object>();
        this.originalPs = ps;
    }

    public SE(String sql, Map<String, Object> map, Object... ps) {
        this.originalSQL = sql;
        this.originalMap = map;
        this.originalPs = ps;
    }

    private synchronized static void putAR(String sql, AnalyseRsult r) {
        CACHE.put(sql, r);
    }

    private static AnalyseRsult getAR(String sql) {
        return CACHE.get(sql);
    }

    public static SE get(String sql, Object... ps) {
        return new SE(sql, ps);
    }

    public static SE get(String sql, Map<String, Object> map, Object... ps) {
        return new SE(sql, map, ps);
    }

    public static int jumpIf(String sql, int i) {
        return jumpIf(sql, i, false);
    }

    public static int jumpIf(String sql, int i, boolean includeBracket) {
        String s1 = null;
        if (i < sql.length() - 1) {
            s1 = sql.substring(i, i + 1);
        }
        String s2 = null;
        if (i < sql.length() - 2) {
            s2 = sql.substring(i, i + 2);
        }

        if (s1 != null) {

            if (s1.equals(SQLKeyword.SINGLE_QUATE.toString())) {
                return jumpSingleQuateIf(sql, i);
            } else if (s1.equals(SQLKeyword.LEFT_DOUBLE_QUATE.toString())) {
                return jumpDoubleQuateIf(sql, i);
            }

            if (includeBracket) {
                if (s1.equals(SQLKeyword.LEFT_BRACKET.toString())) {
                    return jumpBracketIf(sql, i);
                }
            }

        }

        if (s2 != null) {
            if (s2.equals(SQLKeyword.SINGLE_REMARK.toString())) {
                return jumpSingleLineRemarkIf(sql, i);
            } else if (s2.equals(SQLKeyword.LEFT_REMARK.toString())) {
                return jumpMulityLineRemarkIf(sql, i);
            }
        }
        return i;
    }

    private static int jumpBracketIf(String sql, int i) {
        boolean matched = false;
        int brackets = 1;
        while (true) {
            i++;
            if (i >= sql.length() - 1)
                break;
            String c1 = sql.substring(i, i + 1);
            if (c1.equals(SQLKeyword.LEFT_BRACKET.toString())) {
                brackets++;
            }
            if (c1.equals(SQLKeyword.RIGHT_BRACKET.toString())) {
                brackets--;
                if (brackets == 0) {
                    matched = true;
                    break;
                }
            }
        }
        return matched ? i : -1;
    }

    private static int jumpSingleQuateIf(String sql, int i) {
        boolean matched = false;
        while (true) {
            i++;
            if (i >= sql.length())
                break;
            String c1 = sql.substring(i, i + 1);
            String c2 = null;
            if (i + 1 <= sql.length() - 1) {
                c2 = sql.substring(i + 1, i + 2);
            }
            if (c1.equals(SQLKeyword.SINGLE_QUATE.toString())) {
                if (c2 != null) {
                    if (c2.equals(SQLKeyword.SINGLE_QUATE.toString())) {
                        i++;
                    } else {
                        matched = true;
                        break;
                    }
                } else {
                    matched = true;
                    break;
                }
            }
        }
        return matched ? i : -1;
    }

    private static int jumpDoubleQuateIf(String sql, int i) {
        boolean matched = false;
        while (true) {
            i++;
            if (i >= sql.length())
                break;
            String c1 = sql.substring(i, i + 1);
            String c2 = null;
            if (i + 1 <= sql.length() - 1) {
                c2 = sql.substring(i + 1, i + 2);
            }
            if (c1.equals(SQLKeyword.DOUBLE_QUATE.toString())) {
                if (c2 != null) {
                    if (c2.equals(SQLKeyword.DOUBLE_QUATE.toString())) {
                        i++;
                    } else {
                        matched = true;
                        break;
                    }
                } else {
                    matched = true;
                    break;
                }
            }
        }
        return matched ? i : -1;
    }

    private static int jumpMulityLineRemarkIf(String sql, int i) {
        boolean matched = false;
        while (true) {
            i++;
            if (i >= sql.length() - 1)
                break;
            String c1 = sql.substring(i, i + 2);
            if (c1.equals(SQLKeyword.RIGHT_REMARK.toString())) {
                matched = true;
                break;
            }
        }
        return matched ? i : -1;
    }

    private static int jumpSingleLineRemarkIf(String sql, int i) {
        boolean matched = false;
        while (true) {
            i++;
            if (i >= sql.length())
                break;
            String c1 = sql.substring(i, i + 1);
            if (c1.equals(SQLKeyword.LN.toString())) {
                matched = true;
                break;
            }
        }

        if (i == sql.length())
            matched = true;

        return matched ? i : -1;
    }

    public static void main(String[] args) {
        HashMap<String, Object> ps = new HashMap<String, Object>();
        ps.put("e", 5);
        SE a = new SE("a=:e", ps);


        // SE b=new SE("b=:x",new Object[]{":x",3});


    }

    public static int indexOf(String sql, String kw, boolean includeBracket) {
        return indexOf(sql, kw, includeBracket, 0);
    }

    public static int indexOf(String sql, String kw, boolean includeBracket, int formIndex) {
        sql = " " + sql + " ";
        char[] chars = sql.toCharArray();
        int i = formIndex - 1;
        while (true) {

            i++;
            if (i >= chars.length)
                break;

            @SuppressWarnings("unused")
            char c = chars[i];
            int z = jumpIf(sql, i, includeBracket);
            if (z == -1) {
                // err("语句" + sql + "，在第" + i + "个字符处,没有找到与之对应的结尾字符,可能存在语法错误!");
                return -1;
            } else {
                if (z != i) {
                    i = z;
                    continue;
                }
            }

            if (i + kw.length() < sql.length()) {
                String str = sql.substring(i, i + kw.length());
                if (str.equals(kw)) {
                    return i - 1;
                }
            }
        }
        return -1;
    }

    private void initIf() {
        if (inited)
            return;
        inited = true;

        boolean userSet = false;

        AnalyseRsult ar = getAR(originalSQL);

        if (ar == null) {
            // 分析语句
            analyse(this.originalSQL, originalMap, originalPs);
            splitParts.add(this.lastSqlPart);
            ar = new AnalyseRsult(splitParts, paramValueIndexes);
            putAR(originalSQL, ar);
            userSet = true;
            // System.err.println("NC");
        } else {
            splitParts = ar.getSplitParts();
            paramValueIndexes = ar.getPsIndexes();
            userSet = false;
            // System.err.println("UC");
        }

        // 重新参数值
        for (int i = 0; i < this.paramValueIndexes.size(); i++) {
            Object index = this.paramValueIndexes.get(i);
            if (index instanceof String) {
                String indexStr = (String) index;
                if (userSet) {
                    this.paramValues.set(i, originalMap.get(indexStr));
                } else {
                    this.paramValues.add(originalMap.get(indexStr));
                }
            } else {
                Integer indexInt = (Integer) index;
                if (userSet) {
                    this.paramValues.set(i, originalPs[indexInt]);
                } else {
                    this.paramValues.add(originalPs[indexInt]);
                }
            }
        }
        //

        for (Object val : paramValues) {
            if (val instanceof SQL) {
                SQL se = (SQL) val;
                se.setParent(this);
            }
        }
    }

    private void err(String msg) {
        (new Exception(msg)).printStackTrace();
    }

    private void analyse(String sql, Map<String, Object> map, Object... ps) {
        sql = " " + sql + " ";
        char[] chars = sql.toCharArray();
        String part1 = "";
        String part2 = sql;
        int i = -1;
        int matchCount = 0;
        while (true) {

            i++;
            if (i >= chars.length)
                break;

            char c = chars[i];
            int z = jumpIf(sql, i);
            if (z == -1) {
                // err("语句" + sql + "，在第" + i + "个字符处,没有找到与之对应的结尾字符,可能存在语法错误!");
                return;
            } else {
                if (z != i) {
                    i = z;
                    continue;
                }
            }

            if (c == '?') {
                matchCount++;
                part1 = part2.substring(0, i).trim();
                this.splitParts.add(part1);
                part2 = part2.substring(i + 1).trim();
                lastSqlPart = part2;
                paramIndex++;
                if (paramIndex >= ps.length) {
                    err(part1 + "? 处参数个数不足");
                    return;
                }

                this.paramValues.add(ps[paramIndex]);
                this.paramValueIndexes.add(paramIndex);

                if (part2.length() > 0) {
                    analyse(part2, map, ps);
                    return;
                }
            } else if (c == ':' && !ignorColon) {
                matchCount++;
                part1 = part2.substring(0, i).trim();
                this.splitParts.add(part1);
                int end = chars.length;
                for (int j = i + 1; j < chars.length; j++) {
                    char ic = chars[j];
                    if (PARAM_NAME_SUFFIX_CHARS.indexOf(ic) > -1) {
                        end = j;
                        break;
                    }
                }
                String pname = part2.substring(i + 1, end).trim();

                if (!map.containsKey(pname)) {
                    err(part2 + ":" + pname + " 处没有指定参数值");
                    return;
                }
                this.paramValues.add(map.get(pname));
                this.paramValueIndexes.add(pname);
                // part2 = part2.substring(end + 1, part2.length()).trim();
                part2 = part2.substring(end).trim();
                lastSqlPart = part2;
                if (part2.length() > 0) {
                    analyse(part2, map, ps);
                    return;
                }
            }

            if (matchCount == 0) {
                this.lastSqlPart = part2.trim();
            }
        }
    }

    public String getOriginalSQL() {
        return originalSQL;
    }

    public String getSQL() {
        initIf();
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < splitParts.size() - 1; i++) {
            String part = splitParts.get(i);
            Object param = this.paramValues.get(i);
            if (param instanceof SQL) {
                SQL se = (SQL) param;
                se.setIgnorColon(ignorColon);
                sql.append(" " + part + se.getSQL() + " ");
            } else {
                String val = Utils.castValue(param);
                sql.append(" " + part + val + " ");
            }
        }
        sql.append(this.splitParts.get(splitParts.size() - 1));
        return sql.toString().trim();
    }

    public Object[] getParams() {
        initIf();
        if (isEmpty())
            return new Object[]{};
        else {
            ArrayList<Object> list = new ArrayList<Object>();
            for (Object o : this.paramValues) {
                if (o == null && replaceNull)
                    continue;

                if (o instanceof SQL) {
                    SQL se = (SQL) o;
                    se.setIgnorColon(ignorColon);
                    list.addAll(Utils.toArrayList(se.getParams()));
                } else {
                    list.add(o);
                }
            }
            return list.toArray();
        }
    }

    public String getParamedSQL() {
        initIf();
        if (this.isEmpty())
            return "";
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < splitParts.size() - 1; i++) {
            String part = splitParts.get(i);
            Object param = this.paramValues.get(i);
            if (param != null) {
                if (param instanceof SQL) {
                    SQL se = (SQL) param;
                    se.setIgnorColon(ignorColon);
                    sql.append(" " + part + se.getParamedSQL() + " ");
                } else {
                    sql.append(part + " ? ");
                }
            } else {
                if (replaceNull) {
                    sql.append(part + " null ");
                } else {
                    sql.append(part + " ? ");
                }
            }
        }
        sql.append(this.splitParts.get(splitParts.size() - 1));
        return sql.toString().trim();
    }

    public String getParamNamedSQL() {
        initIf();
        if (this.isEmpty())
            return "";
        this.beginParamNameSQL();
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < splitParts.size() - 1; i++) {
            String part = splitParts.get(i);
            Object param = this.paramValues.get(i);
            if (param != null && replaceNull) {
                if (param instanceof SE) {
                    SE se = (SE) param;
                    se.setIgnorColon(ignorColon);
                    sql.append(" " + part + se.getParamNamedSQL() + " ");
                } else {
                    sql.append(part + " " + this.getNextParamName(true) + " ");
                }
            } else {
                if (replaceNull) {
                    sql.append(part + " null ");
                } else {
                    sql.append(part + " " + this.getNextParamName(true) + " ");
                }
            }
        }
        sql.append(this.splitParts.get(splitParts.size() - 1));
        this.endParamNameSQL();
        return sql.toString().trim();
    }

    public HashMap<String, Object> getNamedParams() {
        initIf();
        HashMap<String, Object> ps = new HashMap<String, Object>();
        if (isEmpty())
            return ps;
        this.beginParamNameSQL();
        for (Object o : this.paramValues) {
            if (o == null && replaceNull)
                continue;
            if (o instanceof SQL) {
                ((SQL) o).setIgnorColon(ignorColon);
                HashMap<String, Object> map = ((SQL) o).getNamedParams();
                ps.putAll(map);
            } else {
                ps.put(this.getNextParamName(false), o);
            }
        }
        this.endParamNameSQL();
        return ps;
    }

    public boolean isEmpty() {
        return this.originalSQL == null || this.originalSQL.length() == 0;
    }

    public boolean isAllParamsEmpty(boolean isCE) {
        initIf();
        if (this.isEmpty())
            return false;

        for (int i = 0; i < this.paramValues.size(); i++) {
            Object o = this.paramValues.get(i);
            if (o instanceof SQL) {
                SQL se = (SQL) o;
                se.setIgnorColon(ignorColon);
                if (!se.isAllParamsEmpty(isCE))
                    return false;
            } else {
                if (o != null) {
                    if (isCE && (o instanceof String)) {
                        String str = (String) o;
                        String sql = splitParts.get(i).trim().toUpperCase();
                        if (sql.indexOf("LIKE") > -1) // 此处可以做更为复杂的判断
                        {
                            str = str.replace("%", "");
                            str = str.replace("_", "");
                        }
                        if (!str.equals("") && !str.equals("null"))
                            return false;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public SE append(String se, Object... ps) {
        return append(SE.get(se, ps));
    }

    public SE append(SQL... ses) {
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> ps = new ArrayList<Object>();

        sql.append(this.getParamedSQL());
        ps.addAll(Utils.toArrayList(this.getParams()));

        for (SQL se : ses) {
            String s = se.getParamedSQL();
            sql.append((s.startsWith(SQLKeyword.SPACER.toString()) ? "" : SQLKeyword.SPACER.toString()) + s);
            ps.addAll(Utils.toArrayList(se.getParams()));
        }
        return SE.get(sql.toString(), ps.toArray(new Object[ps.size()]));
    }

    public SE appendIf(String se, Object... ps) {
        return appendIf(SE.get(se, ps));
    }

    public SE appendIf(SQL... ses) {
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> ps = new ArrayList<Object>();

        sql.append(this.getParamedSQL());
        ps.addAll(Utils.toArrayList(this.getParams()));

        for (SQL se : ses) {
            if (se.isAllParamsEmpty())
                continue;
            String s = se.getParamedSQL();
            sql.append(s.startsWith(SQLKeyword.SPACER.toString()) ? ""
                    : SQLKeyword.SPACER.toString() + se.getParamedSQL());
            ps.addAll(Utils.toArrayList(se.getParams()));
        }
        return SE.get(sql.toString(), ps.toArray(new Object[ps.size()]));
    }

    public SpringDAO getDao() {
        return dao;
    }

    public SE setDao(SpringDAO dao) {
        this.dao = dao;
        return this;
    }

    public Rcd record() {
        return dao.uniqueRecord(this);
    }

    public Integer intValue() {
        return dao.uniqueInteger(this);
    }

    public String stringValue() {
        return dao.uniqueString(this);
    }

    public Long longValue() {
        return dao.uniqueLong(this);
    }

    public Date dateValue() {
        return dao.uniqueDate(this);
    }

    public BigDecimal decimalValue() {
        return dao.uniqueDecimal(this);
    }

    public Integer execute() {
        return dao.execute(this);
    }

}

class AnalyseRsult {
    private ArrayList<String> splitParts = null;
    private ArrayList<Object> psIndexes = null;

    public AnalyseRsult(ArrayList<String> parts, ArrayList<Object> indexes) {
        this.splitParts = parts;
        this.psIndexes = indexes;
    }

    public ArrayList<String> getSplitParts() {
        return splitParts;
    }

    public ArrayList<Object> getPsIndexes() {
        return psIndexes;
    }
}
