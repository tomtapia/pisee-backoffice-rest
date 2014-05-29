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

@Path("/dgmn")
@Component
@Scope("singleton")
public class RestDgmn {

	private final static Log LOGGER = LogFactory.getLog(RestDgmn.class);

	@GET
	@Path("v1/consultaSituacionMilitar")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getConsultaSituacionMilitar(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_DGMN_SITUACION_MILITAR, inputParameter, true);
		
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info("getConsultaSituacionMilitar - TIME == " + (endTime - startTime) + " MILISECONDS");
		
	    return value;
    }
	
}
