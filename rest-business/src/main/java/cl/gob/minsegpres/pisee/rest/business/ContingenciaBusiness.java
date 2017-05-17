package cl.gob.minsegpres.pisee.rest.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.core.model.Organismo;
import cl.gob.minsegpres.pisee.core.model.Servicio;
import cl.gob.minsegpres.pisee.core.model.ServicioTramite;
import cl.gob.minsegpres.pisee.core.model.Tramite;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class ContingenciaBusiness {
	
	private static final String COMMENT = "#";
	private static final String JBOSS_SERVER_NAME = "jboss.server.name";
	private static final String PISEE_CONTINGENCIA = "pisee-contingencia";
	private static final String DOUBLE_PIPE = "\\|\\|";
	private List<ConfiguracionServicio> configuraciones;
	private static ContingenciaBusiness _instance = null;
	
	private ContingenciaBusiness(){
		loadConfiguraciones();
	}
	
	public static ContingenciaBusiness getInstance(){
		if (null == _instance){
			_instance = new ContingenciaBusiness(); 
		}
		return _instance;
	}
	
	public static boolean isContingencia(){
		if(PISEE_CONTINGENCIA.equals(System.getProperty(JBOSS_SERVER_NAME))){
			return true;
		} else { 
			return false;	
		}		
	}
	
	public ConfiguracionServicio getConfiguracionServicio(String token){
		for (ConfiguracionServicio cfg: configuraciones){
			if (token.equals(cfg.getToken())){
				return cfg;
			}
		}
		return null;
	}
	
	private void loadConfiguraciones(){
		configuraciones = new ArrayList<ConfiguracionServicio>();
		List<String> lineas; 
		try {
			lineas = FileUtils.readLines(new File(processPathConfig()));
			for (String linea: lineas){
				if (!linea.startsWith(COMMENT)){
					configuraciones.add(fillConfiguracionServicio(linea));	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ConfiguracionServicio fillConfiguracionServicio(String linea){
		String[] arrLinea = linea.split(DOUBLE_PIPE);
		ConfiguracionServicio configuracionServicio;
		configuracionServicio = new ConfiguracionServicio();
		configuracionServicio.setToken(arrLinea[0]);
		configuracionServicio.setEndPoint(arrLinea[1]);
		configuracionServicio.setOperation(arrLinea[2]);
		configuracionServicio.setTimeout(Long.parseLong(arrLinea[3]));
		configuracionServicio.setHttpUserName(arrLinea[4]);
		configuracionServicio.setHttpPassword(arrLinea[5]);		
		configuracionServicio.setCodigoInstitucion(arrLinea[6]);
		configuracionServicio.setCodigoTramite(arrLinea[7]);
		configuracionServicio.setEstado(arrLinea[8]);
		configuracionServicio.setRequiereFirma(arrLinea[9]);
		configuracionServicio.setServicioTramite(new ServicioTramite());
		configuracionServicio.getServicioTramite().setServicio(new Servicio());
		configuracionServicio.getServicioTramite().getServicio().setOrganismo(new Organismo());
		configuracionServicio.getServicioTramite().getServicio().getOrganismo().setSigla(arrLinea[10]);
		configuracionServicio.getServicioTramite().getServicio().setNombreCorto(arrLinea[11]);
		configuracionServicio.getServicioTramite().setTramite(new Tramite());
		configuracionServicio.getServicioTramite().getTramite().setOrganismo(new Organismo());
		configuracionServicio.getServicioTramite().getTramite().getOrganismo().setSigla(arrLinea[12]);
		configuracionServicio.getServicioTramite().getTramite().setNombre(arrLinea[13]);
		return configuracionServicio;		
	}

	private String processPathConfig(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(AppConstants.HOME_DIR);
		buffer.append(AppConstants.CONFIG_DIR);
		buffer.append("PiseeContingencia_REST.properties");
		return buffer.toString();
	}	
	
}
