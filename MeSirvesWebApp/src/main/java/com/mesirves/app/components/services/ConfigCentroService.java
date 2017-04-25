package com.mesirves.app.components.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mesirves.app.components.models.config.ConfigCentro;
import com.mesirves.app.components.models.config.IdCentroPK;
import com.mesirves.app.components.models.config.Secuencia;
import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.dao.ConfigCentroDAO;

@Service
public class ConfigCentroService {
	final static Logger log = LogManager.getLogger(ConfigCentroService.class);
	
	public static final String AUTO_FACTURAR="autoFacturar";
	public static final String AUTO_PAGO="autoPago";
	public static final String SEQ_FACTURA="seqFactura";
	public static final String SEQ_FACTURA_INTEGRITY="seqFacturaIntegrity";	
	
	@Autowired
	private ConfigCentroDAO dao;
	
	@Autowired
	private MongoOperations mongoOperation;

	public ConfigCentroDAO getDAO() {
		return dao;
	}
	

	
	@SuppressWarnings("unchecked")
	private <T> T getValue(Centro centro, String paramId, T defecto){
		try{
			ConfigCentro param=dao.findOne(new IdCentroPK(centro.getId(), paramId));
			if(param!=null){
				return (T)param.getValue();				
			}
		}catch(Exception e){
			log.error("Error al cargar el parametro "+paramId+" para el centro "+centro.getJsonId()+":"+e.getMessage());
			e.printStackTrace();
		}
		return defecto;
	}
	
	public void setValue(Centro centro, String paramId, Object valor){
		ConfigCentro param=new ConfigCentro();
		param.setId(new IdCentroPK(centro.getId(), paramId));
		param.setValue(valor);
		getDAO().save(param);
	}
	
	public void initSequence(Centro centro, String paramId){
		Secuencia seq=new Secuencia();
		seq.setId(new IdCentroPK(centro.getId(), paramId));
		seq.setSeq(0l);
		mongoOperation.save(seq);
	}

	private long getNextSecuencia(ObjectId centroId, String paramId){
		  //get sequence id
		  Query query = new Query(Criteria.where("id.centroId").is(centroId).and("id.paramId").is(paramId));

		  //increase sequence id by 1
		  Update update = new Update();
		  update.inc("seq", 1);

		  //return new increased id
		  FindAndModifyOptions options = new FindAndModifyOptions();
		  options.returnNew(true);
		  
		  //this is the magic happened.
		  Secuencia seq =   mongoOperation.findAndModify(query, update, options, Secuencia.class);

		  //if no id, throws SequenceException
	          //optional, just a way to tell user when the sequence id is failed to generate.
		  if (seq == null) {
			  seq=new Secuencia();
			  seq.setId(new IdCentroPK(centroId, paramId));
			  seq.setSeq(1l);
			  mongoOperation.save(seq);
		  }

		  return seq.getSeq();
	}
	
	public long getSequence(ObjectId centroId, String sequenceId){
		Query query = new Query(Criteria.where("id.centroId").is(centroId).and("id.paramId").is(sequenceId));
		Secuencia s=mongoOperation.findOne(query, Secuencia.class);
		if(s!=null)
			return s.getSeq();
		return -1;
	}

	/**
	 * Indica si debe generar una factura automaticamente al pasar visita
	 * @param centro	 
	 * @return true/false -> Por defecto true 
	 */
	public boolean isAutoFacturar(Centro centro){
		return getValue(centro, AUTO_FACTURAR, true);
	}
	
	/**
	 * Indica si debe generar un pago con el importe de la prestacion al pasar la visita
	 * @param centro	 
	 * @return true/false -> Por defecto true 
	 */
	public boolean isAutoPago(Centro centro){
		return getValue(centro, AUTO_PAGO, true);
	}
	

	/**
	 * Obtiene la siguiente secuencia para el num de factura
	 * @param centro
	 * @return long con el numUltimaFactura+1
	 */
	public long siguienteFactura(Centro centro){
		return getNextSecuencia(centro.getId(), SEQ_FACTURA);
	}
	
	/**
	 * Obtiene el codigo de la ultima factura que se ha comprobado la integridad de numeros de factura
	 * @param centro
	 * @return long con el numero de factura
	 */
	public long getNumFacturaIntegrity(Centro centro){
		return getValue(centro, SEQ_FACTURA_INTEGRITY, 0l);
	}
	
	/**
	 * Establece el numero de la ultima factura que ha pasado las pruebas de integridad
	 * @param centro
	 * @param numFactura
	 */
	public void setNumFacturaIntegrity(Centro centro, long numFactura){
		setValue(centro, SEQ_FACTURA_INTEGRITY, numFactura);
	}
	
	
	/**
	 * Settea un parametro y devuelve true/false en caso de hacerlo bien o mal
	 * @param param
	 * @return
	 */
	public boolean setParam(ConfigCentro param){
		if(param!=null && param.isCorrecto()){
			log.debug("Cambiando el parametro "+param.getId().getParamId()+" para el centro "+param.getId().getCentroId()+" al valor: "+param.getValue());
			getDAO().save(param);
			return true;
		}else{
			return false;
		}
	}

	public ConfigCentro getParam(IdCentroPK idParam){
		if(idParam!=null){
			return getDAO().findOne(idParam);			
		}
		return null;
	}
	
	public void iniciarSequenciasCentro(Centro centro){
		initSequence(centro, SEQ_FACTURA);
		initSequence(centro, SEQ_FACTURA_INTEGRITY);
	}
	
	public void drop(){
		getDAO().deleteAll();
		mongoOperation.dropCollection(Secuencia.class);
	}
}
