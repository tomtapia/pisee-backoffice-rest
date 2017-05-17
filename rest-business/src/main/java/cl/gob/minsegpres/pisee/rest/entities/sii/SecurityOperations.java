package cl.gob.minsegpres.pisee.rest.entities.sii;

import org.w3c.dom.Document;

public interface SecurityOperations {
	
	Document sign(Message message);
	
	void decrypt(Document doc);
}