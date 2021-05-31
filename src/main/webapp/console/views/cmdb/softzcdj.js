function softzcdjCtl($translate, DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $timeout,
                     $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    //软件发行方式
    //opensource  开源
    //business    商业
    //selfresearch  自研
    var softdistributionOpt = [
        {id: "business", name: "商业"},
        {id: "opensource", name: "开源"},
        {id: "selfresearch", name: "自研"}];
    var pbtns = $rootScope.curMemuBtns;
    var gclassroot = '12';
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

    function renderAttach(data, type, full) {
        if (angular.isDefined(data) && data.length > 0) {
            var html = " <span><a href=\"../api/file/filedown.do?id=" + data + "\">下载</a></span> ";
            return html;
        } else {
            return "";
        }
    }


    function renderFxfs(data, type, full) {
        if (angular.isDefined(data)) {
            if (data == "opensource") {
                return "开源";
            } else if (data == "business") {
                return "商业";
            } else if (data == "selfresearch") {
                return "自研";
            } else {
                return data;
            }
        }
        return data;
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
        'select-checkbox checkbox_center').renderWith(function () {
        return ""
    }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('classfullname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('授权数量')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('unit').withTitle('计量单位').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('fs18').withTitle('发行方式').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderFxfs));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('buy_timestr').withTitle('采购日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购价格')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('zcsourcestr').withTitle('来源').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('fs19').withTitle('来源详情').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('fs1').withTitle('标签1').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('fs2').withTitle('标签2').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('attach').withTitle('附件').withOption(
        'sDefaultContent', '').renderWith(renderAttach));
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
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">登记</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            {
                id: "btn6",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            }
        ],
        tools: [{
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入搜索内容",
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
        ps.search = $scope.meta.tools[0].ct;
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
        ps.search = $scope.meta.tools[0].ct;
        $window.open($rootScope.project
            + "/api/base/res/exportServerData.do?classroot=" + ps.classroot + "&loc="
            + ps.loc + "&recycle="
            + ps.recycle + "&search=" + ps.search);
    }
    var gdicts = {};
    var dicts = "zcsource";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "N",
            partusers: "N",
            comp: "N",
            belongcomp: "N",
            uid: "softzcdj",
            classroot: gclassroot
        })
        .success(
            function (res) {
                if (res.success) {
                    gdicts = res.data;
                    var btype = [];
                    angular.copy(gdicts.btype, btype);
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
            type: "select",
            disabled: zcclass,
            label: "资产类别",
            need: true,
            disable_search: "false",
            dataOpt: "classOpt",
            dataSel: "classSel"
        });

        items.push({
            type: "input",
            disabled: zcmodel,
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
            type: "input",
            disabled: zcsn,
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
            type: "input",
            disabled: zcunit,
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
            disabled: "false",
            sub_type: "number",
            required: true,
            maxlength: "50",
            placeholder: "",
            label: "授权数量",
            need: true,
            name: 'zc_cnt',
            ng_model: "zc_cnt"
        });

        items.push({
            type: "select",
            disabled: "false",
            label: "发行方式",
            need: true,
            disable_search: "false",
            dataOpt: "softdistributionOpt",
            dataSel: "softdistributionSel"
        });

        items.push({
            type: "select",
            disabled: zcsoure,
            label: "来源",
            need: false,
            disable_search: "true",
            dataOpt: "zcsourceOpt",
            dataSel: "zcsourceSel"
        });


        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入来源详情",
            label: "来源详情",
            need: false,
            name: 'fs19',
            ng_model: "fs19"
        });

        items.push({
            type: "input",
            disabled: zcbuyprice,
            sub_type: "number",
            required: false,
            maxlength: "30",
            placeholder: "请输入采购价格",
            label: "采购价格",
            need: false,
            name: 'buy_price',
            ng_model: "buy_price"
        });

        items.push({
            type: "datetime",
            disabled: zcbuytime,
            label: "采购日期",
            need: false,
            ng_model: "buytime"
        });

        items.push({
            type: "input",
            disabled: zcconfdesc,
            sub_type: "text",
            required: false,
            maxlength: "200",
            placeholder: "请输入配置描述123",
            label: "配置描述",
            need: false,
            name: 'confdesc',
            ng_model: "confdesc"
        });
        items.push({
            type: "input",
            disabled: zcmark,
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入备注",
            label: "备注",
            need: false,
            name: 'mark',
            ng_model: "mark"
        });
        items.push({
            type: "input",
            disabled: zclabel1,
            sub_type: "text",
            required: false,
            maxlength: "100",
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
            maxlength: "100",
            placeholder: "请输入标签",
            label: "标签2",
            need: false,
            name: 'fs2',
            ng_model: "fs2"
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
            headuserOpt: [],
            softdistributionOpt: softdistributionOpt,
            softdistributionSel: softdistributionOpt[0],
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
                acceptedFiles: "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
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

                //发行方式
                modal_meta.meta.item.fs18 = modal_meta.meta.softdistributionSel.id;

                if (angular.isDefined(modal_meta.meta.zcsourceSel.dict_item_id)) {
                    modal_meta.meta.item.zcsource = modal_meta.meta.zcsourceSel.dict_item_id;
                }

                modal_meta.meta.item.buy_time_f = modal_meta.meta.buytime
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
            for (var i = 0; i < $scope.gmeta.softdistributionOpt.length; i++) {
                if (res.data.fs18 == $scope.gmeta.softdistributionOpt[i].id) {
                    $scope.gmeta.softdistributionSel = $scope.gmeta.softdistributionOpt[i];
                    break;
                }
            }
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

    var zcrecycle = "false";
    var zcclass = "false";
    var zcbuyprice = "false";
    var zcnetworth = "false";
    var zcmodel = "false";
    var zcsn = "false";
    var zcsupper = "false";
    var zcpinp = "false";
    var zcsoure = "false";
    var zcuselife = "false";
    var zccnt = "false";
    var zcbelongcomp = "false";
    var zccomp = "false";
    var zcpart = "false";
    var zcuseduser = "false";
    var zcloc = "false";
    var zcbuytime = "false";
    var zcproductiontime = "false";
    var zcwbsupper = "false";
    var zcwboutdate = "false";
    var zcwb = "false";
    var zcunit = "false";
    var zclabel1 = "false";
    var zcconfdesc = "false";
    var zclocdtl = "false";
    var zcwbct = "false";
    var zcfs20 = "false"
    var zcmark = "false"


    $scope.save = function (type) {
        var id;
        zcrecycle = "false";
        zcclass = "false";
        zcbuyprice = "false";
        zcnetworth = "false";
        if (type == 1) {
            var selrow = getSelectRow();
            if (angular.isDefined(selrow)) {
                id = selrow.id;
                zcclass = "true";
                zcrecycle = "false";
                zcbuyprice = "false";
                zcnetworth = "false";
                zcmodel = "false";
                zcsn = "false";
                zcsupper = "false";
                zcpinp = "false";
                zcsoure = "false";
                zcuselife = "false";
                zccnt = "false";
                zcbelongcomp = "false";
                zccomp = "false";
                zcpart = "false";
                zcuseduser = "false";
                zcloc = "false";
                zcbuytime = "false";
                zcproductiontime = "false";
                zcwbsupper = "false";
                zcwboutdate = "false";
                zcwb = "false";
                zcunit = "false";
                zclabel1 = "false";
                zcconfdesc = "false";
                zclocdtl = "false";
                zcwbct = "false";
                zcfs20 = "false"
                zcmark = "false"
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
            zcrecycle = "false";
            zcbuyprice = "false";
            zcnetworth = "false";
            zcmodel = "false";
            zcsn = "false";
            zcsupper = "false";
            zcpinp = "false";
            zcsoure = "false";
            zcuselife = "false";
            zccnt = "false";
            zcbelongcomp = "false";
            zccomp = "false";
            zcpart = "false";
            zcuseduser = "false";
            zcloc = "false";
            zcbuytime = "false";
            zcproductiontime = "false";
            zcwbsupper = "false";
            zcwboutdate = "false";
            zcwb = "false";
            zcunit = "false";
            zclabel1 = "false";
            zcconfdesc = "false";
            zclocdtl = "false";
            zcwbct = "false";
            zcfs20 = "false"
            zcmark = "false"
            openWindow({});
        }
    }
};
app.register.controller('softzcdjCtl', softzcdjCtl);