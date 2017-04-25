package com.mesirves.app.templates.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.templates.model.AModelCentro;

public interface ICentroIdDAO<I extends AModelCentro> extends IBasicIdDAO<I>{
	public Page<I> findByCentro(Centro centro, Pageable pageable);
	public List<I> findByCentro_id(ObjectId centro);
}
