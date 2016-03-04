angular.module('business.services',[]);

// **********************
// ADD 'business' service
// ======================

angular.module('business.services').factory('business', function($http, $rootScope) {
	return {
		createBusiness: function(data){
			return $http.post($rootScope.urlBackend+'/businesses/1.0/', data); // return promise
		},
		getBusinessByUUID: function(uuid){
			// TODO
		},
		getBusinessByCategory: function(search){
			var pars = {
			      method:'GET',
			      url: $rootScope.urlBackend+'/businesses/1.0/categories/'+search
			};
			return $http(pars); // return promise
		},
		getBusiness: function(search){
			var pars = {
			      method:'GET',
			      url:$rootScope.urlBackend+'/businesses/1.0/business/'+search
			};
			return $http(pars); // return promise
		},
		getFollowedBusiness: function(search){
			var pars = {
			      method:'GET',
			      url:$rootScope.urlBackend+'/businesses/1.0/business/followed'
			};
			return $http(pars); // return promise
		},
		followBusiness: function(uuid){
			var pars = {
			      method:'POST',
			      url:$rootScope.urlBackend+'/businesses/1.0/business/'+uuid+'/follow'
			};
			return $http(pars); // return promise
		}
	}
});