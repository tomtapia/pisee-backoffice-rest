package cl.gob.minsegpres.pisee.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cl.gob.minsegpres.pisee.rest.connector.RestConnector;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.entities.services.ProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;

@Path("/mineduc")
@Component
@Scope("singleton")
public class RestMineduc {

	private static final String _METHOD_GETLICENCIA = "getLicencia";

	@GET
	@Path("licenciaMedia")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getLicencia(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("tokenPisee") String tokenPisee) {
		InputParameter inputParameter;
		JSONUtil jsonUtil;
		PiseeRespuesta respuesta;
		RestConnector restConnector; 
		
		inputParameter = new InputParameter();
		jsonUtil = new JSONUtil();
		restConnector = new RestConnector();
		
		inputParameter.addBodyParameter("numero", rut);
		inputParameter.addBodyParameter("dv", dv);
		inputParameter.addBodyParameter("tokenPisee", tokenPisee);
		
		if (isValidParameters(_METHOD_GETLICENCIA, inputParameter)){			
			respuesta = restConnector.callService(ProveedoresServicios.MINEDUC__LICENCIA_ENSENANZA_MEDIA, inputParameter, tokenPisee);
		}else{
			respuesta = createValidParameterError(_METHOD_GETLICENCIA, inputParameter);
		}
		return jsonUtil.toJSON(respuesta);
    }

	private boolean isValidParameters(String serviceName, InputParameter inputParameter){
		if (_METHOD_GETLICENCIA.equals(serviceName)){
			if (!StringUtils.isEmpty((String)inputParameter.getBodyParameter("numero")) 
					&& !StringUtils.isEmpty((String)inputParameter.getBodyParameter("dv")) 
						&& !StringUtils.isEmpty((String)inputParameter.getBodyParameter("tokenPisee"))){
				return true;	
			}			
		}
		return false;
	}
	
	private PiseeRespuesta createValidParameterError(String serviceName, InputParameter inputParameter){
		PiseeRespuesta respuesta = new PiseeRespuesta();
		String mensaje = "";
		if (_METHOD_GETLICENCIA.equals(serviceName)){
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter("numero"))){
				mensaje = mensaje + "rut ";
			}
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter("dv"))){
				mensaje = mensaje + "dv ";
			}			
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter("tokenPisee"))){
				mensaje = mensaje + "tokenPisee ";
			}		
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_PARAMETER);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_NULL_PARAMETER + mensaje);			
		}
		return respuesta;
	}
	
	
	
	
	
}
