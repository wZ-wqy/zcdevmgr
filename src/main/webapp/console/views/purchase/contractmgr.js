function contractSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                         $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.date = {
        effectivedate: moment(),
        closingdate: moment().add(1, 'days')
    }
    $scope.compOpt = [];
    $scope.compSel = {};
    $scope.partOpt = [];
    $scope.partSel = {};
    $scope.statusOpt = [{id: "finish", name: "已完结"},
        {id: "cancel", name: "已取消"},
        {id: "Waitreview", name: "等待审核"},
        {id: "Waitperformance", name: "等待履约"},
        {id: "Inperformance", name: "履约中"},
        {id: "released", name: "已解除"}
    ];
    $scope.statusSel = $scope.statusOpt[0];
    $scope.firstpartyOpt = [];
    $scope.firstpartySel = {}
    $scope.secondpartyOpt = [];
    $scope.secondpartySel = {}
    $scope.relatedpartyOpt = [];
    $scope.relatedpartySel = {}
    $http.post($rootScope.project + "/api/base/contractEntity/selectList.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.firstpartyOpt = res.data;
                if ($scope.firstpartyOpt.length > 0) {
                    $scope.firstpartySel = $scope.firstpartyOpt[0];
                }
                $scope.secondpartyOpt = res.data;
                if ($scope.secondpartyOpt.length > 0) {
                    $scope.secondpartySel = $scope.secondpartyOpt[0];
                }
                $scope.relatedpartyOpt = res.data;
                if ($scope.relatedpartyOpt.length > 0) {
                    // $scope.relatedpartySel=$scope.relatedpartyOpt[0];
                }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    var dicts = "devwb";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "Y",
            partusers: "N",
            comp: "N",
            belongcomp: "Y",
            zccatused: "N",
            uid: "contractdata"
        })
        .success(
            function (res) {
                if (res.success) {
                    $scope.compOpt = res.data.belongcomp;
                    if ($scope.compOpt.length > 0) {
                        $scope.compSel = $scope.compOpt[0];
                    }
                    $scope.partOpt = res.data.parts
                    $scope.partSel = {};
                    if (angular.isDefined(id)) {
                        $http.post($rootScope.project + "/api/base/contract/selectById.do", {id: id})
                            .success(function (res) {
                                if (res.success) {
                                    $scope.item = res.data;
                                    $scope.date.effectivedate = moment($scope.item.effectivedate);
                                    console.log($scope.date.effectivedate)
                                    $scope.date.closingdate = moment($scope.item.closingdate);
                                    if(angular.isDefined( $scope.item.files)&&$scope.item.files.length>0){
                                        setTimeout(function () {
                                            var mockFile = {
                                                size: 0,
                                                filetype:"file",
                                                name: "文件",
                                                // 需要显示给用户的图片名
                                                uuid: $scope.item.files,
                                                href: $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.files,
                                                url: $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.files,
                                                status: "success",
                                                accepted: true
                                            };
                                            $scope.myDropzone.emit("addedfile", mockFile);
                                            $scope.myDropzone.files.push(mockFile); // file must be added
                                            // manually
                                            var uurl=$rootScope.project + "/api/file/filedown.do?id=" + $scope.item.files;
                                            $scope.myDropzone.createThumbnailFromUrl(mockFile,uurl, function(thumb){
                                                $scope.myDropzone.emit("thumbnail", mockFile, uurl);
                                                $scope.myDropzone.emit("complete", mockFile);
                                            }, "anonymous");

                                        }, 500)
                                    }

                                    for (var i = 0; i < $scope.statusOpt.length; i++) {
                                        if ($scope.item.status == $scope.statusOpt[i].id) {
                                            $scope.statusSel = $scope.statusOpt[i];
                                            break;
                                        }
                                    }
                                    for (var i = 0; i < $scope.typeOpt.length; i++) {
                                        if ($scope.item.type == $scope.typeOpt[i].id) {
                                            $scope.typeSel = $scope.typeOpt[i];
                                            break;
                                        }
                                    }
                                    if (angular.isDefined($scope.item.firstparty)) {
                                        for (var i = 0; i < $scope.firstpartyOpt.length; i++) {
                                            if ($scope.item.firstparty == $scope.firstpartyOpt[i].id) {
                                                $scope.firstpartySel = $scope.firstpartyOpt[i];
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.firstpartySel = {};
                                    }
                                    if (angular.isDefined($scope.item.secondparty)) {
                                        for (var i = 0; i < $scope.secondpartyOpt.length; i++) {
                                            if ($scope.item.secondparty == $scope.secondpartyOpt[i].id) {
                                                $scope.secondpartySel = $scope.secondpartyOpt[i];
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.secondpartySel = {};
                                    }
                                    if (angular.isDefined($scope.item.relatedparty)) {
                                        for (var i = 0; i < $scope.relatedpartyOpt.length; i++) {
                                            if ($scope.item.relatedparty == $scope.relatedpartyOpt[i].id) {
                                                $scope.relatedpartySel = $scope.relatedpartyOpt[i];
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.relatedpartySel = {};
                                    }
                                    if (angular.isDefined($scope.item.belongcomp)) {
                                        for (var i = 0; i < $scope.compOpt.length; i++) {
                                            if ($scope.item.belongcomp == $scope.compOpt[i].id) {
                                                $scope.compSel = $scope.compOpt[i];
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.compSel = {};
                                    }
                                    if (angular.isDefined($scope.item.belongpart)) {
                                        for (var i = 0; i < $scope.partOpt.length; i++) {
                                            if ($scope.item.belongpart == $scope.partOpt[i].partid) {
                                                $scope.partSel = $scope.partOpt[i];
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.partSel = {};
                                    }
                                } else {
                                    notify({
                                        message: res.message
                                    });
                                }
                            })
                    }
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    $scope.typeOpt = [{id: "main", name: "主合同"}, {id: "sub", name: "子合同"}, {
        id: "supplement",
        name: "补充合同"
    }, {id: "renew", name: "续约"}];
    $scope.typeSel = $scope.typeOpt[0];
    $scope.directionOpt = [{id: "payment", name: "付款"}, {id: "collection", name: "收款"}];
    $scope.directionSel = $scope.directionOpt[0];
    $scope.item = {};
    $scope.item.totalamount = 0;
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 1,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 1,
        maxFiles: 1,
        dictDefaultMessage: "点击上传需要上传的文件",
        acceptedFiles: "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        thumbnail: function(file, dataUrl) {
            if (file.previewElement) {
                $(file.previewElement).removeClass("dz-file-preview");
                $(file.previewElement).removeClass("dz-detail");
                var images = $(file.previewElement).find("[data-dz-thumbnail]").each(function() {
                    var thumbnailElement = this;
                    thumbnailElement.alt = "";
                    if(angular.isDefined(file.filetype)&&file.filetype=="file"){
                        thumbnailElement.src = $rootScope.project + "/console/img/sm/uploadfiletpl.jpg";
                    }else{
                        thumbnailElement.src = dataUrl;
                    }
                });
                setTimeout(function() { $(file.previewElement).addClass("dz-image-preview"); }, 1);
            }
        },

        init: function () {
            $scope.myDropzone = this; // closure
        }
    };
    $scope.sure = function () {
        $scope.item.direction = $scope.directionSel.id;
        $scope.item.type = $scope.typeSel.id;
        $scope.item.status = $scope.statusSel.id;
        $scope.item.closingdate = $scope.date.closingdate.format('YYYY-MM-DD');
        $scope.item.effectivedate = $scope.date.effectivedate.format('YYYY-MM-DD');
        var id = getUuid();
        if ($scope.myDropzone.files.length > 0) {
            if (typeof ($scope.myDropzone.files[0].uuid) == "undefined") {
                // 需要上传
                $scope.myDropzone.options.url = $rootScope.project
                    + '/api/file/fileupload.do?uuid=' + id
                    + '&bus=file&interval=10000&bus=file';
                $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
            } else {
                id = $scope.myDropzone.files[0].uuid;
            }
            $scope.item.files = id;
        } else {
            $scope.item.files = "";
        }
        if (angular.isDefined($scope.firstpartySel.id)) {
            $scope.item.firstparty = $scope.firstpartySel.id;
            $scope.item.firstpartyname = $scope.firstpartySel.name;
        }
        if (angular.isDefined($scope.secondpartySel.id)) {
            $scope.item.secondparty = $scope.secondpartySel.id;
            $scope.item.secondpartyname = $scope.secondpartySel.name;
        }
        if (angular.isDefined($scope.relatedpartySel.id)) {
            $scope.item.relatedparty = $scope.relatedpartySel.id;
            $scope.item.relatedpartyname = $scope.relatedpartySel.name;
        }
        if (angular.isDefined($scope.compSel.id)) {
            $scope.item.belongcomp = $scope.compSel.id;
            $scope.item.belongcompname = $scope.compSel.name;
        }
        if (angular.isDefined($scope.partSel.partid)) {
            $scope.item.belongpart = $scope.partSel.partid;
            $scope.item.belongpartname = $scope.partSel.name;
        }
        $http.post($rootScope.project + "/api/base/contract/ext/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
                notify({
                    message: res.message
                });
            }
        })
    };
}

function contractCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
            }, {
                id: "1",
                label: "新增",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="add()" class="btn btn-sm btn-primary" type="submit">录入</button>'
            }, {
                id: "1",
                label: "编辑",
                type: "btn",
                show: true,
                priv: 'select',
                template: ' <button ng-click="edit()" class="btn btn-sm btn-primary" type="submit">编辑</button>'
            }
        ]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlpi')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", true).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', false)
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
    $scope.dtInstance = {}
    $scope.selectCheckBoxAll = function (selected) {
        if (selected) {
            $scope.dtInstance.DataTable.rows().select();
        } else {
            $scope.dtInstance.DataTable.rows().deselect();
        }
    }

    function renderStatus(data, type, full) {
        var statusOpt = [{id: "finish", name: "已完结"},
            {id: "cancel", name: "已取消"},
            {id: "Waitreview", name: "等待审核"},
            {id: "Waitperformance", name: "等待履约"},
            {id: "Inperformance", name: "履约中"},
            {id: "released", name: "已解除"}
        ];
        var res = "";
        for (var i = 0; i < statusOpt.length; i++) {
            if (statusOpt[i].id == data) {
                res = statusOpt[i].name;
                break;
            }
        }
        return res;
    }

    function renderDirection(data, type, full) {
        if (data == "payment") {
            return "付款";
        } else if (data == "collection") {
            return "收款";
        } else {
            return data;
        }
        return acthtml;
    }

    function renderType(data, type, full) {
        if (data == "main") {
            return "主合同";
        } else if (data == "sub") {
            return "子合同";
        } else if (data == "supplement") {
            return "补充合同";
        } else if (data == "renew") {
            return "续约";
        } else {
            return data;
        }
    }

    var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';

    function renderFile(data, type, full) {
        if (angular.isDefined(data)&&data.length>0) {
            var html = " <span><a href=\"../api/file/filedown.do?id=" + data + "\">下载</a></span> ";
            return html;
        }
    }

    $scope.dtInstance = {}
    $scope.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
            'select-checkbox checkbox_center').renderWith(function () {
            return ""
        }),
        DTColumnBuilder.newColumn('busuuid').withTitle('编号').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('status').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(renderStatus),
        DTColumnBuilder.newColumn('type').withTitle('类型').withOption(
            'sDefaultContent', '').renderWith(renderType),
        DTColumnBuilder.newColumn('direction').withTitle('方向').withOption(
            'sDefaultContent', '').renderWith(renderDirection),
        DTColumnBuilder.newColumn('title').withTitle('合同标题').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('secondpartyuser').withTitle('描述').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('totalamount').withTitle('总金额').withOption(
            'sDefaultContent', ''),
        // DTColumnBuilder.newColumn('stratedesc').withTitle('已完成金额').withOption(
        //     'sDefaultContent', ''),
        DTColumnBuilder.newColumn('belongcompname').withTitle($rootScope.BELONGCOMP).withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('effectivedate').withTitle('生效日期').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('closingdate').withTitle('截止日期').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('firstpartyname').withTitle('签约单位').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('firstpartyuser').withTitle('负责人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('secondpartyname').withTitle('对方单位').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('secondpartyuser').withTitle('对方负责人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('relatedpartyname').withTitle('关联单位').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('relatedpartyuser').withTitle('关联负责人').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('files').withTitle('合同文件').withOption(
            'sDefaultContent', '').renderWith(renderFile),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('createTime').withTitle('创建时间').withOption(
            'sDefaultContent', '')
    ];
    $scope.query = function () {
        flush();
    }

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/base/contract/ext/selectList.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }

    function save(id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/purchase/modal_contractSave.html',
            controller: contractSaveCtl,
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

    $scope.add = function () {
        save();
    }
    $scope.edit = function () {
        var selrow = getSelectRow();
        if (angular.isDefined(selrow)) {
            save(selrow.id);
        }
    }
    flush();
};
app.register.controller('contractCtl', contractCtl);