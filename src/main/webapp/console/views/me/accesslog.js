function meAccessLogCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                        $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: []
    }
    $scope.URL = $rootScope.project + "/api/sysLogAccess/selectPage.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST'
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 500)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', false).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        })
    $scope.dtInstance = {}
    $scope.reloadData = reloadData;

    function reloadData() {
        var resetPaging = false;
        $scope.dtInstance.reloadData(callback, resetPaging);
    }

    function callback(json) {
    }

    function renderAction(data, type, full) {
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('rtime').withTitle('日期').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('url').withTitle('访问地址').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('info').withTitle('说明').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('postorget').withTitle('参数').withOption(
            'sDefaultContent', '').withClass('none')]
    $scope.flush = function () {
        reloadData();
    }
};
app.register.controller('meAccessLogCtl', meAccessLogCtl);