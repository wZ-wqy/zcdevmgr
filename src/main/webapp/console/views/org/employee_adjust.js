function importEmployeeCtl($log, $uibModalInstance, notify, $scope, $http,
                           $rootScope, $uibModal, $window, $timeout, meta) {
    $scope.downTpl = function () {
        $window.open($rootScope.project + "/api/hrm/downloadEmployeeImportTpl.do");
    }
    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 2,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 1,
        maxFiles: 1,
        dictDefaultMessage: "点击上传需要上传的文件",
        acceptedFiles: ".xlsx,.xls",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        init: function () {
            $scope.myDropzone = this; // closure
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.ok = function () {
        $scope.okbtnstatus = true;
        var id = getUuid();
        if ($scope.myDropzone.files.length > 0) {
            $scope.myDropzone.options.url = $rootScope.project
                + '/api/file/fileupload.do?uuid=' + id
                + '&bus=file&interval=10000&bus=file';
            $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
        } else {
            notify({
                message: "请选择文件"
            });
            $scope.okbtnstatus = false;
            return;
        }
        $timeout(function () {
            $http.post(
                $rootScope.project
                + "/api/hrm/employeeBatchAdd.do", {
                    id: id
                }).success(function (res) {
                $scope.okbtnstatus = false;
                if (res.success) {
                    $scope.myDropzone.removeAllFiles(true);
                    $uibModalInstance.close('OK');
                } else {
                }
                notify({
                    message: res.message
                });
            })
        }, 3000);
    }
}

// function orgEmpSavePartCtl($rootScope, $scope, $timeout, $log) {
//
// }
function orgEmpSaveCtl(spnode,$timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, id, $http, $rootScope, partOpt, $timeout) {
    $scope.spOpt=spnode;
    $scope.spSel=[];
    $scope.partOpt = []
    $scope.partSel = []
    $scope.$watch('partSel', function () {
        $log.info('partSel change');
        $rootScope.sys_partSel = $scope.partSel;
    }, true);
    // 获取列表
    $scope.$watch(function () {
        return $rootScope.sys_partOpt;
    }, function () {
        // $log.info("wath sys_partOpt change.", $rootScope.sys_partOpt);
        if (angular.isDefined($rootScope.sys_partOpt)) {
            $scope.partOpt = $rootScope.sys_partOpt;
            if ($scope.partOpt.length > 0) {
                $scope.partSel.push($scope.partOpt[0]);
            }
        }
    }, true);
    $scope.$watch(function () {
        return $rootScope.sys_partSelItem;
    }, function () {
        var parts = $rootScope.sys_partSelItem;
        if (angular.isDefined(parts)) {
            // $log.info("wath sys_partSelItem change.",
            // $scope.partOpt.length,$rootScope.sys_partSelItem, parts.length);
            if (parts.length == 0) {
            } else {
                var partsSel = [];
                $timeout(function () {
                    for (var i = 0; i < parts.length; i++) {
                        for (var j = 0; j < $scope.partOpt.length; j++) {
                            if ($scope.partOpt[j].node_id == parts[i].node_id) {
                                $log.info("match");
                                partsSel.push($scope.partOpt[j]);
                                break;
                            }
                        }
                    }
                    if (partsSel.length > 0) {
                        $scope.partSel = partsSel;
                    }
                }, 300);
            }
        }
    }, true);
    $scope.data = {};
    $scope.hrmstatusOpt = [{id: "online", name: "在职"}, {id: "offline", name: "离职"}];
    $scope.hrmstatusSel = $scope.hrmstatusOpt[0];
    $scope.posOpt = [];
    $scope.posSel = {}
    $timeout(function () {
        var d = angular.copy(partOpt)
        d.splice(0, 1);
        $rootScope.sys_partOpt = d;
    }, 800);
    $rootScope.sys_partSel;
    $http.post($rootScope.project + "/api/hrm/hrmPosition/listPositions.do", {}).success(function (res) {
        $scope.posOpt = res.data;
        if (angular.isDefined(id)) {
            // 加载数据
            $http.post($rootScope.project + "/api/hrm/employeeQueryById.do", {
                empl_id: id
            }).success(function (res) {
                if (res.success) {
                    $scope.data = res.data;
                    $scope.data.OLD_PARTS = res.data.PARTS;
                    $timeout(function () {
                        $rootScope.sys_partSelItem = res.data.PARTS
                    }, 500);
                    if (res.data.hrmstatus) {
                        if (res.data.hrmstatus == "online") {
                            $scope.hrmstatusSel = $scope.hrmstatusOpt[0];
                        } else if (res.data.hrmstatus == "offline") {
                            $scope.hrmstatusSel = $scope.hrmstatusOpt[1];
                        }
                    }
                    for (var i = 0; i < $scope.posOpt.length; i++) {
                        if ($scope.posOpt[i].id == res.data.fposition) {
                            $scope.posSel = $scope.posOpt[i];
                            break;
                        }
                    }

                    var sp=angular.fromJson(res.data.approval);
                    for(var i=0;i<$scope.spOpt.length;i++){
                       for(var j=0;j<sp.length;j++){
                           if(sp[j].id==$scope.spOpt[i].id){
                               $scope.spSel.push($scope.spOpt[i]);
                               break;
                           }
                       }
                    }
                    if (!angular.isDefined($scope.posSel.id)) {
                        if (res.data.length > 0) {
                            $scope.posSel = $scope.posOpt[0];
                        }
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
        } else {
            if (res.data.length > 0) {
                $scope.posSel = $scope.posOpt[0];
            }
            $rootScope.sys_partSelItem = [];
        }
    });
    // $timeout(function () {
    //     var modal = document.getElementsByClassName('modal-body');
    //     for (var i = 0; i < modal.length; i++) {
    //         var adom = modal[i].getElementsByClassName('chosen-container');
    //         for (var j = 0; j < adom.length; j++) {
    //             console.log(j);
    //             adom[i].style.width = "100%";
    //         }
    //     }
    // }, 200);
    $scope.sure = function () {
        // 跨越controller获取数据数据
        if (!angular.isDefined($rootScope.sys_partSel)) {
            notify({
                message: "发生系统错误"
            });
            return;
        }
        if ($rootScope.sys_partSel.length == 0) {
            notify({
                message: "至少选择一个组织"
            });
            return;
        }
        if ($rootScope.sys_partSel.length > 3) {
            notify({
                message: "最多只可选三个组织"
            });
            return;
        }
        // 检查姓名
        if (!angular.isDefined($scope.data.name)) {
            notify({
                message: "请输入姓名"
            });
            return;
        }
        $scope.data.hrmstatus = $scope.hrmstatusSel.id;
        $scope.data.nodes = angular.toJson($rootScope.sys_partSel);
        $scope.data.fposition = $scope.posSel.id;


        $scope.data.approval=angular.toJson($scope.spSel);
        var cmd = "";
        if (angular.isDefined($scope.data.empl_id)) {
            cmd = "/api/hrm/employeeUpdate.do"
            $scope.data.old_nodes = angular.toJson($scope.data.old_parts)
        } else {
            cmd = "/api/hrm/employeeAdd.do";
        }
        console.log($scope.data)
        $http.post($rootScope.project + cmd, $scope.data).success(
            function (res) {
                notify({
                    message: res.message
                });
                if (res.success) {
                    $uibModalInstance.close("OK");
                }
            })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                console.log(j);
                adom[i].style.width = "100%";
            }
        }
    }, 100);
}

function orgEmpAdjustCtl($stateParams, DTOptionsBuilder, DTColumnBuilder,
                         $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.data = {
        name: ""
    };
    $scope.partOpt = [];
    $scope.partSel = "";
    $scope.crud = {
        "update": false,
        "insert": false,
        "remove": false
    }
    var pbtns = $rootScope.curMemuBtns;

    var spnode=[];
    $http.post($rootScope.project + "/api/base/sysApprovalNode/selectList.do", {})
        .success(function (res) {
            if (res.success) {
                spnode = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })

    privCrudCompute($scope.crud, pbtns);
    $http.post($rootScope.project + "/api/hrm/orgQueryLevelList.do", {})
        .success(function (res) {
            if (res.success) {
                var d = res.data;
                d.splice(0, 0, {
                    "routename": "全部",
                    node_id: "-1",
                    levels: 0
                })
                $scope.partOpt = d;
                $scope.partSel = $scope.partOpt[0];
                flush();
            } else {
                notify({
                    message: res.message
                });
            }
        })
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withPaginationType(
        'full_numbers').withDisplayLength(25).withOption("ordering", false)
        .withOption("responsive", false).withOption("searching", false)
        .withOption("paging", false).withOption('bStateSave', true)
        .withOption('bProcessing', true).withOption('bFilter', false)
        .withOption('bInfo', true).withOption('serverSide', false)
        .withOption('bAutoWidth', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        if ($scope.crud.update) {
            acthtml = acthtml + " <button ng-click=\"save('" + full.empl_id
                + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        }
        if ($scope.crud.remove) {
            acthtml = acthtml + " <button ng-click=\"row_del('" + full.empl_id
                + "')\" class=\"btn-white btn btn-xs\">删除</button>  ";
        }
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var res = "无效";
        if (full.is_action == "Y") {
            res = "有效";
        }
        return res;
    }

    function renderHrmstatus(data, type, full) {
        var res = "在职";
        if (data == "online") {
            res = "在职";
        } else if (data == "offline") {
            res = "离职";
        } else {
            res = "";
        }
        return res;
    }

    function renderApproval(data, type, full) {

       var data_arr=angular.fromJson(data);
       var html="";
       if(angular.isDefined(data_arr)){
           for(var i=0;i<data_arr.length;i++){
               html=html+data_arr[i].node+ " ";
           }
       }

       return html;
    }


    $scope.dtColumns = [
        DTColumnBuilder.newColumn('empl_id').withTitle('员工编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('姓名').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tel').withTitle('手机号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('fposname').withTitle('岗位').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('hrmstatus').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderHrmstatus),
        DTColumnBuilder.newColumn('approval').withTitle('审批节点').withOption(
            'sDefaultContent', '').renderWith(renderApproval),
        DTColumnBuilder.newColumn('route_name').withTitle('所属组织').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        $scope.data.node_id = $scope.partSel.node_id;
        $http.post($rootScope.project + "/api/hrm/employeeQueryList.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }

    $scope.row_detail = function (id) {
    }
    $scope.row_del = function (id) {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        $confirm({
            text: '是否删除功能?'
        }).then(function () {
            $http.post($rootScope.project + "/api/hrm/employeeDelete.do", {
                empl_id: id
            }).success(function (res) {
                if (res.success) {
                    flush();
                }
                notify({
                    message: res.message
                });
            })
        });
    }
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/org/modal_employee_save.html',
            controller: orgEmpSaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return id;
                },
                spnode:function () {
                    return spnode;
                },
                partOpt: function () {
                    return $scope.partOpt;
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
    $scope.batchimport = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/org/modal_importEmployee.html',
            controller: importEmployeeCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
        });
    }
};
app.register.controller('orgEmpAdjustCtl', orgEmpAdjustCtl);

