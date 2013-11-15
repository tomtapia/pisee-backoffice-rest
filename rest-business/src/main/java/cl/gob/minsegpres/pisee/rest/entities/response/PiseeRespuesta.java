package cl.gob.minsegpres.pisee.rest.entities.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PiseeRespuesta implements Serializable{

	private static final long serialVersionUID = 1L;
	private PiseeEncabezado encabezado;
	private List<Map<String,String>> metadata;

	public PiseeRespuesta(){
		encabezado = new PiseeEncabezado();
	}
	
	public PiseeEncabezado getEncabezado() {
		return encabezado;
	}
	public List<Map<String, String>> getMetadata() {
		return metadata;
	}
	
	public void setEncabezado(PiseeEncabezado encabezado) {
		this.encabezado = encabezado;
	}
	public void setMetadata(List<Map<String, String>> metadata) {
		this.metadata = metadata;
	}	

}
