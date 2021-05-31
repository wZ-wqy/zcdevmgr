/*
 * 管理操作系统下的用户
 * 用户按照功能分类,用户拥有状态
 * 每个节点Ip为唯一,不可重复插入
 * 插入时候ip已有,则更新数据,否则为新的条目
 * 
 * */
function cmdbouterContactSaveCtl($timeout, $localStorage, notify, $log,
                                 $uibModal, $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.users = [];
    if (angular.isDefined(id)) {
        // 加载数据
        $http.post($rootScope.project + "/api/base/res/queryResAllById.do", {
            id: id
        }).success(function (res) {
            if (res.success) {
                $scope.users = res.data.userlist;
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function cmdbouterContactListCtl($sce, DTOptionsBuilder, DTColumnBuilder,
                                 $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                                 $localStorage) {
    var classCode = "outercontact";
    var attrCode = "contact";
    $scope.addNode = function (id) {
        var meta = {};
        var items = [{
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "100",
            placeholder: "请输入名称",
            label: "名称",
            need: true,
            name: 'name',
            ng_model: "name"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "50",
            placeholder: "请输入备注",
            label: "备注",
            need: false,
            name: 'ip',
            ng_model: "ip"
        }];
        meta = {
            footer_hide: false,
            title: "基本信息",
            item: {},
            id: id,
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.classCode = classCode;
                modal_meta.meta.item.attrCode = attrCode;
                if (!angular.isDefined(modal_meta.meta.item.ip)
                    || modal_meta.meta.item.ip == "") {
                    modal_meta.meta.item.ip = "无";
                }
                $http.post($rootScope.project + "/api/base/addResNode.do",
                    modal_meta.meta.item).success(function (res) {
                    if (res.success) {
                        modalInstance.close("OK");
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                });
            },
            init: function (modal_meta) {
                if (angular.isDefined(modal_meta.meta.id)) {
                    $http
                        .post(
                            $rootScope.project
                            + "	/api/base/res/selectById.do", {
                                id: modal_meta.meta.id
                            }).success(function (res) {
                        if (res.success) {
                            modal_meta.meta.item = res.data;
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    });
                }
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
                queryUsers();
            }
        }, function (reason) {
        });
    }
    $scope.search = "";
    $scope.statusOpt = [{
        id: "all",
        name: "全部"
    }, {
        id: "enable",
        name: "启用"
    }, {
        id: "disable",
        name: "停用"
    }]
    $scope.statusSel = $scope.statusOpt[0];
    // 自动获取配置项
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"listuser('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">用户</button>  ";
        acthtml = acthtml + " <button ng-click=\"del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('ucnt').withTitle('数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.classCode = classCode;
        $http.post($rootScope.project + "/api/base/res/queryResByNodeForUser.do",
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

    $scope.delNode = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/base/res/deleteById.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    queryUsers();
                } else {
                    notify({
                        message: res.message
                    });
                }
            });
        });
    }

    function buildHtml(udata) {
        var html = "<table id=\"udatatable\" class=\"table table-bordered\" >";
        html = html
            + "<thead style=\"background-color:#696969\"> <th>名称</th> <th>用户</th>  <th>类型</th><th>状态</th>  <th>联系方式</th> <th>备注</th> <th>账户操作</th><th>节点操作</th>  </thead>  <tbody>";
        for (var i = 0; i < udata.length; i++) {
            var userdata = udata[i].users;
            var userlength = udata[i].users.length;
            if (userlength > 0) {
                for (var j = 0; j < userdata.length; j++) {
                    html = html + "<tr>";
                    if (j == 0) {
                        html = html
                            + "<td style=\"font-weight:bold;width:165px!important;vertical-align: middle; \" rowspan=\""
                            + userlength + "\"   >" + udata[i].name
                            + "</td>";
                    }
                    html = html
                        + "<td style=\"width:80px!important; vertical-align: middle;\">"
                        + userdata[j].attr_value + "</td>";
                    html = html
                        + "<td  style=\"width:100px!important; vertical-align: middle;\" >"
                        + userdata[j].type + "</td>";
                    var statusstr = "未知";
                    if (userdata[j].status == "enable") {
                        html = html
                            + "<td  style=\"width:80px!important ; vertical-align: middle;color:green;font-weight:bold\" >启用</td>";
                    } else if (userdata[j].status == "disable") {
                        html = html
                            + "<td  style=\"width:80px!important; vertical-align: middle;color:red;font-weight:bold\" >停用</td>";
                    } else {
                        html = html
                            + "<td  style=\"width:80px!important; vertical-align: middle;color:red;font-weight:bold\" >未知</td>";
                    }
                    html = html
                        + "<td style=\"width:130px!important; vertical-align: middle;\">"
                        + userdata[j].contact + "</td>";
                    html = html
                        + "<td style=\"vertical-align: middle;\">"
                        + (userdata[j].mark == undefined ?
                            "" : userdata[j].mark) + "</td>";
                    html = html
                        + "<td style=\"font-weight:bold;width:100px!important;vertical-align: middle; \"> <div class=\"btn-group\">    <button ng-click=\"saveitem('"
                        + userdata[j].id
                        + "','"
                        + udata[i].class_id
                        + "','"
                        + udata[i].id
                        + "')\" class=\"btn-white btn btn-xs\">更新</button><button ng-click=\"delitem('"
                        + userdata[j].id
                        + "')\" class=\"btn-white btn btn-xs\">删除</button> </div></td>";
                    if (j == 0) {
                        html = html
                            + "<td rowspan=\""
                            + userlength
                            + "\" style=\"width:170px!important; vertical-align: middle;\" > <div class=\"btn-group\"><button ng-click=\"saveitem(null,'"
                            + udata[i].class_id
                            + "','"
                            + udata[i].id
                            + "')\" class=\"btn-white btn btn-xs\">新增(账户)</button>  <button ng-click=\"addNode('"
                            + udata[i].id
                            + "')\" class=\"btn-white btn btn-xs\">更新</button> <button ng-click=\"delNode('"
                            + udata[i].id
                            + "')\" class=\"btn-white btn btn-xs\">删除</button>   </div></td>";
                    }
                    html = html + "</tr>";
                }
            } else {
                html = html + "<tr>";
                html = html
                    + "<td style=\"font-weight:bold;width:170px!important\"> "
                    + udata[i].name + " </td>";
                // html = html + "<td style=\"width:50px!important\">"
                // + udata[i].ip + "</td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html
                    + "<td style=\"width:180px!important; vertical-align: middle;\" > <div class=\"btn-group\"> <button ng-click=\"saveitem(null,'"
                    + udata[i].class_id
                    + "','"
                    + udata[i].id
                    + "')\" class=\"btn-white btn btn-xs\">新增(账户)</button> <button ng-click=\"addNode('"
                    + udata[i].id
                    + "')\" class=\"btn-white btn btn-xs\">更新</button> <button ng-click=\"delNode('"
                    + udata[i].id
                    + "')\" class=\"btn-white btn btn-xs\">删除</button>   </div></td>";
                html = html + "</tr>";
            }
        }
        html = html + "</tbody></table>";
        return html;
    }

    function queryUsers() {
        var par = {};
        par.status = $scope.statusSel.id;
        par.search = $scope.search;
        par.classCode = classCode;
        par.attrCode = attrCode;
        $http.post($rootScope.project + "api/base/res/queryResAllUsers.do", par)
            .success(function (res) {
                if (res.success) {
                    var html = buildHtml(res.data);
                    var ele = $compile(html)($scope);
                    angular.element("#content_contact").html(ele);
                    // 放置与localstorage
                    if (localStorage) {
                        localStorage.setItem("cmdbcontactlistdata", html);
                    } else {
                        alert("不支持localStorage,无法使用预览功能")
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    $scope.query = function () {
        queryUsers();
    }
    $scope.querylook = function () {
        var url = "views/system/cmdb/contactlook.html";
        window.open(url, '_blank');
    }
    $scope.saveitem = function (vid, class_id, hid) {
        var meta = {};
        var items = [{
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "100",
            placeholder: "请输入用户",
            label: "用户",
            need: true,
            name: 'attrValue',
            ng_model: "attrValue"
        }, {
            type: "select",
            disabled: "false",
            label: "状态",
            need: false,
            disable_search: "true",
            dataOpt: "statusOpt",
            dataSel: "statusSel"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "300",
            placeholder: "请输入类型",
            label: "类型",
            need: true,
            name: 'type',
            ng_model: "type"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "300",
            placeholder: "请输入联系方式",
            label: "联系方式",
            need: true,
            name: 'contact',
            ng_model: "contact"
        }, {
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "300",
            placeholder: "请输入备注",
            label: "备注",
            need: false,
            name: 'mark',
            ng_model: "mark"
        }];
        meta = {
            vid: vid,
            hid: hid,
            class_id: class_id,
            footer_hide: false,
            title: "基本信息",
            item: {},
            statusOpt: [{
                id: "unknow",
                name: "未知"
            }, {
                id: "enable",
                name: "启用"
            }, {
                id: "disable",
                name: "停用"
            }],
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.status = modal_meta.meta.statusSel.id;
                $http.post(
                    $rootScope.project
                    + "/api/base/resAttrValues/insertOrUpdate.do",
                    modal_meta.meta.item).success(function (res) {
                    if (res.success) {
                        modalInstance.close("OK");
                    } else {
                        notify({
                            message: res.message
                        });
                    }
                });
            },
            init: function (modal_meta) {
                modal_meta.meta.statusSel = modal_meta.meta.statusOpt[0];
                if (angular.isDefined(modal_meta.meta.vid)
                    && modal_meta.meta.vid != null) {
                    $http
                        .post(
                            $rootScope.project
                            + "/api/base/resAttrValues/selectById.do",
                            {
                                id: modal_meta.meta.vid
                            })
                        .success(
                            function (res) {
                                if (res.success) {
                                    modal_meta.meta.item = res.data;
                                    // 处理status
                                    for (var i = 0; i < modal_meta.meta.statusOpt.length; i++) {
                                        if (modal_meta.meta.statusOpt[i].id == res.data.status) {
                                            modal_meta.meta.statusSel = modal_meta.meta.statusOpt[i];
                                            break;
                                        }
                                    }
                                } else {
                                    notify({
                                        message: res.message
                                    });
                                }
                            });
                } else {
                    modal_meta.meta.statusSel = modal_meta.meta.statusOpt[1];
                    modal_meta.meta.item.resId = modal_meta.meta.hid;
                    $http
                        .post(
                            $rootScope.project
                            + "/api/base/resClassAttrs/selectByClassIdWithAttrCode.do",
                            {
                                attrCode: attrCode,
                                classId: class_id
                            })
                        .success(
                            function (res) {
                                if (res.success) {
                                    if (res.data.length == 1) {
                                        modal_meta.meta.item.attrId = res.data[0].attrId;
                                    } else {
                                        alert("无法获取元数据");
                                    }
                                } else {
                                    notify({
                                        message: res.message
                                    });
                                }
                            });
                }
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
                queryUsers();
            }
        }, function (reason) {
        });
    }
    queryUsers();
    $scope.delitem = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(
            function () {
                $http.post(
                    $rootScope.project
                    + "/api/base/resAttrValues/deleteById.do",
                    {
                        id: id
                    }).success(function (res) {
                    if (res.success) {
                        queryUsers();
                    }
                    notify({
                        message: res.message
                    });
                })
            });
    }
    $scope.listuser = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/cmdb/modal_listUsers.html',
            controller: cmdbouterContactSaveCtl,
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
app.register.controller('cmdbouterContactListCtl', cmdbouterContactListCtl);