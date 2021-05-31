function sysRegionCtl($confirm, $log, notify, $scope, $http, $rootScope,
                      $uibModal) {
    $scope.data = {
        stime: moment().subtract(15, "days"),
        etime: moment().add(1, 'days')
    }
    $scope.treeData = [];
    $scope.ignoreChanges = false;
    $scope.newNode = {};

    function flush() {
        var ps = {};
        $http.post($rootScope.project + "/api/region/queryTree.do", ps)
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
            "file": {
                "icon": "fa fa-file icon-state-warning icon-lg"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'changed']
    }
    $scope.addNewNode = function () {
    };
    $scope.modelChanges = function (t) {
        return true;
    }
    $scope.test = function () {
        $log.info("测试");
    }
    $scope.readyCB = function () {
    }
    $scope.createCB = function (e, item) {
    };
    $scope.query = function treeInstance() {
        flush();
    }
    flush();
};
app.register.controller('sysRegionCtl', sysRegionCtl);