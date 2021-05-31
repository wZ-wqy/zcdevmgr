function rackobjectCtl($sce,DTColumnBuilder,DTOptionsBuilder,$compile,$timeout, $localStorage, notify, $log, $uibModal,
                        $uibModalInstance, $scope, meta, $http, $rootScope) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
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
        });

    $scope.dtInstance = {}
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

    $scope.errdata=false;

    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('result').withTitle('结果').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('envstr').withTitle('运行环境').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('locdtl').withTitle('位置').withOption(
        'sDefaultContent', '').renderWith(rendeZcLoc));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', ''));

    //首次格式化
    var ps={};
    ps.loc=meta.locid;
    ps.rack=meta.rackid;
    $http.post($rootScope.project + "/api/zc/rack/ext/queryAssetsByRackId.do", ps)
        .success(function (res) {
            if (res.success) {
            } else {
                notify({
                    message: res.message
                });
            }
            var result=formatRackData(res.data,meta.capacity,'机柜');
            $scope.mtable=result.htmltable;
            if(result.falied.length>0){
                $scope.dtOptions.aaData = result.falied;
            }else{
                $scope.errdata=true;
            }
            $scope.trustHtml = $sce.trustAsHtml(result.htmltable);
        })



  }

function addracklistCtl($compile,DTColumnBuilder,DTOptionsBuilder,$timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, meta, $http, $rootScope) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
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
        })
    $scope.dtInstance = {}


    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),

        DTColumnBuilder.newColumn('code').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('capacity').withTitle('容量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '')
    ]

    $http.post($rootScope.project + "/api/ops/opsLayer/ext/chooseRackByLayerId.do", meta)
        .success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })

    $scope.sure = function () {

        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        var res = [];
        for (var i = 0; i < data.length; i++) {
            res.push($scope.dtOptions.aaData[data[i]])
        }
        $uibModalInstance.close(res);
    };
}


function layeraddrackCtl($compile,DTColumnBuilder,DTOptionsBuilder,$timeout, $localStorage, notify, $log, $uibModal,
                            $uibModalInstance, $scope, meta, $http, $rootScope) {
    $scope.areaOpt=[];
    $scope.areaSel={};
    $http.post($rootScope.project + "/api/ops/opsLayer/ext/selectLayer.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.areaOpt=res.data;
                if(res.data.length>0){
                    $scope.areaSel=res.data[0];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })


    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', false).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
            var acthtml = " <div class=\"btn-group\"> ";
            acthtml = acthtml+" <a href=\"javascript:void(0)\" style=\"margin-top: 3px;\" ng-click=\"remove('" + full.id + "')\" class=\"btn-white btn btn-xs\">删除</a>";
            acthtml = acthtml + "</div>";
            return acthtml;
    }
    $scope.remove = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction),
        DTColumnBuilder.newColumn('code').withTitle('编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('capacity').withTitle('容量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '')
    ]

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

    $scope.dtOptions.aaData=[]
    $scope.addrack=function(){
        var meta = {};
        meta.layerid=$scope.areaSel.id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/infrastructure/modal_addRackList.html',
            controller:addracklistCtl ,
            size: 'lg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            for(var i=0;i< result.length;i++){
                $scope.dtOptions.aaData.push(result[i])
            }
        }, function (reason) {
        });
    }

    $scope.sure = function () {
        var ps={};
        ps.layerid=$scope.areaSel.id;
        ps.items= angular.toJson($scope.dtOptions.aaData)
        $http.post($rootScope.project + "/api/ops/opsLayer/ext/addRacK.do",
            ps).success(function (res) {
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


function areaRackCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                            $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $state) {

    $scope.locOpt=[];
    $scope.locSel={};
    $scope.areaOpt=[];
    $scope.areaSel={};


    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
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
        })
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + " <button ng-click=\"row_detail('" + full.maplocid
            + "','"+full.rackid+"',"+full.rackcapacity+")\" class=\"btn-white btn btn-xs\">详情</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('areaname').withTitle('区域').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('layername').withTitle('所在位置').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('maplocname').withTitle('映射位置').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rackcode').withTitle('机柜编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rackname').withTitle('机柜名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rackcapacity').withTitle('机柜容量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('assetcnt').withTitle('当前设备数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rackmark').withTitle('机柜备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').withOption("width", '80').renderWith(renderAction)
    ]
    $scope.query = function () {
        flush();
    }

    $http.post($rootScope.project + "/api/ops/opsArea/selectList.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.areaOpt=res.data;
                if(res.data.length>0){
                    $scope.areaSel=res.data[0];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })

    $scope.$watch('areaSel', function (newValue, oldValue) {
        if (angular.isDefined(newValue) && angular.isDefined(newValue.id)) {
            $http.post(
                $rootScope.project
                + "/api/ops/opsLayer/ext/selectByAreaId.do", {areaid:newValue.id})
                .success(function (res) {
                    if (res.success) {
                        var tloc=res.data;
                        tloc.unshift({
                            id: "all",
                            name: "全部"
                        });
                        $scope.locOpt=res.data;
                        $scope.locSel=$scope.locOpt[0]

                    } else {
                        notify({
                            message: res.message
                        });
                    }
                })
        }
    });
    $scope.dtOptions.createdRow=function(nRow,aData,iDataIndex){
        if(aData.assetcnt==0){
            $(nRow).css("background-color", "#81F7BE"); //设置背景
        }
        $compile(angular.element(nRow).contents())($scope);
    }

    function flush(){
        var ps={}
        ps.areaid=$scope.areaSel.id;
        if($scope.locSel.id!="all") {
            ps.layerid=$scope.locSel.id;
        }
        $http.post(
            $rootScope.project
            + "/api/ops/opsLayer/ext/listRack.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData=res.data;

                } else {
                    notify({
                        message: res.message
                    });
                }
            })

    }


    $scope.row_detail=function(locid,rackid,capacity){
        var meta={};
        meta.locid=locid;
        meta.rackid=rackid;
        meta.capacity=capacity;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/infrastructure/modal_rackobject.html',
            controller: rackobjectCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta
                }
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



    $scope.add = function () {
        var meta = {};
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/infrastructure/modal_layerAddRack.html',
            controller:layeraddrackCtl ,
            size: 'lg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
        });
    }

    $scope.row_del = function (id) {

        $confirm({
            text: '是否删除?'
        })
            .then(
                function () {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/ops/opsLayer/ext/removeRackById.do",
                            {
                                id: id
                            })
                        .success(
                            function (res) {
                                flush()
                            })
                });
    }


};
app.register.controller('areaRackCtl', areaRackCtl);
