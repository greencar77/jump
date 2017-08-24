mod1.controller('MavenProjCtrl', function($scope, $controller) {
    $controller('MainCtrl', {$scope: $scope});

    $scope.config.level = 'MavenProjSpec';
});
