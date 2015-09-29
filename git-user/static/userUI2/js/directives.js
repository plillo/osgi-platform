userUI.directive('uniqueUsername',['$http', '$q', function($http, $q){
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
				//return false;
				if (!ngModel.$pristine){
					var defer = $q.defer();
					
				      return $http.get('/users/1.0/validateEMail?email=' + value)
				         .then(function(response) {
				           //username exists, this means validation success
				        	 alert(response.data.isValid==true);
				        	// alert('OK');
				           //return $q.reject('selected username does not exists');
				        	 return (response.data.isValid==true);
				         }, function() {
				           //username does not exist, therefore this validation fails
				           return $q.reject('selected username does not exists');
				         });
					
/*					return $http.get('/users/1.0/validateEMail?email=' + value).
					 success(function(data, status, headers, config) {
					    alert(data.message);
						 //alert(attrs.toString());
					    defer.reject();
					    //return data.isValid
					    //ngModel.$setValidity('uniqueEmailValidator', false);
					    //return false;
					  }).
					  error(function(data, status, headers, config) {
						  alert(data.message);
						  return false;
					  });*/
			}
				return defer.promise();
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
				if (!ngModel.$pristine){
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
	}
}]).directive("bootstrapNavbar", function() {
	  return {
		    restrict: "E",
		    replace: true,
		    transclude: true,
		    templateUrl: "template/header.html",
		    compile: function(element, attrs) {  // (1)
		      var li, liElements, links, index, length;

		      liElements = $(element).find("#navigation-tabs li");   // (2)
		      for (index = 0, length = liElements.length; index < length; index++) {
		        li = liElements[index];
		        links = $(li).find("a");  // (3)
		        if (links[0].textContent === attrs.currentTab) $(li).addClass("active"); // (4)
		      }
		    }
		  }});
