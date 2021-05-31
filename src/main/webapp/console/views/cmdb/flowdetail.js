function flowdetailCtl($location, $timeout, $localStorage, notify, $log, $uibModal, $window,
                       $scope, $http, $rootScope, DTOptionsBuilder,
                       DTColumnBuilder, $compile) {
    var id = $location.search().id;
    var pagetype = $location.search().pagetype;
    var taskid = $location.search().taskid;
    //pagetype:approval,lookup
    $scope.ctl = {};
    //资产列表
    $scope.ctl.areazclisthide = true;
    //审批区域
    $scope.ctl.areaspprocesshide = true;
    //意见区域
    $scope.ctl.areasuggesthide = false;
    //流程图
    $scope.ctl.areaspflowchart = true;
    var url = "";
    $scope.url = "";
    $scope.spsugguest = [];
    $scope.spsuggest = "";
    $scope.pagetype = pagetype;
    if ($scope.pagetype == "approval") {
        $scope.ctl.areaspprocesshide = false;
    } else if ($scope.pagetype == "lookup") {
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withDOM('frtlpi').withPaginationType('simple').withDisplayLength(
            50).withOption("ordering", false).withOption("responsive",
            false).withOption("searching", false).withOption('scrollY',
            300).withOption('scrollX', true).withOption(
            'bAutoWidth', true).withOption('scrollCollapse', true)
        .withOption('paging', false).withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withoutselect');
    $scope.dtOptions.aaData = [];
    $http
        .post($rootScope.project + "/api/zc/selectBillById.do", {
            id: id
        })
        .success(
            function (res) {
                if (res.success) {
                    $scope.data = res.data;
                    //显示资产列表
                    if ($scope.data.bustype == "LY" || $scope.data.bustype == "JY" || $scope.data.bustype == "DB") {
                        $scope.ctl.areazclisthide = false;
                        $scope.dtOptions.aaData = res.data.items;
                    }
                    //#########################是否有审批流程,获取审批记录及流程图
                    if (angular.isDefined(res.data.processinstanceid) && res.data.processinstanceid != "") {
                        $http
                            .post(
                                $rootScope.project
                                + "/api/flow/loadProcessInstanceData.do",
                                {
                                    processInstanceId: res.data.processinstanceid
                                })
                            .success(
                                function (res) {
                                    if (res.success) {
                                        $scope.spsugguest = res.data;
                                    } else {
                                        notify({
                                            message: res.message
                                        });
                                    }
                                })
                        //设置审批流程图
                        url = $rootScope.project + "uflo/diagram?processInstanceId=" + res.data.processinstanceid;
                    }
                    //############################填冲表单数据
                    var dynamicData = res.data.formdata;
                    let vm;
                    $timeout(function () {
                        var jd = decodeURI(res.data.formconf);
                        if (angular.isDefined(jd) && jd != "undefined" && jd != "") {
                            let jsonData = angular.fromJson(jd);
                            vm = new Vue({
                                el: '#app',
                                data: {
                                    jsonData,
                                    dynamicData
                                },
                                mounted() {
                                    this.init()
                                },
                                methods: {
                                    init() {
                                        this.$refs.kfb.setData(angular.fromJson(res.data.formdata));
                                    },
                                    handleSubmit(p) {
                                    },
                                    getData() {
                                    }
                                }
                            })
                        }
                    }, 1000)
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    $scope.agreen = function () {
        if ($scope.spsuggest.length == 0) {
            $scope.spsuggest = "同意";
        }
        if ($scope.spsuggest.length > 248) {
            notify({
                message: "已超过248,请缩减字数。"
            });
            return;
        }
        $http
            .post($rootScope.project + "/api/zc/flow/completeTask.do",
                {taskId: taskid, opinion: $scope.spsuggest}).success(function (res) {
            if (res.success) {
                window.opener = "ok";
                window.close();
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.rollback = function () {
        if ($scope.spsuggest.length == 0) {
            $scope.spsuggest = "退回";
        }
        if ($scope.spsuggest.length > 248) {
            notify({
                message: "已超过248,请缩减字数。"
            });
            return;
        }
        $http
            .post($rootScope.project + "/api/zc/flow/rollback.do",
                {taskId: taskid, opinion: $scope.spsuggest}).success(function (res) {
            if (res.success) {
                window.opener = "ok";
                window.close();
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.refuse = function () {
        if ($scope.spsuggest.length == 0) {
            $scope.spsuggest = "拒绝";
        }
        if ($scope.spsuggest.length > 248) {
            notify({
                message: "已超过248,请缩减字数。"
            });
            return;
        }
        $http
            .post($rootScope.project + "/api/zc/flow/refuseTask.do",
                {taskId: taskid, opinion: $scope.spsuggest}).success(function (res) {
            if (res.success) {
                window.opener = "ok";
                window.close();
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
    // $scope.submit = function () {
    //     if ($scope.spsuggest.length == 0) {
    //         $scope.spsuggest = "重新提交审批";
    //     }
    //     if ($scope.spsuggest.length > 248) {
    //         notify({
    //             message: "已超过248,请缩减字数。"
    //         });
    //         return;
    //     }
    //     $scope.data.taskId = taskid;
    //     $scope.data.opinion = $scope.spsuggest;
    //     $http
    //         .post($rootScope.project + "/api/cmdb/flow/zc/completeStartTask.do",
    //             $scope.data).success(function (res) {
    //         if (res.success) {
    //           //  $uibModalInstance.close('OK');
    //         } else {
    //             notify({
    //                 message: res.message
    //             });
    //         }
    //     })
    // }
    $scope.selectFlow = function () {
        if (angular.isDefined(url)) {
            $scope.ctl.areaspflowchart = false;
            $scope.url = url;
        }
    }
}

app.register.controller('flowdetailCtl', flowdetailCtl);