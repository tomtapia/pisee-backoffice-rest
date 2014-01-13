package cl.gob.minsegpres.pisee.rest.util;

import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

import com.google.gson.Gson;


public class JSONUtil {
	
	public String toJSON(PiseeRespuesta respuesta){
		Gson gson = new Gson();
		return gson.toJson(respuesta);
	}
	
	/*
	public static void main(String[] args) {
		//String jsonStr = "{'owner_id': '15382984-5','client_id': 'hola','scopes': 'situacionmilitar'}";	
		String jsonStr = "{'owner_id': '15382984-5','client_id': 'hola','scopes': ['situacionmilitar','otro']}";
	    Map<String, Object> map = new Gson().fromJson(jsonStr, new TypeToken<Map<String, Object>>() {}.getType());
	    Object key = map.get("owner_id");
	    System.out.println(key);
	    Object scopes = map.get("scopes");
	    System.out.println(scopes + " - " + scopes.getClass().toString());
		System.out.println("map == " + map);
		
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
