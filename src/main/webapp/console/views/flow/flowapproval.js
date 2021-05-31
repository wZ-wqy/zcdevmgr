function flowapporvalSaveCtl($timeout, $localStorage, notify, $log, $uibModal, meta,
                              $uibModalInstance, $scope, $http, $rootScope, DTOptionsBuilder,
                              DTColumnBuilder, $compile) {

    $scope.mobileOpt=[{id:"1",name:"需要"},{id:"0",name:"不需要"}];
    $scope.mobileSel=$scope.mobileOpt[0];

    $scope.pcOpt=[{id:"1",name:"需要"},{id:"0",name:"不需要"}];
    $scope.pcSel=$scope.pcOpt[0];

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.data = {};
    if (angular.isDefined(meta.id)) {
        $http.post($rootScope.project + "/api/base/sysApprovalBusi/selectById.do", {id: meta.id})
            .success(function (res) {
                if (res.success) {
                    $scope.data = res.data;

                    for(var i=0;i<$scope.pcOpt.length;i++){
                        if($scope.data.webapproval==$scope.pcOpt[i].id){
                            $scope.pcSel=$scope.pcOpt[i];
                            break;
                        }
                    }

                    for(var i=0;i<$scope.mobileOpt.length;i++){
                        if($scope.data.mobileapproval==$scope.mobileOpt[i].id){
                            $scope.mobileSel=$scope.mobileOpt[i];
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

    $scope.sure = function () {

        $scope.data.webapproval=$scope.mobileSel.id;
        $scope.data.mobileapproval=$scope.pcSel.id;
        $http.post($rootScope.project + "/api/base/sysApprovalBusi/insertOrUpdate.do", $scope.data)
            .success(function (res) {
                if (res.success) {
                    $uibModalInstance.close("OK");
                }
                notify({
                    message: res.message
                });
            })
    };

}

function flowapporvalCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                        $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                        $stateParams) {
    $scope.crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
        "priv": false,
        "cpwd": false
    };
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
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
        });
    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }

    function renderType(data, type, full) {
        if (data == "system") {
            return "系统";
        } else if (data == "user") {
            return "用户";
        } else {
            return data;
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('code').withTitle('业务编码').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('流程名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('webapproval').withTitle('PC审批').withOption(
            'sDefaultContent', '').renderWith(
            function (data, type, full) {
                if (angular.isDefined(data)) {
                    if(data=="1"){
                        return "需要审批"
                    }else if(data=="0"){
                        return "不需要审批"
                    }else{
                        return data;
                    }
                }
            }),
        DTColumnBuilder.newColumn('mobileapproval').withTitle('移动端审批').withOption(
            'sDefaultContent', '').renderWith(
            function (data, type, full) {
                if (angular.isDefined(data)) {
                    if(data=="1"){
                        return "需要审批"
                    }else if(data=="0"){
                        return "不需要审批"
                    }else{
                        return data;
                    }
                }
            }),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', '')
    ]

    function flush() {
        $http.post($rootScope.project + "/api/base/sysApprovalBusi/selectList.do", {})
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
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

    // $scope.remove = function () {
    //     var item = getSelectRow();
    //     if (angular.isDefined(item) && angular.isDefined(item.id)) {
    //         $confirm({
    //             text: '是否删除选中列?'
    //         }).then(
    //             function () {
    //                 $http.post(
    //                     $rootScope.project
    //                     + "/api/flow/sysProcessSetting/deleteById.do", {
    //                         id: item.id
    //                     }).success(function (res) {
    //                     if (res.success) {
    //                         flush();
    //                     }
    //                     notify({
    //                         message: res.message
    //                     });
    //                 });
    //             });
    //     }
    // }
    $scope.query = function () {
        flush();
    }

    function save(id) {
        var ps = {};
        ps.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/flow/modal_flowapprovalSave.html',
            controller: flowapporvalSaveCtl,
            size: 'lg',
            resolve: {
                meta: function () {
                    return ps;
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

    $scope.add = function () {
        save();
    }
    $scope.edit = function () {
        var item = getSelectRow();
        if (angular.isDefined(item) && angular.isDefined(item.id)) {
            save(item.id);
        }
    }
    flush();
};
app.register.controller('flowapporvalCtl', flowapporvalCtl);