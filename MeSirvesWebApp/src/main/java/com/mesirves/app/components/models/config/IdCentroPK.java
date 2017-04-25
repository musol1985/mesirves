package com.mesirves.app.components.models.config;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class IdCentroPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -181079546600345586L;
	private ObjectId centroId;
	private String paramId;
	

	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
	public IdCentroPK(){
		
	}
	
	public IdCentroPK(ObjectId centro, String paramId){
		this.centroId=centro;
		this.paramId=paramId;
	}
	public ObjectId getCentroId() {
		return centroId;
	}
	public void setCentroId(ObjectId centroId) {
		this.centroId = centroId;
	}
	
	public String toString(){
		if(centroId!=null){
			return centroId.toHexString()+"#"+paramId;
		}else{
			return "null#"+paramId;
		}
	}
	
	@JsonGetter("centroId")
	public String getJsonCentroId(){
		if(centroId==null)
			return "";
		return centroId.toHexString();
	}
	
	@JsonSetter("centroId")
	public void setJsonCentroId(String id) {
		if(id!=null && !id.isEmpty()){
			centroId=new ObjectId(id);
		}
	}
}
