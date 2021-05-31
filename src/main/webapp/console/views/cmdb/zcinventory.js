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

function modalimportdataFailCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                                $confirm, $log, notify, $scope, meta, $http, $rootScope, $uibModal,
                                $uibModalInstance) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'bAutoWidth', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtColumns = [DTColumnBuilder.newColumn('ct').withTitle('失败列表')
        .withOption('sDefaultContent', '')]
    if (angular.isDefined(meta.failed_data)) {
        $scope.dtOptions.aaData = meta.failed_data;
    }
}

function zcinventoryPdCtl($timeout, $localStorage, notify, $log, $uibModal,
                          $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                          DTColumnBuilder, $compile) {
    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 2,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 1,
        maxFiles: 1,
        dictDefaultMessage: "点击上传需要上传的文件",
        acceptedFiles: ".xlsx,.xls",
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
    $scope.sure = function () {
        $scope.okbtnstatus = true;
        var id = getUuid();
        if ($scope.myDropzone.files.length > 0) {
            $scope.myDropzone.options.url = $rootScope.project
                + '/api/file/fileupload.do?uuid=' + id
                + '&bus=file&interval=10000&bus=file';
            $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
        } else {
            notify({
                message: "请选择文件"
            });
            $scope.okbtnstatus = false;
            return;
        }
        $timeout(function () {
            $http.post($rootScope.project + "/api/zc/resInventory/ext/manualInventoryRes.do", {
                fileid: id,
                id: meta.id
            }).success(function (res) {
                $scope.okbtnstatus = false;
                $scope.myDropzone.removeAllFiles(true);
                if (res.success) {
                    notify({
                        message: "操作成功！"
                    });
                    $uibModalInstance.close('OK');
                } else {
                    if (angular.isDefined(res.data)) {
                        var modalInstance = $uibModal.open({
                            backdrop: true,
                            templateUrl: 'views/cmdb/modal_importFail.html',
                            controller: modalimportdataFailCtl,
                            size: 'blg',
                            resolve: {
                                meta: function () {
                                    return res.data;
                                }
                            }
                        });
                        $scope.myDropzone.removeAllFiles(true);
                        modalInstance.result.then(function (result) {
                        }, function (reason) {

                        });
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                }
            })
        }, 3000);
    };
}

function zcinventoryResCtl($timeout, $localStorage, notify, $log, $uibModal,
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
        });
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');
    item.category = 3;
    function flush() {
        $http.post($rootScope.project + "/api/zc/resInventory/ext/queryInventoryRes.do",
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

    function renderPdStatus(data, type, full) {
        if (data == "wait") {
            return "待盘点"
        } else if (data == "finish") {
            return "已盘点"
        } else {
            return data;
        }
    }

    function renderPdSync(data, type, full) {
        if (data == "1") {
            return "更新"
        } else if (data == "0") {
            return "不更新"
        } else {
            return data;
        }
    }

    if (angular.isDefined(meta.id)) {
        //获取数据
        $scope.dtColumns.push(DTColumnBuilder.newColumn('pdstatus').withTitle('盘点状态').withOption(
            'sDefaultContent', '').withOption("width", '30').renderWith(renderPdStatus));
        $scope.dtColumns.push(DTColumnBuilder.newColumn('pdsyncneed').withTitle('数据更新').withOption(
            'sDefaultContent', '').withOption("width", '30').renderWith(renderPdSync));
        $http.post($rootScope.project + "/api/zc/resInventory/ext/selectById.do",
            item).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data.items;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    } else {
        flush();
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function zcinventoryUserSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                                $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                                DTColumnBuilder, $compile) {
    $scope.pduserOpt = meta.dict.partusers;
    $scope.pduserSel = [];
    var tmparr = []
    var curusers = angular.fromJson(decodeURIComponent(meta.pduserdata));
    if (meta.pduserdata.length > 0) {
        for (var i = 0; i < $scope.pduserOpt.length; i++) {
            for (var j = 0; j < curusers.length; j++) {
                if ($scope.pduserOpt[i].user_id == curusers[j].user_id) {
                    tmparr.push($scope.pduserOpt[i]);
                }
            }
        }
    }
    $scope.pduserSel = tmparr;
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        //分配盘点人员
        $scope.item = {};
        $scope.item.id = meta.id;
        if (angular.isDefined($scope.pduserSel) && $scope.pduserSel.length > 0) {
            var tstr = "";
            for (var i = 0; i < $scope.pduserSel.length; i++) {
                tstr = tstr + $scope.pduserSel[i].name + " ";
            }
            $scope.item.pduserdata = angular.toJson($scope.pduserSel);
            $scope.item.pduserlist = tstr;
        } else {
            notify({
                message: "请选择分配盘点人员"
            });
            return;
        }
        $http.post($rootScope.project + "/api/zc/resInventory/ext/assignusers.do",
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

function zcinventorySaveCtl($timeout, $localStorage, notify, $log, $uibModal,
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
    if (meta.type == "detail") {
        $scope.ctl.name = true;
        $scope.ctl.adminuserSel = true;
        $scope.ctl.pduserSel = true;
        $scope.ctl.pdSel = true;
        $scope.ctl.mark = true;
        $scope.ctl.belongcompSel = true;
        $scope.ctl.compSel = true;
        $scope.ctl.comppartSel = true;
        $scope.ctl.zcCatSel = true;
        $scope.ctl.zcAreaSel = true;
    }
    $scope.item = {};
    $scope.adminuserOpt = meta.dict.partusers;
    $scope.adminuserSel = "";
    if ($scope.adminuserOpt.length > 0) {
        $scope.adminuserSel = $scope.adminuserOpt[0];
    }
    $scope.pdOpt = [{id: "1", name: "支持"}, {id: "0", name: "不支持"}]
    $scope.pdSel = $scope.pdOpt[0];
    $scope.pduserOpt = meta.dict.partusers;
    $scope.pduserSel = [];
    $scope.belongcompOpt = meta.dict.belongcomp;
    $scope.belongcompSel = [];
    $scope.compOpt = meta.dict.comp;
    $scope.compSel = []
    $scope.comppartOpt = meta.dict.parts;
    $scope.comppartSel = [];
    $scope.zcCatOpt = meta.dict.zccatused;
    $scope.zcCatSel = [];
    $scope.zcAreaOpt = meta.dict.devdc
    $scope.zcAreaSel = [];
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        if (angular.isDefined($scope.adminuserSel) && angular.isDefined($scope.adminuserSel.user_id)) {
            $scope.item.adminuserid = $scope.adminuserSel.user_id;
            $scope.item.adminusername = $scope.adminuserSel.name;
        } else {
            notify({
                message: "请选择盘点负责人"
            });
            return;
        }
        //分配盘点人员
        if (angular.isDefined($scope.pduserSel) && $scope.pduserSel.length > 0) {
            var tstr = "";
            for (var i = 0; i < $scope.pduserSel.length; i++) {
                tstr = tstr + $scope.pduserSel[i].name + " ";
            }
            $scope.item.pduserdata = angular.toJson($scope.pduserSel);
            $scope.item.pduserlist = tstr;
        } else {
            notify({
                message: "请选择分配盘点人员"
            });
            return;
        }
        if (angular.isUndefined($scope.item.status)) {
            $scope.item.status = "start";
        }
        if (angular.isUndefined($scope.item.syncstatus)) {
            $scope.item.syncstatus = "0";
        }
        if (angular.isUndefined($scope.item.batchid)) {
            $scope.item.batchid = GetDateNowId();
        }
        if (angular.isDefined($scope.zcAreaSel) && $scope.zcAreaSel.length > 0) {
            var tstr1 = "";
            var tstr2 = "";
            for (var i = 0; i < $scope.zcAreaSel.length; i++) {
                tstr1 = tstr1 + $scope.zcAreaSel[i].dict_item_id + " ";
                tstr2 = tstr2 + $scope.zcAreaSel[i].name + " ";
            }
            $scope.item.areadata = angular.toJson($scope.zcAreaSel);
            $scope.item.area = tstr1;
            $scope.item.areaname = tstr2;
        }
        if (angular.isDefined($scope.belongcompSel) && angular.isDefined($scope.belongcompSel.id)) {
            $scope.item.belongcomp = $scope.belongcompSel.id;
            $scope.item.belongcompname = $scope.belongcompSel.name;
        }
        if (angular.isDefined($scope.compSel) && angular.isDefined($scope.compSel.id)) {
            $scope.item.usedcomp = $scope.compSel.id;
            $scope.item.usedcompname = $scope.compSel.name;
        }
        if (angular.isDefined($scope.comppartSel) && $scope.comppartSel.length > 0) {
            var tstr1 = "";
            var tstr2 = "";
            for (var i = 0; i < $scope.comppartSel.length; i++) {
                tstr1 = tstr1 + $scope.comppartSel[i].partid + " ";
                tstr2 = tstr2 + $scope.comppartSel[i].name + " ";
            }
            $scope.item.usedpartdata = angular.toJson($scope.comppartSel);
            $scope.item.usedpart = tstr1;
            $scope.item.usedpartname = tstr2;
        }
        if (angular.isDefined($scope.zcCatSel) && $scope.zcCatSel.length > 0) {
            var tstr1 = "";
            var tstr2 = "";
            for (var i = 0; i < $scope.zcCatSel.length; i++) {
                tstr1 = tstr1 + $scope.zcCatSel[i].id + " ";
                tstr2 = tstr2 + $scope.zcCatSel[i].name + " ";
            }
            $scope.item.rescatdata = angular.toJson($scope.zcCatSel);
            $scope.item.rescat = tstr1;
            $scope.item.rescatname = tstr2;
        }
        $scope.item.manualinventory = $scope.pdSel.id;
        $scope.item.allusersinventory = 0;
        $scope.item.category = 3;
        $http.post($rootScope.project + "/api/zc/resInventory/ext/insertOrUpdate.do",
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
        var item = {};
        if (angular.isDefined($scope.zcAreaSel) && $scope.zcAreaSel.length > 0) {
            var tstr1 = "";
            var tstr2 = "";
            for (var i = 0; i < $scope.zcAreaSel.length; i++) {
                tstr1 = tstr1 + $scope.zcAreaSel[i].dict_item_id + " ";
                tstr2 = tstr2 + $scope.zcAreaSel[i].name + " ";
            }
            item.areadata = angular.toJson($scope.zcAreaSel);
            item.area = tstr1;
            item.areaname = tstr2;
        }
        if (angular.isDefined($scope.belongcompSel) && angular.isDefined($scope.belongcompSel.id)) {
            item.belongcomp = $scope.belongcompSel.id;
            item.belongcompname = $scope.belongcompSel.name;
        }
        if (angular.isDefined($scope.compSel) && angular.isDefined($scope.compSel.id)) {
            // item.usedcomp = $scope.compSel.id;
            // item.usedcompname = $scope.compSel.name;
        }
        if (angular.isDefined($scope.comppartSel) && $scope.comppartSel.length > 0) {
            var tstr1 = "";
            var tstr2 = "";
            for (var i = 0; i < $scope.comppartSel.length; i++) {
                tstr1 = tstr1 + $scope.comppartSel[i].partid + " ";
                tstr2 = tstr2 + $scope.comppartSel[i].name + " ";
            }
            item.usedpartdata = angular.toJson($scope.comppartSel);
            item.usedpart = tstr1;
            item.usedpartname = tstr2;
        }
        if (angular.isDefined($scope.zcCatSel) && $scope.zcCatSel.length > 0) {
            var tstr1 = "";
            var tstr2 = "";
            for (var i = 0; i < $scope.zcCatSel.length; i++) {
                tstr1 = tstr1 + $scope.zcCatSel[i].id + " ";
                tstr2 = tstr2 + $scope.zcCatSel[i].name + " ";
            }
            item.rescatdata = angular.toJson($scope.zcCatSel);
            item.rescat = tstr1;
            item.rescatname = tstr2;
        }
        item.type = meta.type;
        item.id = meta.id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zcinventory_item.html',
            controller: zcinventoryResCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return item;
                }
            }
        });
        modalInstance.result.then(function (result) {
        }, function (reason) {

        });
    }
}

function zcPdCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $window,
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
            uid: "zcinventory"
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

    // function renderAction(data, type, full) {
    //     //分配，删除，详情
    //     if (data == "wait") {
    //         return "维修中"
    //     } else if (data == "finish") {
    //         return "已完成"
    //     } else {
    //         return data;
    //     }
    // }

    function renderStatus(data, type, full) {
        if (data == "wait") {
            return "等待开始"
        } else if (data == "doing") {
            return "进行中"
        } else if (data == "cancel") {
            return "取消"
        } else if (data == "finish") {
            return "完成"
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
        var tstr = "";
        if (full.status == 'start' || full.status == 'wait') {
        } else {
            tstr = " disabled=\"disabled\"";
        }
        acthtml = acthtml + " <button  " + tstr + " ng-click=\"assignusers('"
            + full.id
            + "','" + encodeURIComponent(full.pduserdata) + "')\" class=\"btn-white btn btn-xs\">分配用户</button>   ";
        var tstr2 = "";
        if (full.status == 'finish' && full.syncstatus != '1') {
        } else {
            tstr2 = " disabled=\"disabled\"";
        }
        // acthtml = acthtml + " <button " + tstr2 + "ng-click=\"syncdata('"
        //     + full.id
        //     + "')\" class=\"btn-white btn btn-xs\">同步数据</button>   ";

        var tstr3 = "";
        if (full.status == 'start' || full.status == 'wait') {
        } else {
            tstr3 = " disabled=\"disabled\"";
        }
        var tstr4 = "";
        if (full.manualinventory == '1') {
        } else {
            tstr4 = " disabled=\"disabled\"";
        }
        // acthtml = acthtml + " <button " + tstr4 + " ng-click=\"inventory('"
        //     + full.id
        //     + "')\" class=\"btn-white btn btn-xs\">手工盘点</button>   ";
        // acthtml = acthtml + " <button ng-click=\"detail('"
        //     + full.id
        //     + "')\" class=\"btn-white btn btn-xs\">详情</button>   ";
        acthtml = acthtml + " <button " + tstr3 + "ng-click=\"cancel('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">取消</button>   ";

        acthtml = acthtml + " <button ng-click=\"finish('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">完成</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderDataStatus(data, type, full) {
        if (data == "1") {
            return "已同步"
        } else if (data == "0") {
            return "未同步"
        } else {
            return data;
        }
    }

    function renderManualinventory(data, type, full) {
        if (data == "1") {
            return "支持"
        } else if (data == "0") {
            return "不支持"
        } else {
            return data;
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('batchid').withTitle('盘点单据').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('adminusername').withTitle('负责人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('manualinventory').withTitle('手工盘点').withOption(
            'sDefaultContent', '').renderWith(renderManualinventory),
        DTColumnBuilder.newColumn('status').withTitle('盘点状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('waitcnt').withTitle('待盘').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('finishnormalcnt').withTitle('正常').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('finishpluscnt').withTitle('盘盈').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('finishlosscnt').withTitle('盘亏').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('syncstatus').withTitle('数据同步').withOption(
            'sDefaultContent', '').renderWith(renderDataStatus),
        DTColumnBuilder.newColumn('belongcompname').withTitle($rootScope.BELONGCOMP).withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('usedcompname').withTitle($rootScope.USEDCOMP).withOption(
        //     'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('resstartdate').withTitle('开始时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('resenddate').withTitle('结束时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('盘点资产').withOption(
            'sDefaultContent', '').renderWith(renderDownload),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').withOption(
            'width', '300px').renderWith(renderAction)
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "input",
                label: "内容",
                placeholder: "请输入查询内容",
                type: "input",
                show: true,
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
            .post($rootScope.project + "/api/zc/resInventory/ext/selectList.do",
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

    $scope.detail = function (id) {
        action(id, "detail");
    }

    function action(id, type) {
        var meta = {};
        meta.id = id;
        meta.dict = gdict;
        meta.type = type;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zcinventory.html',
            controller: zcinventorySaveCtl,
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

    $scope.finish=function(id){
        $confirm({
            text: '完成确认?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/zc/resInventory/ext/finish.do", {
                        id:id
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
    };
    $scope.del = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resInventory/ext/deleteById.do", {
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
    $scope.assignusers = function (id, pduserdata) {
        var meta = {};
        meta.id = id;
        meta.dict = gdict;
        meta.pduserdata = pduserdata;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zcinventory_user.html',
            controller: zcinventoryUserSaveCtl,
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
    }
    $scope.download = function (id) {
        $window.open($rootScope.project
            + "/api/zc/resInventory/ext/downloadInventoryRes.do?category=3&id=" + id);
    }
    $scope.inventory = function (id) {
        var meta = {};
        meta.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zcinventory_pd.html',
            controller: zcinventoryPdCtl,
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
    }
    $scope.cancel = function (id) {
        $confirm({
            text: '是否取消?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resInventory/ext/cancel.do",
                    {id: id}).success(function (res) {
                    if (res.success) {
                        flush()
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.syncdata = function (id) {
        $confirm({
            text: '是否同步数据?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resInventory/ext/syncdata.do",
                    {id: id}).success(function (res) {
                    if (res.success) {
                        flush()
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.add = function () {
        action();
    }
    flush();
};
app.register.controller('zcPdCtl', zcPdCtl);
