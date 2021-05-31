


function knbaseSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
    $uibModalInstance, $scope, meta, $http, $rootScope) {


    $scope.dzconfig = {
        url : 'fileupload.do',
        maxFilesize : 10000,
        paramName : "file",
        maxThumbnailFilesize : 1,
        // 一个请求上传多个文件
        uploadMultiple : true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads : 1,
        maxFiles : 1,
        dictDefaultMessage : "点击上传附件",
        acceptedFiles : "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks : true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue : false,
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
        init : function() {
            $scope.myDropzone = this; // closure
        }
    };




    $scope.data = {};
    $scope.typeOpt = [];
    $scope.typeSel = {};

    $scope.showOpt=[{id:"0",name:"隐藏"},{id:"1",name:"显示"},];
    $scope.showSel= $scope.showOpt[1];

    $http.post($rootScope.project + "/api/ops/knBase/ext/listCategory.do", {
    }).success(function (rs) {
        if (rs.success) {
            $scope.typeOpt=rs.data;
            if(rs.data.length>0){
                $scope.typeSel = $scope.typeOpt[0];
            }

            if (angular.isDefined(meta.id)) {
                // 加载数据
                $http.post($rootScope.project + "/api/ops/knBase/ext/selectById.do", {
                    id: meta.id
                }).success(function (res) {
                    if (res.success) {
                        $scope.data= res.data;
                        if($scope.data.isshow=="1"){
                            $scope.showSel= $scope.showOpt[1];
                        }else{
                            $scope.showSel= $scope.showOpt[0];
                        }
                        for(var i=0;i<$scope.typeOpt.length;i++){
                            if(res.data.catid==$scope.typeOpt[i].id){
                                $scope.typeSel = $scope.typeOpt[i];
                                break;
                            }
                        }
                        editor.txt.html( $scope.data.ct)
                        // 初始化太快,延迟1000ms
                        if (angular.isDefined($scope.data.attach) && $scope.data.attach.length > 0) {
                            setTimeout(function () {
                                var mockFile = {
                                    size: 0,
                                    name: "附件",
                                    filetype:'file',
                                    // 需要显示给用户的图片名
                                    uuid: $scope.data.attach,
                                    href: $rootScope.project + "/api/file/filedown.do?id=" + $scope.data.attach,
                                    url: $rootScope.project + "/api/file/filedown.do?id=" + $scope.data.attach,
                                    status: "success",
                                    accepted: true
                                };
                                $scope.myDropzone.emit("addedfile", mockFile);
                                $scope.myDropzone.files.push(mockFile); // file must be added
                                // manually
                                var uurl=$rootScope.project + "/api/file/filedown.do?id=" + $scope.data.attach;
                                $scope.myDropzone.createThumbnailFromUrl(mockFile,uurl ,function(thumb){
                                    $scope.myDropzone.emit("thumbnail", mockFile, uurl);
                                    $scope.myDropzone.emit("complete", mockFile);
                                }, "anonymous");

                            }, 500)
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
                message: rs.message
            });
        }
    })



    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $timeout(function () {
        var modal = document.getElementsByClassName('modal-body');
        for (var i = 0; i < modal.length; i++) {
            var adom = modal[i].getElementsByClassName('chosen-container');
            for (var j = 0; j < adom.length; j++) {
                adom[i].style.width = "100%";
            }
        }
    }, 200);
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
            $scope.data.attach = id;
        } else {
            $scope.data.attach = "";
        }

        $scope.data.catid=$scope.typeSel.id;
        $scope.data.type="local";
        $scope.data.catname=$scope.typeSel.routeName;
        $scope.data.isshow=$scope.showSel.id;
        $scope.data.ct=editor.txt.html();
        $http.post($rootScope.project + "/api/ops/knBase/ext/save.do",
            $scope.data).success(function (res) {
            if (res.success) {
                $uibModalInstance.close("OK");
            } else {
                notify({
                    message: res.message
                });
            }
            editor.destroy();
        })
    };
}



function opsknbaseCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                       $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    var pbtns = $rootScope.curMemuBtns;

    $scope.URL = $rootScope.project + "/api/ops/knBase/ext/selectPage.do";
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: $scope.URL,
            type: 'POST',
            data: {start: 0,type:"local"}
        })
        .withDataProp('data').withDataProp('data').withDOM('frtlpi').withPaginationType('full_numbers')
        .withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 420)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', false).withOption('bProcessing', true)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', true).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        }) ;
    function stateChange(iColumn, bVisible) {
    }


    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + " <button ng-click=\"review('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">预览</button> ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }

    function renderFile(data, type, full) {
        if (angular.isDefined(data) && data.length > 0) {
            var html = " <span><a href=\"../api/file/filedown.do?id=" + data + "\">下载</a></span> ";
            return html;
        }
    }

    function renderShow(data, type, full) {
        if(data=="1"){
            return "显示";
        }else{
            return "隐藏";
        }

    }

    $scope.dtInstance = {}
    $scope.dtColumns = [];
    $scope.dtColumns.push(DTColumnBuilder.newColumn('id').withTitle('操作')
        .withOption('sDefaultContent', '').withOption("width", '150').renderWith(renderAction));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('isshow').withTitle('状态')
        .withOption('sDefaultContent', '').renderWith(renderShow));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('catname').withTitle('类型')
        .withOption('sDefaultContent', '').withOption("width", '50'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('title').withTitle('标题')
        .withOption('sDefaultContent', '').withOption("width", '100'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('label').withTitle('关键字')
        .withOption('sDefaultContent', '').withOption("width", '100'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('attach').withTitle('附件')
        .withOption('sDefaultContent', '').withOption("width", '30').renderWith(renderFile));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('reviewcnt').withTitle('查询数')
        .withOption('sDefaultContent', '').withOption("width", '10'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('lusername').withTitle('更新人')
        .withOption('sDefaultContent', '').withOption("width", '30'));
    $scope.dtColumns.push(DTColumnBuilder.newColumn('lasttime').withTitle('更新时间')
        .withOption('sDefaultContent', '').withOption("width", '30'));


    $scope.query = function () {
        flush();
    }
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
                id: "btn2",
                label: "",
                type: "btn",
                show: true,
                priv: "insert",
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            },
            {
                id: "btn5",
                label: "",
                type: "btn",
                show: true,
                priv: "search",
                template: ' <button ng-click="websearch()" class="btn btn-sm btn-primary" type="submit">网页搜索</button>'
            }

        ],
        tools: [{
            id: "input",
            show: true,
            label: "内容",
            placeholder: "输入名称",
            type: "input",
            ct: ""
        }]
    };
    $scope.meta = meta;
    privNormalCompute($scope.meta.toolsbtn, pbtns);

    function flush() {
        var ps = {}
        var time = new Date().getTime();
        ps.time = time;
        ps.type="local"
        ps.search=$scope.meta.tools[0].ct;
        $scope.dtOptions.ajax.data = ps
    }

    $scope.websearch = function () {
        $window.open($rootScope.project + "kn/index.html");
    }


    $scope.row_del = function (id) {
            $confirm({
                text: '是否删除?'
            }).then(
                function () {
                    $http.post(
                        $rootScope.project
                        + "/api/ops/knBase/deleteById.do", {
                            id: id
                        }).success(function (res) {
                        if (res.success) {
                            flush();
                        } else {
                            notify({
                                message: res.message
                            });
                        }
                    });
                });

    }

    $scope.review = function (id) {
        $window.open($rootScope.project + "/kn/knbaselook.html?id="+id);
    }



    // //////////////////////////save/////////////////////
    $scope.save = function (id) {
        var meta = {};
        meta.id=id;
        var modalInstance = $uibModal.open({
            backdrop: false,
            templateUrl: 'views/ops/modal_knbaseSave.html',
            controller: knbaseSaveCtl,
            size: 'blg',
            resolve: {
                meta: function () {
                    return meta;
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
app.register.controller('opsknbaseCtl', opsknbaseCtl);