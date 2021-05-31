function modaluserzcCtl($timeout, $localStorage, notify, $log, $uibModal,
                        $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                        DTColumnBuilder, $compile) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');
    var ps = {}
    if (angular.isDefined(meta.userid) && meta.userid != "undefined") {
        ps.userid = meta.userid;
    }
    $http
        .post($rootScope.project + "/api/zc/report/queryEmployeeUsedByUser.do",
            ps).success(function (res) {
        if (res.success) {
            $scope.dtOptions.aaData = res.data;
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function employeezcCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                       $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window, $stateParams) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('bAutoWidth', false)
        .withOption('paging', true).withOption('bStateSave', true)
        .withOption('bProcessing', false).withOption('bFilter', false)
        .withOption('bInfo', true).withOption('serverSide', false)
        .withOption('createdRow',
            function (row) {
                // Recompiling so we can bind Angular,directive to the
                $compile(angular.element(row).contents())($scope);
            });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('" + full.used_userid
            + "')\" class=\"btn-white btn btn-xs\">详情</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('part_fullname').withTitle($rootScope.USEDPART)
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('username').withTitle('姓名')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('usermail').withTitle('邮箱')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('usertel').withTitle('手机')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('cnt').withTitle('数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('part_id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [
            {
                id: "btn",
                show: true,
                label: "",
                type: "btn",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            }]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/report/queryEmployeeUsedReport.do",
            ps).success(function (res) {
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
    $scope.detail = function (userid) {
        var ps = {};
        ps.userid = userid;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/rep/modal_zcreportlist.html',
            controller: modaluserzcCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
            }
        }, function (reason) {
        });
    }
};
app.register.controller('employeezcCtl', employeezcCtl);