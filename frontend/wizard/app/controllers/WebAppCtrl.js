mod1.controller('WebAppCtrl', function($scope, $controller) {
    $controller('MavenProjCtrl', {$scope: $scope});

    $scope.targetContainerList = [
        'WILDFLY',
        'TOMCAT'
    ];

    $scope.webFrameworkList = [
        'JERSEY',
        'RESTEASY',
        'SPRING_MVC'
    ];

    $scope.config.level = 'WebAppSpec';

    $scope.$watch('config.webFramework', function() {
        if ($scope.config.webFramework == 'JERSEY') {
            $scope.config.jersey = new Object();
            //$scope.config.jersey.jerseyVersion = '1.19.4';
        } else {
            delete $scope.config.jersey;
        }
    });
});
