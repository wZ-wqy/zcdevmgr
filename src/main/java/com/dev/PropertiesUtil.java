package com.dev;


import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取Properties综合类,默认绑定到classpath下的config.properties文件。
 *
 * @author lank
 */
public class PropertiesUtil {
    //配置文件的路径  
    private String configPath = null;

    /**
     * 配置文件对象
     */
    private Properties props = null;

    /**
     * 默认构造函数，用于sh运行，自动找到classpath下的config.properties。
     */
    public PropertiesUtil(String configPath) throws IOException {
        // InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(configPath);
        InputStream in = FileUtils.openInputStream(new File(configPath));
        props = new Properties();
        props.load(in);
        //关闭资源  
        in.close();
    }

    public static void main(String[] args) {
        PropertiesUtil p;
        try {
            p = new PropertiesUtil("/tmp/conf.properties");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key值读取配置的值
     * Jun 26, 2010 9:15:43 PM
     *
     * @param key key值
     * @return key 键对应的值
     * @throws IOException
     * @author lank
     */
    public String readValue(String key) throws IOException {
        return props.getProperty(key);
    }

    /**
     * 读取properties的全部信息
     * Jun 26, 2010 9:21:01 PM
     *
     * @throws FileNotFoundException 配置文件没有找到
     * @throws IOException           关闭资源文件，或者加载配置文件错误
     * @author lank
     */
    public Map<String, String> readAllProperties() throws FileNotFoundException, IOException {
        //保存所有的键值
        Map<String, String> map = new HashMap<String, String>();
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        return map;
    }

    /**
     * 设置某个key的值,并保存至文件。
     * Jun 26, 2010 9:15:43 PM
     *
     * @param key key值
     * @return key 键对应的值
     * @throws IOException
     * @author lank
     */
    public void setValue(String key, String value) throws IOException {
        Properties prop = new Properties();
        InputStream fis = new FileInputStream(this.configPath);
        // 从输入流中读取属性列表（键和元素对）
        prop.load(fis);
        // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream fos = new FileOutputStream(this.configPath);
        prop.setProperty(key, value);
        // 以适合使用 load 方法加载到 Properties 表中的格式，
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流
        prop.store(fos, "last update");
        //关闭文件
        fis.close();
        fos.close();
    }
}  