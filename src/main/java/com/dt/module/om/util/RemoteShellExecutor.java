package com.dt.module.om.util;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class RemoteShellExecutor {

    private static final int TIME_OUT = 1000 * 5 * 60;
    private Connection conn;
    /**
     * 远程机器IP
     */
    private String ip;
    /**
     * 用户名
     */
    private String osUsername;
    /**
     * 密码
     */
    private String password;
    private String charset = Charset.defaultCharset().toString();
    private int port = 22;
    private Session session;


    /**
     * 构造函数
     *
     * @param ip
     * @param usr
     * @param pasword
     */
    public RemoteShellExecutor(String ip, String usr, String pasword) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
    }

    public RemoteShellExecutor(String ip, String usr, String pasword, int port) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        // RemoteShellExecutor executor = new
        // RemoteShellExecutor("121.43.168.125",
        // "root", "IBG1uFcrs", 60613);
        // RemoteShellExecutor executor = new
        // RemoteShellExecutor("121.43.168.125",
        // "oracle", "oracle1234", 60613);
        RemoteShellExecutor executor = new RemoteShellExecutor("121.43.168.125", "root", "3UZNCxDF4kfouE", 59991);
        executor.exec(" nohup sh /opt/tomcat/apache-tomcat-8.0.45/bin/startup.sh ;sleep 1 &").print();
        executor.exec("ifconfig").print();

    }

    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {

        conn = new Connection(ip, port);
        conn.connect();
        return conn.authenticateWithPassword(osUsername, password);
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public RemoteShellResult exec(List<String> cmds) throws Exception {
        String cmdstr = "";
        for (int i = 0; i < cmds.size(); i++) {
            cmdstr += cmds.get(i) + ";";
        }
        return exec(cmdstr);

    }

    public RemoteShellResult exec(String cmds) {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        StringBuffer result = new StringBuffer();
        int ret = -1;
        try {
            if (login()) {
                // Open a new {@link Session} on this connection
                session = conn.openSession();

                session.requestPTY("vt100", 80, 24, 640, 480, null);

                session.execCommand(cmds);

                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);

                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);

                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

                result.append(outStr);
                result.append(outErr);
                ret = session.getExitStatus();
            } else {
                ret = -101;
                result.append("登录远程机器失败" + ip);
            }
        } catch (IOException e) {

            e.printStackTrace();
            ret = -102;
            result.append("执行异常IO" + e.getMessage());
        } catch (Exception e) {

            e.printStackTrace();
            ret = -103;
            result.append("执行异常" + e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return RemoteShellResult.setData(ret, result);
    }

    /**
     * @param in
     * @param charset
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private String processStream(InputStream in, String charset) throws Exception {

        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();

    }
}