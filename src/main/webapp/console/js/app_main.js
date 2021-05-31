var app = angular.module('app', ['ui.router', 'oc.lazyLoad', 'ui.bootstrap',
    'pascalprecht.translate', 'ngIdle', 'ngJsTree', 'ngSanitize',
    'cgNotify', 'angular-confirm', 'datatables', 'datatables.select',
    'datatables.fixedcolumns', 'datatables.buttons',
    'localytics.directives', 'swxLocalStorage', 'angular-loading-bar',
     'datePicker', 'treeGrid'])
var $injector = angular.injector();
function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0, index + 1);
    return result;
}
var gct = getContextPath();
var version = new Date().getTime();
app.factory('sessionInjector', [
    '$log',
    '$injector',
    function ($log, $injector) {
        var sessionInjector = {};
        sessionInjector.request = function (config) {
            // 每个请求添加token
            // X-Requested-With,将所有请求标记为ajax请求
            config.headers['X-Requested-With'] = "XMLHttpRequest"
            var userService = $injector.get('userService');
            var tokenstr = userService.getToken();
            if (angular.isDefined(tokenstr) && tokenstr.length > 5) {
               // config.headers['_token'] = tokenstr;
            }
            // 禁止HTML缓存
            if (config.url.indexOf('.html') > -1
                && config.url.indexOf('views') > -1) {
                config.url += "?auto_v=" + version;
            }
            // 禁止JS缓存
            if (config.url.indexOf('.js') > -1
                && config.url.indexOf('views') > -1) {
                config.url += "?auto_v=" + version;
            }
            return config;
        }
        sessionInjector.response = function (responseObject) {
            // 输出调试信息
            if (angular.isDefined(responseObject.config)
                && angular.isDefined(responseObject.config.data)
                && angular.isDefined(responseObject.config.url)
                && angular.isDefined(responseObject.data)) {
                $log.warn("$http|res url:", responseObject.config.url);
                $log.warn("$http|res post:", responseObject.config.data);
                $log.warn("$http|res data:", responseObject.data);
            }
            // 未登录
            if (responseObject.status == "299") {
                var state = $injector.get('$state');
                state.go("login");
            } else if (responseObject.status == "298") {
                // 未授权
            }
            return responseObject;
        }
        sessionInjector.requestError = function (rejectReason) {
            var deferred = $q.defer();
            $log.warn("myInterceptor.requestError : " + responseObject);
            return deferred.promise;
        };
        sessionInjector.responseError = function (responseError) {
            $log.warn("sessionInjector,responseError", responseError);
            var notify = $injector.get('notify');
            notify({
                message: "服务器故障,ErrorCode:" + responseError.status
            });
            return responseError;
        };
        return sessionInjector;
    }]);

function config_main($translateProvider, cfpLoadingBarProvider, $locationProvider,
                     $controllerProvider, $compileProvider, $stateProvider, $filterProvider,
                     $provide, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider,
                     KeepaliveProvider, $httpProvider) {
    //国际
    //{{ 'BELONGCOMP' | translate }}
    //var BELONGCOMP = $translate.instant('BELONGCOMP');
    $translateProvider
        .translations(
            'zh',
            {
                BELONGCOMP: '所属公司',
                BELONGCOMP_B: '所属公司(变更前)',
                BELONGCOMP_A: '所属公司(变更后)',
                BELONGCOMP_OUT: '出库所属公司',
                BELONGCOMP_IN: '入库所属公司',
                USEDCOMP: '使用公司',
                USEDCOMP_B: '使用公司(变更前)',
                USEDCOMP_A: '使用公司(变更后)',
                USEDCOMP_IN: '入库使用公司',
                USEDCOMP_LYTK: '领用/退库后使用公司',
                USEDCOMP_LY: '领用后使用公司',
                USEDCOMP_TK: '退库后使用公司',
                COMP_DC: '调出所属公司',
                COMP_DL: '调入所属公司',
                BELONGPART: "所属部门",
                USEDPART: "使用部门",
                USEDPART_B: "使用部门(变更前)",
                USEDPART_A: "使用部门(变更后)",
                USEDPART_IN: "入库使用部门",
                USEDPART_LYTK: "领用/退库后使用部门",
                USEDPART_LY: "领用后使用部门",
                USEDUSER: "使用人",
                USEDUSER_RK: "入库使用人",
                DICT_COMP: "公司",
                DICT_PART: "部门",
                END: 'ee'
            });
    $translateProvider.preferredLanguage('zh');
    // 圈圈延迟出现控制
    console.log("App main config");
    cfpLoadingBarProvider.latencyThreshold = 1500;
    // 拦截请求
    $httpProvider.interceptors.push('sessionInjector');
    app.register = {
        controller: $controllerProvider.register,
        directive: $compileProvider.directive,
        filter: $filterProvider.register,
        factory: $provide.factory,
        service: $provide.service
    };
    // 间隔interval秒,超时idle秒后，timeout秒未活动则退出
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(5); // in seconds
    KeepaliveProvider.interval(2);
    $urlRouterProvider.otherwise("/login");
    $ocLazyLoadProvider.config({
        debug: true
    });
    $httpProvider.defaults.headers.post['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.post['Pragma'] = 'no-cache';
    $httpProvider.defaults.headers.post['Cache'] = 'no-cache';
    $httpProvider.defaults.headers.post['Expires'] = '0';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    // $httpProvider.defaults.withCredentials = false;
    var param = function (obj) {
        var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
        for (name in obj) {
            value = obj[name];
            if (value instanceof Array) {
                for (i = 0; i < value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name + '[' + i + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            } else if (value instanceof Object) {
                for (subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            } else if (value !== undefined && value !== null)
                query += encodeURIComponent(name) + '='
                    + encodeURIComponent(value) + '&';
        }
        return query.length ? query.substr(0, query.length - 1) : query;
    };
    $httpProvider.defaults.transformRequest = [function (data) {
        return angular.isObject(data) && String(data) !== '[object File]' ? param(data)
            : data;
    }];
    // 登录
    $stateProvider.state('login', {
        url: "/login",
        transclude: true,
        templateUrl: "views/system/login/login.html?v=" + version,
        params: {
            msg: null,
            from: null,
            to: null,
            psBtns: "[]"
        },
        resolve: {
            check: function (userService, $log, $state) {
                userService.checkLogin().then(function (result) {
                    if (result.success) {
                        // 已经登录
                        $log.warn("账户已经登录,马上跳转至content");
                        $state.go("content");
                    } else {
                    }
                })
            },
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/system/login/login.js?v=' + version]
                }, {
                    serie: true,
                    files: ['views/system/login/l.css']
                }]);
            }
        }
    });
    // 默认页面需要做检查
    $stateProvider.state('content', {
        url: "/show_content",
        data: {
            pageTitle: '',
            loginCheck: true
        },
        templateUrl: "views/common/content.html"
    })
}

app
    .config(config_main)
    .run(
        function (Idle, $rootScope, $state, $http, $log, $transitions,
                  $localStorage, userService, $templateCache) {
            console.log("App main run");
            // start watching when the app runs. also starts the
            // Keepalive service by
            // Idle.watch();
            // 替换了之前的$stateNotFound
            $state.onInvalid(function (to, from, injector) {
                $log.warn("to", to);
                $log.warn("from", from);
                $log.warn("injector", injector);
                alert("当前菜单配置有误.");
            });
            // 替换了之前的$stateChangeStart
            $transitions
                .onBefore(
                    {
                        to: '**'
                    },
                    function (trans) {
                        console.log("onBefore", trans);
                        var from_arr = trans._treeChanges.from;
                        var from = null;
                        if (from_arr.length > 0) {
                            from = from_arr[from_arr.length - 1].state.name;
                        }
                        var to_arr = trans._treeChanges.to;
                        var to = null;
                        if (to_arr.length > 0) {
                            to = to_arr[to_arr.length - 1].state.name;
                        }
                        console.log('onBefore|from:' + from
                            + ',to:' + to);
                        // 处理按钮权限
                        if (angular
                            .isDefined(trans._targetState._options.custom.btns)) {
                            // 如果不是空的,直接放到$rootScope中
                            console
                                .log("onBefore｜pbtns trans exists,put btns into rootScope")
                            $rootScope.curMemuBtns = trans._targetState._options.custom.btns;
                            $localStorage
                                .put(
                                    "curMemuBtns_"
                                    + trans._targetState._definition.name,
                                    trans._targetState._options.custom.btns);
                        } else {
                            console
                                .log("onBefore｜pbtns trans not exists,get from localstorage")
                            var v = $localStorage
                                .get("curMemuBtns_"
                                    + trans._targetState._definition.name);
                            $rootScope.curMemuBtns = v;
                            if (angular.isDefined(v)) {
                                console
                                    .log("onBefore｜pbtns trans get from localstorage,success")
                            }
                        }
                    });
            $transitions
                .onSuccess(
                    {
                        to: '**'
                    },
                    function (trans) {
                        console.log("onSuccess", trans);
                        // 删除html缓存
                        var $state = trans.router.stateService;
                        if (angular
                                .isDefined($state.router.globals)
                            && angular
                                .isDefined($state.router.globals.current)
                            && angular
                                .isDefined($state.router.globals.current.templateUrl)) {
                            console
                                .log("onSuccess|Remove"
                                    + $state.router.globals.current.templateUrl);
                            $templateCache
                                .remove($state.router.globals.current.templateUrl)
                        }
                        // 处理按钮权限
                        var from_arr = trans._treeChanges.from;
                        var from = null;
                        if (from_arr.length > 0) {
                            from = from_arr[from_arr.length - 1].state.name;
                        }
                        var pbtns = "";
                        if (from_arr.length > 0) {
                            from = from_arr[from_arr.length - 1].state.name;
                            if (angular
                                .isDefined(from_arr[from_arr.length - 1].paramValues)) {
                                pbtns = from_arr[from_arr.length - 1].paramValues.psBtns;
                            }
                        }
                        var to_arr = trans._treeChanges.to;
                        var to = null;
                        if (to_arr.length > 0) {
                            to = to_arr[to_arr.length - 1].state.name;
                        }
                        console.log('onSuccess|from:' + from
                            + ',to:' + to);
                        // 判断
                        var userService = trans.injector().get(
                            'userService');
                        if (angular.isDefined(from)
                            && from != ""
                            && from != "login") {
                            if (to != "login") {
                                userService
                                    .checkLogin()
                                    .then(
                                        function (result) {
                                            if (!result.success) {
                                                console
                                                    .log('onSuccess|Jump to login'
                                                        + to
                                                        + 'a');
                                                $state
                                                    .go(
                                                        "login",
                                                        {
                                                            from: from,
                                                            to: to,
                                                            msg: "会话超时,请重新登陆!"
                                                        });
                                            }
                                        },
                                        function (error) {
                                            alert('系统错误');
                                            event
                                                .preventDefault();
                                        },
                                        function (
                                            progress) {
                                        })
                            }
                        }
                    });
            $rootScope.$state = $state;
            var ct = getContextPath();
            $rootScope.project = ct + "/";
            $log.info('app project:' + $rootScope.project);
            $rootScope.version = version;
            $rootScope.$on('IdleStart', function () {
                $log.warn('IdleStart');
                // the user appears to have gone idle
            });
            $rootScope.$on('IdleWarn', function (e, countdown) {
                $log.warn('IdleWarncountdown', countdown);
                if (countdown == 1) {
                    // 重新激活
                    // Idle.watch();
                }
                // follows after the IdleStart event, but includes a
                // countdown until the
                // user is considered timed out
                // the countdown arg is the number of seconds remaining
                // until then.
                // you can change the title or display a warning dialog
                // from here.
                // you can let them resume their session by calling
                // Idle.watch()
            });
            $rootScope.$on('IdleTimeout', function () {
                $log.warn('IdleTimeout');
                // the user has timed out (meaning idleDuration +
                // timeout has passed
                // without any activity)
                // this is where you'd log them
            });
            $rootScope.$on('IdleEnd', function () {
                $log.warn('IdleEnd');
                // the user has come back from AFK and is doing stuff.
                // if you are
                // warning them, you can use this to hide the dialog
            });
            $rootScope.$on('Keepalive', function () {
                $log.warn('IdlKeepaliveeEnd');
                // do something to keep the user's session alive
            });
        });
app.config(config_cmdb).run(function () {
    console.log("App cmdb run");
});

app.config(config_cmdb_f).run(function () {
    console.log("App cmdb_f run");
});

app.config(config_ops).run(function () {
    console.log("App ops run");
});
app.config(config_system).run(function () {
    console.log("App System run");
});
app.config(config_monitor).run(function () {
    console.log("App Monitor run");
});

function initDT(DTDefaultOptions) {
    var lng = {
        processing: "处理中...",
        lengthMenu: "每页显示 _MENU_ 项结果",
        zeroRecords: "没有匹配结果",
        info: "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项；当前第 _PAGE_页，共 _PAGES_ 页",
        infoEmpty: "显示第 0 至 0 项结果，共 0 项",
        infoFiltered: "(由 _MAX_ 项结果过滤)",
        infoPostFix: "",
        search: "搜索:",
        url: "",
        emptyTable: "表中数据为空",
        sLoadingRecords: "载入中...",
        infoThousands: ",",
        paginate: {
            first: "首页",
            previous: "上页",
            next: "下页",
            last: "末页"
        },
        oAria: {
            sortAscending: ": 以升序排列此列",
            sortDescending: ": 以降序排列此列"
        },
        select: {
            rows: {
                _: "选择 %d 行",
                1: "选择 1 行"
            },
            // cells: "%d 单元 选中",
            // columns: "%d 列选择",

        }
    };
    DTDefaultOptions.setLanguage(lng);
    DTDefaultOptions.setDOM('frtlpi');
    DTDefaultOptions.setDisplayLength(100);
    //详细分页组，可以支持直接跳转到某页
    DTDefaultOptions.setOption('sPaginationType', 'full_numbers');
    //列是否支持排序
    DTDefaultOptions.setOption('ordering', false);
    //是否显示搜索框
    DTDefaultOptions.setOption('searching', true);
    //是否支持分页
    DTDefaultOptions.setOption('paging', true);
    //是否打开客户端状态记录功能,此功能在ajax刷新纪录的时候不会将个性化设定回复为初始化状态
    DTDefaultOptions.setOption('bStateSave', true);
    //DataTables载入数据时，是否显示‘进度’提示
    DTDefaultOptions.setOption('bProcessing', false);
    //是否显示页脚信息，DataTables插件左下角显示记录数
    DTDefaultOptions.setOption('bInfo', true);
    //列宽度
    DTDefaultOptions.setOption('bAutoWidth', true);
    //是否自动宽度使用
    DTDefaultOptions.setOption('responsive', false);
    //是否启动过滤、搜索功能
    DTDefaultOptions.setOption('bFilter', false);
    //是否启动服务器端数据导入
    DTDefaultOptions.setOption('serverSide', false);
}

app.run(initDT);
// before loading
$("#beforePage").removeClass("preloader");
$("#beforePage").addClass("preloader-hidden");