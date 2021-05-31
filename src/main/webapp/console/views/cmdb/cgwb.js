function GetDateNowId() {
    var vNow = new Date();
    var sNow = "";
    sNow += String(vNow.getFullYear());
    sNow += String(vNow.getMonth() + 1);
    sNow += String(vNow.getDate());
    sNow += String(vNow.getHours());
    sNow += String(vNow.getMinutes());
    sNow += String(vNow.getSeconds());
    sNow += String(vNow.getMilliseconds());
    return sNow;
}

function renderwb(data, type, full) {
    if (angular.isUndefined(data)) {
        data = ""
    }
    if (full.twbstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendersupplier(data, type, full) {
    if (angular.isUndefined(data)) {
        data = ""
    }
    if (full.twbsupplierstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderwbauto(data, type, full) {
    if (angular.isUndefined(data)) {
        data = ""
    }
    if (full.twbautostatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderwboutdate(data, type, full) {
    if (angular.isUndefined(data)) {
        data = ""
    }
    if (full.twboutdatestatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}
function renderbfoutdate(data, type, full) {
    if (angular.isUndefined(data)) {
        data = ""
    }
    if (full.tbfoutdatestatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}
function renderwbct(data, type, full) {
    if (angular.isUndefined(data)) {
        data = ""
    }
    if (full.twbctstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function cgwblistCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                     DTColumnBuilder, $compile) {
    var item = meta;
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
        }).withButtons([
            {
                extend: 'csv',
                text: 'Excel(当前页)',
                exportOptions: {
                    columns: ':visible',
                    trim: true,
                    modifier: {
                        page: 'current'
                    }
                }
            },
            {
                extend: 'print',
                text: '打印',
                exportOptions: {
                    columns: ':visible',
                    stripHtml: false,
                    columns: ':visible',
                    modifier: {
                        page: 'current'
                    }
                }
            }
        ]);
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    var dtColumns = [];



    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('资产名称').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', '').withOption('width', '50'),
        dtColumns.push(DTColumnBuilder.newColumn('fwbstr').withTitle('维保状态(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('twbstr').withTitle('维保状态(变更后)').withOption(
        'sDefaultContent', '').renderWith(renderwb));
    dtColumns.push(DTColumnBuilder.newColumn('fwbsupplierstr').withTitle('维保商(变更前)').withOption(
        'sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('twbsupplierstr').withTitle('维保商(变更后)').withOption(
        'sDefaultContent', '').renderWith(rendersupplier));

    dtColumns.push(DTColumnBuilder.newColumn('fwbautostr').withTitle('维保计算(变更前)').withOption(
        'sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('twbautostr').withTitle('维保计算(变更后)').withOption(
        'sDefaultContent', '').renderWith(renderwbauto));

    dtColumns.push(DTColumnBuilder.newColumn('fwboutdatestr').withTitle('脱保日期(变更前)').withOption(
        'sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('twboutdatestr').withTitle('脱保日期(变更后)').withOption(
        'sDefaultContent', '').renderWith(renderwboutdate));
    dtColumns.push(DTColumnBuilder.newColumn('fwbct').withTitle('维保说明(变更前)').withOption(
        'sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('twbct').withTitle('维保说明(变更后)').withOption(
        'sDefaultContent', '').renderWith(renderwbct));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
    //     'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;

    function flush() {
        $http.post($rootScope.project + "/api/zc/resCMaintenance/ext/selectByUuid.do",
            item).success(function (res) {
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
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function zccgwbCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $window,
                   $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var gdict = {};
    var dicts = "devwb,zcwbsupper,zcwbcomoute";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "N",
            partusers: "Y",
            comp: "N",
            belongcomp: "N" ,
            zccatused: "Y",
            uid: "zccgwb"
        })
        .success(
            function (res) {
                if (res.success) {
                    gdict = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    // 分类
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
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
        })
    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }

    function renderAction(data, type, full) {
        //分配，删除，详情
        if (data == "wait") {
            return "维修中"
        } else if (data == "finish") {
            return "已完成"
        } else {
            return data;
        }
    }

    function renderCheckStatus(data, type, full) {
        if (data == "init") {
            return "等待校验"
        } else if (data == "success") {
            return "成功"
        } else if (data == "failed") {
            return "失败"
        } else {
            return data;
        }
    }



    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('"
            + full.busuuid
            + "','" + full.status + "')\" class=\"btn-white btn btn-xs\">变更明细</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderprocess(data, type, full) {
        if (angular.isDefined(data) && data.length() > 0) {
            return "申请详情"
        } else {
            return "无审批"
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction),
        DTColumnBuilder.newColumn('busuuid').withTitle('变更单号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('status').withTitle('办理状态').withOption(
            'sDefaultContent', '').renderWith(renderZCSPStatus),
        DTColumnBuilder.newColumn('processusername').withTitle('处理人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('twbstr').withTitle('维保状态').withOption(
            'sDefaultContent', '').renderWith(renderwb),
        DTColumnBuilder.newColumn('twbsupplierstr').withTitle('维保商').withOption(
            'sDefaultContent', '').renderWith(rendersupplier),
        DTColumnBuilder.newColumn('twbautostr').withTitle('维保计算').withOption(
            'sDefaultContent', '').renderWith(renderwbauto),
        DTColumnBuilder.newColumn('twboutdatestr').withTitle('脱保日期').withOption(
            'sDefaultContent', '').renderWith(renderwboutdate),
        DTColumnBuilder.newColumn('twbct').withTitle('维保说明').withOption(
            'sDefaultContent', '').renderWith(renderwbct),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createusername').withTitle('创建人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "input",
                show: true,
                label: "内容",
                placeholder: "输入搜索内容",
                type: "input",
                ct: ""
            },
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                priv: "select",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">新建</button>'
            }]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        $http
            .post($rootScope.project + "/api/zc/resCMaintenance/ext/selectList.do",
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

    function getSelectRow() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1) {
            notify({
                message: "请最多选择一项"
            });
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    $scope.detail = function (uuid, status) {
        meta.uuid = uuid;
        meta.status = status;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_cglist.html',
            controller: cgwblistCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
            $log.log("reason", reason)
        });
    }

    function action(id) {
        var meta = {};
        meta.id = id;
        meta.dict = gdict;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zccgwbSave.html',
            controller: zccgwbSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
            $log.log("reason", reason)
        });
    }

    $scope.add = function () {
        action();
    }
    flush();
};
app.register.controller('zccgwbCtl', zccgwbCtl);
