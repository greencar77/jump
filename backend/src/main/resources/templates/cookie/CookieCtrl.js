mod1.controller('CookieCtrl', function($scope, $cookieStore) {
    $cookieStore.put('fruit','Apple');
    $cookieStore.put('flower','Rose');
    $scope.myFruit = $cookieStore.get('fruit');
    
    $scope.doIt = function() {
    	alert($cookieStore.get('fruit'));
    }
});
