package cl.gob.minsegpres.pisee.rest.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.rest.business.SobreBusiness;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

public class MiscUtil {

	private final static Log LOGGER = LogFactory.getLog(MiscUtil.class);
	
    public static Response ConvertPiseeResponse(PiseeRespuesta respuesta){
    	
    	JSONUtil jsonUtil = new JSONUtil();
    	String estadoSobre = respuesta.getEncabezado().getEstadoSobre();
    	String glosaSobre = respuesta.getEncabezado().getGlosaSobre();
    	String value = jsonUtil.toJSONResponse(respuesta);
    	
    	if (AppConstants._CODE_OK.equals(estadoSobre)){
    		return Response.status(Response.Status.OK).entity(value).build();	
    	}
    	if (AppConstants._CODE_OK_PROVEEDOR_NO_DATA_FOUND.equals(estadoSobre) || 
    				AppConstants._CODE_OK_PROVEEDOR_NO_DATA_AVAILABLE.equals(estadoSobre)){    		
    		return Response.status(Response.Status.BAD_REQUEST).entity(glosaSobre).build();	
    	}
    	if (AppConstants._CODE_ERROR_CONSUMIDOR_AUTENTICACION.equals(estadoSobre) || AppConstants._CODE_ERROR_CONSUMIDOR_AUTORIZACION.equals(estadoSobre)){
    		return Response.status(Response.Status.UNAUTHORIZED).entity(glosaSobre).build();	
    	}
    	if (AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA.equals(estadoSobre) ||
    			AppConstants._CODE_ERROR_CONSUMIDOR_XML_ENTRADA.equals(estadoSobre) ||	
    				AppConstants._CODE_ERROR_CONSUMIDOR_TRAMITE_NO_EXISTE.equals(estadoSobre) ){
    		return Response.status(Response.Status.FORBIDDEN).entity(glosaSobre).build();
    	}
    	if (AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_01.equals(estadoSobre) ||
    			AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_02.equals(estadoSobre) ||
    				AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_03.equals(estadoSobre) ||
    					AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_04.equals(estadoSobre)){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(glosaSobre).build();	
    	}
    	if (AppConstants._CODE_ERROR_PROVEEDOR_TIMEOUT.equals(estadoSobre)){
    		return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(glosaSobre).build();	
    	}
    	
    	
    	return Response.status(Response.Status.SEE_OTHER).entity(glosaSobre).build();
    }
    
    public static Response ConvertPiseeResponseNoSobre(PiseeRespuesta respuesta) {
    	JSONUtil jsonUtil = new JSONUtil();
    	String value = jsonUtil.toJSONResponse(respuesta);
   		return Response.status(Response.Status.OK).entity(value).build();
    }
	
	public static String getClientIpAddr(HttpServletRequest request) {
//		public String getConsultaDatosContribuyente(@QueryParam("rut") String rut, @QueryParam("dv") String dv, @QueryParam("pisee_token") String piseeToken, @Context HttpServletRequest req) {
//	    System.out.println( MiscUtil.getClientIpAddr(req) );
		
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}	
	
}
