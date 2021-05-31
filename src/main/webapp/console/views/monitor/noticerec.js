function zbxnoticerecCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
        .withOption('scrollX', true).withOption('bAutoWidth', false)
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

    function renderType(data, type, full) {
        if (full.mediatypes.length > 0) {
            return full.mediatypes[0].name;
        }
    }

    function renderStatus(data, type, full) {
        if (data == "2") {
            return "<span style=\"color:red;font-weight:bold \">失败</span>"
        } else {
            return data;
        }
    }

    function renderRec(data, type, full) {
        var u;
        if (full.users.length > 0) {
            u = full.users[0].alias;
        }
        var txt = full.sendto;
        if (angular.isDefined(u)) {
            txt = txt + "<br>" + "(" + u + ")"
        }
        return txt;
    }

    function renderMsg(data, type, full) {
        var html = "";
        html = "<span style=\"font-weight:bold \">主题:</span><br><textarea readonly=\"readonly\" style=\"width:300px;height:60px\">" + full.subject + "</textarea><br><br>";
        html = html + "<span style=\"font-weight:bold \">消息:</span><br><textarea readonly=\"readonly\" style=\"width:300px;height:180px\">" + full.message + "</textarea>";
        return html;
    }

    function renderFail(data, type, full) {
        var html = "";
        html = "<textarea readonly=\"readonly\" style=\"width:300px;height:60px\">" + data + "</textarea><br>";
        return html;
    }

    function renderTime(data, type, full) {
        return moment(data * 1000).format('YYYY-MM-DD hh:mm:ss');
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('clock').withTitle('时间').withOption(
            'sDefaultContent', '').renderWith(renderTime),
        DTColumnBuilder.newColumn('alertid').withTitle('接收类型').withOption(
            'sDefaultContent', '').renderWith(renderType),
        DTColumnBuilder.newColumn('alertid').withTitle('接收者').withOption(
            'sDefaultContent', '').renderWith(renderRec),
        DTColumnBuilder.newColumn('alertid').withTitle('消息内容').withOption(
            'sDefaultContent', '').renderWith(renderMsg),
        DTColumnBuilder.newColumn('status').withTitle('发生结果')
            .withOption('sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('error').withTitle('失败原因')
            .withOption('sDefaultContent', '').renderWith(renderFail)
    ]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/alarm/getAllAlarm.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    flush();
};
app.register.controller('zbxnoticerecCtl', zbxnoticerecCtl);