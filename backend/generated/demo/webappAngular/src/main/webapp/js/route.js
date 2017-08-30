
mod1.config(function($routeProvider) {
    $routeProvider
    .when("/tabs", {
        templateUrl : "views/tabs.html"
    })
    .when("/directives", {
        templateUrl : "views/directives.html"
    })
    .when("/multiElements", {
        templateUrl : "views/multiElements.html"
    })
    .when("/palette", {
        templateUrl : "views/palette.html"
    })
    .when("/popup", {
        templateUrl : "views/popup.html"
    })
    ;
});
