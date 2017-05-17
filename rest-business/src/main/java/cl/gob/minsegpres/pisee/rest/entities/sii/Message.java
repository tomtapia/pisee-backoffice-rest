package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

public interface Message {
	
	boolean isSigned();
	
	boolean hasMessage();
	
	Document getMessage();
	
	Document getSignedMessage() throws OperationNotSupportedException;
}
