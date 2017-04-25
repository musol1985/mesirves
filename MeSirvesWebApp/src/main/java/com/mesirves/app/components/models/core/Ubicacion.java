package com.mesirves.app.components.models.core;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mesirves.app.json.PosicionJSON;


public class Ubicacion{

	private String calle;
	private String numero;
	private String poblacion;
	private String provincia;
	private String pais;
	private String CP;
	private GeoJsonPoint posicion;
	
	public Ubicacion(){
		
	}

	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCP() {
		return CP;
	}

	public void setCP(String cP) {
		CP = cP;
	}

	@JsonGetter("posicion")
	public PosicionJSON getPosicionJson() {
		if(posicion!=null){
			return new PosicionJSON(posicion.getX(), posicion.getY());
		}
		return null;
	}

	@JsonSetter("posicion")
	public void setPosicionJson(PosicionJSON posicion) {
		if(posicion!=null){
			this.posicion=new GeoJsonPoint(posicion.lat, posicion.lng);
		}		
	}
	
	
	public GeoJsonPoint getPosicion() {
		return posicion;
	}

	public void setPosicion(GeoJsonPoint posicion) {
		this.posicion = posicion;
	}
}
