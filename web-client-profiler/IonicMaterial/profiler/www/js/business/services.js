angular.module('business.services',[]);

// **********************
// ADD 'business' service
// ======================

angular.module('business.services').factory('business', function($http) {
	return {
		createBusiness: function(data){
			return $http.post('http://calimero:8080/'+'businesses/1.0/', data); // return promise
		},
		getBusinessByUUID: function(uuid){
			// TODO
		},
		getBusiness: function(search){
			var pars = {
			      method:'GET',
			      url:'http://calimero:8080/'+'businesses/1.0/categories/'+search
			};
			return $http(pars); // return promise
		}
	}
});