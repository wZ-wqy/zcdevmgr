function objcatesaveCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $timeout, $localStorage, notify, $log, $uibModal,
                        $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.groupOpt = [];
    $scope.groupSel = {}
    $http.post($rootScope.project + "/api/zbx/zbxObjectGroup/selectList.do",
        {}).success(function (res) {
        $scope.groupOpt = res.data;
        $scope.groupSel = res.data[0];
    })
    $scope.item = {};
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 400)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });

    function stateChange(iColumn, bVisible) {
    }

    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('groupid').withTitle('编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('主机组').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('hosts').withTitle('主机数').withOption(
            'sDefaultContent', '')]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/hostgroup/getAllHostGroups.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    flush();
    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
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

    function getSelectRows() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1000) {
            notify({
                message: "不允许超过1000个"
            });
            return;
        } else {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var e = {};
                e.objid = $scope.dtOptions.aaData[data[i]].groupid;
                e.objname = $scope.dtOptions.aaData[data[i]].name;
                res.push(e)
            }
            return angular.toJson(res);
        }
    }

    $scope.sure = function () {
        var selrows = getSelectRows();
        var ps = {};
        if (angular.isDefined(selrows)) {
            ps.groupid = $scope.groupSel.id;
            ps.items = selrows;
            $http.post($rootScope.project + "/api/zbx/zbxObjectItem/ext/batchinsert.do",
                ps).success(function (res) {
                if (res.success) {
                    $uibModalInstance.close("OK");
                } else {
                }
                notify({
                    message: res.message
                });
            })
        } else {
        }
    }
}

function objcateCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                    $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.objcateOpt = [];
    $scope.objcateSel = {}
    $http.post($rootScope.project + "/api/zbx/zbxObjectGroup/selectList.do",
        {}).success(function (res) {
        $scope.objcateOpt = res.data;
        if (res.data.length > 0) {
            $scope.objcateSel = res.data[0];
            flush();
        }
    })
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', false).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"remove('"
            + full.id
            + "','" + full.code + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
        acthtml = acthtml + " </div>";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('objid').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('objname').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        ps.groupid = $scope.objcateSel.id;
        $http.post($rootScope.project + "/api/zbx/zbxObjectItem/ext/selectByGroupId.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    $scope.remove = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/zbx/zbxObjectItem/deleteById.do", {
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
    $scope.add = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/monitor/modal_objcatesave.html',
            controller: objcatesaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return "";
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
app.register.controller('objcateCtl', objcateCtl);