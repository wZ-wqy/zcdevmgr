function config_ops($stateProvider, $ocLazyLoadProvider) {
    console.log("App ops config");
    $ocLazyLoadProvider.config({
        debug: true
    });
    $stateProvider.state('hmgr', {
        abstract: true,
        url: "/hmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('hmgr.hlist', {
        url: "hmgr_hlist",
        data: {pageTitle: '主机管理'},
        templateUrl: "views/ops/hostmgr.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/ops/hostmgr.js?v=' + version]
                }]);
            }
        }
    });
    $stateProvider.state('dbmgr', {
        abstract: true,
        url: "/dbmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('dbmgr.dblist', {
        url: "dbmgr_dblist",
        data: {pageTitle: '数据库管理'},
        templateUrl: "views/ops/dbbackup.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/ops/dbbackup.js?v=' + version]
                }]);
            }
        }
    });

    $stateProvider.state('dtj', {
        url: "/dtj",
        templateUrl: "views/ops/xttj.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        name: 'angular-flot',
                        files: ['plugin/flot/jquery.flot.js', 'plugin/flot/jquery.flot.time.js', 'plugin/flot/jquery.flot.tooltip.min.js', 'plugin/flot/jquery.flot.spline.js', 'plugin/flot/jquery.flot.resize.js', 'plugin/flot/jquery.flot.pie.js', 'plugin/flot/curvedLines.js', 'plugin/flot/angular-flot.js', 'plugin/flot/jquery.flot.barnumbers.js']
                    },
                    {
                        serie: true,
                        files: ['views/ops/xttj.js?v=' + version]
                    }]);
            }
        }
    });


    // $stateProvider.state('dtj', {
    //     abstract: true,
    //     url: "/dtj",
    //     templateUrl: "views/common/content.html?v=" + version
    // }).state('dtj.hd', {
    //     url: "dtj_hd",
    //     data: {pageTitle: '系统统计'},
    //     templateUrl: "views/ops/xttj.html?v=" + version,
    //     resolve: {
    //         loadPlugin: function ($ocLazyLoad) {
    //             return $ocLazyLoad.load([
    //                 {
    //                     serie: true,
    //                     name: 'angular-flot',
    //                     files: ['plugin/flot/jquery.flot.js', 'plugin/flot/jquery.flot.time.js', 'plugin/flot/jquery.flot.tooltip.min.js', 'plugin/flot/jquery.flot.spline.js', 'plugin/flot/jquery.flot.resize.js', 'plugin/flot/jquery.flot.pie.js', 'plugin/flot/curvedLines.js', 'plugin/flot/angular-flot.js', 'plugin/flot/jquery.flot.barnumbers.js']
    //                 },
    //                 {
    //                     serie: true,
    //                     files: ['views/ops/xttj.js?v=' + version]
    //                 }]);
    //         }
    //     }
    // })

    $stateProvider.state('txl', {
        abstract: true,
        url: "/txl",
        templateUrl: "views/common/content.html?v=" + version
    }).state('txl.outercontact', {
        url: "/txl_outercontact",
        data: {pageTitle: '外部联系人'},
        templateUrl: "views/ops/outercontact.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/ops/outercontact.js?v=' + version]
                }]);
            }
        }
    })


    $stateProvider.state('infosys', {
        abstract: true,
        url: "/infosys",
        templateUrl: "views/common/content.html?v=" + version
    }).state('infosys.infosys', {
        url: "infosys_infosys",
        data: {pageTitle: '业务系统'},
        templateUrl: "views/ops/infosys.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        files: ['views/ops/infosys.js?v=' + version]
                    }]);
            }
        }
    })



    $stateProvider.state('kncat', {
        abstract: true,
        url: "/kncat",
        templateUrl: "views/common/content.html?v=" + version
    }).state('kncat.cat', {
        url: "kncat_cat",
        data: {pageTitle: '知识分类'},
        templateUrl: "views/ops/kncat.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        files: ['views/ops/kncat.js?v=' + version]
                    }]);
            }
        }
    })



    $stateProvider.state('knbsearch', {
        abstract: true,
        url: "/knbsearch",
        templateUrl: "views/common/content.html?v=" + version
    }).state('knbsearch.catquery', {
        url: "knbsearch_catquery",
        data: {pageTitle: '分类查询'},
        templateUrl: "views/ops/kncatquery.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        files: ['views/ops/kncatquery.js?v=' + version]
                    }]);
            }
        }
    })

    $stateProvider.state('knmgr', {
        abstract: true,
        url: "/knmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('knmgr.kn', {
        url: "knmgr_kn",
        data: {pageTitle: '知识库'},
        templateUrl: "views/ops/knbase.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        files: ['views/ops/knbase.js?v=' + version]
                    }]);
            }
        }
    }).state('knmgr.remote', {
        url: "knmgr_remote",
        data: {pageTitle: '知识库链接'},
        templateUrl: "views/ops/knremote.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        serie: true,
                        files: ['views/ops/knremote.js?v=' + version]
                    }]);
            }
        }
    })



}