package com.mesirves.app.templates.dao;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.templates.model.AModelNombre;

public interface INombreCentroIdDAO<I extends AModelNombre> extends ICentroIdDAO<I>{
	public I findByCentroAndNombre(Centro centro, String nombre);
}
