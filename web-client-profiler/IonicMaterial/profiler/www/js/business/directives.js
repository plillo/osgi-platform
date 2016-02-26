'use strict';
angular.module('business.directives', []);

//ADD 'appSearchCategories' directive
//...................................
angular.module('business.directives').directive('appBusinessForm', function(business) {
	return {
		replace: false,
		scope: true,
		templateUrl : 'templates/business/business-form.html',
		controller: function($scope, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.categoriesSelected = [];

	       	// insert here scope-functions
			// ...
			$scope.send = function() {
				// TODO populate object
				// ...
				var selected = [];
				for(var i = 0; i < $scope.categoriesSelected.length; i++){
					selected.push($scope.categoriesSelected[i].uuid);
				}
				var pars = {
					name: $scope.name,
					address: $scope.address,
					city: $scope.city,
					cap: $scope.cap,
					pIva: $scope.pIva,
					fiscalCode: $scope.fiscalCode,
					description: $scope.description,	
					categories: selected
				}
				alert(JSON.stringify(pars));

				business.createBusiness(pars).then(
				   function successCallback(response) {
					   alert('OK');
		           },
		           function errorCallback(response) {
			    	   alert('KO');
		           });
			}
	    },
		link: function(scope, element, attributes){
 			// EVENTS BINDING
 			element.find('#mybutton-send').bind('click', scope.send);
		}
	};
});

//ADD 'appSearchCategories' directive
//...................................
angular.module('business.directives').directive('appSearchCategories', function(business) {
	return {
		replace: false,
		scope: {
			categoriesSelected: '=categoriesSelectedAttr'
		},
		templateUrl : 'templates/business/search-categories.html',
		controller: function($scope, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.results = [];
			
	       	// insert here scope-functions
			// ...
			$scope.select = function(index) {
				$scope.categoriesSelected.push($scope.results[index]);
				$scope.results.splice(index,1);
			}
			
			$scope.unselect = function(index) {
				$scope.results.push($scope.categoriesSelected[index]);
				$scope.categoriesSelected.splice(index,1);
			}
			
			$scope.change = function(event) {
				var searchString = $(event.target).val();

				business.getBusinessByCategory(searchString).then(
				   function successCallback(response) {
					   $scope.results = response.data;
		           },
		           function errorCallback(response) {
			    	  alert('KO');
		           });
			}
	    },
		link: function(scope, element, attributes){
 			// EVENTS BINDING
 			element.find('#search').bind('change', scope.change);
		}
	};
});

//ADD 'appSearchBusiness' directive
//.................................
angular.module('business.directives').directive('appSearchBusiness', function(business, ionicMaterialMotion, ionicMaterialInk) {
	return {
		replace: false,
		scope: {},
		templateUrl : 'templates/business/search-business.html',
		controller: function($scope, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.results = [];
			
	       	// insert here scope-functions
			// ...
			$scope.select = function(index) {
			};
			
			$scope.change = function(event) {
				var searchString = $(event.target).val();

				business.getBusiness(searchString).then(
				   function successCallback(response) {
					   $scope.results = response.data.businesses;
					   
					   ionicMaterialMotion.fadeSlideInRight();
				       ionicMaterialInk.displayEffect();
		           },
		           function errorCallback(response) {
			    	  alert('KO');
		           });
			};
			
			$scope.follow = function(uuid) {
				business.followBusiness(uuid).then(
				    function successCallback(response) {
				    	alert('OK!');
		            },
		            function errorCallback(response) {
		            	alert('KO');
		            });
			}
			

	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			element.find('#search').bind('change', scope.change);
		}
	};
});

//ADD 'appFollowedBusiness' directive
//...................................
angular.module('business.directives').directive('appFollowedBusiness', function(business, ionicMaterialMotion, ionicMaterialInk) {
	return {
		replace: false,
		scope: {},
		templateUrl : 'templates/business/followed-business.html',
		controller: function($scope, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.results = [];
			
	       	// insert here scope-functions
			// ...
			$scope.select = function(index) {
			};
			
			$scope.load = function() {
				business.getFollowedBusiness().then(
				   function successCallback(response) {
					   $scope.results = response.data.businesses;
		           },
		           function errorCallback(response) {
			    	  alert('KO');
		           });
			};
			$scope.load();
	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			//element.find('#search').bind('change', scope.change);
		}
	};
});

