function sysParamSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, id, $http, $rootScope) {
    $log.warn("window in:" + id);
    $scope.item = {};
    $scope.typeOpt = [{
        id: "system",
        name: "系统"
    }, {
        id: "user",
        name: "用户"
    }]
    $scope.typeSel = $scope.typeOpt[0];
    if (angular.isDefined(id)) {
        // 加载数据
        $http.post($rootScope.project + "/api/sysParams/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data
                // DICT_LEVEL
                if ($scope.item.dict_level == "system") {
                    $scope.typeSel = $scope.typeOpt[0];
                } else if ($scope.item.dict_level == "user") {
                    $scope.typeSel = $scope.typeOpt[1];
                }
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
    $scope.sure = function () {
        $scope.item.type = $scope.typeSel.id;
        $http.post($rootScope.project + "/api/sysParams/insertOrUpdate.do",
            $scope.item).success(function (res) {
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

function sysParamsCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                      $log, notify, $scope, $http, $rootScope, $uibModal, $stateParams) {
    $scope.meta = {
        tools: [{
            id: "0",
            priv: "select",
            label: "刷新",
            type: "btn_query",
            hide: false,
        }, {
            id: "1",
            priv: "insert",
            label: "新增",
            type: "btn_add",
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
        'bAutoWidth', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderStatus(data, type, full) {
        var value = data
        if (data == 'system') {
            value = '系统'
        } else if (data == 'user') {
            value = '用户'
        }
        return value;
    }

    /** base*** */

    function dt_renderCRUDction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if (crud.update) {
            acthtml = acthtml + " <button ng-click=\"update('" + full.id
                + "')\" class=\"btn-white btn btn-xs\">更新</button>   ";
        }
        if (crud.remove) {
            acthtml = acthtml + " <button ng-click=\"del('" + full.id
                + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
        }
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderValue(data, type, full) {
        if (angular.isDefined(data) && data.length > 35) {
            return data.substr(0, 30) + '...';
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('value').withTitle('值').withOption(
            'sDefaultContent', '').renderWith(renderValue),
        DTColumnBuilder.newColumn('type').withTitle('类型').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').renderWith(
            dt_renderCRUDction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/sysParams/selectList.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                }
            })
    }

    $scope.btn_query = function () {
        flush();
    }
    $scope.update = function (id) {
        save(id);
    }

    function save(id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/params/modal_paramsSave.html',
            controller: sysParamSaveCtl,
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

    $scope.btn_add = function () {
        save();
    }
    $scope.del = function (id) {
        if (angular.isDefined(id)) {
            // 删除
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/sysParams/deleteById.do", {
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
        } else {
            notify({
                message: "请选择一行"
            });
        }
    }
    flush();
};
app.register.controller('sysParamsCtl', sysParamsCtl);