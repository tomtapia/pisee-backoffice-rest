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

@Path("/sii")
@Component
@Scope("singleton")
public class RestSii {

	private final static Log LOGGER = LogFactory.getLog(RestSii.class);

	@GET
	@Path("v1/consulta_datos_contribuyente")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getConsultaDatosContribuyente(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SII_DATOS_CONTRIBUYENTE, inputParameter, true);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SII_DATOS_CONTRIBUYENTE + " - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return MiscUtil.ConvertPiseeResponse(respuesta);
    }		

	@GET
	@Path("v1/consulta_actividad_econonomica")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getConsultaActividadEconomica(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SII_ACTIVIDADES_ECONOMICAS, inputParameter, true);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SII_ACTIVIDADES_ECONOMICAS + " - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return MiscUtil.ConvertPiseeResponse(respuesta);
    }
	
	@GET
	@Path("v1/consulta_fecha_inicio_actividades")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getConsultaFechaInicioActividades(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SII_FECHA_INICIO_ACTIVIDADES, inputParameter, true);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SII_FECHA_INICIO_ACTIVIDADES + " - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return MiscUtil.ConvertPiseeResponse(respuesta);
    }	
	
	@GET
	@Path("v1/consulta_unidad_habitacional_comuna")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getConsultaUnidadHabitacionalComuna(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SII_UNIDAD_HABITACIONAL_COMUNA, inputParameter, true);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SII_UNIDAD_HABITACIONAL_COMUNA + " - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return MiscUtil.ConvertPiseeResponse(respuesta);
    }	
	
	@GET
	@Path("v1/consulta_propiedades_comuna")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getConsultaPropiedadesComuna(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SII_RUT_PROPIEDADES_COMUNA, inputParameter, true);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SII_RUT_PROPIEDADES_COMUNA + " - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return MiscUtil.ConvertPiseeResponse(respuesta);
    }	
	
}
