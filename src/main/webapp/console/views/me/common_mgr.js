function meCommonMgrCtl($localStorage, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var dt_systems = $localStorage.get("dt_systems")
    var sys_user_info = $localStorage.get("dt_sys_user_info")
    $log.warn(dt_systems);
    $log.warn(sys_user_info);
    $scope.systemOpt = [];
    $scope.systemSel = "";
    if (angular.isDefined(dt_systems) && dt_systems.length > 0) {
        $scope.systemOpt = dt_systems;
    }
    if (angular.isDefined(sys_user_info.systemId)) {
        for (var i = 0; i < $scope.systemOpt.length; i++) {
            if (sys_user_info.systemId == $scope.systemOpt[i].menuId) {
                $scope.systemSel = $scope.systemOpt[i];
                break;
            }
        }
    }
    $scope.saveData = function () {
        var ps = {};
        ps.system = $scope.systemSel.menuId;
        $http.post($rootScope.project + "/api/sysUserInfo/my/saveDefMenus.do", ps).success(function (res) {
            notify({
                message: res.message
            });
        })
    }
};
app.register.controller('meCommonMgrCtl', meCommonMgrCtl);