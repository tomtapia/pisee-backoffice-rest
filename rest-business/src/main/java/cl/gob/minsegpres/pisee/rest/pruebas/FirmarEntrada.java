package cl.gob.minsegpres.pisee.rest.pruebas;

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
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
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
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * Construct a SOAP message, sign it and then validate the signature. This
 * implementation follows the <a ref="http://www.w3.org/TR/SOAP-dsig/"> W3C Note
 * on digital signatures in SOAP messages </a>. The validating key is included
 * in the signature. DOM Level 2 is used throughout.
 * <p>
 * The following SOAP message is signed:
 * 
 * <pre>
 * <code>
 * 
 *     <?xml version="1.0" encoding="UTF-8"?>
 *     <soap-env:Envelope 
 *      xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">
 *       <soap-env:Header>
 *         <SOAP-SEC:Signature 
 *          mustUnderstand="1" 
 *          xmlns:SOAP-SEC="http://schemas.xmlsoap.org/soap/security/2000-12"/>
 *       </soap-env:Header>
 *       <soap-env:Body id="Body">
 *         <m:GetLastTradePrice xmlns:m="http://wombats.ztrade.com">
 *           <symbol>SUNW</symbol>
 *         </m:GetLastTradePrice>
 *       </soap-env:Body>
 *     </soap-env:Envelope>
 * 
 * </code>
 * </pre>
 */
public class FirmarEntrada {

	private static boolean debug = false;

	public static void main(String[] args) throws Exception {

		/*
		 * String keystoreType = "JKS"; String keystoreFile =
		 * "/Users/diegotorres/Documents/workspace-sts-3.0.0.RELEASE/ClienteSRCeI/src/keystore.jks"
		 * ; String keystorePass = "123456"; String privateKeyAlias = "1";
		 * String privateKeyPass = "1234"; String certificateAlias = "1";
		 */

		String keystoreType = "PKCS12";
		// public static String keystoreFile = "keystore.jks"; //archivo con mi
		// keystore, donde se dejo la llave privada del certificado
		// public static String respuesta = "respuesta.xml"; //archivo con la
		// respuesta encriptada que llega desde srcei
		// public static String descifrado = "datos.xml"; //archivo donde se
		// dejará la respuesta con los datos descifrados
		String keystoreFile = "E://USR//certificadoDigital.p12";
		String keystorePass = "genchi2013"; // password de mi keystore
		String privateKeyAlias = "id e-sign s.a. de ricardo david nesvara herrera"; // alias
																					// en
																					// el
																					// keystore
																					// de
																					// la
																					// llave
																					// privada
																					// correspondiente
																					// al
																					// certificado
		String privateKeyPass = "genchi2013"; // password de la llave privada
		String certificateAlias = "id e-sign s.a. de ricardo david nesvara herrera"; // alias
																						// en
																						// keystore
																						// del
																						// certificado
																						// (es
																						// lo
																						// mismo
																						// que
																						// privateKeyAlias)

//		int argc = args.length;
//
//		if (argc == 1) {
//			if (args[0].equalsIgnoreCase("-help")) {
//				System.out.println("Usage:  SignedSoap [-debug]");
//				System.out.println("    -debug\tactivates debug messages");
//				return;
//			}
//			debug = args[0].equalsIgnoreCase("-debug");
//		}

		// Create the SOAP message

		System.out.println("Creating the SOAP message...");
		
		//SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		
		
		MessageFactory mf12 = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
		SOAPMessage soapMessage = mf12.createMessage();
		
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		SOAPHeader soapHeader = soapEnvelope.getHeader();
		SOAPBody soapBody = soapEnvelope.getBody();
		soapBody.addAttribute(soapEnvelope.createName("id", "soapenv", "http://schemas.xmlsoap.org/soap/security/2000-12"), "Body");

		
		
		DocumentBuilderFactory dbfc = DocumentBuilderFactory.newInstance();
		dbfc.setNamespaceAware(true);
		Document docm = dbfc.newDocumentBuilder().parse(new FileInputStream("E://USR//entradaInformacion.xml"));
		soapBody.addDocument(docm);
		
		
		
		System.out.println("soapBody.getTagName() == " + soapBody.getTagName());
		System.out.println("soapBody.getTextContent() == " + soapBody.getTextContent());
		

		// Generate a DOM representation of the SOAP message

		System.out.println("Generating the DOM tree...");
		// Get input source
		Source source = soapPart.getContent();
		org.w3c.dom.Node root = null;

		if (source instanceof DOMSource) {
			root = ((DOMSource) source).getNode();
			System.out.println("DOM");

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
			System.out.println("SAX");

		} else {
			System.err.println("error: cannot convert SOAP message (" + source.getClass().getName() + ") into a W3C DOM tree");
			System.exit(-1);
		}

		if (debug) {
			dumpDOMDocument(root);
		}

		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(root), new StreamResult(writer));
		String xml = writer.toString();
		System.out.println(xml);

		// Generate a DSA key pair



		// Assemble the signature parts

		System.out.println("Preparing the signature...");
		String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
		
		System.out.println("providerName = " + providerName);
		
		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
		Reference ref = sigFactory.newReference("#Body", sigFactory.newDigestMethod(DigestMethod.SHA1, null));
		SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null),
				sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));



		KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();

		KeyStore keyStore = KeyStore.getInstance(keystoreType);
		keyStore.load(new FileInputStream(keystoreFile), keystorePass.toCharArray());
		KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(privateKeyAlias, new KeyStore.PasswordProtection(privateKeyPass.toCharArray()));

		X509Certificate cert = (X509Certificate) entry.getCertificate();
		List x509 = new ArrayList();

		x509.add(cert);
		X509Data x509Data = keyInfoFactory.newX509Data(x509);
		List items = new ArrayList();

		items.add(x509Data);
		// items.add(keyValue);
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(items);

		XMLSignature sig = sigFactory.newXMLSignature(signedInfo, keyInfo);

		
		
		System.out.println("root.getTextContent() == " + root.getTextContent());
		
		
		
		
		
		// Insert XML signature into DOM tree and sign

		System.out.println("Signing the SOAP message...");
		// Find where to insert signature
		Element envelope = getFirstChildElement(root);
		
		System.out.println("envelope.getTagName() == " + envelope.getTagName());
		System.out.println("envelope.getTextContent() == " + envelope.getTextContent());
		
		Element header = getFirstChildElement(envelope);
		
		System.out.println("header.getTagName() == " + header.getTagName());
		System.out.println("header.getTextContent() == " + header.getTextContent());		
		
		DOMSignContext sigContext = new DOMSignContext(entry.getPrivateKey(), header);

		// Need to distinguish the Signature element in DSIG (from that in SOAP)
		sigContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");
		// register Body ID attribute
		sigContext.setIdAttributeNS(getNextSiblingElement(header), "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
		sig.sign(sigContext);

		if (debug) {
			dumpDOMDocument(root);
		}

		StringWriter writer2 = new StringWriter();
		Transformer transformer2 = TransformerFactory.newInstance().newTransformer();
		transformer2.transform(new DOMSource(root), new StreamResult(writer2));
		String xml2 = writer2.toString();
		System.out.println(xml2);

		// Validate the XML signature

		// Locate the signature element
		Element sigElement = getFirstChildElement(header);
		// Validate the signature using the public key generated above
		DOMValidateContext valContext = new DOMValidateContext(cert.getPublicKey(), sigElement);
		// register Body ID attribute
		valContext.setIdAttributeNS(getNextSiblingElement(header), "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
		boolean isValid = sig.validate(valContext);
		System.out.println("Validating the signature... " + (isValid ? "valid" : "invalid"));

		
	}

	/*
	 * Outputs DOM representation to the standard output stream.
	 * 
	 * @param root The DOM representation to be outputted
	 */
	private static void dumpDOMDocument(org.w3c.dom.Node root) throws TransformerException, TransformerConfigurationException {

		System.out.println("\n");
		// Create a new transformer object
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// Dump the DOM representation to standard output
		transformer.transform(new DOMSource(root), new StreamResult(System.out));
		System.out.println("\n");
	}

	/**
	 * Returns the first child element of the specified node, or null if there
	 * is no such element.
	 * 
	 * @param node
	 *            the node
	 * @return the first child element of the specified node, or null if there
	 *         is no such element
	 * @throws NullPointerException
	 *             if <code>node == null</code>
	 */
	private static Element getFirstChildElement(org.w3c.dom.Node node) {
		org.w3c.dom.Node child = node.getFirstChild();
		while (child != null && child.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
			child = child.getNextSibling();
		}
		return (Element) child;
	}

	/**
	 * Returns the next sibling element of the specified node, or null if there
	 * is no such element.
	 * 
	 * @param node
	 *            the node
	 * @return the next sibling element of the specified node, or null if there
	 *         is no such element
	 * @throws NullPointerException
	 *             if <code>node == null</code>
	 */
	public static Element getNextSiblingElement(org.w3c.dom.Node node) {
		org.w3c.dom.Node sibling = node.getNextSibling();
		while (sibling != null && sibling.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
			sibling = sibling.getNextSibling();
		}
		return (Element) sibling;
	}
}