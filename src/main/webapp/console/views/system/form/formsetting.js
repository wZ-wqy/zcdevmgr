function modalformsaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                          $uibModalInstance, $scope, meta, $http, $rootScope, $compile) {
    $scope.data = {};
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    if (angular.isDefined(meta.id)) {
        $http.post($rootScope.project + "/api/form/sysForm/selectById.do", {
            id: meta.id
        }).success(function (res) {
            if (res.success) {
                $scope.data = res.data
            } else {
                notify({
                    message: res.message
                });
            }
        });
    } else {
        $scope.data.cid = meta.cid;
    }
    $scope.sure = function () {
        $scope.data.owner = meta.node;
        $http.post($rootScope.project + "/api/form/sysForm/insertOrUpdate.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }
}

function sysFormSettingCtl($window, $stateParams, DTOptionsBuilder,
                           DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http,
                           $rootScope, $uibModal) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withDOM('frtlpi').withPaginationType('simple').withDisplayLength(
            50).withOption("ordering", false).withOption("responsive",
            false).withOption("searching", true).withOption('scrollY',
            300).withOption('scrollX', true).withOption(
            'bAutoWidth', true).withOption('scrollCollapse', true)
        .withOption('paging', false).withOption('bStateSave', true).withOption('bProcessing', false)
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
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml)
        .withClass('select-checkbox checkbox_center').renderWith(
            function () {
                return ""
            }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('编号')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('表单名称')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ct').withTitle('表单配置')
        .withOption('sDefaultContent', '').renderWith(
            function (data, type, full) {
                if (angular.isDefined(data)) {
                    if (data.length > 20) {
                        return data.substring(0, 10) + '...';
                    } else {
                        return data;
                    }
                }
            }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('createTime').withTitle('创建时间')
        .withOption('sDefaultContent', ''));
    $scope.catRootOpt = [];
    $scope.catRootSel = "";
    $scope.item = {};
    var ps = {};
    ps.ids = angular.toJson([6]);
    $http
        .post($rootScope.project + "/api/ctCategoryRoot/ext/selectList.do",
            ps).success(function (res) {
        if (res.success) {
            $scope.catRootOpt = res.data;
            if ($scope.catRootOpt.length > 0) {
                $scope.catRootSel = $scope.catRootOpt[0];
                flushTree($scope.catRootSel.id)
            }
        } else {
            notify({
                message: res.message
            });
        }
    });
    // 树配置
    $scope.treeConfig = {
        core: {
            multiple: false,
            animation: true,
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
            "node": {
                "icon": "glyphicon glyphicon-tag"
            },
            "category": {
                "icon": "glyphicon glyphicon-equalizer"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'changed'],
        contextmenu: {
            items: {}
        }
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
    $scope.test = function () {
        $log.info("测试");
    }
    $scope.curSelNode = "";
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
                    $scope.curSelNode = node;
                    flush();
                }
            }
        });
    }
    $scope.cc = function () {
    }
    $scope.createCB = function (e, item) {
    };

    function flushTree(id) {
        $http
            .post(
                $rootScope.project
                + "/api/ctCategroy/queryCategoryTreeList.do", {
                    root: id
                }).success(function (res) {
            if (res.success) {
                $scope.ignoreChanges = true;
                $scope.treeData = angular.copy(res.data);
                $scope.treeConfig.version++;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    $scope.query = function () {
        flushTree($scope.catRootSel.id);
    }
    $scope.rowupdate = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            rowsave(selrow.id);
        }
    }
    $scope.rowinsert = function () {
        rowsave();
    }

    function rowsave(id) {
        var p = {};
        if ($scope.curSelNode == "") {
            notify({
                message: "请先选择分类"
            });
            return;
        }
        p.node = $scope.curSelNode;
        p.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/form/form_formsave.html',
            controller: modalformsaveCtl,
            size: 'lg',
            resolve: {
                meta: function () {
                    return p;
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

    $scope.itemquery = function () {
        flush();
    }

    function flush() {
        $http.post($rootScope.project + "/api/form/sysForm/selectList.do", {
            owner: $scope.curSelNode
        }).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
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

    $scope.preview = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            $window.open($rootScope.project
                + "/console/views/system/form/formdesign.html?id="
                + selrow.id)
        }
    }
    $scope.rowdel = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/form/sysForm/deleteById.do", {
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
};
app.register.controller('sysFormSettingCtl', sysFormSettingCtl);