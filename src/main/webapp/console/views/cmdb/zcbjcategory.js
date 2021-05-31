function cmdbzcbjCateSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                                $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $timeout, $state) {
    $scope.actionOpt = [{
        id: "Y",
        name: "有效"
    }, {
        id: "N",
        name: "无效"
    }]
    $scope.actionSel = $scope.actionOpt[0];
    $scope.typeOpt = [{
        id: "dir",
        name: "目录"
    }, {
        id: "goods",
        name: "物品"
    }]
    $scope.typeSel = $scope.typeOpt[0];
    $scope.catRootOpt = [];
    $scope.catRootSel = "";
    $scope.item = {};
    var ps = {};
    var parm = [];
    parm.push($state.router.globals.current.data.code);
    ps.ids = angular.toJson(parm);
    $http.post($rootScope.project + "/api/ctCategoryRoot/ext/selectList.do", ps)
        .success(function (res) {
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
            , "dir": {
                "icon": "fa fa-list"
            },
            "goods": {
                "icon": "fa fa-file-o"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'changed'],
        contextmenu: {
            items: {
                "createPoint": {
                    "separator_before": false,
                    "separator_after": false,
                    "label": function () {
                        return "新建节点";
                    },
                    "_disabled": function (data) {
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        // 只有在层级节点可以添加
                        if (angular.isUndefined(obj.type)) {
                            return false;
                        }
                        if (obj.type == "default" || obj.type == "root" || obj.type == "node" || obj.type == "dir") {
                            return false
                        }
                        return true;
                    },
                    "action": function (data) {
                        // first before after last
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        $http.post(
                            $rootScope.project
                            + "/api/ctCategroy/addCategory.do", {
                                old_node_type: obj.type,
                                name: "新节点",
                                old_id: obj.id
                            }).success(function (res) {
                            if (res.success) {
                                $timeout(function () {
                                    $scope.tree.create_node(obj, {
                                        id: res.data.id,
                                        text: "新节点",
                                        parent: res.data.parent_id,
                                        type: "node"
                                    }, "last", function (new_node) {
                                    });
                                }, 300);
                            } else {
                                notify({
                                    message: res.message
                                });
                            }
                        })
                    }
                },
                "DeleteItem": {
                    "separator_before": false,
                    "separator_after": false,
                    "label": "删除节点",
                    "_disabled": function (data) {
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        $log.warn(obj);
                        $log.warn(obj.type);
                        if (obj.type == "root") {
                            return true
                        }
                        return false;
                    },
                    "action": function (data) {
                        $log.info("删除节点");
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        $http
                            .post(
                                $rootScope.project
                                + "/api/ctCategroy/queryCategoryById.do",
                                {
                                    id: obj.id
                                })
                            .success(
                                function (res) {
                                    if (res.success) {
                                        if (angular
                                                .isDefined(res.data.action)
                                            && res.data.action == "ndel") {
                                            notify({
                                                message: "系统默认设置,不允许删除。"
                                            });
                                        } else {
                                            $http
                                                .post(
                                                    $rootScope.project
                                                    + "/api/ctCategroy/deleteCategory.do",
                                                    {
                                                        id: obj.id
                                                    })
                                                .success(
                                                    function (
                                                        res) {
                                                        if (res.success) {
                                                            inst
                                                                .delete_node(obj);
                                                        }
                                                        notify({
                                                            message: res.message
                                                        });
                                                    })
                                        }
                                    } else {
                                        notify({
                                            message: res.message
                                        });
                                    }
                                })
                    }
                }
            }
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
                    $http.post(
                        $rootScope.project
                        + "/api/ctCategroy/queryCategoryById.do", {
                            id: node
                        }).success(function (res) {
                        if (res.success) {
                            if (angular.isDefined(res.data)) {
                                $scope.item = res.data;
                                if (angular.isDefined($scope.item.isaction)) {
                                    if ($scope.item.isaction == "Y") {
                                        $scope.actionSel = $scope.actionOpt[0];
                                    } else if ($scope.item.isaction == "N") {
                                        $scope.actionSel = $scope.actionOpt[1];
                                    } else {
                                        $scope.actionSel = $scope.actionOpt[0];
                                    }
                                } else {
                                    $scope.actionSel = $scope.actionOpt[0];
                                }
                                if (angular.isDefined($scope.item.type)) {
                                    if ($scope.item.type == "goods") {
                                        $scope.typeSel = $scope.typeOpt[1];
                                    } else if ($scope.item.type == "dir") {
                                        $scope.typeSel = $scope.typeOpt[0];
                                    } else {
                                        $scope.typeSel = $scope.typeOpt[0];
                                    }
                                } else {
                                    $scope.typeSel = $scope.typeOpt[0];
                                }
                            }
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    });
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

    $scope.saveItem = function () {
        $scope.item.isaction = $scope.actionSel.id;
        if (angular.isDefined($scope.typeSel.id)) {
            $scope.item.type = $scope.typeSel.id;
        }
        $http.post($rootScope.project + "/api/ctCategroy/updateCategory.do",
            $scope.item).success(function (res) {
            if (res.success) {
                var inst = $scope.tree;
                inst.rename_node($scope.item.id, $scope.item.name)
            }
            notify({
                message: res.message
            });
        });
    }
    $scope.query = function () {
        flushTree($scope.catRootSel.id);
    }
};
app.register.controller('cmdbzcbjCateSettingCtl', cmdbzcbjCateSettingCtl);