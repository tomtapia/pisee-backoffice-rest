package cl.gob.minsegpres.pisee.rest.api.model;

import java.util.List;

public class ApiRequest {

	private String version;
	private String path;
	private String method;
	private String exampleURI;
	private List<ApiParameter> parameters;
	
	public String getPath() {
		return path;
	}
	public String getVersion() {
		return version;
	}	
	public String getMethod() {
		return method;
	}
	public String getExampleURI() {
		return exampleURI;
	}
	public List<ApiParameter> getParameters() {
		return parameters;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}	
	public void setPath(String path) {
		this.path = path;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public void setExampleURI(String exampleURI) {
		this.exampleURI = exampleURI;
	}
	public void setParameters(List<ApiParameter> parameters) {
		this.parameters = parameters;
	}
	
}
