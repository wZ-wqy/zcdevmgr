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
function collectionreturnCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $window,
                             $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var gdict = {};
    var dicts = "devdc";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "N",
            zccatused: "N",
            uid: "zclytkdata"
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
    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('"
            + full.busuuid
            + "','" + full.status + "','" + full.bustype + "')\" class=\"btn-white btn btn-xs\">单据详情</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }
    function renderType(data, type, full) {
        if (data == "LY") {
            return "领用"
        } else if (data == "TK") {
            return "退库"
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
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction),
        DTColumnBuilder.newColumn('busuuid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('单据名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('bustype').withTitle('单据类型').withOption(
            'sDefaultContent', '').renderWith(renderType),
        DTColumnBuilder.newColumn('status').withTitle('办理状态').withOption(
            'sDefaultContent', '').renderWith(renderZCSPStatus),
        DTColumnBuilder.newColumn('crusername').withTitle('领用人/退库人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('busdatestr').withTitle('领用时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('returndatestr').withTitle('预计退库时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rreturndatestr').withTitle('实际退库时间').withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('tcompfullname').withTitle($rootScope.USEDCOMP_LYTK).withOption(
        //     'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tpartname').withTitle($rootScope.USEDPART_LYTK).withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('tlocstr').withTitle('领用/退库后区域').withOption(
        //     'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tlocdtl').withTitle('领用/退库后位置').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('processusername').withTitle('领用处理人/退库处理人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "input",
                show: true,
                label: "内容",
                placeholder: "输入搜索内容",
                type: "input",
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
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">领用</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="tk()" class="btn btn-sm btn-primary" type="submit">退库</button>'
            },
            {
                id: "btn663",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="cancel()" class="btn btn-sm btn-primary" type="submit">取消</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="approval()" class="btn btn-sm btn-primary" type="submit">送审</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="print()" class="btn btn-sm btn-primary" type="submit">打印</button>'
            }
            ]
    }
    $scope.meta = meta;
    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        $http
            .post($rootScope.project + "/api/zc/resCollectionreturn/ext/selectList.do",
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
    $scope.detail = function (uuid, status, type) {
        var ps = {}
        ps.busid = uuid;
        ps.status = status;
        ps.flowpagetype = "lookup";
        if (type == "LY") {
            var modalInstance = $uibModal.open({
                backdrop: true,
                templateUrl: 'views/cmdb/modal_lytklist.html',
                controller: zclylistCtl,
                size: 'blg',
                resolve: {
                    meta: function () {
                        return ps;
                    }
                }
            });
        } else if (type == "TK") {
            var modalInstance = $uibModal.open({
                backdrop: true,
                templateUrl: 'views/cmdb/modal_lytklist.html',
                controller: zctklistCtl,
                size: 'blg',
                resolve: {
                    meta: function () {
                        return ps;
                    }
                }
            });
        }
    }
    function action(id) {
        var ps = {};
        ps.id = id;
        ps.dict = gdict;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_collectionSave.html',
            controller: collectionSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
        });
    }
    $scope.add = function () {
        action();
    }


    function getSelectPrintRows() {
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
                res.push($scope.dtOptions.aaData[data[i]].busuuid)
            }
            return angular.toJson(res);
        }
    }
    function downloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "assetslytk-" + moment().format('L') + '.zip';
        a.href = URL.createObjectURL(file);
        a.click();
        const tempA = document.getElementById('tempId');
        if (tempA) {
            tempA.parentNode.removeChild(tempA);
        }
    }

    $scope.cancel = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            var id = selrow.id;
            $confirm({
                text: '是否取消?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resCollectionreturn/ext/cancel.do", {
                            id: id
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

    $scope.print=function(){
        var selrows = getSelectPrintRows();
        if (angular.isDefined(selrows)) {
            var ps = {}
            ps.data = selrows;
            $http.post($rootScope.project + "/api/zc/resCollectionreturn/ext/print.do", {
                data: selrows
            }, {
                responseType: 'arraybuffer'
            }).success(function (data) {
                var blob = new Blob([data], {
                    type: "application/vnd.ms-excel"
                });
                downloadFile(blob);
            })
        } else {
            return;
        }
    }



    $scope.tk = function () {
        var ps = {};
        ps.dict = gdict;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_tkSave.html',
            controller: tkSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
        });
    }
    flush();
    $scope.approval = function () {
        var item = getSelectRow();
        if (angular.isDefined(item)) {
            item.ptype = item.bustype;
            if (item.status != "apply") {
                notify({
                    message: "该状态不允许送审"
                });
                return;
            }

            $http.post(
                $rootScope.project
                + "/api/base/sysApprovalBusi/ext/selectByCode.do", {
                    code:  item.ptype
                }).success(function (res) {
                if (res.success) {
                    if(res.data.webapproval=="0"){
                        $confirm({
                            text: '领用退库确认?'
                        }).then(
                            function () {
                                var pps={};
                                pps.formtype = "none";
                                pps.ptype = item.bustype
                                pps.title = item.name;
                                pps.busid = item.busuuid;
                                pps.ifsp="0";
                                pps.formtype = "none";
                                $http.post(
                                    $rootScope.project
                                    + "/api/zc/flow/startAssetFlow.do", pps).success(function (res) {
                                    if (res.success) {
                                        flush();
                                    } else {
                                        notify({
                                            message: res.message
                                        });
                                    }
                                });
                            });
                    }else{
                        var modalInstance = $uibModal.open({
                            backdrop: true,
                            templateUrl: 'views/cmdb/flow/modal_chosenFlowTreeView.html',
                            controller: chosenFlowTreeCtl,
                            size: 'blg',
                            resolve: {
                                meta: function () {
                                    return item;
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
                } else {
                    notify({
                        message: res.message
                    });
                }
            })

        }
    }
};
app.register.controller('flowapprovalCommonCtl', flowapprovalCommonCtl);
app.register.controller('flowsuggestCommonCtl', flowsuggestCommonCtl);
app.register.controller('collectionreturnCtl', collectionreturnCtl);