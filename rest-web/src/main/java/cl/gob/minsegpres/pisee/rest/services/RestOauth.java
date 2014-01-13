package cl.gob.minsegpres.pisee.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/oauth")
@Component
@Scope("singleton")
public class RestOauth {

	@GET
	@Path("validateClient")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String validateClient(@QueryParam("client_secret") String dv, @QueryParam("token_pisee") String tokenPisee) {
		
		return null;
	}
	
}
