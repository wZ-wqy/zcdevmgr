function zbxdashboardCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                         $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
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
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
// Use this headerCompiled field to only compile
// header once
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            });
    $scope.dtInstance = {}

    function renderTime(data, type, full) {
        return data;
    }

    function renderLevel(data, type, full) {
        if (data == "0") {
            return "全部";
        } else if (data == "1") {
            return "信息";
        } else if (data == "2") {
            return "警告";
        } else if (data == "3") {
            return "一般";
        } else if (data == "4") {
            return "严重";
        } else if (data == "5") {
            return "灾难";
        } else {
            return data
        }
    }

    function renderZX(data, type, full) {
        if (data == "0") {
            return "未知";
        } else {
            return "已知";
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('lastchange').withTitle('发生时间').withOption(
            'sDefaultContent', '').renderWith(renderTime),
        DTColumnBuilder.newColumn('hostname').withTitle('告警主机').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('lasteventname').withTitle('告警信息').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('severity').withTitle('告警等级').withOption(
            'sDefaultContent', '').renderWith(renderLevel),
        DTColumnBuilder.newColumn('acknowledged').withTitle('是否知晓').withOption(
            'sDefaultContent', '').renderWith(renderZX)
    ]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/trigger/getTriggers.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    flush();
    $scope.data = {}
    $http.post($rootScope.project + "/api/zbx/dashboard/getCountHost.do",
        {}).success(function (res) {
        $scope.data = res.data;
    })
    flush();
};
app.register.controller('zbxdashboardCtl', zbxdashboardCtl);