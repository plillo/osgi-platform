angular.module('hashServices',['ngStorage']);

// ***********************
// ADD 'jwtHelper' service
// =======================
angular.module('hashServices').service('jwtHelper', function() {
	
	// <<urlBase64Decode>> function
	// ----------------------------
    this.urlBase64Decode = function(str) {
        var output = str.replace(/-/g, '+').replace(/_/g, '/');
        switch (output.length % 4) {
            case 0: { break; }
            case 2: { output += '=='; break; }
            case 3: { output += '='; break; }
            default: {
                throw 'Illegal base64url string!';
            }
        }
        return decodeURIComponent(escape(window.atob(output)));
    }

	// <<decodeToken>> function
	// ------------------------
    this.decodeToken = function(token) {
        var parts = token.split('.');

        if (parts.length !== 3) {
            throw new Error('JWT must have 3 parts');
        }

        var decoded = this.urlBase64Decode(parts[1]);
        if (!decoded) {
            throw new Error('Cannot decode the token');
        }

        return JSON.parse(decoded);
    }

	// <<getTokenExpirationDate>> function
	// -----------------------------------
    this.getTokenExpirationDate = function(token) {
        var decoded;
        decoded = this.decodeToken(token);

        if(typeof decoded.exp === "undefined") {
            return null;
        }

        var d = new Date(0);
        d.setUTCSeconds(decoded.exp);

        return d;
    };

	// <<isTokenExpired>> function
	// ---------------------------
    this.isTokenExpired = function(token, offsetSeconds) {
        var d = this.getTokenExpirationDate(token);
        offsetSeconds = offsetSeconds || 0;
        if (d === null) {
            return false;
        }

        return !(d.valueOf() > (new Date().valueOf() + (offsetSeconds * 1000)));
    };
});



// ***********************************
// ADD and INITIALISE 'user' service
// ===================================

// ADD
// ...
angular.module('hashServices').service('user', function($http, $localStorage, $rootScope) {
    this.currentUser = undefined;

	// <<setCurrentUser>> function
	// ---------------------------
    this.setCurrentUser = function(currentUser) {
        this.currentUser = currentUser;
        $localStorage.currentUser = currentUser;

        // brodcast event 'hash.settedUser'
        $rootScope.$broadcast('settedUser');
        
        return this.currentUser;
    };

	// <<getCurrentUser>> function
	// ---------------------------
    this.getCurrentUser = function() {
        return this.currentUser;
    };
    
	// <<resetCurrentUser>> function
	// -----------------------------
    this.resetCurrentUser = function() {
        this.currentUser = undefined;
        delete $localStorage.currentUser;

        $rootScope.$broadcast('unsettedUser');
    };
    
	// <<isLoggedUser>> function
	// -------------------------
    this.isLoggedUser = function() {
        return !this.currentUser==undefined;
    };
    
	// <<update>> function
	// -------------------
    this.updateCurrentUserRemoteProfile = function(){
	   var pars = {
		  withCredentials: true,
	      method: 'POST',
	      url: url+currentUser.uid+'/update',
	      params: {
		     firstName: currentUser.firstName,
		     lastName: currentUser.lastName,
		     username: currentUser.username,
	    	 email: currentUser.email,
	    	 mobile: currentUser.mobile,
	    	 password: currentUser.password
	      }
	   };
	   $http(pars).then(
	      function successCallback(response) {
        	  $rootScope.response = response;
          },
          function errorCallback(response) {
        	  $rootScope.response = response;
		      // TODO: handle the case of error updating user data
	    	  alert('KO');
          });
	};
});

//INITIALIZE the 'user' service
//.............................
angular.module('hashServices').run(function($localStorage, user) {
	user.setCurrentUser($localStorage.currentUser);
});



//*****************************************
//ADD and REGISTER 'authInterceptor' service
//=========================================

//ADD
angular.module('hashServices').factory('authInterceptor',
	function($rootScope, $q, $window) {
		return {
			// <<request>> function
			// --------------------
			request : function(config) {
				config.headers = config.headers || {};
				if ($window.sessionStorage.token) {
					config.headers.Authorization = 'Bearer ' + $window.sessionStorage.token;
				}
				return config;
			},
			// <<response>> function
			// ---------------------
			response : function(response) {
				if (response.status === 401) {
					// TODO: handle the case where the user is not authenticated
				}
				return response || $q.when(response);
			}
		};
	});

//REGISTER as $httpProvider interceptor
angular.module('hashServices').config(function($httpProvider) {
	$httpProvider.interceptors.push('authInterceptor');
});



//***********************************
//ADD 'logger' service
//===================================
angular.module('hashServices').provider('logger', function() {
this.url = undefined;

// $get function returning the service
this.$get = function($http, $rootScope, $window, user, jwtHelper){
	var url = this.url;
	return {
		// <<getUrl>> function
		// -------------------
		getUrl: function(){
			return url;
		},
		// <<login>> function
		// ------------------
		login: function(identificator, password){
		   var pars = {
		      method:'GET',
		      url:url+'login',
		      params:{
		    	 identificator:identificator,
		    	 password:password
		      }
		   };
		   $http(pars).then(
		      function successCallback(response) {
		    	  $rootScope.response = response;
		    	  if(response.status==401) {
		    		  user.resetCurrentUser();
		    		  return;
		    	  };
		    	  
		    	  // set TOKEN in session storage
		    	  $window.sessionStorage.token = response.data.token;
		    	  // decode token
	    	      var decrypted = jwtHelper.decodeToken(response.data.token);
		    	  var user = {
		    		 uuid:decrypted.uuid,
				     username:decrypted.username,
		    	     email:decrypted.email,
		    	     mobile:decrypted.mobile,
		    	     password:'',
		    	     firstName:decrypted.firstName,
		    	     lastName:decrypted.lastName,
		    	     accessToken:response.data.token,
		    	  };
		    	  user.setCurrentUser(user);
		          $rootScope.$broadcast('authorized');
           },
           function errorCallback(response) {
         	// delete TOKEN from session storage
         	  delete $window.sessionStorage.token;
         	  
         	  $rootScope.response = response;
		    	  alert('KO');
           });
		},
		// <<logout>> function
		// -------------------
		logout: function(){
			user.resetCurrentUser();
		},
		// <<validateIdentificator>> function
		// ----------------------------------
		validateIdentificator: function(identificator, deferred, ctrl){
		   var pars = {
		      method:'GET',
		      url:url+'validateIdentificator',
		      params:{
		    	 value:identificator
		      }
		   };
		   $http(pars).then(
		      function successCallback(response) {
		    	  $rootScope.response = response;
		    	  ctrl.identificatorType = response.data.identificatorType;
		    	  
		    	  if(response.data.matched){
		    		  ctrl.matched = true;
		    	  }
		    	  else {
		    		  ctrl.matched = false;
		    	  }
		    	  
		    	  if(response.data.identificatorType=='unmatched')
		    		  ctrl.mode = 'invalid';
		    	  else if(response.data.matched)
		    		  ctrl.mode = 'login';
		    	  else
		    		  ctrl.mode = 'create';
		    	  
		    	  // REJECT if invalid identificator type
		    	  if(response.data.identificatorType=='unmatched')
		    		  deferred.reject();
		    	  else
		    		  deferred.resolve();
           },
           function errorCallback(response) {
		    	  $rootScope.response = response;
		    	  alert('validateIdentificator: ERROR');
	    		  deferred.reject();
           });
		},
		// <<update>> function
		// -------------------
		update: function(){
		   var currentUser = user.getCurrentUser();
		   var pars = {
			  withCredentials: true,
		      method:'POST',
		      url:url+user.uid+'/update',
		      params:{
			     firstName:currentUser.firstName,
			     lastName:currentUser.lastName,
			     username:currentUser.username,
		    	 email:currentUser.email,
		    	 mobile:currentUser.mobile,
		    	 password:currentUser.password
		      }
		   };
		   $http(pars).then(
		      function successCallback(response) {
         	  $rootScope.response = response;
           },
           function errorCallback(response) {
         	  $rootScope.response = response;
		    	  alert('KO');
           });
		}
	}
};

// Set logging URL
this.setUrl = function(url){
	  this.url = url;
};
});



//***********************************
//ADD 'broker' service
//===================================
angular.module('hashServices').provider('broker', function() {
	this.client = undefined;
	this.url = undefined;
	this.port = undefined;
	this.clientID = Math.random().toString(12);
	this.topicHandlers = new Map();
  
	// $get function returning the service
	this.$get = function($q, $rootScope){
		var url = this.url;
		var port = this.port;
		var client = this.client;

		return {
			deferred: undefined,
			connected: false,
			
			// <<setDeferred>> function
			// ------------------------
			getDeferred: function(){
				return deferred;
			},
			
			// <<isConnected>> function
			// ------------------------
			isConnected: function(){
				return connected;
			},
			
			// <<connect>> function
			// --------------------
			connect: function(token, password){
				if(client){
					var deferred = $q.defer();
					$rootScope.haBrokerMessage = 'Connecting to broker: '+url+':'+port+' ...';
					$rootScope.haBrokerConnecting = true;
					$rootScope.haBrokerConnected = false;
					$rootScope.$apply();
					
			    	client.connect({
	    				userName: token,
	    				password: password,
	    				onSuccess:function(frame) { // connection OK
	    					connected = true;
	    					deferred.resolve('Connected to the broker!');
	    					$rootScope.haBrokerMessage = 'Connected!';
	    					$rootScope.haBrokerConnecting = false;
	    					$rootScope.haBrokerConnected = true;
	    					$rootScope.$apply();
	    				},
	    				onFailure:function(error) { // NO connection
	    					connected = false;
	    					deferred.reject('Cannot connect to the broker: '+url+':'+port);
	    					$rootScope.haBrokerMessage = error.errorMessage;
	    					$rootScope.haBrokerConnecting = false;
	    					$rootScope.$apply();
	    				}
	    			});
			    	this.deffered = deferred.promise;
				}
				else
					alert('Broker connection error: broker not initialized yet!');
			},
			// <<subscribe>> function
			// ----------------------
			subscribe: function(topic, topicHandler){
				if(client){
			    	client.subscribe(topic); // subscribe topic
			    	topicHandlers.set(topic, topicHandler); // set topic handler
				}
				else
					alert('Topic subscription error: broker not initialized yet!');
			},
			// <<send>> function
			// -----------------
			send: function(status, topic, retained){
				if(client){
					var sendmessage = new Paho.MQTT.Message(JSON.stringify({status:status}));
					sendmessage.destinationName = topic;
					sendmessage.retained = retained;
					// send
					client.send(sendmessage);
				}
				else
					alert('Send message on topic error: broker not initialized yet!');
		    },
			// <<getUrl>> function
			// -------------------
		    getUrl: function(){
		    	return url;
		    },
		    
			// <<getPort>> function
			// -------------------
		    getPort: function(){
		    	return port;
		    }
		}
	};	
	
	// Initialize the broker
	this.initBroker = function(url, port){
		this.url = url;
		this.port = port;
		
		// Set the broker client
		this.client = new Paho.MQTT.Client(url, port, this.clientID);
		// set onConnectionLost CALLBACK of the broker client
		this.client.onConnectionLost = function(responseObject) {
	        alert('Connection lost: '+responseObject.errorCode+'-'+responseObject.errorMessage);
	    };
		// set onMessageArrived CALLBACK of the broker client
	    this.client.onMessageArrived = function(message) {
	        var topic = message.destinationName; // read topic
	        var topicHandler = this.topicHandlers.get(topic); // get topic handler
	        // call topic handler with payload
	        if(topicHandler)
	        	topicHandler(message.payloadString);
	    };
	};
});

//INITIALIZE
//..........
angular.module('hashServices').run(function($window, broker) {
	var token = $window.sessionStorage.token ? $window.sessionStorage.token : '*';
	
	// Connection to the broker
	broker.connect(token, '*');
});