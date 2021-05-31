function modalAttrSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                          $uibModalInstance, $scope, meta, $http, $rootScope,
                          $compile) {
    $scope.needOpt = [{id: "0", name: "可选"}, {id: "1", name: "必须"}];
    $scope.needSel = $scope.needOpt[0];
    $scope.inputTypeOpt = [{id: "inputstr", name: "输入框(字符串)"}, {id: "inputint", name: "输入框(数字)"}];
    $scope.inputTypeSel = $scope.inputTypeOpt[0];
    $scope.data = {};
    $scope.data.sort = 1;
    $scope.jcOpt = [{id: "0", name: "不可以"}, {id: "1", name: "可以"}];
    $scope.jcSel = $scope.jcOpt[0];
    if (angular.isDefined(meta.attrid)) {
        $http.post(
            $rootScope.project
            + "/api/cmdb/resAttrs/selectById.do", {id: meta.attrid}).success(function (res) {
            if (res.success) {
                $scope.data = res.data;
                if (res.data.ifneed == "1") {
                    $scope.needSel = $scope.needOpt[1];
                } else {
                    $scope.needSel = $scope.needOpt[0];
                }
                if (res.data.ifinheritable == "1") {
                    $scope.jcSel = $scope.jcOpt[1];
                } else {
                    $scope.jcSel = $scope.jcOpt[0];
                }
                for (var i = 0; i < $scope.inputTypeOpt.length; i++) {
                    if ($scope.inputTypeOpt[i].id == res.data.inputtype) {
                        $scope.inputTypeSel = $scope.inputTypeOpt[i];
                        break;
                    }
                }
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        $scope.data.catid = meta.id;
        $scope.data.ifneed = $scope.needSel.id;
        $scope.data.inputtype = $scope.inputTypeSel.id;
        $scope.data.ifinheritable = $scope.jcSel.id;
        $http.post(
            $rootScope.project
            + "/api/cmdb/resAttrs/ext/insertOrUpdate.do", $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close('OK');
            }
            notify({
                message: res.message
            });
        });
    };
}

function cmdbsoftCateSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                                $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $timeout, $state) {
    $scope.actionOpt = [{
        id: "Y",
        name: "有效"
    }, {
        id: "N",
        name: "无效"
    }]
    $scope.typeOpt = [{
        id: "dir",
        name: "目录"
    }, {
        id: "goods",
        name: "物品"
    }]
    $scope.categorylevelOpt = [{id: "normal", name: "常规"}, {id: "low", name: "低价值"}, {id: "high", name: "高价值"}];
    $scope.categorylevelSel = $scope.categorylevelOpt[0];
    $scope.typeSel = $scope.typeOpt[0];
    $scope.actionSel = $scope.actionOpt[0];
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
                        if (obj.type == "root" || obj.type == "node" || obj.type == "dir") {
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
                                old_id: obj.id,
                                categorylevel: $scope.categorylevelSel.id
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

    function flushAttr(id) {
        $http.post(
            $rootScope.project
            + "/api/cmdb/resAttrs/ext/selectByCatId.do", {
                catid: id
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
                                if (angular.isDefined($scope.item.categorylevel)) {
                                    if ($scope.item.categorylevel == "normal") {
                                        $scope.categorylevelSel = $scope.categorylevelOpt[0];
                                    } else if ($scope.item.categorylevel == "low") {
                                        $scope.categorylevelSel = $scope.categorylevelOpt[1];
                                    } else if ($scope.item.categorylevel == "high") {
                                        $scope.categorylevelSel = $scope.categorylevelOpt[2];
                                    }
                                } else {
                                    $scope.categorylevelSel = $scope.categorylevelOpt[0];
                                }
                            }
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    });
                    flushAttr(node);
                    $http.post(
                        $rootScope.project
                        + "/api/cmdb/resAttrs/ext/selectInheritableAttrByCatId.do", {
                            catid: node
                        }).success(function (res) {
                        if (res.success) {
                            $scope.dtOptions2.aaData = res.data;
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
        $scope.item.categorylevel = $scope.categorylevelSel.id;
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
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });

    function renderType(data, type, full) {
        if (data == "inputstr") {
            return "输入框(字符串)"
        } else if (data == "inputint") {
            return "输入框(数字)"
        } else {
            return data;
        }
    }

    function renderIfNeed(data, type, full) {
        if (data == "1") {
            return "必须"
        } else if (data == "0") {
            return "可选"
        } else {
            return data;
        }
    }

    function renderJc(data, type, full) {
        if (data == "1") {
            return "可以"
        } else if (data == "0") {
            return "不可以"
        } else {
            return data;
        }
    }

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\" style='width:100px'> ";
        acthtml = acthtml + " <button ng-click=\"attrmodify('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">修改</button> ";
        acthtml = acthtml + " <button ng-click=\"attrremove('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('attrname').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('attrcode').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('inputtype').withTitle('输入类型').withOption(
            'sDefaultContent', '').renderWith(renderType),
        DTColumnBuilder.newColumn('ifinheritable').withTitle('继承').withOption(
            'sDefaultContent', '').renderWith(renderJc),
        DTColumnBuilder.newColumn('ifneed').withTitle('是否必须').withOption(
            'sDefaultContent', '').renderWith(renderIfNeed),
        DTColumnBuilder.newColumn('sort').withTitle('排序').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]
    $scope.dtInstance = {}
    //以下扩展属性相关
    $scope.addattr = function () {
        var ps = {};
        ps.id = $scope.item.id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_attrSave.html',
            controller: modalAttrSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flushAttr($scope.item.id);
            }
        }, function (reason) {
        });
    }
    $scope.attrmodify = function (id) {
        var ps = $scope.item;
        ps.attrid = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_attrSave.html',
            controller: modalAttrSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flushAttr($scope.item.id);
            }
        }, function (reason) {
        });
    }
    $scope.attrremove = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/cmdb/resAttrs/deleteById.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    flushAttr($scope.item.id);
                } else {
                    notify({
                        message: res.message
                    });
                }
            });
        });
    }
    //继承的属性
    $scope.dtOptions2 = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtColumns2 = [
        DTColumnBuilder.newColumn('attrname').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('attrcode').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('inputtype').withTitle('输入类型').withOption(
            'sDefaultContent', '').renderWith(renderType),
        DTColumnBuilder.newColumn('ifneed').withTitle('是否必须').withOption(
            'sDefaultContent', '').renderWith(renderIfNeed),
        DTColumnBuilder.newColumn('sort').withTitle('排序').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('catname').withTitle('来源').withOption(
            'sDefaultContent', '')]
    $scope.dtInstance2 = {}
};
app.register.controller('cmdbsoftCateSettingCtl', cmdbsoftCateSettingCtl);