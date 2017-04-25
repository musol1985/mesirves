package com.mesirves.app.components.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mesirves.app.components.models.config.ConfigCentro;
import com.mesirves.app.components.models.config.IdCentroPK;
import com.mesirves.app.components.services.ConfigCentroService;
import com.mesirves.app.json.ResponseJSON;

@RestController
@RequestMapping("/config/centro")
public class ConfigCentroREST{
	final static Logger log = LogManager.getLogger(ConfigCentroREST.class);

	@Autowired
	private ConfigCentroService config;

	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Boolean> setConfig(@RequestBody ConfigCentro parametro) {		
		if(config.setParam(parametro)){
			log.debug("Parametro cambiado");
			return new ResponseJSON<Boolean>(ResponseJSON.OK, true);
		}else{
			log.debug("Error cambiando el parametro"+parametro);
			return new ResponseJSON<Boolean>(ResponseJSON.DESCONOCIDO, false);
		}
    }
	
	
	@RequestMapping(method=RequestMethod.GET, path="/{centroId}/{paramId}")
    public ResponseJSON<ConfigCentro> getConfig(@PathVariable String centroId, @PathVariable String paramId) {
		IdCentroPK id=new IdCentroPK(new ObjectId(centroId), paramId);
				
		ConfigCentro param=config.getParam(id);
		if(param!=null){
			return new ResponseJSON<ConfigCentro>(ResponseJSON.OK, param);
		}else{
			log.debug("Error obteniendo el parametro "+id);
			return new ResponseJSON<ConfigCentro>(ResponseJSON.DESCONOCIDO);
		}
	}
}

