function opsxttjCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
                    $log, notify, $scope, $http, $rootScope, $uibModal, $window) {
    $scope.item = {};
    $http.post($rootScope.project + "/api/ops/opsNode/ext/dashboard.do", {})
        .success(function (res) {
            if (res.success) {
                $scope.item = res.data;
                var ticks = res.data.os_chart_meta;
                var db_ticks = res.data.db_chart_meta;
                var mid_ticks = res.data.mid_chart_meta;
                var data = res.data.os_chart_data;
                var dataset = [{
                    label: "数量",
                    data: data,
                    color: "#5482FF"
                }]
                var db_dataset = [{
                    label: "数量",
                    data: res.data.db_chart_data,
                    color: "#5482FF"
                }]
                var mid_dataset = [{
                    label: "数量",
                    data: res.data.mid_chart_data,
                    color: "#5482FF"
                }]
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
                        ticks: ticks
                    },
                    legend: {
                        show: true
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "数量: %y"
                    }
                };
                var db_barOptions = {
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
                        ticks: db_ticks
                    },
                    legend: {
                        show: true
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "数量: %y"
                    }
                };
                var mid_barOptions = {
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
                        ticks: mid_ticks
                    },
                    legend: {
                        show: true
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "数量: %y"
                    }
                };
                $scope.charts = {};
                $scope.charts.flotChartData = dataset;
                $scope.charts.flotBarOptions = barOptions;
                $scope.charts.dbflotChartData = db_dataset;
                $scope.charts.dbflotBarOptions = db_barOptions;
                $scope.charts.midflotChartData = mid_dataset;
                $scope.charts.midflotBarOptions = mid_barOptions;
            } else {
                notify({
                    message: res.message
                });
            }
        })
};
app.register.controller('opsxttjCtl', opsxttjCtl);