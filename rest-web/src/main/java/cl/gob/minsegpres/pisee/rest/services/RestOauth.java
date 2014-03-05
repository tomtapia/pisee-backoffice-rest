package cl.gob.minsegpres.pisee.rest.services;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionOAuth;
import cl.gob.minsegpres.pisee.core.service.ConfiguracionOAuthService;

import cl.gob.minsegpres.pisee.rest.entities.oauth.OAuthValidateResponse;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;

@Path("/oauth")
@Component
@Scope("singleton")
public class RestOauth {

	private final static Log LOGGER = LogFactory.getLog(RestOauth.class);
	
	@GET
	@Path("validateClient")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String validateClient(@QueryParam("client_id") String clientId,
								@QueryParam("client_secret") String clientSecret, 
								@QueryParam("redirect_uri") String redirectUri) {
		
		Date d1,d2;
		d1 = new Date();
		long t1 = d1.getTime();
		JSONUtil jsonUtil;
		jsonUtil = new JSONUtil();		
		
		ConfiguracionOAuthService oAuthService = new ConfiguracionOAuthService();
		ConfiguracionOAuth configuracionOAuth = null;
		
		if (!StringUtils.isEmpty(clientId) && !StringUtils.isEmpty(clientSecret) && StringUtils.isEmpty(redirectUri)){
			configuracionOAuth = oAuthService.findConfigOAuthByClientIdAndSecret(clientId, clientSecret);	
		} else if (!StringUtils.isEmpty(clientId) && StringUtils.isEmpty(clientSecret) && !StringUtils.isEmpty(redirectUri)){			
			configuracionOAuth = oAuthService.findConfigOAuthByClientIdAndRedirectURI(clientId, redirectUri);
		} else if (!StringUtils.isEmpty(clientId) && !StringUtils.isEmpty(clientSecret) && !StringUtils.isEmpty(redirectUri)){			
			configuracionOAuth = oAuthService.findConfigOAuthByClientIdAndSecretAndRedirectURI(clientId, clientSecret, redirectUri);
		}				
		
		OAuthValidateResponse response = new OAuthValidateResponse();
		if (null != configuracionOAuth){
			response.setClientId(configuracionOAuth.getClientId());
			response.setClientSecret(configuracionOAuth.getClientSecret());
			response.setName(configuracionOAuth.getName());
			response.setRedirectUri(configuracionOAuth.getRedirectsURI());
		} else {
			response.setError("Token invalido");
		}
		
		String value = jsonUtil.toJSON(response);
		d2 = new Date();
		long t2 = d2.getTime();		
		LOGGER.debug("validateClient - TIME == " + (t2 - t1));
	    return value;
	}
	
}
