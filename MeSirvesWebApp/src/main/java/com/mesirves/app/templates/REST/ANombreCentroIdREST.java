package com.mesirves.app.templates.REST;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.templates.dao.INombreCentroIdDAO;
import com.mesirves.app.templates.model.AModelNombre;
import com.mesirves.app.templates.service.ANombreCentroIdService;


public class ANombreCentroIdREST<S extends ANombreCentroIdService<D, I>, I extends AModelNombre, D extends INombreCentroIdDAO<I>> extends ACentroIdREST<S,I,D>{
	
	@RequestMapping(method=RequestMethod.PUT)
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<I> nuevo(@RequestBody I item) {
		if(service.getDAO().findByCentroAndNombre(item.getCentro(), item.getNombre())!=null){
			return new ResponseJSON<I>(ResponseJSON.YA_EXISTE);
		}else{
			service.getDAO().save(item);
			
			return new ResponseJSON<I>(ResponseJSON.OK, item);
		}
    }
	
}

