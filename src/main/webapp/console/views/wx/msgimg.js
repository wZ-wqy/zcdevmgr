function wxmsgimgCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                     $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: []
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        /*
         * var acthtml = " <div class=\"btn-group\"> "; acthtml = acthtml + "
         * <button ng-click=\"modify('" + full.id + "')\" class=\"btn-white btn
         * btn-xs\">更新</button> "; acthtml = acthtml + " <button
         * ng-click=\"deleterow('" + full.id + "')\" class=\"btn-white btn
         * btn-xs\">删除</button> </div> "; return acthtml;
         */
    }

    function renderImage(data, type, full) {
        var html = ""
        html = html + "<img style='height:50px;width:50px;' src="
            + $rootScope.project + "/api/file/imagedown.do?id="
            + full.pic_id + "  />"
        return html;
    }

    function renderType(data, type, full) {
        if (data == "6") {
            return "图文消息";
        } else if (data == "text") {
            return "文本消息";
        } else {
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('图片').withOption(
            'sDefaultContent', '').renderWith(renderImage),
        DTColumnBuilder.newColumn('pic_id').withTitle('图片Id').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('ctime').withTitle('日期').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/wx/queryScs.do", ps).success(
            function (res) {
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
    $scope.flush = function () {
        flush();
    }
    $scope.modify = function (id) {
        /* */
    }
    $scope.deleterow = function (id) {
    }
};
app.register.controller('wxmsgimgCtl', wxmsgimgCtl);