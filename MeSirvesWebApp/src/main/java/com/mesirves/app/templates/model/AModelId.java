package com.mesirves.app.templates.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class AModelId {
	@Id
	protected ObjectId id;
	
	@JsonSetter("id")
	public void setJsonId(String id){
		if(id!=null && !id.isEmpty())
			this.id=new ObjectId(id);
	}
	
	@JsonGetter("id")
	public String getJsonId(){
		if(id==null)
			return "";
		return id.toHexString();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
