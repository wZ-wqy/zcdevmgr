function dictSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, id, $http, $rootScope) {
    $log.warn("window in:" + id);
    $scope.item = {};
    $scope.typeOpt = [{
        id: "system",
        name: "系统"
    }, {
        id: "biz",
        name: "业务"
    }]
    $scope.typeSel = $scope.typeOpt[0];
    $scope.statusOpt = [{
        id: "Y",
        name: "有效"
    }, {
        id: "N",
        name: "无效"
    }]
    $scope.statusSel = $scope.statusOpt[0];
    if (angular.isDefined(id)) {
        // 加载数据
        $http.post($rootScope.project + "/api/sysDict/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data
                // STATUS
                if ($scope.item.status == "Y") {
                    $scope.statusSel = $scope.statusOpt[0];
                } else if ($scope.item.status == "N") {
                    $scope.statusSel = $scope.statusOpt[1];
                }
                // DICT_LEVEL
                if ($scope.item.dictLevel == "system") {
                    $scope.typeSel = $scope.typeOpt[0];
                } else if ($scope.item.dictLevel == "biz") {
                    $scope.typeSel = $scope.typeOpt[1];
                }
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
        $scope.item.status = $scope.statusSel.id;
        $scope.item.dictLevel = $scope.typeSel.id;
        $http.post($rootScope.project + "/api/sysDict/insertOrUpdate.do",
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
}

function dictItemSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, data, $http, $rootScope) {
    $log.warn("window in:" + data);
    $scope.item = {};
    $scope.item.dictId = data.dictId
    if (angular.isDefined(data.dictItemId)) {
        // 加载数据
        $http.post($rootScope.project + "/api/sysDictItem/selectById.do", {
            id: data.dictItemId
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                $scope.item.dictId = data.dictId;
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
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/sysDictItem/insertOrUpdate.do",
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
}

function sysDictSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                           $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                           $stateParams) {
    $scope.crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
        "item_insert": false,
        "item_update": false,
        "item_remove": false
    };
    privCrudCompute($scope.crud, $rootScope.curMemuBtns);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withPaginationType(
        'full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption("paging", false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', false)
        .withOption('serverSide', false).withOption('bAutoWidth', true)
        .withOption('rowCallback', rowCallback).withOption('createdRow',
            function (row) {
                // Recompiling so we can bind Angular,directive to the
                $compile(angular.element(row).contents())($scope);
            }).withOption("select", {
            style: 'single'
        });

    function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        // Unbind first in order to avoid any duplicate handler
        // (see https://github.com/l-lin/angular-datatables/issues/87)
        $('td', nRow).unbind('click');
        $('td', nRow).bind('click', function () {
            $scope.$apply(function () {
                someClickHandler(aData);
            });
        });
        return nRow;
    }

    function someClickHandler(data) {
        flushSubtab(data.dictId);
    }

    function renderMType(data, type, full) {
        if (data == "system") {
            return "系统";
        } else if (data == "biz") {
            return "业务";
        } else {
            return data;
        }
    }

    function renderMStatus(data, type, full) {
        if (data == "Y") {
            return "有效";
        } else if (data == "N") {
            return "无效";
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('dictLevel').withTitle('类型').withOption(
            'sDefaultContent', '').renderWith(renderMType),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderMStatus)]
    $scope.dtInstance = {}

    function flush() {
        var ps = {};
        $http.post($rootScope.project + "/api/sysDict/ext/selectList.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                    $scope.dtItemOptions.aaData = [];
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    flush();

    function save(id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/dict/modal_dictSave.html',
            controller: dictSaveCtl,
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

    $scope.add = function () {
        save()
    }
    $scope.del = function () {
        var id = getSelectId();
        if (!angular.isDefined(id)) {
            notify({
                message: "请选择一行"
            });
            return;
        }
        $confirm({
            text: '是否删除?'
        })
            .then(
                function () {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/sysDict/selectById.do",
                            {
                                id: id
                            })
                        .success(
                            function (res) {
                                if (res.success) {
                                    if (res.data.dictLevel == "system") {
                                        notify({
                                            message: "类型为系统类型,不允许删除"
                                        });
                                    } else {
                                        $http
                                            .post(
                                                $rootScope.project
                                                + "/api/sysDict/deleteById.do",
                                                {
                                                    id: id
                                                })
                                            .success(
                                                function (
                                                    res) {
                                                    if (res.success) {
                                                        flush();
                                                    }
                                                    notify({
                                                        message: res.message
                                                    });
                                                })
                                    }
                                }
                            })
                });
    }
    $scope.update = function () {
        var id = getSelectId();
        if (angular.isDefined(id)) {
            save(id)
        } else {
            notify({
                message: "请选择一行"
            });
        }
    }

    function getSelectId() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        // 没有选择,或选择多行都返回错误
        if (data.length == 0 || data.length > 1) {
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]].dictId;
        }
    }

    /** ********************子表******************* */
    $scope.dtItemOptions = DTOptionsBuilder.fromFnPromise().withPaginationType(
        'full_numbers').withDisplayLength(25).withOption("ordering", false)
        .withOption("responsive", false).withOption("searching", false)
        .withOption("paging", false).withOption('bStateSave', true)
        .withOption('bProcessing', true).withOption('bFilter', false)
        .withOption('bInfo', true).withOption('serverSide', false)
        .withOption('bAutoWidth', false).withOption('createdRow',
            function (row) {
                // Recompiling so we can bind Angular,directive to the
                $compile(angular.element(row).contents())($scope);
            });
    $scope.dtItemInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if ($scope.crud.item_update) {
            acthtml = acthtml + " <button ng-click=\"row_update('"
                + full.dictItemId
                + "')\" class=\"btn-white btn btn-xs\">更新</button>   ";
        }
        if ($scope.crud.item_remove) {
            acthtml = acthtml + " <button ng-click=\"row_dtl('"
                + full.dictItemId
                + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
        }
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    $scope.dtItemColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('code').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('sort').withTitle('排序').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('dict_id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flushSubtab(id) {
        var ps = {
            dictId: id
        };
        // id不存在,则尝试从select中获取
        if (!angular.isDefined(id)) {
            ps.dictId = getSelectId();
        }
        // 如果还是不存在则报错
        if (!angular.isDefined(ps.dictId)) {
            notify({
                message: "ID不存在"
            });
            return;
        }
        $http.post(
            $rootScope.project
            + "/api/sysDictItem/selectDictItemByDict.do?", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtItemOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    $scope.itemquery = function () {
        flushSubtab();
    }
    $scope.row_update = function (id) {
        var ps = {};
        ps.dictItemId = id;
        ps.dictId = getSelectId();
        if (!angular.isDefined(ps.dictId)) {
            notify({
                message: "ID不存在"
            });
            return;
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/dict/modal_dictItemSave.html',
            controller: dictItemSaveCtl,
            size: 'lg',
            resolve: {
                data: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flushSubtab();
            }
        }, function (reason) {
        });
    }
    $scope.row_dtl = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/sysDictItem/deleteById.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    flushSubtab();
                }
                notify({
                    message: res.message
                });
            })
        });
    }
};
app.register.controller('sysDictSettingCtl', sysDictSettingCtl);