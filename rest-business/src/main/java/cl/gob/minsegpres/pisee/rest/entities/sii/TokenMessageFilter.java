package cl.gob.minsegpres.pisee.rest.entities.sii;

import org.w3c.dom.Document;

public class TokenMessageFilter implements Filter {

	@Override
	public Document apply(Document document) {
		return XmlHelpers.stringToXml(XmlHelpers.getTextContent(document, "http://DefaultNamespace", "getTokenReturn"));
	}
}