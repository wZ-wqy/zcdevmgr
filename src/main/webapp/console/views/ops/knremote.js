function knremoteSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope) {

    $scope.data = {};
    $scope.typeOpt = [];
    $scope.typeSel = {};

    $scope.showOpt=[{id:"0",name:"隐藏"},{id:"1",name:"显示"},];
    $scope.showSel= $scope.showOpt[1];

    $http.post($rootScope.project + "/api/ops/knBase/ext/listCategory.do", {
    }).success(function (rs) {
        if (rs.success) {
            $scope.typeOpt=rs.data;
            if(rs.data.length>0){
                $scope.typeSel = $scope.typeOpt[0];
            }

            if (angular.isDefined(meta.id)) {
                // 加载数据
                $http.post($rootScope.project + "/api/ops/knBase/ext/selectById.do", {
                    id: meta.id
                }).success(function (res) {
                    if (res.success) {
                        $scope.data= res.data;
                        if($scope.data.isshow=="1"){
                            $scope.showSel= $scope.showOpt[1];
                        }else{
                            $scope.showSel= $scope.showOpt[0];
                        }
                        for(var i=0;i<$scope.typeOpt.length;i++){
                            if(res.data.catid==$scope.typeOpt[i].id){
                                $scope.typeSel = $scope.typeOpt[i];
                                break;
                            }
                        }


                    } else {
                        notify({
                            message: res.message
                        });
                    }
                })
            }
        } else {
            notify({
                message: rs.message
            });
        }
    })

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                adom[i].style.width = "100%";
            }
        }
    }, 200);
    $scope.sure = function () {
        $scope.data.catid=$scope.typeSel.id;
        $scope.data.type="remote";
        $scope.data.catname=$scope.typeSel.routeName;
        $scope.data.isshow=$scope.showSel.id;
        $http.post($rootScope.project + "/api/ops/knBase/ext/save.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
                notify({
                    message: res.message
                });
            }

        })
    };
}



function opsknremoteCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                      $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var pbtns = $rootScope.curMemuBtns;

    $scope.URL = $rootScope.project + "/api/ops/knBase/ext/selectPage.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: {start: 0,type:"remote"}
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
        }) ;
    function stateChange(iColumn, bVisible) {
    }


    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + " <button ng-click=\"review('" + full.shareurl
            + "')\" class=\"btn-white btn btn-xs\">预览</button> ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderShow(data, type, full) {
        if(data=="1"){
            return "显示";
        }else{
            return "隐藏";
        }
    }

    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作')
        .withOption('sDefaultContent', '').withOption("width", '150').renderWith(renderAction));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('isshow').withTitle('状态')
        .withOption('sDefaultContent', '').renderWith(renderShow));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('catname').withTitle('类型')
        .withOption('sDefaultContent', '').withOption("width", '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('title').withTitle('标题')
        .withOption('sDefaultContent', '').withOption("width", '100'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('label').withTitle('关键字')
        .withOption('sDefaultContent', '').withOption("width", '100'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('shareurl').withTitle('链接')
        .withOption('sDefaultContent', '').withOption("width", '150'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('lusername').withTitle('更新人')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('lasttime').withTitle('更新时间')
        .withOption('sDefaultContent', '').withOption("width", '30'));


    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            },{
                id: "btn5",
                label: "",
                type: "btn",
                show: true,
                priv: "search",
                template: ' <button ng-click="websearch()" class="btn btn-sm btn-primary" type="submit">网页搜索</button>'
            }

        ],
        tools: [{
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入名称",
            type: "input",
            ct: ""
        }]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.toolsbtn, pbtns);

    function flush() {
        var ps = {}
        var time = new Date().getTime();
        ps.time = time;
        ps.type="remote"
        ps.search=$scope.meta.tools[0].ct;
        $scope.dtOptions.ajax.data = ps
    }


    $scope.websearch = function () {
        $window.open($rootScope.project + "kn/index.html");
    }



    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/ops/knBase/deleteById.do", {
                        id: id
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

    $scope.review = function (shareurl) {
        $window.open(shareurl);
    }



    // //////////////////////////save/////////////////////
    $scope.save = function (id) {
        var meta = {};
        meta.id=id;
        var modalInstance = $uibModal.open({
            backdrop: false,
            templateUrl: 'views/ops/modal_knremoteSave.html',
            controller: knremoteSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta;
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
};
app.register.controller('opsknremoteCtl', opsknremoteCtl);