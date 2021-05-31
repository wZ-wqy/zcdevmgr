function databackupCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                       $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.meta = {
        tablehide: false,
        tools: [
            {
                id: "1",
                label: "刷新",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">刷新</button>'
            },
            {
                id: "1",
                priv: "act1",
                label: "",
                type: "btn",
                template: ' <button ng-click="action()" class="btn btn-sm btn-primary" type="submit">手工调度</button>',
                show: true,
            }]
    }
    privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
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
            });
    $scope.dtInstance = {}

    function renderResult(data, type, full) {
        if (angular.isDefined(data)) {
            if (data.length < 15) {
                return data;
            } else {
                return "备份失败";
            }
        }
        return "";
    }

    function renderAction(data, type, full) {
        var fp = full.filepath;
        var acthtml = " <div class=\"btn-group\"> ";
        // if(fp.indexOf("a")!=-1){
        acthtml = acthtml + " <button ng-click=\"download('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">下载</button>   ";
        // }
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function downloadFile(file) {
        var a = document.createElement('a');
        a.id = 'tempId';
        document.body.appendChild(a);
        a.download = "backupfile-" + moment().format('L') + '.zip';
        a.href = URL.createObjectURL(file);
        a.click();
        const tempA = document.getElementById('tempId');
        if (tempA) {
            tempA.parentNode.removeChild(tempA);
        }
    }

    $scope.download = function (id) {
        $http.post($rootScope.project + "/api/sysDbbackupRec/downFile.do", {
            id: id
        }, {
            responseType: 'arraybuffer'
        }).success(function (data) {
            if (data.byteLength < 80) {
                var st1 = String.fromCharCode.apply(null, new Uint8Array(data))
                var st2 = decodeURIComponent(escape(st1));//没有这一步中文会乱码
                var st2json = angular.fromJson(st2);
                if (angular.isDefined(st2json.message)) {
                    notify({
                        message: st2json.message
                    });
                }
            } else {
                var blob = new Blob([data], {
                    type: "application/vnd.ms-excel"
                });
                downloadFile(blob);
            }
        })
    }
    $scope.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction),
        DTColumnBuilder.newColumn('dbname').withTitle('数据库').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('result').withTitle('备份结果').withOption(
            'sDefaultContent', '').renderWith(renderResult),
        DTColumnBuilder.newColumn('createTime').withTitle('备份时间')
            .withOption('sDefaultContent', ''),
        DTColumnBuilder.newColumn('duration').withTitle('备份耗时').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('filesize').withTitle('文件大小').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('filepath').withTitle('文件路径').withOption(
            'sDefaultContent', '')
    ]
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        ps.pageSize = "365";
        ps.pageIndex = "1";
        $http.post($rootScope.project + "/api/sysDbbackupRec/selectPage.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    flush();
    $scope.action = function () {
        var ps = {}
        $http.post($rootScope.project + "/api/databackup.do",
            ps).success(function (res) {
            notify({
                message: res.message
            });
        })
    }
};
app.register.controller('databackupCtl', databackupCtl);