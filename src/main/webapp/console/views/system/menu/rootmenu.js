function rootMenuSaveCtl($localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, id, $http, $rootScope, $timeout) {
    $scope.statusOpt = [{
        id: "1",
        name: "正常"
    }, {
        id: "0",
        name: "停用"
    }]
    $scope.statusSel = $scope.statusOpt[0];
    $scope.item = {};
    if (angular.isDefined(id)) {
        // 修改
        $http.post($rootScope.project + "/api/sysMenus/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data
                if ($scope.item.used == "1") {
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
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                adom[i].style.width = "100%";
            }
        }
    }, 200);
    $scope.sure = function () {
        $scope.item.used = $scope.statusSel.id;
        $http.post($rootScope.project + "/api/sysMenus/insertOrUpdate.do",
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
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function sysRootMenugCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                         $log, notify, $scope, $http, $rootScope, $uibModal, $stateParams) {
    $scope.meta = {
        tablehide: false,
        tools: [
            {
                id: "1",
                label: "刷新",
                type: "btn",
                hide: false,
                priv: 'select',
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            }, {
                id: "1",
                priv: "insert",
                label: "新增",
                type: "btn",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>',
                hide: false,
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
        'createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if (crud.update) {
            acthtml = acthtml + " <button ng-click=\"save('" + full.menuId
                + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        }
        if (crud.remove) {
            acthtml = acthtml + " <button ng-click=\"row_del('" + full.menuId
                + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        }
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var res = "停用";
        if (data == "1") {
            res = "正常";
        }
        return res;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('used').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('menu_id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/sysMenus/selectList.do", ps)
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
    $scope.query = function () {
        flush();
    }
    $scope.row_detail = function (id) {
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除功能?'
        }).then(function () {
            $http.post($rootScope.project + "/api/sysMenus/deleteById.do", {
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
            templateUrl: 'views/system/menu/modal_saverootmenu.html',
            controller: rootMenuSaveCtl,
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
app.register.controller('sysRootMenugCtl', sysRootMenugCtl);