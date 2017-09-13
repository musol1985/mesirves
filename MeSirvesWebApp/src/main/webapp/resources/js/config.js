app.config(function ($stateProvider, $urlRouterProvider){
        $urlRouterProvider.otherwise("/editor");


        $stateProvider
        
            //------------------------------
            // HOME
            //------------------------------

            .state ('editor', {
                url: '/editor',
                views: {		
                    'propiedades': {		
                    	templateUrl: 'resources/templates/propiedades.html',		
                    	controller: function($scope){
                    		
                    	
                    		}		
                    	}
                	}
            	});
});       

