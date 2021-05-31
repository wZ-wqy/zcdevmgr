function tkwarnCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                   $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window, $stateParams) {
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
                extend: 'colvis',
                text: '显示/隐藏列',
                columns: ':gt(0)',
                columnText: function (dt, idx, title) {
                    return (idx ) + ': ' + title;
                }
            },
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
    $scope.dtColumns = [];
    var dtColumns = [];
    dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('fs20').withTitle('其他编号').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('classfullname').withTitle('资产类别').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('uuidly').withTitle('领用单据').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('crusername').withTitle('领用人').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('busdate').withTitle('领用时间').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('returndate').withTitle('预计归还时间').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderZcRecycle));
    dtColumns.push(DTColumnBuilder.newColumn('model').withTitle('规格型号').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('sn').withTitle('序列').withOption(
        'sDefaultContent', '').withOption('width', '50'));
    dtColumns.push(DTColumnBuilder.newColumn('zcsourcestr').withTitle('来源').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('supplierstr').withTitle('供应商').withOption(
        'sDefaultContent', '').withOption("width", '30'));
    dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
        'sDefaultContent', '').withOption('width', '30'));
    dtColumns.push(DTColumnBuilder.newColumn('unit').withTitle('计量单位').withOption(
        'sDefaultContent', ''));
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
    dtColumns.push(DTColumnBuilder.newColumn('buy_price').withTitle('采购单价')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('net_worth').withTitle('资产净值')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('accumulateddepreciation').withTitle('累计折旧')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColorGreenH));
    dtColumns.push(DTColumnBuilder.newColumn('wbsupplierstr').withTitle('维保商').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
        'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
    dtColumns.push(DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保日期')
        .withOption('sDefaultContent', '').renderWith(renderDTFontColoBluerH));
    dtColumns.push(DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
        'sDefaultContent', ''));
    dtColumns.push(DTColumnBuilder.newColumn('lastinventorytimestr').withTitle('最近盘点')
        .withOption('sDefaultContent', ''));
    $scope.dtColumns = dtColumns;
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [{
            id: "select",
            label: "到期日期",
            type: "select",
            disablesearch: true,
            dataOpt: [],
            dataSel: "",
            show: true,
        },
            {
                id: "btn",
                show: true,
                label: "",
                type: "btn",
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
            }]
    }
    $scope.meta = meta;
    $scope.meta.tools[0].dataOpt = [{id: "0", name: "今日"}, {id: "15", name: "15天"}, {id: "30", name: "30天"}, {
        id: "90",
        name: "90天"
    }, {id: "365", name: "1年"}, {id: "1825", name: "5年"}]
    $scope.meta.tools[0].dataSel = $scope.meta.tools[0].dataOpt[1];

    function flush() {
        var ps = {}
        ps.day = $scope.meta.tools[0].dataSel.id;
        $http.post($rootScope.project + "/api/zc/report/queryTkZcExpire.do",
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

    flush();
    $scope.btn_query = function () {
        flush();
    }
};
app.register.controller('tkwarnCtl', tkwarnCtl);