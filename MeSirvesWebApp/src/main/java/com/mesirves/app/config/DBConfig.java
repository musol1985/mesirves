package com.mesirves.app.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Perfil.PERFILES;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.components.services.UsuarioService;

@Configuration
@EnableMongoRepositories("com.mesirves.app.dao")
@EnableMongoAuditing
public class DBConfig extends AbstractMongoConfiguration{
	
	@Autowired
	private UsuarioService usuarios;

	@Override
	protected String getDatabaseName() {
		return "MeSirvesDB";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient("localhost");
	}
	

	@Override
	protected Collection<String> getMappingBasePackages() {
		List<String> res=new ArrayList<String>();
		res.add("com.sot.fenix.components.models");
		res.add("com.sot.fenix.components.models.horarios");
		res.add("com.sot.fenix.components.models.citacion");
		res.add("com.sot.fenix.components.models.Test");
		return res;		
	}
	
	@Bean
    public AuditorAware<Usuario> myAuditorProvider() {
        return new AuditorAware<Usuario>(){
			@Override
			public Usuario getCurrentAuditor() {				
				return usuarios.getCurrentUsuario();
			}
        	
        };
    }

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

		if(!mongoTemplate.collectionExists(Usuario.class) || mongoTemplate.getDb().getCollection("usuario").count()==0){
			initDB(mongoTemplate);
		}
				
		return mongoTemplate;
	}
	
	
	
	
	private void initDB(MongoTemplate db){
		System.out.println("***************************************");
		System.out.println("*		La BD es nueva		   		  *");
		System.out.println("***************************************");
		
		Centro c=new Centro();
		c.setNombre("Centro ADMIN");
		c.setCorreoAdmin("root");
		c.setColor("teal");
		
		db.insert(c);
		
		Usuario u=new Usuario();
		u.setCorreo("root");
		u.setNombre("root");
		u.setPassword("16f84a#16f84a");
		u.setPerfil(PERFILES.ROOT);
		u.setCentro(c);
		
		db.insert(u);
	}



}