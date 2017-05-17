package cl.gob.minsegpres.pisee.rest.api.model;

import java.io.Serializable;
import java.util.List;

public class ApiService implements Serializable{
	
	private static final long serialVersionUID = -4610409335245799312L;
	
	private String id;
	private String name;
	private String description;
	private String path;
	private List<ApiMethod> methods;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}	
	public String getPath() {
		return path;
	}
	public List<ApiMethod> getMethods() {
		return methods;
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
	public void setPath(String path) {
		this.path = path;
	}
	public void setMethods(List<ApiMethod> methods) {
		this.methods = methods;
	}
	
}
