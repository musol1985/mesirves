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
	   controller:['$scope','editorService','$q','$http', function ($scope, editorService, $q, $http) {
		   $scope.elementDropped={};
		   
		   $scope.init=function(){
			   $scope.canvas = new fabric.Canvas('canvas'+$scope.id);
			   
			   $scope.canvas.preserveObjectStacking = true;
			   
			   editorService.setupCanvas($scope.canvas);
			   
			   editorService.loadComponents($scope.canvas);
		   }
		   
		   $scope.onDrop=function(event, droppable){			   
			   var canvasHtml=$(".canvas-container")[0];
			   editorService.loadComponent($scope.elementDropped, $scope.canvas, function(groupCanvas){
				   
				   
				   groupCanvas.left=droppable.position.left-canvasHtml.offsetTop;
				   groupCanvas.top=droppable.position.top-canvasHtml.offsetTop;
				   
				   $scope.canvas.setActiveObject(groupCanvas);
			   });
			   /*
			   var dataURL =  $scope.canvas.toDataURL({
				   format: 'png',height:'46', width:'230'
				 });
			   console.log(dataURL);
			   
			   var img={'name':'hola','imageData':dataURL};
			   
			   $http({
			        url: 'ws/editor',
			        method: "POST",
			        data: img 
			    })
			    .then(function(response) {
			    	console.log("OK");
			    }, 
			    function(response) { // optional
			    	alert("KO)");
			    	console.log(response);
			    });*/
		   }
		   
		   $scope.onOver=function(event, draggable){
			   console.log("onOver");
		   }
		   
		   $scope.onOut=function(event, draggable){
			   console.log("onOut");
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
			   $scope.seleccion={};
			   
			   $scope.$on("objectSelected", function(event, target, canvas){
				   $scope.canvas=canvas;
				   
				   if(target!=$scope.seleccion){
					   $scope.seleccion=target;					   
					   console.log(target.component);

					   $scope.propiedades=[];
					   
					   if(target instanceof fabric.Group){					   
						   for(var key in target.component.data.props){
							   var p=target.component.data.props[key];
	
							   p.name=key;
							   p.value=target.props[key];
							   $scope.propiedades.push(p);
						   }
					   }else if(target instanceof fabric.Rect){
						   for(var key in target.props){
							   var p=target.props[key];
							   p.name=key;
							   $scope.propiedades.push(target.props[key]);
						   }						   
					   }

					   $scope.$apply();
				   }			   		
			   });

			   
			   $scope.cambiarPropiedad=function(property, valor){
				   $scope.seleccion.props[property.name]=valor;

				   if($scope.seleccion instanceof fabric.Group){
					   var actions=property.actions[valor];		   				   
					   	
					   var grupo=[];
					   
					   actions.forEach(function(action){
						   var canvasElement=editorService.getElementInCanvasGroup(action.elementName, $scope.seleccion);
						   
						   grupo[action.elementName]=canvasElement;
						   
						   canvasElement=$scope.seleccion.component.applyProperty(property, property.name, valor+"", $scope.seleccion.component.data, grupo, canvasElement, $scope.canvas);
						   
						   if(canvasElement!=null){
							   $scope.seleccion.removeWithUpdate(canvasElement);
						   	   $scope.seleccion.addWithUpdate(canvasElement);
						   }
					   });	
					   
					   $scope.seleccion.component.loadImages($scope.seleccion, function(){
						   $scope.canvas.renderAll(); 
					   });
				   }else{
					   editorService.applyProperty(property, $scope.seleccion);
					   $scope.canvas.renderAll(); 
				   }
			   }
			   
		   }
	  };
	});


app.directive('componentes', function($timeout) {
	  return { 
		  restrict: 'E',
		  templateUrl: 'resources/templates/componentes.html',
		  scope: {
			  
		   },
		   controller:function ($scope, editorService, $http) {
			   $scope.componentes=[];
			   $scope.draw=false;
			   $scope.drawing={};
			   
			   $scope.loadComponent=function(url){
				   $http.get(url).then(function(response) {				    	
				    	console.log("OK");
				    	$scope.componentes.push(response.data);
				    }, 
				    function(response) { // optional
				    	alert("KO)");
				    	console.log(response);
				    });
			   }
			   
			   $scope.drawRect=function(){
				   $scope.draw=true;
			   }
			   
			   $scope.loadComponent('resources/json/checkbox.json');
			   
			   
			   $scope.$on("mouseDown", function(event, o, canvas){

				   if($scope.draw){
					   canvas.selection =false;					   
					    $scope.drawing.isDown = true;
					    var pointer = canvas.getPointer(o.e);
					    $scope.drawing.origX = pointer.x;
					    $scope.drawing.origY = pointer.y;
					    var pointer = canvas.getPointer(o.e);
					    $scope.drawing.rect = new fabric.Rect({
					        left: $scope.drawing.origX,
					        top: $scope.drawing.origY,
					        originX: 'left',
					        originY: 'top',
					        width: pointer.x-$scope.drawing.origX,
					        height: pointer.y-$scope.drawing.origY,
					        angle: 0,
					        fill: '#FF0000',
					        transparentCorners: false
					    });
					    $scope.drawing.rect.props={};
					    $scope.drawing.rect.props.color=({type:"string", value:"#FF0000"});
					    canvas.add($scope.drawing.rect);
				   }
			   });
			   
			   $scope.$on("mouseMove", function(event, o, canvas){
				    if (!$scope.draw || !$scope.drawing.isDown) return;
				    var pointer = canvas.getPointer(o.e);
				    
				    if($scope.drawing.origX>pointer.x){
				    	 $scope.drawing.rect.set({ left: Math.abs(pointer.x) });
				    }
				    if($scope.drawing.origY>pointer.y){
				    	$scope.drawing.rect.set({ top: Math.abs(pointer.y) });
				    }
				    
				    $scope.drawing.rect.set({ width: Math.abs($scope.drawing.origX - pointer.x) });
				    $scope.drawing.rect.set({ height: Math.abs($scope.drawing.origY - pointer.y) });
				    
				    
				    canvas.renderAll();
			   });
			   
			   $scope.$on("mouseUp", function(event, o, canvas){
				   if($scope.draw){
					   $scope.draw=false;
					   $scope.drawing.isDown=false;
					   canvas.selection =true;
					   canvas.remove($scope.drawing.rect);
					   canvas.add($scope.drawing.rect);
				   }
			   });
		   }
	  };
	});