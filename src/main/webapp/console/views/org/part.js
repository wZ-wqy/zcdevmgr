function hrmOrgPartCtl($translate, $confirm, $log, notify, $scope, $http, $rootScope,
                       $uibModal) {
    $scope.topMenuOpt = []
    $scope.topMenuSel = "";
    var comp = $translate.instant("DICT_COMP");
    var part = $translate.instant("DICT_PART");
    $scope.typeOpt = [{id: "comp", name: comp}, {id: "part", name: part}];
    $scope.typeSel = $scope.typeOpt[0];
    $scope.item = {};
    var org_id = "";
    $http.post($rootScope.project + "/api/hrm/orgQuery.do", {}).success(
        function (res) {
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
        $http.post($rootScope.project + "/api/hrm/orgNodeTreeQuery.do", ps)
            .success(function (res) {
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
            items: {
                "createPoint": {
                    "separator_before": false,
                    "separator_after": false,
                    "label": function () {
                        return "新建下级节点";
                    },
                    "action": function (data) {
                        // first before after last
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        $http.post($rootScope.project + "/api/hrm/orgNodeSave.do",
                            {
                                node_type: obj.type,
                                node_name: "新节点",
                                org_id: org_id,
                                parent_id: obj.id
                            }).success(function (res) {
                            if (res.success) {
                                inst.create_node(obj, {
                                    id: res.data.ID,
                                    text: "新节点",
                                    parent: obj.id
                                }, "last", function (new_node) {
                                });
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
                    "action": function (data) {
                        $log.info("删除节点");
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        $http.post(
                            $rootScope.project + "/api/hrm/orgNodeDelete.do", {
                                node_id: obj.id
                            }).success(function (res) {
                            if (res.success) {
                                inst.delete_node(obj);
                            }
                            notify({
                                message: res.message
                            });
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
                    $http.post($rootScope.project + "/api/hrm/orgNodeQuery.do", {
                        node_id: node
                    }).success(function (res) {
                        if (res.success) {
                            if (angular.isDefined(res.data.type)) {
                                if (res.data.type == "comp") {
                                    $scope.typeSel = $scope.typeOpt[0];
                                } else if (res.data.type == "part") {
                                    $scope.typeSel = $scope.typeOpt[1];
                                }
                            }
                            $scope.item = res.data;
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    })
                }
            }
        });
    }
    $scope.cc = function () {
    }
    $scope.createCB = function (e, item) {
    };
    $scope.query = function treeInstance() {
        $log.info($scope.topMenuSel, $scope.roleSel);
        if ($scope.topMenuSel == "" || $scope.roleSel == "") {
            notify({
                message: "请选择条件"
            });
            return;
        }
        flush();
    }
    $scope.ok = function () {
        if (angular.isDefined($scope.item.node_id)) {
            $scope.item.type = $scope.typeSel.id;
            $http.post($rootScope.project + "/api/hrm/orgNodeSave.do", $scope.item)
                .success(
                    function (res) {
                        if (res.success) {
                            var inst = $scope.tree;
                            inst.rename_node($scope.item.node_id,
                                $scope.item.node_name)
                        }
                        notify({
                            message: res.message
                        });
                    })
        } else {
            notify({
                message: "请选择节点"
            });
            return;
        }
    }
};
app.register.controller('hrmOrgPartCtl', hrmOrgPartCtl);