mod1.controller('MainCtrl', function($scope) {

    $scope.config = new Object();

    //$scope.config.projectName = 'aaaa'; //TODO remove

    $scope.refresh = function() {
        if ($scope.config.projectName == null || $scope.config.projectName == '') {
            alert("No project name is set!");
            return;
        }
        $scope.spec = JSON.stringify($scope.config, null, '  ');
    };
});
