

function zcbfCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
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
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
        'select-checkbox checkbox_center').renderWith(function () {
        return ""
    }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('单据编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('title').withTitle('标题').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('status').withTitle('办理状态').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderZCSPStatus));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ct').withTitle('报废原因').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
        'sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('cnt').withTitle('数量').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('busidate').withTitle('业务日期')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('processusername').withTitle('制单人')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('createTime').withTitle('创建时间')
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
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">报废</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="cancel()" class="btn btn-sm btn-primary" type="submit">取消</button>'
            },
            // {
            //     id: "btn3",
            //     label: "",
            //     type: "btn",
            //     show: false,
            //     priv: "update",
            //     template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            // },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "detail",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
            },
            {
                id: "btn51",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            }, {
                id: "btn6",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="approval()" class="btn btn-sm btn-primary" type="submit">送审</button>'
            }, {
                id: "btn6",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="print()" class="btn btn-sm btn-primary" type="submit">打印</button>'
            }
            // {
            //     id: "btn6",
            //     label: "",
            //     type: "btn",
            //     show: false,
            //     priv: "exportfile",
            //     template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
            // }
        ],
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
        $http.post($rootScope.project + "/api/zc/resScrape/ext/selectList.do", ps)
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
                        + "/api/zc/resScrape/ext/cancel.do", {
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
    $scope.detail = function () {
        var id;
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            id = selrow.id;
        } else {
            return;
        }
        var meta = {};
        //  meta.type = "dtl";
        meta.id = id;
        meta.status = selrow.status;
        meta.busid = selrow.uuid;
        meta.flowpagetype = "lookup";
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
        }, function (reason) {
        });
    }
    ////////////////////////////save/////////////////////
    $scope.save = function (type) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zcbf.html',
            controller: assetsbfCtl,
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
                res.push($scope.dtOptions.aaData[data[i]].uuid)
            }
            return angular.toJson(res);
        }
    }
    function downloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "assetsbf-" + moment().format('L') + '.zip';
        a.href = URL.createObjectURL(file);
        a.click();
        const tempA = document.getElementById('tempId');
        if (tempA) {
            tempA.parentNode.removeChild(tempA);
        }
    }

    $scope.print=function(){
        var selrows = getSelectPrintRows();
        if (angular.isDefined(selrows)) {
            var ps = {}
            ps.data = selrows;
            $http.post($rootScope.project + "/api/zc/resScrape/ext/print.do", {
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


    flush();
    $scope.approval = function () {
        var item = getSelectRow();
        if (angular.isDefined(item)) {
            console.log(item);
            item.ptype = "BF";
            item.name = item.title;
            item.busuuid = item.uuid;
            if (item.status != "apply") {
                notify({
                    message: "该状态不允许送审"
                });
                return;
            }


            $http.post(
                $rootScope.project
                + "/api/base/sysApprovalBusi/ext/selectByCode.do", {
                    code: "BF"
                }).success(function (res) {
                if (res.success) {
                    if(res.data.webapproval=="0"){

                        $confirm({
                            text: '报废确认?'
                        }).then(
                            function () {
                                var pps={};
                                pps.formtype = "none";
                                pps.ptype = item.ptype
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
app.register.controller('zcbfCtl', zcbfCtl);