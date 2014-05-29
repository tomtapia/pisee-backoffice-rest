package cl.gob.minsegpres.pisee.rest.util;

import cl.gob.minsegpres.pisee.rest.entities.oauth.OAuthValidateResponse;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

	public String toJSON(PiseeRespuesta respuesta) {
		if (null != respuesta.getMetadata()){
	    	if (respuesta.getMetadata().size() == 1){
	    		respuesta.setCuerpo(respuesta.getMetadata().get(0));
	    		respuesta.setMetadata(null);
	    	}			
		}
	    Gson gson = new GsonBuilder().setExclusionStrategies(new PiseeRESTExclusionStrategy()).create();
	    String json = gson.toJson(respuesta);
	    if (null != respuesta.getResponseRest()){
		    int largo = json.length();
		    String encabezado = json.substring(0 , largo -1);
		    StringBuffer sb = new StringBuffer();
		    sb.append(encabezado);
		    sb.append(",\"cuerpo\":");
		    sb.append(respuesta.getResponseRest());
		    sb.append("}");
	    	return sb.toString();
	    }
	    return json;
	}

	public String toJSON(OAuthValidateResponse response) {
		Gson gson = new Gson();
		return gson.toJson(response);
	}

	public boolean isJSONValid(String JSON_STRING) {
		Gson gson = new Gson();
		try {
			gson.fromJson(JSON_STRING, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}
	
}
