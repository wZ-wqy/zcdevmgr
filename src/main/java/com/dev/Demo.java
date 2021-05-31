package com.dev;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.service.impl.ServerMonitorService;
import com.dt.module.om.term.entity.Machine;
import com.dt.module.om.term.websocket.SftpClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.quartz.CronExpression;
import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Firmware;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Demo {

    private static void printComputerSystem(final ComputerSystem computerSystem) {

    }

    private static Boolean filterWithCronTime(String cron, String format) throws ParseException {
        //分，小时，月，月份，周(0-7)
        CronExpression cronExpression = new CronExpression("* 0/1 7-23 * * ?");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:00:00"));
        return resCron;
    }


    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<uflo-process name=\"TransDepFlow\">\n" +
                "    <start name=\"流程开始\" x=\"21\" y=\"112\" width=\"40\" height=\"70\"  label=\"流程开始\">\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"接收部门接收人审批\"></sequence-flow>\n" +
                "    </start>\n" +
                "    <task name=\"接收部门接收人审批\" x=\"151\" y=\"112\" width=\"40\" height=\"70\"  event-handler-bean=\"baseNodeEventHandler\" label=\"接收部门接收人审批\" task-type=\"Normal\" task-name=\"接收部门接收人审批\" task-listener-bean=\"approvalRoleAppointTaskListener\" assignment-type=\"Assignee\" assignment-handler-bean=\"ufloAssignmentHandler\" allow-specify-assignee=\"true\">\n" +
                "        <assignee provider-id=\"appointDepartmentApprovalRoleAssigneeProvider\" name=\"资产接收人\" id=\"1337506800102014978\"/>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"分支\"></sequence-flow>\n" +
                "    </task>\n" +
                "    <fork name=\"分支\" x=\"257\" y=\"112\" width=\"40\" height=\"70\"  label=\"分支\">\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"接收部门总经理审批\"></sequence-flow>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"本部门总经理审批\" name=\"c0\"></sequence-flow>\n" +
                "    </fork>\n" +
                "    <task name=\"接收部门总经理审批\" x=\"384\" y=\"171\" width=\"40\" height=\"70\"  label=\"接收部门总经理审批\" task-type=\"Normal\" task-name=\"接收部门总经理审批\" task-listener-bean=\"approvalRoleAppointTaskListener\" assignment-type=\"Assignee\" allow-specify-assignee=\"true\">\n" +
                "        <assignee provider-id=\"appointDepartmentApprovalRoleAssigneeProvider\" name=\"总经理\" id=\"1337254365647343617\"/>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"聚合\"></sequence-flow>\n" +
                "    </task>\n" +
                "    <task name=\"本部门总经理审批\" x=\"384\" y=\"32\" width=\"40\" height=\"70\"  label=\"本部门总经理审批\" task-type=\"Normal\" task-name=\"本部门总经理审批\" task-listener-bean=\"approvalRoleAppointTaskListener\" assignment-type=\"Assignee\" allow-specify-assignee=\"true\">\n" +
                "        <assignee provider-id=\"localDepartmentApprovalRoleAssigneeProvider\" name=\"总经理\" id=\"1337254365647343617\"/>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"聚合\"></sequence-flow>\n" +
                "    </task>\n" +
                "    <join name=\"聚合\" x=\"542\" y=\"107\" width=\"40\" height=\"70\"  label=\"聚合\">\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"综合部总经理审批\"></sequence-flow>\n" +
                "    </join>\n" +
                "    <task name=\"综合部总经理审批\" x=\"542\" y=\"223\" width=\"40\" height=\"70\"  label=\"综合部总经理审批\" task-type=\"Normal\" task-name=\"综合部总经理审批\" task-listener-bean=\"approvalRoleAppointTaskListener\" assignment-type=\"Assignee\" allow-specify-assignee=\"true\">\n" +
                "        <assignee provider-id=\"departmentApprovalRoleAssigneeProvider\" name=\"安可宁波分公司/综合部-总经理\" id=\"49-1337254365647343617\"/>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"财务部总经理审批\"></sequence-flow>\n" +
                "    </task>\n" +
                "    <task name=\"财务部总经理审批\" x=\"542\" y=\"328\" width=\"40\" height=\"70\"  label=\"财务部总经理审批\" task-type=\"Normal\" task-name=\"财务部总经理审批\" task-listener-bean=\"approvalRoleAppointTaskListener\" assignment-type=\"Assignee\" allow-specify-assignee=\"true\">\n" +
                "        <assignee provider-id=\"departmentApprovalRoleAssigneeProvider\" name=\"安可宁波分公司/财务部-总经理\" id=\"32-1337254365647343617\"/>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"总裁办审批\"></sequence-flow>\n" +
                "    </task>\n" +
                "    <task name=\"总裁办审批\" x=\"542\" y=\"431\" width=\"40\" height=\"70\"  label=\"总裁办审批\" task-type=\"Normal\" task-name=\"总裁办审批\" task-listener-bean=\"approvalRoleAppointTaskListener\" assignment-type=\"Assignee\" allow-specify-assignee=\"true\">\n" +
                "        <assignee provider-id=\"departmentApprovalRoleAssigneeProvider\" name=\"安可宁波分公司/总裁办-总裁代理\" id=\"50-1337254591711940609\"/>\n" +
                "        <sequence-flow g=\"\" type=\"line\" to=\"流程结束\"></sequence-flow>\n" +
                "    </task>\n" +
                "    <end name=\"流程结束\" x=\"542\" y=\"540\" width=\"40\" height=\"70\"  event-handler-bean=\"assetsEndEventHandler\" label=\"流程结束\" terminate=\"true\"></end>\n" +
                "</uflo-process>";
     //   System.out.println(xml);

        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> tasks=root.selectNodes("task");
            for(int i=0;i<tasks.size();i++){
                List<Element> assignee=tasks.get(i).selectNodes("assignee");
                for(int j=0;j<assignee.size();j++){
                    System.out.println(assignee.get(j).attributeValue("provider-id"));
                    System.out.println(assignee.get(j).attributeValue("name"));
                    System.out.println(assignee.get(j).attributeValue("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        String c = "* * * * 0-7 ?";
//        try {
//            System.out.println(filterWithCronTime(c, null));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            HashMap<String, Object> m = new HashMap<String, Object>();
//            m.put("name", "poi-tl 模板引擎gh");
//            m.put("abcd", "poi-tl 模板引擎");
//            m.put("localbyte", new PictureRenderData(80, 100, ".png", new FileInputStream("/Users/lank/Desktop/abc.jpg")));
//            XWPFTemplate tpl = XWPFTemplate.compile("/Users/lank/Desktop/labeltpl.docx").render(m);
//            FileOutputStream out = new FileOutputStream("/Users/lank/Desktop/output2.docx");
//            tpl.write(out);
//            out.flush();
//            out.close();
//            tpl.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//
//        String name = null;
//        try {
//            name = java.net.URLEncoder.encode("&a=12&adf=1212&c=测试", "UTF-8");
//            System.out.println(name);
//            name = java.net.URLEncoder.encode(name, "UTF-8");
//            System.out.println(name);
//            name = java.net.URLDecoder.decode(name, "UTF-8");
//            System.out.println(name);
//            System.out.println(java.net.URLDecoder.decode(name, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        // System.out.println(ServerMonitorService.createUniqueSn());
//        StringBuffer params = new StringBuffer();
//        try {
//            // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
////            params.append("name=" + URLEncoder.encode("&", "utf-8"));
////            params.append("&");
//            params.append("age=24");
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }

//

//
//        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
//        SecurityManager securityManager = factory.getInstance();
//
//        SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);
//
//        JcaCipherService jcaCipherService =new DefaultBlockCipherService("RSA") ;
//        jcaCipherService.decrypt("UOK4yfsmoavRDq+5NiYuMh2s2KC9GBzUCra4MGIx+7ERbdb0IRGsQZxDza7kir/OAupq18Vzm8cZzaHoKeC/TA==".getBytes(), "MIIBVwIBADANBgkqhk".getBytes());
//


//        for (int i=0;i<30;i++) {
//
//            int v=i;
//            ThreadTaskHelper.run(new Runnable() {
//                @Override
//                public void run( ) {
//                    Demo d=new Demo();
//                    d.action(v);
//                }
//            });
//
//        }

    }

    public void action(int v) {


        //   int v=11;
        System.out.println("thread:" + v);
        String dir = "/tmp";
        String filename = "dt";
        String rfile = dir + "/" + filename + ".war";
        String fstr = "/tmp";
        PropertiesUtil p;
        String pwd = "";


        SftpClient sftp = new SftpClient();
        Machine m = new Machine("39.105.191.22", "39.105.191.22", "root", pwd, 12500);
        sftp.connect(m, "upload");
        sftp.changeDirectory("/tmp");

        try {
            System.out.println("mkdir" + filename + ".war" + v);
            sftp.mkDir(filename + ".war" + v);
            File f = new File(fstr + "/" + filename + ".war" + v);
            sftp.changeDirectory("/tmp" + "/" + filename + ".war" + v);
            sftp.uploadFile(f, filename + ".war", null);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
