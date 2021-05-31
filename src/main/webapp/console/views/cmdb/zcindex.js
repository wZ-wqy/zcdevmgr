function zcindexCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                    $log, notify, $scope, $http, $rootScope, $uibModal, $window, $state) {
    $scope.item = {};

    $http.post($rootScope.project + "/api/zc/dashboard/ext/getstatistics.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.item = res.data;
            } else {
                notify({
                    message: res.message
                });
            }
        })



    $scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
        'responsive', false) .withOption('paging', false).withOption("bInfo", false).withOption("searching", false).withOption('createdRow', function (row) {
        // Recompiling so we can bind Angular,directive to the
        $compile(angular.element(row).contents())($scope);
    });
    $scope.dtInstance = {}


    function renderName(data, type, full) {
        if (data=="idle") {
            return "闲置";
        }else if(data=="scrap") {
            return "报废";
        }else if(data=="inuse") {
            return "在用";
        }else if(data=="borrow") {
            return "借用";
        }else if(data=="stopuse") {
            return "停用";
        }else if(data=="repair") {
            return "维修";
        }else if(data=="allocation") {
            return "调拨";
        }else{
            return data;
        }
    }

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('类型').withOption(
            'sDefaultContent', '').renderWith(renderName),
        DTColumnBuilder.newColumn('cnt').withTitle('数量').withOption(
            'sDefaultContent', ''),
        DTColumnBuilder.newColumn('networth').withTitle('净值(元)').withOption(
            'sDefaultContent', '')

    ]

    function queryAssetsProfile(){
        var ps={};
        $http.post($rootScope.project + "/api/zc/dashboard/ext/getAssetsRecycle.do", ps)
            .success(function (res) {
                if (res.success) {
                    $scope.dtOptions.aaData = res.data;
                    var data = [
                    ];
                    for(var i=0;i<res.data.length;i++){
                        var e={};

                        e.label=renderName(res.data[i].name,null,null);
                        e.data=res.data[i].cnt;
                        data.push(e);
                    }
                    var pipOptions = {
                        series: {
                            pie: {
                                show: true
                            }
                        },
                        grid: {
                            hoverable: true,
                            borderWidth: 2,
                            backgroundColor: {
                                colors: ["#ffffff", "#EDF5FF"]
                            }
                        },
                        legend: {
                            show: false
                        },
                        tooltip: false
                    };
                    $scope.charts.flotChartData = data;
                    $scope.charts.flotpipOptions = pipOptions;

                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }

    function queryAssetsCategory(){
        var ps={};
        $http.post($rootScope.project + "/api/zc/dashboard/ext/getAssetsCategory.do", ps)
            .success(function (res) {
                if (res.success) {

                    var cat_ticks = res.data.cat_chart_meta;
                    var cat_data = res.data.cat_chart_data;
                    var cat_dataset = [{
                        label: "资产分类统计",
                        data: cat_data,
                        color: "#5482FF"
                    }];
                    var catbarOptions = {
                        series: {
                            bars: {
                                show: true,
                                barWidth: 0.6,
                                fill: true,
                                fillColor: {
                                    colors: [{
                                        opacity: 0.8
                                    }, {
                                        opacity: 0.8
                                    }]
                                },
                                align: "center",
                                numbers: {
                                    show: true
                                }
                            }
                        },
                        bars: {
                            align: "center",
                            fill: 1,
                            barWidth: 0.5
                        },
                        grid: {
                            hoverable: true,
                            borderWidth: 2,
                            backgroundColor: {
                                colors: ["#ffffff", "#EDF5FF"]
                            }
                        },
                        xaxis: {
                            axisLabel: "",
                            axisLabelUseCanvas: true,
                            axisLabelFontSizePixels: 12,
                            axisLabelFontFamily: 'Verdana, Arial',
                            axisLabelPadding: 10,
                            tickSize: 2,
                            ticks: cat_ticks
                        },
                        legend: {
                            show: false
                        },
                        tooltip: true,
                        tooltipOpts: {
                            content: "数量: %y"
                        }
                    };
                    $scope.charts.catflotChartData = cat_dataset;
                    $scope.charts.catflotBarOptions = catbarOptions

                } else {
                    notify({
                        message: res.message
                    });
                }
            })
    }
    queryAssetsProfile();
    queryAssetsCategory();


};
app.register.controller('zcindexCtl', zcindexCtl);