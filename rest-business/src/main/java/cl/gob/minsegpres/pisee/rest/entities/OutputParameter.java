package cl.gob.minsegpres.pisee.rest.entities;

import java.util.List;

public class OutputParameter {
	
	//-- attribs
	private String serviceName;
	private String rutaArreglo;
	private List<CampoOutputParameter> campos;
	
	//-- getters
	public String getServiceName() {
		return serviceName;
	}
	public List<CampoOutputParameter> getCampos() {
		return campos;
	}
	public String getRutaArreglo() {
		return rutaArreglo;
	}
	
	//-- setters
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public void setCampos(List<CampoOutputParameter> campos) {
		this.campos = campos;
	}
	public void setRutaArreglo(String rutaArreglo) {
		this.rutaArreglo = rutaArreglo;
	}
	
}
