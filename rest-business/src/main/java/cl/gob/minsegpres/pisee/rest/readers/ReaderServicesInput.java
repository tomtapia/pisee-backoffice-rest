package cl.gob.minsegpres.pisee.rest.readers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public final class ReaderServicesInput {
	
	private final static Log LOGGER = LogFactory.getLog(ReaderServicesInput.class);
	private static ReaderServicesInput instance = null;
	private Map<String, String> pathFiles = new HashMap<String,String>();
	
	private ReaderServicesInput(){ 	}
	
	public static synchronized ReaderServicesInput getInstance() {
		if (instance == null){
			instance = new ReaderServicesInput();
		}
		return instance;
	}
	
	public String readFile(String serviceName){
		String fileName, fileString = null;
		fileString = pathFiles.get(serviceName);
		try {		
			if (null == fileString){
				fileName = processPathInputFiles(serviceName);			
				pathFiles.put(serviceName, readFileToString(new File(fileName)));
				fileString = pathFiles.get(serviceName);
			}
		} catch (IOException e) {
			LOGGER.error("readFile - No se encuentra el archivo de configuracion para " + serviceName + " - " + e.fillInStackTrace() );
		}		
		return fileString;
	}	
	
	private String readFileToString(File theFile) throws IOException{
		return FileUtils.readFileToString(theFile,"ISO-8859-1");
	}
	
	private String processPathInputFiles(String serviceName){
		StringBuffer buffer = new StringBuffer();
		buffer.append(AppConstants.HOME_DIR);
		buffer.append(AppConstants.CONFIG_SERVICES_DIR);
		buffer.append(serviceName);
		buffer.append(AppConstants.PREFIX_CONFIG_SERVICES_INPUT);
		return buffer.toString();
	}	
	
}
