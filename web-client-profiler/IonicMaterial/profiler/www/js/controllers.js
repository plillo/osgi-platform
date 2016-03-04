/* global angular, document, window */
'use strict';

angular.module('starter.controllers', ['ionic', 'uiGmapgoogle-maps'])

.controller('AppCtrl', function($scope, $state, $ionicModal, $ionicPopover, $timeout, user, logger) {
    // Form data for the login modal
    $scope.loginData = {};
    $scope.isExpanded = false;
    $scope.hasHeaderFabLeft = false;
    $scope.hasHeaderFabRight = false;

    var navIcons = document.getElementsByClassName('ion-navicon');
    for (var i = 0; i < navIcons.length; i++) {
        navIcons.addEventListener('click', function() {
            this.classList.toggle('active');
        });
    }
    
    $scope.reloadPage = function(){
    	window.location.reload();
    };
    
    $scope.isUserLogged = function(){
    	return user.isUserLogged();
    };
    
    $scope.isBusinessUser = function(){
    	return user.isUserInRole('business.busadmin');
    };
    
    $scope.logout = function(){
    	logger.logout();
    };
    
	$scope.exit = function() {
		window.close();
		ionic.Platform.exitApp();
	};
  
    ////////////////////////////////////////
    // Event's handlers Methods
    ////////////////////////////////////////
    
	$scope.$on('authorized', function(event, args) {
		$state.go('app.profile');
	});

    ////////////////////////////////////////
    // Layout Methods
    ////////////////////////////////////////

    $scope.hideNavBar = function() {
        document.getElementsByTagName('ion-nav-bar')[0].style.display = 'none';
    };

    $scope.showNavBar = function() {
        document.getElementsByTagName('ion-nav-bar')[0].style.display = 'block';
    };

    $scope.noHeader = function() {
        var content = document.getElementsByTagName('ion-content');
        for (var i = 0; i < content.length; i++) {
            if (content[i].classList.contains('has-header')) {
                content[i].classList.toggle('has-header');
            }
        }
    };

    $scope.setExpanded = function(bool) {
        $scope.isExpanded = bool;
    };

    $scope.setHeaderFab = function(location) {
        var hasHeaderFabLeft = false;
        var hasHeaderFabRight = false;

        switch (location) {
            case 'left':
                hasHeaderFabLeft = true;
                break;
            case 'right':
                hasHeaderFabRight = true;
                break;
        }

        $scope.hasHeaderFabLeft = hasHeaderFabLeft;
        $scope.hasHeaderFabRight = hasHeaderFabRight;
    };

    $scope.hasHeader = function() {
        var content = document.getElementsByTagName('ion-content');
        for (var i = 0; i < content.length; i++) {
            if (!content[i].classList.contains('has-header')) {
                content[i].classList.toggle('has-header');
            }
        }

    };

    $scope.hideHeader = function() {
        $scope.hideNavBar();
        $scope.noHeader();
    };

    $scope.showHeader = function() {
        $scope.showNavBar();
        $scope.hasHeader();
    };

    $scope.clearFabs = function() {
        var fabs = document.getElementsByClassName('button-fab');
        if (fabs.length && fabs.length > 1) {
            fabs[0].remove();
        }
    };
})

.controller('LandingPageCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, broker, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	$scope.$parent.clearFabs();
    $timeout(function() {
        $scope.$parent.hideHeader();
    }, 0);
    ionicMaterialMotion.fadeSlideInRight();
    ionicMaterialInk.displayEffect();
   
	$scope.getUrlBroker = function() {
		alert(broker.getUrl()+':'+broker.getPort()+' ['+(broker.isConnected()?'':'NOT ')+'CONNECTED]');
	};
})

.controller('ServiceCtrl', function($scope, backend, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    $scope.urlBackend = backend.getBackend();
    
    /*
    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();
    */
    // Set Ink
    ionicMaterialInk.displayEffect();
    
	$scope.setUrlService = function(url) {
		backend.setBackend(url);
	};
})

.controller('LoginCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	$scope.$parent.clearFabs();
    $timeout(function() {
        $scope.$parent.hideHeader();
    }, 0);
    ionicMaterialInk.displayEffect();
})

.controller('BusinessCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    /*
    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();
    */
    // Set Ink
    ionicMaterialInk.displayEffect();
    
    $scope.results = [];
    $scope.categoriesSelected = [];
    
    $scope.searchCategories = function($event){
    	alert(event.target.html());
    }
    
    $scope.selectCategory = function(k){
    	alert(JSON.stringify($scope.results));
    	//categoriesSelected.push(results[k]);
    }
})

.controller('BusinessManagerCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('FriendsCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('ProfileCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, $localStorage, ionicMaterialMotion, ionicMaterialInk) {
	// Enable dragging
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = false;
    $scope.$parent.setExpanded(true);
    $scope.$parent.setHeaderFab('left');

    $scope.user = $localStorage.currentUser;
    
    // Set Motion
    $timeout(function() {
        ionicMaterialMotion.slideUp({
            selector: '.slide-up'
        });
    }, 300);

    $timeout(function() {
        ionicMaterialMotion.fadeSlideInRight({
            startVelocity: 3000
        });
    }, 700);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('QRCodeCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, ionicMaterialMotion, ionicMaterialInk) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('SubscriptionsCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, ionicMaterialMotion, ionicMaterialInk) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Ink
    ionicMaterialInk.displayEffect(); 
})

.controller('NewSubscriptionCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, ionicMaterialMotion, ionicMaterialInk) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Ink
    ionicMaterialInk.displayEffect(); 
})

.controller('ChannelsCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, ionicMaterialMotion, ionicMaterialInk) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Ink
    ionicMaterialInk.displayEffect(); 
})

.controller('GalleryCtrl', function($scope, $stateParams, $ionicSideMenuDelegate, $timeout, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	$scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = true;
    $scope.$parent.setExpanded(true);
    $scope.$parent.setHeaderFab(false);

    // Activate ink for controller
    ionicMaterialInk.displayEffect();

    ionicMaterialMotion.pushDown({
        selector: '.push-down'
    });
    ionicMaterialMotion.fadeSlideInRight({
        selector: '.animate-fade-slide-in .item'
    });

})

.controller('HelpCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('MapCtrl', function($scope, $log, $timeout, $stateParams, $ionicSideMenuDelegate, uiGmapIsReady, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(false);
	
	$scope.map = { 
    	center: { latitude: 40.35, longitude: 18.08 },
    	zoom: 10,
    	control : {}
    };
    
    uiGmapIsReady.promise().then(function (maps) {
        var map = $scope.map.control.getGMap();

        var marker = new google.maps.Marker({
            position: new google.maps.LatLng(40.35,18.08),
            title:"ah belloh!!!",
            draggable: true
        });
        
        google.maps.event.addListener(marker, 'dragstop', function(evt) {
        	$log.log('dragstop');
        });
        
        marker.setMap(map);
    });
    
    
})

.controller('InfoCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    /*
    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();
    */
    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('LookingForCtrl', function($scope, $timeout, $stateParams, $ionicSideMenuDelegate, ionicMaterialInk, ionicMaterialMotion) {
	$ionicSideMenuDelegate.canDragContent(true);
	
	// Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Ink
    ionicMaterialInk.displayEffect();
})
;