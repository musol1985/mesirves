module.exports = function(grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        less: {
            development: {
                files: {
                    "resources/css/app.css": "resources/less/app.less",
                },
                cleancss: true
            }
        }
    });

    // Load the plugin that provides the "less" task.
    grunt.loadNpmTasks('grunt-contrib-less');
    
    // Default task(s).
    grunt.registerTask('default', ['less']);

};
