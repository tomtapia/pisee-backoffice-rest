package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

public class SimplePisseMessage implements Message {
	
	private String soapMessageFormat = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:val=\"http://valida.aem.gob.cl\"><soapenv:Header /><soapenv:Body>%s</soapenv:Body></soapenv:Envelope>";
	
//	private Sobre sobre;
//
//	protected SimplePisseMessage(Sobre sobre) {
//		this.sobre = sobre;
//	}

	protected String getSoapMessageFormat() {
		return this.soapMessageFormat;
	}

//	public static Message newInstance(Sobre sobre) {
//		return new SimplePisseMessage(sobre);
//	}

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
		return null;
		
		//return XmlHelpers.stringToXml(String.format(getSoapMessageFormat(), XmlHelpers.serialize(sobre)));
	}

	@Override
	public Document getSignedMessage() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}
}
