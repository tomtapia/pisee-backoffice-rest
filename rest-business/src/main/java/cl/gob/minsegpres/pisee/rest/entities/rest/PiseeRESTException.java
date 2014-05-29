package cl.gob.minsegpres.pisee.rest.entities.rest;

public class PiseeRESTException extends Throwable{

	private static final long serialVersionUID = 1L;
	private String estado;
	private String glosa;
	
	public PiseeRESTException(String nEstado, String nGlosa){
		estado = nEstado;
		glosa = nGlosa; 
	}
	
	public String getEstado() {
		return estado;
	}
	public String getGlosa() {
		return glosa;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}
	
}
