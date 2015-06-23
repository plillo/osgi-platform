angular.module("userUI").controller('RegisterController', ['$scope', '$http', 
  function($scope, $http) {
    $scope.text="Hello World!!!!";
    
    $scope.newUser = {};
    
/*    $scope.newUser.username = "pippo";
    $scope.newUser.firstname = "pippo";
    $scope.newUser.lastname = "pluto";
    $scope.newUser.email = "pippo@pluto.it";
    $scope.newUser.mobile = "3232323232";*/

/*	isValidUsername	/validation/username/{payload}	POST
	isValidPassword	/validation/password/{payload}	POST
	isValidEmail	/users/#UserId/email/	GET
	isValidMobile	/users/#UserId/mobile/	GET		*/
    
    $scope.createUser = function() {
		
		if($scope.validate() == true) {
			alert("post");
			$http.post('/users', $scope.newUser).success(function(data) {
				alert(data);
				$scope.righterrormessage = data;
			}).error(function() {
				$scope.righterrormessage = "Failed to create user";
			});
		}
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
	        //form.$setPristine();
	        form.$setUntouched();
	      }
		$scope.newUser={};
	}
  }]);


/*var controllers = angular.module('controllers', []);

controllers.controller('LoginCtrl', function($scope, $http) {
	$scope.logindetails = {};

	$scope.login = function() {
		$http.post('/adminlogin', $scope.logindetails).success(function() {
			window.location = '/admin/orders/index.html';
		}).error(function() {
			$scope.errormessage = 'Username password combination incorrect';
		});
	}
	
});	*/
