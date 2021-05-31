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

function residualitemlistCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
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

    function renderItemCheckStatus(data, type, full) {
        if (data == 'success') {
            return "<span style=\"color:green;font-weight:bold\">通过</span>";
        } else if (data == 'failed') {
            return "<span style=\"color:red;font-weight:bold\">失败</span>";
        } else if (data == 'init') {
            return "<span style=\"color:red;font-weight:bold\">未校验</span>";
        } else {
            return data;
        }
    }

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    $scope.delitem = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resResidualItem/ext/deleteById.do",
                    {id: id}).success(function (res) {
                    if (res.success) {
                        flush();
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                })
            });
    }
    if (angular.isDefined(meta.status) && meta.status == "success") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').withOption("width", '30').renderWith(renderZCAction));
    }
    dtColumns.push(DTColumnBuilder.newColumn('checkstatus').withTitle('校验').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderItemCheckStatus));
    dtColumns.push(DTColumnBuilder.newColumn('classrootname').withTitle('类目').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('zcuuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('当前采购单价')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('net_worth').withTitle('当前资产净值')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('accumulateddepreciation').withTitle('当前累计折旧')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('buyprice').withTitle('本期单价')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('curresidualvalue').withTitle('本期设置残值')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('baccumulateddepreciation').withTitle('本期累计折旧')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('bnetworth').withTitle('本期资产净值(折旧前)')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('anetworth').withTitle('本期资产净值(折旧后)')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('lossprice').withTitle('本期折旧价')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('fs20').withTitle('其他编号').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('buy_timestr').withTitle('采购日期')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;

    function flush() {
        $http.post($rootScope.project + "/api/zc/resResidualItem/ext/selectListByUuid.do",
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

function zcresidualSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                           $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                           DTColumnBuilder, $compile) {
    $scope.ctl = {};
    $scope.ctl.name = false;
    $scope.ctl.adminuserSel = false;
    $scope.ctl.pduserSel = false;
    $scope.ctl.pdSel = false;
    $scope.ctl.mark = false;
    $scope.ctl.belongcompSel = false;
    $scope.ctl.compSel = false;
    $scope.ctl.comppartSel = false;
    $scope.ctl.zcCatSel = false;
    $scope.ctl.zcAreaSel = false;
    $scope.item = {};
    $scope.adminuserOpt = meta.dict.partusers;
    $scope.adminuserSel = "";
    if ($scope.adminuserOpt.length > 0) {
        $scope.adminuserSel = $scope.adminuserOpt[0];
    }
    $scope.zjOpt = [];
    $scope.zjSel = "";
    $http.post($rootScope.project + "/api/zc/resResidualStrategy/selectList.do",
        $scope.item).success(function (res) {
        if (res.success) {
            for (var i = 0; i < res.data.length; i++) {
                res.data[i].name = res.data[i].name + "-" + res.data[i].strategydesc;
            }
            $scope.zjOpt = res.data;
            if (res.data.length > 0) {
                $scope.zjSel = $scope.zjOpt[0];
            }
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
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
    $scope.dtInstance = {}
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

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
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购单价')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn("net_worth").withTitle('资产净值')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn("accumulateddepreciation").withTitle('累计折旧')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('residualvalue').withTitle('设置残值')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('lastdepreciationdatestr').withTitle('最近折旧时间')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('fs20').withTitle('其他编号').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('buy_timestr').withTitle('采购时间')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        $scope.item.strategyid = $scope.zjSel.id;
        $scope.item.processuserid = $scope.adminuserSel.user_id;
        $scope.item.processusername = $scope.adminuserSel.name;
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        $http.post($rootScope.project + "/api/zc/resResidual/ext/insert.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.review = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "ZJ";
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
        });
    }
}

function zcresidualCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $window,
                       $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var gdict = {};
    var dicts = "devdc";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "Y",
            zccatused: "Y",
            uid: "zcresidual"
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

    function renderStatus(data, type, full) {
        if (data == "wait") {
            return "未处理"
        } else if (data == "cancel") {
            return "取消"
        } else if (data == "success") {
            return "完成"
        } else if (data == "failed") {
            return "失败"
        } else {
            return data;
        }
    }

    function renderDownload(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"download('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">下载</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('"
            + full.uuid
            + "','" + full.status + "')\" class=\"btn-white btn btn-xs\">资产列表</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('uuid').withTitle('折旧单据').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('title').withTitle('标题').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('checkstatus').withTitle('校验状态').withOption(
            'sDefaultContent', '').renderWith(renderCheckStatus),
        DTColumnBuilder.newColumn('cnt').withTitle('数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('residualvaluerate').withTitle('残值率(%)').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('depreciationrate').withTitle('折旧率(%)').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('processusername').withTitle('负责人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('busidate').withTitle('业务时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createTime').withTitle('创建时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                priv: "select",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">新建</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="check()" class="btn btn-sm btn-primary" type="submit">校验</button>'
            },
            {
                id: "zj",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="zj()" class="btn btn-sm btn-primary" type="submit">折旧</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            }]
    }
    $scope.meta = meta;
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);

    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        $http
            .post($rootScope.project + "/api/zc/resResidual/ext/selectList.do",
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
            templateUrl: 'views/cmdb/modal_residualitemlist.html',
            controller: residualitemlistCtl,
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
        });
    }

    function action(id) {
        var meta = {};
        meta.id = id;
        meta.dict = gdict;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_residualsave.html',
            controller: zcresidualSaveCtl,
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
        });
    }

    $scope.check = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            $confirm({
                text: '是否验证数据?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resResidual/ext/checkDataById.do", {
                            id: selrow.id
                        }).success(function (res) {
                        if (res.success) {
                            flush();
                        } else {
                        }
                        notify({
                            message: res.message
                        });
                    });
                });
        }
    }
    $scope.del = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resResidual/ext/deleteById.do", {
                            id: selrow.id
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
        } else {
            return;
        }
    }
    $scope.zj = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            $confirm({
                text: '是否折旧资产?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resResidual/ext/actionSysData.do", {
                            id: selrow.id
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
        } else {
            return;
        }
    }
    $scope.add = function () {
        action();
    }
    flush();
};
app.register.controller('zcresidualCtl', zcresidualCtl);
