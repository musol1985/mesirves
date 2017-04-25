package test.mesirves.services.template;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.components.services.CentroService;
import com.mesirves.app.components.services.UsuarioService;
import com.mesirves.app.config.AppConfig;
import com.mesirves.app.config.SecurityConfig;
import com.mesirves.app.templates.model.AModelId;
import com.mesirves.app.templates.service.ABasicService;

import test.mesirves.TestUtils;
import test.mesirves.config.TestDBConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@WebAppConfiguration
public abstract class ATestServiceTemplate<S extends ABasicService, M extends AModelId> {
	//ITEMS
	protected Centro centro;
	protected Usuario usuario;
	
	@Autowired
	protected S service;
	
	protected M model;
	//SERVICIOS
	@Autowired
	protected UsuarioService usuarios;
    @Autowired
    protected CentroService centros;
    
	@Before
	public void create() {
		centro=TestUtils.getNewCentro();
		centros.getDAO().save(centro);
		
		usuario=TestUtils.getNewUsuario("usuario test", centro);
		usuarios.getDAO().save(usuario);
		
		model=getNewModel();
		if(model!=null)
			service.getDAO().save(model);
	}
	
	@After
	public void delete(){
		centros.getDAO().deleteAll();
		usuarios.getDAO().deleteAll();
	}
    
	public abstract M getNewModel();
}
