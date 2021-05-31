function resAttrCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                    $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [
            {
                id: "select",
                label: "类型",
                type: "select",
                disablesearch: false,
                dataOpt: [],
                dataSel: ""
            },
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
        acthtml = acthtml + " <button ng-click=\"save('" + full.attrId
            + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.attrId
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('attrName').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('attrCode').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('attrType').withTitle('类型').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('attrId').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.classId = $scope.meta.tools[0].dataSel.classId;
        $http.post($rootScope.project + "/api/base/resClassAttrs/selectByClassId.do", ps)
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

    $http.post($rootScope.project + "/api/base/resClass/selectList.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.meta.tools[0].dataOpt = res.data;
                if (res.data.length > 0) {
                    $scope.meta.tools[0].dataSel = res.data[0];
                    flush();
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    $scope.query = function () {
        flush();
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
            name: 'attrName',
            ng_model: "attrName"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "50",
            placeholder: "请输入编码",
            label: "编码",
            need: true,
            name: 'attrCode',
            ng_model: "attrCode"
        }, {
            type: "select",
            disabled: "false",
            label: "类型",
            need: false,
            disable_search: "true",
            dataOpt: "typeOpt",
            dataSel: "typeSel"
        }];
        meta = {
            attrId: id,
            classId: $scope.meta.tools[0].dataSel.classId,
            footer_hide: false,
            title: "基本信息",
            item: {},
            typeOpt: [{
                id: "string",
                name: "文本"
            }, {
                id: "number",
                name: "整数"
            }, {
                id: "string_arr",
                name: "文本数组"
            }],
            typeSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.attrType = modal_meta.meta.typeSel.id;
                $http.post(
                    $rootScope.project
                    + "/api/base/resClassAttrs/insertOrUpdate.do",
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
                if (angular.isDefined(modal_meta.meta.attrId)) {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/base/resClassAttrs/selectById.do",
                            {
                                id: modal_meta.meta.attrId
                            })
                        .success(
                            function (res) {
                                if (res.success) {
                                    modal_meta.meta.item = res.data;
                                    // 处理type
                                    for (var i = 0; i < modal_meta.meta.typeOpt.length; i++) {
                                        if (modal_meta.meta.typeOpt[i].id == res.data.attrType) {
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
                } else {
                    modal_meta.meta.item.classId = modal_meta.meta.classId;
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
                    + "/api/base/resClassAttrs/deleteById.do", {
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
app.register.controller('resAttrCtl', resAttrCtl);
