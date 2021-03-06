function rackscreenCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope,
                       $compile) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.catRootOpt = [];
    $scope.catRootSel = "";
    var dicts = "devdc";
    $http.post($rootScope.project + "/api/zc/queryDictFast.do", {
        uid: "devdconly",
        dicts: dicts,
        parts: "N",
        partusers: "N",
        comp: "N",
        belongcomp: "N"
    }).success(function (res) {
            if (res.success) {
                $scope.catRootOpt = res.data.devdc;
                if ($scope.catRootOpt.length > 0) {
                    $scope.catRootSel = $scope.catRootOpt[0];
                    flush();
                }
            } else {
                notify({
                    message: res.message
                });
            }
        });

    function flush() {
        $http.post($rootScope.project + "/api/zc/rack/ext/queryRackByArea.do", {
            dcid: $scope.catRootSel.dict_item_id
        }).success(function (res) {
            if (res.success) {
                $scope.data = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    $scope.query = function () {
        flush();
    }
    $scope.itemclick = function () {

    }
}

function rackviewCtl($sce,DTOptionsBuilder, DTColumnBuilder, $compile,
                     $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $timeout, $state) {
    $scope.catRootOpt = [];
    $scope.catRootSel = "";
    $scope.item = {};
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', false)
        .withOption('bStateSave', true).withOption('bProcessing', false)
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
        }).withButtons([
            {
                extend: 'colvis',
                columns: ':gt(0)',
                text: '??????/?????????',
                columnText: function (dt, idx, title) {
                    return (idx ) + ': ' + title;
                }
            },
            {
                extend: 'csv',
                text: 'Excel(?????????)',
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
                text: '??????',
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

    function stateChange(iColumn, bVisible) {
    }

    function rendeZcLoc(data, type, full) {
        var html = "";
        if (angular.isDefined(full.rackstr)) {
            html = html + full.rackstr;
        }
        if (angular.isDefined(full.frame)) {
            html = html + "[" + full.frame + "]"
        }
        if (angular.isDefined(full.locdtl)) {
            html = html + " " + full.locdtl
        }
        return html;
    }

    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('??????').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('envstr').withTitle('????????????').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('locdtl').withTitle('??????').withOption(
        'sDefaultContent', '').renderWith(rendeZcLoc));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('??????').withOption(
        'sDefaultContent', ''));

    function flushtab(rack, dc) {
        $http.post(
            $rootScope.project
            + "/api/zc/rack/ext/queryAssetsByRackId.do", {
                rack: rack, loc: dc
            }).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;

                var result=formatRackData(res.data,50,'??????');
                $scope.mtable=result.htmltable;
                if(result.falied.length>0){
                    $scope.dtOptions.aaData = result.falied;
                }else{
                    $scope.errdata=true;
                }
                $scope.trustHtml = $sce.trustAsHtml(result.htmltable);

            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    var gselectdc = "";
    var dicts = "devdc";
    $http.post($rootScope.project + "/api/zc/queryDictFast.do", {
        uid: "devdconly",
        dicts: dicts,
        parts: "N",
        partusers: "N",
        comp: "N",
        belongcomp: "N"
    })
        .success(function (res) {
            if (res.success) {
                $scope.catRootOpt = res.data.devdc;
                if ($scope.catRootOpt.length > 0) {
                    $scope.catRootSel = $scope.catRootOpt[0];
                    flushTree($scope.catRootSel.dict_item_id);
                }
            } else {
                notify({
                    message: res.message
                });
            }
        });
    // ?????????
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
        loading: "???????????????",
        ui: {
            theme_name: "classic" // ??????????????????
        },
        rules: {
            type_attr: "rel", // ??????????????????
            valid_children: "root" // ??????root??????????????????????????????
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
        $log.info("??????");
    }
    $scope.readyCB = function () {
        $scope.tree = $scope.treeInstance.jstree(true)
        // ??????????????????
        $scope.tree.open_all();
        // ??????????????????
        $scope.treeInstance.on("changed.jstree", function (e, data) {
            if (data.action == "select_node") {
                // ????????????
                var snodes = $scope.tree.get_selected();
                if (snodes.length == 1) {
                    var node = snodes[0];

                    flushtab(node, gselectdc);
                }
            }
        });
    }
    $scope.cc = function () {
    }
    $scope.createCB = function (e, item) {
    };

    function flushTree(id) {
        gselectdc = id;
        $http
            .post(
                $rootScope.project
                + "/api/zc/rack/ext/queryRackInfoTreeByDcId.do", {
                    id: id
                }).success(function (res) {
            if (res.success) {
                var data = res.data;
                data.push({parent: "#", id: "1", text: "??????", type: "root"})
        
                $scope.ignoreChanges = true;
                $scope.treeData = angular.copy(data);
                $scope.treeConfig.version++;
            } else {
                notify({
                    message: res.message
                });
            }
        });
    }

    $scope.query = function () {
        flushTree($scope.catRootSel.dict_item_id);
    }
    $scope.screen = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/infrastructure/modal_rackscreen.html',
            controller: rackscreenCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
                }
            }
        });
    }
};
app.register.controller('rackviewCtl', rackviewCtl);