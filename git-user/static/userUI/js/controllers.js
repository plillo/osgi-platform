angular.module("userUI").controller('RegisterController', ['$scope', '$http', '$window',
  function($scope, $http, $window) {
    
    $scope.newUser = {};
    
    
    $scope.createUser = function() {
		if($scope.validate() == true) {
			alert("post");
			$http.post('/users/1.0', $scope.newUser).success(function(data) {
				alert(data);
				$scope.okmessage = "New user created";
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

    
	$scope.validate = function() {
		if($scope.newUser.password != $scope.repeatPassword) {
			return false;
		}

		if($scope.newUser.email == undefined || $scope.newUser.email.lenght == 0) {
			return false;
		}

		return true;
	}
    
	
	$scope.reset = function(form) {
	    if (form) {
	        form.$setPristine();
	        //form.$setUntouched();
	      }
		$scope.newUser={};
		$scope.repeatPassword="";
	}
  }]);
