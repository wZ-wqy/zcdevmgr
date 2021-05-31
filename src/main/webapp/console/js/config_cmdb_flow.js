
function assetsBaseColsCreate(DTColumnBuilder, selectype, colctl) {
//selectype:withoutselect,withselect
    //colctl
    var colctlobj = angular.fromJson(colctl);
    if (angular.isUndefined(colctlobj)) {
        colctlobj = {};
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    var dtColumns = [];
    if (selectype == "withselect") {
        dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }));
    }
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    if (angular.isDefined(colctlobj.fs20) && colctlobj.fs20 == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('fs20').withTitle('其他编号').withOption(
            'sDefaultContent', ''));
    }
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('classfullname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('资产名称').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));

    if (angular.isDefined(colctlobj.sn) && colctlobj.sn == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
            'sDefaultContent', '').withOption('width', '50'));
    }
    if (angular.isDefined(colctlobj.zcsourcestr) && colctlobj.zcsourcestr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('zcsourcestr').withTitle('来源').withOption(
            'sDefaultContent', '').withOption("width", '30'));
    }
    if (angular.isDefined(colctlobj.supplierstr) && colctlobj.supplierstr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('供应商').withOption(
            'sDefaultContent', '').withOption("width", '30'));
    }
    if (angular.isDefined(colctlobj.brandstr) && colctlobj.brandstr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
            'sDefaultContent', '').withOption('width', '30'));
    }
    if (angular.isDefined(colctlobj.unit) && colctlobj.unit == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('unit').withTitle('计量单位').withOption(
            'sDefaultContent', ''));
    }
    // if (angular.isDefined(colctlobj.zc_cnt) && colctlobj.zc_cnt == "N") {
    // } else {
    //     dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
    //         .withOption('sDefaultContent', ''));
    // }
    //
    if (angular.isDefined(colctlobj.confdesc) && colctlobj.confdesc == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
            'sDefaultContent', ''));
    }
    if (angular.isDefined(colctlobj.belongcomp_name) && colctlobj.belongcomp_name == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle('所属公司').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));

    }

    // if (angular.isDefined(colctlobj.comp_name) && colctlobj.comp_name == "N") {
    // } else {
    //     dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle('使用公司').withOption(
    //         'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    // }
    if (angular.isDefined(colctlobj.part_fullname) && colctlobj.part_fullname == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('part_fullname').withTitle('使用部门').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    }
    if (angular.isDefined(colctlobj.used_username) && colctlobj.used_username == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle('使用人').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    }
    if (angular.isDefined(colctlobj.locstr) && colctlobj.locstr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('存放区域').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    }
    if (angular.isDefined(colctlobj.locdtl) && colctlobj.locdtl == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('locdtl').withTitle('位置').withOption(
            'sDefaultContent', '').renderWith(renderZcLoc));
    }
    if (angular.isDefined(colctlobj.usefullifestr) && colctlobj.usefullifestr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
            .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    }
    if (angular.isDefined(colctlobj.buy_timestr) && colctlobj.buy_timestr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('buy_timestr').withTitle('采购日期')
            .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    }
    if (angular.isDefined(colctlobj.buy_price) && colctlobj.buy_price == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购单价')
            .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    }
    if (angular.isDefined(colctlobj.net_worth) && colctlobj.net_worth == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('net_worth').withTitle('资产净值')
            .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    }
    if (angular.isDefined(colctlobj.accumulateddepreciation) && colctlobj.accumulateddepreciation == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('accumulateddepreciation').withTitle('累计折旧')
            .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    }
    if (angular.isDefined(colctlobj.wbsupplierstr) && colctlobj.wbsupplierstr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保商').withOption(
            'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    }
    if (angular.isDefined(colctlobj.wbstr) && colctlobj.wbstr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
            'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    }
    if (angular.isDefined(colctlobj.wbout_datestr) && colctlobj.wbout_datestr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
            .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    }
    // if (angular.isDefined(colctlobj.wb_autostr) && colctlobj.wb_autostr == "N") {
    // } else {
    //     dtColumns.push(DTColumnBuilder.newColumn('wb_autostr').withTitle('脱保计算')
    //         .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    // }
    if (angular.isDefined(colctlobj.mark) && colctlobj.mark == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''));
    }
    if (angular.isDefined(colctlobj.fs1) && colctlobj.fs1 == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('fs1').withTitle('标签1').withOption(
            'sDefaultContent', ''));
    }
    if (angular.isDefined(colctlobj.fs2) && colctlobj.fs2 == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('fs2').withTitle('标签2').withOption(
            'sDefaultContent', ''));
    }
    if (angular.isDefined(colctlobj.lastinventorytimestr) && colctlobj.lastinventorytimestr == "N") {
    } else {
        dtColumns.push(DTColumnBuilder.newColumn('lastinventorytimestr').withTitle('最近盘点')
            .withOption('sDefaultContent', ''));
    }
    return dtColumns;
}

//##########################################资产调拨##########################################//
function assetsallocationCtl($timeout, $localStorage, notify, $log, $uibModal,
                              $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                              DTColumnBuilder, $compile) {
    //type:detail,sure,add

    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;

    $scope.ctl = {};
    $scope.ctl.name = false;
    $scope.ctl.inuserSel = false;
    $scope.ctl.outcompSel = false;
    $scope.ctl.incompSel = false;
    // $scope.ctl.inpartSel = false;
    $scope.ctl.locSel = false;
    $scope.ctl.tolocdtl = false;
    $scope.ctl.mark = false;
    $scope.ctl.chosenzcbtn = false;
    $scope.ctl.surebtn = false;
    $scope.ctl.busdate = false;
    if (meta.actiontype == "detail") {
        $scope.ctl.name = true;
        $scope.ctl.inuserSel = true;
        $scope.ctl.outcompSel = true;
        $scope.ctl.incompSel = true;
        // $scope.ctl.inpartSel = true;
        $scope.ctl.locSel = true;
        $scope.ctl.tolocdtl = true;
        $scope.ctl.mark = true;
        $scope.ctl.chosenzcbtn = true;
        $scope.ctl.surebtn = true;
        $scope.ctl.busdate = true;
    } else if (meta.actiontype == "sure") {
        $scope.ctl.name = true;
        $scope.ctl.inuserSel = true;
        $scope.ctl.outcompSel = true;
        $scope.ctl.incompSel = true;
        // $scope.ctl.inpartSel = true;
        $scope.ctl.locSel = true;
        $scope.ctl.tolocdtl = true;
        $scope.ctl.mark = true;
        $scope.ctl.chosenzcbtn = true;
        $scope.ctl.surebtn = true;
        $scope.ctl.busdate = true;
    } else if (meta.actiontype == "add") {
    }
    $scope.date = {
        busdate: moment()
    }
    $scope.outcompOpt = [];
    $scope.outcompSel = "";
    $scope.incompOpt = [];
    $scope.incompSel = "";
    $scope.inuserOpt = [];
    $scope.inuserSel = "";
    $scope.locOpt = [];
    $scope.locSel = "";
    // $scope.inpartOpt = [];
    // $scope.inpartSel = "";
    $scope.outcompOpt = [];
    $scope.outcompSel = "";

    $scope.inuserOpt = [];
    $scope.incompOpt = [];

    $scope.locOpt = [];
    $scope.locSe = "";


    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        })
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');
    $scope.dtOptions.aaData = [];
    $scope.data = {};
    $scope.data.mark = "";
    $scope.data.reason = "";
    $scope.selectzc = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "DB";
        if (angular.isUndefined($scope.outcompSel.id)) {
            notify({
                message: "请选择调出组织"
            });
            return;
        }
       // mdata.usedcompid = $scope.outcompSel.id;
        mdata.belongcompid = $scope.outcompSel.id;
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


    var dicts = "devdc"
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do",
            {
                dicts: dicts,
                parts: "Y",
                partusers: "Y",
                comp: "Y",
                belongcomp: "Y",
                comppart: "Y",
                uid: "allocation"
            }).success(function (rs) {
        if (rs.success) {
            meta.gdict = rs.data;
            $scope.outcompOpt = meta.gdict.comp;
            if (meta.gdict.length > 0) {
                $scope.outcompSel = $scope.outcompOpt[0];
            }
            $scope.inuserOpt = meta.gdict.partusers;
            $scope.incompOpt = meta.gdict.comp;
            $scope.locOpt = meta.gdict.devdc;
            if (meta.gdict.devdc.length > 0) {
                $scope.locSel = $scope.locOpt[0];
            }


            if (angular.isDefined(meta.busid)) {
                //获取数据
                $http.post($rootScope.project + "/api/zc/resAllocate/ext/selectByBusid.do",
                    {busid: meta.busid}).success(function (res) {
                    if (res.success) {
                        $scope.dtOptions.aaData = res.data.items;
                        $scope.data = res.data;
                        if (angular.isDefined(res.data.frombelongcompid)) {
                            for (var i = 0; i < $scope.outcompOpt.length; i++) {
                                if ($scope.outcompOpt[i].id == res.data.frombelongcompid) {
                                    $scope.outcompSel = $scope.outcompOpt[i];
                                    break;
                                }
                            }
                        }
                        if (angular.isDefined(res.data.tobelongcompid)) {
                            for (var i = 0; i < $scope.incompOpt.length; i++) {
                                if ($scope.incompOpt[i].id == res.data.tobelongcompid) {
                                    $scope.incompSel = $scope.incompOpt[i];
                                    break;
                                }
                            }
                        }
                        if (angular.isDefined(res.data.toloc)) {
                            for (var i = 0; i < $scope.locOpt.length; i++) {
                                if ($scope.locOpt[i].dict_item_id == res.data.toloc) {
                                    $scope.locSel = $scope.locOpt[i];
                                    break;
                                }
                            }
                        }
                        if (angular.isDefined(res.data.allocateuserid)) {
                            var e = {};
                            e.user_id = $scope.data.allocateuserid;
                            e.name = $scope.data.allocateusername;
                            var arr = [];
                            arr.push(e);
                            $scope.inuserOpt = arr;
                            $scope.inuserSel = $scope.inuserOpt[0];
                        }
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                })
            }


        } else {
            notify({
                message: rs.message
            });
        }
    })


    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function close() {
        $uibModalInstance.close('OK');
    }

    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        if (angular.isUndefined($scope.inuserSel.user_id)) {
            notify({
                message: "请选择调度人"
            });
            return
        }
        if (angular.isUndefined($scope.outcompSel.id)) {
            notify({
                message: "请选择调出组织"
            });
            return
        }
        if (angular.isUndefined($scope.incompSel.id)) {
            notify({
                message: "请选择调入组织"
            });
            return
        }
        if (angular.isUndefined($scope.locSel.dict_id)) {
            notify({
                message: "请选择调入区域"
            });
            return
        }
        $scope.data.items = angular.toJson($scope.dtOptions.aaData);
        $scope.data.allocateuserid = $scope.inuserSel.user_id;
        $scope.data.allocateusername = $scope.inuserSel.name;

        $scope.data.frombelongcompid = $scope.outcompSel.id;
        $scope.data.frombelongcompname = $scope.outcompSel.name;

        $scope.data.tobelongcompid = $scope.incompSel.id;
        $scope.data.tobelongcompname = $scope.incompSel.name;

        $scope.data.toloc = $scope.locSel.dict_item_id;
        $scope.data.tolocname = $scope.locSel.name;
        $scope.data.busdate = $scope.date.busdate.format('YYYY-MM-DD');
        $http.post($rootScope.project + "/api/zc/resAllocate/ext/save.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            }
            notify({
                message: res.message
            });
        })
    }
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
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
}

//##########################################报废处理窗口##########################################//
function assetsbfCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                      $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                      $uibModalInstance, $window, $stateParams, $timeout) {
    console.log(meta);
    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;

    $scope.ctl = {}
    $scope.ctl.remark = false;
    $scope.ctl.ywtime = false;
    $scope.ctl.title = false;
    $scope.ctl.range = false;
    $scope.ctl.selectlist = false;
    $scope.ctl.footer = false;
    $scope.ctl.ct = false;
    $scope.data = {};
    $scope.data.zc_cnt = 0;
    $scope.data.ywtime = moment();
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
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function close() {
        $uibModalInstance.close('OK');
    }

    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        })
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');

    // function renderAction(data, type, full) {
    //     var acthtml = " <a href=\"javascript:void(0)\" style=\"margin-top: 3px;\" ng-click=\"modify('" + full.id + "'," + full.zc_cnt + ")\" class=\"btn-white btn btn-xs\">修改</a>";
    //     return acthtml;
    // }

    // if (angular.isDefined(meta.flowpagetype) && meta.flowpagetype == "lookup") {
    // } else {
    //     $scope.dtColumns.push(DTColumnBuilder.newColumn('lid').withTitle('操作').withOption(
    //         'sDefaultContent', '').withOption("name", '30').renderWith(renderAction));
    // }
    // $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('单据编号').withOption(
    //     'sDefaultContent', '').withOption("width", '30'));
    $scope.dtOptions.aaData = [];
    $scope.zcselect = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "BF";
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
    // $scope.modify = function (id, zc_cnt) {
    //     var meta = {};
    //     meta.zc_cnt = zc_cnt;
    //     var modalInstance = $uibModal.open({
    //         backdrop: true,
    //         templateUrl: 'views/cmdb/modal_hcout_cnt.html',
    //         controller: modalhcoutcntCtl,
    //         size: 'blg',
    //         resolve: {
    //             meta: function () {
    //                 return meta;
    //             }
    //         }
    //     });
    //     modalInstance.result.then(function (result) {
    //         for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
    //             if ($scope.dtOptions.aaData[i].id == id) {
    //                 $scope.dtOptions.aaData[i].zc_cnt = result.zc_cnt;
    //             }
    //         }
    //     }, function (reason) {
    //     });
    // }
    if (meta.flowpagetype == "lookup" || meta.flowpagetype == "approval") {
        $scope.ctl.remark = true;
        $scope.ctl.ywtime = true;
        $scope.ctl.title = true;
        $scope.ctl.range = true;
        $scope.ctl.selectlist = true;
        $scope.ctl.footer = true;
        $scope.ctl.ct = true;
    }
    if (angular.isDefined(meta.busid)) {
        $http.post($rootScope.project + "/api/zc/resScrape/ext/selectByBusid.do",
            {busid: meta.busid}).success(function (res) {
            if (res.success) {
                $scope.data = res.data;
                $scope.dtOptions.aaData = res.data.items;
                $scope.data.ywtime = moment(res.data.busidate);
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
        $scope.data.busitimestr = $scope.data.ywtime.format('YYYY-MM-DD');
        $http.post($rootScope.project + "/api/zc/resScrape/ext/insert.do",
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
    //是否ids传入
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
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
}


//资产借用单据
// meta.busid = uuid;
// meta.status = status;
// meta.flowpagetype = "lookup";
function zcjyghlistCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                       DTColumnBuilder, $compile) {
    $scope.item = {};
    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;
    $scope.ctl = {};
    $scope.ctl.hidesuggest = true;
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
        });
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    var dtColumns = [];

    function renderZcReturn(data, type, full) {
        if (data == "1") {
            return "已归还"
        } else if (data == "0") {
            return "未归还"
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('busuuid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
            'sDefaultContent', '').withOption('width', '50'),
        DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
            'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle),
        DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
            'sDefaultContent', '').withOption('width', '50'),
        DTColumnBuilder.newColumn('lrusername').withTitle('借用人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('lruserorginfo').withTitle('借用人所属组织').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('busdatestr').withTitle('借用时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('returndatestr').withTitle('预计归还时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rreturndatestr').withTitle('实际归还时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('isreturn').withTitle('是否归还').withOption(
            'sDefaultContent', '').renderWith(renderZcReturn),
        DTColumnBuilder.newColumn('returnuuid').withTitle('归还单据号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')]
    function flush() {
        $http.post($rootScope.project + "/api/zc/resLoanreturn/ext/selectByUuid.do",
            {uuid: meta.busid}).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                $scope.dtOptions.aaData = res.data.items;
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
    function close() {
        $uibModalInstance.close('OK');
    }
    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
}

//资产退库
// meta.busid = uuid;
// meta.status = status;
// meta.flowpagetype = "lookup";
function zctklistCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                     DTColumnBuilder, $compile) {
    console.log(meta);
    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;
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
        });
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    var dtColumns = [];

    function renderZcReturn(data, type, full) {
        if (data == "1") {
            return "已退库"
        } else if (data == "0") {
            return "未退库"
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('busuuid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
            'sDefaultContent', '').withOption('width', '50'),
        DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
            'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle),
        DTColumnBuilder.newColumn('rreturndatestr').withTitle('实际退库时间').withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('tcompfullname').withTitle($rootScope.USEDCOMP_A).withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tpartfullame').withTitle($rootScope.USEDPART_A).withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tusedusername').withTitle('使用人(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        // DTColumnBuilder.newColumn('tlocstr').withTitle('区域(变更后)').withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tlocdtl').withTitle('位置(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        // DTColumnBuilder.newColumn('fcompfullname').withTitle($rootScope.USEDCOMP_B).withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('fpartfullame').withTitle($rootScope.USEDPART_B).withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('fusedusername').withTitle('使用人(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        // DTColumnBuilder.newColumn('flocstr').withTitle('区域(变更前)').withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('flocdtl').withTitle('位置(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')]

    function flush() {
        $http.post($rootScope.project + "/api/zc/resCollectionreturn/ext/selectByUuid.do",
            {"uuid": meta.busid}).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                $scope.dtOptions.aaData = res.data.items;
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
    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
}

//资产领用单据
// meta.busid = uuid;
// meta.status = status;
// meta.flowpagetype = "lookup";
function zclylistCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                     DTColumnBuilder, $compile) {
    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;
    $scope.ctl = {};
    $scope.ctl.hidesuggest = true;
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
        });
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    var dtColumns = [];
    function renderZcReturn(data, type, full) {
        if (data == "1") {
            return "已退库"
        } else if (data == "0") {
            return "未退库"
        } else {
            return data;
        }
    }
    $scope.dtColumns = [
        DTColumnBuilder.newColumn('busuuid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
            'sDefaultContent', '').withOption('width', '50'),
        DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
            'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle),
        DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
            'sDefaultContent', '').withOption('width', '50'),
        DTColumnBuilder.newColumn('busdatestr').withTitle('领用时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('returndatestr').withTitle('预计退库时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rreturndatestr').withTitle('实际退库时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('isreturn').withTitle('是否退库').withOption(
            'sDefaultContent', '').renderWith(renderZcReturn),
        DTColumnBuilder.newColumn('returnuuid').withTitle('退库编号').withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('tcompfullname').withTitle($rootScope.USEDCOMP_A).withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tpartfullame').withTitle($rootScope.USEDPART_A).withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tusedusername').withTitle('使用人(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        // DTColumnBuilder.newColumn('tlocstr').withTitle('区域(变更后)').withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tlocdtl').withTitle('位置(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        // DTColumnBuilder.newColumn('fcompfullname').withTitle($rootScope.USEDCOMP_B).withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('fpartfullame').withTitle($rootScope.USEDPART_B).withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('fusedusername').withTitle('使用人(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        // DTColumnBuilder.newColumn('flocstr').withTitle('区域(变更前)').withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('flocdtl').withTitle('位置(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColoBluerH),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')]
    //显示意见数据
    function flush() {
        $http.post($rootScope.project + "/api/zc/resCollectionreturn/ext/selectByUuid.do",
            {"uuid": meta.busid}).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                $scope.dtOptions.aaData = res.data.items;
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

    function close() {
        $uibModalInstance.close('OK');
    }

    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
}

function resTranferOrderCtl($timeout, $localStorage, notify, $log, $uibModal,
                            $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                            DTColumnBuilder, $compile) {
    var dicts = "devdc"

    $scope.locOpt=[];
    $scope.locSel="";
    $scope.incompOpt=[];
    $scope.incompSel={};
    $scope.inpartOpt=[];
    $scope.inpartSel={};
    $scope.inuserOpt=[];
    $scope.inuserSel={};
    $scope.date = {
        receivedate: moment()
    }
    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;
    $scope.item={};
    $scope.catOpt=[{id:"inside",name:"离职"},{id:"outside",name:"调拨"},{id:"other",name:"其他"}];
    $scope.catSel= $scope.catOpt[0];
    //save,select,
    if(angular.isUndefined(meta.pagetype)){
        meta.pagetype="select";
    }
    $scope.pagetype=meta.pagetype;
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtOptions =
        DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
            .withPaginationType('full_numbers').withDisplayLength(50)
            .withOption("ordering", false).withOption("responsive", false)
            .withOption("searching", true).withOption('scrollY', 600)
            .withOption('scrollX', true).withOption('bAutoWidth', true)
            .withOption('scrollCollapse', true).withOption('paging', false)
            .withOption('bStateSave', true).withOption('bProcessing', false)
            .withOption('bFilter', false).withOption('bInfo', true)
            .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        })
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');
    $scope.dtOptions.aaData = [];

    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do",
            {
                dicts: dicts,
                parts: "Y",
                partusers: "Y",
                comp: "Y",
                belongcomp: "Y",
                comppart: "Y",
                uid: "resTranfer"
            }).success(function (res) {
        if (res.success) {
            $scope.incompOpt=res.data.comp
            if($scope.incompOpt.length>0){
                $scope.incompSel=$scope.incompOpt[0]
            }
            $scope.inpartOpt=res.data.parts
            if($scope.inpartOpt.length>0){
                $scope.inpartSel=$scope.inpartOpt[0]
            }

            $scope.locOpt=res.data.devdc
            if($scope.locOpt.length>0){
                $scope.locSel=$scope.locOpt[0]
            }
            $scope.inuserOpt=res.data.partusers;


            if(angular.isDefined(meta.busid)){
                $http.post($rootScope.project + "/api/zc/resTranfer/ext/selectByBusid.do",
                    {busid:meta.busid}).success(function (res) {
                    if (res.success) {
                        $scope.item=res.data;

                        if(angular.isDefined(res.data.transfercatid)){
                            for(var i=0;i<$scope.catOpt.length;i++){
                                if($scope.catOpt[i].id==res.data.transfercatid){
                                    $scope.catSel=$scope.catOpt[i] ;
                                    break;
                                }
                            }

                        }
                        $scope.date.receivedate=  moment(res.data.receivedatestr);
                        $scope.dtOptions.aaData = res.data.items;
                        if (angular.isDefined(res.data.tloc)) {
                            for (var i = 0; i < $scope.locOpt.length; i++) {
                                if ($scope.locOpt[i].id == res.data.tloc) {
                                    $scope.locSel = $scope.locOpt[i];
                                    break;
                                }
                            }
                        }
                        if (angular.isDefined(res.data.tusedcompid)) {
                            for (var i = 0; i < $scope.incompOpt.length; i++) {
                                if ($scope.incompOpt[i].id == res.data.tusedcompid) {
                                    $scope.incompSel = $scope.incompOpt[i];
                                    break;
                                }
                            }
                        }
                        if (angular.isDefined(res.data.tusedpartid)) {
                            for (var i = 0; i < $scope.inpartOpt.length; i++) {
                                if ($scope.inpartOpt[i].partid == res.data.tusedpartid) {
                                    $scope.inpartSel = $scope.inpartOpt[i];
                                    break;
                                }
                            }
                        }

                    }
                })
            }

        } else {
            notify({
                message: res.message
            });
        }
    })

console.log('ok',meta)
    $scope.$watch('inpartSel', function (newValue, oldValue) {
        if (angular.isDefined(newValue) && angular.isDefined(newValue.partid)) {

            // if(meta.pagetype=="insert"){
                $scope.inuserOpt =[];
                $scope.inuserSel={};
                $http.post(
                    $rootScope.project
                    + "/api/hrm/queryEmplByOrg.do", {
                        node_id: newValue.partid
                    }).success(function (result) {
                    if (result.success) {
                        $scope.inuserOpt = result.data;
                        $scope.inuserSel={}

                        console.log('watch',$scope.item)
                        if (angular.isDefined(meta.busid)&& angular.isDefined($scope.item.receiveruserid) ) {

                            for (var i = 0; i < $scope.inuserOpt.length; i++) {
                                console.log(i)
                                if ($scope.inuserOpt[i].user_id ==$scope.item.receiveruserid) {
                                    $scope.inuserSel = $scope.inuserOpt[i];
                                    break;
                                }
                            }
                        }
                    }
                });
//            }


        }
    });



    function close() {
        $uibModalInstance.close('OK');
    }
    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }


        if(angular.isUndefined($scope.inuserSel.user_id)){
            notify({
                message: "请选择接收人"
            });
            return;

        }
        $scope.item.transfercatid=$scope.catSel.id;
        $scope.item.transfercatname=$scope.catSel.name;

        $scope.item.tusedcompid=$scope.incompSel.id;
        $scope.item.tusedcompname=$scope.incompSel.name

        $scope.item.receiveruserid= $scope.inuserSel.user_id
        $scope.item.receiverusername= $scope.inuserSel.name

        $scope.item.tloc=$scope.locSel.dict_item_id;
        $scope.item.tlocname=$scope.locSel.name;
        $scope.item.receivedate = $scope.date.receivedate.format('YYYY-MM-DD');
        $scope.item.tusedpartid=$scope.inpartSel.partid;
        $scope.item.tusedpartname=$scope.inpartSel.name;
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        $http.post($rootScope.project + "/api/zc/resTranfer/ext/save.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            }
            notify({
                message: res.message
            });
        })


    }

    $scope.selectzc = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "ZY";


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

function resPurchaseOrderCtl($timeout, $localStorage, notify, $log, $uibModal,
                             $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                             DTColumnBuilder, $compile) {
    //type:detail,sure,add
    $rootScope.flowpagetype = meta.flowpagetype;
    $rootScope.flowbusid = meta.busid;
    $rootScope.flowtaskid = meta.taskid;
    $scope.item={};
    $scope.planOpt=[{id:"inside",name:"计划内"},{id:"outside",name:"计划外"}];
    $scope.planSel=$scope.planOpt[0];
    //save,select,
    if(angular.isUndefined(meta.pagetype)){
        meta.pagetype="select";
    }
    $scope.pagetype=meta.pagetype;

    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 1,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 1,
        maxFiles: 1,
        dictDefaultMessage: "点击上传需要上传的文件",
        acceptedFiles: "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        init: function () {
            $scope.myDropzone = this; // closure
        }
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    if(angular.isDefined(meta.busid)){
        $http.post($rootScope.project + "/api/zc/resPurchase/ext/selectById.do",
            {busid:meta.busid}).success(function (res) {
            if (res.success) {
                $scope.item=res.data;
                if($scope.item.plan=="inside"){
                    $scope.planSel=$scope.planOpt[0];
                }else if($scope.item.plan=="outside"){
                    $scope.planSel=$scope.planOpt[1];
                }
                if (angular.isDefined($scope.item.files) && $scope.item.files.length > 0) {
                    setTimeout(function () {
                        var mockFile = {
                            size: 0,
                            name: "附件",
                            // 需要显示给用户的图片名
                            uuid: $scope.item.files,
                            href: $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.files,
                            url: $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.files,
                            status: "success",
                            accepted: true
                        };
                        $scope.myDropzone.emit("addedfile", mockFile);
                        $scope.myDropzone.files.push(mockFile); // file must be added
                        // manually
                        $scope.myDropzone.createThumbnailFromUrl(mockFile, $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.files);
                        $scope.myDropzone.emit("complete", mockFile);
                    }, 500)
                }



            }

        })
    }

    function close() {
        $uibModalInstance.close('OK');
    }
    $scope.windowclose = function () {
        $uibModalInstance.close('OK');
    }
    $scope.sure = function () {
        var id = getUuid();
        if ($scope.myDropzone.files.length > 0) {
            if (typeof ($scope.myDropzone.files[0].uuid) == "undefined") {
                // 需要上传
                $scope.myDropzone.options.url = $rootScope.project
                    + '/api/file/fileupload.do?uuid=' + id
                    + '&bus=file&interval=10000&bus=file';
                $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
            } else {
                id = $scope.myDropzone.files[0].uuid;
            }
            $scope.item.files = id;
        } else {
            $scope.item.files = "";
        }

        $scope.item.plan=$scope.planSel.id;
        $http.post($rootScope.project + "/api/zc/resPurchase/ext/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            }
            notify({
                message: res.message
            });
        })
    }

}