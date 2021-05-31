function config_system($stateProvider, $ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true
    });
    console.log("App System config");
    $stateProvider.state('me', {
        abstract: true,
        url: "/me",
        templateUrl: "views/common/content.html?v=" + version
    }).state('me.common_mgr', {
        url: "/common_mgr",
        data: {pageTitle: '通用设置'},
        templateUrl: "views/me/common_mgr.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/me/common_mgr.js?v=' + version]
                }]);
            }
        }
    }).state('me.receivingaddr', {
        url: "/receivingaddr",
        data: {pageTitle: '收货地址'},
        template: '<div ng-controller="meRecAddrCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/me/receaddr.js?v=' + version]
                }]);
            }
        }
    }).state('me.accesslog', {
        url: "/accesslog",
        data: {pageTitle: '访问日志'},
        template: '<div ng-controller="meAccessLogCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/me/accesslog.js?v=' + version]
                }]);
            }
        }
    }).state('me.pwdreset', {
        url: "/pwdreset",
        data: {pageTitle: '密码修改'},
        templateUrl: "views/me/pwdreset.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/me/pwdreset.js?v=' + version]
                }]);
            }
        }
    });
    // 内容管理
    $stateProvider.state('ct', {
        abstract: true,
        templateUrl: "views/common/content.html?v=" + version,
    }).state('ct.catesetting', {
        url: "/catesetting",
        data: {pageTitle: '类目设置'},
        templateUrl: "views/content/ctCategory.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/content/ctCategory.js?v=' + version]
                }]);
            }
        }
    }).state('ct.publishnews', {
        url: "/publishnews",
        data: {pageTitle: '发布信息'},
        templateUrl: "views/content/newsPublish.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/content/newsPublish.js?v=' + version]
                }]);
            }
        }
    }).state('ct.news_mgr', {
        url: "/newMgr",
        data: {pageTitle: '新闻管理'},
        template: '<div ng-controller="ctNewsMgrCtl" >' + buildSimpleToolTableTpl() + '</div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/content/newsMgr.js?v=' + version]
                }]);
            }
        }
    }).state('ct.company_profile', {
        url: "/company_profile",
        data: {pageTitle: '公司简介'},
        templateUrl: "views/content/company.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/content/company.js?v=' + version]
                }]);
            }
        }
    }).state('ct.demo', {
        url: "/ct_demo",
        data: {pageTitle: 'DEMO'},
        templateUrl: "views/system//base/demo.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system//base/demo.js?v=' + version]
                }]);
            }
        }
    });
    // 用户管理
    $stateProvider.state('user', {
        abstract: true,
        url: "/user",
        templateUrl: "views/common/content.html?v=" + version
    }).state('user.user_query', {
        url: "/user_query",
        data: {pageTitle: '用户查询'},
        template: '<div ng-controller="sysUserQueryCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/user/user_query.js?v=' + version]
                }]);
            }
        }
    }).state('user.user_group', {
        url: "/user_group",
        data: {pageTitle: '用户组设置'},
        template: '<div ng-controller="sysGroupSettingCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/user/group_setting.js?v=' + version]
                }]);
            }
        }
    }).state('user.user_add', {
        url: "/user_add",
        data: {pageTitle: '用户新增'},
        templateUrl: "views/system/user/user_add.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/user/user_add.js?v=' + version]
                }]);
            }
        }
    }).state(
        'user.user_setting',
        {
            url: "/user_setting",
            templateUrl: "views/system/user/user_setting.html?v=" + version,
            data: {pageTitle: '用户设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            name: 'frapontillo.bootstrap-duallistbox',
                            files: ['vendor/bootstrap-duallistbox/dist/bootstrap-duallistbox.min.css', 'vendor/bootstrap-duallistbox/dist/jquery.bootstrap-duallistbox.min.js', 'plugin/dualListbox/angular-bootstrap-duallistbox.js?v=' + version]
                        }, {
                            files: ['views/system/user/user_setting.js?v=' + version]
                        }]);
                }
            }
        });
    // 模块管理
    $stateProvider.state('module', {
        abstract: true,
        url: "/module",
        templateUrl: "views/common/content.html?v=" + version
    }).state('module.module_setting', {
        url: "/module_setting",
        data: {pageTitle: '模块管理'},
        templateUrl: "views/system/menu/menu.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        files: ['views/system/menu/menu.js?v=' + version]
                    }
                ]);
            }
        }
    }).state('module.rootmenu', {
        url: "/rootmenu",
        data: {pageTitle: '主菜单管理'},
        template: '<div ng-controller="sysRootMenugCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/menu/rootmenu.js?v=' + version]
                }]);
            }
        }
    });
    // 角色管理
    $stateProvider.state('role', {
        abstract: true,
        url: "/role",
        templateUrl: "views/common/content.html?v=" + version
    }).state('role.role_setting', {
        url: "/role_setting",
        data: {pageTitle: '角色设置'},
        template: '<div ng-controller="sysRoleSettingCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/role/role_setting.js?v=' + version]
                }]);
            }
        }
    }).state('role.role_module_map', {
        url: "/role_module_map",
        data: {pageTitle: '角色模块映射'},
        templateUrl: "views/system/role/role_module_map.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/role/role_module_map.js?v=' + version]
                }]);
            }
        }
    });
    // 权限管理
    $stateProvider.state('privilige', {
        abstract: true,
        url: "/privilige",
        templateUrl: "views/common/content.html?v=" + version
    }).state('privilige.role', {
        url: "/role",
        abstract: true,
        templateUrl: "views/common/c.html?v=" + version
    }).state('privilige.role.role_module_map', {
        url: "/role_module_map",
        templateUrl: "views/system/role/role_module_map.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/role/role_module_map.js?v=' + version]
                }]);
            }
        }
    });
    // 系统设置
    $stateProvider.state('system', {
        abstract: true,
        url: "/system",
        templateUrl: "views/common/content.html?v=" + version
    }).state('system.file_setting', {
        url: "/filesetting",
        data: {pageTitle: '文件设置'},
        template: '<div ng-controller="sysfileConfCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/filesetting.js?v=' + version]
                }]);
            }
        }
    }).state('system.dict_setting', {
        url: "/dict_setting",
        data: {pageTitle: '字典设置'},
        templateUrl: "views/system/dict/dict.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/dict/dict.js?v=' + version]
                }]);
            }
        }
    }).state('system.params', {
        url: "/sys_params",
        data: {pageTitle: '参数设置'},
        template: '<div ng-controller="sysParamsCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/params/params.js?v=' + version]
                }]);
            }
        }
    }).state('system.storesql', {
        url: "/storesql",
        data: {pageTitle: 'StoreSQL设置'},
        template: '<div ng-controller="sysStoreSqlCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/store/storesql.js?v=' + version]
                }]);
            }
        }
    }).state('system.onlinesession', {
        url: "/online",
        data: {pageTitle: '在线用户'},
        template: '<div ng-controller="sysOnlineSessionCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/user/online.js?v=' + version]
                }]);
            }
        }
    }).state('system.cachemgr', {
        url: "/cachemgr",
        data: {pageTitle: '缓存管理'},
        template: '<div ng-controller="sysCacheCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/cache/cache.js?v=' + version]
                }]);
            }
        }
    }).state('system.druid', {
        url: "/druid",
        data: {pageTitle: 'Druid监控'},
        templateUrl: "views/system/mon/druid.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/mon/druid.js?v=' + version]
                }]);
            }
        }
    }).state('system.servermonitor', {
        url: "/servermonitor",
        data: {pageTitle: '服务监控'},
        templateUrl: "views/system/mon/servermonitor.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/mon/servermonitor.js?v=' + version]
                }]);
            }
        }
    }).state('system.databackup', {
        url: "/systemdatabackup",
        data: {pageTitle: '数据备份'},
        templateUrl: "views/system/backup/databackup.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/backup/databackup.js?v=' + version]
                }]);
            }
        }
    });
    // 组织架构
    $stateProvider.state('org', {
        abstract: true,
        url: "/org",
        templateUrl: "views/common/content.html?v=" + version
    }).state('org.employee', {
        url: "/org_employee",
        data: {pageTitle: '人员查询'},
        templateUrl: "views/org/employee.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/org/employee.js?v=' + version]
                }]);
            }
        }
    }).state('org.employee_adjust', {
        url: "/org_employee_adjust",
        data: {pageTitle: '人员调整'},
        templateUrl: "views/org/employee_adjust.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/org/employee_adjust.js?v=' + version]
                }]);
            }
        }
    }).state('org.part', {
        url: "/org_part",
        data: {pageTitle: '组织设置'},
        templateUrl: "views/org/part.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/org/part.js?v=' + version]
                }]);
            }
        }
    }).state('org.position', {
        url: "/org_positionmgr",
        data: {pageTitle: '岗位管理'},
        template: '<div ng-controller="positionCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/org/position.js?v=' + version]
                }]);
            }
        }
    }).state('org.approvalnode', {
        url: "/org_approvalnode",
        data: {pageTitle: '审批节点'},
        template: '<div ng-controller="approvalnodeCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/org/approvalnode.js?v=' + version]
                }]);
            }
        }
    });
    // 任务设置
    $stateProvider.state('task', {
        abstract: true,
        url: "/task",
        templateUrl: "views/common/content.html?v=" + version
    }).state('task.task_mgr', {
        url: "/task_mgr",
        data: {pageTitle: '任务管理'},
        template: '<div ng-controller="sysTaskCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/task/task.js?v=' + version]
                }]);
            }
        }
    });
    // 表单管理
    $stateProvider.state('formmgr', {
        abstract: true,
        url: "/formmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('formmgr.setting', {
        url: "/formmgr_setting",
        data: {pageTitle: '表单设置'},
        //	template:'<div ng-controller="formSettingCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        templateUrl: "views/system/form/formsetting.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/form/formsetting.js?v=' + version]
                }]);
            }
        }
    }).state('formmgr.fdesigner', {
        url: "/formmgr_fdesigner",
        data: {pageTitle: '表单设计'},
        templateUrl: "views/system/form/fdesigner.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        files: ['plugin/form/k-form-design.css', 'plugin/form/vue.min.js?v=' + version, 'plugin/form/k-form-design.umd.min.js?v=' + version, 'plugin/form/vue-resource.min.js?v=' + version]
                    }
                    ,
                    {
                        serie: true,
                        files: ['views/system/form/fdesigner.js?v=' + version]
                    }]);
            }
        }
    });
    // flow
    $stateProvider.state('flow', {
        abstract: true,
        url: "/flow",
        templateUrl: "views/common/content.html?v=" + version
    }).state('flow.designer', {
        url: "/flow_designer",
        data: {pageTitle: '流程设计'},
        templateUrl: "views/flow/designer.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/designer.js?v=' + version]
                }]);
            }
        }
    }).state('flow.flowtree', {
        url: "/flow_flowtree",
        data: {pageTitle: '流程树', code: "5"},
        templateUrl: "views/flow/flowtree.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/flowtree.js?v=' + version]
                }]);
            }
        }
    }).state('flow.pgroup', {
        url: "/flow_pgroup",
        data: {pageTitle: '流程分组'},
        template: '<div ng-controller="sysFlowGroupCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/flowgroup.js?v=' + version]
                }]);
            }
        }
    }).state('flow.processmatch', {
        url: "/flow_processmatch",
        data: {pageTitle: '流程设置'},
        templateUrl: "views/flow/flowmatch.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/flowmatch.js?v=' + version]
                }]);
            }
        }
    }).state('flow.flowquery', {
        url: "/flow_flowquery",
        data: {pageTitle: '流程监测'},
        templateUrl: "views/flow/processmonitor.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/processmonitor.js?v=' + version]
                }]);
            }
        }
    }).state('flow.flowapproval', {
        url: "/flow_flowapproval",
        data: {pageTitle: '业务流程'},
        templateUrl: "views/flow/flowapproval.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/flowapproval.js?v=' + version]
                }]);
            }
        }
    }).state('flow.processconf', {
        url: "/processconf",
        data: {pageTitle: '流程配置'},
        templateUrl: "views/flow/processconf.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/flow/processconf.js?v=' + version]
                }]);
            }
        }
    });
    // flow
    $stateProvider.state('fullpage', {
        abstract: true,
        url: "/fullpage",
        templateUrl: "views/common/fullpage.html?v=" + version
    }).state('fullpage.flowdetail', {
        url: "/fullpage_flowdetail",
        params: {
            id: null,
            pagetype: null
        },
        data: {pageTitle: '详情'},
        templateUrl: "views/cmdb/flowdetail.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/flowdetail.js?v=' + version]
                }]);
            }
        }
    })
}

function renderDTFontColorRedH(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:red;font-weight:bold\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColorRed(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:red\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColorGreenH(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:green;font-weight:bold\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColorGreen(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:green\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColoBluerH(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:blue;font-weight:bold\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColoBlue(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:blue\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColoPurpleH(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return data;
    }
}

function renderDTFontColoPurple(data, type, full) {
    if (angular.isDefined(data)) {
        return "<span style=\"color:purple\">" + data + "</span>"
    } else {
        return data;
    }
}
