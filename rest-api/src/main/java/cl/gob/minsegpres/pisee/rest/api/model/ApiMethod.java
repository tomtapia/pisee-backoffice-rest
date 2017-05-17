package cl.gob.minsegpres.pisee.rest.api.model;

import java.io.Serializable;

public class ApiMethod implements Serializable{

	private static final long serialVersionUID = -2039169742612097441L;
	
	private String id;
	private String name;
	private String description;
	private ApiRequest request;
	private ApiResponse response;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public ApiRequest getRequest() {
		return request;
	}
	public ApiResponse getResponse() {
		return response;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setRequest(ApiRequest request) {
		this.request = request;
	}
	public void setResponse(ApiResponse response) {
		this.response = response;
	}
	
}
