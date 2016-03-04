angular.module('hashDirectives',['hashServices']);

////////////////////////////////////////
// Core directives
////////////////////////////////////////

//ADD 'loginComponent' directive
//..............................
angular.module('hashDirectives').directive('haLoginComponent', function(logger, user) {
	return {
		replace: false,
		scope: true,
		controller: function($scope, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.identificator = '';
			$scope.password = '';
			$scope.rePassword = '';

	       	// insert here scope-functions
			// ...
			
			$scope.login = function(){
			    var identificator = $scope.identificator;
			    var password = $scope.password;
			    if (identificator && password) {
			    	logger.login(identificator, password);
			    	// on successfully logging the 'logger' service brodcasts 'authorized' event 
			    }
			};

			$scope.create = function(){
			    var identificator = $scope.identificator;
			    var password = $scope.password;
			    if (identificator && password) {
			    	user.createUser(identificator, password);
			    };
			};
			
		    $scope.authenticator = function() {
		    	var authenticator = $(this).attr('data-authenticator');
				switch(authenticator){
					case 'google':
						$window.location.href='https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force&state={ "authenticator" : "google" }';
					    break;
					case 'facebook':
				    	$window.location.href='https://www.facebook.com/dialog/oauth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=492601017561308&approval_prompt=force&state={ "authenticator" : "facebook" }';
					    break;
					case 'linkedin':
				    	$window.location.href='https://www.linkedin.com/uas/oauth2/authorization?scope=r_emailaddress&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=77degzl9awx3h8&approval_prompt=force&state={ "authenticator" : "linkedin" }';
					    break;
				}
			};
			
			$scope.$watch('loginForm.loginIdentificator.mode', function(newValue, OldValue){
				newValue = newValue==undefined ? 'login' : newValue;
				$scope.loginForm.mode = newValue;
				
				if(newValue=='invalid' || newValue=='login')
					$element.find("input[name='retype-password']").hide();
				else
					$element.find("input[name='retype-password']").show();
				
				if(newValue=='login'){
					$element.find('#button-login').show();
					$element.find('#button-forgotten').show();
				}
				else{
					$element.find('#button-login').hide();
					$element.find('#button-forgotten').hide();
				}
				
				if(newValue=='create')
					$element.find('#button-create').show();
				else
					$element.find('#button-create').hide();
			});
	    },
		link: function(scope, element, attributes){
			element.find("input[name='retype-password']").hide();

 			// EVENTS BINDING
 			element.find('#button-login').hide().bind('click', scope.login);
 			element.find('#button-create').hide().bind('click', scope.create);
 			/*
 			element.find("[data-authenticator]").bind('click', scope.authenticator);
 			*/
		}
	};
});

angular.module('hashDirectives').directive('haIdentificator', function($q, logger, $timeout) {
	return {
		restrict: 'AEC',
		require: 'ngModel',
		link: function(scope, elm, attrs, ctrl) {
			scope.mode = 'invalid';
			// set ASYNC validator
			ctrl.$asyncValidators.identificator = function(modelValue, viewValue) {
	
				if (ctrl.$isEmpty(modelValue)) {
					// consider empty model valid
					return $q.when();
				}
	
				var def = $q.defer();
	
				logger.validateIdentificator(modelValue, def, ctrl);
	      
				return def.promise;
			};
		}
	};
});

angular.module('hashDirectives').directive('rePassword', function() {
return {
  require: 'ngModel',
  link: function(scope, elm, attrs, ctrl) {
      // set SYNC validator
      ctrl.$validators.rePassword = function(modelValue, viewValue) {
      	return scope.password==modelValue;
      };
  }
};
});

//ADD 'profileComponent' directive
//................................
angular.module('hashDirectives').directive('haProfileComponent', function(logger, user) {
	return {
		replace: false,
		scope: true,
		controller: function($scope, $rootScope, $window, $element){
	       	// insert here scope-properties
			// ...
			$scope.identificator = '';
			$scope.password = '';
			$scope.rePassword = '';
			
			$scope.updatingUser = {
				username:$rootScope.currentUser.username,
				email:$rootScope.currentUser.email,
				mobile:$rootScope.currentUser.mobile,
				firstName:$rootScope.currentUser.firstName,
				lastName:$rootScope.currentUser.lastName,
			}

	       	// insert here scope-functions
			// ...
			$scope.update = function(){
				$scope.updatingUser.attributes = [];
				$.each( $element.find('[name^="extra."]'), function( index, elem ) {
					var name = $(this).attr('name').substring(6);
					var value = undefined;
					switch($(this).attr('type')) {
						case 'radio':
							if($(this).prop('checked'))
								value = $(this).attr('value');
							break;
						case undefined:
							break;
						default:
							value = $( this ).val();
					}
					if(value!=undefined){
						var attribute = {
							attributeUuid: name, //TODO replace with real UUID attribute 
							value: {
								value: value
							}
						}
						$scope.updatingUser.attributes.push(attribute);
					}
				});
				
			    user.updateUser($scope.updatingUser)
			   	.then(
		  	        function successCallback(response) {
		          	    $rootScope.response = response;
		          	    $scope.button.removeClass('button-assertive');
		          	    $scope.button.addClass('button-positive');
		            },
		            function errorCallback(response) {
		          	  	$rootScope.response = response;
		          	  	alert('KO');
		            }
		        );
			};
	    },
		link: function(scope, element, attributes){
			scope.button = element.find('#button-update');
			// EVENTS BINDING
			scope.button.bind('click', scope.update);
		}
	};
});



////////////////////////////////////////
// Smart buttons directives
////////////////////////////////////////

// ADD 'haSmartbutton' directive
// .............................
angular.module('hashDirectives').directive('btSmartbutton', function(broker, $state) {
	return {
		replace: false,
		scope: {
			title:'@',
			label:'@'
		},
		templateUrl : 'templates/bt-smartbutton.html',
		//template : '###',
		controller: function($scope, $element){
			


			$scope.$on('$destroy', function () {
			    broker.unsubscribe($scope.topic);
			});


	       	// insert here scope-attributes
			// ...

			// click
			$scope.click = function(){
				$scope.status = ($scope.status % $scope.statesNumber) + 1;
				$scope.leftBackgroundPos = -($scope.status-1) * $scope.width;
			};
			
			// getTopicStatus
			$scope.getTopicStatus = function(message){
				// Get body message from topic
				
				var payload = undefined;
				try {
					var payload = JSON.parse(unescape(message));
				}
				catch(error) {
					alert(error);
					return;
				}
				
				if(payload.status > $scope.statesNumber)
					return;
				
				if(payload.label!=undefined){
					var number = Number(payload.label).toFixed(2);

					$scope.label = ''+number;
				}
					
				$scope.status = payload.status;
				$scope.leftBackgroundPos = -($scope.status-1) * $scope.width;

				// APPLY (in order to make visible changes to <status>)
				$scope.$apply();

				$scope.element.css('background-position',$scope.leftBackgroundPos+'px -'+$scope.height+'px');
			};

			// getStatus
			$scope.getStatus = function(){
				return $scope.status;
			};
			
			$scope.onClick = function(){
				$scope.status = ($scope.status % $scope.statesNumber) + 1;
				$scope.leftBackgroundPos = -($scope.status-1) * $scope.width;

				// APPLY (in order to make visible changes to <status>)
				$scope.$apply();

				$scope.element.removeClass('bt-mousedown');
				$scope.element.css('background-position',$scope.leftBackgroundPos+'px -'+$scope.height+'px');
				
				// publication to topic
				if($scope.topic) {
					broker.deferred.then(
						function(message){
						    broker.send($scope.status, $scope.topic, true);
						},
						function(message){
							alert('Promise rejected with message: '+message);
						});
				}
				else if($scope.srefs[$scope.status-1]!==undefined)
					$state.go($scope.srefs[$scope.status-1]);
			};

			$scope.onMouseOver = function(){
				$scope.element.addClass('bt-mouseover');
				$scope.element.css('background-position',$scope.leftBackgroundPos+'px -'+$scope.height+'px');
			}

			$scope.onMouseOut = function(){
				$scope.element.removeClass('bt-mouseover');
				$scope.element.css('background-position',$scope.leftBackgroundPos+'px -'+$scope.height*2+'px');
			}

			$scope.onMouseDown = function(){
				$scope.element.addClass('bt-mousedown');
				$scope.element.css('background-position',$scope.leftBackgroundPos+'px 0');
			}

			$scope.onMouseUp = function(){
				$scope.element.removeClass('bt-mousedown');
				$scope.element.css('background-position',$scope.leftBackgroundPos+'px -'+$scope.height+'px');
			}
	    },
		link: function(scope, element, attributes){
			// Set <element> in scope
			scope.element = element.find('.bt-smartbutton');
			
		    if(scope.label==undefined)
		    	scope.element.find('.bt-label>span').hide();

			// Get from attribute 'status' and Set <status> in scope
			scope.status = parseInt(attributes['status']);
			
			// Get states links
			scope.srefs = attributes['srefs']==undefined?[]:attributes['srefs'].match(/(?=\S)[^,]+?(?=\s*(,|$))/g);
			
			scope.topic = attributes['topic'];
			if(scope.topic) {
				broker.deferred.then(
					function(message){
						broker.subscribe(scope.topic, scope.getTopicStatus);
					},
					function(message){
						alert('Promise rejected with message: '+message);
					});
			}

			scope.width = parseInt(attributes['width']);
   			scope.height = parseInt(attributes['height']);
			scope.iconImg = attributes['icon'];
			scope.backgroundImg = attributes['background'];
			scope.leftBackgroundPos = -(scope.status-1) * scope.width;
			
			var v_unit = scope.height/20;
			var h_unit = scope.width/20;

			// Get from attribute 'states-number' Set <statesNumber> in scope
			scope.statesNumber = parseInt(attributes['statesnumber']);

			// STYLING ELEMENT
			scope.class = attributes['class'];

			// -- width, height
			scope.element.css('width', scope.width);
   			scope.element.css('height', scope.height);

   			// -- icon image
   			if(scope.iconImg!==undefined){
   				var img = $('<img>');
   				img.attr('src', scope.iconImg);
   				img.css('width', (v_unit*10)+'px');
   				img.css('display', 'inline-block');
   				img.css('margin', (v_unit*5)+'px 0 0 '+(v_unit*5)+'px');
   				img.appendTo(scope.element.find('.bt-icon'));
   			}
   			else {
   				scope.element.find('.bt-icon').hide();
   			}
   			
			scope.element.find('.bt-label').css('position', 'absolute');
			scope.element.find('.bt-label').css('top', (scope.height-h_unit*4)+'px');
   			
			var w = scope.element.find('.bt-label>span').width();

			scope.element.find('.bt-label>span').css('display', 'table');
			scope.element.find('.bt-label>span').css('width', (h_unit*18)+'px');
			scope.element.find('.bt-label>span').css('font-size', (h_unit*2)+'px');
   			scope.element.find('.bt-label>span').css('margin', '0 '+h_unit+'px');
   			
   			// -- background image
   			if(scope.backgroundImg!==undefined)
   				scope.element.css('background-image','url('+scope.backgroundImg+')');
   			scope.element.css('background-position',scope.leftBackgroundPos+'px -'+scope.height*2+'px');
   			// -- line-height
   			//scope.element.find('.bt-label').css('line-height', scope.height+'px');

   			// EVENTS BINDING
   			scope.element.bind('mouseover', scope.onMouseOver);
   			scope.element.bind('mouseout', scope.onMouseOut);
   			scope.element.bind('mousedown', scope.onMouseDown);	
   			scope.element.bind('click', scope.onClick);
        }
	};
});

angular.module('hashDirectives').directive('btMulti', function() {
	return {
		replace: false,
		scope: {
			number:'@'
		},
		templateUrl : 'template/bt-multi.html',
		controller: function($scope, $element){
	       	// insert here scope-properties
			// ...

	       	// insert here scope-functions
			// ...
			
	    },
        compile: function(element, attributes){
        	// Do here DOM transformations which are executed before than LINK function...
    		// ...
        	var number = Number(attributes['number']);
        	var inner = element.find('bt-multi').html();

        	if(number)
	    		for(var i=0; i<number; i++){
	    			if(i%20==0)
	    				element.find('.bt-multi').append('<br />');
	    			element.find('.bt-multi').append('<bt-smartbutton label="'+i+'" width="50" height="50" status="1" statesnumber="3"></bt-smartbutton>');
	    		}

    		// ...then RETURN the link function
    		return function(scope, element, attributes){
    			// ...

        }
      }
	};
});
