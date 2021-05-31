function assetsuuidSaveCtl($timeout, $localStorage, notify, $log, $uibModal,
                            $uibModalInstance, $scope, meta, $http, $rootScope) {
    $scope.item={};
    $scope.item.seq=5
    $scope.item.split="-";
    $scope.item.str1="AS";
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    var list=[
        {id:"{str}",name:"自定义字符串"}, {id:"{year}",name:"年"},{id:"{month}",name:"月"},{id:"{day}",name:"日"},{id:"{seq}",name:"序列"},{id:"{random}",name:"随机"}
    ]
    $scope.list1=[];
    $scope.list2=[];
    if(angular.isDefined(meta.id)){
        $http.post($rootScope.project + "/api/zc/resUuidStrategy/selectById.do",
            {id:meta.id}).success(function (res) {
            if (res.success) {
               $scope.item=res.data;
               var arr=angular.fromJson(res.data.ct);
               $scope.list1=[];
               for(var j=0;j<list.length;j++){
                    var listflag="1"
                    for(var i=0;i<arr.length;i++){
                        if(arr[i].id==list[j].id){
                            $scope.list2.push(list[j])
                            listflag="0"
                            break;
                        }
                    }
                    if(listflag=="1"){
                        $scope.list1.push(list[j]);
                    }
               }
            } else {
                notify({
                    message: res.message
                });
            }
        })
    }else{
        $scope.list1=list;
    }
    $scope.sure = function () {
        $scope.uuidrule="";
        var arr=[];
        $("#sellist2 option").each(function(){  //遍历全部option
            var txt=$(this).text();
            var value = $(this).val();   //获取option值
            var obj={};
            obj.id=value;
            obj.txt=txt;
            if(value!=''){
                arr.push(obj);  //加入到数组中
            }
        })
        if(arr.length==0){
            notify({
                message:"请设置资产编号规则"
            });
            return;
        }
       var rule="";
        for(var i=0;i<arr.length;i++){
            if(i==0){
                rule=arr[i].id;
            }else{
                rule=rule+"-"+arr[i].id;
            }
        }
        //替换分隔符
        var split=""
        if($scope.item.split!="#"){
            split=$scope.item.split;
        }
        var rule2=rule.replace(new RegExp("-",'gm'),split)
        var rule3=rule2
        if(angular.isDefined($scope.item.str1)){
            rule3=rule2.replace(new RegExp("{str}",'gm'),$scope.item.str1)
        }
        $scope.item.uuidrule=rule3;
        $scope.item.ct=angular.toJson(arr);
        $http.post($rootScope.project + "/api/zc/resUuidStrategy/insertOrUpdate.do",
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
function assetsuuidCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
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
                id: "2",
                label: "新增",
                type: "btn",
                show: true,
                priv: 'add',
                template: ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'
            },
        ]
    }
    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
        .withPaginationType('full_numbers').withDisplayLength(50)
        .withOption("ordering", false).withOption("responsive", false)
        .withOption("searching", false).withOption('scrollY', 600)
        .withOption('scrollX', true).withOption('bAutoWidth', true)
        .withOption('scrollCollapse', true).withOption('paging', true)
        .withOption('bStateSave', true).withOption('bProcessing', false)
        .withOption('bFilter', false).withOption('bInfo', true)
        .withOption('serverSide', false).withOption('createdRow', function (row) {
            $compile(angular.element(row).contents())($scope);
        });
    function renderDef(data, type, full) {
        if(data=="1"){
            return "是";
        }
        return "否";
    }
    function renderRule(data, type, full) {
        var html="";
        if(angular.isDefined(data)){
            var arr=angular.fromJson(data);
            if(angular.isDefined(arr)&&arr.length>0){
                for(var i=0;i<arr.length;i++){
                    if(i==0){
                        html=arr[i].txt;
                    }else{
                        html=html+"-"+arr[i].txt;
                    }
                }
            }
        }
        return html;
    }


    function renderAction(data, type, full) {
        var acthtml = " <div class=\"btn-group\"> ";
        acthtml = acthtml + " <button ng-click=\"save('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">更新</button> ";
        acthtml = acthtml + " <button ng-click=\"def('" + full.id
            + "')\" class=\"btn-white btn btn-xs\">设置默认</button>   ";
        acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
            + "','"+full.def+"')\" class=\"btn-white btn btn-xs\">删除</button>   ";
        acthtml = acthtml + "</div>"
        return acthtml;
    }
    $scope.def = function (id) {
        $confirm({
            text: '是否设置默认?'
        }).then(function () {
            $http.post($rootScope.project + "/api/zc/resUuidStrategy/ext/setDefault.do", {
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
    $scope.row_del = function (id,def) {
        if(def=="1"){
            notify({
                message: "当前状态不允许删除"
            });
            return;
        }
        $confirm({
            text: '是否删除?'
        }).then(function () {
            $http.post($rootScope.project + "/api/zc/resUuidStrategy/deleteById.do", {
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
    $scope.dtInstance = {}
    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('def').withTitle('默认').withOption(
            'sDefaultContent', '').renderWith(renderDef),
        DTColumnBuilder.newColumn('uuidrule').withTitle('编码规则').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('ct').withTitle('编码名称').withOption(
            'sDefaultContent', '').renderWith(renderRule),
        DTColumnBuilder.newColumn('split').withTitle('分隔符').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('seq').withTitle('序列位数').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('str1').withTitle('字符串').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
            'sDefaultContent', '').withOption("width", '150').renderWith(renderAction)
    ];
    $scope.query = function () {
        flush();
    }
    function flush() {
        var ps = {}
        $http.post($rootScope.project + "/api/zc/resUuidStrategy/selectList.do",
            ps).success(function (res) {
            $scope.dtOptions.aaData = res.data;
        })
    }
    flush();

    $scope.save=function(id){
        var ps={}
        ps.id=id;
        var modalInstance = $uibModal.open({
            backdrop: true,
            templateUrl: 'views/cmdb/modal_assetsuuid.html',
            controller: assetsuuidSaveCtl,
            size: 'lg',
            resolve: {
                meta: function () {
                    return ps;
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
app.register.controller('assetsuuidCtl', assetsuuidCtl);