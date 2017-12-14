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
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;
import cl.gob.minsegpres.pisee.rest.util.MiscUtil;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

@Path("/srcei")
@Component
@Scope("singleton")
public class RestSrcei {

	private final static Log LOGGER = LogFactory.getLog(RestSrcei.class);
	
	@GET
	@Path("v1/consulta_certificado_nacimiento_genchi")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getConsultaCertificadoNacimientoGENCHI(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_GENCHI, inputParameter, true);
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_GENCHI + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		
	    return value;	    
    }
	
	@GET
	@Path("v1/consulta_informacion_personal_lobby")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getConsultaInformacionPersonalLobby(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__INFORMACION_PERSONAL_LOBBY, inputParameter, true);
		
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__INFORMACION_PERSONAL_LOBBY + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		
	    return value;	 
    }		
	
	@GET
	@Path("v2/consulta_informacion_personal_lobby")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getConsultaInformacionPersonalLobby2(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__INFORMACION_PERSONAL_LOBBY, inputParameter, true);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__INFORMACION_PERSONAL_LOBBY + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponse(respuesta);	 
    }
	
	
	@GET
	@Path("certificado_nacimiento_segpres")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertificadoNacimientoSegpres(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_SEGPRES, inputParameter, false);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_SEGPRES + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponseNoSobre(respuesta);
    }
	
	
	@GET
	@Path("certificado_matrimonio_segpres")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertificadoMatrimonioSegpres(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_MATRIMONIO_SEGPRES, inputParameter, false);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_MATRIMONIO_SEGPRES + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponseNoSobre(respuesta);
    }
	
	
	@GET
	@Path("certificado_defuncion_segpres")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertificadoDefuncionSegpres(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_DEFUNCION_SEGPRES, inputParameter, false);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_DEFUNCION_SEGPRES + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponseNoSobre(respuesta);
    }
	
	
	@GET
	@Path("certificado_matricula_segpres")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertificadoMatriculaSegpres(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_MATRICULA_SEGPRES, inputParameter, false);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_MATRICULA_SEGPRES + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponseNoSobre(respuesta);
    }
	
	
	@GET
	@Path("certificado_cese_convivencia_segpres")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertificadoCeseConvivenciaSegpres(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_CESE_CONVIVENCIA_SEGPRES, inputParameter, false);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_CESE_CONVIVENCIA_SEGPRES + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponseNoSobre(respuesta);
    }
	
	
	private Response getCertificadoParaTotem(String rut, String dv, String piseeToken, String tramite) {
		long startTime = System.currentTimeMillis();
		InputParameter inputParameter = new InputParameter();		
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		respuesta = restBusiness.callService(tramite, inputParameter, false);
		
		long endTime = System.currentTimeMillis();		
		LOGGER.info(tramite + " - TIME == " + (endTime - startTime) + " MILISECONDS");
		return MiscUtil.ConvertPiseeResponseNoSobre(respuesta);
	}
	
	
	
}
