function totalassetsCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
        // DTColumnBuilder.newColumn('catrootname').withTitle('类目')
        //     .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('classname').withTitle('资产分类')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('tbuyprice').withTitle('资产原值').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tnetworth').withTitle('资产净值').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('taccumulateddepreciation').withTitle('累计折旧').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tcnt').withTitle('数量').withOption(
            'sDefaultContent', ''),
    ]
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
        $http.post($rootScope.project + "/api/zc/report/queryZcTotalAssets.do",
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
};
app.register.controller('totalassetsCtl', totalassetsCtl);