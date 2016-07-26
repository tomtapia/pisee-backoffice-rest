package cl.gob.minsegpres.pisee.rest.connector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.business.SobreBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeEncabezado;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeTemporalData;
import cl.gob.minsegpres.pisee.rest.entities.rest.ConfigServiceREST;
import cl.gob.minsegpres.pisee.rest.entities.rest.ParameterServiceREST;
import cl.gob.minsegpres.pisee.rest.entities.rest.PiseeRESTException;
import cl.gob.minsegpres.pisee.rest.readers.ReaderTemplateConfigREST;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.ClientHelper;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;
import cl.gob.minsegpres.pisee.rest.util.PiseeRESTExclusionStrategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RestToRestConnector {

	private final static Log LOGGER = LogFactory.getLog(RestToRestConnector.class);

	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		PiseeRespuesta respuesta;
		ConfigServiceREST configService = ReaderTemplateConfigREST.getInstance().findConfigService(proveedorServiceName);
		SobreBusiness sobreBusiness = new SobreBusiness();
		inputParameter = sobreBusiness.fillSobre(inputParameter, configuracionServicio);
		
		//TODO: Faltar validar que el TOKEN sea para el consumo del servicio en particular, actualmente sirve cualquier token para consumo de REST a REST
		respuesta = callService(configService, inputParameter, configuracionServicio);
		
		respuesta.getEncabezado().setIdSobre(sobreBusiness.generateSobreID(configuracionServicio));
		respuesta.getEncabezado().setFechaHora(sobreBusiness.generateSobreFechaHora());
		respuesta.getEncabezado().setNombreProveedor(configuracionServicio.getServicioTramite().getServicio().getOrganismo().getSigla());
		respuesta.getEncabezado().setNombreServicio(configuracionServicio.getServicioTramite().getServicio().getNombre());
		respuesta.getEncabezado().setNombreConsumidor(configuracionServicio.getServicioTramite().getTramite().getOrganismo().getSigla());
		respuesta.getEncabezado().setNombreTramite(configuracionServicio.getServicioTramite().getTramite().getNombre());
		return respuesta;
	}
	
	@SuppressWarnings("unchecked")
	private PiseeRespuesta callService(ConfigServiceREST configService, InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		long startTime = System.currentTimeMillis();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		PiseeEncabezado encabezado = new PiseeEncabezado();
		ClientResponse response = null;
		try {
			Client client = ClientHelper.createClient(configuracionServicio);
			String queryString, endpoint, extendEndpoint = null, result = null;
			WebResource webResource;
			MultivaluedMap<String, String> parameters;
			if (AppConstants._HTTP_METHOD_GET.equals(configService.getMethod())){
				if (!hasPathParameter(configService, inputParameter)){
					queryString = createQueryString(configService, inputParameter);
					endpoint = configService.getBaseEndpoint() + queryString;
					webResource = client.resource(endpoint);
					LOGGER.info(transactionLogInput(inputParameter, configService.getMethod(), endpoint));
					response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
				}else{
					queryString = createQueryString(configService, inputParameter);
					endpoint = configService.getBaseEndpoint() + queryString;
					webResource = client.resource(endpoint);
					extendEndpoint = createPathParameter(configService, inputParameter);
					LOGGER.info(transactionLogInput(inputParameter, configService.getMethod(), configService.getBaseEndpoint() + extendEndpoint + queryString));
					response = webResource.path(extendEndpoint).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
				}				
			}else if (AppConstants._HTTP_METHOD_POST.equals(configService.getMethod())){
				webResource = client.resource(configService.getBaseEndpoint());
				parameters = createMultivaluedMap(configService, inputParameter);
				LOGGER.info(transactionLogInput(inputParameter, configService.getMethod(), configService.getBaseEndpoint() + configService.getExtendEndpoint()));
				response = webResource.path(configService.getExtendEndpoint()).queryParams(parameters).post(ClientResponse.class);
			}else if (AppConstants._HTTP_METHOD_PUT.equals(configService.getMethod())){
				webResource = client.resource(configService.getBaseEndpoint());
				parameters = createMultivaluedMap(configService, inputParameter);
				LOGGER.info(transactionLogInput(inputParameter, configService.getMethod(), configService.getBaseEndpoint() + configService.getExtendEndpoint()));
				response = webResource.path(configService.getExtendEndpoint()).queryParams(parameters).put(ClientResponse.class);
			}else {
				LOGGER.info("Metodo HTTP: " + configService.getMethod() + " no es soportado");
				encabezado.setEmisorSobre(AppConstants._EMISOR_PISEE);
				encabezado.setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_TRAMITE_NO_EXISTE);
				encabezado.setGlosaSobre(AppConstants._MSG_UNSUPPORT_HTTP_METHOD + configService.getMethod() );
				respuesta.setEncabezado(encabezado);				
			}
			if (null != response){
				if (response.getStatus() == 200) {
					result = response.getEntity(String.class);
					Map<String,Object> mapResult = convertToPiseeResponse(result); 
					if (null != mapResult){
						//Es una respuesta en formato PISEE
						Map<String,String> mapEncabezado = (Map<String,String>)mapResult.get("encabezado");
						encabezado.setEmisorSobre((String)mapEncabezado.get("emisorSobre"));					
						encabezado.setEstadoSobre((String)mapEncabezado.get("estadoSobre"));					
						encabezado.setGlosaSobre((String)mapEncabezado.get("glosaSobre"));						
						respuesta.setEncabezado(encabezado);
						String jsonResponse = null;
						ArrayList<String> mapCuerpo, mapMetadata;
						mapCuerpo = (ArrayList<String>)mapResult.get("cuerpo");
						mapMetadata = (ArrayList<String>)mapResult.get("metadata");
						if (null != mapCuerpo){
						    Gson gson = new GsonBuilder().setExclusionStrategies(new PiseeRESTExclusionStrategy()).create();
						    jsonResponse = gson.toJson(mapCuerpo);							
						}else if (null != mapMetadata){
						    Gson gson = new GsonBuilder().setExclusionStrategies(new PiseeRESTExclusionStrategy()).create();
						    jsonResponse = gson.toJson(mapMetadata);								
						}
						respuesta.setResponseRest(jsonResponse);	
					}else{
						//NO Es respuesta formato PISEE
						//Se retorna todo como OK a la espera de un motor de validaciones segun el servicio
						encabezado.setEmisorSobre(AppConstants._EMISOR_PISEE);					
						encabezado.setEstadoSobre(AppConstants._CODE_OK);					
						encabezado.setGlosaSobre(AppConstants._MSG_CODE_OK);
						respuesta.setEncabezado(encabezado);
						respuesta.setResponseRest(result);						
					}
				}else{
					encabezado.setEmisorSobre(AppConstants._EMISOR_PISEE);
					encabezado.setEstadoSobre(AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_01);
					encabezado.setGlosaSobre(AppConstants._MSG_ERROR_HTTP + response.getStatus());
					respuesta.setEncabezado(encabezado);				
				}				
			}
		} catch (PiseeRESTException e) {
			encabezado.setEmisorSobre(AppConstants._EMISOR_PISEE);
			encabezado.setEstadoSobre(AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_01);
			encabezado.setGlosaSobre(AppConstants._MSG_ERROR + e.getEstado() + " - " + e.getGlosa());
			respuesta.setEncabezado(encabezado);
			LOGGER.info("PiseeRESTException - result == " + encabezado.getGlosaSobre());
		} catch (ClientHandlerException che) {
			if (che.getMessage().indexOf(AppConstants._READ_TIMED_OUT) != -1){
				encabezado.setEmisorSobre(AppConstants._EMISOR_PROVEEDOR);
				encabezado.setEstadoSobre(AppConstants._CODE_ERROR_PROVEEDOR_TIMEOUT);
				encabezado.setGlosaSobre(AppConstants._MSG_TIMEOUT);
				respuesta.setEncabezado(encabezado);
				LOGGER.info("PiseeRESTException - El servicio no respondio en el tiempo determinado TIMEOUT");
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("TOTAL CONNECTOR TIME == " + (endTime - startTime) + " MILISECONDS");
		
		respuesta.setTemporalData(new PiseeTemporalData());
		respuesta.getTemporalData().setFechaConsultaRecibida((Calendar)inputParameter.getHeaderParameter(ParametersName.FECHA_CONSULTA_RECIBIDA));
		respuesta.getTemporalData().setDuracionLlamadaServicio((endTime - startTime));
		return respuesta;
	}	
	
	private String transactionLogInput(InputParameter inputParameter, String httpMethod, String endpoint) {
		StringBuilder sb = new StringBuilder();
		sb.append("Invocando a URL: ");
		sb.append(endpoint);
		sb.append(" - ");
		sb.append("Method: ");
		sb.append(httpMethod);
		sb.append("\n");
		sb.append("ID Sobre - Fecha Hora : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.ID_SOBRE));
		sb.append(" - ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.FECHA_HORA));
		sb.append("\n");
		sb.append("Proveedor - Servicio: ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_PROVEEDOR));
		sb.append(" - ");		
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_SERVICIO));
		sb.append("\n");
		sb.append("Consumidor - Tramite: ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_CONSUMIDOR));
		sb.append(" - ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_TRAMITE));
		return sb.toString();
	}
	
	private boolean hasPathParameter(ConfigServiceREST configService, InputParameter inputParameter){
		for (Entry<String, Object> e: inputParameter.getBodyParameters().entrySet()) {
			for (ParameterServiceREST param: configService.getPathParameters()){
				if (e.getKey().equals(param.getName())){
					return true;
				}
			}
	    }		
		return false;
	}
	
	private String createQueryString(ConfigServiceREST configService, InputParameter inputParameter) throws PiseeRESTException{
		String queryString = AppConstants.BLANK;
		if (null != configService.getQueryParameters() && configService.getQueryParameters().size() > 0){
			for (ParameterServiceREST  param: configService.getQueryParameters()){
				for (Entry<String, Object> e: inputParameter.getBodyParameters().entrySet()) {
					if (e.getKey().equals(param.getName())){
						if (!param.isOptional() && null == e.getValue()){
							throw new PiseeRESTException(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA, AppConstants._MSG_INVALID_NULL_PARAMETER  + param.getName());
						}
						if (AppConstants.BLANK.equals(queryString)){
							queryString = AppConstants.QUESTION + e.getKey() + AppConstants.EQUAL + e.getValue(); 
						}else{
							queryString = queryString + AppConstants.AMPERSAND + e.getKey() + AppConstants.EQUAL + (String)e.getValue();
						}
					}
				}
		    }			
		}
		return queryString;
	}
	
	private String createPathParameter(ConfigServiceREST configService, InputParameter inputParameter){
		String extendEndpoint = configService.getExtendEndpoint();
		String tmpParameter = null;	
		boolean foundPathParameter = false;
		for (ParameterServiceREST p: configService.getPathParameters()){
			foundPathParameter = false;
			if (null != inputParameter.getBodyParameters() && inputParameter.getBodyParameters().size() > 0 ){
				for (Entry<String, Object> e: inputParameter.getBodyParameters().entrySet()) {
					if (e.getKey().equals(p.getName())){
						tmpParameter = (String)inputParameter.getBodyParameter(p.getName());
						foundPathParameter = true;
					}
				}			
			}
			if (foundPathParameter){
				extendEndpoint = extendEndpoint.replace(AppConstants.KEY_LEFT + p.getName() + AppConstants.KEY_RIGHT, tmpParameter);
			}else{
				extendEndpoint = extendEndpoint.replace(AppConstants.SLASH + AppConstants.KEY_LEFT + p.getName() + AppConstants.KEY_RIGHT, AppConstants.BLANK);
			}				
		}		
		return extendEndpoint;
	}
	
	
	private MultivaluedMap<String, String> createMultivaluedMap(ConfigServiceREST configService, InputParameter inputParameter) throws PiseeRESTException{
		MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
		for (ParameterServiceREST  param: configService.getQueryParameters()){
			for (Entry<String, Object> e: inputParameter.getBodyParameters().entrySet()) {
				if (e.getKey().equals(param.getName())){
					if (!param.isOptional() && null == e.getValue()){						
						throw new PiseeRESTException(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA, AppConstants._MSG_INVALID_NULL_PARAMETER  + param.getName());
					}					
					parameters.add(e.getKey(), (String) e.getValue());
				}
			}
	    }		
		return parameters;
	}
	
	private Map<String,Object> convertToPiseeResponse(String response){
		Map<String,Object> piseeResponse;
		try{
			piseeResponse = new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {}.getType());

			if (null != piseeResponse.get("encabezado") && 
					(null != piseeResponse.get("metadata") || null != piseeResponse.get("cuerpo")) ){
				return piseeResponse;
			}
		}catch(Exception e){
			LOGGER.debug("Error en la transformacion del servicio por tanto no es formato PISEE-REST ");
		}
		return null;
	}	
	
}
