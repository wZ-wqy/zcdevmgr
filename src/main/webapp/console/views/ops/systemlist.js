function cmdbsystemListCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                           $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var classId = "systemlist";
    var meta = {
        tablehide: false,
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
    $scope.meta = meta;
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

    function renderLevel(data, type, full) {
        for (var i = 0; i < mainlevelOpt.length; i++) {
            if (data == mainlevelOpt[i].dictItemId) {
                return mainlevelOpt[i].name
                break
            }
        }
        return data;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mainlevel').withTitle('等级').withOption(
            'sDefaultContent', '').renderWith(renderLevel),
        DTColumnBuilder.newColumn('rto').withTitle('RTO').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rpo').withTitle('RPO').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('leader').withTitle('负责人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.classId = classId;
        $http.post($rootScope.project + "/api/base/res/selectListResExd.do", ps)
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
    var mainlevelOpt = [];
    $http.post($rootScope.project + "/api/sysDictItem/selectDictItemByDict.do",
        {
            dictId: "sys_system_level"
        }).success(function (res) {
        mainlevelOpt = res.data;
        flush();
    });

    function loadOpt(modal_meta) {
        var item = modal_meta.meta.item;
        modal_meta.meta.mainlevelOpt = mainlevelOpt;
        if (angular.isDefined(modal_meta.meta.id)) {
            $http.post($rootScope.project + "/api/base/res/selectById.do", {
                id: modal_meta.meta.id
            }).success(function (res) {
                modal_meta.meta.item = res.data;
                for (var i = 0; i < mainlevelOpt.length; i++) {
                    if (res.data.mainlevel == mainlevelOpt[i].dictItemId) {
                        modal_meta.meta.mainlevelSel = mainlevelOpt[i];
                        break;
                    }
                }
            });
        }
    }

    // //////////////////////////save/////////////////////
    $scope.save = function (id) {
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
            type: "select",
            disabled: "false",
            label: "等级",
            need: true,
            disable_search: "true",
            dataOpt: "mainlevelOpt",
            dataSel: "mainlevelSel"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "50",
            placeholder: "请输入rto",
            label: "RTO",
            need: true,
            name: 'rto',
            ng_model: "rto"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "50",
            placeholder: "请输入rpo",
            label: "RPO",
            need: true,
            name: 'rpo',
            ng_model: "rpo"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "50",
            placeholder: "请输入负责人",
            label: "联系人",
            need: true,
            name: 'leader',
            ng_model: "leader"
        }];
        meta = {
            id: id,
            classId: classId,
            footer_hide: false,
            title: "信息系统列表",
            item: {},
            mainlevelOpt: [],
            mainlevelSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.classId = classId;
                if (angular.isDefined(modal_meta.meta.mainlevelSel.dictItemId)) {
                    modal_meta.meta.item.mainlevel = modal_meta.meta.mainlevelSel.dictItemId;
                }
                $http.post(
                    $rootScope.project + "/api/base/res/insertOrUpdate.do",
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
                loadOpt(modal_meta);
            }
        }
        // 打开静态框
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
app.register.controller('cmdbsystemListCtl', cmdbsystemListCtl);