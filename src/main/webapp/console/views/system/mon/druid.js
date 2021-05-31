function sysMonDruidCtl($scope, $rootScope) {
    $scope.url = $rootScope.project + "/druid/"
};
app.register.controller('sysMonDruidCtl', sysMonDruidCtl);