package cl.gob.minsegpres.pisee.rest.connector;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.business.SobreBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeEncabezado;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeTemporalData;
import cl.gob.minsegpres.pisee.rest.entities.security.Certificate;
import cl.gob.minsegpres.pisee.rest.entities.security.CertificateFromPfx;
import cl.gob.minsegpres.pisee.rest.entities.sii.InteroperabilityMessageFactory;
import cl.gob.minsegpres.pisee.rest.entities.sii.Message;
import cl.gob.minsegpres.pisee.rest.entities.sii.MessageFactory;
import cl.gob.minsegpres.pisee.rest.entities.sii.OrganismSecurityOperationsFactory;
import cl.gob.minsegpres.pisee.rest.entities.sii.SecurityOperationsFactory;
import cl.gob.minsegpres.pisee.rest.entities.sii.Seed;
import cl.gob.minsegpres.pisee.rest.entities.sii.SeedMessageFilter;
import cl.gob.minsegpres.pisee.rest.entities.sii.TokenMessageFilter;
import cl.gob.minsegpres.pisee.rest.entities.sii.WebConnection;
import cl.gob.minsegpres.pisee.rest.entities.sii.XmlHelpers;
import cl.gob.minsegpres.pisee.rest.entities.soap.CampoOutputParameterSOAP;
import cl.gob.minsegpres.pisee.rest.entities.soap.OutputParameterSOAP;
import cl.gob.minsegpres.pisee.rest.readers.ReaderTemplateInputSOAP;
import cl.gob.minsegpres.pisee.rest.readers.ReaderTemplateOutputSOAP;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;
import cl.gob.minsegpres.pisee.rest.util.PiseeStringUtils;
import cl.gob.minsegpres.pisee.rest.util.el.ExpressionLanguageProcessor;

public class SiiRestToSoapConnector {

	private static final String _UTF_8 = "UTF-8";
	private final static Log LOGGER = LogFactory.getLog(SiiRestToSoapConnector.class);

	private MessageFactory piseeMessageFactory;
	private WebConnection wc;

	public SiiRestToSoapConnector() {
		piseeMessageFactory = InteroperabilityMessageFactory.newInstance();
	}

	public PiseeRespuesta callService(String proveedorServiceName, InputParameter inputParameter, ConfiguracionServicio configuracionServicio) {
		long startTime = System.currentTimeMillis();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		SobreBusiness sobreBusiness = new SobreBusiness();
		try {
			Call call;
			ExpressionLanguageProcessor processor;
			InputStream is;
			String fileInputStr, fileInputProcessed;
			SOAPEnvelope soapEnvelopeInput, soapEnvelopeOutput;
			processor = new ExpressionLanguageProcessor();
			call = createCall(configuracionServicio);
			inputParameter = sobreBusiness.fillSobre(inputParameter, configuracionServicio);
			fileInputStr = ReaderTemplateInputSOAP.getInstance().readFile(proveedorServiceName);
			if (null != fileInputStr) {

				// solo para SII
				inputParameter.addBodyParameter(ParametersName.SII_TOKEN, getToken());

				fileInputProcessed = processor.processInput(fileInputStr, inputParameter);
				is = new ByteArrayInputStream(fileInputProcessed.getBytes(_UTF_8));
				soapEnvelopeInput = new SOAPEnvelope(is);
				LOGGER.info(proveedorServiceName + " - INPUT SERVICE : " + PiseeStringUtils.prettyFormat(soapEnvelopeInput.getAsString()));

				long tc1 = System.currentTimeMillis();
				LOGGER.info(transactionLogInput(inputParameter, configuracionServicio));
				
				soapEnvelopeOutput = call.invoke(soapEnvelopeInput);
				String tmpResponse = "";
				
				//LOGGER.info(transactionLogOutput(inputParameter, configuracionServicio));
				LOGGER.info(proveedorServiceName + " - Tiempo de respuesta del servicio == " + (System.currentTimeMillis() - tc1));

				//FIX: Esto es lo que se esdta demorando ya no es el parseo
				tmpResponse = soapEnvelopeOutput.getAsDocument().getDocumentElement().getTextContent().trim();
				
				org.dom4j.Document theDocument = DocumentHelper.parseText(tmpResponse);
				LOGGER.info(proveedorServiceName + " - OUTPUT SERVICE : " + PiseeStringUtils.prettyFormat(theDocument.asXML()));
				respuesta.setEncabezado(fillEncabezado(configuracionServicio));
				respuesta.setMetadata(fillMetaData(proveedorServiceName, theDocument.getRootElement()));
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
		respuesta.getTemporalData().setFechaConsultaRecibida((Calendar) inputParameter.getHeaderParameter(ParametersName.FECHA_CONSULTA_RECIBIDA));
		respuesta.getTemporalData().setDuracionLlamadaServicio((endTime - startTime));
		return respuesta;
	}

	public String getToken() throws Exception {
		Certificate certificate = CertificateFromPfx.newInstance("E:\\USR\\Dropbox\\interop\\Daniel Troncoso\\siiPersonal.pfx", "nscarlos001".toCharArray());
		Seed seed = getSeed();
		SecurityOperationsFactory securityOperationsFactory = OrganismSecurityOperationsFactory.newInstance();
		Message tokenMessage = piseeMessageFactory.createTokenMessage(seed, securityOperationsFactory.createSiiSecurityOperations(certificate));
		wc = WebConnection.newInstance("https://www3.pisee.cl/SIIGetTokenFromSeedWs1Proxy", tokenMessage);
		// LOGGER.info("REQUEST - getToken() == " +
		// XmlHelpers.xmlToString(tokenMessage.getSignedMessage()) );
		// LOGGER.info("------------------------------------------------------------------------------------------------------");
		Document tokenResponse = wc.response(new TokenMessageFilter());
		// LOGGER.info("RESPONSE - getToken() == " +
		// XmlHelpers.xmlToString(tokenResponse) );
		// return XmlHelpers.getTextContent(tokenResponse, "", "TOKEN");

		String token = XmlHelpers.getTextContent(tokenResponse, "", "TOKEN");
		LOGGER.info("TOKEN SII = " + token);
		return token;
	}

	public Seed getSeed() throws Exception {
		Message seedMessage = piseeMessageFactory.createSeedMessage();
		wc = WebConnection.newInstance("https://www3.pisee.cl/SIICrSeedWs1Proxy", seedMessage);
		// LOGGER.info("REQUEST - getSeed() == " +
		// XmlHelpers.xmlToString(seedMessage.getMessage()) );
		// LOGGER.info("------------------------------------------------------------------------------------------------------");
		Document response = wc.response(new SeedMessageFilter());
		// LOGGER.info("RESPONSE - getSeed() == " +
		// XmlHelpers.xmlToString(response) );
		// LOGGER.info("------------------------------------------------------------------------------------------------------");
		return new Seed(response);
	}

	private PiseeEncabezado fillEncabezado(ConfiguracionServicio configuracionServicio) throws Exception {
		SobreBusiness theBusiness = new SobreBusiness();
		PiseeEncabezado encabezado = new PiseeEncabezado();
		encabezado.setIdSobre(theBusiness.generateSobreID(configuracionServicio));
		encabezado.setFechaHora(theBusiness.generateSobreFechaHora());
		encabezado.setNombreProveedor(configuracionServicio.getServicioTramite().getServicio().getOrganismo().getSigla());
		encabezado.setNombreServicio(configuracionServicio.getServicioTramite().getServicio().getNombre());
		encabezado.setNombreConsumidor(configuracionServicio.getServicioTramite().getTramite().getOrganismo().getSigla());
		encabezado.setNombreTramite(configuracionServicio.getServicioTramite().getTramite().getNombre());
		encabezado.setEmisorSobre(AppConstants._EMISOR_PISEE);
		encabezado.setEstadoSobre(AppConstants._CODE_OK);
		encabezado.setGlosaSobre(AppConstants._MSG_CODE_OK);
		return encabezado;
	}

	// --------------------------------------------------------------------------------------------------------------

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

	
	
	
	
	public static void main(String[] args) {
		SiiRestToSoapConnector fw = new SiiRestToSoapConnector();
        fw.walk("E:\\USR\\daniel\\workspace_pisee_prod\\" );
	}
	
	 public void walk( String path ) {
		 File root = new File( path );
		 File[] list = root.listFiles();
		 if (list == null) return;
		 for ( File f : list ) {
			 if (f.isDirectory() ) {
				 walk( f.getAbsolutePath() );
			 }else {
				 if (!f.getAbsoluteFile().toString().contains(".svn") && !f.getAbsoluteFile().toString().contains(".metadata")){
					 if (f.getAbsoluteFile().toString().contains("jboss-esb.xml")){
						 System.out.println("" + f.getAbsoluteFile() );
						 recorrerXML(f.getAbsoluteFile());
					 }					 
				 }
			 }
		 }
	 }
	 
	 public void recorrerXML(File theFile){
		 try {
			 org.dom4j.io.SAXReader reader = new SAXReader();
			 org.dom4j.Document document = reader.read(theFile);
			 Element eServices, eActions;
			 Element eService, eAction, eProperty;
			 eServices = PiseeStringUtils.elementRecursive("services", document.getRootElement());
			 for (int i = 0; i < eServices.elements().size(); i++){
				 eService = (Element)eServices.elements().get(i);
				 eActions = PiseeStringUtils.elementRecursive("actions", eService);
				 for (int j = 0; j < eActions.elements().size(); j++){
					 eAction = (Element)eActions.elements().get(j);
					 if (eAction.attribute("class").getText().equals("cl.minsegpres.pisee.esb.action.servicios.integradores.HttpRouterAction")){
						 for (int x = 0; x < eAction.elements("property").size(); x++){
							 eProperty = (Element)eAction.elements().get(x);
							 if (eProperty.attribute("name").getText().equals("endpointUrl")){
								 System.out.println("										" + eProperty.attribute("value").getText());
							 }
						 }
					 }
				 }
			 }
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		    
	 }
	 
	

}
