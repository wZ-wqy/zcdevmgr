function myProcessCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                      $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.meta = {
        tablehide: false,
        tools: [
            {
                id: "1",
                label: "开始时间",
                type: "datetime",
                time: moment().subtract(30, "days"),
                show: true,
            },
            {
                id: "2",
                label: "结束时间",
                type: "datetime",
                time: moment().add(1, "days"),
                show: true,
            },
            {
                id: "3",
                label: "查询",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "4",
                priv: "act1",
                label: "详情",
                type: "btn",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>',
                show: true,
            }]
    }
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
    function renderBusType(data, type, full) {
        var html = data;
        if (angular.isDefined(data)) {
            if (data == "asset") {
                html = "资产管理";
            }
        }
        return html;
    }


    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('processinstanceid').withTitle('流程编号')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('busid').withTitle('业务编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('pstatus').withTitle('流程状态').withOption(
            'sDefaultContent', '').renderWith(renderZCSPStatus),
        DTColumnBuilder.newColumn('ptitle').withTitle('主题').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('bustype').withTitle('业务类型').withOption(
            'sDefaultContent', '').renderWith(renderBusType),
        DTColumnBuilder.newColumn('ptype').withTitle('流程分类').withOption(
            'sDefaultContent', '').renderWith(renderBusCat),
        DTColumnBuilder.newColumn('pstartusername').withTitle('发起人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createTime').withTitle('发起时间')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('pendtime').withTitle('结束时间').withOption(
            'sDefaultContent', '')]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        if ($scope.meta.tools[1].time - $scope.meta.tools[0].time >= 0) {
        } else {
            notify({
                message: "请选择正确的时间范围"
            });
            return;
        }
        ps.sdate = $scope.meta.tools[0].time.format('YYYY-MM-DD');
        ps.edate = $scope.meta.tools[1].time.format('YYYY-MM-DD');
        $http.post(
            $rootScope.project
            + "/api/flow/sysProcessData/ext/selectMyList.do", ps)
            .success(function (res) {
                $scope.dtOptions.aaData = res.data
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
    $scope.detail = function () {
        var item = getSelectRow();
        console.log(item);
        if (angular.isDefined(item)) {
            var meta = {};
            meta.busid = item.busid;
            meta.flowpagetype = "lookup";
            meta.pagetype = "select";
            var flowhtml = "";
            var flowctl;
            var ptype = item.ptype;
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
            }else {
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
            } else {
                notify({
                    message: "未配置流程页"
                });
            }

        }
    }
};
app.register.controller('flowapprovalCommonCtl', flowapprovalCommonCtl);
app.register.controller('flowsuggestCommonCtl', flowsuggestCommonCtl);
app.register.controller('myProcessCtl', myProcessCtl);