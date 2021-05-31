package com.dt.module.om.term.websocket;

import ch.ethz.ssh2.*;
import com.dt.module.om.term.bean.SftpBean;
import com.dt.module.om.term.bean.SftpFileBean;
import com.dt.module.om.term.entity.Machine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SftpClient {

    private static Logger log = LoggerFactory.getLogger(SftpClient.class);

    private Connection conn;
    private SFTPv3Client client;
    private Stack<String> catalogs = new Stack<>();
    private String cutrrentCatalog;

    public SftpClient() {
    }

    public SftpClient(Machine m, String spkey) {
        super();
        connect(m, spkey);
    }

    public void close() {
        if (client != null) {
            if (client.isConnected()) {
                client.close();
            }
        }
    }

    public boolean connect(Machine m, String spkey) {

        try {
            conn = new Connection(m.getHostname(), m.getPort());
            conn.connect();
            if (!conn.authenticateWithPassword(m.getUsername(), m.getPassword()))
                return false;
            client = new SFTPv3Client(conn);

            // init the current catalogs
            initCatalogs(client.canonicalPath("."));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        if (client != null)
            return client.isConnected();
        return false;
    }

    private void initCatalogs(String str) {

        cutrrentCatalog = str;
        catalogs.push("/");

        String[] ss = str.split("/");

        for (String s : ss) {
            if (!"".equals(s))
                catalogs.push(s);
        }
    }

    public String getCurrentCatalog() {
        return cutrrentCatalog;
    }

    public void changeDirectory(String dirName) {

        // if param is a absolute path
        if ("/".equals(dirName.substring(0, 1))) {
            catalogs.clear();
            initCatalogs(dirName);
            cutrrentCatalog = dirName;
            return;
        }

        // parent directory
        if ("..".equals(dirName) && catalogs.size() > 1) {
            // update the catalogs stack
            catalogs.pop();
        }
        // child directory
        else {
            catalogs.push(dirName);
        }
        updateCurrentCatalog();
    }

    private void updateCurrentCatalog() {
        String path = "";
        for (String s : catalogs) {
            if (!"/".equals(s))
                path += "/" + s;
        }
        if (catalogs.size() == 1)
            path = "/";
        cutrrentCatalog = path;
    }

    public SftpBean ls() throws IOException {

        List<SFTPv3DirectoryEntry> list = client.ls(cutrrentCatalog);
        List<SftpFileBean> sftpFiles = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        for (SFTPv3DirectoryEntry entry : list) {

            if (!entry.filename.equals("..") && !entry.filename.equals(".")) {
                SftpFileBean sfile = new SftpFileBean();
                sfile.setFilename(entry.filename);
                sfile.setDirectory(entry.attributes.isDirectory());
                sfile.setIntPermissions(entry.attributes.permissions);
                sfile.setStrPermissions(getStringPermission(entry.attributes.permissions));
                sfile.setOctalPermissions(entry.attributes.getOctalPermissions());
                sfile.setMtime(sdf.format(new Date((long) entry.attributes.mtime * 1000)));
                sfile.setSize(entry.attributes.size);
                sftpFiles.add(sfile);
            }
        }

        Collections.sort(sftpFiles, new Comparator<SftpFileBean>() {
            @Override
            public int compare(SftpFileBean o1, SftpFileBean o2) {
                return o1.compareTo(o2);
            }
        });

        return new SftpBean(getCurrentCatalog(), sftpFiles);
    }

    private String getStringPermission(Integer p) {
        String[] temp = new String[]{"---", "--x", "-w-", "-wx", "r--", "r-x", "rw-", "rwx"};
        // is a directory ?
        return (p / 8 / 8 / 8 / 8 % 8 == 04 ? "d" : "-") + temp[p / 8 / 8 % 8] + temp[p / 8 % 8] + temp[p % 8];
    }

    public void uploadFile(File file, String fileName, Map<String, Object> session) throws IOException {

        if (file == null)
            return;
        FileInputStream fis = new FileInputStream(file);

        long totalSize = file.length();
        byte[] b = new byte[1024 * 8];
        long count = 0;

        SFTPv3FileHandle handle = client.createFile(getCurrentCatalog() + "/" + fileName);
        DecimalFormat df = new DecimalFormat("#.00");

        while (true) {
            int len = fis.read(b);
            if (len == -1)
                break;
            client.write(handle, count, b, 0, len);
            count += len;
            System.out.println(fileName + " {\"percent\":\"" + df.format((double) count / totalSize * 100)
                    + "%\",\"num\":\"" + (int) ((double) count / totalSize) + "\"}");
            if (session != null) {
                session.put("progress", "{\"percent\":\"" + df.format((double) count / totalSize * 100)
                        + "%\",\"num\":\"" + (int) ((double) count / totalSize) + "\"}");
            }
        }
        client.closeFile(handle);
        fis.close();
    }

    // download file from current catalog
    public InputStream downloadFile(String fileName) throws IOException {
        log.info("downloadfile:" + getCurrentCatalog() + "/" + fileName.trim());
        SFTPv3FileHandle handle = client.openFileRO(getCurrentCatalog() + "/" + fileName);
        long count = 0;
        byte[] buff = new byte[1024 * 8];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int len = client.read(handle, count, buff, 0, 1024 * 8);
            if (len == -1)
                break;
            baos.write(buff, 0, len);
            count += len;
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public void downloadFile(String fileName, String dest) throws IOException {
        InputStream input = downloadFile(fileName);
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(dest);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();

    }

    public void deleteFile(String fileName) throws IOException {
        client.rm(getCurrentCatalog() + "/" + fileName);
    }

    public void mkDir(String dirName) throws IOException {
        client.mkdir(getCurrentCatalog() + "/" + dirName, 0755);
    }

    public void createFile(String fileName) throws IOException {
        client.createFileTruncate(getCurrentCatalog() + "/" + fileName);
    }

    public void setAttributes(String fileName, Integer permissions) throws IOException {

        SFTPv3FileAttributes attr = client.stat(getCurrentCatalog() + "/" + fileName);

        SFTPv3FileAttributes attr1 = new SFTPv3FileAttributes();
        attr1.permissions = attr.permissions / 8 / 8 / 8 * 8 * 8 * 8 + permissions;
        client.setstat(getCurrentCatalog() + "/" + fileName, attr1);

    }
}
