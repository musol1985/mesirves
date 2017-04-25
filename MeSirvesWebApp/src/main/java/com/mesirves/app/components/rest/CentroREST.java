package com.mesirves.app.components.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.services.CentroService;
import com.mesirves.app.dao.CentroDAO;
import com.mesirves.app.exceptions.ExceptionREST;
import com.mesirves.app.json.ListJSON;
import com.mesirves.app.json.PageJSON;
import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.json.request.NuevoCentroJSON;
import com.mesirves.app.templates.REST.ABasicREST;

@RestController
@RequestMapping("/centro")
public class CentroREST extends ABasicREST<CentroService, Centro, CentroDAO>{
	final static Logger log = LogManager.getLogger(CentroREST.class);

	@RequestMapping(method=RequestMethod.GET)
    public String current() {
		return "OK";     
    }
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
    public ResponseJSON<Centro> eliminar(@PathVariable String id){
		return new ResponseJSON<Centro>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}

	@RequestMapping(method=RequestMethod.PUT,path="/nuevo")
    public ResponseJSON<Centro> nuevo(@RequestBody NuevoCentroJSON nuevoCentro) {
		try{
			Centro centro=service.nuevoCentro(nuevoCentro);
			return new ResponseJSON<Centro>(ResponseJSON.OK, centro);
		}catch(ExceptionREST ex){
			log.error(ex.getMessage());
			
			return (ResponseJSON<Centro>)ex.toResponse();
		}
    }
	
	
	@RequestMapping(method=RequestMethod.GET, path="/all/{page}/{size}")
    public PageJSON<Centro> getAll(@PathVariable int page, @PathVariable int size) {
		Page<Centro> centros=this.service.getDAO().findAll(new PageRequest(page-1, size));   

		return new PageJSON<Centro>(centros.getTotalElements(), centros.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/all")
    public ListJSON<Centro> getAll() {
		List<Centro> centros=this.service.getDAO().findAll();   

		return new ListJSON<Centro>(ResponseJSON.OK, centros);
    }

}

