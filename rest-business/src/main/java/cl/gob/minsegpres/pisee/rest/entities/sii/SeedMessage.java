package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

class SeedMessage implements Message {	
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
		StringBuilder sb = new StringBuilder();
		sb.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:def=\"http://DefaultNamespace\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<def:getSeed soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"/>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		return XmlHelpers.stringToXml(sb.toString());
	}
	
	private SeedMessage(){ }
	
	public static Message newInstance()
	{
		return new SeedMessage();
	}

	@Override
	public Document getSignedMessage() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}
}
