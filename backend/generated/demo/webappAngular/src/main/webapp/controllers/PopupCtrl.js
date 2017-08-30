
mod1.controller('PopupCtrl', function($scope, $uibModalInstance) {
    $scope.buttonOk = function() {
        $uibModalInstance.close('aaa');
    };
    $scope.buttonCancel = function() {
        $uibModalInstance.dismiss('cancel');
    };
});
