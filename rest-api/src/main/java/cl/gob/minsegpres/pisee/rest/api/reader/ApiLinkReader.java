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

import cl.gob.minsegpres.pisee.rest.api.model.ApiLink;
import cl.gob.minsegpres.pisee.rest.api.util.AppConstants;

public class ApiLinkReader {

	private static final Log LOGGER = LogFactory.getLog(ApiLinkReader.class);
	private static ApiLinkReader _READER = null;
	private List<ApiLink> apiLinks = new ArrayList<ApiLink>();

	// --- getters
	public synchronized static ApiLinkReader getInstance() {
		//TODO: solo para pruebas
		//if (_READER == null) {
			_READER = new ApiLinkReader();
		//}
		return _READER;
	}

	// -- getters
	public synchronized List<ApiLink> getApiLinks() {
		return apiLinks;
	}

	// --- constructor
	private ApiLinkReader() {
		readFiles();
	}

	// --- public
	public synchronized ApiLink findApiService(String id) {
		for (ApiLink apiLink : apiLinks) {
			if (id.equals(apiLink.getId())) {
				return apiLink;
			}
		}
		return null;
	}

	// --- utils
	private synchronized void readFiles() {
		apiLinks = new ArrayList<ApiLink>();
		File file, folderPath;
		StringBuffer buffer = new StringBuffer();
		buffer.append(AppConstants.HOME_DIR);
		buffer.append(AppConstants.CONFIG_API_LINKS_DIR);
		folderPath = new File(buffer.toString());
		if (folderPath.isDirectory()) {
			File[] files = folderPath.listFiles();
			try {
				for (int i = 0; i < files.length; i++) {
					file = files[i];
					LOGGER.info("ReadFiles - Leyendo el archivo: " + file.getName());
					//apiLinks.addAll(readApiLinks(file.getCanonicalPath()));
					
					readApiLinks(file.getCanonicalPath());
				}
			} catch (IOException e) {
				LOGGER.error("ReadFiles - IOException == " + e.fillInStackTrace());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void readApiLinks(String pathFileName) {
		SAXReader reader = new SAXReader();
		ApiLink apiLink;
		try {
			Document document = reader.read(pathFileName);
			Element eRoot = document.getRootElement();
			List<Element> pathParameters = eRoot.elements();
			if (null != pathParameters) {
				for (Element param : pathParameters) {
					apiLink = new ApiLink();
					apiLink.setId(param.attributeValue("id"));
					apiLink.setName(param.elementTextTrim("name"));
					apiLink.setValue(param.elementTextTrim("value"));
					apiLinks.add(apiLink);
				}
			}
		} catch (DocumentException e) {
			LOGGER.error("readApiLink - DocumentException - para servicio == " + pathFileName + " , " + e.fillInStackTrace());
		}
	}

}
