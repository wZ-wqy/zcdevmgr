app.factory('scriptLoader', ['$q', '$timeout', function ($q, $timeout) {
    /**
     * Naming it processedScripts because there is no guarantee any of those have been actually loaded/executed
     * @type {Array}
     */
    var processedScripts = [];
    return {
        /**
         * Parses 'data' in order to find & execute script tags asynchronously as defined.
         * Called for partial views.
         * @param data
         * @returns {*} promise that will be resolved when all scripts are loaded
         */
        loadScriptTagsFromData: function (data) {
            var deferred = $q.defer();
            var $contents = $($.parseHTML(data, document, true)),
                $scripts = $contents.filter('script[data-src][type="text/javascript-lazy"]').add($contents.find('script[data-src][type="text/javascript-lazy"]')),
                scriptLoader = this;
            scriptLoader.loadScripts($scripts.map(function () {
                return $(this).attr('data-src')
            }).get())
                .then(function () {
                    deferred.resolve(data);
                });
            return deferred.promise;
        },
        /**
         * Sequentially and asynchronously loads scripts (without blocking) if not already loaded
         * @param scripts an array of url to create script tags from
         * @returns {*} promise that will be resolved when all scripts are loaded
         */
        loadScripts: function (scripts) {
            var previousDefer = $q.defer();
            previousDefer.resolve();
            scripts.forEach(function (script) {
                if (processedScripts[script]) {
                    if (processedScripts[script].processing) {
                        previousDefer = processedScripts[script];
                    }
                    return
                }
                var scriptTag = document.createElement('script'),
                    $scriptTag = $(scriptTag),
                    defer = $q.defer();
                scriptTag.src = script;
                defer.processing = true;
                $scriptTag.load(function () {
                    $timeout(function () {
                        defer.resolve();
                        defer.processing = false;
                        Pace.restart();
                    })
                });
                previousDefer.promise.then(function () {
                    document.body.appendChild(scriptTag);
                });
                processedScripts[script] = previousDefer = defer;
            });
            return previousDefer.promise;
        }
    }
}]);
app.service('userService', function ($http, $q, $log, $rootScope, $localStorage) {
    return {
        logout: function () {
            var deferred = $q.defer();
            $http.post($rootScope.project + "/api/user/logout.do", {}).success(function (res) {
                if (res.success) {
                    $rootScope.dt_app_token = ""
                    $localStorage.put('dt_app_token', "");
                    $rootScope.dt_sys_menus = []
                    $localStorage.put('dt_sys_menus', []);
                }
                deferred.resolve(res);
            })
            return deferred.promise;
        },
        login: function (e) {
            var deferred = $q.defer();
            e.basePublic = "yes";
            e.client = "web";
            $http.post($rootScope.project + "/api/user/login.do", e).success(function (res) {
                $log.warn("service login return", res);
                if (res.success) {
                    $http.post($rootScope.project + "/api/zc/queryZcColCtlById.do", {
                        id: "zccolctl"
                    }).success(function (rscolctl1) {
                        if (rscolctl1.success) {
                            $rootScope.zccolctl = rscolctl1.data;
                            $localStorage.put('zccolctl', rscolctl1.data);
                        }
                    });
                    $http.post($rootScope.project + "/api/zc/queryZcColCtlById.do", {
                        id: "zccolctlcommon"
                    }).success(function (rscolctl2) {
                        if (rscolctl2.success) {
                            $rootScope.zccolctlcommon = rscolctl2.data;
                            $localStorage.put('zccolctlcommon', rscolctl2.data);
                        }
                    });
                    if (angular.isDefined(res.data.token)) {
                        // 用户token
                        $log.warn("set token to $rootScope")
                        $rootScope.dt_app_token = res.data.token;
                        $localStorage.put('dt_app_token', res.data.token);
                        // 用户信息
                        $rootScope.dt_sys_user_info = res.data.user_info;
                        $localStorage.put('dt_sys_user_info', res.data.user_info);
                        // 用户拥有的系统资源
                        $rootScope.dt_systems = res.data.systems;
                        $localStorage.put('dt_systems', res.data.systems);
                        if (angular.isUndefined(res.data.dtmsg)) {
                            $rootScope.dt_sys_user_info = {};
                            $localStorage.put('dt_systems', []);
                        } else {
                            $rootScope.dt_msg = res.data.dtmsg;
                            $localStorage.put('dt_msg', res.data.dtmsg);
                        }

                        if (angular.isUndefined(res.data.dtversion)) {
                            $rootScope.dt_sys_user_info = {};
                            $localStorage.put('dt_systems', []);
                        } else {
                            $rootScope.dt_version = res.data.dtversion;
                            $localStorage.put('dt_version', res.data.dtversion);
                        }

                        //初始化菜单,当前默认可能存在Id为1的系统,默认获取该资源
                        var menuid = "";
                        if (angular.isDefined(res.data.cur_system) && res.data.cur_system.length > 0) {
                            menuid = res.data.cur_system;
                        }
                        $log.info("selected menu_id:" + menuid);
                        if (menuid.length > 0) {
                            $http.post($rootScope.project + "/api/sysUserInfo/my/listMyMenusById.do", {
                                menu_id: menuid
                            }).success(function (rs) {
                                if (rs.success) {
                                    $log.warn("###Action login,load user_menu from service######");
                                    $rootScope.dt_sys_menus = rs.data;
                                    $localStorage.put('dt_sys_menus', rs.data);
                                }
                                deferred.resolve(rs);
                            });
                        } else {
                            $log.info("当前无菜单")
                            $rootScope.dt_sys_menus = [];
                            $localStorage.put('dt_sys_menus', []);
                        }
                    } else {
                        $log.debug("返回不存在token");
                    }
                }
                deferred.resolve(res);
            })
            return deferred.promise;
        },
        getToken: function () {
            if (angular.isDefined($rootScope.dt_app_token)) {
                return $rootScope.dt_app_token;
            } else {
                return $localStorage.get("dt_app_token");
            }
            return "";
        },
        checkLogin: function () {
            var deferred = $q.defer();
            // 后期需要加上菜单选择判断,当前暂时不实现
            console.log("检查是否登陆状态");
            $http.post($rootScope.project + "/api/user/checkLogin.do", {}).success(function (res) {
                deferred.resolve(res);
            });
            return deferred.promise;
//			if(!angular.isDefined($rootScope.project )){
//				var deferred = $q.defer();
//				$http.post( "../api/system/queryContextPath.do", {}).success(function(res) {
//					if(res.success){
//						$rootScope.project=res.data+"/";
//						var deferred = $q.defer();
//						// 后期需要加上菜单选择判断,当前暂时不实现
//						$http.post($rootScope.project + "/api/user/checkLogin.do", {}).success(function(res) {
//							deferred.resolve(res);
//						});
//						return deferred.promise;
//					}
//					 
//				})
//			 
//			}
        },
        switchSystem: function (id) {
            var deferred = $q.defer();
            $http.post($rootScope.project + "/api/sysUserInfo/my//listMyMenusById.do", {
                menu_id: id
            }).success(function (rs) {
                if (rs.success) {
                    $log.warn("###Action switchSystem,load user_menu from service######");
                    $rootScope.dt_sys_menus = rs.data;
                    $localStorage.put('dt_sys_menus', rs.data);
                    $localStorage.put('dt_cur_systemId', id);
                }
                deferred.resolve(rs);
            });
            return deferred.promise;
        },
        getZcColCtl: function (id) {
            var deferred = $q.defer();
            $http.post($rootScope.project + "/api/zc/queryZcColCtlById.do", {
                id: id
            }).success(function (rs) {
                if (rs.success) {
                    $rootScope.zccolctlcommon = rs.data;
                }
                deferred.resolve(rs);
            });
            return deferred.promise;
        }
    }
});