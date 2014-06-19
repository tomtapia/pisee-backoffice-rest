package cl.gob.minsegpres.pisee.rest.api.model;

import java.io.Serializable;

public class ApiLink implements Serializable{
	
	private static final long serialVersionUID = -4527766321186006739L;
	private String id;
	private String name;
	private String value;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
