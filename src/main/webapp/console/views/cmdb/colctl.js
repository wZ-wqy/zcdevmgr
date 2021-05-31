function colctlSaveCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                       $uibModalInstance, $scope, meta, $http, $rootScope
    , $compile) {
    var item = [{id: "Y", name: "显示"}, {id: "N", name: "隐藏"}];
    $scope.uuidOpt = item;
    $scope.fs20Opt = item;
    $scope.recyclestrOpt = item;
    $scope.classfullnameOpt = item;
    $scope.modelOpt = item;
    $scope.snOpt = item;
    $scope.zcsourcestrOpt = item;
    $scope.supplierstrOpt = item;
    $scope.brandstrOpt = item;
    $scope.unitOpt = item;
    $scope.zc_cntOpt = item;
    $scope.confdescOpt = item;
    $scope.belongcomp_nameOpt = item;
    $scope.comp_nameOpt = item;
    $scope.part_fullnameOpt = item;
    $scope.used_usernameOpt = item;
    $scope.locstrOpt = item;
    $scope.locdtlOpt = item;
    $scope.usefullifestrOpt = item;
    $scope.buy_timestrOpt = item;
    $scope.buy_priceOpt = item;
    $scope.net_worthOpt = item;
    $scope.accumulateddepreciationOpt = item;
    $scope.wbsupplierstrOpt = item;
    $scope.wbstrOpt = item;
    $scope.wbout_datestrOpt = item;
    $scope.wb_autostrOpt = item;
    $scope.markOpt = item;
    $scope.fs1Opt = item;
    $scope.fs2Opt = item;
    $scope.lastinventorytimestrOpt = item;
    $scope.classrootnameOpt = item;
    $scope.uuidSel = $scope.uuidOpt[0]
    $scope.fs20Sel = $scope.fs20Opt[0]
    $scope.recyclestrSel = $scope.recyclestrOpt[0]
    $scope.classfullnameSel = $scope.classfullnameOpt[0]
    $scope.modelSel = $scope.modelOpt[0]
    $scope.snSel = $scope.snOpt[0]
    $scope.zcsourcestrSel = $scope.zcsourcestrOpt[0]
    $scope.supplierstrSel = $scope.supplierstrOpt[0]
    $scope.brandstrSel = $scope.brandstrOpt[0]
    $scope.unitSel = $scope.unitOpt[0]
    $scope.zc_cntSel = $scope.zc_cntOpt[0]
    $scope.confdescSel = $scope.confdescOpt[0]
    $scope.belongcomp_nameSel = $scope.belongcomp_nameOpt[0]
    $scope.comp_nameSel = $scope.comp_nameOpt[0]
    $scope.part_fullnameSel = $scope.part_fullnameOpt[0]
    $scope.used_usernameSel = $scope.used_usernameOpt[0]
    $scope.locstrSel = $scope.locstrOpt[0]
    $scope.locdtlSel = $scope.locdtlOpt[0]
    $scope.usefullifestrSel = $scope.usefullifestrOpt[0]
    $scope.buy_timestrSel = $scope.buy_timestrOpt[0]
    $scope.buy_priceSel = $scope.buy_priceOpt[0]
    $scope.net_worthSel = $scope.net_worthOpt[0]
    $scope.accumulateddepreciationSel = $scope.accumulateddepreciationOpt[0]
    $scope.wbsupplierstrSel = $scope.wbsupplierstrOpt[0]
    $scope.wbstrSel = $scope.wbstrOpt[0]
    $scope.wbout_datestrSel = $scope.wbout_datestrOpt[0]
    $scope.wb_autostrSel = $scope.wb_autostrOpt[0]
    $scope.markSel = $scope.markOpt[0]
    $scope.fs1Sel = $scope.fs1Opt[0]
    $scope.fs2Sel = $scope.fs2Opt[0]
    $scope.lastinventorytimestrSel = $scope.lastinventorytimestrOpt[0]
    $scope.classrootnameSel = $scope.classrootnameOpt[0]
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    if (angular.isDefined(meta.id)) {
        $http.post($rootScope.project + "/api/zc/queryZcColCtlById.do", {id: meta.id})
            .success(function (res) {
                if (res.success) {
                    var confjson = angular.fromJson(res.data.value);
                    console.log(confjson);
                    //cat a |awk -F "'" '{print "if(angular.isDefined(confjson."$2")&&confjson."$2"==\"N\"){ $scope."$2"Sel=$scope."$2"Opt[1];}else{$scope."$2"Sel=$scope."$2"Opt[0];}"}' |grep -v "DefaultContent"
                    if (angular.isDefined(confjson.uuid) && confjson.uuid == "N") {
                        $scope.uuidSel = $scope.uuidOpt[1];
                    } else {
                        $scope.uuidSel = $scope.uuidOpt[0];
                    }
                    if (angular.isDefined(confjson.fs20) && confjson.fs20 == "N") {
                        $scope.fs20Sel = $scope.fs20Opt[1];
                    } else {
                        $scope.fs20Sel = $scope.fs20Opt[0];
                    }
                    if (angular.isDefined(confjson.recyclestr) && confjson.recyclestr == "N") {
                        $scope.recyclestrSel = $scope.recyclestrOpt[1];
                    } else {
                        $scope.recyclestrSel = $scope.recyclestrOpt[0];
                    }
                    if (angular.isDefined(confjson.classfullname) && confjson.classfullname == "N") {
                        $scope.classfullnameSel = $scope.classfullnameOpt[1];
                    } else {
                        $scope.classfullnameSel = $scope.classfullnameOpt[0];
                    }
                    if (angular.isDefined(confjson.model) && confjson.model == "N") {
                        $scope.modelSel = $scope.modelOpt[1];
                    } else {
                        $scope.modelSel = $scope.modelOpt[0];
                    }
                    if (angular.isDefined(confjson.sn) && confjson.sn == "N") {
                        $scope.snSel = $scope.snOpt[1];
                    } else {
                        $scope.snSel = $scope.snOpt[0];
                    }
                    if (angular.isDefined(confjson.zcsourcestr) && confjson.zcsourcestr == "N") {
                        $scope.zcsourcestrSel = $scope.zcsourcestrOpt[1];
                    } else {
                        $scope.zcsourcestrSel = $scope.zcsourcestrOpt[0];
                    }
                    if (angular.isDefined(confjson.supplierstr) && confjson.supplierstr == "N") {
                        $scope.supplierstrSel = $scope.supplierstrOpt[1];
                    } else {
                        $scope.supplierstrSel = $scope.supplierstrOpt[0];
                    }
                    if (angular.isDefined(confjson.brandstr) && confjson.brandstr == "N") {
                        $scope.brandstrSel = $scope.brandstrOpt[1];
                    } else {
                        $scope.brandstrSel = $scope.brandstrOpt[0];
                    }
                    if (angular.isDefined(confjson.unit) && confjson.unit == "N") {
                        $scope.unitSel = $scope.unitOpt[1];
                    } else {
                        $scope.unitSel = $scope.unitOpt[0];
                    }
                    if (angular.isDefined(confjson.zc_cnt) && confjson.zc_cnt == "N") {
                        $scope.zc_cntSel = $scope.zc_cntOpt[1];
                    } else {
                        $scope.zc_cntSel = $scope.zc_cntOpt[0];
                    }
                    if (angular.isDefined(confjson.confdesc) && confjson.confdesc == "N") {
                        $scope.confdescSel = $scope.confdescOpt[1];
                    } else {
                        $scope.confdescSel = $scope.confdescOpt[0];
                    }
                    if (angular.isDefined(confjson.belongcomp_name) && confjson.belongcomp_name == "N") {
                        $scope.belongcomp_nameSel = $scope.belongcomp_nameOpt[1];
                    } else {
                        $scope.belongcomp_nameSel = $scope.belongcomp_nameOpt[0];
                    }
                    if (angular.isDefined(confjson.comp_name) && confjson.comp_name == "N") {
                        $scope.comp_nameSel = $scope.comp_nameOpt[1];
                    } else {
                        $scope.comp_nameSel = $scope.comp_nameOpt[0];
                    }
                    if (angular.isDefined(confjson.part_fullname) && confjson.part_fullname == "N") {
                        $scope.part_fullnameSel = $scope.part_fullnameOpt[1];
                    } else {
                        $scope.part_fullnameSel = $scope.part_fullnameOpt[0];
                    }
                    if (angular.isDefined(confjson.used_username) && confjson.used_username == "N") {
                        $scope.used_usernameSel = $scope.used_usernameOpt[1];
                    } else {
                        $scope.used_usernameSel = $scope.used_usernameOpt[0];
                    }
                    if (angular.isDefined(confjson.locstr) && confjson.locstr == "N") {
                        $scope.locstrSel = $scope.locstrOpt[1];
                    } else {
                        $scope.locstrSel = $scope.locstrOpt[0];
                    }
                    if (angular.isDefined(confjson.locdtl) && confjson.locdtl == "N") {
                        $scope.locdtlSel = $scope.locdtlOpt[1];
                    } else {
                        $scope.locdtlSel = $scope.locdtlOpt[0];
                    }
                    if (angular.isDefined(confjson.usefullifestr) && confjson.usefullifestr == "N") {
                        $scope.usefullifestrSel = $scope.usefullifestrOpt[1];
                    } else {
                        $scope.usefullifestrSel = $scope.usefullifestrOpt[0];
                    }
                    if (angular.isDefined(confjson.buy_timestr) && confjson.buy_timestr == "N") {
                        $scope.buy_timestrSel = $scope.buy_timestrOpt[1];
                    } else {
                        $scope.buy_timestrSel = $scope.buy_timestrOpt[0];
                    }
                    if (angular.isDefined(confjson.buy_price) && confjson.buy_price == "N") {
                        $scope.buy_priceSel = $scope.buy_priceOpt[1];
                    } else {
                        $scope.buy_priceSel = $scope.buy_priceOpt[0];
                    }
                    if (angular.isDefined(confjson.net_worth) && confjson.net_worth == "N") {
                        $scope.net_worthSel = $scope.net_worthOpt[1];
                    } else {
                        $scope.net_worthSel = $scope.net_worthOpt[0];
                    }
                    if (angular.isDefined(confjson.accumulateddepreciation) && confjson.accumulateddepreciation == "N") {
                        $scope.accumulateddepreciationSel = $scope.accumulateddepreciationOpt[1];
                    } else {
                        $scope.accumulateddepreciationSel = $scope.accumulateddepreciationOpt[0];
                    }
                    if (angular.isDefined(confjson.wbsupplierstr) && confjson.wbsupplierstr == "N") {
                        $scope.wbsupplierstrSel = $scope.wbsupplierstrOpt[1];
                    } else {
                        $scope.wbsupplierstrSel = $scope.wbsupplierstrOpt[0];
                    }
                    if (angular.isDefined(confjson.wbstr) && confjson.wbstr == "N") {
                        $scope.wbstrSel = $scope.wbstrOpt[1];
                    } else {
                        $scope.wbstrSel = $scope.wbstrOpt[0];
                    }
                    if (angular.isDefined(confjson.wbout_datestr) && confjson.wbout_datestr == "N") {
                        $scope.wbout_datestrSel = $scope.wbout_datestrOpt[1];
                    } else {
                        $scope.wbout_datestrSel = $scope.wbout_datestrOpt[0];
                    }
                    if (angular.isDefined(confjson.wb_autostr) && confjson.wb_autostr == "N") {
                        $scope.wb_autostrSel = $scope.wb_autostrOpt[1];
                    } else {
                        $scope.wb_autostrSel = $scope.wb_autostrOpt[0];
                    }
                    if (angular.isDefined(confjson.mark) && confjson.mark == "N") {
                        $scope.markSel = $scope.markOpt[1];
                    } else {
                        $scope.markSel = $scope.markOpt[0];
                    }
                    if (angular.isDefined(confjson.fs1) && confjson.fs1 == "N") {
                        $scope.fs1Sel = $scope.fs1Opt[1];
                    } else {
                        $scope.fs1Sel = $scope.fs1Opt[0];
                    }
                    if (angular.isDefined(confjson.fs2) && confjson.fs2 == "N") {
                        $scope.fs2Sel = $scope.fs2Opt[1];
                    } else {
                        $scope.fs2Sel = $scope.fs2Opt[0];
                    }
                    if (angular.isDefined(confjson.lastinventorytimestr) && confjson.lastinventorytimestr == "N") {
                        $scope.lastinventorytimestrSel = $scope.lastinventorytimestrOpt[1];
                    } else {
                        $scope.lastinventorytimestrSel = $scope.lastinventorytimestrOpt[0];
                    }
                    if (angular.isDefined(confjson.classrootname) && confjson.classrootname == "N") {
                        $scope.classrootnameSel = $scope.classrootnameOpt[1];
                    } else {
                        $scope.classrootnameSel = $scope.classrootnameOpt[0];
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
    $scope.item = {};
    $scope.sure = function () {
        //cat a |awk -F "'" '{print "$scope.item."$2"=$scope."$2"Sel.id;"}' |grep -v Content
        $scope.item.uuid = $scope.uuidSel.id;
        $scope.item.fs20 = $scope.fs20Sel.id;
        $scope.item.recyclestr = $scope.recyclestrSel.id;
        $scope.item.classfullname = $scope.classfullnameSel.id;
        $scope.item.model = $scope.modelSel.id;
        $scope.item.sn = $scope.snSel.id;
        $scope.item.zcsourcestr = $scope.zcsourcestrSel.id;
        $scope.item.supplierstr = $scope.supplierstrSel.id;
        $scope.item.brandstr = $scope.brandstrSel.id;
        $scope.item.unit = $scope.unitSel.id;
        $scope.item.zc_cnt = $scope.zc_cntSel.id;
        $scope.item.confdesc = $scope.confdescSel.id;
        $scope.item.belongcomp_name = $scope.belongcomp_nameSel.id;
        $scope.item.comp_name = $scope.comp_nameSel.id;
        $scope.item.part_fullname = $scope.part_fullnameSel.id;
        $scope.item.used_username = $scope.used_usernameSel.id;
        $scope.item.locstr = $scope.locstrSel.id;
        $scope.item.locdtl = $scope.locdtlSel.id;
        $scope.item.usefullifestr = $scope.usefullifestrSel.id;
        $scope.item.buy_timestr = $scope.buy_timestrSel.id;
        $scope.item.buy_price = $scope.buy_priceSel.id;
        $scope.item.net_worth = $scope.net_worthSel.id;
        $scope.item.accumulateddepreciation = $scope.accumulateddepreciationSel.id;
        $scope.item.wbsupplierstr = $scope.wbsupplierstrSel.id;
        $scope.item.wbstr = $scope.wbstrSel.id;
        $scope.item.wbout_datestr = $scope.wbout_datestrSel.id;
        $scope.item.wb_autostr = $scope.wb_autostrSel.id;
        $scope.item.mark = $scope.markSel.id;
        $scope.item.fs1 = $scope.fs1Sel.id;
        $scope.item.fs2 = $scope.fs2Sel.id;
        $scope.item.lastinventorytimestr = $scope.lastinventorytimestrSel.id;
        $scope.item.classrootname = $scope.classrootnameSel.id;
        var ps = {};
        ps.id = meta.id;
        ps.json = angular.toJson($scope.item);
        $http.post($rootScope.project + "/api/zc/modifyZcColCtlShow.do", ps)
            .success(function (res) {
                if (res.success) {
                    $uibModalInstance.close('OK');
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
}

function zccolctlCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log,
                     notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            // Recompiling so we can bind Angular,directive to the
            $compile(angular.element(row).contents())($scope);
        }).withOption(
            'headerCallback',
            function (header) {
                if ((!angular.isDefined($scope.headerCompiled))
                    || $scope.headerCompiled) {
                    // Use this headerCompiled field to only compile
                    // header once
                    $scope.headerCompiled = true;
                    $compile(angular.element(header).contents())
                    ($scope);
                }
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

    function renderColParse(data, type, full) {
        if (angular.isDefined(data) && data == "N") {
            return "隐藏"
        } else {
            return "显示"
        }
    }

    $scope.dtColumns = [];
    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
    var dtColumns = [];
    dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
        'select-checkbox checkbox_center').renderWith(function () {
        return ""
    }));
    dtColumns.push(DTColumnBuilder.newColumn('zccolparname').withTitle('字段控制组').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('fs20').withTitle('其他编号').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('classfullname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('zcsourcestr').withTitle('来源').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('供应商').withOption(
        'sDefaultContent', '').withOption("width", '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('unit').withTitle('计量单位').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('zc_cnt').withTitle('数量')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('belongcomp_name').withTitle($rootScope.BELONGCOMP).withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('comp_name').withTitle($rootScope.USEDCOMP).withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('part_fullname').withTitle($rootScope.USEDPART).withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('used_username').withTitle($rootScope.USEDUSER).withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('locstr').withTitle('存放区域').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('locdtl').withTitle('位置').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('usefullifestr').withTitle('使用年限')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('buy_timestr').withTitle('采购日期')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购单价')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('net_worth').withTitle('资产净值')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('accumulateddepreciation').withTitle('累计折旧')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    // dtColumns.push(DTColumnBuilder.newColumn('wb_autostr').withTitle('脱保计算')
    //     .withOption('sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('fs1').withTitle('标签1').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('fs2').withTitle('标签2').withOption(
        'sDefaultContent', '').renderWith(renderColParse));
    dtColumns.push(DTColumnBuilder.newColumn('lastinventorytimestr').withTitle('最近盘点')
        .withOption('sDefaultContent', '').renderWith(renderColParse));
    // dtColumns.push(DTColumnBuilder.newColumn('classrootname').withTitle('类目').withOption(
    //     'sDefaultContent', '').withOption("width", '30').renderWith(renderColParse));
    $scope.dtColumns = dtColumns;
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [
            {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            }, {
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="edit()" class="btn btn-sm btn-primary" type="submit">编辑</button>'
            }]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/queryZcColCtlShow.do", ps)
            .success(function (res) {
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
    $scope.edit = function () {
        var id = "";
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            id = selrow.zccolparid;
        } else {
            return;
        }
        var ps = {};
        ps.id = id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_colctlSave.html',
            controller: colctlSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ps;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
                flush();
            }
        }, function (reason) {
            $log.log("reason", reason)
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

    function getSelectRows() {
        var data = $scope.dtInstance.DataTable.rows({
            selected: true
        })[0];
        if (data.length == 0) {
            notify({
                message: "请至少选择一项"
            });
            return;
        } else if (data.length > 100) {
            notify({
                message: "不允许超过500个"
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
};
app.register.controller('zccolctlCtl', zccolctlCtl);