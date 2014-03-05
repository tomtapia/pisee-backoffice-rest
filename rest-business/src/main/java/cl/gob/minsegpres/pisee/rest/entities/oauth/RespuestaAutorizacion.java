package cl.gob.minsegpres.pisee.rest.entities.oauth;

import java.util.List;
import java.util.Map;

public class RespuestaAutorizacion {

	private String accessToken;
	private String errorMessage;
	private Map<String, Object> resultado;
	
	//-- getters
	public String getAccessToken() {
		return accessToken;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public Map<String, Object> getResultado() {
		return resultado;
	}
	
	//-- setters
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setResultado(Map<String, Object> resultado) {
		this.resultado = resultado;
	}

	//--	
	@SuppressWarnings("unchecked")
	public List<String> getScopes(){
		return (List<String>)resultado.get("scopes");
	}
	
	public boolean isValidAccessToken(){
		if (null == errorMessage){
			return true;
		} else{
			return false;
		}
	}
	
}
