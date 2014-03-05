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

import cl.gob.minsegpres.pisee.rest.business.ProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.business.RestBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

@Path("/dipreca")
@Component
@Scope("singleton")
public class RestDipreca {

	private final static Log LOGGER = LogFactory.getLog(RestDipreca.class);
	
	private static final String _METHOD_GETCONSULTAIMPONENTES = "getConsultaImponentes";

	@GET
	@Path("consultaImponentes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getConsultaImponentes(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		Date d1,d2;
		d1 = new Date();
		long t1 = d1.getTime();
		InputParameter inputParameter;
		JSONUtil jsonUtil;
		PiseeRespuesta respuesta;
		RestBusiness restService; 
		inputParameter = new InputParameter();
		jsonUtil = new JSONUtil();
		restService = new RestBusiness();
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);
		inputParameter.addBodyParameter(ParametersName.OAUTH_SCOPE, ProveedoresServicios.DIPRECA__CONSULTA_IMPONENTES);
		if (isValidParameters(_METHOD_GETCONSULTAIMPONENTES, inputParameter)){
			respuesta = restService.callService(ProveedoresServicios.DIPRECA__CONSULTA_IMPONENTES, inputParameter);			
		}else{
			respuesta = createValidParameterError(_METHOD_GETCONSULTAIMPONENTES, inputParameter);
		}
		String value = jsonUtil.toJSON(respuesta);
		d2 = new Date();
		long t2 = d2.getTime();		
		LOGGER.debug("consultaImponentes - TIME == " + (t2 - t1));
	    return value;
    }

	private boolean isValidParameters(String serviceName, InputParameter inputParameter){
		if (_METHOD_GETCONSULTAIMPONENTES.equals(serviceName)){
			if (!StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.RUT)) 
					&& !StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.DV)) 
						&& !StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN))){
				return true;	
			}			
		}
		return false;
	}
	
	private PiseeRespuesta createValidParameterError(String serviceName, InputParameter inputParameter){
		PiseeRespuesta respuesta = new PiseeRespuesta();
		StringBuffer mensaje = new StringBuffer();
		if (_METHOD_GETCONSULTAIMPONENTES.equals(serviceName)){
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.RUT))){
				mensaje.append(ParametersName.RUT);
				mensaje.append(AppConstants.SPACE);
			}
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.DV))){
				mensaje.append(ParametersName.DV);
				mensaje.append(AppConstants.SPACE);
			}			
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN))){
				mensaje.append(ParametersName.PISEE_TOKEN);
				mensaje.append(AppConstants.SPACE);
			}		
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_PARAMETER);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_NULL_PARAMETER + mensaje.toString());			
		}
		return respuesta;
	}
	
}
