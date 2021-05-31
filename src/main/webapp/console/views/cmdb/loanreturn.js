function GetDateNowId() {
    var vNow = new Date();
    var sNow = "";
    sNow += String(vNow.getFullYear());
    sNow += String(vNow.getMonth() + 1);
    sNow += String(vNow.getDate());
    sNow += String(vNow.getHours());
    sNow += String(vNow.getMinutes());
    sNow += String(vNow.getSeconds());
    sNow += String(vNow.getMilliseconds());
    return sNow;
}



function ghSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                   $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                   DTColumnBuilder, $compile, $confirm) {
    $scope.item = {};
    $scope.ctl = {ghprocessuser: false};
    $scope.processuserOpt = meta.dict.partusers;
    $scope.processuserSel = "";
    if ($scope.processuserOpt.length > 0) {
        $scope.processuserSel = $scope.processuserOpt[0];
        if (angular.isDefined($rootScope.dt_sys_user_info)) {
            for (var i = 0; i < $scope.processuserOpt.length; i++) {
                if ($rootScope.dt_sys_user_info.userId == $scope.processuserOpt[i].user_id) {
                    $scope.processuserSel = $scope.processuserOpt[i];
                    $scope.ctl.ghprocessuser = true;
                    break;
                }
            }
        }
    }
    $scope.date = {
        rreturndate: moment()
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.sure = function () {
        $scope.item.rreturndate = $scope.date.rreturndate.format('YYYY-MM-DD');
        $scope.item.rprocessuserid = $scope.processuserSel.user_id;
        $scope.item.rprocessusername = $scope.processuserSel.name;
        $scope.item.bustype = "gh";
        $scope.item.id = meta.id;
        $confirm({
            text: '是否确定归还?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resLoanreturn/ext/zcgh.do",
                    $scope.item).success(function (res) {
                    if (res.success) {
                        $uibModalInstance.close('OK');
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.review = function () {
        var mdata = {};
        mdata.id = "";
        mdata.type = "many";
        mdata.datarange = "TK";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_common_zclist.html',
            controller: modal_common_ZcListCtl,
            size: 'blg',
            resolve: {
                data: function () {
                    return mdata
                }
            }
        });
        modalInstance.result.then(function (result) {
            $scope.dtOptions.aaData = result;
        }, function (reason) {
            $log.log("reason", reason)
        });
    }
}

function loanreturnCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $window,
                       $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var gdict = {};
    var dicts = "devdc";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "N",
            zccatused: "N",
            uid: "zcjydata"
        })
        .success(
            function (res) {
                if (res.success) {
                    gdict = res.data;
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    // 分类
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

    function renderAction(data, type, full) {
        //分配，删除，详情
        if (data == "wait") {
            return "维修中"
        } else if (data == "finish") {
            return "已完成"
        } else {
            return data;
        }
    }

    function renderCheckStatus(data, type, full) {
        if (data == "init") {
            return "等待校验"
        } else if (data == "success") {
            return "成功"
        } else if (data == "failed") {
            return "失败"
        } else {
            return data;
        }
    }

    function renderCGStatus(data, type, full) {
        if (data == "wait") {
            return "未处理"
        } else if (data == "cancel") {
            return "取消"
        } else if (data == "success") {
            return "完成"
        } else if (data == "failed") {
            return "失败"
        } else {
            return data;
        }
    }

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('"
            + full.busuuid
            + "','" + full.status + "','" + full.bustype + "')\" class=\"btn-white btn btn-xs\">单据详请</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }



    function renderBusStatus(data, type, full) {
        if (data == "JY") {
            return "借用"
        } else if (data == "GH") {
            return "归还"
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
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction),
        DTColumnBuilder.newColumn('busuuid').withTitle('单据编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('单据名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('status').withTitle('办理状态').withOption(
            'sDefaultContent', '').renderWith(renderZCSPStatus),
        DTColumnBuilder.newColumn('busstatus').withTitle('业务状态').withOption(
            'sDefaultContent', '').renderWith(renderBusStatus),
        DTColumnBuilder.newColumn('lrusername').withTitle('借用人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('lruserorginfo').withTitle('借用人所属组织').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('busdatestr').withTitle('借用时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('returndatestr').withTitle('预计归还时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rreturndatestr').withTitle('实际归还时间').withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('lprocessusername').withTitle('借用处理人').withOption(
        //     'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('rprocessusername').withTitle('归还处理人').withOption(
        //     'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('借用备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('rmark').withTitle('归还备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createusername').withTitle('创建人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "input",
                show: true,
                label: "内容",
                placeholder: "输入搜索内容",
                type: "input",
                ct: ""
            },
            {
                id: "btn1",
                label: "",
                type: "btn",
                show: true,
                priv: "select",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">借用</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="gh()" class="btn btn-sm btn-primary" type="submit">归还</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="cancel()" class="btn btn-sm btn-primary" type="submit">取消</button>'
            },{
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="approval()" class="btn btn-sm btn-primary" type="submit">送审</button>'
            }, {
                id: "btn4",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="print()" class="btn btn-sm btn-primary" type="submit">打印</button>'
            }
            ]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        $http
            .post($rootScope.project + "/api/zc/resLoanreturn/ext/selectList.do",
                ps).success(function (res) {
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

    $scope.detail = function (uuid, status, type) {
        meta.uuid = uuid;
        var ps = {}
        ps.busid = uuid;
        ps.status = status;
        ps.type = type;
        ps.flowpagetype = "lookup";
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_jyghlist.html',
            controller: zcjyghlistCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
    }

    function action(id) {
        var meta = {};
        meta.id = id;
        meta.dict = gdict;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_loanreturnSave.html',
            controller: loanSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            flush();
        }, function (reason) {
            $log.log("reason", reason)
        });
    }

    $scope.add = function () {
        action();
    }
    $scope.gh = function () {
        var row = getSelectRow();
        if (angular.isDefined(row)) {
            console.log(row);
            if(row.busstatus=="GH"){
                notify({
                    message: "当前状态不能操作"
                });
                return ;
            }
            if(!(row.status=="success"||row.status=="finish"||row.status=="finish_na")){
                notify({
                    message: "当前状态不能操作"
                });
                return ;
            }
            var meta = {};
            meta.dict = gdict;
            meta.id = row.id;
            var modalInstance = $uibModal.open({
                backdrop: true,
                templateUrl: 'views/cmdb/modal_ghSave.html',
                controller: ghSaveCtl,
                size: 'blg',
                resolve: {
                    meta: function () {
                        return meta;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                flush();
            }, function (reason) {
                $log.log("reason", reason)
            });
        }
    }
    flush();
    function getSelectPrintRows() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 1000) {
            notify({
                message: "不允许超过1000个"
            });
            return;
        } else {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                res.push($scope.dtOptions.aaData[data[i]].busuuid)
            }
            return angular.toJson(res);
        }
    }

    function downloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "assetsjy-" + moment().format('L') + '.zip';
        a.href = URL.createObjectURL(file);
        a.click();
        const tempA = document.getElementById('tempId');
        if (tempA) {
            tempA.parentNode.removeChild(tempA);
        }
    }

    $scope.print=function(){
        var selrows = getSelectPrintRows();
        if (angular.isDefined(selrows)) {
            var ps = {}
            ps.data = selrows;
            $http.post($rootScope.project + "/api/zc/resLoanreturn/ext/print.do", {
                data: selrows
            }, {
                responseType: 'arraybuffer'
            }).success(function (data) {
                var blob = new Blob([data], {
                    type: "application/vnd.ms-excel"
                });
                downloadFile(blob);
            })
        } else {
            return;
        }
    }

    $scope.cancel = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            var id = selrow.id;
            $confirm({
                text: '是否取消?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/zc/resLoanreturn/ext/cancel.do", {
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

        } else {
            return;
        }
    }


    $scope.approval = function () {
        var item = getSelectRow();
        if (angular.isDefined(item)) {
            item.ptype = item.busstatus;
            if (item.status != "apply") {
                notify({
                    message: "该状态不允许送审"
                });
                return;
            }
            $http.post(
                $rootScope.project
                + "/api/base/sysApprovalBusi/ext/selectByCode.do", {
                    code: "JY"
                }).success(function (res) {
                if (res.success) {
                    if(res.data.webapproval=="0"){
                        $confirm({
                            text: '借用确认?'
                        }).then(
                            function () {
                                var pps={};
                                pps.formtype = "none";
                                pps.ptype = item.ptype
                                pps.title = item.name;
                                pps.busid = item.busuuid;
                                pps.ifsp="0";
                                pps.formtype = "none";
                                $http.post(
                                    $rootScope.project
                                    + "/api/zc/flow/startAssetFlow.do", pps).success(function (res) {
                                    if (res.success) {
                                        flush();
                                    } else {
                                        notify({
                                            message: res.message
                                        });
                                    }
                                });
                            });
                    }else{
                        var modalInstance = $uibModal.open({
                            backdrop: true,
                            templateUrl: 'views/cmdb/flow/modal_chosenFlowTreeView.html',
                            controller: chosenFlowTreeCtl,
                            size: 'blg',
                            resolve: {
                                meta: function () {
                                    return item;
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
                } else {
                    notify({
                        message: res.message
                    });
                }
            })





        }
    }
};
app.register.controller('flowapprovalCommonCtl', flowapprovalCommonCtl);
app.register.controller('flowsuggestCommonCtl', flowsuggestCommonCtl);
app.register.controller('loanreturnCtl', loanreturnCtl);
