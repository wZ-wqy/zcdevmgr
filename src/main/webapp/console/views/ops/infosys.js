function loadOptNode(modal_meta, gdicts) {
    var item = modal_meta.meta.item;
    // 类型
    if (angular.isDefined(gdicts.sysinfotype) && gdicts.sysinfotype.length > 0) {
        modal_meta.meta.typeOpt = gdicts.sysinfotype;
        if (angular.isDefined(item) && angular.isDefined(item.type)) {
            for (var i = 0; i < modal_meta.meta.typeOpt.length; i++) {
                if (modal_meta.meta.typeOpt[i].dict_item_id == item.type) {
                    modal_meta.meta.typeSel = modal_meta.meta.typeOpt[i];
                }
            }
        } else {
        }
    }
    //
    if (angular.isDefined(gdicts.sysinfograde) && gdicts.sysinfograde.length > 0) {
        modal_meta.meta.gradeOpt = gdicts.sysinfograde;
        if (angular.isDefined(item) && angular.isDefined(item.grade)) {
            for (var i = 0; i < modal_meta.meta.gradeOpt.length; i++) {
                if (modal_meta.meta.gradeOpt[i].dict_item_id == item.grade) {
                    modal_meta.meta.gradeSel = modal_meta.meta.gradeOpt[i];
                }
            }
        } else {
        }
    }
    if (angular.isDefined(gdicts.sysinfoops) && gdicts.sysinfoops.length > 0) {
        modal_meta.meta.opsmethodOpt = gdicts.sysinfoops;
        if (angular.isDefined(item) && angular.isDefined(item.opsmethod)) {
            for (var i = 0; i < modal_meta.meta.opsmethodOpt.length; i++) {
                if (modal_meta.meta.opsmethodOpt[i].dict_item_id == item.opsmethod) {
                    modal_meta.meta.opsmethodSel = modal_meta.meta.opsmethodOpt[i];
                }
            }
        } else {
        }
    }
    if (angular.isDefined(gdicts.sysinfodev) && gdicts.sysinfodev.length > 0) {
        modal_meta.meta.devmethodOpt = gdicts.sysinfodev;
        if (angular.isDefined(item) && angular.isDefined(item.devmethod)) {
            for (var i = 0; i < modal_meta.meta.devmethodOpt.length; i++) {
                if (modal_meta.meta.devmethodOpt[i].dict_item_id == item.devmethod) {
                    modal_meta.meta.devmethodSel = modal_meta.meta.devmethodOpt[i];
                }
            }
        } else {
        }
    }
}

function sysinfomgrCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                       $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var pbtns = $rootScope.curMemuBtns;
    var gdicts = {};
    var dicts = "sysinfodev,sysinfograde,sysinfoops,sysinfotype";
    $http.post($rootScope.project + "/api/zc/queryDictFast.do", {
        dicts: dicts,
        uid: "infosys"
    }).success(function (res) {
        if (res.success) {
            gdicts = res.data;
            flush();
        } else {
            notify({
                message: res.message
            });
        }
    })
    var setTabHeight=getDtTabHeight(500,450);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50).withOption("ordering", false).withOption(
            "responsive", false).withOption("searching", true)
        .withOption('scrollY', setTabHeight).withOption('scrollX', true)
        .withOption('bAutoWidth', true).withOption('scrollCollapse', true)
        .withOption('paging', true).withOption('bStateSave', true).withOption('bProcessing', false)
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
        }).withButtons([{
            extend: 'colvis',
            text: '显示/隐藏列',
            columns: ':gt(0)',
            columnText: function (dt, idx, title) {
                return (idx ) + ': ' + title;
            }
        }, {
            extend: 'csv',
            text: 'Excel(当前页)',
            exportOptions: {
                columns: ':visible',
                trim: true,
                modifier: {
                    page: 'current'
                }
            }
        }, {
            extend: 'print',
            text: '打印',
            exportOptions: {
                columns: ':visible',
                stripHtml: false,
                columns: ':visible',
                modifier: {
                    page: 'current'
                }
            }
        }]);

    function stateChange(iColumn, bVisible) {
    }

    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml)
        .withClass('select-checkbox checkbox_center').renderWith(
            function () {
                return ""
            }));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('name').withTitle('名称')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('gradestr').withTitle('评级')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('ifmain').withTitle('是否重要')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('typestr').withTitle('类型')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('rto').withTitle('RTO')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('rpo').withTitle('RPO')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('devmethodstr').withTitle('开发模式')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('opsmethodstr').withTitle('运维模式')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('sameplacebkmethod')
        .withTitle('同城备份').withOption('sDefaultContent', '').withOption(
            "width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('diffplacebkmethod')
        .withTitle('异地备份').withOption('sDefaultContent', '').withOption(
            "width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('tcontact').withTitle('技术联系')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('bcontact').withTitle('业务联系')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('bpart').withTitle('项目所属')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('lastdrilldate').withTitle('最近演练')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('bkmethod').withTitle('备份')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('os').withTitle('操作系统')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('db').withTitle('数据库')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('app').withTitle('应用')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('hardware').withTitle('硬件')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('ondatestr').withTitle('上线时间')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns
        .push(DTColumnBuilder.newColumn('downdatestr').withTitle('下线时间')
            .withOption('sDefaultContent', '')
            .withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('label1').withTitle('标签1')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('label2').withTitle('标签2')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "insert",
                template: ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">新增</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "update",
                template: ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: false,
                priv: "remove",
                template: ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: false,
                priv: "exportfile",
                template: ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
            }
//				,
//				{
//					id : "btn4",
//					label : "",
//					type : "btn",
//					show : false,
//					priv : "importfile",
//					template : ' <button ng-click="importfile()" class="btn btn-sm btn-primary" type="submit">导入数据</button>'
//				}
        ],
        tools: [{
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入名称",
            type: "input",
            ct: ""
        }]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.toolsbtn, pbtns);

    function flush() {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $http.post($rootScope.project + "/api/ops/opsNodeInfosys/ext/selectList.do",
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

    $scope.filedown = function () {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $window.open($rootScope.project
            + "/api/ops/opsNodeInfosys/ext/selectListExport.do?search="
            + ps.search);
    }

    function getSelectRows() {
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
                res.push($scope.dtOptions.aaData[data[i]].id)
            }
            return angular.toJson(res);
        }
    }

    $scope.del = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/ops/opsNodeInfosys/deleteById.do", {
                            id: selrow.id
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
        }
    }
    $scope.detail = function () {
        var id = "";
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            id = selrow.id;
        } else {
            return;
        }
        var ps = {};
        ps.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_dtl.html',
            controller: modalcmdbdtlCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
            }
        }, function (reason) {
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

    // //////////////////////////save/////////////////////
    $scope.save = function (type) {
        var meta = {};
        var items = [];
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "500",
            placeholder: "请输入名称",
            label: "名称",
            need: true,
            name: 'name',
            ng_model: "name"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: true,
            maxlength: "500",
            placeholder: "请输入名称",
            label: "介绍",
            need: true,
            name: 'about',
            ng_model: "about"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入名称",
            label: "是否重要系统",
            need: false,
            name: 'ifmain',
            ng_model: "ifmain"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "类型",
            need: false,
            disable_search: "false",
            dataOpt: "typeOpt",
            dataSel: "typeSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "评级",
            need: false,
            disable_search: "true",
            dataOpt: "gradeOpt",
            dataSel: "gradeSel"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入RTO",
            label: "RTO",
            need: false,
            name: 'rto',
            ng_model: "rto"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入RPO",
            label: "RPO",
            need: false,
            name: 'rpo',
            ng_model: "rpo"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入技术联系内容",
            label: "技术联系",
            need: false,
            name: 'tcontact',
            ng_model: "tcontact"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入业务联系内容",
            label: "业务联系",
            need: false,
            name: 'bcontact',
            ng_model: "bcontact"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入系统所属",
            label: "系统所属",
            need: false,
            name: 'bpart',
            ng_model: "bpart"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "运维模式",
            need: false,
            disable_search: "true",
            dataOpt: "opsmethodOpt",
            dataSel: "opsmethodSel"
        });
        items.push({
            type: "select",
            disabled: "false",
            label: "开发模式",
            need: false,
            disable_search: "true",
            dataOpt: "devmethodOpt",
            dataSel: "devmethodSel"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入同城内容",
            label: "同城",
            need: false,
            name: 'sameplacebkmethod',
            ng_model: "sameplacebkmethod"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入异地内容",
            label: "异地",
            need: false,
            name: 'diffplacebkmethod',
            ng_model: "diffplacebkmethod"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入操作系统",
            label: "操作系统",
            need: false,
            name: 'os',
            ng_model: "os"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入应用",
            label: "应用",
            need: false,
            name: 'app',
            ng_model: "app"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入数据库",
            label: "数据库",
            need: false,
            name: 'db',
            ng_model: "db"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入硬件",
            label: "硬件",
            need: false,
            name: 'hardware',
            ng_model: "hardware"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入上线时间",
            label: "上线时间",
            need: false,
            name: 'ondatestr',
            ng_model: "ondatestr"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入下线时间",
            label: "下线时间",
            need: false,
            name: 'downdatestr',
            ng_model: "downdatestr"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入最近演练",
            label: "最近演练",
            need: false,
            name: 'lastdrilldate',
            ng_model: "lastdrilldate"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入备份类型",
            label: "备份类型",
            need: false,
            name: 'bkmethod',
            ng_model: "bkmethod"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入标签",
            label: "标签1",
            need: false,
            name: 'label1',
            ng_model: "label1"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入标签",
            label: "标签2",
            need: false,
            name: 'label2',
            ng_model: "label2"
        });
        items.push({
            type: "input",
            disabled: "false",
            sub_type: "text",
            required: false,
            maxlength: "500",
            placeholder: "请输入备注",
            label: "备注",
            need: false,
            name: 'mark',
            ng_model: "mark"
        });
        var id;
        var itemvalue = {
            mark: "",
            pwdmark: "",
            leader: "",
            ip: "",
            nodebackup: "",
            label1: "",
            label2: "",
            name: ""
        };
        // type 1 更新
        if (type == 1) {
            var selrow = getSelectRow();
            angular.copy(selrow, itemvalue);
            if (angular.isDefined(itemvalue) && angular.isDefined(itemvalue.id)) {
                id = itemvalue.id;
            } else {
                return;
            }
        }
        meta = {
            footer_hide: false,
            title: "" + $state.router.globals.current.data.pageTitle,
            item: itemvalue,
            opsmethodOpt: [],
            opsmethodSel: "",
            gradeOpt: [],
            gradeSel: "",
            typeOpt: [],
            typeSel: "",
            devmethodOpt: [],
            devmethodSel: "",
            items: items,
            sure: function (modalInstance, modal_meta) {
                if (angular.isDefined(modal_meta.meta.typeSel.dict_item_id)) {
                    modal_meta.meta.item.type = modal_meta.meta.typeSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.gradeSel.dict_item_id)) {
                    modal_meta.meta.item.grade = modal_meta.meta.gradeSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.opsmethodSel.dict_item_id)) {
                    modal_meta.meta.item.opsmethod = modal_meta.meta.opsmethodSel.dict_item_id
                }
                if (angular.isDefined(modal_meta.meta.devmethodSel.dict_item_id)) {
                    modal_meta.meta.item.devmethod = modal_meta.meta.devmethodSel.dict_item_id
                }
                $http.post(
                    $rootScope.project
                    + "/api/ops/opsNodeInfosys/insertOrUpdate.do",
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
                var gdicts2 = {};
                angular.copy(gdicts, gdicts2)
                loadOptNode(modal_meta, gdicts2);
            }
        }
        // 打开静态框
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
app.register.controller('sysinfomgrCtl', sysinfomgrCtl);