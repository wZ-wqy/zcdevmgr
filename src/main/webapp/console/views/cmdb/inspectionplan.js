function inspectionplanSaveCtl(DTColumnBuilder, DTOptionsBuilder, $timeout, $localStorage, notify, $log, $uibModal,
                               $uibModalInstance, $compile, $scope, id, $http, $rootScope) {
    $scope.statusOpt = [{id: "stop", name: "停用"}, {id: "start", name: "启用"}]
    $scope.statusSel = $scope.statusOpt[0];
    $scope.userOpt = [];
    $scope.userSel = [];
    $scope.item = {};
    $scope.methodOpt=[{id:"fix",name:"固定模式"},{id:"free",name:"自由模式"}];
    $scope.methodSel=$scope.methodOpt[0];
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption("select", {
            style: 'multi',
            selector: 'td:first-child'
        });

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span></div>   ";

        return acthtml;
    }

    var dtColumns = [];
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '100').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保供应商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    var dicts = "";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "N",
            partusers: "Y",
            comp: "N",
            belongcomp: "N",
            zccatused: "N",
            uid: "inspectionplan"
        })
        .success(
            function (res) {
                if (res.success) {
                    $scope.userOpt = res.data.partusers;
                    if (angular.isDefined(id)) {
                        //修改
                        $http.post($rootScope.project + "/api/zc/resInspectionPlan/ext/selectById.do", {
                            id: id
                        }).success(function (rs) {
                            if (rs.success) {
                                $scope.data = rs.data;
                                if ($scope.data.status == "start") {
                                    $scope.statusSel = $scope.statusOpt[1];
                                } else if ($scope.data.status == "stop") {
                                    $scope.statusSel = $scope.statusOpt[0];
                                }

                                if ($scope.data.method == "fix") {
                                    $scope.methodSel=$scope.methodOpt[0];
                                } else if ($scope.data.method == "free") {
                                    $scope.methodSel=$scope.methodOpt[1];
                                }

                                $scope.dtOptions.aaData = $scope.data.items;

                                var actionusers = angular.fromJson(rs.data.actionusers);
                                var usersel = []
                                if (res.data.partusers.length > 0 && actionusers.length > 0) {
                                    for (var i = 0; i < res.data.partusers.length; i++) {
                                        for (var j = 0; j < actionusers.length; j++) {
                                            if (res.data.partusers[i].user_id == actionusers[j].user_id) {
                                                usersel.push(res.data.partusers[i]);
                                            }
                                        }
                                    }
                                }
                                $scope.userSel = usersel;
                            } else {
                                notify({
                                    message: rs.message
                                });
                            }
                        })
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    $scope.sure = function () {
        $scope.data.status = $scope.statusSel.id;
        $scope.data.actionusers = angular.toJson($scope.userSel);
        $scope.data.items = angular.toJson($scope.dtOptions.aaData);
        $scope.data.method= $scope.methodSel.id;
        $http.post($rootScope.project + "/api/zc/resInspectionPlan/ext/create.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.zcselect = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "XJ";
        mdata.showusefullife = "true";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
}

function inspectionplanCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log,
                           notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
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
        acthtml = acthtml + " <button ng-click=\"dd('" + full.busid
            + "')\" class=\"btn-white btn btn-xs\">手工调度</button> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>";
        acthtml = acthtml + "</div>";
        return acthtml;
    }

    function renderUsers(data, type, full) {
        var html = "";
        if (angular.isDefined(data)) {
            var arr = angular.fromJson(data);
            for (var i = 0; i < arr.length; i++) {
                if (i == 0) {
                    html = arr[i].name;
                } else {
                    html = html + "," + arr[i].name;
                }
            }
        }
        return html;
    }

    function renderMethod(data, type, full) {
        if (data == "free") {
            return "自由模式";
        } else if (data == "fix") {
            return "固定模式";
        } else {
            return data;
        }
    }


    function renderStatus(data, type, full) {

        if (data == "stop") {
            return "停用";
        } else if (data == "start") {
            return "启用";
        } else {
            return data;
        }

    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('巡检名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('method').withTitle('巡检方式').withOption(
            'sDefaultContent', '').renderWith(renderMethod),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('cron').withTitle('周期').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('actionusers').withTitle('人员').withOption(
            'sDefaultContent', '').renderWith(renderUsers),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createTime').withTitle('创建时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').withOption("width", '150').renderWith(renderAction)
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [
            {
                id: "btn1",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            },
            {
                id: "btn1",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            }

        ]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/resInspectionPlan/ext/selectList.do", ps)
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

    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_inspectplanSave.html',
            controller: inspectionplanSaveCtl,
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

    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/zc/resInspectionPlan/deleteById.do", {
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
    flush();
    $scope.query = function () {
        flush();
    }

    $scope.dd = function (busid) {
        $http.post($rootScope.project + "/api/zc/resInspectionPlan/ext/createInspection.do", {
            busid: busid
        }).success(function (res) {
            if (res.success) {
            }
            notify({
                message: res.message
            });
        })
    }
};
app.register.controller('inspectionplanCtl', inspectionplanCtl);