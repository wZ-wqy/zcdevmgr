function userRoleAdjustFormCtl($localStorage, notify, $log, $uibModal,
                               $uibModalInstance, $scope, userIds, $http, $rootScope) {
    $log.warn("window in:", userIds);
    $scope.settings = {
        bootstrap2: false,
        filterClear: '显示所有',
        filterPlaceHolder: '过滤',
        moveSelectedLabel: 'Move selected only',
        moveAllLabel: '移动所有',
        removeSelectedLabel: 'Remove selected only',
        removeAllLabel: '删除所有',
        moveOnSelect: true,
        preserveSelection: '移动',
        selectedListLabel: '已选择',
        nonSelectedListLabel: '未选择',
        postfix: '_helperz',
        selectMinHeight: 130,
        filter: true,
        filterNonSelected: '',
        filterSelected: '',
        infoAll: '显示 {0}',
        infoFiltered: '<span class="label label-warning">Filtered</span> {0} from {1}!',
        infoEmpty: '空队列!',
        filterValues: true
    };
    $scope.userRoles = []
    $http
        .post($rootScope.project + "/api/sysRoleInfo/roleQueryFormatKV.do",
            {})
        .success(
            function (res) {
                if (res.success) {
                    $scope.userRoles = res.data;
                    // 如果只有一个用户,则加载他的权限信息
                    if (angular.fromJson(userIds).length == 1) {
                        $http
                            .post(
                                $rootScope.project
                                + "/api/sysUserInfo/queryRoles.do",
                                {
                                    user_id: angular
                                        .fromJson(userIds)[0]
                                }).success(function (res) {
                            if (res.success) {
                                $scope.roleSel = res.data;
                            }
                        })
                    }
                }
            })
    $scope.roleSel = [];
    $scope.sure = function () {
        $log.warn(angular.toJson($scope.roleSel))
        if ($scope.roleSel.length == 0) {
            notify({
                message: "请至少选择一项角色"
            });
            return;
        }
        var ps = {};
        ps.userIds = userIds;
        ps.roles = angular.toJson($scope.roleSel);
        $http.post($rootScope.project + "/api/sysUserInfo/changeRoles.do", ps)
            .success(function (res) {
                if (res.success) {
                    $uibModalInstance.close("OK");
                }
                notify({
                    message: res.message
                });
            })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function userPwdFormCtl($timeout, $localStorage, notify, $log, $uibModal,
                        $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.item = {pwd1: "", pwd2: ""};
    $scope.item.user_id = id;
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/sysUserInfo/changeUserPwd.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
            }
            notify({
                message: res.message
            });
        });
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function userSaveFormCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.item = {}
    $scope.lockedOpt = [{
        id: "Y",
        name: "锁定"
    }, {
        id: "N",
        name: "正常"
    }]
    $scope.lockedSel = $scope.lockedOpt[0];
    if (!angular.isDefined(id)) {
        notify({
            message: "不存在ID"
        });
        $uibModalInstance.dismiss('cancel');
    } else {
        $http.post($rootScope.project + "/api/sysUserInfo/selectById.do", {
            id: id
        }).success(function (res) {
            $log.warn(res);
            if (res.success) {
                $scope.item = res.data;
                if (angular.isDefined(res.data.locked)) {
                    if (res.data.locked == "Y") {
                        $scope.lockedSel = $scope.lockedOpt[0];
                    } else if (res.data.locked == "N") {
                        $scope.lockedSel = $scope.lockedOpt[1];
                    }
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
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
        $scope.item.locked = $scope.lockedSel.id;
        $http.post($rootScope.project + "/api/sysUserInfo/updateById.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
            }
            notify({
                message: res.message
            });
        });
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function sysUserSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                           $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                           $stateParams) {
    //hide
    $scope.crud = {
        "update": false,
        "insert": false,
        "select": false,
        "remove": false,
        "priv": false,
        "cpwd": false
    };
    privCrudCompute($scope.crud, $rootScope.curMemuBtns);
    $scope.userGroupOpt = [];
    $scope.userGroupSel = "";
    $http.post($rootScope.project + "/api/sysUserGroup/selectList.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.userGroupOpt = prepend(res.data, {
                    groupId: "ALL",
                    name: "全部"
                });
                $scope.userGroupSel = $scope.userGroupOpt[0];
            } else {
                notify({
                    message: res.message
                });
            }
        });
    $scope.URL = $rootScope.project + "/api/sysUserInfo/selectPage.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST'
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(25)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption("paging", true)
        .withOption('bStateSave', true).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('bAutoWidth', false)
        .withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
            }).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
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

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"row_dtl('" + full.userId
            + "')\" class=\"btn-white btn btn-xs\">详细</button> </div> ";
        return acthtml;
    }

    function renderStatus(data, type, full) {

        if (angular.isDefined(full.islogoff) && full.islogoff == "1") {
            return "已注销";
        }
        var res = "正常";
        if (full.locked == "Y") {
            res = "锁定";
        }
        return res;
    }

    function renderType(data, type, full) {
        if (data == "system") {
            return "系统";
        } else if (data == "empl") {
            return "员工";
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
        DTColumnBuilder.newColumn('emplId').withTitle('员工编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('userName').withTitle('登录名').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('姓名').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tel').withTitle('手机号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('userType').withTitle('用户类型').withOption(
            'sDefaultContent', '').renderWith(renderType),
        DTColumnBuilder.newColumn('userId').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus)]

    function flush() {
        var time = new Date().getTime();
        var url = "";
        if (angular.isDefined($scope.ct)) {
            url = $rootScope.project
                + "/api/sysUserInfo/selectPage.do?none=none&ct="
                + $scope.ct;
        } else {
            url = $rootScope.project
                + "/api/sysUserInfo/selectPage.do?none=none";
        }
        if ($scope.userGroupSel.groupId != "ALL") {
            url = url + "&groupId=" + $scope.userGroupSel.groupId;
        }
        $scope.URL = url;
        var ps = {};
        ps.time = time;
        $scope.dtOptions.ajax.data = ps
        $scope.dtOptions.ajax.url = $scope.URL
        $scope.dtInstance.reloadData(callback, true);
    }

    function callback(json) {
        console.log(json)
    }

    $scope.row_dtl = function (id) {
        notify({
            message: "待开发"
        });
    }

    $scope.logoff = function () {

        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一个用户"
            });
            return;
        }
        var d = $scope.dtInstance.DataTable.context[0].json.data;
        // 批量删除
        var userids = [];
        for (var i = 0; i < data.length; i++) {
            // alert($scope.dtOptions.aaData[data[i]].USER_NO)
            userids.push(d[data[i]].userId);
        }
        $confirm({
            text: '是否注销中的用户?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/sysUserInfo/logOffByIds.do", {
                        ids: angular.toJson(userids)
                    }).success(function (res) {
                    if (res.success) {
                        flush();
                    }
                    notify({
                        message: res.message
                    });
                });
            });

    }
    $scope.del = function () {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一个用户"
            });
            return;
        }
        var d = $scope.dtInstance.DataTable.context[0].json.data;
        // 批量删除
        var userids = [];
        for (var i = 0; i < data.length; i++) {
            // alert($scope.dtOptions.aaData[data[i]].USER_NO)
            userids.push(d[data[i]].userId);
        }
        $confirm({
            text: '是否删除选中的用户?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/sysUserInfo/deleteByIds.do", {
                        ids: angular.toJson(userids)
                    }).success(function (res) {
                    if (res.success) {
                        flush();
                    }
                    notify({
                        message: res.message
                    });
                });
            });
    }
    $scope.cpwd = function () {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一个用户"
            });
            return;
        }
        if (data.length > 1) {
            notify({
                message: "只能选择一个用户"
            });
            return;
        }
        var d = $scope.dtInstance.DataTable.context[0].json.data;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/user/modal_user_pwd.html',
            controller: userPwdFormCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return d[data[0]].userId;
                }
            }
        });
        modalInstance.result.then(function (result) {
        }, function (reason) {
        });
    }
    $scope.update = function () {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一个用户"
            });
            return;
        }
        if (data.length > 1) {
            notify({
                message: "只能选择一个用户"
            });
            return;
        }
        var d = $scope.dtInstance.DataTable.context[0].json.data;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/user/modal_user_save.html',
            controller: userSaveFormCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return d[data[0]].userId;
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
    $scope.updateRole = function () {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一个用户"
            });
            return;
        }
        var d = $scope.dtInstance.DataTable.context[0].json.data;
        var userids = [];
        for (var i = 0; i < data.length; i++) {
            // alert($scope.dtOptions.aaData[data[i]].user_no)
            userids.push(d[data[i]].userId);
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/user/modal_user_role_save.html',
            controller: userRoleAdjustFormCtl,
            size: 'lg',
            resolve: {
                userIds: function () {
                    return angular.toJson(userids);
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
    $scope.detail = function () {
        notify({
            message: "待开发"
        });
    }
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/role/modal_role_save.html',
            controller: roleSaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return id;
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
app.register.controller('sysUserSettingCtl', sysUserSettingCtl);