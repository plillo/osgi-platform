angular.module("userUI").directive('uniqueUsername',['$http', function($http){
	return{
		restrict: 'A',
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {
			//if (element.$touched) alert("OK");
			ngModel.$validators.uniqueUsernameValidator = function(value) {
				//alert("ok");
/*				element.bind('blur', function () {
				if (value != "ciao") {
					alert(value);
					return true;
					} else {
						return false;
				}			
				});*/
				
/*				element.bind('blur', function (e) {
					alert(value);
				})*/
				
/*				if (ngModel.$touched) {
					alert(value);
					return true;
					} else {
						return false;
				}*/
				if (!ngModel.$pristine){
					alert(value);
					//return $http.post('/auth/validateuser', value).success(function(){alert("response"); return true;});
					return true;
				} else {
					return false;
			}
				

			}
			
		}
	}
}])






/*angular.module("prova").directive('navbar', function() {
	return {
		restrict: 'E',
		templateUrl: 'partials/navbar.html'
	}
});*/