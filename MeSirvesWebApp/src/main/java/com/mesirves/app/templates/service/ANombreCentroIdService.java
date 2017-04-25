package com.mesirves.app.templates.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.templates.dao.INombreCentroIdDAO;
import com.mesirves.app.templates.model.AModelNombre;

@Service
public class ANombreCentroIdService<D extends INombreCentroIdDAO<I>, I extends AModelNombre> extends ACentroIdService<D, I>{
	
	public I getByCentroAndNombre(String centro, String nombre){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return getDAO().findByCentroAndNombre(c, nombre);
	}
	
}
