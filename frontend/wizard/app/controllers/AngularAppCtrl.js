mod1.controller('AngularAppCtrl', function($scope, $controller) {
    $controller('MainCtrl', {$scope: $scope});

    $scope.config.level = 'AngularAppSpec';

});
