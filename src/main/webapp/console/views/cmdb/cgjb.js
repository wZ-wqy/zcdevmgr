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

function rendersn(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tsnstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderclass(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tclassidstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendermodel(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tmodelstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendersource(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tzcsourcestatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendersupplier(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tsupplierstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderusefullife(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tusefullifestatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderbrand(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tbrandstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderloc(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tlocstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendercbuytime(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tbuytimestatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderptime(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tfd1status == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}


function renderbelongcomp(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tbelongcompstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}


function renderpart(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tpartidstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderuser(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tuseduseridstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendercnt(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tzccntstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderunit(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tunitstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderfs1(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tlabel1status == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderfs2(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tlabel2status == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}
function renderbatch(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tbatchstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}


function rendermark(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tmarkstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function rendername(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tnamestatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}



function renderfs20(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tfs20status == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderlocdtl(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tlocdtlstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function renderconfdesc(data, type, full) {
    if (angular.isUndefined(data)) {
        data = "";
    }
    if (full.tconfdescstatus == "true") {
        return "<span style=\"color:purple;font-weight:bold\">" + data + "</span>"
    } else {
        return "<span style=\"color:red;font-weight:bold\">不变更</span>"
    }
}

function cgjblistCtl($confirm, $timeout, $localStorage, notify, $log, $uibModal,
                     $uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
                     DTColumnBuilder, $compile) {
    var item = meta;
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
        }).withButtons([
            {
                extend: 'csv',
                text: 'Excel(当前页)',
                exportOptions: {
                    columns: ':visible',
                    trim: true,
                    modifier: {
                        page: 'current'
                    }
                }
            },
            {
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
            }
        ]);
    $scope.dtInstance = {}
    $scope.dtColumns = [];
    var dtColumns = [];

    function renderZCAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"delitem('"
            + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
            'sDefaultContent', '').withOption("width", '30'),
        DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
            'sDefaultContent', '').withOption('width', '50'),
        DTColumnBuilder.newColumn('fclassfullname').withTitle('资产类别(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tclassfullname').withTitle('资产类别(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderclass),

        DTColumnBuilder.newColumn('fname').withTitle('资产名称(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tname').withTitle('资产名称(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendername),
        DTColumnBuilder.newColumn('fmodel').withTitle('规格型号(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tmodel').withTitle('规格型号(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendermodel),
        DTColumnBuilder.newColumn('fsn').withTitle('序列(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tsn').withTitle('序列(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendersn),
        DTColumnBuilder.newColumn('ffs20').withTitle('其他编号(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tfs20').withTitle('其他编号(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendersn),
        DTColumnBuilder.newColumn('funit').withTitle('计量单位(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tunit').withTitle('计量单位(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderunit),
        // DTColumnBuilder.newColumn('fzccnt').withTitle('数量(变更前)').withOption(
        //     'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        // DTColumnBuilder.newColumn('tzccnt').withTitle('数量(变更后)').withOption(
        //     'sDefaultContent', '').renderWith(rendercnt),
        DTColumnBuilder.newColumn('fsupplierstr').withTitle('供应商(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tsupplierstr').withTitle('供应商(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendersupplier),
        DTColumnBuilder.newColumn('fbrandstr').withTitle('品牌(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tbrandstr').withTitle('品牌(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderbrand),
        DTColumnBuilder.newColumn('fzcsourcestr').withTitle('来源(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tzcsourcestr').withTitle('来源(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendersource),
        DTColumnBuilder.newColumn('flocstr').withTitle('存放区域(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tlocstr').withTitle('存放区域(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderloc),
        DTColumnBuilder.newColumn('flocdtl').withTitle('位置(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tlocdtl').withTitle('位置(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderlocdtl),
        DTColumnBuilder.newColumn('fusefullifestr').withTitle('使用期限(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tusefullifestr').withTitle('使用期限(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderusefullife),
        DTColumnBuilder.newColumn('ffd1str').withTitle('生产日期(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tfd1str').withTitle('生产日期(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderptime),
        DTColumnBuilder.newColumn('fbuytimestr').withTitle('采购日期(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tbuytimestr').withTitle('采购日期(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendercbuytime),
        DTColumnBuilder.newColumn('fconfdesc').withTitle('配置描述(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tconfdesc').withTitle('配置描述(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderconfdesc),
        DTColumnBuilder.newColumn('fmark').withTitle('备注(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tmark').withTitle('备注(变更后)').withOption(
            'sDefaultContent', '').renderWith(rendermark),


        DTColumnBuilder.newColumn('fbatch').withTitle('批次号(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tbatch').withTitle('批次号(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderbatch),
        DTColumnBuilder.newColumn('fbelongcompanyname').withTitle($rootScope.BELONGCOMP_B).withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tbelongcompanyname').withTitle($rootScope.BELONGCOMP_A).withOption(
            'sDefaultContent', '').renderWith(renderbelongcomp),

        DTColumnBuilder.newColumn('fpartname').withTitle($rootScope.USEDPART_B).withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tpartname').withTitle($rootScope.USEDPART_A).withOption(
            'sDefaultContent', '').renderWith(renderpart),
        DTColumnBuilder.newColumn('fusedusername').withTitle('使用人(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tusedusername').withTitle('使用人(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderuser),


        DTColumnBuilder.newColumn('flabel1').withTitle('标签1(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tlabel1').withTitle('标签1(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderfs1),
        DTColumnBuilder.newColumn('flabel2').withTitle('标签2(变更前)').withOption(
            'sDefaultContent', '').renderWith(renderDTFontColorGreenH),
        DTColumnBuilder.newColumn('tlabel2').withTitle('标签2(变更后)').withOption(
            'sDefaultContent', '').renderWith(renderfs2),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', '')]

    function flush() {
        $http.post($rootScope.project + "/api/zc/resCBasicinformation/ext/selectByUuid.do",
            item).success(function (res) {
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
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function zccgjbCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $window,
                   $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    var gdict = {};
    var dicts = "zcusefullife,devbrand,devdc,zcsource,zcsupper";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "Y",
            comp: "Y",
            belongcomp: "N",
            classroot: "3",
            uid: "zccgjbd"
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


    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"detail('"
            + full.busuuid
            + "','" + full.status + "')\" class=\"btn-white btn btn-xs\">变更明细</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderprocess(data, type, full) {
        if (angular.isDefined(data) && data.length() > 0) {
            return "申请详情"
        } else {
            return "无审批"
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
        DTColumnBuilder.newColumn('busuuid').withTitle('变更单号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('status').withTitle('办理状态').withOption(
            'sDefaultContent', '').renderWith(renderZCSPStatus),
        // DTColumnBuilder.newColumn('pinst').withTitle('流程详情').withOption(
        //     'sDefaultContent', '').renderWith(renderprocess),
        DTColumnBuilder.newColumn('processusername').withTitle('处理人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tclassfullname').withTitle('资产类别').withOption(
            'sDefaultContent', '').renderWith(renderclass),
        DTColumnBuilder.newColumn('tname').withTitle('资产名称').withOption(
            'sDefaultContent', '').renderWith(rendername),
        DTColumnBuilder.newColumn('tmodel').withTitle('规格型号').withOption(
            'sDefaultContent', '').renderWith(rendermodel),
        DTColumnBuilder.newColumn('tsn').withTitle('序列').withOption(
            'sDefaultContent', '').renderWith(rendersn),
        DTColumnBuilder.newColumn('tfs20').withTitle('其他编号').withOption(
            'sDefaultContent', '').renderWith(renderfs20),
        DTColumnBuilder.newColumn('tunit').withTitle('计量单位').withOption(
            'sDefaultContent', '').renderWith(renderunit),
        // DTColumnBuilder.newColumn('tzccnt').withTitle('数量').withOption(
        //     'sDefaultContent', '').renderWith(rendercnt),
        DTColumnBuilder.newColumn('tsupplierstr').withTitle('供应商').withOption(
            'sDefaultContent', '').renderWith(rendersupplier),
        DTColumnBuilder.newColumn('tbrandstr').withTitle('品牌').withOption(
            'sDefaultContent', '').renderWith(renderbrand),
        DTColumnBuilder.newColumn('tzcsourcestr').withTitle('来源').withOption(
            'sDefaultContent', '').renderWith(rendersource),
        DTColumnBuilder.newColumn('tlocstr').withTitle('存放区域').withOption(
            'sDefaultContent', '').renderWith(renderloc),
        DTColumnBuilder.newColumn('tlocdtl').withTitle('位置').withOption(
            'sDefaultContent', '').renderWith(renderlocdtl),
        DTColumnBuilder.newColumn('tusefullifestr').withTitle('使用期限').withOption(
            'sDefaultContent', '').renderWith(renderusefullife),
        DTColumnBuilder.newColumn('tfd1str').withTitle('生产日期').withOption(
            'sDefaultContent', '').renderWith(renderptime),
        DTColumnBuilder.newColumn('tbuytimestr').withTitle('采购日期').withOption(
            'sDefaultContent', '').renderWith(rendercbuytime),
        DTColumnBuilder.newColumn('tconfdesc').withTitle('配置描述').withOption(
            'sDefaultContent', '').renderWith(renderconfdesc),
        DTColumnBuilder.newColumn('tbatch').withTitle('批次号').withOption(
            'sDefaultContent', '').renderWith(renderbatch),
        DTColumnBuilder.newColumn('tmark').withTitle('备注').withOption(
            'sDefaultContent', '').renderWith(rendermark),
        DTColumnBuilder.newColumn('tbelongcompanyname').withTitle($rootScope.BELONGCOMP).withOption(
            'sDefaultContent', '').renderWith(renderbelongcomp),
        DTColumnBuilder.newColumn('tpartname').withTitle($rootScope.USEDPART).withOption(
            'sDefaultContent', '').renderWith(renderpart),
        DTColumnBuilder.newColumn('tusedusername').withTitle($rootScope.USEDUSER).withOption(
            'sDefaultContent', '').renderWith(renderuser),

        DTColumnBuilder.newColumn('tlabel1').withTitle('标签1').withOption(
            'sDefaultContent', '').renderWith(renderfs1),
        DTColumnBuilder.newColumn('tlabel2').withTitle('标签2').withOption(
            'sDefaultContent', '').renderWith(renderfs2),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createusername').withTitle('操作人').withOption(
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
                id: "btn",
                label: "",
                type: "btn",
                show: true,
                priv: "select",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            },
            {
                id: "btn3",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">新建</button>'
            }]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {};
        ps.search = $scope.meta.tools[0].ct;
        $http
            .post($rootScope.project + "/api/zc/resCBasicinformation/ext/selectList.do",
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

    $scope.detail = function (uuid, status) {
        meta.uuid = uuid;
        meta.status = status;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_cglist.html',
            controller: cgjblistCtl,
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

    function action(id) {
        var meta = {};
        meta.id = id;
        meta.dict = gdict;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_zccgjbSave.html',
            controller: zccgjbSaveCtl,
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
    flush();
};
app.register.controller('zccgjbCtl', zccgjbCtl);
