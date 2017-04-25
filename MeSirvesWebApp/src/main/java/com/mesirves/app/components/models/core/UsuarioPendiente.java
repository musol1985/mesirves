package com.mesirves.app.components.models.core;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mesirves.app.templates.model.AModelId;
import com.mesirves.app.templates.model.IUsuario;


@Document
public class UsuarioPendiente extends AModelId implements IUsuario{

	@Indexed(unique = true)
	private String correo;

	private String nombre;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	private Date fechaEnvio;
	
	private String color;
	
	@DBRef
	private Centro centro;

	public UsuarioPendiente() {

	}
	
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Centro getCentro() {
		return centro;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	@Override
	public boolean isAdmin() {
		return (centro!=null && centro.getCorreoAdmin().equals(correo));		
	}

	@Override
	public boolean isPendiente() {
		return true;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
}
