mod1.controller('PaletteCtrl', function($scope, $http) {
    $scope.aa = 'f';
    
    $scope.articles = ['Alpha', 'Beta', 'Gamma'];

    //httpbin allows cross site requests
    $scope.restTest = function() {
        $http.get('https://httpbin.org/get')
            .then(function(response) {
                $scope.restTestResponse = response.data;
            });
    };

    $scope.restTestWithError = function() {
        $http.get('https://httpbin.org/get')
            .then(function successCallback(response) {
                $scope.restTestResponse = response.data;
              }, function errorCallback(response) {
                  $scope.restTestResponse = response.data;
              });
    };
});
