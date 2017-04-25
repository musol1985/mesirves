package com.mesirves.app.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.UsuarioPendiente;
import com.mesirves.app.templates.dao.IBasicIdDAO;



@Repository
public interface UsuarioPendienteDAO extends IBasicIdDAO<UsuarioPendiente>{
	public UsuarioPendiente findByCorreo(String correo);
	public Page<UsuarioPendiente> findByCentro(Centro centro, Pageable pageable);
	public List<UsuarioPendiente> findByCentro(Centro centro);
}
