package test.mesirves.rest.templates;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.components.providers.LoginProvider;
import com.mesirves.app.components.services.CentroService;
import com.mesirves.app.components.services.ConfigCentroService;
import com.mesirves.app.components.services.UsuarioService;
import com.mesirves.app.config.AppConfig;
import com.mesirves.app.config.SecurityConfig;
import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.templates.REST.ABasicREST;
import com.mesirves.app.templates.dao.IBasicIdDAO;
import com.mesirves.app.templates.model.AModelCentro;
import com.mesirves.app.templates.model.AModelId;
import com.mesirves.app.templates.model.AModelNombre;
import com.mesirves.app.templates.service.ABasicService;

import test.mesirves.TestUtils;
import test.mesirves.config.TestDBConfig;

/**
 * Implementación basica de los tests de REST
 * inicializa centro y usuario
 * @author eduarmar
 *
 * @param <T>
 * @param <S>
 * @param <I>
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestTemplateREST<I extends AModelId, D extends IBasicIdDAO<I>, S extends ABasicService<D>, R extends ABasicREST<S,I,D>> {
	@Autowired
    protected R rest;
	
    
    @Autowired
    protected S service;    

    protected I model;
	
	protected MockMvc mockMvc;
	
	//ITEMS
	protected Centro centro;
	protected Usuario usuario;
	
	
	//SERVICIOS
	@Autowired
	protected UsuarioService usuarios;
    @Autowired
    protected CentroService centros;
    @Autowired
    protected LoginProvider security;
    @Autowired
    protected ConfigCentroService config;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }
    
	@Before
	public void create() {
		MockitoAnnotations.initMocks(this);
		mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

		centro=TestUtils.getNewCentro();
		centros.getDAO().save(centro);
		
		usuario=TestUtils.getNewUsuario("usuario test", centro);
		usuarios.getDAO().save(usuario);
		
		model=getNewModel(true);
	}
	
	public MockHttpSession getSession(){
		UsernamePasswordAuthenticationToken principal = security.autenticar(usuario.getCorreo(), usuario.getPassword());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                new MockSecurityContext(principal));
        return session;
	}
	
	public void login()throws Exception{
		login(usuario);
	}
	
	public void login(Usuario usuario)throws Exception{
		mockMvc
		.perform(formLogin("/login").user("correo",usuario.getCorreo()).password("password",usuario.getPassword()))
		.andExpect(authenticated());
	}
	
	protected I getNewModel(boolean saveBD){
		I model=getModel();
		
		if(model!=null){
			if(model instanceof AModelCentro){				
				((AModelCentro)model).setCentro(centro);
			}		
			if(model instanceof AModelNombre){
				((AModelNombre)model).setNombre(model.getClass().getName()+" Test");
			}
			
			if(saveBD)
				service.getDAO().save(model);
		}
		return model;
	}
	
	public abstract I getModel();
	
	@After
	public void  drop(){
		service.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		usuarios.getDAO().deleteAll();				
		config.drop();
	}
	
	public abstract String getRestURL();

	
	@Test
	public void get() throws Exception {		
		I item2=getNewModel(true);


		performGET("/"+getRestURL()+"/"+item2.getId().toHexString())
			  .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    
		performGET("/"+getRestURL()+"/"+new ObjectId().toHexString())
			  .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));	
	}
	
	
	public int getModificarCode(){
		return ResponseJSON.OK;
	}
	
	public int getDeleteCode(){
		return ResponseJSON.OK;
	}
	
	/**
	 * Metodo para modificar un model para el test de modificar(por ejemplo cambiarle el nombre)
	 * @param modelAModificar
	 * @return
	 */
	public abstract I getModelTestModificar(I modelAModificar);
	
	/**
	 * Metodo que se ejecuta en el test modificar, y que sirve para comprobar el resultado de la modificacion
	 * @param res
	 */
	public abstract void postTestModificar(ResultActions res)throws Exception;
	
	@Test
	public void modificar() throws Exception {	
		I modelModificado=getModelTestModificar(model);
		
		ResultActions res=performPOST("/"+getRestURL(), modelModificado)
			.andExpect(jsonPath("$.cod").value(getModificarCode()));
		
	    postTestModificar(res);

	    if(getModificarCode()==ResponseJSON.OK){
		    modelModificado.setId(new ObjectId());
		    performPOST("/"+getRestURL(), modelModificado)
				.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	    }
	}

	
	@Test
	public void delete() throws Exception {		

		performDELETE("/"+getRestURL()+"/"+model.getId().toHexString())
			.andExpect(jsonPath("$.cod").value(getDeleteCode()));
	    
		if(getDeleteCode()==ResponseJSON.OK){
			performDELETE("/"+getRestURL()+"/"+model.getId().toHexString())
				.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
		}
		//.andDo(print())
	}
	
	public ResultActions performDELETE(String url)throws Exception{
		return mockMvc.perform(MockMvcRequestBuilders.delete(url).session(getSession())).andExpect(status().isOk());
	}
	
	public ResultActions performGET(String url)throws Exception{
		return mockMvc.perform(MockMvcRequestBuilders.get(url).session(getSession())).andExpect(status().isOk());
	}
	
	public ResultActions performPOST(String url, Object json)throws Exception{
		return mockMvc.perform(post(url).session(getSession()).contentType(TestUtils.APPLICATION_JSON_UTF8)
	                .content(TestUtils.convertObjectToJsonBytes(json)))	    		
			.andExpect(status().isOk());
	}
	
	public ResultActions performPUT(String url, Object json)throws Exception{
		return mockMvc.perform(MockMvcRequestBuilders.put(url).session(getSession()).contentType(TestUtils.APPLICATION_JSON_UTF8)
	                .content(TestUtils.convertObjectToJsonBytes(json)))	    		
			.andExpect(status().isOk());
	}
}
