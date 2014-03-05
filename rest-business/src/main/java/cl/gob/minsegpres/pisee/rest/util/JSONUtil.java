package cl.gob.minsegpres.pisee.rest.util;

import cl.gob.minsegpres.pisee.rest.entities.oauth.OAuthValidateResponse;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

import com.google.gson.Gson;


public class JSONUtil {
	
	public String toJSON(PiseeRespuesta respuesta){
		Gson gson = new Gson();
		return gson.toJson(respuesta);
	}
	
	public String toJSON(OAuthValidateResponse response){
		Gson gson = new Gson();
		return gson.toJson(response);
	}	
	
	/*
	public static void main(String[] args) {
		String jsonStr2 = "{'owner_id': '15382984-5','client_id': 'hola','scopes': ['situacionmilitar','otro']}";
		String jsonStr1 = "{'error':'Access token is not valid'}";
		String jsonStr3 = "{'owner_id':'15382984-5','client_id':'mineduc','scopes':['infogeneral, situacionmilitar']}";
	    Map<String, Object> map = new Gson().fromJson(jsonStr1, new TypeToken<Map<String, Object>>() {}.getType());
	    System.out.println("0.- key = " + map);
	    Object error = map.get("error");	    
	    System.out.println("1.- error = " + error);
	    if (null == error){
		    Object key = map.get("owner_id");
		    Object scopes = map.get("scopes");
		    System.out.println("2.- key = " + key);
		    System.out.println("3.- scopes = " + scopes + " - " + scopes.getClass().toString());
			System.out.println("4.- map = " + map);	    	
	    }
		
		//-----
		
		Client client = Client.create();
		WebResource webResource = client.resource("https://www.pisee.cl/rest-web/dipreca/consultaImponentes/4895102/3/D1n4MtH");
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		System.out.println(response.getEntity(String.class));
		
	}
	*/
	


	
}
