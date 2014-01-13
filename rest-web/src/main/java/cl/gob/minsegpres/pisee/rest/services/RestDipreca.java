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

import cl.gob.minsegpres.pisee.rest.connector.RestConnector;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.entities.services.ProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;

@Path("/dipreca")
@Component
@Scope("singleton")
public class RestDipreca {

	private final static Log LOGGER = LogFactory.getLog(RestDipreca.class);
	
	private static final String _METHOD_GETCONSULTAIMPONENTES = "getConsultaImponentes";

	@GET
	@Path("consultaImponentes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	//@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public String getConsultaImponentes(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("tokenPisee") String tokenPisee) {
		
		Date d1,d2;
		d1 = new Date();
		long t1 = d1.getTime();
		
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
		
		if (isValidParameters(_METHOD_GETCONSULTAIMPONENTES, inputParameter)){
			respuesta = restConnector.callService(ProveedoresServicios.DIPRECA__CONSULTA_IMPONENTES, inputParameter, tokenPisee);			
		}else{
			respuesta = createValidParameterError(_METHOD_GETCONSULTAIMPONENTES, inputParameter);
		}
		
		String value = jsonUtil.toJSON(respuesta);
		d2 = new Date();
		long t2 = d2.getTime();		
		LOGGER.info("getConsultaImponentes - TIME == " + (t2 - t1));
	    return value;
    }

	private boolean isValidParameters(String serviceName, InputParameter inputParameter){
		if (_METHOD_GETCONSULTAIMPONENTES.equals(serviceName)){
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
		if (_METHOD_GETCONSULTAIMPONENTES.equals(serviceName)){
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