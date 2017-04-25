package com.mesirves.app.exceptions;

import com.mesirves.app.json.ResponseJSON;

public class ExceptionREST extends Exception {

	private static final long serialVersionUID = 5527694657538728103L;
	
	private int codigo;
	
	public ExceptionREST(int codigo, String mensaje){
		super(mensaje);
		this.codigo=codigo;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	@SuppressWarnings("rawtypes")
	public ResponseJSON toResponse(){
		return new ResponseJSON(codigo);
	}
}
