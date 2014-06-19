package cl.gob.minsegpres.pisee.rest.api.model;

import java.io.Serializable;

public class ApiParameter implements Serializable{

	private static final long serialVersionUID = 8687153362480983094L;
	
	private String id;
	private String name;
	private String description;
	private boolean required;
	private String type;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public boolean isRequired() {
		return required;
	}
	public String getType() {
		return type;
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
	public void setRequired(boolean required) {
		this.required = required;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
