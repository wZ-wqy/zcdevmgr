function roleSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.statusOpt = [{
        id: "Y",
        name: "有效"
    }, {
        id: "N",
        name: "无效"
    }]
    $scope.statusSel = $scope.statusOpt[0];
    $scope.item = {};
    if (angular.isDefined(id)) {
        // 修改
        $http.post($rootScope.project + "/api/sysRoleInfo/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data
                if ($scope.item.isAction == "Y") {
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
        $scope.item.isAction = $scope.statusSel.id;
        $http.post($rootScope.project + "/api/sysRoleInfo/insertOrUpdate.do",
            $scope.item).success(function (res) {
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
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                adom[i].style.width = "100%";
            }
        }
    }, 200);
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function sysRoleSettingCtl($stateParams, DTOptionsBuilder, DTColumnBuilder,
                           $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [  {
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入搜索内容",
            type: "input",
            ct: ""
        },
            {
                id: "0",
                priv: "select",
                label: "查询",
                type: "btn_query",
                hide: false,
            },
            {
                id: "1",
                label: "新增",
                priv: "insert",
                show: false,
                type: "btn",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }]
    }
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
    var crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
    };
    privCrudCompute(crud, $rootScope.curMemuBtns);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'responsive', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if (crud.update) {
            acthtml = acthtml + " <button ng-click=\"save('" + full.roleId
                + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        }
        if (crud.remove) {
            acthtml = acthtml + " <button ng-click=\"row_del('" + full.roleId
                + "')\" class=\"btn-white btn btn-xs\">删除</button>";
        }
        acthtml = acthtml + "</div>";
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var res = "无效";
        if (full.isAction == "Y") {
            res = "有效";
        }
        return res;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('roleName').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('isAction').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('remark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('roleId').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $http.post($rootScope.project + "/api/sysRoleInfo/selectList.do", ps)
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
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除功能?'
        }).then(function () {
            $http.post($rootScope.project + "/api/sysRoleInfo/deleteById.do", {
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
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/role/modal_role_save.html',
            controller: roleSaveCtl,
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
app.register.controller('sysRoleSettingCtl', sysRoleSettingCtl);