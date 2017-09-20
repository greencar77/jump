mod1.controller('AngularAppCtrl', function($scope, $controller) {
    $controller('MainCtrl', {$scope: $scope});

    $scope.config.level = 'AngularAppSpec';

    $scope.appGenMethods = [
        'buildAppTutti',
        'buildAppFeatures',
        'buildAppEmpty',
    ];

    $scope.$watch('config.bootstrapUi', function() {
        if ($scope.config.bootstrapUi) {
            $scope.config.bootstrapUiSpec = new Object();
        } else {
            delete $scope.config.bootstrapUiSpec;
        }
    });

});
