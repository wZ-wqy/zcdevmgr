function zbxlastdataCtl($scope, $rootScope, $sce) {
    $scope.url = $sce.trustAsResourceUrl('http://47.92.240.43:15211/zabbix.php?action=latest.view')
};
app.register.controller('zbxlastdataCtl', zbxlastdataCtl);