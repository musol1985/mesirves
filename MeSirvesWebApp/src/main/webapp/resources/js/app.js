var app = angular.module('MeSirvES', [
    'ngAnimate',
    'ngMaterial'
])

app.config(function($mdThemingProvider) {
	 $mdThemingProvider.theme('default')
						     .primaryPalette("red")
						     .accentPalette('green')
						     .warnPalette('blue');
})
 