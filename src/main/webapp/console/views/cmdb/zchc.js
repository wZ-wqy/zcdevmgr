function modalHcTjCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                      $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,
                      $uibModalInstance, $window, $stateParams, $timeout) {
    // 分类
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 200)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        })
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctid').withTitle('物品编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctname').withTitle('物品类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctmodel').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctunit').withTitle('计量单位').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctunitprice').withTitle('默认单价').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctdowncnt').withTitle('安全库存下限').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctupcnt').withTitle('安全库存上限').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('当前数量').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('存放区域').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('warehousestr').withTitle('仓库').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ctmark').withTitle('备注').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    var gdicts = {};
    var dicts = "devdc";
    $scope.areaOpt = [];
    $scope.areaSel = {};
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            uid: "hctjlist",
            dicts: dicts
        })
        .success(
            function (res) {
                if (res.success) {
                    gdicts = res.data;
                    angular.copy(gdicts.devdc, $scope.areaOpt);
                    $scope.areaOpt.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.areaSel = $scope.areaOpt[0];
                    flush();
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    function flush() {
        var ps = {};
        if ($scope.areaSel.dict_item_id != "all") {
            ps.loc = $scope.areaSel.dict_item_id;
        }
        $http.post($rootScope.project + "/api/zc/resInout/ext/selectHcTj.do",
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

    $scope.query = function () {
        flush();
    }
}

function zcHcCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                 $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var pbtns = $rootScope.curMemuBtns;
    var gclassroot = '7';
    $scope.URL = $rootScope.project + "/api/base/res/queryPageResAllByClass.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: {classroot: "-1", start: 0}
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(25)
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
        }).withButtons([
            {
                extend: 'colvis',
                text: '显示/隐藏列',
                columns: ':gt(0)',
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
    $scope.dtColumns = zcBaseColsHCCreate(DTColumnBuilder, 'withselect');
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
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            // {
            // 	id : "btn2",
            // 	label : "",
            // 	type : "btn",
            // 	show:false,
            // 	priv:"insert",
            // 	template : ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">入库</button>'
            // },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            // {
            // 	id : "btn4",
            // 	label : "",
            // 	type : "btn",
            // 	show:false,
            // 	priv:"detail",
            // 	template : ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
            // },
            {
                id: "btn5",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="hctj()" class="btn btn-sm btn-primary" type="submit">耗材统计</button>'
            }
            // ,
            // {
            //     id: "btn6",
            //     label: "",
            //     type: "btn",
            //     show: false,
            //     priv: "exportfile",
            //     template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
            // }
        ],
        tools: [{
            id: "select",
            label: "存放区域",
            width: "200",
            type: "select",
            disablesearch: true,
            show: true,
            dataOpt: [],
            dataSel: ""
        }, {
            id: "select",
            label: "状态",
            type: "select",
            disablesearch: true,
            show: true,
            dataOpt: [],
            dataSel: ""
        }, {
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入型号、编号、序列号",
            type: "input",
            ct: ""
        }]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.toolsbtn, pbtns);

    function flush() {
        var ps = {}
        var time = new Date().getTime();
        ps.classroot = gclassroot;
        ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
        ps.recycle = $scope.meta.tools[1].dataSel.dict_item_id;
        ps.search = $scope.meta.tools[2].ct;
        ps.time = time;
        $scope.dtOptions.ajax.data = ps
        $scope.dtInstance.reloadData(callback, true);
    }

    function callback(json) {
        console.log(json)
    }

    $scope.filedown = function () {
        var ps = {}
        ps.classroot = gclassroot;
        ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
        ps.recycle = $scope.meta.tools[1].dataSel.dict_item_id;
        ps.search = $scope.meta.tools[2].ct;
        $window.open($rootScope.project
            + "/api/base/res/exportServerData.do?classroot=" + ps.classroot + "&loc="
            + ps.loc + "&recycle="
            + ps.recycle + "&search=" + ps.search);
    }
    var gdicts = {};
    //
    var dicts = "warehouse,zcwbcomoute,devbrand,devwb,devdc,devrecycle,zcsource,zcwbsupper,zcsupper";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "Y",
            uid: "zchcdictdata",
            classroot: gclassroot
        })
        .success(
            function (res) {
                if (res.success) {
                    gdicts = res.data;
                    var btype = [];
                    angular.copy(gdicts.btype, btype);
                    // 填充行数据
                    var tenv = [];
                    angular.copy(gdicts.devenv, tenv);
                    var twb = [];
                    angular.copy(gdicts.devwb, twb);
                    var tloc = [];
                    angular.copy(gdicts.devdc, tloc);
                    var trecycle = [];
                    angular.copy(gdicts.devrecycle, trecycle);
                    // var trecycle = [];
                    // var trecycle2=[];
                    // angular.copy(gdicts.zchcstatus, trecycle2);
                    // angular.copy(trecycle2, trecycle);
                    // gdicts.devrecycle=trecycle2;
                    var parts = [];
                    angular.copy(gdicts.parts, parts);
                    var partusers = [];
                    angular.copy(gdicts.partusers, partusers);
                    tloc.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.meta.tools[0].dataOpt = tloc;
                    $scope.meta.tools[0].dataSel = tloc[0];
                    trecycle.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.meta.tools[1].dataOpt = trecycle;
                    $scope.meta.tools[1].dataSel = trecycle[0];
                    flush();
                } else {
                    notify({
                        message: res.message
                    });
                }
            })

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

    $scope.batchupate = function () {
        var selrows = getSelectRows();
        var ps = {};
        if (angular.isDefined(selrows)) {
            ps.selrows = selrows;
            ps.gdicts = gdicts;
            var modalInstance = $uibModal.open({
                backdrop: true,
                templateUrl: 'views/cmdb/modal_batchUpdateRes.html',
                controller: modalresBatchUpdateCtl,
                size: 'blg',
                resolve: {
                    meta: function () {
                        return ps;
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
        var id = "";
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            id = selrow.id;
        } else {
            return;
        }
        var ps = {};
        ps.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_dtl.html',
            controller: modalcmdbdtlCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
            }
        }, function (reason) {
        });
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
            var d = $scope.dtInstance.DataTable.context[0].json.data;
            return d[data[0]]
        }
    }

    // //////////////////////////save/////////////////////
    $scope.save = function (type) {
        var id;
        var zcrecycle = "false";
        if (type == 1) {
            var selrow = getSelectRow();
            if (angular.isDefined(selrow)) {
                id = selrow.id;
                zcrecycle = "true";
            } else {
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
                    var items = [];
                    items.push({
                        type: "input",
                        disabled: "true",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "系统自动生成",
                        label: "资产编号",
                        need: false,
                        name: 'uuid',
                        ng_model: "uuid"
                    });
                    items.push({
                        type: "select",
                        disabled: "true",
                        label: "物品类别",
                        need: false,
                        disable_search: "false",
                        dataOpt: "classOpt",
                        dataSel: "classSel"
                    });
                    items.push({
                        type: "input",
                        disabled: "true",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "请输型号",
                        label: "规格型号",
                        need: true,
                        name: 'ctmodel',
                        ng_model: "ctmodel"
                    });
                    items.push({
                        type: "input",
                        disabled: "true",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "计量单位",
                        label: "计量单位",
                        need: false,
                        name: 'ctunit',
                        ng_model: "ctunit"
                    });
                    items.push({
                        type: "input",
                        disabled: "true",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "品牌",
                        label: "品牌",
                        need: false,
                        name: 'ctbrandmark',
                        ng_model: "ctbrandmark"
                    });
                    items.push({
                        type: "select",
                        disabled: "false",
                        label: "厂商",
                        need: false,
                        disable_search: "false",
                        dataOpt: "zcsupperOpt",
                        dataSel: "zcsupperSel"
                    });
                    items.push({
                        type: "input",
                        disabled: "false",
                        sub_type: "number",
                        required: true,
                        maxlength: "50",
                        placeholder: "",
                        label: "数量",
                        need: true,
                        name: 'zc_cnt',
                        ng_model: "zc_cnt"
                    });
                    items.push({
                        type: "input",
                        disabled: "false",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "",
                        label: "批次号",
                        need: false,
                        name: 'batchno',
                        ng_model: "batchno"
                    });
                    items.push({
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
                    });
                    items.push({
                        type: "input",
                        disabled: "false",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "请输入标签",
                        label: "标签1",
                        need: false,
                        name: 'fs1',
                        ng_model: "fs1"
                    });
                    items.push({
                        type: "input",
                        disabled: "false",
                        sub_type: "text",
                        required: false,
                        maxlength: "50",
                        placeholder: "请输入标签",
                        label: "标签2",
                        need: false,
                        name: 'fs2',
                        ng_model: "fs2"
                    });
                    items.push({
                        type: "dashedword",
                        name: 'model',
                        label: "组织信息"
                    });
                    items.push({
                        type: "select",
                        disabled: "false",
                        label: $rootScope.BELONGCOMP,
                        need: true,
                        disable_search: "false",
                        dataOpt: "belongcompOpt",
                        dataSel: "belongcompSel"
                    });
                    // items.push({
                    //     type: "select",
                    //     disabled: "false",
                    //     label: $rootScope.USEDCOMP,
                    //     need: true,
                    //     disable_search: "false",
                    //     dataOpt: "compOpt",
                    //     dataSel: "compSel"
                    // });
                    items.push({
                        type: "dashed",
                        name: 'model'
                    });
                    items.push({
                        type: "select",
                        disabled: "false",
                        label: "存放区域",
                        need: false,
                        disable_search: "false",
                        dataOpt: "locOpt",
                        dataSel: "locSel"
                    });
                    items.push({
                        type: "select",
                        disabled: "false",
                        label: "仓库",
                        need: false,
                        disable_search: "false",
                        dataOpt: "warehouseOpt",
                        dataSel: "warehouseSel"
                    });
                    items.push({
                        type: "dashedword",
                        name: 'model',
                        label: "财务信息"
                    });
                    items.push({
                        type: "datetime",
                        disabled: "false",
                        label: "采购日期",
                        false: true,
                        need: true,
                        ng_model: "buytime"
                    });
                    items.push({
                        type: "input",
                        disabled: "false",
                        sub_type: "number",
                        required: false,
                        maxlength: "30",
                        placeholder: "请输入采购价格",
                        label: "采购单价",
                        need: false,
                        name: 'buy_price',
                        ng_model: "buy_price"
                    });
                    var bt = moment().subtract(1, "days");
                    var tbtime = moment();
                    if (angular.isDefined(res.data)
                        && angular.isDefined(res.data.data)
                        && angular
                            .isDefined(res.data.data.buy_timestr)) {
                        bt = moment(res.data.data.buy_timestr);
                    }
                    if (angular.isDefined(res.data)
                        && angular.isDefined(res.data.data)
                        && angular
                            .isDefined(res.data.data.wbout_datestr)) {
                        tbtime = moment(res.data.data.wbout_datestr);
                    }
                    meta = {
                        classroot: gclassroot,
                        footer_hide: false,
                        title: "资产-" + $state.router.globals.current.data.pageTitle,
                        item: {},
                        buytime: bt,
                        typeOpt: [],
                        typeSel: "",
                        belongcompOpt:[],
                        belongcompSel: "",
                        compSel: "",
                        wboutdate: tbtime,
						bfoutdate: tbtime,
                        statusOpt: [],
                        statusSel: "",
                        pinpOpt: [],
                        pinpSel: "",
                        headuserOpt: [],
                        headuserSel: "",
                        partOpt: [],
                        partSel: "",
                        classOpt: [],
                        classSel: [],
                        usedunameOpt: [],
                        usedunameSel: "",
                        warehouseOpt: [],
                        warehouseSel: "",
                        locOpt: [],
                        locSel: "",
                        wbOpt: [],
                        wbSel: "",
                        envOpt: [],
                        tbOpt: [],
                        tbSel: "",
                        envSel: "",
                        jgOpt: [],
                        jgSel: "",
                        riskOpt: [],
                        riskSel: "",
                        items: items,
                        sure: function (modalInstance, modal_meta) {
                            modal_meta.meta.item.class_id = modal_meta.meta.classSel.dict_item_id;
                            if (angular.isDefined(modal_meta.meta.typeSel.dict_item_id)) {
                                modal_meta.meta.item.type = modal_meta.meta.typeSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.partSel.partid)) {
                                modal_meta.meta.item.part_id = modal_meta.meta.partSel.partid;
                            }
                            if (angular.isDefined(modal_meta.meta.usedunameSel.user_id)) {
                                modal_meta.meta.item.used_userid = modal_meta.meta.usedunameSel.user_id;
                            }
                            if (angular.isDefined(modal_meta.meta.recycelSel.dict_item_id)) {
                                modal_meta.meta.item.recycle = modal_meta.meta.recycelSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.pinpSel.dict_item_id)) {
                                modal_meta.meta.item.brand = modal_meta.meta.pinpSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.wbSel.dict_item_id)) {
                                modal_meta.meta.item.wb = modal_meta.meta.wbSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.locSel.dict_item_id)) {
                                modal_meta.meta.item.loc = modal_meta.meta.locSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.warehouseSel.dict_item_id)) {
                                modal_meta.meta.item.warehouse = modal_meta.meta.warehouseSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.zcwbsupperSel.dict_item_id)) {
                                modal_meta.meta.item.wbsupplier = modal_meta.meta.zcwbsupperSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.zcsourceSel.dict_item_id)) {
                                modal_meta.meta.item.zcsource = modal_meta.meta.zcsourceSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.zcsupperSel.dict_item_id)) {
                                modal_meta.meta.item.supplier = modal_meta.meta.zcsupperSel.dict_item_id;
                            }
                            if (angular.isDefined(modal_meta.meta.belongcompSel.id)) {
                                modal_meta.meta.item.belong_company_id = modal_meta.meta.belongcompSel.id;
                            }
                            //使用公司
                            // if (angular.isDefined(modal_meta.meta.compSel.id)) {
                            //     modal_meta.meta.item.used_company_id = modal_meta.meta.compSel.id;
                            // }
                            modal_meta.meta.item.buy_time_f = modal_meta.meta.buytime
                                .format('YYYY-MM-DD');
                            modal_meta.meta.item.wbout_date_f = modal_meta.meta.wboutdate
                                .format('YYYY-MM-DD');
								modal_meta.meta.item.wbout_date_f = modal_meta.meta.bfoutdate
                                .format('YYYY-MM-DD');
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
                            var tt = {};
                            angular.copy(gdicts, tt)
                            loadOpt(modal_meta, tt);
                        }
                    }
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
    // flush();
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
app.register.controller('zcHcCtl', zcHcCtl);