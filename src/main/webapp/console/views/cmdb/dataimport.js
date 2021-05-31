function modalimportdataFailCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                                $confirm, $log, notify, $scope, meta, $http, $rootScope, $uibModal,
                                $uibModalInstance) {
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'bAutoWidth', false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.dtColumns = [DTColumnBuilder.newColumn('ct').withTitle('失败列表')
        .withOption('sDefaultContent', '')]
    $scope.dtOptions.aaData = meta.failed_data;
}

function modalimpordocCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                          $confirm, $log, notify, $scope, meta, $http, $rootScope, $uibModal,
                          $window, $uibModalInstance) {
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.downdict = function () {
        $window.open($rootScope.project + "/api/base/res/exportDictItems.do");
    }
    $scope.downTpl = function () {
        $window.open($rootScope.project + "/api/base/res/exportAllRes.do?loc=-1");
    }
}

function zcdataImportCtl($state, DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                         $log, notify, $scope, $http, $rootScope, $uibModal, $window, $timeout) {
    var category = $state.router.globals.current.data.category;
    $scope.okbtnstatus = false;
    $scope.importOpt = [{
        id: "insert",
        name: "新增"
    }, {
        id: "update",
        name: "更新"
    }];
    $scope.importSel = $scope.importOpt[1];
    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 2,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 1,
        maxFiles: 1,
        dictDefaultMessage: "点击上传需要上传的文件",
        acceptedFiles: ".xlsx,.xls",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        init: function () {
            $scope.myDropzone = this; // closure
        }
    };
    $scope.doc = function () {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_importdoc.html',
            controller: modalimpordocCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return ""
                }
            }
        });
        $scope.myDropzone.removeAllFiles(true);
        modalInstance.result.then(function (result) {
        }, function (reason) {
        });
    }
    $scope.ok = function () {
        $scope.okbtnstatus = true;
        var id = getUuid();
        if ($scope.myDropzone.files.length > 0) {
            $scope.myDropzone.options.url = $rootScope.project
                + '/api/file/fileupload.do?uuid=' + id
                + '&bus=file&interval=10000&bus=file';
            $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
        } else {
            notify({
                message: "请选择文件"
            });
            $scope.okbtnstatus = false;
            return;
        }
        $timeout(function () {
            $http.post($rootScope.project + "/api/base/res/importResData.do", {
                type: $scope.importSel.id,
                category: category,
                id: id
            }).success(function (res) {
                $scope.okbtnstatus = false;
                $scope.myDropzone.removeAllFiles(true);
                if (res.success) {
                    notify({
                        message: "操作成功！"
                    });
                } else {

                    if(angular.isDefined(res.data)&&angular.isDefined(res.data.failed_data)){
                        var modalInstance = $uibModal.open({
                            backdrop: true,
                            templateUrl: 'views/cmdb/modal_importFail.html',
                            controller: modalimportdataFailCtl,
                            size: 'blg',
                            resolve: {
                                meta: function () {
                                    return res.data;
                                }
                            }
                        });
                    }else{
                        notify({
                            message: res.message
                        });

                    }



                }
            })
        }, 3000);
    }
};
app.register.controller('zcdataImportCtl', zcdataImportCtl);