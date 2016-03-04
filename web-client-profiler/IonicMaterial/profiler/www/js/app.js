angular.module('starter', ['ionic', 'uiGmapgoogle-maps', 'hashServices', 'hashDirectives', 'starter.controllers', 'business.services', 'business.directives', 'ionic-material', 'ionMdInput'])

.config(function($compileProvider){
  $compileProvider.imgSrcSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel):/);
})

/*
.config(function(backend){
	backend.setBackend('http://calimero:8080/');
})
*/
.run(function(backend){})

.config(function(applicationProvider){
	applicationProvider.setAppcode('bsnss-v1.0');
	applicationProvider.setDescription('Profiler 1.0');
})

.config(function(loggerProvider){
	loggerProvider.setPath('users/1.0/');
	loggerProvider.setAppCode('bsnss-v1.0');
})

.config(function(brokerProvider){
	brokerProvider.initBroker('calimero', 61614);
	//brokerProvider.initBroker('52.28.84.18', 61614);
})

.run(function($ionicPlatform) {
    $ionicPlatform.ready(function() {
        // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
        // for form inputs)
        if (window.cordova && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        }
        if (window.StatusBar) {
            // org.apache.cordova.statusbar required
            StatusBar.styleDefault();
        }
    });
})

.config(function($stateProvider, $urlRouterProvider, $ionicConfigProvider) {

    // Turn off caching for demo simplicity's sake
    $ionicConfigProvider.views.maxCache(0);

    /*
    // Turn off back button text
    $ionicConfigProvider.backButton.previousTitleText(false);
    */

    $stateProvider.state('app', {
        url: '/app',
        abstract: true,
        templateUrl: 'templates/menu.html',
        controller: 'AppCtrl'
    })
    
    .state('app.qrcode', {
        url: '/qrcode',
        views: {
            'menuContent': {
                templateUrl: 'templates/qrcode.html',
                controller: 'QRCodeCtrl'
            },
            'fabContent': {
                template: '<button id="fab-qrcode" class="button button-fab button-fab-top-left expanded button-energized-900 flap"><i class="icon ion-ios-barcode"></i></button>',
                controller: function ($timeout) {
                    $timeout(function () {
                        document.getElementById('fab-qrcode').classList.toggle('on');
                    }, 200);
                }
            }
        }
    })

    .state('app.subscriptions', {
        url: '/subscriptions',
        views: {
            'menuContent': {
                templateUrl: 'templates/subscriptions.html',
                controller: 'SubscriptionsCtrl'
            },
            'fabContent': {
                template: '<button id="fab-activity" class="button button-fab button-fab-top-right expanded button-energized-900 flap" ng-click="click()"><i class="icon ion-plus"></i></button>',
                controller: function ($scope, $state, $timeout) {
                    $timeout(function () {
                        document.getElementById('fab-activity').classList.toggle('on');
                    }, 200);
                    
                    $scope.click = function() {
                    	$state.go('app.newsubscription');
                    };
                }
            }
        }
    })
    
    .state('app.newsubscription', {
        url: '/new-subscription',
        views: {
            'menuContent': {
                templateUrl: 'templates/new-subscription.html',
                controller: 'NewSubscriptionCtrl'
            },
            'fabContent': {
                template: '<button id="fab-subscriptions" class="button button-fab button-fab-top-right expanded button-energized-900 flap" ng-click="click()"><i class="icon ion-card"></i></button>',
                controller: function ($scope, $state, $timeout) {
                    $timeout(function () {
                        document.getElementById('fab-subscriptions').classList.toggle('on');
                    }, 200);
                    
                    $scope.click = function() {
                    	$state.go('app.subscriptions');
                    };
                }
            }
        }
    })

    .state('app.channels', {
        url: '/channels',
        views: {
            'menuContent': {
                templateUrl: 'templates/channels.html',
                controller: 'ChannelsCtrl'
            },
            'fabContent': {
                template: '<button id="fab-channels" class="button button-fab button-fab-top-right expanded button-energized-900 flap"><i class="icon ion-barcode"></i></button>',
                controller: function ($timeout) {
                    $timeout(function () {
                        document.getElementById('fab-channels').classList.toggle('on');
                    }, 200);
                }
            }
        }
    })
    
    .state('app.friends', {
        url: '/friends',
        views: {
            'menuContent': {
                templateUrl: 'templates/friends.html',
                controller: 'FriendsCtrl'
            },
            'fabContent': {
                template: '<button id="fab-friends" class="button button-fab button-fab-top-left expanded button-energized-900 spin"><i class="icon ion-chatbubbles"></i></button>',
                controller: function ($timeout) {
                    $timeout(function () {
                        document.getElementById('fab-friends').classList.toggle('on');
                    }, 900);
                }
            }
        }
    })

    .state('app.gallery', {
        url: '/gallery',
        views: {
            'menuContent': {
                templateUrl: 'templates/gallery.html',
                controller: 'GalleryCtrl'
            },
            'fabContent': {
                template: '<button id="fab-gallery" class="button button-fab button-fab-top-right expanded button-energized-900 drop"><i class="icon ion-heart"></i></button>',
                controller: function ($timeout) {
                    $timeout(function () {
                        document.getElementById('fab-gallery').classList.toggle('on');
                    }, 600);
                }
            }
        }
    })

    .state('app.login', {
        url: '/login',
        views: {
            'menuContent': {
                templateUrl: 'templates/login.html',
                controller: 'LoginCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    
    .state('app.business', {
        url: '/business',
        views: {
            'menuContent': {
                templateUrl: 'templates/business.html',
                controller: 'BusinessCtrl'
            },
            'fabContent': {
                template: '<button id="fab-business" class="button button-fab button-fab-top-left expanded button-energized-900 spin"><i class="icon ion-chatbubbles"></i></button>',
                controller: function ($timeout) {
                    $timeout(function () {
                        document.getElementById('fab-business').classList.toggle('on');
                    }, 900);
                }
            }
        }
    })
    
    .state('app.business-manager', {
        url: '/business-manager',
        views: {
            'menuContent': {
                templateUrl: 'templates/business-manager.html',
                controller: 'BusinessManagerCtrl'
            },
            'fabContent': {
                template: '<button id="fab-business-manager" class="button button-fab button-fab-top-left expanded button-energized-900 spin"><i class="icon ion-chatbubbles"></i></button>',
                controller: function ($timeout) {
                    $timeout(function () {
                        document.getElementById('fab-business-manager').classList.toggle('on');
                    }, 900);
                }
            }
        }
    })
    
    .state('app.landingpage', {
        url: '/landingpage',
        views: {
            'menuContent': {
                templateUrl: 'templates/landingpage.html',
                controller: 'LandingPageCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })

    .state('app.service', {
        url: '/service',
        views: {
            'menuContent': {
                templateUrl: 'templates/service.html',
                controller: 'ServiceCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    
    .state('app.profile', {
        url: '/profile',
        views: {
            'menuContent': {
                templateUrl: 'templates/profile.html',
                controller: 'ProfileCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    
    .state('app.help', {
        url: '/help',
        views: {
            'menuContent': {
                templateUrl: 'templates/help.html',
                controller: 'HelpCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    
    .state('app.map', {
        url: '/map',
        views: {
            'menuContent': {
                templateUrl: 'templates/map.html',
                controller: 'MapCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    
    .state('app.info', {
        url: '/info',
        views: {
            'menuContent': {
                templateUrl: 'templates/info.html',
                controller: 'InfoCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    
    .state('app.lookingfor', {
        url: '/lookingfor',
        views: {
            'menuContent': {
                templateUrl: 'templates/lookingfor.html',
                controller: 'LookingForCtrl'
            },
            'fabContent': {
                template: ''
            }
        }
    })
    ;

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise('/app/landingpage');
});
