function zcapprovalnodeSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                               $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.statusOpt = [{"id": "valid", name: "有效"}, {"id": "invalid", name: "无效"}]
    $scope.statusSel = $scope.statusOpt[0];
    $scope.data = {};
    $scope.userOpt = [];
    $scope.userSel = {};
    var dicts = "zcdoctype";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            partusers: "Y",
            comp: "N",
            belongcomp: "N",
            zccatused: "N",
            uid: "approvalnodedata"
        })
        .success(
            function (res) {
                if (res.success) {
                    if (res.data.partusers.length > 0) {
                        $scope.userOpt = res.data.partusers;
                        if ($scope.userOpt.length > 0) {
                            //$scope.userOpt = $scope.userOpt[0];
                        }
                    }
                    if (angular.isDefined(id)) {
                        //修改
                        $http.post($rootScope.project + "/api/zc/resApprovalnode/selectById.do", {
                            id: id
                        }).success(function (rs) {
                            if (rs.success) {
                                $scope.data = rs.data;
                                if (angular.isDefined(rs.data.status)) {
                                    if (rs.data.status == "valid") {
                                        $scope.statusSel = $scope.statusOpt[0];
                                    } else {
                                        $scope.statusSel = $scope.statusOpt[1];
                                    }
                                }
                                if (angular.isDefined(rs.data.userid) && $scope.userOpt.length > 0) {
                                    for (var i = 0; i < $scope.userOpt.length; i++) {
                                        if (rs.data.userid == $scope.userOpt[i].user_id) {
                                            $scope.userSel = $scope.userOpt[i];
                                            break;
                                        }

                                    }
                                }

                            } else {
                                notify({
                                    message: rs.message
                                });
                            }
                        })
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })

    $scope.sure = function () {
        $scope.data.status = $scope.statusSel.id;
        $scope.data.userid = $scope.userSel.user_id;
        $scope.data.username = $scope.userSel.name;
        $http.post($rootScope.project + "/api/zc/resApprovalnode/insertOrUpdate.do",
            $scope.data).success(function (res) {
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

function zcapprovalnodeCtl($stateParams, DTOptionsBuilder, DTColumnBuilder,
                           $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [
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
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    renderStatus

    function renderStatus(data, type, full) {
        if (data == "valid") {
            return "有效";
        } else if (data == "invalid") {
            return "无效";
        } else {
            return data;
        }

    }

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>";
        acthtml = acthtml + "</div>";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('审批节点').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('code').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('username').withTitle('绑定用户').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/resApprovalnode/selectList.do", ps)
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
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/zc/resApprovalnode/deleteById.do", {
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
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/flow/modal_approvalnodesave.html',
            controller: zcapprovalnodeSaveCtl,
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
app.register.controller('zcapprovalnodeCtl', zcapprovalnodeCtl);