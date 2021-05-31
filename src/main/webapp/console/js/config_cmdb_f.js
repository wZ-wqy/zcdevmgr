function config_cmdb_f($stateProvider, $ocLazyLoadProvider) {
    console.log("App cmdb_f config");
    $ocLazyLoadProvider.config({
        debug: true
    });

    // 财务
    $stateProvider.state('financialmgr', {
        abstract: true,
        url: "/financialmgr",
        templateUrl: "views/common/content.html?v=" + version
    }).state('financialmgr.depreciation', {
        url: "/financialmgr_depreciation",
        data: {pageTitle: '资产折旧'},
        templateUrl: "views/cmdb/residual.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/cmdb/residual.js?v=' + version]
                }]);
            }
        }
    });

    $stateProvider.state('purchase', {
        abstract: true,
        url: "/purchase",
        templateUrl: "views/common/content.html?v=" + version
    }).state('purchase.company', {
        url: "/purchase_company",
        data: {pageTitle: '合同单位及联系人'},
        templateUrl: "views/purchase/company.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/purchase/company.js?v=' + version]
                }]);
            }
        }
    }).state('purchase.contractmgr', {
        url: "/purchase_contractmgr",
        data: {pageTitle: '合同'},
        templateUrl: "views/purchase/contractmgr.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/purchase/contractmgr.js?v=' + version]
                }]);
            }
        }
    }).state('purchase.purchase', {
        url: "/purchase_purchase",
        data: {pageTitle: '采购流程'},
        templateUrl: "views/purchase/purchaseflow.html?v=" + version,
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([{
                    serie: true,
                    files: ['views/purchase/purchaseflow.js?v=' + version]
                }]);
            }
        }
    })
}