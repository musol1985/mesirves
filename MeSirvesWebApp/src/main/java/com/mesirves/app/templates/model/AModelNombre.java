package com.mesirves.app.templates.model;

/**
 * Model que exitende de ModelCentro con ObjectId como id y el centro
 * Aparte se le pone el campo nombre(sobre todo para los maestros)
 * @author eduarmar
 *
 */
public abstract class AModelNombre extends AModelCentro{
	protected String nombre;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
