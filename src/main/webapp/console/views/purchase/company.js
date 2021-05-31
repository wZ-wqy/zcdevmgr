function compSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/base/contractEntity/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
                notify({
                    message: res.message
                });
            }
        })
    };
}

function purchasecompCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                         $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                         $stateParams) {
    $scope.crud = {
        "update": true,
        "insert": true,
        "select": true,
        "remove": true,
        "item_insert": true,
        "item_update": true,
        "item_remove": true
    };
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withPaginationType(
        'full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption("paging", false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', false)
        .withOption('serverSide', false).withOption('bAutoWidth', true)
        .withOption('rowCallback', rowCallback).withOption('createdRow',
            function (row) {
                // Recompiling so we can bind Angular,directive to the
                $compile(angular.element(row).contents())($scope);
            }).withOption("select", {
            style: 'single'
        });

    function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        // Unbind first in order to avoid any duplicate handler
        // (see https://github.com/l-lin/angular-datatables/issues/87)
        $('td', nRow).unbind('click');
        $('td', nRow).bind('click', function () {
            $scope.$apply(function () {
                someClickHandler(aData);
            });
        });
        return nRow;
    }

    function someClickHandler(data) {
        $scope.item = data;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('单位').withOption(
            'sDefaultContent', '')]
    $scope.dtInstance = {}

    function flush() {
        var ps = {};
        $http.post($rootScope.project + "/api/base/contractEntity/selectList.do", ps)
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
    $scope.add = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/purchase/modal_compSave.html',
            controller: compSaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return "";
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
    $scope.row_update = function () {
        if (angular.isDefined($scope.item.id)) {
            $http.post($rootScope.project + "/api/base/contractEntity/insertOrUpdate.do", $scope.item)
                .success(function (res) {
                    notify({
                        message: res.message
                    });
                })
        } else {
            notify({
                message: "请先选择组织"
            });
        }
    }
};
app.register.controller('purchasecompCtl', purchasecompCtl);