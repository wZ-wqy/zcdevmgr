function cmdbdevsearchCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                          $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {

    var setTabHeight=getDtTabHeight(500,350);
    // 分类
    var printbtn = "<div class=\"btn-group\" role=\"group\">\n" +
        "    <button type=\"button\" class=\"btn btn-sm btn-primary dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
        "      打印\n" +
        "      <span class=\"caret\"></span>\n" +
        "    </button>\n" +
        "    <ul class=\"dropdown-menu\">\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"print('rwm')\">下载二维码</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"print('txm')\">下载条形码</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"print('lb')\">打印资产标签</a></li>\n" +
        "      <li><a href=\"javascript:void(0)\" ng-click=\"print('card')\">打印资产卡片</a></li>\n" +

        "    </ul>\n" +
        "  </div>";
    $scope.URL = $rootScope.project + "/api/base/res/queryPageResAll.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: {classroot: "-1", start: 0, category: 3}
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', setTabHeight)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', false).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('createdRow', function (row) {
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
                    return (idx) + ': ' + title;
                }
            },
            {
                extend: 'csv',
                text: 'Excel',
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
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }
    $scope.dtColumns = [];
    $scope.dtColumns = assetsBaseColsCreate(DTColumnBuilder, 'withselect');
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
                id: "btn5",
                label: "",
                type: "btn",
                show: true,
                priv: "detail",
                template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
            },
            {
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "export",
                template: ' <button ng-click="exportfile()" class="btn btn-sm btn-primary" type="submit">导出</button>'
            }, {
                id: "btn8",
                label: "",
                type: "btn",
                show: true,
                template: printbtn
            }],
        tools: [{
            id: "select",
            label: "存放区域",
            type: "select",
            width: "180",
            disablesearch: true,
            show: true,
            dataOpt: [],
            dataSel: ""
        },
            {
            id: "select",
            label: "维保",
            type: "select",
            disablesearch: true,
            show: true,
            dataOpt: [],
            dataSel: ""
        }, {
            id: "select",
            label: "状态",
            type: "select",
            disablesearch: true,
            show: true,
            dataOpt: [],
            dataSel: ""
        },
            {
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入型号、编号、序列号",
            type: "input",
            ct: ""
        }]
    };
    $scope.meta = meta;

    function flush() {
        var ps = {}
        var time = new Date().getTime();
        ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
        ps.wb = $scope.meta.tools[1].dataSel.dict_item_id;
        ps.recycle = $scope.meta.tools[2].dataSel.dict_item_id;
        ps.search = $scope.meta.tools[3].ct;
        ps.time = time;
        ps.category = 3;
        $scope.dtOptions.ajax.data = ps
    }

    $scope.query = function () {
        flush();
    }
    $scope.exportfile = function () {
        var ps = {}
        ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
        // ps.env = $scope.meta.tools[1].dataSel.dict_item_id;
        ps.wb = $scope.meta.tools[1].dataSel.dict_item_id;
        ps.recycle = $scope.meta.tools[2].dataSel.dict_item_id;
        ps.search = $scope.meta.tools[3].ct;
        $window.open($rootScope.project + "/api/base/res/exportAllRes.do?category=3&loc="
            + ps.loc  + "&wb=" + ps.wb + "&recycle="
            + ps.recycle + "&search=" + ps.search);
    }
    var gdicts = {};
    $http
        .post(
            $rootScope.project + "/api/zc/queryDictFast.do",
            {
                dicts: "devbrand,devrisk,devenv,devrecycle,devwb,devdc,devservertype,devrack",
                parts: "Y",
                partusers: "Y",
                uid: "devsearch"
            }).success(function (res) {
        if (res.success) {
            gdicts = res.data;
            // 填充行数据
            var tenv = [];
            angular.copy(gdicts.devenv, tenv);
            var twb = [];
            angular.copy(gdicts.devwb, twb);
            var tloc = [];
            angular.copy(gdicts.devdc, tloc);
            var trecycle = [];
            angular.copy(gdicts.devrecycle, trecycle);
            var parts = [];
            angular.copy(gdicts.parts, parts);
            var partusers = [];
            angular.copy(gdicts.partusers, partusers);
            tloc.unshift({
                dict_item_id: "all",
                name: "全部"
            });
            $scope.meta.tools[0].dataOpt = tloc;
            $scope.meta.tools[0].dataSel = tloc[0];
            tenv.unshift({
                dict_item_id: "all",
                name: "全部"
            });
            // $scope.meta.tools[1].dataOpt = tenv;
            // $scope.meta.tools[1].dataSel = tenv[0];
            twb.unshift({
                dict_item_id: "all",
                name: "全部"
            });
            $scope.meta.tools[1].dataOpt = twb;
            $scope.meta.tools[1].dataSel = twb[0];

            trecycle.unshift({
                dict_item_id: "all",
                name: "全部"
            });
            $scope.meta.tools[2].dataOpt = trecycle;
            $scope.meta.tools[2].dataSel = trecycle[0];
        } else {
            notify({
                message: res.message
            });
        }
    })
    // $scope.detail = function () {
    //     var id = "";
    //     var selrow = getSelectRow();
    //     if (angular.isDefined(selrow)) {
    //         id = selrow.id;
    //     } else {
    //         return;
    //     }
    //     var ps = {};
    //     ps.id = id;
    //     var modalInstance = $uibModal.open({
    //         backdrop: true,
    //         templateUrl: 'views/cmdb/modal_dtl.html',
    //         controller: modalcmdbdtlCtl,
    //         size: 'blg',
    //         resolve: {
    //             meta: function () {
    //                 return ps;
    //             }
    //         }
    //     });
    //     modalInstance.result.then(function (result) {
    //         if (result == "OK") {
    //         }
    //     }, function (reason) {
    //         $log.log("reason", reason)
    //     });
    // }
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
            var d = $scope.dtInstance.DataTable.context[0].json.data;
            return d[data[0]]
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
            $log.log("reason", reason)
        });
    }
    flush();

    function getSelectRowsUUID() {
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
            var d = $scope.dtInstance.DataTable.context[0].json.data;
            for (var i = 0; i < data.length; i++) {
                res.push(d[data[i]].uuid)
            }
            return angular.toJson(res);
        }
    }

    function downloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "assets-" + moment().format('L') + '.zip';
        a.href = URL.createObjectURL(file);
        a.click();
        const tempA = document.getElementById('tempId');
        if (tempA) {
            tempA.parentNode.removeChild(tempA);
        }
    }

    $scope.print = function (type) {
        var selrows = getSelectRowsUUID();
        if (angular.isDefined(selrows)) {
            var ps = {}
            ps.data = selrows;
            if (type == "rwm" || type == "txm") {
                $http.post($rootScope.project + "/api/zc/downloadZcImage.do", {
                    data: selrows,
                    type: type
                }, {
                    responseType: 'arraybuffer'
                }).success(function (data) {
                    var blob = new Blob([data], {
                        type: "application/vnd.ms-excel"
                    });
                    downloadFile(blob);
                })
            } else if (type == "lb") {
                $http.post($rootScope.project + "/api/zc/downloadLabel.do", {
                    data: selrows
                }, {
                    responseType: 'arraybuffer'
                }).success(function (data) {
                    var blob = new Blob([data], {
                        type: "application/vnd.ms-excel"
                    });
                    downloadFile(blob);
                })
            } else if (type == "card") {
                $http.post($rootScope.project + "/api/zc/downloadCard.do", {
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
                notify({
                    message: "请先后台配置"
                });
            }
        } else {
            return;
        }
    }
};
app.register.controller('cmdbdevsearchCtl', cmdbdevsearchCtl);