function zbxtplsCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                    $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.meta = {
        tablehide: false,
        tools: [
            {
                id: "1",
                label: "刷新",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
        ]
    }
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

    function renderHost(data, type, full) {
        return data.length;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('templateid').withTitle('编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('可见名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('applications').withTitle('应用集').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('items').withTitle('指标总').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('triggers').withTitle('触发器').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('graphs').withTitle('图形').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('discoveries').withTitle('自动发现规则').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('hosts').withTitle('关联主机数').withOption(
            'sDefaultContent', '').renderWith(renderHost)
    ]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/template/getTemplate.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    flush();
};
app.register.controller('zbxtplsCtl', zbxtplsCtl);