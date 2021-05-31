function areasettingSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, id, $http, $rootScope) {
    $log.warn("window in:" + id);

    if (angular.isDefined(id)) {
        // 加载数据
        $http.post($rootScope.project + "/api/ops/opsArea/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data

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
        $http.post($rootScope.project + "/api/ops/opsArea/insertOrUpdate.do",
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

function arealayerCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, data, $http, $rootScope) {
    $log.warn(data);
    $scope.locOpt=[];
    $scope.locSel={};
    var dicts = "devdc";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            uid: "generic_devdc",
            dicts: dicts,
            parts: "N",
            partusers: "N",
            comp: "N",
            belongcomp: "N",
        })
        .success(
            function (res) {
                if (res.success) {
                    $scope.locOpt= res.data.devdc;
                    if($scope.locOpt.length>0){
                        $scope.locSel=$scope.locOpt[0];
                    }


                    if (angular.isDefined(data.id)) {
                        // 加载数据
                        $http.post($rootScope.project + "/api/ops/opsLayer/selectById.do", {
                            id:data.id
                        }).success(function (rs) {
                            if (rs.success) {
                              $scope.item=rs.data;
                              $scope.locSel={}
                              for(var i=0;i<$scope.locOpt.length;i++){
                                  if($scope.locOpt[i].dict_item_id==rs.data.locid){
                                      $scope.locSel=$scope.locOpt[i];
                                      break;
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
        if(!angular.isDefined($scope.locSel.dict_item_id)){
            notify({
                message: "请选择位置"
            });

        }
        $scope.item.locid=$scope.locSel.dict_item_id;
        $scope.item.areaid=data.areaid;
        $http.post($rootScope.project + "/api/ops/opsLayer/insertOrUpdate.do",
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

function areaSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                           $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                           $stateParams) {


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
        flushSubtab(data.id);
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
        DTColumnBuilder.newColumn('name').withTitle('区域名称').withOption(
            'sDefaultContent', '')]
    $scope.dtInstance = {}

    function flush() {
        var ps = {};
        $http.post($rootScope.project + "/api/ops/opsArea/selectList.do", ps)
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
            templateUrl: 'views/cmdb/infrastructure/modal_areasetting.html',
            controller: areasettingSaveCtl,
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
                            + "/api/ops/opsArea/selectById.do",
                            {
                                id: id
                            })
                        .success(
                            function (res) {
                                if (res.success) {
                                    if (res.data.name == "system") {
                                        notify({
                                            message: "类型为系统类型,不允许删除"
                                        });
                                    } else {
                                        $http
                                            .post(
                                                $rootScope.project
                                                + "/api/ops/opsArea/deleteById.do",
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
            return $scope.dtOptions.aaData[data[0]].id;
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

        acthtml = acthtml + " <button ng-click=\"row_update('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button>   ";

        acthtml = acthtml + " <button ng-click=\"row_dtl('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> ";

        acthtml = acthtml + "</div>"
        return acthtml;
    }

    $scope.dtItemColumns = [
        DTColumnBuilder.newColumn('name').withTitle('所在位置').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('locname').withTitle('映射位置').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rackcnt').withTitle('机柜数').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('dict_id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flushSubtab(id) {
        var ps = {
            areaid: id
        };
        // id不存在,则尝试从select中获取
        if (!angular.isDefined(id)) {
            ps.areaid = getSelectId();
        }
        // 如果还是不存在则报错
        if (!angular.isDefined(ps.areaid)) {
            notify({
                message: "ID不存在"
            });
            return;
        }
        $http.post(
            $rootScope.project
            + "/api/ops/opsLayer/ext/selectByAreaId.do?", ps)
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
        ps.id = id;
        ps.areaid = getSelectId();
        if (!angular.isDefined(ps.areaid)) {
            notify({
                message: "ID不存在"
            });
            return;
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/infrastructure/modal_arealayer.html',
            controller: arealayerCtl,
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
            $http.post($rootScope.project + "/api/ops/opsLayer/deleteById.do", {
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
app.register.controller('areaSettingCtl', areaSettingCtl);