function myProcessTodoCtl($window, $state, DTOptionsBuilder, DTColumnBuilder, $compile,
                          $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.meta = {
        tablehide: false,
        tools: [
            {
                id: "1",
                label: "查询",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "1",
                priv: "act1",
                label: "处理",
                type: "btn",
                template: ' <button ng-click="oper()" class="btn btn-sm btn-primary" type="submit">处理</button>',
                show: true,
            }
        ]
    }
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
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
        });
    $scope.dtInstance = {}

    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('id').withTitle('任务编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('businessId').withTitle('业务编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('subject').withTitle('主题').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('taskName').withTitle('任务名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('description').withTitle('任务描述')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('state').withTitle('任务状态').withOption(
            'sDefaultContent', '').renderWith(renderUfloTaskStatus),
        DTColumnBuilder.newColumn('rootProcessInstanceId').withTitle('流程实例').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createDate').withTitle('发起时间')
            .withOption('sDefaultContent', '')]
    $scope.query = function () {
        flush();
    }
    function flush() {
        var ps = {}
        ps.search = "";
        ps.pageSize = 1000;
        ps.pageIndex = 1;
        $http.post($rootScope.project + "/api/zc/flow/myProcessTodo.do",
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
    flush();
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
    $scope.oper = function () {
        var item = getSelectRow();
        if (angular.isDefined(item) && angular.isDefined(item.businessId)) {
            console.log(item);
            var meta = {};
            meta.busid = item.businessId;
            meta.pagetype = "select";
            meta.flowpagetype = "approval";
            meta.taskid = item.id;
            $http.post(
                $rootScope.project
                + "/api/flow/sysProcessData/ext/selectByBusinessId.do", {businessid: item.businessId})
                .success(function (res) {
                    var ptype = res.data.ptype;
                    if (ptype == "LY") {
                        flowhtml = 'views/cmdb/modal_lytklist.html';
                        flowctl = zclylistCtl;
                    } else if (ptype == "TK") {
                        flowhtml = 'views/cmdb/modal_lytklist.html';
                        flowctl = zctklistCtl;
                    } else if (ptype == "JY") {
                        flowhtml = 'views/cmdb/modal_jyghlist.html';
                        flowctl = zcjyghlistCtl;
                    } else if (ptype == "BF") {
                        flowhtml = 'views/cmdb/modal_zcbf.html';
                        flowctl = assetsbfCtl;
                    } else if (ptype == "DB") {
                        meta.actiontype = 'detail';
                        flowhtml = 'views/cmdb/modal_zcallocation.html';
                        flowctl = assetsallocationCtl;
                    } else if (ptype == "RESPURCHASE") {
                        flowhtml = 'views/purchase/modal_purchaseOrder.html';
                        flowctl = resPurchaseOrderCtl;
                    } else if (ptype == "ZY") {
                        flowhtml = 'views/cmdb/change/modal_tranferOrder.html';
                        flowctl = resTranferOrderCtl;
                    }

                    else {
                        ptype = "";
                    }
                    if (angular.isDefined(ptype) && ptype != "") {
                        var modalInstance = $uibModal.open({
                            backdrop: true,
                            templateUrl: flowhtml,
                            controller: flowctl,
                            size: 'blg',
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
                    } else {
                        notify({
                            message: "未配置流程页"
                        });
                    }
                })

        } else {
        }
    }
};
app.register.controller('flowapprovalCommonCtl', flowapprovalCommonCtl);
app.register.controller('flowsuggestCommonCtl', flowsuggestCommonCtl);
app.register.controller('myProcessTodoCtl', myProcessTodoCtl);