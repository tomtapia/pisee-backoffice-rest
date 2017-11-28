package cl.gob.minsegpres.pisee.rest.connector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.business.SobreBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeEncabezado;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeTemporalData;
import cl.gob.minsegpres.pisee.rest.entities.soap.CampoOutputParameterSOAP;
import cl.gob.minsegpres.pisee.rest.entities.soap.KeyStoreParameterSOAP;
import cl.gob.minsegpres.pisee.rest.entities.soap.OutputParameterSOAP;
import cl.gob.minsegpres.pisee.rest.readers.ReaderTemplateInputSOAP;
import cl.gob.minsegpres.pisee.rest.readers.ReaderTemplateKeyStoreSOAP;
import cl.gob.minsegpres.pisee.rest.readers.ReaderTemplateOutputSOAP;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;
import cl.gob.minsegpres.pisee.rest.util.PiseeStringUtils;
import cl.gob.minsegpres.pisee.rest.util.el.ExpressionLanguageProcessor;

public class RestToSoapConnector {

	private static final String _UTF_8 = "UTF-8";
	private final static Log LOGGER = LogFactory.getLog(RestToSoapConnector.class);

	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		long startTime = System.currentTimeMillis();
		KeyStoreParameterSOAP keyStoreParameter = null; 
		PiseeRespuesta respuesta = new PiseeRespuesta();
		SobreBusiness sobreBusiness = new SobreBusiness();
		try {
			String result = null;
			Call call;
			ExpressionLanguageProcessor processor;
			InputStream is;
			String fileInputStr, fileInputProcessed;
			SOAPEnvelope soapEnvelopeInput, soapEnvelopeOutput;
			SrceiRestToSoapConnector srceiConnector = new SrceiRestToSoapConnector();
			Document document;
			processor = new ExpressionLanguageProcessor();
			call = createCall(configuracionServicio);
			inputParameter = sobreBusiness.fillSobre(inputParameter, configuracionServicio);
			fileInputStr = ReaderTemplateInputSOAP.getInstance().readFile(proveedorServiceName);
			if (null != fileInputStr) {
				fileInputProcessed = processor.processInput(fileInputStr, inputParameter);
				is = new ByteArrayInputStream(fileInputProcessed.getBytes(_UTF_8));
				if (configuracionServicio.hasNeedFirmaDigital()) {
					long tFirmaIN = System.currentTimeMillis();
					keyStoreParameter = ReaderTemplateKeyStoreSOAP.getInstance().findKeyStore(proveedorServiceName + AppConstants.PREFIX_CONFIG_SERVICES_KEYSTORE);
					LOGGER.info(proveedorServiceName + " - INPUT SERVICE ANTES DE FIRMAR : " + PiseeStringUtils.prettyFormat(fileInputProcessed));
					result = srceiConnector.firmarEntrada(is, keyStoreParameter);
					is = new ByteArrayInputStream(result.getBytes(_UTF_8));
					LOGGER.info(proveedorServiceName + " - Tiempo en firmar entrada == " + (System.currentTimeMillis() - tFirmaIN));
				}
				soapEnvelopeInput = new SOAPEnvelope(is);
				
				if (null != result) {
					LOGGER.info(proveedorServiceName + " - INPUT SERVICE : " + PiseeStringUtils.prettyFormat(result));	
				} else {
					LOGGER.info(proveedorServiceName + " - INPUT SERVICE : " + PiseeStringUtils.prettyFormat(soapEnvelopeInput.getAsString()));
				}
				
				long tc1 = System.currentTimeMillis();
				LOGGER.info(transactionLogInput(inputParameter, configuracionServicio));
				soapEnvelopeOutput = call.invoke(soapEnvelopeInput);
				LOGGER.info(proveedorServiceName + " - " + transactionLogOutput(inputParameter, configuracionServicio));
				LOGGER.info(proveedorServiceName + " - Tiempo de respuesta del servicio == " + (System.currentTimeMillis() - tc1));
				if (configuracionServicio.hasNeedFirmaDigital()){
					// Si son los servicios para el totem segpres, no hay que desencriptar...
					if (proveedorServiceName.equals("SOAP_SRCEI_CertificadoNacimientoSEGPRES")) {
						document = PiseeStringUtils.getDom4jDocument(soapEnvelopeOutput.getAsDocument());
					} else if (proveedorServiceName.equals("SOAP_SRCEI_CertificadoMatrimonioSEGPRES")) {
						document = PiseeStringUtils.getDom4jDocument(soapEnvelopeOutput.getAsDocument());
					} else if (proveedorServiceName.equals("SOAP_SRCEI_CertificadoDefuncionSEGPRES")) {
						document = PiseeStringUtils.getDom4jDocument(soapEnvelopeOutput.getAsDocument());
					} else {
						long tFirmaOUT = System.currentTimeMillis();
						result = srceiConnector.descrifarRespuesta(soapEnvelopeOutput.getAsDocument(), keyStoreParameter);
						document = DocumentHelper.parseText(result);
						LOGGER.info(proveedorServiceName + " - Tiempo en descrifrar respuesta == " + (System.currentTimeMillis() - tFirmaOUT));
					}
				} else {
					document = PiseeStringUtils.getDom4jDocument(soapEnvelopeOutput.getAsDocument());	
				}
				LOGGER.info(proveedorServiceName + " - OUTPUT SERVICE : " + PiseeStringUtils.prettyFormat(document.asXML()));
				Element eRoot = document.getRootElement();
				respuesta.setEncabezado(fillEncabezado(eRoot));
				if (AppConstants._CODE_OK.equals(respuesta.getEncabezado().getEstadoSobre())) {
					respuesta.setMetadata(fillMetaData(proveedorServiceName, eRoot));
				}
				// Para los servicios del totem segpres no existe la glosa o estado _CODE_OK...
				if (proveedorServiceName.equals("SOAP_SRCEI_CertificadoNacimientoSEGPRES")) {
					respuesta.setMetadata(fillMetaData(proveedorServiceName, eRoot));
				}
				if (proveedorServiceName.equals("SOAP_SRCEI_CertificadoMatrimonioSEGPRES")) {
					respuesta.setMetadata(fillMetaData(proveedorServiceName, eRoot));
				}
				if (proveedorServiceName.equals("SOAP_SRCEI_CertificadoDefuncionSEGPRES")) {
					respuesta.setMetadata(fillMetaData(proveedorServiceName, eRoot));
				}
			}
		} catch (Exception e) {
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_PROVEEDOR_INTERNO_01);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INTERNAL_ERROR);			
			LOGGER.error(proveedorServiceName + " - Error consumiendo el servicio: " + e);
			e.printStackTrace();
		} 
		long endTime = System.currentTimeMillis();
		LOGGER.info(proveedorServiceName + " - TOTAL CONNECTOR TIME == " + (endTime - startTime) + " MILISECONDS");
		respuesta.setTemporalData(new PiseeTemporalData());
		respuesta.getTemporalData().setFechaConsultaRecibida((Calendar)inputParameter.getHeaderParameter(ParametersName.FECHA_CONSULTA_RECIBIDA));
		respuesta.getTemporalData().setDuracionLlamadaServicio((endTime - startTime));
		
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
		Element eIdSobre, eFechaHora, eNombreProveedor, eNombreServicio, eNombreConsumidor, eNombreTramite;
		Element eEmisorSobre, eEstadoSobre, eGlosaSobre;
		eIdSobre = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/idSobre", eRoot);
		if (null != eIdSobre) {
			encabezado.setIdSobre(eIdSobre.getTextTrim());
		}
		eFechaHora = PiseeStringUtils.elementRecursive("Body/sobre/encabezado/fechaHora", eRoot);
		if (null != eFechaHora) {
			encabezado.setFechaHora(eFechaHora.getTextTrim());
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
			encabezado.setNombreTramite(eNombreTramite.getTextTrim());
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
		OutputParameterSOAP outputParameter = ReaderTemplateOutputSOAP.getInstance().findOutputParameter(serviceName + AppConstants.PREFIX_CONFIG_SERVICES_OUTPUT);
		Element eTmp;
		List<Map<String, String>> resultados = new ArrayList<Map<String, String>>();
		Map<String, String> resultado;
		if (null != outputParameter) {
			if (null != outputParameter.getRutaArreglo()) {
				for (Element element : findElements(eRoot, outputParameter.getRutaArreglo())) {
					resultado = new HashMap<String, String>();
					for (CampoOutputParameterSOAP campo : outputParameter.getCampos()) {
						eTmp = PiseeStringUtils.elementRecursive(campo.getRuta(), element);
						if (null != eTmp) {
							resultado.put(campo.getNombre(), eTmp.getTextTrim());
						}
					}
					resultados.add(resultado);
				}
			} else {
				resultado = new HashMap<String, String>();
				for (CampoOutputParameterSOAP campo : outputParameter.getCampos()) {
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
	
	private String transactionLogInput(InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		StringBuilder sb = new StringBuilder();
		sb.append("Invocando a URL: ");
		sb.append(configuracionServicio.getEndPoint());
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
	
	private String transactionLogOutput(InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		StringBuilder sb = new StringBuilder();
		sb.append("REST - Retorno de la URL: ");
		sb.append(configuracionServicio.getEndPoint());
		sb.append("\n");
		sb.append("idSobre : ");
		sb.append(inputParameter.getHeaderParameter(SobreBusiness.ID_SOBRE));
		return sb.toString();
	}

}
