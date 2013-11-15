package cl.gob.minsegpres.pisee.rest.readers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cl.gob.minsegpres.pisee.rest.entities.CampoOutputParameter;
import cl.gob.minsegpres.pisee.rest.entities.OutputParameter;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class ReaderServicesOutput {

	private static final String _EN_ARREGLO = "en_arreglo";
	private static final String _RUTA_ARREGLO = "ruta_arreglo";
	private static final String _RUTA = "ruta";
	private static final String _NOMBRE = "nombre";
	private static final String _CAMPO = "campo";
	private static final Log LOGGER = LogFactory.getLog(ReaderServicesOutput.class);
	private static ReaderServicesOutput _READER = null;
	private List<OutputParameter> servicesOutputParameters = new ArrayList<OutputParameter>();

	//--- getters
	public synchronized static ReaderServicesOutput getInstance() {
		try {
			if (_READER == null) {
				_READER = new ReaderServicesOutput();
			}
		} catch (Exception e) {			
			LOGGER.error("Exception == " + e.fillInStackTrace() );
		}
		return _READER;
	}
	
	//-- getters
	public List<OutputParameter> getServicesOutputParameters() {
		return servicesOutputParameters;
	}
	
	//--- constructor
	private ReaderServicesOutput() {
		readFiles();
	}
	
	//--- public
	public synchronized OutputParameter findOutputParameter(String serviceName){
		for (OutputParameter outputParameter: servicesOutputParameters){
			if (serviceName.equals(outputParameter.getServiceName())){
				return outputParameter;
			}
		}
		return null;
	}

	//--- utils
	private synchronized void readFiles(){
		File file, folderPath;
		StringBuffer buffer = new StringBuffer();
		buffer.append(AppConstants.HOME_DIR);
		buffer.append(AppConstants.CONFIG_SERVICES_DIR);
		folderPath = new File (buffer.toString());
		if (folderPath.isDirectory()){
			File[] files = folderPath.listFiles();
			try {
				for (int i = 0; i < files.length; i++){
					file = files[i];
					if (file.getCanonicalPath().indexOf(AppConstants.PREFIX_CONFIG_SERVICES_OUTPUT) != -1){
						servicesOutputParameters.add( readOutputFile(file.getName(), file.getCanonicalPath()) );
					}
				}
			} catch (IOException e) {
				LOGGER.error("ReadFiles - IOException == " + e.fillInStackTrace());
			}				
		}
	}	
	
	private OutputParameter readOutputFile(String fileName, String pathFileName) {
		OutputParameter outputParameter = null;
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(pathFileName);
			Element eRoot = document.getRootElement();
			outputParameter = new OutputParameter();
			outputParameter.setRutaArreglo(eRoot.elementText(_RUTA_ARREGLO));
			outputParameter.setServiceName(fileName);
			outputParameter.setCampos(loadCampos(eRoot));
		} catch (DocumentException e) {
			LOGGER.error("ReadOutputFile - DocumentException - para servicio == " + fileName + " , " + e.fillInStackTrace());
		}
		return outputParameter;
	}
	
	@SuppressWarnings("unchecked")
	private List<CampoOutputParameter> loadCampos(Element wsConfig) {
		List<CampoOutputParameter> campos = new ArrayList<CampoOutputParameter>();
		List<Element> eCampos = wsConfig.elements(_CAMPO);
		for (Element eCampo : eCampos) {
			campos.add(fillCampoOutputParameter(eCampo));
		}
		return campos;
	}	
	
	private CampoOutputParameter fillCampoOutputParameter(Element element){
		CampoOutputParameter campo = new CampoOutputParameter();
		campo.setEnArreglo(element.elementText(_EN_ARREGLO));
		campo.setNombre(element.elementText(_NOMBRE));
		campo.setRuta(element.elementText(_RUTA));
		return campo;
	}	
	
}
