package com.mesirves.app.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.templates.dao.IBasicIdDAO;


@Repository
public interface UsuarioDAO extends IBasicIdDAO<Usuario>{
	public Usuario findByCorreo(String correo);
	

	public Page<Usuario> findByCentro(Centro centro, Pageable pageable);
	public List<Usuario> findByCentro(Centro centro);
	public List<Usuario> findByCentro_id(ObjectId centro);

}
