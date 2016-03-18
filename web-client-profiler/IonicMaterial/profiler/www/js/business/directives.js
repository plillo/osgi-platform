'use strict';
angular.module('business.directives', []);

//ADD 'appSearchCategories' directive
//...................................
angular.module('business.directives').directive('appBusinessForm', function(business) {
	return {
		replace: false,
		scope: true,
		templateUrl : 'templates/business/business-form.html',
		controller: function($scope, $state, $window, $element){
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
				business.createBusiness(pars).then(
				   function successCallback(response) {
					   $state.go('app.business-manager');
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

				business.getCategoriesBySearchKeyword(searchString).then(
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
		controller: function($scope, $state, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.results = [];
			
	       	// insert here scope-functions
			// ...
			$scope.select = function(index) {
			};
			
			$scope.change = function(event) {
				var searchString = $(event.target).val();

				business.getNotFollowedBusiness(searchString).then(
				    function successCallback(response) {
					    $scope.results = response.data.businesses;
		            },
		            function errorCallback(response) {
			    	    alert('KO');
		            });
			};
			
			$scope.follow = function(uuid) {
				business.followBusiness(uuid).then(
				    function successCallback(response) {
				    	$state.go('app.subscriptions');
		            },
		            function errorCallback(response) {
		            	alert('KO');
		            });
			};
	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			element.find('#search').bind('change', scope.change);
		}
	};
});

//ADD 'appFollowedBusiness' directive
//...................................
angular.module('business.directives').directive('appFollowedBusiness', function(business) {
	return {
		replace: false,
		scope: {},
		templateUrl : 'templates/business/followed-business.html',
		controller: function($scope, $state, $window, $element){
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
			
			$scope.unfollow = function(uuid) {
				business.unfollowBusiness(uuid).then(
				    function successCallback(response) {
				    	$scope.load();
		            },
		            function errorCallback(response) {
		            	alert('KO');
		            });
			};
			
			$scope.configure = function(uuid, tostate) {
				$state.go(tostate, {uuid:uuid});
			};

	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			//element.find('#search').bind('change', scope.change);
		}
	};
});


//ADD 'appOwnedBusiness' directive
//................................
angular.module('business.directives').directive('appOwnedBusiness', function(business) {
	return {
		replace: false,
		scope: {},
		templateUrl : 'templates/business/owned-business.html',
		controller: function($scope, $state, $window, $element){
	       	// scope-properties
			$scope.results = [];
			
	       	// scope-functions
			$scope.load = function() {
				business.getOwnedBusiness().then(
				   function successCallback(response) {
					   $scope.results = response.data.businesses;
		           },
		           function errorCallback(response) {
			    	  alert('KO');
		           });
			};
			$scope.load();
			
			$scope.edit = function(uuid, tostate) {
				$state.go(tostate, {uuid:uuid});
			};
			
			$scope.remove = function(uuid, tostate) {
				alert('TODO: delete business: '+uuid);
				$state.go(tostate);
			};
	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			//element.find('#search').bind('change', scope.change);
		}
	};
});

//ADD 'appOwnedBusinessForm' directive
//....................................
angular.module('business.directives').directive('appOwnedBusinessForm', function() {
	return {
		replace: false,
		scope: {
			uuid: '@uuid'
		},
		templateUrl : 'templates/business/owned-business-form.html',
		controller: function($scope, $state, $rootScope, $http, $window, $element, administration, business){
	       	// scope-properties
			$scope.updatingBusiness = undefined;

	       	// scope-functions
			$scope.send = function() {
				//cloning object in order to parse and send
				var data = (JSON.parse(JSON.stringify($scope.updatingBusiness)));
				business.updateBusiness($scope.uuid, data).then(
			    	function successCallback(response) {
			    		$state.go('app.business-manager');
			    	},
			    	function errorCallback(response) {
			    		alert('KO');
			    	});
			};
			
			$scope.load = function() {
				business.getBusinessByUUID($scope.uuid).then(
			    	function successCallback(response) {
			    		$scope.updatingBusiness = response.data;
			    		// Modello per adattamento campi composti:
			    		// $scope.updatingBusiness.<JSON FIELD> = JSON.stringify($scope.updatingBusiness.<JSON FIELD>);
			    	},
			    	function errorCallback(response) {
			    		alert('KO');
			    	});
			};
			$scope.load();
	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			element.find('#send').bind('click', scope.send);
		}
	};
});

//ADD 'haBusinessForm' directive
//..............................
angular.module('business.directives').directive('appConfigureBusinessForm', function() {
	return {
		replace: false,
		scope: {
			uuid: '@uuid'
		},
		templateUrl : 'templates/business/configure-business-form.html',
		controller: function($scope, $rootScope, $http, $window, $element, administration, business){
	       	// scope-properties
			$scope.configuringBusiness = undefined;

	       	// scope-functions
			$scope.send = function() {
				//cloning object in order to parse and send
				var pars = (JSON.parse(JSON.stringify($scope.configuringBusiness)));
				$http.post($rootScope.urlBackend+'/businesses/1.0/businesses/by_selfConfiguration/'+$scope.uuid, pars)
					.then(
				    	function successCallback(response) {
				    	},
				    	function errorCallback(response) {
				    		alert('KO');
				    	});
			};
			
			$scope.load = function() {
				// TODO
			};
			$scope.load();
	    },
		link: function(scope, element, attributes){
			// EVENTS BINDING
			element.find('#send').bind('click', scope.send);
		}
	};
});

