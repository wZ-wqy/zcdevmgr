function sysLoginCtl($timeout, $rootScope, $scope, $log, $http, userService,
                     $state, $localStorage, notify, $stateParams) {

    $scope.version="";
    //系统版本v2.2.24
    $http.post($rootScope.project + "/api/system/querySystemVersion.do",
        {}).success(function (res) {
        if (res.success) {
            $scope.version="系统版本:"+res.data.value
        } else {
            notify({
                message: res.message
            });
        }
    })

    var to = $stateParams.to;
    var from = $stateParams.from;
    var msg = $stateParams.msg;
    $scope.user = {
        user: "",
        pwd: "",
        type: "username"
    };
    if (angular.isDefined(msg) && msg != null) {
        if (to != from) {
            notify({
                message: msg
            });
        }
    }

    function login(e){
        var luserid = $localStorage.get("app_cur_userid");
        var lsystemid = $localStorage.get("dt_cur_systemId");
        userService.login($scope.user).then(
            function (result) {
                if (result.success) {
                    var curuserid = result.data.user_info.userId;
                    $localStorage.put("app_cur_userid", curuserid);
                    var cur_system = result.data.cur_system;
                    // 与上次登陆用户相同,并且所在子系统也相同，则考虑切换到该菜单
                    if (angular.isDefined(luserid) && luserid == curuserid
                        && cur_system == lsystemid) {
                        console.log("与上次登陆用户相同,before_userId:" + luserid
                            + ",cur_userId:" + curuserid);
                        console.log(cur_system, lsystemid);
                        $timeout(function () {
                            if (angular.isDefined(from) && from != null
                                && from != 'login') {
                                $log.warn("end:" + from);
                                $state.go(from, {});
                            } else {
                                $state.go("content");
                            }
                        }, 500)
                    } else {
                        console.log("与上次登陆用户不同,before_userId:" + luserid
                            + ",cur_userId:" + curuserid);
                        console.log(cur_system, lsystemid);
                        $state.go("content");
                    }
                } else {
                    notify({
                        message: result.message
                    });
                }
            })
    }

    $("#loginkey1").on('keypress', function(event){
        if(event.keyCode==13){
            login();
        }else{
        }
    });

    $("#loginkey2").on('keypress', function(event){
        if(event.keyCode==13){
            login();
        }else{
        }
    });


    $scope.typecheck = function (v) {
        $scope.user.type = v;
    }
    $scope.login = function () {
        login();
    }
};
app.register.controller('sysLoginCtl', sysLoginCtl);