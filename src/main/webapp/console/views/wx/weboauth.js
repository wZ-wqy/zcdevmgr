function saveweboauthCtl($timeout, notify, $log, $uibModal, $uibModalInstance, $scope,
                         id, $http, $rootScope) {
    $scope.loginOpt = [{
        id: "0",
        name: "否"
    }, {
        id: "1",
        name: "是"
    }]
    $scope.loginSel = $scope.loginOpt[0];
    $scope.item = {};
    if (angular.isDefined(id)) {
        $http.post($rootScope.project + "/api/wx/queryWebOAuthById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                if (res.data.login == "1") {
                    $scope.loginSel = $scope.loginOpt[1];
                }
            } else {
            }
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
    $scope.sure = function () {
        $scope.item.login = $scope.loginSel.id;
        $http.post($rootScope.project + "/api/wx/saveWebOAuth.do", $scope.item)
            .success(function (res) {
                if (res.success) {
                    $uibModalInstance.close("OK");
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function weboauthCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                     $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [{
            id: "1",
            label: "新增",
            type: "btn",
            template: ' <button ng-click="modify()" class="btn btn-sm btn-primary" type="submit">新增</button>'
        }]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"modify('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div>  ";
        return acthtml;
    }

    function renderStatus(data, type, full) {
        if (data == "1") {
            return "是";
        } else if (data == "0") {
            return "否";
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('state').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('value').withTitle('值').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('login').withTitle('是否登录').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/wx/queryWebOAuth.do", ps)
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
    $scope.flush = function () {
        flush();
    }
    $scope.modify = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/wx/modal_saveweboauth.html',
            controller: saveweboauthCtl,
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
    $scope.del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/wx/delWebOAuth.do", {
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
app.register.controller('weboauthCtl', weboauthCtl);