function config_wx($stateProvider, $ocLazyLoadProvider) {
    console.log("App Wx config");
    $ocLazyLoadProvider.config({
        debug: false
    });
    // 菜单
    $stateProvider.state('wxmgr', {
        abstract: true,
        url: "/wxmgr",
        data: {
            pageTitle: '微信'
        },
        templateUrl: "views/common/content.html"
    }).state('wxmgr.app', {
        url: "/wxmgr_app",
        data: {
            pageTitle: '应用设置'
        },
        template: '<div ng-controller="wxappCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/wx/app.js?v=' + version]
                }]);
            }
        }
    }).state('wxmgr.weboauth', {
        url: "/wxmgr_weboauth",
        data: {
            pageTitle: '网页授权'
        },
        template: '<div ng-controller="weboauthCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/wx/weboauth.js?v=' + version]
                }]);
            }
        }
    });
    // 菜单
    $stateProvider.state('wxmsg', {
        abstract: true,
        url: "/wxmgr",
        data: {
            pageTitle: '消息'
        },
        templateUrl: "views/common/content.html"
    }).state('wxmsg.imgtext', {
        url: "/imgtext",
        data: {
            pageTitle: '图文消息'
        },
        template: '<div ng-controller="wximgtextCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/wx/imgtext.js?v=' + version]
                }]);
            }
        }
    }).state('wxmsg.setting', {
        url: "/setting",
        data: {
            pageTitle: '消息设置'
        },
        template: '<div ng-controller="wxmsgsettingCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/wx/setting.js?v=' + version]
                }]);
            }
        }
    }).state('wxmsg.msgimg', {
        url: "/msgimg",
        data: {
            pageTitle: '图片素材'
        },
        template: '<div ng-controller="wxmsgimgCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/wx/msgimg.js?v=' + version]
                }]);
            }
        }
    });
}
