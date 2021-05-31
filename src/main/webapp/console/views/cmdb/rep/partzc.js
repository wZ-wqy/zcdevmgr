function modalpartzcCtl($timeout, $localStorage, notify, $log, $uibModal,
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
    ps.part_id = meta.part_id;
    $http
        .post($rootScope.project + "/api/zc/report/queryPartUsedByPart.do",
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

function cmdbrepPartZcCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
        acthtml = acthtml + " <button ng-click=\"detail('" + full.part_id
            + "')\" class=\"btn-white btn btn-xs\">详情</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('part_fullname').withTitle($rootScope.USEDPART)
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('zc_cnt').withTitle('数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('part_id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [{
            id: "select",
            label: "类目",
            type: "select",
            disablesearch: true,
            dataOpt: [],
            dataSel: "",
            show: true,
        },
            {
                id: "btn",
                show: true,
                label: "",
                type: "btn",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            }]
    }
    $scope.meta = meta;
    $http.post($rootScope.project + "/api/zc/selectZcCats.do",
        {}).success(function (res) {
        if (res.success) {
            var temp = res.data;
            temp.unshift({
                id: "all",
                name: "全部"
            });
            $scope.meta.tools[0].dataOpt = temp;
            $scope.meta.tools[0].dataSel = temp[0];
        } else {
            notify({
                message: res.message
            });
        }
    })

    function flush() {
        var ps = {}
        if ($scope.meta.tools[0].dataSel.id != "all") {
            ps.catid = $scope.meta.tools[0].dataSel.id;
        }
        $http.post($rootScope.project + "/api/zc/report/queryPartUsedReport.do",
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
    $scope.detail = function (id) {
        var ps = {};
        ps.part_id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/rep/modal_zcreportlist.html',
            controller: modalpartzcCtl,
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
app.register.controller('cmdbrepPartZcCtl', cmdbrepPartZcCtl);