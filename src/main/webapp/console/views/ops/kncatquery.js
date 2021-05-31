function opsknbasecatqueryCtl($window,DTOptionsBuilder, DTColumnBuilder, $compile,
                         $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $timeout, $state) {

    $scope.URL = $rootScope.project + "/api/ops/knBase/ext/selectPage.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: {start: 0,type:"local"}
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 420)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', false).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }) ;
    function stateChange(iColumn, bVisible) {
    }

   
    function flush(catid){
         var ps = {}
         var time = new Date().getTime();
         ps.time = time;
         ps.catid=catid;
         ps.type="local";
         $scope.dtOptions.ajax.data = ps
    }

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";

        acthtml = acthtml + " <button ng-click=\"review('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">预览</button> ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderFile(data, type, full) {
        if (angular.isDefined(data) && data.length > 0) {
            var html = " <span><a href=\"../api/file/filedown.do?id=" + data + "\">下载</a></span> ";
            return html;
        }
    }

    $scope.review = function (id) {
        $window.open($rootScope.project + "/kn/knbaselook.html?id="+id);
    }

    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作')
        .withOption('sDefaultContent', '').withOption("width", '30').renderWith(renderAction));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('catname').withTitle('类型')
        .withOption('sDefaultContent', '').withOption("width", '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('title').withTitle('标题')
        .withOption('sDefaultContent', '').withOption("width", '150'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('lasttime').withTitle('更新时间')
        .withOption('sDefaultContent', '').withOption("width", '30'));


    var ps = {};
    var parm = [];

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
                "icon": "glyphicon glyphicon-th"
            },
            "category": {
                "icon": "glyphicon glyphicon-th"
            }
            , "dir": {
                "icon": "glyphicon glyphicon-th"
            },
            "goods": {
                "icon": "glyphicon glyphicon-th"
            }
        },
        version: 1,
        plugins: ['themes', 'types', 'contextmenu', 'changed'],
        contextmenu: {
            items: {


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
                    flush(node);
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


    flushTree("1");
    $scope.query=function(){
        flushTree("1");
    }

};
app.register.controller('opsknbasecatqueryCtl', opsknbasecatqueryCtl);