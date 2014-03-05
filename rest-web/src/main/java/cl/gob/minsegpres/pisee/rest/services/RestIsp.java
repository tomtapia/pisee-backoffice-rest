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

@Path("/isp")
@Component
@Scope("singleton")
public class RestIsp {

	private final static Log LOGGER = LogFactory.getLog(RestIsp.class);
	private static final String _METHOD_GERLISTADOESPERACORAZON = "getListadoEsperaCorazon";
	
	@GET
	@Path("listadoEsperaCorazon")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getListadoEsperaCorazon(@QueryParam("pisee_token") String piseeToken) {
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
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);
		inputParameter.addBodyParameter(ParametersName.OAUTH_SCOPE, ProveedoresServicios.ISP__LISTADO_ESPERA_CORAZON);
		if (isValidParameters(_METHOD_GERLISTADOESPERACORAZON, inputParameter)){			
			respuesta = restService.callService(ProveedoresServicios.ISP__LISTADO_ESPERA_CORAZON, inputParameter);
		}else{
			respuesta = createValidParameterError(_METHOD_GERLISTADOESPERACORAZON, inputParameter);
		}		
		String value = jsonUtil.toJSON(respuesta);
		d2 = new Date();
		long t2 = d2.getTime();		
		LOGGER.debug("listadoEsperaCorazon - TIME == " + (t2 - t1));
	    return value;
    }
	
	private boolean isValidParameters(String serviceName, InputParameter inputParameter){
		if (_METHOD_GERLISTADOESPERACORAZON.equals(serviceName)){
			if (!StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN))){
				return true;	
			}			
		}
		return false;
	}
	
	private PiseeRespuesta createValidParameterError(String serviceName, InputParameter inputParameter){
		PiseeRespuesta respuesta = new PiseeRespuesta();
		StringBuffer mensaje = new StringBuffer();
		if (_METHOD_GERLISTADOESPERACORAZON.equals(serviceName)){
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
