function sysFlowGroupCtl($stateParams, DTOptionsBuilder, DTColumnBuilder,
                         $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [
            {
                id: "0",
                priv: "select",
                label: "查询",
                type: "btn_query",
                hide: false,
            },
            {
                id: "1",
                label: "新增",
                priv: "insert",
                show: false,
                type: "btn",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }]
    }
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
    var crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
    };
    privCrudCompute(crud, $rootScope.curMemuBtns);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'responsive', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if (crud.update) {
            acthtml = acthtml + " <button ng-click=\"save('" + full.id
                + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        }
        if (crud.remove) {
            acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
                + "')\" class=\"btn-white btn btn-xs\">删除</button>";
        }
        acthtml = acthtml + "</div>";
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var res = "无效";
        if (full.isAction == "Y") {
            res = "有效";
        }
        return res;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post(
            $rootScope.project + "/api/flow/sysProcessClass/selectList.do",
            ps).success(function (res) {
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
    $scope.btn_query = function () {
        flush();
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除功能?'
        })
            .then(
                function () {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/flow/sysProcessClass/deleteById.do",
                            {
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
        var meta = {};
        var items = [{
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "100",
            placeholder: "请输入名称",
            label: "名称",
            need: true,
            name: 'name',
            ng_model: "name"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入备注",
            label: "备注",
            need: false,
            name: 'mark',
            ng_model: "mark"
        }];
        meta = {
            id: id,
            footer_hide: false,
            title: "基本信息",
            item: {},
            typeSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.attrType = modal_meta.meta.typeSel.id;
                $http
                    .post(
                        $rootScope.project
                        + "/api/flow/sysProcessClass/insertOrUpdate.do",
                        modal_meta.meta.item).success(function (res) {
                    if (res.success) {
                        modalInstance.close("OK");
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                });
            },
            init: function (modal_meta) {
                if (angular.isDefined(modal_meta.meta.id)) {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/flow/sysProcessClass/selectById.do",
                            {
                                id: modal_meta.meta.id
                            }).success(function (res) {
                        if (res.success) {
                            modal_meta.meta.item = res.data;
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    });
                }
            }
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/Template/modal_simpleForm.html',
            controller: modal_simpleFormCtl,
            size: 'lg',
            resolve: {
                meta: function () {
                    return meta;
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
};
app.register.controller('sysFlowGroupCtl', sysFlowGroupCtl);