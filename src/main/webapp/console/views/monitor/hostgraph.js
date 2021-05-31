function hostgroupimageCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.data = {
        stime: moment().subtract(3, "days"),
        etime: moment()
    }
    $scope.treeData = [];
    $scope.ignoreChanges = false;
    $scope.newNode = {};

    function flush() {
        var ps = {};
        $http.post($rootScope.project + "/api/zbx/hostgroup/getAllHostGroupsListFormatTree.do?groups=1", ps).success(function (res) {
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

    flush();
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
    $scope.datagraphraw = [];
    $scope.datagraph = [];

    function flushGraph() {
        var ps = {};
        ps.sdate = $scope.data.stime.format('YYYY-MM-DD HH:mm');
        ps.edate = $scope.data.etime.format('YYYY-MM-DD HH:mm');
        if ($scope.data.etime - $scope.data.stime >= 61) {
        } else {
            notify({
                message: "时间区间不可超过2个月或时间选择错误!"
            });
            return;
        }
        var temp = [];
        for (var i = 0; i < $scope.datagraphraw.length; i++) {
            var e = {};
            e.name = $scope.datagraphraw[i].name;
            e.url = "../api/zbx/image/getOneByGraphId.do?width=800&graphid=" + $scope.datagraphraw[i].graphid + "&start=" + ps.sdate + ":00&end=" + ps.edate + ":00";
            temp.push(e);
        }
        $scope.datagraph = temp;
    }

    $scope.query = function () {
        flushGraph();
    }
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
        // 展开所有节点
        $scope.tree.open_all();
        // 响应节点变化
        $scope.treeInstance.on("changed.jstree", function (e, data) {
            if (data.action == "select_node") {
                // 加载数据
                var item = data.node.original;
                if (item.type == "host") {
                    //获取图片列表
                    var ps = {};
                    ps.hostid = item.hostid;
                    $http.post($rootScope.project + "/api/zbx/graph/getGraphByHostId.do", ps).success(function (res) {
                        if (res.success) {
                            $scope.datagraphraw = res.data;
                            flushGraph();
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
};
app.register.controller('hostgroupimageCtl', hostgroupimageCtl);
