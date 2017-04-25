package test.mesirves;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mesirves.app.components.models.core.Centro;
import com.mesirves.app.components.models.core.Perfil.PERFILES;
import com.mesirves.app.components.models.core.Ubicacion;
import com.mesirves.app.components.models.core.Usuario;
import com.mesirves.app.templates.model.AModelCentro;
import com.mesirves.app.templates.model.AModelNombre;

public class TestUtils {

	    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	 
	    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	        return mapper.writeValueAsBytes(object);
	    }
	    
	    public static String convertObjectToString(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();

	        return mapper.writeValueAsString(object);
	    }
	    
	    public static <T> T convertStringToObject(String object, Class<T> obj) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();

	        return mapper.readValue(object, obj);
	    }

	
	    public static <T extends AModelCentro> T generarModelCentro(Centro centro, Class<T> clase){
	    	try{
	    		AModelCentro modelCentro=clase.newInstance();
	    		modelCentro.setCentro(centro);
	    		
	    		return (T)modelCentro;
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	return null;
	    }
	    
	    
	    public static <T extends AModelNombre> T generarModelNombreCentro(Centro centro, String descripcion, Class<T> clase){
	    	T res=(T)generarModelCentro(centro, clase);
	    	res.setNombre(descripcion);
	    	return res;
	    }
	    

		
		public static Date getDate(int offset){
			Calendar date = Calendar.getInstance();
			long t= date.getTimeInMillis();
			return new Date(t + (offset * 60000));
		}
		

		
		public static Date toDate(String date){
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String fromDate(Date date){
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
	   
			return df.format(date);
		}
		
		public static String fromDateCalendar(Date date){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   
			return df.format(date);
		}
	    
	    
	    public static Centro getNewCentro(){
	    	Centro centro=new Centro();
	    	centro.setColor("purple");
			centro.setCorreoAdmin("test@test.com");
			centro.setNombre("Centro test");

			centro.setUbicacion(getNewUbicacion());
			
			return centro;
	    }
	    
	    public static Ubicacion getNewUbicacion(){			
			Ubicacion u=new Ubicacion();
			u.setCalle("Calle test");
			u.setCP("08292");
			u.setNumero("33");
			u.setPais("ES");
			u.setPoblacion("Terrassa");
			u.setProvincia("Barcelona");
			u.setPosicion(new GeoJsonPoint(1, 1));
			return u;
	    }
	    
	    public static Usuario getNewUsuario(String nombre, Centro centro){
			Usuario usuario=new Usuario();
			usuario.setCorreo("test"+nombre);
			usuario.setNombre(nombre);
			usuario.setPassword("pass");
			usuario.setPerfil(PERFILES.ROOT);
			usuario.setCentro(centro);
			
			return usuario;		
		}
	    
	    public static Usuario getNewUsuarioNoRoot(String nombre, Centro centro){
	    	Usuario usuario=new Usuario();
			usuario.setCorreo("testUsuario"+nombre);
			usuario.setNombre(nombre);
			usuario.setPassword("pass");
			usuario.setPerfil(PERFILES.USER);
			usuario.setCentro(centro);
			return usuario;
	    }
	    
}
