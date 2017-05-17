package cl.gob.minsegpres.pisee.rest.entities.response;

import java.util.Calendar;

public class PiseeTemporalData {

	//--
	private long duracionLlamadaServicio;
	private Calendar fechaConsultaRecibida;
	
	//--
	public long getDuracionLlamadaServicio() {
		return duracionLlamadaServicio;
	}
	public Calendar getFechaConsultaRecibida() {
		return fechaConsultaRecibida;
	}
	
	//--
	public void setDuracionLlamadaServicio(long duracionLlamadaServicio) {
		this.duracionLlamadaServicio = duracionLlamadaServicio;
	}
	public void setFechaConsultaRecibida(Calendar fechaConsultaRecibida) {
		this.fechaConsultaRecibida = fechaConsultaRecibida;
	}
	
}
