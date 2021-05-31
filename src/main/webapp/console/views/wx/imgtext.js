String.prototype.endWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substring(this.length - s.length) == s)
        return true;
    else
        return false;
    return true;
}
String.prototype.startWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
    return true;
}

function msgtextsaveCtl(notify, $log, $uibModal, $uibModalInstance, $scope, id,
                        $http, $rootScope, group_id) {
    $scope.dtldzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 1,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 1,
        maxFiles: 1,
        dictDefaultMessage: "点击上传图片",
        acceptedFiles: "image/jpeg,image/png,image/gif",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        init: function () {
            $scope.myDropzone = this; // closure
        }
    };
    $scope.item = {};
    $scope.item.group_id = group_id;
    if (angular.isDefined(id)) {
        $http.post($rootScope.project + "/api/wx/queryImageTextMessageById.do",
            {
                id: id
            }).success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                // 处理调整图片
                var pic = res.data.imgurl;
                if (pic.startWith("http")) {
                } else {
                    setTimeout(function () {
                        var mockFile = {
                            name: "图",
                            uuid: $scope.item.imgurl,
                            href: $rootScope.project
                                + "/api/file/imagedown.do?id="
                                + pic,
                            url: $rootScope.project
                                + "/api/file/imagedown.do?id="
                                + pic,
                            status: "success",
                            accepted: true,
                            type: 'image/png'
                        };
                        $scope.myDropzone.emit("addedfile", mockFile);
                        $scope.myDropzone.files.push(mockFile); // file
                        // must
                        // be
                        $scope.myDropzone.createThumbnailFromUrl(
                            mockFile, $rootScope.project
                            + "/api/file/imagedown.do?id="
                            + pic);
                        $scope.myDropzone.emit("complete", mockFile);
                    }, 600)
                }
            } else {
            }
        })
    }
    $scope.wxsavepic = function () {
        // 处理图片
        var picid = getUuid();
        $scope.myDropzone.options.url = $rootScope.project
            + '/api/file/fileupload.do?bus=news&uuid=' + picid
            + '&type=image&interval=10000';
        if ($scope.myDropzone.files.length == 0) {
            alert("先上传图片");
            return;
        }
        if (angular.isDefined($scope.myDropzone.files[0].uuid)) {
            // 已经上传
            picid = $scope.myDropzone.files[0].uuid;
        } else {
            $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
            // 记录图片地址
            $http.post($rootScope.project + "/api/wx/addSc.do", {
                sctype: 'image',
                pic_id: picid
            }).success(function (res) {
                if (res.success) {
                } else {
                }
            })
        }
        $scope.item.imgurl = picid;
    }
    $scope.sure = function () {
        $http.post($rootScope.project + "/api/wx/saveImageTextMessage.do",
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
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function wximgtextCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                      $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {
    $scope.meta = {
        tools: [{
            id: "1",
            label: "图文消息组",
            type: "select",
            disablesearch: true,
            dataOpt: [],
            dataSel: ""
        }, {
            id: "1",
            label: "查询",
            type: "btn",
            template: ' <button ng-click="flush()" class="btn btn-sm btn-primary" type="submit">查询</button>'
        }, {
            id: "1",
            label: "新增",
            type: "btn",
            template: ' <button ng-click="modify()" class="btn btn-sm btn-primary" type="submit">新增</button>'
        }]
    }
    $http.post($rootScope.project + "/api/wx/queryImageTextMessagesGroup.do",
        {}).success(function (res) {
        if (res.success) {
            $scope.meta.tools[0].dataOpt = res.data;
            if (res.data.length > 0) {
                $scope.meta.tools[0].dataSel = res.data[0];
            }
            flush();
        } else {
            notify({
                message: res.message
            });
        }
    });
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"modify('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button>  ";
        acthtml = acthtml + " <button ng-click=\"deleterow('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>  </div>  ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('title').withTitle('标题').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('msgdesc').withTitle('描述').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('docurl').withTitle('跳转Url').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('imgurl').withTitle('图片Url').withOption(
            'sDefaultContent', '').withClass('none'),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').renderWith(renderAction)]

    function flush() {
        var ps = {}
        ps.id = $scope.meta.tools[0].dataSel.group_id;
        $http
            .post($rootScope.project + "/api/wx/queryImageTextMessages.do",
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
    $scope.flush = function () {
        flush();
    }
    $scope.modify = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/wx/modal_msgtext.html',
            controller: msgtextsaveCtl,
            size: 'lg',
            resolve: {
                id: function () {
                    return id;
                },
                group_id: function () {
                    return $scope.meta.tools[0].dataSel.group_id;
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
    $scope.deleterow = function (id) {
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post(
                $rootScope.project + "/api/wx/deleteImageTextMessage.do", {
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
};
app.register.controller('wximgtextCtl', wximgtextCtl);