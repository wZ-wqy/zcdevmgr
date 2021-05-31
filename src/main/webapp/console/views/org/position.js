function positionSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.typeOpt = []
    $scope.typeSel = "";
    $scope.item = {};
    $http.post($rootScope.project + "/api/hrm/hrmPositionType/selectList.do", {}).success(function (res) {
        if (res.success) {
            if (res.data.length > 0) {
                $scope.typeOpt = res.data;
                if (res.data.length > 0) {
                    $scope.typeSel = $scope.typeOpt[0];
                }
                if (angular.isDefined(id)) {
                    //修改
                    $http.post($rootScope.project + "/api/hrm/hrmPosition/selectById.do", {
                        id: id
                    }).success(function (rs) {
                        if (rs.success) {
                            $scope.item = rs.data;
                            for (var i = 0; i < $scope.typeOpt.length; i++) {
                                if ($scope.typeOpt[i].id == rs.data.type) {
                                    $scope.typeSel = $scope.typeOpt[i];
                                    break;
                                }
                            }
                        } else {
                            notify({
                                message: rs.message
                            });
                        }
                    })
                } else {
                    // 新增
                }
            }
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.sure = function () {
        $scope.item.type = $scope.typeSel.id;
        $http.post($rootScope.project + "/api/hrm/hrmPosition/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function positionCtl($stateParams, DTOptionsBuilder, DTColumnBuilder,
                     $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [
            {
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
                show: true,
            },
            {
                id: "1",
                label: "新增",
                priv: "insert",
                show: true,
                type: "btn",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'responsive', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        // acthtml = acthtml + " <button ng-click=\"row_del('" + full.roleId
        //     + "')\" class=\"btn-white btn btn-xs\">删除</button>";
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
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('typestr').withTitle('类型').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('code').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $http.post($rootScope.project + "/api/hrm/hrmPosition/listPositions.do", ps)
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
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/org/modal_positionSave.html',
            controller: positionSaveCtl,
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
app.register.controller('positionCtl', positionCtl);