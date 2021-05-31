/*
 * 管理操作系统下的用户
 * 用户按照功能分类,用户拥有状态
 * 每个节点Ip为唯一,不可重复插入
 * 插入时候ip已有,则更新数据,否则为新的条目
 * 
 * */
function cmdblistUserCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, id, $http, $rootScope) {
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

function cmdbUserListCtl($sce, DTOptionsBuilder, DTColumnBuilder, $compile,
                         $confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
                         $localStorage) {
    var classCode = "xtlist";
    var attrCode = "userlist";
    $scope.custExec = function () {
        //根据用户修改
        //status:enable|disable
        //type:admin,app,db,yw
        //update  res_attr_values set status='enable' where attr_value='jinjie';
        var meta = {};
        var items = [{
            type: "textarea",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "1000",
            placeholder: "请输入sql",
            label: "SQL",
            need: true,
            name: 'sql',
            ng_model: "sql"
        }]
        meta = {
            footer_hide: false,
            title: "基本信息",
            item: {},
            items: items,
            sure: function (modalInstance, modal_meta) {
                $http.post($rootScope.project + "/api/base/res/batchWork.do",
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
            required: true,
            maxlength: "50",
            placeholder: "请输入ip",
            label: "Ip",
            need: true,
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
    $scope.typeOpt = [{
        id: "all",
        name: "全部"
    },
        {
            id: "work",
            name: "全部(db_admin_oper_app)"
        },
        {
            id: "admin",
            name: "管理员"
        }, {
            id: "yw",
            name: "运维账户"
        }, {
            id: "db",
            name: "数据库"
        }, {
            id: "inter",
            name: "内置"
        }, {
            id: "unknow",
            name: "未知"
        }]
    $scope.typeSel = $scope.typeOpt[1];
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
    $scope.statusSel = $scope.statusOpt[1];
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

    //
    var t = {};
    t.ip = "10.18.1.211";
    t.name = "测试主机2啊2ad";
    var u = [];
    u.push({
        user: "root",
        status: "enable",
        act: "delete"
    });
    u.push({
        user: "tomcat123",
        status: "enable",
        act: "update"
    });
    t.list = u;
    var p = {};
    p.data = angular.toJson(t);
    $http.post($rootScope.project + "/api/base/addUserBySingleNode.do", p)
        .success(function (res) {
            if (res.success) {
                $scope.dtOptions.aaData = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })
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
            + "<thead style=\"background-color:#696969\"> <th>名称</th> <th>IP</th><th>用户</th>  <th>类型</th><th>状态</th> <th>备注</th> <th>账户操作</th><th>节点操作</th>  </thead>  <tbody>";
        for (var i = 0; i < udata.length; i++) {
            var userdata = udata[i].users;
            var userlength = udata[i].users.length;
            if (userlength > 0) {
                for (var j = 0; j < userdata.length; j++) {
                    html = html + "<tr>";
                    if (j == 0) {
                        html = html
                            + "<td style=\"font-weight:bold;width:255px!important;vertical-align: middle; \" rowspan=\""
                            + userlength + "\"   >" + udata[i].name
                            + "</td>";
                        html = html
                            + "<td style=\"width:50px!important;vertical-align: middle;\" rowspan=\""
                            + userlength + "\">" + udata[i].ip + "</td>";
                    }
                    html = html
                        + "<td style=\"width:60px!important; vertical-align: middle;\">"
                        + userdata[j].attr_value + "</td>";
                    var typestr = "未知";
                    if (userdata[j].type == "admin") {
                        html = html
                            + "<td  style=\"width:100px!important; vertical-align: middle;\" >管理员</td>";
                    } else if (userdata[j].type == "app") {
                        html = html
                            + "<td  style=\"width:100px!important ; vertical-align: middle;\" >应用账户</td>";
                    } else if (userdata[j].type == "db") {
                        html = html
                            + "<td  style=\"width:100px!important ; vertical-align: middle;\" >数据库账户</td>";
                    } else if (userdata[j].type == "yw") {
                        html = html
                            + "<td  style=\"width:100px!important; vertical-align: middle;\" >运维人员</td>";
                    } else if (userdata[j].type == "inter") {
                        html = html
                            + "<td  style=\"width:100px!important; vertical-align: middle;\" >内置账户</td>";
                    } else {
                        html = html
                            + "<td  style=\"width:100px!important; vertical-align: middle;\" >未知</td>";
                    }
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
                        + "<td style=\"vertical-align: middle;\">"
                        + (userdata[j].mark == undefined ? ""
                            : userdata[j].mark) + "</td>";
                    html = html
                        + "<td style=\"font-weight:bold;width:90px!important;vertical-align: middle; \"> <div class=\"btn-group\">    <button ng-click=\"saveitem('"
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
                            + "<td  rowspan=\""
                            + userlength
                            + "\"  style=\"width:170px!important; vertical-align: middle;\" > <div class=\"btn-group\"><button ng-click=\"saveitem(null,'"
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
                    + "<td style=\"font-weight:bold;width:165px!important\"> "
                    + udata[i].name + " </td>";
                html = html + "<td style=\"width:50px!important\">"
                    + udata[i].ip + "</td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html
                    + "<td style=\"width:170px!important; vertical-align: middle;\" > <div class=\"btn-group\"> <button ng-click=\"saveitem(null,'"
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
        par.type = $scope.typeSel.id;
        par.search = $scope.search;
        par.classCode = classCode;
        par.attrCode = attrCode;
        $http.post($rootScope.project + "/api/base/res/queryResAllUsers.do", par)
            .success(function (res) {
                if (res.success) {
                    var html = buildHtml(res.data);
                    var ele = $compile(html)($scope);
                    angular.element("#content_user").html(ele);
                    // 放置与localstorage
                    if (localStorage) {
                        localStorage.setItem("cmdbuserlistdata", html);
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
        var url = "views/system/cmdb/userlook.html";
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
            type: "select",
            disabled: "false",
            label: "类型",
            need: false,
            disable_search: "true",
            dataOpt: "typeOpt",
            dataSel: "typeSel"
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
            statusSel: "",
            typeOpt: [{
                id: "admin",
                name: "管理员"
            }, {
                id: "app",
                name: "应用用户"
            }, {
                id: "db",
                name: "数据库用户"
            }, {
                id: "yw",
                name: "运维人员"
            }, {
                id: "inter",
                name: "内置"
            }],
            typeSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                modal_meta.meta.item.status = modal_meta.meta.statusSel.id;
                modal_meta.meta.item.type = modal_meta.meta.typeSel.id;
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
                modal_meta.meta.typeSel = modal_meta.meta.typeOpt[0];
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
                                    // 处理status
                                    for (var i = 0; i < modal_meta.meta.typeOpt.length; i++) {
                                        if (modal_meta.meta.typeOpt[i].id == res.data.type) {
                                            modal_meta.meta.typeSel = modal_meta.meta.typeOpt[i];
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
            controller: cmdblistUserCtl,
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
app.register.controller('cmdbUserListCtl', cmdbUserListCtl);