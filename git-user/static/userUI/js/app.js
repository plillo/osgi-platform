angular.module("userUI", ['ngRoute','remoteValidation'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.
  when('/', {templateUrl: 'partials/register.html', controller: 'RegisterController'}).
/*  when('/a', {templateUrl: 'partials/a.html', controller: 'AController'}).
  when('/b', {templateUrl: 'partials/b.html', controller: 'BController'}).*/
  otherwise({redirectTo: '/'}); 
}]);
