function sysStoreSqlSaveCtl(notify, $log, $uibModal, $uibModalInstance, $scope, id, $http, $rootScope, $timeout) {
    $scope.usedOpt = [{
        id: "Y",
        name: "正常"
    }, {
        id: "N",
        name: "停用"
    }];
    $scope.usedSel = $scope.usedOpt[0];
    $scope.aclOpt = [{
        id: "public",
        name: "公共"
    }, {
        id: "user",
        name: "用户"
    }, {
        id: "system",
        name: "系统"
    }];
    $scope.aclSel = $scope.aclOpt[0];
    $scope.returnOpt = [{
        id: "array",
        name: "数组"
    }, {
        id: "object",
        name: "对象"
    }, {
        id: "action",
        name: "行为"
    }];
    $scope.returnSel = $scope.returnOpt[0];
    if (angular.isDefined(id)) {
        // 加载数据
        $http.post($rootScope.project + "/api//store/queryStoreSqlById.do", {
            store_id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                if ($scope.item.is_used == "Y") {
                    $scope.usedSel = $scope.usedOpt[0];
                } else if ($scope.item.is_used == "N") {
                    $scope.usedSel = $scope.usedOpt[1];
                }
                if (res.data.acl == "public") {
                    $scope.aclSel = $scope.aclOpt[0];
                } else if (res.data.acl == "user") {
                    $scope.aclSel = $scope.aclOpt[1];
                } else if (res.data.acl == "system") {
                    $scope.aclSel = $scope.aclOpt[2];
                }
                if (res.data.return_type == "array") {
                    $scope.returnSel = $scope.returnOpt[0];
                } else if (res.data.return_type == "object") {
                    $scope.returnSel = $scope.returnOpt[1];
                } else if (res.data.return_type == "action") {
                    $scope.returnSel = $scope.returnOpt[2];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.sure = function () {
        $scope.item.is_used = $scope.usedSel.id;
        $scope.item.acl = $scope.aclSel.id;
        $scope.item.return_type = $scope.returnSel.id;
        $http.post($rootScope.project + "/api/store/saveStoreSql.do", $scope.item).success(function (res) {
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

function sysStoreSqlCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [{
            id: "1",
            label: "新增",
            type: "btn",
            template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
        }]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.store_id + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.store_id + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    function renderStatus(data, type, full) {
        if (data == "Y") {
            return "正常";
        } else if (data == "N") {
            return "停用";
        } else {
            return data;
        }
    }

    function renderACL(data, type, full) {
        if (data == "public") {
            return "公共";
        } else if (data == "user") {
            return "用户";
        } else if (data == "system") {
            return "系统";
        } else {
            return data;
        }
    }

    function renderRT(data, type, full) {
        if (data == "action") {
            return "行为";
        } else if (data == "array") {
            return "数组";
        } else if (data == "object") {
            return "对象";
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('store_id').withTitle('ID').withOption('sDefaultContent', ''), DTColumnBuilder.newColumn('name').withTitle('名称').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('acl').withTitle('访问类型').withOption('sDefaultContent', '').renderWith(renderACL),
        DTColumnBuilder.newColumn('return_type').withTitle('返回类型').withOption('sDefaultContent', '').renderWith(renderRT),
        DTColumnBuilder.newColumn('is_used').withTitle('状态').withOption('sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('sqltext').withTitle('SQL文本').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('user_id').withTitle('操作').withOption('sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/store/queryStoreSql.do", ps).success(function (res) {
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
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/store/deleteStoreSql.do", {
                store_id: id
            }).success(function (res) {
                flush();
            })
        });
    }
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/store/modal_storesqlSave.html',
            controller: sysStoreSqlSaveCtl,
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
app.register.controller('sysStoreSqlCtl', sysStoreSqlCtl);