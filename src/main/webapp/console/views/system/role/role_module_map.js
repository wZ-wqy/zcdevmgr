function sysRoleModuleMapCtl($confirm, $log, notify, $scope, $http, $rootScope, $stateParams,
                             $uibModal) {
    $scope.topMenuOpt = []
    $scope.topMenuSel = "";
    $scope.show = false;
    $scope.roleOpt = []
    $scope.roleSel = "";
    var role_id = "";
    $scope.crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
        "submit": false
    };
    privCrudCompute($scope.crud, $rootScope.curMemuBtns);
    $http.post($rootScope.project + "/api/sysMenus/selectList.do", {}).success(
        function (res) {
            if (res.success) {
                $scope.topMenuOpt = res.data;
                if (res.data.length > 0) {
                    $scope.topMenuSel = res.data[0];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    $http.post($rootScope.project + "/api/sysRoleInfo/selectList.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.roleOpt = res.data;
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
        ps.menu_id = $scope.topMenuSel.menuId;
        ps.role_id = $scope.roleSel.roleId;
        role_id = ps.role_id;
        $http.post($rootScope.project + "/api/menu/treeRoleChecked.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.show = true;
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
            multiple: true,
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
            "default2": {
                "valid_children": "all",
                icon: 'glyphicon glyphicon-flash'
            },
            dir: {
                icon: 'glyphicon glyphicon-folder-open'
            },
            menu: {
                icon: 'glyphicon glyphicon-th-list'
            },
            btn: {
                icon: 'glyphicon glyphicon-stop'
            },
            root: {
                valid_children: "folder",
                icon: 'glyphicon glyphicon-star'
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'checkbox'],
        contextmenu: {
            items: {
                "createArea": {
                    "icon": false,
                    "_disabled": false,
                    _class: "class",
                    "separator_before": false,
                    "separator_after": false,
                    "label": function () {
                        return "新建区域";
                    },
                    "action": function (data) {
                        // first before after last
                        var inst = $scope.tree;
                        var obj = inst.get_node(data.reference);
                        inst.create_node(obj, {}, "last", function (new_node) {
                            try {
                                inst.edit(new_node);
                            } catch (ex) {
                                alert(ex);
                            }
                        });
                    }
                },
                "createPoint": {
                    "separator_before": false,
                    "separator_after": false,
                    "label": function () {
                        return "新建巡逻点";
                    },
                    "action": function (data) {
                    }
                },
                "DeleteItem": {
                    "separator_before": false,
                    "separator_after": false,
                    "label": "删除",
                    "action": function (data) {
                        this.rename(data);
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
    $scope.ignoreModelChanges = function (t) {
    }
    $scope.ignoreModelChanges = function () {
    }
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
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
    $scope.submit = function () {
        $log.info("提交");
        $http.post($rootScope.project + "/api/menu/treeNodeRoleMap.do", {
            role_id: role_id,
            menu_id: $scope.topMenuSel.menuId,
            modules_arr: angular.toJson($scope.tree.get_selected())
        }).success(function (res) {
            notify({
                message: res.message
            });
        })
    }
};
app.register.controller('sysRoleModuleMapCtl', sysRoleModuleMapCtl);