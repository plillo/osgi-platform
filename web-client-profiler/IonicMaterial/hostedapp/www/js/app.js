// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'hashServices', 'starter.services', 'starter.controllers'])

.config(function($compileProvider){
  $compileProvider.imgSrcSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel):/);
})

.config(function(loggerProvider){
	loggerProvider.setUrl('http://calimero:8080/users/1.0/');
})

.config(function(brokerProvider){
	brokerProvider.initBroker('calimero', 61614);
})

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.StatusBar) {
      StatusBar.styleDefault(); // org.apache.cordova.statusbar required
    }

    console.log('hello world');
  });
})

.controller('MainCtrl', function($scope, user, logger, broker) {

	$scope.set = function() {
		var currentUser = {
			firstName: 'Gino',
			lastName: 'Bartali',
			username: 'ginone',
			email: 'gino@bartali.com',
			mobile: '3345544321',
			password: ''  
		};
		user.setCurrentUser(currentUser);
	};
  
	$scope.get = function() {
		alert(JSON.stringify(user.getCurrentUser()));
	};
  
	$scope.reset = function() {
		user.resetCurrentUser();
	};
	
	$scope.getUrlLogger = function() {
		alert(logger.getUrl());
	};
	
	$scope.getUrlBroker = function() {
		alert(broker.getUrl()+':'+broker.getPort()+' ['+(broker.isConnected()?'':'NOT ')+'CONNECTED]');
	};
	
	$scope.reload = function() {
	    window.location.replace("http://calimero:8080/");
	};
	
	$scope.exit = function() {
		window.close();
		ionic.Platform.exitApp();
	};

})