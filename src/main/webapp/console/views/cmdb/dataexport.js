function cmdbDataExportCtl($log, notify, $scope, $http, $rootScope, $window) {
    var dicts = "devdc";
    $scope.locOpt = [];
    $scope.locSel = "";
    $http.post($rootScope.project + "/api/zc/queryDictFast.do", {
        dicts: dicts,
        parts: "Y",
        partusers: "Y",
        uid: "dataexport"
    }).success(function (res) {
        if (res.success) {
            var gdicts = res.data;
            $scope.locOpt = gdicts.devdc;
            if ($scope.locOpt.length > 0) {
                $scope.locSel = $scope.locOpt[0];
            }
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.exportdata = function () {
        var ps = {}
        ps.loc = $scope.locSel.dict_item_id;
        $window.open($rootScope.project + "/api/base/res/exportAllRes.do?loc="
            + ps.loc);
    }
};
app.register.controller('cmdbDataExportCtl', cmdbDataExportCtl);