mod1.controller('MainCtrl', function($scope) {

    $scope.specs = [
        {className: 'MavenProjSpec', title: 'Maven application'},
        {className: 'WebAppSpec', title: 'Web application', template: 'views/webapp.html'},
        {className: 'AngularAppSpec', title: 'Angular application'},
    ];

    //$scope.selectedSpec = $scope.specs[0];

    $scope.selectedSpec = null;

    $scope.$watch('selectedClass', function() {
        //$scope.selview = 'views/webapp.html';
        //alert($scope.specs.filter(x => x.className === $scope.selectedClass)[0].template);
        $scope.selview = $scope.specs.filter(x => x.className === $scope.selectedClass)[0].template;
    });

        /*
            $scope.config = new Object();

            $scope.config.projectName = 'aaaa'; //TODO remove

            $scope.config.platform = "java";

            $scope.refresh = function() {
                if ($scope.config.projectName == null || $scope.config.projectName == '') {
                    alert("No project name is set!");
                    return;
                }
                if ($scope.config.platform == null || $scope.config.platform == '') {
                    alert("No platform is set!");
                    return;
                }
                $scope.spec = JSON.stringify($scope.config, null, '  ');
            };

            $scope.$watch('config.platform', function() {

                if ($scope.config.platform == 'java') {
                    delete $scope.config.javascript;
                }

                if ($scope.config.platform == 'javascript') {
                    delete $scope.config.java;
                    $scope.config.javascript = new Object();

                    //if ($scope.config.javascript.angular) {
                        $scope.config.javascript.angularVersion = "1.5.7";
                    //}

                    $scope.config.javascript.angularSource = "STANDALONE";
                }
            });*/
});
