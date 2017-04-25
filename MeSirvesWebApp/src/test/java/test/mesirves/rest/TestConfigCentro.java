package test.mesirves.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mesirves.app.components.models.config.ConfigCentro;
import com.mesirves.app.components.models.config.IdCentroPK;
import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.rest.ConfigCentroREST;
import com.mesirves.app.components.services.CentroService;
import com.mesirves.app.components.services.ConfigCentroService;
import com.mesirves.app.config.AppConfig;
import com.mesirves.app.config.SecurityConfig;
import com.mesirves.app.json.ResponseJSON;

import test.mesirves.TestUtils;
import test.mesirves.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfigCentro {
	@Autowired
	private ConfigCentroREST rest;
    @Autowired
    private ConfigCentroService config;
    @Autowired
    private CentroService centros;

	private Centro centro;
	
	
	private MockMvc mockMvc;

    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(rest).build();
		centro=TestUtils.getNewCentro();
		centros.getDAO().save(centro);
	}

	
	
	@After
	public void  drop(){
		centros.getDAO().deleteAll();
		config.getDAO().deleteAll();
	}
	
	
	@Test
	public void testGetSetParam()throws Exception{
		System.out.println("TEsting get parametro para el centro "+centro.getJsonId());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/config/centro/"+centro.getJsonId()+"/param1"))
		.andExpect(status().isOk())
		//.andDo(print())
		  .andExpect(jsonPath("$.cod").value(ResponseJSON.DESCONOCIDO));
		
		System.out.println("TEsting set parametro para el centro "+centro.getJsonId());
		
		IdCentroPK paramId=new IdCentroPK(centro.getId(),"param1");
		ConfigCentro param=new ConfigCentro();
		param.setId(paramId);
		param.setValue("ValorParametro");
		
		System.out.println("TEsting set parametro para el parametro "+paramId);
		System.out.println("ParamId: "+TestUtils.convertObjectToString(paramId));
		
		ResultActions res=mockMvc.perform(post("/config/centro").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(param)))	    		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/config/centro/"+centro.getJsonId()+"/param1"))
		.andExpect(status().isOk())
		//.andDo(print())
		  .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	}

	@Test
	public void testParams()throws Exception{
		assertTrue( config.isAutoFacturar(centro));
		config.setValue(centro, config.AUTO_FACTURAR, false);
		assertFalse( config.isAutoFacturar(centro));
		
		for(int i=0;i<10;i++){
			assertEquals(i+1, config.siguienteFactura(centro));
		}
	}

	
}
