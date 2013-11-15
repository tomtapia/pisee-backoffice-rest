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

@Path("/isp")
@Component
@Scope("singleton")
public class RestIsp {

	private static final String _METHOD_GERLISTADOESPERACORAZON = "getListadoEsperaCorazon";
	
	@GET
	@Path("listadoEsperaCorazon")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getListadoEsperaCorazon(@QueryParam("tokenPisee") String tokenPisee) {
		InputParameter inputParameter;
		JSONUtil jsonUtil;
		PiseeRespuesta respuesta;
		RestConnector restConnector; 
		
		inputParameter = new InputParameter();
		jsonUtil = new JSONUtil();
		restConnector = new RestConnector();

		inputParameter.addBodyParameter("tokenPisee", tokenPisee);
		
		if (isValidParameters(_METHOD_GERLISTADOESPERACORAZON, inputParameter)){			
			respuesta = restConnector.callService(ProveedoresServicios.ISP__LISTADO_ESPERA_CORAZON, inputParameter, tokenPisee);
		}else{
			respuesta = createValidParameterError(_METHOD_GERLISTADOESPERACORAZON, inputParameter);
		}		
		
	    return jsonUtil.toJSON(respuesta);
    }
	
	private boolean isValidParameters(String serviceName, InputParameter inputParameter){
		if (_METHOD_GERLISTADOESPERACORAZON.equals(serviceName)){
			if (!StringUtils.isEmpty((String)inputParameter.getBodyParameter("tokenPisee"))){
				return true;	
			}			
		}
		return false;
	}
	
	private PiseeRespuesta createValidParameterError(String serviceName, InputParameter inputParameter){
		PiseeRespuesta respuesta = new PiseeRespuesta();
		String mensaje = "";
		if (_METHOD_GERLISTADOESPERACORAZON.equals(serviceName)){
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
