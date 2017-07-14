app
    .config(function ($stateProvider, $urlRouterProvider){
        $urlRouterProvider.otherwise("/home");


        $stateProvider
        
            //------------------------------
            // HOME
            //------------------------------

            .state ('home', {
                url: '/home',
                templateUrl: 'resources/views/home.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                     'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css',
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                insertBefore: '#app-level-js',
                                files: [
                                    'resources/vendors/sparklines/jquery.sparkline.min.js',
                                    'resources/vendors/bower_components/jquery.easy-pie-chart/dist/jquery.easypiechart.min.js',
                                    'resources/vendors/bower_components/simpleWeather/jquery.simpleWeather.min.js',
                                    'resources/vendors/input-mask/input-mask.min.js',
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
                                ]
                            }
                        ])
                    }
                }
            })

    });
