function demoCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log,
                 notify, $scope, $http, $rootScope, $uibModal, $timeout) {
    $scope.btn_query = function () {
        $scope.text = $scope.text + "a";
    }
    $scope.dzconfig = {
        url: 'fileupload.do',
        maxFilesize: 10000,
        paramName: "file",
        maxThumbnailFilesize: 5,
        // 一个请求上传多个文件
        uploadMultiple: true,
        // 当多文件上传,需要设置parallelUploads>=maxFiles
        parallelUploads: 5,
        maxFiles: 5,
        dictDefaultMessage: "点击上传附件",
        acceptedFiles: "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
        // 添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        // 关闭自动上传功能，默认会true会自动上传
        // 也就是添加一张图片向服务器发送一次请求
        autoProcessQueue: false,
        init: function () {
            $scope.myDropzone = this; // closure
        }
    };
    var html = "<div class=\"form-group\">\n" +
        "								<label class=\"col-sm-2 control-label\">附件</label>\n" +
        "								<div class=\"col-sm-10\">\n" +
        "									<div class=\"dropzone\" drop-zone dzconfig=\"dzconfig\"\n" +
        "										dzeventHandlers=\"dtldzevent\" enctype=\"multipart/form-data\">\n" +
        "										<div id=\"dropzone\" class=\"fallback\">\n" +
        "											<input name=\"file\" type=\"file\" multiple=\"\" />\n" +
        "										</div>\n" +
        "									</div>\n" +
        "								</div>";
    var html2 = $compile(html);
    var $dom = html2($scope);
    angular.element(cc).append($dom);
    // 添加到文档中
//
//	 var scope = angular.element(sidebar).scope();
//	 console.log("ffffffffff",scope);
//	 $dom=$complite(html)
    //$dom.appendTo('body');
    //$scope.template = html2;
    $timeout(function () {
    }, 3000);
    $scope.btn_add = function () {
        return
        var meta = {};
        meta = {
            footer_hide: false,
            title: "测试",
            item: {
                addr: "金"
            },
            stime: moment().subtract(15, "days"),
            statusOpt: [{
                id: 1,
                name: "1"
            }, {
                id: 2,
                name: "2"
            }, {
                id: 3,
                name: "3"
            }],
            statusSel: "",
            items: [{
                type: "pic",
                disabled: "false",
                label: "图片",
                need: true,
                name: 'text',
                conf: "dtldzconfig"
            }, {
                type: "datetime",
                disabled: "false",
                label: "文本",
                need: true,
                ng_model: "stime"
            }, {
                type: "textarea",
                disabled: "false",
                sub_type: "text",
                required: true,
                maxlength: "2",
                placeholder: "请输入姓名",
                label: "文本",
                need: true,
                height: "200px",
                name: 'text',
                ng_model: "text"
            }, {
                type: "input",
                disabled: "false",
                sub_type: "text",
                required: false,
                maxlength: "2",
                placeholder: "请输入姓名",
                label: "姓名",
                need: true,
                name: 'name',
                ng_model: "name"
            }, {
                type: "dashed"
            }, {
                type: "input",
                disabled: "false",
                sub_type: "number",
                required: true,
                maxlength: "10",
                placeholder: "请输入手机",
                label: "手机",
                need: true,
                name: 'mobile',
                ng_model: "mobile"
            }, {
                type: "input",
                disabled: "false",
                sub_type: "text",
                required: true,
                maxlength: "10",
                placeholder: "请输入地址",
                label: "地址",
                need: false,
                name: 'addr',
                ng_model: "addr"
            }, {
                type: "select",
                disabled: "false",
                label: "地址",
                need: false,
                disable_search: "true",
                dataOpt: "statusOpt",
                dataSel: "statusSel"
            }],
            sure: function (modalInstance, modal_meta) {
                // 返回接口
                console.log(modal_meta);
                console.log($scope);
                console.log(modal_meta.meta.stime.format('YYYY-MM-DD'));
                var picid = getUuid();
                console.log("####", $scope.myDropzone);
                $scope.myDropzone.options.url = $rootScope.project
                    + '/api/file/fileupload.do?bus=prodimgs&uuid=' + picid
                    + '&type=image&interval=10000';
                $scope.myDropzone.uploadFile($scope.myDropzone.files[0])
                console.log($scope.myDropzone.files[0]);
            },
            init: function (modal_meta) {
                console.log("INIT");
                $http.post($rootScope.project + "/api/user/queryGroup.do", {})
                    .success(function (res) {
                        console.log(res);
                        modal_meta.meta.statusOpt = res.data;
                    });
            }
        }
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/Template/modal_simpleForm.html',
            controller: modal_simpleFormCtl,
            size: 'lg',
            resolve: {
                meta: function () {
                    return meta;
                }
            }
        });
        modalInstance.result.then(function (result) {
            if (result == "OK") {
            }
        }, function (reason) {
        });
    }
    $scope.btn_del = function () {
        console.log("del");
    }
    $scope.btn_modify = function () {
        console.log("modify");
    }

    function a() {
        console.log(99);
    }

    var opt = [{
        id: "1",
        name: "adfasdfasA"
    }, {
        id: "1",
        name: "B"
    }, {
        id: "1",
        name: "C"
    }];
    var meta = {
        tablehide: false,
        tools: [
            {
                id: "datetime",
                label: "测试",
                type: "datetime"
            },
            {
                id: "input",
                label: "212",
                label_hide: true,
                type: "input",
                placeholder: "1212",
                ct: ""
            },
            {
                id: "select",
                label: "选择",
                type: "select",
                disablesearch: true,
                dataOpt: opt,
                dataSel: opt[0]
            },
            {
                id: "btn",
                label: "",
                type: "btn",
                template: ' <button ng-click="abcd1()" class="btn btn-sm btn-primary" type="submit">查询c</button>'
            }, {
                id: "btn_query",
                label: "查询",
                type: "btn_query"
            }, {
                id: "btn_add",
                fun: "",
                label: "新增",
                type: "btn_add"
            }, {
                id: "btn_del",
                fun: "",
                label: "删除",
                type: "btn_del"
            }, {
                id: "btn_modify",
                fun: "",
                label: "更新",
                type: "btn_modify"
            }, {
                id: "btn_actiona",
                fun: "",
                label: "action",
                type: "btn_actiona"
            }]
    }
    $scope.meta = meta;
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('scrollY',
        500).withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('responsive', false).withOption('scrollCollapse', true)
        .withOption('paging', true).withFixedColumns({
            leftColumns: 0,
            rightColumns: 1
        })
    $scope.dtInstance = {}

    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.role_id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        // acthtml = acthtml + " <button ng-click=\"row_detail()\"
        // class=\"btn-white btn btn-xs\">详细</button> ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.role_id
            + "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
        return acthtml;
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('is_action').withTitle('状态').withOption(
            'sDefaultContent', '').renderWith(
            function (data, type, full) {
                return dt_renderMapSimple(data, type, full, [{
                    id: "Y",
                    name: "有效",
                    id: "N",
                    name: "无效"
                }]);
            }),
        DTColumnBuilder.newColumn('role_id').withTitle('操作').withOption(
            'width', '200px').withOption('sDefaultContent', '')
            .renderWith(renderAction)]

    function flush() {
        console.log("!!!!!!!!!!!!!!323");
        var ps = {}
        var d = [];
        var a = {
            role_id: 1
        };
        d.push({
            role_id: 2
        });
        d.push({
            role_id: 3
        });
        d.push({
            role_id: 5
        });
        d.push({
            role_id: 7
        });
        d.push({
            role_id: 8
        });
        d.push({
            role_id: 19
        });
        d.push({
            role_id: 11
        });
        $scope.dtOptions.aaData = d;
    }

    $scope.row_detail = function (id) {
    }
    $scope.row_del = function (id) {
        $confirm({
            text: '是否删除功能?'
        }).then(function () {
            $http.post($rootScope.project + "/api/role/roleDelete.do", {
                role_id: id
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
    $scope.query = function () {
        flush();
    }
    $scope.save = function (id) {
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/system/role/modal_role_save.html',
            controller: roleSaveCtl,
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
app.register.controller('demoCtl', demoCtl);