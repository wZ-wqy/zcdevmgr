function genericzcdjCtl($translate, DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $timeout,
                        $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var setTabHeight=getDtTabHeight(500,320);
    var pbtns = $rootScope.curMemuBtns;
    var gclassroot = '3';
    var fastbtn = "<div class=\"btn-group\" role=\"group\">\n" +
        "    <button type=\"button\" class=\"btn btn-sm btn-primary dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
        "      快速处理\n" +
        "      <span class=\"caret\"></span>\n" +
        "    </button>\n" +
        "    <ul class=\"dropdown-menu\">\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('LY')\">领用</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('TK')\">退库</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('JY')\">借用</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('DB')\">调拨</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('CGJB')\">实物信息变更</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('CGCW')\">财务信息变更</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('CGWB')\">维保信息变更</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('BX')\">维修登记</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"fastProcess('BF')\">报废处理</a></li>\n" +
        "    </ul>\n" +
        "  </div>";
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
        .withOption("searching", false).withOption('scrollY', setTabHeight)
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
    if (angular.isUndefined($rootScope.zccolctl) || angular.isUndefined($rootScope.zccolctl.value)) {
        var coldata = {value: "{}"};
        $rootScope.zccolctl = coldata;
    }
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withselect', $rootScope.zccolctl.value);
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
            }, {
                id: "btn8",
                label: "",
                type: "btn",
                show: true,
                template: fastbtn
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">入库</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            {
                id: "btn5",
                label: "",
                type: "btn",
                show: false,
                priv: "detail",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
            },
            {
                id: "btn6",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            },
            {
                id: "btn7",
                label: "",
                type: "btn",
                show: false,
                priv: "exportfile",
                template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
            }],
        tools: [{
            id: "select",
            label: "存放区域",
            type: "select",
            disablesearch: true,
            show: true,
            width: "200",
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
    var dicts = "zcusefullife,zcwbcomoute,devbrand,devwb,devdc,devrecycle,zcsource,zcwbsupper,zcsupper";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "Y",
            uid: "zcdj",
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
                    // var partusers = [];
                    // angular.copy(gdicts.partusers, partusers);
                    tloc.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.meta.tools[0].dataOpt = tloc;
                    if (angular.isDefined(tloc) && tloc.length > 0) {
                        $scope.meta.tools[0].dataSel = tloc[0];
                    }
                    trecycle.unshift({
                        dict_item_id: "all",
                        name: "全部"
                    });
                    $scope.meta.tools[1].dataOpt = trecycle;
                    if (angular.isDefined(trecycle) && trecycle.length > 0) {
                        $scope.meta.tools[1].dataSel = trecycle[0];
                    }
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
    $scope.gmeta = {};
    $scope.$watch('gmeta.classSel', function (newValue, oldValue) {
        if (angular.isDefined(newValue) && angular.isDefined(newValue.dict_item_id)) {
            $http.post(
                $rootScope.project
                + "/api/cmdb/resAttrs/ext/selectAllAttrByCatId.do", {
                    catid: newValue.dict_item_id
                }).success(function (result) {
                if (result.success) {
                    $scope.gmeta.extitems = result.data;
                }
            });
        }
    });
    $scope.$watch('gmeta.partSel', function (newValue, oldValue) {
        if (angular.isDefined(newValue) && angular.isDefined(newValue.partid)) {
            if($scope.gmeta.actiontype=="insert"){
                $scope.gmeta.usedunameOpt =[];
                $scope.gmeta.usedunameSel={};
                $http.post(
                    $rootScope.project
                    + "/api/hrm/queryEmplByOrg.do", {
                        node_id: newValue.partid
                    }).success(function (result) {
                    if (result.success) {
                        $scope.gmeta.usedunameOpt = result.data;
                        $scope.gmeta.usedunameSel={}
                    }
                });
            }
        }
    });


    function openWindow(res) {
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
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "50",
            placeholder: "请输入其他编号",
            label: "其他编号",
            need: false,
            name: 'fs20',
            ng_model: "fs20"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: true,
            maxlength: "500",
            placeholder: "请输入资产名称",
            label: "资产名称",
            need: true,
            name: 'name',
            ng_model: "name"
        });

        items.push({
            type: "select",
            disabled: zcclass,
            label: "资产类别",
            need: true,
            disable_search: "false",
            dataOpt: "classOpt",
            dataSel: "classSel"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "资产状态",
            need: true,
            disable_search: "true",
            dataOpt: "recycelOpt",
            dataSel: "recycelSel"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: true,
            maxlength: "50",
            placeholder: "请输入规格型号",
            label: "规格型号",
            need: true,
            name: 'model',
            ng_model: "model"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "50",
            placeholder: "请输入序列号",
            label: "序列",
            need: false,
            name: 'sn',
            ng_model: "sn"
        });

        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "供应商",
            need: false,
            disable_search: "false",
            dataOpt: "zcsupperOpt",
            dataSel: "zcsupperSel"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "品牌",
            need: false,
            disable_search: "false",
            dataOpt: "pinpOpt",
            dataSel: "pinpSel"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "来源",
            need: false,
            disable_search: "true",
            dataOpt: "zcsourceOpt",
            dataSel: "zcsourceSel"
        });

        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "使用年限",
            need: false,
            disable_search: "false",
            dataOpt: "uselifeOpt",
            dataSel: "uselifeSel"
        });
        items.push({
            type: "datetime",
            disabled: zcinputflag,
            label: "生产日期",
            need: false,
            ng_model: "productiontime"
        });
        items.push({
            type: "datetime",
            disabled: zcinputflag,
            label: "采购日期",
            need: false,
            ng_model: "buytime"
        });
        // items.push({
        //     type: "input",
        //     disabled: zccnt,
        //     sub_type: "number",
        //     required: false,
        //     maxlength: "50",
        //     placeholder: "",
        //     label: "数量",
        //     need: false,
        //     name: 'zc_cnt',
        //     ng_model: "zc_cnt"
        // });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "50",
            placeholder: "请输入计量单位",
            label: "计量单位",
            need: false,
            name: 'unit',
            ng_model: "unit"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "200",
            placeholder: "请输入配置描述",
            label: "配置描述",
            need: false,
            name: 'confdesc',
            ng_model: "confdesc"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "50",
            placeholder: "请输入批次号",
            label: "批次号",
            need: false,
            name: 'batch',
            ng_model: "batch"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "200",
            placeholder: "请输入备注",
            label: "备注",
            need: false,
            name: 'mark',
            ng_model: "mark"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
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
            disabled: zcinputflag,
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
            disabled: zcinputflag,
            label: $rootScope.BELONGCOMP,
            need: true,
            disable_search: "false",
            dataOpt: "belongcompOpt",
            dataSel: "belongcompSel"
        });

        items.push({
            type: "select",
            disabled: zcinputflag,
            label: $rootScope.USEDPART,
            need: false,
            disable_search: "false",
            dataOpt: "partOpt",
            dataSel: "partSel"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: $rootScope.USEDUSER,
            need: false,
            disable_search: "false",
            dataOpt: "usedunameOpt",
            dataSel: "usedunameSel"
        });
		items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "200",
            placeholder: "请输入",
            label: "是否涉密",
            need: false,
            name: 'sfsm',
            ng_model: "sfsm"
        });
		
		
        items.push({
            type: "dashedword",
            name: 'model',
            label: "区域位置"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "存放区域",
            need: false,
            disable_search: "true",
            dataOpt: "locOpt",
            dataSel: "locSel"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "50",
            placeholder: "请输入详细位置",
            label: "位置",
            need: false,
            name: 'locdtl',
            ng_model: "locdtl"
        });
        items.push({
            type: "dashedword",
            name: 'model',
            label: "财务信息"
        });

        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "number",
            required: false,
            maxlength: "30",
            placeholder: "请输入采购价格",
            label: "采购单价",
            need: false,
            name: 'buy_price',
            ng_model: "buy_price"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "number",
            required: false,
            maxlength: "30",
            placeholder: "请输入资产净值",
            label: "资产净值",
            need: false,
            name: 'net_worth',
            ng_model: "net_worth"
        });
        items.push({
            type: "dashedword",
            name: 'model',
            label: "维保信息"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "维保商",
            need: false,
            disable_search: "false",
            dataOpt: "zcwbsupperOpt",
            dataSel: "zcwbsupperSel"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "脱保计算",
            need: false,
            disable_search: "true",
            dataOpt: "tbOpt",
            dataSel: "tbSel"
        });
        items.push({
            type: "datetime",
            disabled: zcinputflag,
            label: "脱保日期",
            need: false,
            ng_model: "wboutdate"
        });
		items.push({
            type: "datetime",
            disabled: zcinputflag,
            label: "报废日期",
            need: false,
            ng_model: "bfoutdate"
        });
        items.push({
            type: "select",
            disabled: zcinputflag,
            label: "维保状态",
            false: true,
            disable_search: "true",
            dataOpt: "wbOpt",
            dataSel: "wbSel"
        });
        items.push({
            type: "input",
            disabled: zcinputflag,
            sub_type: "text",
            required: false,
            maxlength: "100",
            placeholder: "请输入维保说明",
            label: "维保说明",
            need: false,
            name: 'wbct',
            ng_model: "wbct"
        });
        items.push({
            type: "picupload",
            disabled: "false",
            required: false,
            label: "图片",
            need: false,
            conf: "picconfig"
        });
        items.push({
            type: "fileupload",
            disabled: "false",
            required: false,
            label: "附件",
            need: false,
            conf: "attachconfig"
        });
        var bt = moment().subtract(1, "days");
        var tbtime = moment();
        var pt = moment().subtract(1, "days");
        if (angular.isDefined(res)
            && angular.isDefined(res.data)
            && angular
                .isDefined(res.data.buy_timestr)) {
            bt = moment(res.data.buy_timestr);
        }
        if (angular.isDefined(res)
            && angular.isDefined(res.data)
            && angular
                .isDefined(res.data.wbout_datestr)) {
            tbtime = moment(res.data.wbout_datestr);
        }
        if (angular.isDefined(res.data)
            && angular
                .isDefined(res.data.fd1str)) {
            pt = moment(res.data.fd1str);
        }
        $scope.gmeta = {
            classroot: gclassroot,
            footer_hide: false,
            title: "资产-" + $state.router.globals.current.data.pageTitle,
            item: {zc_cnt: 1},
            buytime: bt,
            productiontime: pt,
            typeOpt: [],
            typeSel: "",
            belongcompSel: "",
            compSel: "",
            wboutdate: tbtime,
			bfoutdate: tbtime,
            statusOpt: [],
            statusSel: "",
            pinpOpt: [],
            pinpSel: "",
			sfsmOpt: [],
            sfsmSel: "",
            headuserOpt: [],
            headuserSel: "",
            partOpt: [],
            partSel: "",
            classOpt: [],
            classSel: [],
            usedunameOpt: [],
            usedunameSel: "",
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
            picconfig: {
                url: 'fileupload.do',
                maxFilesize: 10000,
                paramName: "file",
                maxThumbnailFilesize: 6,
                // 一个请求上传多个文件
                uploadMultiple: true,
                // 当多文件上传,需要设置parallelUploads>=maxFiles
                parallelUploads: 6,
                maxFiles: 6,
                dictDefaultMessage: "点击上传图片",
                acceptedFiles: "image/jpeg,image/png,image/gif,*.jpg",
                // 添加上传取消和删除预览图片的链接，默认不添加
                addRemoveLinks: true,
                // 关闭自动上传功能，默认会true会自动上传
                // 也就是添加一张图片向服务器发送一次请求
                autoProcessQueue: false,
                init: function () {
                    Dropzone.autoDiscover = false;
                    $scope.myDropzonepic = this;
                }
            },
            attachconfig: {
                url: 'fileupload.do',
                maxFilesize: 10000,
                paramName: "file",
                maxThumbnailFilesize: 1,
                // 一个请求上传多个文件
                uploadMultiple: true,
                // 当多文件上传,需要设置parallelUploads>=maxFiles
                parallelUploads: 1,
                maxFiles: 1,
                dictDefaultMessage: "点击上传附件",
                acceptedFiles: ".xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
                // 添加上传取消和删除预览图片的链接，默认不添加
                addRemoveLinks: true,
                // 关闭自动上传功能，默认会true会自动上传
                // 也就是添加一张图片向服务器发送一次请求
                autoProcessQueue: false,
                init: function () {
                    Dropzone.autoDiscover = false;
                    $scope.myDropzonefile = this;
                }
            },
            items: items,
            sure: function (modalInstance, modal_meta) {
                //处理图片
                // 只允许传一张图片
                modal_meta.meta.item.img = "";
                if ($scope.myDropzonepic.files.length > 0 && $scope.myDropzonepic.files.length == 1) {
                    var id = getUuid();
                    if (typeof ($scope.myDropzonepic.files[0].uuid) == "undefined") {
                        // 需要上传
                        $scope.myDropzonepic.options.url = $rootScope.project
                            + '/api/file/fileupload.do?uuid=' + id
                            + '&bus=file&interval=10000&bus=file';
                        $scope.myDropzonepic.uploadFile($scope.myDropzonepic.files[0])
                    } else {
                        id = $scope.myDropzonepic.files[0].uuid;
                    }
                    modal_meta.meta.item.img = id;
                }
                // 只允许传一张附件
                modal_meta.meta.item.attach = "";
                if ($scope.myDropzonefile.files.length > 0 && $scope.myDropzonefile.files.length == 1) {
                    var id = getUuid();
                    if (typeof ($scope.myDropzonefile.files[0].uuid) == "undefined") {
                        // 需要上传
                        $scope.myDropzonefile.options.url = $rootScope.project
                            + '/api/file/fileupload.do?uuid=' + id
                            + '&bus=file&interval=10000&bus=file';
                        $scope.myDropzonefile.uploadFile($scope.myDropzonefile.files[0])
                    } else {
                        id = $scope.myDropzonefile.files[0].uuid;
                    }
                    modal_meta.meta.item.attach = id;
                }
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
                if (angular.isDefined(modal_meta.meta.uselifeSel.dict_item_id)) {
                    modal_meta.meta.item.usefullife = modal_meta.meta.uselifeSel.dict_item_id;
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
                modal_meta.meta.item.fd1str = modal_meta.meta.productiontime
                    .format('YYYY-MM-DD');
				modal_meta.meta.item.wbout_date_f = modal_meta.meta.bfoutdate
                    .format('YYYY-MM-DD');
                // 动态参数
                if (angular.isDefined(modal_meta.meta.extitems)
                    && modal_meta.meta.extitems.length > 0) {
                    for (var j = 0; j < modal_meta.meta.extitems.length; j++) {
                        var code = modal_meta.meta.extitems[j].attrcode;
                        modal_meta.meta.extitems[j].attrvalue = modal_meta.meta.item[code];
                    }
                }
                modal_meta.meta.item.attrvals = angular
                    .toJson(modal_meta.meta.extitems);
                if (angular.isDefined(modal_meta.meta.item.id)) {
                    $confirm({
                        title: "资产修改确认",
                        text: '修改功能不保存变更记录是否确认使用修改功能?'
                    }).then(
                        function () {
                            $http
                                .post(
                                    $rootScope.project
                                    + "/api/base/res/addResCustom.do",
                                    modal_meta.meta.item)
                                .success(function (result) {
                                    if (result.success) {
                                        modalInstance.close("OK");
                                    } else {
                                        notify({
                                            message: result.message
                                        });
                                    }
                                });
                        });
                } else {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/base/res/addResCustom.do",
                            modal_meta.meta.item)
                        .success(function (result) {
                            if (result.success) {
                                modalInstance.close("OK");
                            } else {
                                notify({
                                    message: result.message
                                });
                            }
                        });
                }
            },
            init: function (modal_meta) {
                var tt = {};
                angular.copy(gdicts, tt)
                loadOpt(modal_meta, tt);
                var iid = modal_meta.meta.item.img;
                if (angular.isDefined(iid) && iid.length > 0) {
                    $timeout(function () {
                        var mockFile = {
                            name: "主图",
                            uuid: iid,
                            href: $rootScope.project
                                + "/api/file/imagedown.do?id="
                                + iid,
                            url: $rootScope.project
                                + "/api/file/imagedown.do?id="
                                + iid,
                            status: "success",
                            accepted: true,
                            type: 'image/png'
                        };
                        $scope.myDropzonepic.emit("addedfile", mockFile);
                        $scope.myDropzonepic.files.push(mockFile);
                        // manually
                        $scope.myDropzonepic.createThumbnailFromUrl(
                            mockFile, $rootScope.project
                            + "/api/file/imagedown.do?id="
                            + iid);
                        $scope.myDropzonepic.emit("complete", mockFile);
                    }, 300);
                }
                var iidf = modal_meta.meta.item.attach;
                if (angular.isDefined(iidf) && iidf.length > 0) {
                    $timeout(function () {
                        var mockFile = {
                            name: "主图",
                            uuid: iidf,
                            href: $rootScope.project
                                + "/api/file/imagedown.do?id="
                                + iidf,
                            url: $rootScope.project
                                + "/api/file/imagedown.do?id="
                                + iidf,
                            status: "success",
                            accepted: true,
                            type: 'image/png'
                        };
                        $scope.myDropzonefile.emit("addedfile", mockFile);
                        $scope.myDropzonefile.files.push(mockFile);
                        // manually
                        $scope.myDropzonefile.createThumbnailFromUrl(
                            mockFile, $rootScope.project
                            + "/api/file/imagedown.do?id="
                            + iidf);
                        $scope.myDropzonefile.emit("complete", mockFile);
                    }, 300);
                }
            }
        }
        if (angular.isDefined(res.data)
            && angular.isDefined(res.data.id)) {
            $scope.gmeta.item = res.data;
            $scope.gmeta.actiontype="update";
        }else{
            $scope.gmeta.actiontype="insert";
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
                        return $scope.gmeta;
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

    var zcclass = "false";
    var zcinputflag="false";
    $scope.save = function (type) {
        var id;
        zcinputflag="false";
        zcclass = "false";
        if (type == 1) {
            var selrow = getSelectRow();
            if (angular.isDefined(selrow)) {
                id = selrow.id;
                zcinputflag="true"
                zcclass = "true";
            } else {
                return;
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
                        openWindow(res.data);
                    })
        } else {
            zcclass = "false";
            zcinputflag="false"
            openWindow({});
        }
    }
    //####################################快速处理##########################################################
    var gfastdict = {};
    var dicts = "zcwbcomoute,devwb,zcwbsupper,zcusefullife,devbrand,devdc,zcsource,zcsupper";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "Y",
            zccatused: "Y",
            classroot: "3",
            uid: "fastprocessid"
        })
        .success(
            function (res) {
                if (res.success) {
                    gfastdict = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    $scope.fastProcess = function (type) {
        var selrows = getSelectRows();
        if (angular.isDefined(selrows)) {
            if (angular.fromJson(selrows).length > 50) {
                notify({
                    message: "选择项超过50个,请重新进行筛选！"
                });
                return;
            }
            $http
                .post($rootScope.project + "/api/zc/fastProcessItemCheck.do", {
                    type: type,
                    items: selrows
                })
                .success(
                    function (res) {
                        if (res.success) {
                            //检查
                            //快速处理按钮
                            var meta = {};
                            meta.ids = selrows;
                            if (type == "BX") {
                                meta.actiontype = "add";
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_zcfault.html',
                                    controller: modaldevfaultCtl,
                                    size: 'lg',
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
                            } else if (type == "LY") {
                                meta.dict = gfastdict;
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_collectionSave.html',
                                    controller: collectionSaveCtl,
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
                            } else if (type == "TK") {
                                meta.dict = gfastdict;
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_tkSave.html',
                                    controller: tkSaveCtl,
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
                            } else if (type == "JY") {
                                meta.dict = gfastdict;
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_loanreturnSave.html',
                                    controller: loanSaveCtl,
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
                            } else if (type == "CGJB") {
                                meta.dict = gfastdict;
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_zccgjbSave.html',
                                    controller: zccgjbSaveCtl,
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
                            } else if (type == "CGCW") {
                                meta.dict = gfastdict;
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_zccgcwSave.html',
                                    controller: zccgcwSaveCtl,
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
                            } else if (type == "CGWB") {
                                meta.dict = gfastdict;
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
                                });
                            } else if (type == "BF") {
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_zcbf.html',
                                    controller: assetsbfCtl,
                                    size: 'blg',
                                    resolve: {
                                        meta: function () {
                                            return meta
                                        }
                                    }
                                });
                                modalInstance.result.then(function (result) {
                                    if (result == "OK") {
                                        flush();
                                    }
                                }, function (reason) {
                                });
                            } else if (type == "DB") {
                                meta.actiontype = "add";
                                meta.gdict = gfastdict;
                                var modalInstance = $uibModal.open({
                                    backdrop: true,
                                    templateUrl: 'views/cmdb/modal_zcallocation.html',
                                    controller: assetsallocationCtl,
                                    size: 'lg',
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
                            } else {
                                alert("该功能暂未实现!");
                            }
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    })
        }
    }
    //####################################快速结束##########################################################
};
app.register.controller('flowapprovalCommonCtl', flowapprovalCommonCtl);
app.register.controller('flowsuggestCommonCtl', flowsuggestCommonCtl);
app.register.controller('genericzcdjCtl', genericzcdjCtl);
