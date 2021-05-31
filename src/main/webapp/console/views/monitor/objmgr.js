function objsaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                    $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.item = {};
    if (angular.isDefined(id)) {
        // 修改
        $http.post($rootScope.project + "/api/zbx/zbxObjectGroup/selectById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data
            } else {
                notify({
                    message: res.message
                });
            }
        })
    } else {
        // 新增
    }
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/zbx/zbxObjectGroup/insertOrUpdate.do",
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
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                adom[i].style.width = "100%";
            }
        }
    }, 200);
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function objmgrCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
            + full.id
            + "','" + full.code + "')\" class=\"btn-white btn btn-xs\">修改</button> ";
        acthtml = acthtml + " <button ng-click=\"remove('"
            + full.id
            + "','" + full.code + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
        acthtml = acthtml + " </div>";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('code').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zbx/zbxObjectGroup/selectList.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    $scope.remove = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/zbx/zbxObjectGroup/deleteById.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    flush();
                }
                notify({
                    message: res.message
                });
            })
        });
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/monitor/modal_objsave.html',
            controller: objsaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return id;
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
app.register.controller('objmgrCtl', objmgrCtl);