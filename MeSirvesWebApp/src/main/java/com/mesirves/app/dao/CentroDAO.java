package com.mesirves.app.dao;

import org.springframework.stereotype.Repository;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.templates.dao.IBasicIdDAO;

@Repository
public interface CentroDAO extends IBasicIdDAO<Centro>{
	public Centro findByCorreoAdmin(String correo);
}
