package cl.gob.minsegpres.pisee.rest.entities.security;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

abstract class CertificateInfo implements Certificate {
	private X509Certificate certificate;
	private KeyPair keyPair;
	
	protected CertificateInfo(X509Certificate certificate, KeyPair keyPair)
	{
		this.certificate = certificate;
		this.keyPair = keyPair;
	}
	
	@Override
	public X509Certificate getCertificate() {
		return this.certificate;
	}

	@Override
	public KeyPair getKeyPair() {
		return this.keyPair;
	}
}
