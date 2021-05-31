
function renderName(data, type, full) {
    var html = full.model;
    return html;
}

function renderBusCat(data, type, full) {
    var html = data;
    if (angular.isDefined(data)) {
        if (data == "LY") {
            html = "资产领用";
        } else if (data == "TK") {
            html = "资产退库"
        } else if (data == "JY") {
            html = "资产借用"
        } else if (data == "BX") {
            html = "资产报销"
        } else if (data == "ZY") {
            html = "资产转移"
        } else if (data == "WX") {
            html = "资产维修"
        } else if (data == "BF") {
            html = "资产报废"
        } else if (data == "DB") {
            html = "资产调拨"
        }
    }
    return html;
}

function renderZCSPStatus(data, type, full) {
    var html = data;
    if (angular.isDefined(data)) {
        if (data == "submitforapproval" || data == "apply") {
            html = "<span style='color:#33FFFF; font-weight:bold'>待送审</span>";
        } else if (data == "inapproval") {
            html = "<span style='color:#00F; font-weight:bold'>审批中</span>";
        } else if (data == "running") {
            html = "<span style='color:#00F; font-weight:bold'>审批中</span>";
        } else if (data == "success") {
            html = "<span style='color:green; font-weight:bold'>审批成功</span>";
        } else if (data == "failed") {
            html = "<span style='color:red;font-weight:bold'>审批失败</span>";
        } else if (data == "cancel") {
            html = "<span style='color:red;font-weight:bold'>取消</span>";
        } else if (data == "cancel") {
            html = "<span style='color:red;font-weight:bold'>审批取消</span>"
        } else if (data == "finish") {
            html = "<span style='color:green;font-weight:bold'>流程结束</span>"
        } else if (data == "finish_na") {
            html = "<span style='color:green;font-weight:bold'>办理完成(未审批)</span>"
        } else if (data == "rollback") {
            html = "<span style='color:red;font-weight:bold'>审批退回</span>";
        } else if (data == "finish") {
            html = "<span style='color:red;font-weight:bold'>流程结束</span>";
        } else {
            html = data;
        }
    }
    return html;
}

function renderZcRecycle(data, type, full) {
    if (full.inprocess == "1" && angular.isDefined(full.inprocesstype)) {
        if (full.inprocesstype == "LY") {
            data = data + "(领用流程中)";
        } else if (full.inprocesstype == "TK") {
            data = data + "(退库流程中)";
        } else if (full.inprocesstype == "JY") {
            data = data + "(借用流程中)";
        } else if (full.inprocesstype == "BF") {
            data = data + "(报废流程中)";
        } else if (full.inprocesstype == "DB") {
            data = data + "(调拨流程中)";
        } else if (full.inprocesstype == "MYZY") {
            data = data + "(转移流程中)";
        } else {
            data = data + "(流程中)";
        }
    }
    if (full.recycle == "inuse") {
        return "<span style=\"color:green;font-weight:bold\">" + data + "</span>";
    } else if (full.recycle == "idle") {
        return "<span style=\"color:#8A2BE2;font-weight:bold\">" + data + "</span>";
    } else if (full.recycle == "scrap" || full.recycle == "stopuse") {
        return "<span style=\"color:red;font-weight:bold\">" + data + "</span>";
    } else if (full.recycle == "repair") {
        return "<span style=\"color:blue;font-weight:bold\">" + data + "</span>";
    } else {
        return data;
    }
}

function renderZcLoc(data, type, full) {
    var html = "";
    if (angular.isDefined(full.rackstr)) {
        html = html + full.rackstr;
    }
    if (angular.isDefined(full.frame)) {
        html = html + "[" + full.frame + "]"
    }
    if (angular.isDefined(full.locdtl)) {
        html = html + " " + full.locdtl
    }
    return "<span style=\"color:purple;font-weight:bold\">" + html + "</span>"
}

function zcBaseColsHCCreate(DTColumnBuilder, selectype) {
    //selectype:withoutselect,withselect
    dtColumns = [];
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    dtColumns = [];
    if (selectype == "withselect") {
        dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }));
    }
    // dtColumns.push(DTColumnBuilder.newColumn('classrootname').withTitle('类目').withOption(
    //     'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('单据编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('ctid').withTitle('物品编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('物品类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('ctmodel').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('ctunit').withTitle('计量单位').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('ctbrandmark').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('供应商').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('ctdowncnt').withTitle('安全库存下限').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('ctupcnt').withTitle('安全库存上限').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('batchno').withTitle('批次号').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle('所属公司').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('存放区域').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('warehousestr').withTitle('仓库').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购单价')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('lastinventorytimestr').withTitle('最近盘点')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('fs1').withTitle('标签1').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('fs2').withTitle('标签2').withOption(
        'sDefaultContent', ''));
    return dtColumns;
}



//##########################################财务变更处理窗口##########################################//
function zccgcwSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                       DTColumnBuilder, $compile, $confirm) {
    $scope.ctl = {processuser: false};
    $scope.item = {};

    function selectnall() {
        $scope.item.tbelongcompstatus = false;
        $scope.item.tbelongpartstatus = false;
        $scope.item.tbuypricestatus = false;
        $scope.item.tnetworthstatus = false;
        $scope.item.tnetworthstatus = false;
        $scope.item.taccumulatedstatus = false;
        $scope.item.tresidualvaluestatus = false;
    }

    function selectall() {
        $scope.item.tbelongcompstatus = true;
        $scope.item.tbelongpartstatus = true;
        $scope.item.tbuypricestatus = true;
        $scope.item.tnetworthstatus = true;
        $scope.item.tnetworthstatus = true;
        $scope.item.taccumulatedstatus = true;
        $scope.item.tresidualvaluestatus = true;
    }

    selectnall();
    $scope.selectall = function () {
        selectall();
    }
    $scope.selectnall = function () {
        selectnall();
    }
    $scope.adminuserOpt = meta.dict.partusers;
    $scope.adminuserSel = "";
    if ($scope.adminuserOpt.length > 0) {
        $scope.adminuserSel = $scope.adminuserOpt[0];
        if (angular.isDefined($rootScope.dt_sys_user_info)) {
            for (var i = 0; i < $scope.adminuserOpt.length; i++) {
                if ($rootScope.dt_sys_user_info.userId == $scope.adminuserOpt[i].user_id) {
                    $scope.adminuserSel = $scope.adminuserOpt[i];
                    $scope.ctl.processuser = true;
                    break;
                }
            }
        }
    }
    $scope.belongcompOpt = meta.dict.belongcomp;
    $scope.belongcompSel = "";
    if ($scope.belongcompOpt.length > 0) {
        $scope.belongcompSel = $scope.belongcompOpt[0];
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
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
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span>   ";
        acthtml = acthtml + " <span ng-click=\"filldata('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">填充</span>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function fillresdata(id) {
        var data = {};
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                data = $scope.dtOptions.aaData[i];
                break;
            }
        }
        $scope.belongcompSel = "";
        $scope.item.tbuyprice = "";
        $scope.item.tnetworth = "";
        $scope.item.tresidualvalue = "";
        $scope.item.taccumulateddepreciation = "";
        if (angular.isDefined(data.net_worth)) {
            $scope.item.tnetworth = data.net_worth;
        }
        if (angular.isDefined(data.buy_price)) {
            $scope.item.tbuyprice = data.buy_price;
        }
        if (angular.isDefined(data.residualvalue)) {
            $scope.item.tresidualvalue = data.residualvalue;
        }
        if (angular.isDefined(data.accumulateddepreciation)) {
            $scope.item.taccumulateddepreciation = data.accumulateddepreciation;
        }
        if (angular.isDefined(data.belong_company_id)) {
            for (var i = 0; i < $scope.belongcompOpt.length; i++) {
                if (data.belong_company_id == $scope.belongcompOpt[i].id) {
                    $scope.belongcompSel = $scope.belongcompOpt[i];
                    break;
                }
            }
        }
    }

    $scope.filldata = function (id) {
        fillresdata(id);
    }
    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '100').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购单价')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn("net_worth").withTitle('资产净值')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn("accumulateddepreciation").withTitle('累计折旧')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('residualvalue').withTitle('设置残值')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('lastdepreciationdatestr').withTitle('最近折旧时间')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('part_name').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        $scope.item.tbelongcomp = $scope.belongcompSel.id;
        $scope.item.processuserid = $scope.adminuserSel.user_id;
        $scope.item.processusername = $scope.adminuserSel.name;
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        if ($scope.item.tbuypricestatus) {
            if (angular.isDefined($scope.item.tbuyprice)) {
            } else {
                notify({
                    message: "请输入采购单价"
                });
                return;
            }
        }
        if ($scope.item.tnetworthstatus) {
            if (angular.isDefined($scope.item.tnetworth)) {
            } else {
                notify({
                    message: "请输入资产净值"
                });
                return;
            }
        }
        if ($scope.item.tresidualvaluestatus) {
            if (angular.isDefined($scope.item.tresidualvalue)) {
            } else {
                notify({
                    message: "请输入残值"
                });
                return;
            }
        }
        if ($scope.item.taccumulatedstatus) {
            if (angular.isDefined($scope.item.taccumulateddepreciation)) {
            } else {
                notify({
                    message: "请输入累计折扣"
                });
                return;
            }
        }
        $confirm({
            text: '是否确定变更?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resCFinance/ext/insert.do",
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
        mdata.datarange = "CG";
        mdata.showusefullife = "true";
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
    //是否ids传入
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                    if (res.data.length == 1) {
                        fillresdata(res.data[0].id);
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}

//##########################################基本信息变更处理窗口##########################################//
function zccgjbSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                       DTColumnBuilder, $compile, $confirm) {
    $scope.ctl = {processuser: false};
    $scope.item = {};

    function selectnall() {
        $scope.item.tclassidstatus = false;
        $scope.item.tmodelstatus = false;
        $scope.item.tsnstatus = false;
        $scope.item.tzcsourcestatus = false;
        $scope.item.tzccntstatus = false;
        $scope.item.tsupplierstatus = false;
        $scope.item.tbrandstatus = false;
        $scope.item.tbuytimestatus = false;
        $scope.item.tlocstatus = false;
        $scope.item.tusefullifestatus = false;
        $scope.item.tusedcompanyidstatus = false;
        $scope.item.tpartidstatus = false;
        $scope.item.tuseduseridstatus = false;
        $scope.item.tsnstatus = false;
        $scope.item.tlabel1status = false;
        $scope.item.tunitstatus = false;
        $scope.item.tconfdescstatus = false;
        $scope.item.tlocdtlstatus = false;
        $scope.item.tmarkstatus = false;
        $scope.item.tfd1status = false;
        $scope.item.tfs20status = false;
        $scope.item.tnamestatus=false;

        $scope.item.tlabel2status=false;
        $scope.item.tbatchstatus=false;
        $scope.item.tbelongcompstatus=false;


    }

    function selectall() {
        $scope.item.tnamestatus=true;
        $scope.item.tclassidstatus = true;
        $scope.item.tmodelstatus = true;
        $scope.item.tsnstatus = true;
        $scope.item.tzcsourcestatus = true;
        $scope.item.tzccntstatus = true;
        $scope.item.tsupplierstatus = true;
        $scope.item.tbrandstatus = true;
        $scope.item.tbuytimestatus = true;
        $scope.item.tlocstatus = true;
        $scope.item.tusefullifestatus = true;
        $scope.item.tusedcompanyidstatus = true;
        $scope.item.tpartidstatus = true;
        $scope.item.tuseduseridstatus = true;
        $scope.item.tsnstatus = true;
        $scope.item.tlabel1status = true;
        $scope.item.tunitstatus = true;
        $scope.item.tconfdescstatus = true;
        $scope.item.tlocdtlstatus = true;
        $scope.item.tmarkstatus = true;
        $scope.item.tfd1status = true;
        $scope.item.tfs20status = true;

        $scope.item.tlabel2status=true;
        $scope.item.tbatchstatus=true;
        $scope.item.tbelongcompstatus=true;
    }

    selectnall();
    $scope.selectall = function () {
        selectall();
    }
    $scope.selectnall = function () {
        selectnall();
    }
    $scope.classOpt = meta.dict.btype;
    $scope.classSel = "";
    if ($scope.classOpt.length > 0) {
        $scope.classSel = $scope.classOpt[0];
    }
    $scope.supplierOpt = meta.dict.zcsupper;
    $scope.supplierSel = "";
    if ($scope.supplierOpt.length > 0) {
        $scope.supplierSel = $scope.supplierOpt[0];
    }
    $scope.brandOpt = meta.dict.devbrand;
    $scope.brandSel = "";
    if ($scope.brandOpt.length > 0) {
        $scope.brandSel = $scope.brandOpt[0];
    }
    $scope.usefullifeOpt = meta.dict.zcusefullife
    $scope.usefullifeSel = ""
    if ($scope.usefullifeOpt.length > 0) {
        $scope.usefullifeSel = $scope.usefullifeOpt[0];
    }
    $scope.zcsourceOpt = meta.dict.zcsource;
    $scope.zcsourceSel = "";
    if ($scope.zcsourceOpt.length > 0) {
        $scope.zcsourceSel = $scope.zcsourceOpt[0];
    }
    $scope.locOpt = meta.dict.devdc;
    $scope.locSel = "";
    if ($scope.locOpt.length > 0) {
        $scope.locSel = $scope.locOpt[0];
    }
    $scope.belongcompOpt = meta.dict.comp;
    $scope.belongcompSel = {};
    if ($scope.belongcompOpt.length > 0) {
        $scope.belongcompSel = $scope.belongcompOpt[0];
    }
    $scope.usedpartOpt = meta.dict.parts;
    $scope.usedpartSel = {};
    if ($scope.usedpartOpt.length > 0) {
        $scope.usedpartSel = $scope.usedpartOpt[0];
    }
    $scope.useduserOpt = meta.dict.partusers;
    $scope.useduserSel = "";
    if ($scope.useduserOpt.length > 0) {
        $scope.useduserSel = $scope.useduserOpt[0];
    }
    $scope.adminuserOpt = meta.dict.partusers;
    $scope.adminuserSel = "";
    if ($scope.adminuserOpt.length > 0) {
        $scope.adminuserSel = $scope.adminuserOpt[0];
        if (angular.isDefined($rootScope.dt_sys_user_info)) {
            for (var i = 0; i < $scope.adminuserOpt.length; i++) {
                if ($rootScope.dt_sys_user_info.userId == $scope.adminuserOpt[i].user_id) {
                    $scope.adminuserSel = $scope.adminuserOpt[i];
                    $scope.ctl.processuser = true;
                    break;
                }
            }
        }
    }
    $scope.date = {
        buytime: moment(),
        fd1time: moment()
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
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
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <span ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</span>   ";
        acthtml = acthtml + " <span ng-click=\"filldata('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">填充</span>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    $scope.delitem = function (id) {
        var del = 0;
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                del = i;
                break;
            }
        }
        $scope.dtOptions.aaData.splice(del, 1);
    }
    dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
        'sDefaultContent', '').withOption("width", '100').renderWith(renderZCAction));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('fs20').withTitle('其他编号').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('classfullname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('zcsourcestr').withTitle('来源').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('供应商').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    // dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
    //     'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    dtColumns.push(DTColumnBuilder.newColumn('part_fullname').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('存放区域').withOption(
        'sDefaultContent', '').renderWith(renderDTFontColoPurpleH));
    dtColumns.push(DTColumnBuilder.newColumn('locdtl').withTitle('位置').withOption(
        'sDefaultContent', '').renderWith(renderZcLoc));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('buy_timestr').withTitle('采购日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('fs1').withTitle('标签1').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('fs2').withTitle('标签2').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('lastinventorytimestr').withTitle('最近盘点')
        .withOption('sDefaultContent', ''));
    // dtColumns.push(DTColumnBuilder.newColumn('classrootname').withTitle('类目').withOption(
    //     'sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns = dtColumns;
    $scope.dtOptions.aaData = [];
    $scope.sure = function () {
        if ($scope.dtOptions.aaData.length == 0) {
            notify({
                message: "请选择资产"
            });
            return;
        }
        if ($scope.item.tmodelstatus) {
            if (angular.isDefined($scope.item.tmodel) && $scope.item.tmodel.length > 0) {
            } else {
                notify({
                    message: "请输入型号"
                });
                return;
            }
        }
        $scope.item.processuserid = $scope.adminuserSel.user_id;
        $scope.item.processusername = $scope.adminuserSel.name;
        $scope.item.tbuytime = $scope.date.buytime.format('YYYY-MM-DD');
        $scope.item.tfd1 = $scope.date.fd1time.format('YYYY-MM-DD');
        $scope.item.items = angular.toJson($scope.dtOptions.aaData);
        if (angular.isDefined($scope.zcsourceSel.dict_item_id)) {
            $scope.item.tzcsource = $scope.zcsourceSel.dict_item_id;
        }
        if (angular.isDefined($scope.supplierSel.dict_item_id)) {
            $scope.item.tsupplier = $scope.supplierSel.dict_item_id;
        }
        if (angular.isDefined($scope.brandSel.dict_item_id)) {
            $scope.item.tbrand = $scope.brandSel.dict_item_id;
        }
        if (angular.isDefined($scope.locSel.dict_item_id)) {
            $scope.item.tloc = $scope.locSel.dict_item_id;
        }
        if (angular.isDefined($scope.classSel.dict_item_id)) {
            $scope.item.tclassid = $scope.classSel.dict_item_id;
        }
        if (angular.isDefined($scope.usefullifeSel.dict_item_id)) {
            $scope.item.tusefullife = $scope.usefullifeSel.dict_item_id;
        }
        if (angular.isDefined($scope.belongcompSel.id)) {
            $scope.item.tbelongcomp = $scope.belongcompSel.id;
        }
        if (angular.isDefined($scope.usedpartSel.partid)) {
            $scope.item.tpartid = $scope.usedpartSel.partid;
        }
        if (angular.isDefined($scope.useduserSel.user_id)) {
            $scope.item.tuseduserid = $scope.useduserSel.user_id;
        }
        $confirm({
            text: '是否确定变更?'
        }).then(
            function () {
                $http.post($rootScope.project + "/api/zc/resCBasicinformation/ext/insert.do",
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
        mdata.datarange = "CG";
        mdata.showusefullife = "true";
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

    function fillresdata(id) {
        var data = {};
        for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
            if ($scope.dtOptions.aaData[i].id == id) {
                data = $scope.dtOptions.aaData[i];
                break;
            }
        }
        $scope.zcsourceSel = "";
        $scope.supplierSel = "";
        $scope.brandSel = "";
        $scope.locSel = "";
        $scope.classSel = "";
        $scope.usefullifeSel = "";

        $scope.usedpartSel = "";
        $scope.useduserSel = "";
        $scope.item.tsn = ""
        $scope.item.tmodel = "";
        $scope.item.tlabel1 = "";
        $scope.item.tunit = "";
        $scope.item.tconfdesc = "";
        $scope.item.tlocdtl = "";
        $scope.item.tmark = "";
        $scope.item.tname="";

        $scope.item.tlabel2="";
        $scope.item.tbatch="";
        $scope.belongcompSel = "";


        $scope.item.tfs20 = "";
        if (angular.isDefined(data.fs1)) {
            $scope.item.tlabel1 = data.fs1;
        }
        if (angular.isDefined(data.fs2)) {
            $scope.item.tlabel2 = data.fs2;
        }
        if (angular.isDefined(data.batch)) {
            $scope.item.tbatch = data.batch;
        }
        if (angular.isDefined(data.fs20)) {
            $scope.item.tfs20 = data.fs20;
        }
        if (angular.isDefined(data.mark)) {
            $scope.item.tmark = data.mark;
        }
        if (angular.isDefined(data.locdtl)) {
            $scope.item.tlocdtl = data.locdtl;
        }
        if (angular.isDefined(data.unit)) {
            $scope.item.tunit = data.unit;
        }
        if (angular.isDefined(data.name)) {
            $scope.item.tname = data.name;
        }
        if (angular.isDefined(data.confdesc)) {
            $scope.item.tconfdesc = data.confdesc;
        }
        if (angular.isDefined(data.fs1)) {
            $scope.item.tlabel1 = data.fs1;
        }
        if (angular.isDefined(data.sn)) {
            $scope.item.tsn = data.sn;
        }
        if (angular.isDefined(data.zc_cnt)) {
            $scope.item.tzccnt = data.zc_cnt;
        }
        if (angular.isDefined(data.model)) {
            $scope.item.tmodel = data.model;
        }
        if (angular.isDefined(data.buy_timestr)) {
            $scope.date.buytime = moment(data.buy_timestr);
        }
        if (angular.isDefined(data.fd1str)) {
            $scope.date.fd1time = moment(data.fd1str);
        }
        if (angular.isDefined(data.zcsource)) {
            for (var i = 0; i < $scope.zcsourceOpt.length; i++) {
                if (data.zcsource == $scope.zcsourceOpt[i].dict_item_id) {
                    $scope.zcsourceSel = $scope.zcsourceOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.supplier)) {
            for (var i = 0; i < $scope.supplierOpt.length; i++) {
                if (data.supplier == $scope.supplierOpt[i].dict_item_id) {
                    $scope.supplierSel = $scope.supplierOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.brand)) {
            for (var i = 0; i < $scope.brandOpt.length; i++) {
                if (data.brand == $scope.brandOpt[i].dict_item_id) {
                    $scope.brandSel = $scope.brandOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.loc)) {
            for (var i = 0; i < $scope.locOpt.length; i++) {
                if (data.loc == $scope.locOpt[i].dict_item_id) {
                    $scope.locSel = $scope.locOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.usefullife)) {
            for (var i = 0; i < $scope.usefullifeOpt.length; i++) {
                if (data.usefullife == $scope.usefullifeOpt[i].dict_item_id) {
                    $scope.usefullifeSel = $scope.usefullifeOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.class_id)) {
            for (var i = 0; i < $scope.classOpt.length; i++) {
                if (data.class_id == $scope.classOpt[i].dict_item_id) {
                    $scope.classSel = $scope.classOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.belong_company_id)) {
            for (var i = 0; i < $scope.belongcompOpt.length; i++) {
                if (data.belong_company_id == $scope.belongcompOpt[i].id) {
                    $scope.belongcompSel = $scope.belongcompOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.part_id)) {
            for (var i = 0; i < $scope.usedpartOpt.length; i++) {
                if (data.part_id == $scope.usedpartOpt[i].partid) {
                    $scope.usedpartSel = $scope.usedpartOpt[i];
                    break;
                }
            }
        }
        if (angular.isDefined(data.used_userid)) {
            for (var i = 0; i < $scope.useduserOpt.length; i++) {
                if (data.used_userid == $scope.useduserOpt[i].user_id) {
                    $scope.useduserSel = $scope.useduserOpt[i];
                    break;
                }
            }
        }
    }

    $scope.filldata = function (id) {
        fillresdata(id);
    }
    //是否ids传入
    if (angular.isDefined(meta.ids)) {
        var ps = {};
        ps.ids = meta.ids;
        ps.datarange = "all";
        ps.category = 3;
        $http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                    if (res.data.length == 1) {
                        fillresdata(res.data[0].id);
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}

