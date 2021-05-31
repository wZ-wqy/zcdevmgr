function resTypeCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                    $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [
            {
                id: "btn",
                label: "",
                type: "btn",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.classId
            + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.classId
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('classCode').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('type').withTitle('类型').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('classId').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/base/resClass/selectList.do", ps)
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
            required: true,
            maxlength: "50",
            placeholder: "请输入编码",
            label: "编码",
            need: true,
            name: 'classCode',
            ng_model: "classCode"
        }, {
            type: "select",
            disabled: "false",
            label: "类型",
            need: false,
            disable_search: "true",
            dataOpt: "typeOpt",
            dataSel: "typeSel"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "500",
            placeholder: "请输入备注",
            label: "备注",
            need: true,
            name: 'mark',
            ng_model: "mark"
        }];
        meta = {
            classId: id,
            footer_hide: false,
            title: "基本信息",
            item: {},
            typeOpt: [{
                id: "confitem",
                name: "配置项"
            }, {
                id: "class",
                name: "类目"
            }],
            typeSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.type = modal_meta.meta.typeSel.id;
                $http.post(
                    $rootScope.project
                    + "/api/base/resClass/insertOrUpdate.do",
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
                modal_meta.meta.typeSel = modal_meta.meta.typeOpt[0];
                if (angular.isDefined(modal_meta.meta.classId)) {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/base/resClass/selectById.do",
                            {
                                id: modal_meta.meta.classId
                            })
                        .success(
                            function (res) {
                                if (res.success) {
                                    modal_meta.meta.item = res.data;
                                    // 处理type
                                    for (var i = 0; i < modal_meta.meta.typeOpt.length; i++) {
                                        if (modal_meta.meta.typeOpt[i].id == res.data.type) {
                                            modal_meta.meta.typeSel = modal_meta.meta.typeOpt[i];
                                            break;
                                        }
                                    }
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
    $scope.flush = function () {
        flush();
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/base/resClass/delResClass.do", {
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
app.register.controller('resTypeCtl', resTypeCtl);
