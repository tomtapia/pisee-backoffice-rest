package cl.gob.minsegpres.pisee.rest.connector;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import cl.gob.minsegpres.pisee.rest.entities.soap.KeyStoreParameterSOAP;

public class SrceiRestToSoapConnector {

	public SrceiRestToSoapConnector() {
//		System.out.println("SrceiConnector - INIT");
		org.apache.xml.security.Init.init();
	}

	public String firmarEntrada(InputStream xmlInput, KeyStoreParameterSOAP keyStoreParameter) throws Exception {
		
        System.setProperty("javax.xml.soap.MessageFactory","com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl");
//        System.setProperty("javax.xml.soap.SOAPConnectionFactory","weblogic.wsee.saaj.SOAPConnectionFactoryImpl");		
		
/*
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.addAttribute(soapEnvelope.createName("id", "soapenv", "http://schemas.xmlsoap.org/soap/security/2000-12"), "Body");
		DocumentBuilderFactory dbfc = DocumentBuilderFactory.newInstance();
		dbfc.setNamespaceAware(true);
		Document docm = dbfc.newDocumentBuilder().parse(xmlInput);
		soapBody.addDocument(docm);
		System.out.println("firmarEntrada == soapBody.getTextContent() == " + soapBody.getTextContent());
		Source source = soapPart.getContent();
		org.w3c.dom.Node root = null;
		root = ((DOMSource) source).getNode();
*/
		// -------

		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		//SOAPHeader soapHeader = soapEnvelope.getHeader();
		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.addAttribute(soapEnvelope.createName("id", "soapenv", "http://schemas.xmlsoap.org/soap/security/2000-12"), "Body");
		DocumentBuilderFactory dbfc = DocumentBuilderFactory.newInstance();
		dbfc.setNamespaceAware(true);
		Document docm = dbfc.newDocumentBuilder().parse(xmlInput);
		soapBody.addDocument(docm);
//		System.out.println("firmarEntrada == soapBody.getTextContent() == " + soapBody.getTextContent());
		Source source = soapPart.getContent();
		org.w3c.dom.Node root = null;
		if (source instanceof DOMSource) {
			root = ((DOMSource) source).getNode();
//			System.out.println("DOM");
		} else if (source instanceof SAXSource) {
			InputSource inSource = ((SAXSource) source).getInputSource();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = null;
			synchronized (dbf) {
				db = dbf.newDocumentBuilder();
			}
			Document doc = db.parse(inSource);
			root = (org.w3c.dom.Node) doc.getDocumentElement();
//			System.out.println("SAX");
		} else {
			System.err.println("error: cannot convert SOAP message (" + source.getClass().getName() + ") into a W3C DOM tree");
			// System.exit(-1);
		}

//		System.out.println("firmarEntrada == root.getTextContent() == " + root.getTextContent());
		
//		XStream xxx = new XStream();
//		System.out.println("getFirstChild == " +  xxx.toXML(root.getFirstChild()));
//		System.out.println("getNextSibling == " +  xxx.toXML(root.getNextSibling()));
		
		
		

		String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");

		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
		Reference ref = sigFactory.newReference("#Body", sigFactory.newDigestMethod(DigestMethod.SHA1, null));
		SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null),
				sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
		KeyStore keyStore = KeyStore.getInstance(keyStoreParameter.getKeystoreType());
		keyStore.load(new FileInputStream(keyStoreParameter.getKeystoreFile()), keyStoreParameter.getKeystorePass().toCharArray());
		KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyStoreParameter.getPrivateKeyAlias(), new KeyStore.PasswordProtection(keyStoreParameter.getPrivateKeyPass()
				.toCharArray()));

		X509Certificate cert = (X509Certificate) entry.getCertificate();
		List<X509Certificate> x509 = new ArrayList<X509Certificate>();
		x509.add(cert);
		X509Data x509Data = keyInfoFactory.newX509Data(x509);
		List<X509Data> items = new ArrayList<X509Data>();
		items.add(x509Data);
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(items);
		XMLSignature sig = sigFactory.newXMLSignature(signedInfo, keyInfo);

		Element envelope = getFirstChildElement(root);
//		System.out.println("envelope.getTagName == " + envelope.getTagName());
//		System.out.println("envelope.getTextContent == " + envelope.getTextContent());

		Element header = getFirstChildElement(envelope);
//		System.out.println("header.getTagName == " + header.getTagName());
//		System.out.println("header.getTextContent == " + header.getTextContent());

		DOMSignContext sigContext = new DOMSignContext(entry.getPrivateKey(), header);
		sigContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");
		sigContext.setIdAttributeNS(getNextSiblingElement(header), "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
		sig.sign(sigContext);

		StringWriter writer2 = new StringWriter();
		Transformer transformer2 = TransformerFactory.newInstance().newTransformer();
		transformer2.transform(new DOMSource(root), new StreamResult(writer2));
		return writer2.toString();
	}

	public String descrifarRespuesta(Document respuesta, KeyStoreParameterSOAP keyStoreParameter) throws Exception {
		Element encryptedDataElement = (Element) respuesta.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);
		Key secretKey = loadDecryptionKey(respuesta, keyStoreParameter);
		XMLCipher xmlCipher = XMLCipher.getInstance();
		xmlCipher.init(XMLCipher.DECRYPT_MODE, secretKey);
		xmlCipher.doFinal(respuesta, encryptedDataElement);
		return getStringFromDocument(respuesta);
	}

	private Key loadDecryptionKey(Document document, KeyStoreParameterSOAP keyStoreParameter) throws Exception {
		Element e = (Element) document.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);
		XMLCipher cipher = XMLCipher.getInstance();
		cipher.init(XMLCipher.DECRYPT_MODE, null);
		EncryptedData encryptedData = cipher.loadEncryptedData(document, e);
		if (encryptedData == null) {
			throw new Exception("EncryptedData es null");
		} else if (encryptedData.getKeyInfo() == null) {
			throw new Exception("KeyInfo de EncryptedData es null");
		}

		KeyStore keyStore = KeyStore.getInstance(keyStoreParameter.getKeystoreType());
		keyStore.load(new FileInputStream(keyStoreParameter.getKeystoreFile()), keyStoreParameter.getKeystorePass().toCharArray());
		KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyStoreParameter.getPrivateKeyAlias(), new KeyStore.PasswordProtection(keyStoreParameter.getPrivateKeyPass()
				.toCharArray()));

		EncryptedKey ek = encryptedData.getKeyInfo().itemEncryptedKey(0);
		Key key = null;
		if (ek != null) {
			XMLCipher keyCipher = XMLCipher.getInstance();
			keyCipher.init(XMLCipher.UNWRAP_MODE, entry.getPrivateKey());
			key = keyCipher.decryptKey(ek, encryptedData.getEncryptionMethod().getAlgorithm());
		}
		return key;
	}

	public String getStringFromDocument(org.w3c.dom.Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
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

	private static Element getFirstChildElement(org.w3c.dom.Node node) {
		org.w3c.dom.Node child = node.getFirstChild();
		while (child != null && child.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
			child = child.getNextSibling();
		}
		return (Element) child;
	}

	public static Element getNextSiblingElement(org.w3c.dom.Node node) {
		org.w3c.dom.Node sibling = node.getNextSibling();
		while (sibling != null && sibling.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
			sibling = sibling.getNextSibling();
		}
		return (Element) sibling;
	}
}
