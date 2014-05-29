package cl.gob.minsegpres.pisee.rest.entities.soap;

import java.util.List;

public class OutputParameterSOAP {
	
	//-- attribs
	private String serviceName;
	private String rutaArreglo;
	private List<CampoOutputParameterSOAP> campos;
	
	//-- getters
	public String getServiceName() {
		return serviceName;
	}
	public List<CampoOutputParameterSOAP> getCampos() {
		return campos;
	}
	public String getRutaArreglo() {
		return rutaArreglo;
	}
	
	//-- setters
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public void setCampos(List<CampoOutputParameterSOAP> campos) {
		this.campos = campos;
	}
	public void setRutaArreglo(String rutaArreglo) {
		this.rutaArreglo = rutaArreglo;
	}
	
}
