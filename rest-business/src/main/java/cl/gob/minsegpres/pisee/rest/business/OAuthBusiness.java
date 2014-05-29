package cl.gob.minsegpres.pisee.rest.business;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;

import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.oauth.RespuestaAutorizacion;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class OAuthBusiness {

	//TODO: Dejar en un propertie
	private static final String _SERVICE_OAUTH = "http://192.168.29.205/claveunica/oauth2/check_token?access_token=";
	private static final String _TEXT_HTML = "text/html";
	//private static final String _APPLICATION_JSON = "application/json";
	private final static Log LOGGER = LogFactory.getLog(OAuthBusiness.class);
	
	public boolean isValidRespuestaAutorizacion(RespuestaAutorizacion respuesta, InputParameter inputParameter, ConfiguracionServicio configuracion){

		//TODO: Validate scope
		
		//String scope = (String)inputParameter.getBodyParameter(ParametersName.SCOPE);
//		String scope = "educacion";
//		List<String> scopes = respuesta.getScopes();
//		for (String tmpScope: scopes){
//			if (scope.equals(tmpScope)){
//				return false;
//			}
//		}
		return true;
	}
	
	
	
	public InputParameter processParameters(RespuestaAutorizacion respuesta, InputParameter inputParameter){
		try{
			String clientId, ownerId;
			String rut, dv;

			//TODO: Validate scope con el servicio en cuestion
			//String scope = (String)inputParameter.getBodyParameter(ParametersName.OAUTH_SCOPE);
			//ProveedoresServicios.MINEDUC__LICENCIA_ENSENANZA_MEDIA			
			
			ownerId = (String)respuesta.getResultado().get(ParametersName.OAUTH_OWNER_ID);
			String[] arrRut = ownerId.split("-");
			rut = arrRut[0];
			dv = arrRut[1];
			
			clientId = (String)respuesta.getResultado().get(ParametersName.OAUTH_CLIENT_ID);
			inputParameter.addBodyParameter(ParametersName.RUT, rut);
			inputParameter.addBodyParameter(ParametersName.DV, dv);
			inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, clientId);
			
			return inputParameter;
		}catch(Exception e){
			LOGGER.error("Ocurrio un error al extraer los datos de OWNER y CLIENT en la respuesta del servicio OAUTH");
			e.printStackTrace();
		}
		
		return null;
	}	
	
	
	
	
	
	public RespuestaAutorizacion processAccessToken(String accessToken){
		RespuestaAutorizacion respuestaAutorizacion = null;
		String result;
		result = callServiceOAuth(accessToken);
		if (null != result){
			respuestaAutorizacion = readRespuesta(result);
		}
		return respuestaAutorizacion;
	}
	
	private String callServiceOAuth(String accessToken){
		LOGGER.info("Calling... = " + _SERVICE_OAUTH + accessToken);
		try{
			Client client = Client.create();
			client.setConnectTimeout(50000);
			client.setReadTimeout(50000);
			WebResource webResource = client.resource(_SERVICE_OAUTH + accessToken);
			//ClientResponse response = webResource.accept(_APPLICATION_JSON).get(ClientResponse.class);
			ClientResponse response = webResource.accept(_TEXT_HTML).get(ClientResponse.class);
			if (response.getStatus() == 200) {
				String result = response.getEntity(String.class);
				LOGGER.info(_SERVICE_OAUTH + accessToken + ", result = " + result);
				return result;
			}else{
				LOGGER.info(_SERVICE_OAUTH + accessToken + ", Servicio OAUTH no disponible, error = " + response.getStatus());	
			}
		}catch (Exception e){
			LOGGER.error("Error en la llamada al servicio de OAUTH");
			e.printStackTrace();
		}
		return null;
	}
	
	private RespuestaAutorizacion readRespuesta(String result){
		Map<String,Object> map; 
		RespuestaAutorizacion respuesta;
		respuesta = new RespuestaAutorizacion();
		map = convertResult(result);
	    respuesta.setResultado(map);
	    if (null != respuesta.getResultado().get(ParametersName.OAUTH_ERROR)){
	    	respuesta.setErrorMessage(respuesta.getResultado().get(ParametersName.OAUTH_ERROR).toString());    
	    }		
		return respuesta; 
	}
	
	private Map<String,Object> convertResult(String result){
		return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
	}
}
