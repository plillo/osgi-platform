var userUI=angular.module('userUI', ['ui.bootstrap', 'ui.router', 'ui.navbar','remoteValidation'])
    userUI.config(function ($stateProvider, $urlRouterProvider) {
    	
        // For any unmatched url, redirect to /state1 
        $urlRouterProvider.otherwise("/");
 
        // Now set up the states 
        $stateProvider
	        .state('home', {
	                url: "/",
	                templateUrl: "partials/home.html"
	            })
            .state('login', {
                url: "/login",
                templateUrl: "partials/login.html",controller: 'LoginController',controllerAs: 'vm'
            })
            .state('user', {
                url: "/user",
                templateUrl: "partials/userPage.html",controller: 'UserController'
            })
            .state('contact', {
                url: "/contact",
                templateUrl: "partials/contact.html"
            })
            .state('product1', {
                url: "/product1",
                templateUrl: "partials/product1.html"
            })
            .state('register', {
                url: "/register",
                templateUrl: "partials/register.html",controller: 'RegisterController'
            });
    })