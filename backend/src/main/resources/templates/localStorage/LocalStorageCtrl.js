mod1.controller('LocalStorageCtrl', function($scope) {
    $scope.save = function() {
    	localStorage.setItem("lastname", "Smith");
    	localStorage.setItem("bbb", $scope.bbb);
    }
    
    $scope.retrieve = function() {
    	$scope.x = localStorage.getItem("lastname");
    	$scope.bbbRetrieved = localStorage.getItem("bbb");
    }
});
