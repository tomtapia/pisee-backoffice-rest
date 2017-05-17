package cl.gob.minsegpres.pisee.rest.api.model;

import java.io.Serializable;
import java.util.List;

public class ApiResponse implements Serializable{
	
	private static final long serialVersionUID = 3373213853391655367L;
	private String produces;
	private String example;
	private List<ApiParameter> parameters;
	
	public String getProduces() {
		return produces;
	}
	public String getExample() {
		return example;
	}
	public List<ApiParameter> getParameters() {
		return parameters;
	}
	
	public void setProduces(String produces) {
		this.produces = produces;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public void setParameters(List<ApiParameter> parameters) {
		this.parameters = parameters;
	}
	
}
