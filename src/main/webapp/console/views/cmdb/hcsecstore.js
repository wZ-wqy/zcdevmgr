function hcsecStoreCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('route_name').withTitle('物品类别').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('unit').withTitle('计量单位').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('brandmark').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('downcnt').withTitle('安全库存下限').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('upcnt').withTitle('安全库存上限制').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('当前数量').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('msg').withTitle('状态').withOption(
        'sDefaultContent', '').withOption('width', '30'));

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('" + full.used_userid
            + "')\" class=\"btn-white btn btn-xs\">详情</button> </div> ";
        return acthtml;
    }

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
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            }]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/resInout/ext/selectSafetyStore.do",
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
app.register.controller('hcsecStoreCtl', hcsecStoreCtl);