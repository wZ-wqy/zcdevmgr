package com.dev;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.*;

/**
 * Created by ehomeud on 2017/4/26.
 */

public class CustomGeneratorMysql {
    @SuppressWarnings("resource")
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");

        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        scanner.close();
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws InterruptedException {

        // GlobalConfig g = new GlobalConfig();

        AutoGenerator mpg = new AutoGenerator();
        // String dir = "/Users/lank/git/zcdevmgr/src/main";
        String dir = "/Users/lank/IdeaProjectsnew/zcdevmgr/src/main";
        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(dir + "/java/");
        gc.setOpen(true);// 生成后打开文件夹
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("lank");
        gc.setFileOverride(true);// 是否覆盖文件
        gc.setDateType(DateType.ONLY_DATE);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！

        gc.setMapperName("%sMapper");
        gc.setServiceName("I%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @SuppressWarnings("unused")
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return processTypeConvert(fieldType);
            }
        });

        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        //47.92.240.43:10050
        //127.0.0.1:3306
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/dt?useUnicode=true&characterEncoding=utf8&useSSL=false");
        dsc.setUsername("root");
        dsc.setPassword("root_pwd");
        mpg.setDataSource(dsc);

        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("dr", FieldFill.INSERT));
        tableFillList.add(new TableFill("create_by", FieldFill.INSERT));
        tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
        tableFillList.add(new TableFill("update_by", FieldFill.INSERT_UPDATE));
        tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));

        StrategyConfig strategy = new StrategyConfig();
        // strategy.setLogicDeleteFieldName("DR");
        strategy.setSuperControllerClass("com.dt.core.common.base.BaseController");

        strategy.entityTableFieldAnnotationEnable(true);
        // strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        // "","sys_qud_qux"
        // "res_attr_value","res_class","res_class_attrs"
        // "res_attr_value","res_class_attrs"
        String busRoute = "zc";
        strategy.setInclude("res_uuid_strategy"); // 需要生成的表

        strategy.setTableFillList(tableFillList);
        strategy.setSuperEntityClass("com.dt.core.common.base.BaseModel");

        strategy.setSuperEntityColumns("dr", "create_by", "create_time", "update_by", "update_time");
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.dt.module");
        pc.setModuleName(busRoute);
        pc.setXml(null);

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                map.put("dt_api", "/api");

                this.setMap(map);
            }
        };

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("template/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dir + "/resources/mybatis/" + busRoute + "/" + tableInfo.getMapperName() + ".xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        mpg.setPackageInfo(pc);

        // 关闭原来的mapper文件
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);

        tc.setController("/template/controller.java.vm");
        tc.setEntity("/template/entity.java.vm");
        tc.setMapper("/template/mapper.java.vm");
        tc.setService("/template/service.java.vm");
        tc.setServiceImpl("/template/serviceImpl.java.vm");

        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

}