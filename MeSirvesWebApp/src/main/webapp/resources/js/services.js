app.factory('editorService', function( $rootScope , $http, $state) {
	return{
	
		setupCanvas:function(canvas){
			var service=this;
			
			canvas.helpers=[];
			
			canvas.on("mouse:up", function(options){
				service.clearHelpers(canvas);
			});
			
			canvas.on("object:moving", function(options){
				service.moveObject(service, options.target, canvas);
			});
			
			canvas.on("object:selected", function(options){
				$rootScope.$broadcast('objectSelected', options.target, canvas);
			});
		},
		
		clearHelpers:function(canvas){
			if(canvas.helpers.length>0){
				canvas.helpers.forEach(function(helper){
					canvas.remove(helper);		
				});
				canvas.helpers=[];
			}
		},
		
		moveObject:function(service, me, canvas){
			service.clearHelpers(canvas);

			if(me){				
				canvas.getObjects().forEach(function(item){
					if(item!=me){
						
						if(!service.checkHelper(service, canvas, me, item.top+item.height, me.top, true,0) &&
						 	!service.checkHelper(service, canvas, me, item.top, me.top, true,0)){
								service.checkHelper(service, canvas, me, item.top+item.height, me.top+me.height, true, me.height);
								service.checkHelper(service, canvas, me, item.top, me.top+me.height, true, me.height);
						}
						
						
						if(!service.checkHelper(service, canvas, me, item.left+item.width, me.left, false,0) &&
							!service.checkHelper(service, canvas, me, item.left, me.left, false,0)){
								service.checkHelper(service, canvas, me, item.left+item.width, me.left+me.width, false, me.width);
								service.checkHelper(service, canvas, me, item.left, me.left+me.width, false, me.width);
						}
						
					}
				});
			}
		},

		checkHelper:function(service, canvas, me, pos1, posItem, horizontal, add){
			if(Math.abs((pos1 )-(posItem))<20){
				var helper;
				
				if(horizontal){
					me.top=pos1-add;
					helper=service.makeLine([0, pos1 , canvas.width, pos1 ]);
				}else{
					me.left=pos1-add;
					helper=service.makeLine([pos1, 0 , pos1, canvas.height ]);
				}
				
				canvas.helpers.push(helper);
				canvas.add(helper);
				
				return true;
			}
			
			return false;
		},

		makeLine:function(coords) {
		    return new fabric.Line(coords, {
		      strokeDashArray: [5, 5],
		      stroke: 'blue',
		      strokeWidth: 1,
		      selectable: false
		    });
		 },
		 
		 loadComponents:function(canvas){			 			
			 canvas.components=[];
			 
			 this.loadComponent('resources/json/checkbox.json', canvas);
			 this.loadComponent('resources/json/checkboxNOK.json', canvas);
		 },
		 
		 loadComponent:function(url, canvas){
			 var service=this;
			 
			 $http.get(url).then(function(res) {
				 var comp=service.getNewComponent(res.data)
				 comp.create(canvas);
				 canvas.components.push(comp);
			 });
		 },		
		 
		 getElementInCanvasGroup:function(elementName, group){
			for(var i=0;i<group.size();i++){				
				var element=group.item(i);
				if(element.name==elementName)
					return element;
			} 
			return null;
		 },
		 
		 getNewComponent:function(data){
			return {
				data:data,
				create:function(canvas){
					//Cargamos las imagenes
					var component=this;
					
					/*if(data.images && data.images.length>0){
						console.log("Cargamos las imagenes");
						data.imagesLoaded=0;
						
						data.images.forEach(function(imageBean){
							
							fabric.Image.fromURL(imageBean.url, function(img){
								data.imagesLoaded++;
								console.log("Imagen "+data.imagesLoaded+"/"+data.images.length+" cargada =>"+imageBean.url)
								imageBean.img=img;
								imageBean.id=data.imagesLoaded;
								if(data.imagesLoaded==data.images.length){
									//Cuando se hayan cargado todas las imagenes, cargamos los elementos
									component.loadElements(canvas, data);
								}
							});
							
						});
					}else{
						console.log("No hay imagenes a cargar");
						component.loadElements(canvas, data);
					}	*/
					component.loadElements(canvas, data);
				},
				loadElements:function(canvas, data){//metodo para cargar los elementos del componente
					var group=[];
					var component=this;
					
					console.log("Cargando elementos");
					
					data.elements.forEach(function(element, index){
						console.log("Cargando elemento "+(index+1)+"/"+data.elements.length);
						
						
						
						if(element.type=="image"){
							var image={imageId:"-1", name:element.name, left:element.left, top:element.top};
							group[element.name]=image;
						}else if(element.type=="text"){							
							var txt=new fabric.Text(element.value,{left:element.left, top:element.top});
							txt.name=element.name;
							group[element.name]=txt;
						}
						
						if(element.properties){								
							element.properties.forEach(function(propKey){
								var prop=data.props[propKey];

								component.applyProperty(prop, data, group, element, canvas);
							});
						}
					});
					
					
					var grupo={
							mapItems:group,
							items:[],
							forEachObject:function(callback){
								for(var g in this.mapItems){
									var entry=group[g];
									
									callback(entry);
								}
							},
							removeWithUpdate:function(entry){
								//NADA
							},
							addWithUpdate:function(entry){
								this.items.push(entry);
							},
							addNotImage:function(entry){
								this.items.push(entry);
							}
					};

					component.loadImages(grupo, function(){
						var groupComponent=new fabric.Group(grupo.items);
						groupComponent.component=component;
						canvas.add(groupComponent);		
					});
					
				},
				
				applyProperty:function(prop, data, group, element, canvas){
					if(prop.type=="boolean" || prop.type=="select"){
						var actions=prop.actions[prop.value];

						actions.forEach(function(action){
							if(action.elementName==element.name){
								
								if(element.image!=action.image){
									console.log("cambio de imagen!");
									element.changeImage=true;
									element.newImageURL=action.image;
									group[element.name]=element;									
								}else if(action.position!=undefined){
									group[element.name].top=action.position.top;
									group[element.name].left=action.position.left;
								}else if(action.text!=undefined){									
									group[element.name].setText(action.text);	
								}
							}
						});
						
						if(element.changeImage)
							return null;
						
						return element;
					}
				},
				
				loadImages:function(group, callback){
					var imagesToLoad=0;
					var imagesLoaded=0;
					
					var toRemove=[];
					
					group.forEachObject(function(entry){
						
						if(entry.changeImage){							
							imagesToLoad++;
							toRemove.push(entry);
							
							fabric.Image.fromURL(entry.newImageURL, function(img){
								imagesLoaded++;
								
								img.image=entry.newImageURL;
								img.name=entry.name;
								img.top=entry.top;
								img.left=entry.left;
								
								group.addWithUpdate(img);
								
								if(imagesLoaded>=imagesToLoad)
									callback();
							});
						}else if(group.addNotImage){
							group.addNotImage(entry);
						}
					});
					
					toRemove.forEach(function(entry) {
						group.removeWithUpdate(entry);
					});					
					
					if(imagesToLoad==0){
						callback();
					}
				}
			}
		 }
	}
});