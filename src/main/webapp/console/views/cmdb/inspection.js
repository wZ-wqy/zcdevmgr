function inspectionlistCtl(DTColumnBuilder, DTOptionsBuilder, $timeout, $localStorage, notify, $log, $uibModal,
                           $uibModalInstance, $compile, $scope, id, $http, $rootScope) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function renderStatus(data, type, full) {
        if (data == "wait") {
            return "未巡检";
        } else if (data == "success") {
            return "正常";
        } else if (data == "failed") {
            return "故障";
        } else {
            return data;
        }
    }

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
            })

    var dtColumns = [];
    dtColumns.push(DTColumnBuilder.newColumn('inspectstatus').withTitle('巡检状态').withOption(
        'sDefaultContent', '').renderWith(renderStatus));
    dtColumns.push(DTColumnBuilder.newColumn('inspectmark').withTitle('巡检备注').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('inspectpics').withTitle('巡检图片').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('inspectusername').withTitle('巡检人员').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('inspectactiontime').withTitle('巡检时间').withOption(
        'sDefaultContent', ''));
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

    if (angular.isDefined(id)) {
        //修改
        $http.post($rootScope.project + "/api/zc/resInspection/ext/selectById.do", {
            id: id
        }).success(function (rs) {
            if (rs.success) {
                $scope.dtOptions.aaData = rs.data.items;
            } else {
                notify({
                    message: rs.message
                });
            }
        })
    }
}

function inspectionCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log,
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

    $scope.dtInstance = {};

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"rowlist('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">资产列表</button>";
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

    function renderStatus(data, type, full) {
        if (data == "wait") {
            return "未开始";
        } else if (data == "acting") {
            return "进行中";
        } else if (data == "finish") {
            return "结束";
        } else {
            return data;
        }
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

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('busid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('巡检名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('method').withTitle('巡检方式').withOption(
            'sDefaultContent', '').renderWith(renderMethod),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('sdate').withTitle('开始时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('edate').withTitle('结束时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('cnt').withTitle('总数').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('normalcnt').withTitle('正常数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('faultcnt').withTitle('故障数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('actingcnt').withTitle('未巡检数').withOption(
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
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            }

        ]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/resInspection/ext/selectList.do", ps)
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

    $scope.rowlist = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_inspectionlist.html',
            controller: inspectionlistCtl,
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
            $http.post($rootScope.project + "/api/zc/resInspection/deleteById.do", {
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

};
app.register.controller('inspectionCtl', inspectionCtl);