package cl.gob.minsegpres.pisee.rest.entities.oauth;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OAuthValidateResponse {

	@SerializedName("client_id")
	private String clientId;
	
	@SerializedName("client_secret")
	private String clientSecret;

	@SerializedName("name")
	private String name;
	
	@SerializedName("redirect_uri")
	private List<String> redirectUri;
	
	@SerializedName("error")
	private String error;
	
	//--
	public String getClientId() {
		return clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public String getClientName() {
		return name;
	}	
	public List<String> getRedirectUri() {
		return redirectUri;
	}
	public String getError() {
		return error;
	}
	
	//--
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public void setRedirectUri(List<String> redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	//--
	public void addRedirectUri(String redirect){
		if (null == redirectUri){
			redirectUri = new ArrayList<String>();
		}
		redirectUri.add(redirect);
	}
	public void setError(String error) {
		this.error = error;
	}	
	
}
