angular.module("userUI").controller('RegisterController', ['$scope', '$http', '$window', '$rootScope', '$location',
  function($scope, $http, $window, $rootScope, $location) {
    
    $scope.newUser = {};
    
    
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
    
    $scope.googleRegistration = function() {
    	$window.location.href='https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force&state={ "authenticator" : "google" }';
    	//$window.location.href='https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force&state=google';
    	//$window.location.href='https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/Oauth/Oauth2callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force&state=google';
    	//$window.open('https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/Oauth/Oauth2callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force&state=google');
    	//$http.jsonp('https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/Google/Oauth2callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force?callback=JSON_CALLBACK"');
    	//.success(function(){alert('ok')});
    }
    
    $scope.facebookRegistration = function() {
    	$window.location.href='https://www.facebook.com/dialog/oauth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=492601017561308&approval_prompt=force&state={ "authenticator" : "facebook" }';
    	//$window.location.href='https://www.facebook.com/dialog/oauth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=492601017561308&approval_prompt=force&state=facebook';
    	//$window.location.href='https://www.facebook.com/dialog/oauth?scope=email&redirect_uri=http://localhost:8080/Oauth/Oauth2callback&response_type=code&client_id=492601017561308&approval_prompt=force&state=facebook';
    	//$window.open('https://www.facebook.com/dialog/oauth?scope=email&redirect_uri=http://localhost:8080/Oauth/Oauth2callback&response_type=code&client_id=492601017561308&approval_prompt=force&state=facebook');
    }

    
    $scope.linkedinRegistration = function() {
    	$window.location.href='https://www.linkedin.com/uas/oauth2/authorization?scope=r_emailaddress&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=77degzl9awx3h8&approval_prompt=force&state={ "authenticator" : "linkedin" }';
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
    
	
	$scope.reset = function(form) { 
		//alert("Clear Form");
	    if (form) {
	        form.$setPristine();
	        form.$setUntouched();
	      }
		$scope.newUser={};
		$scope.repeatPassword="";
		$scope.okmessage="";
		$scope.righterrormessage="";
	}
  }])
  .controller('LoginController', ['$scope', '$location', '$sce', '$http', '$rootScope', '$window',
                                  function($scope, $location, $sce, $http, $rootScope, $window) {

	  $scope.logindetails = {};

	  $scope.go = function (path) {
		  $location.path(path);
	  };

	  $scope.passwordTooltip = $sce.trustAsHtml('Insert password used during registration');
	  $scope.userTooltip = $sce.trustAsHtml('Insert username, email or mobile used during registration');

	  $scope.login = function() {

		  if ($scope.validate()){
			  $http.get('/users/1.0/login?identificator='+$scope.logindetails.user+'&password='+$scope.logindetails.password)
			  .success(function(data, status, headers, config) {
				  $location.path("/user");
				  //$rootScope.user=data.username;
				  //$window.location.href='/dealerUI/index.html';
				  //alert(data.firstName);
				  $rootScope.user=data.firstName;
/*				  if (data.returnCode == 110){
					  $scope.errormessage = "Invalid credentials!";
				  }else{
					  $location.path("/user");
				  }*/
				  
			  }).error(function(data, status, headers, config) {
				  if (status=="403"){
					  $scope.errormessage = "Invalid credentials!";  
				  }
				  //$scope.errormessage = "Invalid credentials!";
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
			  $rootScope.user="unknown";
			  $location.path("/");
		  }).error(function() {
			  $location.path("/");
		  });
	  };
  }]);
