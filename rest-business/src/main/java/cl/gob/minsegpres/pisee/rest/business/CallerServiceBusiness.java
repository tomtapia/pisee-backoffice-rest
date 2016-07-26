package cl.gob.minsegpres.pisee.rest.business;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.core.service.ConfiguracionServicioService;
import cl.gob.minsegpres.pisee.rest.connector.RestToRestConnector;
import cl.gob.minsegpres.pisee.rest.connector.RestToSoapConnector;
import cl.gob.minsegpres.pisee.rest.connector.SiiRestToSoapConnector;
import cl.gob.minsegpres.pisee.rest.entities.DataPiseeToQueue;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
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
		ConfiguracionServicio configuracionServicio;
		PiseeRespuesta respuesta = new PiseeRespuesta();
		RestToSoapConnector restToSoapConnector = new RestToSoapConnector();
		RestToRestConnector restToRestConnector = new RestToRestConnector();
		SiiRestToSoapConnector siiRestToSoapConnector = new SiiRestToSoapConnector();  
		String tokenPisee = (String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN);
		
		//-------------------------------------------------------------------------------------------------------------------
		if (validateRutParameters){			
			if (!ValidationUtil.validarRut((String)inputParameter.getBodyParameter(ParametersName.RUT), 
					(String)inputParameter.getBodyParameter(ParametersName.DV))){ 
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_RUT_PARAMETER);
				return respuesta;
			}
			
			StringBuffer mensaje = new StringBuffer();
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.RUT)) 
					|| StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.DV)) 
						|| StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN))){
				if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.RUT))){
					mensaje.append(ParametersName.RUT);
					mensaje.append(AppConstants.SPACE);
				}
				if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.DV))){
					mensaje.append(ParametersName.DV);
					mensaje.append(AppConstants.SPACE);
				}		
				respuesta = new PiseeRespuesta();
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_NULL_PARAMETER + mensaje.toString());
				return respuesta;
			}			
		}
		
		if (null == tokenPisee){
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_AUTORIZACION);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_NULL_PARAMETER + ParametersName.PISEE_TOKEN);	
			LOGGER.error(proveedorServiceName + " - Token en null");
			return respuesta;
		}
		
		configuracionServicio = findConfiguracionServicio(tokenPisee);
		if (null == configuracionServicio) {
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN);
			LOGGER.error(proveedorServiceName + " - Token: " + tokenPisee + " invalido para consumir el servicio");
			return respuesta;
		}else if (ConfiguracionServicio.BLOQUEADO.equals(configuracionServicio.getEstado())) {
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_AUTORIZACION);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_BLOCKED_SERVICE);			
			LOGGER.error(proveedorServiceName + " - Token: " + tokenPisee + " bloqueado para consumir el servicio");
			return respuesta;
		}		
		//-------------------------------------------------------------------------------------------------------------------
		
		if (proveedorServiceName.startsWith(AppConstants.PREFIX_SERVICE_SOAP_SII)){
			respuesta = siiRestToSoapConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);
		}else if (proveedorServiceName.startsWith(AppConstants.PREFIX_SERVICE_SOAP)){
				respuesta = restToSoapConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);	
			}else if (proveedorServiceName.startsWith(AppConstants.PREFIX_SERVICE_REST)){
					respuesta = restToRestConnector.callService(proveedorServiceName, inputParameter, configuracionServicio);
					TrazabilidadQueue.getInstance().accept(new DataPiseeToQueue(configuracionServicio, respuesta));
			}
		return respuesta;
	}
	
	private ConfiguracionServicio findConfiguracionServicio(String tokenPisee){
		ConfiguracionServicioService configuracionServicioBusiness = new ConfiguracionServicioService();
		ConfiguracionServicio configuracionServicio;
		if(ContingenciaBusiness.isContingencia()){
			LOGGER.info("Configuracion de servicio obtenida en CONTINGENCIA");
			configuracionServicio = ContingenciaBusiness.getInstance().getConfiguracionServicio(tokenPisee);
		} else { 
			configuracionServicio = configuracionServicioBusiness.findConfiguracionServicio(tokenPisee);	
		}
		return configuracionServicio;
	}
	
}
