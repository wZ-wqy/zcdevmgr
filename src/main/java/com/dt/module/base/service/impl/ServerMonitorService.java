package com.dt.module.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.common.base.BaseService;
import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.net.IpUtils;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class ServerMonitorService extends BaseService {

    public static void main(String[] args) throws InterruptedException {

        ServerMonitorService.getMemInfo();
        ServerMonitorService.getThread();
        ServerMonitorService.getSysInfo();
        ServerMonitorService.getJvmInfo();
        ServerMonitorService.getSysFiles();
        ServerMonitorService.getCpuInfo();

    }

    /**
     * @Description: 获取系统CPU信息
     */
    public static JSONObject getCpuInfo() {

        JSONObject r = new JSONObject();
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            // 睡眠1s
            TimeUnit.SECONDS.sleep(1);
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
            r.put("core", processor.getLogicalProcessorCount());
            r.put("sysusage", new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));
            r.put("usrusage", new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));
            r.put("iowaitusage", new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));
            r.put("usage", new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * @Description: 获取系统内存信息
     */
    public static JSONObject getMemInfo() {
        JSONObject r = new JSONObject();
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();
        r.put("totalmem", formatByte(totalByte));
        r.put("used", formatByte(totalByte - acaliableByte));
        r.put("aliable", formatByte(acaliableByte));
        r.put("usage", new DecimalFormat("#.##%").format((totalByte - acaliableByte) * 1.0 / totalByte));
        return r;
    }

    /**
     * @Description: 获取序列号
     */
    public static String createUniqueSn() {
        String uid = "";
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        ComputerSystem computerSystem = hal.getComputerSystem();
        String str = "";
//        System.out.println("manufacturer: " + computerSystem.getManufacturer());
//        System.out.println("model: " + computerSystem.getModel());
//        System.out.println("serialnumber: " + computerSystem.getSerialNumber());
        try {
            str = str + computerSystem.getManufacturer() + computerSystem.getSerialNumber();
        }catch (Exception e){
            System.out.println("Get Sn error");
        }


        final Firmware firmware = computerSystem.getFirmware();
//        System.out.println("firmware:");
//        System.out.println("  manufacturer: " + firmware.getManufacturer());
//        System.out.println("  name: " + firmware.getName());
//        System.out.println("  description: " + firmware.getDescription());
//        System.out.println("  version: " + firmware.getVersion());
        str = str + firmware.getName();
        final Baseboard baseboard = computerSystem.getBaseboard();
//        System.out.println("baseboard:");
//        System.out.println("  manufacturer: " + baseboard.getManufacturer());
//        System.out.println("  model: " + baseboard.getModel());
//        System.out.println("  version: " + baseboard.getVersion());
//        System.out.println("  serialnumber: " + baseboard.getSerialNumber());
        str = str + baseboard.getSerialNumber();
        uid = MD5Util.encrypt(str);
        return uid;
    }

    /**
     * @Description: 获取系统信息
     */
    public static JSONObject getSysInfo() {
        JSONObject r = new JSONObject();
        Properties props = System.getProperties();
        //系统名称
        String osName = props.getProperty("os.name");
        //架构名称
        String osArch = props.getProperty("os.arch");
        r.put("osname", osName);
        r.put("osarch", osArch);
        r.put("ip", IpUtils.getHostIp());
        r.put("hostname", IpUtils.getHostName());
        return r;
    }

    /**
     * @Description: 获取系统文件系统信息
     */
    public static JSONArray getSysFiles() {
        JSONArray r = new JSONArray();
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            JSONObject e = new JSONObject();
            e.put("DirName", fs.getMount());
            e.put("SysTypeName", fs.getType());
            e.put("TypeName", fs.getName());
            e.put("Total", convertFileSize(total));
            e.put("Free", convertFileSize(free));
            e.put("Used", convertFileSize(used));
            e.put("usage", mul(div(used, total, 4), 100));
            r.add(e);
        }
        return r;
    }

    /**
     * @Description: 获取JVM信息
     */
    public static JSONObject getJvmInfo() {
        JSONObject r = new JSONObject();
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtime2 = ManagementFactory.getRuntimeMXBean();
        //jvm总内存
        long jvmTotalMemoryByte = runtime.totalMemory();
        //jvm最大可申请
        long jvmMaxMoryByte = runtime.maxMemory();
        //空闲空间
        long freeMemoryByte = runtime.freeMemory();
        //jdk版本
        String jdkVersion = props.getProperty("java.version");
        //jdk路径
        String dir = props.getProperty("user.dir");
        //启动时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String jdkHome = props.getProperty("java.home");
        //jvm内存总量
        r.put("jvmtotal", formatByte(jvmTotalMemoryByte));
        //jvm已使用内存
        r.put("jvmused", formatByte(jvmTotalMemoryByte - freeMemoryByte));
        //jvm剩余内存
        r.put("jvmfree", formatByte(freeMemoryByte));
        //jvm内存使用率
        r.put("jvmusage", new DecimalFormat("#.##%").format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        //安装路径
        r.put("jdkhome", jdkHome);
        //java版本
        r.put("javaversion", jdkVersion);
        //启动时间
        r.put("starttime", sdf.format(new Date(time)));
        //项目路径
        r.put("dir", dir);
        r.put("javaname", runtime2.getVmName());
        r.put("uptime", runtime2.getUptime());

        return r;
    }

    /**
     * @Description: 获取线程信息
     */
    public static JSONObject getThread() {
        JSONObject r = new JSONObject();
        System.out.println("----------------线程信息----------------");
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();

        while (currentGroup.getParent() != null) {
            // 返回此线程组的父线程组
            currentGroup = currentGroup.getParent();
        }
        //此线程组中活动线程的估计数
        int noThreads = currentGroup.activeCount();

        Thread[] lstThreads = new Thread[noThreads];
        //把对此线程组中的所有活动子组的引用复制到指定数组中。
        currentGroup.enumerate(lstThreads);
        for (Thread thread : lstThreads) {
            System.out.println("线程数量：" + noThreads + " 线程id：" + thread.getId() + " 线程名称：" + thread.getName() + " 线程状态：" + thread.getState());
        }
        return r;
    }

    /**
     * @Description: 格式转化
     */
    public static String formatByte(long byteNumber) {
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}
