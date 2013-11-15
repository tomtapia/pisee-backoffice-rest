package cl.gob.minsegpres.pisee.rest.util;

import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

import com.google.gson.Gson;

public class JSONUtil {
	
	public String toJSON(PiseeRespuesta respuesta){
		/*
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias("respuesta", PiseeRespuesta.class);
		xstream.registerConverter(new PiseeMapConverter()); 
		return xstream.toXML(respuesta);
		*/
		
		Gson gson = new Gson();
		return gson.toJson(respuesta);
		
		
	}
	
	
	
}
