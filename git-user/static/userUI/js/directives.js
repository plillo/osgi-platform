angular.module("userUI").directive('uniqueUsername',['$http', '$q', function($http, $q){
	return{
		restrict: 'A',
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {
			//if (element.$touched) alert("OK");
			ngModel.$asyncValidators.uniqueUsernameValidator = function(value) {

				//if (!ngModel.$pristine){
					//alert(value);
					//return $http.post('/auth/validateuser', value).success(function(){alert("response"); return true;});
					
/*					return $http.get('/users/1.0/validateUsername?username=' + value).then(
		                    function(response) {
		                        if (response.data.isValid) {
		                        	alert(response.data.status);
		                            //return $q.reject(response.data.message);
		                        	return $q.resolve(response.data.message);
		                        }
		                        return true;
		                    }
		                );*/
					
					
					return $http.get('/users/1.0/validateUsername?username=' + value).
					 success(function(data, status, headers, config) {
					    alert(data.message);
						 //alert(data.isValid);
					    //ngModel.setValidity(ngModel.$asyncValidators.$error.uniqueUsernameValidator,data.isValid);
					    return data.isValid;
					    //return false;
					  }).
					  error(function(data, status, headers, config) {
						  alert(data.message);
						  return false;
					  });
					
/*				     then(function resolved() {
				       //username exists, this means validation fails
				       return $q.reject('exists');
				     }, function rejected() {
				       //username does not exist, therefore this validation passes
				       return false;
				     }
					
					return true;*/
				//} 
				

			}
			
		}
	}
}])
.directive('uniqueEmail',['$http', '$q', function($http, $q){
	return{
		restrict: 'A',
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {
			ngModel.$asyncValidators.uniqueEmailValidator = function(value) {
				if (!ngModel.$pristine){
					return $http.get('/users/1.0/validateEMail?email=' + value).
					 success(function(data, status, headers, config) {
					    alert(data.message);
						 //alert(data.isValid);
					    //return data.isValid
					    return false;
					  }).
					  error(function(data, status, headers, config) {
						  alert(data.message);
						  return false;
					  });
			}
			}
			
		}
	}
}])
.directive('uniqueMobile',['$http', '$q', function($http, $q){
	return{
		restrict: 'A',
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {
			ngModel.$asyncValidators.uniqueMobileValidator = function(value) {
					return $http.get('/users/1.0/validateMobile?mobile=' + value).
					 success(function(data, status, headers, config) {
					    alert(data.message);
						 //alert(data.isValid);
					    //return data.isValid
					    return false;
					  }).
					  error(function(data, status, headers, config) {
						  alert(data.message);
						  return false;
					  });
			}
			
		}
	}
}])
