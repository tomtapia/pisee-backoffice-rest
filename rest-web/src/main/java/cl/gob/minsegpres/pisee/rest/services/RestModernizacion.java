package cl.gob.minsegpres.pisee.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
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

@Path("/modernizacion")
@Component
@Scope("singleton")
public class RestModernizacion {

	private final static Log LOGGER = LogFactory.getLog(RestModernizacion.class);

	@GET
	@Path("v1/regiones")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String consultaRegiones(@QueryParam("limit") String paramLimit,
									@QueryParam("offset") String paramOffset,
									@QueryParam("orderBy") String paramOrderBy,
									@QueryParam("orderDir") String paramOrderDir,
									@QueryParam("geolocation") String paramGeolocation,
									@QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		String value = processConsultaRegiones(null, null, null, paramLimit, paramOffset, paramOrderBy, paramOrderDir, paramGeolocation, piseeToken); 
		long endTime = System.currentTimeMillis();		
		LOGGER.info("consultaRegiones - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return value;
    }
	
	@GET
	@Path("v1/regiones/{codigoRegion}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String consultaRegion(@PathParam("codigoRegion") String paramCodigoRegion,												
									@QueryParam("limit") String paramLimit,
									@QueryParam("offset") String paramOffset,
									@QueryParam("orderBy") String paramOrderBy,
									@QueryParam("orderDir") String paramOrderDir,
									@QueryParam("geolocation") String paramGeolocation,
									@QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		String value = processConsultaRegiones(paramCodigoRegion, null, null, paramLimit, paramOffset, paramOrderBy, paramOrderDir, paramGeolocation, piseeToken); 
		long endTime = System.currentTimeMillis();		
		LOGGER.info("consultaRegion - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return value;
    }	
	
	@GET
	@Path("v1/regiones/{codigoRegion}/{provincias}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String consultaProvincias(@PathParam("codigoRegion") String paramCodigoRegion,		
									@PathParam("provincias") String provincias,
									@QueryParam("limit") String paramLimit,
									@QueryParam("offset") String paramOffset,
									@QueryParam("orderBy") String paramOrderBy,
									@QueryParam("orderDir") String paramOrderDir,
									@QueryParam("geolocation") String paramGeolocation,
									@QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		String value = processConsultaRegiones(paramCodigoRegion, provincias, null, paramLimit, paramOffset, paramOrderBy, paramOrderDir, paramGeolocation, piseeToken); 
		long endTime = System.currentTimeMillis();		
		LOGGER.info("consultaProvincias - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return value;
    }	
	
	@GET
	@Path("v1/regiones/{codigoRegion}/{provincias}/{codigoProvincia}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String consultaProvincia(@PathParam("codigoRegion") String paramCodigoRegion,
									@PathParam("provincias") String provincias,
									@PathParam("codigoProvincia") String paramCodigoProvincia,			
									@QueryParam("limit") String paramLimit,
									@QueryParam("offset") String paramOffset,
									@QueryParam("orderBy") String paramOrderBy,
									@QueryParam("orderDir") String paramOrderDir,
									@QueryParam("geolocation") String paramGeolocation,
									@QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();
		String value = processConsultaRegiones(paramCodigoRegion, provincias, paramCodigoProvincia, paramLimit, paramOffset, paramOrderBy, paramOrderDir, paramGeolocation, piseeToken); 
		long endTime = System.currentTimeMillis();		
		LOGGER.info("consultaProvincia - TIME == " + (endTime - startTime) + " MILISECONDS");
	    return value;
    }	
	
	private String processConsultaRegiones(String paramCodigoRegion,
											String provincias,
											String paramCodigoProvincia,
											String paramLimit,
											String paramOffset,
											String paramOrderBy,
											String paramOrderDir,
											String paramGeolocation,
											String piseeToken){
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		if (!StringUtils.isEmpty(paramCodigoRegion)){
			inputParameter.addBodyParameter("codigoRegion", paramCodigoRegion);	
		}
		if (!StringUtils.isEmpty(provincias)){
			inputParameter.addBodyParameter("provincias", provincias);	
		}		
		if (!StringUtils.isEmpty(paramCodigoProvincia)){
			inputParameter.addBodyParameter("codigoProvincia", paramCodigoProvincia);	
		}		
		if (!StringUtils.isEmpty(paramLimit)){
			inputParameter.addBodyParameter("limit", paramLimit);	
		}		
		if (!StringUtils.isEmpty(paramOffset)){
			inputParameter.addBodyParameter("offset", paramOffset);	
		}		
		if (!StringUtils.isEmpty(paramOrderBy)){
			inputParameter.addBodyParameter("orderBy", paramOrderBy);	
		}		
		if (!StringUtils.isEmpty(paramOrderDir)){
			inputParameter.addBodyParameter("orderDir", paramOrderDir);	
		}		
		if (!StringUtils.isEmpty(paramGeolocation)){
			inputParameter.addBodyParameter("geolocation", paramGeolocation);	
		}		
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);
		respuesta = restBusiness.callService(ConfigProveedoresServicios.REST_MODERNIZACION__REGIONES, inputParameter);
		return jsonUtil.toJSON(respuesta);		
	}
	
}
