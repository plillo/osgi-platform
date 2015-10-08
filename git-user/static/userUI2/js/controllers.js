	
userUI.controller('RegisterController', ['$scope', '$http', '$window', '$rootScope', '$location','oauthlink',
  function($scope, $http, $window, $rootScope, $location,oauthlink) {
    
    $scope.newUser = {};
 
    $http.get("/users/1.0/oauth").success(function(response) {$scope.ouths = response;});
    
    $scope.callouth = oauthlink.callouth;
    $scope.createUser = function() {
    	//alert($scope.form.$valid);
		if($scope.validate() == true) {
			$http.post('/users/1.0', $scope.newUser).success(function(data) {
				//alert("data.isValid="+data.isValid);
				if (data.created) {
					$scope.okmessage = "New user created";
					$rootScope.user = data.user.email;
					//alert($rootScope.user);
					$location.path("/user");
				}
			}).error(function() {
				$scope.righterrormessage = "Failed to create user";
			});
		}
	}
    
    
	$scope.validate = function() {
		if ($scope.form.$valid && ($scope.newUser.password != $scope.repeatPassword)) {
			return false;
		}

		if ($scope.form.$valid && ($scope.newUser.email == undefined || $scope.newUser.email.lenght == 0)) {
			return false;
		}
		
		if (!$scope.form.$valid) {
			return false;
		}

		return true;
	}
    
	
	
  }])
  .controller('LoginController', ['$scope', '$location', '$sce', '$http', '$rootScope', '$window','oauthlink',
                                  function($scope, $location, $sce, $http, $rootScope, $window, oauthlink) {
	  
	  $scope.logindetails = {};

	  $scope.go = function (path) {
		  $location.path(path);
	  };

	  $http.get("/users/1.0/oauth").success(function(response) {$scope.ouths = response;});
 
	  $scope.callouth = oauthlink.callouth;

	  $scope.passwordTooltip = $sce.trustAsHtml('Insert password used during registration');
	  $scope.userTooltip = $sce.trustAsHtml('Insert username, email or mobile used during registration');

	  $scope.login = function() {

		  if ($scope.validate()){
			  $http.get('/users/1.0/login?identificator='+$scope.logindetails.user+'&password='+$scope.logindetails.password)
			  .success(function(data, status, headers, config) {
				  $location.path("/user");
				  
				  $rootScope.user=data.firstName;
				  
			  }).error(function(data, status, headers, config) {
				  if (status=="403"){
					  $scope.errormessage = "Invalid credentials!";  
				  }
				 
			  });
		  }
	  };

	  $scope.validate = function(){
		  return $scope.loginForm.$valid;
	  }
	    

  }])
  .controller('UserController', ['$scope', '$location', '$sce', '$http', '$rootScope',
                                 function($scope, $location, $sce, $http, $rootScope) {

	  $scope.logout = function() {

		  $http.get('/users/1.0/logout')
		  .success(function(data, status, headers, config) {
			  $rootScope.user=null;
			  $location.path("/");
		  }).error(function() {
			  $location.path("/");
		  });
	  };
  }]).controller('NavigationController', ['$scope', '$location', '$sce', '$http', '$rootScope',
                                          function($scope, $location, $sce, $http, $rootScope) {
	  
                                          
	  $scope.logout = function() {

		  $http.get('/users/1.0/logout')
		  .success(function(data, status, headers, config) {
			  $rootScope.user=null;
			  $location.path("/");
		  }).error(function() {
			  $location.path("/");
		  });
	  };
	  
	  $scope.checkLogin=function(){
	  if($rootScope.user==null){
	  return true;
	  }
	  else
		  return false;
	  };

				$scope.tree = [{
				  name: "Product1",
				  link: "product1",
						  subtree: [{
						    name: "Product1A",
						    link: "Product1A",
						    subtree: [{name: "Product1AA",
						    link: "state1"}]
						  }, {
						    name: "Product2A",
						    link: "state2"
						  }]
				}, {
				  name: "Product3",
				  link: "#",
						  subtree: [{
						    name: "Product3A",
						    link: "#"
						  }]
				}, {
				  name: "divider",
				  link: "#"
				
				}, {
				  name: "Product3",
				  link: "#"
				}, {
				  name: "divider",
				  link: "#"
				}, {
				  name: "Product4",
				  link: "#"
				}];
				}]);
