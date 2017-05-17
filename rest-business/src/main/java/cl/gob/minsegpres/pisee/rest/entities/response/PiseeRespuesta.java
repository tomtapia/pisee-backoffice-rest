package cl.gob.minsegpres.pisee.rest.entities.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PiseeRespuesta implements Serializable{

	//-- attrib
	private static final long serialVersionUID = 855439714978536370L;
	
	private PiseeEncabezado encabezado;
	
	private List<Map<String,String>> metadata;
	
	private Map<String,String> cuerpo;
	
	private String responseRest = null;
	
	private PiseeTemporalData temporalData;
	
	//--constructor
	public PiseeRespuesta(){
		encabezado = new PiseeEncabezado();
	}
	
	//-- getters
	public PiseeEncabezado getEncabezado() {
		return encabezado;
	}
	public List<Map<String, String>> getMetadata() {
		return metadata;
	}
	public String getResponseRest() {
		return responseRest;
	}
	public Map<String, String> getCuerpo() {
		return cuerpo;
	}
	public PiseeTemporalData getTemporalData(){
		return temporalData;
	}
	
	//-- setters
	public void setEncabezado(PiseeEncabezado encabezado) {
		this.encabezado = encabezado;
	}
	public void setMetadata(List<Map<String, String>> metadata) {
		this.metadata = metadata;
	}
	public void setResponseRest(String responseRest) {
		this.responseRest = responseRest;
	}
	public void setCuerpo(Map<String, String> cuerpo) {
		this.cuerpo = cuerpo;
	}
	public void setTemporalData(PiseeTemporalData newVal){
		temporalData = newVal;
	}
	
}
