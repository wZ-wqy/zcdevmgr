

function cmdbfaultrecordCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                            $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $state) {
    var datatype = $state.router.globals.current.data.datatype;
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

    function renderStatus(data, type, full) {
        if (data == "underrepair") {
            return "维修中"
        } else if (data == "cancel") {
            return "作废"
        } else if (data == "finish") {
            return "已完成"
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
        DTColumnBuilder.newColumn('fuuid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('fstatus').withTitle('办理状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('fprocessuser').withTitle('维护人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('fprocesstime').withTitle('维护时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('fmoney').withTitle('费用').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('freason').withTitle('原因').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('fmark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createTime').withTitle('创建时间').withOption(
            'sDefaultContent', '')]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "input",
                label: "内容",
                placeholder: "输入型号、编号、序列号",
                type: "input",
                show: true,
                ct: ""
            },
            {
                id: "btn",
                label: "",
                type: "btn",
                show: false,
                priv: "select",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "insert",
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">申请报修</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: false,
                priv: "act1",
                template: ' <button ng-click="finish()" class="btn btn-sm btn-primary" type="submit">完成维修</button>'
            }, {
                id: "btn5",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="modify()" class="btn btn-sm btn-primary" type="submit">修改</button>'
            }
            , {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "act2",
                template: ' <button ng-click="cancellation()" class="btn btn-sm btn-primary" type="submit">作废</button>'
            }, {
                id: "btn5",
                label: "",
                type: "btn",
                show: false,
                priv: "detail",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详细</button>'
            }
            , {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            }, {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="print()" class="btn btn-sm btn-primary" type="submit">打印</button>'
            }, {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="printdata()" class="btn btn-sm btn-primary" type="submit">批量导出</button>'
            }]
    }
    $scope.meta = meta;
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);

    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        url = "/api/zc/resRepair/ext/selectList.do"
        if (angular.isDefined(datatype) && datatype == "self") {
            url = "/api/zc/resRepair/ext/selectMyList.do"
        }
        $http
            .post($rootScope.project + url,
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

    function action(actiontype, id) {
        var meta = {};
        meta.actiontype = actiontype;
        meta.id = id;
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
    }

    $scope.finish = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            $confirm({
                text: '确定要将所选单据状态修改为已完成?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resRepair/ext/finish.do", {
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
    $scope.del = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resRepair/ext/deleteById.do", {
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
    $scope.modify = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            if (selrow.fstatus == "finish") {
                notify({
                    message: "当前状态不允许修改"
                });
                return
            }
            action('modify', selrow.id);
        } else {
            return;
        }
    }
    $scope.detail = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            action('detail', selrow.id);
        } else {
            return;
        }
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
                res.push($scope.dtOptions.aaData[data[i]].fuuid)
            }
            return angular.toJson(res);
        }
    }
    function downloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "assetsbx-" + moment().format('L') + '.zip';
        a.href = URL.createObjectURL(file);
        a.click();
        const tempA = document.getElementById('tempId');
        if (tempA) {
            tempA.parentNode.removeChild(tempA);
        }
    }
	function datadownloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "assetsbx-" + moment().format('L') + '.xls';
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
            $http.post($rootScope.project + "/api/zc/resRepair/ext/print.do", {
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
	$scope.printdata=function(){
        var selrows = getSelectPrintRows();
        if (angular.isDefined(selrows)) {
            var ps = {}
            ps.data = selrows;
            $http.post($rootScope.project + "/api/zc/resRepair/ext/printData.do", {
                data: selrows
            }, {
                responseType: 'arraybuffer'
            }).success(function (data) {
                var blob = new Blob([data], {
                    type: "application/vnd.ms-excel"
                });
                datadownloadFile(blob);
            })
        } else {
            return;
        }
    }

    $scope.add = function () {
        action('add');
    }
    flush();
    $scope.cancellation = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow) && angular.isDefined(selrow.id)) {
            //调用作废
            $confirm({
                text: '是否作废处理?'
            }).then(
                function () {
                    $http
                        .post($rootScope.project + "/api/zc/resRepair/ext/cancellation.do",
                            {id: selrow.id}).success(function (res) {
                        if (res.success) {
                            flush();
                        }
                        notify({
                            message: res.message
                        });
                    })
                });
        } else {
            return;
        }
    }
};
app.register.controller('cmdbfaultrecordCtl', cmdbfaultrecordCtl);
