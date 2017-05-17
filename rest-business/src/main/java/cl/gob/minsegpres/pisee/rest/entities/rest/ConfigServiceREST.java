package cl.gob.minsegpres.pisee.rest.entities.rest;

import java.util.List;

public class ConfigServiceREST {

	//-- attribs
	private String id;
	private String name;
	private String description;
	private String method;
	private String baseEndpoint;
	private String extendEndpoint;
	private List<ParameterServiceREST> pathParameters;
	private List<ParameterServiceREST> queryParameters;
	
	//-- getters
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}	
	public String getMethod() {
		return method;
	}
	public String getBaseEndpoint() {
		return baseEndpoint;
	}
	public String getExtendEndpoint() {
		return extendEndpoint;
	}	
	public List<ParameterServiceREST> getPathParameters() {
		return pathParameters;
	}
	public List<ParameterServiceREST> getQueryParameters() {
		return queryParameters;
	}
	
	//-- setters
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	public void setMethod(String method) {
		this.method = method;
	}
	public void setBaseEndpoint(String endpoint) {
		this.baseEndpoint = endpoint;
	}
	public void setExtendEndpoint(String extendEndpoint) {
		this.extendEndpoint = extendEndpoint;
	}	
	public void setPathParameters(List<ParameterServiceREST> pathParameters) {
		this.pathParameters = pathParameters;
	}
	public void setQueryParameters(List<ParameterServiceREST> queryParameters) {
		this.queryParameters = queryParameters;
	}
	
}