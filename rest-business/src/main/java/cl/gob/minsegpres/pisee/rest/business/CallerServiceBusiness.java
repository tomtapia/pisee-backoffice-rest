package cl.gob.minsegpres.pisee.rest.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.core.service.ConfiguracionServicioService;
import cl.gob.minsegpres.pisee.rest.connector.RestToRestConnector;
import cl.gob.minsegpres.pisee.rest.connector.RestToSoapConnector;
import cl.gob.minsegpres.pisee.rest.entities.DataPiseeToQueue;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.oauth.RespuestaAutorizacion;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;
import cl.gob.minsegpres.pisee.rest.util.ValidationUtil;

public class CallerServiceBusiness {

	private final static Log LOGGER = LogFactory.getLog(CallerServiceBusiness.class);

	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter) {
		return callService(proveedorServiceName, inputParameter, false);
	}
	
	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter, boolean validateRutParameters) {
		ConfiguracionServicio configuracionServicio = null;
		ConfiguracionServicioService configuracionServicioBusiness = new ConfiguracionServicioService();
		PiseeRespuesta respuesta;
		RestToSoapConnector restToSoapConnector = new RestToSoapConnector();
		RestToRestConnector restToRestConnector = new RestToRestConnector();
		String tokenPisee;
		tokenPisee = (String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN);
		
		if (validateRutParameters){
			ValidationUtil validationUtil = new ValidationUtil();
			respuesta = validationUtil.validateRutParameters(inputParameter);
			if (null != respuesta){
				return respuesta;				
			}
		}
		
		//-----
		respuesta = new PiseeRespuesta();
		if (null == tokenPisee){
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_NULL_PARAMETER + ParametersName.PISEE_TOKEN);	
			LOGGER.error("Consulta al servicio " + proveedorServiceName + " con token en null");
		}else{
			if(ContingenciaBusiness.isContingencia()){
				LOGGER.info("Configuracion de servicio obtenida en CONTINGENCIA");
				configuracionServicio = ContingenciaBusiness.getInstance().getConfiguracionServicio(tokenPisee);
			} else { 
				configuracionServicio = configuracionServicioBusiness.findConfiguracionServicio(tokenPisee);	
			}		
			if (null != configuracionServicio) {
				if (ConfiguracionServicio.BLOQUEADO.equals(configuracionServicio.getEstado())) {
					respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_CONSUMIDOR);
					respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_AUTORIZACION);
					respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_BLOCKED_SERVICE);
					LOGGER.info("Servicio de token = " + tokenPisee + " bloqueado para consumo");
				} else {
					if (proveedorServiceName.startsWith(AppConstants.PREFIX_SERVICE_SOAP)){
						respuesta = restToSoapConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);	
					}else if (proveedorServiceName.startsWith(AppConstants.PREFIX_SERVICE_REST)){
						respuesta = restToRestConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);
					}
				}
				
				//---------------------------------------------------------------------------------------------------------------------------
				if (proveedorServiceName.startsWith(AppConstants.PREFIX_SERVICE_REST)){
					//Dejar este codigo asincrono -> http://comunidad.fware.pro/dev/java/insercion-asincrona-de-datos-desde-java/
					//El token se debe validar sin registrarlo dado el modelo de la BD
					
//					Long idSobreConsumidor = null, idSobreProveedor = null, idLogEsb = null;
//					BigDecimal idLogTpoProveedor = null;
//					TrazabilidadBusiness trazabilidadBusiness = new TrazabilidadBusiness();
//					idSobreConsumidor = trazabilidadBusiness.insertSobreConsumidor(respuesta);
//					if (null != configuracionServicio || AppConstants._EMISOR_CONSUMIDOR.equals(respuesta.getEncabezado().getEmisorSobre())) {
//						idSobreProveedor = trazabilidadBusiness.insertSobreProveedor(respuesta);	
//					}
//					idLogEsb = trazabilidadBusiness.insertLogEsb(respuesta, idSobreConsumidor, idSobreProveedor, configuracionServicio);
//					idLogTpoProveedor = trazabilidadBusiness.insertLogTiempoProveedor(respuesta, idLogEsb, configuracionServicio);
//					trazabilidadBusiness.updateLogEsb(idLogEsb, idLogTpoProveedor);
					
					TrazabilidadQueue.getInstance().accept(new DataPiseeToQueue(configuracionServicio, respuesta));
					
				}
				//---------------------------------------------------------------------------------------------------------------------------	
				
			} else {
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_CONSUMIDOR);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN);
				LOGGER.info("Token = " + tokenPisee + " invalido para consumo");
			}			
		}

		return respuesta;
	}
	
	public PiseeRespuesta callServiceOAuth(String proveedorServiceName, InputParameter inputParameter) {
		ConfiguracionServicio configuracionServicio = null;
		ConfiguracionServicioService configuracionServicioBusiness = new ConfiguracionServicioService();
		OAuthBusiness oAuthBusiness =  new OAuthBusiness();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		RestToSoapConnector restConnector = new RestToSoapConnector();	
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
						}else{
							respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_CONSUMIDOR);
							respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
							respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN);
							LOGGER.error("Error en PISEE, La configuracion del servicio es nula, para el tokenPisee = " + tokenPisee);
						}						
					}else{
						respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_CONSUMIDOR);
						respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
						respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_PARAMETERS);
						LOGGER.error("Ocurrio un error al extraer los datos de OWNER y CLIENT en la respuesta del servicio OAUTH");
					}
				} else{
					respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_CONSUMIDOR);
					respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
					respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN_OAUTH);
					LOGGER.error("Error en OAUTH, la informacion entregada por el access_token = " + accessToken + " no es valida para el consumo del servicio REST");
				}
			} else {
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_CONSUMIDOR);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN_OAUTH);
				LOGGER.error("Error en OAUTH, El access-token = " + accessToken + " no es valido, respuesta del servicio OAUTH = " + respuestaAutorizacion.getErrorMessage());
			}
		}else{
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PROVEEDOR);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_PROVEEDOR_INTERNO);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_VALIDATE_CALL_OAUTH);
			LOGGER.error("Ocurrio un error en la llamada al servicio OAUTH");
		}
		
		//TODO: Cuando se integre ouath revisar su trazabilidad
		
		return respuesta;
	}	
	
}
