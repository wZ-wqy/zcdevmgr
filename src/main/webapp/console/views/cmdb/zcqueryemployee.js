



function zcqueryEmployeeCtl($timeout, $localStorage, notify, $log, $uibModal,
                   $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                   DTColumnBuilder, $compile, $confirm) {

    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('bAutoWidth', false)
        .withOption('paging', true).withOption('bStateSave', true)
        .withOption('bProcessing', false).withOption('bFilter', false)
        .withOption('bInfo', true).withOption('serverSide', false)
        .withOption('createdRow',
            function (row) {
                // Recompiling so we can bind Angular,directive to the
                $compile(angular.element(row).contents())($scope);
            });
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sourcetype').withTitle('资产动作').withOption(
        'sDefaultContent', '').withOption('width', '30'));


    $scope.dtColumns.push(DTColumnBuilder.newColumn('classfullname').withTitle('资产类型').withOption(
        'sDefaultContent', '').withOption('width', '30'));

    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption('width', '30'));

    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('资产名称').withOption(
        'sDefaultContent', '').withOption('width', '30'));

    $scope.dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('资产型号').withOption(
        'sDefaultContent', '').withOption('width', '30'));

    $scope.dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列号').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    function flush() {
        var ps = {}
        ps.userid=meta.user_id;
        $http.post($rootScope.project + "/api/zc/queryUserAssetsHistory.do",
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

}

function assetsemployCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.topMenuOpt = []
    $scope.topMenuSel = "";
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withPaginationType('full_numbers').withDisplayLength(25).withOption("ordering", false).withOption("responsive", true)
        .withOption("searching", false).withOption("paging", false).withOption('bStateSave', true).withOption('bProcessing', true).withOption('bFilter', false).withOption(
            'bInfo', true).withOption('serverSide', false).withOption('bAutoWidth', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"queryAssets('" + full.user_id + "')\" class=\"btn-white btn btn-xs\">查询资产</button> ";
        acthtml=acthtml+"</div>"
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var res = "无效";
        if (full.is_action == "Y") {
            res = "有效";
        }
        return res;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('empl_id').withTitle('员工编号').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('姓名').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('orgfullname').withTitle('所属组织').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption('sDefaultContent', ''),
        			DTColumnBuilder.newColumn('user_id').withTitle('操作').withOption(
        					'sDefaultContent', '').renderWith(renderAction)
    ]
    var org_id = "";
    $http.post($rootScope.project + "/api/hrm/orgQuery.do", {}).success(function (res) {
        if (res.success) {
            $scope.topMenuOpt = res.data;
            if (res.data.length > 0) {
                $scope.topMenuSel = res.data[0];
                // 加载组织信息
                flush();
            }
        } else {
            notify({
                message: res.message
            });
        }
    })
    $scope.treeData = [];
    $scope.ignoreChanges = false;
    $scope.newNode = {};

    function flush() {
        var ps = {};
        ps.org_id = $scope.topMenuSel.org_id;
        org_id = ps.org_id;
        $http.post($rootScope.project + "/api/hrm/orgNodeTreeQuery.do", ps).success(function (res) {
            if (res.success) {
                $scope.ignoreChanges = true;
                $scope.treeData = angular.copy(res.data);
                $scope.treeConfig.version++;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }

    $scope.treeConfig = {
        core: {
            multiple: false,
            animation: true,
            error: function (error) {
                $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
            },
            check_callback: true,
            worker: true
        },
        loading: "加载中……",
        ui: {
            theme_name: "classic" // 设置皮肤样式
        },
        rules: {
            type_attr: "rel", // 设置节点类型
            valid_children: "root" // 只有root节点才能作为顶级结点
        },
        callback: {
            onopen: function (node, tree_obj) {
                return true;
            }
        },
        types: {
            "default": {
                icon: 'glyphicon glyphicon-th'
            },
            root: {
                icon: 'glyphicon glyphicon-home'
            },
            "file": {
                "icon": "fa fa-file icon-state-warning icon-lg"
            },
            "comp": {
                "icon": "fa fa-building"
            },
            "part": {
                "icon": "fa fa-users"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'changed']
    }
    $scope.addNewNode = function () {
        $scope.treeData.push({
            id: (newId++).toString(),
            parent: $scope.newNode.parent,
            text: $scope.newNode.text
        });
    };
    $scope.modelChanges = function (t) {
        return true;
    }

    function flushEmployee() {
        var node = "";
        var snodes = $scope.tree.get_selected();
        if (snodes.length == 1) {
            var node = snodes[0];
        }
        $http.post($rootScope.project + "/api/hrm/queryEmplByOrg.do", {
            node_id: node
        }).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }

    $scope.test = function () {
        $log.info("测试");
    }
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
        // 展开所有节点
        $scope.tree.open_all();
        // 响应节点变化
        $scope.treeInstance.on("changed.jstree", function (e, data) {
            if (data.action == "select_node") {
                // 加载数据
                flushEmployee();
            }
        });
    }
    $scope.cc = function () {
    }
    $scope.createCB = function (e, item) {
    };

    $scope.queryAssets=function(user_id){

        var ps={};
        ps.user_id=user_id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zcqueryEmployee.html',
            controller: zcqueryEmployeeCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });

    }
};
app.register.controller('assetsemployCtl', assetsemployCtl);
