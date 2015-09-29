userUI.service("oauthlink",
 function($window) { 	 
    
    this.callouth = function($name) {

		switch($name) {
		    case 'google':
		    	return $window.location.href='https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com&approval_prompt=force&state={ "authenticator" : "google" }';
		        break;
		    case 'facebook':
		    	$window.location.href='https://www.facebook.com/dialog/oauth?scope=email&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=492601017561308&approval_prompt=force&state={ "authenticator" : "facebook" }';
		        break;
		    case 'linkedin':
		    	$window.location.href='https://www.linkedin.com/uas/oauth2/authorization?scope=r_emailaddress&redirect_uri=http://localhost:8080/OAuth2/callback&response_type=code&client_id=77degzl9awx3h8&approval_prompt=force&state={ "authenticator" : "linkedin" }';
		    	break;
		}
		 
	 }
	
});