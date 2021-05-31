function catusedreportCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
    $scope.dtColumns.push(DTColumnBuilder.newColumn('catrootname').withTitle('类目').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('catname').withTitle('分类').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('idle').withTitle('闲置').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('borrow').withTitle('借用').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('inuse').withTitle('在用').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('stopuse').withTitle('停用').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('scrap').withTitle('报废').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('allocation').withTitle('调拨中').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('repair').withTitle('维修中').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    // $http.post($rootScope.project + "/api/sysDict/ext/selectItemListByDictId.do",
    // 	{dict_id:"devrecycle"}).success(function(res) {
    // 	if (res.success) {
    // 		for(var i=0;i<res.data.length;i++){
    // 			dtColumns.push( DTColumnBuilder.newColumn(res.data[i].code).withTitle(res.data[i].name).withOption(
    // 				'sDefaultContent', '').withOption('width', '30'));
    // 		}
    // 		$scope.dtColumns=dtColumns
    // 		console.log($scope.dtColumns);
    // 		$scope.dtInstance = {}
    // 	} else {
    // 		notify({
    // 			message : res.message
    // 		});
    // 	}
    // })
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
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            }]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/report/queryCatUsedReport.do",
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
app.register.controller('catusedreportCtl', catusedreportCtl);