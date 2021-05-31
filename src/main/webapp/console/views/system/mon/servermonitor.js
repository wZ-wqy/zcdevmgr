function servermonitorCtl($window, $stateParams,
                          $confirm, notify, $scope, $http,
                          $rootScope) {
    $scope.data = {}
    $http.post($rootScope.project + "/api/monitor/queryServerInfo.do", {}).success(function (res) {
        if (res.success) {
            $scope.data = res.data;
        } else {
            notify({
                message: res.message
            });
        }
    });
};
app.register.controller('servermonitorCtl', servermonitorCtl);