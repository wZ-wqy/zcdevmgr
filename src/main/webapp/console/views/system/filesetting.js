function sysfileConfCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                        $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $stateParams) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        return "";
    }

    function renderStatus(data, type, full) {
        if (data == "Y") {
            return "正常";
        } else if (data == "N") {
            return "停用";
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('path').withTitle('路径').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('isUsed').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('userId').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/sysFileConf/selectList.do", ps)
            .success(function (res) {
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
    $scope.row_del = function (id) {
        alert("待开发");
    }
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        alert("待开发");
    }
};
app.register.controller('sysfileConfCtl', sysfileConfCtl);