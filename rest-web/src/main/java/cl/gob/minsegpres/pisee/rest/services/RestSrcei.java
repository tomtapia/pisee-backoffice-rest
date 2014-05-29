package cl.gob.minsegpres.pisee.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cl.gob.minsegpres.pisee.rest.business.CallerServiceBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.ConfigProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;
import cl.gob.minsegpres.pisee.rest.util.ValidationUtil;

@Path("/srcei")
@Component
@Scope("singleton")
public class RestSrcei {

	private final static Log LOGGER = LogFactory.getLog(RestSrcei.class);
	
	@GET
	@Path("v1/consultaCertificadoNacimientoGENCHI")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getConsultaCertificadoNacimientoGENCHI(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		
		long startTime = System.currentTimeMillis();
		
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		ValidationUtil validationUtil = new ValidationUtil();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);
		inputParameter.addBodyParameter(ParametersName.OAUTH_SCOPE, ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_GENCHI);

		respuesta = validationUtil.validateRutParameters(inputParameter);
		if (null == respuesta){
			respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_GENCHI, inputParameter);
		}		
		
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info("getConsultaCertificadoNacimientoGENCHI - TIME == " + (endTime - startTime) + " MILISECONDS");
		
	    return value;		
    }


}
