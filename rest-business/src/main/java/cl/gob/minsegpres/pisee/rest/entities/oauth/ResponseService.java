package cl.gob.minsegpres.pisee.rest.entities.oauth;

public class ResponseService {

	//--
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String nombreConsumidor;
	
	//--
	public String getClientId() {
		return clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public String getNombreConsumidor() {
		return nombreConsumidor;
	}
	
	//--
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public void setNombreConsumidor(String nombreConsumidor) {
		this.nombreConsumidor = nombreConsumidor;
	}
	
	
}
