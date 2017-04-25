package com.mesirves.app.json;

public class ResponseJSON<T extends Object> {
	//GENERICOS
	public static final int DESCONOCIDO=-1;
	public static final int OK=0;
	public static final int NO_EXISTE=1;
	public static final int YA_EXISTE=2;
	public static final int ES_ADMIN=3;
	public static final int ACCION_PROHIBIDA_REST=4;
	public static final int NO_CLIENTE=5;
	public static final int NO_PRESTACION=6;
	public static final int NO_PROFESIONAL=7;
	
	
	//VISITAS(80's)
	public static final int YA_PAGADA=89;
	public static final int PAGO_EN_EXCESO=88;
	public static final int NO_HAY_PAGOS=87;
	public static final int YA_FACTURADA=86;
	
	//CITAS(90's)
	public static final int RES_TIENE_SOLAPA=99;
	public static final int YA_CAPTURADA=98;
	public static final int RES_ESTADO_INCORRECTO=97;
	public static final int RES_NO_ID_CITA=96;
	
	
	public int cod;
	public T data;
	
	public ResponseJSON(int codigo, T json) {
		this.cod = codigo;
		this.data = json;
	}
	
	public ResponseJSON(int codigo) {
		this.cod = codigo;
	}
}
