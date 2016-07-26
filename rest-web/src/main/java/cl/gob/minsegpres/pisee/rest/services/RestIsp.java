package cl.gob.minsegpres.pisee.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cl.gob.minsegpres.pisee.rest.business.CallerServiceBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.ConfigProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.util.MiscUtil;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

@Path("/isp")
@Component
@Scope("singleton")
public class RestIsp {

	private final static Log LOGGER = LogFactory.getLog(RestIsp.class);
	
	@GET
	@Path("v1/listado_espera_corazon")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getListadoEsperaCorazon(@QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_ISP__LISTADO_ESPERA_CORAZON, inputParameter);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_ISP__LISTADO_ESPERA_CORAZON + " - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return MiscUtil.ConvertPiseeResponse(respuesta);
    }
	
}
