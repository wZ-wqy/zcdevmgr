function sysTaskCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [
            {
                id: "2",
                label: "刷新",
                type: "btn",
                show: true,
                template: ' <button ng-click="flush()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            }
            // , {
            //     id: "3",
            //     label: "清除缓存",
            //     type: "btn",
            //     show: true,
            //     template: ' <button ng-click="cacheclear()" class="btn btn-sm btn-primary" type="submit">清除缓存</button>'
            // }
        ]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"enable('" + full.seq + "')\" class=\"btn-white btn btn-xs\">启用</button>  ";
        acthtml = acthtml + " <button ng-click=\"disable('" + full.seq + "')\" class=\"btn-white btn btn-xs\">停用</button>  ";
        acthtml = acthtml + " <button ng-click=\"pause('" + full.seq + "')\" class=\"btn-white btn btn-xs\">暂停</button>  ";
        acthtml = acthtml + " <button ng-click=\"resume('" + full.seq + "')\" class=\"btn-white btn btn-xs\">恢复</button>  ";
        acthtml = acthtml + " <button ng-click=\"runonce('" + full.seq + "')\" class=\"btn-white btn btn-xs\">执行一次</button> </div> ";
        return acthtml;
    }

    function renderStatus(data, type, full) {
        var jobenableimg = '<img src="img/sm/unknow.png"/>';
        if (data == 'true') {
            jobenableimg = '<img src="img/sm/green.png"/>';
        } else if (data == 'false') {
            jobenableimg = '<img src="img/sm/grey.png"/>';
        }
        return jobenableimg;
    }

    function renderRunStatus(data, type, full) {
        var value = data
        if (data == 'NORMAL') {
            value = '正常'
        } else if (data == 'PAUSED') {
            value = '暂停'
        }
        return value;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('jobname').withTitle('任务名称').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('jobcontent').withTitle('内容').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('jobenable').withTitle('初始化').withOption('sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('jobrunstatus').withTitle('运行状态').withOption('sDefaultContent', '').renderWith(renderRunStatus),
        DTColumnBuilder.newColumn('last_run').withTitle('最后运行').withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption('sDefaultContent', '').withClass('none'), ,
        DTColumnBuilder.newColumn('seq').withTitle('操作').withOption('sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/schedule/queryJobs.do", ps).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }

    flush();
    $scope.flush = function () {
        flush();
    }
    $scope.save = function () {
        alert("待开发");
    }
    $scope.del = function (seq) {
        alert("待开发");
    }
    $scope.enable = function (seq) {
        $http.post($rootScope.project + "/api/schedule/enablejob.do", {
            seq: seq
        }).success(function (res) {
            notify({
                message: res.message
            });
            if (res.success) {
                flush();
            }
        })
    }
    $scope.disable = function (seq) {
        $http.post($rootScope.project + "/api/schedule/disablejob.do", {
            seq: seq
        }).success(function (res) {
            notify({
                message: res.message
            });
            if (res.success) {
                flush();
            }
        })
    }
    $scope.pause = function (seq) {
        $http.post($rootScope.project + "/api/schedule/pausejob.do", {
            seq: seq
        }).success(function (res) {
            notify({
                message: res.message
            });
            if (res.success) {
                flush();
            }
        })
    }
    $scope.resume = function (seq) {
        $http.post($rootScope.project + "/api/schedule/resumejob.do", {
            seq: seq
        }).success(function (res) {
            notify({
                message: res.message
            });
            if (res.success) {
                flush();
            }
        })
    }
    $scope.runonce = function (seq) {
        $http.post($rootScope.project + "/api/schedule/runonce.do", {
            seq: seq
        }).success(function (res) {
            if (res.success) {
                flush();
            }
            notify({
                message: res.message
            });
        })
    }
};
app.register.controller('sysTaskCtl', sysTaskCtl);