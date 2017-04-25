package test.mesirves.rest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Perfil.PERFILES;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.components.models.core.UsuarioPendiente;
import com.mesirves.app.components.rest.UsuarioREST;
import com.mesirves.app.components.services.UsuarioService;
import com.mesirves.app.config.AppConfig;
import com.mesirves.app.config.SecurityConfig;
import com.mesirves.app.dao.UsuarioDAO;
import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.json.request.NuevoPendienteJSON;
import com.mesirves.app.json.request.RegistrarJSON;

import test.mesirves.TestUtils;
import test.mesirves.config.TestDBConfig;
import test.mesirves.rest.templates.TestTemplateREST;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUsuario extends TestTemplateREST<Usuario, UsuarioDAO, UsuarioService, UsuarioREST> {
    
    @Autowired
    private AuthenticationManager auManager;

    
	
	
	
	private UsuarioPendiente getUsuarioPendienteTest(){
		UsuarioPendiente uP=new UsuarioPendiente();
		uP.setCentro(centro);
		uP.setCorreo("test@test.com");
		uP.setNombre("TestEnviarCorreo");
		usuarios.getPendientesDAO().save(uP);
		System.out.println("Usuario Pendiente: "+uP.getId().toHexString());
		return uP;
	}
	
	private Usuario getUsuarioAdmin(){
		return getUsuarioTest(PERFILES.ADMIN);
	}
	
	private Usuario getUsuario(){
		return getUsuarioTest(PERFILES.USER);
	}
	
	private Usuario getUsuarioTest(PERFILES perfil){
		Usuario uP=new Usuario();
		uP.setPassword("passTest");
		uP.setCentro(centro);
		uP.setCorreo(perfil.name()+"@test.com");
		uP.setNombre("TestUsuario"+perfil.name());
		uP.setPerfil(perfil);
		usuarios.getDAO().save(uP);
		return uP;
	}
	
	
	@After
	public void  drop(){
		usuarios.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		usuarios.getPendientesDAO().deleteAll();
	}
	
	
	@Test
    public void testInit(){
		List<Centro> list =centros.getDAO().findAll();
		assertEquals(1, list.size());
		List<Usuario> usuarios=this.usuarios.getDAO().findAll();
		assertEquals(2, usuarios.size());
	}
	
	@Test
	public void testGetById() throws Exception {
	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/"+usuario.getId().toHexString()))
	    		.andExpect(status().isOk())
	    		//.andDo(print())
	    		.andExpect(jsonPath("$.data.id", containsString(usuario.getId().toHexString())))
	    		.andExpect(jsonPath("$.data.centro.id", containsString(centro.getId().toHexString())));
    }
	
	@Test
	public void nuevoPendiente() throws Exception {
		NuevoPendienteJSON request=new NuevoPendienteJSON();
		
		request.centro=centro.getId().toHexString();
		request.usuario=new UsuarioPendiente();
		request.usuario.setNombre("Edu");
		request.usuario.setCorreo("edu@gmail.com");
		
		
	    mockMvc.perform(post("/usuario/pendiente").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0))
		.andExpect(jsonPath("$.data.correo", containsString("edu@gmail.com")));
	    
	    
	    mockMvc.perform(post("/usuario/pendiente").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.YA_EXISTE));
	}
	
	@Test
	public void enviarCorreo() throws Exception {
		UsuarioPendiente uPendiente=getUsuarioPendienteTest();
	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/pendiente/correo/"+uPendiente.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0));
	    
	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/pendiente/correo/"+new ObjectId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void eliminarPendiente() throws Exception {
		UsuarioPendiente uPendiente=getUsuarioPendienteTest();
	    mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/pendiente/"+uPendiente.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0));
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/pendiente/"+uPendiente.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void eliminarUsuario() throws Exception {
		Usuario u=getUsuarioAdmin();
	    mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/"+u.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.ES_ADMIN));
	    
	    u=getUsuario();
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/"+u.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/usuario/"+u.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void registrar() throws Exception {
		UsuarioPendiente uP=getUsuarioPendienteTest();
		RegistrarJSON request=new RegistrarJSON();
		
		request.idPendiente=uP.getId().toHexString();
		request.nombre=uP.getNombre()+"PostRegistro";
		request.password="testPassword";		
		
	    mockMvc.perform(post("/usuario/registrar").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0))
		.andExpect(jsonPath("$.data.nombre", containsString(uP.getNombre()+"PostRegistro")))
		.andExpect(jsonPath("$.data.correo", containsString(uP.getCorreo())));
	    
	    Usuario u=usuarios.getUsuarioByCorreo(uP.getCorreo());

	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/"+u.getId().toHexString()))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data.correo", containsString(uP.getCorreo())));
	}

	
	@Test
	public void getByCentro() throws Exception {
	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/"+centro.getId().toHexString()+"/1/10"))
			.andExpect(status().isOk())
			//.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(usuario.getId().toHexString()));
	}
	
	@Test
	public void getAllByCentro() throws Exception {
		Usuario u1=getUsuario();
		UsuarioPendiente uP=getUsuarioPendienteTest();
		
	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/all/"+centro.getId().toHexString()+"/1/10"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.total").value(4));
	}
	
	@Test
	public void getCurrent() throws Exception {		  
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testusuario test", "pass");
        Authentication authentication = auManager.authenticate(token);

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    mockMvc.perform(MockMvcRequestBuilders.get("/usuario/current"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(usuario.getId().toHexString()));
	}
	
	

	@Override
	public Usuario getModel() {
		return TestUtils.getNewUsuario("hola", centro);
	}

	@Override
	public String getRestURL() {
		return "usuario";
	}

	@Override
	public Usuario getModelTestModificar(Usuario modelAModificar) {
		return modelAModificar;
	}

	@Override
	public void postTestModificar(ResultActions res) throws Exception {
		
	}

	@Override
	public int getDeleteCode() {
		return ResponseJSON.ES_ADMIN;
	}

	@Override
	@Test
	public void delete() throws Exception {
		super.delete();
		
		Usuario u=TestUtils.getNewUsuario("otroUsu", centro);
		u.setCorreo("otroUsu@gmail.com");
		u.setPerfil(PERFILES.USER);
		usuarios.getDAO().save(u);
		
		 mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+u.getId().toHexString()))
			.andExpect(status().isOk())
		    .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
		 
		 mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+u.getId().toHexString()))
			.andExpect(status().isOk())
		    .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}


}
