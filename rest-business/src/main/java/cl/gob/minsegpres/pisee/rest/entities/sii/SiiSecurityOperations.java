package cl.gob.minsegpres.pisee.rest.entities.sii;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.XMLStructure;
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
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import org.w3c.dom.Document;

import cl.gob.minsegpres.pisee.rest.entities.security.Certificate;

class SiiSecurityOperations implements SecurityOperations {

	private Certificate certificate;

	private SiiSecurityOperations(Certificate certificate) {
		this.certificate = certificate;
	}

	public static SecurityOperations newInstance(Certificate certificate) {
		return new SiiSecurityOperations(certificate);
	}

	@Override
	public Document sign(Message message) {
		if (!message.isSigned()){
			return null;
		}
		Document doc = message.getMessage();
		XMLSignatureFactory xmlSigFactory = XMLSignatureFactory.getInstance("DOM");
		DOMSignContext domSignCtx = new DOMSignContext(this.certificate.getKeyPair().getPrivate(), doc.getDocumentElement());
		Reference ref = null;
		SignedInfo signedInfo = null;
		try {
			ref = xmlSigFactory.newReference("", xmlSigFactory.newDigestMethod(DigestMethod.SHA1, null),
					Collections.singletonList(xmlSigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null, null);
			signedInfo = xmlSigFactory.newSignedInfo(xmlSigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
					xmlSigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (InvalidAlgorithmParameterException ex) {
			ex.printStackTrace();
		}
		KeyInfoFactory keyInfoFactory = xmlSigFactory.getKeyInfoFactory();
		List<X509Certificate> x509 = new ArrayList<X509Certificate>();
		x509.add(this.certificate.getCertificate());
		javax.xml.crypto.dsig.keyinfo.X509Data x509Data = keyInfoFactory.newX509Data(x509);
		List<XMLStructure> items = new ArrayList<XMLStructure>();
		KeyValue kv = null;
		try {
			kv = keyInfoFactory.newKeyValue(this.certificate.getCertificate().getPublicKey());
		} catch (KeyException e1) {
			e1.printStackTrace();
		}
		items.add(kv);
		items.add(x509Data);
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(items);
		XMLSignature xmlSignature = xmlSigFactory.newXMLSignature(signedInfo, keyInfo);
		try {
			xmlSignature.sign(domSignCtx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	@Override
	public void decrypt(Document doc) {
	}
}
