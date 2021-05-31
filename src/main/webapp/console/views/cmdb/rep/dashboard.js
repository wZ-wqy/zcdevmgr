function cmdbdashboardCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
                          $confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.item = {};
    $http.post($rootScope.project + "/api/base/res/rep/dashboard.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                var ticks = res.data.chart_meta;
                var data = res.data.chart_data;
                //
                // var ticks = [ [ 0, "第一周" ], [ 1, "第二周" ], [ 2, "第三周" ], [
                // 3, "第四周" ],
                // [ 4, "第五周" ], [ 5, "第六周" ][6, "第三周"] ];
                //
                // var data = [ [ 0, 11 ], //London, UK
                // [ 1, 15 ], //New York, USA
                // [ 2, 25 ], //New Delhi, India
                // [ 3, 24 ], //Taipei, Taiwan
                // [ 4, 13 ], //Beijing, China
                // [ 5, 18 ] //Sydney, AU
                // ];
                var dataset = [{
                    label: "数量",
                    data: data,
                    color: "#5482FF"
                }
                ];
                var barOptions = {
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
                        // axisLabelPadding : 10,
                        axisLabelPadding: 10,
                        tickSize: 2,
                        ticks: ticks
                    },
//						legend : {
//							noColumns : 0,
//							labelBoxBorderColor : "#000000",
//							position : "nw"
//						},
                    legend: {
                        show: false
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "数量: %y"
                    }
                };
                $scope.charts = {};
                $scope.charts.flotChartData = dataset;
                $scope.charts.flotBarOptions = barOptions;
            } else {
                notify({
                    message: res.message
                });
            }
        })
};
app.register.controller('cmdbdashboardCtl', cmdbdashboardCtl);