package cl.gob.minsegpres.pisee.rest.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.core.service.ConfiguracionServicioService;
import cl.gob.minsegpres.pisee.rest.connector.RestConnector;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.oauth.RespuestaAutorizacion;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

public class RestBusiness {

	private final static Log LOGGER = LogFactory.getLog(RestBusiness.class);

	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter) {
		ConfiguracionServicio configuracionServicio = null;
		ConfiguracionServicioService configuracionServicioBusiness = new ConfiguracionServicioService();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		RestConnector restConnector = new RestConnector();	
		String tokenPisee;
		tokenPisee = (String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN);
		if(ContingenciaBusiness.isContingencia()){
			LOGGER.info("Configuracion de servicio obtenida en CONTINGENCIA");
			configuracionServicio = ContingenciaBusiness.getInstance().getConfiguracionServicio(tokenPisee);
		} else { 
			configuracionServicio = configuracionServicioBusiness.findConfiguracionServicio(tokenPisee);	
		}
		if (null != configuracionServicio) {
			if (ConfiguracionServicio.BLOQUEADO.equals(configuracionServicio.getEstado())) {
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_SERVICE);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_BLOCKED_SERVICE);
				LOGGER.info("Servicio de token = " + tokenPisee + " bloqueado para consumo");
				return respuesta;
			} else {
				respuesta = restConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);
				return respuesta;
			}
		} else {
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_TOKEN);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN);
			LOGGER.info("Token = " + tokenPisee + " invalido para consumo");
		}
		return respuesta;
	}
	
	public PiseeRespuesta callServiceOAuth(String proveedorServiceName, InputParameter inputParameter) {
		ConfiguracionServicio configuracionServicio = null;
		ConfiguracionServicioService configuracionServicioBusiness = new ConfiguracionServicioService();
		OAuthBusiness oAuthBusiness =  new OAuthBusiness();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		RestConnector restConnector = new RestConnector();	
		RespuestaAutorizacion respuestaAutorizacion; 
		String tokenPisee;
		String accessToken = (String)inputParameter.getBodyParameter(ParametersName.OAUTH_ACCESS_TOKEN);
		respuestaAutorizacion = oAuthBusiness.processAccessToken(accessToken);
		if (null != respuestaAutorizacion){
			if (respuestaAutorizacion.isValidAccessToken()){
				if (oAuthBusiness.isValidRespuestaAutorizacion(respuestaAutorizacion, inputParameter, configuracionServicio)){
					inputParameter = oAuthBusiness.processParameters(respuestaAutorizacion, inputParameter);
					if (null != inputParameter){
						tokenPisee = (String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN);
						if(ContingenciaBusiness.isContingencia()){
							LOGGER.info("Configuracion de servicio obtenida en CONTINGENCIA para el consumo de OAUTH con token = " + accessToken);
							configuracionServicio = ContingenciaBusiness.getInstance().getConfiguracionServicio(tokenPisee);
						} else { 
							configuracionServicio = configuracionServicioBusiness.findConfiguracionServicio(tokenPisee);	
						}							
						if (null != configuracionServicio ){
							respuesta = restConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);
							return respuesta;						
						}else{
							respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
							respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_SERVICE);
							respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INTERNAL_ERROR);
							LOGGER.error("Error en PISEE, La configuracion del servicio es nula, para el tokenPisee = " + tokenPisee);
							return respuesta;						
						}						
					}else{
						respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
						respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_SERVICE);
						respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INTERNAL_ERROR);
						LOGGER.error("Ocurrio un error al extraer los datos de OWNER y CLIENT en la respuesta del servicio OAUTH");
						return respuesta;						
					}
				} else{
					respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
					respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_DATA_OAUTH);
					respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_VALIDATE_CALL_OAUTH);
					LOGGER.error("Error en OAUTH, la informacion entregada por el access_token = " + accessToken + " no es valida para el consumo del servicio REST");
					return respuesta;						
				}
			} else {
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_CALL_OAUTH);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_VALIDATE_CALL_OAUTH);
				LOGGER.error("Error en OAUTH, El access-token = " + accessToken + " no es valido, respuesta del servicio OAUTH = " + respuestaAutorizacion.getErrorMessage());
				return respuesta;							
			}
		}else{
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_DATA_OAUTH);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_VALIDATE_CALL_OAUTH);
			LOGGER.error("Ocurrio un error en la llamada al servicio OAUTH");
			return respuesta;					
		}
	}	
	
}
