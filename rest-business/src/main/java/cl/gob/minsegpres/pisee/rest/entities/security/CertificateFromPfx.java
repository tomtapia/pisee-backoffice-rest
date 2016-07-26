package cl.gob.minsegpres.pisee.rest.entities.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CertificateFromPfx extends CertificateInfo {

	private CertificateFromPfx(X509Certificate certificate, KeyPair keyPair) {
		super(certificate, keyPair);
	}
	
	public static Certificate newInstance(String filePath, char[] password) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		FileInputStream fis = new FileInputStream(filePath);

		ks.load(fis, password);
		Enumeration<String> aliasesList = ks.aliases();

		X509Certificate certificate = null;
		KeyPair keyPair = null;
		
		while (aliasesList.hasMoreElements()) {
			String alias = (String) aliasesList.nextElement();
			certificate = (X509Certificate) ks.getCertificate(alias);
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password);
			PublicKey publicKey = certificate.getPublicKey();
			keyPair = new KeyPair(publicKey, privateKey);
			break;
		}
		
		return new CertificateFromPfx(certificate, keyPair);
	}
}
