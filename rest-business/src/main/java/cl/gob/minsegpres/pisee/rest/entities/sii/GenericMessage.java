package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

public class GenericMessage implements Message {

	private Document message;

	private GenericMessage(Document message) {
		this.message = message;
	}

	public static Message newInstance(Document message) {
		return new GenericMessage(message);
	}

	@Override
	public boolean isSigned() {
		return false;
	}

	@Override
	public boolean hasMessage() {
		return true;
	}

	@Override
	public Document getMessage() {
		return this.message;
	}

	@Override
	public Document getSignedMessage() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}
}
