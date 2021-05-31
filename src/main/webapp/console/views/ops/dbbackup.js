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

function modalimportDBCtl($log, $uibModalInstance, notify, $scope, $http,
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
                + "/api/ops/opsNode/ext/selectListDBImport.do", {
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
                        controller: modalimportdataDBFailCtl,
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

function dbinstanceSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                           $uibModalInstance, $scope, meta, $http, $rootScope, dicts) {
    $scope.item = {};
    $scope.item = meta
    $scope.bktypeOpt = dicts.dbbktype;
    $scope.bktypeSel = "";
    if (angular.isDefined($scope.item.bktype)) {
        for (var i = 0; i < $scope.bktypeOpt.length; i++) {
            if ($scope.item.bktype == $scope.bktypeOpt[i].dict_item_id) {
                $scope.bktypeSel = $scope.bktypeOpt[i];
                break;
            }
        }
    }
    $scope.bkstatusOpt = dicts.dbbkstatus;
    $scope.bkstatusSel = "";
    if (angular.isDefined($scope.item.bkstatus)) {
        for (var i = 0; i < $scope.bkstatusOpt.length; i++) {
            if ($scope.item.bkstatus == $scope.bkstatusOpt[i].dict_item_id) {
                $scope.bkstatusSel = $scope.bkstatusOpt[i];
                break;
            }
        }
    }
    $scope.archtypeOpt = dicts.dbbkarchtype;
    $scope.archtypeSel = "";
    if (angular.isDefined($scope.item.archtype)) {
        for (var i = 0; i < $scope.archtypeOpt.length; i++) {
            if ($scope.item.archtype == $scope.archtypeOpt[i].dict_item_id) {
                $scope.archtypeSel = $scope.archtypeOpt[i];
                break;
            }
        }
    }
    $scope.bkmethodOpt = dicts.dbbkmethod;
    $scope.bkmethodSel = "";
    if (angular.isDefined($scope.item.bkmethod)) {
        for (var i = 0; i < $scope.bkmethodOpt.length; i++) {
            if ($scope.item.bkmethod == $scope.bkmethodOpt[i].dict_item_id) {
                $scope.bkmethodSel = $scope.bkmethodOpt[i];
                break;
            }
        }
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                adom[i].style.width = "100%";
            }
        }
    }, 200);
    $scope.sure = function () {
        $scope.item.bkmethod = $scope.bkmethodSel.dict_item_id;
        $scope.item.archtype = $scope.archtypeSel.dict_item_id;
        $scope.item.bkstatus = $scope.bkstatusSel.dict_item_id;
        $scope.item.bktype = $scope.bktypeSel.dict_item_id;
        $scope.item.type = "dbinstance";
        $http.post(
            $rootScope.project + "/api/ops/opsNodeItem/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
                notify({
                    message: res.message
                });
            }
        })
    };
}

function opsdbbackupCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                        $log, notify, $scope, $http, $rootScope, $uibModal, $stateParams,
                        $window) {
    var dicts = "dbbktype,dbbkstatus,dbbkmethod,dbbkarchtype";
    var gdicts = {};
    $http.post($rootScope.project + "/api/zc/queryDictFast.do", {
        dicts: dicts,
        uid: "dbbackup"
    }).success(function (res) {
        if (res.success) {
            gdicts = res.data;
        } else {
            notify({
                message: res.message
            });
        }
    })
    var pbtns = $rootScope.curMemuBtns;
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询全部实例</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">新增实例</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新实例</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除实例</button>'
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
            }]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.tools, pbtns);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollX', true)
        .withOption('bAutoWidth', false).withOption('scrollCollapse', true)
        .withOption('paging', false).withOption('bStateSave', false)
        .withOption('bProcessing', false).withOption('bFilter', false)
        .withOption('bInfo', false).withOption('serverSide', false)
        .withOption('rowCallback', rowCallback).withOption('createdRow',
            function (row) {
                // Recompiling so we can bind Angular,directive to the
                $compile(angular.element(row).contents())($scope);
            }).withOption("select", {
            style: 'single'
        });
    $scope.dtInstance = {}

    function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        // Unbind first in order to avoid any duplicate handler
        // (see https://github.com/l-lin/angular-datatables/issues/87)
        $('td', nRow).unbind('click');
        $('td', nRow).bind('click', function () {
            $scope.$apply(function () {
                someClickHandler(aData);
            });
        });
        return nRow;
    }

    var gid = ""

    function someClickHandler(data) {
        gid = data.id;
        flushSubtab(data.id);
    }

    $scope.dtColumns = [DTColumnBuilder.newColumn('dbname').withTitle('系统')
        .withOption('sDefaultContent', '')]

    function flush() {
        var ps = {};
        $http.post(
            $rootScope.project
            + "/api/ops/opsNode/ext/selectDBListSimple.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                    $scope.dtItemOptions.aaData = [];
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    flush();

    function getSelectId() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        // 没有选择,或选择多行都返回错误
        if (data.length == 0 || data.length > 1) {
            return;
        } else {
            return $scope.dtOptions.aaData[data[0]];
        }
    }

    /** ********************子表******************* */
    $scope.dtItemOptions = DTOptionsBuilder.fromFnPromise()
        .withDataProp('data').withDOM('frtlpi').withPaginationType(
            'full_numbers').withDisplayLength(50).withOption(
            "ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 400)
        .withOption('scrollX', true).withOption('bAutoWidth', false)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', false).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow',
            function (row) {
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
    $scope.dtItemInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtItemInstance.DataTable.rows().select();
        } else {
            $scope.dtItemInstance.DataTable.rows().deselect();
        }
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';

    function ColWidthRender(data, type, full) {
        // return "<span style=\"white-space:normal;word-break:break-all;\">"
        // + data + "</span>";
        return data;
    }

    function ColWidthHHRender(data, type, full) {
        return "<span style=\"white-space:normal!important;word-break:break-all!important;\">"
            + data + "</span>";
    }

    $scope.dtItemColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }).withOption('width', '5px'),
        DTColumnBuilder.newColumn('xtname').withTitle('节点名称').withOption(
            'sDefaultContent', '').withOption('width', '30px')
            .renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('sysstatusstr').withTitle('节点状态').withOption(
            'sDefaultContent', '').withOption('width', '30px'),
        DTColumnBuilder.newColumn('nodebackupstr').withTitle('节点备份类型').withOption(
            'sDefaultContent', '').withOption('width', '30px')
            .renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
            'sDefaultContent', '').withOption('width', '5px'),
        DTColumnBuilder.newColumn('dbinstance').withTitle('数据库实例')
            .withOption('sDefaultContent', '').withOption('width',
            '5px').renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('sysdbdtlstr').withTitle('数据库版本')
            .withOption('sDefaultContent', '').withOption('width',
            '20px').renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('dbbkstatusstr').withTitle('当前状况')
            .withOption('sDefaultContent', '').withOption('width',
            '10px').renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('dbbktypestr').withTitle('备份类型')
            .withOption('sDefaultContent', '').withOption('width',
            '10px').renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('dbbkmethodstr').withTitle('备份方式')
            .withOption('sDefaultContent', '').withOption('width',
            '10px').renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('dbbkarchtypestr').withTitle('日志模式')
            .withOption('sDefaultContent', '').withOption('width',
            '50px').renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('dsize').withTitle('备份大小').withOption(
            'sDefaultContent', '').withOption('width', '10px')
            .renderWith(ColWidthRender),
        DTColumnBuilder.newColumn('bkkeep').withTitle('保留策略').withOption(
            'sDefaultContent', '').withOption('width', '20px')
            .renderWith(ColWidthHHRender),
        DTColumnBuilder.newColumn('bkstrategy').withTitle('备份策略')
            .withOption('sDefaultContent', '').withOption('width',
            '50px').renderWith(ColWidthHHRender),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '').withOption('width', '60px')
            .renderWith(ColWidthHHRender)]

    function flushSubtab(id) {
        var ps = {
            nodeid: id
        };
        // id不存在,则尝试从select中获取
        if (!angular.isDefined(id)) {
            var node = getSelectId();
            ps.nodeid = node.id;
        }
        // 如果还是不存在则报错
        if (!angular.isDefined(ps.nodeid)) {
            notify({
                message: "ID不存在"
            });
            return;
        }
        $http.post($rootScope.project + "/api/ops/opsNode/ext/selectDBList.do",
            ps).success(function (res) {
            if (res.success) {
                $scope.dtItemOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }

    function getSelectRowSubTab() {
        var data = $scope.dtItemInstance.DataTable.rows({
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
            return $scope.dtItemOptions.aaData[data[0]];
        }
    }

    $scope.query = function () {
        $http.post($rootScope.project + "/api/ops/opsNode/ext/selectDBList.do",
            {}).success(function (res) {
            if (res.success) {
                $scope.dtItemOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.itemquery = function () {
        flushSubtab();
    }
    $scope.save = function (type) {
        var ps = {};
        if (type == "1") {
            var node = getSelectRowSubTab();
            if (!angular.isDefined(node)) {
                return;
            }
            ps = node;
        } else {
            var node = getSelectId();
            if (!angular.isDefined(node)) {
                notify({
                    message: "请先选择系统"
                });
                return;
            }
            ps.xtname = node.dbname;
            ps.nid = node.id;
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/ops/modal_dbinstanceSave.html',
            controller: dbinstanceSaveCtl,
            size: 'lg',
            resolve: {
                meta: function () {
                    return ps;
                },
                dicts: function () {
                    return gdicts;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flushSubtab();
            }
        }, function (reason) {
        });
    }
    $scope.del = function () {
        var node = getSelectRowSubTab();
        if (!angular.isDefined(node)) {
            return;
        }
        $confirm({
            text: '是否删除?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/ops/opsNodeItem/deleteById.do", {
                        id: node.id
                    }).success(function (res) {
                    if (res.success) {
                        flushSubtab();
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.filedown = function () {
        var ps = {}
        $window.open($rootScope.project
            + "/api/ops/opsNode/ext/selectDBListExport.do");
    }
    $scope.importfile = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/ops/modal_importfile.html',
            controller: modalimportDBCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
                }
            }
        });
        modalInstance.result.then(function (result) {
            // flush();
        }, function (reason) {
        });
    }
};
app.register.controller('opsdbbackupCtl', opsdbbackupCtl);