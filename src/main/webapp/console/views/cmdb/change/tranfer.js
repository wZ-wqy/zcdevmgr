

function tranferCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                        $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var gdict = {};
    var dicts = "devdc"
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do",
            {
                dicts: dicts,
                parts: "Y",
                partusers: "Y",
                comp: "Y",
                belongcomp: "Y",
                comppart: "Y",
                uid: "allocation"
            }).success(function (res) {
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

    
    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('"
            + full.id
            + "','" + full.busid + "','" + full.status + "')\" class=\"btn-white btn btn-xs\">单据详情</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction),
        DTColumnBuilder.newColumn('busid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('status').withTitle('办理状态').withOption(
            'sDefaultContent', '').withOption("width", '30').renderWith(renderZCSPStatus),
  

        DTColumnBuilder.newColumn('transfercatname').withTitle('转移原因').withOption(
            'sDefaultContent', '').withOption("width", '30'),

        DTColumnBuilder.newColumn('tusedcompname').withTitle('接收公司').withOption(
            'sDefaultContent', '').withOption("width", '30'),

        DTColumnBuilder.newColumn('tusedpartname').withTitle('接收部门').withOption(
            'sDefaultContent', '').withOption("width", '30'),

        DTColumnBuilder.newColumn('receiverusername').withTitle('接收人').withOption(
            'sDefaultContent', '').withOption("width", '30'),


        DTColumnBuilder.newColumn('receivedate').withTitle('接收时间').withOption(
            'sDefaultContent', '').withOption("width", '30'),

        DTColumnBuilder.newColumn('tlocname').withTitle('存放区域').withOption(
        'sDefaultContent', '').withOption("width", '30'),

            DTColumnBuilder.newColumn('tlocdtl').withTitle('位置详情况').withOption(
        'sDefaultContent', '').withOption("width", '30'),

  
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '').withOption("width", '30'),
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
                placeholder: "输入内容",
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
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">转移</button>'
            },
            {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="update()" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            {
                id: "btn5",
                label: "",
                type: "btn",
                show: true,
                priv: "remove",
                template: ' <button ng-click="approval()" class="btn btn-sm btn-primary" type="submit">送审</button>'
            }

        ]
    }
    $scope.meta = meta;
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);

    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        $http
            .post($rootScope.project + "/api/zc/resTranfer/ext/selectList.do",
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

    function action(id, busid, status) {
        var meta = {};
        meta.id = id;
        meta.status = status;
        meta.busid = busid;
        meta.pagetype="insert";
        meta.flowpagetype = "lookup";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/change/modal_tranferOrder.html',
            controller: resTranferOrderCtl,
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


    $scope.update=function(){
        var item = getSelectRow();
        if (angular.isDefined(item)&&angular.isDefined(item.busid)) {
            if (item.status != "apply") {
                notify({
                    message: "该状态不允许更新"
                });
                return;
            }else{
                var ps={};
                ps.id=item.id
                ps.busid = item.busid;
                ps.pagetype="insert";
                ps.flowpagetype = "lookup";
                var modalInstance = $uibModal.open({
                    backdrop: true,
                    templateUrl: 'views/cmdb/change/modal_tranferOrder.html',
                    controller: resTranferOrderCtl,
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
        }
    }
    $scope.detail = function (id,busid,status) {
        var ps={};
        ps.busid = busid;
        ps.status = status;
        ps.pagetype="select";
        ps.flowpagetype = "lookup";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/change/modal_tranferOrder.html',
            controller: resTranferOrderCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });


    }
    $scope.add = function () {
        action();
    }
    flush();

    $scope.approval = function () {
        var item = getSelectRow();
        if (angular.isDefined(item)) {
            if (item.status != "apply") {
                notify({
                    message: "该状态不允许送审"
                });
                return;
            }
            $confirm({
                text: '是否确定送审?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resTranfer/ext/approval.do", {
                            busid: item.busid
                        }).success(function (res) {
                        if (res.success) {
                            $http.post(
                                $rootScope.project
                                + "/api/zc/flow/startAssetFlow.do", res.data).success(function (rs) {
                                if (rs.success) {
                                    flush();
                                } else {

                                }
                                notify({
                                    message: res.message
                                });

                            });
                        } else {
                            notify({
                                message: res.message
                            });
                        }

                    });
                });
        }
    }
};
app.register.controller('flowapprovalCommonCtl', flowapprovalCommonCtl);
app.register.controller('flowsuggestCommonCtl', flowsuggestCommonCtl);
app.register.controller('tranferCtl', tranferCtl);
