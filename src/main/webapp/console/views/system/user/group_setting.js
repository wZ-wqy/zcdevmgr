function groupSaveFormCtl($localStorage, notify, $log, $uibModal,
                          $uibModalInstance, $scope, id, $http, $rootScope) {
    $log.warn("window in:", id);
    $scope.item = {}
    if (angular.isDefined(id)) {
        $http.post($rootScope.project + "/api/sysUserGroup/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    } else {
    }
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/sysUserGroup/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            }
            notify({
                message: res.message
            });
        });
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function sysGroupSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                            $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                            $stateParams) {
    $scope.meta = {
        tools: [
            {
                id: "0",
                priv: "select",
                label: "查询",
                type: "btn_query",
                hide: false,
            },
            {
                id: "1",
                priv: "insert",
                label: "新增",
                type: "btn",
                show: false,
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }]
    }
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}
    var crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
    };
    privCrudCompute(crud, $rootScope.curMemuBtns);

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if (crud.update) {
            acthtml = acthtml + " <button ng-click=\"save('" + full.groupId
                + "')\" class=\"btn-white btn btn-xs\">更新</button>   ";
        }
        if (crud.remove) {
            acthtml = acthtml + " <button ng-click=\"row_delete('"
                + full.groupId
                + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        }
        acthtml = acthtml + " </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('groupId').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        $http.post($rootScope.project + "/api/sysUserGroup/selectList.do", {})
            .success(function (res) {
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
    $scope.btn_query = function () {
        flush();
    }
    $scope.row_delete = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/sysUserGroup/deleteById.do", {
                        id: id
                    }).success(function (res) {
                    flush();
                    notify({
                        message: res.message
                    });
                });
            });
    }
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/user/modal_group_save.html',
            controller: groupSaveFormCtl,
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
app.register.controller('sysGroupSettingCtl', sysGroupSettingCtl);