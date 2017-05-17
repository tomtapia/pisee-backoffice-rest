package cl.gob.minsegpres.pisee.rest.entities.sii;

import org.w3c.dom.Document;

public class Seed {
	private String status;
	private String value;
	private Document source;

	public Seed(Document doc) {
		setup(doc);
	}

	public Seed(String status, String value) {
		this.status = status;
		this.value = value;
	}

	private void setup(Document doc) {
		this.source = doc;
		this.status = this.source.getElementsByTagName("ESTADO").item(0).getTextContent();
		this.value = this.source.getElementsByTagName("SEMILLA").item(0).getTextContent();
	}

	public String getStatus() {
		return this.status;
	}

	public String getValue() {
		return this.value;
	}
}
