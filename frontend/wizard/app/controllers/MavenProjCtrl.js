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

    $scope.hibernateVersionList = [
        {id: 'V3_6_3', name: '3.6.3', jpa: '2.0'},
        {id: 'V4_3_0', name: '4.3.0', jpa: '2.1'},
        {id: 'V5_2_7', name: '5.2.7', jpa: '2.1'},
        {id: 'V5_2_10', name: '5.2.10', jpa: '2.1'}
    ];

    $scope.$watch('config.featureHibernate', function() {
        if ($scope.config.featureHibernate) {
            $scope.config.hibernate = new Object();
        } else {
            delete $scope.config.hibernate;
        }
    });

    $scope.entityManagerSetupStrategyList = [
        {id: 'PROGRAMMATICALLY', name: 'Programmatically'},
        {id: 'SPRING', name: 'By Spring'},
        {id: 'EJB', name: 'By EJB'},
        ]
});