package com.mesirves.app.components.models.core;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mesirves.app.templates.model.AModelId;

@Document
public class Centro extends AModelId{
	
	
	private String nombre;
	
	private Ubicacion ubicacion;
	
	private String color;
		
	@Indexed
	private String correoAdmin;


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getCorreoAdmin() {
		return correoAdmin;
	}

	public void setCorreoAdmin(String correoAdmin) {
		this.correoAdmin = correoAdmin;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


	@Override
	public String toString(){
		String res=super.toString()+"{";
		
		if(id!=null)
			res+=id.toHexString()+",";
		if(nombre!=null){
			res+=nombre;
		}
		
		return res+"}";
	}
}
