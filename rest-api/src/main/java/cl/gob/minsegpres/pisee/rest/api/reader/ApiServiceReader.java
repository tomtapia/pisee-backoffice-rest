package cl.gob.minsegpres.pisee.rest.api.reader;

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

import cl.gob.minsegpres.pisee.rest.api.model.ApiMethod;
import cl.gob.minsegpres.pisee.rest.api.model.ApiParameter;
import cl.gob.minsegpres.pisee.rest.api.model.ApiRequest;
import cl.gob.minsegpres.pisee.rest.api.model.ApiResponse;
import cl.gob.minsegpres.pisee.rest.api.model.ApiService;
import cl.gob.minsegpres.pisee.rest.api.util.AppConstants;

public class ApiServiceReader {

	private static final Log LOGGER = LogFactory.getLog(ApiServiceReader.class);
	private static ApiServiceReader _READER = null;
	private List<ApiService> apiServices = new ArrayList<ApiService>();
	
	//--- getters
	public synchronized static ApiServiceReader getInstance() {
		try {
			//TODO: solo para pruebas
			//if (_READER == null) {
				_READER = new ApiServiceReader();
			//}
		} catch (Exception e) {			
			LOGGER.error("Exception == " + e.fillInStackTrace() );
		}
		return _READER;
	}
	
	//-- getters
	public synchronized List<ApiService> getApiServices() {
		return apiServices;
	}
	
	//--- constructor
	private ApiServiceReader() {
		readFiles();
	}
	
	//--- public
	public synchronized ApiService findApiService(String id){
		for (ApiService apiService: apiServices){
			if (id.equals(apiService.getId())){				
				return apiService;
			}
		}
		return null;
	}	
	
	//--- utils
	private synchronized void readFiles(){
		File file, folderPath;
		StringBuffer buffer = new StringBuffer();
		buffer.append(AppConstants.HOME_DIR);
		buffer.append(AppConstants.CONFIG_API_SERVICES_DIR);
		folderPath = new File (buffer.toString());
		if (folderPath.isDirectory()){
			File[] files = folderPath.listFiles();
			try {
				for (int i = 0; i < files.length; i++){
					file = files[i];
					LOGGER.info("ReadFiles - Leyendo el archivo: " + file.getName());
					apiServices.add(readApiService(file.getCanonicalPath()) );
					
				}
			} catch (IOException e) {
				LOGGER.error("ReadFiles - IOException == " + e.fillInStackTrace());
			}				
		}
	}	
	
	private ApiService readApiService(String pathFileName){
		ApiService apiService = null;
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(pathFileName);
			Element eRoot = document.getRootElement();
			eRoot = eRoot.element("api_service");
			apiService = new ApiService();
			apiService.setId(eRoot.attributeValue("id"));
			apiService.setName(eRoot.elementTextTrim("name"));
			apiService.setDescription(eRoot.elementTextTrim("description"));
			apiService.setPath(eRoot.elementTextTrim("path"));
			apiService.setMethods(readApiMethods(eRoot));
		} catch (DocumentException e) {
			LOGGER.error("readApiService - DocumentException - para servicio == " + pathFileName + " , " + e.fillInStackTrace());
		}
		return apiService;			
	}
	
	@SuppressWarnings("unchecked")
	private List<ApiMethod> readApiMethods(Element eRoot){
		ApiMethod apiMethod = null;
		List<ApiMethod> apiMethods = null;
		List<Element> pathParameters = eRoot.element("api_methods").elements();
		if (null != pathParameters){
			apiMethods = new ArrayList<ApiMethod>();
			for (Element param: pathParameters){
				apiMethod = new ApiMethod();
				apiMethod.setId(param.attributeValue("id"));
				apiMethod.setName(param.elementTextTrim("name"));
				apiMethod.setDescription(param.elementTextTrim("description"));
				apiMethod.setRequest(readApiRequest(param));
				apiMethod.setResponse(readApiResponse(param));
				apiMethods.add(apiMethod);
			}
		}
		return apiMethods;
	}
	
	private ApiRequest readApiRequest(Element eRoot){
		ApiRequest apiRequest = null;
		apiRequest = new ApiRequest();
		Element element;
		element = eRoot.element("request");
		apiRequest = new ApiRequest();
		apiRequest.setVersion(element.elementTextTrim("version"));
		apiRequest.setMethod(element.elementTextTrim("method"));
		apiRequest.setExampleURI(element.elementTextTrim("example_uri"));
		apiRequest.setPath(element.elementTextTrim("path"));
		apiRequest.setParameters(readApiParameters(element));
		return apiRequest;
	}

	private ApiResponse readApiResponse(Element eRoot){
		ApiResponse apiResponse = null;
		apiResponse = new ApiResponse();
		Element element;
		element = eRoot.element("response");
		apiResponse = new ApiResponse();
		apiResponse.setProduces(element.elementTextTrim("produces"));
		apiResponse.setExample(element.elementTextTrim("example"));
		apiResponse.setParameters(readApiParameters(element));
		return apiResponse;
	}	
	
	@SuppressWarnings("unchecked")
	private List<ApiParameter> readApiParameters(Element eRoot){
		ApiParameter apiParameter = null;
		List<ApiParameter> apiParameters = null;
		List<Element> pathParameters = eRoot.element("parameters").elements();
		if (null != pathParameters){
			apiParameters = new ArrayList<ApiParameter>();
			for (Element param: pathParameters){
				apiParameter = new ApiParameter();
				apiParameter.setId(param.attributeValue("id"));
				apiParameter.setName(param.elementTextTrim("name"));
				apiParameter.setDescription(param.elementTextTrim("description"));
				if (null != param.elementTextTrim("required")){
					apiParameter.setRequired(Boolean.parseBoolean(param.elementTextTrim("required")));	
				}
				if (null != param.elementTextTrim("type")){
					apiParameter.setType(param.elementTextTrim("type"));	
				}
				apiParameters.add(apiParameter);
			}
		}
		return apiParameters;
	}	

}
