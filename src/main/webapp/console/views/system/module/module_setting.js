function moduleSaveCtl($localStorage, notify, $log, $uibModal, $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.statusOpt = [{
        ID: "Y",
        NAME: "有效"
    }, {
        ID: "N",
        NAME: "无效"
    }]
    $scope.statusSel = $scope.statusOpt[0];
    $scope.item = {};
    if (angular.isDefined(id)) {
        // 修改
        $http.post($rootScope.project + "/module/moduleQueryById.do", {
            MODULE_ID: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data
                if ($scope.item.IS_ACTION == "Y") {
                    $scope.statusSel = $scope.statusOpt[0];
                } else {
                    $scope.statusSel = $scope.statusOpt[1];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    } else {
        // 新增
    }
    $scope.sure = function () {
        $scope.item.IS_ACTION = $scope.statusSel.ID;
        $http.post($rootScope.project + "/module/moduleSave.do", $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
                notify({
                    message: res.message
                });
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function sysModuleCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withPaginationType('full_numbers').withDisplayLength(25).withOption("ordering", false).withOption("responsive", true)
        .withOption("searching", false).withOption("paging", false).withOption('bStateSave', true).withOption('bProcessing', true).withOption('bFilter', false).withOption(
            'bInfo', true).withOption('serverSide', false).withOption('bAutoWidth', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.MODULE_ID + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        //acthtml = acthtml + " <button ng-click=\"row_detail()\" class=\"btn-white btn btn-xs\">详细</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.MODULE_ID + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var res = "无效";
        if (full.IS_ACTION == "Y") {
            res = "有效";
        }
        return res;
    }

    $scope.dtColumns = [DTColumnBuilder.newColumn('MODULE_ID').withTitle('编号').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('MODULE_NAME').withTitle('名称').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('IS_ACTION').withTitle('状态').withOption('sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('MARK').withTitle('备注').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('MODULE_ID').withTitle('操作').withOption('sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/module/module_query.do", ps).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            }
        })
    }

    flush();
    $scope.row_detail = function (id) {
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除功能?'
        }).then(function () {
            $http.post($rootScope.project + "/role/roleDelete.do", {
                role_id: id
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
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/module/modal_module_save.html',
            controller: moduleSaveCtl,
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
};
app.register.controller('sysModuleCtl', sysModuleCtl);