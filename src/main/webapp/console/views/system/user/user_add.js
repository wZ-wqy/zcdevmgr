function sysUserAddCtl($confirm, $log, notify, $scope, $http, $rootScope) {
    $scope.item = {};
    $scope.item.userType = "system";
    $scope.item.name = "";
    $scope.item.userName = "";
    $scope.userStatusOpt = [{
        id: "Y",
        name: "锁定"
    }, {
        id: "N",
        name: "正常"
    }]
    $scope.userStatusSel = $scope.userStatusOpt[1];
    $scope.reset = function () {
        $scope.item = {};
        $scope.item.userType = "system";
        $scope.item.userName = " ";
        $scope.item.name = " ";
        $scope.userStatusSel = $scope.userStatusOpt[1];
    }
    $scope.ok = function () {
        $scope.item.locked = $scope.userStatusSel.id;
        if($scope.item.name.trim()==""){
            notify({
                message: "请输入用户名"
            });
            return;
        }
        if($scope.item.userName.trim()==""){
            notify({
                message: "请输入登录名"
            });
            return;
        }

        $http.post($rootScope.project + "/api/sysUserInfo/addUser.do", $scope.item).success(
            function (res) {
                if (res.success) {
                    $scope.item = {};
                    $scope.item.userName = " ";
                    $scope.item.name = " ";
                    $scope.item.userType = "system";
                    $scope.userStatusSel = $scope.userStatusOpt[1];
                }
                notify({
                    message: res.message
                });
            })
    }
};
app.register.controller('sysUserAddCtl', sysUserAddCtl);