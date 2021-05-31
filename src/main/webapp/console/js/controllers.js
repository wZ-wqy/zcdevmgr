var app = {}

function MainCtrl($translate, $log, $http, $scope, $rootScope, $state, $localStorage,
                  userService, notify, $timeout) {
    $rootScope.BELONGCOMP = $translate.instant('BELONGCOMP');
    $rootScope.BELONGCOMP_B = $translate.instant('BELONGCOMP_B');
    $rootScope.BELONGCOMP_A = $translate.instant('BELONGCOMP_A');
    $rootScope.BELONGCOMP_OUT = $translate.instant('BELONGCOMP_OUT');
    $rootScope.BELONGCOMP_IN = $translate.instant('BELONGCOMP_IN');
    $rootScope.USEDCOMP = $translate.instant('USEDCOMP');
    $rootScope.USEDCOMP_B = $translate.instant('USEDCOMP_B');
    $rootScope.USEDCOMP_A = $translate.instant('USEDCOMP_A');
    $rootScope.USEDCOMP_IN = $translate.instant('USEDCOMP_IN');
    $rootScope.USEDCOMP_LYTK = $translate.instant('USEDCOMP_LYTK');
    $rootScope.USEDCOMP_LY = $translate.instant('USEDCOMP_LY');
    $rootScope.USEDCOMP_TK = $translate.instant('USEDCOMP_TK');
    $rootScope.COMP_DC = $translate.instant('COMP_DC');
    $rootScope.COMP_DL = $translate.instant('COMP_DL');
    $rootScope.USEDPART = $translate.instant('USEDPART');
    $rootScope.USEDPART_B = $translate.instant('USEDPART_B');
    $rootScope.USEDPART_A = $translate.instant('USEDPART_A');
    $rootScope.USEDPART_IN = $translate.instant('USEDPART_IN');
    $rootScope.USEDPART_LYTK = $translate.instant('USEDPART_LYTK');
    $rootScope.USEDUSER = $translate.instant('USEDUSER');
    // 修改主题
    if (angular.isUndefined($rootScope.dt_msg)) {
        $rootScope.dt_msg = $localStorage.get("dt_msg")
    }
    if (angular.isUndefined($rootScope.dt_version)) {
        $rootScope.dt_version = $localStorage.get("dt_version")
    }
    var cur_theme = $localStorage.get("cur_theme");
    if (angular.isDefined(cur_theme)) {
        $scope.cur_skin = cur_theme;
    } else {
        $scope.cur_skin = "default";
    }
    $scope.change_theme = function (theme) {
        $scope.cur_skin = theme;
        $localStorage.put("cur_theme", theme);
    }
    $scope.fullScreen = function () {
        var element = document.documentElement; // 若要全屏页面中div，var element=
        // document.getElementById("divID");
        // IE 10及以下ActiveXObject
        if (window.ActiveXObject) {
            var WsShell = new ActiveXObject('WScript.Shell')
            WsShell.SendKeys('{F11}');
        }
        // HTML W3C 提议
        else if (element.requestFullScreen) {
            element.requestFullScreen();
        }
        // IE11
        else if (element.msRequestFullscreen) {
            element.msRequestFullscreen();
        }
        // Webkit (works in Safari5.1 and Chrome 15)
        else if (element.webkitRequestFullScreen) {
            element.webkitRequestFullScreen();
        }
        // Firefox (works in nightly)
        else if (element.mozRequestFullScreen) {
            element.mozRequestFullScreen();
        }
    }
    // 退出登录
    $scope.logout = function () {
        userService.logout().then(function (result) {
            $log.warn("userService logout result", result)
            if (result.success) {
                $state.go("login");
            } else {
            }
        }, function (error) {
        }, function (progress) {
        })
    }
    $scope.changepwd = function () {
        $state.go("me.pwdreset");
    }
    // 切换系统
    $scope.switchSystem = function (id) {
        userService.switchSystem(id).then(function (result) {
            if (result.success) {
                $timeout(function () {
                    $state.go("content");
                }, 800);
            } else {
                notify({
                    message: result.message
                });
            }
        }, function (error) {
        }, function (progress) {
        })
    }
    // 处理菜单的logo函数
    $scope.menuLogoIsExist = function (logo) {
        if (angular.isDefined(logo) && logo != "") {
            return true;
        } else {
            return false;
        }
    }
    // 监听左边菜单数据
    $scope.$watch(function () {
        return $rootScope.dt_sys_menus;
    }, function () {
        $log.warn("watch $rootScope.dt_sys_menus change,load from this,again.",
            $rootScope.dt_sys_menus);
        if (angular.isDefined($rootScope.dt_sys_menus)
            && $rootScope.dt_sys_menus != null) {
            $scope.menu = $rootScope.dt_sys_menus;
            $state.go($state.current, null, {
                reload: true
            });
        }
    }, true);
    // 页面刷新
    var dt_sys_menu = $localStorage.get("dt_sys_menus");
    if (angular.isDefined(dt_sys_menu)) {
        $log.warn("dt_sys_menu load from localstorage", dt_sys_menu);
        $scope.menu = dt_sys_menu;
        // fixnav();
    }
    // 监听用户数据
    $scope
        .$watch(
            function () {
                return $rootScope.dt_sys_user_info;
            },
            function () {
                $log
                    .warn(
                        "watch $rootScope.dt_sys_user_info change,load from this,again.",
                        $rootScope.dt_sys_user_info);
                if (angular.isDefined($rootScope.dt_sys_user_info)
                    && $rootScope.dt_sys_user_info != null) {
                    $scope.sys_user_info = $rootScope.dt_sys_user_info
                }
            }, true);
    // 页面刷新
    var sys_user_info = $localStorage.get("dt_sys_user_info")
    if (angular.isDefined(sys_user_info)) {
        $log.warn("user_info load from localstorage", sys_user_info);
        $scope.sys_user_info = sys_user_info;
    }
    //资产列显示控制
    if (angular.isUndefined($rootScope.zccolctl)) {
        $rootScope.zccolctl = $localStorage.get("zccolctl")
    }
    if (angular.isUndefined($rootScope.zccolctlcommon)) {
        $rootScope.zccolctlcommon = $localStorage.get("zccolctlcommon")
    }

    //当前登陆用户数据
    if (angular.isUndefined($rootScope.dt_sys_user_info)) {
        if (angular.isDefined($rootScope.dt_msg)) {
            $rootScope.dt_sys_user_info = $localStorage.get("dt_sys_user_info")
        }
    }
    // 列举系统
    $scope.$watch(function () {
        return $rootScope.dt_systems;
    }, function () {
        $log.warn("watch $rootScope.dt_systems change,load from this,again.",
            $rootScope.dt_systems);
        if (angular.isDefined($rootScope.dt_systems)
            && $rootScope.dt_systems != null) {
            $scope.dt_systems = $rootScope.dt_systems
        }
    }, true);
    var dt_systems = $localStorage.get("dt_systems")
    if (angular.isDefined(dt_systems)) {
        $log.warn("dt_systems load from localstorage", dt_systems);
        $scope.dt_systems = dt_systems;
    }
};
angular.module('app').controller('MainCtrl', MainCtrl);