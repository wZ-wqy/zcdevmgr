function modalhcinCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                      $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                      $uibModalInstance, $window, $stateParams, $timeout) {
    $scope.ctl = {};
    $scope.ctl.title = false;
    $scope.ctl.suppliername = false;
    $scope.ctl.remark = false;
    $scope.ctl.goods = false;
    $scope.ctl.addlist = false;
    $scope.ctl.ywtime = false;
    $scope.ctl.footer = false;
    $scope.data = {};
    $scope.data.zc_cnt = 1;
    $scope.data.batchno = new Date().getTime();
    $scope.data.buy_price = 0;
    $scope.data.ywtime = moment();
    $scope.catOpt = [];
    $scope.catSel = {};
    $scope.compOpt = [];
    $scope.compSel = {};
    $scope.locOpt = [];
    $scope.locSel = {};
    $scope.wareHouseOpt = [];
    $scope.wareHouseSel = {};
    $scope.supperOpt = [];
    $scope.supperSel = {};
    var dicts = "devdc,zcsupper,warehouse";
    if (angular.isDefined(meta.type) && meta.type == "dtl") {
        $scope.ctl.title = true;
        $scope.ctl.suppliername = true;
        $scope.ctl.remark = true;
        $scope.ctl.goods = true;
        $scope.ctl.addlist = true;
        $scope.ctl.ywtime = true;
        $scope.ctl.footer = true;
        $http.post($rootScope.project + "/api/zc/resInout/ext/selectHcInDataById.do",
            {id: meta.id}).success(function (res) {
            if (res.success) {
                $scope.data = res.data;
                if (angular.isDefined(res.data.busidate)) {
                    $scope.data.ywtime = moment(res.data.busidate);
                }
                $scope.dtOptions.aaData = res.data.items;
            } else {
            }
        })
    } else {
        $http.post($rootScope.project + "/api/zc/queryDictFast.do",
            {
                uid: "hcindicts",
                zchccat: "Y",
                comp: "Y",
                dicts: dicts
            }).success(function (res) {
            if (res.success) {
                $scope.supperOpt = res.data.zcsupper;
                if ($scope.supperOpt.length > 0) {
                    $scope.supperSel = $scope.supperOpt[0];
                }
                $scope.wareHouseOpt = res.data.warehouse;
                if ($scope.wareHouseOpt.length > 0) {
                    $scope.wareHouseSel = $scope.wareHouseOpt[0];
                }
                $scope.locOpt = res.data.devdc;
                if ($scope.locOpt.length > 0) {
                    $scope.locSel = $scope.locOpt[0];
                }
                $scope.catOpt = res.data.zchccat;
                if ($scope.catOpt.length > 0) {
                    $scope.catSel = $scope.catOpt[0];
                    $scope.catSel.seckc = $scope.catSel.downcnt + "-" + $scope.catSel.upcnt;
                    $scope.catSel.class_name = $scope.catSel.name;
                }
                $scope.compOpt = res.data.comp;
                if ($scope.compOpt.length > 0) {
                    $scope.compSel = $scope.compOpt[0];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.$watch('catSel', function (newValue, oldValue) {
        if (angular.isDefined($scope.catSel.id)) {
            $scope.catSel.seckc = $scope.catSel.downcnt + "-" + $scope.catSel.upcnt;
            $scope.catSel.class_name = $scope.catSel.name;
        }
    });
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
        var acthtml = " <a href=\"javascript:void(0)\" style=\"margin-top: 3px;\" ng-click=\"remove('" + full.lid + "')\" class=\"btn-white btn btn-xs\">??????</a>";
        return acthtml;
    }

    var dtColumns = [];
    if (angular.isDefined(meta.type) && meta.type == "dtl") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('lid').withTitle('??????').withOption(
            'sDefaultContent', '').withOption("name", '30').renderWith(renderAction));
    }
    dtColumns.push(DTColumnBuilder.newColumn('class_name').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("name", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('unit').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('brandmark').withTitle('??????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('?????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('warehousestr').withTitle('??????').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('????????????')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('??????')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('batchno').withTitle('?????????')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('??????').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.zcadd = function () {
        var e = {};
        var time = new Date().getTime();
        $scope.data.lid = time;
        $scope.data.name = $scope.catSel.name;
        if (angular.isDefined($scope.catSel.model)) {
            $scope.data.model = $scope.catSel.model;
        }
        if (angular.isDefined($scope.catSel.unit)) {
            $scope.data.unit = $scope.catSel.unit;
        }
        if (angular.isDefined($scope.locSel.dict_item_id)) {
            $scope.data.loc = $scope.locSel.dict_item_id;
            $scope.data.locstr = $scope.locSel.name;
        }
        if (angular.isDefined($scope.wareHouseSel.dict_item_id)) {
            $scope.data.warehouse = $scope.wareHouseSel.dict_item_id;
            $scope.data.warehousestr = $scope.wareHouseSel.name
        }
        if (angular.isDefined($scope.supperSel.dict_item_id)) {
            $scope.data.supplier = $scope.supperSel.dict_item_id;
            $scope.data.supplierstr = $scope.supperSel.name;
        }
        if (angular.isDefined($scope.compSel.id)) {
            $scope.data.belong_company_id = $scope.compSel.id;
            $scope.data.belongcomp_name = $scope.compSel.name;
        }
        $scope.data.busitimestr = $scope.data.ywtime.format('YYYY-MM-DD')
        $scope.data.class_id = $scope.catSel.id;
        $scope.data.class_name = $scope.catSel.name;
        $scope.data.category = $scope.catSel.root;
        $scope.data.brandmark = $scope.catSel.brandmark;
        angular.copy($scope.data, e);
        $scope.dtOptions.aaData.push(e);
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
                message: "???????????????"
            });
            return;
        }
        $scope.data.items = angular.toJson($scope.dtOptions.aaData)
        $scope.data.type = "hc";
        $scope.data.action = "HCRK";
        $scope.data.busitimestr = $scope.data.ywtime.format('YYYY-MM-DD');
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

function zcHcinCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
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
                text: '??????/?????????',
                columns: ':gt(0)',
                columnText: function (dt, idx, title) {
                    return (idx ) + ': ' + title;
                }
            },
            {
                extend: 'csv',
                text: 'Excel(?????????)',
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
                text: '??????',
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
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
        'select-checkbox checkbox_center').renderWith(function () {
        return ""
    }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('title').withTitle('??????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('status').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(function (data, type, full) {
        if (data == "none") {
            return "????????????"
        } else if (data == "back") {
            data == "??????"
        } else if (data == "deny") {
            data == "??????"
        } else if (data == "agreen") {
            data == "??????"
        } else if (data == "wait") {
            data == "?????????"
        } else {
            return data;
        }
    }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('cnt').withTitle('??????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('suppliername').withTitle('?????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('busidate').withTitle('????????????')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('createTime').withTitle('????????????')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('operusername').withTitle('?????????')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('remark').withTitle('??????').withOption(
        'sDefaultContent', ''));
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">??????</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">??????</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "detail",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">??????</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">??????</button>'
            },
            {
                id: "btn5",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">??????</button>'
            },
            {
                id: "btn6",
                label: "",
                type: "btn",
                show: false,
                priv: "exportfile",
                template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">????????????(Excel)</button>'
            }],
        tools: [
            // 	{
            // 	id : "select",
            // 	label : "??????",
            // 	type : "select",
            // 	disablesearch : true,
            // 	show:true,
            // 	dataOpt : [],
            // 	dataSel : ""
            // }, {
            // 	id : "select",
            // 	label : "??????",
            // 	type : "select",
            // 	disablesearch : true,
            // 	show:true,
            // 	dataOpt : [],
            // 	dataSel : ""
            // },
            {
                id: "input",
                show: true,
                label: "??????",
                placeholder: "?????????????????????????????????",
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
        ps.action = "HCRK";
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

    function getSelectRow() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "?????????????????????"
            });
            return;
        } else if (data.length > 1) {
            notify({
                message: "?????????????????????"
            });
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    $scope.del = function () {
        var selrows = getSelectRows();
        if (angular.isDefined(selrows)) {
            $confirm({
                text: '?????????????'
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
    // //////////////////////////save/////////////////////
    $scope.save = function (type) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_hcin.html',
            controller: modalhcinCtl,
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
            templateUrl: 'views/cmdb/modal_hcin.html',
            controller: modalhcinCtl,
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
    flush();
    $scope.hctj = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_hctj.html',
            controller: modalHcTjCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
                }
            }
        });
        modalInstance.result.then(function (result) {
        }, function (reason) {
        });
    }
};
app.register.controller('zcHcinCtl', zcHcinCtl);