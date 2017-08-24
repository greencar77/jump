
mod1.config(function($routeProvider) {
    $routeProvider
        .when("/mavenproj", {
            templateUrl : 'views/mavenproj.html',
            controller: 'MavenProjCtrl'
        })
        .when("/webapp", {
            templateUrl : 'views/webapp.html',
            controller: 'WebAppCtrl'
        })
    ;
});

