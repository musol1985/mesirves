package com.mesirves.app.templates.REST;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mesirves.app.json.ListJSON;
import com.mesirves.app.json.PageJSON;
import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.templates.dao.ICentroIdDAO;
import com.mesirves.app.templates.model.AModelCentro;
import com.mesirves.app.templates.service.ACentroIdService;


public class ACentroIdREST<S extends ACentroIdService<D, I>, I extends AModelCentro, D extends ICentroIdDAO<I>> extends ABasicREST<S,I,D>{


	@RequestMapping(method=RequestMethod.GET, path="{centro}/{page}/{size}")
	//@Secured(value="ROLE_ADMIN")
    public PageJSON<I> getByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {

		Page<I> items=service.getByCentro(centro, new PageRequest(page-1, size));		

		return new PageJSON<I>(items.getSize(), items.getTotalPages(), items.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="all/{centro}")
	//@Secured(value="ROLE_ADMIN")
    public ListJSON<I> getAllByCentro(@PathVariable String centro) {

		List<I> items=service.getDAO().findByCentro_id(new ObjectId(centro));

		return new ListJSON<I>(ResponseJSON.OK, items);
    }
	
	
}

