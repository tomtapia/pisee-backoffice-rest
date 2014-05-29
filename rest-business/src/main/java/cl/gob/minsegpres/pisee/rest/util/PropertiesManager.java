package cl.gob.minsegpres.pisee.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesManager implements Serializable {

	private static final long serialVersionUID = -5869502595510212212L;
	private static final Logger LOGGER = Logger.getLogger(PropertiesManager.class);
	public static final String PISEE_CONF_DIRECTORY = "/deploy/base/piseeConf";

	private static PropertiesManager instance = null;
	private HashMap<String, HashMap<String, String>> properties;
	private String _PROPERTIES_AMBIENTE = "ambientepisee.properties";
	

	public static synchronized PropertiesManager getInstance() {
		if (instance == null){
			instance = new PropertiesManager();
		}
		return instance;
	} 
	
	private PropertiesManager(){
		properties = new HashMap<String, HashMap<String,String>>();
		properties.put(_PROPERTIES_AMBIENTE, loadProperties(_PROPERTIES_AMBIENTE));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<String, String> loadProperties(String fileName) {
		HashMap<String, String> propiedades = null;
		try {
			File file = new File(System.getProperty("jboss.server.home.dir") + PISEE_CONF_DIRECTORY, fileName);
			Properties propertiesTemporal = new Properties();
			propertiesTemporal.load(new FileInputStream(file));
			propiedades = new HashMap(propertiesTemporal);
			LOGGER.info("Cargado valores servidor " + fileName + "[" + propiedades + "]");
		} catch (Exception e) {
			LOGGER.warn("CARGA ARCHIVO DE PROPIEDADES " + fileName + " fallida.");
			propiedades = new HashMap<String, String>();
		}
		return propiedades;
	}

	public String getPropertyAmbiente(String key){
		HashMap<String, String> propertiesAmbiente;
		propertiesAmbiente = properties.get(_PROPERTIES_AMBIENTE);
		String propertyValue = null;
		if (propertiesAmbiente.containsKey(key)){
			propertyValue = propertiesAmbiente.get(key); 
		}
		return propertyValue;
	}	


}
