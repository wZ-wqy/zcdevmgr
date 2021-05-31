$(document).ready(
    function () {
        // Full height of sidebar
        function fix_height() {
            var heightWithoutNavbar = $("#wrapper").height() - 61;
            $(".sidebar-panel").css("min-height",
                heightWithoutNavbar + "px");
            var navbarHeight = $('nav.navbar-default').height();
            var wrapperHeight = $('#page-wrapper').height();
            // $(".sidebar-panel").css("min-height", wrapperHeigh - 61 +
            // "px");
            if (navbarHeight > wrapperHeight) {
                $('#page-wrapper').css("min-height", navbarHeight + "px");
            }
            if (navbarHeight < wrapperHeight) {
                $('#page-wrapper').css("min-height",
                    $(window).height() + "px");
            }
            if ($('body').hasClass('fixed-nav')) {
                if (navbarHeight > wrapperHeight) {
                    $('#page-wrapper').css("min-height",
                        navbarHeight + "px");
                } else {
                    $('#page-wrapper').css("min-height",
                        $(window).height() - 60 + "px");
                }
            }
        }

        $(window).bind("load resize scroll", function () {
            if (!$("body").hasClass('body-small')) {
                fix_height();
            }
        });
        // Move right sidebar top after scroll
        $(window).scroll(
            function () {
                if ($(window).scrollTop() > 0
                    && !$('body').hasClass('fixed-nav')) {
                    $('#right-sidebar').addClass('sidebar-top');
                } else {
                    $('#right-sidebar').removeClass('sidebar-top');
                }
            });
        setTimeout(function () {
            fix_height();
        })
    });
// Minimalize menu when screen is less than 768px
$(window).bind("load resize", function () {
    if ($(document).width() < 769) {
        $('body').addClass('body-small')
    } else {
        $('body').removeClass('body-small')
    }
});

function getDtTabHeight(offHeight,minHeight){
    var bodyHeight = document.body.offsetHeight;
    var setTabHeight=450;
    console.log(bodyHeight)
    if(bodyHeight>500){
        setTabHeight=bodyHeight-offHeight;

    }
    console.log("setTabHeight:"+setTabHeight);
    if(minHeight>0){
        if(setTabHeight<minHeight){
            setTabHeight=minHeight;
            console.log("setTabHeight again:"+setTabHeight);
        }
    }
    return setTabHeight;
}

/** *******************常用函数**************************** */
// 生产UUid
function getUuid() {
    var len = 32;// 32长度
    var radix = 16;// 16进制
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
        .split('');
    var uuid = [], i;
    radix = radix || chars.length;
    if (len) {
        for (i = 0; i < len; i++)
            uuid[i] = chars[0 | Math.random() * radix];
    } else {
        var r;
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random() * 16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }
    return uuid.join('');
}

function prepend(arr, item) {
    // 将arr数组复制给a
    var a = arr.slice(0);
    // 使用unshift方法向a开头添加item
    a.unshift(item);
    return a;
}
function formatRackData(data,cap,rackuuid){
    var result={};
    var ctime=Date.now();
    var successdata=[];
    var successdata2=[];
    var faileddata=[];
    for(var i=0;i<data.length;i++){
        var obj=data[i];
        if(angular.isUndefined(data[i].frame)){
            data[i].result="frame不存在"
            faileddata.push(data[i]);
            continue;
        }
        var frame=data[i].frame.split("-");
        if(frame.length!=2){
            data[i].result="frame格式出错"
            faileddata.push(data[i]);
            continue;
        }
        var framest=frame[0];
        var frameet=frame[1];
        // console.log(framest+","+frameet+"|",parseInt(framest)+","+parseInt(frameet));
        //frame错误判断
        if(isNaN(framest)|| isNaN(frameet)||framest>frameet||framest>cap){
            // console.log("failed")
            data[i].result="frame格式出错"
            faileddata.push(data[i]);
            continue;
        }
        data[i].framest=parseInt(framest);
        data[i].frameet=parseInt(frameet);
        data[i].framediff=parseInt(frameet)-parseInt(framest);
        successdata.push(data[i]);
    }
    //继续判断
    for(var i=0;i<successdata.length;i++){
        var obj=successdata[i];
        var flag="success";
        for(var j=0;j<successdata.length;j++){
            var matchobj=successdata[j];
            if(obj.id==matchobj.id){
                continue;
            }
            var a=obj.framest;
            var b=obj.frameet;
            var c=matchobj.framest;
            var d=matchobj.frameet;
            if(b<c||a>d){
            }else{
                flag="failed";
                break;
            }
        }
        console.log(a+","+b+","+c+","+d+",flag:"+flag+","+obj.id+matchobj.id);
        if(flag=="success"){
            successdata2.push(successdata[i])
        }else{
            successdata[i].result="frame冲突";
            faileddata.push(successdata[i])
        }
    }
    var noobj="<tr name=\"nonedevice\" id=\""+ctime+"f#SEQ#\">\n" +
        "                    <td class=\"no-padding bg-light-blue\">#SEQ#</td>\n" +
        "                    <td class=\"no-padding\"></td>\n" +
        "                    <td class=\"no-padding bg-light-blue\">#SEQ#</td>\n" +
        "                    <td class=\"no-padding\"></td>\n" +
        "                    <td class=\"no-padding\"></td>\n" +
        "                    <td class=\"no-padding\"></td>\n" +
        "                    <td class=\"no-padding\"></td>\n" +
        "                    <td class=\"no-padding\"></td>\n" +
        "                    <td class=\"no-padding no-print\"></td>\n" +
        "                </tr>";
    //显示html
    var htmltbbody="  <thead>\n" +
        "                <tr class=\"active\">\n" +
        "                    <th style=\"width:2%\" class=\"bg-light-blue\">U</th>\n" +
        "                    <th style=\"text-align:center;width:16%\" class=\"bg-light-blue\">"+rackuuid+"</th>\n" +
        "                    <th style=\"width:2%\" class=\"bg-light-blue\">U</th>\n" +
        "                    <th style=\"text-align:center;\">状态</th>\n" +
        "                    <th style=\"text-align:center;\">IP地址</th>\n" +
        "                    <th style=\"text-align:center;\">设备SN号</th>\n" +
        "                    <th style=\"text-align:center;\">设备型号</th>\n" +
        "                    <th style=\"text-align:center;\">运行环境</th>\n" +
        "                    <th style=\"text-align:center;\" class=\"no-print\">操作</th>\n" +
        "                </tr>\n" +
        "                </thead> <tbody>"
    var htmltr=[];
    //初始化机柜
    for(var i=0;i<cap;i++){
        var u="";
        if(i>9){u=i;}else{u="0"+i;}
        htmltr.push(noobj.replace(new RegExp("#SEQ#","gm"),u));
    }
    //放置设备
    for(var i=0;i<successdata2.length;i++){
        var assets=successdata2[i];
        var s=assets.framest;
        var e=assets.frameet;
        var d=e-s+1;
        var u="";
        if(e>9){u=e;}else{u="0"+e;}
        var rowspan="";
        if(d>0){
            rowspan="rowspan=\""+d+"\"";
        }
        var devhtml="<tr name=\"hasdevice\" class=\"danger\">\n" +
            "                    <td style=\"vertical-align: middle;\" class=\"no-padding bg-light-blue\">"+u+"</td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+" class=\"no-padding\"><a href=\"/detail/online-95/\">"+assets.uuid+"</a></td>\n" +
            "                    <td style=\"vertical-align: middle;\" class=\"no-padding bg-light-blue\">"+u+"</td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+"  class=\"no-padding\">"+assets.recyclestr+"</td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+"  class=\"no-padding\"><span>"+assets.ip+"</span></td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+"  class=\"no-padding\">"+assets.sn+"</td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+"  class=\"no-padding\">"+assets.name+"/"+assets.model+"</td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+"  class=\"no-padding\">"+assets.envstr+"</td>\n" +
            "                    <td style=\"vertical-align: middle;\" "+rowspan+"  class=\"no-padding no-print\"><a href=\"/update/online-95/\"></a></td>\n" +
            "                </tr>";
        console.log("before")
        console.log(htmltr[e])
        htmltr[e]=devhtml;
        console.log("after")
        console.log(htmltr[e])
        if(d>1){
            for(var j=1;j<d;j++){
                var ii=e-j;
                var uu="";
                if(ii>9){uu=ii;}else{uu="0"+ii;}
                var devhtml2="<tr name=\"hasdevice\" class=\"danger\">\n" +
                    "                    <td style=\"vertical-align: middle;\" class=\"no-padding bg-light-blue\">"+uu+"</td>\n" +
                    "                    <td style=\"vertical-align: middle;\" class=\"no-padding bg-light-blue\">"+uu+"</td>\n" +
                    "                </tr>";
                console.log("AAAbefore")
                console.log(htmltr[ii])
                htmltr[ii]=devhtml2;
                console.log("AAAafter")
                console.log(htmltr[ii])

            }
        }
    }
    for(var i=htmltr.length-1;i>0;i--){
        htmltbbody=htmltbbody+htmltr[i];
    }
    htmltbbody=htmltbbody+"<tr>\n" +
        "                    <td class=\"no-padding bg-light-blue\">-1</td>\n" +
        "                    <td class=\"no-padding bg-light-blue\"></td>\n" +
        "                    <td class=\"no-padding bg-light-blue\">-1</td>\n" +
        "                     <td class=\"no-padding\"></td><td class=\"no-padding\"></td><td class=\"no-padding\"></td><td class=\"no-padding\"></td><td class=\"no-padding\"></td><td class=\"no-padding no-print\"></td>\n" +
        "                </tr>\n" +
        "                </tbody>";
    var htmltable="<table id=\"rack-detail\" style=\"text-align:center;margin:0; padding:0;\" class=\"table table-bordered text-center nowarp table-condensed\">"+htmltbbody+"</table>";
    result.htmltable=htmltable;
    result.falied=faileddata;
    result.success=successdata2;
    return result;
}

/** **************按钮权限判断函数*************** */
// $scope.crud = {
//     "update" : false,
//     "insert" : false,
//     "remove" : false
// }
function privCrudCompute(curd, pbtns) {
    if (!angular.isDefined(pbtns)) {
        return;
    }
    if (pbtns == "") {
        return;
    }
    var pbtns_arr = angular.fromJson(pbtns);
    if (angular.isDefined(pbtns_arr) && pbtns_arr.length > 0) {
        for (var i = 0; i < pbtns_arr.length; i++) {
            if (pbtns_arr[i].p == "search") {
                curd.search = true;
                continue;
            }
            if (pbtns_arr[i].p == "root_insert") {
                curd.root_insert = true;
                continue;
            }
            if (pbtns_arr[i].p == "update") {
                curd.update = true;
                continue;
            }
            if (pbtns_arr[i].p == "remove") {
                curd.remove = true;
                continue;
            }
            if (pbtns_arr[i].p == "select") {
                curd.select = true;
                continue;
            }
            if (pbtns_arr[i].p == "insert") {
                curd.insert = true;
                continue;
            }
            if (pbtns_arr[i].p == "item_insert") {
                curd.item_insert = true;
                continue;
            }
            if (pbtns_arr[i].p == "item_select") {
                curd.item_select = true;
                continue;
            }
            if (pbtns_arr[i].p == "item_update") {
                curd.item_update = true;
                continue;
            }
            if (pbtns_arr[i].p == "item_remove") {
                curd.item_remove = true;
                continue;
            }
            if (pbtns_arr[i].p == "cancel") {
                curd.cancel = true;
                continue;
            }
            if (pbtns_arr[i].p == "submit") {
                curd.submit = true;
                continue;
            }
            if (pbtns_arr[i].p == "save") {
                curd.save = true;
                continue;
            }
            if (pbtns_arr[i].p == "exportfile") {
                curd.exportfile = true;
                continue;
            }
            if (pbtns_arr[i].p == "importfile") {
                curd.importfile = true;
                continue;
            }
            if (pbtns_arr[i].p == "uploadfile") {
                curd.uploadfile = true;
                continue;
            }
            if (pbtns_arr[i].p == "downfile") {
                curd.downfile = true;
                continue;
            }
            // 授权
            if (pbtns_arr[i].p == "priv") {
                curd.priv = true;
                continue;
            }
            if (pbtns_arr[i].p == "fix") {
                curd.fix = true;
                continue;
            }
            if (pbtns_arr[i].p == "cpwd") {
                curd.cpwd = true;
                continue;
            }
            if (pbtns_arr[i].p == "act1") {
                curd.act1 = true;
                continue;
            }
            if (pbtns_arr[i].p == "act2") {
                curd.act2 = true;
                continue;
            }
            if (pbtns_arr[i].p == "act3") {
                curd.act3 = true;
                continue;
            }
            if (pbtns_arr[i].p == "act4") {
                curd.act4 = true;
                continue;
            }
            if (pbtns_arr[i].p == "act5") {
                curd.act5 = true;
                continue;
            }
            if (pbtns_arr[i].p == "act6") {
                curd.act6 = true;
                continue;
            }
        }
    }
}

function privNormalCompute(meta, pbtns) {
    console.log("#########privNormalCompute###########");
    if (!angular.isDefined(pbtns)) {
        return;
    }
    if (pbtns == "") {
        return;
    }
    var pbtns_arr = angular.fromJson(pbtns);
    if (angular.isDefined(pbtns_arr) && pbtns_arr.length > 0) {
        for (var i = 0; i < meta.length; i++) {
            for (var j = 0; j < pbtns_arr.length; j++) {
                if (meta[i].priv == pbtns_arr[j].p) {
                    meta[i].show = true;
                }
            }
        }
    }
}

/** *******************datatable**************************** */
function dt_renderUDAction(data, type, full) {
    var acthtml = " <div class=\"btn-group\"> ";
    acthtml = acthtml + " <button ng-click=\"update('" + full.id
        + "')\" class=\"btn-white btn btn-xs\">更新</button>   ";
    acthtml = acthtml + " <button ng-click=\"del('" + full.id
        + "')\" class=\"btn-white btn btn-xs\">删除</button> ";
    acthtml = acthtml + "</div>"
    return acthtml;
}

function dt_renderMapSimple(data, type, full, map) {
    if (typeof (map) == undefined) {
        return "unknow";
    }
    for (var i = 0; i < map.length; i++) {
        if (map[i].id = data) {
            return map[i].name;
        }
    }
    return "unknow";
}

/** *******************datatable end**************************** */
/** ******************************modal 模版************************* */
// 支持:输入框,文本框,单选,日期控件
function modal_simpleFormCtl($timeout, $localStorage, notify, $log, $uibModal,
                             $uibModalInstance, $scope, meta, $http, $rootScope, $compile) {
    $scope.meta = meta;
    var formhtml = " <form id=\"formct\" class=\"form-horizontal m-t-md\" name=\"myForm\" novalidate compile=\"template\" action=\"\">";
    var items = meta.items;
    var select_ids = [];
    if (typeof (items) != "undefined" && items.length > 0) {
        for (var i = 0; i < items.length; i++) {
            var tmp_tpl = "";
            var obj = items[i];
            var need_col = "";
            if (obj.need) {
                need_col = "<span class=\"text-danger\">*</span>";
            }
            if (obj.type == "input") {
                var required_col = "";
                if (obj.required) {
                    required_col = "required";
                }
                var maxlength_col = "";
                if (typeof (obj.maxlength) != "undefined" && obj.maxlength > 0) {
                    maxlength_col = "ng-maxlength=\"" + obj.maxlength + "\"";
                }
                tmp_tpl = tmp_tpl + "<div class=\"form-group\">";
                tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\">    ";
                tmp_tpl = tmp_tpl
                    + "	<input ng-disabled=\""
                    + obj.disabled
                    + "\" type=\""
                    + obj.sub_type
                    + "\" "
                    + required_col
                    + "  "
                    + maxlength_col
                    + "  class=\"form-control ng-pristine ng-untouched ng-valid ng-empty\" placeholder=\""
                    + obj.placeholder + "\" name=\"" + obj.name
                    + "\" ng-model=\"meta.item." + obj.ng_model + "\" > ";
                tmp_tpl = tmp_tpl
                    + "	<div class=\"text-danger\" ng-if=\"myForm."
                    + obj.name + ".$dirty && myForm." + obj.name
                    + ".$invalid\"> ";
                tmp_tpl = tmp_tpl + "		<span ng-if=\"myForm." + obj.name
                    + ".$error.required\"> 输入不能为空 </span> ";
                tmp_tpl = tmp_tpl + "		<span ng-show=\"myForm." + obj.name
                    + ".$error.maxlength\">不能超过" + obj.maxlength
                    + "个字符</span> ";
                tmp_tpl = tmp_tpl + "</div> ";
                tmp_tpl = tmp_tpl + "</div> ";
                tmp_tpl = tmp_tpl + "</div> ";
                formhtml = formhtml + tmp_tpl;
            } else if (obj.type == "select") {
                var uid = getUuid()
                select_ids.push(uid);
                tmp_tpl = tmp_tpl + " <div class=\"form-group\">";
                tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\"> ";
                tmp_tpl = tmp_tpl
                    + "	<select ng-disabled=\"" + obj.disabled + "\" class=\"dt_select\"  style=\"width:100%\" id=\""
                    + uid
                    + "\"  chosen disable-search=\""
                    + obj.disable_search
                    + "\"  search_contains=\"true\"  class=\"chosen-select\" no-results-text=\"'没有找到相应条目'\" ng-model=\"meta."
                    + obj.dataSel
                    + "\"  data-placeholder-text-single=\"'请选择...'\" ng-options=\"item.name for item in meta."
                    + obj.dataOpt + "\"> ";
                tmp_tpl = tmp_tpl + "		<option value=\"\"></option> ";
                tmp_tpl = tmp_tpl + "	</select> ";
                tmp_tpl = tmp_tpl + "</div> ";
                tmp_tpl = tmp_tpl + "</div> ";
                formhtml = formhtml + tmp_tpl;
            } else if (obj.type == "selectmultiple") {
                var uid = getUuid()
                select_ids.push(uid);
                tmp_tpl = tmp_tpl + " <div class=\"form-group\">";
                tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\"> ";
                tmp_tpl = tmp_tpl
                    + "	<select ng-disabled=\"" + obj.disabled + "\"  class=\"dt_select\" style=\"width:100%\"  id=\""
                    + uid
                    + "\"   multiple chosen  data-placeholder-text-multiple=\"'请选择...'\"    disable-search=\""
                    + obj.disable_search
                    + "\" search_contains=\"true\"  class=\"chosen-select\" no-results-text=\"'没有找到相应条目'\" ng-model=\"meta."
                    + obj.dataSel
                    + "\"  data-placeholder-text-single=\"'请选择...'\" ng-options=\"item.name for item in meta."
                    + obj.dataOpt + "\"> ";
                tmp_tpl = tmp_tpl + "		<option value=\"\"></option> ";
                tmp_tpl = tmp_tpl + "	</select> ";
                tmp_tpl = tmp_tpl + "</div> ";
                tmp_tpl = tmp_tpl + "</div> ";
                formhtml = formhtml + tmp_tpl;
            } else if (obj.type == "dashed") {
                formhtml = formhtml + "<div class=\"hr-line-dashed\"></div>";
            } else if (obj.type == "dashedword") {
                formhtml = formhtml + "<div><table style=\"width:100%\">";
                formhtml = formhtml + "<tr><td style=\"width:50%\"><div class=\"hr-line-dashed\"></div></td>   ";
                formhtml = formhtml + "<td style=\"color:#C0C0C0;width:80px;padding-left:10px;padding-right:10px\">" + obj.label + "</td>  ";
                formhtml = formhtml + "<td style=\"width:50%\"><div class=\"hr-line-dashed\"></div></td>  ";
                formhtml = formhtml + "</tr></table></div>";
            } else if (obj.type == "textarea") {
                var required_col = "";
                if (obj.required) {
                    required_col = "required";
                }
                var maxlength_col = "";
                if (typeof (obj.maxlength) != "undefined" && obj.maxlength > 0) {
                    maxlength_col = "ng-maxlength=\"" + obj.maxlength + "\"";
                }
                var height_col = "";
                if (typeof (obj.height) != "undefined" && obj.height.length > 0) {
                    height_col = "style=\"height:" + obj.height + "\"";
                }
                tmp_tpl = tmp_tpl + "<div class=\"form-group\">";
                tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\">    ";
                tmp_tpl = tmp_tpl
                    + "	<textarea "
                    + height_col
                    + " ng-disabled=\""
                    + obj.disabled
                    + "\" type=\""
                    + obj.sub_type
                    + "\" "
                    + required_col
                    + "  "
                    + maxlength_col
                    + "  class=\"form-control ng-pristine ng-untouched ng-valid ng-empty\" placeholder=\""
                    + obj.placeholder + "\" name=\"" + obj.name
                    + "\" ng-model=\"meta.item." + obj.ng_model
                    + "\" > </textarea>";
                tmp_tpl = tmp_tpl
                    + "	<div class=\"text-danger\" ng-if=\"myForm."
                    + obj.name + ".$dirty && myForm." + obj.name
                    + ".$invalid\"> ";
                tmp_tpl = tmp_tpl + "		<span ng-if=\"myForm." + obj.name
                    + ".$error.required\"> 输入不能为空 </span> ";
                tmp_tpl = tmp_tpl + "		<span ng-show=\"myForm." + obj.name
                    + ".$error.maxlength\">不能超过" + obj.maxlength
                    + "个字符</span> ";
                tmp_tpl = tmp_tpl + "	</div> ";
                tmp_tpl = tmp_tpl + "</div> ";
                tmp_tpl = tmp_tpl + "</div> ";
                formhtml = formhtml + tmp_tpl;
            } else if (obj.type == "datetime") {
                tmp_tpl = tmp_tpl + "<div class=\"form-group\"> ";
                tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\"> ";
                tmp_tpl = tmp_tpl
                    + "	<input ng-disabled=\"" + obj.disabled + "\" date-time time-zone=\"Asia/Hong_Kong\" ng-model=\"meta."
                    + obj.ng_model
                    + "\" date-change=\"changeDate\" view=\"date\" auto-close=\"true\" min-view=\"date\" format=\"YYYY-MM-DD\" class=\"form-control\" type=\"datetime\">";
                tmp_tpl = tmp_tpl + "</div>";
                tmp_tpl = tmp_tpl + "</div>";
                formhtml = formhtml + tmp_tpl;
            } else if (obj.type == "picupload") {
                tmp_tpl = tmp_tpl + "<div class=\"form-group\"> ";
                tmp_tpl = tmp_tpl + "	<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "	<div class=\"col-sm-10\"> ";
                tmp_tpl = tmp_tpl
                    + "		<div class=\"dropzone\" drop-zone dzconfig=\"meta."
                    + obj.conf
                    + "\" dzeventHandlers=\"dtldzevent\" enctype=\"multipart/form-data\"> ";
                tmp_tpl = tmp_tpl
                    + "			<div id=\"dropzone1\" class=\"fallback\"> ";
                tmp_tpl = tmp_tpl
                    + "				<input name=\"file\" type=\"file\" multiple=\"\" /> ";
                tmp_tpl = tmp_tpl + "			</div> ";
                tmp_tpl = tmp_tpl + "		</div> ";
                tmp_tpl = tmp_tpl + "	</div> ";
                tmp_tpl = tmp_tpl + "</div>";
                formhtml = formhtml + tmp_tpl;
            } else if (obj.type == "fileupload") {
                tmp_tpl = tmp_tpl + "<div class=\"form-group\"> ";
                tmp_tpl = tmp_tpl + "	<label class=\"col-sm-2 control-label\">"
                    + need_col + obj.label + ":</label> ";
                tmp_tpl = tmp_tpl + "	<div class=\"col-sm-10\"> ";
                tmp_tpl = tmp_tpl
                    + "		<div class=\"dropzone\" drop-zone dzconfig=\"meta."
                    + obj.conf
                    + "\" dzeventHandlers=\"dtldzevent2\" enctype=\"multipart/form-data\"> ";
                tmp_tpl = tmp_tpl
                    + "			<div id=\"dropzone2\" class=\"fallback\"> ";
                tmp_tpl = tmp_tpl
                    + "				<input name=\"file\" type=\"file\" multiple=\"\" /> ";
                tmp_tpl = tmp_tpl + "			</div> ";
                tmp_tpl = tmp_tpl + "		</div> ";
                tmp_tpl = tmp_tpl + "	</div> ";
                tmp_tpl = tmp_tpl + "</div>";
                formhtml = formhtml + tmp_tpl;
            }
        }
    }
    formhtml = formhtml + " </form> ";
    $timeout(function () {
        var tplhtml = $compile(formhtml);
        var $dom = tplhtml($scope);
        var ct = document.getElementById('formct');
        angular.element(ct).append($dom);
        // $timeout(
        //     function () {
        //         // 设置select全宽度
        //         for (var i = 0; i < select_ids.length; i++) {
        //             document.getElementById(select_ids[i] + "_chosen").style.width = "100%";
        //         }
        //     }, 10);
    }, 30);
    //########################################################本段为资产管理所需########################################################
    $scope.$watch('meta.extitems', function (newValue, oldValue) {
        var extitemshtml = " <form id=\"formctcust\" class=\"form-horizontal m-t-md\" name=\"myForm2\" novalidate compile=\"template\" action=\"\">";
        var extitems = newValue;
        extitemshtml = extitemshtml + "<div><table style=\"width:100%\">";
        extitemshtml = extitemshtml + "<tr><td style=\"width:50%\"><div class=\"hr-line-dashed\"></div></td>   ";
        extitemshtml = extitemshtml + "<td style=\"color:#C0C0C0;width:80px;padding-left:10px;padding-right:10px\">扩展属性</td>  ";
        extitemshtml = extitemshtml + "<td style=\"width:50%\"><div class=\"hr-line-dashed\"></div></td>  ";
        extitemshtml = extitemshtml + "</tr></table></div>";
        if (angular.isDefined(extitems)) {
            for (var i = 0; i < extitems.length; i++) {
                var obj = extitems[i];
                var need_col = "";
                if (obj.ifneed == "1") {
                    need_col = "<span class=\"text-danger\">*</span>";
                }
                if (obj.inputtype == "inputint") {
                    var required_col = "";
                    if (obj.ifneed == "1") {
                        required_col = "required";
                    }
                    var maxlength_col = "";
                    if (typeof (obj.maxlength) != "undefined" && obj.maxlength > 0) {
                        maxlength_col = "ng-maxlength=\"" + obj.maxlength + "\"";
                    }
                    var tmp_tpl = "";
                    tmp_tpl = tmp_tpl + "<div class=\"form-group\">";
                    tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                        + need_col + obj.attrname + ":</label> ";
                    tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\">    ";
                    tmp_tpl = tmp_tpl
                        + "	<input ng-disabled=\""
                        + obj.disabled
                        + "\" type=\"number\" "
                        + required_col
                        + "  "
                        + maxlength_col
                        + "  class=\"form-control ng-pristine ng-untouched ng-valid ng-empty\" placeholder=\"请输入内容\" name=\"" + obj.attrname
                        + "\" ng-model=\"meta.item." + obj.attrcode + "\" > ";
                    tmp_tpl = tmp_tpl
                        + "	<div class=\"text-danger\" ng-if=\"myForm2."
                        + obj.attrcode + ".$dirty && myForm2." + obj.attrcode
                        + ".$invalid\"> ";
                    tmp_tpl = tmp_tpl + "		<span ng-if=\"myForm2." + obj.attrcode
                        + ".$error.required\"> 输入不能为空 </span> ";
                    tmp_tpl = tmp_tpl + "		<span ng-show=\"myForm2." + obj.attrcode
                        + ".$error.maxlength\">不能超过" + obj.maxlength
                        + "个字符</span> ";
                    tmp_tpl = tmp_tpl + "</div> ";
                    tmp_tpl = tmp_tpl + "</div> ";
                    tmp_tpl = tmp_tpl + "</div> ";
                    extitemshtml = extitemshtml + tmp_tpl;
                } else if (obj.inputtype == "inputstr") {
                    var required_col = "";
                    if (obj.ifneed == "1") {
                        required_col = "required";
                    }
                    var maxlength_col = "";
                    if (typeof (obj.maxlength) != "undefined" && obj.maxlength > 0) {
                        maxlength_col = "ng-maxlength=\"" + obj.maxlength + "\"";
                    }
                    var tmp_tpl = "";
                    tmp_tpl = tmp_tpl + "<div class=\"form-group\">";
                    tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
                        + need_col + obj.attrname + ":</label> ";
                    tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\">    ";
                    tmp_tpl = tmp_tpl
                        + "	<input ng-disabled=\""
                        + obj.disabled
                        + "\" type=\"text\" "
                        + required_col
                        + "  "
                        + maxlength_col
                        + "  class=\"form-control ng-pristine ng-untouched ng-valid ng-empty\" placeholder=\"请输入内容\" name=\"" + obj.attrname
                        + "\" ng-model=\"meta.item." + obj.attrcode + "\" > ";
                    tmp_tpl = tmp_tpl
                        + "	<div class=\"text-danger\" ng-if=\"myForm2."
                        + obj.attrcode + ".$dirty && myForm2." + obj.attrcode
                        + ".$invalid\"> ";
                    tmp_tpl = tmp_tpl + "		<span ng-if=\"myForm2." + obj.attrcode
                        + ".$error.required\"> 输入不能为空 </span> ";
                    tmp_tpl = tmp_tpl + "		<span ng-show=\"myForm2." + obj.attrcode
                        + ".$error.maxlength\">不能超过" + obj.maxlength
                        + "个字符</span> ";
                    tmp_tpl = tmp_tpl + "</div> ";
                    tmp_tpl = tmp_tpl + "</div> ";
                    tmp_tpl = tmp_tpl + "</div> ";
                    extitemshtml = extitemshtml + tmp_tpl;
                }
            }
            extitemshtml = extitemshtml + " </form> ";
            if (extitems.length > 0) {
                $timeout(function () {
                    var tplhtml2 = $compile(extitemshtml);
                    var $dom2 = tplhtml2($scope);
                    var ct2 = document.getElementById('formitem');
                    var ct3 = document.getElementById('formctcust');
                    angular.element(ct3).remove();
                    angular.element(ct2).append($dom2);
                }, 20);
            }
        }
    });
    $scope.sure = function () {
        meta.sure($uibModalInstance, $scope);
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    if (typeof ($scope.meta.init) != "undefined") {
        $scope.meta.init($scope);
    }
}

/** ******************************modal end************************* */
/** ******************************simple tool table模版************************ */
// 原因:tooltable模版的table的instance无法初始化
function buildSimpleToolTableTpl() {
    return '<div class="wrapper wrapper-content animated fadeInRight">  <ng-include src="\'views/Template/simpleTool.html\'"></ng-include>   <div class="row"> <div class="col-lg-12"><div class="ibox"><div class="ibox-content">	<table datatable="ed" dt-options="dtOptions" dt-instance="dtInstance" dt-columns="dtColumns" dt-column-defs="dtColumnDefs" class="table table-hover"></table></div></div></div></div> </div> ';
}

/** ******************************simple tool table模版结束************************ */
