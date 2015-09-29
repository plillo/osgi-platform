angular.module("userUI", ['ngRoute','remoteValidation','ui.bootstrap'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.
  when('/', {templateUrl: 'partials/login.html', controller: 'LoginController'}).
  when('/register', {templateUrl: 'partials/register.html', controller: 'RegisterController'}).
  when('/user', {templateUrl: 'partials/userPage.html', controller: 'UserController'}).
/*  when('/a', {templateUrl: 'partials/a.html', controller: 'AController'}).
  when('/b', {templateUrl: 'partials/b.html', controller: 'BController'}).*/
  otherwise({redirectTo: '/'}); 
}])
.run(function($rootScope){
	  $rootScope.user="unknown";	
});


