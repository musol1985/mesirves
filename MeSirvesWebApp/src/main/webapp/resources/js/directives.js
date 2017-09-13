app.directive('editor', function($timeout) {
  return { 
	  restrict: 'E',
	  templateUrl: 'resources/templates/editor.html',
	  scope: {
	      test: '@',
	      id: '@',
	      width: '@',
	      height: '@'
	   },
	   link: function (scope, element, attr) {
		   $timeout(function() {
			   scope.init();
	          });
       },
	   controller:['$scope','editorService', function ($scope, editorService) {
		   
		   $scope.init=function(){
			   $scope.canvas = new fabric.Canvas('canvas'+$scope.id);
			   
			   editorService.setupCanvas($scope.canvas);
			   
			   editorService.loadComponents($scope.canvas);
		   }
	   }]
  };
});

app.directive('propiedades', function($timeout) {
	  return { 
		  restrict: 'E',
		  templateUrl: 'resources/templates/propiedades.html',
		  scope: {
			  
		   },
		   controller:function ($scope, editorService) {
			   $scope.propiedades=[{name:"adios"}];
			   $scope.seleccion={};
			   
			   $scope.$on("objectSelected", function(event, target, canvas){
				   $scope.canvas=canvas;
				   
				   if(target!=$scope.seleccion){
					   $scope.seleccion=target;					   
					   console.log(target.component);

					   $scope.propiedades=[];
					   
					   for(var key in target.component.data.props){
						   var p=target.component.data.props[key];

						   p.name=key;
						   
						   $scope.propiedades.push(p);
					   }

					   $scope.$apply();
				   }			   		
			   });
			   
			   $scope.edu=function(e){
				   alert(e=="unchecked");
				   if(e=="unchecked")
					   return true;
				   return false;				   
			   }
			   
			   $scope.cambiarPropiedad=function(property, valor){
				   property.value=valor+"";
				   
				   var actions=property.actions[valor];		   				   
				   	
				   var grupo=[];
				   
				   actions.forEach(function(action){
					   var canvasElement=editorService.getElementInCanvasGroup(action.elementName, $scope.seleccion);
					   
					   grupo[action.elementName]=canvasElement;
					   
					   canvasElement=$scope.seleccion.component.applyProperty(property, $scope.seleccion.component.data, grupo, canvasElement, $scope.canvas);
					   
					   if(canvasElement!=null){
						   $scope.seleccion.removeWithUpdate(canvasElement);
					   	   $scope.seleccion.addWithUpdate(canvasElement);
					   }
				   });	
				   
				   $scope.seleccion.component.loadImages($scope.seleccion, function(){
					   $scope.canvas.renderAll(); 
				   });
			   }
			   
		   }
	  };
	});