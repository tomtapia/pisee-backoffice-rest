package cl.gob.minsegpres.pisee.rest.entities.sii;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlHelpers {

//	private static String dateTimeFormat = "yyyy-MM-dd'T'hh:mm:ss.S";

	public static String xmlToString(Document xmlDocument) {
		try {
			DOMSource domSource = new DOMSource(xmlDocument);

			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static Document toXml(InputSource inputSource) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputSource);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document stringToXml(String xmlString) {
		return toXml(new InputSource(new StringReader(xmlString)));
	}

	public static Document byteArrayToXml(ByteArrayOutputStream byteArrayOutputStream) {
		return toXml(new InputSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
	}

//	private static final Serializer defaultSerializer() {
//		DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
//		RegistryMatcher matcher = new RegistryMatcher();
//		matcher.bind(Date.class, new DateFormatTransformer(dateFormat));
//		matcher.bind(Documento.class, new XmlDocumentTransformer());
//		Serializer serializer = new Persister(matcher);
//		return serializer;
//	}
//
//	public static String serialize(Sobre sobre) {
//		Serializer serializer = defaultSerializer();
//		StringWriter sw = new StringWriter();
//		try {
//			serializer.write(sobre, sw);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return StringEscapeUtils.unescapeXml(sw.toString());
//	}

	public static String getTextContent(Document doc, String namespace, String node) {
//		System.out.println("getTextContent - node = " + node + " doc = "+ xmlToString(doc) );
//		System.out.println("-------------------------------------------------------------------------------");
//		System.out.println("-------------------------------------------------------------------------------");
//		System.out.println("-------------------------------------------------------------------------------");
		
		return doc.getElementsByTagNameNS(namespace, node).item(0).getTextContent();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
