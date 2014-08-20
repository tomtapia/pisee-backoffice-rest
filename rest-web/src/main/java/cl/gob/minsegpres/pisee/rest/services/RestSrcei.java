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
		inputParameter.addBodyParameter(ParametersName.OAUTH_SCOPE, ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_GENCHI);
		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_DGMN_SITUACION_MILITAR, inputParameter, true);
		
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info("getConsultaCertificadoNacimientoGENCHI - TIME == " + (endTime - startTime) + " MILISECONDS");
		
	    return value;	    
    }

	//--

	@GET
	@Path("v1/consulta_informacion_personal_pisee")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String getConsultaInformacionPersonalPISEE(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken) {
		/*
		long startTime = System.currentTimeMillis();
		
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		inputParameter.addBodyParameter(ParametersName.RUT, rut);
		inputParameter.addBodyParameter(ParametersName.DV, dv);
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);		
		inputParameter.addBodyParameter(ParametersName.OAUTH_SCOPE, ConfigProveedoresServicios.SOAP_SRCEI__CERTIFICADO_NACIMIENTO_GENCHI);
		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_DGMN_SITUACION_MILITAR, inputParameter, true);
		
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info("getConsultaSituacionMilitar - TIME == " + (endTime - startTime) + " MILISECONDS");
		
	    return value;	 
	    */
		
		
		
		LOGGER.info("getConsultaInformacionPersonalPISEE - rut = " + rut + " , dv = " + dv + " , pisee_token = " + piseeToken);
		
		StringBuffer result = new StringBuffer();
		
		result.append("{");
		result.append("\"encabezado\": {");
		result.append("\"id_sobre\": \"150501008220140613000000100\",");
		result.append("\"emisor_sobre\": \"SRCeI\",");
		result.append("\"estado_sobre\": \"00\",");
		result.append("\"glosa_sobre\": \"TRANSACCION EXITOSA\",");
		result.append("\"proveedor\": \"SRCeI\",");
		result.append("\"servicio\": \"INFORMACION PERSONAL PISEE\",");
		result.append("\"consumidor\": \"PISEE\",");
		result.append("\"tramite\": \"ESCRITORIO CIUDADANO\",");
		result.append("\"fecha_hora\": \"2014-08-19T12:27:49.5205537-04:00\"");
		result.append("},");
		result.append("\"cuerpo\": {");
		result.append("\"run\": \"10546911\",");
		result.append("\"dv\": \"K\",");
		result.append("\"nombres\": \"VICTOR DAVID\",");
		result.append("\"apellido_paterno\": \"RUMINOT\",");
		result.append("\"apellido_materno\": \"BASCUR\",");
		result.append("\"fecha_nacimiento\": \"1967-05-30\",");
		result.append("\"fecha_defuncion\": \"0000-00-00\",");
		result.append("\"estado_civil\": \"C\",");
		result.append("\"nacionalidad\": \"C\",");
		result.append("\"sexo\": \"M\",");
		result.append("\"circunscripcion_nacimiento\": \"754\",");
		result.append("\"profesion\": \"No Informado\"");
		result.append("}");
		result.append("}");
		
		/*
		{
			"encabezado": {
				"id_sobre": "150501008220140613000000100",
				"emisor_sobre": "SRCeI",
				"estado_sobre": "00",
				"glosa_sobre": "TRANSACCION EXITOSA",
				"proveedor": "SRCeI",
				"servicio": "INFORMACION PERSONAL PISEE",
				"consumidor": "PISEE",
				"tramite": "ESCRITORIO CIUDADANO",
				"fecha_hora": "2014-06-13T09:57:49.5205537-04:00"
			},
			"cuerpo": {
				"run": "10546911",
				"dv": "K",
				"nombres": "VICTOR DAVID",
				"apellido_paterno": "RUMINOT",
				"apellido_materno": "BASCUR",
				"fecha_nacimiento": "1967-05-30",
				"fecha_defuncion": "0000-00-00",
				"estado_civil": "C",
				"nacionalidad": "C",
				"sexo": "M",
				"circunscripcion_nacimiento": "754",
				"profesion": ""
			}
		}
		*/
		
		return result.toString();
    }	
}
