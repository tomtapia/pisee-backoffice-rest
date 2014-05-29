package cl.gob.minsegpres.pisee.rest.entities;

import java.util.Calendar;
import java.util.HashMap;

import cl.gob.minsegpres.pisee.rest.util.ParametersName;

public class InputParameter {

	//-- attribs
	private HashMap<String, Object> headerParameters;
	private HashMap<String, Object> bodyParameters;
	
	//-- getters
	public HashMap<String, Object> getHeaderParameters() {
		return headerParameters;
	}
	public HashMap<String, Object> getBodyParameters() {
		return bodyParameters;
	}
	
	//-- setters
	public void setHeaderParameters(HashMap<String, Object> headerMapParameters) {
		this.headerParameters = headerMapParameters;
	}
	public void setBodyParameters(HashMap<String, Object> bodyMapParameters) {
		this.bodyParameters = bodyMapParameters;
	}
	
	//-- constructor
	public InputParameter(){
		addHeaderParameter(ParametersName.FECHA_CONSULTA_RECIBIDA, Calendar.getInstance());
	}
	
	//-- adds
	public void addHeaderParameter(String name, Object parameter){
		if(null == headerParameters){
			headerParameters = new HashMap<String, Object>();
		}
		headerParameters.put(name, parameter);
	}
	public void addBodyParameter(String name, Object parameter){
		if(null == bodyParameters){
			bodyParameters = new HashMap<String, Object>();
		}
		bodyParameters.put(name, parameter);
	}
	
	//-- gets
	public Object getHeaderParameter(String name){
		if(null != headerParameters){
			return headerParameters.get(name);
		}
		return null;
	}
	public Object getBodyParameter(String name){
		if(null != bodyParameters){
			return bodyParameters.get(name);
		}
		return null;
	}	
}
