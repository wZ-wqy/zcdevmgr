

function config_cmdb($stateProvider, $ocLazyLoadProvider) {
    console.log("App cmdb config");
    $ocLazyLoadProvider.config({
        debug: true
    });
    // 软件
    $stateProvider.state('softzc', {
        abstract: true,
        url: "/softzc",
        templateUrl: "views/common/content.html?v=" + version
    }).state('softzc.softzcdj', {
        url: "/softzcdj",
        data: {pageTitle: '软件资产登记'},
        templateUrl: "views/cmdb/softzcdj.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/softzcdj.js?v=' + version]
                }]);
            }
        }
    }).state('softzc.softlegalization', {
        url: "/softlegalization",
        data: {pageTitle: '软件正版化'},
        templateUrl: "views/cmdb/softlegalization.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/softlegalization.js?v=' + version]
                }]);
            }
        }
    }).state('softzc.softzcquerycat', {
        url: "/softzcquerycat",
        data: {pageTitle: '资产查询'},
        templateUrl: "views/cmdb/softzcquerycat.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/softzcquerycat.js?v=' + version]
                }]);
            }
        }
    })


    $stateProvider.state('zcindex', {
        url: "/zcindex",
        templateUrl: "views/cmdb/zcindex.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        name: 'angular-flot',
                        files: ['plugin/flot/jquery.flot.js', 'plugin/flot/jquery.flot.time.js', 'plugin/flot/jquery.flot.tooltip.min.js', 'plugin/flot/jquery.flot.spline.js', 'plugin/flot/jquery.flot.resize.js', 'plugin/flot/jquery.flot.pie.js', 'plugin/flot/curvedLines.js', 'plugin/flot/angular-flot.js',]
                    },
                    {
                        serie: true,
                        files: ['views/cmdb/zcindex.js?v=' + version]
                    }]);
            }
        }
    });


    $stateProvider.state('zcprocess', {
        abstract: true,
        url: "/zcprocess",
        templateUrl: "views/common/content.html?v=" + version
    }).state('zcprocess.allprocess', {
        url: "/zcprocess_allprocess",
        data: {pageTitle: '所有流程'},
        templateUrl: "views/cmdb/flow/allprocess.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/flow/allprocess.js?v=' + version]
                }]);
            }
        }
    })
    // 文档管理
    $stateProvider.state('docmgr', {
        abstract: true,
        url: "/docmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('docmgr.doclist', {
        url: "/docmgr_doclist",
        data: {pageTitle: '文档管理'},
        // templateUrl: "views/cmdb/doclist.html?v=" + version,
        template: '<div ng-controller="doclistCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/doclist.js?v=' + version]
                }]);
            }
        }
    })


    // 盘点管理
    $stateProvider.state('pandian', {
        abstract: true,
        url: "/pandian",
        templateUrl: "views/common/content.html?v=" + version
    }).state('pandian.zcpd', {
        url: "/pandian_pcpd",
        data: {pageTitle: '资产盘点'},
        templateUrl: "views/cmdb/zcinventory.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcinventory.js?v=' + version]
                }]);
            }
        }
    })
    // cmdb
    $stateProvider.state('maintain', {
        abstract: true,
        url: "/maintain",
        templateUrl: "views/common/content.html?v=" + version
    }).state('maintain.faultrecord', {
        url: "/maintain_faultrecord",
        data: {pageTitle: '报修工作', datatype: "full"},
        templateUrl: "views/cmdb/faultrecord.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/faultrecord.js?v=' + version]
                }]);
            }
        }
    }).state('maintain.review', {
        url: "/maintain_review",
        data: {pageTitle: '资产复核'},
        templateUrl: "views/cmdb/review.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/review.js?v=' + version]
                }]);
            }
        }
    }).state('maintain.inspectionplan', {
        url: "/maintain_inspectionplan",
        data: {pageTitle: '巡检计划表'},
        templateUrl: "views/cmdb/inspectionplan.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/inspectionplan.js?v=' + version]
                }]);
            }
        }
    }).state('maintain.inspection', {
        url: "/maintain_inspection",
        data: {pageTitle: '巡检'},
        templateUrl: "views/cmdb/inspection.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/inspection.js?v=' + version]
                }]);
            }
        }
    });

    // 报表
    $stateProvider.state('cmdbresp', {
        abstract: true,
        url: "/cmdbresp",
        templateUrl: "views/common/content.html?v=" + version
    }).state('cmdbresp.partzc', {
        url: "/maintain_partzc",
        data: {pageTitle: '部门资产'},
        template: '<div ng-controller="cmdbrepPartZcCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/partzc.js?v=' + version]
                }]);
            }
        }
    }).state('cmdbresp.zctjshow', {
        url: "/cmdbresp_zctjshow",
        data: {pageTitle: '资产统计'},
        templateUrl: "views/cmdb/rep/zctj.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/zctj.js?v=' + version]
                }]);
            }
        }
    }).state('cmdbresp.bashboard', {
        url: "/cmdbresp_bashboard",
        data: {pageTitle: '展示面板'},
        templateUrl: "views/cmdb/rep/dashboard.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        name: 'angular-flot',
                        files: ['plugin/flot/jquery.flot.js', 'plugin/flot/jquery.flot.time.js', 'plugin/flot/jquery.flot.tooltip.min.js', 'plugin/flot/jquery.flot.spline.js', 'plugin/flot/jquery.flot.resize.js', 'plugin/flot/jquery.flot.pie.js', 'plugin/flot/curvedLines.js', 'plugin/flot/angular-flot.js',]
                    },
                    {
                        serie: true,
                        files: ['views/cmdb/rep/dashboard.js?v=' + version]
                    }]);
            }
        }
    });
    $stateProvider.state('cmsetting', {
        abstract: true,
        url: "/cmsetting",
        templateUrl: "views/common/content.html?v=" + version
    }).state('cmsetting.zccat', {
        url: "/cmsetting_zccat",
        data: {pageTitle: '资产分类', code: "3"},
        templateUrl: "views/cmdb/zccategory.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zccategory.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.softcat', {
        url: "/cmsetting_softcat",
        data: {pageTitle: '软件分类', code: "12"},
        templateUrl: "views/cmdb/softcategory.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/softcategory.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.colctl', {
        url: "/cmsetting_colctl",
        data: {pageTitle: '字段显示',},
        templateUrl: "views/cmdb/colctl.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/colctl.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.zjstrategy', {
        url: "/cmsetting_zjstrategy",
        data: {pageTitle: '折旧策略'},
        templateUrl: "views/cmdb/zjstrategy.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zjstrategy.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.goodscat', {
        url: "/cmsetting_goodscat",
        data: {pageTitle: '物品档案', code: "7"},
        templateUrl: "views/cmdb/goodscategory.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/goodscategory.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.bjcat', {
        url: "/cmsetting_bjcat",
        data: {pageTitle: '备件分类', code: "8"},
        templateUrl: "views/cmdb/zcbjcategory.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcbjcategory.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.dataimport', {
        url: "/cmsetting_dataimport",
        data: {pageTitle: '资产导入', category: 3},
        templateUrl: "views/cmdb/dataimport.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/dataimport.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.approvalnode', {
        url: "/approvalnode",
        data: {pageTitle: '审批节点',},
        template: '<div ng-controller="zcapprovalnodeCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/flow/approvalnode.js?v=' + version]
                }]);
            }
        }
    })
        .state('cmsetting.bjimport', {
            url: "/cmsetting_bjimport",
            data: {pageTitle: '备件导入', type: 'bj', category: '8'},
            templateUrl: "views/cmdb/dataimport.html?v=" + version,
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([{
                        serie: true,
                        files: ['views/cmdb/dataimport.js?v=' + version]
                    }]);
                }
            }
        }).state('cmsetting.labeltpl', {
        url: "/cmsetting_labeltpl",
        data: {pageTitle: '资产标签'},
        templateUrl: "views/cmdb/labeltpl.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/labeltpl.js?v=' + version]
                }]);
            }
        }
    }).state('cmsetting.assetsuuid', {
        url: "/cmsetting_assetsuuid",
        data: {pageTitle: '资产编号'},
        templateUrl: "views/cmdb/assetsuuid.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/assetsuuid.js?v=' + version]
                }]);
            }
        }
    })
    
    
    

    $stateProvider.state('myprocess', {
        abstract: true,
        url: "/myprocess",
        templateUrl: "views/common/content.html?v=" + version
    }).state('myprocess.waittask', {
        url: "/myprocess_waittask",
        data: {pageTitle: '我的待处理任务'},
        templateUrl: "views/cmdb/flow/todo.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/flow/todo.js?v=' + version]
                }]);
            }
        }
    }).state('myprocess.taskfinish', {
        url: "/myprocess_taskfinish",
        data: {pageTitle: '已处理任务'},
        templateUrl: "views/cmdb/flow/processfinish.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/flow/processfinish.js?v=' + version]
                }]);
            }
        }
    }).state('myprocess.myprocess', {
        url: "/myprocess_myprocess",
        data: {pageTitle: '我的流程'},
        templateUrl: "views/cmdb/flow/myprocess.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/flow/myprocess.js?v=' + version]
                }]);
            }
        }
    }).state('myprocess.mybx', {
        url: "/myprocess_mybx",
        data: {pageTitle: '我的报修', datatype: "self"},
        templateUrl: "views/cmdb/faultrecord.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/faultrecord.js?v=' + version]
                }]);
            }
        }
    }).state('myprocess.myassets', {
        url: "/myassets",
        data: {pageTitle: '我的资产'},
        templateUrl: "views/cmdb/myassets.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/myassets.js?v=' + version]
                }]);
            }
        }
    });

    //infrastructure
    $stateProvider.state('infrastructure', {
        abstract: true,
        url: "/infrastructure",
        templateUrl: "views/common/content.html?v=" + version
    }).state('infrastructure.rackview', {
        url: "/infrastructure_rackview",
        data: {pageTitle: '机柜视图'},
        templateUrl: "views/cmdb/infrastructure/rackview.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/infrastructure/rackview.js?v=' + version]
                }]);
            }
        }
    }).state('infrastructure.areasetting', {
        url: "/infrastructure_areasetting",
        data: {pageTitle: '区域设置'},
        templateUrl: "views/cmdb/infrastructure/areasetting.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/infrastructure/areasetting.js?v=' + version]
                }]);
            }
        }
    }).state('infrastructure.arearack', {
        url: "/infrastructure_arearack",
        data: {pageTitle: '区域机柜'},
        templateUrl: "views/cmdb/infrastructure/arearack.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/infrastructure/arearack.js?v=' + version]
                }]);
            }
        }
    }).state('infrastructure.rackmgr', {
        url: "/infrastructure_rackinfo",
        data: {pageTitle: '机柜管理'},
        templateUrl: "views/cmdb/infrastructure/rackinfo.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/infrastructure/rackinfo.js?v=' + version]
                }]);
            }
        }
    });
    // // cmdb
    // $stateProvider.state('xt', {
    //     abstract: true,
    //     url: "/xt",
    //     templateUrl: "views/common/content.html?v=" + version
    // }).state('xt.outercontact', {
    //     url: "/xt_outercontact",
    //     data: {pageTitle: '外部联系人'},
    //     templateUrl: "views/cmdb/outercontact.html?v=" + version,
    //     resolve: {
    //         loadPlugin: function ($ocLazyLoad) {
    //             return $ocLazyLoad.load([{
    //                 serie: true,
    //                 files: ['views/cmdb/outercontact.js?v=' + version]
    //             }]);
    //         }
    //     }
    // })
    //     .state('xt.systemlist', {
    //         url: "/xt_systemlist",
    //         data: {pageTitle: '信息系统清单'},
    //         template: '<div ng-controller="cmdbsystemListCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
    //         resolve: {
    //             loadPlugin: function ($ocLazyLoad) {
    //                 return $ocLazyLoad.load([{
    //                     serie: true,
    //                     files: ['views/cmdb/systemlist.js?v=' + version]
    //                 }]);
    //             }
    //         }
    //     });
    // 备件管理
    $stateProvider.state('bjmgr', {
        abstract: true,
        url: "/bjmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('bjmgr.bjpj', {
        url: "/bjmgr_bjpj",
        data: {pageTitle: '备件配件'},
        templateUrl: "views/cmdb/zcbj.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcbj.js?v=' + version]
                }]);
            }
        }
    });

    //预警中心
    $stateProvider.state('ewcenter', {
        abstract: true,
        url: "/ewcenter",
        templateUrl: "views/common/content.html?v=" + version
    }).state('ewcenter.wbout', {
        url: "/ewcenter_wbout",
        data: {pageTitle: ''},
        template: '<div ng-controller="wbexpireCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/ewcenter/wbout.js?v=' + version]
                }]);
            }
        }
    }).state('ewcenter.collect', {
        url: "/ewcenter_collect",
        data: {pageTitle: ''},
        template: '<div ng-controller="tkwarnCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/ewcenter/collect.js?v=' + version]
                }]);
            }
        }
    }).state('ewcenter.borrow', {
        url: "/ewcenter_borrow",
        data: {pageTitle: ''},
        template: '<div ng-controller="ghwarnCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/ewcenter/borrow.js?v=' + version]
                }]);
            }
        }
    }).state('ewcenter.bfout', {
        url: "/ewcenter_bfout",
        data: {pageTitle: ''},
        template: '<div ng-controller="bfexpireCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/ewcenter/bfout.js?v=' + version]
                }]);
            }
        }
    });

    // 耗材管理
    $stateProvider.state('hcmgr', {
        abstract: true,
        url: "/hcmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('hcmgr.hc', {
        url: "/hcmgr_hc",
        data: {pageTitle: '耗材'},
        templateUrl: "views/cmdb/zchc.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zchc.js?v=' + version]
                }]);
            }
        }
    }).state('hcmgr.hcin', {
        url: "/hcmgr_hcin",
        data: {pageTitle: '耗材入库单'},
        templateUrl: "views/cmdb/hcin.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/hcin.js?v=' + version]
                }]);
            }
        }
    }).state('hcmgr.hcout', {
        url: "/hcmgr_hcout",
        data: {pageTitle: '耗材出库单'},
        templateUrl: "views/cmdb/hcout.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/hcout.js?v=' + version]
                }]);
            }
        }
    }).state('hcmgr.hcsecstore', {
        url: "/hcmgr_hcsecstore",
        data: {pageTitle: '安全库存'},
        template: '<div ng-controller="hcsecStoreCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/hcsecstore.js?v=' + version]
                }]);
            }
        }
    }).state('hcmgr.hcdb', {
        url: "/hcmgr_hcdb",
        data: {pageTitle: '调拨单'},
        templateUrl: "views/cmdb/hcdb.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/hcdb.js?v=' + version]
                }]);
            }
        }
    });
    $stateProvider.state('zcmgr', {
        abstract: true,
        url: "/zcmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('zcmgr.zctz', {
        url: "/zcmgr_zctz",
        data: {pageTitle: '资产台账'},
        templateUrl: "views/cmdb/zcsearch.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcsearch.js?v=' + version]
                }]);
            }
        }
    }).state('zcmgr.zcqueryorg', {
        url: "/zcmgr_zcqueryorg",
        data: {pageTitle: '资产查询(组织)'},
        templateUrl: "views/cmdb/zcqueryorg.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcqueryorg.js?v=' + version]
                }]);
            }
        }
    })
        .state('zcmgr.zcqueryemployee', {
            url: "/zcmgr_zcqueryemployee",
            data: {pageTitle: '资产查询'},
            templateUrl: "views/cmdb/zcqueryemployee.html?v=" + version,
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([{
                        serie: true,
                        files: ['views/cmdb/zcqueryemployee.js?v=' + version]
                    }]);
                }
            }
        }).
    state('zcmgr.zcquerycat', {
        url: "/zcmgr_zcquerycat",
        data: {pageTitle: '资产查询(类别)'},
        templateUrl: "views/cmdb/zcquerycat.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcquerycat.js?v=' + version]
                }]);
            }
        }
    }).state('zcmgr.zcqueryloc', {
        url: "/zcmgr_zcqueryloc",
        data: {pageTitle: '资产查询(位置)'},
        templateUrl: "views/cmdb/zcqueryloc.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcqueryloc.js?v=' + version]
                }]);
            }
        }
    }).state('zcmgr.zcdj', {
        url: "/zcmgr_zcdj",
        data: {pageTitle: '资产登记'},
        templateUrl: "views/cmdb/zcdj.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcdj.js?v=' + version]
                }]);
            }
        }
    });


    $stateProvider.state('zcchange', {
        abstract: true,
        url: "/zcchange",
        templateUrl: "views/common/content.html?v=" + version
    }).state('zcchange.collectionreturn', {
        url: "zcchange_collectionreturn",
        data: {pageTitle: '资产领用&退库'},
        templateUrl: "views/cmdb/collectionreturn.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/collectionreturn.js?v=' + version]
                }]);
            }
        }
    }).state('zcchange.loanreturn', {
        url: "loanreturn",
        data: {pageTitle: '资产借用&归还'},
        templateUrl: "views/cmdb/loanreturn.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/loanreturn.js?v=' + version]
                }]);
            }
        }
    })
        .state('zcchange.zczy', {
            url: "/zcchange_zczy",
            data: {pageTitle: '资产调拨'},
            templateUrl: "views/cmdb/zcallocation.html?v=" + version,
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            files: ['views/cmdb/zcallocation.js?v=' + version]
                        }]);
            }
        }
    }).state('zcchange.changefinancial', {
        url: "/zcchange_cgcw",
        data: {pageTitle: '财务信息变更 '},
        templateUrl: "views/cmdb/cgcw.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/cgcw.js?v=' + version]
                }]);
            }
        }
    }).state('zcchange.changemaintenance', {
        url: "/zcchange_cgwb",
        data: {pageTitle: '维保信息变更 '},
        templateUrl: "views/cmdb/cgwb.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/cgwb.js?v=' + version]
                }]);
            }
        }
    }).state('zcchange.changephyzc', {
        url: "/zcchange_cgjb",
        data: {pageTitle: '实物信息变更 '},
        templateUrl: "views/cmdb/cgjb.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/cgjb.js?v=' + version]
                }]);
            }
        }
    }).state('zcchange.zctransfer', {
        url: "/zcchange_zctransfer",
        data: {pageTitle: '资产转移 '},
        templateUrl: "views/cmdb/change/tranfer.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/change/tranfer.js?v=' + version]
                }]);
            }
        }
    }).state('zcchange.zcbf', {
        url: "/zcchange_zcbf",
        data: {pageTitle: '资产报废'},
        templateUrl: "views/cmdb/zcbf.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zcbf.js?v=' + version]
                }]);
            }
        }
    }).state('zcchange.zchandle', {
        url: "/zcchange_zchandle",
        data: {pageTitle: '资产处置'},
        templateUrl: "views/cmdb/zchandle.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/zchandle.js?v=' + version]
                }]);
            }
        }
    })


    // cmdb
    $stateProvider.state('cf', {
        abstract: true,
        url: "/cf",
        templateUrl: "views/common/content.html?v=" + version
    }).state('cf.custom0', {
        url: "/cf_custom0?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom1', {
        url: "/cf_custom1?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom2', {
        url: "/cf_custom2?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom3', {
        url: "/cf_custom3?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom4', {
        url: "/cf_custom4?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom5', {
        url: "/cf_custom5?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom6', {
        url: "/cf_custom6?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom7', {
        url: "/cf_custom7?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom8', {
        url: "/cf_custom8?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    }).state('cf.custom9', {
        url: "/cf_custom9?categoryid",
        data: {pageTitle: ''},
        templateUrl: "views/cmdb/genericdev.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/genericdev.js?v=' + version]
                }]);
            }
        }
    })

    $stateProvider.state('report', {
        abstract: true,
        url: "/report",
        templateUrl: "views/common/content.html?v=" + version
    }).state('report.totalassets', {
        url: "/report_totalassets",
        data: {pageTitle: '资产总值汇总表'},
        template: '<div ng-controller="totalassetsCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/totalassets.js?v=' + version]
                }]);
            }
        }
    }).state('report.zcbfrep', {
        url: "/report_zcbfrep",
        data: {pageTitle: '资产报废汇总表'},
        template: '<div ng-controller="zcbfrepCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/zcBfreport.js?v=' + version]
                }]);
            }
        }
    }).state('report.departsummary', {
        url: "/report_departsummary",
        data: {pageTitle: '部门资产汇总'},
        template: '<div ng-controller="cmdbrepPartZcCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/partzc.js?v=' + version]
                }]);
            }
        }
    }).state('report.Taxonomy', {
        url: "/report_Taxonomy",
        data: {pageTitle: '分类使用汇总表'},
        template: '<div ng-controller="catusedreportCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/catused.js?v=' + version]
                }]);
            }
        }
    }).state('report.cleaninglist', {
        url: "/report_cleaninglist",
        data: {pageTitle: '清理清单'},
        templateUrl: "views/cmdb/rep/cleaninglist.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/cleaninglist.js?v=' + version]
                }]);
            }
        }
    }).state('report.catreport', {
        url: "/report_catreport",
        data: {pageTitle: '资产分类汇总表'},
        template: '<div ng-controller="catreportCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/catreport.js?v=' + version]
                }]);
            }
        }
    }).state('report.employeezc', {
        url: "/report_employeezc",
        data: {pageTitle: '员工资产汇总表'},
        // templateUrl: "views/cmdb/rep/catreport.html?v=" + version,
        template: '<div ng-controller="employeezcCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/rep/employeezc.js?v=' + version]
                }]);
            }
        }
    });
}





function renderJg(data, type, full) {
    var html = "";
    if (angular.isDefined(full.rackstr)) {
        html = html + full.rackstr + "-";
    }
    if (angular.isDefined(full.frame)) {
        html = html + full.frame;
    }
    return html;
}

function renderReview(data, type, full) {
    if (data == "reviewed") {
        return "已复核"
    } else {
        return "未复核"
    }
}

function renderWb(data, type, full) {
    if (angular.isDefined(full.wb)) {
        if (full.wb == "valid") {
            return "<span style=\"color:blue;font-weight:bold\">" + full.wbstr + "</span>"
        } else {
            return "<span style=\"color:red;font-weight:bold\">" + full.wbstr + "</span>"
        }
    } else {
        return "";
    }
}


function renderUfloTaskStatus(data, type, full) {
    if (data == "Created") {
        return "新建";
    } else if (data == "Ready") {
        return "准备"
    } else if (data == "Reserved") {
        return "保留"
    } else if (data == "InProgress") {
        return "进行中"
    } else if (data == "Completed") {
        return "已完成"
    } else if (data == "Suspended") {
        return "挂起"
    } else if (data == "Canceled") {
        return "取消"
    } else if (data == "Forwarded") {
        return "转发"
    } else if (data == "Rollback") {
        return "回滚"
    } else if (data == "Withdraw") {
        return "撤回"
    } else {
        return data;
    }
}


function loadOpt(modal_meta, gdicts) {
    var item = modal_meta.meta.item;
    console.log("LoadOpt,Item:", item);
    // 维保
    modal_meta.meta.tbOpt = gdicts.zcwbcomoute;
    if (angular.isDefined(gdicts.zcwbcomoute) && modal_meta.meta.tbOpt.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.wb_auto)) {
            for (var i = 0; i < gdicts.zcwbcomoute.length; i++) {
                if (modal_meta.meta.tbOpt[i].dict_item_id == item.wb_auto) {
                    modal_meta.meta.tbSel = modal_meta.meta.tbOpt[i];
                    break;
                }
            }
        } else {
            if (gdicts.zcwbcomoute.length > 0) {
                modal_meta.meta.tbSel = modal_meta.meta.tbOpt[0];
            }
        }
    }
    // 品牌
    modal_meta.meta.pinpOpt = gdicts.devbrand;
    if (angular.isDefined(gdicts.devbrand) && modal_meta.meta.pinpOpt.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.brand)) {
            for (var i = 0; i < gdicts.devbrand.length; i++) {
                if (modal_meta.meta.pinpOpt[i].dict_item_id == item.brand) {
                    modal_meta.meta.pinpSel = modal_meta.meta.pinpOpt[i];
                    break;
                }
            }
        } else {
            if (gdicts.devbrand.length > 0) {
                modal_meta.meta.pinpSel = modal_meta.meta.pinpOpt[0];
            }
        }
    }
    // 使用年限
    modal_meta.meta.uselifeOpt = gdicts.zcusefullife;
    if (angular.isDefined(gdicts.zcusefullife) && modal_meta.meta.uselifeOpt.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.usefullife)) {
            for (var i = 0; i < gdicts.zcusefullife.length; i++) {
                if (modal_meta.meta.uselifeOpt[i].dict_item_id == item.usefullife) {
                    modal_meta.meta.uselifeSel = modal_meta.meta.uselifeOpt[i];
                    break;
                }
            }
        } else {
            if (gdicts.zcusefullife.length > 0) {
                modal_meta.meta.uselifeSel = modal_meta.meta.uselifeOpt[0];
            }
        }
    }


    // 风险等级
    modal_meta.meta.riskOpt = gdicts.devrisk;
    if (angular.isDefined(gdicts.devrisk) && gdicts.devrisk.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.risk)) {
            for (var i = 0; i < gdicts.devrisk.length; i++) {
                if (gdicts.devrisk[i].dict_item_id == item.risk) {
                    modal_meta.meta.riskSel = gdicts.devrisk[i];
                    break;
                }
            }
        } else {
            if (gdicts.devrisk.length > 0) {
                modal_meta.meta.riskSel = gdicts.devrisk[0];
            }
        }
    }
    // 环境
    modal_meta.meta.envOpt = gdicts.devenv;
    if (angular.isDefined(gdicts.devenv) && gdicts.devenv.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.env)) {
            for (var i = 0; i < gdicts.devenv.length; i++) {
                if (gdicts.devenv[i].dict_item_id == item.env) {
                    modal_meta.meta.envSel = gdicts.devenv[i];
                    break;
                }
            }
        } else {
            if (gdicts.devenv.length > 0) {
            }
        }
    }
    // 状态
    modal_meta.meta.recycelOpt = gdicts.devrecycle;
    if (angular.isDefined(gdicts.devrecycle) && gdicts.devrecycle.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.recycle)) {
            for (var i = 0; i < gdicts.devrecycle.length; i++) {
                if (gdicts.devrecycle[i].dict_item_id == item.recycle) {
                    modal_meta.meta.recycelSel = gdicts.devrecycle[i];
                    break;
                }
            }
        } else {
            if (gdicts.devrecycle.length > 0) {
                modal_meta.meta.recycelSel = gdicts.devrecycle[0];
            }
        }
    }
    // 维保
    modal_meta.meta.wbOpt = gdicts.devwb;
    if (angular.isDefined(gdicts.devwb) && gdicts.devwb.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.wb)) {
            for (var i = 0; i < gdicts.devwb.length; i++) {
                if (gdicts.devwb[i].dict_item_id == item.wb) {
                    modal_meta.meta.wbSel = gdicts.devwb[i];
                    break;
                }
            }
        } else {
            if (gdicts.devwb.length > 0) {
                modal_meta.meta.wbSel = gdicts.devwb[0];
            }
        }
    }
    //仓库cmsetting_zccat
    modal_meta.meta.warehouseOpt = gdicts.warehouse;
    if (angular.isDefined(gdicts.warehouse) && gdicts.warehouse.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.warehouse)) {
            for (var i = 0; i < gdicts.warehouse.length; i++) {
                if (gdicts.warehouse[i].dict_item_id == item.warehouse) {
                    modal_meta.meta.warehouseSel = gdicts.warehouse[i];
                    break;
                }
            }
        } else {
            if (gdicts.warehouse.length > 0) {
                modal_meta.meta.warehouseSel = gdicts.warehouse[0];
            }
        }
    }
    // 区域
    modal_meta.meta.locOpt = gdicts.devdc;
    if (angular.isDefined(gdicts.devdc) && gdicts.devdc.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.loc)) {
            for (var i = 0; i < gdicts.devdc.length; i++) {
                if (gdicts.devdc[i].dict_item_id == item.loc) {
                    modal_meta.meta.locSel = gdicts.devdc[i];
                    break;
                }
            }
        } else {
            if (gdicts.devdc.length > 0) {
                modal_meta.meta.locSel = gdicts.devdc[0];
            }
        }
    }
    // 小类
    modal_meta.meta.typeOpt = gdicts.stype;
    if (angular.isDefined(gdicts.stype) && gdicts.stype.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.type)) {
            for (var i = 0; i < gdicts.stype.length; i++) {
                if (gdicts.stype[i].dict_item_id == item.type) {
                    modal_meta.meta.typeSel = gdicts.stype[i];
                    break;
                }
            }
        } else {
            if (gdicts.stype.length > 0) {
                modal_meta.meta.typeSel = gdicts.stype[0];
            }
        }
    }
    // 大类
    modal_meta.meta.classOpt = gdicts.btype;
    if (angular.isDefined(gdicts.btype) && gdicts.btype.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.class_id)) {
            for (var i = 0; i < gdicts.btype.length; i++) {
                if (gdicts.btype[i].dict_item_id == item.class_id) {
                    modal_meta.meta.classSel = gdicts.btype[i];
                    break;
                }
            }
        } else {
            if (gdicts.btype.length > 0) {
                modal_meta.meta.classSel = gdicts.btype[0];
            }
        }
    }
    //供应商
    modal_meta.meta.zcsupperOpt = gdicts.zcsupper;
    if (angular.isDefined(gdicts.zcsupper) && gdicts.zcsupper.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.supplier)) {
            for (var i = 0; i < gdicts.zcsupper.length; i++) {
                if (gdicts.zcsupper[i].dict_item_id == item.supplier) {
                    modal_meta.meta.zcsupperSel = gdicts.zcsupper[i];
                    break;
                }
            }
        } else {
            if (gdicts.zcsupper.length > 0) {
                modal_meta.meta.zcsupperSel = gdicts.zcsupper[0];
            }
        }
    }
    //资产来源
    modal_meta.meta.zcsourceOpt = gdicts.zcsource;
    if (angular.isDefined(gdicts.zcsource) && gdicts.zcsource.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.zcsource)) {
            for (var i = 0; i < gdicts.zcsource.length; i++) {
                if (gdicts.zcsource[i].dict_item_id == item.zcsource) {
                    modal_meta.meta.zcsourceSel = gdicts.zcsource[i];
                    break;
                }
            }
        } else {
            if (gdicts.zcsource.length > 0) {
                modal_meta.meta.zcsourceSel = gdicts.zcsource[0];
            }
        }
    }
    //维保供应商
    modal_meta.meta.zcwbsupperOpt = gdicts.zcwbsupper;
    if (angular.isDefined(gdicts.zcwbsupper) && gdicts.zcwbsupper.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.wbsupplier)) {
            for (var i = 0; i < gdicts.zcwbsupper.length; i++) {
                if (gdicts.zcwbsupper[i].dict_item_id == item.wbsupplier) {
                    modal_meta.meta.zcwbsupperSel = gdicts.zcwbsupper[i];
                    break;
                }
            }
        } else {
            if (gdicts.zcwbsupper.length > 0) {
                modal_meta.meta.zcwbsupperSel = gdicts.zcwbsupper[0];
            }
        }
    }

    //所属公司
    modal_meta.meta.belongcompOpt = gdicts.belongcomp;
    if (angular.isDefined(gdicts.belongcomp) && gdicts.belongcomp.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.belong_company_id)) {
            for (var i = 0; i < gdicts.belongcomp.length; i++) {
                if (gdicts.belongcomp[i].id == item.belong_company_id) {
                    modal_meta.meta.belongcompSel = gdicts.belongcomp[i];
                    break;
                }
            }
        } else {
            if (gdicts.belongcomp.length > 0) {
                modal_meta.meta.belongcompSel = gdicts.belongcomp[0];
            }
        }
    }
    //使用公司
    // modal_meta.meta.compOpt = gdicts.comp;
    // if (angular.isDefined(gdicts.comp) && gdicts.comp.length > 0) {
    //     if (angular.isDefined(item) && angular.isDefined(item.used_company_id)) {
    //         for (var i = 0; i < gdicts.comp.length; i++) {
    //             if (gdicts.comp[i].id == item.used_company_id) {
    //                 modal_meta.meta.compSel = gdicts.comp[i];
    //             }
    //         }
    //     } else {
    //         if (gdicts.comp.length > 0) {
    //           //  modal_meta.meta.compSel = gdicts.comp[0];
    //         }
    //     }
    // }
    // 部门
    modal_meta.meta.partOpt = gdicts.parts;
    if (angular.isDefined(gdicts.parts) && gdicts.parts.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.part_id)) {
            var partFlag="0";
            for (var i = 0; i < gdicts.parts.length; i++) {
                if (gdicts.parts[i].partid == item.part_id) {
                    modal_meta.meta.partSel = gdicts.parts[i];
                    partFlag="1";
                    break;
                }
            }
            if(partFlag=="1"&&angular.isDefined(item.used_userid)){
                var uarr=[];
                var u={};
                u.user_id=item.used_userid;
                u.name=item.used_username;
                uarr.push(u)
                modal_meta.meta.usedunameOpt = uarr;
                modal_meta.meta.usedunameSel = uarr[0];
            }
        }
    }
    // 机柜
    modal_meta.meta.jgOpt = gdicts.devrack;
    if (angular.isDefined(gdicts.devrack) && gdicts.devrack.length > 0) {
        if (angular.isDefined(item) && angular.isDefined(item.rack)) {
            for (var i = 0; i < gdicts.devrack.length; i++) {
                if (gdicts.devrack[i].dict_item_id == item.rack) {
                    modal_meta.meta.jgSel = gdicts.devrack[i];
                }
            }
        } else {
            if (gdicts.devrack.length > 0) {
            }
        }
    }
}

// 流程-部分-审批
//$rootScope.flowpagetype
//$rootScope.flowbusid
//$rootScope.flowtaskid
function flowapprovalCommonCtl($rootScope, $scope, $http, notify) {
    console.log($rootScope.flowpagetype)
    console.log($rootScope.flowbusid)
    console.log($rootScope.flowtaskid)
    $scope.hideapproval = true;
    $scope.spsuggest = "";
    $scope.tt="tog "
    if (angular.isDefined($rootScope.flowpagetype) && $rootScope.flowpagetype == "approval") {
        $scope.hideapproval = false;
    }
    $scope.agreen = function () {


        if ($scope.spsuggest.length == 0) {
            $scope.spsuggest = "同意";
        }
        if ($scope.spsuggest.length > 248) {
            notify({
                message: "已超过248,请缩减字数。"
            });
            return;
        }


        $http
            .post($rootScope.project + "/api/zc/flow/completeTask.do",
                {taskId: $rootScope.flowtaskid, opinion: $scope.spsuggest}).success(function (res) {
            if (res.success) {
                $scope.$parent.$parent.windowclose();
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.rollback = function () {
        if ($scope.spsuggest.length == 0) {
            $scope.spsuggest = "退回";
        }
        if ($scope.spsuggest.length > 248) {
            notify({
                message: "已超过248,请缩减字数。"
            });
            return;
        }
        $http
            .post($rootScope.project + "/api/zc/flow/rollback.do",
                {taskId: $rootScope.flowtaskid, opinion: $scope.spsuggest}).success(function (res) {
            if (res.success) {
                $scope.$parent.$parent.windowclose();
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.refuse = function () {
        if ($scope.spsuggest.length == 0) {
            $scope.spsuggest = "拒绝";
        }
        if ($scope.spsuggest.length > 248) {
            notify({
                message: "已超过248,请缩减字数。"
            });
            return;
        }
        $http
            .post($rootScope.project + "/api/zc/flow/refuseTaskForwardEnd.do",
                {taskId: $rootScope.flowtaskid, opinion: $scope.spsuggest}).success(function (res) {
            if (res.success) {
                $scope.$parent.$parent.windowclose();
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
}

// 流程-部分-意见
//$rootScope.flowbusid
function flowsuggestCommonCtl($rootScope, $scope, $http, notify, $window) {
    $scope.hidesuggest = true;
    $scope.taskinfodata = [];
    var processinstanceid = "";
    if (angular.isDefined($rootScope.flowbusid)) {
        $http.post($rootScope.project + "/api/zc/flow/queryFlowTaskInfoByBusid.do",
            {"busid": $rootScope.flowbusid}).success(function (res) {
            if (res.success) {
                if (angular.isDefined(res.data.taskinfodata) && res.data.taskinfodata.length > 0) {
                    $scope.hidesuggest = false;
                    $scope.taskinfodata = res.data.taskinfodata;
                    processinstanceid = res.data.processinstanceid;
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.flowreview = function () {
        var url = $rootScope.project + "uflo/diagram?processInstanceId=" + processinstanceid;
        var win = $window.open(url, "_bank", "fullscreen:no,menubar:no,status:no,location:no,menubar:no,height=500, width=800")
    }
}


function modalreviewProcessCtl(meta, $rootScope, $window, $scope, $http, $timeout, $uibModalInstance) {
    var url = $rootScope.project + "uflo/diagram?processKey=" + meta.ptplkey;
    $scope.url = url;
    if (angular.isDefined(meta.form)) {
        $http
            .post(
                $rootScope.project
                + "/api/form/sysForm/selectById.do",
                {
                    id: meta.form
                })
            .success(
                function (res) {
                    if (res.success) {
                        if (angular.isDefined(res.data.ct)) {
                            $timeout(function () {
                                var jd = decodeURI(res.data.ct);
                                let jsonData = angular.fromJson(jd);
                                console.log(jsonData);
                                vm = new Vue({
                                    el: '#app',
                                    data: {
                                        jsonData
                                    },
                                    mounted() {
                                        this.init()
                                    },
                                    methods: {
                                        init() {
                                            console.log(this);
                                        },
                                        handleSubmit(p) {
                                            // 通过表单提交按钮触发，获取promise对象
                                            p().then(res => {
                                                // 获取数据成功
                                                alert(JSON.stringify(res))
                                            })
                                                .catch(err => {
                                                    console.log(err, '校验失败')
                                                })
                                        },
                                        getData() {
                                            // 通过函数获取数据
                                            this.$refs.kfb.getData().then(res => {
                                                // 获取数据成功
                                                alert(JSON.stringify(res))
                                            })
                                                .catch(err => {
                                                    console.log(err, '校验失败')
                                                })
                                        }
                                    }
                                })
                            }, 800);
                        }
                    }
                })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}


function modalcmdbdtlCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                         DTColumnBuilder, $compile, $window) {
    $scope.item = {};
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}
    $scope.attachdown = function (faultid) {
        $http.post($rootScope.project + "/api/base/res/queryResFaultById.do", {
            id: faultid
        }).success(function (res) {
            if (res.success) {
                // $window
                if (res.data.attachdata.length > 0) {
                    for (var i = 0; i < res.data.attachdata.length; i++) {
                        var objectUrl = $rootScope.project + "/api/file/filedown.do?id=" + res.data.attachdata[i].fileid;
                        $window.open(objectUrl)
                    }
                }
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }
    $scope.dtColumns = [
        DTColumnBuilder.newColumn('create_time').withTitle('操作时间')
            .withOption('sDefaultContent', '').withOption('width', '30'),
        DTColumnBuilder.newColumn('fuuid').withTitle('报修编号').withOption(
            'sDefaultContent', '').withOption('width', '30'),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '').withOption('width', '30')
    ]
    $scope.dtOptions2 = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            })
    $scope.dtInstance2 = {}

    function renderType(data, type, full) {
        if (data == "DB") {
            return "资产调拨";
        } else if (data == "LY") {
            return "资产领用"
        } else if (data == "ADD") {
            return "资产新增"
        } else if (data == "UPDATE") {
            return "资产更新"
        } else if (data == "TK") {
            return "资产退库"
        } else if (data == "JY") {
            return "资产借用"
        } else if (data == "GH") {
            return "资产归还"
        } else if (data == "ZJ") {
            return "资产折旧"
        } else if (data == "BF") {
            return "资产报废"
        } else if (data == "CGCW") {
            return "财务信息变更"
        } else if (data == "CGWB") {
            return "维保信息变更"
        } else if (data == "CGJB") {
            return "基本信息变更"
        } else {
            return data;
        }
    }

    $scope.dtColumns2 = [
        DTColumnBuilder.newColumn('create_time').withTitle('操作时间')
            .withOption('sDefaultContent', '').withOption('width', '30'),
        DTColumnBuilder.newColumn('busuuid').withTitle('单据编号').withOption(
            'sDefaultContent', '').withOption('width', '30'),
        DTColumnBuilder.newColumn('type').withTitle('处理类型').withOption(
            'sDefaultContent', '').withOption('width', '30').renderWith(renderType),
        DTColumnBuilder.newColumn('ct').withTitle('处理内容').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('create_uname').withTitle('操作人').withOption(
            'sDefaultContent', '')
    ]
    if (angular.isDefined(meta.id)) {
        // 加载数据
        $http.post($rootScope.project + "/api/base/res/queryResAllById.do", {
            id: meta.id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data.data;
                var extdata = [];
                var extdataitem = [];
                for (var i = 0; i < res.data.extattr.length; i++) {
                    if (i % 4 == 3 && i > 0) {
                        extdataitem.push(res.data.extattr[i]);
                        extdata.push(extdataitem);
                        extdataitem = [];
                    } else {
                        extdataitem.push(res.data.extattr[i]);
                    }
                }
                if (extdataitem.length > 0) {
                    extdata.push(extdataitem);
                }
                $scope.extdata = extdata;
                $scope.dtOptions.aaData = res.data.faultdata;
                $scope.dtOptions2.aaData = res.data.updatadata;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

//##########################################资产选择##########################################//
function modal_common_ZcListCtl($timeout, $localStorage, notify, $log, $uibModal,
                                $uibModalInstance, $scope, $http, $rootScope, DTOptionsBuilder,
                                DTColumnBuilder, $compile, data) {
    // type:one|many
    // datatype: LY|
    // usedcompid
    // belongcompid
    $scope.data = data;
    $scope.ctl = {"showusefullife": false};
    if (angular.isDefined(data.showusefullife)) {
        $scope.ctl.showusefullife = data.showusefullife;
    }
    console.log("data:" + data);
    $scope.partOpt = [];
    $scope.partSel = {};
    $scope.belongcompOpt = [];
    $scope.belongcompSel = {};
    $scope.areaOpt = [];
    $scope.areaSel = {};
    $scope.recycleOpt = [];
    $scope.recycleSel = {};
    $scope.zcusefullifeOpt = [];
    $scope.zcusefullifeSel = {};
    var gdicts = {};
    var dicts = "devrecycle,devdc,zcusefullife";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            uid: "zclistmodaldata",
            dicts: dicts,
            parts: "Y",
            comp: "Y"
        })
        .success(
            function (res) {
                if (res.success) {
                    gdicts = res.data;
                    angular.copy(gdicts.zcusefullife, $scope.zcusefullifeOpt);
                    $scope.zcusefullifeOpt.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.zcusefullifeSel = $scope.zcusefullifeOpt[0];
                    angular.copy(gdicts.devrecycle, $scope.recycleOpt);
                    $scope.recycleOpt.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.recycleSel = $scope.recycleOpt[0];

                    angular.copy(gdicts.comp, $scope.belongcompOpt);
                    $scope.belongcompOpt.unshift({
                        id: "all",
                        name: "全部"
                    });
                    $scope.belongcompSel = $scope.belongcompOpt[0];
                    angular.copy(gdicts.parts, $scope.partOpt);
                    $scope.partOpt.unshift({
                        partid: "all",
                        name: "全部"
                    });
                    $scope.partSel = $scope.partOpt[0];
                    angular.copy(gdicts.devdc, $scope.areaOpt);
                    $scope.areaOpt.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.areaSel = $scope.areaOpt[0];
                    flush();
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    if (!angular.isDefined(data.type)) {
        data.type = "many"
    }
    $scope.search = "";
    // 分类
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 500)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        })
    $scope.dtInstance = {}

    function renderName(data, type, full) {
        var html = full.model;
        return html;
    }

    function renderJg(data, type, full) {
        var html = full.rackstr + "-" + full.frame;
        return html;
    }

    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withselect');

    function flush() {
        var ps = {};
        ps.id = data.id;
        ps.type = data.type;
        ps.datarange = data.datarange;
        if (angular.isDefined($scope.search)) {
            ps.search = $scope.search;
        }
        if (angular.isDefined($scope.belongcompSel.id) && $scope.belongcompSel.id != 'all') {
            ps.belongcomp = $scope.belongcompSel.id;
        }
        if (angular.isDefined($scope.partSel.partid) && $scope.partSel.partid != 'all') {
            ps.part = $scope.partSel.partid;
        }
        if (angular.isDefined($scope.recycleSel.dict_item_id) && $scope.recycleSel.dict_item_id != 'all') {
            ps.recycle = $scope.recycleSel.dict_item_id;
        }
        if (angular.isDefined($scope.areaSel.dict_item_id) && $scope.areaSel.dict_item_id != 'all') {
            ps.loc = $scope.areaSel.dict_item_id;
        }
        ps.category = 3;
        //如果传入使用公司,则强制使用传入的参数
        // if (angular.isDefined(data.usedcompid)) {
        //     ps.comp = data.usedcompid;
        // }
        if (angular.isDefined(data.belongcompid)) {
            ps.belongcomp = data.belongcompid;
        }
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    $scope.query = function () {
        flush()
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        var dtdata = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (dtdata.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        }
        if (data.type == "one") {
            if (dtdata.length > 1) {
                notify({
                    message: "请最多选择一项"
                });
            }
            var item = $scope.dtOptions.aaData[dtdata[0]];
            if (angular.isDefined(item)) {
                $uibModalInstance.close(item);
            }
            return;
        }
        if (data.type == "many") {
            var res = [];
            for (var i = 0; i < dtdata.length; i++) {
                var item = $scope.dtOptions.aaData[dtdata[i]];
                res.push(item);
            }
            if (angular.isDefined(res)) {
                $uibModalInstance.close(res);
            }
            return;
        }
    }
}

//资产表单选择
function modalFormListSelCtl($timeout, $localStorage, notify, $log, $uibModal,
                             $window, $uibModalInstance, $scope, $http, $rootScope,
                             DTOptionsBuilder, DTColumnBuilder, $compile, data) {
    $scope.typeOpt = [];
    $scope.typeSel = "";
    var formtype = [];
    $http.post($rootScope.project + "/api/ctCategory/ext/selectListByRoot.do",
        {
            root: "6"
        }).success(function (res) {
        if (res.success) {
            $scope.typeOpt = res.data;
            if ($scope.typeOpt.length > 0) {
                $scope.typeSel = $scope.typeOpt[0];
                flush();
            }
        } else {
            notify({
                message: res.message
            });
        }
    });
    // 分类
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 200)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        })
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var html = "";
        return html;
    }

    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle("").withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('id').withTitle('编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createTime').withTitle('创建时间').withOption(
            'sDefaultContent', '')
    ]

    function flush() {
        var ps = {};
        ps.owner = $scope.typeSel.id;
        $http.post(
            $rootScope.project + "/api/form/sysForm/ext/selectByOwner.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    $scope.query = function () {
        flush()
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function getSelectRow() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1) {
            notify({
                message: "请最多选择一项"
            });
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    $scope.review = function () {
        var obj = getSelectRow();
        if (angular.isDefined(obj)) {
            $window
                .open($rootScope.project
                    + "/console/views/system/form/formdesign.html?id="
                    + obj.id)
        }
    }
    $scope.sure = function () {
        var obj = getSelectRow();
        if (angular.isDefined(obj)) {
            $uibModalInstance.close(obj);
        }
    }
}

//资产流程选择
function modalFlowSelectCtl($timeout, $localStorage, notify, $log, $uibModal,
                            $uibModalInstance, $scope, $http, $rootScope, DTOptionsBuilder,
                            DTColumnBuilder, $compile) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withDOM('frtlpi').withPaginationType('simple').withDisplayLength(
            50).withOption("ordering", false).withOption("responsive",
            false).withOption("searching", false).withOption('scrollY',
            300).withOption('scrollX', true).withOption(
            'bAutoWidth', true).withOption('scrollCollapse', true)
        .withOption('paging', false).withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });

    function stateChange(iColumn, bVisible) {
    }

    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }

    function renderStatus(data, type, full) {
        var res = "";
        if (data == "normal") {
            res = "正常";
        } else if (data == "stop") {
            res = "停用";
        } else {
            res = data;
        }
        return res;
    }

    function renderType(data, type, full) {
        var res = "";
        if (data == "form") {
            res = "表单模式";
        } else if (data == "withoutform") {
            res = "无表单模式";
        } else {
            res = data;
        }
        return res;
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml)
        .withClass('select-checkbox checkbox_center').renderWith(
            function () {
                return ""
            }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('ID')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('名称')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('type').withTitle('类型')
        .withOption('sDefaultContent', '').renderWith(renderType)),
        $scope.dtColumns.push(DTColumnBuilder.newColumn('status').withTitle('状态')
            .withOption('sDefaultContent', '').renderWith(renderStatus)),
        $scope.dtColumns.push(DTColumnBuilder.newColumn('ptplkey')
            .withTitle('流程模版').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('formname').withTitle('表单')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('createTime').withTitle('创建时间')
        .withOption('sDefaultContent', ''));
    $scope.catRootOpt = [];
    $scope.catRootSel = "";
    $scope.item = {};
    var ps = {};
    ps.ids = angular.toJson([5]);
    $http
        .post($rootScope.project + "/api/ctCategoryRoot/ext/selectList.do",
            ps).success(function (res) {
        if (res.success) {
            $scope.catRootOpt = res.data;
            if ($scope.catRootOpt.length > 0) {
                $scope.catRootSel = $scope.catRootOpt[0];
                flushTree($scope.catRootSel.id)
            }
        } else {
            notify({
                message: res.message
            });
        }
    });
    // 树配置
    $scope.treeConfig = {
        core: {
            multiple: false,
            animation: true,
            error: function (error) {
                $log.error('treeCtrl: error from js tree - '
                    + angular.toJson(error));
            },
            check_callback: true,
            worker: true
        },
        loading: "加载中……",
        ui: {
            theme_name: "classic" // 设置皮肤样式
        },
        rules: {
            type_attr: "rel", // 设置节点类型
            valid_children: "root" // 只有root节点才能作为顶级结点
        },
        callback: {
            onopen: function (node, tree_obj) {
                return true;
            }
        },
        types: {
            "default": {
                icon: 'glyphicon glyphicon-th'
            },
            root: {
                icon: 'glyphicon glyphicon-home'
            },
            "node": {
                "icon": "glyphicon glyphicon-tag"
            },
            "category": {
                "icon": "glyphicon glyphicon-equalizer"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'changed'],
        contextmenu: {
            items: {}
        }
    }
    $scope.addNewNode = function () {
        $scope.treeData.push({
            id: (newId++).toString(),
            parent: $scope.newNode.parent,
            text: $scope.newNode.text
        });
    };
    $scope.modelChanges = function (t) {
        return true;
    }
    $scope.curSelNode = "";
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
        // 展开所有节点
        $scope.tree.open_all();
        // 响应节点变化
        $scope.treeInstance.on("changed.jstree", function (e, data) {
            if (data.action == "select_node") {
                // 加载数据
                var snodes = $scope.tree.get_selected();
                if (snodes.length == 1) {
                    var node = snodes[0];
                    $scope.curSelNode = node;
                    flush();
                }
            }
        });
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function flushTree(id) {
        $http
            .post(
                $rootScope.project
                + "/api/ctCategroy/queryCategoryTreeList.do", {
                    root: id
                }).success(function (res) {
            if (res.success) {
                $scope.ignoreChanges = true;
                $scope.treeData = angular.copy(res.data);
                $scope.treeConfig.version++;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    $scope.query = function () {
        flushTree($scope.catRootSel.id);
    }

    function flush() {
        $http.post(
            $rootScope.project
            + "/api/flow/sysProcessDef/ext/selectList.do", {
                owner: $scope.curSelNode
            }).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    function getSelectRow() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1) {
            notify({
                message: "请最多选择一项"
            });
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    $scope.sure = function () {
        var item = getSelectRow();
        if (angular.isDefined(item) && angular.isDefined(item.id)) {
            $uibModalInstance.close(item);
        }
    };
}

//流程实例选择
function modalFlowInstanceListSelCtl($timeout, $localStorage, notify, $log, $uibModal,
                                     $uibModalInstance, $scope, $http, $rootScope, DTOptionsBuilder,
                                     DTColumnBuilder, $compile) {
    // 分类
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 200)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        })
    $scope.dtInstance = {}

    function renderName(data, type, full) {
        var html = full.model;
        return html;
    }

    function renderJg(data, type, full) {
        var html = full.rackstr + "-" + full.frame;
        return html;
    }

    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle("").withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('id').withTitle('编号').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('name').withTitle('流程模版').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('key').withTitle('流程实例').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('version').withTitle('版本号').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('createDate').withTitle('创建时间')
            .withOption('sDefaultContent', '')
            .withOption('width', '30')]

    function flush() {
        var ps = {}
        ps.pageIndex = 1;
        ps.pageSize = 100;
        $http.post($rootScope.project + "uflo/central/loadProcess", ps)
            .success(function (res) {
                $scope.dtOptions.aaData = res.result;
            })
    }

    $scope.query = function () {
        flush()
    }
    flush();
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function getSelectRow() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1) {
            notify({
                message: "请最多选择一项"
            });
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    $scope.sure = function () {
        var id = getSelectRow();
        if (angular.isDefined(id)) {
            $uibModalInstance.close(id);
        }
    }
}

//##########################################报修处理窗口##########################################//
function modaldevfaultCtl($timeout, $localStorage, notify, $log, $uibModal,
                          $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                          DTColumnBuilder, $compile) {
    //type:detail,modify,add
    $scope.ctl = {};
    $scope.ctl.fprocessuser = false;
    $scope.ctl.fprocesstime = false;
    $scope.ctl.status = false;
    $scope.ctl.fmoney = false;
    $scope.ctl.freason = false;
    $scope.ctl.ffile = false;
    $scope.ctl.fmark = false;
    $scope.ctl.chosenzcbtn = false;
    $scope.ctl.surebtn = false;
    if (meta.actiontype == "detail") {
        $scope.ctl.fprocessuser = true;
        $scope.ctl.fprocesstime = true;
        $scope.ctl.status = true;
        $scope.ctl.fmoney = true;
        $scope.ctl.freason = true;
        $scope.ctl.fmark = true;
        $scope.ctl.ffile = true;
        $scope.ctl.chosenzcbtn = true;
        $scope.ctl.surebtn = true;
    } else if (meta.actiontype == "modify") {
        $scope.ctl.status = true;
        $scope.ctl.chosenzcbtn = true;
    }
    $scope.statusOpt = [{id: "underrepair", name: "维修中"}, {id: "finish", name: "已完成"}];
    $scope.statusSel = $scope.statusOpt[0];
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        })
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');
    $scope.dtOptions.aaData = [];
    $scope.data = {};
    $scope.data.mark = "";
    $scope.data.reason = "";
    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 20000,
        paramName: "file",
        maxThumbnailFilesize: 5,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 5,
        maxFiles: 5,
        dictDefaultMessage: "点击上传图片",
        acceptedFiles: "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        init: function () {
            $scope.myDropzone = this; // closure
        }
    };
    $scope.selectzc = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "BX";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
    if (angular.isDefined(meta.id)) {
        //获取数据
        $http.post($rootScope.project + "/api/zc/resRepair/ext/selectById.do",
            {id: meta.id}).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data.items;
                $scope.data = res.data;
                if (res.data.fstatus == "cancel") {
                    $scope.statusOpt.push({id: "underrepair", name: "作废"});
                    $scope.statusSel = $scope.statusOpt[2];
                } else {
                    for (var i = 0; i < $scope.statusOpt.length; i++) {
                        if (res.data.fstatus == $scope.statusOpt[i].id) {
                            $scope.statusSel = $scope.statusOpt[i];
                        }
                    }
                }
                $timeout(function () {
                    var files = res.data.files;
                    for (var i = 0; i < files.length; i++) {
                        var iid = files[i].fileid
                        var mockFile = {
                            name: "主图",
                            uuid: iid,
                            href: $rootScope.project
                                + "/api/file/filedown.do?id="
                                + iid,
                            url: $rootScope.project
                                + "/api/file/filedown.do?id="
                                + iid,
                            status: "success",
                            accepted: true,
                            type: 'image/png'
                        };
                        $scope.myDropzone.emit("addedfile", mockFile);
                        $scope.myDropzone.files.push(mockFile);
                        // manually
                        $scope.myDropzone.createThumbnailFromUrl(
                            mockFile, $rootScope.project
                            + "/api/file/filedown.do?id="
                            + iid);
                        $scope.myDropzone.emit("complete", mockFile);
                    }
                }, 500);
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        // 产品图片
        var file = "";
        for (var i = 0; i < $scope.myDropzone.files.length; i++) {
            var id = getUuid();
            // 判断,已经上传的不在上传
            if (typeof ($scope.myDropzone.files[i].uuid) == "undefined") {
                $scope.myDropzone.options.url = $rootScope.project
                    + '/api/file/fileupload.do?uuid=' + id
                    + '&bus=file&interval=10000&bus=file';
                $scope.myDropzone.uploadFile($scope.myDropzone.files[i])
            } else {
                id = $scope.myDropzone.files[i].uuid;
            }
            file = file + id + "#";
        }
        $scope.data.files = file
        $scope.data.items = angular.toJson($scope.dtOptions.aaData);
        $scope.data.fstatus = $scope.statusSel.id;
        $http.post($rootScope.project + "/api/zc/resRepair/ext/insertOrUpdate.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            }
            notify({
                message: res.message
            });
        })
    }
    //是否ids传入
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}

//##########################################维保变更处理窗口##########################################//
function zccgwbSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                       DTColumnBuilder, $compile, $confirm) {
    $scope.ctl = {processuser: false};
    $scope.item = {};
    $scope.adminuserOpt = meta.dict.partusers;
    $scope.adminuserSel = "";

    function selectnall() {
        $scope.item.twbstatus = false;
        $scope.item.twbsupplierstatus = false;
        $scope.item.twbautostatus = false;
        $scope.item.twbctstatus = false;
        $scope.item.twboutdatestatus = false;
        $scope.item.twbautostatus = false;
    }

    function selectall() {
        $scope.item.twbstatus = true;
        $scope.item.twbsupplierstatus = true;
        $scope.item.twbautostatus = true;
        $scope.item.twbctstatus = true;
        $scope.item.twboutdatestatus = true;
        $scope.item.twbautostatus = true;
    }

    selectnall();
    $scope.selectall = function () {
        selectall();
    }
    $scope.selectnall = function () {
        selectnall();
    }
    $scope.date = {
        wboutdate: moment()
    }
	$scope.date = {
        bfoutdate: moment()
    }
    if ($scope.adminuserOpt.length > 0) {
        $scope.adminuserSel = $scope.adminuserOpt[0];
        if (angular.isDefined($rootScope.dt_sys_user_info)) {
            for (var i = 0; i < $scope.adminuserOpt.length; i++) {
                if ($rootScope.dt_sys_user_info.userId == $scope.adminuserOpt[i].user_id) {
                    $scope.adminuserSel = $scope.adminuserOpt[i];
                    $scope.ctl.processuser = true;
                    break;
                }
            }
        }
    }
    $scope.compOpt = meta.dict.zcwbsupper;
    $scope.compSel = "";
    if ($scope.compOpt.length > 0) {
        $scope.compSel = $scope.compOpt[0];
    }
    $scope.statusOpt = meta.dict.devwb;
    $scope.statusSel = "";
    if ($scope.statusOpt.length > 0) {
        $scope.statusSel = $scope.statusOpt[0];
    }

    $scope.wbautoOpt = meta.dict.zcwbcomoute;
    $scope.wbautoSel = {}
    if ($scope.wbautoOpt.length > 0) {
        $scope.wbautoSel = $scope.wbautoOpt[0];
    }


    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };


    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });
    $scope.dtInstance = {}
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span>   ";
        acthtml = acthtml + " <span ng-click=\"filldata('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">填充</span>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function fillresdata(id) {
        var data = {};
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                data = $scope.dtOptions.aaData[i];
                break;
            }
        }
        $scope.compSel = "";
        $scope.wbautoSel = "";
        $scope.statusSel = "";
        $scope.item.twbct = "";
        if (angular.isDefined(data.wbct)) {
            $scope.item.twbct = data.wbct;
        }
        if (angular.isDefined(data.wbout_datestr)) {
            $scope.date.wboutdate = moment(data.wbout_datestr);
        }
		if (angular.isDefined(data.bfout_datestr)) {
            $scope.date.bfoutdate = moment(data.bfout_datestr);
        }
        if (angular.isDefined(data.wbsupplier)) {
            for (var i = 0; i < $scope.compOpt.length; i++) {
                if (data.wbsupplier == $scope.compOpt[i].dict_item_id) {
                    $scope.compSel = $scope.compOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.wb_auto)) {
            for (var i = 0; i < $scope.wbautoOpt.length; i++) {
                if (data.wb_auto == $scope.wbautoOpt[i].dict_item_id) {
                    $scope.wbautoSel = $scope.wbautoOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.wb)) {
            for (var i = 0; i < $scope.statusOpt.length; i++) {
                if (data.wb == $scope.statusOpt[i].dict_item_id) {
                    $scope.statusSel = $scope.statusOpt[i];
                    break;
                }
            }
        }
    }

    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '100').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保供应商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wb_autostr').withTitle('脱保计算').withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        $scope.item.twboutdate = $scope.date.wboutdate.format('YYYY-MM-DD');
        $scope.item.twbsupplier = $scope.compSel.dict_item_id;
        $scope.item.twbauto = $scope.wbautoSel.dict_item_id;
        $scope.item.twb = $scope.statusSel.dict_item_id;
        $scope.item.processuserid = $scope.adminuserSel.user_id;
        $scope.item.processusername = $scope.adminuserSel.name;
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        $confirm({
            text: '是否确定变更?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resCMaintenance/ext/insert.do",
                    $scope.item).success(function (res) {
                    if (res.success) {
                        $uibModalInstance.close('OK');
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.review = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "CG";
        mdata.showusefullife = "true";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
    $scope.filldata = function (id) {
        fillresdata(id);
    };
    //是否ids传入
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                    if (res.data.length == 1) {
                        fillresdata(res.data[0].id);
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }


}


//##########################################资产借用##########################################//
function loanSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                     DTColumnBuilder, $compile, $confirm) {
    $scope.item = {};
    $scope.ctl = {jyprocessuser: false};
    $scope.useduserOpt = meta.dict.partusers;
    $scope.useduserSel = "";
    if ($scope.useduserOpt.length > 0) {
        $scope.useduserSel = $scope.useduserOpt[0];
    }
    $scope.processuserOpt = meta.dict.partusers;
    $scope.processuserSel = "";
    if ($scope.processuserOpt.length > 0) {
        $scope.processuserSel = $scope.processuserOpt[0];
        if (angular.isDefined($rootScope.dt_sys_user_info)) {
            for (var i = 0; i < $scope.processuserOpt.length; i++) {
                if ($rootScope.dt_sys_user_info.userId == $scope.processuserOpt[i].user_id) {
                    $scope.processuserSel = $scope.processuserOpt[i];
                    $scope.ctl.jyprocessuser = true;
                    break;
                }
            }
        }
    }
    $scope.date = {
        busdate: moment(),
        returndate: moment(),
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });
    $scope.dtInstance = {}
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span>   ";
        return acthtml;
    }

    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保供应商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    // dtColumns.push(DTColumnBuilder.newColumn('wb_autostr').withTitle('脱保计算')
    //     .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        $scope.item.busdate = $scope.date.busdate.format('YYYY-MM-DD');
        $scope.item.returndate = $scope.date.returndate.format('YYYY-MM-DD');
        $scope.item.lruserid = $scope.useduserSel.user_id;
        $scope.item.lrusername = $scope.useduserSel.name;
        $scope.item.lprocessuserid = $scope.processuserSel.user_id;
        $scope.item.lprocessusername = $scope.processuserSel.name;
        $scope.item.bustype = "jy";
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        $confirm({
            text: '是否确定借用?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resLoanreturn/ext/zcjy.do",
                    $scope.item).success(function (res) {
                    if (res.success) {
                        $uibModalInstance.close('OK');
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.review = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "JY";
        mdata.showusefullife = "true";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
    //是否ids传入
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}

//##########################################资产领用##########################################//
function collectionSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                           $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                           DTColumnBuilder, $compile, $confirm) {
    $scope.item = {};
    $scope.useduserOpt = [];
    $scope.useduserSel = "";
    $scope.compOpt = meta.dict.comp;
    $scope.compSel = "";
    $scope.partOpt = meta.dict.parts;
    $scope.partSel = "";

    $scope.date = {
        busdate: moment(),
        returndate: moment().add(365, "days")
    }
    // if ($scope.useduserOpt.length > 0) {
    //     $scope.useduserSel = $scope.useduserOpt[0];
    // }
    if ($scope.compOpt.length > 0) {
        $scope.compSel = $scope.compOpt[0];
    }
    if ($scope.partOpt.length > 0) {
        $scope.partSel = $scope.partOpt[0];
    }
    // if ($scope.locOpt.length > 0) {
    //     $scope.locSel = $scope.locOpt[0];
    // }

    $scope.$watch('partSel', function (newValue, oldValue) {
        if (angular.isDefined(newValue) && angular.isDefined(newValue.partid)) {

            $scope.useduserOpt=[];
            $scope.useduserSel={}

                $http.post(
                    $rootScope.project
                    + "/api/hrm/queryEmplByOrg.do", {
                        node_id: newValue.partid
                    }).success(function (result) {
                    if (result.success) {
                        $scope.useduserOpt= result.data;
                        if($scope.useduserOpt.length>0){
                            $scope.useduserSel=$scope.useduserOpt[0];
                        }

                    }
                });

        }
    });
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });
    $scope.dtInstance = {}
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span>   ";
        return acthtml;
    }

    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保供应商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    // dtColumns.push(DTColumnBuilder.newColumn('wb_autostr').withTitle('脱保计算')
    //     .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        $scope.item.busdate = $scope.date.busdate.format('YYYY-MM-DD');
        $scope.item.returndate = $scope.date.returndate.format('YYYY-MM-DD');
        $scope.item.cruserid = $scope.useduserSel.user_id;
        $scope.item.crusername = $scope.useduserSel.name;
        // $scope.item.processuserid = $scope.useduserSel.user_id;
        // $scope.item.processusername = $scope.useduserSel.name;
        // $scope.item.tusedcompanyid = $scope.compSel.id;
        $scope.item.tpartid = $scope.partSel.partid;
        $scope.item.tpartname = $scope.partSel.name;
        $scope.item.tuseduserid = $scope.useduserSel.user_id;
        $scope.item.tusedusername = $scope.useduserSel.name;
        // $scope.item.tloc = $scope.locSel.dict_item_id;
        $scope.item.bustype = "LY";
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        $confirm({
            text: '是否确定领用?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resCollectionreturn/ext/insertOrUpdate.do",
                    $scope.item).success(function (res) {
                    if (res.success) {
                        $uibModalInstance.close('OK');
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.review = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "LY";
        mdata.showusefullife = "true";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}

//##########################################资产退库##########################################//
function tkSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                   $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                   DTColumnBuilder, $compile, $confirm) {
    $scope.item = {};
    $scope.userOpt = meta.dict.partusers;
    $scope.userSel = "";
    $scope.compOpt = meta.dict.comp;
    $scope.compSel = "";
    // $scope.partOpt=meta.dict.parts;
    // $scope.partSel="";
    // $scope.locOpt = meta.dict.devdc
    // $scope.locSel = "";
    $scope.date = {
        rreturndate: moment()
    }
    if ($scope.userOpt.length > 0) {
        $scope.userSel = $scope.userOpt[0];
    }
    if ($scope.compOpt.length > 0) {
        $scope.compSel = $scope.compOpt[0];
    }
    // if ($scope.locOpt.length > 0) {
    //     $scope.locSel = $scope.locOpt[0];
    // }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });
    $scope.dtInstance = {}
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span>   ";
        return acthtml;
    }

    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保供应商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    // dtColumns.push(DTColumnBuilder.newColumn('wb_autostr').withTitle('脱保计算')
    //     .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle('使用人').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        $scope.item.rreturndate = $scope.date.rreturndate.format('YYYY-MM-DD');
        $scope.item.cruserid = $scope.userSel.user_id;
        $scope.item.crusername = $scope.userSel.name;
        // $scope.item.processuserid = $scope.userSel.user_id;
        // $scope.item.processusername = $scope.userSel.name;
     //   $scope.item.tusedcompanyid = $scope.compSel.id;
        $scope.item.tpartid = ""
        $scope.item.tuseduserid = ""
        //  $scope.item.tloc = $scope.locSel.dict_item_id;
        $scope.item.bustype = "TK";
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        $confirm({
            text: '是否确定退库?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resCollectionreturn/ext/insertOrUpdate.do",
                    $scope.item).success(function (res) {
                    if (res.success) {
                        $uibModalInstance.close('OK');
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.review = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "TK";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}


//选择流程,输入参数ptype
//   ps.title = meta.name;
//   ps.busid = meta.busuuid;
//
//
function chosenFlowTreeCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                           $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                           $uibModalInstance, $window, $stateParams, $timeout) {
    console.log("need parameter:ptype,name,busuuid");
    console.log("flowdata", meta);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withDOM('frtlpi').withPaginationType('simple').withDisplayLength(
            50).withOption("ordering", false).withOption("responsive",
            false).withOption("searching", false).withOption('scrollY',
            300).withOption('scrollX', true).withOption(
            'bAutoWidth', true).withOption('scrollCollapse', true)
        .withOption('paging', false).withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });

    function stateChange(iColumn, bVisible) {
    }
    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    function renderStatus(data, type, full) {
        var res = "";
        if (data == "normal") {
            res = "正常";
        } else if (data == "stop") {
            res = "停用";
        } else {
            res = data;
        }
        return res;
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml)
        .withClass('select-checkbox checkbox_center').renderWith(
            function () {
                return ""
            }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('名称')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ptplkey')
        .withTitle('流程实例').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('status').withTitle('状态')
        .withOption('sDefaultContent', '').renderWith(renderStatus)),
        $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注')
            .withOption('sDefaultContent', ''));
    $scope.item = {};
    // 树配置
    $scope.treeConfig = {
        core: {
            multiple: false,
            animation: true,
            error: function (error) {
                $log.error('treeCtrl: error from js tree - '
                    + angular.toJson(error));
            },
            check_callback: true,
            worker: true
        },
        loading: "加载中……",
        ui: {
            theme_name: "classic" // 设置皮肤样式
        },
        rules: {
            type_attr: "rel", // 设置节点类型
            valid_children: "root" // 只有root节点才能作为顶级结点
        },
        callback: {
            onopen: function (node, tree_obj) {
                return true;
            }
        },
        types: {
            "default": {
                icon: 'glyphicon glyphicon-th'
            },
            root: {
                icon: 'glyphicon glyphicon-home'
            },
            "node": {
                "icon": "glyphicon glyphicon-tag"
            },
            "category": {
                "icon": "glyphicon glyphicon-equalizer"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'changed'],
        contextmenu: {
            items: {}
        }
    }

    $scope.curSelNode = "";
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
        // 展开所有节点
        $scope.tree.open_all();
        // 响应节点变化
        $scope.treeInstance.on("changed.jstree", function (e, data) {
            if (data.action == "select_node") {
                // 加载数据
                var snodes = $scope.tree.get_selected();
                if (snodes.length == 1) {
                    var node = snodes[0];
                    $scope.curSelNode = node;
                    flush();
                }
            }
        });
    }
    function flushTree(id) {
        $http
            .post(
                $rootScope.project
                + "/api/ctCategroy/queryCategoryTreeList.do", {
                    root: id
                }).success(function (res) {
            if (res.success) {
                $scope.ignoreChanges = true;
                $scope.treeData = angular.copy(res.data);
                $scope.treeConfig.version++;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    flushTree(5);
    function flush() {
        $http.post(
            $rootScope.project
            + "/api/flow/sysProcessDef/ext/selectList.do", {
                owner: $scope.curSelNode
            }).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }
    function getSelectRow() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            // notify({
            //     message: "请至少选择一项"
            // });
            return;
        } else if (data.length > 1) {
            notify({
                message: "请最多选择一项"
            });
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    $scope.preview = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            var ps = {};
            ps = selrow;
            var modalInstance = $uibModal.open({
                backdrop: true,
                templateUrl: 'views/flow/modal_reviewProcess.html',
                controller: modalreviewProcessCtl,
                size: 'lg',
                resolve: {
                    meta: function () {
                        return ps;
                    }
                }
            });
            modalInstance.result.then(function (result) {
            }, function (reason) {
            });
        }
    }
    $scope.itemquery = function () {
        flush();
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {

        var sel = getSelectRow();
        var ps = {};
        if (angular.isDefined(sel) && angular.isDefined(sel.id)) {
            ps.ifsp = "1";
            ps.processdefid = sel.id;
        } else {
            notify({
                message:"请选择审批流程"
            });
            return;
        }
        $confirm({
            text: "是否确认送审"
        }).then(
            function () {
                ps.ptype = meta.ptype
                ps.title = meta.name;
                ps.busid = meta.busuuid;
                ps.formtype = "none";
                $http.post(
                    $rootScope.project
                    + "/api/zc/flow/startAssetFlow.do", ps).success(function (res) {
                    if (res.success) {
                        $uibModalInstance.close("OK");
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                });
            });
    }
}

