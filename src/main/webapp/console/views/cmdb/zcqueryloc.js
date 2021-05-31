function zcquerylocCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                       $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    // 树配置
    $scope.treeConfig = {
        core: {
            multiple: false,
            animation: false,
            error: function (error) {
                $log.error('treeCtrl: error from js tree - '
                    + angular.toJson(error));
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
            "fold": {
                "icon": "fold fa fa-folder icon-state-success"
            },
            "comp": {
                "icon": "fa fa-building"
            },
            "part": {
                "icon": "fa fa-users"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'changed'],
        contextmenu: {
            items: {}
        }
    }
    $scope.modelChanges = function (t) {
        return true;
    }
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
        // 展开所有节点
        $scope.tree.open_all();
        // 响应节点变化
        $scope.treeInstance.on("changed.jstree", function (e, data) {
            if (data.action == "select_node") {
                // 加载数据
                var snodes = $scope.tree.get_selected();
                if (snodes.length == 1) {
                    var node = snodes[0];
                    flush(node);
                }
            }
        });
    }

    function flushTree(id) {
        var dicts = "devdc";
        $http
            .post($rootScope.project + "/api/zc/queryDictFast.do", {
                dicts: dicts,
                parts: "N",
                partusers: "N",
                comp: "N",
                belongcomp: "N",
                zccatused: "N",
                uid: "zcqueryloc"
            })
            .success(
                function (res) {
                    if (res.success) {
                        var fdata = [];
                        var e = {};
                        e.id = "1";
                        e.parent = "#";
                        e.text = "存放位置";
                        fdata.push(e);
                        for (var i = 0; i < res.data.devdc.length; i++) {
                            var e = {};
                            e.id = res.data.devdc[i].dict_item_id;
                            e.parent = "1";
                            e.text = res.data.devdc[i].name;
                            fdata.push(e);
                        }
                        $scope.ignoreChanges = true;
                        $scope.treeData = fdata
                        $scope.treeConfig.version++;
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                })
    }

    flushTree()
    // 分类
    $scope.URL = $rootScope.project + "/api/base/res/queryPageResAll.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: {classroot: "0", start: 0, category: 3}
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 420)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', false).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('createdRow', function (row) {
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
        }).withButtons([
            {
                extend: 'colvis',
                text: '显示/隐藏列',
                columns: ':gt(0)',
                columnText: function (dt, idx, title) {
                    return (idx ) + ': ' + title;
                }
            },
            {
                extend: 'csv',
                text: 'Excel',
                exportOptions: {
                    columns: ':visible',
                    trim: true,
                    modifier: {
                        page: 'current'
                    }
                }
            },
            {
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
            }
        ]);
    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withselect');

    function flush(node) {
        var ps = {}
        var time = new Date().getTime();
        ps.category = 3;
        if (angular.isDefined((node))) {
            ps.loc = node;
        }
        $scope.dtOptions.ajax.data = ps
    }

    $scope.query = function () {
        flush();
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
            $log.log("reason", reason)
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
            var d = $scope.dtInstance.DataTable.context[0].json.data;
            return d[data[0]]
        }
    }
};
app.register.controller('zcquerylocCtl', zcquerylocCtl);