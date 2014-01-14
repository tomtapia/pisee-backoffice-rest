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

import cl.gob.minsegpres.pisee.rest.entities.KeyStoreParameter;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class ReaderServicesKeyStore {

	private static final Log LOGGER = LogFactory.getLog(ReaderServicesKeyStore.class);
	private static ReaderServicesKeyStore _READER = null;
	private List<KeyStoreParameter> keyStoreParameters = new ArrayList<KeyStoreParameter>();
	
	//--- getters
	public synchronized static ReaderServicesKeyStore getInstance() {
		try {
			if (_READER == null) {
				_READER = new ReaderServicesKeyStore();
			}
		} catch (Exception e) {			
			LOGGER.error("Exception == " + e.fillInStackTrace() );
		}
		return _READER;
	}
	
	//-- getters
	public List<KeyStoreParameter> getKeyStoreParameters() {
		return keyStoreParameters;
	}
	
	//--- constructor
	private ReaderServicesKeyStore() {
		readFiles();
	}
	
	//--- public
	public synchronized KeyStoreParameter findKeyStore(String serviceName){
		for (KeyStoreParameter keyStoreParameter: keyStoreParameters){
			if (serviceName.equals(keyStoreParameter.getServiceName())){
				return keyStoreParameter;
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
					if (file.getCanonicalPath().indexOf(AppConstants.PREFIX_CONFIG_SERVICES_KEYSTORE) != -1){
						keyStoreParameters.add(readKeyStoreFile(file.getName(), file.getCanonicalPath()) );
					}
				}
			} catch (IOException e) {
				LOGGER.error("ReadFiles - IOException == " + e.fillInStackTrace());
			}				
		}
	}	
	
	private KeyStoreParameter readKeyStoreFile(String fileName, String pathFileName) {
		KeyStoreParameter keyStoreParameter = null;
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(pathFileName);
			Element eRoot = document.getRootElement();
			keyStoreParameter = new KeyStoreParameter();
			keyStoreParameter.setServiceName(fileName);
			keyStoreParameter.setKeystoreType(eRoot.elementTextTrim("keystore_type"));
			keyStoreParameter.setKeystoreFile(eRoot.elementTextTrim("keystore_file"));
			keyStoreParameter.setKeystorePass(eRoot.elementTextTrim("keystore_pass"));
			keyStoreParameter.setPrivateKeyAlias(eRoot.elementTextTrim("private_key_alias"));
			keyStoreParameter.setPrivateKeyPass(eRoot.elementTextTrim("private_key_pass"));
		} catch (DocumentException e) {
			LOGGER.error("readKeyStoreFile - DocumentException - para servicio == " + fileName + " , " + e.fillInStackTrace());
		}
		return keyStoreParameter;
	}	
	
}
