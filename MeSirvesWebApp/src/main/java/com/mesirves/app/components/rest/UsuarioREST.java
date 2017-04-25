package com.mesirves.app.components.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Perfil.PERFILES;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.components.models.core.UsuarioPendiente;
import com.mesirves.app.components.providers.LoginProvider;
import com.mesirves.app.components.services.CentroService;
import com.mesirves.app.components.services.UsuarioService;
import com.mesirves.app.dao.UsuarioDAO;
import com.mesirves.app.json.ListJSON;
import com.mesirves.app.json.PageJSON;
import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.json.request.LoginJSON;
import com.mesirves.app.json.request.NuevoPendienteJSON;
import com.mesirves.app.json.request.RegistrarJSON;
import com.mesirves.app.templates.REST.ABasicREST;
import com.mesirves.app.templates.model.IUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioREST extends ABasicREST<UsuarioService, Usuario, UsuarioDAO>{
	
	@Autowired
	private CentroService centros;
	
	@Autowired
	@Qualifier("myAuthenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private LoginProvider loginProvider;
	
	@RequestMapping(method=RequestMethod.POST, path="/login")
	public ResponseJSON<Usuario> login(@RequestBody LoginJSON usuario) {			
 
	    try {
	    	UsernamePasswordAuthenticationToken token=loginProvider.autenticar(usuario.getCorreo(), usuario.getPassword());
	    	Authentication auth = authenticationManager.authenticate(token);
	    	SecurityContextHolder.getContext().setAuthentication(auth);
	    	return new ResponseJSON<Usuario>(ResponseJSON.OK);
	    } catch (BadCredentialsException e) {
	    	return new ResponseJSON<Usuario>(ResponseJSON.NO_EXISTE);
	    }
	}


	@RequestMapping(method=RequestMethod.GET, path="/current")
    public Usuario current() {
		String current=service.getCurrent();
		
		Usuario usuario=service.getUsuarioByCorreo(current);
		
		return usuario;
    }

	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Usuario> modificar(@RequestBody Usuario usuario) {
		Usuario usuarioBD=service.getDAO().findOne(usuario.getId());
		if(usuarioBD==null)
			return new ResponseJSON<Usuario>(ResponseJSON.NO_EXISTE);
		
		usuarioBD.setNombre(usuario.getNombre());
		usuarioBD.setColor(usuario.getColor());
		
		service.getDAO().save(usuario);
		return new ResponseJSON<Usuario>(ResponseJSON.OK, usuario);
    }
	
	
	@RequestMapping(method=RequestMethod.POST, path="/registrar")
    public ResponseJSON<Usuario> registrar(@RequestBody RegistrarJSON registrar) {
		UsuarioPendiente uPendienteBD=service.getPendientesDAO().findOne(new ObjectId(registrar.idPendiente));

		if(uPendienteBD!=null){			
			if(service.getUsuarioByCorreo(uPendienteBD.getCorreo())==null){									
					Usuario usuario=new Usuario();
					usuario.setCorreo(uPendienteBD.getCorreo());
					usuario.setNombre(registrar.nombre);
					usuario.setPassword(registrar.password);
					usuario.setColor(uPendienteBD.getColor());
					
					Centro centro=uPendienteBD.getCentro();
					
					if(centro.getCorreoAdmin().equals(uPendienteBD.getCorreo())){
						usuario.setPerfil(PERFILES.ADMIN);
					}else{
						usuario.setPerfil(PERFILES.USER);
					}
					usuario.setCentro(centro);
				
					service.getDAO().save(usuario);
					service.getPendientesDAO().delete(uPendienteBD);
					
					if(centro.getId()==null){
						centros.getDAO().save(centro);
					}
					
				return new ResponseJSON<Usuario>(ResponseJSON.OK,usuario);
			}else{
				return new ResponseJSON<Usuario>(ResponseJSON.YA_EXISTE);
			}
		}else{
			return new ResponseJSON<Usuario>(ResponseJSON.NO_EXISTE);
		}
    }

	@RequestMapping(method=RequestMethod.GET, path="/{centro}/{page}/{size}")
    public PageJSON<Usuario> getByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {
		System.out.println(centro);
		Page<Usuario> usuariosBD;		

		usuariosBD=service.getUsuarioByCentro(centro, new PageRequest(page-1, size));

		return new PageJSON<Usuario>(usuariosBD.getSize(), usuariosBD.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="lista/{centro}")
    public ListJSON<Usuario> getListaByCentro(@PathVariable String centro) {
		
		List<Usuario> res=service.getDAO().findByCentro_id(new ObjectId(centro));

		return new ListJSON<Usuario>(ResponseJSON.OK, res);
    }
	
	@RequestMapping(method=RequestMethod.GET, path="all/{centro}/{page}/{size}")
    public PageJSON<IUsuario> getAllByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {
		System.out.println(centro);
		List<IUsuario> res=new ArrayList<IUsuario>();

		res.addAll(service.getUsuarioByCentro(centro));		
		res.addAll(service.getUsuarioPendienteByCentro(centro));
		
		int total=res.size();
		page--;
		int max=page*size+size;
		if(max>res.size())
			max=res.size();
		int ini=page*size;
		if(ini>max)
			ini=0;
		res=res.subList(ini, max);

		return new PageJSON<IUsuario>(total, res);
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/pendientes/{centro}/{page}/{size}")
    public PageJSON<UsuarioPendiente> getPendientes(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {
		System.out.println("Centro:"+centro);
		Page<UsuarioPendiente> pagina=service.getUsuarioPendienteByCentro(centro, new PageRequest(page-1, size));
		return new PageJSON<UsuarioPendiente>(pagina.getTotalElements(), pagina.getContent());     
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/pendiente")
    public ResponseJSON<UsuarioPendiente> nuevoPendiente(@RequestBody NuevoPendienteJSON nuevo) {
		UsuarioPendiente uPendiente=nuevo.usuario;
		String correo=uPendiente.getCorreo();
		
		UsuarioPendiente uPendienteBD=service.getUsuarioPendienteByCorreo(correo);
		
		if(uPendienteBD==null){			
			uPendiente.setFechaEnvio(new Date());
			
			Centro centro=centros.getDAO().findOne(new ObjectId(nuevo.centro));
			uPendiente.setCentro(centro);
			
			uPendiente=service.getPendientesDAO().save(uPendiente);
			
			service.enviarEmail(uPendiente);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK, uPendiente);
		}else{
			uPendienteBD.setFechaEnvio(new Date());
			service.enviarEmail(uPendienteBD);
			service.getPendientesDAO().save(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.YA_EXISTE);
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
	@ResponseBody
    public ResponseJSON<Usuario> eliminar(@PathVariable String id) {
		Usuario usuario=service.getDAO().findOne(new ObjectId(id));
		
		if(usuario==null){						
			return new ResponseJSON<Usuario>(ResponseJSON.NO_EXISTE);
		}else if(usuario.isAdmin()){
			return new ResponseJSON<Usuario>(ResponseJSON.ES_ADMIN);
		}else{
			service.getDAO().delete(usuario);
			
			return new ResponseJSON<Usuario>(ResponseJSON.OK);
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/pendiente/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> eliminarPendiente(@PathVariable String id) {
		UsuarioPendiente uPendienteBD=service.getPendientesDAO().findOne(new ObjectId(id));
		
		if(uPendienteBD==null){						
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.NO_EXISTE);
		}else{			
			service.getPendientesDAO().delete(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
		}
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/pendiente/correo/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> enviarCorreo(@PathVariable String id) {
		UsuarioPendiente uPendienteBD=service.getPendientesDAO().findOne(new ObjectId(id));
		
		if(uPendienteBD==null){						
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.NO_EXISTE);
		}else{			
			uPendienteBD.setFechaEnvio(new Date());
			service.getPendientesDAO().save(uPendienteBD);
						
			service.enviarEmail(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
		}
    }
	
	
	@RequestMapping(method=RequestMethod.POST, path="/reset")
    public ResponseJSON<Usuario> reset(@RequestBody  Usuario usuario) {
		return new ResponseJSON<Usuario>(ResponseJSON.OK);

    }
}

