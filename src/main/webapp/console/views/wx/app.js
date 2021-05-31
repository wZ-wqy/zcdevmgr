function wxappsavectl(notify, $log, $uibModal, $uibModalInstance, $scope, id,
                      $http, $rootScope) {
    $scope.item = {};
    if (angular.isDefined(id)) {
        $http.post($rootScope.project + "/api/wx/queryWxAppById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
            } else {
            }
        })
    }
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/wx/saveWxApp.do", $scope.item)
            .success(function (res) {
                if (res.success) {
                    $uibModalInstance.close("OK");
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function wxappCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                  $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: []
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"modify('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"syncToWx('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">同步到微信</button>  ";
        acthtml = acthtml + " <button ng-click=\"syncFromWx('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">从微信同步</button>   ";
        acthtml = acthtml + " <button ng-click=\"del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div>  ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('app_id').withTitle('AppId').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('cdate').withTitle('创建时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('menu').withTitle('菜单').withOption(
            'sDefaultContent', '').withClass('none'),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '').withClass('none'),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/wx/queryWxApps.do", ps).success(
            function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    flush();
    $scope.flush = function () {
        flush();
    }
    $scope.modify = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/wx/modal_saveapp.html',
            controller: wxappsavectl,
            size: 'lg',
            resolve: {
                id: function () {
                    return id;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flush();
            }
        }, function (reason) {
        });
    }
    $scope.syncToWx = function (id) {
        $http.post($rootScope.project + "/api/wx/createMenuToWx.do", {
            id: id
        }).success(function (res) {
            notify({
                message: res.message
            });
        })
    }
    $scope.syncFromWx = function (id) {
        $http.post($rootScope.project + "/api/wx/sysncMenu.do", {
            id: id
        }).success(function (res) {
            notify({
                message: res.message
            });
        })
    }
    $scope.del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/wx/delWxapp.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    flush();
                }
                notify({
                    message: res.message
                });
            })
        });
    }
};
app.register.controller('wxappCtl', wxappCtl);