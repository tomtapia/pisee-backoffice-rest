package cl.gob.minsegpres.pisee.rest.connector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.SAXException;

import cl.gob.minsegpres.pisee.rest.business.SobreBusiness;
import cl.gob.minsegpres.pisee.rest.entities.CampoOutputParameter;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.OutputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeEncabezado;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.readers.ReaderServicesInput;
import cl.gob.minsegpres.pisee.rest.readers.ReaderServicesOutput;
import cl.gob.minsegpres.pisee.rest.services.ConfiguracionServicioService;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.PiseeStringUtils;
import cl.gob.minsegpres.pisee.rest.util.el.ExpressionLanguageProcessor;

public class RestConnector {

	private final static Log LOGGER = LogFactory.getLog(RestConnector.class);

	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter, String tokenPisee) {
		PiseeRespuesta respuesta = new PiseeRespuesta();
		SobreBusiness sobreBusiness = new SobreBusiness();
		ConfiguracionServicioService configuracionServicioBusiness = new ConfiguracionServicioService();

		try {
			
			Call call;
			ExpressionLanguageProcessor processor;
			InputStream is;
			ConfiguracionServicio configuracionServicio;
			String fileInputStr, fileInputProcessed;
			SOAPEnvelope soapEnvelopeInput, soapEnvelopeOutput;

			processor = new ExpressionLanguageProcessor();
			long t1 = System.currentTimeMillis();

			configuracionServicio = configuracionServicioBusiness.findConfiguracionServicio(tokenPisee);
			if (null != configuracionServicio) {
				if (ConfiguracionServicio.BLOQUEADO.equals(configuracionServicio.getEstado())) {
					respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
					respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_SERVICE);
					respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_BLOCKED_SERVICE);
				} else {
					call = createCall(configuracionServicio);
					inputParameter = sobreBusiness.fillSobre(inputParameter, configuracionServicio);
					fileInputStr = ReaderServicesInput.getInstance().readFile(proveedorServiceName);

					if (null != fileInputStr) {
						long tp = System.nanoTime();
						fileInputProcessed = processor.processInput(fileInputStr, inputParameter);
						LOGGER.info("[nanoTime.] - " + proveedorServiceName + " - time process xml input == " + (System.nanoTime() - tp));

						LOGGER.info(transactionLog(inputParameter, configuracionServicio));
						is = new ByteArrayInputStream(fileInputProcessed.getBytes("UTF-8"));
						
						//-----
						SrceiConnector s = new SrceiConnector();
						Document document;
						
						//FIXME: Dejar en la BD si el servicio necesita ser firmado para su consumo
						
						if (configuracionServicio.getHttpUserName().equalsIgnoreCase("GENCHI")){
							String result = s.firmarEntrada(is);
							is = new ByteArrayInputStream(result.getBytes("UTF-8"));	
						}
						//------
						
						
						
						soapEnvelopeInput = new SOAPEnvelope(is);
						LOGGER.info(proveedorServiceName + " - INPUT SERVICE : " + PiseeStringUtils.prettyFormat(soapEnvelopeInput.getAsString()));
						
						soapEnvelopeOutput = call.invoke(soapEnvelopeInput);
						
						//-----
						if (configuracionServicio.getHttpUserName().equalsIgnoreCase("GENCHI")){					
							String result = s.descrifarRespuesta(soapEnvelopeOutput.getAsDocument());
							document = DocumentHelper.parseText(result);	
						}else{
							document = PiseeStringUtils.getDom4jDocument(soapEnvelopeOutput.getAsDocument());	
						}
						//------						
						
						LOGGER.info(proveedorServiceName + " - OUTPUT SERVICE : " + PiseeStringUtils.prettyFormat(document.asXML()));
						LOGGER.info(proveedorServiceName + " - TOTAL TIME STUB == " + (System.currentTimeMillis() - t1) + " [TimeMillis]");

						Element eRoot = document.getRootElement();

						respuesta.setEncabezado(fillEncabezado(eRoot));
						
						
						if (AppConstants._CODE_OK.equals(respuesta.getEncabezado().getEstadoSobre())) {
							respuesta.setMetadata(fillMetaData(proveedorServiceName, eRoot));
						}
						
						
					}
				}
			} else {
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_TOKEN);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_TOKEN);
			}
		} catch (AxisFault e) {
			LOGGER.error("AxisFault Error en la llamada al servicio : " + " - " + e);
			e.printStackTrace();
		} catch (MalformedURLException e) {
			LOGGER.error("MalformedURLException Error en la creacion de la llamada: " + " - " + e);
			e.printStackTrace();
		} catch (ServiceException e) {
			LOGGER.error("ServiceException Error en la creacion de la llamada: " + " - " + e);
			e.printStackTrace();
		} catch (SAXException e) {
			LOGGER.error("SAXException en el paseo del XML de entrada: " + " - " + e);
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception == " + e);
			e.printStackTrace();
		} finally {
			if (!AppConstants._CODE_OK.equals(respuesta.getEncabezado().getEstadoSobre()) && !AppConstants._CODE_NOK_TOKEN.equals(respuesta.getEncabezado().getEstadoSobre())) {
				respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
				respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_NOK_INTERNAL);
				respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INTERNAL_ERROR);
			}
		}
		
		

		
		return respuesta;
	}

	private Call createCall(ConfiguracionServicio serviceConfig) throws MalformedURLException, ServiceException {
		Call call = null;

		// FIXME: deshabilitar certificado en llamadas a WS
		// -------------------------------------------------------------------------------------------------------------
		System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
		// -------------------------------------------------------------------------------------------------------------

		Service service = new Service();
		call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(serviceConfig.getEndPoint()));
		call.setTimeout(serviceConfig.getTimeout().intValue());
		call.setOperationName(new QName(serviceConfig.getEndPoint(), serviceConfig.getOperation()));
		call.setUsername(serviceConfig.getHttpUserName());
		call.setPassword(serviceConfig.getHttpPassword());
		return call;
	}

	private PiseeEncabezado fillEncabezado(Element eRoot) throws Exception {
		PiseeEncabezado encabezado = new PiseeEncabezado();
		Element eIdSobre, eFechaHora, eFechaHoraReq, eNombreProveedor, eNombreServicio, eNombreConsumidor, eNombreTramite;
		Element eEmisorSobre, eEstadoSobre, eGlosaSobre;

		eIdSobre = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/idSobre", eRoot);
		if (null != eIdSobre) {
			encabezado.setIdSobre(eIdSobre.getTextTrim());
		}
		eFechaHora = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/fechaHora", eRoot);
		if (null != eFechaHora) {
			encabezado.setFechaHora(eFechaHora.getTextTrim());
		}
		eFechaHoraReq = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/fechaHoraReq", eRoot);
		if (null != eFechaHoraReq) {
			encabezado.setFechaHoraReq(eFechaHoraReq.getTextTrim());
		}

		eNombreProveedor = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/proveedor/nombre", eRoot);
		if (null != eNombreProveedor) {
			encabezado.setNombreProveedor(eNombreProveedor.getTextTrim());
		}
		eNombreServicio = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/proveedor/servicios/servicio", eRoot);
		if (null != eNombreServicio) {
			encabezado.setNombreServicio(eNombreServicio.getTextTrim());
		}
		eNombreConsumidor = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/consumidor/nombre", eRoot);
		if (null != eNombreConsumidor) {
			encabezado.setNombreConsumidor(eNombreConsumidor.getTextTrim());
		}
		eNombreTramite = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/consumidor/tramite", eRoot);
		if (null != eNombreTramite) {
			encabezado.setNombreConsumidor(eNombreTramite.getTextTrim());
		}

		eEmisorSobre = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/emisor", eRoot);
		if (null != eEmisorSobre) {
			encabezado.setEmisorSobre(eEmisorSobre.getTextTrim());
		}
		eEstadoSobre = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/metadataOperacional/estadoSobre", eRoot);
		if (null != eEstadoSobre) {
			encabezado.setEstadoSobre(eEstadoSobre.getTextTrim());
		}
		eGlosaSobre = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/metadataOperacional/glosaSobre", eRoot);
		if (null != eGlosaSobre) {
			encabezado.setGlosaSobre(eGlosaSobre.getTextTrim());
		}

		return encabezado;
	}

	private List<Map<String, String>> fillMetaData(String serviceName, Element eRoot) {
		OutputParameter outputParameter = ReaderServicesOutput.getInstance().findOutputParameter(serviceName + AppConstants.PREFIX_CONFIG_SERVICES_OUTPUT);
		Element eTmp;
		List<Map<String, String>> resultados = new ArrayList<Map<String, String>>();
		Map<String, String> resultado;
		if (null != outputParameter) {
			// Si es arreglo
			if (null != outputParameter.getRutaArreglo()) {
				for (Element element : findElements(eRoot, outputParameter.getRutaArreglo())) {
					resultado = new HashMap<String, String>();
					for (CampoOutputParameter campo : outputParameter.getCampos()) {
						eTmp = PiseeStringUtils.elementRecursive(campo.getRuta(), element);
						if (null != eTmp) {
							resultado.put(campo.getNombre(), eTmp.getTextTrim());
						}
					}
					resultados.add(resultado);
				}
			} else {
				resultado = new HashMap<String, String>();
				for (CampoOutputParameter campo : outputParameter.getCampos()) {
					eTmp = PiseeStringUtils.elementRecursive(campo.getRuta(), eRoot);
					if (null != eTmp) {
						resultado.put(campo.getNombre(), eTmp.getTextTrim());
					}
				}
				resultados.add(resultado);
			}
		}
		return resultados;
	}

	private String transactionLog(InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		StringBuilder sb = new StringBuilder();
		sb.append("REST - Invocando a URL: ");
		sb.append(configuracionServicio.getEndPoint());
		sb.append("\n");
		sb.append("idSobre : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.ID_SOBRE));
		sb.append("\n");
		sb.append("fechaHora : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.FECHA_HORA));
		sb.append("\n");
		sb.append("proveedorNombre : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_PROVEEDOR));
		sb.append("\n");
		sb.append("proveedorServicio : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_SERVICIO));
		sb.append("\n");
		sb.append("consumidorNombre : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_CONSUMIDOR));
		sb.append("\n");
		sb.append("consumidorTramite : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.NOMBRE_TRAMITE));
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private List<Element> findElements(Element eRoot, String rutaArreglo) {
		String[] arrRutaArreglo = rutaArreglo.split("/");
		Element tmpElement;
		tmpElement = eRoot;
		for (int i = 0; i < arrRutaArreglo.length - 1; i++) {
			tmpElement = tmpElement.element(arrRutaArreglo[i].toString());
		}
		return tmpElement.elements(arrRutaArreglo[arrRutaArreglo.length - 1].toString());
	}

}
