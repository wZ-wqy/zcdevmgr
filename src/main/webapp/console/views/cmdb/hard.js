function cmdbHardCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                     $log, notify, $scope, $http, $rootScope, $uibModal) {
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "select",
                label: "分类",
                type: "select",
                disablesearch: true,
                dataOpt: [],
                dataSel: ""
            },
            {
                id: "select",
                label: "配置项",
                type: "select",
                disablesearch: true,
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
    $scope.meta = meta;
    // 分类
    $http
        .post(
            $rootScope.project
            + "/api/base/resClass/queryCategoryChildren.do?parent_id=3&is_action=Y",
            {}).success(function (res) {
        if (res.success) {
            $scope.meta.tools[0].dataOpt = res.data;
            ;
            if (res.data.length > 0) {
                $scope.meta.tools[0].dataSel = res.data[0];
            }
        } else {
            notify({
                message: res.message
            });
        }
    })
    var watch2 = $scope.$watch('meta.tools[0].dataSel', function (oldValue,
                                                                  newValue, scope) {
        if (angular.isDefined($scope.meta.tools[0].dataSel)) {
            var ps = {};
            ps.class_id = $scope.meta.tools[0].dataSel.id;
            $http.post(
                $rootScope.project
                + "/api/base/resClass/queryConfItemByCategory.do",
                ps).success(function (xlres) {
                if (xlres.success) {
                    $scope.meta.tools[1].dataOpt = xlres.data;
                    if (xlres.data.length > 0) {
                        $scope.meta.tools[1].dataSel = xlres.data[0];
                        // $scope.confSel.class_id=xlres.data[0];
                    }
                }
            })
        }
    });
    // 自动获取配置项
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        // DTColumnBuilder.newColumn('uuid').withTitle('编号').withOption(
        // 'sDefaultContent', ''),
        DTColumnBuilder.newColumn('pinpstr').withTitle('品牌').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('version').withTitle('版本').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('envstr').withTitle('环境').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('maintenancestr').withTitle('维保').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('statusstr').withTitle('状态').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('statusstr').withTitle('启用时间')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        if (angular.isDefined($scope.meta.tools[1].dataSel.class_id)) {
            ps.id = $scope.meta.tools[1].dataSel.class_id;
        }
        $http.post($rootScope.project + "/api/base/res/queryResAllByClass.do", ps)
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

    $scope.query = function () {
        flush();
    }
    $scope.del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/base/res/deleteById.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    flush();
                } else {
                    notify({
                        message: res.message
                    });
                }
            });
        });
    }

    function loadOpt(modal_meta) {
        // 品牌
        var item = modal_meta.meta.item;
        $http.post(
            $rootScope.project
            + "/api/sysDictItem/selectDictItemByDict.do ", {
                dictId: "sys_device_pinp"
            }).success(function (res) {
            modal_meta.meta.pinpOpt = res.data;
            if (res.data.length > 0) {
                if (angular.isDefined(item) && angular.isDefined(item.pinp)) {
                    for (var i = 0; i < res.data.length; i++) {
                        if (res.data[i].dictItemId == item.pinp) {
                            modal_meta.meta.pinpSel = res.data[i];
                        }
                    }
                } else {
                    if (res.data.length > 0) {
                        modal_meta.meta.pinpSel = res.data[0];
                    }
                }
            }
        });
        // 负责人
        // $http.post(
        // $rootScope.project
        // + "/api/sysDictItem/selectDictItemByDict.do ", {
        // dictId : "1079009262096052225"
        // }).success(
        // function(res) {
        // modal_meta.meta.headuserOpt = res.data;
        // if (res.data.length > 0) {
        //
        // if (angular.isDefined(item)
        // && angular.isDefined(item.headuserid)) {
        // for (var i = 0; i < res.data.length; i++) {
        // if (res.data[i].dictItemId == item.headuserid) {
        // modal_meta.meta.headuserSel = res.data[i];
        // }
        // }
        // } else {
        // if (res.data.length > 0) {
        // modal_meta.meta.headuserSel = res.data[0];
        // }
        // }
        // }
        // });
        // 等级
        // $http.post(
        // $rootScope.project
        // + "/api/sysDictItem/selectDictItemByDict.do ", {
        // dictId : "1079695969170706433"
        // }).success(
        // function(res) {
        // modal_meta.meta.mainlevelOpt = res.data;
        // if (res.data.length > 0) {
        //
        // if (angular.isDefined(item)
        // && angular.isDefined(item.mainlevel)) {
        // for (var i = 0; i < res.data.length; i++) {
        // if (res.data[i].dictItemId == item.mainlevel) {
        // modal_meta.meta.mainlevelSel = res.data[i];
        // }
        // }
        // } else {
        // if (res.data.length > 0) {
        // modal_meta.meta.mainlevelSel = res.data[0];
        // }
        // }
        // }
        // });
        // 环境
        $http.post(
            $rootScope.project
            + "/api/sysDictItem/selectDictItemByDict.do ", {
                dictId: "sys_device_env"
            }).success(function (res) {
            modal_meta.meta.envOpt = res.data;
            if (res.data.length > 0) {
                if (angular.isDefined(item) && angular.isDefined(item.env)) {
                    for (var i = 0; i < res.data.length; i++) {
                        if (res.data[i].dictItemId == item.env) {
                            modal_meta.meta.envSel = res.data[i];
                        }
                    }
                } else {
                    if (res.data.length > 0) {
                        modal_meta.meta.envSel = res.data[0];
                    }
                }
            }
        });
        $http.post(
            $rootScope.project
            + "/api/sysDictItem/selectDictItemByDict.do ", {
                dictId: "sys_device_status"
            }).success(function (res) {
            modal_meta.meta.statusOpt = res.data;
            if (res.data.length > 0) {
                if (angular.isDefined(item) && angular.isDefined(item.status)) {
                    for (var i = 0; i < res.data.length; i++) {
                        if (res.data[i].dictItemId == item.status) {
                            modal_meta.meta.statusSel = res.data[i];
                        }
                    }
                } else {
                    if (res.data.length > 0) {
                        modal_meta.meta.statusSel = res.data[0];
                    }
                }
            }
        });
        // 状态
        $http.post(
            $rootScope.project
            + "/api/sysDictItem/selectDictItemByDict.do ", {
                dictId: "sys_device_wbstatus"
            }).success(function (res) {
            modal_meta.meta.maintenanceOpt = res.data;
            if (res.data.length > 0) {
                if (angular.isDefined(item) && angular.isDefined(item.maintenance)) {
                    for (var i = 0; i < res.data.length; i++) {
                        if (res.data[i].dictItemId == item.maintenance) {
                            modal_meta.meta.maintenanceSel = res.data[i];
                        }
                    }
                } else {
                    if (res.data.length > 0) {
                        modal_meta.meta.maintenanceSel = res.data[0];
                    }
                }
            }
        });
        // 设备厂商
        // $http.post(
        // $rootScope.project
        // + "/api/sysDictItem/selectDictItemByDict.do ", {
        // dictId : "sys_device_comp"
        // }).success(
        // function(res) {
        // modal_meta.meta.companyOpt = res.data;
        // if (res.data.length > 0) {
        //
        // if (angular.isDefined(item)
        // && angular.isDefined(item.company)) {
        // for (var i = 0; i < res.data.length; i++) {
        // if (res.data[i].dictItemId == item.company) {
        // modal_meta.meta.companySel = res.data[i];
        // }
        // }
        // } else {
        // if (res.data.length > 0) {
        // modal_meta.meta.companySel = res.data[0];
        // }
        // }
        // }
        // });
    }

    // //////////////////////////save/////////////////////
    $scope.save = function (id) {
        var class_id = "";
        if (!angular.isDefined(id)) {
            // 获取class_id
            if (angular.isDefined($scope.meta.tools[1].dataSel)
                && $scope.meta.tools[1].dataSel != null
                && angular.isDefined($scope.meta.tools[1].dataSel.class_id)) {
                class_id = $scope.meta.tools[1].dataSel.class_id;
            } else {
                alert("请选择配置项!");
                return;
            }
        }
        $http
            .post($rootScope.project + "/api/base/res/queryResAllById.do", {
                id: id
            })
            .success(
                function (res) {
                    if (!res.success) {
                        notify({
                            message: res.message
                        });
                        return;
                    }
                    var meta = {};
                    var items = [{
                        type: "input",
                        disabled: "false",
                        sub_type: "text",
                        required: true,
                        maxlength: "50",
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
                        maxlength: "50",
                        placeholder: "请输入序列号",
                        label: "序列",
                        need: false,
                        name: 'sn',
                        ng_model: "sn"
                    }, {
                        type: "input",
                        disabled: "false",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "请输入版本",
                        label: "版本",
                        need: false,
                        name: 'version',
                        ng_model: "version"
                    }, {
                        type: "datetime",
                        disabled: "false",
                        label: "开始时间",
                        need: true,
                        ng_model: "stime"
                    }, {
                        type: "select",
                        disabled: "false",
                        label: "维保",
                        need: true,
                        disable_search: "true",
                        dataOpt: "maintenanceOpt",
                        dataSel: "maintenanceSel"
                    }, {
                        type: "select",
                        disabled: "false",
                        label: "状态",
                        need: true,
                        disable_search: "true",
                        dataOpt: "statusOpt",
                        dataSel: "statusSel"
                    }, {
                        type: "select",
                        disabled: "false",
                        label: "环境",
                        need: true,
                        disable_search: "true",
                        dataOpt: "envOpt",
                        dataSel: "envSel"
                    }, {
                        type: "select",
                        disabled: "false",
                        label: "品牌",
                        need: true,
                        disable_search: "true",
                        dataOpt: "pinpOpt",
                        dataSel: "pinpSel"
                    }, {
                        type: "input",
                        disabled: "false",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "请输入备注",
                        label: "备注",
                        need: false,
                        name: 'mark',
                        ng_model: "mark"
                    }];
                    meta = {
                        class_id: class_id,
                        footer_hide: false,
                        title: "基础设施",
                        item: {},
                        stime: moment().subtract(15, "days"),
                        statusOpt: [],
                        statusSel: "",
                        pinpOpt: [],
                        pinpSel: "",
                        headuserOpt: [],
                        headuserSel: "",
                        locOpt: [],
                        locSel: "",
                        maintenanceOpt: [],
                        maintenance: "",
                        envOpt: [],
                        envSel: "",
                        mainlevelOpt: [],
                        mainlevelSel: "",
                        companyOpt: [],
                        companySel: "",
                        items: items,
                        sure: function (modalInstance, modal_meta) {
                            // 返回接口
                            if (!angular
                                .isDefined(modal_meta.meta.item.id)) {
                                modal_meta.meta.item.class_id = modal_meta.meta.class_id;
                            }
                            modal_meta.meta.item.env = modal_meta.meta.envSel.dictItemId;
                            modal_meta.meta.item.status = modal_meta.meta.statusSel.dictItemId;
                            modal_meta.meta.item.pinp = modal_meta.meta.pinpSel.dictItemId;
                            modal_meta.meta.item.maintenance = modal_meta.meta.maintenanceSel.dictItemId;
                            // 动态参数
                            if (angular.isDefined(modal_meta.meta.attr)
                                && modal_meta.meta.attr.length > 0) {
                                for (var j = 0; j < modal_meta.meta.attr.length; j++) {
                                    var code = modal_meta.meta.attr[j].attr_code;
                                    modal_meta.meta.attr[j].attr_value = modal_meta.meta.item[modal_meta.meta.attr[j].attr_code];
                                }
                            }
                            modal_meta.meta.item.attrvals = angular
                                .toJson(modal_meta.meta.attr);
                            $http
                                .post(
                                    $rootScope.project
                                    + "/api/base/res/addResCustom.do",
                                    modal_meta.meta.item)
                                .success(function (res) {
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
                            loadOpt(modal_meta);
                        }
                    }
                    //
                    if (angular.isDefined(res.data.data)
                        && angular.isDefined(res.data.data.id)) {
                        meta.item = res.data.data;
                        // 填充其他数据
                    }
                    // 打开静态框
                    var modalInstance = $uibModal
                        .open({
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
                })
    }
};
app.register.controller('cmdbHardCtl', cmdbHardCtl);