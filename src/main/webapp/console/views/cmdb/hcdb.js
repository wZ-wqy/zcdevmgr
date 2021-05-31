function modalhcdbcntCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                         $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                         $uibModalInstance, $window, $stateParams, $timeout) {
    $scope.data = {};
    $scope.data.zc_cnt = 1;
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        if ($scope.data.zc_cnt > meta.zc_cnt) {
            notify({
                message: "数量不允许超过" + meta.zc_cnt + "个"
            });
            return;
        }
        if ($scope.data.zc_cnt <= 0) {
            notify({
                message: "数量必须大于0个"
            });
            return;
        }
        $uibModalInstance.close($scope.data);
    }
}

function modalhcdblistCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                          $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                          $uibModalInstance, $window, $stateParams, $timeout) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.URL = $rootScope.project + "/api/base/res/queryPageResAllByClass.do";
    meta.classroot = 7;
    meta.start = 0;
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: meta
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 420)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('createdRow', function (row) {
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

    function stateChange(iColumn, bVisible) {
    }

    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    $scope.dtColumns = [];
    $scope.dtColumns = zcBaseColsHCCreate(DTColumnBuilder, 'withselect');

    function flush() {
        var ps = {}
        var time = new Date().getTime();
        ps.classroot = 7
        ps.time = time;
        ps.belongcomp = meta.belongcomp;
        ps.loc = meta.loc;
        ps.zcnumber = 1;
        ps.warehouse = meta.warehouse;
        $scope.dtOptions.ajax.data = ps
        //$scope.dtInstance.reloadData(callback, true);
    }

    function callback() {
    };
    flush();

    function getSelectRows() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1000) {
            notify({
                message: "不允许超过1000个"
            });
            return;
        } else {
            var res = [];
            var d = $scope.dtInstance.DataTable.context[0].json.data;
            for (var i = 0; i < data.length; i++) {
                res.push(d[data[i]])
            }
            return res;
        }
    }

    $scope.sure = function () {
        var selrows = getSelectRows();
        var ps = {};
        if (angular.isDefined(selrows)) {
            console.log(selrows);
            $uibModalInstance.close(selrows);
        }
    }
}

function modalhcdbCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                      $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                      $uibModalInstance, $window, $stateParams, $timeout) {
    $scope.ctl = {}
    $scope.ctl.remark = false;
    $scope.ctl.ywtime = false;
    $scope.ctl.title = false;
    $scope.ctl.range = false;
    $scope.ctl.selectlist = false;
    $scope.ctl.footer = false;
    $scope.data = {};
    $scope.data.zc_cnt = 0;
    $scope.data.ywtime = moment();
    $scope.outbelongCompOpt = [];
    $scope.outbelongCompSel = "";
    $scope.outareaOpt = [];
    $scope.outareaSel = "";
    $scope.outwarehouseOpt = [];
    $scope.outwarehouseSel = "";
    $scope.inbelongCompOpt = [];
    $scope.inbelongCompSel = "";
    $scope.inareaOpt = [];
    $scope.inareaSel = "";
    $scope.inwarehouseOpt = [];
    $scope.inwarehouseSel = "";
    $scope.inuserOpt = [];
    $scope.inuserSel = "";
    var dicts = "devdc,zcsupper,warehouse";
    $http.post($rootScope.project + "/api/zc/queryDictFast.do",
        {
            uid: "hcoutdicts",
            zchccat: "Y",
            comp: "Y",
            parts: "Y",
            partusers: "Y",
            belongcomp: "Y",
            dicts: dicts
        }).success(function (res) {
        if (res.success) {
            angular.copy(res.data.belongcomp, $scope.outbelongCompOpt);
            if ($scope.outbelongCompOpt.length > 0) {
                $scope.outbelongCompSel = $scope.outbelongCompOpt[0];
            }
            angular.copy(res.data.devdc, $scope.outareaOpt);
            if ($scope.outareaOpt.length > 0) {
                $scope.outareaSel = $scope.outareaOpt[0];
            }
            angular.copy(res.data.warehouse, $scope.outwarehouseOpt);
            if ($scope.outwarehouseOpt.length > 0) {
                $scope.outwarehouseSel = $scope.outwarehouseOpt[0];
            }
            //
            angular.copy(res.data.belongcomp, $scope.inbelongCompOpt);
            if ($scope.inbelongCompOpt.length > 0) {
                $scope.inbelongCompSel = $scope.inbelongCompOpt[0];
            }
            angular.copy(res.data.devdc, $scope.inareaOpt);
            if ($scope.inareaOpt.length > 0) {
                $scope.inareaSel = $scope.inareaOpt[0];
            }
            angular.copy(res.data.warehouse, $scope.inwarehouseOpt);
            if ($scope.inwarehouseOpt.length > 0) {
                $scope.inwarehouseSel = $scope.inwarehouseOpt[0];
            }
            angular.copy(res.data.partusers, $scope.inuserOpt);
            if ($scope.inuserOpt.length > 0) {
                $scope.inuserSel = $scope.inuserOpt[0];
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
        .withPaginationType('full_numbers')
        .withDisplayLength(25)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 420)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <a href=\"javascript:void(0)\" style=\"margin-top: 3px;\" ng-click=\"modify('" + full.id + "'," + full.zc_cnt + ")\" class=\"btn-white btn btn-xs\">修改</a>";
        return acthtml;
    }

    var dtColumns = [];
    $scope.dtColumns = [];
    if (angular.isDefined(meta.type) && meta.type == "dtl") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('lid').withTitle('操作').withOption(
            'sDefaultContent', '').withOption("name", '30').renderWith(renderAction));
    }
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('单据编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('ctid').withTitle('物品编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('物品类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('ctmodel').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('ctunit').withTitle('计量单位').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('ctbrandmark').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('供应商').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('ctdowncnt').withTitle('安全库存下限').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('ctupcnt').withTitle('安全库存上限').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('batchno').withTitle('批次号').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('存放区域').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('warehousestr').withTitle('仓库').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.zcselect = function () {
        var meta = {};
        meta.belongcomp = $scope.outbelongCompSel.id;
        meta.loc = $scope.outareaSel.dict_item_id;
        meta.warehouse = $scope.outwarehouseSel.dict_item_id;
        meta.zcnumber = "1";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_hcout_list.html',
            controller: modalhcdblistCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta
                }
            }
        });
        modalInstance.result.then(function (result) {
            console.log("result", result);
            $scope.dtOptions.aaData = result;
        }, function (reason) {

        });
    }
    $scope.modify = function (id, zc_cnt) {
        var meta = {};
        meta.zc_cnt = zc_cnt;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_hcout_cnt.html',
            controller: modalhcdbcntCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
                if ($scope.dtOptions.aaData[i].id == id) {
                    $scope.dtOptions.aaData[i].zc_cnt = result.zc_cnt;
                }
            }
        }, function (reason) {
        });
    }
    if (angular.isDefined(meta.type) && meta.type == "dtl") {
        $scope.ctl.remark = true;
        $scope.ctl.ywtime = true;
        $scope.ctl.title = true;
        $scope.ctl.range = true;
        $scope.ctl.selectlist = true;
        $scope.ctl.footer = true;
        $http.post($rootScope.project + "/api/zc/resInout/ext/selectHcDbDataById.do",
            meta).success(function (res) {
            if (res.success) {
                $scope.data = res.data;
                $scope.dtOptions.aaData = res.data.items;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.remove = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].lid == id) {
                del = i;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    $scope.sure = function () {
        if ($scope.dtOptions.aaData == 0) {
            notify({
                message: "请选择物品"
            });
            return;
        }
        $scope.data.items = angular.toJson($scope.dtOptions.aaData)
        $scope.data.type = "hc";
        $scope.data.action = "HCDB";
        $scope.data.busitimestr = $scope.data.ywtime.format('YYYY-MM-DD');
        $scope.data.compid = $scope.outbelongCompSel.id;
        $scope.data.loc = $scope.outareaSel.dict_item_id;
        $scope.data.warehouse = $scope.outwarehouseSel.dict_item_id;
        //inbelongCompSel
        $scope.data.useduserid = $scope.inuserSel.user_id;
        $scope.data.inloc = $scope.inareaSel.dict_item_id;
        $scope.data.inwarehouse = $scope.inwarehouseSel.dict_item_id;
        $scope.data.belongcompid = $scope.inbelongCompSel.id;
        $http.post($rootScope.project + "/api/zc/resInout/ext/insert.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
}

function zcHcDbCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                   $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var pbtns = $rootScope.curMemuBtns;
    var gclassroot = '7';
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers')
        .withDisplayLength(25)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 420)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', true)
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
                extend: 'colvis',
                columns: ':gt(0)',
                text: '显示/隐藏列',
                columnText: function (dt, idx, title) {
                    return (idx ) + ': ' + title;
                }
            },
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

    function stateChange(iColumn, bVisible) {
    }

    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    $scope.dtColumns = [];
    $scope.dtColumns = [];
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
        'select-checkbox checkbox_center').renderWith(function () {
        return ""
    }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('单据编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('title').withTitle('标题').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('status').withTitle('审批状态').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(function (data, type, full) {
        if (data == "none") {
            return "无需审批"
        } else if (data == "back") {
            data == "打回"
        } else if (data == "deny") {
            data == "拒绝"
        } else if (data == "agreen") {
            data == "同意"
        } else if (data == "wait") {
            data == "待审批"
        } else {
            return data;
        }
    }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('cnt').withTitle('数量').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('outbelongcompname').withTitle($rootScope.BELONGCOMP_OUT)
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('outlocstr').withTitle('出库区域')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('outwarehousestr').withTitle('出库仓库')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('inbelongcompname').withTitle($rootScope.BELONGCOMP_IN)
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('inlocstr').withTitle('入库区域')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('inwarehousestr').withTitle('入库仓库')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('inusername').withTitle('入库使用人')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('operusername').withTitle('制单人')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('remark').withTitle('备注').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('busidate').withTitle('业务日期')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('create_time').withTitle('创建时间')
        .withOption('sDefaultContent', ''));
    $scope.query = function () {
        flush();
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

    var meta = {
        tablehide: false,
        toolsbtn: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">调拨单</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "detail",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
            },
            {
                id: "btn5",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            },
            {
                id: "btn6",
                label: "",
                type: "btn",
                show: false,
                priv: "exportfile",
                template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
            }],
        tools: [
            {
                id: "input",
                show: true,
                label: "内容",
                placeholder: "输入型号、编号、序列号",
                type: "input",
                ct: ""
            }
        ]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.toolsbtn, pbtns);

    function flush() {
        var ps = {};
        ps.type = 'hc';
        ps.action = "HCDB";
        $http.post($rootScope.project + "/api/zc/resInout/ext/selectList.do", ps)
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

    function callback(json) {
        console.log(json)
    }

    function getSelectRows() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1000) {
            notify({
                message: "不允许超过1000个"
            });
            return;
        } else {
            var res = [];
            var d = $scope.dtInstance.DataTable.context[0].json.data;
            for (var i = 0; i < data.length; i++) {
                res.push(d[data[i]].id)
            }
            return angular.toJson(res);
        }
    }

    $scope.del = function () {
        var selrows = getSelectRows();
        if (angular.isDefined(selrows)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/base/res/deleteByIds.do", {
                            ids: selrows
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
    }
    $scope.detail = function () {
        var id;
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            id = selrow.id;
        } else {
            return;
        }
        var meta = {};
        meta.type = "dtl";
        meta.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_hcdb.html',
            controller: modalhcdbCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta
                }
            }
        });
        modalInstance.result.then(function (result) {
        }, function (reason) {
        });
    }
    // //////////////////////////save/////////////////////
    $scope.save = function (type) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_hcdb.html',
            controller: modalhcdbCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
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
    flush();
};
app.register.controller('zcHcDbCtl', zcHcDbCtl);