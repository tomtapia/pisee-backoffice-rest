package cl.gob.minsegpres.pisee.rest.services;


import java.util.List;

import org.apache.log4j.Logger;

import cl.gob.minsegpres.pisee.rest.dao.ConfiguracionServicioDao;
import cl.gob.minsegpres.pisee.rest.model.ConfiguracionServicio;

public class ConfiguracionServicioService {

	private static final Logger LOGGER = Logger.getLogger(ConfiguracionServicioService.class);
	
	public ConfiguracionServicio findConfiguracionServicio(String tokenPisee){
		ConfiguracionServicioDao theDao = new ConfiguracionServicioDao();
		return theDao.findConfiguracionServicio(tokenPisee);
	}
	
	public ConfiguracionServicio findConfiguracionServicioByServicioTramite(Long idServicioTramite){
		ConfiguracionServicioDao theDao = new ConfiguracionServicioDao();
		return theDao.findConfiguracionServicioByServicioTramite(idServicioTramite);		
	}
	
	public List<ConfiguracionServicio> findConfiguracionServicio(Long idConsumidor){
		ConfiguracionServicioDao theDao = new ConfiguracionServicioDao();
		return theDao.findConfiguracionesServicio(idConsumidor);
	}
	
	public boolean saveConfiguracionServicio(ConfiguracionServicio configuracionServicio) {
		ConfiguracionServicioDao theDao = new ConfiguracionServicioDao();
		boolean result = false;
		try {
			theDao.saveConfiguracionServicio(configuracionServicio);
			result = true;
		} catch (Exception e) {
			result = false;
			LOGGER.error("saveConfiguracionServicio - Exception == " + e);
		}
		return result;
	}	

	public boolean updateConfiguracionServicio(ConfiguracionServicio configuracionServicio) {
		ConfiguracionServicioDao theDao = new ConfiguracionServicioDao();
		boolean result = false;
		try {
			theDao.saveConfiguracionServicio(configuracionServicio);
			result = true;
		} catch (Exception e) {
			result = false;
			LOGGER.error("saveConfiguracionServicio - Exception == " + e);
		}
		return result;
	}	
	
}
