package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

class TokenSignedMessage implements Message {
	private SecurityOperations securityOperations;
	private String soapMessageFormat;
	private Message tokenMessage;

	private TokenSignedMessage(Seed seed, SecurityOperations securityOperations) {
		this.tokenMessage = TokenMessage.newInstance(seed);
		this.securityOperations = securityOperations;
		this.soapMessageFormat = getSoapMessageFormat();
	}

	public static Message newInstance(Seed seed, SecurityOperations securityOperations) {
		return new TokenSignedMessage(seed, securityOperations);
	}

	private String getSoapMessageFormat() {
		StringBuilder smf = new StringBuilder();
		smf.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:def=\"http://DefaultNamespace\">");
		smf.append("<soapenv:Header/>");
		smf.append("<soapenv:Body>");
		smf.append("<def:getToken soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
		smf.append("<pszXml xsi:type=\"xsd:string\"><![CDATA[%s]]></pszXml>");
		smf.append("</def:getToken>");
		smf.append("</soapenv:Body>");
		smf.append("</soapenv:Envelope>");

		return smf.toString();
	}

	@Override
	public Document getMessage() {
		String signedXml = XmlHelpers.xmlToString(this.tokenMessage.getMessage());
		return XmlHelpers.stringToXml(String.format(this.soapMessageFormat, signedXml));
	}

	@Override
	public boolean isSigned() {
		return this.tokenMessage.isSigned();
	}

	@Override
	public boolean hasMessage() {
		return this.tokenMessage.hasMessage();
	}

	@Override
	public Document getSignedMessage() throws OperationNotSupportedException {
		Document doc = this.securityOperations.sign(this.tokenMessage);
		String signedXml = XmlHelpers.xmlToString(doc);
		return XmlHelpers.stringToXml(String.format(this.soapMessageFormat, signedXml));
	}
}
