package cl.gob.minsegpres.pisee.rest.entities.rest;

public class ParameterServiceREST {
	
	//-- attribs
	private boolean optional;
	private String name;

	//-- getters
	public boolean isOptional() {
		return optional;
	}
	public String getName() {
		return name;
	}
	
	//-- setters
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
