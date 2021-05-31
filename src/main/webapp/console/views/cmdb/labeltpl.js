function labeltplSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
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
                        $http.post($rootScope.project + "/api/cmdb/resLabelTpl/selectById.do", {
                            id: id
                        }).success(function (rs) {
                            if (rs.success) {
                                $scope.item = rs.data;
                                // 初始化太快,延迟1000ms
                                if (angular.isDefined($scope.item.tplfileid) && $scope.item.tplfileid.length > 0) {
                                    setTimeout(function () {
                                        var mockFile = {
                                            name: "模板",
                                            filetype:"file",
                                            uuid: $scope.item.tplfileid,
                                            href: $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.tplfileid,
                                            url: $rootScope.project + "/api/file/filedown.do?id=" + $scope.item.tplfileid,
                                            status: "success",
                                            accepted: true
                                        };
                                        $scope.myDropzone.emit("addedfile", mockFile);
                                        $scope.myDropzone.files.push(mockFile); // file must be added
                                        var uurl=$rootScope.project + "/api/file/filedown.do?id=" + $scope.item.tplfileid;
                                        $scope.myDropzone.createThumbnailFromUrl(mockFile,uurl ,function(thumb){
                                            $scope.myDropzone.emit("thumbnail", mockFile, uurl);
                                            $scope.myDropzone.emit("complete", mockFile);
                                        }, "anonymous");

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
        acceptedFiles: ".doc,.docx",
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
            $scope.item.tplfileid = id;
        } else {
            $scope.item.tplfileid = "";
        }
        if (!angular.isDefined(id)) {
            $scope.item.ifdef = "0";
        }

        $http.post($rootScope.project + "/api/cmdb/resLabelTpl/insertOrUpdate.do",
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

function labeltplCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log,
                     notify, $scope, $http, $rootScope, $uibModal, $window) {
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

    function renderName(data, type, full) {
        var html = full.model;
        return html;
    }

    function renderJg(data, type, full) {
        var html = full.rackstr + "-" + full.frame;
        return html;
    }

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

    function renderDef(data, type, full) {
        if (data == "1") {
            return "默认"
        } else {
            var acthtml = " <div class=\"btn-group\"> ";
            acthtml = acthtml + " <button ng-click=\"sefdef('" + full.id
                + "')\" class=\"btn-white btn btn-xs\">设置默认</button> ";
            acthtml = acthtml + "</div>";
            return acthtml;
        }

    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('conf').withTitle('配置').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('tplfileid').withTitle('模版文件').withOption(
            'sDefaultContent', '').renderWith(renderFile),
        DTColumnBuilder.newColumn('ifdef').withTitle('是否默认').withOption(
            'sDefaultContent', '').renderWith(renderDef),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').withOption("width", '100').renderWith(renderAction)
    ]
    $scope.query = function () {
        flush();
    }
    var meta = {
        tablehide: false,
        toolsbtn: [],
        tools: [
            {
                id: "btn1",
                label: "",
                type: "btn",
                show: true,
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            }
            // ,
            // {
            //     id: "btn3",
            //     label: "",
            //     type: "btn",
            //     show: true,
            //     template: ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
            // }
        ]
    }
    $scope.meta = meta;

    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/cmdb/resLabelTpl/selectList.do", ps)
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

    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_label.html',
            controller: labeltplSaveCtl,
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

    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/cmdb/resLabelTpl/deleteById.do", {
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
    flush();
    $scope.sefdef = function (id) {
        $http.post($rootScope.project + "/api/cmdb/resLabelTpl/ext/setdef.do", {id: id})
            .success(function (res) {
                if (res.success) {
                    flush();
                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
};
app.register.controller('labeltplCtl', labeltplCtl);