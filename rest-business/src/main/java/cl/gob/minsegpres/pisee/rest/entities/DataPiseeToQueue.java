package cl.gob.minsegpres.pisee.rest.entities;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

public class DataPiseeToQueue {

	private ConfiguracionServicio configuracionServicio;
	private PiseeRespuesta piseeRespuesta;
	
	public DataPiseeToQueue(ConfiguracionServicio newConfiguracionServicio, PiseeRespuesta newPiseeRespuesta){
		configuracionServicio = newConfiguracionServicio;
		piseeRespuesta = newPiseeRespuesta;
	}
	
	public ConfiguracionServicio getConfiguracionServicio() {
		return configuracionServicio;
	}
	public PiseeRespuesta getPiseeRespuesta() {
		return piseeRespuesta;
	}
	
	public void setConfiguracionServicio(ConfiguracionServicio configuracionServicio) {
		this.configuracionServicio = configuracionServicio;
	}
	public void setPiseeRespuesta(PiseeRespuesta respuesta) {
		this.piseeRespuesta = respuesta;
	}
	
}
