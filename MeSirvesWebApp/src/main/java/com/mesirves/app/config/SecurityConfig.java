package com.mesirves.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.mesirves.app.components.handlers.AjaxAwareAuthenticationEntryPoint;
import com.mesirves.app.components.handlers.LoginSuccessHandler;
import com.mesirves.app.components.models.core.Perfil;
import com.mesirves.app.components.providers.LoginProvider;
import com.mesirves.app.components.providers.TokenProvider;
 
@Configuration
@EnableWebSecurity(debug=false)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginProvider loginProvider;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private LoginSuccessHandler handlerLoginOK;
     
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginProvider);
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	.csrf().disable()
        	.authorizeRequests()
        		.antMatchers("/").permitAll() 
        		.antMatchers("/app/admin/**").hasRole(Perfil.PERFILES.ADMIN.name())
        		.antMatchers("/app/root/**").hasRole(Perfil.PERFILES.ROOT.name()) 
        		.antMatchers("/app/**").hasRole(Perfil.PERFILES.USER.name()) 
    	.and()
    		.formLogin()
    			.successHandler(handlerLoginOK)
    			//.successForwardUrl("/app")
    			.loginPage("/login") 
    				.usernameParameter("correo").passwordParameter("password")
    	.and()
    		.rememberMe().userDetailsService(loginProvider).tokenRepository(tokenProvider).rememberMeParameter("remember-me").tokenValiditySeconds(1209600)
    	.and()
    		.exceptionHandling().authenticationEntryPoint(new AjaxAwareAuthenticationEntryPoint("/login")).accessDeniedPage("/denegado");
    }
    
    @Bean(name="myAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    

}
