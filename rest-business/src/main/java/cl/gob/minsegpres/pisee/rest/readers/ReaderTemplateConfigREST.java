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

import cl.gob.minsegpres.pisee.rest.entities.rest.ConfigServiceREST;
import cl.gob.minsegpres.pisee.rest.entities.rest.ParameterServiceREST;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class ReaderTemplateConfigREST {

	private static final Log LOGGER = LogFactory.getLog(ReaderTemplateConfigREST.class);
	private static ReaderTemplateConfigREST _READER = null;
	private List<ConfigServiceREST> configServices = new ArrayList<ConfigServiceREST>();
	
	//--- getters
	public synchronized static ReaderTemplateConfigREST getInstance() {
		try {
			if (_READER == null) {
				_READER = new ReaderTemplateConfigREST();
			}
		} catch (Exception e) {			
			LOGGER.error("Exception == " + e.fillInStackTrace() );
		}
		return _READER;
	}
	
	//-- getters
	public List<ConfigServiceREST> getConfigServices() {
		return configServices;
	}
	
	//--- constructor
	private ReaderTemplateConfigREST() {
		readFiles();
	}
	
	//--- public
	public synchronized ConfigServiceREST findConfigService(String serviceName){
		for (ConfigServiceREST configService: configServices){
			if (configService.getName().equals(serviceName + AppConstants.PREFIX_CONFIG_SERVICES_CONFIG)){				
				return configService;
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
					if (file.getCanonicalPath().indexOf(AppConstants.PREFIX_SERVICE_REST) != -1){
						configServices.add(readConfigServiceREST(file.getName(), file.getCanonicalPath()) );
					}
				}
			} catch (IOException e) {
				LOGGER.error("ReadFiles - IOException == " + e.fillInStackTrace());
			}				
		}
	}	
	
	private ConfigServiceREST readConfigServiceREST(String fileName, String pathFileName) {
		ConfigServiceREST configService = null;
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(pathFileName);
			Element eRoot = document.getRootElement();
			eRoot = eRoot.element("rest_service");
			configService = new ConfigServiceREST();
			configService.setName(fileName);
			configService.setId(eRoot.attributeValue("id"));
			configService.setDescription(eRoot.elementTextTrim("description"));
			configService.setMethod(eRoot.elementTextTrim("method"));
			configService.setBaseEndpoint(eRoot.elementTextTrim("base_endpoint"));
			configService.setExtendEndpoint(eRoot.elementTextTrim("extend_endpoint"));
			configService.setQueryParameters(readParameters(eRoot, "query_parameters"));
			configService.setPathParameters(readParameters(eRoot, "path_parameters"));
		} catch (DocumentException e) {
			LOGGER.error("readConfigServiceREST - DocumentException - para servicio == " + fileName + " , " + e.fillInStackTrace());
		}
		return configService;
	}	
	
	@SuppressWarnings("unchecked")
	private List<ParameterServiceREST> readParameters(Element eRoot, String elementName){
		List<ParameterServiceREST> params = null;
		ParameterServiceREST parameterService;
		List<Element> pathParameters = eRoot.element(elementName).elements();
		if (null != pathParameters){
			params = new ArrayList<ParameterServiceREST>();
			for (Element param: pathParameters){
				parameterService = new ParameterServiceREST();
				if (null != param.attribute("optional")){
					parameterService.setOptional(Boolean.parseBoolean(param.attributeValue("optional")));
				}else{
					parameterService.setOptional(Boolean.FALSE);
				}
				parameterService.setName(param.getTextTrim());
				params.add(parameterService);
			}
		}
		return params;
	}
	
}
