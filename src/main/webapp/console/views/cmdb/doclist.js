function doclistSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                        $uibModalInstance, $scope, id, $http, $rootScope) {
    $scope.typeOpt = []
    $scope.typeSel = "";
    $scope.item = {};
    var dicts = "zcdoctype";
    $http
        .post($rootScope.project + "/api/zc/queryDictFast.do", {
            dicts: dicts,
            parts: "N",
            partusers: "N",
            comp: "N",
            belongcomp: "N",
            zccatused: "N",
            uid: "zcdocdictdata"
        })
        .success(
            function (res) {
                if (res.success) {
                    if (res.data.zcdoctype.length > 0) {
                        $scope.typeOpt = res.data.zcdoctype;
                        if ($scope.typeOpt.length > 0) {
                            $scope.typeSel = $scope.typeOpt[0];
                        }
                    }
                    if (angular.isDefined(id)) {
                        //修改
                        $http.post($rootScope.project + "/api/ct/docMgr/selectById.do", {
                            id: id
                        }).success(function (rs) {
                            if (rs.success) {
                                $scope.item = rs.data;
                                for (var i = 0; i < $scope.typeOpt.length; i++) {
                                    if ($scope.typeOpt[i].dict_item_id == rs.data.type) {
                                        $scope.typeSel = $scope.typeOpt[i];
                                        break;
                                    }
                                }
                                // 初始化太快,延迟1000ms
                                if (angular.isDefined($scope.item.files) && $scope.item.files.length > 0) {
                                    setTimeout(function () {
                                        var mockFile = {
                                            size: 0,
                                            name: "文件",
                                            filetype:"file",
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
                                        $scope.myDropzone.createThumbnailFromUrl(mockFile,uurl,function(thumb){
                                            $scope.myDropzone.emit("thumbnail", mockFile, uurl);
                                            $scope.myDropzone.emit("complete", mockFile);
                                        },"anonymous");
                                    }, 500)
                                }

                            } else {
                                notify({
                                    message: rs.message
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
        acceptedFiles: ".xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
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
        $scope.item.type = $scope.typeSel.dict_item_id;
        $http.post($rootScope.project + "/api/ct/docMgr/ext/insertOrUpdate.do",
            $scope.item).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
            }
            notify({
                message: res.message
            });
        })
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function doclistCtl($stateParams, DTOptionsBuilder, DTColumnBuilder,
                    $compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
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
                id: "0",
                priv: "select",
                label: "查询",
                type: "btn_query",
                show: true,
            },
            {
                id: "1",
                label: "新增",
                priv: "insert",
                show: true,
                type: "btn",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'responsive', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>";
        acthtml = acthtml + "</div>";
        return acthtml;
    }

    function renderFile(data, type, full) {
        if (angular.isDefined(data) && data.length > 0) {
            var html = " <span><a href=\"../api/file/filedown.do?id=" + data + "\">下载</a></span> ";
            return html;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('typestr').withTitle('类型').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('files').withTitle('附件').withOption(
            'sDefaultContent', '').renderWith(renderFile),
        DTColumnBuilder.newColumn('create_time').withTitle('创建时间').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.search = $scope.meta.tools[0].ct;
        $http.post($rootScope.project + "/api/ct/docMgr/ext/selectList.do", ps)
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
    $scope.btn_query = function () {
        flush();
    }
    $scope.query = function () {
        flush();
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/ct/docMgr/deleteById.do", {
                id: id
            }).success(function (res) {
                if (res.success) {
                    flush();
                }
                notify({
                    message: res.message
                });
            })
        });
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_doclistSave.html',
            controller: doclistSaveCtl,
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
app.register.controller('doclistCtl', doclistCtl);