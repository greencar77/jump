
mod1.controller('MultiElementsCtrl', function($scope, $uibModal) {

    $scope.buttonPush = function() {
        $uibModal.open({
            templateUrl: 'views/popup.html',
            controller: 'PopupCtrl',
        });
    };
    $scope.buttonAlert = function() {
        alert('aaa');
    };
});
