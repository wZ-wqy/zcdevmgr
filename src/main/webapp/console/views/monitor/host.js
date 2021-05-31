function zbxhostCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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

    function renderIp(data, type, full) {
        if (data.length > 0) {
            return data[0].ip + ":" + data[0].port;
        }
    }

    function renderHgroup(data, type, full) {
        var text = "";
        for (var i = 0; i < data.length; i++) {
            text = data[i].name + " " + text;
        }
        return text;
    }

    function renderTpl(data, type, full) {
        var text = "";
        for (var i = 0; i < data.length; i++) {
            text = data[i].name + "," + text;
        }
        return text;
    }

    function renderStatus(data, type, full) {
        if (data == "0") {
            return "启用"
        }
    }

    function renderAvailable(data, type, full) {
        if (data == "0") {
            return "未知"
        } else if (data == "1") {
            return "正常"
        } else if (data == "2") {
            return "异常"
        } else {
            return data
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('可见名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('interfaces').withTitle('接口IP').withOption(
            'sDefaultContent', '').renderWith(renderIp),
        DTColumnBuilder.newColumn('groups').withTitle('主机组').withOption(
            'sDefaultContent', '').renderWith(renderHgroup),
        DTColumnBuilder.newColumn('parentTemplates').withTitle('模版').withOption(
            'sDefaultContent', '').renderWith(renderTpl),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('available').withTitle('监控状态')
            .withOption('sDefaultContent', '').renderWith(renderAvailable)]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/host/hostList.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    flush();
};
app.register.controller('zbxhostCtl', zbxhostCtl);