package cl.gob.minsegpres.pisee.rest.connector;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.EncryptionConstants;
import org.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import cl.gob.minsegpres.pisee.rest.entities.security.Certificate;
import cl.gob.minsegpres.pisee.rest.entities.security.CertificateFromPfx;
import cl.gob.minsegpres.pisee.rest.entities.soap.KeyStoreParameterSOAP;

public class SrceiRestToSoapConnector {
	
	private Certificate certificate;
	
	public SrceiRestToSoapConnector() {
	}

	public String firmarEntrada(InputStream xmlInput, KeyStoreParameterSOAP keyStoreParameter) {
		try {
			certificate = CertificateFromPfx.newInstance(keyStoreParameter.getKeystoreFile(), keyStoreParameter.getKeystorePass().toCharArray());
			DocumentBuilderFactory dbfc = DocumentBuilderFactory.newInstance();
			dbfc.setNamespaceAware(true);
			Document docm = dbfc.newDocumentBuilder().parse(xmlInput);		

			XPath xpath = XPathFactory.newInstance().newXPath();
			XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM", new XMLDSigRI());
			DigestMethod digestMethod = factory.newDigestMethod(DigestMethod.SHA1, null);

			Map<String, String> soapNamespace = new HashMap<String, String>();
			soapNamespace.put("soap", "http://schemas.xmlsoap.org/soap/envelope/");

			Transform trans1 = factory.newTransform(Transforms.TRANSFORM_C14N_OMIT_COMMENTS, (TransformParameterSpec) null);
			Transform trans2 = factory.newTransform(Transform.XPATH, new XPathFilterParameterSpec("ancestor-or-self::soap:Body", soapNamespace));

			List<Transform> transforms = new ArrayList<Transform>();
			transforms.add(trans2);
			transforms.add(trans1);

			Reference reference = factory.newReference("", digestMethod, transforms, null, null);
			CanonicalizationMethod canonicalizationMethod = factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);
			SignatureMethod signatureMethod = factory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
			SignedInfo signedInfo = factory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(reference));

			KeyInfoFactory keyInfoFactory = factory.getKeyInfoFactory();
			X509Certificate cert = this.certificate.getCertificate();
			List<X509Certificate> x509 = new ArrayList<X509Certificate>();

			x509.add(cert);
			javax.xml.crypto.dsig.keyinfo.X509Data x509Data = keyInfoFactory.newX509Data(x509);
			List<javax.xml.crypto.dsig.keyinfo.X509Data> items = new ArrayList<javax.xml.crypto.dsig.keyinfo.X509Data>();

			items.add(x509Data);
			KeyInfo keyInfo = keyInfoFactory.newKeyInfo(items);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			String xmlString = getStringFromDocument(docm);
			
			Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
			Node soapHeader = (Node) xpath.evaluate("/*/*[local-name()='Header']", doc, XPathConstants.NODE);

			DOMSignContext dsc = new DOMSignContext(this.certificate.getKeyPair().getPrivate(), soapHeader);

			XMLSignature signature = factory.newXMLSignature(signedInfo, keyInfo);
			signature.sign(dsc);

			return getStringFromDocument(doc);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public String descrifarRespuesta(Document respuesta, KeyStoreParameterSOAP keyStoreParameter) throws Exception {
		certificate = CertificateFromPfx.newInstance(keyStoreParameter.getKeystoreFile(), keyStoreParameter.getKeystorePass().toCharArray());

		String namespaceURI = EncryptionConstants.EncryptionSpecNS;
		String localName = EncryptionConstants._TAG_ENCRYPTEDDATA;
		Element encryptedDataElement = (Element) respuesta.getElementsByTagNameNS(namespaceURI, localName).item(0);
		XMLCipher xmlCipher = XMLCipher.getInstance();

		if (null != encryptedDataElement){
			xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
			xmlCipher.setKEK(certificate.getKeyPair().getPrivate());
			xmlCipher.doFinal(respuesta, encryptedDataElement);			
		}
		
		return getStringFromDocument(respuesta);
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

}
