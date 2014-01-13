package cl.gob.minsegpres.pisee.rest.connector;

import java.io.FileInputStream;
import java.io.StringWriter;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SrceiConnector {

	public void prepareInput() throws Exception {

		String keystoreType = "PKCS12";
		String keystoreFile = "E://USR//certificadoDigital.p12";
		String keystorePass = "genchi2013";
		String privateKeyAlias = "id e-sign s.a. de ricardo david nesvara herrera";
		String privateKeyPass = "genchi2013";
		String entradaInformacion = "E://USR//entradaInformacion.xml";

		// System.out.println("Creating the SOAP message...");
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		SOAPHeader soapHeader = soapEnvelope.getHeader();
		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.addAttribute(soapEnvelope.createName("id", "soapenv", "http://schemas.xmlsoap.org/soap/security/2000-12"), "Body");
		DocumentBuilderFactory dbfc = DocumentBuilderFactory.newInstance();
		dbfc.setNamespaceAware(true);
		Document docm = dbfc.newDocumentBuilder().parse(new FileInputStream(entradaInformacion));
		soapBody.addDocument(docm);

		System.out.println("soapBody.getTagName() == " + soapBody.getTagName());
		System.out.println("soapBody.getTextContent() == " + soapBody.getTextContent());

		
		
		
		
		Source source = soapPart.getContent();
		org.w3c.dom.Node root = null;
		root = ((DOMSource) source).getNode();

		/*
		 * Source source = soapPart.getContent(); org.w3c.dom.Node root = null;
		 * if (source instanceof DOMSource) { root = ((DOMSource)
		 * source).getNode(); } else if (source instanceof SAXSource) {
		 * InputSource inSource = ((SAXSource) source).getInputSource();
		 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 * dbf.setNamespaceAware(true); DocumentBuilder db = null; synchronized
		 * (dbf) { db = dbf.newDocumentBuilder(); } Document doc =
		 * db.parse(inSource); root = (org.w3c.dom.Node)
		 * doc.getDocumentElement(); } else {
		 * System.err.println("error: cannot convert SOAP message (" +
		 * source.getClass().getName() + ") into a W3C DOM tree");
		 * System.exit(-1); }
		 */

		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(root), new StreamResult(writer));

		String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
		
		System.out.println("providerName = " + providerName);
		
		/*
		09:31:34,009 INFO  [STDOUT] providerName = org.jcp.xml.dsig.internal.dom.XMLDSigRI
		09:31:34,035 INFO  [STDOUT] 09:31:34,035 ERROR [RestConnector] 
		Exception == java.lang.ClassCastException: 
		org.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory 
		cannot be cast to 
		javax.xml.crypto.dsig.XMLSignatureFactory
		*/
		
		
		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
		Reference ref = sigFactory.newReference("#Body", sigFactory.newDigestMethod(DigestMethod.SHA1, null));
		SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null),
				sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
		KeyStore keyStore = KeyStore.getInstance(keystoreType);
		keyStore.load(new FileInputStream(keystoreFile), keystorePass.toCharArray());
		KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(privateKeyAlias, new KeyStore.PasswordProtection(privateKeyPass.toCharArray()));
		X509Certificate cert = (X509Certificate) entry.getCertificate();
		List<X509Certificate> x509 = new ArrayList<X509Certificate>();
		x509.add(cert);
		X509Data x509Data = keyInfoFactory.newX509Data(x509);
		List<X509Data> items = new ArrayList<X509Data>();
		items.add(x509Data);
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(items);
		XMLSignature sig = sigFactory.newXMLSignature(signedInfo, keyInfo);

		// Insert XML signature into DOM tree and sign

		// System.out.println("Signing the SOAP message...");
		Element envelope = getFirstChildElement(root);

		System.out.println("envelope.getTagName() == " + envelope.getTagName());
		System.out.println("envelope.getTextContent() == " + envelope.getTextContent());

		Element header = getFirstChildElement(envelope);

		System.out.println("header.getTagName() == " + header.getTagName());
		System.out.println("header.getTextContent() == " + header.getTextContent());

		DOMSignContext sigContext = new DOMSignContext(entry.getPrivateKey(), header);

		sigContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");
		sigContext.setIdAttributeNS(getNextSiblingElement(header), "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
		sig.sign(sigContext);

		// ---
		StringWriter writer2 = new StringWriter();
		Transformer transformer2 = TransformerFactory.newInstance().newTransformer();
		transformer2.transform(new DOMSource(root), new StreamResult(writer2));
		String xml2 = writer2.toString();
		System.out.println(xml2);

		// Element sigElement = getFirstChildElement(header);
		// DOMValidateContext valContext = new
		// DOMValidateContext(cert.getPublicKey(), sigElement);
		// valContext.setIdAttributeNS(getNextSiblingElement(header),
		// "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
		// boolean isValid = sig.validate(valContext);
		// System.out.println("Validating the signature... " + (isValid ?
		// "valid" : "invalid"));
	}

	private static void dumpDOMDocument(org.w3c.dom.Node root) throws TransformerException, TransformerConfigurationException {
		System.out.println("\n");
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(root), new StreamResult(System.out));
		System.out.println("\n");
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
