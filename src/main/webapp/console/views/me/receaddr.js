function meRecAddrCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: []
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
//		acthtml = acthtml + " <button ng-click=\"save('" + full.id + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [DTColumnBuilder.newColumn('contactuser').withTitle('联系人').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('contact').withTitle('联系方式').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('zcode').withTitle('邮编').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('ctdtl').withTitle('地址').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption('sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/sysUserInfo/my/queryReceivingaddr.do", ps).success(function (res) {
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
    $scope.flush = function () {
        flush();
    }
    $scope.save = function () {
        alert('功能未实现');
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除该地址?'
        }).then(function () {
            $http.post($rootScope.project + "/api/sysUserInfo/my/deleteReceivingaddr.do", {
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
};
app.register.controller('meRecAddrCtl', meRecAddrCtl);