function modalimportdataHostFailCtl(DTOptionsBuilder, DTColumnBuilder,
                                    $compile, $confirm, $log, notify, $scope, meta, $http, $rootScope,
                                    $uibModal, $uibModalInstance) {
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
    $scope.dtOptions.aaData = meta.failed_data;
}

function loadOptHost(modal_meta, gdicts) {
    var item = modal_meta.meta.item;
    // 运行环境
    if (angular.isDefined(gdicts.sysenv) && gdicts.sysenv.length > 0) {
        modal_meta.meta.runenvOpt = gdicts.sysenv;
        if (angular.isDefined(item) && angular.isDefined(item.runenv)) {
            for (var i = 0; i < modal_meta.meta.runenvOpt.length; i++) {
                if (modal_meta.meta.runenvOpt[i].dict_item_id == item.runenv) {
                    modal_meta.meta.runenvSel = modal_meta.meta.runenvOpt[i];
                }
            }
        } else {
        }
    }
    // 业务
    if (angular.isDefined(gdicts.systype) && gdicts.systype.length > 0) {
        modal_meta.meta.busitypeOpt = gdicts.systype;
        if (angular.isDefined(item) && angular.isDefined(item.busitype)) {
            for (var i = 0; i < modal_meta.meta.busitypeOpt.length; i++) {
                if (modal_meta.meta.busitypeOpt[i].dict_item_id == item.busitype) {
                    modal_meta.meta.busitypeSel = modal_meta.meta.busitypeOpt[i];
                }
            }
        } else {
        }
    }
    // 位置
    if (angular.isDefined(gdicts.sysloc) && gdicts.sysloc.length > 0) {
        modal_meta.meta.locOpt = gdicts.sysloc;
        if (angular.isDefined(item) && angular.isDefined(item.loc)) {
            for (var i = 0; i < modal_meta.meta.locOpt.length; i++) {
                if (modal_meta.meta.locOpt[i].dict_item_id == item.loc) {
                    modal_meta.meta.locSel = modal_meta.meta.locOpt[i];
                }
            }
        } else {
        }
    }
    // 系统
    if (angular.isDefined(gdicts.sysos) && gdicts.sysos.length > 0) {
        modal_meta.meta.osOpt = gdicts.sysos;
        if (angular.isDefined(item) && angular.isDefined(item.os)) {
            for (var i = 0; i < modal_meta.meta.osOpt.length; i++) {
                if (modal_meta.meta.osOpt[i].dict_item_id == item.os) {
                    modal_meta.meta.osSel = modal_meta.meta.osOpt[i];
                }
            }
        } else {
        }
    }
    // 系统详细
    if (angular.isDefined(gdicts.sysosdtl) && gdicts.sysosdtl.length > 0) {
        modal_meta.meta.osdtlOpt = gdicts.sysosdtl;
        if (angular.isDefined(item) && angular.isDefined(item.osdtl)) {
            for (var i = 0; i < modal_meta.meta.osdtlOpt.length; i++) {
                if (modal_meta.meta.osdtlOpt[i].dict_item_id == item.osdtl) {
                    modal_meta.meta.osdtlSel = modal_meta.meta.osdtlOpt[i];
                }
            }
        } else {
        }
    }
    // 数据库
    if (angular.isDefined(gdicts.sysstatus) && gdicts.sysstatus.length > 0) {
        modal_meta.meta.statusOpt = gdicts.sysstatus;
        if (angular.isDefined(item) && angular.isDefined(item.status)) {
            for (var i = 0; i < modal_meta.meta.statusOpt.length; i++) {
                if (modal_meta.meta.statusOpt[i].dict_item_id == item.status) {
                    modal_meta.meta.statusSel = modal_meta.meta.statusOpt[i];
                }
            }
        } else {
        }
    }
    // 数据库
    if (angular.isDefined(gdicts.sysdb) && gdicts.sysdb.length > 0) {
        modal_meta.meta.dbOpt = gdicts.sysdb;
        if (angular.isDefined(item) && angular.isDefined(item.db)) {
            for (var i = 0; i < modal_meta.meta.dbOpt.length; i++) {
                if (modal_meta.meta.dbOpt[i].dict_item_id == item.db) {
                    modal_meta.meta.dbSel = modal_meta.meta.dbOpt[i];
                }
            }
        } else {
        }
    }
    // 数据库详细
    if (angular.isDefined(gdicts.sysdbdtl) && gdicts.sysdbdtl.length > 0) {
        modal_meta.meta.dbdtlOpt = gdicts.sysdbdtl;
        if (angular.isDefined(item) && angular.isDefined(item.dbdtl)) {
            for (var i = 0; i < modal_meta.meta.dbdtlOpt.length; i++) {
                if (modal_meta.meta.dbdtlOpt[i].dict_item_id == item.dbdtl) {
                    modal_meta.meta.dbdtlSel = modal_meta.meta.dbdtlOpt[i];
                }
            }
        } else {
        }
    }
    // 执行环境
    if (angular.isDefined(gdicts.sysexecenv) && gdicts.sysexecenv.length > 0) {
        modal_meta.meta.execOpt = gdicts.sysexecenv;
        if (angular.isDefined(item) && angular.isDefined(item.execenv)) {
            for (var i = 0; i < modal_meta.meta.execOpt.length; i++) {
                if (modal_meta.meta.execOpt[i].dict_item_id == item.execenv) {
                    modal_meta.meta.execSel = modal_meta.meta.execOpt[i];
                }
            }
        } else {
        }
    }
    // 监控
    if (angular.isDefined(gdicts.sysmonitor) && gdicts.sysmonitor.length > 0) {
        modal_meta.meta.monitorOpt = gdicts.sysmonitor;
        if (angular.isDefined(item) && angular.isDefined(item.monitor)) {
            for (var i = 0; i < modal_meta.meta.monitorOpt.length; i++) {
                if (modal_meta.meta.monitorOpt[i].dict_item_id == item.monitor) {
                    modal_meta.meta.monitorSel = modal_meta.meta.monitorOpt[i];
                }
            }
        } else {
        }
    }
    // 改密
    if (angular.isDefined(gdicts.syspwdstrategy)
        && gdicts.syspwdstrategy.length > 0) {
        modal_meta.meta.pwdOpt = gdicts.syspwdstrategy;
        if (angular.isDefined(item) && angular.isDefined(item.pwdstrategy)) {
            for (var i = 0; i < modal_meta.meta.pwdOpt.length; i++) {
                if (modal_meta.meta.pwdOpt[i].dict_item_id == item.pwdstrategy) {
                    modal_meta.meta.pwdSel = modal_meta.meta.pwdOpt[i];
                }
            }
        } else {
        }
    }
    // 等级
    if (angular.isDefined(gdicts.syslevel) && gdicts.syslevel.length > 0) {
        modal_meta.meta.syslevelOpt = gdicts.syslevel;
        if (angular.isDefined(item) && angular.isDefined(item.syslevel)) {
            for (var i = 0; i < modal_meta.meta.syslevelOpt.length; i++) {
                if (modal_meta.meta.syslevelOpt[i].dict_item_id == item.syslevel) {
                    modal_meta.meta.syslevelSel = modal_meta.meta.syslevelOpt[i];
                }
            }
        } else {
        }
    }
    // 节点备份
    if (angular.isDefined(gdicts.nodebak) && gdicts.nodebak.length > 0) {
        modal_meta.meta.nodebakOpt = gdicts.nodebak;
        if (angular.isDefined(item) && angular.isDefined(item.nodebackup)) {
            for (var i = 0; i < modal_meta.meta.nodebakOpt.length; i++) {
                if (modal_meta.meta.nodebakOpt[i].dict_item_id == item.nodebackup) {
                    modal_meta.meta.nodebakSel = modal_meta.meta.nodebakOpt[i];
                }
            }
        } else {
        }
    }
    // 中间价
    if (angular.isDefined(gdicts.sysmid) && gdicts.sysmid.length > 0) {
        modal_meta.meta.midOpt = gdicts.sysmid;
        if (angular.isDefined(item) && angular.isDefined(item.middleware)) {
            var middleware = angular.fromJson(item.middleware)
            if (middleware.length > 0) {
                var resarr = [];
                for (var i = 0; i < modal_meta.meta.midOpt.length; i++) {
                    for (var j = 0; j < middleware.length; j++) {
                        if (middleware[j] == modal_meta.meta.midOpt[i].dict_item_id) {
                            resarr.push(modal_meta.meta.midOpt[i]);
                        }
                    }
                }
                modal_meta.meta.midSel = resarr;
            }
        } else {
        }
    }
}

function modalimportHostCtl($log, $uibModalInstance, notify, $scope, $http,
                            $rootScope, $uibModal, $window, $timeout, meta) {
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
    $scope.ok = function () {
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
            $http.post(
                $rootScope.project
                + "/api/ops/opsNode/ext/selectListImport.do", {
                    id: id
                }).success(function (res) {
                $scope.okbtnstatus = false;
                if (res.success) {
                    $scope.myDropzone.removeAllFiles(true);
                    notify({
                        message: "操作成功！"
                    });
                    $uibModalInstance.close('OK');
                } else {
                    var modalInstance = $uibModal.open({
                        backdrop: true,
                        templateUrl: 'views/cmdb/modal_importFail.html',
                        controller: modalimportdataHostFailCtl,
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
                }
            })
        }, 3000);
    }
}

function syshostmgrCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                       $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var pbtns = $rootScope.curMemuBtns;
    var gdicts = {};

    var setTabHeight=getDtTabHeight(500,450);
    var dicts = "nodebak,sysstatus,sysdb,sysdbdtl,sysenv,sysexecenv,syslevel,sysloc,sysmid,sysmonitor,sysos,sysosdtl,syspwdstrategy,systype";
    $http.post($rootScope.project + "/api/zc/queryDictFast.do", {
        dicts: dicts,
        uid: "hostmgr"
    }).success(function (res) {
        if (res.success) {
            gdicts = res.data;
            flush();
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50).withOption("ordering", false).withOption(
            "responsive", false).withOption("searching", true)
        .withOption('scrollY', setTabHeight).withOption('scrollX', true)
        .withOption('bAutoWidth', true).withOption('scrollCollapse', true)
        .withOption('paging', true).withOption('bStateSave', true).withOption('bProcessing', true)
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
        }).withButtons([{
            extend: 'colvis',
            text: '显示/隐藏列',
            columns: ':gt(0)',
            columnText: function (dt, idx, title) {
                return (idx ) + ': ' + title;
            }
        }, {
            extend: 'csv',
            text: 'Excel(当前页)',
            exportOptions: {
                columns: ':visible',
                trim: true,
                modifier: {
                    page: 'current'
                }
            }
        }, {
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
        }]);

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

    function statusRender(data, type, full) {
        if (angular.isDefined(data)) {
            if (data == "在线") {
                return "<span style='color:green'>" + data + "</span>"
            } else if (data == "下线") {
                return "<span style='color:red'>" + data + "</span>"
            } else {
                return data;
            }
        } else {
            return data;
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml)
        .withClass('select-checkbox checkbox_center').renderWith(
            function () {
                return ""
            }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('名称')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ip').withTitle('IP')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('systypestr').withTitle(
        '系统类型').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('statusstr')
        .withTitle('状态').withOption('sDefaultContent', '').renderWith(statusRender));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('syslocstr')
        .withTitle('位置').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sysmonitorstr').withTitle(
        '监控部署').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sysosdtlstr').withTitle(
        '操作系统').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sysdbdtlstr').withTitle(
        '数据库').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('middlewarestr').withTitle(
        '中间件').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('syspwdstrategystr')
        .withTitle('改密策略').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('leader').withTitle('负责人')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('nodebackupstr').withTitle(
        '节点备份类型').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('nodebackupdtl').withTitle(
        '节点备份详情').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sysexecenvstr').withTitle(
        '执行环境').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('syslevelstr').withTitle(
        '风险等级').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sysenvstr').withTitle(
        '运行环境').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('label1').withTitle('标签1')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('useradmin').withTitle(
        '系统用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('userops')
        .withTitle('运维用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('userapp')
        .withTitle('应用用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('userdb').withTitle(
        '数据库安装用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('userdbused').withTitle(
        '数据库使用用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('usermid').withTitle(
        '中间件用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('userother').withTitle(
        '其他用户').withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('usernologin').withTitle(
        '未登陆用户').withOption('sDefaultContent', ''));
    $scope.query = function () {
        flush();
    }

    $scope.dtOptions.createdRow=function(nRow,aData,iDataIndex){
        if(aData.statusstr=='下线'){
            $(nRow).css("background-color", "#F78181"); //设置背景
        }
        $compile(angular.element(nRow).contents())($scope);
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
                show: false,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">新增</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            // {
            // id : "btn2",
            // label : "",
            // type : "btn",
            // show : true,
            // template : ' <button ng-click="detail()" class="btn btn-sm
            // btn-primary" type="submit">详情</button>'
            // },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "importfile",
                template: ' <button ng-click="arch()" class="btn btn-sm btn-primary" type="submit">归档</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "exportfile",
                template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: false,
                priv: "importfile",
                template: ' <button ng-click="importfile()" class="btn btn-sm btn-primary" type="submit">导入数据</button>'
            }],
        tools: [
            {
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入名称、IP",
            type: "input",
            ct: ""
        }

        ]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.toolsbtn, pbtns);

    function flush() {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $http.post($rootScope.project + "/api/ops/opsNode/ext/selectList.do",
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

    $scope.filedown = function () {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $window.open($rootScope.project
            + "/api/ops/opsNode/ext/selectListExport.do?search="
            + ps.search);
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
            for (var i = 0; i < data.length; i++) {
                res.push($scope.dtOptions.aaData[data[i]].id)
            }
            return angular.toJson(res);
        }
    }

    $scope.del = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/ops/opsNode/deleteById.do", {
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
        }
    }
    $scope.arch = function () {
        var selrows = getSelectRows();
        if (angular.isDefined(selrows)) {
            $confirm({
                text: '是否归档?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/ops/opsNode/ext/archAction.do", {
                            ids: selrows
                        }).success(function (res) {
                        if (res.success) {
                            flush();
                        }
                        notify({
                            message: res.message
                        });
                    });
                });
        } else {
            return;
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
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    // //////////////////////////save/////////////////////
    $scope.save = function (type) {
        var meta = {};
        var items = [];
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "500",
            placeholder: "请输入名称",
            label: "名称",
            need: true,
            name: 'name',
            ng_model: "name"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "500",
            placeholder: "请输入ip",
            label: "Ip",
            need: true,
            name: 'ip',
            ng_model: "ip"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "风险等级",
            need: false,
            disable_search: "false",
            dataOpt: "syslevelOpt",
            dataSel: "syslevelSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "状态",
            need: true,
            disable_search: "false",
            dataOpt: "statusOpt",
            dataSel: "statusSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "运行环境",
            need: true,
            disable_search: "false",
            dataOpt: "runenvOpt",
            dataSel: "runenvSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "业务类型",
            need: true,
            disable_search: "false",
            dataOpt: "busitypeOpt",
            dataSel: "busitypeSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "位置",
            need: true,
            disable_search: "false",
            dataOpt: "locOpt",
            dataSel: "locSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "节点备份类型",
            need: true,
            disable_search: "false",
            dataOpt: "nodebakOpt",
            dataSel: "nodebakSel"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入节点备份内容",
            label: "节点备份",
            need: false,
            name: 'nodebackupdtl',
            ng_model: "nodebackupdtl"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "负责人",
            label: "负责人",
            need: false,
            name: 'leader',
            ng_model: "leader"
        });
        items.push({
            type: "dashedword",
            name: 'model',
            label: "用户信息"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "超级用户",
            label: "超级用户",
            need: false,
            name: 'useradmin',
            ng_model: "useradmin"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "应用用户",
            label: "应用用户",
            need: false,
            name: 'userapp',
            ng_model: "userapp"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "数据库安装用户",
            label: "数据库安装用户",
            need: false,
            name: 'userdb',
            ng_model: "userdb"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "数据库使用用户",
            label: "数据库使用用户",
            need: false,
            name: 'userdbused',
            ng_model: "userdbused"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "中间件用户",
            label: "中间件用户",
            need: false,
            name: 'usermid',
            ng_model: "usermid"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "运维用户",
            label: "运维用户",
            need: false,
            name: 'userops',
            ng_model: "userops"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "其他用户",
            label: "其他用户",
            need: false,
            name: 'userother',
            ng_model: "userother"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "1000",
            placeholder: "未登陆用户",
            label: "不可登陆用户",
            need: false,
            name: 'usernologin',
            ng_model: "usernologin"
        });
        items.push({
            type: "dashedword",
            name: 'model',
            label: "应用中间件"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "操作系统",
            need: false,
            disable_search: "false",
            dataOpt: "osOpt",
            dataSel: "osSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "操作系统详情",
            need: false,
            disable_search: "false",
            dataOpt: "osdtlOpt",
            dataSel: "osdtlSel"
        });
        items.push({
            type: "selectmultiple",
            disabled: "false",
            label: "中间件",
            need: false,
            disable_search: "false",
            dataOpt: "midOpt",
            dataSel: "midSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "数据库",
            need: false,
            disable_search: "false",
            dataOpt: "dbOpt",
            dataSel: "dbSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "数据库详情",
            need: false,
            disable_search: "false",
            dataOpt: "dbdtlOpt",
            dataSel: "dbdtlSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "执行环境",
            need: false,
            disable_search: "false",
            dataOpt: "execOpt",
            dataSel: "execSel"
        });
        items.push({
            type: "dashedword",
            name: 'model',
            label: "密码策略"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "改密策略",
            need: false,
            disable_search: "true",
            dataOpt: "pwdOpt",
            dataSel: "pwdSel"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入密码备注内容",
            label: "密码备注",
            need: false,
            name: 'pwdmark',
            ng_model: "pwdmark"
        });
        items.push({
            type: "dashedword",
            name: 'model',
            label: "其他信息"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "监控",
            need: true,
            disable_search: "true",
            dataOpt: "monitorOpt",
            dataSel: "monitorSel"
        });
        items.push({
            type: "input",
            disabled: "false",
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
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入标签",
            label: "标签1",
            need: false,
            name: 'label1',
            ng_model: "label1"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入标签",
            label: "标签2",
            need: false,
            name: 'label2',
            ng_model: "label2"
        });
        var id;
        var itemvalue = {
            mark: "",
            pwdmark: "",
            leader: "",
            ip: "",
            name: "",
            nodebackup: "",
            label1: "",
            label2: ""
        };
        // type 1 更新
        if (type == 1) {
            var selrow = getSelectRow();
            var itemvalue = {};
            angular.copy(selrow, itemvalue);
            if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
                id = selrow.id;
            } else {
                return;
            }
        }
        meta = {
            footer_hide: false,
            title: "" + $state.router.globals.current.data.pageTitle,
            item: itemvalue,
            runenvOpt: [],
            runenvSel: "",
            busitypeOpt: [],
            busitypeSel: "",
            locOpt: [],
            locSel: "",
            osOpt: [],
            osSel: "",
            osdtlOpt: [],
            osdtlSel: "",
            midOpt: [],
            midSel: "",
            middtlOpt: [],
            middtlSel: "",
            dbOpt: [],
            dbSel: "",
            dbdtlOpt: [],
            dbdtlSel: "",
            execOpt: [],
            execSel: "",
            monitorOpt: [],
            monitorSel: "",
            pwdOpt: [],
            pwdSel: "",
            syslevelOpt: [],
            syslevelSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                if (angular.isDefined(modal_meta.meta.statusSel) && angular.isDefined(modal_meta.meta.statusSel.dict_item_id)) {
                    modal_meta.meta.item.status = modal_meta.meta.statusSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.runenvSel) && angular.isDefined(modal_meta.meta.runenvSel.dict_item_id)) {
                    modal_meta.meta.item.runenv = modal_meta.meta.runenvSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.busitypeSel) && angular.isDefined(modal_meta.meta.busitypeSel.dict_item_id)) {
                    modal_meta.meta.item.busitype = modal_meta.meta.busitypeSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.locSel) && angular.isDefined(modal_meta.meta.locSel.dict_item_id)) {
                    modal_meta.meta.item.loc = modal_meta.meta.locSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.osSel) && angular.isDefined(modal_meta.meta.osSel.dict_item_id)) {
                    modal_meta.meta.item.os = modal_meta.meta.osSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.osdtlSel) && angular.isDefined(modal_meta.meta.osdtlSel.dict_item_id)) {
                    modal_meta.meta.item.osdtl = modal_meta.meta.osdtlSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.dbSel) && angular.isDefined(modal_meta.meta.dbSel.dict_item_id)) {
                    modal_meta.meta.item.db = modal_meta.meta.dbSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.dbdtlSel) && angular.isDefined(modal_meta.meta.dbdtlSel.dict_item_id)) {
                    modal_meta.meta.item.dbdtl = modal_meta.meta.dbdtlSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.execSel) && angular.isDefined(modal_meta.meta.execSel.dict_item_id)) {
                    modal_meta.meta.item.execenv = modal_meta.meta.execSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.monitorSel) && angular.isDefined(modal_meta.meta.monitorSel.dict_item_id)) {
                    modal_meta.meta.item.monitor = modal_meta.meta.monitorSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.pwdSel) && angular.isDefined(modal_meta.meta.pwdSel.dict_item_id)) {
                    modal_meta.meta.item.pwdstrategy = modal_meta.meta.pwdSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.syslevelSel) && angular.isDefined(modal_meta.meta.syslevelSel.dict_item_id)) {
                    modal_meta.meta.item.syslevel = modal_meta.meta.syslevelSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.nodebakSel) && angular.isDefined(modal_meta.meta.nodebakSel.dict_item_id)) {
                    modal_meta.meta.item.nodebackup = modal_meta.meta.nodebakSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.midSel)) {
                    var name = ""
                    var ids = [];
                    for (var i = 0; i < modal_meta.meta.midSel.length; i++) {
                        if (i == 0) {
                            name = name + modal_meta.meta.midSel[i].name;
                        } else {
                            name = name + "," + modal_meta.meta.midSel[i].name;
                        }
                        ids.push(modal_meta.meta.midSel[i].dict_item_id);
                    }
                    modal_meta.meta.item.middleware = angular.toJson(ids);
                    modal_meta.meta.item.middlewarestr = name;
                }
                $http.post(
                    $rootScope.project
                    + "/api/ops/opsNode/ext/insertOrUpdate.do",
                    modal_meta.meta.item).success(function (res) {
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
                var gdicts2 = {};
                angular.copy(gdicts, gdicts2)
                loadOptHost(modal_meta, gdicts2);
            }
        }
        // 打开静态框
        var modalInstance = $uibModal.open({
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
    }
    $scope.importfile = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/ops/modal_importfile.html',
            controller: modalimportHostCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
        });
    }
};
app.register.controller('syshostmgrCtl', syshostmgrCtl);