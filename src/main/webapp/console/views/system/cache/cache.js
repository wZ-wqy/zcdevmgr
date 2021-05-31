function sysCacheCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                     $log, notify, $scope, $http, $rootScope, $uibModal, $stateParams) {
    $scope.meta = {
        tools: [
            {
                id: "1",
                label: "缓存域",
                type: "select",
                disablesearch: true,
                dataOpt: [],
                dataSel: "",
                width: 300,
                show: true,
            },
            {
                id: "2",
                label: "查询",
                type: "btn",
                show: true,
                template: ' <button ng-click="flush()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            }, {
                id: "3",
                label: "清除缓存",
                type: "btn",
                show: true,
                template: ' <button ng-click="cacheclear()" class="btn btn-sm btn-primary" type="submit">清除缓存</button>'
            }
            , {
                id: "4",
                label: "一键清除缓存",
                type: "btn",
                show: true,
                template: ' <button ng-click="cacheclearall()" class="btn btn-sm btn-primary" type="submit">一键清除缓存</button>'
            }]
    }
    var gcacheOpt = [];
    $http.post($rootScope.project + "/api/sysApi/system/queryCacheName.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.meta.tools[0].dataOpt = res.data;
                gcacheOpt = res.data;
                if (res.data.length > 0) {
                    $scope.meta.tools[0].dataSel = res.data[0]
                    flush();
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}
    var crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
    };
    var pbtns = $rootScope.curMemuBtns;
    privCrudCompute(crud, pbtns);

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"refresh('" + full.key + "','"
            + full.cache
            + "')\" class=\"btn-white btn btn-xs\">刷新</button>   ";
        if (crud.remove) {
            acthtml = acthtml + " <button ng-click=\"removeCacheKey('"
                + full.key
                + "','" + full.cache + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
        }
        acthtml = acthtml + " <button ng-click=\"queryKeyValue('"
            + full.key
            + "','" + full.cache + "')\" class=\"btn-white btn btn-xs\">查看</button> ";
        acthtml = acthtml + " </div>";
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
        DTColumnBuilder.newColumn('key').withTitle('Key').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('ttl').withTitle('TTL').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tti').withTitle('TTI').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('hit').withTitle('命中次数').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('ctime').withTitle('创建时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('accesstime').withTitle('访问时间')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('expiretime').withTitle('过期时间')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('key').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        $http.post($rootScope.project + "/api/sysApi/system/queryCacheKeys.do",
            {
                cache: $scope.meta.tools[0].dataSel.id
            }).success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }

    $scope.flush = function () {
        flush();
    }
    $scope.save = function () {
        alert("待开发");
    }
    $scope.del = function (seq) {
        alert("待开发");
    }
    $scope.queryKeyValue = function (key, cache) {
        $http.post($rootScope.project + "/api/sysApi/system/queryCacheKeyValue.do",
            {
                key: key,
                cache: cache
            }).success(function (res) {
            if (res.success) {
                alert(res.data);
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.removeCacheKey = function (key, cache) {
        $http.post($rootScope.project + "/api/sysApi/system/removeCacheKey.do",
            {
                key: key,
                cache: cache
            }).success(function (res) {
            notify({
                message: res.message
            });
            if (res.success) {
                flush();
            }
        })
    }
    $scope.refresh = function (key, cache) {
        $http.post($rootScope.project + "/api/sysApi/system/refreshCache.do", {
            key: key,
            cache: cache
        }).success(function (res) {

            if (res.success) {
                flush();
            }
        })
    }
    $scope.cacheclearall = function () {
        $confirm({
            text: '清除缓存的动作将在后台触发,是否清除所有缓存(不包含shiro及wxconf)?'
        }).then(function () {
            if (gcacheOpt.length > 0) {
                for (var i = 0; i < gcacheOpt.length; i++) {
                    if (gcacheOpt[i].id == "passwordRetryCache" || gcacheOpt[i].id == "authorizationCache" || gcacheOpt[i].id == "authenticationCache" || gcacheOpt[i].id == "wxconf") {
                        continue;
                    }
                    $http.post($rootScope.project + "/api/sysApi/system/clearCache.do",
                        {cache: gcacheOpt[i].id}).success(function (res) {
                        if (res.success) {
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    });
                }
            }
        });
    }
    $scope.cacheclear = function () {
        var meta = {};
        var items = [{
            type: "select",
            disabled: "false",
            label: "缓存域",
            need: false,
            disable_search: "true",
            dataOpt: "cacheOpt",
            dataSel: "cacheSel"
        }];
        meta = {
            footer_hide: false,
            title: "清除缓存",
            item: {},
            cacheOpt: gcacheOpt,
            cacheSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                if (angular.isDefined(modal_meta.meta.cacheSel.id)) {
                    var cache = modal_meta.meta.cacheSel.id;
                    $http.post($rootScope.project + "/api/sysApi/system/clearCache.do",
                        {cache: cache}).success(function (res) {
                        if (res.success) {
                            modalInstance.close("OK");
                        } else {
                        }
                        notify({
                            message: res.message
                        });
                    });
                } else {
                    notify({
                        message: "请选择缓存域"
                    });
                }
            },
            init: function (modal_meta) {
            }
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/Template/modal_simpleForm.html',
            controller: modal_simpleFormCtl,
            size: 'lg',
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
app.register.controller('sysCacheCtl', sysCacheCtl);