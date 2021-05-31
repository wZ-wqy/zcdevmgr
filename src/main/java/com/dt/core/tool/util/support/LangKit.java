package com.dt.core.tool.util.support;

import com.dt.core.tool.util.ToolUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author: lank
 * @date: 2020年1月25日 下午1:08:22
 * @Description:
 */
public class LangKit {

    private static final Pattern IPV4_PATTERN = Pattern
            .compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern
            .compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
    public static int HASH_BUFF_SIZE = 16 * 1024;

    /**
     * 用运行时异常包裹抛出对象，如果抛出对象本身就是运行时异常，则直接返回。
     * <p>
     * 如果是 InvocationTargetException，那么将其剥离，只包裹其 TargetException
     *
     * @param e 抛出对象
     * @return 运行时异常
     */
    public static RuntimeException wrapThrow(Throwable e) {
        if (e instanceof RuntimeException)
            return (RuntimeException) e;
        if (e instanceof InvocationTargetException)
            return wrapThrow(((InvocationTargetException) e).getTargetException());
        return new RuntimeException(e);
    }

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    /**
     * 生成一个未实现的运行时异常
     *
     * @return 一个未实现的运行时异常
     */
    public static RuntimeException noImplement() {
        return new RuntimeException("Not implement yet!");
    }

    /**
     * 根据格式化字符串，生成运行时异常
     *
     * @param format 格式
     * @param args   参数
     * @return 运行时异常
     */
    public static RuntimeException makeThrow(String format, Object... args) {
        return new RuntimeException(String.format(format, args));
    }

    /**
     * 将抛出对象包裹成运行时异常，并增加自己的描述
     *
     * @param e    抛出对象
     * @param fmt  格式
     * @param args 参数
     * @return 运行时异常
     */
    public static RuntimeException wrapThrow(Throwable e, String fmt, Object... args) {
        return new RuntimeException(String.format(fmt, args), e);
    }

    public static String readAll(Reader reader) {
        if (!(reader instanceof BufferedReader))
            reader = new BufferedReader(reader);
        try {
            StringBuilder sb = new StringBuilder();

            char[] data = new char[64];
            int len;
            while (true) {
                if ((len = reader.read(data)) == -1)
                    break;
                sb.append(data, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            throw LangKit.wrapThrow(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将一个数组转换成字符串
     * <p>
     * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
     *
     * @param fmt  格式
     * @param objs 数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concatBy(String fmt, T[] objs) {
        StringBuilder sb = new StringBuilder();
        for (T obj : objs)
            sb.append(String.format(fmt, obj));
        return sb;
    }

    /**
     * 将一个数组转换成字符串
     * <p>
     * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param ptn  格式
     * @param c    分隔符
     * @param objs 数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concatBy(String ptn, Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        for (T obj : objs)
            sb.append(String.format(ptn, obj)).append(c);
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb;
    }

    /**
     * 将一个数组转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c    分隔符
     * @param objs 数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs || 0 == objs.length)
            return sb;

        sb.append(objs[0]);
        for (int i = 1; i < objs.length; i++)
            sb.append(c).append(objs[i]);

        return sb;
    }

    /**
     * 清除数组中的特定值
     *
     * @param objs 数组
     * @param val  值，可以是 null，如果是对象，则会用 equals 来比较
     * @return 新的数组实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] without(T[] objs, T val) {
        if (null == objs || objs.length == 0) {
            return objs;
        }
        List<T> list = new ArrayList<T>(objs.length);
        Class<?> eleType = null;
        for (T obj : objs) {
            if (obj == val || (null != obj && null != val && obj.equals(val)))
                continue;
            if (null == eleType && obj != null)
                eleType = obj.getClass();
            list.add(obj);
        }
        if (list.isEmpty()) {
            return (T[]) new Object[0];
        }
        return list.toArray((T[]) Array.newInstance(eleType, list.size()));
    }

    /**
     * 将一个长整型数组转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c    分隔符
     * @param vals 数组
     * @return 拼合后的字符串
     */
    public static StringBuilder concat(Object c, long[] vals) {
        StringBuilder sb = new StringBuilder();
        if (null == vals || 0 == vals.length)
            return sb;

        sb.append(vals[0]);
        for (int i = 1; i < vals.length; i++)
            sb.append(c).append(vals[i]);

        return sb;
    }

    /**
     * 将一个整型数组转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c    分隔符
     * @param vals 数组
     * @return 拼合后的字符串
     */
    public static StringBuilder concat(Object c, int[] vals) {
        StringBuilder sb = new StringBuilder();
        if (null == vals || 0 == vals.length)
            return sb;

        sb.append(vals[0]);
        for (int i = 1; i < vals.length; i++)
            sb.append(c).append(vals[i]);

        return sb;
    }

    /**
     * 将一个数组的部分元素转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param offset 开始元素的下标
     * @param len    元素数量
     * @param c      分隔符
     * @param objs   数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(int offset, int len, Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs || len < 0 || 0 == objs.length)
            return sb;

        if (offset < objs.length) {
            sb.append(objs[offset]);
            for (int i = 1; i < len && i + offset < objs.length; i++) {
                sb.append(c).append(objs[i + offset]);
            }
        }
        return sb;
    }

    /**
     * 将一个数组所有元素拼合成一个字符串
     *
     * @param objs 数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(T[] objs) {
        StringBuilder sb = new StringBuilder();
        for (T e : objs)
            sb.append(e.toString());
        return sb;
    }

    /**
     * 将一个数组部分元素拼合成一个字符串
     *
     * @param offset 开始元素的下标
     * @param len    元素数量
     * @param array  数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(int offset, int len, T[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(array[i + offset].toString());
        }
        return sb;
    }

    /**
     * 将一个集合转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c    分隔符
     * @param coll 集合
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(Object c, Collection<T> coll) {
        StringBuilder sb = new StringBuilder();
        if (null == coll || coll.isEmpty())
            return sb;
        return concat(c, coll.iterator());
    }

    /**
     * 将一个迭代器转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c  分隔符
     * @param it 集合
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(Object c, Iterator<T> it) {
        StringBuilder sb = new StringBuilder();
        if (it == null || !it.hasNext())
            return sb;
        sb.append(it.next());
        while (it.hasNext())
            sb.append(c).append(it.next());
        return sb;
    }


    public static <T> List<T> array2list(T[] array) {
        if (null == array)
            return null;
        List<T> re = new ArrayList<T>(array.length);
        for (T obj : array)
            re.add(obj);
        return re;
    }

    /**
     * 获得一个容器（Map/集合/数组）对象包含的元素数量
     * <ul>
     * <li>null : 0
     * <li>数组
     * <li>集合
     * <li>Map
     * <li>一般 Java 对象。 返回 1
     * </ul>
     *
     * @param obj
     * @return 对象长度
     * @since Nutz 1.r.62
     */
    public static int eleSize(Object obj) {
        // 空指针，就是 0
        if (null == obj)
            return 0;
        // 数组
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        // 容器
        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).size();
        }
        // Map
        if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).size();
        }
        // 其他的就是 1 咯
        return 1;
    }

    /**
     * 如果是数组或集合取得第一个对象。 否则返回自身
     *
     * @param obj 任意对象
     * @return 第一个代表对象
     */
    public static Object first(Object obj) {
        if (null == obj)
            return obj;

        if (obj instanceof Collection<?>) {
            Iterator<?> it = ((Collection<?>) obj).iterator();
            return it.hasNext() ? it.next() : null;
        }

        if (obj.getClass().isArray())
            return Array.getLength(obj) > 0 ? Array.get(obj, 0) : null;

        return obj;
    }

    /**
     * 安全的从一个数组获取一个元素，容忍 null 数组，以及支持负数的 index
     * <p>
     * 如果该下标越界，则返回 null
     *
     * @param <T>
     * @param array 数组，如果为 null 则直接返回 null
     * @param index 下标，-1 表示倒数第一个， -2 表示倒数第二个，以此类推
     * @return 数组元素
     */
    public static <T> T get(T[] array, int index) {
        if (null == array)
            return null;
        int i = index < 0 ? array.length + index : index;
        if (i < 0 || i >= array.length)
            return null;
        return array[i];
    }

    public static Number strTonumber(String s) {
        // null 值
        if (null == s) {
            return 0;
        }
        s = s.toUpperCase();
        // 浮点
        if (s.indexOf('.') != -1) {
            char c = s.charAt(s.length() - 1);
            if (c == 'F' || c == 'f') {
                return Float.valueOf(s);
            }
            return Double.valueOf(s);
        }
        // 16进制整数
        if (s.startsWith("0X")) {
            return Integer.valueOf(s.substring(2), 16);
        }
        // 长整数
        if (s.charAt(s.length() - 1) == 'L' || s.charAt(s.length() - 1) == 'l') {
            return Long.valueOf(s.substring(0, s.length() - 1));
        }
        // 普通整数
        Long re = Long.parseLong(s);
        if (Integer.MAX_VALUE >= re && re >= Integer.MIN_VALUE)
            return re.intValue();
        return re;
    }

    /**
     * 将字符数组强制转换成字节数组。如果字符为双字节编码，则会丢失信息
     *
     * @param cs 字符数组
     * @return 字节数组
     */
    public static byte[] toBytes(char[] cs) {
        byte[] bs = new byte[cs.length];
        for (int i = 0; i < cs.length; i++)
            bs[i] = (byte) cs[i];
        return bs;
    }

    /**
     * 将整数数组强制转换成字节数组。整数的高位将会被丢失
     *
     * @param is 整数数组
     * @return 字节数组
     */
    public static byte[] toBytes(int[] is) {
        byte[] bs = new byte[is.length];
        for (int i = 0; i < is.length; i++)
            bs[i] = (byte) is[i];
        return bs;
    }

    /**
     * 判断当前系统是否为Windows
     *
     * @return true 如果当前系统为Windows系统
     */
    public static boolean isWin() {
        try {
            String os = System.getenv("OS");
            return os != null && os.indexOf("Windows") > -1;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 一个便利的方法，将当前线程睡眠一段时间
     *
     * @param ms 要睡眠的时间 ms
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw LangKit.wrapThrow(e);
        }
    }

    /**
     * 一个便利的等待方法同步一个对象
     *
     * @param lock 锁对象
     * @param ms   要等待的时间 ms
     */
    public static void wait(Object lock, long ms) {
        if (null != lock)
            synchronized (lock) {
                try {
                    lock.wait(ms);
                } catch (InterruptedException e) {
                    throw LangKit.wrapThrow(e);
                }
            }
    }

    /**
     * 通知对象的同步锁
     *
     * @param lock 锁对象
     */
    public static void notifyAll(Object lock) {
        if (null != lock)
            synchronized (lock) {
                lock.notifyAll();
            }
    }

    public static void runInAnThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * 获得访问者的IP地址, 反向代理过的也可以获得
     *
     * @param request 请求的req对象
     * @return 来源ip
     */
    public static String getIP(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        if (ToolUtil.isEmpty(ip))
            return "";
        if (isIPv4Address(ip) || isIPv6Address(ip)) {
            return ip;
        }
        return "";
    }

    /**
     * @return 返回当前程序运行的根目录
     */
    public static String runRootPath() {
        String cp = LangKit.class.getClassLoader().getResource("").toExternalForm();
        if (cp.startsWith("file:")) {
            cp = cp.substring("file:".length());
        }
        return cp;
    }

}
