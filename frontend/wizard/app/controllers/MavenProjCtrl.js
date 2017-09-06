mod1.controller('MavenProjCtrl', function($scope, $controller) {
    $controller('MainCtrl', {$scope: $scope});

    $scope.config.level = 'MavenProjSpec';

    $scope.javaVersionList = [
        'V15',
        'V16',
        'V17',
        'V18',
    ];

    $scope.javaVersion = 'V18';

    $scope.springConfigBasisList = [
        'XML',
        'JAVA',
    ];

    $scope.$watch('config.featureSpring', function() {
        if ($scope.config.featureSpring) {
            $scope.config.spring = new Object();
        } else {
            delete $scope.config.spring;
        }
    });
});
