package com.mesirves.app.components.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.components.services.UsuarioService;



@Component
public class LoginProvider implements AuthenticationProvider, UserDetailsService {
	
	@Autowired
	private UsuarioService usuarios;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        return autenticar(username, password);
	}
	
	public UsernamePasswordAuthenticationToken autenticar(String username, String password)throws BadCredentialsException{
		 Usuario usuario=usuarios.getUsuarioByCorreo(username);
		 
		 if(usuario==null)
	        	throw new BadCredentialsException("Nombre de usuario incorrecto");
	        
        if(!usuario.getPassword().equalsIgnoreCase(password.trim()))
        	throw new BadCredentialsException("Contraseña incorrecta");
            
        
        return new UsernamePasswordAuthenticationToken(usuario, password, usuario.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 Usuario usuario=usuarios.getUsuarioByCorreo(username);
		 if(usuario==null)
			 throw new UsernameNotFoundException(username);
		return usuario;
	}

}
