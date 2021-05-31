function hostgroupSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                          $uibModalInstance, $scope, id, name, $http, $rootScope) {
    $scope.item = {};
    $scope.item.name = name;
    $scope.sure = function () {
        var url = "";
        if (angular.isDefined(id)) {
            url = "/api/zbx/hostgroup/updateHostGroup.do";
            $scope.item.groupid = id;
        } else {
            url = "/api/zbx/hostgroup/addHostGroup.do"
        }
        $http.post($rootScope.project + url,
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function zbxhostgroupCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
            }, {
                id: "1",
                label: "新增",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }
        ]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', false).withOption('bAutoWidth', true)
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

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('"
            + full.groupid
            + "','" + full.name + "')\" class=\"btn-white btn btn-xs\">修改</button> ";
        // acthtml = acthtml + " <button ng-click=\"remove('"
        //     + full.id
        //     + "','" + full.code + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
        acthtml = acthtml + " </div>";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('groupid').withTitle('编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('主机组').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('hosts').withTitle('主机数').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('groupid').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/hostgroup/getAllHostGroups.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    $scope.save = function (id, name) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/monitor/modal_hostgroupsave.html',
            controller: hostgroupSaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return id;
                },
                name: function () {
                    return name;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flush();
            }
        }, function (reason) {
        });
    }
    flush();
};
app.register.controller('zbxhostgroupCtl', zbxhostgroupCtl);